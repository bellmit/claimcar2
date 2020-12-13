package ins.sino.claimcar.selfHelpClaimCar.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
/**
 * 
 * 自助案件业务员受理接口
 * @author ★zhujunde
 */
@XStreamAlias("CASECARSTATE")
public class CaseCarState  implements Serializable{
	
    private static final long serialVersionUID = 1L;

    @XStreamAlias("INSCASENO")
    private String inscaseNo;//保险报案号
    @XStreamAlias("TYPE")
    private String types;//类型
    @XStreamAlias("NEXTHANDLERCODE")
    private String nextHandlerCode;//处理人员编码
    @XStreamAlias("NEXTHANDLERNAME")
    private String nextHandlerName;//处理人员名称
    @XStreamAlias("SCHEDULEOBJECTID")
    private String scheduleObjectId;//处理人员机构编码
    @XStreamAlias("SCHEDULEOBJECTNAME")
    private String scheduleObjectName;//处理人员机构名称
    @XStreamAlias("OPERATIONTIME")
    private String operAtionTime;//受理时间
	public String getInscaseNo() {
		return inscaseNo;
	}
	public void setInscaseNo(String inscaseNo) {
		this.inscaseNo = inscaseNo;
	}
	public String getTypes() {
		return types;
	}
	public void setTypes(String types) {
		this.types = types;
	}
	public String getNextHandlerCode() {
		return nextHandlerCode;
	}
	public void setNextHandlerCode(String nextHandlerCode) {
		this.nextHandlerCode = nextHandlerCode;
	}
	public String getNextHandlerName() {
		return nextHandlerName;
	}
	public void setNextHandlerName(String nextHandlerName) {
		this.nextHandlerName = nextHandlerName;
	}
	
	public String getScheduleObjectId() {
		return scheduleObjectId;
	}
	public void setScheduleObjectId(String scheduleObjectId) {
		this.scheduleObjectId = scheduleObjectId;
	}
	public String getScheduleObjectName() {
		return scheduleObjectName;
	}
	public void setScheduleObjectName(String scheduleObjectName) {
		this.scheduleObjectName = scheduleObjectName;
	}
	public String getOperAtionTime() {
		return operAtionTime;
	}
	public void setOperAtionTime(String operAtionTime) {
		this.operAtionTime = operAtionTime;
	}
 
    
}
