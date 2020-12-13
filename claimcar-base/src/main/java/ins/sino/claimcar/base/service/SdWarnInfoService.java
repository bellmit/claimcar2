package ins.sino.claimcar.base.service;



import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import ins.framework.lang.Springs;
import ins.sino.claimcar.claim.service.SdwarnService;
import ins.sino.claimcar.claim.vo.BaseInfoSdVo;
import ins.sino.claimcar.claim.vo.PacketSdVo;
import ins.sino.claimcar.claim.vo.PrpLwarnInfoVo;
import ins.sino.claimcar.claim.vo.RequestSdBodyVo;
import ins.sino.claimcar.claim.vo.RequestSdHeadVo;
import ins.sino.claimcar.claim.vo.ResHeadSdVo;
import ins.sino.claimcar.claim.vo.ResPacketSdVo;
import ins.sino.claimcar.claim.vo.WarnInfoSdVo;
import ins.sino.claimcar.regist.service.PrpLCMainService;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.sinosoft.api.service.ServiceInterface;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import com.thoughtworks.xstream.io.xml.XppDriver;

public class SdWarnInfoService implements ServiceInterface {
	private static Logger logger = LoggerFactory.getLogger(SdWarnInfoService.class);
	@Autowired
	private SdwarnService sdwarnService;
	@Autowired
	private PrpLCMainService prpLCMainService;
	@Override
	public Object service(String arg0, Object arg1) {
		init();
		XStream stream = new XStream(new XppDriver(new XmlFriendlyNameCoder("-_","_")));
		stream.autodetectAnnotations(true);
		stream.setMode(XStream.NO_REFERENCES);
		stream.aliasSystemAttribute(null,"class");// 去掉 class属性
		stream.processAnnotations(PacketSdVo.class);//请求VO类
		stream.processAnnotations(ResPacketSdVo.class);//响应VO类
		
		PacketSdVo reqVo=(PacketSdVo)arg1;//请求VO类
		RequestSdHeadVo reqheadVo=new RequestSdHeadVo();
		if(reqVo!=null){
			reqheadVo=reqVo.getHeadVo();
		}
		
		
		ResPacketSdVo resVo=new ResPacketSdVo();//响应Vo类
		ResHeadSdVo resHeadVo=new ResHeadSdVo();
		
		try{
			if (!"C06".equals(reqheadVo.getRequestType())) {
				throw new IllegalArgumentException(" 请求头参数错误  ");
			}
			logger.info("山东预警信息推送接口请求XML--->"+stream.toXML(reqVo));
			resHeadVo.setRequestType("C06");
			resHeadVo.setResponseCode("1");
			resHeadVo.setErrorCode("0000");
			resHeadVo.setErrorMessage("成功");
			
		}catch(Exception e){
			resHeadVo.setRequestType("C06");//异常失败返回
			resHeadVo.setResponseCode("0");
			resHeadVo.setErrorCode("3001");
			resHeadVo.setErrorMessage(e.getMessage());
		}finally{
			if(resHeadVo!=null && "1".equals(resHeadVo.getResponseCode())){
				if(reqVo!=null){
					RequestSdBodyVo requestSdBodyVo=reqVo.getBodyVo();
					if(requestSdBodyVo!=null){
						BaseInfoSdVo baseInfoSdVo=requestSdBodyVo.getBaseInfoSdVo();
						List<PrpLwarnInfoVo> warnVos=new ArrayList<PrpLwarnInfoVo>();
						PrpLCMainVo prpLCMainVo=prpLCMainService.findPrpLCMainByClaimSequenceNo(baseInfoSdVo.getClaimSequenceNo());
						if(baseInfoSdVo!=null && baseInfoSdVo.getWarnInfoSdVos()!=null && baseInfoSdVo.getWarnInfoSdVos().size()>0){
							for(WarnInfoSdVo warnVo:baseInfoSdVo.getWarnInfoSdVos()){
								PrpLwarnInfoVo prpLwarnInfoVo=new PrpLwarnInfoVo();
								if(prpLCMainVo!=null){
									prpLwarnInfoVo.setRegistNo(prpLCMainVo.getRegistNo());
									
								}
								prpLwarnInfoVo.setCreateTime(new Date());
								prpLwarnInfoVo.setClaimsequenceNo(baseInfoSdVo.getClaimSequenceNo());
								prpLwarnInfoVo.setSerialNumber(warnVo.getSerialNumber());
								prpLwarnInfoVo.setWarnMessage(warnVo.getWarnMessage());
								prpLwarnInfoVo.setWarnstageCode(baseInfoSdVo.getWarnStage());
								prpLwarnInfoVo.setWarnstageName(changeName(baseInfoSdVo.getWarnStage()));
								warnVos.add(prpLwarnInfoVo);
							   
								
							}
						}
						sdwarnService.savePrplwarninfo(warnVos);
						
					}
				}
			}
		}
		resVo.setResHeadSdVo(resHeadVo);
		logger.info("山东预警信息推送接口响应XML--->"+stream.toXML(resVo));
		return resVo;
	}
	private void init() {
		if(sdwarnService==null){
			sdwarnService = (SdwarnService)Springs.getBean(SdwarnService.class);
		}
		if(prpLCMainService==null){
			prpLCMainService=(PrpLCMainService)Springs.getBean(PrpLCMainService.class);
		}
	}
	/**
	 * 翻译节点代码
	 * @param warnStageCode
	 * @return
	 */
    private String changeName(String warnStageCode){
    	String warnStageName="";
    	if(StringUtils.isNotBlank(warnStageCode)){
    		if("01".equals(warnStageCode)){
    			warnStageName="报案";
    		}else if("02".equals(warnStageCode)){
    			warnStageName="立案";
    		}else if("03".equals(warnStageCode)){
    			warnStageName="查勘";
    		}else if("04".equals(warnStageCode)){
    			warnStageName="定核损";
    		}else if("05".equals(warnStageCode)){
    			warnStageName="理算核赔";
    		}else if("06".equals(warnStageCode)){
    			warnStageName="结案";
    		}
    	}

    	return warnStageName;
    }
}
