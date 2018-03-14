package com.belonk.crypt;

public class AlgorithmData {
	
	private String key;
	
	private String dataMing;
	
	private String dataMi;
	
	private boolean doDisplay =true;//true-需要做显示处理，false-不需要做显示处理

	public boolean isDoDisplay() {
		return doDisplay;
	}

	public void setDoDisplay(boolean doDisplay) {
		this.doDisplay = doDisplay;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getDataMing() {
		return dataMing;
	}

	public void setDataMing(String dataMing) {
		this.dataMing = dataMing;
	}

	public String getDataMi() {
		return dataMi;
	}

	public void setDataMi(String dataMi) {
		this.dataMi = dataMi;
	}	

}
