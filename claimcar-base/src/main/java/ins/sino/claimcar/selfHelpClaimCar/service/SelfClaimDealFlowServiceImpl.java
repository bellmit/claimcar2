package ins.sino.claimcar.selfHelpClaimCar.service;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ins.framework.lang.Springs;
import ins.framework.service.CodeTranService;
import ins.platform.utils.ClaimBaseCoder;
import ins.sino.claimcar.flow.service.WfFlowQueryService;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.selfHelpClaimCar.vo.ResponseHeadVo;
import ins.sino.claimcar.selfHelpClaimCar.vo.ResquestHeadVo;
import ins.sino.claimcar.selfHelpClaimCar.vo.SelfClaimDealFlowReqPacketVo;
import ins.sino.claimcar.selfHelpClaimCar.vo.SelfClaimDealFlowResBodyVo;
import ins.sino.claimcar.selfHelpClaimCar.vo.SelfClaimDealFlowResPacketVo;
import ins.sino.claimcar.selfHelpClaimCar.vo.TaskInfoVo;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.common.utils.Assert;
import com.sinosoft.api.service.ServiceInterface;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import com.thoughtworks.xstream.io.xml.XppDriver;

//自助理赔处理流程查询接口
public class SelfClaimDealFlowServiceImpl implements ServiceInterface {
	private static Logger logger = LoggerFactory.getLogger(SelfClaimDealFlowServiceImpl.class);
	@Autowired
	WfFlowQueryService wfFlowQueryService;
	@Autowired
	CodeTranService codeTranService;
	@Override
	public Object service(String arg0, Object arg1) {
		init();
		SelfClaimDealFlowResPacketVo resVo=new SelfClaimDealFlowResPacketVo();
		String sign="";
		try{
			
			XStream stream = new XStream(new XppDriver(new XmlFriendlyNameCoder("-_","_")));
			stream.autodetectAnnotations(true);
			stream.setMode(XStream.NO_REFERENCES);
			stream.aliasSystemAttribute(null,"class");// 去掉 class属性
			stream.processAnnotations(SelfClaimDealFlowReqPacketVo.class);
			stream.processAnnotations(SelfClaimDealFlowResPacketVo.class);
			SelfClaimDealFlowReqPacketVo reqVo=(SelfClaimDealFlowReqPacketVo)arg1;
			Assert.notNull(reqVo,"请求信息为空 ");
			ResquestHeadVo head = reqVo.getReqHeadVo();
			sign=head.getRequestType();
		    Assert.notNull(head,"请求信息头部为空 ");
		        if(!"SELFCLAIM_007".equals(head.getRequestType()) || !"claim_user".equals(head.getUser())|| !"claim_psd".equals(head.getPassWord())){
		            throw new IllegalArgumentException("请求头参数错误  ");
		        }
		    Assert.notNull(reqVo.getReqBodyVo(),"请求信息Body信息为空 ");
		    String reqXml=ClaimBaseCoder.objToXml(reqVo);//请求的Xml
		    logger.info("自助理赔流程处理查询接口请求XML----->"+reqXml);
		    //查询工作流任务
		    List<PrpLWfTaskVo> wftaskVoList=new ArrayList<PrpLWfTaskVo>();
		    if(reqVo!=null && reqVo.getReqBodyVo()!=null){
		        wftaskVoList=wfFlowQueryService.findTaskVoForInAndOutByRegistNo(reqVo.getReqBodyVo().getRegistNo());
		    }
		    //给返回信息赋值
		    if(head!=null){
		    	 resVo=forParams(wftaskVoList,resVo,head.getRequestType());
		    }
		    String resXml=ClaimBaseCoder.objToXml(resVo);//响应的Xml
		    logger.info("自助理赔流程处理查询接口响应XML----->"+resXml);
		}catch(Exception e){
			ResponseHeadVo headVo=new ResponseHeadVo();//头部信息
			headVo.setResponseType(sign);
			headVo.setErrno("0");
			headVo.setErrmsg(e.getMessage());
			resVo.setReqHeadVo(headVo);
			e.printStackTrace();
			logger.info("错误信息"+e.getMessage());
		}
		
	        
		return resVo;
	}
	
   //给返回信息赋值
	private SelfClaimDealFlowResPacketVo forParams(List<PrpLWfTaskVo> taskVos,SelfClaimDealFlowResPacketVo resVo,String accsign)throws Exception{
		ResponseHeadVo headVo=new ResponseHeadVo();//头部信息
		SelfClaimDealFlowResBodyVo bodyVo=new SelfClaimDealFlowResBodyVo();
		List<TaskInfoVo> taskinfoList=new ArrayList<TaskInfoVo>();
		headVo.setErrno("1");
		headVo.setErrmsg("success");
		headVo.setResponseType(accsign);
		if(taskVos!=null && taskVos.size()>0){
			for(PrpLWfTaskVo taskvo:taskVos){
				TaskInfoVo vo=new TaskInfoVo();
				vo.setBussNo(taskvo.getRegistNo());
				vo.setHandlerTime(DateFormatString(taskvo.getHandlerTime()));
				vo.setHandlerUser(taskvo.getHandlerUser());
				if(StringUtils.isNotBlank(taskvo.getHandlerUser())){
					vo.setHandlerUserName(codeTranService.transCode("UserCode",taskvo.getHandlerUser()));
				}
				vo.setNodeName(taskvo.getTaskName());
				vo.setNodeType(taskvo.getNodeCode());
				vo.setTaskInTime(DateFormatString(taskvo.getTaskInTime()));
				vo.setTaskOutTime(DateFormatString(taskvo.getTaskOutTime()));
				vo.setTaskOutUser(taskvo.getTaskOutUser());
				if(StringUtils.isNotBlank(taskvo.getTaskOutUser())){
					vo.setTaskOutUserName(codeTranService.transCode("UserCode",taskvo.getTaskOutUser()));
				}
				vo.setWorkStatus(taskvo.getHandlerStatus());
				taskinfoList.add(vo);
			}
		}
		bodyVo.setTaskInfoVoList(taskinfoList);
		resVo.setReqHeadVo(headVo);
		resVo.setReqBodyVo(bodyVo);
		return resVo;
	}

  //手动注入
  private void init(){
	
		if(wfFlowQueryService==null){
			wfFlowQueryService = (WfFlowQueryService)Springs.getBean(WfFlowQueryService.class);
		}
		if(codeTranService==null){
			codeTranService = (CodeTranService)Springs.getBean(CodeTranService.class);
		}
		
	}
	
	/**
	 * 时间转换方法
	 * Date 类型转换 String类型
	 * @param strDate
	 * @return
	 * @throws ParseException
	 */
	private String DateFormatString(Date strDate){
		String str="";
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		  if(strDate!=null){
			  str=format.format(strDate);
		}
		return str;
	}
}
