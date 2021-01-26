package com.kindustry.cashier.entity;

import java.io.Serializable;

public class Department implements Serializable {
  private String deptid;

  private String createdept;

  private String createdeptname;

  private String createtime;

  private String createuser;

  private String createusername;

  private String modifydept;

  private String modifydeptname;

  private String modifytime;

  private String modifyuser;

  private String modifyusername;

  private Integer orderindex;

  private String status;

  private Integer layer;

  private String nodeinfo;

  private String nodeinfotype;

  private String nodetype;

  private String deptcode;

  private String deptname;

  private String parent;

  private static final long serialVersionUID = 1L;

  public String getDeptid() {
    return deptid;
  }

  public void setDeptid(String deptid) {
    this.deptid = deptid == null ? null : deptid.trim();
  }

  public String getCreatedept() {
    return createdept;
  }

  public void setCreatedept(String createdept) {
    this.createdept = createdept == null ? null : createdept.trim();
  }

  public String getCreatedeptname() {
    return createdeptname;
  }

  public void setCreatedeptname(String createdeptname) {
    this.createdeptname = createdeptname == null ? null : createdeptname.trim();
  }

  public String getCreatetime() {
    return createtime;
  }

  public void setCreatetime(String createtime) {
    this.createtime = createtime == null ? null : createtime.trim();
  }

  public String getCreateuser() {
    return createuser;
  }

  public void setCreateuser(String createuser) {
    this.createuser = createuser == null ? null : createuser.trim();
  }

  public String getCreateusername() {
    return createusername;
  }

  public void setCreateusername(String createusername) {
    this.createusername = createusername == null ? null : createusername.trim();
  }

  public String getModifydept() {
    return modifydept;
  }

  public void setModifydept(String modifydept) {
    this.modifydept = modifydept == null ? null : modifydept.trim();
  }

  public String getModifydeptname() {
    return modifydeptname;
  }

  public void setModifydeptname(String modifydeptname) {
    this.modifydeptname = modifydeptname == null ? null : modifydeptname.trim();
  }

  public String getModifytime() {
    return modifytime;
  }

  public void setModifytime(String modifytime) {
    this.modifytime = modifytime == null ? null : modifytime.trim();
  }

  public String getModifyuser() {
    return modifyuser;
  }

  public void setModifyuser(String modifyuser) {
    this.modifyuser = modifyuser == null ? null : modifyuser.trim();
  }

  public String getModifyusername() {
    return modifyusername;
  }

  public void setModifyusername(String modifyusername) {
    this.modifyusername = modifyusername == null ? null : modifyusername.trim();
  }

  public Integer getOrderindex() {
    return orderindex;
  }

  public void setOrderindex(Integer orderindex) {
    this.orderindex = orderindex;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status == null ? null : status.trim();
  }

  public Integer getLayer() {
    return layer;
  }

  public void setLayer(Integer layer) {
    this.layer = layer;
  }

  public String getNodeinfo() {
    return nodeinfo;
  }

  public void setNodeinfo(String nodeinfo) {
    this.nodeinfo = nodeinfo == null ? null : nodeinfo.trim();
  }

  public String getNodeinfotype() {
    return nodeinfotype;
  }

  public void setNodeinfotype(String nodeinfotype) {
    this.nodeinfotype = nodeinfotype == null ? null : nodeinfotype.trim();
  }

  public String getNodetype() {
    return nodetype;
  }

  public void setNodetype(String nodetype) {
    this.nodetype = nodetype == null ? null : nodetype.trim();
  }

  public String getDeptcode() {
    return deptcode;
  }

  public void setDeptcode(String deptcode) {
    this.deptcode = deptcode == null ? null : deptcode.trim();
  }

  public String getDeptname() {
    return deptname;
  }

  public void setDeptname(String deptname) {
    this.deptname = deptname == null ? null : deptname.trim();
  }

  public String getParent() {
    return parent;
  }

  public void setParent(String parent) {
    this.parent = parent == null ? null : parent.trim();
  }
}