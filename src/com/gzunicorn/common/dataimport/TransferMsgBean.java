package com.gzunicorn.common.dataimport;

public class TransferMsgBean {
	public TransferMsgBean(){
	}
	/**
	 * title 返回页面显示时的列表的列名
	 */
	private String[] title;
	/**
	 * RSRow 导入及最终的显示列表
	 */
	private String[][] RSRow;

	/**
	 * checkMsg 检查是否通过
	 * 	ok 通过
	 * 	no 不通过
	 */
	private String checkMsg;
	
	public String getCheckMsg() {
		return checkMsg;
	}
	public void setCheckMsg(String checkMsg) {
		this.checkMsg = checkMsg;
	}
	public String[][] getRSRow() {
		return RSRow;
	}
	public void setRSRow(String[][] row) {
		RSRow = row;
	}
	public String[] getTitle() {
		return title;
	}
	public void setTitle(String[] title) {
		this.title = title;
	}
}
