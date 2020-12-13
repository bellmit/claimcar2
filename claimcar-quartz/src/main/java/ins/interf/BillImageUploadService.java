package ins.interf;

import ins.framework.dao.database.DatabaseDao;
import ins.framework.lang.Springs;
import ins.platform.common.service.facade.BaseDaoService;
import ins.platform.utils.SqlJoinUtils;
import ins.sino.claimcar.carYxImage.service.CarXyImageService;
import ins.sino.claimcar.carinterface.service.ClaimInterfaceLogService;
import ins.sino.claimcar.carinterface.util.BusinessType;
import ins.sino.claimcar.carinterface.vo.ClaimInterfaceLogVo;
import ins.sino.claimcar.other.vo.PrpLAssessorFeeVo;
import ins.sino.claimcar.other.vo.PrpLCheckFeeVo;
import ins.sino.claimcar.vat.service.BilllclaimService;
import ins.sino.claimcar.vat.vo.PrpLbillcontVo;
import ins.sino.claimcar.vat.vo.PrplAcbillcontVo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.jws.WebService;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

@WebService(targetNamespace="http://interf/billImageUploadService/",serviceName="billImageUploadService",endpointInterface = "ins.interf.BillImageUploadService")
public class BillImageUploadService extends SpringBeanAutowiringSupport{
	private Logger logger = LoggerFactory.getLogger(BillImageUploadService.class);
	@Autowired
	BaseDaoService baseDaoService;
	@Autowired
	DatabaseDao databaseDao;
	@Autowired
	BilllclaimService billlclaimService;
	@Autowired
	CarXyImageService carXyImageService;
	@Autowired
	private ClaimInterfaceLogService claimInterfaceLogService;
	
    public void dojob(){
    	init();
    	Map<String,String> resultMap=new HashMap<String,String>();
    	//日志记录
    	ClaimInterfaceLogVo logVo = new ClaimInterfaceLogVo();
    	try{ 
    		// 当前时间
            Calendar endcalendar = Calendar.getInstance();
            endcalendar.set(Calendar.DAY_OF_YEAR, endcalendar.get(Calendar.DAY_OF_YEAR));
            // 前一天
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR)-1);
            //
            resultMap=getBillImage(calendar.getTime(),endcalendar.getTime());
            if(resultMap!=null){
  			  for(Map.Entry<String,String> entry:resultMap.entrySet()){
  				  if("1".equals(entry.getKey())){
  					 logVo.setStatus("1");
  				  }else{
  					logVo.setStatus("0");
  					logVo.setErrorMessage("错误信息:"+entry.getValue());
  				  }
  			  }
  		  }
    	}catch(Exception e){
    		  logVo.setStatus("0");
    		  logVo.setErrorCode("9999");
    		  logVo.setErrorMessage("错误信息:"+e.getMessage());
    		  if(resultMap!=null){
    			  for(Map.Entry<String,String> entry:resultMap.entrySet()){
    				  logVo.setErrorMessage("错误信息:"+entry.getValue());
    			  }
    		  }
    	}finally{
		    	 logVo.setRegistNo("BiLLImageCount_0000000000");//标志
		   		 logVo.setServiceType("BiLLImageCount");
		   		 logVo.setRequestTime(new Date());
		   		 logVo.setRequestXml("发票归集");
		   		 logVo.setBusinessType(BusinessType.BILL_ImageCount.name());
		   		 logVo.setBusinessName(BusinessType.BILL_ImageCount.getName());
		   		 logVo.setCreateTime(new Date());
		   		 logVo.setCreateUser("0000000000");
		   		 logVo.setRequestUrl("接口类:BillImageUploadService");
		   		 claimInterfaceLogService.save(logVo);
    	}
    	
        
    }
	
	
	
	/**
	 * 初始化
	 */
	private void init() {
		if(baseDaoService==null){
			baseDaoService=(BaseDaoService)Springs.getBean(BaseDaoService.class);
		}
		if(databaseDao==null){
			databaseDao=(DatabaseDao)Springs.getBean(DatabaseDao.class);
		}
		if(billlclaimService==null){
			billlclaimService=(BilllclaimService)Springs.getBean(BilllclaimService.class);
		}
		if(carXyImageService==null){
			carXyImageService=(CarXyImageService)Springs.getBean(CarXyImageService.class);
		}
		if(claimInterfaceLogService==null){
			claimInterfaceLogService=(ClaimInterfaceLogService)Springs.getBean(ClaimInterfaceLogService.class);
		}
		
	}
	
	/**
	 * 获取需要上传的图片信息
	 * @param startDate
	 * @param endDate
	 * @param flagType--0正常推送，1-补送
	 */
	private Map<String,String> getBillImage(Date startDate, Date endDate) {
		Map<String,String> resultmap=new HashMap<String,String>();
		String succeeFlag="1";//本次推送是否成功标志
		String message="操作成功";
		SqlJoinUtils sqlUtils1=new SqlJoinUtils();
		//key-报案号，value-图片地址
		Map<String,List<String>> urlMap=new HashMap<String,List<String>>();
		//报案号与发票号码与发票代码
		Set<String> billSet=new HashSet<String>();
		//PrpLbillcont的id
		Set<Long> contIdSet=new HashSet<Long>();
		//PrplAcbillcont的id
		Set<Long> asscheIdSet=new HashSet<Long>();
		sqlUtils1.append(" select a.registNo,a.billNo,a.billCode,a.billUrl,a.id,a.billsortType from PrpLbillcont a where a.uploadFlag !='1' and a.vidFlag='1' "
				+ " and not exists(select 1 from PrpLbillcont b where  b.registNo=a.registNo and b.billNo=a.billNo "
				+ " and b.billCode=a.billCode and b.uploadFlag ='1' and b.vidFlag='1' and b.billsortType=a.billsortType ) "
				+ " and exists(select 1 from PrpLbillinfo c where c.billNo=a.billNo and c.billCode=a.billCode and c.flag='1' )");
	        	sqlUtils1.append(" and a.updateTime>=? and a.updateTime<=? ");
	        	sqlUtils1.addParamValue(startDate);
	    		sqlUtils1.addParamValue(endDate);
		
		String sql1=sqlUtils1.getSql();
		Object[] params1=sqlUtils1.getParamValues();
		List<Object[]> objList1=baseDaoService.getAllBySql(sql1, params1);
		if(objList1!=null && objList1.size()>0){
			for(int i=0;i<objList1.size();i++){
				Object[] objs=objList1.get(i);
				if(billSet.add(objs[0]!=null?objs[0].toString():""+objs[1]!=null?objs[1].toString():""+objs[2]!=null?objs[2].toString():""+objs[5]!=null?objs[5].toString():"")){
					List<String> urlList=urlMap.get(objs[0].toString());
					contIdSet.add(Long.valueOf(objs[4].toString()));
					if(urlList!=null && urlList.size()>0){
						urlList.add(objs[3].toString());
					}else{
						List<String> urlnewList=new ArrayList<String>();
						urlnewList.add(objs[3].toString());
						urlMap.put(objs[0].toString(),urlnewList);
					}
					
				}
			}
		}
		SqlJoinUtils sqlUtils2=new SqlJoinUtils();
		//发票号码与发票代码
		Set<String> assbillSet=new HashSet<String>();
		//发票号码与发票代码
		Set<String> chebillSet=new HashSet<String>();
		sqlUtils2.append(" select a.billId,a.billNo,a.billCode,a.billUrl,a.id,a.billsortType from PrplAcbillcont a where a.uploadFlag !='1' and a.vidFlag='1' "
				+ " and not exists(select 1 from PrplAcbillcont b where b.billNo=a.billNo "
				+ " and b.billCode=a.billCode and b.uploadFlag ='1' and b.vidFlag='1' and b.billsortType=a.billsortType )"
				+ " and exists(select 1 from PrpLbillinfo c where c.billNo=a.billNo and c.billCode=a.billCode and c.flag='1' ) ");
					sqlUtils2.append(" and a.updateTime>=? and a.updateTime<=? ");
					sqlUtils2.addParamValue(startDate);
					sqlUtils2.addParamValue(endDate);
		String sql2=sqlUtils2.getSql();
		Object[] params2=sqlUtils2.getParamValues();
		List<Object[]> objList2=baseDaoService.getAllBySql(sql2, params2);
		if(objList2!=null && objList2.size()>0){
			for(int i=0;i<objList2.size();i++){
				Object[] objs=objList2.get(i);
				if(objs[0].toString().startsWith("F")){//公估费
					if(assbillSet.add(objs[1]!=null?objs[1].toString():""+objs[2]!=null?objs[2].toString():""+objs[5]!=null?objs[5].toString():"")){
						asscheIdSet.add(Long.valueOf(objs[4].toString()));
						List<PrpLAssessorFeeVo> assFeeVoList=billlclaimService.findPrpLAssessorFeeBylinkBillNo(objs[1].toString()+"_"+objs[2].toString());
						if(assFeeVoList!=null && assFeeVoList.size()>0){
							//报案号
							Set<String> registNoSet=new HashSet<String>();
							for(PrpLAssessorFeeVo feeVo:assFeeVoList){
								if(registNoSet.add(feeVo.getRegistNo())){
									List<String> urlList=urlMap.get(feeVo.getRegistNo());
									                     
									if(urlList!=null && urlList.size()>0){
										urlList.add(objs[3].toString());
									}else{
										List<String> urlnewList=new ArrayList<String>();
										urlnewList.add(objs[3].toString());
										urlMap.put(feeVo.getRegistNo(),urlnewList);
									}
								}
							}
						}
						
						
					}
				}else{//查勘费
					if(chebillSet.add(objs[1]!=null?objs[1].toString():""+objs[2]!=null?objs[2].toString():""+objs[5]!=null?objs[5].toString():"")){
						asscheIdSet.add(Long.valueOf(objs[4].toString()));
						List<PrpLCheckFeeVo> cheFeeVoList=billlclaimService.findPrpLCheckFeeBylinkBillNo(objs[1].toString()+"_"+objs[2].toString());
						if(cheFeeVoList!=null && cheFeeVoList.size()>0){
							//报案号
							Set<String> registNoSet=new HashSet<String>();
							for(PrpLCheckFeeVo feeVo:cheFeeVoList){
								if(registNoSet.add(feeVo.getRegistNo())){
									List<String> urlList=urlMap.get(feeVo.getRegistNo());
									if(urlList!=null && urlList.size()>0){
										urlList.add(objs[3].toString());
									}else{
										List<String> urlnewList=new ArrayList<String>();
										urlnewList.add(objs[3].toString());
										urlMap.put(feeVo.getRegistNo(),urlnewList);
									}
								}
							}
						}
						
						
					}
				}
				
			}
		}
		if(urlMap!=null && urlMap.size()>0){
        	for(Map.Entry<String,List<String>> entry:urlMap.entrySet()){
        		String retuXml=carXyImageService.upBillImageInfoToXyd(entry.getKey(),entry.getValue(),"bill","");
        		//成功
            	if(StringUtils.isNotBlank(retuXml) && retuXml.contains("<RESPONSE_CODE>200</RESPONSE_CODE>")){
            		
            	}else{
            		succeeFlag="0";
            		message=retuXml;
            		break;
            	}
        	}
        }
		
		if("0".equals(succeeFlag)){
			if(contIdSet!=null && contIdSet.size()>0){
			 for(Long id:contIdSet){
				 PrpLbillcontVo prpLbillcontVo=billlclaimService.findPrpLbillcontById(id);
					if(prpLbillcontVo!=null){
						Calendar endcalendar = Calendar.getInstance();
						//如果错误，下次推送的时候，就一起查出来，方便再上传发票
				        endcalendar.set(Calendar.HOUR_OF_DAY, endcalendar.get(Calendar.HOUR_OF_DAY)+3);
						prpLbillcontVo.setUpdateTime(endcalendar.getTime());
		    			billlclaimService.saveOrUpdatePrplbillcont(prpLbillcontVo);
					}
			 }
			}
			if(asscheIdSet!=null && asscheIdSet.size()>0){
				for(Long id:asscheIdSet){
					PrplAcbillcontVo prplAcbillcontVo=billlclaimService.findPrplAcbillcontById(id);
					if(prplAcbillcontVo!=null){
						Calendar endcalendar = Calendar.getInstance();
				        endcalendar.set(Calendar.HOUR_OF_DAY, endcalendar.get(Calendar.HOUR_OF_DAY)+3);
				        prplAcbillcontVo.setUpdateTime(endcalendar.getTime());
		    			billlclaimService.saveOrUpdatePrplAcbillcont(prplAcbillcontVo);
					}
				}
			}
			resultmap.put(succeeFlag, message);
		}else{
			if(contIdSet!=null && contIdSet.size()>0){
				 for(Long id:contIdSet){
					 PrpLbillcontVo prpLbillcontVo=billlclaimService.findPrpLbillcontById(id);
						if(prpLbillcontVo!=null){
							prpLbillcontVo.setUploadFlag("1");
			    			billlclaimService.saveOrUpdatePrplbillcont(prpLbillcontVo);
						}
				 }
				}
				if(asscheIdSet!=null && asscheIdSet.size()>0){
					for(Long id:asscheIdSet){
						PrplAcbillcontVo prplAcbillcontVo=billlclaimService.findPrplAcbillcontById(id);
						if(prplAcbillcontVo!=null){
							prplAcbillcontVo.setUploadFlag("1");
			    			billlclaimService.saveOrUpdatePrplAcbillcont(prplAcbillcontVo);
						}
					}
				}
				resultmap.put(succeeFlag, message);
		}
		return resultmap;
	}
}
