package com.kindustry.cashier.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.mybatis.spring.annotation.MapperScan;

import com.kindustry.cashier.entity.Goods;

/**
 * 商品数据访问接口
 */
@MapperScan
// @Mapper
// @Repository
public interface CashierMapper extends BaseMapper<Goods>{

  /**
   * 根据商品编号获得商品对象 findGoodsByBarcode
   */
  public Goods getGoodsByBarcode(@Param("barcode") String barcode);

  /**
   * 获得所有
   */
  public List<Goods> getAllGoods();

  /**
   * 添加商品
   */
  public int add(Goods entity);

}