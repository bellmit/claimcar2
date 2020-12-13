package ins.sino.claimcar.moblie.commonmark.service;

import ins.framework.lang.Springs;
import ins.platform.common.util.CodeTranUtil;
import ins.platform.utils.ClaimBaseCoder;
import ins.platform.utils.DateUtils;
import ins.sino.claimcar.carinterface.vo.ClaimInterfaceLogVo;
import ins.sino.claimcar.common.claimtext.service.ClaimTextService;
import ins.sino.claimcar.common.claimtext.vo.PrpLClaimTextVo;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.service.SysMsgContentService;
import ins.sino.claimcar.manager.vo.SysMsgContentVo;
import ins.sino.claimcar.mobileCheckCommon.vo.MobileCheckBody;
import ins.sino.claimcar.mobileCheckCommon.vo.MobileCheckHead;
import ins.sino.claimcar.mobileCheckCommon.vo.MobileCheckRequest;
import ins.sino.claimcar.mobileCheckCommon.vo.MobileCheckResponseHead;
import ins.sino.claimcar.moblie.commonmark.vo.MCCommonRemarksBody;
import ins.sino.claimcar.moblie.commonmark.vo.MCCommonRemarksPacket;
import ins.sino.claimcar.moblie.commonmark.vo.MCCommonRemarksRemarkInfo;
import ins.sino.claimcar.moblie.commonmark.vo.MCCommonRemarksRemarksOptionInfo;
import ins.sino.claimcar.moblie.logUtil.QuickClaimLogUtil;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.common.utils.Assert;
import com.sinosoft.api.service.ServiceInterface;

/**
 * 
 * <pre></pre>
 * @author ★niuqiang
 */
public class CommonMarkServiceImpl implements ServiceInterface {

	@Autowired
	private SysMsgContentService sysMsgContentService;
	@Autowired
	private ClaimTextService claimTextService;
	
	private static Logger logger = LoggerFactory.getLogger(CommonMarkServiceImpl.class);
	
	@Override
	public Object service(String arg0, Object arg1) {
		if(sysMsgContentService == null){
			sysMsgContentService = (SysMsgContentService)Springs.getBean(SysMsgContentService.class);
		}
		if(claimTextService == null){
			claimTextService = (ClaimTextService)Springs.getBean(ClaimTextService.class);
		}
		 MCCommonRemarksPacket resPacket = new MCCommonRemarksPacket();
		 String registNo ="";
		try{
			String reqXml = ClaimBaseCoder.objToXmlUtf(arg1);
			logger.info("============公共备注信息查询请求报文："+reqXml);
			MobileCheckRequest packet =(MobileCheckRequest)arg1;
			  registNo = this.validHead(packet);
			
			 MobileCheckResponseHead head = new MobileCheckResponseHead();
			 head.setResponseType("004");
			 head.setResponseCode("YES");
			 head.setResponseMessage("Success");
			 
	    	 List<SysMsgContentVo>  msgConList = sysMsgContentService.findSysMsg(registNo);
	    	 List<MCCommonRemarksRemarkInfo>  remarkList = new ArrayList<MCCommonRemarksRemarkInfo>();
	    	 int i=0;
	    	 if(msgConList != null && msgConList.size() > 0){
	    		 for(SysMsgContentVo msgConentVo : msgConList){
	    			 MCCommonRemarksRemarkInfo optionInfo = new MCCommonRemarksRemarkInfo();
	    			 optionInfo.setRemarkId(i+++"");
	    			 optionInfo.setRemarkType(msgConentVo.getMsgType());
	    			 optionInfo.setHandlerName(CodeTranUtil.transCode("UserCode", msgConentVo.getCreateUser()));
	    			 if(msgConentVo.getNodeCode()!=null && !msgConentVo.getNodeCode().isEmpty()){
	    				 optionInfo.setMessageNode(FlowNode.valueOf(msgConentVo.getNodeCode()).getName());
	    			 }
	    			 optionInfo.setMessageNode(msgConentVo.getNodeCode());
	    			 optionInfo.setInputDate(DateUtils.dateToStr(msgConentVo.getCreateDate(), DateUtils.YToSec));
	    			 optionInfo.setRemark(msgConentVo.getMsgContents());
	    			 remarkList.add(optionInfo);
	    		 }
	    	 }
	    	 i=0;
	    	 List<PrpLClaimTextVo> claimTextList = claimTextService.findClaimTextList(null, registNo, null);
	    	 List<MCCommonRemarksRemarksOptionInfo> remarkOptionList = new ArrayList<MCCommonRemarksRemarksOptionInfo>();
	    	 if(claimTextList != null && claimTextList.size() > 0){
	    		 for(PrpLClaimTextVo claimText : claimTextList){
	    			 MCCommonRemarksRemarksOptionInfo optionInfo = new MCCommonRemarksRemarksOptionInfo();
	    			 optionInfo.setRemarkId(i+++"");
	    			 optionInfo.setRole(claimText.getNodeCode());
	    			 optionInfo.setOperateCode(claimText.getOperatorCode());
	    			 optionInfo.setComCode(claimText.getComCode());
	    			 optionInfo.setInputDate(DateUtils.dateToStr(claimText.getInputTime(), DateUtils.YToSec));
	    			 optionInfo.setOptionDesc(claimText.getDescription());
	    			 optionInfo.setAuditState(claimText.getStatus());
	    			 remarkOptionList.add(optionInfo);
	    		 }
	    	 }
	    	 
	    	 MCCommonRemarksBody body = new MCCommonRemarksBody();
	    	 body.setRegistNo(registNo);
	    	 body.setRemarkList(remarkList);
	    	 body.setRemarkOptionList(remarkOptionList);
	    	 
	    	// MCCommonRemarksPacket resPacket = new MCCommonRemarksPacket();
	    	 resPacket.setHead(head);
	    	 resPacket.setBody(body);
	    	 
		}catch(Exception e){
			MobileCheckResponseHead head = new MobileCheckResponseHead();
			 head.setResponseType("004");
			 head.setResponseCode("NO");
			 head.setResponseMessage(e.getMessage());
			 resPacket.setHead(head);
			 logger.info("移动查勘公共信息备注接口报错信息： ");
			 e.printStackTrace();
		}
		
		String resXml = ClaimBaseCoder.objToXmlUtf(resPacket);
		logger.info("============公共备注信息查询返回报文："+resXml);
		return resPacket;
	}
  private String validHead(MobileCheckRequest packet){
	  Assert.notNull(packet, " 请求信息为空  ");
		MobileCheckHead head = packet.getHead();
		if (!"004".equals(head.getRequestType())|| !"claim_user".equals(head.getUser())|| !"claim_psd".equals(head.getPassWord())) {
			throw new IllegalArgumentException(" 请求头参数错误  ");
		}
		MobileCheckBody body = packet.getBody();
		Assert.notNull(body.getRegistNo(), " 报案号为空  ");
		return body.getRegistNo();
  }
}
