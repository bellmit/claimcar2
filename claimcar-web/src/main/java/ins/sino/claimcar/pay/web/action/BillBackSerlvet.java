package ins.sino.claimcar.pay.web.action;

import ins.framework.lang.Springs;
import ins.sino.claimcar.carinterface.service.ClaimInterfaceLogService;
import ins.sino.claimcar.carinterface.util.BusinessType;
import ins.sino.claimcar.carinterface.vo.ClaimInterfaceLogVo;
import ins.sino.claimcar.vat.service.BilllclaimService;
import ins.sino.claimcar.vat.vo.BillbackDtoVo;
import ins.sino.claimcar.vat.vo.PrpLbillinfoVo;
import ins.sino.claimcar.vat.vo.PrplAcbillcontVo;
import ins.sino.claimcar.vat.vo.RejectInvoiceReqDtoVo;
import ins.sino.claimcar.vat.vo.ResVatResultVo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
 * 发票打回接口
 * @author Think
 *
 */
public class BillBackSerlvet extends HttpServlet{

	private static final long serialVersionUID = 1L;
	private Logger logger = LoggerFactory.getLogger(BillBackSerlvet.class);
	@Autowired
	private ClaimInterfaceLogService claimInterfaceLogService;
    @Autowired
    private BilllclaimService billlclaimService;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		doPost(request,response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		 init();//方法初始化
		 //日志记录
		 ClaimInterfaceLogVo logVo = new ClaimInterfaceLogVo();
		 logVo.setStatus("1");//默认为成功
		 RejectInvoiceReqDtoVo rejectInvoiceReqDtoVo = new RejectInvoiceReqDtoVo();
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
	         classMap.put("billbacksDtoList",BillbackDtoVo.class);
	         rejectInvoiceReqDtoVo=(RejectInvoiceReqDtoVo)JSONObject.toBean(rejson,RejectInvoiceReqDtoVo.class,classMap);
	         //检测请求报文是否正确
	         checkNull(rejectInvoiceReqDtoVo);
	         
	 	  }catch(Exception e){
	 		  logVo.setStatus("0");
	 		  logVo.setErrorCode("9999");
	 		  logVo.setErrorMessage("错误信息:"+e.getMessage());
	 		  code="9999";
	 		  message="错误信息:"+e.getMessage();
	     	  logger.info("VAT发票打回接口---->：",e);
	     }finally{
	    	 try{
				 if("0000".equals(code)){
		    		 if(rejectInvoiceReqDtoVo!=null && rejectInvoiceReqDtoVo.getBillbacksDtoList()!=null && rejectInvoiceReqDtoVo.getBillbacksDtoList().size()>0){
		    			 for(BillbackDtoVo billVo:rejectInvoiceReqDtoVo.getBillbacksDtoList()){
		    				 PrpLbillinfoVo  prpLbillinfoVo= billlclaimService.findPrpLbillinfoVoByVatId(billVo.getInvoiceId());
		    				 if(prpLbillinfoVo==null){
		    					 throw new IllegalArgumentException("发票Id="+billVo.getInvoiceId()+"在理赔系统不存在，请核实！");
		    				 }else{
		    					 List<PrplAcbillcontVo> prplAcbillcontVoList=billlclaimService.findPrplAcbillcontByBillNoAndBillCode(prpLbillinfoVo.getBillNo(), prpLbillinfoVo.getBillCode(),null);
		    					 if(prplAcbillcontVoList!=null && prplAcbillcontVoList.size()>0){
		    						 prpLbillinfoVo.setBackFlag("1");//被打回
			    					 prpLbillinfoVo.setSendstatus("0");//未推送
			    					 prpLbillinfoVo.setRegisterStatus("0");//未登记
			    					 prpLbillinfoVo.setBackCauseinfo(billVo.getCauseMessage());
			    					 billlclaimService.saveOrUpdatePrpLbillinfoVo(prpLbillinfoVo);
			    					 if(prplAcbillcontVoList.get(0).getBillId().startsWith("F")){
			    						 billlclaimService.updatePrpLAssessorFee(prpLbillinfoVo.getBillNo(), prpLbillinfoVo.getBillCode());
			    					 }else if(prplAcbillcontVoList.get(0).getBillId().startsWith("C")){
			    						 billlclaimService.updatePrpLCheckFee(prpLbillinfoVo.getBillNo(), prpLbillinfoVo.getBillCode());
			    					 }
			    					 
		    					 }else{
		    						 prpLbillinfoVo.setBackFlag("1");//被打回
			    					 prpLbillinfoVo.setSendstatus("0");//未推送
			    					 prpLbillinfoVo.setBackCauseinfo(billVo.getCauseMessage());
			    					 billlclaimService.saveOrUpdatePrpLbillinfoVo(prpLbillinfoVo);
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
	    		  logger.info("VAT发票打回接口保存错误信息---->：",e);
	    	 }finally{
	    		 if("0000".equals(code)){
	    			 out.print(JSONObject.fromObject(ResVatResultVo.success("0000","Success")));
	    		 }else{
	    			 out.print(JSONObject.fromObject(ResVatResultVo.success("9999","错误信息:"+message)));
	    		 }
	    		 out.flush();
	    	     out.close();
	    		 logVo.setRegistNo("BiLLBack_0000000000");//标志
	    		 logVo.setServiceType("BiLLBack");
	    		 logVo.setRequestTime(new Date());
	    		 logVo.setRequestXml(jsonStr);
	    		 logVo.setBusinessType(BusinessType.BILL_BACK.name());
	    		 logVo.setBusinessName(BusinessType.BILL_BACK.getName());
	    		 logVo.setCreateTime(new Date());
	    		 logVo.setCreateUser("0000000000");
	    		 logVo.setRequestUrl("接口类:BillBackSerlvet");
	    		 claimInterfaceLogService.save(logVo);
	    	 }
	    	 
	    }
	    
	}
	
	/**
	 * 接口接受数据前校验
	 * @param rejectInvoiceReqDtoVo
	 */
	private void checkNull(RejectInvoiceReqDtoVo rejectInvoiceReqDtoVo)throws Exception{
		if(rejectInvoiceReqDtoVo==null){
			throw new IllegalArgumentException("请求内容不能为空！");
		}else{
			if(!"102001".equals(rejectInvoiceReqDtoVo.getSystemType())){
				throw new IllegalArgumentException("系统编码systemType错误！");
			}
			if(rejectInvoiceReqDtoVo.getBillbacksDtoList()!=null && rejectInvoiceReqDtoVo.getBillbacksDtoList().size()>0){
				for(BillbackDtoVo billVo:rejectInvoiceReqDtoVo.getBillbacksDtoList()){
					if(StringUtils.isBlank(billVo.getInvoiceId())){
						throw new IllegalArgumentException("发票id不允许为空！");
					}
					if(StringUtils.isBlank(billVo.getCauseMessage())){
						throw new IllegalArgumentException("发票id="+billVo.getInvoiceId()+"打回原因不允许为空！");
					}
				}
			}else{
				throw new IllegalArgumentException("发票信息集合billbacksDtoList不能为空！");
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
}
