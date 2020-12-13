package ins.sino.claimcar.claimcarYJ.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Path;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.taglibs.standard.tag.common.core.SetSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.SpringProperties;

import com.alibaba.dubbo.config.annotation.Service;

import ins.platform.common.service.facade.CodeTranService;
import ins.platform.sysuser.service.facade.SysUserService;
import ins.platform.utils.ClaimBaseCoder;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.carinterface.service.ClaimInterfaceLogService;
import ins.sino.claimcar.carinterface.util.BusinessType;
import ins.sino.claimcar.carinterface.vo.ClaimInterfaceLogVo;
import ins.sino.claimcar.claimcarYJ.Util.vo.SignProduce;
import ins.sino.claimcar.claimcarYJ.vo.CarspareAddVo;
import ins.sino.claimcar.claimcarYJ.vo.ClaimcarJYAddVo;
import ins.sino.claimcar.claimcarYJ.vo.ClaimcarSupplyVo;
import ins.sino.claimcar.claimcarYJ.vo.CompVo;
import ins.sino.claimcar.claimcarYJ.vo.PrpLyjlosscarCompVo;
import ins.sino.claimcar.claimcarYJ.vo.PrplyjPartoffersVo;
import ins.sino.claimcar.claimcarYJ.vo.ReqHeadYJVo;
import ins.sino.claimcar.claimcarYJ.vo.SupplyYjVo;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.service.RepairFactoryService;
import ins.sino.claimcar.hnbxrest.vo.RespondMsg;
import ins.sino.claimcar.losscar.service.LossCarService;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarCompVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarInfoVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarMainVo;
import ins.sino.claimcar.manager.vo.PrpLRepairFactoryVo;
import ins.sino.claimcar.regist.service.RegistService;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;

@Service(protocol = {"dubbo"}, validation = "true" , registry = {"default"}, timeout = 1000000)
@Path("claimcarYJService")
public class ClaimcarYJServiceIpml implements ClaimcarYJService{
	private static Logger logger = LoggerFactory.getLogger(ClaimcarYJServiceIpml.class);
	@Autowired
	LossCarService lossCarService;
	@Autowired
	CodeTranService codeTranService;
	@Autowired
	RegistService registService;
	@Autowired
	ClaimInterfaceLogService claimInterfaceLogService;
	@Autowired
	RepairFactoryService repairFactoryService;
	@Autowired
	ClaimcarYJComService claimcarYJComService;
	@Autowired
	SysUserService sysUserService;
    /**
     * 阳杰询价接口
     */
	@Override
	public void claimcarYJAskPriceAdd(Long id, SysUserVo userVo) {
		ReqHeadYJVo headVo=new ReqHeadYJVo();
		headVo=assginHeadVo(headVo,"0000005","add");
		ClaimInterfaceLogVo logVo = new ClaimInterfaceLogVo();
		 String url = "";
		 url =SpringProperties.getProperty("YANGJIE_URL")+"add";
		 url=url+"?"+headVo.getSign();
		String requestContent = "";
		String responContent = "";
		JSONObject json = new JSONObject();
		JSONArray arrayJson=new JSONArray();
		ClaimcarJYAddVo jyclaimcarAddVo=new ClaimcarJYAddVo();
		PrpLDlossCarMainVo prpLDlossCarMainVo=lossCarService.findLossCarMainById(id);
        try{
            jyclaimcarAddVo=assignmentAdd(id,userVo,prpLDlossCarMainVo);
            json.put("carId",jyclaimcarAddVo.getCarId());
            json.put("thirdInquiryNo",jyclaimcarAddVo.getThirdInquiryNo());
            json.put("brandName",jyclaimcarAddVo.getBrandName());
            json.put("modelName",jyclaimcarAddVo.getModelName());
            json.put("vin",jyclaimcarAddVo.getVin());
            json.put("plateno",jyclaimcarAddVo.getPlateno());
            json.put("branch",jyclaimcarAddVo.getBranch());
            json.put("garageName",jyclaimcarAddVo.getGarageName());
            json.put("garageTel",jyclaimcarAddVo.getGarageTel());
            json.put("garageCon",jyclaimcarAddVo.getGarageCon());
            json.put("inquiryName",jyclaimcarAddVo.getInquiryName());
            json.put("inquiryTel",jyclaimcarAddVo.getInquiryTel());
            json.put("submitTime",jyclaimcarAddVo.getSubmitTime());
            json.put("remark",jyclaimcarAddVo.getRemark());
            json.put("proname",jyclaimcarAddVo.getProname());
            json.put("areaname",jyclaimcarAddVo.getAreaname());
            json.put("reportno",jyclaimcarAddVo.getReportno());
            arrayJson.addAll(jyclaimcarAddVo.getCarspareList());
            json.put("carspareList",arrayJson);
            requestContent = json.toString();
			logger.info("send======阳杰询价新增接口请求报文："+requestContent);

            HttpPost httpPost = new HttpPost(url);
            if(StringUtils.isNotBlank(requestContent)){
                StringEntity requestEntity = new StringEntity(requestContent,ContentType.create("application/json","UTF-8"));
                httpPost.setEntity(requestEntity);
            }
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity httpEntity = response.getEntity();
            responContent = EntityUtils.toString(httpEntity);
            responContent = new String(responContent.getBytes("UTF-8"), "UTF-8");
			logger.info("return======阳杰询价新增接口结果返回报文："+responContent);
			if(StringUtils.isBlank(responContent)){
				throw new Exception("阳杰询价新增接口异常，未返回信息!");
			}
            JSONObject rejson = JSONObject.fromObject(responContent);
            String error = rejson.getString("error");
            String msg = rejson.getString("msg");
            if("000000".equals(error)){
            	logVo.setStatus("1");
            }else{
            	logVo.setStatus("0");
            	logVo.setErrorCode(error);
            }
            logVo.setErrorMessage(msg);
            
           
        }catch(Exception e){
            logger.error("阳杰新增接口错误信息：",e);
            logVo.setStatus("0");
            if(StringUtils.isNotBlank(e.getMessage()) && e.getMessage().length()>900){
            	logVo.setErrorMessage(e.getMessage().substring(0,900));
            }else{
            	logVo.setErrorMessage(e.getMessage());
            }
            
        }finally{
			// 存日志表
        	logVo.setFlag(id+"");
            logVo.setRequestUrl(url);
            logVo.setRequestXml(ClaimBaseCoder.objToXml(jyclaimcarAddVo));
            logVo.setResponseXml(responContent);
            logVo.setRegistNo(jyclaimcarAddVo.getReportno());
            logVo.setBusinessType(BusinessType.YJ_claimAdd.name());
            logVo.setBusinessName(BusinessType.YJ_claimAdd.getName());
            logVo.setOperateNode(FlowNode.DLCar.name());
            logVo.setRequestTime(new Date());
            logVo.setCreateTime(new Date());
            logVo.setCreateUser(userVo.getUserCode());
            claimInterfaceLogService.save(logVo);
            if("1".equals(logVo.getStatus())){
             List<CarspareAddVo> addVos=jyclaimcarAddVo.getCarspareList();
             List<String> carMainIds=new ArrayList<String>();
             if(addVos!=null && addVos.size()>0){
            	for(CarspareAddVo vo:addVos){
            		carMainIds.add(vo.getThirdPartEnquiryId());
            	}
             }
             if(prpLDlossCarMainVo!=null && prpLDlossCarMainVo.getPrpLDlossCarComps()!=null && prpLDlossCarMainVo.getPrpLDlossCarComps().size()>0){
            	for(PrpLDlossCarCompVo compVo:prpLDlossCarMainVo.getPrpLDlossCarComps()){
            		if(carMainIds.contains(compVo.getId().toString())){
            			compVo.setyJAskPrivceFlag("1");
            		}
            	}
             }
            
             lossCarService.updateJyDlossCarMain(prpLDlossCarMainVo);
        }
        }
	}
	
	public ClaimcarJYAddVo assignmentAdd(Long id,SysUserVo userVo,PrpLDlossCarMainVo prpLDlossCarMainVo){
		ClaimcarJYAddVo jyclaimAddVo=new ClaimcarJYAddVo();
		PrpLDlossCarInfoVo PrpLDlossCarInfoVo=prpLDlossCarMainVo.getLossCarInfoVo();
		PrpLRegistVo prpLRegistVo=registService.findRegistByRegistNo(prpLDlossCarMainVo.getRegistNo());
		if(prpLDlossCarMainVo!=null && PrpLDlossCarInfoVo!=null){
			jyclaimAddVo.setThirdInquiryNo(id+"T"+System.currentTimeMillis());//三方分配的id+时间戳
			jyclaimAddVo.setBrandName(PrpLDlossCarInfoVo.getBrandName());//车辆品牌
			jyclaimAddVo.setModelName(PrpLDlossCarInfoVo.getModelName());//车辆型号
			jyclaimAddVo.setVin(PrpLDlossCarInfoVo.getVinNo());//vin码
			jyclaimAddVo.setPlateno(PrpLDlossCarInfoVo.getLicenseNo());//车牌号
			String comCodeName = codeTranService.transCode("ComCode",prpLRegistVo.getComCode());
			jyclaimAddVo.setBranch(CodeConstants.compName+comCodeName);
			jyclaimAddVo.setGarageName(prpLDlossCarMainVo.getRepairFactoryName());//修理厂
			jyclaimAddVo.setGarageTel(prpLDlossCarMainVo.getFactoryMobile());//修理厂联系电话
			jyclaimAddVo.setGarageCon(prpLDlossCarMainVo.getFactoryMobile());//修理厂联系人
        	jyclaimAddVo.setInquiryName(userVo.getUserName());//询价人姓名
			jyclaimAddVo.setInquiryTel(userVo.getMobile());//询价人电话
			jyclaimAddVo.setSubmitTime(DateFormatString(new Date()));//发起询价时间
			jyclaimAddVo.setCarId(id.toString());
			if(prpLRegistVo!=null && StringUtils.isNotBlank(prpLRegistVo.getDamageAreaCode())){
				 jyclaimAddVo.setProname(codeTranService.transCode("AreaCode",prpLRegistVo.getDamageAreaCode().substring(0,2)+"0000"));//省名称
				 jyclaimAddVo.setAreaname(codeTranService.transCode("AreaCode",prpLRegistVo.getDamageAreaCode().substring(0,4)+"00"));//市名称
				 if(StringUtils.isBlank(jyclaimAddVo.getProname()) && StringUtils.isNotBlank(jyclaimAddVo.getAreaname())){
					 String[] areaCodes=jyclaimAddVo.getAreaname().split("-");
					 if(areaCodes!=null && areaCodes.length>0){
						 jyclaimAddVo.setProname(areaCodes[0]);
					 }
				 
				 }
			 }
			 jyclaimAddVo.setRemark("");//询价备注
			 jyclaimAddVo.setReportno(prpLDlossCarMainVo.getRegistNo());//报案号
			List<CarspareAddVo> carspareList=new ArrayList<CarspareAddVo>();
			List<PrpLDlossCarCompVo> carcompVos=prpLDlossCarMainVo.getPrpLDlossCarComps();
			if(carcompVos!=null && carcompVos.size()>0){
				for(PrpLDlossCarCompVo compVo:carcompVos){
					CarspareAddVo addVo=new CarspareAddVo();
					if(compVo.getMaterialFee()!=null && compVo.getMaterialFee().intValue()>=1000 && !"1".equals(compVo.getyJAskPrivceFlag())){
						addVo.setThirdPartEnquiryId(compVo.getId().toString());//三方分配的零件ID
						addVo.setPartcode(compVo.getOriginalId());//零配件原厂编码
						addVo.setPartname(compVo.getOriginalName());//零配件原厂名称
						addVo.setPartnum(compVo.getQuantity()+"");//换件数量
						addVo.setQuotationAmount(compVo.getMaterialFee()+"");//询价单价
						//1-4S价格 2-市场原厂价格 3-品牌价格 4-适用价格 5-再制造价格
						if(CodeConstants.PriceType.PRICETYPE4S.equals(compVo.getPriceType()) || CodeConstants.PriceType.ORIGINAL.equals(compVo.getPriceType())){
							addVo.setParttype("1");
						}else if(CodeConstants.PriceType.BRAND.equals(compVo.getPriceType())){
							addVo.setParttype("2");
						}else if(CodeConstants.PriceType.APPLY.equals(compVo.getPriceType())){
							addVo.setParttype("3");
						}else{
							addVo.setParttype("1");//供货配件品质1 原厂件 2精品件 3合格件 4定制件
						}
						addVo.setPartRemark("");
						carspareList.add(addVo);
					}
					
				}
			}
			jyclaimAddVo.setCarspareList(carspareList);
			
		}
		return jyclaimAddVo;
	}
	/**
     * 阳杰下单接口
     */
	@Override
	public Map<String,String> claimcarYJOrder(Long id, SysUserVo userVo,List<SupplyYjVo> supplyVos) {
		Map<String,String> map=new HashMap<String,String>();
		ReqHeadYJVo headVo=new ReqHeadYJVo();
		headVo=assginHeadVo(headVo,"0000005","supply");
		ClaimInterfaceLogVo logVo = new ClaimInterfaceLogVo();
		 String url = "";
		 url = SpringProperties.getProperty("YANGJIE_URL")+"supply";
		 url=url+"?"+headVo.getSign();
		String requestContent = "";
		String responContent = "";
		JSONObject json = new JSONObject();
		JSONArray arrayJson=new JSONArray();
		ClaimcarSupplyVo claimcarSupplyVo=new ClaimcarSupplyVo();
		PrpLDlossCarMainVo prpLDlossCarMainVo=lossCarService.findLossCarMainById(id);
		PrpLyjlosscarCompVo prpLyjlosscarCompVo=claimcarYJComService.findPrpLyjlosscarCompBypartId(prpLDlossCarMainVo.getId().toString());
        try{
        	claimcarSupplyVo=assignmentSupply(id,userVo,supplyVos,prpLDlossCarMainVo,prpLyjlosscarCompVo);
            json.put("supplyId",claimcarSupplyVo.getSupplyid());
            json.put("thirdInquiryNo",claimcarSupplyVo.getThirdinquiryno());
            json.put("brandName",claimcarSupplyVo.getBrandName());
            json.put("modelName",claimcarSupplyVo.getModelName());
            json.put("vin",claimcarSupplyVo.getVin());
            json.put("plateno",claimcarSupplyVo.getPlateno());
            json.put("reportno",claimcarSupplyVo.getReportno());
            json.put("branch",claimcarSupplyVo.getBranch());
            json.put("garageName",claimcarSupplyVo.getGaragename());
            json.put("garageTel",claimcarSupplyVo.getGaragetel());
            json.put("garageCon",claimcarSupplyVo.getGaragecon());
            json.put("areaname",claimcarSupplyVo.getAreaname());
            json.put("garageType",claimcarSupplyVo.getGaragetype());
            json.put("supplyName",claimcarSupplyVo.getSupplyname());
            json.put("supplyTel",claimcarSupplyVo.getSupplytel());
            json.put("submitTime",claimcarSupplyVo.getSubmittime());
            json.put("remark",claimcarSupplyVo.getRemark());
            arrayJson.addAll(claimcarSupplyVo.getCarsparelist());
            json.put("carspareList",arrayJson);
            requestContent = json.toString();
			logger.info("send======阳杰下单接口请求报文"+claimcarSupplyVo.getReportno()+":"+requestContent);

            HttpPost httpPost = new HttpPost(url);
            if(StringUtils.isNotBlank(requestContent)){
                StringEntity requestEntity = new StringEntity(requestContent,ContentType.create("application/json","UTF-8"));
                httpPost.setEntity(requestEntity);
            }
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity httpEntity = response.getEntity();
            responContent = EntityUtils.toString(httpEntity);
            responContent = new String(responContent.getBytes("UTF-8"), "UTF-8");
			logger.info("return======阳杰下单接口结果返回报文"+claimcarSupplyVo.getReportno()+":"+responContent);
			if(StringUtils.isBlank(responContent)){
				throw new Exception("阳杰下单接口异常，未返回信息!"+claimcarSupplyVo.getReportno()+":");
			}
            JSONObject rejson = JSONObject.fromObject(responContent);
            String error = rejson.getString("error");
            String msg = rejson.getString("msg");
            if("000000".equals(error)){
            	logVo.setStatus("1");
            }else{
            	logVo.setStatus("0");
            	logVo.setErrorCode(error);
            	logVo.setErrorMessage(msg);
            }
            
            
           
        }catch(Exception e){
        	logger.error("阳杰下单接口错误信息"+claimcarSupplyVo.getReportno(),e);
            logVo.setStatus("0");
            logVo.setErrorMessage(e.getMessage());
        }finally{
			// 存日志表
        	logVo.setFlag(id+"");
            logVo.setRequestUrl(url);
            logVo.setRequestXml(ClaimBaseCoder.objToXml(claimcarSupplyVo));
            logVo.setResponseXml(responContent);
            logVo.setRegistNo(claimcarSupplyVo.getReportno());
            logVo.setBusinessType(BusinessType.YJ_claimSupply.name());
            logVo.setBusinessName(BusinessType.YJ_claimSupply.getName());
            logVo.setOperateNode(FlowNode.DLCar.name());
            logVo.setRequestTime(new Date());
            logVo.setCreateTime(new Date());
            logVo.setCreateUser(userVo.getUserCode());
            claimInterfaceLogService.save(logVo);
            map.put("status",logVo.getStatus());
            map.put("errorMsg",logVo.getErrorMessage());
            List<String> partenquiryIds=new ArrayList<String>();
            if(supplyVos!=null && supplyVos.size()>0){
            	for(SupplyYjVo yjVo:supplyVos){
            	   partenquiryIds.add(yjVo.getThirdpartenquiryid());
            	}
            }
            if("1".equals(logVo.getStatus())){
            	if(prpLyjlosscarCompVo!=null && prpLyjlosscarCompVo.getPrplyjPartoffers()!=null && prpLyjlosscarCompVo.getPrplyjPartoffers().size()>0){
            		for(PrplyjPartoffersVo vo:prpLyjlosscarCompVo.getPrplyjPartoffers()){
            			if(StringUtils.isNotBlank(vo.getPartenquiryId()) && partenquiryIds.contains(vo.getPartenquiryId())){
            				vo.setSupplyFlag("1");
            			}
            			
            		}
            	}
            	claimcarYJComService.updatePrpLyjlosscarComp(prpLyjlosscarCompVo);
            }
            
        }
		
        
		return map;
	}
	
	private ClaimcarSupplyVo assignmentSupply(Long id,SysUserVo userVo,List<SupplyYjVo> supplyVos,PrpLDlossCarMainVo prpLDlossCarMainVo,PrpLyjlosscarCompVo prpLyjlosscarCompVo) throws Exception{
		ClaimcarSupplyVo supplyVo=new ClaimcarSupplyVo();
		PrpLRegistVo prpLRegistVo=registService.findRegistByRegistNo(prpLDlossCarMainVo.getRegistNo());
		PrpLRepairFactoryVo repairVo=null;
		if(prpLDlossCarMainVo!=null && StringUtils.isNotBlank(prpLDlossCarMainVo.getRepairFactoryCode())){
			 repairVo=repairFactoryService.findFactoryById(prpLDlossCarMainVo.getRepairFactoryCode());
			if(repairVo==null){//解决历史数据问题
			 repairVo=repairFactoryService.findFactoryByCode(prpLDlossCarMainVo.getRepairFactoryCode(), prpLDlossCarMainVo.getRepairFactoryType());
			}
		   
		}
		if(repairVo!=null){
			supplyVo.setAreaname(StringUtils.isNotBlank(repairVo.getAreaCode())?codeTranService.transCode("AreaCode",repairVo.getAreaCode()):"");
			supplyVo.setGaragecon(repairVo.getLinker());
			supplyVo.setGaragename(repairVo.getFactoryName());
			supplyVo.setGaragetel(repairVo.getMobile());
			supplyVo.setGaragetype("1");
		}
		String comCodeName = codeTranService.transCode("ComCode",prpLRegistVo.getComCode());
		supplyVo.setBranch(CodeConstants.compName+comCodeName);
		supplyVo.setRemark("");
		supplyVo.setReportno(prpLRegistVo.getRegistNo());
		supplyVo.setSubmittime(DateFormatString(new Date()));
		supplyVo.setSupplyname(userVo.getUserName());
		SysUserVo sysUserVo=sysUserService.findByUserCode(userVo.getUserCode());
		if(sysUserVo!=null){
		supplyVo.setSupplytel(sysUserVo.getMobile());
		}
		if(prpLyjlosscarCompVo!=null){
			supplyVo.setBrandName(prpLyjlosscarCompVo.getBrandName());
			supplyVo.setModelName(prpLyjlosscarCompVo.getModelName());
			supplyVo.setPlateno(prpLyjlosscarCompVo.getPlateno());
			supplyVo.setThirdinquiryno(prpLyjlosscarCompVo.getEnquiryno());
			supplyVo.setVin(prpLyjlosscarCompVo.getVin());
			supplyVo.setSupplyid(prpLyjlosscarCompVo.getId().toString());
		}
		List<CompVo> compVos=new ArrayList<CompVo>();
		if(supplyVos!=null && supplyVos.size()>0){
			for(SupplyYjVo vo:supplyVos){
				CompVo compVo=new CompVo();
				compVo.setThirdpartenquiryid(vo.getThirdpartenquiryid());//三方分配的零件ID	
				compVo.setPartcode(vo.getPartcode());//零配件原厂编码
				compVo.setPartName(vo.getPartName());//零配件原厂名称
				compVo.setPartnum(vo.getDlossNums());//换件数量	
				compVo.setQuotationamount(vo.getQuotationAmount());//供货协商单价
				//1-4S价格 2-市场原厂价格 3-品牌价格 4-适用价格 5-再制造价格
				if(CodeConstants.PriceType.PRICETYPE4S.equals(vo.getPriceType()) || CodeConstants.PriceType.ORIGINAL.equals(vo.getPriceType())){
					compVo.setParttype("1");
				}else if(CodeConstants.PriceType.BRAND.equals(vo.getPriceType())){
					compVo.setParttype("2");
				}else if(CodeConstants.PriceType.APPLY.equals(vo.getPriceType())){
					compVo.setParttype("3");
				}else{
					compVo.setParttype("1");//供货配件品质
				}
				compVo.setPartremark("");
				compVos.add(compVo);
			}
		}
		
		supplyVo.setCarsparelist(compVos);
		return supplyVo;
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

	
	private ReqHeadYJVo assginHeadVo(ReqHeadYJVo headVo,String appId,String MethodName){
		headVo.setAppId(appId);
		headVo.setTimeStamp(System.currentTimeMillis()+"");
		String signStr=SignProduce.getSignurl(headVo.getAppId(),MethodName,System.currentTimeMillis());
		headVo.setSign(signStr);
		return headVo;
		
	}
	
	
	
}
