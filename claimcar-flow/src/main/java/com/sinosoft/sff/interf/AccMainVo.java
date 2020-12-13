package com.sinosoft.sff.interf;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * �����VO
 * 
 * @author ��<a href="mailto:huangyi@sinosoft.com.cn">HuangYi</a>
 * @Company www.sinosoft.com.cn
 * @Copyright Copyright (c) 2016��7��6��
 * @since (2016��7��6�� ����8:11:16): <br>
 */
@XStreamAlias("AccrollbackaccountMain")
public class AccMainVo implements Serializable {

	private static final long serialVersionUID = 1L;
	@XStreamAlias("CertiNo")
	private String CertiNo;// �ַ� ҵ��ţ�������š�Ԥ��
	@XStreamAlias("paytype")
	private String paytype;// �ո�ԭ��
	@XStreamAlias("ModifyCode")
	private String ModifyCode;// �޸���
	@XStreamAlias("OperateComCode")
	private String OperateComCode;//  �����
	@XStreamAlias("ModifyTime")
	private String ModifyTime;// �޸�ʱ��
	@XStreamAlias("Status")
	private String Status;// ״̬��
	@XStreamAlias("CertiType")
	private String certiType;//业务类型 正常理赔是C 结算单是JS
	@XStreamAlias("Modifytype")
	private String modifytype;
	@XStreamImplicit
	private List<AccRecAccountVo> accountVoList;
	public String getCertiNo() {
		return CertiNo;
	}
	public void setCertiNo(String certiNo) {
		CertiNo = certiNo;
	}
	public String getPaytype() {
		return paytype;
	}
	public void setPaytype(String paytype) {
		this.paytype = paytype;
	}
	public String getModifyCode() {
		return ModifyCode;
	}
	public void setModifyCode(String modifyCode) {
		ModifyCode = modifyCode;
	}
	public String getModifyTime() {
		return ModifyTime;
	}
	public void setModifyTime(String modifyTime) {
		ModifyTime = modifyTime;
	}
	
	
	public String getStatus() {
		return Status;
	}
	public void setStatus(String status) {
		Status = status;
	}
	public List<AccRecAccountVo> getAccountVo() {
		return accountVoList;
	}
	public void setAccountVo(List<AccRecAccountVo> accountVoList) {
		this.accountVoList = accountVoList;
	}
	public String getOperateComCode() {
		return OperateComCode;
	}
	public void setOperateComCode(String operateComCode) {
		OperateComCode = operateComCode;
	}
	public String getCertiType() {
		return certiType;
	}
	public void setCertiType(String certiType) {
		this.certiType = certiType;
	}
	public String getModifytype() {
		return modifytype;
	}
	public void setModifytype(String modifytype) {
		this.modifytype = modifytype;
	}

}