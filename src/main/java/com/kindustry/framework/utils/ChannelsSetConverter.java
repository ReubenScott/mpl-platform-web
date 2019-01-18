package com.kindustry.framework.utils;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.beanutils.Converter;


public class ChannelsSetConverter implements Converter {

	public Object convert(Class targetClass, Object value) {
		
		String[] channelIds = null;
		
		if(value instanceof String){
			channelIds = new String[]{(String)value};
		}
		if(value instanceof String[]){
			channelIds = (String[])value;
		}
		
		if(channelIds != null){
			Set channels = new HashSet();
			for(String channelId:channelIds){
//				Channel c = new Channel();
//				c.setId(Integer.parseInt(channelId));
//				channels.add(c);
			}
			return channels;
		}
		
		return null;
	}

}
