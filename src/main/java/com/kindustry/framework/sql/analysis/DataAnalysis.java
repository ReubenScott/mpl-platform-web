package com.kindustry.framework.sql.analysis;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.kindustry.framework.sql.util.DataSplit;


public class DataAnalysis {
	
	private byte[] data;
	
	private List<String[]> result;
	
	public DataAnalysis(byte[] data) {
		this.data = data;
	}

	public void process(){
		
		List<byte[]> list = DataSplit.splitBytes(data, (byte) 10);

		result = new ArrayList<String[]>();

		try {
			for (byte[] lineByte : list) {
				result.add(new String(DataSplit.replace(lineByte, (byte)29,(byte)44), "gbk").split(","));
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	public List<String[]> getResult(){
		return result;
	}
	
	public List<String> getData(int index){
		return Arrays.asList(result.get(index));
	}
	
	public int getDataSize(){
		return result.size();
	}
}
