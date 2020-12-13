package ins.sino.claimcar.claimjy.vo.price;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
@XStreamAlias("EvalLossInfo") 
public class JyPriceReqEvalLossInfo  implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@XStreamAlias("LossNo") 
    private String  lossNo="";	//定损单号

	@XStreamAlias("ReportCode") 
    private String  reportCode="";	//报案号

	@XStreamAlias("DmgVhclId") 
    private String  dmgVhclId="";	//车损标的主键

	@XStreamAlias("EstiComCode") 
    private String  estiComCode="";	//核价员所属分公司代码

	@XStreamAlias("EstiCompany") 
    private String  estiCompany="";	//核价员所属分公司名称

	@XStreamAlias("EstiBranchComCode") 
    private String  estiBranchComCode="";	//核价员所属中支代码

	@XStreamAlias("EstiBranchComName") 
    private String  estiBranchComName="";	//核价员所属中支名称

	@XStreamAlias("EstiHandlerCode") 
    private String  estiHandlerCode="";	//核价员代码

	@XStreamAlias("EstiHandlerName") 
    private String  estiHandlerName="";	//核价员名称

	@XStreamAlias("EvalRemark") 
    private String  evalRemark="";	//定损单备注

	public String getLossNo() {
		return lossNo;
	}

	public void setLossNo(String lossNo) {
		this.lossNo = lossNo;
	}

	public String getReportCode() {
		return reportCode;
	}

	public void setReportCode(String reportCode) {
		this.reportCode = reportCode;
	}

	public String getDmgVhclId() {
		return dmgVhclId;
	}

	public void setDmgVhclId(String dmgVhclId) {
		this.dmgVhclId = dmgVhclId;
	}

	public String getEstiComCode() {
		return estiComCode;
	}

	public void setEstiComCode(String estiComCode) {
		this.estiComCode = estiComCode;
	}

	public String getEstiCompany() {
		return estiCompany;
	}

	public void setEstiCompany(String estiCompany) {
		this.estiCompany = estiCompany;
	}

	public String getEstiBranchComCode() {
		return estiBranchComCode;
	}

	public void setEstiBranchComCode(String estiBranchComCode) {
		this.estiBranchComCode = estiBranchComCode;
	}

	public String getEstiBranchComName() {
		return estiBranchComName;
	}

	public void setEstiBranchComName(String estiBranchComName) {
		this.estiBranchComName = estiBranchComName;
	}

	public String getEstiHandlerCode() {
		return estiHandlerCode;
	}

	public void setEstiHandlerCode(String estiHandlerCode) {
		this.estiHandlerCode = estiHandlerCode;
	}

	public String getEstiHandlerName() {
		return estiHandlerName;
	}

	public void setEstiHandlerName(String estiHandlerName) {
		this.estiHandlerName = estiHandlerName;
	}

	public String getEvalRemark() {
		return evalRemark;
	}

	public void setEvalRemark(String evalRemark) {
		this.evalRemark = evalRemark;
	}

	

}
