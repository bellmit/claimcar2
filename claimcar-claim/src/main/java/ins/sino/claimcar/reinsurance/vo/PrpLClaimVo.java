package ins.sino.claimcar.reinsurance.vo;

import ins.platform.utils.xstreamconverters.SinosoftDateConverter;
import ins.platform.utils.xstreamconverters.SinosoftDoubleConverter;

import java.io.Serializable;
import java.util.Date;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;


/**
 * �����������PrpLClaimVo
 * @author luows
 * @since (2016��7��26�� ����9:11:16): <br>
 */ 
@XStreamAlias("PrpLClaim")
public class PrpLClaimVo implements Serializable  {
	private static final long serialVersionUID = 1L;
	@XStreamAlias("claimNo")   //��������
	private String claimNo;
	@XStreamAlias("riskCode")	//���ִ���
	private String riskCode;
	@XStreamAlias("registNo")	//��������
	private String registNo;
	@XStreamAlias("policyNo")	//������
	private String policyNo;
	@XStreamAlias("currency")	//�ұ����
	private String currency;
	@XStreamAlias("codeCName")
	private String codeCName;
	@XStreamConverter(value = SinosoftDateConverter.class, strings = {"yyyyMMdd"})
	private Date damageStartDate;	//����������(����ʱ��)
	@XStreamConverter(value = SinosoftDateConverter.class, strings = {"yyyyMMdd"})
	private Date damageEndDate;		//��������ֹ(����ʱ��)
	@XStreamAlias("damageCode")		//����ԭ�����
	private String damageCode;
	@XStreamAlias("addressCode")	//���յش���
	private String addressCode;
	@XStreamAlias("agentName")		//���������
	private String agentName;
	@XStreamAlias("makeCom")		//��������
	private String makeCom;
	@XStreamAlias("handlerCode")	//�����˴���
	private String handlerCode;
	@XStreamConverter(value = SinosoftDateConverter.class, strings = {"yyyyMMdd"})
	private Date inputDate;			//������䵥����
	@XStreamAlias("damageAddress")	//���յص�
	private String damageAddress;
	@XStreamAlias("damageKind")		//�����ձ�
	private String damageKind;
	@XStreamConverter(value = SinosoftDoubleConverter.class,strings = {"0.00"})
	private double sumClaim;		//������ʧ���
	@XStreamAlias("damageAreaPostCode")
	private String damageAreaPostCode;		//���յ��ʱ�
	@XStreamAlias("endCaseFlag")
	private String endCaseFlag;				//�Ƿ��Զ��᰸
	@XStreamAlias("dangerNo")
	private int dangerNo;					//Σ�յ�λ��


	public PrpLClaimVo() {
	
    }
	public String getClaimNo() {
		return this.claimNo;
	}

	public void setClaimNo(String claimNo) {
		this.claimNo = claimNo;
	}

	public String getRegistNo() {
		return this.registNo;
	}

	public void setRegistNo(String registNo) {
		this.registNo = registNo;
	}

	public String getPolicyNo() {
		return this.policyNo;
	}

	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}

	public String getRiskCode() {
		return this.riskCode;
	}

	public void setRiskCode(String riskCode) {
		this.riskCode = riskCode;
	}

	public String getCurrency() {
		return this.currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public Date getDamageStartDate() {
		return this.damageStartDate;
	}

	public void setDamageStartDate(Date damageStartDate) {
		this.damageStartDate = damageStartDate;
	}

	public Date getDamageEndDate() {
		return this.damageEndDate;
	}

	public void setDamageEndDate(Date damageEndDate) {
		this.damageEndDate = damageEndDate;
	}

	public String getDamageCode() {
		return this.damageCode;
	}

	public void setDamageCode(String damageCode) {
		this.damageCode = damageCode;
	}

	public String getAddressCode() {
		return this.addressCode;
	}

	public void setAddressCode(String addressCode) {
		this.addressCode = addressCode;
	}

	public String getAgentName () {
		return this.agentName;
	}

	public void setAgentName (String agentName) {
		this.agentName = agentName;
	}

	public String getMakeCom() {
		return this.makeCom;
	}

	public void setMakeCom(String makeCom) {
		this.makeCom = makeCom;
	}

	public String getHandlerCode(){
		return this.handlerCode;
	}
	
	public void setHandlerCode(String handlerCode){
		this.handlerCode=handlerCode;
	}
	
	public Date getInputDate() {
		return this.inputDate;
	}

	public void setInputDate(Date inputDate) {
		this.inputDate = inputDate;
	}
	
	public String getDamageAddress() {
		return this.damageAddress;
	}

	public void setDamageAddress(String damageAddress) {
		this.damageAddress = damageAddress;
	}
	
	public String getDamageKind() {
		return this.damageKind;
	}

	public void setDamageKind(String damageKind) {
		this.damageKind = damageKind;
	}

	public double getSumClaim() {
		return this.sumClaim;
	}

	public void setSumDefLoss(double sumClaim) {
		this.sumClaim = sumClaim;
	}

	public String getDamageAreaPostCode() {
		return this.damageAreaPostCode;
	}

	public void setDamageAreaPostCode(String damageAreaPostCode) {
		this.damageAreaPostCode = damageAreaPostCode;
	}


	public String getEndCaseFlag() {
		return this.endCaseFlag;
	}

	public void setEndCaseFlag(String endCaseFlag) {
		this.endCaseFlag = endCaseFlag;
	}
	
	public int getDangerNo() {
		return this.dangerNo;
	}

	public void setDangerNo(int dangerNo) {
		this.dangerNo = dangerNo;
	}
	public String getCodeCName() {
		return codeCName;
	}
	public void setCodeCName(String codeCName) {
		this.codeCName = codeCName;
	}
	public void setSumClaim(double sumClaim) {
		this.sumClaim = sumClaim;
	}
}
