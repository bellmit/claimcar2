package ins.sino.claimcar.base.claimyj.service;

/**
 * 阳杰图片查询接口
 */
import ins.framework.lang.Springs;
import ins.platform.common.service.facade.CodeTranService;
import ins.platform.utils.ClaimBaseCoder;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.carYxImage.service.CarXyImageService;
import ins.sino.claimcar.certify.service.CertifyService;
import ins.sino.claimcar.check.service.CheckTaskService;
import ins.sino.claimcar.check.vo.PrpLCheckCarVo;
import ins.sino.claimcar.claimcarYJ.vo.ImageReqBody;
import ins.sino.claimcar.claimcarYJ.vo.ImageReqPacket;
import ins.sino.claimcar.claimcarYJ.vo.ImageResBody;
import ins.sino.claimcar.claimcarYJ.vo.ResYJImageVo;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.service.WfFlowQueryService;
import ins.sino.claimcar.losscar.service.LossCarService;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarCompVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarMainVo;
import ins.sino.claimcar.mobileCheckCommon.vo.MobileCheckHead;
import ins.sino.claimcar.mobileCheckCommon.vo.MobileCheckResponseHead;
import ins.sino.claimcar.regist.service.RegistService;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;
import ins.sino.claimcar.sunyardimage.vo.common.BaseDataVo;
import ins.sino.claimcar.sunyardimage.vo.request.ReqBatchQueryVo;
import ins.sino.claimcar.sunyardimage.vo.request.ReqFatherNodeVo;
import ins.sino.claimcar.sunyardimage.vo.request.ReqImageBaseQueryRootVo;
import ins.sino.claimcar.sunyardimage.vo.request.ReqMetaDataQueryVo;
import ins.sino.claimcar.sunyardimage.vo.request.ReqSonNodeVo;
import ins.sino.claimcar.sunyardimage.vo.request.ReqVtreeVo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.SpringProperties;

import com.alibaba.dubbo.common.utils.Assert;
import com.sinosoft.api.service.ServiceInterface;
import com.sinosoft.image.tree.ImgTypeTree;
import com.sinosoft.image.tree.TypeNode;
import com.sunyard.insurance.encode.client.EncodeAccessParam;

public class YangJieImageServiceImpl  implements ServiceInterface {
	private static Logger logger = LoggerFactory.getLogger(YangJieImageServiceImpl.class);

	@Autowired
	private CheckTaskService checkTaskService;
	
	@Autowired
	private CertifyService certifyService;
	
	@Autowired
	private LossCarService lossCarService;
	
	@Autowired
	CarXyImageService carXyImageService;
	@Autowired
	RegistService registService;
	@Autowired
	CodeTranService codeTranService;
	@Autowired
	private WfFlowQueryService wfFlowQueryService;
	@Override
	public ResYJImageVo service(String arg0, Object arg1) {
		long t1 = System.currentTimeMillis();
		init();
		String errMsg = "Success";
		String errNo = "1";
		ImageResBody imageResBody = new ImageResBody();
		ResYJImageVo resYJImageVo = new ResYJImageVo();
		MobileCheckResponseHead responseHead = new MobileCheckResponseHead();
		String Qurl="";
		try{
			String reqXml = ClaimBaseCoder.objToXmlUtf(arg1);
			ImageReqPacket reqPacket = (ImageReqPacket) arg1;
			Assert.notNull(reqPacket, "数据为空 ");
			MobileCheckHead head = reqPacket.getHead();
			if (!"CertifyImages".equals(head.getRequestType())|| !"yangjie_user".equals(head.getUser())|| !"yangjie_psd".equals(head.getPassWord())) {
				throw new IllegalArgumentException(" 请求头参数错误  ");
			}
			ImageReqBody reqBody = reqPacket.getBody();
			String registNo = reqBody.getRegistNo();
			logger.info("阳杰图片查询接口请求报文: \n"+reqXml);
			SysUserVo user=new SysUserVo();
			user.setComCode("00000000");
			user.setUserCode("0000000000");
			user.setUserName("系统管理员");
			String reqYJXml=this.getReqQueryParam(user,CodeConstants.APPROLE,registNo);
			String key=SpringProperties.getProperty("YX_YJkey");
			Qurl=SpringProperties.getProperty("YX_YJQUrl");
			//生成请求参数
			String param = EncodeAccessParam.getEncodeParam("format=xml&code="+CodeConstants.YXQUERYCODE+"&xml="+reqYJXml,20*1000,key);
			String url = Qurl+"?data="+param;
			logger.info("信雅达接口调用参数:"+"format=xml&code="+CodeConstants.YXQUERYCODE+"&xml="+reqXml);
			imageResBody.setImageUrl(url);
			imageResBody.setRegistNo(registNo);
			resYJImageVo.setBody(imageResBody);
		}catch(Exception e){
			errNo = "0";
			errMsg = "阳杰图片查询接口报错："+e.getMessage();
			logger.info("阳杰图片查询接口报错：\n", e);
		}finally{
			responseHead.setResponseCode(errNo);
			if("1".equals(errNo)){
				responseHead.setResponseMessage("Success");
			}else{
				responseHead.setResponseMessage(errMsg);
			}
			responseHead.setResponseType("CertifyImages");
			resYJImageVo.setHead(responseHead);
		}
		
		String resXml = ClaimBaseCoder.objToXmlUtf(resYJImageVo);
		logger.info("阳杰图片查询接口返回报文=========：\n"+resXml);
		logger.warn("接口({})调用耗时{}ms", Qurl, System.currentTimeMillis() - t1);
		return resYJImageVo;
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
		TypeNode certifyNode = new TypeNode("certify","索赔清单");
		TypeNode cerNode=new TypeNode("C08","小额简易理赔索赔单证");
    	TypeNode nodeD=new TypeNode("C0803","行驶证");
    	cerNode.addNode(nodeD);
    	certifyNode.addNode(cerNode);
        
        TypeNode pictureNode = new TypeNode("picture","损失照片");
        claimNode.addNode(pictureNode);
		Map<String,String> carNodeMap = new HashMap<String,String>();
		String code = "images";//之后查询上传图片数量
		//查勘
		List<PrpLCheckCarVo> prpLCheckCarVoList = checkTaskService.findCheckCarVo(registNo);
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

        
        if(carNodeMap.size() > 0){
        	TypeNode node=new TypeNode("carLoss","车辆损失");
			pictureNode.addNode(node);
			for(String key:carNodeMap.keySet()){
				TypeNode nodeTem=new TypeNode(key,carNodeMap.get(key));
				node.addNode(nodeTem);
			}
        }
        
		return imgTypeTree;
	}
	
    private void init() {
    	if(checkTaskService == null){
    		checkTaskService = (CheckTaskService)Springs.getBean(CheckTaskService.class);
    	}
    	if(certifyService == null){
    		certifyService = (CertifyService)Springs.getBean(CertifyService.class);
    	}
    	if(lossCarService == null){
    		lossCarService = (LossCarService)Springs.getBean(LossCarService.class);
    	}
    	if(carXyImageService == null){
    		carXyImageService=(CarXyImageService)Springs.getBean(CarXyImageService.class);
    	}
    	if(registService ==null){
    		registService=(RegistService)Springs.getBean(RegistService.class);
    	}
    	if(codeTranService ==null){
    		codeTranService=(CodeTranService)Springs.getBean(CodeTranService.class);
    	}
    	if(wfFlowQueryService ==null){
    		wfFlowQueryService=(WfFlowQueryService)Springs.getBean(WfFlowQueryService.class);
    	}
    	
    }
    
    private String getReqQueryParam(SysUserVo user, String role,String bussNo) {
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
		baseDataVo.setRoleCode(role);
		rootVo.setBaseDataVo(baseDataVo);
		ReqMetaDataQueryVo  metaDataQueryVo=new ReqMetaDataQueryVo();
		List<ReqBatchQueryVo> batchQueryVos=new ArrayList<ReqBatchQueryVo>();
		ReqBatchQueryVo BatchQueryVo=new ReqBatchQueryVo();
		BatchQueryVo.setBusiNo(bussNo);
		BatchQueryVo.setAppCode(CodeConstants.APPCODECLAIM);
		BatchQueryVo.setAppName(CodeConstants.APPNAMECLAIM);
		//回写部分
		ReqVtreeVo vtreeVo=new ReqVtreeVo();
		List<ReqFatherNodeVo> fatherNodeVos=new ArrayList<ReqFatherNodeVo>();
		
		Set<Long> losscarIdSet=new HashSet<Long>();
        List<PrpLDlossCarMainVo> prpLDlossCarMainVoList = lossCarService.findLossCarMainByRegistNo(bussNo);
		if(prpLDlossCarMainVoList != null && prpLDlossCarMainVoList.size() > 0){
			for(PrpLDlossCarMainVo prpLDlossCarMainVo:prpLDlossCarMainVoList){
				if(!CodeConstants.defLossSourceFlag.ADDTIONALDEFLOSS.equals(prpLDlossCarMainVo.getDeflossSourceFlag())){
				  if(prpLDlossCarMainVo.getPrpLDlossCarComps()!=null && prpLDlossCarMainVo.getPrpLDlossCarComps().size()>0){
					  for(PrpLDlossCarCompVo compVo:prpLDlossCarMainVo.getPrpLDlossCarComps()){
						  if("1".equals(compVo.getyJAskPrivceFlag())){
							  losscarIdSet.add(prpLDlossCarMainVo.getId());
						  }
					  }
				  }
			}
		   }
		}
	
		//索赔单证
		ReqFatherNodeVo reqFatherNodeVoc=new ReqFatherNodeVo();
		reqFatherNodeVoc.setId(CodeConstants.APPCODEL1);
		reqFatherNodeVoc.setName(CodeConstants.APPNAMEL1);
		reqFatherNodeVoc.setBarCode("");
		reqFatherNodeVoc.setChildFlag("0");
		reqFatherNodeVoc.setMinpages("0");
		reqFatherNodeVoc.setMaxpages("99999");
		reqFatherNodeVoc.setReseize("800*600");
		reqFatherNodeVoc.setRight("CRUTD");
		
		List<ReqSonNodeVo> reqSonNodeVocs=new ArrayList<ReqSonNodeVo>();
		ReqSonNodeVo reqSonNodeVoc=new ReqSonNodeVo();
		reqSonNodeVoc.setId("certify_C08");
		reqSonNodeVoc.setMaxpages("99999");
		reqSonNodeVoc.setMinpages("0");
		reqSonNodeVoc.setName("小额简易理赔索赔单证");
		reqSonNodeVoc.setReseize("800*600");
		reqSonNodeVoc.setRight("CRUTD");
		reqSonNodeVoc.setChildFlag("0");
		reqSonNodeVoc.setBarCode("");
		
		List<ReqSonNodeVo> SonNodeVocs=new ArrayList<ReqSonNodeVo>();
		ReqSonNodeVo SonNodecVo=new ReqSonNodeVo();
		if(losscarIdSet!=null && losscarIdSet.size()>0){
			SonNodecVo.setId("certify_C0803");
			SonNodecVo.setMaxpages("99999");
			SonNodecVo.setMinpages("0");
			SonNodecVo.setName("行驶证");
			SonNodecVo.setReseize("800*600");
			SonNodecVo.setRight("CRUTD");
			SonNodecVo.setChildFlag("1");
			SonNodecVo.setBarCode("");
			SonNodeVocs.add(SonNodecVo);
			reqSonNodeVoc.setSonNodes(SonNodeVocs);
			reqSonNodeVocs.add(reqSonNodeVoc);
			reqFatherNodeVoc.setSonNodes(reqSonNodeVocs);
			fatherNodeVos.add(reqFatherNodeVoc);
		}else{
			SonNodecVo.setId("certify_C_");
			SonNodecVo.setMaxpages("99999");
			SonNodecVo.setMinpages("0");
			SonNodecVo.setName("行驶证");
			SonNodecVo.setReseize("800*600");
			SonNodecVo.setRight("CRUTD");
			SonNodecVo.setChildFlag("1");
			SonNodecVo.setBarCode("");
			SonNodeVocs.add(SonNodecVo);
			reqSonNodeVoc.setSonNodes(SonNodeVocs);
			reqSonNodeVocs.add(reqSonNodeVoc);
			reqFatherNodeVoc.setSonNodes(reqSonNodeVocs);
			fatherNodeVos.add(reqFatherNodeVoc);
		}
		//车损照片
		ReqFatherNodeVo reqFatherNodeVo=new ReqFatherNodeVo();
		reqFatherNodeVo.setId(CodeConstants.APPCODEL2);
		reqFatherNodeVo.setName(CodeConstants.APPNAMEL2);
		reqFatherNodeVo.setBarCode("");
		reqFatherNodeVo.setChildFlag("0");
		reqFatherNodeVo.setMinpages("0");
		reqFatherNodeVo.setMaxpages("99999");
		reqFatherNodeVo.setReseize("800*600");
		reqFatherNodeVo.setRight("CRUTD");
		
		List<ReqSonNodeVo> reqSonNodeVos=new ArrayList<ReqSonNodeVo>();
		ReqSonNodeVo reqSonNodeVo=new ReqSonNodeVo();
		reqSonNodeVo.setId("carLoss");
		reqSonNodeVo.setMaxpages("99999");
		reqSonNodeVo.setMinpages("0");
		reqSonNodeVo.setName("车辆损失");
		reqSonNodeVo.setReseize("800*600");
		reqSonNodeVo.setRight("CRUTD");
		reqSonNodeVo.setChildFlag("0");
		reqSonNodeVo.setBarCode("");
		
		
		//定损
		if(prpLDlossCarMainVoList != null && prpLDlossCarMainVoList.size() > 0 && losscarIdSet!=null && losscarIdSet.size()>0){
			List<ReqSonNodeVo> SonNodeVos=new ArrayList<ReqSonNodeVo>();
			for(PrpLDlossCarMainVo prpLDlossCarMainVo:prpLDlossCarMainVoList){
				if(losscarIdSet.contains(prpLDlossCarMainVo.getId())){
					String licenseNo = prpLDlossCarMainVo.getLicenseNo();
		    		String serialNo = prpLDlossCarMainVo.getSerialNo().toString();
		    		if(serialNo.equals("1")){//标的车
	        		    licenseNo = "标的车("+licenseNo+")";
		    		}else{
		    			licenseNo = "三者车("+licenseNo+")";
		    		}
		    		//carNodeMap.put(code+"_1_"+serialNo,licenseNo);
		    		
		    		ReqSonNodeVo SonNodeVo=new ReqSonNodeVo();
		    		SonNodeVo.setId("images_1_"+serialNo);
		    		SonNodeVo.setMaxpages("99999");
		    		SonNodeVo.setMinpages("0");
		    		SonNodeVo.setName(licenseNo);
		    		SonNodeVo.setReseize("800*600");
		    		SonNodeVo.setRight("CRUTD");
		    		SonNodeVo.setChildFlag("1");
		    		SonNodeVo.setBarCode("");
					SonNodeVos.add(SonNodeVo);
				}
				
	    		
			}
		    reqSonNodeVo.setSonNodes(SonNodeVos);
		}else{
			List<ReqSonNodeVo> SonNodeVos=new ArrayList<ReqSonNodeVo>();
			ReqSonNodeVo SonNodeVo=new ReqSonNodeVo();
    		SonNodeVo.setId("images_1_");
    		SonNodeVo.setMaxpages("99999");
    		SonNodeVo.setMinpages("0");
    		SonNodeVo.setName("无照片");
    		SonNodeVo.setReseize("800*600");
    		SonNodeVo.setRight("CRUTD");
    		SonNodeVo.setChildFlag("1");
    		SonNodeVo.setBarCode("");
			SonNodeVos.add(SonNodeVo);
			reqSonNodeVo.setSonNodes(SonNodeVos);
		}
		reqSonNodeVos.add(reqSonNodeVo);
		 //复检
        if(wfFlowQueryService.findPrpWfTaskVo(bussNo, FlowNode.DLoss.name() , FlowNode.DLChk.name()).size() > 0 && losscarIdSet.size()>0){
    		ReqSonNodeVo reqSonNodeVofc=new ReqSonNodeVo();
    		reqSonNodeVofc.setId("images_5");
    		reqSonNodeVofc.setMaxpages("99999");
    		reqSonNodeVofc.setMinpages("0");
    		reqSonNodeVofc.setName("复检");
    		reqSonNodeVofc.setReseize("800*600");
    		reqSonNodeVofc.setRight("CRUTD");
    		reqSonNodeVofc.setChildFlag("0");
    		reqSonNodeVofc.setBarCode("");
    		
    		List<ReqSonNodeVo> SonNodeVofcs=new ArrayList<ReqSonNodeVo>();
    		ReqSonNodeVo SonNodefcVo=new ReqSonNodeVo();
    		SonNodefcVo.setId("images_5_1");
    		SonNodefcVo.setMaxpages("99999");
    		SonNodefcVo.setMinpages("0");
    		SonNodefcVo.setName("复检资料");
    		SonNodefcVo.setReseize("800*600");
    		SonNodefcVo.setRight("CRUTD");
    		SonNodefcVo.setChildFlag("1");
    		SonNodefcVo.setBarCode("");
    		SonNodeVofcs.add(SonNodefcVo);
    		reqSonNodeVofc.setSonNodes(SonNodeVofcs);
    		reqSonNodeVos.add(reqSonNodeVofc);
    		
     
        }
        reqFatherNodeVo.setSonNodes(reqSonNodeVos);
		fatherNodeVos.add(reqFatherNodeVo);
		vtreeVo.setFatherNodeVos(fatherNodeVos);
		BatchQueryVo.setVtreeVo(vtreeVo);
		
		batchQueryVos.add(BatchQueryVo);
		metaDataQueryVo.setBatchQueryVos(batchQueryVos);
		rootVo.setMetaDataQueryVo(metaDataQueryVo);
		String ReqXml=ClaimBaseCoder.objToXmlUtf(rootVo);
		logger.info("影像图片查看接口请求报文："+ReqXml);
		return ReqXml;
	}
}
