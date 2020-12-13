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
			logger.info("=================移动查勘案件列表查询（快赔请求理赔）接口请求报文:"+reqXml);
			MCqryCaseListReqPacket packet = (MCqryCaseListReqPacket)arg1;
			Assert.notNull(packet, "数据为空 ");
			MobileCheckHead head = packet.getHead();
			if (!"014".equals(head.getRequestType())|| !"claim_user".equals(head.getUser())|| !"claim_psd".equals(head.getPassWord())) {
				throw new IllegalArgumentException(" 请求头参数错误  ");
			}
			MCqryCaseListReqBody body = packet.getBody();
			Assert.notNull(body, " 数据为空 ");
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
				Assert.notNull(null, " 车牌号、报案号、被保险人至少一个有值 ");
			List<MCqryCaseListResBody> resBodyList = this.findResMsg(body);
			MCqryCaseListResCaseList caseList = new MCqryCaseListResCaseList();
			caseList.setCaseList(resBodyList);
			resPacket.setCaseList(caseList);
			
		}catch(Exception e){
			resHead.setResponseType("014");
			resHead.setResponseMessage(e.getMessage());
			resHead.setResponseCode("NO");
			resPacket.setHead(resHead);
			logger.info("移动查勘案件列表查询（快赔请求理赔）接口报错信息： "+e.getMessage());
			 e.printStackTrace();
		}
		String resXml = ClaimBaseCoder.objToXmlUtf(resPacket);
		logger.info("=================移动查勘案件列表查询（快赔请求理赔）接口返回报文:"+resXml);
		return resPacket;
	}
	
	/**
	 * 组织业务数据 查询 PRPLWFmain PRPLWFTASKQUERY PRPLREGIST PRPLWFTASKIN PRPLWFTASKOUT 5张表
	 * 此接口为移动查勘和自助理赔的案件列表查询接口 移动查勘只查询当前登录工号下的做过查勘定损任务 
	 * 移动查勘必传处理人
	 * <pre></pre>
	 * @param reqBody
	 * @return
	 * @modified:
	 * *牛强(2017年5月18日 上午9:27:58): <br>
	 * 
	 *朱彬修改：添加字段是否打印和评价
	 */
	private List<MCqryCaseListResBody> findResMsg(MCqryCaseListReqBody reqBody){
		Date currentDate = new Date();
		logger.info("案件查询接口开始");
		SqlJoinUtils sqlUtil = new SqlJoinUtils();
		//增加了
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
			sqlUtil.addParamValue("7");//排除移交的查勘
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
			sqlUtil.addParamValue("7");//排除移交的查勘
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
		logger.info("案件查询接口开始调用sql=" + sqlUtil.getSql() +"耗时"+(System.currentTimeMillis()-currentDate.getTime() )+"毫秒");
		List<Object[]> objListResult = baseDaoService.getAllBySql(sqlUtil.getSql(), sqlUtil.getParamValues());
		logger.info("案件查询接口调用sql=" + sqlUtil.getSql() +"耗时"+(System.currentTimeMillis()-currentDate.getTime() )+"毫秒");
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
				resBody.setIsVip("0");  // 写死 否
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
				resBody.setCasesSatus((String)obj[3]);//任务状态
				resBody.setCaseFlag("3");//案件标示
				if(obj[6] != null &&obj[7] != null && "1".equals((String)obj[7])){
					if("0".equals((String)obj[6])){//电话直赔
						resBody.setCaseFlag("1");//案件标示
					}else if("1".equals((String)obj[6])){//微信自助理赔
						resBody.setCaseFlag("2");//案件标示
					}
				}
				
				//判断是否查勘完成 
				if(obj[8] != null){
					if("3".equals(obj[8].toString())){
						resBody.setIsJudge("1");
					}
				}
				resBodyList.add(resBody);
			}
		}
		if(registNos != null && registNos.size() > 0){
			logger.info("案件查询接口开始调用(根据多个报案号查询所有信息)方法，报案号={}，耗时"+(System.currentTimeMillis()-currentDate.getTime() )+"毫秒",registNos);
			List<PrpLDlossCarMainVo> prpLDlossCarMainVos = deflossHandleService.findAllLossCarMainInfoByRegistNo(registNos);
			logger.info("案件查询接口结束调用(根据多个报案号查询所有信息)方法，耗时"+(System.currentTimeMillis()-currentDate.getTime() )+"毫秒");
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
						printInfo.setIfObject(vo.getSerialNo()==1?"标的":"三者");
						printInfo.setLicenseNo(vo.getLicenseNo());
						printInfo.setIsPrint(vo.getUnderWriteFlag().equals("1")?"1":"0");
						printInfos.add(printInfo);
					}
				}
				body.setPrintInfos(printInfos);
			}
		}
		logger.info("案件查询接口结束");
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
