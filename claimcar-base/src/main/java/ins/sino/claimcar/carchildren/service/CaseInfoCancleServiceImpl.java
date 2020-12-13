package ins.sino.claimcar.carchildren.service;

import ins.framework.lang.Springs;
import ins.platform.utils.ClaimBaseCoder;
import ins.sino.claimcar.carchild.service.CarchildService;
import ins.sino.claimcar.carchild.util.CarchildUtil;
import ins.sino.claimcar.carchild.vo.PrplcarchildregistcancleVo;
import ins.sino.claimcar.carchildren.vo.CarchildReqHeadVo;
import ins.sino.claimcar.carchildren.vo.CarchildResHeadVo;
import ins.sino.claimcar.carchildren.vo.CaseCancleNopassReqBodyVo;
import ins.sino.claimcar.carchildren.vo.CaseCancleNopassReqVo;
import ins.sino.claimcar.carchildren.vo.CaseCancleNopassResVo;
import ins.sino.claimcar.carinterface.service.ClaimInterfaceLogService;
import ins.sino.claimcar.carinterface.util.BusinessType;
import ins.sino.claimcar.carinterface.vo.ClaimInterfaceLogVo;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.service.WfFlowQueryService;
import ins.sino.claimcar.flow.service.WfTaskHandleService;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.SpringProperties;

import com.alibaba.dubbo.common.utils.Assert;
import com.sinosoft.api.service.ServiceInterface;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import com.thoughtworks.xstream.io.xml.XppDriver;

public class CaseInfoCancleServiceImpl implements ServiceInterface{
	private static Logger logger = LoggerFactory.getLogger(CaseInfoCancleServiceImpl.class);
	   
    private static final String CT_04="dhDockingService.revokeOrder";
	@Autowired
	private CarchildService carchildService;
	@Autowired
	ClaimInterfaceLogService interfaceLogService;
	@Autowired
	WfFlowQueryService wfFlowQueryService;
    @Autowired
    private WfTaskHandleService wfTaskHandleService;
	@Override
	public Object service(String arg0, Object arg1) {
		init();
	    CaseCancleNopassResVo resVo = new CaseCancleNopassResVo();
		CarchildResHeadVo resHead = new CarchildResHeadVo();
		CaseCancleNopassReqVo reqVo = new CaseCancleNopassReqVo();
		String sign = "";
		String requestType = "";
		CarchildReqHeadVo head = new CarchildReqHeadVo();
		try{
			XStream stream = new XStream(new XppDriver(new XmlFriendlyNameCoder("-_","_")));
			stream.autodetectAnnotations(true);
			stream.setMode(XStream.NO_REFERENCES);
			stream.aliasSystemAttribute(null,"class");// 去掉 class属性
			stream.processAnnotations(CaseCancleNopassReqVo.class);
			reqVo = (CaseCancleNopassReqVo)arg1;
			Assert.notNull(reqVo,"请求信息为空 ");
			head = reqVo.getHead();
		    Assert.notNull(head,"请求信息头部为空 ");
	        if( !"CC_001".equals(head.getRequestType())|| !"claim_user".equals(head.getUser())|| !"claim_psd".equals(head.getPassWord())){
	        	 throw new IllegalArgumentException("请求头参数错误  ");
	        }
		    Assert.notNull(reqVo.getBody(),"请求信息Body信息为空 ");
		    requestType = head.getRequestType();
		}catch(Exception e){
			resHead.setResponsetype(requestType);
			resHead.setErrNo("2");//失败
			resHead.setErrMsg(e.getMessage());
			e.printStackTrace();
			logger.info("错误信息"+e.getMessage());
		}finally{
		    try{
                sendCaseCancleNopassToCtMta(reqVo);
            }
            catch(Exception e){
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
		}
        resHead.setErrNo("1");//成功
        resHead.setErrMsg("success");
        resHead.setResponsetype(requestType);
		resVo.setHead(resHead);
		return resVo;
	}
	
	private void init(){
		if(carchildService==null){
		    carchildService=(CarchildService)Springs.getBean(CarchildService.class);
		}
		if(interfaceLogService == null){
            interfaceLogService = (ClaimInterfaceLogService)Springs.getBean(ClaimInterfaceLogService.class);
        }
		if(wfFlowQueryService == null){
		    wfFlowQueryService = (WfFlowQueryService)Springs.getBean(WfFlowQueryService.class);
        }
        if(wfTaskHandleService == null){
            wfTaskHandleService = (WfTaskHandleService)Springs.getBean(WfTaskHandleService.class);
        }
	}
	
	   /**
     * 车童、民太安日志保存
     * @param reqObj
     * @param resObj
     * @param logVo
     * @param flag
     * @param errorMsg
     */
    private  void logUtil(CaseCancleNopassReqVo reqObj,CaseCancleNopassResVo resObj,ClaimInterfaceLogVo logVo,String sign) throws Exception {
        try{
            CarchildResHeadVo headVo=new CarchildResHeadVo();
            headVo=resObj.getHead();
            String reqXml = ClaimBaseCoder.objToXmlUtf(reqObj);
            String resXml = ClaimBaseCoder.objToXmlUtf(resObj);
            logger.info("===============车童网、民太安===========");
            logger.info(reqXml);
            logger.info(resXml);
            if(sign.contains("CT")){
                logVo.setBusinessType(BusinessType.CT_caseCancleNoPass.name());
                logVo.setBusinessName(BusinessType.CT_caseCancleNoPass.getName());
            }else{
                logVo.setBusinessType(BusinessType.MTA_caseCancleNoPass.name());
                logVo.setBusinessName(BusinessType.MTA_caseCancleNoPass.getName());
            }
            logVo.setRequestXml(reqXml);
            logVo.setResponseXml(resXml);
            logVo.setCreateTime(new Date());
            logVo.setRequestTime(new Date());
            if("1".equals(headVo.getErrNo())){
                logVo.setStatus("1");
            }else{
                logVo.setStatus("0");
                logVo.setErrorMessage(headVo.getErrMsg());
                
            }
        }catch(Exception e1){
            e1.printStackTrace();
        }finally{
            interfaceLogService.save(logVo);
        }
     }
    

    private  void sendCaseCancleNopassToCtMta(CaseCancleNopassReqVo reqVo) throws Exception{
        PrplcarchildregistcancleVo carchildregistcancleVo = new PrplcarchildregistcancleVo();
        CaseCancleNopassResVo resVo = new CaseCancleNopassResVo();
        String registNo = reqVo.getBody().getRegistNo();
        String url = "";
        carchildregistcancleVo = carchildService.findCarchildregistcancleVoByRegistNo(registNo);
        CaseCancleNopassReqBodyVo caseCancleNopassReqBodyVo = new CaseCancleNopassReqBodyVo();
        String sign = "";
        if(carchildregistcancleVo != null){
            sign = carchildregistcancleVo.getRequestSource();
        }
        try{
            caseCancleNopassReqBodyVo = reqVo.getBody();
            //逻辑
            
            if(sign.contains("CT")){
                if(reqVo.getHead() != null){
                    reqVo.getHead().setRequestType("CT_009");
                }
                String xmlToSend = ClaimBaseCoder.objToXmlUtf(reqVo);
                logger.info("发送的Xml------------------"+xmlToSend);
                url = SpringProperties.getProperty("CT_URL")+CT_04;
                logger.info("注销申请不通过接口send---------------------------"+xmlToSend);
                String xmlReturn = CarchildUtil.requestPlatformForCT(xmlToSend,url,CT_04);
                logger.info("注销申请不通过接口return---------------------------"+xmlReturn);
                resVo = ClaimBaseCoder.xmlToObj(xmlReturn,CaseCancleNopassResVo.class);
            }else{
                if(reqVo.getHead() != null){
                    reqVo.getHead().setRequestType("MTA_009");
                }
                String xmlToSend = ClaimBaseCoder.objToXmlUtf(reqVo);
                logger.info("发送的Xml------------------"+xmlToSend);
                url = SpringProperties.getProperty("MTA_URL_CANCELFAILED");//请求地址MTA_CASENOPASS_URL
                String responseXml = CarchildUtil.requestPlatform(xmlToSend,url,sign);
                logger.info("民太安的返回Xml------------------"+responseXml);
                resVo = ClaimBaseCoder.xmlToObj(responseXml,CaseCancleNopassResVo.class);
            }
            //锁定
            CarchildResHeadVo carchildResHeadVo = resVo.getHead();
            if("1".equals(carchildResHeadVo.getErrNo())){
                List<PrpLWfTaskVo> checkVolist = wfFlowQueryService.findTaskVoForInByNodeCode(registNo, FlowNode.Check.toString());
                //List<PrpLWfTaskVo> dLossVolist = wfFlowQueryService.findTaskVoForInByNodeCode(registNo, FlowNode.DLoss.toString());
                List<PrpLWfTaskVo> volist = new ArrayList<PrpLWfTaskVo>();
                if(checkVolist != null && checkVolist.size() > 0){
                    volist.addAll(checkVolist);
                }
               /* if(dLossVolist != null && dLossVolist.size() > 0){
                    volist.addAll(dLossVolist);
                }*/
                List<PrpLWfTaskVo> prpLWfTaskVoList = new ArrayList<PrpLWfTaskVo>();
                if(volist != null && volist.size() > 0){
                    for(PrpLWfTaskVo vo : volist){
                        if("0".equals(vo.getHandlerStatus()) || "2".equals(vo.getHandlerStatus())){
                            if(sign.contains("CT")){
                                vo.setIsMobileAccept("2");
                            }else{
                                vo.setIsMobileAccept("3");
                            }
                            prpLWfTaskVoList.add(vo);
                        }
                    }
                }
                logger.info("prpLWfTaskVoList==========================="+prpLWfTaskVoList.size());
                wfTaskHandleService.updateIsMobileCaseByFlowId(prpLWfTaskVoList);
                //回写PrplcarchildregistcancleVo
                PrplcarchildregistcancleVo cancleVo= carchildService.findPrplcarchildregistcancleVoByRegistNo(registNo);
                if(cancleVo != null){
                    cancleVo.setHandleDate(new Date());
                    cancleVo.setExamineRusult("0");
                    cancleVo.setStatus("1");
                    carchildService.updatePrplcarchildregistcancle(cancleVo);
                }
            }
        }catch(Exception e){
                e.printStackTrace();
                logger.info("错误信息"+e.getMessage());
        }finally{
                ClaimInterfaceLogVo logVo = new ClaimInterfaceLogVo();
                logVo.setRegistNo(registNo);
                if(sign.contains("CT")){
                    logVo.setServiceType("CT"); //请求来源--车童网
                }else if(sign.contains("MTA")){
                    logVo.setServiceType("MTA"); //请求来源--民太安
                }
                logVo.setCreateUser(caseCancleNopassReqBodyVo.getUsercode());
                logVo.setComCode("");
                logVo.setRequestUrl(url);
                try{
                    this.logUtil(reqVo,resVo,logVo,sign);
                }
                catch(Exception e2){
                    e2.printStackTrace();
                }
        }
    }
}
