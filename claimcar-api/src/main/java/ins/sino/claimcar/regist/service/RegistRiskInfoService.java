package ins.sino.claimcar.regist.service;

public interface RegistRiskInfoService {

	/**
	 * 更新代为求偿时，回写风险提示信息表，代为时flag取1，非代为取0,handler是处理人.
	 * @param registNo
	 * @param flag
	 */
	public void writePrpLRegistRiskInfo(String registNo,String flag,String handler);
	/**
	 * 车辆定损时回写
	 * @param registNo
	 * @param handler
	 */
	public void writeRiskInfoByLossCar(String registNo,String handler);
}
