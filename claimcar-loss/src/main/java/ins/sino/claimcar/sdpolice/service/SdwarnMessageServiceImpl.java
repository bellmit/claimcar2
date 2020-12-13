package ins.sino.claimcar.sdpolice.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ins.framework.lang.Springs;
import ins.sino.claimcar.regist.service.PrpLCMainService;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.sdpolice.policeInfoVo.RequestPoliceInfoBaseBodyVo;
import ins.sino.claimcar.sdpolice.policeInfoVo.SdpoliceInfoVo;
import ins.sino.claimcar.sdpolice.policeInfoVo.WarnInfoVo;
import ins.sino.claimcar.trafficplatform.vo.PrpLsdpoliceInfoVo;
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

public class SdwarnMessageServiceImpl implements ServiceInterface {
	private static Logger logger = LoggerFactory.getLogger(SdwarnMessageServiceImpl.class);
	@Autowired
	PrpLCMainService prpLCMainService;
	@Autowired
	SdpoliceServiceImpl sdpoliceServiceImpl;
	@Override
	public Object service(String paramString, Object paramObject) {
		init();
		SdResponseVo sdResponseVo=new SdResponseVo();
		ResponseHeadVo resHeadVo=new ResponseHeadVo();
	try{
		XStream stream = new XStream(new XppDriver(new XmlFriendlyNameCoder("-_","_")));
		stream.autodetectAnnotations(true);
		stream.setMode(XStream.NO_REFERENCES);
		stream.aliasSystemAttribute(null,"class");// 去掉 class属性
		stream.processAnnotations(SdpoliceInfoVo.class);
		SdpoliceInfoVo reqVo=(SdpoliceInfoVo)paramObject;
		RequestHeadVo headVo=reqVo.getHeadVo();
		Assert.notNull(headVo,"请求信息头部为空 ");
		if(!"C06".equals(headVo.getRequestType())){
			 throw new IllegalArgumentException("请求头参数错误  ");
		}
		Assert.notNull(reqVo.getBodyVo(),"请求信息Body信息为空 ");
		RequestPoliceInfoBaseBodyVo baseBodyVo=reqVo.getBodyVo().getBaseBodyVo();
		resHeadVo.setRequestType("C06");
		resHeadVo.setResponseCode("1");
		resHeadVo.setErrorCode("0000");
		resHeadVo.setErrorMessage("成功");
        List<PrpLCMainVo> cmainList=prpLCMainService.findPrpLCMainsByClaimSequenceNo(baseBodyVo.getClaimSequenceNo());
        List<PrpLsdpoliceInfoVo> infoVoList= paramsforPrpLsdpoliceInfoVo(cmainList,baseBodyVo);
           try {
        	    if(infoVoList!=null && infoVoList.size()>0){
        	       sdpoliceServiceImpl.saveprplsdpoliceInfo(infoVoList);
        	     }
			
		      } catch (Exception e) {
			   e.printStackTrace();
			   logger.info("保存山东预警信息表失败,错误信息--->"+e.getMessage());
		     }
      }catch(Exception e){
    	  resHeadVo.setRequestType("C06");
    	  resHeadVo.setResponseCode("0");
    	  resHeadVo.setErrorCode("3001");
    	  resHeadVo.setErrorMessage(e.getMessage());
        }
	sdResponseVo.setHeadVo(resHeadVo);
    return sdResponseVo;
	}
	
  //手动注入
  private void init(){
	  
	  if(prpLCMainService==null){
		  prpLCMainService=(PrpLCMainService)Springs.getBean(PrpLCMainService.class);
	  }
	  if(sdpoliceServiceImpl==null){
		 sdpoliceServiceImpl=(SdpoliceServiceImpl)Springs.getBean(SdpoliceServiceImpl.class);
	   }
		
	}
  /**
   * 翻译理赔阶段
   * @param claimStageCode
   * @return
   */
  private String translateClaimStage(String claimStageCode){
	  String claimStageName="";
	  if("01".equals(claimStageCode)){
		  claimStageName="报案";
	  }else if("02".equals(claimStageCode)){
		  claimStageName="立案";
	  }else if("03".equals(claimStageCode)){
		  claimStageName="查勘";
	  }else if("04".equals(claimStageCode)){
		  claimStageName="定核损";
	  }else if("05".equals(claimStageCode)){
		  claimStageName="理算核赔";
	  }else if("06".equals(claimStageCode)){
		  claimStageName="结案";
	  }else{
		  claimStageName="";
	  }
	  return claimStageName;
  }
  /**
   * 预警信息Vo赋值
   * @param cmainVoList
   * @param baseBodyVo
   * @return
   */
  private List<PrpLsdpoliceInfoVo> paramsforPrpLsdpoliceInfoVo(List<PrpLCMainVo> cmainVoList,RequestPoliceInfoBaseBodyVo baseBodyVo){
	  String registNo="";
	  if(cmainVoList!=null && cmainVoList.size()>0){
		  registNo=cmainVoList.get(0).getRegistNo();
	  }
	  List<PrpLsdpoliceInfoVo> infoVoList=new ArrayList<PrpLsdpoliceInfoVo>();
	  if(baseBodyVo!=null){
		  List<WarnInfoVo> warnInfoVoList= baseBodyVo.getWarnInfoList();
		  if(warnInfoVoList!=null && warnInfoVoList.size()>0){
			  for(WarnInfoVo vo:warnInfoVoList){
				  PrpLsdpoliceInfoVo infoVo=new PrpLsdpoliceInfoVo();
				  infoVo.setCreateTime(new Date());
				  infoVo.setWarnMessage(vo.getWarnMessage());
				  infoVo.setRegistNo(registNo);
				  infoVo.setClaimsequenceNo(baseBodyVo.getClaimSequenceNo());//理赔编码
			      infoVo.setClaimStage(translateClaimStage(baseBodyVo.getWarnStage()));//理赔阶段
			      infoVoList.add(infoVo);
			  }
		  }
	  }
	  return infoVoList;
  }
}
