package com.kindustry.cashier.entity;

import java.io.Serializable;

public class Supplier implements Serializable {
  private String supplierid;

  private String companyname;

  private String contactname;

  private String address;

  private String phone;

  private static final long serialVersionUID = 1L;

  public String getSupplierid() {
    return supplierid;
  }

  public void setSupplierid(String supplierid) {
    this.supplierid = supplierid == null ? null : supplierid.trim();
  }

  public String getCompanyname() {
    return companyname;
  }

  public void setCompanyname(String companyname) {
    this.companyname = companyname == null ? null : companyname.trim();
  }

  public String getContactname() {
    return contactname;
  }

  public void setContactname(String contactname) {
    this.contactname = contactname == null ? null : contactname.trim();
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address == null ? null : address.trim();
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone == null ? null : phone.trim();
  }
}