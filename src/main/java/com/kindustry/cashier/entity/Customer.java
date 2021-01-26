package com.kindustry.cashier.entity;

import java.io.Serializable;

public class Customer implements Serializable {
  private String customerid;

  private String companyname;

  private String contactname;

  private String phone;

  private String fax;

  private String address;

  private static final long serialVersionUID = 1L;

  public String getCustomerid() {
    return customerid;
  }

  public void setCustomerid(String customerid) {
    this.customerid = customerid == null ? null : customerid.trim();
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

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone == null ? null : phone.trim();
  }

  public String getFax() {
    return fax;
  }

  public void setFax(String fax) {
    this.fax = fax == null ? null : fax.trim();
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address == null ? null : address.trim();
  }
}