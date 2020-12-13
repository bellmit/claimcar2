package ins.sino.claimcar.claimjy;

import ins.framework.exception.BusinessException;
import ins.platform.common.util.CodeTranUtil;
import ins.platform.utils.DateUtils;
import ins.platform.utils.XstreamFactory;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.claimjy.service.ClaimToJyService;
import ins.sino.claimcar.claimjy.util.JyHttpUtil;
import ins.sino.claimcar.claimjy.vo.JyReqHeadVo;
import ins.sino.claimcar.claimjy.vo.price.JyPriceReqBody;
import ins.sino.claimcar.claimjy.vo.price.JyPriceReqEvalLossInfo;
import ins.sino.claimcar.claimjy.vo.price.JyPriceReqInfo;
import ins.sino.claimcar.claimjy.vo.price.JyPriceReqVo;
import ins.sino.claimcar.claimjy.vo.price.JyPriceResVo;
import ins.sino.claimcar.claimjy.vo.vloss.JyVLossReqBody;
import ins.sino.claimcar.claimjy.vo.vloss.JyVLossReqEvalLossInfo;
import ins.sino.claimcar.claimjy.vo.vloss.JyVLossReqVo;
import ins.sino.claimcar.losscar.service.DeflossService;
import ins.sino.claimcar.losscar.vo.ClaimFittingVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarMainVo;
import ins.sino.claimcar.losscar.vo.PrpLJingYouLogVo;
import ins.sino.claimcar.losscar.vo.SendVo;
import ins.sino.claimcar.trafficplatform.service.EarlyWarnService;

import java.util.Date;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.SpringProperties;
import org.springframework.util.StringUtils;

import com.alibaba.dubbo.config.annotation.Service;
@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"},timeout = 1000000)
@Path("claimToJyService")
public class ClaimToJyServiceImpl implements ClaimToJyService {

	private static Logger logger = LoggerFactory.getLogger(ClaimToJyServiceImpl.class);
	
	@Autowired
	DeflossService deflossService;
	@Autowired
	EarlyWarnService earlyWarnService;
	
	@Override
	public String priceToJy(ClaimFittingVo claimFittingVo,SysUserVo userVo) throws Exception{
		JyPriceReqVo reqVo = new JyPriceReqVo();
		JyReqHeadVo head = new JyReqHeadVo();
		JyPriceReqBody body = new JyPriceReqBody();
		JyPriceReqInfo info = new JyPriceReqInfo();
		JyPriceReqEvalLossInfo lossInfo = new JyPriceReqEvalLossInfo();
		Date date = new Date();
		
		this.initRealUrl(claimFittingVo, claimFittingVo.getOperateType());
		PrpLDlossCarMainVo lossCarMainVo = deflossService.findDeflossByPk(Long.parseLong(claimFittingVo.getLossCarId()));
		claimFittingVo.setRegistNo(lossCarMainVo.getRegistNo());
		String userCode = SpringProperties.getProperty("JY_USER");
		String passWord = SpringProperties.getProperty("JY_PAW");
		String branchComCode = userVo.getComCode();
		String comCode = "";
		if(branchComCode.startsWith("00")){
			comCode = branchComCode.substring(0,4)+"0000";
		}else{
			comCode = branchComCode.substring(0,2)+"000000";
		}
		
		head.setUserCode(userCode);
		head.setPassWord(passWord);
		head.setRequestSourceCode("DHIC");
		head.setRequestSourceName("鼎和");
		head.setRequestType("003");
		head.setOperatingTime(DateUtils.dateToStr(date, DateUtils.YToSec));
		reqVo.setHead(head);
		
		info.setAddonFlag("2".equals(lossCarMainVo.getDeflossSourceFlag()) ? "1":"0");
		info.setReturnURL(claimFittingVo.getReturnURL());
		info.setRefreshURL(claimFittingVo.getRefreshURL());
		body.setInfo(info);
		
		lossInfo.setLossNo(lossCarMainVo.getId().toString());
		lossInfo.setReportCode(lossCarMainVo.getRegistNo());
		lossInfo.setDmgVhclId(lossCarMainVo.getId().toString());
		lossInfo.setEstiComCode(comCode);
		lossInfo.setEstiCompany(CodeTranUtil.transCode("ComCodeFull",comCode));
		lossInfo.setEstiBranchComCode(branchComCode);
		lossInfo.setEstiBranchComName(CodeTranUtil.transCode("ComCodeFull",branchComCode));
		lossInfo.setEstiHandlerCode(userVo.getUserCode());
		lossInfo.setEstiHandlerName(userVo.getUserName());
		body.setLossInfo(lossInfo);
		reqVo.setBody(body);
		
		// 发送报文
		String reqXML = XstreamFactory.objToXml(reqVo);
		String resXML = "";
		String url = SpringProperties.getProperty("JY2_VPRICE");
		logger.info("核价请求精友发送报文---------------------------"+reqXML);
		claimFittingVo.setRequestXml(reqXML);
		resXML = JyHttpUtil.sendData(reqXML,url,15);
		claimFittingVo.setResponseXml(resXML);
		logger.info("核价请求精友返回报文---------------------------"+resXML);
		
		if(StringUtils.isEmpty(resXML)){
			saveJingYouLog(claimFittingVo, "send", "");
			throw new BusinessException("提交配件系统","配件系统没有响应,请联系系统管理员!");
		}
		
		JyPriceResVo resVo = XstreamFactory.xmlToObj(resXML, JyPriceResVo.class);
		String newUrl = resVo.getBody().getUrl();
		saveJingYouLog(claimFittingVo, "send", newUrl);
		
		return newUrl;
	}
	
	@Override
	public String vlossToJy(ClaimFittingVo claimFittingVo,SysUserVo userVo) throws Exception{
		JyVLossReqVo reqVo = new JyVLossReqVo();
		JyReqHeadVo head = new JyReqHeadVo();
		JyVLossReqBody body = new JyVLossReqBody();
		JyPriceReqInfo info = new JyPriceReqInfo();
		JyVLossReqEvalLossInfo lossInfo = new JyVLossReqEvalLossInfo();
		Date date = new Date();
		
		this.initRealUrl(claimFittingVo, claimFittingVo.getOperateType());
		PrpLDlossCarMainVo lossCarMainVo = deflossService.findDeflossByPk(Long.parseLong(claimFittingVo.getLossCarId()));
		claimFittingVo.setRegistNo(lossCarMainVo.getRegistNo());
		String userCode = SpringProperties.getProperty("JY_USER");
		String passWord = SpringProperties.getProperty("JY_PAW");
		String branchComCode = userVo.getComCode();
		String comCode = "";
		if(branchComCode.startsWith("00")){
			comCode = branchComCode.substring(0,4)+"0000";
		}else{
			comCode = branchComCode.substring(0,2)+"000000";
		}
		
		head.setUserCode(userCode);
		head.setPassWord(passWord);
		head.setRequestSourceCode("DHIC");
		head.setRequestSourceName("鼎和");
		head.setRequestType("005");
		head.setOperatingTime(DateUtils.dateToStr(date, DateUtils.YToSec));
		reqVo.setHead(head);
		
		info.setAddonFlag("2".equals(lossCarMainVo.getDeflossSourceFlag()) ? "1":"0");
		info.setReturnURL(claimFittingVo.getReturnURL());
		info.setRefreshURL(claimFittingVo.getRefreshURL());
		body.setInfo(info);
		
		lossInfo.setLossNo(lossCarMainVo.getId().toString());
		lossInfo.setReportCode(lossCarMainVo.getRegistNo());
		lossInfo.setDmgVhclId(lossCarMainVo.getId().toString());
		lossInfo.setApprComCode(comCode);
		lossInfo.setApprCompany(CodeTranUtil.transCode("ComCodeFull",comCode));
		lossInfo.setApprBranchComCode(branchComCode);
		lossInfo.setApprBranchComName(CodeTranUtil.transCode("ComCodeFull",branchComCode));
		lossInfo.setApprHandlerCode(userVo.getUserCode());
		lossInfo.setApprHandlerName(userVo.getUserName());
		body.setLossInfo(lossInfo);
		reqVo.setBody(body);
		
		// 发送报文
		String reqXML = XstreamFactory.objToXml(reqVo);
		String resXML = "";
		String url = SpringProperties.getProperty("JY2_VLOSS");
		logger.info("核损请求精友发送报文---------------------------"+reqXML);
		claimFittingVo.setRequestXml(reqXML);
		resXML = JyHttpUtil.sendData(reqXML,url,15);
		claimFittingVo.setResponseXml(resXML);
		logger.info("核损请求精友返回报文---------------------------"+resXML);
		
		if(StringUtils.isEmpty(resXML)){
			saveJingYouLog(claimFittingVo, "send", "");
			throw new BusinessException("提交配件系统","配件系统没有响应,请联系系统管理员!");
		}
		
		JyPriceResVo resVo = XstreamFactory.xmlToObj(resXML, JyPriceResVo.class);
		String newUrl = resVo.getBody().getUrl();
		saveJingYouLog(claimFittingVo, "send", newUrl);
		
		return newUrl;
	}
	
	
	/**
	 * 对封装地址进行内外网封装
	 **/
	public void initRealUrl(ClaimFittingVo claimFittingVo,String operateType){
		SendVo sendVo = claimFittingVo.getSendVo();
		String claimUrl = "http://" + sendVo.getServerName()
			+ ":"+ sendVo.getServerPort()
			+ sendVo.getContextPath();
		// 暂时这么取 理赔内网地址 ，之后配置到fram的app文件中
		String claimUrl_In = SpringProperties.getProperty("CLAIM_URL_IN");
		if("verifyPrice".equals(operateType)){
			claimUrl_In = claimUrl_In + "/jyPriceReceive";
			claimUrl = claimUrl+"/reloadFittings?operateType=verifyPrice";
		}else{
			claimUrl_In = claimUrl_In + "/jyVLossReceive";
			claimUrl = claimUrl+"/reloadFittings?operateType=verifyLoss";
		}
		claimFittingVo.setReturnURL(claimUrl_In);
		claimFittingVo.setRefreshURL(claimUrl);
		
	}
	
	/**
	 * 保存精友交互日志
	 */
	public void saveJingYouLog(ClaimFittingVo claimFittingVo,String logType,String reUrl) {
		String sendXml = claimFittingVo.getRequestXml();
		String reXml = claimFittingVo.getResponseXml();
		if( sendXml!=null && sendXml.length()>=3000){
			sendXml=sendXml.substring(0,2998);
		}
		if( reXml!=null && reXml.length()>=3000){
			reXml=reXml.substring(0,2998);
		}
		if( reUrl!=null && reUrl.length()>=3000){
			reUrl=reUrl.substring(0,2998);
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
