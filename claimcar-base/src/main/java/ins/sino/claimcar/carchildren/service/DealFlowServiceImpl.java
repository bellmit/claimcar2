package ins.sino.claimcar.carchildren.service;





import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import ins.framework.lang.Springs;
import ins.platform.common.service.facade.CodeTranService;
import ins.sino.claimcar.carchildren.vo.CarchildReqHeadVo;
import ins.sino.claimcar.carchildren.vo.CarchildResHeadVo;
import ins.sino.claimcar.carchildren.vo.DealFlowInfoReqVo;
import ins.sino.claimcar.carchildren.vo.DealFlowInfoResBody;
import ins.sino.claimcar.carchildren.vo.DealFlowInfoResVo;
import ins.sino.claimcar.carchildren.vo.TaskinfoVo;
import ins.sino.claimcar.flow.service.WfFlowQueryService;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;

import com.alibaba.dubbo.common.utils.Assert;
import com.sinosoft.api.service.ServiceInterface;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import com.thoughtworks.xstream.io.xml.XppDriver;

public class DealFlowServiceImpl implements ServiceInterface {
	private static Logger logger = LoggerFactory.getLogger(DealFlowServiceImpl.class);
	@Autowired
	WfFlowQueryService wfFlowQueryService;
	@Autowired
	CodeTranService codeTranService;

	

	@Override
	public Object service(String paramString, Object paramObject) {
		init();
		DealFlowInfoResVo resVo=new DealFlowInfoResVo();
		String sign="";
		try{
			
			XStream stream = new XStream(new XppDriver(new XmlFriendlyNameCoder("-_","_")));
			stream.autodetectAnnotations(true);
			stream.setMode(XStream.NO_REFERENCES);
			stream.aliasSystemAttribute(null,"class");// 去掉 class属性
			stream.processAnnotations(DealFlowInfoReqVo.class);
			DealFlowInfoReqVo reqVo=(DealFlowInfoReqVo)paramObject;
			Assert.notNull(reqVo,"请求信息为空 ");
			CarchildReqHeadVo head = reqVo.getHead();
			sign=head.getRequestType();
		    Assert.notNull(head,"请求信息头部为空 ");
		        if( (!"MTA_005".equals(head.getRequestType()) && !"CT_005".equals(head.getRequestType()))|| !"claim_user".equals(head.getUser())|| !"claim_psd".equals(head.getPassWord())){
		            throw new IllegalArgumentException("请求头参数错误  ");
		        }
		    Assert.notNull(reqVo.getBody(),"请求信息Body信息为空 ");
		    //查询工作流任务
		    List<PrpLWfTaskVo> wftaskVoList=wfFlowQueryService.findTaskVoForInAndOutByRegistNo(reqVo.getBody().getRegistNo());
		   
		    //给返回信息赋值
		    assignmentForReq(wftaskVoList,resVo,head.getRequestType());
		    
		}catch(Exception e){
			CarchildResHeadVo headVo=new CarchildResHeadVo();//头部信息
			headVo.setResponsetype(sign);
			headVo.setErrNo("其他");
			headVo.setErrMsg(e.getMessage());
			resVo.setHead(headVo);
			e.printStackTrace();
			logger.info("错误信息"+e.getMessage());
		}
		
	        
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
	//给返回信息赋值
	private void assignmentForReq(List<PrpLWfTaskVo> taskVos,DealFlowInfoResVo resVo,String accsign)throws Exception{
		CarchildResHeadVo headVo=new CarchildResHeadVo();//头部信息
		DealFlowInfoResBody bodyVo=new DealFlowInfoResBody();
		List<TaskinfoVo> taskinfoList=new ArrayList<TaskinfoVo>();
		headVo.setErrNo("1");
		headVo.setErrMsg("success");
		headVo.setResponsetype(accsign);
		if(taskVos!=null && taskVos.size()>0){
			for(PrpLWfTaskVo taskvo:taskVos){
				TaskinfoVo vo=new TaskinfoVo();
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
		bodyVo.setTasklist(taskinfoList);
		resVo.setHead(headVo);
		resVo.setBody(bodyVo);
		
	}
	
	/**
	 * 时间转换方法
	 * Date 类型转换 String类型
	 * @param strDate
	 * @return
	 * @throws ParseException
	 */
	private String DateFormatString(Date strDate) throws ParseException{
		String str="";
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		  if(strDate!=null){
			  str=format.format(strDate);
		}
		return str;
	}
   
}
