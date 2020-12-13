package ins.sino.claimcar.other.service;

public interface HolidayService {
	/**
	 * 根据userCode判断该员工是否正在休假
	 * @param userCode
	 * @return
	 */
	public Boolean existHoliday(String userCode);

}
