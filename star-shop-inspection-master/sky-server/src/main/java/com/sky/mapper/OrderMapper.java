package com.sky.mapper;

import com.sky.dto.GoodsSalesDTO;
import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface OrderMapper {

    void insert(Orders orders);


    @Select("select * from orders where status = #{status} and order_time < #{orderTime}")
    List<Orders> getByStatusAndOrderTime(Integer status, LocalDateTime orderTime);

    void update(Orders orders);

    @Select("select * from orders where id = #{outTradeNo}")
    Orders getByNumberAndUserId(String outTradeNo);

    @Select("select * from orders where id = #{id}")
    Orders getById(Long id);

    Double sumByMap(Map map);

    Integer countByMap(Map map);

    List<GoodsSalesDTO> getSalesTop10(@Param("begin") LocalDateTime beginTime, @Param("end") LocalDateTime endTime);
}
