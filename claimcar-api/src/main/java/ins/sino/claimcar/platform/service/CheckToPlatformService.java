package ins.sino.claimcar.platform.service;

import ins.sino.claimcar.carplatform.vo.CiClaimPlatformLogVo;
import ins.sino.claimcar.check.vo.history.CarInfoHistoryResBodyVo;

/**
 * 查勘送平台接口服务
 * @author ★LuWei
 * @CreateTime 2016年6月28日
 */
public interface CheckToPlatformService {

	/**
	 * 查勘登记 交强送平台
	 * @param registNo
	 * @throws Exception
	 */
	public void sendCheckToPlatformCI(String registNo) throws Exception;
	
	/**
	 * 查勘登记 交强送平台
	 * @param registNo
	 * @throws Exception
	 */
	public void sendCheckToPlatformBI(String registNo) throws Exception;
	
	/**
	 * 查勘平台交互补传
	 * @param CiClaimPlatformLogVo
	 * @throws Exception 
	 */
	public void sendToPaltform(CiClaimPlatformLogVo logVo) throws Exception;
	
	/**
	 * <pre>查勘三者车历史出险信息</pre>
	 * @param carId
	 * @param comCode
	 * @return
	 * @modified:
	 */
	public CarInfoHistoryResBodyVo thirdCarInfoQuery(Long carId, String comCode);

	public void sendCheckSubmitToPlatform(String registNo) throws Exception;
}
