package ins.sino.claimcar.certify.vo;

import java.util.List;

/**
 * Custom VO class of PO PrpLCertifyCode
 */ 
public class PrpLCertifyCodeVo extends PrpLCertifyCodeVoBase implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
	private List<PrpLCertifyCodeVo> prpLCertifyCodeVoList;
	
	private String isSelected;//是否勾选，1勾选
	
	private String disabled;//是否可以取消   1代表禁止取消

	
	public List<PrpLCertifyCodeVo> getPrpLCertifyCodeVoList() {
		return prpLCertifyCodeVoList;
	}

	
	public void setPrpLCertifyCodeVoList(List<PrpLCertifyCodeVo> prpLCertifyCodeVoList) {
		this.prpLCertifyCodeVoList = prpLCertifyCodeVoList;
	}

	
	public String getIsSelected() {
		return isSelected;
	}

	
	public void setIsSelected(String isSelected) {
		this.isSelected = isSelected;
	}


	
	public String getDisabled() {
		return disabled;
	}


	
	public void setDisabled(String disabled) {
		this.disabled = disabled;
	}
	
	
}
