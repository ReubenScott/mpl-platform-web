package com.kindustry.cashier.entity;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.kindustry.framework.orm.BaseEntity;

/**
 * 商品信息表
 */
public class Goods extends BaseEntity {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  /**
   * 商品条码
   */
  @JsonProperty("out")
  private String barcode; // barcode VARCHAR(20) NOT NULL, -- 商品条码

  /**
   * 商品名称
   */
  private String title; // name VARCHAR(80) NOT NULL, -- 商品名称

  /**
   * 货号
   */
  private String cargoNo; // cargo_no VARCHAR(20) unique NOT NULL, -- 货号

  /**
   * 规格型号
   */
  private String specs; // specs varchar(20), -- 规格型号

  /**
   * 单位
   */
  private String unit; // varchar(20), -- 单位

  /**
   * 商品类别
   */
  private String categoryId; // category_id varchar(20), -- 商品类别

  /**
   * 进价
   */
  private BigDecimal purchasePrice; // purchase_price NUMERIC(16,6), -- 进价

  /**
   * 零售价
   */
  private BigDecimal salePrice; // sale_price NUMERIC(16,2), -- 零售价

  /**
   * 会员价
   */
  private BigDecimal memberPrice; // member_price NUMERIC(16,2), -- 会员价

  /**
   * 首次入库时间
   */
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date storageTime; // storage_time TIMESTAMP(0) WITHOUT TIME ZONE, --

  /**
   * 机构号
   */
  @JsonProperty("branchNo")
  private String branchNo; // branch_no varchar(20) , -- 机构号 暂时不用

  /**
   * 备注
   */
  @JsonIgnore
  private String remarks; // remarks VARCHAR(80), -- 备注

  public String getBarcode() {
    return barcode;
  }

  public void setBarcode(String barcode) {
    this.barcode = barcode;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getCargoNo() {
    return cargoNo;
  }

  public void setCargoNo(String cargoNo) {
    this.cargoNo = cargoNo;
  }

  public String getSpecs() {
    return specs;
  }

  public void setSpecs(String specs) {
    this.specs = specs;
  }

  public String getUnit() {
    return unit;
  }

  public void setUnit(String unit) {
    this.unit = unit;
  }

  public String getCategoryId() {
    return categoryId;
  }

  public void setCategoryId(String categoryId) {
    this.categoryId = categoryId;
  }

  public BigDecimal getPurchasePrice() {
    return purchasePrice;
  }

  public void setPurchasePrice(BigDecimal purchasePrice) {
    this.purchasePrice = purchasePrice;
  }

  public BigDecimal getSalePrice() {
    return salePrice;
  }

  public void setSalePrice(BigDecimal salePrice) {
    this.salePrice = salePrice;
  }

  public BigDecimal getMemberPrice() {
    return memberPrice;
  }

  public void setMemberPrice(BigDecimal memberPrice) {
    this.memberPrice = memberPrice;
  }

  public Date getStorageTime() {
    return storageTime;
  }

  public void setStorageTime(Date storageTime) {
    this.storageTime = storageTime;
  }

  public String getBranchNo() {
    return branchNo;
  }

  public void setBranchNo(String branchNo) {
    this.branchNo = branchNo;
  }

  public String getRemarks() {
    return remarks;
  }

  public void setRemarks(String remarks) {
    this.remarks = remarks;
  }

}