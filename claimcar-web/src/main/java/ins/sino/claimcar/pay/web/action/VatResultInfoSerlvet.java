package ins.sino.claimcar.pay.web.action;

import ins.framework.lang.Springs;
import ins.sino.claimcar.carinterface.service.ClaimInterfaceLogService;
import ins.sino.claimcar.carinterface.util.BusinessType;
import ins.sino.claimcar.carinterface.vo.ClaimInterfaceLogVo;
import ins.sino.claimcar.vat.service.BilllclaimService;
import ins.sino.claimcar.vat.vo.BillDtoVo;
import ins.sino.claimcar.vat.vo.PrpLbillcontVo;
import ins.sino.claimcar.vat.vo.PrpLbillinfoVo;
import ins.sino.claimcar.vat.vo.PrplAcbillcontVo;
import ins.sino.claimcar.vat.vo.ResVatResultVo;
import ins.sino.claimcar.vat.vo.VatResultReqVo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
/**
 * VAT发票识别结果通知理赔接口
 * @author Think
 *
 */
public class VatResultInfoSerlvet extends HttpServlet{
	private Logger logger = LoggerFactory.getLogger(VatResultInfoSerlvet.class);
    private static final long serialVersionUID = 1L;
    
    @Autowired
	private ClaimInterfaceLogService claimInterfaceLogService;
    @Autowired
    private BilllclaimService billlclaimService;

@Override
protected void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
	doPost(request,response);
}

@SuppressWarnings("resource")
@Override
protected void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
	init();//方法初始化
	//日志记录
	ClaimInterfaceLogVo logVo = new ClaimInterfaceLogVo();
	logVo.setStatus("1");//默认为成功
	 VatResultReqVo vatResultReqVo = new VatResultReqVo();
	 request.setCharacterEncoding("UTF-8");
	 response.setContentType("application/json;charset=UTF-8");
     PrintWriter out = response.getWriter();
     String jsonStr = "";
     String code="0000";//返回编码，默认为成功
     String message="成功";//返回信息
     try{
         InputStreamReader read = new InputStreamReader(request.getInputStream(),"UTF-8");
         BufferedReader bufferedReader = new BufferedReader(read);
         String temp = "";
         while(( temp = bufferedReader.readLine() )!=null){
             jsonStr += temp;
         }
         read.close();
         JSONObject rejson = JSONObject.fromObject(jsonStr);
         Map<String,Object> classMap = new HashMap<String,Object>();
         classMap.put("billDtoList",BillDtoVo.class);
         vatResultReqVo=(VatResultReqVo)JSONObject.toBean(rejson,VatResultReqVo.class,classMap);
         //检测请求报文是否正确
         checkNull(vatResultReqVo);
         
 	  }catch(Exception e){
 		  logVo.setStatus("0");
 		  logVo.setErrorCode("9999");
 		  logVo.setErrorMessage("错误信息:"+e.getMessage());
 		  code="9999";
 		  message="错误信息:"+e.getMessage();
     	  logger.info("VAT发票识别结果通知理赔接口---->：",e);
     }finally{
    	 try{
			 if("0000".equals(code)){
	    		 if(vatResultReqVo!=null && vatResultReqVo.getBillDtoList()!=null && vatResultReqVo.getBillDtoList().size()>0){
	    			 for(BillDtoVo billVo:vatResultReqVo.getBillDtoList()){
	    				 if(billVo.getIdEcdinvoiceocr().startsWith("B")){//一般实赔与预赔
	    					 PrpLbillcontVo  prpLbillcontVo= billlclaimService.findPrpLbillcontVoByBillId(billVo.getIdEcdinvoiceocr());
		    				 if(prpLbillcontVo==null){
		    					 throw new IllegalArgumentException("发票Id="+billVo.getIdEcdinvoiceocr()+"在理赔系统不存在，请核实！");
		    				 }else{
		    					 prpLbillcontVo.setBillNo(billVo.getInvoiceNo());//发票号码
		    					 prpLbillcontVo.setBillCode(billVo.getInvoiceCode());//发票代码
		    					 prpLbillcontVo.setSaleNo(billVo.getSellerIdentifiNumber());//销方纳税人识别号
		    					 prpLbillcontVo.setSaleName(billVo.getSellerName());//销方名称
		    					 prpLbillcontVo.setUpdateTime(new Date());//更新时间
		    					 prpLbillcontVo.setVidFlag(billVo.getVerifyFlag());//验真结果
		    					 prpLbillcontVo.setBillsortType(billVo.getBillsortName());//发票联与抵扣联标识
		    					 if("1".equals(billVo.getBillsortName())){
		    						 prpLbillcontVo.setStatus("0");//当是发票联的时候,数据置为无效
		    					 }
		    					 billlclaimService.saveOrUpdatePrplbillcont(prpLbillcontVo);
		    					 if("1".equals(billVo.getVerifyFlag())){//验真通过，把发票信息保存到发票表中
		    						 PrpLbillinfoVo prpLbillinfoVo=billlclaimService.findPrpLbillinfoVoByParams(billVo.getInvoiceNo(), billVo.getInvoiceCode());
			    					 if(prpLbillinfoVo==null){
			    						 PrpLbillinfoVo billinfoVo=new PrpLbillinfoVo();
			    						 billinfoVo.setBillNo(billVo.getInvoiceNo());//发票号码
			    						 billinfoVo.setBillCode(billVo.getInvoiceCode());//发票代码
			    						 billinfoVo.setBillDate(DateBillString(billVo.getBillingDate()));//开票时间
			    						 billinfoVo.setCreatTime(new Date());//创建时间
			    						 billinfoVo.setBillNum(NullToZero(billVo.getSumFee()));//价税合计
			    						 billinfoVo.setBillSnum(NullToZero(billVo.getSumTaxFee()));//发票税额
			    						 billinfoVo.setBillSl(NullToZero(billVo.getTaxRate()));//税率
			    						 billinfoVo.setBillNnum(NullToZero(billVo.getSumall()));//不含税金额
			    						 billinfoVo.setSaleName(billVo.getSellerName());//销方名称
			    						 billinfoVo.setSaleNo(billVo.getSellerIdentifiNumber());//销方纳税人识别号
			    						 billinfoVo.setFlag(billVo.getFlag());//是否为增值税专票
			    						 billinfoVo.setVatbillId(billVo.getInvoiceId());//vat发票id
			    						 billinfoVo.setSendstatus("0");//开始默认未推送
			    						 billinfoVo.setRegisterStatus("0");//开始默认未绑定
			    						 //保存发票信息
			    						 billlclaimService.saveOrUpdatePrpLbillinfoVo(billinfoVo);
			    						 }
		    					 }
		    					 
		    				 }
	    				 }else{//公估与查勘
	    					 PrplAcbillcontVo  prplAcbillcontVo= billlclaimService.findPrplAcbillcontVoByBillId(billVo.getIdEcdinvoiceocr());
		    				 if(prplAcbillcontVo==null){
		    					 throw new IllegalArgumentException("发票(公估或查勘)Id="+billVo.getIdEcdinvoiceocr()+"在理赔系统不存在，请核实！");
		    				 }else{
		    					 prplAcbillcontVo.setBillNo(billVo.getInvoiceNo());//发票号码
		    					 prplAcbillcontVo.setBillCode(billVo.getInvoiceCode());//发票代码
		    					 prplAcbillcontVo.setUpdateTime(new Date());//更新时间
		    					 prplAcbillcontVo.setVidFlag(billVo.getVerifyFlag());//验真结果
		    					 prplAcbillcontVo.setBillsortType(billVo.getBillsortName());//发票联与抵扣联标识
		    					 if("1".equals(billVo.getBillsortName())){
		    						 prplAcbillcontVo.setStatus("0");//当是发票联的时候,数据置为无效
		    					 }
		    					 billlclaimService.saveOrUpdatePrplAcbillcont(prplAcbillcontVo);
		    					 PrpLbillinfoVo prpLbillinfoVo=billlclaimService.findPrpLbillinfoVoByParams(billVo.getInvoiceNo(), billVo.getInvoiceCode());
		    					 if(prpLbillinfoVo==null){
		    						 PrpLbillinfoVo billinfoVo=new PrpLbillinfoVo();
		    						 billinfoVo.setBillNo(billVo.getInvoiceNo());//发票号码
		    						 billinfoVo.setBillCode(billVo.getInvoiceCode());//发票代码
		    						 billinfoVo.setBillDate(DateBillString(billVo.getBillingDate()));//开票时间
		    						 billinfoVo.setCreatTime(new Date());//创建时间
		    						 billinfoVo.setBillNum(NullToZero(billVo.getSumFee()));//价税合计
		    						 billinfoVo.setBillSnum(NullToZero(billVo.getSumTaxFee()));//发票税额
		    						 billinfoVo.setBillSl(NullToZero(billVo.getTaxRate()));//税率
		    						 billinfoVo.setBillNnum(NullToZero(billVo.getSumall()));//不含税金额
		    						 billinfoVo.setSaleName(billVo.getSellerName());//销方名称
		    						 billinfoVo.setSaleNo(billVo.getSellerIdentifiNumber());//销方纳税人识别号
		    						 billinfoVo.setFlag(billVo.getFlag());//是否为增值税专票
		    						 billinfoVo.setVatbillId(billVo.getInvoiceId());//vat发票id
		    						 billinfoVo.setSendstatus("0");//开始默认未推送
		    						 billinfoVo.setRegisterStatus("0");//开始默认未绑定
		    						 //保存发票信息
		    						 billlclaimService.saveOrUpdatePrpLbillinfoVo(billinfoVo);
		    						 }
		    				 }
	    				 }
	    				 
	    					
	    				 }
	    		 }
	    	 }
        	 
    	 }catch(Exception e){
    		  logVo.setStatus("0");
    		  logVo.setErrorCode("9999");
    		  logVo.setErrorMessage("保存错误信息:"+e.getMessage());
    		  code="9999";
    		  message="错误信息:"+e.getMessage();
    		  logger.info("VAT发票识别结果通知理赔接口保存错误信息---->：",e);
    	 }finally{
    		 if("0000".equals(code)){
    			 out.print(JSONObject.fromObject(ResVatResultVo.success("0000","Success")));
    		 }else{
    			 out.print(JSONObject.fromObject(ResVatResultVo.success("9999","错误信息:"+message)));
    		 }
    		 out.flush();
    	     out.close();
    		 logVo.setRegistNo("BiLLResult_0000000000");//标志
    		 logVo.setServiceType("BiLLResult");
    		 logVo.setRequestTime(new Date());
    		 logVo.setRequestXml(jsonStr);
    		 logVo.setBusinessType(BusinessType.BILL_Result.name());
    		 logVo.setBusinessName(BusinessType.BILL_Result.getName());
    		 logVo.setCreateTime(new Date());
    		 logVo.setCreateUser("0000000000");
    		 logVo.setRequestUrl("接口类:VatResultInfoSerlvet");
    		 claimInterfaceLogService.save(logVo);
    	 }
    	 
    }
    
}
/**
 * 接口接受数据前校验
 * @param vatResultReqVo
 */
private void checkNull(VatResultReqVo vatResultReqVo)throws Exception{
	if(vatResultReqVo==null){
		throw new IllegalArgumentException("请求内容不能为空！");
	}else{
		if(!"102001".equals(vatResultReqVo.getSystemType())){
			throw new IllegalArgumentException("系统编码systemType错误！");
		}
		if(vatResultReqVo.getBillDtoList()!=null && vatResultReqVo.getBillDtoList().size()>0){
			for(BillDtoVo billVo:vatResultReqVo.getBillDtoList()){
				if(StringUtils.isBlank(billVo.getIdEcdinvoiceocr())){
					throw new IllegalArgumentException("文件id不允许为空！");
				}
				if(StringUtils.isBlank(billVo.getBussNo())){
					throw new IllegalArgumentException("发票id="+billVo.getIdEcdinvoiceocr()+"业务号不允许为空！");
				}
				if(StringUtils.isBlank(billVo.getVerifyFlag())){
					throw new IllegalArgumentException("发票id="+billVo.getIdEcdinvoiceocr()+"验真通过标识不允许为空！");
				}
				if("1".equals(billVo.getVerifyFlag())){
					if(StringUtils.isBlank(billVo.getFlag())){
						throw new IllegalArgumentException("当发票id="+billVo.getIdEcdinvoiceocr()+"验真结果为真时，是否增值税专票不能为空！");
					}
					if(StringUtils.isBlank(billVo.getInvoiceNo())){
						throw new IllegalArgumentException("当发票id="+billVo.getIdEcdinvoiceocr()+"验真结果为真时，发票号码不能为空！");
					}
					if(StringUtils.isBlank(billVo.getInvoiceCode())){
						throw new IllegalArgumentException("当发票id="+billVo.getIdEcdinvoiceocr()+"验真结果为真时，发票编码不能为空！");
					}
					if(StringUtils.isBlank(billVo.getInvoiceCode())){
						throw new IllegalArgumentException("当发票id="+billVo.getIdEcdinvoiceocr()+"验真结果为真时，发票编码不能为空！");
					}
					if(StringUtils.isBlank(billVo.getSellerName()) && "1".equals(billVo.getFlag())){
						throw new IllegalArgumentException("当发票id="+billVo.getIdEcdinvoiceocr()+"验真结果为真时，销方名称不能为空！");
					}
					if(StringUtils.isBlank(billVo.getSellerIdentifiNumber()) && "1".equals(billVo.getFlag())){
						throw new IllegalArgumentException("当发票id="+billVo.getIdEcdinvoiceocr()+"验真结果为真时，销方纳税人识别号不能为空！");
					}
					if(StringUtils.isBlank(billVo.getSumall())){
						throw new IllegalArgumentException("当发票id="+billVo.getIdEcdinvoiceocr()+"验真结果为真时，不含税金额不能为空！");
					}
					if(StringUtils.isBlank(billVo.getSumTaxFee())){
						throw new IllegalArgumentException("当发票id="+billVo.getIdEcdinvoiceocr()+"验真结果为真时，税额不能为空！");
					}
					if(StringUtils.isBlank(billVo.getSumFee())){
						throw new IllegalArgumentException("当发票id="+billVo.getIdEcdinvoiceocr()+"验真结果为真时，价税合计金额不能为空！");
					}
					if(StringUtils.isBlank(billVo.getTaxRate())){
						throw new IllegalArgumentException("当发票id="+billVo.getIdEcdinvoiceocr()+"验真结果为真时，税率不能为空！");
					}
					if(StringUtils.isBlank(billVo.getBillingDate())){
						throw new IllegalArgumentException("当发票id="+billVo.getIdEcdinvoiceocr()+"验真结果为真时，开票日期不能为空！");
					}
					if(StringUtils.isBlank(billVo.getInvoiceId())){
						throw new IllegalArgumentException("当发票id="+billVo.getIdEcdinvoiceocr()+"验真结果为真时，vat发票Id不能为空！");
					}
				}
			}
		}else{
			throw new IllegalArgumentException("发票信息集合billDtoList不能为空！");
		}
	}
}


public void init() throws ServletException {
	if(claimInterfaceLogService==null){
		claimInterfaceLogService=(ClaimInterfaceLogService)Springs.getBean(ClaimInterfaceLogService.class);
	}
	if(billlclaimService==null){
		billlclaimService=(BilllclaimService)Springs.getBean(BilllclaimService.class);
	}
}

/**
 * 将String 类型转为BigDecimal类型
 * @param strNum
 * @return
 */
private static BigDecimal NullToZero(String strNum) {
	if(strNum==null||strNum.equals("")){
		strNum = "0";
	}
	return new BigDecimal(strNum);
}
    /**
	 * 时间转换方法
	 * Date 类型转换 String类型
	 * @param strDate
	 * @return
	 * @throws ParseException2020-05-27 14:45:02
	 */
	private static Date DateString(String strDate){
		Date date=null;
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		  if(strDate!=null){
			  try {
				date=format.parse(strDate);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return date;
	}
	/**
	 * 时间转换方法
	 * Date 类型转换 String类型
	 * @param strDate
	 * @return
	 * @throws ParseException2020-05-27 14:45:02
	 */
	private static Date DateBillString(String strDate){
		Date date=null;
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
		  if(strDate!=null){
			  try {
				date=format.parse(strDate);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return date;
	}
}
