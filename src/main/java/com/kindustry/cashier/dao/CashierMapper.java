package com.kindustry.cashier.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.kindustry.cashier.entity.CommodityEntity;
import com.kindustry.orm.dao.BaseMapper;

/**
 * 商品数据访问接口
 */
@Mapper
public interface CashierMapper extends BaseMapper<CommodityEntity> {

  /**
   * 根据商品编号获得商品对象 findGoodsByBarcode
   */
  public CommodityEntity getGoodsByBarcode(@Param("barcode") String barcode);

  /**
   * 获得所有
   */
  public List<CommodityEntity> getAllGoods();

  /**
   * 添加商品
   */
  public int add(CommodityEntity entity);

}