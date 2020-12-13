package ins.sino.claimcar.moblie.commonmark.caseDetails.vo;

import ins.sino.claimcar.moblie.lossPerson.vo.FeeInfoVos;
import ins.sino.claimcar.moblie.lossPerson.vo.PersonInfoVos;

import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
/**
 * 案件详情查询（快赔请求理赔）
 * @author zjd
 *
 */
@XStreamAlias("BODY")
public class CaseDetailsInfoResBodyVo implements Serializable{
	
	/**  */
	private static final long serialVersionUID = 1L;

    @XStreamAlias("CHECKTASKINFO")
    private CheckInfoInitVo checkInfoInitVo;
    
	
    @XStreamAlias("CERTAINSTASKINFO")
    private CertainsTaskInfosVo certainsTaskInfosVo;
    
    @XStreamAlias("CASEINFO")
    private CaseInfoVo caseInfoVo;
     
    @XStreamAlias("COMPENSATELIST")
    private List<CompensateInfo> compensateInfos;
    
    @XStreamAlias("PERSIONINFO")
    private PersionInfo persionInfo;
    
	@XStreamAlias("PERSIONINFOLIST")
	private List<PersonInfoVos> personInfoVo;
	
	@XStreamAlias("FEELIST")
	private List<FeeInfoVos> feeInfoVo;

    
    public PersionInfo getPersionInfo() {
		return persionInfo;
	}


	public void setPersionInfo(PersionInfo persionInfo) {
		this.persionInfo = persionInfo;
	}


	public List<PersonInfoVos> getPersonInfoVo() {
		return personInfoVo;
	}


	public void setPersonInfoVo(List<PersonInfoVos> personInfoVo) {
		this.personInfoVo = personInfoVo;
	}


	public List<FeeInfoVos> getFeeInfoVo() {
		return feeInfoVo;
	}


	public void setFeeInfoVo(List<FeeInfoVos> feeInfoVo) {
		this.feeInfoVo = feeInfoVo;
	}


	public CheckInfoInitVo getCheckInfoInitVo() {
        return checkInfoInitVo;
    }

    
    public void setCheckInfoInitVo(CheckInfoInitVo checkInfoInitVo) {
        this.checkInfoInitVo = checkInfoInitVo;
    }


    
    public CertainsTaskInfosVo getCertainsTaskInfosVo() {
        return certainsTaskInfosVo;
    }


    
    public void setCertainsTaskInfosVo(CertainsTaskInfosVo certainsTaskInfosVo) {
        this.certainsTaskInfosVo = certainsTaskInfosVo;
    }


	public CaseInfoVo getCaseInfoVo() {
		return caseInfoVo;
	}


	public void setCaseInfoVo(CaseInfoVo caseInfoVo) {
		this.caseInfoVo = caseInfoVo;
	}


	public List<CompensateInfo> getCompensateInfos() {
		return compensateInfos;
	}


	public void setCompensateInfos(List<CompensateInfo> compensateInfos) {
		this.compensateInfos = compensateInfos;
	}
     
    
}
