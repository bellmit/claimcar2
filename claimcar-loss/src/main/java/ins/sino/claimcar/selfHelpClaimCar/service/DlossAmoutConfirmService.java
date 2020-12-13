package ins.sino.claimcar.selfHelpClaimCar.service;

import java.util.List;

import ins.framework.lang.Springs;
import ins.sino.claimcar.flow.service.WfTaskHandleService;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.selfHelpClaimCar.vo.DlossAmoutConfirmVo;
import ins.sino.claimcar.selfHelpClaimCar.vo.ResponseHeadVo;
import ins.sino.claimcar.selfHelpClaimCar.vo.ResponseVo;
import ins.sino.claimcar.selfHelpClaimCar.vo.ResquestHeadVo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import aj.org.objectweb.asm.Type;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import com.thoughtworks.xstream.io.xml.XppDriver;
import com.sinosoft.api.service.ServiceInterface;

public class DlossAmoutConfirmService implements ServiceInterface {
	@Autowired
	private SelfHelpClaimCarService selfHelpClaimCarService;
	@Autowired
	WfTaskHandleService wfTaskHandleService;
	private static Logger logger = LoggerFactory.getLogger(DlossAmoutConfirmService.class);
	@Override
	public Object service(String arg0, Object arg1) {
		init();
		XStream stream = new XStream(new XppDriver(new XmlFriendlyNameCoder("-_","_")));
		stream.autodetectAnnotations(true);
		stream.setMode(XStream.NO_REFERENCES);
		stream.aliasSystemAttribute(null,"class");// 去掉 class属性
		stream.processAnnotations(DlossAmoutConfirmVo.class);//请求VO类
		stream.processAnnotations(ResponseVo.class);//响应VO类
		
		DlossAmoutConfirmVo reqVo=(DlossAmoutConfirmVo)arg1;//请求VO类
		ResquestHeadVo reqheadVo=reqVo.getHeadVo();
		
		ResponseVo resVo=new ResponseVo();//响应Vo类
		ResponseHeadVo resHeadVo=new ResponseHeadVo();
		
		try{
			if (!"SELFCLAIM_005".equals(reqheadVo.getRequestType())|| !"claim_user".equals(reqheadVo.getUser())|| !"claim_psd".equals(reqheadVo.getPassWord())) {
				throw new IllegalArgumentException(" 请求头参数错误  ");
			}
			logger.info("自助理赔客户定损金额确认接口请求XML--->"+stream.toXML(reqVo));
			resHeadVo.setResponseType("SELFCLAIM_005");
			resHeadVo.setErrno("1");
			resHeadVo.setErrmsg("success");
		}catch(Exception e){
			resHeadVo.setResponseType("SELFCLAIM_005");
			resHeadVo.setErrno("0");
			resHeadVo.setErrmsg(e.getMessage());
		}finally{
			String signFlag="0";//0表示该案件未查勘提交，1表示该案件已查勘提交
			if(reqVo.getBodyVo().getCasecarStates()!=null && reqVo.getBodyVo().getCasecarStates().size()>0){
				List<PrpLWfTaskVo> wftaskOutVos=wfTaskHandleService.findTaskOutVo(reqVo.getBodyVo().getCasecarStates().get(0).getInscaseNo(),"Check");
				if(wftaskOutVos!=null && wftaskOutVos.size()>0){
					for(PrpLWfTaskVo vo:wftaskOutVos){
						if("3".equals(vo.getWorkStatus())){
							signFlag="1";
							break;
						}
					}
				}
				
			}
			
			if("1".equals(resHeadVo.getErrno()) && "0".equals(signFlag)){
				selfHelpClaimCarService.updateOrSavePrpLAutocasestateInfo(reqVo);//保存或更新返回结果信息
			}
			
		}
		resVo.setResHeadVo(resHeadVo);
		logger.info("自助理赔客户定损金额确认接口响应XML--->"+stream.toXML(resVo));
		return resVo;
	}
	
	private void init() {
		if(selfHelpClaimCarService==null){
			selfHelpClaimCarService = (SelfHelpClaimCarService)Springs.getBean(SelfHelpClaimCarService.class);
		}
		if(wfTaskHandleService==null){
			wfTaskHandleService = (WfTaskHandleService)Springs.getBean(WfTaskHandleService.class);
		}
	}
}
