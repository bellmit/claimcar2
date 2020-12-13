package ins.sino.claimcar.manager.vo;

/**
 * Custom VO class of PO PrpLUserHoliday
 */ 
public class PrpLUserHolidayVo extends PrpLUserHolidayVoBase implements java.io.Serializable {
	private static final long serialVersionUID = 1L; 

	private PrpLUserHolidayGradeVo prpLUserHolidayGrade=null;
	public PrpLUserHolidayGradeVo getPrpLUserHolidayGrade() {
		if(prpLUserHolidayGrade==null&&!getPrpLUserHolidayGrades().isEmpty()){
			prpLUserHolidayGrade=getPrpLUserHolidayGrades().get(0);
			
		}
		return prpLUserHolidayGrade;
	}
	public void setPrpLUserHolidayGrade(PrpLUserHolidayGradeVo prpLUserHolidayGrade) {
		this.prpLUserHolidayGrade = prpLUserHolidayGrade;
	}
}
