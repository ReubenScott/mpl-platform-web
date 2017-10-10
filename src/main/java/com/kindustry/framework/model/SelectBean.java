package com.kindustry.framework.model;

import java.util.List;

import com.kindustry.framework.annotation.XMLParse;
import com.kindustry.framework.annotation.XMLParse.XMLType;

public class SelectBean {

	@XMLParse
	private String id;
	@XMLParse
	private String selectType;
	@XMLParse(type=XMLType.ComplexElement,key="option",subType="com.temobi.base.model.OptionBean")
	private List<OptionBean> optionList;
	
	private String serviceId;
	private String method;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSelectType() {
		return selectType;
	}
	public void setSelectType(String selectType) {
		this.selectType = selectType;
	}
	public List<OptionBean> getOptionList() {
		return optionList;
	}
	public void setOptionList(List<OptionBean> optionList) {
		this.optionList = optionList;
	}
	public String getServiceId() {
		return serviceId;
	}
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	
	
	
}
