package com.feng.seckill.dao;

import com.feng.seckill.entity.SerialNumber;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SerialNumberMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table serial_number
     *
     * @mbg.generated Thu Jan 21 11:40:58 CST 2021
     */
    int deleteByPrimaryKey(String name);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table serial_number
     *
     * @mbg.generated Thu Jan 21 11:40:58 CST 2021
     */
    int insert(SerialNumber record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table serial_number
     *
     * @mbg.generated Thu Jan 21 11:40:58 CST 2021
     */
    SerialNumber selectByPrimaryKey(String name);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table serial_number
     *
     * @mbg.generated Thu Jan 21 11:40:58 CST 2021
     */
    List<SerialNumber> selectAll();

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table serial_number
     *
     * @mbg.generated Thu Jan 21 11:40:58 CST 2021
     */
    int updateByPrimaryKey(SerialNumber record);
}