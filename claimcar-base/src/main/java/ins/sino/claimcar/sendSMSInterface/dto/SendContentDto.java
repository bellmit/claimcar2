/******************************************************************************
* Copyright 2010-2011 the original author or authors.
* CREATETIME : 2012-5-22 ����10:39:45
******************************************************************************/
package ins.sino.claimcar.sendSMSInterface.dto;

import java.util.Date;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * ���ݷ���Dto
 * @Copyright Copyright (c) 2012
 * @Company www.sinosoft.com.cn
 * @author ��<a href="mailto:LiangYouKu@sinosoft.com.cn">LiangYouKu</a> 
 * @since  2012-5-22 ����10:39:45
 */
@XStreamAlias("BODY")
public class SendContentDto {
	/**短信编码**/
	@XStreamAlias("MSGID")
	private String msgID; //不能为空
	/**业务号**/
	@XStreamAlias("BUSSNO")
	private String bussNo; //不能为空
	/**手机号**/
	@XStreamAlias("PHONENO")
	private String phoneNo;  //不能为空
	/**信息内容**/
	@XStreamAlias("MESSAGE")
	private String message; //不能为空
	/**操作员代码**/
	@XStreamAlias("OPTCODE")
	private String optCode;
	/**客户姓名**/
	@XStreamAlias("CUSTNAME")
	private String custName;
	@XStreamAlias("OPERATORCODE")
	private String operatorCode;
	/**备注**/
	@XStreamAlias("REMARK")
	private String remark;
	/**机构**/
	@XStreamAlias("COMCODE")
	private String comCode;
	/**发送时间**/
	@XStreamAlias("SENDTIME")
	private Date sendTime;
	
	@XStreamAlias("CHANNELTYPE")
	private String channelType;
	
	public String getMsgID() {
		return msgID;
	}
	public String getBussNo() {
		return bussNo;
	}
	public String getPhoneNo() {
		return phoneNo;
	}
	public String getMessage() {
		return message;
	}
	public String getOptCode() {
		return optCode;
	}
	public String getCustName() {
		return custName;
	}
	public String getRemark() {
		return remark;
	}
	public void setMsgID(String msgID) {
		this.msgID = msgID;
	}
	public void setBussNo(String bussNo) {
		this.bussNo = bussNo;
	}
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public void setOptCode(String optCode) {
		this.optCode = optCode;
	}
	public void setCustName(String custName) {
		this.custName = custName;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public void setOperatorCode(String operatorCode) {
		this.operatorCode = operatorCode;
	}
	public String getOperatorCode() {
		return operatorCode;
	}

	public void setComCode(String comCode) {
		this.comCode = comCode;
	}

	public String getComCode() {
		return comCode;
	}
	public Date getSendTime() {
		return sendTime;
	}
	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}
	public String getChannelType() {
		return channelType;
	}
	public void setChannelType(String channelType) {
		this.channelType = channelType;
	}
	
	
	
}
