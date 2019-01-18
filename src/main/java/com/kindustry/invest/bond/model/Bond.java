package com.kindustry.invest.bond.model;

import java.io.Serializable;

public class Bond implements Serializable{

  private static final long serialVersionUID = 1L;
  
  
  private String code; // 代码
  private String name; // 名称
  private Float facePrice; // 面值
  private Float netPrice; // 净价
  private Float fullPrice; // 全价
  private Float accruedInterest; // 应计利息
  private Float turnVolume; // 成交额(万元)
  private Integer inpaydays; // 距付息天数 Interest payment days
  private Float couponRate; // 票息
  private String bondCredit; // 债券信用
  private String mainCredit; // 主体信用
  private String assukind; // 担保方式
  private String dueDate; // 到期日
  private Float amount; // 规模 (亿)

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Float getFacePrice() {
    return facePrice;
  }

  public void setFacePrice(Float facePrice) {
    this.facePrice = facePrice;
  }

  public Float getNetPrice() {
    return netPrice;
  }

  public void setNetPrice(Float netPrice) {
    this.netPrice = netPrice;
  }

  public Float getFullPrice() {
    return fullPrice;
  }

  public void setFullPrice(Float fullPrice) {
    this.fullPrice = fullPrice;
  }

  public Float getAccruedInterest() {
    return accruedInterest;
  }

  public void setAccruedInterest(Float accruedInterest) {
    this.accruedInterest = accruedInterest;
  }

  public Float getTurnVolume() {
    return turnVolume;
  }

  public void setTurnVolume(Float turnVolume) {
    this.turnVolume = turnVolume;
  }

  public Integer getInpaydays() {
    return inpaydays;
  }

  public void setInpaydays(Integer inpaydays) {
    this.inpaydays = inpaydays;
  }

  public Float getCouponRate() {
    return couponRate;
  }

  public void setCouponRate(Float couponRate) {
    this.couponRate = couponRate;
  }

  public String getBondCredit() {
    return bondCredit;
  }

  public void setBondCredit(String bondCredit) {
    this.bondCredit = bondCredit;
  }

  public String getMainCredit() {
    return mainCredit;
  }

  public void setMainCredit(String mainCredit) {
    this.mainCredit = mainCredit;
  }

  public String getAssukind() {
    return assukind;
  }

  public void setAssukind(String assukind) {
    this.assukind = assukind;
  }

  public String getDueDate() {
    return dueDate;
  }

  public void setDueDate(String dueDate) {
    this.dueDate = dueDate;
  }

  public Float getAmount() {
    return amount;
  }

  public void setAmount(Float amount) {
    this.amount = amount;
  }

}
