package ins.sino.claimcar.moblie.commonmark.service;

import ins.framework.lang.Springs;
import ins.platform.utils.ClaimBaseCoder;
import ins.sino.claimcar.carinterface.vo.ClaimInterfaceLogVo;
import ins.sino.claimcar.common.claimtext.service.ClaimTextService;
import ins.sino.claimcar.common.claimtext.vo.PrpLClaimTextVo;
import ins.sino.claimcar.mobileCheckCommon.vo.MobileCheckBody;
import ins.sino.claimcar.mobileCheckCommon.vo.MobileCheckHead;
import ins.sino.claimcar.mobileCheckCommon.vo.MobileCheckRequest;
import ins.sino.claimcar.mobileCheckCommon.vo.MobileCheckResponseHead;
import ins.sino.claimcar.moblie.commonmark.vo.MCCommonRemarksBody;
import ins.sino.claimcar.moblie.commonmark.vo.MCCommonRemarksPacket;
import ins.sino.claimcar.moblie.commonmark.vo.MCCommonRemarksRemarksOptionInfo;
import ins.sino.claimcar.moblie.logUtil.QuickClaimLogUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.common.utils.Assert;
import com.sinosoft.api.service.ServiceInterface;

public class BigCasePredictServiceImpl implements ServiceInterface{

	@Autowired
	private ClaimTextService claimTextService;
	private static Logger logger = LoggerFactory.getLogger(BigCasePredictServiceImpl.class);
	@Override
	public Object service(String arg0, Object arg1) {
		init();
		 MCCommonRemarksPacket resPacket = new MCCommonRemarksPacket();
		 MobileCheckResponseHead resHead = new MobileCheckResponseHead();
		 String registNo = "";
		try {
			String reqXml = ClaimBaseCoder.objToXmlUtf(arg1);
			logger.info("===================大案预报信息查询接口（快赔请求理赔）请求报文： "+reqXml);
			MobileCheckRequest reqPacket =(MobileCheckRequest)arg1;
			Assert.notNull(reqPacket, " 请求信息为空  ");
			MobileCheckHead head = reqPacket.getHead();
			if (!"005".equals(head.getRequestType())|| !"claim_user".equals(head.getUser())|| !"claim_psd".equals(head.getPassWord())) {
				throw new IllegalArgumentException(" 请求头参数错误  ");
			}
			MobileCheckBody body = reqPacket.getBody();
			 registNo = body.getRegistNo();
			Assert.notNull(registNo, " 报案号为空  ");
			
			 resHead.setResponseType("005");
			 resHead.setResponseCode("YES");
			 resHead.setResponseMessage("Success");
			 List<PrpLClaimTextVo> claimTextList = claimTextService.findClaimTextList(null, registNo, null);
	    	 List<MCCommonRemarksRemarksOptionInfo> remarkOptionList = new ArrayList<MCCommonRemarksRemarksOptionInfo>();
	    	 if(claimTextList != null && claimTextList.size() > 0){
	    		 for(PrpLClaimTextVo claimText : claimTextList){
	    			 if(!claimText.getBigNode().contains("Big")){
	    				 continue;
	    			 }
	    			 MCCommonRemarksRemarksOptionInfo optionInfo = new MCCommonRemarksRemarksOptionInfo();
	    			 optionInfo.setRole(claimText.getNodeCode());
	    			 optionInfo.setOperateCode(claimText.getOperatorCode());
	    			 optionInfo.setComCode(claimText.getComCode());
	    			 SimpleDateFormat sf = new SimpleDateFormat("YYYY-MM-DD hh:mm:ss ");
	    			 optionInfo.setInputDate(sf.format(claimText.getInputTime()));
	    			 optionInfo.setOptionDesc(claimText.getDescription());
	    			 optionInfo.setAuditState(claimText.getStatus());
	    			 remarkOptionList.add(optionInfo);
	    		 }
	    	 }
	    	 
	    	 MCCommonRemarksBody resBody = new MCCommonRemarksBody();
	    	 resBody.setRegistNo(registNo);
	    	 resBody.setRemarkOptionList(remarkOptionList);
	    	 
	    	 
	    	 resPacket.setHead(resHead);
	    	 resPacket.setBody(resBody);
	    	 
			
		} catch (Exception e) {
			resHead.setResponseType("005");
			resHead.setResponseCode("NO");
			resHead.setResponseMessage(e.getMessage());
			resPacket.setHead(resHead);
			logger.info("大案预报信息查询接口（快赔请求理赔）报错信息： ");
			 e.printStackTrace();
		}
		
		String resXml = ClaimBaseCoder.objToXmlUtf(resPacket);
		logger.info("===================大案预报信息查询接口（快赔请求理赔）返回报文： "+resXml);
		return resPacket;
	}
	
	private void init(){
		if(claimTextService == null){
			claimTextService = (ClaimTextService)Springs.getBean(ClaimTextService.class);
		}
	}

}
