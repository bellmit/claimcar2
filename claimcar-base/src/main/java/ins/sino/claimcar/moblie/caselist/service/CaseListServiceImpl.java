package ins.sino.claimcar.moblie.caselist.service;

import ins.framework.lang.Springs;
import ins.platform.common.service.facade.BaseDaoService;
import ins.platform.common.util.ConfigUtil;
import ins.platform.utils.ClaimBaseCoder;
import ins.platform.utils.DateUtils;
import ins.platform.utils.SqlJoinUtils;
import ins.platform.vo.PrpLConfigValueVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.check.service.CheckHandleService;
import ins.sino.claimcar.check.vo.PrpLCheckCarInfoVo;
import ins.sino.claimcar.check.vo.PrpLCheckCarVo;
import ins.sino.claimcar.check.vo.PrpLCheckVo;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.service.WfFlowQueryService;
import ins.sino.claimcar.flow.service.WfMainService;
import ins.sino.claimcar.flow.vo.PrpLWfMainVo;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.losscar.service.DeflossHandleService;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarMainVo;
import ins.sino.claimcar.mobileCheckCommon.vo.MobileCheckHead;
import ins.sino.claimcar.mobileCheckCommon.vo.MobileCheckResponseHead;
import ins.sino.claimcar.moblie.caselist.vo.MCqryCaseListReqBody;
import ins.sino.claimcar.moblie.caselist.vo.MCqryCaseListReqPacket;
import ins.sino.claimcar.moblie.caselist.vo.MCqryCaseListResBody;
import ins.sino.claimcar.moblie.caselist.vo.MCqryCaseListResCaseList;
import ins.sino.claimcar.moblie.caselist.vo.MCqryCaseListResPacket;
import ins.sino.claimcar.moblie.caselist.vo.PrintInfo;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.common.utils.Assert;
import com.sinosoft.api.service.ServiceInterface;

public class CaseListServiceImpl implements ServiceInterface {

	@Autowired
	BaseDaoService baseDaoService;
	@Autowired
	WfFlowQueryService wfFlowQueryService;
	@Autowired
	DeflossHandleService deflossHandleService;
	@Autowired
	CheckHandleService checkHandleService;
	private static Logger logger = LoggerFactory.getLogger(CaseListServiceImpl.class);
	
	@Override
	public Object service(String arg0, Object arg1) {
		init();
		MCqryCaseListResPacket resPacket = new MCqryCaseListResPacket();
		MobileCheckResponseHead resHead = new MobileCheckResponseHead();
		resHead.setResponseType("014");
		resHead.setResponseMessage("Success");
		resHead.setResponseCode("YES");
		resPacket.setHead(resHead);
		String registNo ="";
		try{
			String reqXml = ClaimBaseCoder.objToXmlUtf(arg1);
			logger.info("=================????????????????????????????????????????????????????????????????????????:"+reqXml);
			MCqryCaseListReqPacket packet = (MCqryCaseListReqPacket)arg1;
			Assert.notNull(packet, "???????????? ");
			MobileCheckHead head = packet.getHead();
			if (!"014".equals(head.getRequestType())|| !"claim_user".equals(head.getUser())|| !"claim_psd".equals(head.getPassWord())) {
				throw new IllegalArgumentException(" ?????????????????????  ");
			}
			MCqryCaseListReqBody body = packet.getBody();
			Assert.notNull(body, " ???????????? ");
			int i=3;
			if(StringUtils.isBlank(body.getLicenseNo())){
			    registNo = body.getLicenseNo();
				i--;
			}
			 if(StringUtils.isBlank(body.getInsuredName())){
			     registNo = body.getInsuredName();
	                i--;
			 }
			if(StringUtils.isBlank(body.getRegistNo())){
			    registNo =body.getRegistNo();
				i--;
			}
		
			if(i<1)
				Assert.notNull(null, " ?????????????????????????????????????????????????????? ");
			List<MCqryCaseListResBody> resBodyList = this.findResMsg(body);
			MCqryCaseListResCaseList caseList = new MCqryCaseListResCaseList();
			caseList.setCaseList(resBodyList);
			resPacket.setCaseList(caseList);
			
		}catch(Exception e){
			resHead.setResponseType("014");
			resHead.setResponseMessage(e.getMessage());
			resHead.setResponseCode("NO");
			resPacket.setHead(resHead);
			logger.info("??????????????????????????????????????????????????????????????????????????? "+e.getMessage());
			 e.printStackTrace();
		}
		String resXml = ClaimBaseCoder.objToXmlUtf(resPacket);
		logger.info("=================????????????????????????????????????????????????????????????????????????:"+resXml);
		return resPacket;
	}
	
	/**
	 * ?????????????????? ?????? PRPLWFmain PRPLWFTASKQUERY PRPLREGIST PRPLWFTASKIN PRPLWFTASKOUT 5??????
	 * ?????????????????????????????????????????????????????????????????? ????????????????????????????????????????????????????????????????????? 
	 * ???????????????????????????
	 * <pre></pre>
	 * @param reqBody
	 * @return
	 * @modified:
	 * *??????(2017???5???18??? ??????9:27:58): <br>
	 * 
	 *????????????????????????????????????????????????
	 */
	private List<MCqryCaseListResBody> findResMsg(MCqryCaseListReqBody reqBody){
		Date currentDate = new Date();
		logger.info("????????????????????????");
		SqlJoinUtils sqlUtil = new SqlJoinUtils();
		//?????????
		sqlUtil.append("select QRYCASE.REGISTNO,QRYCASE.LICENSENO,QRYCASE.INSUREDNAME,QRYCASE.lastNode,QRYCASE.DRIVERNAME,QRYCASE.DAMAGETIME,"
				+ "QRYCASE.SELFREGISTFLAG,scheduletask.IsAutoCheck,wftaskout.WORKSTATUS from ("
				+ " select a.REGISTNO, b.LICENSENO, b.INSUREDNAME, a.lastNode, c.DRIVERNAME, c.DAMAGETIME, c.SELFREGISTFLAG ");
		sqlUtil.append(" from PRPLWFmain a, PRPLWFTASKQUERY b, PRPLREGIST c ");
		sqlUtil.append(" where a.REGISTNO = b.REGISTNO and a.REGISTNO = c.registNo ");
		sqlUtil.append(" and a.REGISTNO in ");
		sqlUtil.append(" (select distinct (registno) from (");
		sqlUtil.append(" select distinct (registno) registno from PRPLWFTASKIN where 1=1 ");
		if(StringUtils.isNotBlank(reqBody.getHandlerCode())){
			sqlUtil.append(" and (HANDLERUSER = ? ");
			sqlUtil.addParamValue(reqBody.getHandlerCode());
			sqlUtil.append(" or assignuser = ? )");
			sqlUtil.addParamValue(reqBody.getHandlerCode());
			sqlUtil.append(" and workstatus != ? ");
			sqlUtil.addParamValue("7");//?????????????????????
			sqlUtil.append(" and (Subnodecode = ? or Subnodecode = ?)");
			sqlUtil.addParamValue("Chk");
			sqlUtil.addParamValue("DLCar");
		}
		sqlUtil.append("  union all ");
		sqlUtil.append("  select distinct (registno) registno from PRPLWFTASKOUT where 1=1 ");
		if(StringUtils.isNotBlank(reqBody.getHandlerCode())) {
			sqlUtil.append(" and HANDLERUSER = ? ");
			sqlUtil.addParamValue(reqBody.getHandlerCode());
			sqlUtil.append(" and workstatus != ? ");
			sqlUtil.addParamValue("7");//?????????????????????
			sqlUtil.append(" and (Subnodecode = ? or Subnodecode = ?)");
			sqlUtil.addParamValue("Chk");
			sqlUtil.addParamValue("DLCar");
		}
		sqlUtil.append(" )) ");
		
		if(StringUtils.isNotBlank(reqBody.getIdentifyNumber())){
			sqlUtil.append(" and a.POLICYNO in ");
			sqlUtil.append(" (select distinct (a.POLICYNO) from ");
			sqlUtil.append(" PrpLCInsured a where a.identifynumber = ? and a.InsuredFlag ='1' )");
			sqlUtil.addParamValue(reqBody.getIdentifyNumber());
		}
		if(StringUtils.isNotBlank(reqBody.getRegistNo())){
			sqlUtil.append(" and a.REGISTNO = ? ");
			sqlUtil.addParamValue(reqBody.getRegistNo());
		}
		if(StringUtils.isNotBlank(reqBody.getLicenseNo())){
			sqlUtil.append(" and b.LICENSENO = ? ");
			sqlUtil.addParamValue(reqBody.getLicenseNo());
		}
		if(StringUtils.isNotBlank(reqBody.getInsuredName())){
			sqlUtil.append(" and b.INSUREDNAME = ? ");
			sqlUtil.addParamValue(reqBody.getInsuredName());
		}
		sqlUtil.append(" ) QRYCASE  LEFT JOIN prplscheduletask   scheduletask ON scheduletask.registno = QRYCASE.REGISTNO and scheduletask.IsAutoCheck  = '1'  and scheduletask.validflag = '0' "
				+ "LEFT JOIN prplwftaskout wftaskout ON wftaskout.Registno = QRYCASE.REGISTNO AND wftaskout.Subnodecode ='Chk' and wftaskout.WORKSTATUS = '3'");
		logger.info("??????????????????????????????sql=" + sqlUtil.getSql() +"??????"+(System.currentTimeMillis()-currentDate.getTime() )+"??????");
		List<Object[]> objListResult = baseDaoService.getAllBySql(sqlUtil.getSql(), sqlUtil.getParamValues());
		logger.info("????????????????????????sql=" + sqlUtil.getSql() +"??????"+(System.currentTimeMillis()-currentDate.getTime() )+"??????");
		List<Object[]> objList = new ArrayList<Object[]>();
		Map<String,Object[]> objListMap = new HashMap<String,Object[]>();

		for(Object[] object : objListResult){
			String registNo = (String) object[0];
			if(objListMap.get(registNo) == null){
				objList.add(object);
				objListMap.put(registNo, object);
			}
		}
		List<MCqryCaseListResBody> resBodyList = new ArrayList<MCqryCaseListResBody>();
		List<String> registNos = new ArrayList<String>();
		Map<String,List<PrpLDlossCarMainVo>> prpLDlossCarMainVoMap = new HashMap<String,List<PrpLDlossCarMainVo>>();
		if(objList != null && objList.size() > 0){
			for(Object[] obj : objList){
				MCqryCaseListResBody resBody = new MCqryCaseListResBody();
				String registNo = (String)obj[0];
				resBody.setRegistNo(registNo);
				registNos.add(registNo);
				resBody.setLicenseNo((String)obj[1]);
				resBody.setInsuredName((String)obj[2]);
				resBody.setCaseState((String)obj[3]);
				resBody.setIsVip("0");  // ?????? ???
				resBody.setDriverName((String)obj[4]);
				if(obj[5] != null){
					try {
						String damageDate = DateUtils.dateToStr(DateUtils.strToDate(obj[5].toString(), DateUtils.YToSec), DateUtils.YToSec);
						resBody.setDamageDate(damageDate);
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}else{
					resBody.setDamageDate("");
				}
				resBody.setCasesSatus((String)obj[3]);//????????????
				resBody.setCaseFlag("3");//????????????
				if(obj[6] != null &&obj[7] != null && "1".equals((String)obj[7])){
					if("0".equals((String)obj[6])){//????????????
						resBody.setCaseFlag("1");//????????????
					}else if("1".equals((String)obj[6])){//??????????????????
						resBody.setCaseFlag("2");//????????????
					}
				}
				
				//???????????????????????? 
				if(obj[8] != null){
					if("3".equals(obj[8].toString())){
						resBody.setIsJudge("1");
					}
				}
				resBodyList.add(resBody);
			}
		}
		if(registNos != null && registNos.size() > 0){
			logger.info("??????????????????????????????(???????????????????????????????????????)??????????????????={}?????????"+(System.currentTimeMillis()-currentDate.getTime() )+"??????",registNos);
			List<PrpLDlossCarMainVo> prpLDlossCarMainVos = deflossHandleService.findAllLossCarMainInfoByRegistNo(registNos);
			logger.info("??????????????????????????????(???????????????????????????????????????)???????????????"+(System.currentTimeMillis()-currentDate.getTime() )+"??????");
			for(PrpLDlossCarMainVo lossCarMainVo : prpLDlossCarMainVos){
				String registNoMap = lossCarMainVo.getRegistNo();
				if(prpLDlossCarMainVoMap.get(registNoMap) != null && prpLDlossCarMainVoMap.get(registNoMap).size() > 0){
					List<PrpLDlossCarMainVo> dlossCarMainVos = prpLDlossCarMainVoMap.get(registNoMap);
					dlossCarMainVos.add(lossCarMainVo);
					prpLDlossCarMainVoMap.put(registNoMap, dlossCarMainVos);
				}else{
					List<PrpLDlossCarMainVo> dlossCarMainVos = new ArrayList<PrpLDlossCarMainVo>();
					dlossCarMainVos.add(lossCarMainVo);
					prpLDlossCarMainVoMap.put(registNoMap, dlossCarMainVos);
				}
			}
			for(MCqryCaseListResBody body : resBodyList){
				String registNo = body.getRegistNo();
				List<PrpLDlossCarMainVo> dlossCarMainVos = prpLDlossCarMainVoMap.get(registNo);
				List<PrintInfo> printInfos = new ArrayList<PrintInfo>();
				if(dlossCarMainVos != null && !dlossCarMainVos.isEmpty()){
					for(PrpLDlossCarMainVo vo : dlossCarMainVos){
						PrintInfo printInfo = new PrintInfo();
						printInfo.setIfObject(vo.getSerialNo()==1?"??????":"??????");
						printInfo.setLicenseNo(vo.getLicenseNo());
						printInfo.setIsPrint(vo.getUnderWriteFlag().equals("1")?"1":"0");
						printInfos.add(printInfo);
					}
				}
				body.setPrintInfos(printInfos);
			}
		}
		logger.info("????????????????????????");
		return resBodyList;
	}
	
	private void init(){
		if(baseDaoService == null){
			 baseDaoService = (BaseDaoService)Springs.getBean(BaseDaoService.class);
		}
		if(wfFlowQueryService == null){
			wfFlowQueryService = (WfFlowQueryService)Springs.getBean(WfFlowQueryService.class);
		}
		if(deflossHandleService == null){
			deflossHandleService = (DeflossHandleService)Springs.getBean(DeflossHandleService.class);
		}
		if(checkHandleService == null){
			checkHandleService = (CheckHandleService)Springs.getBean(CheckHandleService.class);
		}
	}

}
