package ins.sino.claimcar.carchildren.service;

import ins.framework.lang.Springs;
import ins.framework.utils.Beans;
import ins.platform.common.service.facade.CodeTranService;
import ins.sino.claimcar.carchild.service.spring.CarchildServiceImpl;
import ins.sino.claimcar.carchild.vo.PrplcarchildregistcancleVo;
import ins.sino.claimcar.carchildren.vo.CarchildReqHeadVo;
import ins.sino.claimcar.carchildren.vo.CarchildResHeadVo;
import ins.sino.claimcar.carchildren.vo.RegistCancleReqBodyVo;
import ins.sino.claimcar.carchildren.vo.RegistCancleReqVo;
import ins.sino.claimcar.carchildren.vo.RegistCancleResVo;
import ins.sino.claimcar.regist.service.FounderCustomService;
import ins.sino.claimcar.regist.service.PrpLCMainService;
import ins.sino.claimcar.regist.service.RegistService;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLRegistCarLossVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.common.utils.Assert;
import com.sinosoft.api.service.ServiceInterface;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import com.thoughtworks.xstream.io.xml.XppDriver;

public class RegistCancleServiceImpl implements ServiceInterface{
	private static Logger logger = LoggerFactory.getLogger(RegistCancleServiceImpl.class);
	@Autowired
	private PrpLCMainService prpLCMainService;
	@Autowired
	private RegistService registService;
	@Autowired
	private CodeTranService codeTranService;
	@Autowired
	private CarchildServiceImpl carchildServiceImpl;
	@Autowired
	FounderCustomService founderService;
	
	@Override
	public Object service(String arg0, Object arg1) {
		init();
		RegistCancleResVo resVo=new RegistCancleResVo();
		CarchildResHeadVo resHead=new CarchildResHeadVo();
		String sign="";
		String registNo = "";
		try{
			
			XStream stream = new XStream(new XppDriver(new XmlFriendlyNameCoder("-_","_")));
			stream.autodetectAnnotations(true);
			stream.setMode(XStream.NO_REFERENCES);
			stream.aliasSystemAttribute(null,"class");// ?????? class??????
			stream.processAnnotations(RegistCancleReqVo.class);
			RegistCancleReqVo reqVo=(RegistCancleReqVo)arg1;
			Assert.notNull(reqVo,"?????????????????? ");
			CarchildReqHeadVo head = reqVo.getHead();
			sign=head.getRequestType();
			sign=head.getRequestType();
		    Assert.notNull(head,"???????????????????????? ");
		        if( (!"MTA_008".equals(head.getRequestType()) && !"CT_008".equals(head.getRequestType()))|| !"claim_user".equals(head.getUser())|| !"claim_psd".equals(head.getPassWord())){
		        	 throw new IllegalArgumentException("?????????????????????  ");
		        }
		    Assert.notNull(reqVo.getBody(),"????????????Body???????????? ");
		    List<PrpLCMainVo> cmainList=prpLCMainService.findPrpLCMainsByRegistNo(reqVo.getBody().getRegistNo());
		    PrplcarchildregistcancleVo vo=new PrplcarchildregistcancleVo();
		    assgnForPrplcarchildregistcancleVo(reqVo.getBody(),cmainList,vo,head);
		    PrplcarchildregistcancleVo prplcarchildregistcancleVo1= carchildServiceImpl.findPrplcarchildregistcancleVoByRegistNo(vo.getRegistNo());
		    if(prplcarchildregistcancleVo1!=null){
		    	Beans.copy().excludeNull().from(vo).to(prplcarchildregistcancleVo1);
		    	carchildServiceImpl.updatePrplcarchildregistcancle(prplcarchildregistcancleVo1);
		    	
		    }else{
		    	carchildServiceImpl.savePrplcarchildregistcancle(vo);
		    }
		    
		    
		    resHead.setErrNo("1");//??????
		    resHead.setErrMsg("success");
		    resHead.setResponsetype(head.getRequestType());
		    registNo = reqVo.getBody().getRegistNo();
		}catch(Exception e){
			resHead.setResponsetype(sign);
			resHead.setErrNo("2");//??????
			resHead.setErrMsg(e.getMessage());
			e.printStackTrace();
			logger.info("????????????"+e.getMessage());
		}
		resVo.setHead(resHead);
        //??????????????????????????????????????????????????????????????????????????????
		if("1".equals(resHead.getErrNo())){
	       try{
	            founderService.registCancelCtAtnToFounder(registNo);
	        }
	        catch(Exception e){
	            logger.info("??????????????????????????????=====================================================???");
	            e.printStackTrace();
	        }
		}
		return resVo;
	}
	
	
	
	private void init(){
		if(prpLCMainService==null){
			prpLCMainService=(PrpLCMainService)Springs.getBean(PrpLCMainService.class);
		}
		if(registService==null){
			registService=(RegistService)Springs.getBean(RegistService.class);
		}
		
		if(codeTranService==null){
			codeTranService = (CodeTranService)Springs.getBean(CodeTranService.class);
		}
		if(carchildServiceImpl==null){
			carchildServiceImpl=(CarchildServiceImpl)Springs.getBean(CarchildServiceImpl.class);
		}
		if(founderService==null){
		    founderService=(FounderCustomService)Springs.getBean(FounderCustomService.class);
        }
	}
	/**
	 * ???ForPrplcarchildregistcancle??????
	 * @param body
	 * @param cmainList
	 * @param vo
	 * @param head
	 * @throws Exception
	 */
	private void assgnForPrplcarchildregistcancleVo(RegistCancleReqBodyVo body,List<PrpLCMainVo> cmainList,PrplcarchildregistcancleVo vo,CarchildReqHeadVo head) throws Exception{
		PrpLRegistVo registVo=registService.findRegistByRegistNo(body.getRegistNo());
		List<PrpLRegistCarLossVo> carLossVos=new ArrayList<PrpLRegistCarLossVo>();
		if(registVo!=null){
			 carLossVos=registVo.getPrpLRegistCarLosses();
		}else{
			 throw new IllegalArgumentException("????????????????????????????????? ");
		}
		
		vo.setCancleDate(StringFormatDate(body.getCancelDate()));
		vo.setCreateTime(new Date());
		if(carLossVos!=null && carLossVos.size()>0){
			for(PrpLRegistCarLossVo lossVo:carLossVos){
				if("1".equals(lossVo.getLossparty())){
					vo.setLicenseNo(lossVo.getLicenseNo());
				}
			}
		}
		if("1".equals(body.getReason())){
			vo.setReason("???????????????");//????????????
		}else if("2".equals(body.getReason())){
			vo.setReason("????????????");//????????????
		}else if("4".equals(body.getReason())){
			vo.setReason("????????????????????????????????????");//????????????
		}else{
			vo.setReason("??????????????????");//3-????????????
			
		}
		//vo.setExamineRusult("0");
		vo.setRemark(body.getRemark());
		vo.setRegistNo(body.getRegistNo());
		vo.setRequestSource(head.getRequestType());
		vo.setStatus("0");
		vo.setTimesTamp(StringFormatDate(body.getTimestamp()));
		vo.setUserCode(codeTranService.transCode("UserCode",body.getUsercode()));
		if(cmainList!=null && cmainList.size()>0){
			for(PrpLCMainVo cmainVo:cmainList){
				if("1101".equals(cmainVo.getRiskCode())){
					vo.setCipolicyNo(cmainVo.getPolicyNo());
					vo.setFlagLog(cmainVo.getInsuredName());
				}else{
					vo.setBipolicyNo(cmainVo.getPolicyNo());
					vo.setFlagLog(cmainVo.getInsuredName());
				}
				
			}
		} 
		
	}

	/**
	 * ??????????????????
	 * String ???????????? Date??????
	 * @param strDate
	 * @return
	 * @throws ParseException
	 */
	private Date StringFormatDate(String strDate) throws ParseException{
		Date date=null;
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		  if(StringUtils.isNotBlank(strDate)){
			date=format.parse(strDate);
		}
		return date;
	}
  
}
