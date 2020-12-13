package ins.sino.claimcar.genilex.vo.check;

import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Survery")
public class Survery implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@XStreamAlias("ReportNo") 
	private String  reportNo;
	
	@XStreamAlias("SelfSurveyType") 
	private String  selfSurveyType;
	
	@XStreamAlias("isCompleteSurvery") 
	private String  isCompleteSurvery;
	
	@XStreamAlias("SurveryVehicles") 
	private List<SurveryVehicle>  surveryVehicles;
	
	@XStreamAlias("SurveryProtects") 
	private List<SurveryProtect>  surveryProtects;
	
	@XStreamAlias("SurveryPersons") 
	private List<SurveryPerson>  surveryPersons;

	public String getReportNo() {
		return reportNo;
	}

	public void setReportNo(String reportNo) {
		this.reportNo = reportNo;
	}

	public String getSelfSurveyType() {
		return selfSurveyType;
	}

	public void setSelfSurveyType(String selfSurveyType) {
		this.selfSurveyType = selfSurveyType;
	}

	public String getIsCompleteSurvery() {
		return isCompleteSurvery;
	}

	public void setIsCompleteSurvery(String isCompleteSurvery) {
		this.isCompleteSurvery = isCompleteSurvery;
	}

	public List<SurveryVehicle> getSurveryVehicles() {
		return surveryVehicles;
	}

	public void setSurveryVehicles(List<SurveryVehicle> surveryVehicles) {
		this.surveryVehicles = surveryVehicles;
	}

	public List<SurveryProtect> getSurveryProtects() {
		return surveryProtects;
	}

	public void setSurveryProtects(List<SurveryProtect> surveryProtects) {
		this.surveryProtects = surveryProtects;
	}

	public List<SurveryPerson> getSurveryPersons() {
		return surveryPersons;
	}

	public void setSurveryPersons(List<SurveryPerson> surveryPersons) {
		this.surveryPersons = surveryPersons;
	}
	
	
}
