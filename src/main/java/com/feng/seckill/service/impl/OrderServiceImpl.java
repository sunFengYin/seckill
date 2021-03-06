package com.feng.seckill.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.feng.seckill.entity.Item;
import com.feng.seckill.common.BusinessException;
import com.feng.seckill.common.ErrorCode;
import com.feng.seckill.common.Toolbox;
import com.feng.seckill.dao.OrderMapper;
import com.feng.seckill.dao.SerialNumberMapper;
import com.feng.seckill.entity.ItemStockLog;
import com.feng.seckill.entity.Order;
import com.feng.seckill.entity.SerialNumber;
import com.nowcoder.seckill.entity.*;
import com.feng.seckill.service.ItemService;
import com.feng.seckill.service.OrderService;
import com.feng.seckill.service.UserService;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

@Service
public class OrderServiceImpl implements OrderService, ErrorCode {

    private Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private SerialNumberMapper serialNumberMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    /**
     * ??????????????? + ??????
     * ?????????20210123000000000001
     *
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    private String generateOrderID() {
        StringBuilder sb = new StringBuilder();

        // ????????????
        sb.append(Toolbox.format(new Date(), "yyyyMMdd"));

        // ???????????????
        SerialNumber serial = serialNumberMapper.selectByPrimaryKey("order_serial");
        Integer value = serial.getValue();

        // ???????????????
        serial.setValue(value + serial.getStep());
        serialNumberMapper.updateByPrimaryKey(serial);

        // ???????????????
        String prefix = "000000000000".substring(value.toString().length());
        sb.append(prefix).append(value);

        return sb.toString();
    }

    @Transactional
    public Order createOrder(int userId, int itemId, int amount, Integer promotionId, String itemStockLogId) {
        // ????????????
        if (amount < 1 || (promotionId != null && promotionId.intValue() <= 0)) {
            throw new BusinessException(PARAMETER_ERROR, "???????????????????????????");
        }

        // ????????????
//        User user = userService.findUserById(userId);
//        User user = userService.findUserFromCache(userId);
//        if (user == null) {
//            throw new BusinessException(PARAMETER_ERROR, "???????????????????????????");
//        }

        // ????????????
//        Item item = itemService.findItemById(itemId);
        Item item = itemService.findItemInCache(itemId);
        if (item == null) {
            throw new BusinessException(PARAMETER_ERROR, "???????????????????????????");
        }

        // ????????????
//        int stock = item.getItemStock().getStock();
//        if (amount > stock) {
//            throw new BusinessException(STOCK_NOT_ENOUGH, "???????????????");
//        }

        // ????????????
//        if (promotionId != null) {
//            if (item.getPromotion() == null) {
//                throw new BusinessException(PARAMETER_ERROR, "???????????????????????????");
//            } else if (!item.getPromotion().getId().equals(promotionId)) {
//                throw new BusinessException(PARAMETER_ERROR, "???????????????????????????");
//            } else if (item.getPromotion().getStatus() != 0) {
//                throw new BusinessException(PARAMETER_ERROR, "???????????????????????????");
//            }
//        }

        // ????????????
        // ???????????????????????????????????????????????????????????????
        // ???????????????????????????????????????????????????????????????
//        boolean successful = itemService.decreaseStock(itemId, amount);
        boolean successful = itemService.decreaseStockInCache(itemId, amount);
        logger.debug("????????????????????? [" + successful + "]");
        if (!successful) {
            throw new BusinessException(STOCK_NOT_ENOUGH, "???????????????");
        }

        // ????????????
        Order order = new Order();
        order.setId(this.generateOrderID());
        order.setUserId(userId);
        order.setItemId(itemId);
        order.setPromotionId(promotionId);
        order.setOrderPrice(promotionId != null ? item.getPromotion().getPromotionPrice() : item.getPrice());
        order.setOrderAmount(amount);
        order.setOrderTotal(order.getOrderPrice().multiply(new BigDecimal(amount)));
        order.setOrderTime(new Timestamp(System.currentTimeMillis()));
        orderMapper.insert(order);
        logger.debug("?????????????????? [" + order.getId() + "]");

        // ????????????
//        itemService.increaseSales(itemId, amount);
//        logger.debug("?????????????????? [" + itemId + "]");
        JSONObject body = new JSONObject();
        body.put("itemId", itemId);
        body.put("amount", amount);
        Message msg = MessageBuilder.withPayload(body.toString()).build();
        rocketMQTemplate.asyncSend("seckill:increase_sales", msg, new SendCallback() {

            @Override
            public void onSuccess(SendResult sendResult) {
                logger.debug("????????????????????????????????????");
            }

            @Override
            public void onException(Throwable e) {
                logger.error("????????????????????????????????????", e);
            }
        }, 60 * 1000);

        // ????????????????????????
        itemService.updateItemStockLogStatus(itemStockLogId, 1);
        logger.debug("?????????????????? [" + itemStockLogId + "]");

        return order;
    }

    @Override
    public void createOrderAsync(int userId, int itemId, int amount, Integer promotionId) {
        // ????????????
        if (redisTemplate.hasKey("item:stock:over:" + itemId)) {
            throw new BusinessException(STOCK_NOT_ENOUGH, "???????????????");
        }

        // ??????????????????
        ItemStockLog itemStockLog = itemService.createItemStockLog(itemId, amount);
        logger.debug("???????????????????????? [" + itemStockLog.getId() + "]");

        // ?????????
        JSONObject body = new JSONObject();
        body.put("itemId", itemId);
        body.put("amount", amount);
        body.put("itemStockLogId", itemStockLog.getId());

        // ??????????????????
        JSONObject arg = new JSONObject();
        arg.put("userId", userId);
        arg.put("itemId", itemId);
        arg.put("amount", amount);
        arg.put("promotionId", promotionId);
        arg.put("itemStockLogId", itemStockLog.getId());

        String dest = "seckill:decrease_stock";
        Message msg = MessageBuilder.withPayload(body.toString()).build();
        try {
            logger.debug("?????????????????????????????? [" + body.toString() + "]");
            TransactionSendResult sendResult = rocketMQTemplate.sendMessageInTransaction(dest, msg, arg);
            if (sendResult.getLocalTransactionState() == LocalTransactionState.UNKNOW) {
                throw new BusinessException(UNDEFINED_ERROR, "?????????????????????");
            } else if (sendResult.getLocalTransactionState() == LocalTransactionState.ROLLBACK_MESSAGE) {
                throw new BusinessException(CREATE_ORDER_FAILURE, "?????????????????????");
            }
        } catch (MessagingException e) {
            throw new BusinessException(CREATE_ORDER_FAILURE, "?????????????????????");
        }
    }
}
