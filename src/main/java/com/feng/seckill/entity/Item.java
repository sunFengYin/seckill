package com.feng.seckill.entity;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class Item {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column item.id
     *
     * @mbg.generated Tue Jan 19 14:37:36 CST 2021
     */
    private Integer id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column item.title
     *
     * @mbg.generated Tue Jan 19 14:37:36 CST 2021
     */
    @NotBlank(message = "商品标题不能为空")
    private String title;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column item.price
     *
     * @mbg.generated Tue Jan 19 14:37:36 CST 2021
     */
    @NotNull(message = "商品价格不能为空")
    @Min(value = 0, message = "商品价格不能小于0")
    private BigDecimal price;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column item.sales
     *
     * @mbg.generated Tue Jan 19 14:37:36 CST 2021
     */
    private Integer sales;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column item.image_url
     *
     * @mbg.generated Tue Jan 19 14:37:36 CST 2021
     */
    @NotBlank(message = "商品主图不能为空")
    private String imageUrl;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column item.description
     *
     * @mbg.generated Tue Jan 19 14:37:36 CST 2021
     */
    @NotBlank(message = "商品描述不能为空")
    private String description;

    /**
     * 商品库存
     */
    @NotNull(message = "商品库存不能为空")
    private ItemStock itemStock;

    /**
     * 促销活动
     */
    private Promotion promotion;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column item.id
     *
     * @return the value of item.id
     * @mbg.generated Tue Jan 19 14:37:36 CST 2021
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column item.id
     *
     * @param id the value for item.id
     * @mbg.generated Tue Jan 19 14:37:36 CST 2021
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column item.title
     *
     * @return the value of item.title
     * @mbg.generated Tue Jan 19 14:37:36 CST 2021
     */
    public String getTitle() {
        return title;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column item.title
     *
     * @param title the value for item.title
     * @mbg.generated Tue Jan 19 14:37:36 CST 2021
     */
    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column item.price
     *
     * @return the value of item.price
     * @mbg.generated Tue Jan 19 14:37:36 CST 2021
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column item.price
     *
     * @param price the value for item.price
     * @mbg.generated Tue Jan 19 14:37:36 CST 2021
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column item.sales
     *
     * @return the value of item.sales
     * @mbg.generated Tue Jan 19 14:37:36 CST 2021
     */
    public Integer getSales() {
        return sales;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column item.sales
     *
     * @param sales the value for item.sales
     * @mbg.generated Tue Jan 19 14:37:36 CST 2021
     */
    public void setSales(Integer sales) {
        this.sales = sales;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column item.image_url
     *
     * @return the value of item.image_url
     * @mbg.generated Tue Jan 19 14:37:36 CST 2021
     */
    public String getImageUrl() {
        return imageUrl;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column item.image_url
     *
     * @param imageUrl the value for item.image_url
     * @mbg.generated Tue Jan 19 14:37:36 CST 2021
     */
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl == null ? null : imageUrl.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column item.description
     *
     * @return the value of item.description
     * @mbg.generated Tue Jan 19 14:37:36 CST 2021
     */
    public String getDescription() {
        return description;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column item.description
     *
     * @param description the value for item.description
     * @mbg.generated Tue Jan 19 14:37:36 CST 2021
     */
    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public ItemStock getItemStock() {
        return itemStock;
    }

    public void setItemStock(ItemStock itemStock) {
        this.itemStock = itemStock;
    }

    public Promotion getPromotion() {
        return promotion;
    }

    public void setPromotion(Promotion promotion) {
        this.promotion = promotion;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", price=" + price +
                ", sales=" + sales +
                ", imageUrl='" + imageUrl + '\'' +
                ", description='" + description + '\'' +
                ", itemStock=" + itemStock +
                ", promotion=" + promotion +
                '}';
    }
}