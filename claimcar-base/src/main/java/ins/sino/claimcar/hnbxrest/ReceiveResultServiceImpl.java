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
			// ??????????????????
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
			logger.info("send======???????????????????????????????????????????????????"+requestContent);

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
			logger.info("return======???????????????????????????????????????????????????"+responContent);
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
			// ????????????
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
		String url = "";// ????????????
		String req = "";// ????????????
		String responContent = "";
   	 	ClaimInterfaceLogVo logVo = new ClaimInterfaceLogVo();
		// ??????????????????
		rReceivecpsresultVo=paramsForReceivecpsresultVo(registNo,status);
		try{
            JSONObject json = new JSONObject();
            String username = SpringProperties.getProperty("hnbxrest_user");
            String password = SpringProperties.getProperty("hnbxrest_password");
            url = SpringProperties.getProperty("hnbxrest_cps_url");
			// ???????????????
            json.put("casenumber",rReceivecpsresultVo.getCasenumber());
			// ?????????????????????
            json.put("inscaseno",rReceivecpsresultVo.getInscaseno());
			// ????????????????????????
            json.put("casecarcpslist",rReceivecpsresultVo.getCasecarcpslist());
			// ?????????--????????????????????????
            json.put("username",username);
			// ????????????
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
			logger.info("??????restate"+restate+",????????????"+redes);
        }catch(Exception e){
			logger.info("????????????:"+e.getMessage());
            e.printStackTrace();
            logVo.setStatus("0");
            logVo.setErrorMessage(e.getMessage());
        }finally{
        	logVo.setComCode(userVo.getComCode());
			logVo.setRegistNo(registNo);// ???????????????
			logVo.setClaimNo(rReceivecpsresultVo.getCasenumber());// ???????????????
	        logVo.setCreateUser(userVo.getUserCode());
	        logVo.setComCode(userVo.getComCode());
			logVo.setFlag(status);// 1-??????2-??????3-??????4-??????
	        if("1".equals(status)){
				logVo.setOperateNode("??????");
	        }else if("2".equals(status)){
				logVo.setOperateNode("??????");
	        }else if("3".equals(status)){
				logVo.setOperateNode("??????");
	        }else if("4".equals(status)){
				logVo.setOperateNode("??????");
	        }
	        logVo.setBusinessType(BusinessType.HNQC_endCas.name());
	        logVo.setBusinessName(BusinessType.HNQC_endCas.getName());
	        logVo.setRequestXml(req);
	        logVo.setCreateTime(new Date());
	        logVo.setRequestTime(new Date());
	        logVo.setRequestUrl(url+"receivecpsresult");
	        logVo.setResponseXml(responContent);
	        interfaceLogService.save(logVo);
			logger.info("??????({})????????????{}ms"+( System.currentTimeMillis()-t1 ));
        }
	}
	
	
	@Override
	public RespondMsg receivegusunresult(String registNo,SysUserVo sysUserVo) {	
		ClaimInterfaceLogVo logVo = new ClaimInterfaceLogVo();
		String url = "";
		String requestContent = "";
		String responContent = "";
		// ???????????????????????????
		String needtype = "";
		// ???????????????????????????
		PrplQuickCaseInforVo prplQuickCaseInforVo = submitcaseinforService.findByRegistNo(registNo);
		PrpLDlossCarMainVo carMainVo = lossCarService.findLossCarMainBySerialNo(registNo, 1).get(0);
    	List<PrpLDlossCarMainVo> prpLDlossCarMainVoList = lossCarService.findLossCarMainByRegistNo(registNo);
		ReceivegusunresultVo receivegusunresultVo = new ReceivegusunresultVo();
        try{
        	String username = SpringProperties.getProperty("hnbxrest_user");
            String password = SpringProperties.getProperty("hnbxrest_password");
			// ??????????????????
            url = SpringProperties.getProperty("hnbxrest_gusun_url");
            
            List<PrpLCMainVo> cMainVoList = policyViewService.findPrpLCMainVoListByRegistNo(registNo);
			// ??????????????????????????????????????????????????????????????????????????????????????????0
    		if(cMainVoList != null){
    			for(PrpLCMainVo cMainVo:cMainVoList){
    				if(!"1101".equals(cMainVo.getRiskCode())){
    					needtype = "0";
    					break;
    				}
    			}
    		}
			/*?????????????????????????????????????????????0???????????????????????????2000??????????????????????????????
			 * ???????????????????????????????????????2000???????????????????????????*/
    		if(!"0".equals(needtype)){
    			PrpLCheckDutyVo checkDutyVo = checkTaskService.findCheckDuty(registNo, 1);
    			if("1".equals(checkDutyVo.getIsClaimSelf())){
    				if(NullToZero(carMainVo.getSumVeriLossFee()).compareTo(new BigDecimal(2000)) > 0){
    					needtype = "1";
    				}
    			}else{
					// ??????????????????????????????
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
			logger.info("send======???????????????????????????????????????"+requestContent);
            
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
			logger.info("return======???????????????????????????????????????"+responContent);
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
			// ????????????
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
	 * ?????????????????????
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
		if( !"4".equals(status)){// ???????????????????????????
			if(lossCarMains!=null && lossCarMains.size()>0){
				for(PrpLDlossCarMainVo carMainVo:lossCarMains){// ??????????????????????????????????????????
				CasecarcpsVo cpsVo=new CasecarcpsVo();
				BigDecimal Cpsmoney = new BigDecimal(0);
					cpsVo.setCasecarno(carMainVo.getLicenseNo());// ???????????????
					cpsVo.setCpstime(this.DateFormatString(new Date()));// ????????????
					cpsVo.setCpstype(status);// ????????????
					cpsVo.setCpsresult("????????????");
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
		}else{// ?????????????????????
			if(prplregistVo!=null){
				List<PrpLRegistCarLossVo> carLossList = prplregistVo.getPrpLRegistCarLosses();
				if(carLossList!=null&&carLossList.size()>0){
					for(PrpLRegistCarLossVo vo:carLossList){
						if("1".equals(vo.getLossparty())){
							CasecarcpsVo cpsVo = new CasecarcpsVo();
							cpsVo.setCasecarno(vo.getLicenseNo());// ???????????????
							cpsVo.setCpstime(this.DateFormatString(new Date()));// ????????????
							cpsVo.setCpstype(status);// ????????????
							cpsVo.setCpsresult("????????????");
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
		resultVo.setInscaseno(registNo);// ???????????????
		resultVo.setUsername(username);// ??????????????????
		resultVo.setPassword(password);// ??????
		resultVo.setCasecarcpslist(caseCarList);
		return resultVo;
	}
	
	/**
	 * ?????????????????? Date ???????????? String??????
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
	 * BigDecimal null???0
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
