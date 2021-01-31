package com.kindustry.cashier.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.ibatis.type.Alias;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.kindustry.orm.entity.BaseEntity;

/**
 * 商品信息表
 */
@Table(name = "goods")
@Alias("goodsEntity")
public class GoodsEntity extends BaseEntity {
  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  /**
   * 单位
   */
  @Id
  @Column(name = "barcode")
  private String barcode;

  /**
   * 商品名称
   */
  @Column(name = "title")
  private String title;

  /**
   * 货号
   */
  @Column(name = "cargo_no")
  private String cargoNo;

  /**
   * 规格型号
   */
  @Column(name = "specs")
  private String specs;

  /**
   * 单位
   */
  @Column(name = "unit")
  private String unit;

  /**
   * 商品类别
   */
  @Column(name = "category_id")
  private String categoryId;

  /**
   * 进价
   */
  @Column(name = "purchase_price")
  private Double purchasePrice;

  /**
   * 零售价
   */
  @Column(name = "sale_price")
  private Double salePrice;

  /**
   * 会员价
   */
  @Column(name = "member_price")
  private Double memberPrice;

  /**
   * 首次入库时间
   */
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  @Column(name = "storage_time")
  private Date storageTime;

  /**
   * 机构号 暂时不用
   */
  @JsonProperty("branchNo")
  @Column(name = "branch_no")
  private String branchNo;

  /**
   * 备注
   */
  @JsonIgnore
  @Column(name = "remarks")
  private String remarks;

  /**
   * 单位
   * 
   * @return barcode 单位
   */
  public String getBarcode() {
    return barcode;
  }

  /**
   * 单位
   * 
   * @param barcode
   *          单位
   */
  public void setBarcode(String barcode) {
    this.barcode = barcode;
  }

  /**
   * 商品名称
   * 
   * @return title 商品名称
   */
  public String getTitle() {
    return title;
  }

  /**
   * 商品名称
   * 
   * @param title
   *          商品名称
   */
  public void setTitle(String title) {
    this.title = title;
  }

  /**
   * 货号
   * 
   * @return cargo_no 货号
   */
  public String getCargoNo() {
    return cargoNo;
  }

  /**
   * 货号
   * 
   * @param cargoNo
   *          货号
   */
  public void setCargoNo(String cargoNo) {
    this.cargoNo = cargoNo;
  }

  /**
   * 规格型号
   * 
   * @return specs 规格型号
   */
  public String getSpecs() {
    return specs;
  }

  /**
   * 规格型号
   * 
   * @param specs
   *          规格型号
   */
  public void setSpecs(String specs) {
    this.specs = specs;
  }

  /**
   * 单位
   * 
   * @return unit 单位
   */
  public String getUnit() {
    return unit;
  }

  /**
   * 单位
   * 
   * @param unit
   *          单位
   */
  public void setUnit(String unit) {
    this.unit = unit;
  }

  /**
   * 商品类别
   * 
   * @return category_id 商品类别
   */
  public String getCategoryId() {
    return categoryId;
  }

  /**
   * 商品类别
   * 
   * @param categoryId
   *          商品类别
   */
  public void setCategoryId(String categoryId) {
    this.categoryId = categoryId;
  }

  /**
   * 进价
   * 
   * @return purchase_price 进价
   */
  public Double getPurchasePrice() {
    return purchasePrice;
  }

  /**
   * 进价
   * 
   * @param purchasePrice
   *          进价
   */
  public void setPurchasePrice(Double purchasePrice) {
    this.purchasePrice = purchasePrice;
  }

  /**
   * 零售价
   * 
   * @return sale_price 零售价
   */
  public Double getSalePrice() {
    return salePrice;
  }

  /**
   * 零售价
   * 
   * @param salePrice
   *          零售价
   */
  public void setSalePrice(Double salePrice) {
    this.salePrice = salePrice;
  }

  /**
   * 会员价
   * 
   * @return member_price 会员价
   */
  public Double getMemberPrice() {
    return memberPrice;
  }

  /**
   * 会员价
   * 
   * @param memberPrice
   *          会员价
   */
  public void setMemberPrice(Double memberPrice) {
    this.memberPrice = memberPrice;
  }

  /**
   * 首次入库时间
   * 
   * @return storage_time 首次入库时间
   */
  public Date getStorageTime() {
    return storageTime;
  }

  /**
   * 首次入库时间
   * 
   * @param storageTime
   *          首次入库时间
   */
  public void setStorageTime(Date storageTime) {
    this.storageTime = storageTime;
  }

  /**
   * 机构号 暂时不用
   * 
   * @return branch_no 机构号 暂时不用
   */
  public String getBranchNo() {
    return branchNo;
  }

  /**
   * 机构号 暂时不用
   * 
   * @param branchNo
   *          机构号 暂时不用
   */
  public void setBranchNo(String branchNo) {
    this.branchNo = branchNo;
  }

  /**
   * 备注
   * 
   * @return remarks 备注
   */
  public String getRemarks() {
    return remarks;
  }

  /**
   * 备注
   * 
   * @param remarks
   *          备注
   */
  public void setRemarks(String remarks) {
    this.remarks = remarks;
  }
}