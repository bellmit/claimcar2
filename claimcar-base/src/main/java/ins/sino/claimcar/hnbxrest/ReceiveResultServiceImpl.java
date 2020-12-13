package ins.sino.claimcar.hnbxrest;



import ins.framework.dao.database.DatabaseDao;
import ins.platform.utils.DataUtils;
import ins.platform.utils.DateUtils;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.carinterface.service.ClaimInterfaceLogService;
import ins.sino.claimcar.carinterface.util.BusinessType;
import ins.sino.claimcar.carinterface.vo.ClaimInterfaceLogVo;
import ins.sino.claimcar.check.service.CheckTaskService;
import ins.sino.claimcar.check.vo.PrpLCheckDutyVo;
import ins.sino.claimcar.claim.service.CompensateTaskService;
import ins.sino.claimcar.claim.vo.PrpLLossItemVo;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.hnbxrest.service.ReceiveResultService;
import ins.sino.claimcar.hnbxrest.service.SubmitcaseinforService;
import ins.sino.claimcar.hnbxrest.util.EncryptUtils;
import ins.sino.claimcar.hnbxrest.vo.CasecarcpsVo;
import ins.sino.claimcar.hnbxrest.vo.CasecarfeeVo;
import ins.sino.claimcar.hnbxrest.vo.PrplQuickCaseInforVo;
import ins.sino.claimcar.hnbxrest.vo.ReceiveauditingresultVo;
import ins.sino.claimcar.hnbxrest.vo.ReceivecpsresultVo;
import ins.sino.claimcar.hnbxrest.vo.ReceivegusunresultVo;
import ins.sino.claimcar.hnbxrest.vo.RespondMsg;
import ins.sino.claimcar.losscar.service.LossCarService;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarMainVo;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLRegistCarLossVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.SpringProperties;

import com.alibaba.dubbo.config.annotation.Service;
@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"},timeout = 1000000)
@Path("receiveResultService")
public class ReceiveResultServiceImpl implements ReceiveResultService{
    private static Logger logger = LoggerFactory.getLogger(ReceiveResultServiceImpl.class);
	@Autowired 
	private DatabaseDao databaseDao;
	@Autowired
    ClaimInterfaceLogService interfaceLogService;
	@Autowired
	private SubmitcaseinforService submitcaseinforService;
    @Autowired
	LossCarService lossCarService;
    @Autowired
    CompensateTaskService compensateTaskService;
    @Autowired
    RegistQueryService registQueryService;
    @Autowired
    PolicyViewService policyViewService;
    @Autowired
    CheckTaskService checkTaskService;
    
	@Override
    public RespondMsg receiveauditingresult(ReceiveauditingresultVo receiveauditingresultVo) {
    	
		ClaimInterfaceLogVo logVo = new ClaimInterfaceLogVo();
		String url = "";
		String requestContent = "";
		String responContent = "";
        try{
            JSONObject json = new JSONObject();
            String username = SpringProperties.getProperty("hnbxrest_user");
            String password = SpringProperties.getProperty("hnbxrest_password");
			// 使用代理地址
            url = SpringProperties.getProperty("hnbxrest_auditing_url");
            json.put("casenumber",receiveauditingresultVo.getCasenumber());
            json.put("inscaseno",receiveauditingresultVo.getInscaseno());
            json.put("datatype",receiveauditingresultVo.getDatatype());
            json.put("isqualify",receiveauditingresultVo.getIsqualify());
            json.put("imagetype",receiveauditingresultVo.getImagetype());
            json.put("isass",receiveauditingresultVo.getIsass());
            json.put("username",username);
            json.put("password",EncryptUtils.desEncrypt(password));
            requestContent = json.toString();
			logger.info("send======河南快赔定损照片审核结果请求报文："+requestContent);

            HttpPost httpPost = new HttpPost(url);
            if(requestContent!=null&& !requestContent.trim().equals("")){
                StringEntity requestEntity = new StringEntity(requestContent,ContentType.create("application/json","UTF-8"));
                httpPost.setEntity(requestEntity);
            }
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity httpEntity = response.getEntity();
            responContent = EntityUtils.toString(httpEntity);
            responContent = new String(responContent.getBytes("UTF-8"), "UTF-8");
			logger.info("return======河南快赔定损照片审核结果返回报文："+responContent);
            JSONObject rejson = JSONObject.fromObject(responContent);
            String restate = rejson.getString("restate");
            String redes = rejson.getString("redes");
            if("1".equals(restate)){
            	logVo.setStatus("1");
            }else{
            	logVo.setStatus("0");
            	logVo.setErrorCode(restate);
            }
            logVo.setErrorMessage(redes);
            
           
        }catch(Exception e){
            e.printStackTrace();
            logVo.setStatus("0");
            logVo.setErrorMessage(e.getMessage());
        }finally{
			// 存日志表
            logVo.setRequestUrl(url);
            logVo.setRequestXml(requestContent);
            logVo.setResponseXml(responContent);
            logVo.setRegistNo(receiveauditingresultVo.getInscaseno());
            logVo.setBusinessType(BusinessType.HNQC_PhotoVerify.name());
            logVo.setBusinessName(BusinessType.HNQC_PhotoVerify.getName());
            logVo.setOperateNode(FlowNode.VLCar.name());
            logVo.setRequestTime(new Date());
            logVo.setCreateTime(new Date());
            logVo.setCreateUser(receiveauditingresultVo.getOperateuser());
            interfaceLogService.save(logVo);
        }
        return RespondMsg.SUCCESS();
    }
	
	@Override
	public void receivecpsresult(String registNo,String status,SysUserVo userVo){
		long t1 = System.currentTimeMillis();
		ReceivecpsresultVo rReceivecpsresultVo=new ReceivecpsresultVo();
		String url = "";// 请求地址
		String req = "";// 请求数据
		String responContent = "";
   	 	ClaimInterfaceLogVo logVo = new ClaimInterfaceLogVo();
		// 组装请求数据
		rReceivecpsresultVo=paramsForReceivecpsresultVo(registNo,status);
		try{
            JSONObject json = new JSONObject();
            String username = SpringProperties.getProperty("hnbxrest_user");
            String password = SpringProperties.getProperty("hnbxrest_password");
            url = SpringProperties.getProperty("hnbxrest_cps_url");
			// 快赔案件号
            json.put("casenumber",rReceivecpsresultVo.getCasenumber());
			// 保险公司报案号
            json.put("inscaseno",rReceivecpsresultVo.getInscaseno());
			// 定损车辆信息列表
            json.put("casecarcpslist",rReceivecpsresultVo.getCasecarcpslist());
			// 用户名--鼎和保险机构代码
            json.put("username",username);
			// 用户密码
            json.put("password",EncryptUtils.desEncrypt(password));
            String params = json.toString();
            req=params;
            HttpPost httpPost = new HttpPost(url);
            if(StringUtils.isNotBlank(params)){
                StringEntity requestEntity = new StringEntity(params,ContentType.create("application/json","UTF-8"));
                httpPost.setEntity(requestEntity);
            }
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity httpEntity = response.getEntity();
            responContent = EntityUtils.toString(httpEntity);
            responContent = new String(responContent.getBytes("UTF-8"), "UTF-8");
            JSONObject rejson = JSONObject.fromObject(responContent);
            String restate = rejson.getString("restate");
            String redes = rejson.getString("redes");
            if("1".equals(restate)){
            	logVo.setStatus("1");
            }else{
            	logVo.setStatus("0");
            	logVo.setErrorCode(restate);
            }
            logVo.setErrorMessage(redes);
			logger.info("状态restate"+restate+",返回信息"+redes);
        }catch(Exception e){
			logger.info("错误信息:"+e.getMessage());
            e.printStackTrace();
            logVo.setStatus("0");
            logVo.setErrorMessage(e.getMessage());
        }finally{
        	logVo.setComCode(userVo.getComCode());
			logVo.setRegistNo(registNo);// 理赔报案号
			logVo.setClaimNo(rReceivecpsresultVo.getCasenumber());// 快赔案件号
	        logVo.setCreateUser(userVo.getUserCode());
	        logVo.setComCode(userVo.getComCode());
			logVo.setFlag(status);// 1-结案2-拒赔3-零结4-注销
	        if("1".equals(status)){
				logVo.setOperateNode("结案");
	        }else if("2".equals(status)){
				logVo.setOperateNode("拒赔");
	        }else if("3".equals(status)){
				logVo.setOperateNode("零结");
	        }else if("4".equals(status)){
				logVo.setOperateNode("注销");
	        }
	        logVo.setBusinessType(BusinessType.HNQC_endCas.name());
	        logVo.setBusinessName(BusinessType.HNQC_endCas.getName());
	        logVo.setRequestXml(req);
	        logVo.setCreateTime(new Date());
	        logVo.setRequestTime(new Date());
	        logVo.setRequestUrl(url+"receivecpsresult");
	        logVo.setResponseXml(responContent);
	        interfaceLogService.save(logVo);
			logger.info("接口({})调用耗时{}ms"+( System.currentTimeMillis()-t1 ));
        }
	}
	
	
	@Override
	public RespondMsg receivegusunresult(String registNo,SysUserVo sysUserVo) {	
		ClaimInterfaceLogVo logVo = new ClaimInterfaceLogVo();
		String url = "";
		String requestContent = "";
		String responContent = "";
		// 是否需要商业险赔付
		String needtype = "";
		// 定损金额送河南快赔
		PrplQuickCaseInforVo prplQuickCaseInforVo = submitcaseinforService.findByRegistNo(registNo);
		PrpLDlossCarMainVo carMainVo = lossCarService.findLossCarMainBySerialNo(registNo, 1).get(0);
    	List<PrpLDlossCarMainVo> prpLDlossCarMainVoList = lossCarService.findLossCarMainByRegistNo(registNo);
		ReceivegusunresultVo receivegusunresultVo = new ReceivegusunresultVo();
        try{
        	String username = SpringProperties.getProperty("hnbxrest_user");
            String password = SpringProperties.getProperty("hnbxrest_password");
			// 使用代理地址
            url = SpringProperties.getProperty("hnbxrest_gusun_url");
            
            List<PrpLCMainVo> cMainVoList = policyViewService.findPrpLCMainVoListByRegistNo(registNo);
			// 先判断商业保单有没有在系统报案，若有，则是否需要商业赔付则为0
    		if(cMainVoList != null){
    			for(PrpLCMainVo cMainVo:cMainVoList){
    				if(!"1101".equals(cMainVo.getRiskCode())){
    					needtype = "0";
    					break;
    				}
    			}
    		}
			/*如果非互碰自赔本车损失金额大于0或者三者车损失大于2000，则涉及商业险赔付；
			 * 如果是互碰自赔本车损失超过2000则涉及商业险赔付。*/
    		if(!"0".equals(needtype)){
    			PrpLCheckDutyVo checkDutyVo = checkTaskService.findCheckDuty(registNo, 1);
    			if("1".equals(checkDutyVo.getIsClaimSelf())){
    				if(NullToZero(carMainVo.getSumVeriLossFee()).compareTo(new BigDecimal(2000)) > 0){
    					needtype = "1";
    				}
    			}else{
					// 统计三者车的损失金额
    				BigDecimal thirdSumVeriLossFee =BigDecimal.ZERO;
    				for(PrpLDlossCarMainVo lossCarMainVo:prpLDlossCarMainVoList){
    					if(!"1".equals(lossCarMainVo.getDeflossCarType())){
    						thirdSumVeriLossFee = thirdSumVeriLossFee.add(NullToZero(lossCarMainVo.getSumVeriLossFee()));
    					}
    				}
    				if(NullToZero(carMainVo.getSumVeriLossFee()).compareTo(BigDecimal.ZERO) > 0
    						|| thirdSumVeriLossFee.compareTo(new BigDecimal(2000)) > 0){
    					needtype = "1";
    				}
    			}
    		}   		
            receivegusunresultVo.setCasenumber(prplQuickCaseInforVo.getCasenumber());
    		receivegusunresultVo.setInscaseno(registNo);
    		receivegusunresultVo.setNeedtype(needtype);
    		receivegusunresultVo.setOperateuser(sysUserVo.getUserCode());
    		List<CasecarfeeVo> casecarfeelist = new ArrayList<CasecarfeeVo>();	
    		for(PrpLDlossCarMainVo lossCarMainVo:prpLDlossCarMainVoList){
    			CasecarfeeVo casecarfeeVo = new CasecarfeeVo();
    			casecarfeeVo.setCasecarno(lossCarMainVo.getLicenseNo());
    			casecarfeeVo.setFeepaymoney(lossCarMainVo.getSumVeriLossFee());
    			casecarfeeVo.setFeetime(DateUtils.dateToStr(new Date(), DateUtils.YToSec));
    			casecarfeelist.add(casecarfeeVo);
    		}
    		
    		receivegusunresultVo.setCasecarfeelist(casecarfeelist);
            
            JSONObject json = new JSONObject();
            json.put("casenumber",receivegusunresultVo.getCasenumber());
            json.put("inscaseno",receivegusunresultVo.getInscaseno());
            json.put("casecarfeelist",receivegusunresultVo.getCasecarfeelist());
            json.put("needtype",receivegusunresultVo.getNeedtype());
            json.put("username",username);
            json.put("password",EncryptUtils.desEncrypt(password));
            requestContent = json.toString();
			logger.info("send======河南快赔定损金额请求报文："+requestContent);
            
            HttpPost httpPost = new HttpPost(url);
            if(requestContent!=null&& !requestContent.trim().equals("")){
                StringEntity requestEntity = new StringEntity(requestContent,ContentType.create("application/json","UTF-8"));
                httpPost.setEntity(requestEntity);
            }
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity httpEntity = response.getEntity();
            responContent = EntityUtils.toString(httpEntity);
            responContent = new String(responContent.getBytes("UTF-8"), "UTF-8");
			logger.info("return======河南快赔定损金额返回报文："+responContent);
            JSONObject rejson = JSONObject.fromObject(responContent);
            String restate = rejson.getString("restate");
            String redes = rejson.getString("redes");
            if("1".equals(restate)){
            	logVo.setStatus("1");
            }else{
            	logVo.setStatus("0");
            	logVo.setErrorCode(restate);
            }
            logVo.setErrorMessage(redes);
        }catch(Exception e){
            e.printStackTrace();
            logVo.setStatus("0");
            logVo.setErrorMessage(e.getMessage());
        }finally{
			// 存日志表
            logVo.setRequestUrl(url);
            logVo.setRequestXml(requestContent);
            logVo.setResponseXml(responContent);
            logVo.setRegistNo(receivegusunresultVo.getInscaseno());
            logVo.setBusinessType(BusinessType.HNQC_DLossResult.name());
            logVo.setBusinessName(BusinessType.HNQC_DLossResult.getName());
            logVo.setOperateNode(FlowNode.VLCar.name());
            logVo.setRequestTime(new Date());
            logVo.setCreateTime(new Date());
            logVo.setCreateUser(receivegusunresultVo.getOperateuser());
            interfaceLogService.save(logVo);
        }
        return RespondMsg.SUCCESS();
    }
	
	
	
	/**
	 * 给请求数据赋值
	 * @param registNo
	 * @return
	 */
	private ReceivecpsresultVo paramsForReceivecpsresultVo(String registNo,String status) {
		String username = SpringProperties.getProperty("hnbxrest_user");
		String password = SpringProperties.getProperty("hnbxrest_password");
		List<PrpLLossItemVo> lossItemVos = compensateTaskService.findLossItemsByRegistNo(registNo);
		List<PrpLDlossCarMainVo> lossCarMains = lossCarService.findLossCarMainByRegistNo(registNo);
		PrpLRegistVo prplregistVo = registQueryService.findByRegistNo(registNo);

		ReceivecpsresultVo resultVo = new ReceivecpsresultVo();
		List<CasecarcpsVo> caseCarList = new ArrayList<CasecarcpsVo>();
		PrplQuickCaseInforVo caseInfo = submitcaseinforService.findByRegistNo(registNo);
		if( !"4".equals(status)){// 案件状态为注销除外
			if(lossCarMains!=null && lossCarMains.size()>0){
				for(PrpLDlossCarMainVo carMainVo:lossCarMains){// 根据定损车辆情况组织赔付数据
				CasecarcpsVo cpsVo=new CasecarcpsVo();
				BigDecimal Cpsmoney = new BigDecimal(0);
					cpsVo.setCasecarno(carMainVo.getLicenseNo());// 案件车牌号
					cpsVo.setCpstime(this.DateFormatString(new Date()));// 理赔时间
					cpsVo.setCpstype(status);// 理赔状态
					cpsVo.setCpsresult("返回结果");
				if("1".equals(status)){
					if(lossItemVos!=null && lossItemVos.size()>0){
						for(PrpLLossItemVo vo:lossItemVos){
							if(carMainVo.getLicenseNo().equals(vo.getItemName())){
								Cpsmoney = Cpsmoney.add(DataUtils.NullToZero(vo.getSumRealPay()));
							}
						}
					}
				}
				cpsVo.setCpsmoney(Cpsmoney.toString());
				
				caseCarList.add(cpsVo);
			 }
		   }
		}else{// 案件状态为注销
			if(prplregistVo!=null){
				List<PrpLRegistCarLossVo> carLossList = prplregistVo.getPrpLRegistCarLosses();
				if(carLossList!=null&&carLossList.size()>0){
					for(PrpLRegistCarLossVo vo:carLossList){
						if("1".equals(vo.getLossparty())){
							CasecarcpsVo cpsVo = new CasecarcpsVo();
							cpsVo.setCasecarno(vo.getLicenseNo());// 案件车牌号
							cpsVo.setCpstime(this.DateFormatString(new Date()));// 理赔时间
							cpsVo.setCpstype(status);// 理赔状态
							cpsVo.setCpsresult("返回结果");
							cpsVo.setCpsmoney("0.0");
							caseCarList.add(cpsVo);
						}
					}
				}
			}
		}
		if(caseInfo!=null){
			resultVo.setCasenumber(caseInfo.getCasenumber());
		}
		resultVo.setInscaseno(registNo);// 案件报案号
		resultVo.setUsername(username);// 用户机构名称
		resultVo.setPassword(password);// 密码
		resultVo.setCasecarcpslist(caseCarList);
		return resultVo;
	}
	
	/**
	 * 时间转换方法 Date 类型转换 String类型
	 * @param strDate
	 * @return
	 * @throws ParseException
	 */
	private String DateFormatString(Date strDate){
		String str="";
		SimpleDateFormat format=new SimpleDateFormat("YYYY-MM-DD HH:MM:SS");
		  if(strDate!=null){
			  str=format.format(strDate);
		}
		return str;
	}
	
	/**
	 * BigDecimal null转0
	 * @param strNum
	 * @return
	 */
	private static BigDecimal NullToZero(BigDecimal strNum) {
		if(strNum==null){
			return new BigDecimal("0");
		}
		return strNum;
	}
	
}
