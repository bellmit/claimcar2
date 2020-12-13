package ins.sino.claimcar.schedule.vo;

import ins.sino.claimcar.mobilecheck.vo.HandleScheduleReqScheduleDOrG;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;

import java.util.List;


/**
 * Custom VO class of PO PrpLRegist
 */ 
public class PrpDScheduleDOrGMainVo implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private HandleScheduleReqScheduleDOrG scheduleDOrG;
	private List<PrpLCMainVo> prpLCMainVoList;
	private PrpLRegistVo prpLRegistVo;
	private PrpLScheduleTaskVo prpLScheduleTaskVo;
	private String schType;
	private String checkAreaCode;
	private String lngXlatY;
	public HandleScheduleReqScheduleDOrG getScheduleDOrG() {
		return scheduleDOrG;
	}
	public void setScheduleDOrG(HandleScheduleReqScheduleDOrG scheduleDOrG) {
		this.scheduleDOrG = scheduleDOrG;
	}
	public List<PrpLCMainVo> getPrpLCMainVoList() {
		return prpLCMainVoList;
	}
	public void setPrpLCMainVoList(List<PrpLCMainVo> prpLCMainVoList) {
		this.prpLCMainVoList = prpLCMainVoList;
	}
	public PrpLRegistVo getPrpLRegistVo() {
		return prpLRegistVo;
	}
	public void setPrpLRegistVo(PrpLRegistVo prpLRegistVo) {
		this.prpLRegistVo = prpLRegistVo;
	}
	public PrpLScheduleTaskVo getPrpLScheduleTaskVo() {
		return prpLScheduleTaskVo;
	}
	public void setPrpLScheduleTaskVo(PrpLScheduleTaskVo prpLScheduleTaskVo) {
		this.prpLScheduleTaskVo = prpLScheduleTaskVo;
	}
	public String getSchType() {
		return schType;
	}
	public void setSchType(String schType) {
		this.schType = schType;
	}
	public String getCheckAreaCode() {
		return checkAreaCode;
	}
	public void setCheckAreaCode(String checkAreaCode) {
		this.checkAreaCode = checkAreaCode;
	}
	public String getLngXlatY() {
		return lngXlatY;
	}
	public void setLngXlatY(String lngXlatY) {
		this.lngXlatY = lngXlatY;
	}
	
	
}
