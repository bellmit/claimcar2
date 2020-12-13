package ins.sino.claimcar.moblie.commonmark.caseDetails.vo;


import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("CERTAINSTASKINFO")
public class CertainsTaskInfosVo implements Serializable{

	private static final long serialVersionUID = 8423652723600188374L;
    @XStreamAlias("REGISTNO")
    private String registNo;  //报案号
    
    @XStreamAlias("EXIGENCEGREE")
    private String exigenceGree;  //案件紧急程度
    
	@XStreamAlias("TASKCARLIST")
    private List<CertainsTaskInfoVo> certainsTaskInfoVo;

    
    public String getRegistNo() {
        return registNo;
    }

    
    public void setRegistNo(String registNo) {
        this.registNo = registNo;
    }

    
    public String getExigenceGree() {
        return exigenceGree;
    }

    
    public void setExigenceGree(String exigenceGree) {
        this.exigenceGree = exigenceGree;
    }

    
    public List<CertainsTaskInfoVo> getCertainsTaskInfoVo() {
        return certainsTaskInfoVo;
    }

    
    public void setCertainsTaskInfoVo(List<CertainsTaskInfoVo> certainsTaskInfoVo) {
        this.certainsTaskInfoVo = certainsTaskInfoVo;
    }
	
	
	
}
