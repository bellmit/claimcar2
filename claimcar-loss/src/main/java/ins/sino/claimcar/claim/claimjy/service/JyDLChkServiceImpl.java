package ins.sino.claimcar.claim.claimjy.service;

import ins.platform.common.util.CodeTranUtil;
import ins.platform.utils.ClaimBaseCoder;
import ins.platform.utils.DateUtils;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.claimjy.service.JyDLChkService;
import ins.sino.claimcar.claimjy.util.JyHttpUtil;
import ins.sino.claimcar.claimjy.vo.JyDLChkReqBodyEvalLossInfoVo;
import ins.sino.claimcar.claimjy.vo.JyDLChkReqBodyReqInfoVo;
import ins.sino.claimcar.claimjy.vo.JyDLChkReqBodyVo;
import ins.sino.claimcar.claimjy.vo.JyDLChkReqVo;
import ins.sino.claimcar.claimjy.vo.JyReqHeadVo;
import ins.sino.claimcar.claimjy.vo.JyResVo;
import ins.sino.claimcar.losscar.service.DeflossService;
import ins.sino.claimcar.losscar.service.LossCarService;
import ins.sino.claimcar.losscar.vo.ClaimFittingVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarMainVo;
import ins.sino.claimcar.losscar.vo.PrpLJingYouLogVo;
import ins.sino.claimcar.losscar.vo.SendVo;
import ins.sino.claimcar.regist.service.PrpLCMainService;
import ins.sino.claimcar.regist.service.RegistService;

import java.util.Date;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.SpringProperties;

import com.alibaba.dubbo.config.annotation.Service;

/**
 * 精友复检接口(从理赔系统请求定损系统)
 * 
 * <pre></pre>
 * @author ★LiYi
 */
@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"})
@Path(value = "jyDLChkService")
public class JyDLChkServiceImpl implements JyDLChkService {

	private static Logger LOGGER = LoggerFactory.getLogger(JyDLChkServiceImpl.class);
	@Autowired
	private LossCarService lossCarService;
	@Autowired
	private RegistService registService;
	@Autowired
	private PrpLCMainService prpLCMainService;
	@Autowired
	DeflossService deflossService;

	@Override
	public String dLChkAskService(ClaimFittingVo claimFittingVo,SysUserVo sysUserVo) {
		JyDLChkReqVo reqVo = new JyDLChkReqVo();
		JyResVo resVo = new JyResVo();
		String url = "";
		try{
			this.initRealUrl(claimFittingVo);
			reqVo = this.orgnaizeInfo(claimFittingVo,sysUserVo);
			String xmlToSend = "";
			String xmlReturn = "";
			xmlToSend = ClaimBaseCoder.objToXml(reqVo);
			// url = "http://10.252.8.21:7001/ClaimCloudProd-app/jyClaim/SipClaimInterfaceServlet?insCode=DHIC&requestType=016";
			url = SpringProperties.getProperty("JY2_DLChk");
			LOGGER.info("复检请求接口提交send---------------------------"+xmlToSend);
			claimFittingVo.setRequestXml(xmlToSend);
			xmlReturn = JyHttpUtil.sendData(xmlToSend,url,200);
			claimFittingVo.setResponseXml(xmlReturn);
			LOGGER.info("复检请求接口接口提交return---------------------------"+xmlReturn);
			resVo = ClaimBaseCoder.xmlToObj(xmlReturn,JyResVo.class);		
		}catch(Exception e){
			LOGGER.info(resVo.getHead().getErrorMessage());
			LOGGER.error("复检请求接口异常",e);
		}
		finally{
			saveJingYouLog(claimFittingVo,"send",url);
		}
		return resVo.getBody().getUrl();
	}

	private JyDLChkReqVo orgnaizeInfo(ClaimFittingVo claimFittingVo,SysUserVo sysUserVo) {
		String dmgVhclId = claimFittingVo.getLossCarId();
		String userCode = sysUserVo.getUserCode();
		String userName = sysUserVo.getUserName();
		PrpLDlossCarMainVo lossCarMainVo = lossCarService.findLossCarMainById(Long.parseLong(dmgVhclId));
		String registNo = lossCarMainVo.getRegistNo();
		JyDLChkReqVo packet = new JyDLChkReqVo();
		JyReqHeadVo head = new JyReqHeadVo();
		JyDLChkReqBodyVo body = new JyDLChkReqBodyVo();
		JyDLChkReqBodyReqInfoVo reqInfoVo = new JyDLChkReqBodyReqInfoVo();
		JyDLChkReqBodyEvalLossInfoVo evalLossInfo = new JyDLChkReqBodyEvalLossInfoVo();
		String branchComCode = sysUserVo.getComCode();
		String comCode = "";
		if(branchComCode.startsWith("00")){
			comCode = branchComCode.substring(0,4)+"0000";
		}else{
			comCode = branchComCode.substring(0,2)+"000000";
		}
		/**
		 * HEAD封装
		 */
		head.setUserCode("jy");
		head.setPassWord("jy");
		head.setRequestSourceCode("DHIC");
		head.setRequestSourceName("鼎和财产保险股份有限公司");
		head.setRequestType("016");
		head.setOperatingTime(DateUtils.dateToStr(DateUtils.now(),DateUtils.YToSec));
		/**
		 * BODY封装 -->ReqInfoVo
		 */
		reqInfoVo.setReturnURL(claimFittingVo.getReturnURL());
		reqInfoVo.setRefreshURL(claimFittingVo.getRefreshURL());
		reqInfoVo.setAddOnFlag("");// N 追加定损标志 1=是 0=否
		/**
		 * BODY封装 -->EvalLossInfo
		 */
		evalLossInfo.setDmgVhclId(dmgVhclId);// 车损标的主键
		evalLossInfo.setLossNo(dmgVhclId);// 定损单号
		evalLossInfo.setReportCode(registNo);// 报案号
		evalLossInfo.setRecheckComCode(comCode);// 复检员所属分机构代码
		evalLossInfo.setRecheckCompany(CodeTranUtil.transCode("ComCodeFull",comCode));// 复检员所属分机构名称
		evalLossInfo.setRecheckBranchComCode(branchComCode);// 复检员所属中支代码
		evalLossInfo.setRecheckBranchComName(CodeTranUtil.transCode("ComCodeFull",branchComCode));// 复检员所属中支名称
		evalLossInfo.setRecheckHandlerCode(userCode);// 复检员代码
		evalLossInfo.setRecheckHandlerName(userName);// 复检员名称
		evalLossInfo.setEvalRemark(lossCarMainVo.getRemark());// 定损单备注

		body.setReqInfoVo(reqInfoVo);
		body.setEvalLossInfo(evalLossInfo);
		packet.setHead(head);
		packet.setBody(body);

		return packet;
	}
	
	/**
	 * 对封装地址进行内外网封装
	 **/
	public void initRealUrl(ClaimFittingVo claimFittingVo) {
		SendVo sendVo = claimFittingVo.getSendVo();
		String claimUrl = "http://"+sendVo.getServerName()+":"+sendVo.getServerPort()+sendVo.getContextPath();
		// 暂时这么取 理赔内网地址 ，之后配置到fram的app文件中
		String claimUrl_In = SpringProperties.getProperty("CLAIM_URL_IN");
		// String tempURL = "http://"+"10.0.44.52";
		claimFittingVo.setReturnURL(claimUrl_In+"/jyDLChkReceive");
		claimFittingVo.setRefreshURL(claimUrl+"/reloadFittings");

	}

	/**
	 * 保存精友交互日志
	 */
	public void saveJingYouLog(ClaimFittingVo claimFittingVo,String logType,String reUrl) {
		String sendXml = claimFittingVo.getRequestXml();
		String reXml = claimFittingVo.getResponseXml();
		if(sendXml!=null&&sendXml.length()>=3000){
			sendXml = sendXml.substring(0,2998);
		}
		if(reXml!=null&&reXml.length()>=3000){
			reXml = reXml.substring(0,2998);
		}
		if(reUrl!=null&&reUrl.length()>=3000){
			reUrl = reUrl.substring(0,2998);
		}

		PrpLJingYouLogVo logVo = new PrpLJingYouLogVo();
		logVo.setRegistno(claimFittingVo.getRegistNo());
		logVo.setUserCode(claimFittingVo.getOperatorCode());
		logVo.setLossNo(Long.parseLong(claimFittingVo.getLossCarId()));
		logVo.setOptType(claimFittingVo.getOperateType());
		logVo.setSendXml(sendXml);
		logVo.setLogType(logType);
		logVo.setReXml(reXml);
		logVo.setReUrl(reUrl);
		logVo.setCreateTime(new Date());

		deflossService.saveLog(logVo);
	}
}
