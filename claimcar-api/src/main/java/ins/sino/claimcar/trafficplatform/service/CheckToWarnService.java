package ins.sino.claimcar.trafficplatform.service;


/**
 * 查勘送预警系统服务类
 * @author wurh
 *
 */
public interface CheckToWarnService {

	/**
	 * 查勘送预警系统
	 * @param registNo
	 */
	public void checkToWarn(Long checkTaskId,Long flowTaskId,String policyNo) throws Exception;
	
}
