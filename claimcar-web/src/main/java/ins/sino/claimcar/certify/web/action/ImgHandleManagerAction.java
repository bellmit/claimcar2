package ins.sino.claimcar.certify.web.action;

import freemarker.template.utility.StringUtil;
import ins.framework.web.AjaxResult;
import ins.platform.common.web.util.WebUserUtils;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.carYxImage.service.CarXyImageService;
import ins.sino.claimcar.certify.service.CertifyService;
import ins.sino.claimcar.certify.vo.PrpLCertifyDirectVo;
import ins.sino.claimcar.certify.vo.PrpLCertifyItemVo;
import ins.sino.claimcar.check.service.CheckTaskService;
import ins.sino.claimcar.check.vo.PrpLCheckCarVo;
import ins.sino.claimcar.check.vo.PrpLCheckPersonVo;
import ins.sino.claimcar.check.vo.PrpLCheckPropVo;
import ins.sino.claimcar.common.web.action.YxImageUploadAction;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.service.WfFlowQueryService;
import ins.sino.claimcar.flow.service.WfTaskHandleService;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.losscar.service.LossCarService;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarMainVo;
import ins.sino.claimcar.lossperson.service.PersTraceDubboService;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersInjuredVo;
import ins.sino.claimcar.lossprop.service.PropTaskService;
import ins.sino.claimcar.lossprop.vo.PrpLdlossPropMainVo;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.SpringProperties;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.sinosoft.image.action.ImgManagerAction;
import com.sinosoft.image.tree.ImgTypeTree;
import com.sinosoft.image.tree.TypeNode;
import com.sinosoft.image.util.ImgEncryptUtils;
import com.sunyard.insurance.encode.client.EncodeAccessParam;


/**
 * 
 * @author dengkk
 * @CreateTime Mar 14, 2016
 */
@Controller
@RequestMapping("/imgManager")
public class ImgHandleManagerAction {
	public Logger logger = LoggerFactory.getLogger(YxImageUploadAction.class);

	@Autowired
	private CertifyService certifyService;
	
	@Autowired
	private CheckTaskService checkTaskService;
	
	@Autowired
	private PersTraceDubboService persTraceDubboService;
	
	@Autowired
	private PropTaskService propTaskService;
	
	@Autowired
	private LossCarService lossCarService;
	
	@Autowired
	private WfFlowQueryService wfFlowQueryService;
	
	@Autowired
	private WfTaskHandleService wfTaskHandleService;
	
	@Autowired
	CarXyImageService carXyImageService;
	@RequestMapping(value = "/uploadCertify.do", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView uploadCertify(HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException {
		ModelAndView modelAndView = new ModelAndView();
		String taskId = request.getParameter("taskId");
		PrpLWfTaskVo prpLWfTaskVo = wfTaskHandleService.queryTask(Double.valueOf(taskId));
		String registNo = prpLWfTaskVo.getRegistNo();
		String subNodeCode = prpLWfTaskVo.getSubNodeCode();
		String handlerStatus = prpLWfTaskVo.getHandlerStatus();
		//上个节点
		PrpLWfTaskVo upPrpLWfTaskVo = wfTaskHandleService.queryTask(prpLWfTaskVo.getUpperTaskId().doubleValue());
		String upWorkStatus = upPrpLWfTaskVo.getWorkStatus();
		String imgServerUrl = SpringProperties.getProperty("IMG_MANAGER_URL");
		//内网地址 
		String imgInUrl = SpringProperties.getProperty("IMG_MANAGER_URL_IN");
		ImgManagerAction imgMgrAction = new ImgManagerAction(request,response);
		imgMgrAction.setBussNo(registNo);
		imgMgrAction.setUserCode(WebUserUtils.getUserCode());
		imgMgrAction.setOperatorCode(WebUserUtils.getUserCode());
		imgMgrAction.setSystemCode("claim");
		imgMgrAction.setComCode(WebUserUtils.getComCode());
		imgMgrAction.setViewType("upload");
		imgMgrAction.setPowerUpload("Y");
		//已处理、已注销、上个节点已退回的任务不能删除资料
		if("3".equals(handlerStatus) || "9".equals(handlerStatus) || "5".equals(upWorkStatus)){
			imgMgrAction.setPowerDelete("N");
			imgMgrAction.setPowerEditPic("N");
		}else{
			imgMgrAction.setPowerDelete("Y");
			imgMgrAction.setPowerEditPic("Y");
		}
		//复勘、复检、定损修改、定损追加、人伤后续跟踪不能删除资料
		if(FlowNode.ChkRe.name().equals(subNodeCode) || FlowNode.DLCarMod.name().equals(subNodeCode)
				|| FlowNode.DLCarAdd.name().equals(subNodeCode) || FlowNode.DLChk.name().equals(subNodeCode)
				|| FlowNode.PLNext.name().equals(subNodeCode)){
			imgMgrAction.setPowerDelete("N");
			imgMgrAction.setPowerEditPic("N");
		}
		imgMgrAction.setUploadNode(subNodeCode);//上传节点
		imgMgrAction.setTypeTreeXML(this.buildImgTree(registNo).bulidTreeXml());
         //跳转到影像系统
		modelAndView.setViewName("redirect:"+imgServerUrl+imgMgrAction.getImgURL(imgInUrl));
		return modelAndView;
	}
	
	@RequestMapping(value = "/viewImg.do", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView viewImg(String bussNo) throws IOException, ServletException {
		ModelAndView modelAndView = new ModelAndView();
		String encodeBussNo = ImgEncryptUtils.encodeStr(bussNo);
		String imgViewUrl = SpringProperties.getProperty("IMG_MANAGER_URL");
         //跳转到影像系统
		modelAndView.setViewName("redirect:"+imgViewUrl+"imgView.do?action=viewByBuss&bussNo="+encodeBussNo);
		return modelAndView;
	}
	
	/**
	 * 上传公估费资料
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 */
	@RequestMapping(value = "/uploadAssessorsFee")
	@ResponseBody
	public ModelAndView uploadAssessorsFee(String bussNo,String handlerStatus){
		ModelAndView mv = new ModelAndView();
		try {
			//获取请求的xml参数
			SysUserVo user = WebUserUtils.getUser();
			String reqXml=carXyImageService.getReqUploadParam(user,CodeConstants.APPROLE,bussNo,CodeConstants.YXASSESSORSFEE,handlerStatus);
			String key=SpringProperties.getProperty("YX_key");
			String Qurl=SpringProperties.getProperty("YX_QUrl");
			//生成请求参数
			String param = EncodeAccessParam.getEncodeParam("format=xml&code="+CodeConstants.YXUPDATECODE+"&xml="+reqXml,20*1000,key);
			String url = Qurl+"?data="+param;
			mv.addObject("url",url);
		} catch (Exception e) {
			logger.info("获取影像上传报错", e);
		}
		mv.setViewName("image/ImgUpload");
		return mv;
		
		
		/*ModelAndView modelAndView = new ModelAndView();
		String bussNo = request.getParameter("bussNo");
		String handlerStatus = request.getParameter("handlerStatus");
		String imgServerUrl = SpringProperties.getProperty("IMG_MANAGER_URL");
		//内网地址 
		String imgInUrl = SpringProperties.getProperty("IMG_MANAGER_URL_IN");
		ImgManagerAction imgMgrAction = new ImgManagerAction(request,response);
		imgMgrAction.setBussNo(bussNo);
		imgMgrAction.setUserCode(WebUserUtils.getUserCode());
		imgMgrAction.setOperatorCode(WebUserUtils.getUserCode());
		imgMgrAction.setSystemCode("claim");
		imgMgrAction.setComCode(WebUserUtils.getComCode());
		imgMgrAction.setViewType("upload");
		imgMgrAction.setPowerUpload("Y");
		imgMgrAction.setPowerEditPic("Y");
		if("3".equals(handlerStatus) || "9".equals(handlerStatus)){
			imgMgrAction.setPowerDelete("N");
		}else{
			imgMgrAction.setPowerDelete("Y");
		}
		ImgTypeTree imgTypeTree = new ImgTypeTree();
		TypeNode claimNode = new TypeNode("claim","理赔");
		imgTypeTree.addNode(claimNode);
		TypeNode certifyNode = new TypeNode("assessorsFee","公估费资料");
		claimNode.addNode(certifyNode);
		imgMgrAction.setTypeTreeXML(imgTypeTree.bulidTreeXml());
         //跳转到影像系统
		modelAndView.setViewName("redirect:"+imgServerUrl+imgMgrAction.getImgURL(imgInUrl));
		return modelAndView;*/
	}
	
	/**
	 * 上传查勘费资料
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 */
	@RequestMapping(value = "/uploadChecksFee")
	@ResponseBody
	public ModelAndView uploadChecksFee(String bussNo,String handlerStatus){
		ModelAndView mv = new ModelAndView();
		try {
			//获取请求的xml参数
			SysUserVo user = WebUserUtils.getUser();
			String reqXml=carXyImageService.getReqUploadParam(user,CodeConstants.APPROLE,bussNo,CodeConstants.YXCHECKSFEE,handlerStatus);
			String key=SpringProperties.getProperty("YX_key");
			String Qurl=SpringProperties.getProperty("YX_QUrl");
			//生成请求参数
			String param = EncodeAccessParam.getEncodeParam("format=xml&code="+CodeConstants.YXUPDATECODE+"&xml="+reqXml,20*1000,key);
			String url = Qurl+"?data="+param;
			mv.addObject("url",url);
		} catch (Exception e) {
			logger.info("获取影像上传报错", e);
		}
		mv.setViewName("image/ImgUpload");
		return mv;
		
	}
	
	/**
	 * 构建报案号下的上传单证目录
	 * @param registNo
	 * @return
	 */
	private ImgTypeTree buildImgTree(String registNo){
		ImgTypeTree imgTypeTree = new ImgTypeTree();
		TypeNode claimNode = new TypeNode("claim","理赔");
		imgTypeTree.addNode(claimNode);
		Map<String,String> map = new HashMap<String,String>();
		map.put("registNo",registNo);
		List<PrpLCertifyItemVo>  prpLCertifyItemVoList = certifyService.findPrpLCertifyItemVoList(map);
		if(prpLCertifyItemVoList != null && prpLCertifyItemVoList.size() > 0){
			TypeNode certifyNode = new TypeNode("certify","索赔清单");
			claimNode.addNode(certifyNode);
	        for(PrpLCertifyItemVo prpLCertifyItemVo:prpLCertifyItemVoList){
	        	TypeNode node=new TypeNode(prpLCertifyItemVo.getCertifyTypeCode(),prpLCertifyItemVo.getCertifyTypeName());
	        	for(PrpLCertifyDirectVo prpLCertifyDirectVo:prpLCertifyItemVo.getPrpLCertifyDirects()){
					TypeNode nodeD=new TypeNode(prpLCertifyDirectVo.getLossItemCode(),prpLCertifyDirectVo.getLossItemName());
					node.addNode(nodeD);
				}
	        	certifyNode.addNode(node);
	        }
		}
        
        TypeNode pictureNode = new TypeNode("picture","损失照片");
        claimNode.addNode(pictureNode);
		Map<String,String> carNodeMap = new HashMap<String,String>();
		Map<String,String> propNodeMap = new HashMap<String,String>();
		Map<String,String> checkPersonNodeMap = new HashMap<String,String>();
		Map<String,String> personNodeMap = new HashMap<String,String>();
		Map<String,String> thirdPersonNodeMap = new HashMap<String,String>();
		String code = "images";//之后查询上传图片数量
		//查勘
		List<PrpLCheckCarVo> prpLCheckCarVoList = checkTaskService.findCheckCarVo(registNo);
		List<PrpLCheckPropVo> prpLCheckPropVoList = checkTaskService.findCheckPropVo(registNo);
		List<PrpLCheckPersonVo> prpLCheckPersonList = checkTaskService.findCheckPersonVo(registNo);
		if(prpLCheckCarVoList != null && prpLCheckCarVoList.size() > 0){
			for(PrpLCheckCarVo prpLCheckCarVo:prpLCheckCarVoList){
            	 String lossItem = prpLCheckCarVo.getSerialNo().toString();//0地面；1标的；3三者
            	 String licenseNo = prpLCheckCarVo.getPrpLCheckCarInfo().getLicenseNo();
            	 if(lossItem.equals("1")){//标的车
            		    licenseNo = "标的车("+licenseNo+")";
		    		}else{
		    			licenseNo = "三者车("+licenseNo+")";
		    		}
            	 carNodeMap.put(code+"_1_"+lossItem,licenseNo);
             }
        }
        if(prpLCheckPropVoList != null && prpLCheckPropVoList.size() > 0){
        	for(PrpLCheckPropVo prpLCheckPropVo:prpLCheckPropVoList){
        		String lossPartyId = prpLCheckPropVo.getLossPartyId().toString();
        		String lossPartyName = prpLCheckPropVo.getLossPartyName();
        		if(lossPartyId.equals("1")){//标的车
        			lossPartyName = "标的车上财产损失("+lossPartyName+")";
	    		}else if(lossPartyId.equals("0")){
	    			lossPartyName = "财产损失("+lossPartyName+")";
	    		}else{
	    			lossPartyName = "三者车上财产损失("+lossPartyName+")";
	    		}
        		propNodeMap.put(code+"_2_"+lossPartyId,lossPartyName);
        	}
        }
        if(prpLCheckPersonList != null && !prpLCheckPersonList.isEmpty()){
        	for(PrpLCheckPersonVo prpLCheckPersonVo:prpLCheckPersonList){
        		String lossPartyId = prpLCheckPersonVo.getLossPartyId().toString();
        		String lossPartyName = "";
        		String codeCode = "";
        		if(lossPartyId.equals("1")){//标的车
        			lossPartyName = "本车人员";
        			codeCode = "subjectPerson";
	    		}else{
	    			lossPartyName = "三者人员";
	    			codeCode = "thirdPerson";
	    		}
        		checkPersonNodeMap.put(codeCode,lossPartyName);
        	}
        }
        
		//定损
        List<PrpLDlossCarMainVo> prpLDlossCarMainVoList = lossCarService.findLossCarMainByRegistNo(registNo);
		if(prpLDlossCarMainVoList != null && prpLDlossCarMainVoList.size() > 0){
			for(PrpLDlossCarMainVo prpLDlossCarMainVo:prpLDlossCarMainVoList){
				if(!CodeConstants.defLossSourceFlag.ADDTIONALDEFLOSS.equals(prpLDlossCarMainVo.getDeflossSourceFlag())){
					String licenseNo = prpLDlossCarMainVo.getLicenseNo();
		    		String serialNo = prpLDlossCarMainVo.getSerialNo().toString();
		    		if(serialNo.equals("1")){//标的车
	        		    licenseNo = "标的车("+licenseNo+")";
		    		}else{
		    			licenseNo = "三者车("+licenseNo+")";
		    		}
		    		carNodeMap.put(code+"_1_"+serialNo,licenseNo);
				}
			}
		}
		List<PrpLdlossPropMainVo> prpLdlossPropMainVoList = propTaskService.findPropMainListByRegistNo(registNo);
		if(prpLdlossPropMainVoList != null && prpLdlossPropMainVoList.size() > 0){
			for(PrpLdlossPropMainVo prpLdlossPropMainVo:prpLdlossPropMainVoList){
				if(!CodeConstants.defLossSourceFlag.ADDTIONALDEFLOSS.equals(prpLdlossPropMainVo.getDeflossSourceFlag())){
					String serialNo = prpLdlossPropMainVo.getSerialNo().toString();
	        		String licenseNo = prpLdlossPropMainVo.getLicense();
	        		if(serialNo.equals("1")){//标的车
	        		    licenseNo = "标的车上财产损失("+licenseNo+")";
		    		}else if(serialNo.equals("0")){
		    			licenseNo = "财产损失("+licenseNo+")";
		    		}else{
		    			licenseNo = "三者车上财产损失("+licenseNo+")";
		    		}
	        		propNodeMap.put(code+"_2_"+serialNo,licenseNo);
				}
			}
		}
        
		//人伤
		List<PrpLDlossPersInjuredVo> prpLDlossPersInjuredVoList= persTraceDubboService.findPersInjuredByRegistNo(registNo);
        if(prpLDlossPersInjuredVoList != null && prpLDlossPersInjuredVoList.size() > 0){
        	for(PrpLDlossPersInjuredVo prpLDlossPersInjuredVo:prpLDlossPersInjuredVoList){
        		String personName = prpLDlossPersInjuredVo.getPersonName();
        		String personId = prpLDlossPersInjuredVo.getId().toString();
        		int serialNo = prpLDlossPersInjuredVo.getSerialNo();
        		if(serialNo == 1){//标的车
        			personNodeMap.put(code+"_3_"+StringUtil.replace(personId, "-", "_"), personName);
        		}else{
        			thirdPersonNodeMap.put(code+"_3_"+StringUtil.replace(personId, "-", "_"), personName);
        		}
        	}
        }
        
        if(carNodeMap.size() > 0){
        	TypeNode node=new TypeNode("carLoss","车辆损失");
			pictureNode.addNode(node);
			for(String key:carNodeMap.keySet()){
				TypeNode nodeTem=new TypeNode(key,carNodeMap.get(key));
				node.addNode(nodeTem);
			}
        }
        
        if(propNodeMap.size() > 0){
        	TypeNode node=new TypeNode("propLoss","财产损失");
			pictureNode.addNode(node);
			for(String key:propNodeMap.keySet()){
				TypeNode nodeTem=new TypeNode(key,propNodeMap.get(key));
				node.addNode(nodeTem);
			}
        }
        
        if(checkPersonNodeMap.size() > 0){
        	TypeNode node=new TypeNode("personLoss","人伤");
        	pictureNode.addNode(node);
        	for(String key:checkPersonNodeMap.keySet()){
				TypeNode nodeTem=new TypeNode(key,checkPersonNodeMap.get(key));
				node.addNode(nodeTem);
			}
        	TypeNode subNode=new TypeNode("subPersonLoss","人员伤亡");
        	node.addNode(subNode);
        }
        
        if(personNodeMap.size() > 0 || thirdPersonNodeMap.size() > 0){
        	TypeNode node=new TypeNode("personLoss","人员损失");
			pictureNode.addNode(node);
			if(personNodeMap.size() > 0){
				TypeNode subjectNode=new TypeNode("subjectPerson","本车人员");
				node.addNode(subjectNode);
				for(String key:personNodeMap.keySet()){
					TypeNode nodeTem=new TypeNode(key,personNodeMap.get(key));
					subjectNode.addNode(nodeTem);
				}
			}
			if(thirdPersonNodeMap.size() > 0){
				TypeNode thirdNode=new TypeNode("thirdPerson","三者人员");
				node.addNode(thirdNode);
				for(String key:thirdPersonNodeMap.keySet()){
					TypeNode nodeTem=new TypeNode(key,thirdPersonNodeMap.get(key));
					thirdNode.addNode(nodeTem);
				}
			}
        }
        
        //复勘
        if(wfFlowQueryService.findPrpWfTaskVo(registNo, FlowNode.Check.name() , FlowNode.ChkRe.name()).size() > 0){
        	TypeNode node=new TypeNode(code+"_4","复勘");
			pictureNode.addNode(node);
        }
        
        //复检
        if(wfFlowQueryService.findPrpWfTaskVo(registNo, FlowNode.DLoss.name() , FlowNode.DLChk.name()).size() > 0){
        	TypeNode node=new TypeNode(code+"_5","复检");
			pictureNode.addNode(node);
        }
        
        //现场照片
        TypeNode sceneNode=new TypeNode("scenePicture","现场照片");
        pictureNode.addNode(sceneNode);
        
        //查勘报告
        TypeNode checkNode=new TypeNode("checkReport","查勘报告");
        pictureNode.addNode(checkNode);
        
		return imgTypeTree;
	}
	
}
