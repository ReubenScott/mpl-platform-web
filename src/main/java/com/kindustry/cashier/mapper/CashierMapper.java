package com.kindustry.cashier.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.mybatis.spring.annotation.MapperScan;

import com.kindustry.cashier.entity.Goods;

/**
 * 商品数据访问接口
 */
@MapperScan
public interface CashierMapper {

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

  /**
   * 根据商品编号删除商品
   */
  public int delete(int id);

  /**
   * 更新商品
   */
  public int update(Goods entity);
  
  
}