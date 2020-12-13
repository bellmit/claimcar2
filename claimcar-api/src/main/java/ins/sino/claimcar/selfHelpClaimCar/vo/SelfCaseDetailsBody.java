package ins.sino.claimcar.selfHelpClaimCar.vo;


import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 *  自助理赔案件详情查询（快赔请求理赔）
 * @author zhujunde
 *
 */
@XStreamAlias("BODY")
public class SelfCaseDetailsBody  implements Serializable{

    private static final long serialVersionUID = 1L;

    @XStreamAlias("CASEINFO") 
    private CaseInfo caseInfo;

	public CaseInfo getCaseInfo() {
		return caseInfo;
	}

	public void setCaseInfo(CaseInfo caseInfo) {
		this.caseInfo = caseInfo;
	}
    
   
}
