/******************************************************************************
 * CREATETIME : 2016年9月23日 下午6:09:20
 ******************************************************************************/
package ins.sino.claimcar.regist.service;

import ins.sino.claimcar.mobilecheck.vo.CallPhoneReqVo;
import ins.sino.claimcar.mobilecheck.vo.CallPhoneResVo;
import ins.sino.claimcar.schedule.vo.PrpLScheduleTaskVo;

/**
 * <pre></pre>
 * @author ★Luwei
 */
public interface FounderCustomService {

	/**
	 * <pre>车险报案接口服务</pre>
	 * @param registNo
	 * @throws Exception
	 * @modified:
	 * ☆Luwei(2016年8月4日 下午6:33:50): <br>
	 */
	public abstract void carRegistToFounder(String registNo) throws Exception;

	/**
	 * <pre>调度信息更新推送至客服系统接口</pre>
	 * @param taskVo,
	 * @param scheduleType-调度类型(1-调度查勘，2-调度定损，3-调度改派)
	 * @throws Exception
	 * @modified:
	 * ☆Luwei(2016年8月4日 下午6:33:33): <br>
	 */
	public abstract void scheduleInfoToFounder(PrpLScheduleTaskVo taskVo,String scheduleType) throws Exception;

	/**
	 * 报案注销推送至客服系统接口
	 * @param registNo
	 * @throws Exception
	 * @modified:
	 * ☆Luwei(2016年8月4日 下午6:33:15): <br>
	 */
	public abstract void registCancelToFounder(String registNo) throws Exception;

	/**
	 * <pre>保单关联推送至客服系统接口</pre>
	 * @param registNo,conPolicyNo-关联，取消的保单号
	 * @param isConnect - 1=关联；0=取消
	 * @throws Exception
	 * @modified:
	 * ☆Luwei(2016年8月4日 下午6:34:15): <br>
	 */
	public abstract void PolicyRelationToFounder(String registNo,String conPolicyNo,String isConnect) throws Exception;

	/**
	 * <pre>调度注销定损任务</pre>
	 * @param taskVo
	 * @throws Exception 
	 * @modified:
	 * ☆Luwei(2016年8月4日 下午6:37:22): <br>
	 */
	public abstract void cancelDflossTaskToFounder(PrpLScheduleTaskVo taskVo) throws Exception;
	/**
	 * 报案返回报案电话
	 * @return
	 * @throws Exception
	 * @modified:
	 * ☆yzyuan(2017年2月21日 下午2:40:22): <br>
	 */
	public abstract String carRegistPhoneToFounder()throws Exception;
	//查勘员号码更新
	public abstract CallPhoneResVo sendCallPhoneToFounder(CallPhoneReqVo callPhoneReqVo)throws Exception;
	
	/**
	 * 公估车险案件注销
	 * <pre></pre>
	 * @param registNo
	 * @return
	 * @throws Exception
	 * @modified:
	 * ☆zhujunde(2017年12月29日 下午2:41:43): <br>
	 */
	public abstract String registCancelCtAtnToFounder(String registNo)throws Exception;

}
