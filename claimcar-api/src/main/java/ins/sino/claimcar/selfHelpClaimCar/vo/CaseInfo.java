package ins.sino.claimcar.selfHelpClaimCar.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 自助理赔案件详情查询（快赔请求理赔）
 * @author zhujunde
 *
 */
@XStreamAlias("BODY")
public class CaseInfo  implements Serializable{

    private static final long serialVersionUID = 1L;

    @XStreamAlias("REGISTNO") 
    private String registNo;//报案号
    @XStreamAlias("DAMAGETIME") 
    private String DAMAGETIME;//出险时间 格式：YYYY-MM-DD HH:MM:SS(24小时)
    @XStreamAlias("CASEADDRESS") 
    private String CASEADDRESS;
    @XStreamAlias("CASECARNO") 
    private String casecarNo;
	public String getRegistNo() {
		return registNo;
	}
	public void setRegistNo(String registNo) {
		this.registNo = registNo;
	}

	public String getCasecarNo() {
		return casecarNo;
	}
	public void setCasecarNo(String casecarNo) {
		this.casecarNo = casecarNo;
	}
    
    
}
