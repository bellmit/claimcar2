package ins.sino.claimcar.platform.service;

import ins.framework.lang.Springs;
import ins.sino.claimcar.regist.service.PrpLCMainService;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.trafficplatform.vo.CarRiskCaseWriteReqBasePartVo;
import ins.sino.claimcar.trafficplatform.vo.CarRiskCaseWriteReqBodyVo;
import ins.sino.claimcar.trafficplatform.vo.CarRiskCaseWriteReqVo;
import ins.sino.claimcar.trafficplatform.vo.RequestHeadVo;
import ins.sino.claimcar.trafficplatform.vo.ResponseHeadVo;
import ins.sino.claimcar.trafficplatform.vo.SdResponseVo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.common.utils.Assert;
import com.sinosoft.api.service.ServiceInterface;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import com.thoughtworks.xstream.io.xml.XppDriver;
/**
 * 重复/虚假案件回写
 * <pre></pre>
 * @author ★zhujunde
 */
public class CarRiskCaseWritePlatformServiceImpl implements ServiceInterface{
	private static Logger logger = LoggerFactory.getLogger(CarRiskCaseWritePlatformServiceImpl.class);
	@Autowired
	private PrpLCMainService prpLCMainService;
	
	@Override
	public Object service(String arg0, Object arg1) {
		init();
		SdResponseVo resVo = new SdResponseVo();
		ResponseHeadVo resHead=new ResponseHeadVo();
		XStream stream = new XStream(new XppDriver(new XmlFriendlyNameCoder("-_","_")));
        stream.autodetectAnnotations(true);
        stream.setMode(XStream.NO_REFERENCES);
        stream.aliasSystemAttribute(null,"class");// 去掉 class属性
		try{
			stream.processAnnotations(CarRiskCaseWriteReqVo.class);
			CarRiskCaseWriteReqVo reqVo=(CarRiskCaseWriteReqVo)arg1;
	        String xml = stream.toXML(reqVo);
			logger.info("重复/虚假案件回写请求报文======================="+xml);
			Assert.notNull(reqVo,"请求信息为空 ");
			RequestHeadVo head = reqVo.getHead();
		    Assert.notNull(head,"请求信息头部为空 ");
		        if( (!"C07".equals(head.getRequestType())) || (!"sysuser".equals(head.getUser()))
		                || (!"123".equals(head.getPassword()))){
		        	 throw new IllegalArgumentException("请求头参数错误  ");
		        }
		    Assert.notNull(reqVo.getBody(),"请求信息Body信息为空 ");
		    CarRiskCaseWriteReqBodyVo carRiskCaseWriteReqBodyVo = reqVo.getBody();
		    CarRiskCaseWriteReqBasePartVo basePartVo = carRiskCaseWriteReqBodyVo.getBasePart();
		    String claimSequenceNo = basePartVo.getClaimSequenceNo();
		    String claimType = basePartVo.getClaimType();
		    String remarks = basePartVo.getRemark();
		    PrpLCMainVo prpLCMainVo = new PrpLCMainVo();
		    prpLCMainVo = prpLCMainService.findPrpLCMainByClaimSequenceNo(claimSequenceNo);
		    //根据理赔编码查询
		    prpLCMainVo.setClaimType(claimType);
		    prpLCMainVo.setRemarks(remarks);
		    prpLCMainService.updatePrpLCMain(prpLCMainVo);
		    //待续
		    resHead.setRequestType("C07");
		    resHead.setErrorCode("1");
		    resHead.setErrorCode("0000");
		    resHead.setErrorMessage("成功");
		    resVo.setHeadVo(resHead);
		}catch(Exception e){
		    resHead.setRequestType("C07");
            resHead.setErrorCode("0");
            resHead.setErrorCode("3001");
            resHead.setErrorMessage(e.getMessage());
            resVo.setHeadVo(resHead);
			e.printStackTrace();
			logger.info("错误信息"+e.getMessage());
		}
        stream.processAnnotations(SdResponseVo.class);
        logger.info("重复/虚假案件回写返回报文=========：\n"+stream.toXML(resVo));
		return resVo;
	}
	private void init(){
		if(prpLCMainService==null){
			prpLCMainService=(PrpLCMainService)Springs.getBean(PrpLCMainService.class);
		}
	}
}
