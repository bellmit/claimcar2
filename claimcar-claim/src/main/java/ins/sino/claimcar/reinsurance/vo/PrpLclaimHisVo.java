package ins.sino.claimcar.reinsurance.vo;

import ins.platform.utils.xstreamconverters.SinosoftDoubleConverter;
import ins.platform.utils.xstreamconverters.SinosoftIntegerConverter;

import java.io.Serializable;
import java.util.Date;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;
/**
 * ������ʷ����Ϣ
 * @author luows
 */ 
@XStreamAlias("PrpLclaimHis")
public class PrpLclaimHisVo implements Serializable  {
			private static final long serialVersionUID = 1L;
			@XStreamAlias("claimNo")
			private String claimNo;// ������
			@XStreamAlias("riskCode")
			private String riskCode;// ����
			@XStreamConverter(value = SinosoftIntegerConverter.class)
			private int serialNo;// ���
			@XStreamConverter(value = SinosoftIntegerConverter.class)
			private int itemKindNo;// �Ա���������
			@XStreamAlias("kindCode")
			private String kindCode;// �ձ����
			@XStreamAlias("kindName")
			private String kindName;// �ձ����
			@XStreamAlias("sumClaim")
			private  double sumClaim;
			@XStreamAlias("itemCode")
			private String itemCode;// ���������Ŀ����
			@XStreamAlias("currency")
			private String currency;// �ұ�
			@XStreamAlias("inputDate")
			private Date inputDate;// ��������
			@XStreamAlias("lossFeeType")
			private String lossFeeType;// ��ʧ��Ŀ
			@XStreamAlias("itemDetailName")
			private String itemDetailName;// �����Ŀ��ϸ���
			@XStreamConverter(value = SinosoftIntegerConverter.class)
			private int dangerNo;// Σ�յ�λ���
			@XStreamConverter(value = SinosoftDoubleConverter.class,strings = {"0.00"})
			private double chgSumClaim;// ������仯��
			
		public double getSumClaim() {
				return sumClaim;
			}

			public void setSumClaim(double sumClaim) {
				this.sumClaim = sumClaim;
			}

		public String getClaimNo(){
			return claimNo;
		}
		
		public void setClaimNo(String claimNo){
			this.claimNo=claimNo;
		}
		
		public String getRiskCode(){
			return riskCode;
		}
		
		public void setRiskCode(String riskCode){
		      this.riskCode=riskCode;
		}
		
		public int getSerialNo(){
			return serialNo;
		}
		
		public void setSerialNo(int serialNo){
		      this.serialNo=serialNo;
		}
		
		public int getItemKindNo(){
			return itemKindNo;
		}
		
		public void setItemKindNo(int itemKindNo){
		      this.itemKindNo=itemKindNo;
		}
		
		public String getKindCode(){
			return kindCode;
		}
		
		public void setKindCode(String kindCode){
		      this.kindCode=kindCode;
		}
		
		public String getKindName(){
			return kindName;
		}
		
		public void setKindName(String kindName){
		      this.kindName=kindName;
		}
		
		public String getItemCode(){
			return itemCode;
		}
		
		public void setItemCode(String itemCode){
		      this.itemCode=itemCode;
		}
		
		public String getCurrency(){
			return currency;
		}
		public void setCurrency(String currency){
			  this.currency=currency;
		}
		
		public Date getInputDate(){
			return inputDate;
		}
		
		public void setInputDate(Date inputDate){
		      this.inputDate=inputDate;
		}
		
		public String getLossFeeType(){
			return lossFeeType;
		}
		
		public void setLossFeeType(String lossFeeType){
		      this.lossFeeType=lossFeeType;
		}
		
		public String getItemDetailName(){
			return itemDetailName;
		}
		
		public void setItemDetailName(String itemDetailName){
		      this.itemDetailName=itemDetailName;
		}
		
		public int getDangerNo(){
			return dangerNo;
		}
		
		public void setDangerNo(int dangerNo){
		      this.dangerNo=dangerNo;
		}
		
		public double getChgSumClaim(){
			return chgSumClaim;
		}
		
		public void setChgSumClaim(double chgSumClaim){
		      this.chgSumClaim=chgSumClaim;
		}
}
