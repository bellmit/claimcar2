package ins.sino.claimcar.xydImageUpload.service;

import freemarker.template.utility.StringUtil;
import ins.platform.common.service.facade.CodeTranService;
import ins.platform.utils.ClaimBaseCoder;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.TypeNode;
import ins.sino.claimcar.carYxImage.service.CarXyImageService;
import ins.sino.claimcar.certify.service.CertifyService;
import ins.sino.claimcar.certify.vo.PrpLCertifyDirectVo;
import ins.sino.claimcar.certify.vo.PrpLCertifyItemVo;
import ins.sino.claimcar.check.service.CheckTaskService;
import ins.sino.claimcar.check.vo.PrpLCheckCarVo;
import ins.sino.claimcar.check.vo.PrpLCheckPersonVo;
import ins.sino.claimcar.check.vo.PrpLCheckPropVo;
import ins.sino.claimcar.claim.service.CompensateTaskService;
import ins.sino.claimcar.claim.vo.PrpLCompensateVo;
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
import ins.sino.claimcar.regist.service.RegistService;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;
import ins.sino.claimcar.sunyardimage.vo.common.BaseDataVo;
import ins.sino.claimcar.sunyardimage.vo.request.ReqBatchQueryVo;
import ins.sino.claimcar.sunyardimage.vo.request.ReqBatchUpdateVo;
import ins.sino.claimcar.sunyardimage.vo.request.ReqBatchVo;
import ins.sino.claimcar.sunyardimage.vo.request.ReqFatherNodeVo;
import ins.sino.claimcar.sunyardimage.vo.request.ReqImageBaseQueryRootVo;
import ins.sino.claimcar.sunyardimage.vo.request.ReqImageBaseRootVo;
import ins.sino.claimcar.sunyardimage.vo.request.ReqImageBaseUpdateRootVo;
import ins.sino.claimcar.sunyardimage.vo.request.ReqMetaDataQueryVo;
import ins.sino.claimcar.sunyardimage.vo.request.ReqMetaDataUpdateVo;
import ins.sino.claimcar.sunyardimage.vo.request.ReqMetaDataVo;
import ins.sino.claimcar.sunyardimage.vo.request.ReqPageNodeVo;
import ins.sino.claimcar.sunyardimage.vo.request.ReqPageVo;
import ins.sino.claimcar.sunyardimage.vo.request.ReqPagesVo;
import ins.sino.claimcar.sunyardimage.vo.request.ReqParameterVo;
import ins.sino.claimcar.sunyardimage.vo.request.ReqPhotoVo;
import ins.sino.claimcar.sunyardimage.vo.request.ReqSonNodeVo;
import ins.sino.claimcar.sunyardimage.vo.request.ReqVtreeVo;
import ins.sino.claimcar.vat.service.BilllclaimService;
import ins.sino.claimcar.vat.vo.PrpLbillcontVo;
import ins.sino.claimcar.vat.vo.PrplAcbillcontVo;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ws.rs.Path;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.SpringProperties;











import com.alibaba.dubbo.config.annotation.Service;
import com.sunyard.insurance.encode.client.EncodeAccessParam;

@Service(protocol = { "dubbo" }, validation = "true", registry = { "default" }, timeout = 1000000)
@Path("carXyImageService")
public class CarXyImageServiceImpl implements CarXyImageService{
	private Logger logger = LoggerFactory.getLogger(CarXyImageServiceImpl.class);
	@Autowired
	RegistService registService;
	@Autowired
	CodeTranService codeTranService;
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
	private CompensateTaskService compensateTaskService;
	
	@Autowired
	private BilllclaimService billlclaimService;

	@Override
	public String getReqQueryParam(SysUserVo user, String role,String bussNo) {
		PrpLRegistVo prpLRegistVo=registService.findRegistByRegistNo(bussNo);
		ReqImageBaseQueryRootVo rootVo=new ReqImageBaseQueryRootVo();
		BaseDataVo baseDataVo=new BaseDataVo();
		baseDataVo.setComCode("00000000");
		if(StringUtils.isNotBlank(bussNo) && (bussNo.startsWith("7") || bussNo.startsWith("Y"))){
        	PrpLCompensateVo prpLCompensateVo=compensateTaskService.findCompByPK(bussNo);
        	if(prpLCompensateVo!=null){
				  baseDataVo.setOrgCode(prpLCompensateVo.getComCode());
				  baseDataVo.setOrgName(codeTranService.transCode("ComCode",prpLCompensateVo.getComCode()));
				}else{
				  baseDataVo.setOrgCode(user.getComCode());
				  baseDataVo.setOrgName(codeTranService.transCode("ComCode",user.getComCode()));
			}
		}else{
			if(prpLRegistVo!=null){
				  baseDataVo.setOrgCode(prpLRegistVo.getComCode());
				  baseDataVo.setOrgName(codeTranService.transCode("ComCode",prpLRegistVo.getComCode()));
				}else{
				  baseDataVo.setOrgCode(user.getComCode());
				  baseDataVo.setOrgName(codeTranService.transCode("ComCode",user.getComCode()));
			}
		}
		baseDataVo.setUserCode(user.getUserCode());
		baseDataVo.setUserName(user.getUserName());
		baseDataVo.setRoleCode(role);
		rootVo.setBaseDataVo(baseDataVo);
		ReqMetaDataQueryVo  metaDataQueryVo=new ReqMetaDataQueryVo();
		List<ReqBatchQueryVo> batchQueryVos=new ArrayList<ReqBatchQueryVo>();
		ReqBatchQueryVo BatchQueryVo=new ReqBatchQueryVo();
		BatchQueryVo.setBusiNo(bussNo);
		BatchQueryVo.setAppCode(CodeConstants.APPCODECLAIM);
		BatchQueryVo.setAppName(CodeConstants.APPNAMECLAIM);
		batchQueryVos.add(BatchQueryVo);
		metaDataQueryVo.setBatchQueryVos(batchQueryVos);
		rootVo.setMetaDataQueryVo(metaDataQueryVo);
		String ReqXml=ClaimBaseCoder.objToXmlUtf(rootVo);
		logger.info("???????????????????????????????????????"+ReqXml);
		return ReqXml;
	}

	/**
	 * ???????????????????????????????????????
	 * @param registNo
	 * @return
	 */
	public TypeNode buildTree(String registNo){
		TypeNode claimNode = new TypeNode("claim","??????");
		try{
		PrpLRegistVo prpLRegistVo=registService.findRegistByRegistNo(registNo);

		Map<String,String> map = new HashMap<String,String>();
		map.put("registNo",registNo);
		List<PrpLCertifyItemVo>  prpLCertifyItemVoList = certifyService.findPrpLCertifyItemVoList(map);
		if(prpLCertifyItemVoList != null && prpLCertifyItemVoList.size() > 0){
			TypeNode certifyNode = new TypeNode("certify","????????????-????????????");
			for(PrpLCertifyItemVo prpLCertifyItemVo:prpLCertifyItemVoList){
	        	TypeNode node=new TypeNode("certify_"+prpLCertifyItemVo.getCertifyTypeCode(),this.replaceBlank(prpLCertifyItemVo.getCertifyTypeName()));
	        	for(PrpLCertifyDirectVo prpLCertifyDirectVo:prpLCertifyItemVo.getPrpLCertifyDirects()){
					TypeNode nodeD=new TypeNode("certify_"+prpLCertifyDirectVo.getLossItemCode(),this.replaceBlank(prpLCertifyDirectVo.getLossItemName()));
					node.addNode(nodeD);
				}
	        	certifyNode.addNode(node);
	        }
			claimNode.addNode(certifyNode);
		}

        TypeNode pictureNode = new TypeNode("picture","????????????-????????????");
		Map<String,String> carNodeMap = new HashMap<String,String>();
		Map<String,String> propNodeMap = new HashMap<String,String>();
		Map<String,String> checkPersonNodeMap = new HashMap<String,String>();
		Map<String,String> personNodeMap = new HashMap<String,String>();
		Map<String,String> thirdPersonNodeMap = new HashMap<String,String>();
		String code = "images";//??????????????????????????????
		//??????
		List<PrpLCheckCarVo> prpLCheckCarVoList = checkTaskService.findCheckCarVo(registNo);
		List<PrpLCheckPropVo> prpLCheckPropVoList = checkTaskService.findCheckPropVo(registNo);
		List<PrpLCheckPersonVo> prpLCheckPersonList = checkTaskService.findCheckPersonVo(registNo);
		if(prpLCheckCarVoList != null && prpLCheckCarVoList.size() > 0){
			for(PrpLCheckCarVo prpLCheckCarVo:prpLCheckCarVoList){
            	 String lossItem = prpLCheckCarVo.getSerialNo().toString();//0?????????1?????????3??????
            	 String licenseNo = prpLCheckCarVo.getPrpLCheckCarInfo().getLicenseNo();
            	 if(lossItem.equals("1")){//?????????
            		    licenseNo = "?????????("+licenseNo+")";
		    		}else{
		    			licenseNo = "?????????("+licenseNo+")";
		    		}
            	 carNodeMap.put(code+"_1_"+lossItem,licenseNo);
             }
        }
        if(prpLCheckPropVoList != null && prpLCheckPropVoList.size() > 0){
        	for(PrpLCheckPropVo prpLCheckPropVo:prpLCheckPropVoList){
        		String lossPartyId = prpLCheckPropVo.getLossPartyId().toString();
        		String lossPartyName = prpLCheckPropVo.getLossPartyName();
        		// ????????????lossPartyId = "2"??? ???????????????????????????????????????????????????
        		if ("2".equals(lossPartyId)) {
        			lossPartyId = "1";
        		}
        		if(lossPartyId.equals("1")){//?????????
        			lossPartyName = "????????????????????????("+lossPartyName+")";
	    		}else if(lossPartyId.equals("0")){
	    			lossPartyName = "????????????("+lossPartyName+")";
	    		}else{
	    			lossPartyName = "????????????????????????("+lossPartyName+")";
	    		}
        		propNodeMap.put(code+"_2_"+lossPartyId,lossPartyName);
        	}
        }
        if(prpLCheckPersonList != null && !prpLCheckPersonList.isEmpty()){
        	for(PrpLCheckPersonVo prpLCheckPersonVo:prpLCheckPersonList){
        		String lossPartyId = prpLCheckPersonVo.getLossPartyId().toString();
        		String lossPartyName = "";
        		String codeCode = "";
        		if(lossPartyId.equals("1")){//?????????
        			lossPartyName = "????????????";
        			//codeCode = "subjectPerson_"+lossPartyId;
        			codeCode = "subjectPerson";
	    		}else{
	    			lossPartyName = "????????????";
	    			//codeCode = "thirdPerson_"+lossPartyId;
	    			codeCode = "thirdPerson";

	    		}
        		checkPersonNodeMap.put(codeCode,lossPartyName);
        	}
        }

		//??????
        List<PrpLDlossCarMainVo> prpLDlossCarMainVoList = lossCarService.findLossCarMainByRegistNo(registNo);
		if(prpLDlossCarMainVoList != null && prpLDlossCarMainVoList.size() > 0){
			for(PrpLDlossCarMainVo prpLDlossCarMainVo:prpLDlossCarMainVoList){
				if(!CodeConstants.defLossSourceFlag.ADDTIONALDEFLOSS.equals(prpLDlossCarMainVo.getDeflossSourceFlag())){
					String licenseNo = prpLDlossCarMainVo.getLicenseNo();
		    		String serialNo = prpLDlossCarMainVo.getSerialNo().toString();
		    		if(serialNo.equals("1")){//?????????
	        		    licenseNo = "?????????("+licenseNo+")";
		    		}else{
		    			licenseNo = "?????????("+licenseNo+")";
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
	        		if(serialNo.equals("1")){//?????????
	        		    licenseNo = "????????????????????????("+licenseNo+")";
		    		}else if(serialNo.equals("0")){
		    			licenseNo = "????????????("+licenseNo+")";
		    		}else{
		    			licenseNo = "????????????????????????("+licenseNo+")";
		    		}
	        		propNodeMap.put(code+"_2_"+serialNo,licenseNo);
				}
			}
		}

		//??????
		List<PrpLDlossPersInjuredVo> prpLDlossPersInjuredVoList= persTraceDubboService.findPersInjuredByRegistNo(registNo);
        if(prpLDlossPersInjuredVoList != null && prpLDlossPersInjuredVoList.size() > 0){
        	for(PrpLDlossPersInjuredVo prpLDlossPersInjuredVo:prpLDlossPersInjuredVoList){
        		String personName = this.replaceBlank(prpLDlossPersInjuredVo.getPersonName());
        		String personId = prpLDlossPersInjuredVo.getId().toString();
        		int serialNo = prpLDlossPersInjuredVo.getSerialNo();
        		if(serialNo == 1){//?????????
        			personNodeMap.put(code+"_3_"+StringUtil.replace(personId, "-", "_"), personName);
        		}else{
        			thirdPersonNodeMap.put(code+"_3_"+StringUtil.replace(personId, "-", "_"), personName);
        		}
        	}
        }

        if(carNodeMap.size() > 0){
        	TypeNode node=new TypeNode("carLoss","????????????");
			pictureNode.addNode(node);
			for(String key:carNodeMap.keySet()){
				TypeNode nodeTem=new TypeNode(key,carNodeMap.get(key));
				node.addNode(nodeTem);
			}
        }

        if(propNodeMap.size() > 0){
        	TypeNode node=new TypeNode("propLoss","????????????");
			pictureNode.addNode(node);
			for(String key:propNodeMap.keySet()){
				TypeNode nodeTem=new TypeNode(key,propNodeMap.get(key));
				node.addNode(nodeTem);
			}
        }

        if(checkPersonNodeMap.size() > 0){
        	TypeNode node=new TypeNode("personLoss","??????");
        	pictureNode.addNode(node);
        	for(String key:checkPersonNodeMap.keySet()){
				TypeNode nodeTem=new TypeNode(key,checkPersonNodeMap.get(key));
				node.addNode(nodeTem);
			}
        	TypeNode subNode=new TypeNode("subPersonLoss","????????????");
        	node.addNode(subNode);
        }

        if(personNodeMap.size() > 0 || thirdPersonNodeMap.size() > 0){
        	TypeNode node=new TypeNode("personLoss_p","????????????");
			pictureNode.addNode(node);
			if(personNodeMap.size() > 0){
				TypeNode subjectNode=new TypeNode("subjectPerson","????????????");
				node.addNode(subjectNode);
				for(String key:personNodeMap.keySet()){
					TypeNode nodeTem=new TypeNode(key,personNodeMap.get(key));
					subjectNode.addNode(nodeTem);
				}
			}
			if(thirdPersonNodeMap.size() > 0){
				TypeNode thirdNode=new TypeNode("thirdPerson","????????????");
				node.addNode(thirdNode);
				for(String key:thirdPersonNodeMap.keySet()){
					TypeNode nodeTem=new TypeNode(key,thirdPersonNodeMap.get(key));
					thirdNode.addNode(nodeTem);
				}
			}
        }

        //??????
        if(wfFlowQueryService.findPrpWfTaskVo(registNo, FlowNode.Check.name() , FlowNode.ChkRe.name()).size() > 0){
        	TypeNode node=new TypeNode(code+"_4","??????");
        	TypeNode nodeF=new TypeNode (code+"_4_1","????????????");
        	node.addNode(nodeF);
			pictureNode.addNode(node);
        }

        //??????
        if(wfFlowQueryService.findPrpWfTaskVo(registNo, FlowNode.DLoss.name() , FlowNode.DLChk.name()).size() > 0){
        	TypeNode node=new TypeNode(code+"_5","??????");
        	TypeNode nodeF=new TypeNode (code+"_5_1","????????????");
        	node.addNode(nodeF);
			pictureNode.addNode(node);
        }

        //????????????
        TypeNode sceneNode=new TypeNode("scenePicture","??????");
        TypeNode sceneNodeN=new TypeNode ("scenePicture_1","????????????");
        sceneNode.addNode(sceneNodeN);
        pictureNode.addNode(sceneNode);

        //????????????
        TypeNode checkNode=new TypeNode("checkReport","????????????");
        TypeNode checkNodeC=new TypeNode ("checkReport_1","??????????????????");
        checkNode.addNode(checkNodeC);
        pictureNode.addNode(checkNode);
        //????????????
        if(prpLRegistVo!=null && ("1".equals(prpLRegistVo.getIsQuickCase()) || "2".equals(prpLRegistVo.getIsQuickCase()))){
	        TypeNode hnNode=new TypeNode(CodeConstants.HNKPFNODE,"????????????????????????");
	        TypeNode subNode1=new TypeNode(CodeConstants.HNKPSNODE1,"??????????????????");
	        TypeNode subNode2=new TypeNode(CodeConstants.HNKPSNODE2,"???????????????");
	        TypeNode subNode3=new TypeNode(CodeConstants.HNKPSNODE3,"??????????????????");
	        hnNode.addNode(subNode1);
	        hnNode.addNode(subNode2);
	        hnNode.addNode(subNode3);
	        pictureNode.addNode(hnNode);
        }
        //???????????????
        TypeNode assessorNode=new TypeNode("assessorsFee","?????????");
        TypeNode assessorNodeA=new TypeNode("assessorsFee_1","???????????????");
        assessorNode.addNode(assessorNodeA);
        pictureNode.addNode(assessorNode);

        //???????????????
        TypeNode checkFeeNode=new TypeNode("checksFee","?????????");
        TypeNode checkFeeNodeA=new TypeNode("checksFee_1","???????????????");
        checkFeeNode.addNode(checkFeeNodeA);
        pictureNode.addNode(checkFeeNode);
        claimNode.addNode(pictureNode);
	}catch(Exception e){
		 e.printStackTrace();
	}
		return claimNode;
	}


	@Override
	public String getReqUploadParam(SysUserVo user, String role,String taskId,String flags,String handlerStatus) {
		String bussNo = taskId;
		String power = CodeConstants.YXRIGHTCODED;
		if(CodeConstants.YXQUERYPARAM.equals(flags)){
			PrpLWfTaskVo prpLWfTaskVo = wfTaskHandleService.queryTask(Double.valueOf(taskId));
			//????????????
			PrpLWfTaskVo upPrpLWfTaskVo = wfTaskHandleService.queryTask(prpLWfTaskVo.getUpperTaskId().doubleValue());
			String upWorkStatus = upPrpLWfTaskVo.getWorkStatus();
			bussNo = prpLWfTaskVo.getRegistNo();
			String subNodeCode = prpLWfTaskVo.getSubNodeCode();

			handlerStatus = prpLWfTaskVo.getHandlerStatus();
			//????????????????????????????????????????????????????????????????????????
			if("3".equals(handlerStatus) || "9".equals(handlerStatus) || "5".equals(upWorkStatus)){
				power = CodeConstants.YXRIGHTCODE;
			}
			//????????????????????????????????????????????????????????????????????????????????????
			if(FlowNode.ChkRe.name().equals(subNodeCode) || FlowNode.DLCarMod.name().equals(subNodeCode)
					|| FlowNode.DLCarAdd.name().equals(subNodeCode) || FlowNode.DLChk.name().equals(subNodeCode)
					|| FlowNode.PLNext.name().equals(subNodeCode)){
				power = CodeConstants.YXRIGHTCODE;
			}

		}else{
			if("3".equals(handlerStatus) || "9".equals(handlerStatus)){
				power = CodeConstants.YXRIGHTCODE;
			}
		}
		PrpLRegistVo prpLRegistVo=registService.findRegistByRegistNo(bussNo);
		ReqImageBaseUpdateRootVo rootVo=new ReqImageBaseUpdateRootVo();
		BaseDataVo baseDataVo=new BaseDataVo();
		baseDataVo.setComCode("00000000");
		if(prpLRegistVo!=null){
		  baseDataVo.setOrgCode(prpLRegistVo.getComCode());
		  baseDataVo.setOrgName(codeTranService.transCode("ComCode",prpLRegistVo.getComCode()));
		}else{
		  baseDataVo.setOrgCode(user.getComCode());
		  baseDataVo.setOrgName(codeTranService.transCode("ComCode",user.getComCode()));
		}
		baseDataVo.setUserCode(user.getUserCode());
		baseDataVo.setUserName(user.getUserName());
		baseDataVo.setRoleCode(role);
		rootVo.setBaseDataVo(baseDataVo);
		ReqMetaDataUpdateVo  metaDataUpdateVo=new ReqMetaDataUpdateVo();
		List<ReqBatchUpdateVo> batchUpdateVos=new ArrayList<ReqBatchUpdateVo>();
		TypeNode typeNode=null;
		if(CodeConstants.YXQUERYPARAM.equals(flags)){
			typeNode=this.buildTree(bussNo);
		}else if(CodeConstants.YXASSESSORSFEE.equals(flags)){
			typeNode=this.buildAssessorsFeeTree(bussNo);
		}else if(CodeConstants.YXCHECKSFEE.equals(flags)){
			typeNode=this.buildChecksFeeTree(bussNo);
		}
		if(typeNode!=null){
			if(typeNode.getChildNodes()!=null && typeNode.getChildNodes().size()>0){
				ReqBatchUpdateVo BatchUpdateVo=new ReqBatchUpdateVo();
				BatchUpdateVo.setBusiNo(bussNo);
				BatchUpdateVo.setAppCode(CodeConstants.APPCODECLAIM);
				BatchUpdateVo.setAppName(CodeConstants.APPNAMECLAIM);
				ReqVtreeVo vtreeVo=new ReqVtreeVo();
				vtreeVo.setAppCode(CodeConstants.APPCODECLAIM);
				vtreeVo.setAppName(CodeConstants.APPNAMECLAIM);
				List<ReqFatherNodeVo> fatherNodeVos=new ArrayList<ReqFatherNodeVo>();
				if(CodeConstants.YXQUERYPARAM.equals(flags)){
					for(TypeNode typeNodeL1:typeNode.getChildNodes()){
						ReqFatherNodeVo fatherNodeVo=new ReqFatherNodeVo();
						fatherNodeVo.setId("claim-"+typeNodeL1.getTypeCode());
						fatherNodeVo.setName(typeNodeL1.getTypeName());
						fatherNodeVo.setBarCode("");
						fatherNodeVo.setChildFlag(CodeConstants.IsSingleAccident.NOT);
						fatherNodeVo.setMinpages("0");
						fatherNodeVo.setMaxpages("99999");
						fatherNodeVo.setReseize("800*600");
						fatherNodeVo.setRight(power);
						if(typeNodeL1.getChildNodes()!=null && typeNodeL1.getChildNodes().size()>0){
							List<ReqSonNodeVo> sonNodeVos1=new ArrayList<ReqSonNodeVo>();
							for(TypeNode typeNodeL2:typeNodeL1.getChildNodes()){
								ReqSonNodeVo sonNodeVo1 = this.setReqSonNodeVo(typeNodeL2,power);
								List<ReqSonNodeVo> sonNodeVos=new ArrayList<ReqSonNodeVo>();
								if(typeNodeL2.getChildNodes()!=null && typeNodeL2.getChildNodes().size()>0){
									for(TypeNode typeNodeL3:typeNodeL2.getChildNodes()){
										ReqSonNodeVo sonNodeVo = this.setReqSonNodeVo(typeNodeL3,power);
										List<ReqSonNodeVo> sonNodeVos4=new ArrayList<ReqSonNodeVo>();
										if(typeNodeL3.getChildNodes()!=null && typeNodeL3.getChildNodes().size()>0){
											for(TypeNode typeNodeL4:typeNodeL3.getChildNodes()){
												ReqSonNodeVo sonNodeVo4 = this.setReqSonNodeVo(typeNodeL4,power);
												sonNodeVo4.setChildFlag(CodeConstants.IsSingleAccident.YES);
												sonNodeVos4.add(sonNodeVo4);
											}
											sonNodeVo.setSonNodes(sonNodeVos4);
										}else{
											sonNodeVo.setChildFlag(CodeConstants.IsSingleAccident.YES);
										}
										sonNodeVos.add(sonNodeVo);
									}
									sonNodeVo1.setSonNodes(sonNodeVos);
								}else{
									sonNodeVo1.setChildFlag(CodeConstants.IsSingleAccident.YES);
								}
								sonNodeVos1.add(sonNodeVo1);
							}
							fatherNodeVo.setSonNodes(sonNodeVos1);
						}else{
							fatherNodeVo.setChildFlag(CodeConstants.IsSingleAccident.YES);
						}
						fatherNodeVos.add(fatherNodeVo);
					}
				}else{
					for(TypeNode typeNodeL1:typeNode.getChildNodes()){
						ReqFatherNodeVo fatherNodeVo=new ReqFatherNodeVo();
						fatherNodeVo.setId(typeNodeL1.getTypeCode());
						fatherNodeVo.setName(typeNodeL1.getTypeName());
						fatherNodeVo.setBarCode("");
						fatherNodeVo.setChildFlag("1");
						fatherNodeVo.setMinpages("0");
						fatherNodeVo.setMaxpages("99999");
						fatherNodeVo.setReseize("800*600");
						fatherNodeVo.setRight(power);
						fatherNodeVos.add(fatherNodeVo);
					}
				}
				vtreeVo.setFatherNodeVos(fatherNodeVos);
				BatchUpdateVo.setVtreeVo(vtreeVo);
				batchUpdateVos.add(BatchUpdateVo);
			}

		}
		metaDataUpdateVo.setBatchUpdateVos(batchUpdateVos);
		rootVo.setMetaDataUpdateVo(metaDataUpdateVo);
		String ReqXml=ClaimBaseCoder.objToXmlUtf(rootVo);
		logger.info("ReqXml========="+ReqXml);
		return ReqXml;
	}

	public TypeNode buildImageTree(String registNo){

		TypeNode claimNode = new TypeNode("claim","??????");
		Map<String,String> map = new HashMap<String,String>();
		map.put("registNo",registNo);
		TypeNode certifyNode = new TypeNode("certify","????????????");
		TypeNode cerNode=new TypeNode("C08","??????????????????????????????");
    	TypeNode nodeD=new TypeNode("C0803","?????????");
    	cerNode.addNode(nodeD);
    	certifyNode.addNode(cerNode);
    	claimNode.addNode(certifyNode);
        TypeNode pictureNode = new TypeNode("picture","????????????-????????????");

		Map<String,String> carNodeMap = new HashMap<String,String>();
		String code = "images";//??????????????????????????????
		//??????
		List<PrpLCheckCarVo> prpLCheckCarVoList = checkTaskService.findCheckCarVo(registNo);
		if(prpLCheckCarVoList != null && prpLCheckCarVoList.size() > 0){
			for(PrpLCheckCarVo prpLCheckCarVo:prpLCheckCarVoList){
            	 String lossItem = prpLCheckCarVo.getSerialNo().toString();//0?????????1?????????3??????
            	 String licenseNo = prpLCheckCarVo.getPrpLCheckCarInfo().getLicenseNo();
            	 if(lossItem.equals("1")){//?????????
            		    licenseNo = "?????????("+licenseNo+")";
		    		}else{
		    			licenseNo = "?????????("+licenseNo+")";
		    		}
            	 carNodeMap.put(code+"_1_"+lossItem,licenseNo);
             }
        }

		//??????
        List<PrpLDlossCarMainVo> prpLDlossCarMainVoList = lossCarService.findLossCarMainByRegistNo(registNo);
		if(prpLDlossCarMainVoList != null && prpLDlossCarMainVoList.size() > 0){
			for(PrpLDlossCarMainVo prpLDlossCarMainVo:prpLDlossCarMainVoList){
				if(!CodeConstants.defLossSourceFlag.ADDTIONALDEFLOSS.equals(prpLDlossCarMainVo.getDeflossSourceFlag())){
					String licenseNo = prpLDlossCarMainVo.getLicenseNo();
		    		String serialNo = prpLDlossCarMainVo.getSerialNo().toString();
		    		if(serialNo.equals("1")){//?????????
	        		    licenseNo = "?????????("+licenseNo+")";
		    		}else{
		    			licenseNo = "?????????("+licenseNo+")";
		    		}
		    		carNodeMap.put(code+"_1_"+serialNo,licenseNo);
				}
			}
		}


        if(carNodeMap.size() > 0){
        	TypeNode node=new TypeNode("carLoss","????????????");
			pictureNode.addNode(node);
			for(String key:carNodeMap.keySet()){
				TypeNode nodeTem=new TypeNode(key,carNodeMap.get(key));
				node.addNode(nodeTem);
			}
        }

    	claimNode.addNode(pictureNode);
		return claimNode;

	}

	public TypeNode buildAssessorsFeeTree(String registNo){
		TypeNode claimNode = new TypeNode("claim","??????");
		TypeNode certifyNode = new TypeNode("assessorsFee","???????????????");
		claimNode.addNode(certifyNode);
		return claimNode;
	}
	public TypeNode buildChecksFeeTree(String registNo){
		TypeNode claimNode = new TypeNode("claim","??????");
		TypeNode certifyNode = new TypeNode("checksFee","???????????????");
		claimNode.addNode(certifyNode);
		return claimNode;
	}
	public ReqSonNodeVo setReqSonNodeVo(TypeNode typeNode,String power){
		ReqSonNodeVo sonNodeVo=new ReqSonNodeVo();
		sonNodeVo.setId(typeNode.getTypeCode());
		String typeName = typeNode.getTypeName();
		if (typeName != null) {
			typeName = typeName.replaceAll("/", "|");
			typeName = typeName.replaceAll("[ ???\\\\???#?????????$*??&??????:]", "");
		}
		sonNodeVo.setName(typeName);
		sonNodeVo.setBarCode("");
		sonNodeVo.setChildFlag(CodeConstants.IsSingleAccident.NOT);
		sonNodeVo.setMinpages("0");
		sonNodeVo.setMaxpages("99999");
		sonNodeVo.setReseize("800*600");
		sonNodeVo.setRight(power);
		return sonNodeVo;
	}

	@Override
	public String reqResourceFromXy(SysUserVo user, String bussNo,String url){
		String respondXml="";
		try{
			PrpLRegistVo prpLRegistVo=registService.findRegistByRegistNo(bussNo);
			ReqImageBaseQueryRootVo rootVo=new ReqImageBaseQueryRootVo();
			BaseDataVo baseDataVo=new BaseDataVo();
			baseDataVo.setComCode("00000000");
			if(prpLRegistVo!=null){
			  baseDataVo.setOrgCode(prpLRegistVo.getComCode());
			  baseDataVo.setOrgName(codeTranService.transCode("ComCode",prpLRegistVo.getComCode()));
			}else{
			  baseDataVo.setOrgCode(user.getComCode());
			}
			baseDataVo.setUserCode(user.getUserCode());
			baseDataVo.setUserName(user.getUserName());
			baseDataVo.setRoleCode("admin");
			rootVo.setBaseDataVo(baseDataVo);
			ReqMetaDataQueryVo  metaDataQueryVo=new ReqMetaDataQueryVo();
			List<ReqBatchQueryVo> batchQueryVos=new ArrayList<ReqBatchQueryVo>();
			ReqBatchQueryVo BatchQueryVo=new ReqBatchQueryVo();
			BatchQueryVo.setBusiNo(bussNo);
			BatchQueryVo.setAppCode(CodeConstants.APPCODECLAIM);
			BatchQueryVo.setAppName(CodeConstants.APPNAMECLAIM);
			batchQueryVos.add(BatchQueryVo);
			metaDataQueryVo.setBatchQueryVos(batchQueryVos);
			rootVo.setMetaDataQueryVo(metaDataQueryVo);
			String ReqXml=ClaimBaseCoder.objToXmlUtf(rootVo);
			logger.info("???????????????????????????????????????"+ReqXml);
			//String Qurl=SpringProperties.getProperty("YX_QUrl");
			//??????????????????
			//String param = EncodeAccessParam.getEncodeParam("format=xml&code="+CodeConstants.YXResourceCODE+"&xml="+ReqXml,20*1000,key);
			//String url = Qurl+"?data="+param;
			respondXml=requestXydResource(ReqXml,url);
			// 2019???????????????????????????????????????????????????????????????????????????????????????????????????????????????
			// logger.info("?????????????????????????????????:"+respondXml);
		}catch(Exception e){
			logger.info("?????????????????????????????????:",e);
		}

		return respondXml;
	}
	/**
	 * ??????????????????
	 * @param requestXML
	 * @param urlStr
	 * @param seconds
	 * @return
	 * @throws Exception
	 */
	private String requestXydResource(String requestXML,String urlStr) throws Exception {
		long t1 = System.currentTimeMillis();

		//????????????
		logger.info("xmlToSend==============="+requestXML);
		StringBuilder sb = new StringBuilder("xml="+requestXML+"&code="+CodeConstants.YXResourceCODE+"&format=xml");
		String resXml="";
		PostMethod postMethod = null;
		HttpClient httpClient = null;
		try{
			String key = SpringProperties.getProperty("YX_DownLoadKey");
		    String param = EncodeAccessParam.getEncodeParam(sb.toString(), 1000, key);
		    logger.info("???????????????????????????==============="+urlStr);
			logger.info("???????????????????????????==============="+param);
			postMethod = new PostMethod(urlStr);
			// ????????????
			postMethod.getParams().setContentCharset("UTF-8");
			// ????????????n
			postMethod.setParameter("data", param);
			//String id = SpringProperties.getProperty("YX_DownLoadID");
            postMethod.setRequestHeader("Referer", "claimcar");
			httpClient = new HttpClient();
			httpClient.getParams().setSoTimeout(600000);
			// ??????postMethod
			int statusCode = httpClient.executeMethod(postMethod);
			if(statusCode==HttpStatus.SC_OK){
			    byte[] bodydata = postMethod.getResponseBody();
			    //???????????????
			    resXml = new String(bodydata, "UTF-8");
			}else{
			}
		}catch (HttpException e) {
			//?????????????????????URL??????????????????URL???
			logger.info("?????????????????????URL??????????????????URL",e);
		} catch (IOException e) {
			//?????????????????????????????????????????????????????????
			logger.info("??????????????????????????????????????????????????????",e);
		} catch(Exception e){
			logger.info("?????????????????????????????????",e);
		}finally {
			if (postMethod != null) {
				try {
					postMethod.releaseConnection();
				} catch (Exception e) {
					logger.info("????????????????????????releaseConnection?????????",e);
				}
			}
			if (httpClient != null) {
				try {
					((SimpleHttpConnectionManager) httpClient.getHttpConnectionManager()).shutdown();
				} catch (Exception e) {
					logger.info("????????????????????????shutdown?????????",e);
				}
				httpClient = null;
			}
		}
		// 2019???????????????????????????????????????????????????????????????????????????????????????????????????????????????
		// logger.info("????????????????????????????????????======="+resXml);
		logger.info("?????????????????????????????????----???"+(System.currentTimeMillis()-t1));
		return resXml;

	}

	@Override
	public String upImageToXyd(String bussNo,Map<String,File> maps,String flagType,String path){
		//???????????????????????????
		String returnXml="";//????????????
		try{
			ReqImageBaseRootVo imageBaseRootVo=params(bussNo,maps);
	    	XydImageUploadAllMethodService xydImageUploadAllMethodService=new XydImageUploadAllMethodService();
	    	List<ReqPhotoVo> photos = photos(maps);
			String ip=SpringProperties.getProperty("YX_URL");//??????ip
	    	String port=SpringProperties.getProperty("YX_PORT");//?????????
	    	String key=SpringProperties.getProperty("YX_key");//??????
	    	String id=SpringProperties.getProperty("YX_ID");
	    	ReqParameterVo parameterVo =new ReqParameterVo();
	    	parameterVo.setAppCode(CodeConstants.APPCODEL2);
	    	parameterVo.setSunIcmsIp(ip);
	    	parameterVo.setSocketNo(port!=null?Integer.valueOf(port).intValue():null);
	    	parameterVo.setKey(key);
	    	parameterVo.setId(id);
	    	//???????????????????????????
	    	returnXml=xydImageUploadAllMethodService.saveImageLocation(imageBaseRootVo, photos, parameterVo,"bill",null,"");
		}catch(Exception e){
			logger.info("?????????????????????????????????????????????bussNo=???"+bussNo,e);
			returnXml=e.getMessage();
		}

		return returnXml;
	}

	/**
	 * ??????????????????
	 * @param String
	 * @return
	 */
	private ReqImageBaseRootVo params(String bussNo,Map<String,File> fileMaps){
		ReqImageBaseRootVo root=new ReqImageBaseRootVo();
		BaseDataVo baseDataVo=new BaseDataVo();
		ReqMetaDataVo metaDataVo=new ReqMetaDataVo();
		baseDataVo.setComCode("00000000");
		baseDataVo.setUserCode("0000000000");
		baseDataVo.setUserName("0000000000");
		baseDataVo.setOrgCode("DHIC");
		baseDataVo.setOrgName("????????????");
		baseDataVo.setRoleCode(CodeConstants.APPROLE);
		ReqBatchVo batchVo=new ReqBatchVo();
		batchVo.setAppCode(CodeConstants.APPCODECLAIM);
		batchVo.setAppName(CodeConstants.APPNAMECLAIM);
		batchVo.setBusiNo(bussNo);
		batchVo.setComCode("00000000");
		ReqVtreeVo vtreeVo=new ReqVtreeVo();
		vtreeVo.setAppCode(CodeConstants.APPCODECLAIM);
		vtreeVo.setAppName(CodeConstants.APPNAMECLAIM);

		ReqFatherNodeVo fatherNodeVo=new ReqFatherNodeVo();
		fatherNodeVo.setId(CodeConstants.APPCODEL2);
		fatherNodeVo.setName(CodeConstants.APPNAMEL2);
		fatherNodeVo.setBarCode("");
		fatherNodeVo.setChildFlag("0");
		fatherNodeVo.setMinpages("0");
		fatherNodeVo.setMaxpages("99999");
		fatherNodeVo.setReseize("800*600");
		fatherNodeVo.setRight(CodeConstants.YXRIGHTCODE);

		List<ReqSonNodeVo> sonNodeVos1=new ArrayList<ReqSonNodeVo>();
		ReqSonNodeVo sonNodeVo1=new ReqSonNodeVo();
		sonNodeVo1.setId(CodeConstants.paystandard);
		sonNodeVo1.setMaxpages("99999");
		sonNodeVo1.setMinpages("0");
		sonNodeVo1.setName("???????????????????????????");
		sonNodeVo1.setReseize("800*600");
		sonNodeVo1.setRight(CodeConstants.YXRIGHTCODE);
		sonNodeVo1.setChildFlag("0");
		sonNodeVo1.setBarCode("");


		List<ReqSonNodeVo> sonNodeVos=new ArrayList<ReqSonNodeVo>();
		commonParam(sonNodeVos,fileMaps);
		sonNodeVo1.setSonNodes(sonNodeVos);
		sonNodeVos1.add(sonNodeVo1);
		fatherNodeVo.setSonNodes(sonNodeVos1);
		List<ReqFatherNodeVo> fatherNodeVos=new ArrayList<ReqFatherNodeVo>();
		fatherNodeVos.add(fatherNodeVo);
		vtreeVo.setFatherNodeVos(fatherNodeVos);
		batchVo.setVtreeVo(vtreeVo);
		ReqPagesVo reqPagesVo=new ReqPagesVo();
		List<ReqPageNodeVo> pages=new ArrayList<ReqPageNodeVo>();
		if(sonNodeVos!=null && sonNodeVos.size()>0 && fileMaps!=null && fileMaps.size()>0){
			for(ReqSonNodeVo sonNodeVo:sonNodeVos){
				for(Map.Entry<String,File> fileVo:fileMaps.entrySet()){
					if(StringUtils.isNotBlank(sonNodeVo.getId()) && sonNodeVo.getId().equals(fileVo.getKey())){
						ReqPageNodeVo pageNodeVo=new ReqPageNodeVo();
						pageNodeVo.setId(sonNodeVo.getId());
						pageNodeVo.setAction("ADD");
						List<ReqPageVo> pageVos=new ArrayList<ReqPageVo>();
						if(fileVo.getValue()!=null){
								ReqPageVo pageVo=new ReqPageVo();
								if(StringUtils.isNotBlank(fileVo.getValue().getAbsolutePath())){
									pageVo.setFileName(fileVo.getValue().getName());
									pageVo.setRemark("????????????????????????");
									pageVo.setUpUser("0000000000");
									pageVo.setUpUsername("0000000000");
									pageVo.setUpOrgname("?????????");
									pageVo.setUp_org("0000000000");
									pageVo.setUpTime(DateFormatString(new Date()));
									pageVos.add(pageVo);
								}
						}
						pageNodeVo.setPageVos(pageVos);
						pages.add(pageNodeVo);
					}
					
				}
				
			}
		}
		reqPagesVo.setReqPageNodeVos(pages);
		batchVo.setReqPagesVo(reqPagesVo);
		metaDataVo.setBatchVo(batchVo);
		root.setBaseDataVo(baseDataVo);
		root.setMetaDataVo(metaDataVo);
	return root;
	}

	/**
	 * ??????????????????
	 * @param sonNodeVos
	 * @param nodeId
	 * @param nodeName
	 * @return
	 */
	private List<ReqSonNodeVo> commonParam(List<ReqSonNodeVo> sonNodeVos,Map<String,File> fileMaps){
		if(fileMaps!=null && fileMaps.size()>0){
			for(Map.Entry<String,File> fileVo:fileMaps.entrySet()){
				ReqSonNodeVo sonNodeVo=new ReqSonNodeVo();
				sonNodeVo.setId(fileVo.getKey());
				sonNodeVo.setName(fileVo.getKey());
				sonNodeVo.setRight(CodeConstants.YXRIGHTCODE);
				sonNodeVo.setReseize("800*600");
				sonNodeVo.setChildFlag("1");
				sonNodeVo.setBarCode("");
				sonNodeVo.setMaxpages("99999");
				sonNodeVo.setMinpages("0");
				sonNodeVos.add(sonNodeVo);
			}
		}
		return sonNodeVos;
	}

	/**
	 * ??????????????????
	 * Date ???????????? String??????
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
	/**
	 * ???????????????????????????
	 * @param File
	 * @param bussNo
	 * @return
	 * @throws Exception
	 */
	private List<ReqPhotoVo> photos(Map<String,File> mapFiles) throws Exception{
		List<ReqPhotoVo> photoVos=new ArrayList<ReqPhotoVo>();
		if(mapFiles!=null && mapFiles.size()>0){
			for(Map.Entry<String,File> fileMapVo:mapFiles.entrySet()){
				if(StringUtils.isNotBlank(fileMapVo.getValue().getAbsolutePath())){
					ReqPhotoVo photoVo=new ReqPhotoVo();
						photoVo.setCarCaseNo(fileMapVo.getKey());
						photoVo.setImageName(fileMapVo.getValue().getName());
						photoVo.setImageUrl(fileMapVo.getValue().getAbsolutePath());//????????????????????????
						photoVos.add(photoVo);
					}
	
			}
			
		}
		return photoVos;
	}
	
	/**
	 * ???????????????????????????
	 * @param File
	 * @param bussNo
	 * @return
	 * @throws Exception
	 */
	private List<ReqPhotoVo> billphotos(String bussNo,List<String> mapUrls) throws Exception{
		List<ReqPhotoVo> photoVos=new ArrayList<ReqPhotoVo>();
		int i=0;
		if(mapUrls!=null && mapUrls.size()>0){
			for(String url:mapUrls){
				ReqPhotoVo photoVo=new ReqPhotoVo();
				photoVo.setCarCaseNo(bussNo);
				photoVo.setImageName(bussNo+i+".jpg");
				photoVo.setImageUrl(url);//????????????????????????
				photoVos.add(photoVo);
				i++;
			  }
	
			}
			
		return photoVos;
	}
	
	@Override
	public String upBillImageToXyd(String bussNo, Map<String, File> maps,String flagType, Map<String, String> bussTypeMaps,Map<String, String> codeMaps, Map<String, String> payIdMaps,String path) {
		        //???????????????????????????
				String returnXml="";//????????????
				try{
					ReqImageBaseRootVo imageBaseRootVo=billparams(bussNo,maps,bussTypeMaps,codeMaps,payIdMaps);
			    	XydImageUploadAllMethodService xydImageUploadAllMethodService=new XydImageUploadAllMethodService();
			    	List<ReqPhotoVo> photos = photos(maps);
					String ip=SpringProperties.getProperty("YX_URL");//??????ip
			    	String port=SpringProperties.getProperty("YX_PORT");//?????????
			    	String key=SpringProperties.getProperty("YX_key");//??????
			    	String id=SpringProperties.getProperty("YX_ID");
			    	ReqParameterVo parameterVo =new ReqParameterVo();
			    	if(bussTypeMaps!=null && bussTypeMaps.size()>0){
			    		for(Map.Entry<String,String> bussTypeVo: bussTypeMaps.entrySet()){
			    			parameterVo.setAppCode("B"+bussTypeVo.getKey());
			    		}
			    	}
			    	
			    	parameterVo.setSunIcmsIp(ip);
			    	parameterVo.setSocketNo(port!=null?Integer.valueOf(port).intValue():null);
			    	parameterVo.setKey(key);
			    	parameterVo.setId(id);
			    	//???????????????????????????
			    	returnXml=xydImageUploadAllMethodService.saveImageLocation(imageBaseRootVo, photos, parameterVo,flagType,maps,path);
				}catch(Exception e){
					logger.info("??????VAT??????????????????????????????bussNo=???"+bussNo,e);
					returnXml=e.getMessage();
				}

				return returnXml;
	}

	@Override
	public String upasscheckBillImageToXyd(String bussNo,Map<String, File> maps, String flagType,Map<String, String> bussTypeMaps, String path) {
		//???????????????????????????
		String returnXml="";//????????????
		try{
			ReqImageBaseRootVo imageBaseRootVo=asscheckBillparams(bussNo,maps,bussTypeMaps);
	    	XydImageUploadAllMethodService xydImageUploadAllMethodService=new XydImageUploadAllMethodService();
	    	List<ReqPhotoVo> photos = photos(maps);
			String ip=SpringProperties.getProperty("YX_URL");//??????ip
	    	String port=SpringProperties.getProperty("YX_PORT");//?????????
	    	String key=SpringProperties.getProperty("YX_key");//??????
	    	String id=SpringProperties.getProperty("YX_ID");
	    	ReqParameterVo parameterVo =new ReqParameterVo();
	    	if(bussTypeMaps!=null && bussTypeMaps.size()>0){
	    		for(Map.Entry<String,String> bussTypeVo: bussTypeMaps.entrySet()){
	    			parameterVo.setAppCode("B"+bussNo+bussTypeVo.getKey());
	    		}
	    	}
	    	
	    	parameterVo.setSunIcmsIp(ip);
	    	parameterVo.setSocketNo(port!=null?Integer.valueOf(port).intValue():null);
	    	parameterVo.setKey(key);
	    	parameterVo.setId(id);
	    	//???????????????????????????
	    	returnXml=xydImageUploadAllMethodService.saveImageLocation(imageBaseRootVo, photos, parameterVo,flagType,maps,path);
		}catch(Exception e){
			logger.info("??????VAT????????????(?????????????????????)??????????????????bussNo=???"+bussNo,e);
			returnXml=e.getMessage();
		}

		return returnXml;
	}
	
	
	/**
	 * ??????????????????????????????
	 * @param String
	 * @return
	 */
	private ReqImageBaseRootVo asscheckBillparams(String bussNo,Map<String,File> fileMaps,Map<String, String> bussTypeMaps){
		ReqImageBaseRootVo root=new ReqImageBaseRootVo();
		BaseDataVo baseDataVo=new BaseDataVo();
		ReqMetaDataVo metaDataVo=new ReqMetaDataVo();
		baseDataVo.setComCode("00000000");
		baseDataVo.setUserCode("0000000000");
		baseDataVo.setUserName("0000000000");
		baseDataVo.setOrgCode("DHIC");
		baseDataVo.setOrgName("????????????");
		baseDataVo.setRoleCode(CodeConstants.APPROLE);
		ReqBatchVo batchVo=new ReqBatchVo();
		batchVo.setAppCode(CodeConstants.APPCODECLAIM);
		batchVo.setAppName(CodeConstants.APPNAMECLAIM);
		batchVo.setBusiNo(bussNo);//????????????
		batchVo.setComCode("00000000");
		ReqVtreeVo vtreeVo=new ReqVtreeVo();
		vtreeVo.setAppCode(CodeConstants.APPCODECLAIM);
		vtreeVo.setAppName(CodeConstants.APPNAMECLAIM);
		//?????????
		List<ReqSonNodeVo> sonNodeVoImagesList=new ArrayList<ReqSonNodeVo>();
		ReqFatherNodeVo fatherNodeVo=new ReqFatherNodeVo();
		//????????????
		if(bussTypeMaps!=null && bussTypeMaps.size()>0){
			for(Map.Entry<String,String> bussTypeVo:bussTypeMaps.entrySet()){
				fatherNodeVo.setId("B"+bussNo+bussTypeVo.getKey());
				fatherNodeVo.setName(bussTypeVo.getValue());
				fatherNodeVo.setMaxpages("99999");
				fatherNodeVo.setMinpages("0");
				fatherNodeVo.setReseize("800*600");
				fatherNodeVo.setRight(CodeConstants.YXRIGHTCODE);
				fatherNodeVo.setChildFlag("0");
				fatherNodeVo.setBarCode("");
				 if(fileMaps!=null && fileMaps.size()>0){
			    	  int i=0;
					  for(Map.Entry<String,File> fileVo:fileMaps.entrySet()){ 
						  ReqSonNodeVo sonNodeImageVo=new ReqSonNodeVo();
						  sonNodeImageVo.setId(fileVo.getKey());
						  sonNodeImageVo.setName("??????("+new Date().getTime()+i+")");
						  sonNodeImageVo.setMaxpages("99999");
						  sonNodeImageVo.setMinpages("0");
						  sonNodeImageVo.setReseize("800*600");
						  sonNodeImageVo.setRight(CodeConstants.YXRIGHTCODE);
						  sonNodeImageVo.setChildFlag("1");
						  sonNodeImageVo.setBarCode("");
						  sonNodeVoImagesList.add(sonNodeImageVo);
						  i++;
					  }
				  }
			    fatherNodeVo.setSonNodes(sonNodeVoImagesList);
		    }
	    }
		//page???
		List<ReqFatherNodeVo> fatherNodeVos=new ArrayList<ReqFatherNodeVo>();
		fatherNodeVos.add(fatherNodeVo);
		vtreeVo.setFatherNodeVos(fatherNodeVos);
		batchVo.setVtreeVo(vtreeVo);
		ReqPagesVo reqPagesVo=new ReqPagesVo();
		List<ReqPageNodeVo> pages=new ArrayList<ReqPageNodeVo>();
		if(sonNodeVoImagesList!=null && sonNodeVoImagesList.size()>0 && fileMaps!=null && fileMaps.size()>0){
			for(ReqSonNodeVo sonNodeVo:sonNodeVoImagesList){
				for(Map.Entry<String,File> fileVo:fileMaps.entrySet()){
					if(StringUtils.isNotBlank(sonNodeVo.getId()) && sonNodeVo.getId().equals(fileVo.getKey())){
						ReqPageNodeVo pageNodeVo=new ReqPageNodeVo();
						pageNodeVo.setId(sonNodeVo.getId());
						pageNodeVo.setAction("ADD");
						List<ReqPageVo> pageVos=new ArrayList<ReqPageVo>();
						if(fileVo.getValue()!=null){
								ReqPageVo pageVo=new ReqPageVo();
								if(StringUtils.isNotBlank(fileVo.getValue().getAbsolutePath())){
									pageVo.setFileName(fileVo.getValue().getName());
									pageVo.setRemark("vat????????????");
									pageVo.setUpUser("0000000000");
									pageVo.setUpUsername("0000000000");
									pageVo.setUpOrgname("?????????");
									pageVo.setUp_org("0000000000");
									pageVo.setUpTime(DateFormatString(new Date()));
									pageVos.add(pageVo);
								}
						}
						pageNodeVo.setPageVos(pageVos);
						pages.add(pageNodeVo);
					}
					
				}
				
			}
		}
		reqPagesVo.setReqPageNodeVos(pages);
		batchVo.setReqPagesVo(reqPagesVo);
		metaDataVo.setBatchVo(batchVo);
		root.setBaseDataVo(baseDataVo);
		root.setMetaDataVo(metaDataVo);
	return root;
	}

	/**
	 * ??????????????????????????????
	 * @param String
	 * @return
	 */
	private ReqImageBaseRootVo billparams(String bussNo,Map<String,File> fileMaps,Map<String, String> bussTypeMaps,Map<String, String> codeMaps, Map<String, String> payIdMaps){
		ReqImageBaseRootVo root=new ReqImageBaseRootVo();
		BaseDataVo baseDataVo=new BaseDataVo();
		ReqMetaDataVo metaDataVo=new ReqMetaDataVo();
		baseDataVo.setComCode("00000000");
		baseDataVo.setUserCode("0000000000");
		baseDataVo.setUserName("0000000000");
		baseDataVo.setOrgCode("DHIC");
		baseDataVo.setOrgName("????????????");
		baseDataVo.setRoleCode(CodeConstants.APPROLE);
		ReqBatchVo batchVo=new ReqBatchVo();
		batchVo.setAppCode(CodeConstants.APPCODECLAIM);
		batchVo.setAppName(CodeConstants.APPNAMECLAIM);
		batchVo.setBusiNo(bussNo);//????????????
		batchVo.setComCode("00000000");
		ReqVtreeVo vtreeVo=new ReqVtreeVo();
		vtreeVo.setAppCode(CodeConstants.APPCODECLAIM);
		vtreeVo.setAppName(CodeConstants.APPNAMECLAIM);
		//?????????
		List<ReqSonNodeVo> sonNodeVoImagesList=new ArrayList<ReqSonNodeVo>();
		ReqFatherNodeVo fatherNodeVo=new ReqFatherNodeVo();
		//????????????
		if(bussTypeMaps!=null && bussTypeMaps.size()>0){
			for(Map.Entry<String,String> bussTypeVo:bussTypeMaps.entrySet()){
				fatherNodeVo.setId("B"+bussTypeVo.getKey());
				fatherNodeVo.setName(bussTypeVo.getValue());
				fatherNodeVo.setMaxpages("99999");
				fatherNodeVo.setMinpages("0");
				fatherNodeVo.setReseize("800*600");
				fatherNodeVo.setRight(CodeConstants.YXRIGHTCODE);
				fatherNodeVo.setChildFlag("0");
				fatherNodeVo.setBarCode("");
			 //???????????????????????????
		     if(codeMaps!=null && codeMaps.size()>0){
		    	 List<ReqSonNodeVo> sonNodeVoList2=new ArrayList<ReqSonNodeVo>();
			    for(Map.Entry<String,String> codeTypeVo:codeMaps.entrySet()){
						if("0".equals(bussTypeVo.getKey()) && "37,38,39".contains(codeTypeVo.getKey())){//??????
							ReqSonNodeVo sonNodeVo2=new ReqSonNodeVo();
							sonNodeVo2.setId(codeTypeVo.getKey());
							sonNodeVo2.setName(codeTypeVo.getValue());
							sonNodeVo2.setMaxpages("99999");
							sonNodeVo2.setMinpages("0");
							sonNodeVo2.setReseize("800*600");
							sonNodeVo2.setRight(CodeConstants.YXRIGHTCODE);
							sonNodeVo2.setChildFlag("1");
							sonNodeVo2.setBarCode("");
							//?????????
						 if(payIdMaps!=null && payIdMaps.size()>0){
							 List<ReqSonNodeVo> sonNodeVoList3=new ArrayList<ReqSonNodeVo>();
							 for(Map.Entry<String,String> payIdVo:payIdMaps.entrySet()){
								 ReqSonNodeVo sonNodeVo3=new ReqSonNodeVo();
								    sonNodeVo3.setId(codeTypeVo.getKey()+payIdVo.getKey());
								    sonNodeVo3.setName(payIdVo.getValue()+"(?????????)");
								    sonNodeVo3.setMaxpages("99999");
								    sonNodeVo3.setMinpages("0");
								    sonNodeVo3.setReseize("800*600");
								    sonNodeVo3.setRight(CodeConstants.YXRIGHTCODE);
								    sonNodeVo3.setChildFlag("1");
								    sonNodeVo3.setBarCode("");
								  if(fileMaps!=null && fileMaps.size()>0){
									  int i=0;
									  for(Map.Entry<String,File> fileVo:fileMaps.entrySet()){ 
										  ReqSonNodeVo sonNodeImageVo=new ReqSonNodeVo();
										  sonNodeImageVo.setId(fileVo.getKey());
										  sonNodeImageVo.setName("??????("+new Date().getTime()+i+")");
										  sonNodeImageVo.setMaxpages("99999");
										  sonNodeImageVo.setMinpages("0");
										  sonNodeImageVo.setReseize("800*600");
										  sonNodeImageVo.setRight(CodeConstants.YXRIGHTCODE);
										  sonNodeImageVo.setChildFlag("1");
										  sonNodeImageVo.setBarCode("");
										  sonNodeVoImagesList.add(sonNodeImageVo);
										  i++;
									  }
								  }
								    //???????????????
								    sonNodeVo3.setSonNodes(sonNodeVoImagesList);
								    sonNodeVoList3.add(sonNodeVo3);
							 }
							 sonNodeVo2.setSonNodes(sonNodeVoList3);
						 }
						 sonNodeVoList2.add(sonNodeVo2);
						}else{//??????
							ReqSonNodeVo sonNodeVo2=new ReqSonNodeVo();
							sonNodeVo2.setId(codeTypeVo.getKey());
							sonNodeVo2.setName(codeTypeVo.getValue());
							sonNodeVo2.setMaxpages("99999");
							sonNodeVo2.setMinpages("0");
							sonNodeVo2.setReseize("800*600");
							sonNodeVo2.setRight(CodeConstants.YXRIGHTCODE);
							sonNodeVo2.setChildFlag("1");
							sonNodeVo2.setBarCode("");
							//?????????
							if(payIdMaps!=null && payIdMaps.size()>0){
								 List<ReqSonNodeVo> sonNodeVoList3=new ArrayList<ReqSonNodeVo>();
								 for(Map.Entry<String,String> payIdVo:payIdMaps.entrySet()){
									 ReqSonNodeVo sonNodeVo3=new ReqSonNodeVo();
									    sonNodeVo3.setId(codeTypeVo.getKey()+payIdVo.getKey());
									    sonNodeVo3.setName(payIdVo.getValue()+"(?????????)");
									    sonNodeVo3.setMaxpages("99999");
									    sonNodeVo3.setMinpages("0");
									    sonNodeVo3.setReseize("800*600");
									    sonNodeVo3.setRight(CodeConstants.YXRIGHTCODE);
									    sonNodeVo3.setChildFlag("1");
									    sonNodeVo3.setBarCode("");
									    //?????????
									    
									    if(fileMaps!=null && fileMaps.size()>0){
									    	  int i=0;
											  for(Map.Entry<String,File> fileVo:fileMaps.entrySet()){ 
												  ReqSonNodeVo sonNodeImageVo=new ReqSonNodeVo();
												  sonNodeImageVo.setId(fileVo.getKey());
												  sonNodeImageVo.setName("??????("+new Date().getTime()+i+")");
												  sonNodeImageVo.setMaxpages("99999");
												  sonNodeImageVo.setMinpages("0");
												  sonNodeImageVo.setReseize("800*600");
												  sonNodeImageVo.setRight(CodeConstants.YXRIGHTCODE);
												  sonNodeImageVo.setChildFlag("1");
												  sonNodeImageVo.setBarCode("");
												  sonNodeVoImagesList.add(sonNodeImageVo);
												  i++;
											  }
										  }
										//???????????????
										sonNodeVo3.setSonNodes(sonNodeVoImagesList);
									    sonNodeVoList3.add(sonNodeVo3);
								 }
								 sonNodeVo2.setSonNodes(sonNodeVoList3);
							 }
							sonNodeVoList2.add(sonNodeVo2);
						}
				}
			    fatherNodeVo.setSonNodes(sonNodeVoList2);
		    }
	    }
}
		//page???
		List<ReqFatherNodeVo> fatherNodeVos=new ArrayList<ReqFatherNodeVo>();
		fatherNodeVos.add(fatherNodeVo);
		vtreeVo.setFatherNodeVos(fatherNodeVos);
		batchVo.setVtreeVo(vtreeVo);
		ReqPagesVo reqPagesVo=new ReqPagesVo();
		List<ReqPageNodeVo> pages=new ArrayList<ReqPageNodeVo>();
		if(sonNodeVoImagesList!=null && sonNodeVoImagesList.size()>0 && fileMaps!=null && fileMaps.size()>0){
			for(ReqSonNodeVo sonNodeVo:sonNodeVoImagesList){
				for(Map.Entry<String,File> fileVo:fileMaps.entrySet()){
					if(StringUtils.isNotBlank(sonNodeVo.getId()) && sonNodeVo.getId().equals(fileVo.getKey())){
						ReqPageNodeVo pageNodeVo=new ReqPageNodeVo();
						pageNodeVo.setId(sonNodeVo.getId());
						pageNodeVo.setAction("ADD");
						List<ReqPageVo> pageVos=new ArrayList<ReqPageVo>();
						if(fileVo.getValue()!=null){
								ReqPageVo pageVo=new ReqPageVo();
								if(StringUtils.isNotBlank(fileVo.getValue().getAbsolutePath())){
									pageVo.setFileName(fileVo.getValue().getName());
									pageVo.setRemark("vat????????????");
									pageVo.setUpUser("0000000000");
									pageVo.setUpUsername("0000000000");
									pageVo.setUpOrgname("?????????");
									pageVo.setUp_org("0000000000");
									pageVo.setUpTime(DateFormatString(new Date()));
									pageVos.add(pageVo);
								}
						}
						pageNodeVo.setPageVos(pageVos);
						pages.add(pageNodeVo);
					}
					
				}
				
			}
		}
		reqPagesVo.setReqPageNodeVos(pages);
		batchVo.setReqPagesVo(reqPagesVo);
		metaDataVo.setBatchVo(batchVo);
		root.setBaseDataVo(baseDataVo);
		root.setMetaDataVo(metaDataVo);
	return root;
	}

	@Override
	public String upBillImageInfoToXyd(String bussNo,List<String> urls,String flagType, String path) {
		//???????????????????????????
		String returnXml="";//????????????
		try{
			ReqImageBaseRootVo imageBaseRootVo=billInfoparams(bussNo,urls);
	    	XydImageUploadAllMethodService xydImageUploadAllMethodService=new XydImageUploadAllMethodService();
	    	List<ReqPhotoVo> photos = billphotos(bussNo,urls);
			String ip=SpringProperties.getProperty("YX_URL");//??????ip
	    	String port=SpringProperties.getProperty("YX_PORT");//?????????
	    	String key=SpringProperties.getProperty("YX_key");//??????
	    	String id=SpringProperties.getProperty("YX_ID");
	    	ReqParameterVo parameterVo =new ReqParameterVo();
	    	parameterVo.setAppCode(CodeConstants.APPCODEL2);
	    	parameterVo.setSunIcmsIp(ip);
	    	parameterVo.setSocketNo(port!=null?Integer.valueOf(port).intValue():null);
	    	parameterVo.setKey(key);
	    	parameterVo.setId(id);
	    	//???????????????????????????
	    	returnXml=xydImageUploadAllMethodService.saveImageLocation(imageBaseRootVo, photos, parameterVo,flagType,null,"");
	    	
		}catch(Exception e){
			logger.info("????????????????????????????????????bussNo=???"+bussNo,e);
			returnXml=e.getMessage();
		}

		return returnXml;
	}
     /**
      * 
      * @param bussNo
      * @param urls
      * @return
      */
	private ReqImageBaseRootVo billInfoparams(String bussNo,List<String> urls){
		ReqImageBaseRootVo root=new ReqImageBaseRootVo();
		BaseDataVo baseDataVo=new BaseDataVo();
		ReqMetaDataVo metaDataVo=new ReqMetaDataVo();
		baseDataVo.setComCode("00000000");
		baseDataVo.setUserCode("0000000000");
		baseDataVo.setUserName("0000000000");
		baseDataVo.setOrgCode("DHIC");
		baseDataVo.setOrgName("????????????");
		baseDataVo.setRoleCode(CodeConstants.APPROLE);
		ReqBatchVo batchVo=new ReqBatchVo();
		batchVo.setAppCode(CodeConstants.APPCODECLAIM);
		batchVo.setAppName(CodeConstants.APPNAMECLAIM);
		batchVo.setBusiNo(bussNo);
		batchVo.setComCode("00000000");
		ReqVtreeVo vtreeVo=new ReqVtreeVo();
		vtreeVo.setAppCode(CodeConstants.APPCODECLAIM);
		vtreeVo.setAppName(CodeConstants.APPNAMECLAIM);

		ReqFatherNodeVo fatherNodeVo=new ReqFatherNodeVo();
		fatherNodeVo.setId(CodeConstants.APPCODEL2);
		fatherNodeVo.setName(CodeConstants.APPNAMEL2);
		fatherNodeVo.setBarCode("");
		fatherNodeVo.setChildFlag("0");
		fatherNodeVo.setMinpages("0");
		fatherNodeVo.setMaxpages("99999");
		fatherNodeVo.setReseize("800*600");
		fatherNodeVo.setRight(CodeConstants.YXRIGHTCODE);

		List<ReqSonNodeVo> sonNodeVos1=new ArrayList<ReqSonNodeVo>();
		ReqSonNodeVo sonNodeVo1=new ReqSonNodeVo();
		sonNodeVo1.setId(CodeConstants.BILLCODE+"F");
		sonNodeVo1.setMaxpages("99999");
		sonNodeVo1.setMinpages("0");
		sonNodeVo1.setName("?????????????????????");
		sonNodeVo1.setReseize("800*600");
		sonNodeVo1.setRight(CodeConstants.YXRIGHTCODE);
		sonNodeVo1.setChildFlag("0");
		sonNodeVo1.setBarCode("");
       
        List<ReqSonNodeVo> sonNodeVos2=new ArrayList<ReqSonNodeVo>();
		ReqSonNodeVo sonNodeVo2=new ReqSonNodeVo();
		sonNodeVo2.setId(CodeConstants.BILLCODE+"S");
		sonNodeVo2.setMaxpages("99999");
		sonNodeVo2.setMinpages("0");
		sonNodeVo2.setName("?????????????????????");
		sonNodeVo2.setReseize("800*600");
		sonNodeVo2.setRight(CodeConstants.YXRIGHTCODE);
		sonNodeVo2.setChildFlag("1");
		sonNodeVo2.setBarCode("");
		sonNodeVos2.add(sonNodeVo2);
		sonNodeVo1.setSonNodes(sonNodeVos2);
		sonNodeVos1.add(sonNodeVo1);
		fatherNodeVo.setSonNodes(sonNodeVos1);
		List<ReqFatherNodeVo> fatherNodeVos=new ArrayList<ReqFatherNodeVo>();
		fatherNodeVos.add(fatherNodeVo);
		vtreeVo.setFatherNodeVos(fatherNodeVos);
		batchVo.setVtreeVo(vtreeVo);
		ReqPagesVo reqPagesVo=new ReqPagesVo();
		List<ReqPageNodeVo> pages=new ArrayList<ReqPageNodeVo>();
		int i=0;
		if(urls!=null && urls.size()>0){
			for(ReqSonNodeVo sonNodeVo:sonNodeVos2){
				for(String url:urls){
					   
						ReqPageNodeVo pageNodeVo=new ReqPageNodeVo();
						pageNodeVo.setId(sonNodeVo.getId());
						pageNodeVo.setAction("ADD");
						List<ReqPageVo> pageVos=new ArrayList<ReqPageVo>();
						if(StringUtils.isNotBlank(url)){
								ReqPageVo pageVo=new ReqPageVo();
									pageVo.setFileName(bussNo+i+".jpg");
									pageVo.setRemark("???????????????????????????");
									pageVo.setUpUser("0000000000");
									pageVo.setUpUsername("0000000000");
									pageVo.setUpOrgname("?????????");
									pageVo.setUp_org("0000000000");
									pageVo.setUpTime(DateFormatString(new Date()));
									pageVos.add(pageVo);
						}
						pageNodeVo.setPageVos(pageVos);
						pages.add(pageNodeVo);
					i++;
				}
				
			}
		}
		reqPagesVo.setReqPageNodeVos(pages);
		batchVo.setReqPagesVo(reqPagesVo);
		metaDataVo.setBatchVo(batchVo);
		root.setBaseDataVo(baseDataVo);
		root.setMetaDataVo(metaDataVo);
		return root;
	}
	public  String replaceBlank(String str) {
		String dest = "";
		if (str!=null) {
			Pattern p = Pattern.compile("\\s*|\t|\r|\n");
			Matcher m = p.matcher(str);
			dest = m.replaceAll("");
		}
		return dest;
	}
}
