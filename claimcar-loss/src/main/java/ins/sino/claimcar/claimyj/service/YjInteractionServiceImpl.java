package ins.sino.claimcar.claimyj.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.http.client.ClientProtocolException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.SpringProperties;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;

import ins.platform.common.util.CodeTranUtil;
import ins.platform.utils.DateUtils;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.carinterface.service.ClaimInterfaceLogService;
import ins.sino.claimcar.carinterface.util.BusinessType;
import ins.sino.claimcar.carinterface.vo.ClaimInterfaceLogVo;
import ins.sino.claimcar.claimcarYJ.Util.vo.HttpClientJsonUtil;
import ins.sino.claimcar.claimcarYJ.Util.vo.MD5Util;
import ins.sino.claimcar.claimcarYJ.Util.vo.SignProduce;
import ins.sino.claimcar.claimcarYJ.vo.ResYJVo;
import ins.sino.claimcar.claimyj.vo.DlossCarCompVo;
import ins.sino.claimcar.claimyj.vo.DlossCarSpareVo;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.service.WfTaskHandleService;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.losscar.service.DeflossService;
import ins.sino.claimcar.losscar.service.LossCarService;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarCompVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarMainVo;
import ins.sino.claimcar.mobileCheckCommon.vo.MobileCheckResponseHead;
import ins.sino.claimcar.regist.service.RegistService;
import ins.sino.claimcar.regist.vo.PrpLRegistCarLossVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;
@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"},timeout = 1000000)
@Path("yjInteractionService")
public class YjInteractionServiceImpl implements YjInteractionService {

	private static Logger logger = LoggerFactory.getLogger(YjInteractionServiceImpl.class);
	private static String REVIEW = "review?";
	private static String REVIEWLOSS = "reviewloss?";
	private static String DHIC = "DHIC";
	private static String APPID = "0000005";
	private static String METHOD = "fukanadd";
	private static String COMPANYNAME = "鼎和保险";
	@Autowired
	DeflossService deflossService;
	@Autowired
	RegistService registService;
	@Autowired
    ClaimInterfaceLogService interfaceLogService;
	@Autowired
	WfTaskHandleService wfTaskHandleService;
	@Autowired
	LossCarService lossCarService;
	@Override
	public ResYJVo sendDlChkInfoService(PrpLDlossCarMainVo dlossCarMainVo,SysUserVo userVo) {
		String url = "";
		String requestContent = "";
		String responContent = "";
        JSONObject json = new JSONObject();
        ClaimInterfaceLogVo logVo = new ClaimInterfaceLogVo();
        ResYJVo resYJVo = new ResYJVo();
    	MobileCheckResponseHead responseHead = new MobileCheckResponseHead();
		// 使用代理地址
        url = SpringProperties.getProperty("YANGJIE_URL");
        url = url + REVIEW;
        try {
	        url = url+SignProduce.getSignurl(APPID, METHOD, System.currentTimeMillis());
	        json.put("notificationNo",dlossCarMainVo.getRegistNo());
	        List<PrpLWfTaskVo> wfTaskVoList = null;
	        wfTaskVoList = wfTaskHandleService.findInTask(dlossCarMainVo.getRegistNo(),dlossCarMainVo.getId().toString(),FlowNode.DLChk.name());
	        if(wfTaskVoList != null && wfTaskVoList.size() > 0 ){
	        	json.put("topActualId",wfTaskVoList.get(0).getTaskId());//案卷ActualId
	        }
	        json.put("actualId",DHIC+dlossCarMainVo.getId());//车辆出险ActualId
	        json.put("assessNo",DHIC+dlossCarMainVo.getId());//估损单ActualId
	        String comName = "";
	        if(StringUtils.isNotBlank(dlossCarMainVo.getComCode())){
	        	comName = CodeTranUtil.transCode("ComCodeFull",dlossCarMainVo.getComCode().substring(0, 4)+"0000")+"/";
	        }
	        json.put("insuredBranchName",COMPANYNAME+comName+dlossCarMainVo.getComCode());//估损单ActualId
	        json.put("vin",dlossCarMainVo.getLossCarInfoVo().getVinNo());//车架号
	        json.put("licenseNo",dlossCarMainVo.getLicenseNo());
	        
	        //车型名称
	        if(dlossCarMainVo.getLossCarInfoVo() != null){
	        	json.put("vehicleModelName",dlossCarMainVo.getLossCarInfoVo().getModelName());
	        }else{
	        	json.put("vehicleModelName","");
	        }
	        json.put("garageName",dlossCarMainVo.getRepairFactoryName());//修理厂名称
	        json.put("operateDate",DateUtils.dateToStr(new Date(), DateUtils.YToSec));//发起配件鉴定时间
	        json.put("operator",userVo.getUserCode());//发起鉴定人
	        json.put("mobile",userVo.getPhone());//发起人手机
	        json.put("remark","");//备注
	        
	        List<PrpLDlossCarCompVo> prpLDlossCarComps = dlossCarMainVo.getPrpLDlossCarComps();
	        List<PrpLDlossCarCompVo> prpLDlossCarCompList = new ArrayList<PrpLDlossCarCompVo>();
	        List<DlossCarCompVo> lossCarCompListVo = new ArrayList<DlossCarCompVo>();
			if(prpLDlossCarComps != null && prpLDlossCarComps.size() > 0 ){
				for(PrpLDlossCarCompVo vo : prpLDlossCarComps){
					if(!"1".equals(vo.getIsYangJie()) && vo.getMaterialFee() != null && (vo.getMaterialFee().compareTo(new BigDecimal(5000)) >= 0)){
						DlossCarCompVo lossCarCompVo = new DlossCarCompVo();
						lossCarCompVo.setPartName(vo.getCompName());//配件名称
						lossCarCompVo.setPartNo(vo.getCompCode());//配件编号
						if(vo.getId() != null){
							lossCarCompVo.setPartId(vo.getId().toString());//配件ID
						}
						if(vo.getMaterialFee() != null){
							lossCarCompVo.setUnitPrice(vo.getMaterialFee().toString());//配件估损单价
						}
						vo.setIsYangJie("1");
						prpLDlossCarCompList.add(vo);
						lossCarCompListVo.add(lossCarCompVo);
					}
				}
			}
			if(lossCarCompListVo != null && lossCarCompListVo.size() > 0){
				//更新PrpLDlossCarCompVo
				deflossService.saveOrUpdateCarComp(prpLDlossCarCompList,dlossCarMainVo);
				String compList = JSON.toJSON(lossCarCompListVo).toString();
				json.put("partList",compList);
				requestContent = json.toString();
				logger.info("阳杰汽配复勘新增接口请求报文"+requestContent);
				responContent = HttpClientJsonUtil.postUtf(url, requestContent,"UTF-8");
				logger.info("阳杰汽配复勘新增接口返回报文=================="+responContent);
				if(responContent!=null && !"".equals(responContent)){
				    JSONObject rejson = JSONObject.fromObject(responContent);
		            String error = rejson.getString("error");
		            String msg = rejson.getString("msg");
		            responseHead.setResponseType("");
		            responseHead.setResponseCode(error);
		            responseHead.setResponseMessage(msg);
		            if("000000".equals(error)){
		            	logVo.setStatus("1");
		            }else{
		            	logVo.setStatus("0");
		            }
					logVo.setErrorCode(error);
					logVo.setErrorMessage(msg);
				}else{
					logger.info("阳杰汽配复勘新增接口没有返回信息===================");
					logVo.setStatus("0");
					logVo.setErrorMessage("没有返回信息");
				}
			}
		} catch (ClientProtocolException e) {
			logger.info("阳杰汽配复勘新增接口报错==================="+e.getMessage());
			e.printStackTrace();
			logVo.setStatus("0");
			logVo.setErrorMessage(e.getMessage());
		} catch (IOException e) {
			logger.info("阳杰汽配复勘新增接口报错==================="+e.getMessage());
			e.printStackTrace();
			logVo.setStatus("0");
			logVo.setErrorMessage(e.getMessage());
		}finally{

			logVo.setRequestUrl(url);
			logVo.setRequestXml(requestContent);
			logVo.setResponseXml(responContent);
			logVo.setRegistNo(dlossCarMainVo.getRegistNo());
			
			logVo.setBusinessType(BusinessType.YJ_DLCHK.name());
            logVo.setBusinessName(BusinessType.YJ_DLCHK.getName());
            
            logVo.setOperateNode(FlowNode.DLChk.name());
            logVo.setRequestTime(new Date());
            logVo.setCreateTime(new Date());
            logVo.setCreateUser(userVo.getUserCode());
            logVo.setRemark(userVo.getPhone());
            logVo.setCompensateNo(dlossCarMainVo.getId().toString());
            interfaceLogService.save(logVo);
		
		}
        resYJVo.setHead(responseHead);
		return resYJVo;
	}
	@Override
	public ResYJVo sendVLossInfoService(PrpLDlossCarMainVo dlossCarMainVo,SysUserVo userVo) {
		ResYJVo resYJVo = new ResYJVo();
		MobileCheckResponseHead responseHead = new MobileCheckResponseHead();
		String url = "";
		ClaimInterfaceLogVo logVo = new ClaimInterfaceLogVo();
		String requestContent = "";
		String responContent = "";
		try {
			PrpLRegistVo prpLRegistVo =registService.findRegistByRegistNo(dlossCarMainVo.getRegistNo());
			List<PrpLRegistCarLossVo>  registCarLossVos = prpLRegistVo.getPrpLRegistCarLosses();
	        JSONObject json = new JSONObject();
			// 使用代理地址
	        url = SpringProperties.getProperty("YANGJIE_URL");
	        url = url + REVIEWLOSS;
	        url = url+SignProduce.getSignurl(APPID, METHOD, System.currentTimeMillis());
	        json.put("thirdInquiryNo",MD5Util.getMd5(""+System.currentTimeMillis()));
	        String brand = "";
	        if(registCarLossVos != null && !registCarLossVos.isEmpty()){
	        	for(PrpLRegistCarLossVo registCarLossVo : registCarLossVos){
	        		if(dlossCarMainVo.getLicenseNo().equals(registCarLossVo.getLicenseNo())){
	        			brand = registCarLossVo.getBrand();
	        			break;
	        		}
	        	}
	        }
	        json.put("brandName",brand);
	        //车型名称
	        if(dlossCarMainVo.getLossCarInfoVo() != null){
	        	json.put("modelName",dlossCarMainVo.getLossCarInfoVo().getModelName());
	        }else{
	        	json.put("modelName","");
	        }
	        json.put("vin",dlossCarMainVo.getLossCarInfoVo().getVinNo());
	        json.put("plateno",dlossCarMainVo.getLicenseNo());
	        String comName = "";
	        if(StringUtils.isNotBlank(dlossCarMainVo.getComCode())){
	        	comName = CodeTranUtil.transCode("ComCodeFull",dlossCarMainVo.getComCode().substring(0, 4)+"0000");
	        }
	        json.put("branch",COMPANYNAME+comName);
	        json.put("inquiryName",userVo.getUserName());
	        json.put("inquiryTel",userVo.getMobile());//询价人电话
	        json.put("submitTime",DateUtils.dateToStr(new Date(), DateUtils.YToSec));
	        json.put("remark",dlossCarMainVo.getRemark());
			String proName = CodeTranUtil.transCode("DamageAreaCode", prpLRegistVo.getDamageAreaCode().substring(0, 2)+"0000");//省名称
			String areaName = CodeTranUtil.transCode("DamageAreaCode", prpLRegistVo.getDamageAreaCode().substring(0, 4)+"00");
	        json.put("proname",proName);
	        json.put("areaname",areaName);
	        json.put("reportno",dlossCarMainVo.getRegistNo());
	        List<PrpLDlossCarCompVo> prpLDlossCarComps = dlossCarMainVo.getPrpLDlossCarComps();
	        List<DlossCarSpareVo> carSpareVos = new ArrayList<DlossCarSpareVo>();
	        if(prpLDlossCarComps != null && prpLDlossCarComps.size() > 0 ){
				for(PrpLDlossCarCompVo vo : prpLDlossCarComps){
					if("1".equals(vo.getyJAskPrivceFlag())){
						DlossCarSpareVo carSpareVo = new DlossCarSpareVo();
						carSpareVo.setThirdPartEnquiryId(vo.getId().toString());
						carSpareVo.setPartcode(vo.getOriginalId());
						carSpareVo.setPartname(vo.getOriginalName());
						if(vo.getQuantity() != null){
							carSpareVo.setPartnum(vo.getQuantity().toString());
						}
						if(vo.getVeriMaterFee() != null){
							carSpareVo.setQuotationAmount(vo.getVeriMaterFee().toString());
						}
						//1-4S价格 2-市场原厂价格 3-品牌价格 4-适用价格 5-再制造价格
						if(CodeConstants.PriceType.PRICETYPE4S.equals(vo.getPriceType()) || CodeConstants.PriceType.ORIGINAL.equals(vo.getPriceType())){
							carSpareVo.setParttype("1");
						}else if(CodeConstants.PriceType.BRAND.equals(vo.getPriceType())){
							carSpareVo.setParttype("2");
						}else if(CodeConstants.PriceType.APPLY.equals(vo.getPriceType())){
							carSpareVo.setParttype("3");
						}
						carSpareVo.setPartRemark(vo.getRemark());
						carSpareVos.add(carSpareVo);
					}
				}
			}
	        if(carSpareVos != null && carSpareVos.size() > 0){
				String spareList = JSON.toJSON(carSpareVos).toString();
				json.put("carspareList",spareList);
				requestContent = json.toString();
				logger.info("阳杰汽配核损接口请求报文"+requestContent);
				
					responContent = HttpClientJsonUtil.postUtf(url, requestContent,"UTF-8");
					logger.info("阳杰汽配核损接口返回报文=================="+responContent);
					if(responContent!=null && !"".equals(responContent)){
					    JSONObject rejson = JSONObject.fromObject(responContent);
			            String error = rejson.getString("error");
			            String msg = rejson.getString("msg");
			            responseHead.setResponseType("");
			            responseHead.setResponseCode(error);
			            responseHead.setResponseMessage(msg);
			            if("000000".equals(error)){
			            	logVo.setStatus("1");
			            }else{
			            	logVo.setStatus("0");
			            }
						logVo.setErrorCode(error);
						logVo.setErrorMessage(msg);
						
					}else{
						logger.info("阳杰汽配核损接口没有返回信息===================");
						logVo.setStatus("0");
						logVo.setErrorMessage("没有返回信息");
					}
				
			}
        } catch (ClientProtocolException e) {
			logger.info("阳杰汽配核损接口报错==================="+e.getMessage());
			e.printStackTrace();
			logVo.setStatus("0");
			logVo.setErrorMessage(e.getMessage());
		} catch (IOException e) {
			logger.info("阳杰汽配核损接口报错==================="+e.getMessage());
			e.printStackTrace();
			logVo.setStatus("0");
			logVo.setErrorMessage(e.getMessage());
		}finally{
			logVo.setRequestUrl(url);
			logVo.setRequestXml(requestContent);
			logVo.setResponseXml(responContent);
			logVo.setRegistNo(dlossCarMainVo.getRegistNo());
			
			logVo.setBusinessType(BusinessType.YJ_VLoss.name());
            logVo.setBusinessName(BusinessType.YJ_VLoss.getName());
            
            logVo.setOperateNode(FlowNode.VLoss.name());
            logVo.setRequestTime(new Date());
            logVo.setCreateTime(new Date());
            logVo.setCreateUser(userVo.getUserCode());
            logVo.setRemark(userVo.getMobile());
            logVo.setCompensateNo(dlossCarMainVo.getId().toString());
            interfaceLogService.save(logVo);
		}
        resYJVo.setHead(responseHead);
		return resYJVo;
	}
}
