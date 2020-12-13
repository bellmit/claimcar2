package ins.sino.claimcar.ciitc.service;



import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import ins.platform.common.service.facade.BaseDaoService;
import ins.platform.utils.ClaimBaseCoder;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.carchild.util.CarchildUtil;
import ins.sino.claimcar.carinterface.service.ClaimInterfaceLogService;
import ins.sino.claimcar.carinterface.util.BusinessType;
import ins.sino.claimcar.carinterface.vo.ClaimInterfaceLogVo;
import ins.sino.claimcar.ciitc.vo.CiitcReqHeadVo;
import ins.sino.claimcar.ciitc.vo.accident.CiitcAccidentReqBodyVo;
import ins.sino.claimcar.ciitc.vo.accident.CiitcAccidentReqVo;
import ins.sino.claimcar.ciitc.vo.accident.DataInformation;

import java.util.List;

import ins.framework.dao.database.DatabaseDao;
import ins.framework.dao.database.support.QueryRule;
import ins.framework.utils.Beans;
import ins.sino.claimcar.ciitc.po.PrplZBXArea;
import ins.sino.claimcar.ciitc.po.PrplZBXPushInfo;
import ins.sino.claimcar.ciitc.vo.PrplZBXAreaVo;
import ins.sino.claimcar.ciitc.vo.PrplZBXPushInfoVo;
import ins.sino.claimcar.ciitc.vo.accident.CiitcAccidentResVo;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.regist.service.RegistService;
import ins.sino.claimcar.regist.vo.PrpLRegistCarLossVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.SpringProperties;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import com.thoughtworks.xstream.io.xml.XppDriver;

@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"})
@Path("accidentService")
public class AccidentServiceImpl implements AccidentService {
	private static final Logger LOGGER = LoggerFactory.getLogger(AccidentServiceImpl.class);
	private static final String FALSEFLAG = "false";
	private static final String DHIC = "DHIC";

	
    @Autowired
    private ClaimInterfaceLogService interfaceLogService;
	
	@Autowired
    private DatabaseDao databaseDao;
	@Autowired
	RegistService registService;
	@Autowired
	BaseDaoService baseDaoService;
	
	@Override
	public CiitcAccidentResVo findAccidentInfoByRegistNo(String registNo,SysUserVo userVo) {
		
	    String xmlToSend = "";
	    String url = "";
	    String xmlReturn = "";
	    CiitcAccidentReqVo ciitcAccidentReqVo = new CiitcAccidentReqVo();
	    CiitcAccidentResVo ciitcAccidentResVo = new CiitcAccidentResVo();
	    CiitcReqHeadVo headVo = new CiitcReqHeadVo();
	    CiitcAccidentReqBodyVo bodyVo = new CiitcAccidentReqBodyVo();
		PrpLRegistVo registVo = registService.findRegistByRegistNo(registNo);
		String licenseNo = "";
		if (null != registVo) {
			List<PrpLRegistCarLossVo> prpLRegistCarLosses = registVo.getPrpLRegistCarLosses();
			if(prpLRegistCarLosses != null && prpLRegistCarLosses.size() > 0 ){
				PrpLRegistCarLossVo prpLRegistCarLossVo = prpLRegistCarLosses.get(0);
				licenseNo = prpLRegistCarLossVo.getLicenseNo();
			}
		}
	    List<PrplZBXPushInfoVo> prplZBXPushInfoVos = findAccidentInfoByOther(registNo, licenseNo);
	    if(prplZBXPushInfoVos != null && prplZBXPushInfoVos.size() > 0){
	    	PrplZBXPushInfoVo prplZBXPushInfoVo = prplZBXPushInfoVos.get(0);
	    	DataInformation dataInformation = new DataInformation();
	    	dataInformation.setAcciAreaCode(prplZBXPushInfoVo.getAcciAreaCode());
	    	headVo.setAcciAreaCode(prplZBXPushInfoVo.getAcciAreaCode());
	    	dataInformation.setAcciNo(prplZBXPushInfoVo.getAcciNo());
	    	String reportDate = prplZBXPushInfoVo.getReportDate();
	    	reportDate = reportDate.substring(0, 4)+"-"+reportDate.substring(4, 6)+"-"+reportDate.substring(6, 8)+" "+
	    			reportDate.substring(8,10)+":"+reportDate.substring(10, 12)+":"+reportDate.substring(12, 14);
	    	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    	try {
				Date reportEndDate = formatter.parse(reportDate);
				Calendar afterTime = Calendar.getInstance();
				afterTime = dataToCalendar(reportEndDate);
				afterTime.add(Calendar.MINUTE, 5);
				reportEndDate = (Date)afterTime.getTime();
				//reportEndDate = new Date(reportEndDate.getTime() + 300000);
				Date reportStartDate = formatter.parse(reportDate);
				//reportStartDate = new Date(reportStartDate.getTime() - 300000);
				Calendar startTime = Calendar.getInstance();
				startTime = dataToCalendar(reportStartDate);
				startTime.add(Calendar.MINUTE, -5);
				reportStartDate = (Date)startTime.getTime();
				String reportEndDateString = formatter.format(reportEndDate);
				reportEndDateString = reportEndDateString.replaceAll("-", "");
				reportEndDateString = reportEndDateString.replaceAll(" ", "");
				reportEndDateString = reportEndDateString.replaceAll(":", "");
				String reportStartDateString = formatter.format(reportStartDate);
				reportStartDateString = reportStartDateString.replaceAll("-", "");
				reportStartDateString = reportStartDateString.replaceAll(" ", "");
				reportStartDateString = reportStartDateString.replaceAll(":", "");
		    	dataInformation.setReportEndDate("");
		    	dataInformation.setReportStartDate("");
			} catch (ParseException e) {
				e.printStackTrace();
			}
	    	bodyVo.setDataInformation(dataInformation);
	    	headVo.setRequestType("02");
	 	    headVo.setInstitutionCode(DHIC);
	 	    String userName = SpringProperties.getProperty("CIITC_VIEW_USER");
	 	    String passWord = SpringProperties.getProperty("CIITC_VIEW_PWD");
	 	    headVo.setUserName(userName);
	 	    headVo.setPassWord(passWord);
	 	    ciitcAccidentReqVo.setBody(bodyVo);
	 	    ciitcAccidentReqVo.setHead(headVo);
	 	    
	 	    
	 	    //设置值
	 	    xmlToSend = ClaimBaseCoder.objToXml(ciitcAccidentReqVo);
	         url = SpringProperties.getProperty("CIITC_URL");
	         LOGGER.info("事故信息查询接口提交send---------------------------"+xmlToSend);
	         try {
	 			xmlReturn = CarchildUtil.requestPlatformByCode(xmlToSend,url,200,"","GBK");
	 			LOGGER.info("事故信息查询接口返回报文send---------------------------"+xmlReturn);
	 			ciitcAccidentResVo = ClaimBaseCoder.xmlToObj(xmlReturn, CiitcAccidentResVo.class);
	 		} catch (Exception e) {
	 			LOGGER.info("事故信息查询接口返回报文send异常",e);
	 		} finally {
	 			saveCiitcAccidentInterfaceLog(userVo, registNo, ciitcAccidentReqVo, ciitcAccidentResVo, url);
	 		}
	    }
	   
		return ciitcAccidentResVo;
	}

	
    private void saveCiitcAccidentInterfaceLog(SysUserVo userVo,String registNo,CiitcAccidentReqVo reqVo,CiitcAccidentResVo resVo,String url){     
        ClaimInterfaceLogVo logVo = new ClaimInterfaceLogVo();
        String requestXml = "";
        String responseXml = "";
        try{
        	logVo.setBusinessType(BusinessType.CIITC_Regist.name());
            logVo.setBusinessName(BusinessType.CIITC_Regist.getName());
            
            XStream stream = new XStream(new XppDriver(new XmlFriendlyNameCoder("-_","_")));
            stream.autodetectAnnotations(true);
            stream.setMode(XStream.NO_REFERENCES);
            stream.aliasSystemAttribute(null,"class");// 去掉 class属性
            requestXml = stream.toXML(reqVo);
            responseXml = stream.toXML(resVo);
            if("0".equals(resVo.getHead().getResCode())){//成功
                logVo.setStatus("1");
                logVo.setErrorCode("true");
            }else {
                logVo.setStatus("0");
                logVo.setErrorCode(FALSEFLAG);
            }
            if(StringUtils.isNotEmpty(resVo.getHead().getErrorMessage())){
                logVo.setErrorMessage(resVo.getHead().getErrorMessage());
            }
        }
        catch(Exception e){
            logVo.setStatus("0");
            logVo.setErrorCode(FALSEFLAG);
            LOGGER.error("事故信息查询接口返回报文send异常",e);
        }
        finally{
            logVo.setRegistNo(registNo);
            logVo.setOperateNode(FlowNode.Regis.name());
            logVo.setComCode(userVo.getComCode());
            Date date = new Date();
            logVo.setRequestTime(date);
            logVo.setRequestUrl(url);
            logVo.setCreateUser(userVo.getUserCode());
            logVo.setCreateTime(date);
            logVo.setRequestXml(requestXml);
            logVo.setResponseXml(responseXml);
            interfaceLogService.save(logVo);
        }
       
    }

	@Override
	public void saveAccidentInfo(List<PrplZBXPushInfoVo> prplZBXPushInfoVos) {
		if(prplZBXPushInfoVos!=null){
			PrplZBXPushInfo prplZBXPushInfo = null;
			for (PrplZBXPushInfoVo prplZBXPushInfoVo : prplZBXPushInfoVos) {
				prplZBXPushInfo = new PrplZBXPushInfo();
				Beans.copy().from(prplZBXPushInfoVo).to(prplZBXPushInfo);
				databaseDao.save(PrplZBXPushInfo.class, prplZBXPushInfo);
			}
		}
	}

	@Override
	public PrplZBXAreaVo findZBXArea(String areaCode) {
		PrplZBXAreaVo prplZBXAreaVo = null;
		if(StringUtils.isNotEmpty(areaCode)){
			PrplZBXArea prplZBXArea = databaseDao.findByPK(PrplZBXArea.class, areaCode);
			if(prplZBXArea!=null){
				prplZBXAreaVo = new PrplZBXAreaVo();
				Beans.copy().from(prplZBXArea).to(prplZBXAreaVo);
			}
		}
		return prplZBXAreaVo;
	}
	
	@Override
	public List<PrplZBXPushInfoVo> findAccidentInfoByOther(String registNo,String licenseNo) {
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo",registNo);
		if(StringUtils.isNotEmpty(licenseNo)){
			queryRule.addEqual("licenseNo",licenseNo);
		}
		List<PrplZBXPushInfoVo> prplZBXPushInfoVos = new ArrayList<PrplZBXPushInfoVo>();
		List<PrplZBXPushInfo> prplZBXPushInfoPoList = databaseDao.findAll(PrplZBXPushInfo.class,queryRule);
		if(prplZBXPushInfoPoList != null && prplZBXPushInfoPoList.size() > 0){
			prplZBXPushInfoVos = Beans.copyDepth().from(prplZBXPushInfoPoList).toList(PrplZBXPushInfoVo.class);
		}
		return prplZBXPushInfoVos;
	}


	  public static Calendar dataToCalendar(Date date) {
	        Calendar calendar = Calendar.getInstance();
	        calendar.setTime(date);
	        return calendar;
	    }


	@Override
	public List<PrplZBXPushInfoVo> findAccidentInfo(String frameNo,
			String engineNo, String licenseNo) {
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("vin",frameNo);
		queryRule.addEqual("licenseNo",licenseNo);
		queryRule.addEqual("engineNo",engineNo);
		queryRule.addDescOrder("reportDate");
		List<PrplZBXPushInfoVo> prplZBXPushInfoVos = new ArrayList<PrplZBXPushInfoVo>();
		List<PrplZBXPushInfo> prplZBXPushInfoPoList = databaseDao.findAll(PrplZBXPushInfo.class,queryRule);
		if(prplZBXPushInfoPoList != null && prplZBXPushInfoPoList.size() > 0){
			prplZBXPushInfoVos = Beans.copyDepth().from(prplZBXPushInfoPoList).toList(PrplZBXPushInfoVo.class);
		}
		return prplZBXPushInfoVos;
	}


	@Override
	public PrplZBXPushInfoVo findInfoByAcciNo(String acciNo) {
		// TODO Auto-generated method stub
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("acciNo",acciNo);
		PrplZBXPushInfoVo prplZBXPushInfoVo = null;
		PrplZBXPushInfo prplZBXPushInfoPo = databaseDao.findUnique(PrplZBXPushInfo.class,queryRule);
		if(prplZBXPushInfoPo != null){
			prplZBXPushInfoVo = Beans.copyDepth().from(prplZBXPushInfoPo).to(PrplZBXPushInfoVo.class);
		}
		return prplZBXPushInfoVo;
	}


	@Override
	public void updateAccidentInfo(PrplZBXPushInfoVo prplZBXPushInfoVo) {
		String sql = "UPDATE PrplZBXPushInfo SET updateTime= sysdate WHERE id= " + prplZBXPushInfoVo.getId();
		baseDaoService.executeSQL(sql);
	}
} 
