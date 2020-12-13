package ins.sino.claimcar.reinsurance.vo;

import ins.platform.utils.xstreamconverters.SinosoftDoubleConverter;
import ins.platform.utils.xstreamconverters.SinosoftIntegerConverter;

import java.io.Serializable;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;

/**
 * �����⸶�� PrpLcfeecoinsVo
 * @author luows
 */
@XStreamAlias("PrpLcfeecoins")
public class PrpLcfeecoinsVo implements Serializable{
	private static final long serialVersionUID = 1L;
	@XStreamAlias("compensateNo")
	private	String 	compensateNo;	//	�������
	@XStreamConverter(value = SinosoftIntegerConverter.class)
	private	int	dangerNo;	//	Σ�յ�λ���Ϊ1
	@XStreamConverter(value = SinosoftIntegerConverter.class)
	private	int	serialNo;	//	���Ϊ0
	@XStreamAlias("riskCode")
	private	String	riskCode;	//	����
	@XStreamAlias("policyNo")
	private	String 	policyNo;	//	������
	@XStreamAlias("currency")
	private	String	currency;	//	�ұ�
	@XStreamAlias("coinsCode")
	private	String 	coinsCode;	//	�����˱��
	@XStreamAlias("coinsName")
	private	String 	coinsName;	//	���������
	@XStreamAlias("coinsType")
	private	String 	coinsType;	//	��������־
	@XStreamConverter(value = SinosoftDoubleConverter.class,strings = {"0.00"})
	private	double	coinsRate;	//	������־
	@XStreamAlias("chiefFlag")
	private	String	chiefFlag;	//	��������־
	@XStreamConverter(value = SinosoftDoubleConverter.class,strings = {"0.00"})
	private	double	coinsSumPaid;	//	������ռ���
	@XStreamAlias("lossFeeType")
	private	String	lossFeeType;	//	�⸶����

	protected PrpLcfeecoinsVo() {
		
    }
	
	public String getCompensateNo() {
		return this.compensateNo;
	}

	public void setCompensateNo(String compensateNo) {
		this.compensateNo = compensateNo;
	}
	
	public int getDangerNo(){
		return this.dangerNo;
	}
	
	public void setDangerNo(int dangerNo){
		this.dangerNo=dangerNo;
	}
	
	public int getSerialNo(){
		return this.serialNo;
	}
	
	public void setSerialNo(int serialNo){
		this.serialNo=serialNo;
	}
	
	public String getRiskCode() {
		return this.riskCode;
	}

	public void setRiskCode(String riskCode) {
		this.riskCode = riskCode;
	}
	
	public String getPolicyNo() {
		return this.policyNo;
	}

	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}
	
	public String getCurrency() {
		return this.currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}
	
	public String getCoinsCode() {
		return this.coinsCode;
	}

	public void setCoinsCode(String coinsCode) {
		this.coinsCode = coinsCode;
	}
	
	public String getCoinsName() {
		return this.coinsName;
	}

	public void setCoinsName(String coinsName) {
		this.coinsName = coinsName;
	}
	
	public String getCoinsType() {
		return this.coinsType;
	}

	public void setCoinsType(String coinsType) {
		this.coinsType = coinsType;
	}
	
	public double getCoinsRate(){
		return this.coinsRate;
	}
	
	public void setCoinsRate(double coinsRate){
		this.coinsRate=coinsRate;
	}
	
	public String getChiefFlag() {
		return this.chiefFlag;
	}

	public void setChiefFlag(String chiefFlag) {
		this.chiefFlag = chiefFlag;
	}
	
	public double getCoinsSumPaid(){
		return this.coinsSumPaid;
	}
	
	public void setCoinsSumPaid(double coinsSumPaid){
		this.coinsSumPaid=coinsSumPaid;
	}
	
	public String getLossFeeType() {
		return this.lossFeeType;
	}

	public void setLossFeeType(String lossFeeType) {
		this.lossFeeType = lossFeeType;
	}
}
