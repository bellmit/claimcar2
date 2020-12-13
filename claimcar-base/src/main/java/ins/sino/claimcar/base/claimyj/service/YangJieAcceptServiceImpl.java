package ins.sino.claimcar.base.claimyj.service;

/**
 * 阳杰接收接口
 */
import java.util.List;

import ins.framework.lang.Springs;
import ins.platform.utils.ClaimBaseCoder;
import ins.sino.claimcar.claimcarYJ.vo.DLhkReqBody;
import ins.sino.claimcar.claimcarYJ.vo.DLhkReqPacket;
import ins.sino.claimcar.claimcarYJ.vo.PrpLDlhkMainVo;
import ins.sino.claimcar.claimcarYJ.vo.ResYJVo;
import ins.sino.claimcar.claimyj.service.YjPrpLDlhkMainService;


import ins.sino.claimcar.flow.service.WfTaskHandleService;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.losscar.service.LossCarService;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarMainVo;
import ins.sino.claimcar.mobileCheckCommon.vo.MobileCheckHead;
import ins.sino.claimcar.mobileCheckCommon.vo.MobileCheckResponseHead;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.common.utils.Assert;
import com.sinosoft.api.service.ServiceInterface;

public class YangJieAcceptServiceImpl  implements ServiceInterface {

	private static Logger logger = LoggerFactory.getLogger(YangJieAcceptServiceImpl.class);
	private static String DHIC = "DHIC";
	@Autowired
	YjPrpLDlhkMainService yjPrpLDlhkMainService;
	@Autowired
	WfTaskHandleService wfTaskHandleService;
	@Autowired
	LossCarService lossCarService;
    
    @Override
	public ResYJVo service(String arg0, Object arg1) {
    	init();
		String reqXml = ClaimBaseCoder.objToXmlUtf(arg1);
		DLhkReqPacket reqPacket = (DLhkReqPacket) arg1;
		Assert.notNull(reqPacket, "数据为空 ");
		DLhkReqBody reqBody = reqPacket.getBody();
		MobileCheckHead head = reqPacket.getHead();
		if (!"DlChkInform".equals(head.getRequestType())|| !"yangjie_user".equals(head.getUser())|| !"yangjie_psd".equals(head.getPassWord())) {
			throw new IllegalArgumentException(" 请求头参数错误  ");
		}
		PrpLDlhkMainVo prpLDlhkMainVo = reqBody.getPrpLDlhkMainVo();
		logger.info("阳杰复勘接收接口请求报文: \n"+reqXml);
		
		String errMsg = "成功";
		String errNo = "1";
		ResYJVo resYJVo = new ResYJVo();
		MobileCheckResponseHead responseHead = new MobileCheckResponseHead();
		
		responseHead.setResponseType("");
		try{
			List<PrpLDlhkMainVo> prpLDlhkMainVos = yjPrpLDlhkMainService.findPrpLDlhkMainsBytopActualId(prpLDlhkMainVo.getTopActualId());
			if(prpLDlhkMainVos != null && prpLDlhkMainVos.size() >0){
				errNo = "0";
				errMsg = "案卷id重复，请勿重复提交!";
				logger.info("阳杰复勘接收接口的案卷id案卷id重复，请勿重复提交：\n");
			}else{
				PrpLWfTaskVo wfTaskVo = wfTaskHandleService.queryTask(Double.parseDouble(prpLDlhkMainVo.getTopActualId()));
				String carMianId = prpLDlhkMainVo.getActualId().replaceAll(DHIC, "");
				PrpLDlossCarMainVo dlossCarMainVo = lossCarService.findLossCarMainById(Long.parseLong(carMianId));

				if(wfTaskVo != null && prpLDlhkMainVo.getNotificationNo().equals(wfTaskVo.getRegistNo()) &&
						prpLDlhkMainVo.getLicenseNo().equals(dlossCarMainVo.getLicenseNo()) &&
						carMianId.equals(wfTaskVo.getHandlerIdKey())){
					//保存数据
					yjPrpLDlhkMainService.savePrpLDlhkMain(prpLDlhkMainVo);
				}else{
					errNo = "0";
					errMsg = "案卷id不存在";
					logger.info("阳杰复勘接收接口的案卷id不存在：\n");
				}
			}
		}catch(Exception e){
			errNo = "0";
			errMsg = "保存数据报错："+e.getMessage();
			logger.info("阳杰复勘接收接口报错：\n", e);
		}
		responseHead.setResponseCode(errNo);
		if("1".equals(errNo)){
			responseHead.setResponseMessage("Success");
		}else{
			responseHead.setResponseMessage(errMsg);
		}
		responseHead.setResponseType("DlChkInform");
		resYJVo.setHead(responseHead);
		
		String resXml = ClaimBaseCoder.objToXmlUtf(resYJVo);
		logger.info("阳杰复勘接收接口返回报文=========：\n"+resXml);
		return resYJVo;
	}
    
    private void init() {
    	if(yjPrpLDlhkMainService == null){
    		yjPrpLDlhkMainService = (YjPrpLDlhkMainService)Springs.getBean(YjPrpLDlhkMainService.class);
    	}
    	if(wfTaskHandleService == null){
    		wfTaskHandleService = (WfTaskHandleService)Springs.getBean(WfTaskHandleService.class);
    	}
    	if(lossCarService == null){
    		lossCarService = (LossCarService)Springs.getBean(LossCarService.class);
    	}
    }

}
