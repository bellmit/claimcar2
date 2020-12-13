package ins.sino.claimcar.vat.service.spring;

import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.carinterface.service.ClaimInterfaceLogService;
import ins.sino.claimcar.carinterface.util.BusinessType;
import ins.sino.claimcar.carinterface.vo.ClaimInterfaceLogVo;
import ins.sino.claimcar.claim.service.CompensateTaskService;
import ins.sino.claimcar.claim.vo.PrpLCompensateVo;
import ins.sino.claimcar.claim.vo.PrpLPadPayMainVo;
import ins.sino.claimcar.pay.service.PadPayService;
import ins.sino.claimcar.vat.service.BilllclaimService;
import ins.sino.claimcar.vat.service.ExchangeVatService;
import ins.sino.claimcar.vat.vo.BillImageDtoVo;
import ins.sino.claimcar.vat.vo.BillImageReqVo;
import ins.sino.claimcar.vat.vo.BindInvoiceReqDto;
import ins.sino.claimcar.vat.vo.PrpJBatchDto;
import ins.sino.claimcar.vat.vo.PrpLbillinfoVo;
import ins.sino.claimcar.vat.vo.PrplAcbillcontVo;
import ins.sino.claimcar.vat.vo.UntyingInvoiceResDtoVo;
import ins.sino.claimcar.vat.vo.BillImageViewSonVo;
import ins.sino.claimcar.vat.vo.BillImageViewVo;
import ins.sino.claimcar.vat.vo.PrpLbillcontVo;
import ins.sino.claimcar.vat.vo.ResmessageVo;
import ins.sino.claimcar.vat.vo.UntyingReqDtoVo;
import ins.sino.claimcar.vat.vo.UntyingResDtoVo;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Path;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.PropertyFilter;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.SpringProperties;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;

@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"})
@Path("exchangeVatService")
public class ExchangeVatServiceImpl implements ExchangeVatService{
	private static Logger logger = LoggerFactory.getLogger(ExchangeVatServiceImpl.class);
	
	@Autowired
	ClaimInterfaceLogService claimInterfaceLogService;
	@Autowired
	BilllclaimService billlclaimService;
	@Autowired
	PadPayService padPayService;
	@Autowired
	CompensateTaskService compensateTaskService;
    /**
     * 发票图片信息推送给vat
     */
	@Override
	public int sendBillImageToVat(BillImageViewVo billImageViewVo,SysUserVo userVo){
		int flag=1;//返回结果标志 1--表示成功，0--表示失败
		BillImageReqVo billImageReqVo=new BillImageReqVo();
		List<BillImageDtoVo> billList=new ArrayList<BillImageDtoVo>();
		if(billImageViewVo!=null){
			billImageReqVo.setSendTime(billImageViewVo.getSendTime());
			billImageReqVo.setSystemType(billImageViewVo.getSystemType());
			if(billImageViewVo.getBillImageDtoList()!=null && billImageViewVo.getBillImageDtoList().size()>0){
				for(BillImageViewSonVo ImageViewSonVo:billImageViewVo.getBillImageDtoList()){
					BillImageDtoVo billImageDtoVo=new BillImageDtoVo();
					billImageDtoVo.setBussNo(ImageViewSonVo.getBussNo());
					billImageDtoVo.setBussType(ImageViewSonVo.getBussType());
					billImageDtoVo.setChargeCode(ImageViewSonVo.getChargeCode());
					billImageDtoVo.setIdEcdinvoiceocr(ImageViewSonVo.getIdEcdinvoiceocr());
					billImageDtoVo.setBillUrl(ImageViewSonVo.getBillUrl());
					billList.add(billImageDtoVo);
				}
			}
		}
		billImageReqVo.setBillImageDtoList(billList);
		ClaimInterfaceLogVo logVo = new ClaimInterfaceLogVo();
		//请求Url
		String url =SpringProperties.getProperty("vat_sendurl");
		String requestContent = "";//请求内容
		String responContent = "";//返回内容
        try{
			if(billImageReqVo!=null){
				JsonConfig jsonConfig = new JsonConfig();
		        PropertyFilter filter = new PropertyFilter() {
		                public boolean apply(Object object, String fieldName, Object fieldValue) {
		                if(fieldValue instanceof List){
		                    List<Object> list = (List<Object>) fieldValue;
		                    if (list==null || list.size()==0) {
		                        return true;
		                    }
		                }
		                return null == fieldValue || "".equals(fieldValue);
		                }
		        };
		        jsonConfig.setJsonPropertyFilter(filter);
				JSONObject rejson = JSONObject.fromObject(billImageReqVo,jsonConfig);
				if(rejson!=null){
					requestContent=rejson.toString();//json格式
					logger.info("=====================请求报文："+requestContent);
				}
				
			}
			 //推送信息给vat,vat返回的数据
			 byte[] barrbys=postVAT(url,requestContent,"UTF-8");
			 if(barrbys!=null){
				 responContent=new String(barrbys,"UTF-8");
				 com.alibaba.fastjson.JSONObject jSONObject=JSON.parseObject(responContent);
				 if(jSONObject!=null && "0000".equals(jSONObject.get("resultCode")!=null?jSONObject.get("resultCode").toString():"")){
					 logVo.setStatus("1");
				 }else{
					 flag=0;
					 logVo.setStatus("0"); 
					 if(jSONObject!=null){
						 logVo.setErrorMessage(jSONObject.get("resultMsg")!=null?jSONObject.get("resultMsg").toString():"");
						 logVo.setErrorCode(jSONObject.get("resultCode")!=null?jSONObject.get("resultCode").toString():"");
					 }else{
						 flag=0;
						 logVo.setStatus("0");
						 logVo.setErrorMessage("json格式转换对象错误");
						 logVo.setErrorCode("9999");
					 }
					 
				 }
			 }else{
				 flag=0;
				 logVo.setStatus("0");
				 logVo.setErrorMessage("通信方法未返回结果,网络通信异常！");
			 }
        }catch(Exception e){
        	flag=0;
        	logVo.setStatus("0");
        	logVo.setErrorMessage("catch异常"+e.getMessage());
        	logger.error("发票图片信息推送vat接口错误信息：-------------->",e);
        }finally{
			try{
				if("1".equals(logVo.getStatus())){//当操作有成功记录时，保存数据表
					if(billImageViewVo!=null){
						if(billImageViewVo.getBillImageDtoList()!=null && billImageViewVo.getBillImageDtoList().size()>0){
							for(BillImageViewSonVo billVo:billImageViewVo.getBillImageDtoList()){
								if("1".equals(billVo.getFlag())){//公估或查勘
									PrplAcbillcontVo prplAcbillcontVo=new PrplAcbillcontVo();
									prplAcbillcontVo.setBillId(billVo.getIdEcdinvoiceocr());//发票Id
									prplAcbillcontVo.setBussType(billVo.getBussType());//业务类型
									prplAcbillcontVo.setFeeType(billVo.getChargeCode());//费用类型
									prplAcbillcontVo.setVidFlag("00");//验真标志
									prplAcbillcontVo.setCreatTime(new Date());
									prplAcbillcontVo.setUpdateTime(new Date());
									prplAcbillcontVo.setCreatUser(userVo.getUserCode());
									prplAcbillcontVo.setStatus("1");//开始默认有效状态
									prplAcbillcontVo.setTaskNo(billVo.getBussNo());//业务号
									prplAcbillcontVo.setBillUrl(billVo.getBillUrl());//发票图片地址
									prplAcbillcontVo.setUploadFlag("0");
									//保存数据表
									billlclaimService.saveOrUpdatePrplAcbillcont(prplAcbillcontVo);
								}else{//一般实赔与预付
									PrpLbillcontVo prpLbillcontVo=new PrpLbillcontVo();
									prpLbillcontVo.setBillId(billVo.getIdEcdinvoiceocr());//发票Id
									prpLbillcontVo.setBussType(billVo.getBussType());//业务类型
									prpLbillcontVo.setFeeType(billVo.getChargeCode());//费用类型
									prpLbillcontVo.setLocalFeeType(CodeConstants.backFeeType(billVo.getChargeCode()));//本系统的费用编码
									prpLbillcontVo.setPayId(Long.valueOf(billVo.getPaycustId()));//收款人Id
									prpLbillcontVo.setVidFlag("00");//验真标志
									prpLbillcontVo.setCreatTime(new Date());
									prpLbillcontVo.setCreatUser(userVo.getUserCode());
									prpLbillcontVo.setUpdateTime(new Date());
									prpLbillcontVo.setStatus("1");//开始默认有效状态
									prpLbillcontVo.setUploadFlag("0"); 
									prpLbillcontVo.setCompensateNo(billVo.getBussNo());//业务号
									prpLbillcontVo.setBillUrl(billVo.getBillUrl());//发票图片地址
									prpLbillcontVo.setLinktableId(billVo.getLinktableId());//关联表Id
									prpLbillcontVo.setLinktableName(billVo.getLinktableName());//关联表名称
									if(StringUtils.isNotBlank(billVo.getBussNo())){//业务号
										if(billVo.getBussNo().startsWith("D")){//为垫付时
											PrpLPadPayMainVo prpLPadPayMainVo=padPayService.findPadPayMainByCompNo(billVo.getBussNo());
											if(prpLPadPayMainVo!=null){
												prpLbillcontVo.setRegistNo(prpLPadPayMainVo.getRegistNo());
												prpLbillcontVo.setComCode(prpLPadPayMainVo.getComCode());
												prpLbillcontVo.setPolicyNo(prpLPadPayMainVo.getPolicyNo());
											}
										}else{
											PrpLCompensateVo prpLCompensateVo=compensateTaskService.findCompByPK(billVo.getBussNo());
											if(prpLCompensateVo!=null){
												prpLbillcontVo.setRegistNo(prpLCompensateVo.getRegistNo());
												prpLbillcontVo.setComCode(prpLCompensateVo.getComCode());
												prpLbillcontVo.setPolicyNo(prpLCompensateVo.getPolicyNo());
											}
										}
									}
									//保存数据表
									billlclaimService.saveOrUpdatePrplbillcont(prpLbillcontVo);
									
								}
								
								
							}
						}
						
					}
					
				}
			}catch(Exception e){
				flag=0;
				logVo.setStatus("0");
				logVo.setErrorMessage("数据保存或更新出错!"+e.getMessage());
				logger.error("发票图片信息推送vat接口数据保存错误：-------------->",e);
			}finally{
				logVo.setRegistNo("BillImage_"+userVo.getUserCode());
				logVo.setServiceType("billImage");//发票图片
				logVo.setRequestTime(new Date());
				logVo.setRequestXml(requestContent);
				logVo.setResponseXml(responContent);
				logVo.setBusinessType(BusinessType.BILL_Image.name());
				logVo.setBusinessName(BusinessType.BILL_Image.getName());
				logVo.setCreateTime(new Date());
				logVo.setCreateUser(userVo.getUserCode());
				logVo.setRequestUrl(url);
				claimInterfaceLogService.save(logVo);
			}
			
        }
        return flag;
	}
	
	 /**
     * 推送vat
     * @param url
     * @param content
     * @param charset
     * @return
     * @throws Exception
     */
    @SuppressWarnings("restriction")
	private byte[] postVAT(String url, String content, String charset) throws Exception{
    	URL console =new URL(url);
    	HttpURLConnection conn=(HttpURLConnection)console.openConnection();
        conn.setDoOutput(true);
        conn.setDoInput(true);
        //设置请求头
        conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Connection", "Keep-Alive");
        conn.setConnectTimeout(120*1000);//连接超时时间为120秒
        conn.setReadTimeout(90*1000);//读取超时时间为90
        conn.connect();
        DataOutputStream out = new DataOutputStream(conn.getOutputStream());
        out.write(content.getBytes(charset));
        // 刷新、关闭 
        out.flush();
        out.close();
        InputStream is = conn.getInputStream();
        if (is != null) {
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = is.read(buffer)) != -1) {
                   outStream.write(buffer, 0, len);
            }
            is.close();
            return outStream.toByteArray();
             }
       return null;
       }
    
    
    /**
     * 发票撤回申请
     */
	@Override
	public ResmessageVo sendUntyingInfoToVat(UntyingReqDtoVo untyingReqDtoVo,SysUserVo userVo) {
		String flag="0000";//返回结果标志 0000--表示成功，9999--表示失败
		//日志类
		ClaimInterfaceLogVo logVo = new ClaimInterfaceLogVo();
		//返回信息类
		ResmessageVo resmessageVo=new ResmessageVo();
		//请求url
		String url=SpringProperties.getProperty("vat_backReqUrl");
		String requestContent = "";//请求内容
		String responContent = "";//返回内容
		UntyingResDtoVo untyingResDtoVo=null;//返回数据接收类
		String billNoOpen="";//允许撤回的发票号码
		String billNoClose="";//不允许撤回的发票号码
        try{
			if(untyingReqDtoVo!=null){
				JsonConfig jsonConfig = new JsonConfig();
		        PropertyFilter filter = new PropertyFilter() {
		                public boolean apply(Object object, String fieldName, Object fieldValue) {
		                if(fieldValue instanceof List){
		                    List<Object> list = (List<Object>) fieldValue;
		                    if (list==null || list.size()==0) {
		                        return true;
		                    }
		                }
		                return null == fieldValue || "".equals(fieldValue);
		                }
		        };
		        jsonConfig.setJsonPropertyFilter(filter);
				JSONObject rejson = JSONObject.fromObject(untyingReqDtoVo,jsonConfig);
				if(rejson!=null){
					requestContent=rejson.toString();//json格式
					logger.info("=====================请求报文："+requestContent);
				}
				
			}
			 //推送信息给vat,vat返回的数据
			 byte[] barrbys=postVAT(url,requestContent,"UTF-8");
			 if(barrbys!=null){
				 responContent=new String(barrbys,"UTF-8");
				 JSONObject jSONObject=JSONObject.fromObject(responContent);
				 Map<String,Object> hashMap=new HashMap<String,Object>();
				 hashMap.put("untyingInvoiceDtoList",UntyingInvoiceResDtoVo.class);
				 //将返回信息转变成类对象
				 untyingResDtoVo=(UntyingResDtoVo)JSONObject.toBean(jSONObject,UntyingResDtoVo.class,hashMap);
				 if(untyingResDtoVo!=null){
					 if("0000".equals(untyingResDtoVo.getResultCode())){
						 logVo.setStatus("1");
						 logVo.setErrorMessage(untyingResDtoVo.getResultMsg());
					 }else{
						 flag="9999";
						 logVo.setStatus("0");
						 logVo.setErrorMessage(untyingResDtoVo.getResultMsg());
						 logVo.setErrorCode(untyingResDtoVo.getResultCode());
						 
					 }
				 }else{
					 flag="9999";
					 logVo.setStatus("0");
					 logVo.setErrorMessage("json格式转换对象错误");
					 logVo.setErrorCode("9999");
				 }
			 }else{
				 flag="9999";
				 logVo.setStatus("0");
				 logVo.setErrorMessage("通信方法未返回结果,网络通信异常！");
			 }
        }catch(Exception e){
        	flag="9999";
        	logVo.setStatus("0");
        	logVo.setErrorMessage("catch异常"+e.getMessage());
        	logger.error("发票撤回申请信息推送vat接口错误信息：-------------->",e);
        }finally{
			try{
				if("1".equals(logVo.getStatus())){//当操作有成功记录时，更新数据表
					if(untyingResDtoVo!=null){
						if(untyingResDtoVo.getUntyingInvoiceDtoList()!=null && untyingResDtoVo.getUntyingInvoiceDtoList().size()>0){
							int m=0;//用于拼接判断
							int n=0;//用于拼接判断
							for(UntyingInvoiceResDtoVo resDtoVo:untyingResDtoVo.getUntyingInvoiceDtoList()){
								PrpLbillinfoVo prpLbillinfoVo=billlclaimService.findPrpLbillinfoVoByVatId(resDtoVo.getInvoiceId());
								if("1".equals(resDtoVo.getOpenFlag())){
									if(m>0){
										if(prpLbillinfoVo!=null){
											billNoOpen=billNoOpen+","+prpLbillinfoVo.getBillNo();	
										}
										
									}else{
										if(prpLbillinfoVo!=null){
											billNoOpen=prpLbillinfoVo.getBillNo();
										}
									}
									prpLbillinfoVo.setSendstatus("0");;//改为未推送
									billlclaimService.saveOrUpdatePrpLbillinfoVo(prpLbillinfoVo);
									m++;
								}else{
									if(n>0){
										billNoClose=billNoClose+","+prpLbillinfoVo.getBillNo();
									}else{
										billNoClose=prpLbillinfoVo.getBillNo();
									}
									n++;
								}
								
								
							}
						}
						
					}
					
				}
			}catch(Exception e){
				flag="9999";
				logVo.setStatus("0");
				logVo.setErrorMessage("数据保存或更新出错!"+e.getMessage());
				logger.error("发票撤回申请信息推送vat接口数据保存错误：-------------->",e);
			}finally{
				resmessageVo.setCode(flag);
				resmessageVo.setOpenBillNos(billNoOpen);
				resmessageVo.setCloseBillNos(billNoClose);
				resmessageVo.setErrormessage(logVo.getErrorMessage());
				logVo.setRegistNo("batchRecall_"+userVo.getUserCode());
				logVo.setServiceType("batchRecall");//批次撤回
				logVo.setRequestTime(new Date());
				logVo.setRequestXml(requestContent);
				logVo.setResponseXml(responContent);
				logVo.setBusinessType(BusinessType.Batch_Recall.name());
				logVo.setBusinessName(BusinessType.Batch_Recall.getName());
				logVo.setCreateTime(new Date());
				logVo.setCreateUser(userVo.getUserCode());
				logVo.setRequestUrl(url);
				claimInterfaceLogService.save(logVo);
			}
			
        }
        return resmessageVo;
	}
	
    /**
     * 发票绑定接口
     */
	@Override
	public Map<String,String> sendBillRegisterToVat(BindInvoiceReqDto bindInvoiceReqDto,SysUserVo userVo) {
		Map<String,String> map=new HashMap<String,String>();
		int flag=1;//返回结果标志 1--表示成功，0--表示失败
		String returnMessage="成功";
		ClaimInterfaceLogVo logVo = new ClaimInterfaceLogVo();
		//请求url
		String url=SpringProperties.getProperty("vat_bindsUrl");
		String requestContent = "";//请求内容
		String responContent = "";//返回内容
        try{
			if(bindInvoiceReqDto!=null){
				JsonConfig jsonConfig = new JsonConfig();
		        PropertyFilter filter = new PropertyFilter() {
		                public boolean apply(Object object, String fieldName, Object fieldValue) {
		                if(fieldValue instanceof List){
		                    List<Object> list = (List<Object>) fieldValue;
		                    if (list==null || list.size()==0) {
		                        return true;
		                    }
		                }
		                return null == fieldValue || "".equals(fieldValue);
		                }
		        };
		        jsonConfig.setJsonPropertyFilter(filter);
				JSONObject rejson = JSONObject.fromObject(bindInvoiceReqDto,jsonConfig);
				if(rejson!=null){
					requestContent=rejson.toString();//json格式
					logger.info("=====================请求报文："+requestContent);
				}
				
			}
			 //推送信息给vat,vat返回的数据
			 byte[] barrbys=postVAT(url,requestContent,"UTF-8");
			 if(barrbys!=null){
				 responContent=new String(barrbys,"UTF-8");
				 com.alibaba.fastjson.JSONObject jSONObject=JSON.parseObject(responContent);
				 if(jSONObject!=null && "0000".equals(jSONObject.get("resultCode")!=null?jSONObject.get("resultCode").toString():"")){
					 logVo.setStatus("1");
				 }else{
					 flag=0;
					 logVo.setStatus("0"); 
					 if(jSONObject!=null){
						 logVo.setErrorMessage(jSONObject.get("resultMsg")!=null?jSONObject.get("resultMsg").toString():"");
						 logVo.setErrorCode(jSONObject.get("resultCode")!=null?jSONObject.get("resultCode").toString():"");
						 returnMessage=logVo.getErrorMessage();
					 }else{
						 flag=0;
						 logVo.setStatus("0");
						 logVo.setErrorMessage("json格式转换对象错误");
						 logVo.setErrorCode("9999");
						 returnMessage=logVo.getErrorMessage();
					 }
					 
				 }
			 }else{
				 flag=0;
				 logVo.setStatus("0");
				 logVo.setErrorMessage("通信方法未返回结果,网络通信异常！");
				 returnMessage=logVo.getErrorMessage();
			 }
        }catch(Exception e){
        	flag=0;
        	logVo.setStatus("0");
        	logVo.setErrorMessage("catch异常"+e.getMessage());
        	logger.error("发票绑定接口错误信息：-------------->",e);
        	returnMessage=logVo.getErrorMessage();
        }finally{
			try{
				if("1".equals(logVo.getStatus())){//当操作有成功记录时，保存数据表
					if(bindInvoiceReqDto!=null){
						if(bindInvoiceReqDto.getPrpJBatchDtoList()!=null && bindInvoiceReqDto.getPrpJBatchDtoList().size()>0){
							for(PrpJBatchDto billVo:bindInvoiceReqDto.getPrpJBatchDtoList()){
								PrpLbillinfoVo prpLbillinfoVo=billlclaimService.findPrpLbillinfoVoByVatId(billVo.getInvoiceId());
								if(prpLbillinfoVo!=null){
									prpLbillinfoVo.setSendstatus("1");//已推送状态
									prpLbillinfoVo.setSendvattime(new Date());//推送时间
									prpLbillinfoVo.setSendvatuser(userVo.getUserCode());//推送人
									prpLbillinfoVo.setBackFlag("0");//被打回标识
								}
								//保存数据表
								billlclaimService.saveOrUpdatePrpLbillinfoVo(prpLbillinfoVo);
								
								
							}
						}
						
					}
					
				}
			}catch(Exception e){
				flag=0;
				logVo.setStatus("0");
				logVo.setErrorMessage("数据保存或更新出错!"+e.getMessage());
				logger.error("发票绑定接口数据保存错误：-------------->",e);
				returnMessage=logVo.getErrorMessage();
			}finally{
				logVo.setRegistNo("Billreg_"+userVo.getUserCode());
				logVo.setServiceType("Billregister");//类型
				logVo.setRequestTime(new Date());
				logVo.setRequestXml(requestContent);
				logVo.setResponseXml(responContent);
				logVo.setBusinessType(BusinessType.BILL_Register.name());
				logVo.setBusinessName(BusinessType.BILL_Register.getName());
				logVo.setCreateTime(new Date());
				logVo.setCreateUser(userVo.getUserCode());
				logVo.setRequestUrl(url);
				claimInterfaceLogService.save(logVo);
				map.put("flag",flag+"");
				map.put("returnMessage", returnMessage);
			}
			
        }
        return map;
	}
	
}
