package ins.sino.claimcar.reinsurance.vo;

import java.io.Serializable;
import java.util.Collection;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
/**
 * �᰸���ٱ��Ѿ���ݷ�̯����
 * @author luows
 * @since (2016��8��2�� ����9:21:16): <br>
 */ 
@XStreamAlias("CompensateVo")
public class CompensateVo  implements Serializable{
	private static final long serialVersionUID = 1L;
	@XStreamAlias("businessType")
	private  String  businessType; 
	@XStreamAlias("adjustTimes")
	private String adjustTimes;
	@XStreamAlias("remarks")
	private String remarks;
	//����������Ϣ   ����1�� ����3 ���᰸/�᰸/ע��/����/���ؿ��ⰸ 0
	@XStreamAlias("prpLClaimVo")
	private PrpLClaimVo   prpLClaimVo; 
	//����������Ϣ
	@XStreamAlias("prpLCompensateVo")
	private PrpLCompensateVo prpLCompensateVo;
	
	@SuppressWarnings("rawtypes")
	@XStreamImplicit
	private Collection prpLlossList;    
	//���㳵����ʧ�Ʋ����Ϣ
	@SuppressWarnings("rawtypes")
	@XStreamImplicit
	private Collection prpLpersonLossList;	
	//����������ʧ����Ϣ
	@SuppressWarnings("rawtypes")
	@XStreamImplicit
	private Collection prpLchargeList;		
	//���������Ϣ����Ϣ
	@SuppressWarnings("rawtypes")
	@XStreamImplicit
	private Collection prpLcfeecoinsList;	
	//�����⸶����Ϣ
	
	@SuppressWarnings({ "rawtypes", "unused" })
	private Collection PrpLcfeecoinsList;
	public String getBusinessType() {
		return this.businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}
	
	public String getAdjustTimes() {
		return adjustTimes;
	}

	public void setAdjustTimes(String adjustTimes) {
		this.adjustTimes = adjustTimes;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
	public PrpLClaimVo getPrpLClaimVo() {
		return this.prpLClaimVo;
	}

	public void setPrpLClaimVo(PrpLClaimVo prpLClaimVo) {
		this.prpLClaimVo = prpLClaimVo;
	}
	
	public PrpLCompensateVo getPrpLCompensateVo() {
		return this.prpLCompensateVo;
	}

	public void setPrpLCompensateVo(PrpLCompensateVo prpLCompensateVo) {
		this.prpLCompensateVo = prpLCompensateVo;
	}
	
	@SuppressWarnings("rawtypes")
	public Collection getPrpLLossVo() {
		return prpLlossList;
	}

	public void setPrpLLossVo(@SuppressWarnings("rawtypes") Collection prpLlossList) {
		this.prpLlossList = prpLlossList;
	}
	
	@SuppressWarnings("rawtypes")
	public Collection getPrpLpersonLossVo() {
		return this.prpLpersonLossList;
	}

	public void setPrpLpersonLossVo(@SuppressWarnings("rawtypes") Collection prpLpersonLossList) {
		this.prpLpersonLossList = prpLpersonLossList;
	}
	
	@SuppressWarnings("rawtypes")
	public Collection getPrpLChargeList() {
		return this.prpLchargeList;
	}

	public void setPrpLChargeList(@SuppressWarnings("rawtypes") Collection prpLchargeList) {
		this.prpLchargeList = prpLchargeList;
	}
	
	@SuppressWarnings("rawtypes")
	public Collection getPrpLcfeecoinsVo() {
		return this.prpLcfeecoinsList;
	}

	public void setPrpLcfeecoinsVo(@SuppressWarnings("rawtypes") Collection PrpLcfeecoinsList) {
		this.prpLcfeecoinsList = PrpLcfeecoinsList;
	}
	
}
