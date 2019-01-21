package com.kindustry.cashier.entity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单
 */
public class Order {

  // paycode VARCHAR(20) NOT NULL, -- 订单号
  private String paycode;

  // guide_no VARCHAR(20) , -- 促销员 预留
  private String guideNo;

  // member_no VARCHAR(10) , -- 会员号
  private String memberNo;

  // terminal_no VARCHAR(20) NOT NULL, -- 终端号
  private String terminalNo;

  // cashier_no VARCHAR(20) NOT NULL, -- 收银员
  private String cashierNo;

  // total_pieces NUMERIC(16,6) NOT NULL, -- 总件数
  private int totalPieces;

  // total_quantity NUMERIC(16,6) NOT NULL, -- 总数量
  private BigDecimal totalQuantity;

  // total_amount NUMERIC(16,6) NOT NULL, -- 总金額
  private BigDecimal totalAmount;

  // payment_mode CHAR(10) NOT NULL, -- 支付方式
  private String paymentMode;

  // traded_mark CHAR(1) NOT NULL, -- 交易成功标识
  private Boolean tradedMark;

  // trade_time TIMESTAMP, -- 交易时间
  private Date tradeTime;

  // remark VARCHAR(80),
  private String remark;

  public String getPaycode() {
    return paycode;
  }

  public void setPaycode(String paycode) {
    this.paycode = paycode;
  }

  public String getGuideNo() {
    return guideNo;
  }

  public void setGuideNo(String guideNo) {
    this.guideNo = guideNo;
  }

  public String getMemberNo() {
    return memberNo;
  }

  public void setMemberNo(String memberNo) {
    this.memberNo = memberNo;
  }

  public String getTerminalNo() {
    return terminalNo;
  }

  public void setTerminalNo(String terminalNo) {
    this.terminalNo = terminalNo;
  }

  public String getCashierNo() {
    return cashierNo;
  }

  public void setCashierNo(String cashierNo) {
    this.cashierNo = cashierNo;
  }

  public int getTotalPieces() {
    return totalPieces;
  }

  public void setTotalPieces(int totalPieces) {
    this.totalPieces = totalPieces;
  }

  public BigDecimal getTotalQuantity() {
    return totalQuantity;
  }

  public void setTotalQuantity(BigDecimal totalQuantity) {
    this.totalQuantity = totalQuantity;
  }

  public BigDecimal getTotalAmount() {
    return totalAmount;
  }

  public void setTotalAmount(BigDecimal totalAmount) {
    this.totalAmount = totalAmount;
  }

  public String getPaymentMode() {
    return paymentMode;
  }

  public void setPaymentMode(String paymentMode) {
    this.paymentMode = paymentMode;
  }

  public Boolean getTradedMark() {
    return tradedMark;
  }

  public void setTradedMark(Boolean tradedMark) {
    this.tradedMark = tradedMark;
  }

  public Date getTradeTime() {
    return tradeTime;
  }

  public void setTradeTime(Date tradeTime) {
    this.tradeTime = tradeTime;
  }

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

}