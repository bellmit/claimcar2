package ins.sino.claimcar.other.service;

import ins.sino.claimcar.other.vo.SendMsgParamVo;
import ins.sino.claimcar.other.vo.SysMsgModelVo;

import java.util.Date;
import java.util.List;

public interface SendMsgService {

	/**
	 * 根据模板类型,发送节点,案件类型机构查询短信模板Vo
	 * @param modelType
	 * @param systemNode
	 * @param caseType
	 * @return
	 */
	public SysMsgModelVo findmsgModelVo(String modelType,String systemNode,String comCode,String caseType);
	
	/**
	 * 根据归属机构查询需要发送的员工手机号码，flag等于1是正常案件，2是自助理赔
	 * @param comCode,flag
	 * @return
	 */
	public List<String> getMobile(String comCode,String flag);
	
	public String getMessage(String message,SendMsgParamVo msgParamVo);
	
	public String getLossPart(String code);
	
	/**
	 * 根据发送时间类型返回发送时间
	 * @param timeType
	 * @return
	 */
	public Date getSendTime(String timeType);
	
	/**
	 * 根据报案号获取承保险种
	 * @param registNo
	 * @return
	 */
	public String getKindName(String registNo);
}
