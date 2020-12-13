package ins.sino.claimcar.losscar.service.spring;

import ins.framework.common.ResultPage;
import ins.framework.dao.database.DatabaseDao;
import ins.framework.dao.database.support.Page;
import ins.framework.dao.database.support.QueryRule;
import ins.framework.utils.Beans;
import ins.platform.common.service.facade.BaseDaoService;
import ins.platform.utils.DateUtils;
import ins.platform.utils.SqlJoinUtils;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.CodeConstants.VeriFlag;
import ins.sino.claimcar.claim.service.ClaimTaskService;
import ins.sino.claimcar.claim.service.CompensateTaskService;
import ins.sino.claimcar.claim.vo.PrpLClaimVo;
import ins.sino.claimcar.claimjy.service.ClaimToJyService;
import ins.sino.claimcar.claimjy.service.JyDLChkService;
import ins.sino.claimcar.claimjy.service.JyDlossService;
import ins.sino.claimcar.constant.Risk;
import ins.sino.claimcar.fitting.service.ClaimFittingCertaService;
import ins.sino.claimcar.fitting.service.ClaimFittingCheckLossService;
import ins.sino.claimcar.fitting.service.ClaimFittingCheckPriceService;
import ins.sino.claimcar.fitting.service.ClaimFittingInterService;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.service.AssignService;
import ins.sino.claimcar.flow.service.WfFlowQueryService;
import ins.sino.claimcar.flow.service.WfTaskHandleService;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.flow.vo.WfTaskSubmitVo;
import ins.sino.claimcar.losscar.po.PrpLDlossCarComp;
import ins.sino.claimcar.losscar.po.PrpLDlossCarInfo;
import ins.sino.claimcar.losscar.po.PrpLDlossCarMain;
import ins.sino.claimcar.losscar.po.PrpLDlossCarMainHis;
import ins.sino.claimcar.losscar.po.PrpLDlossCarRepair;
import ins.sino.claimcar.losscar.service.CarLossAdjustService;
import ins.sino.claimcar.losscar.service.CarLossQueryService;
import ins.sino.claimcar.losscar.service.CaseCompService;
import ins.sino.claimcar.losscar.service.DeflossService;
import ins.sino.claimcar.losscar.service.LossCarService;
import ins.sino.claimcar.losscar.vo.CarQueryReslutVo;
import ins.sino.claimcar.losscar.vo.CarQueryVo;
import ins.sino.claimcar.losscar.vo.ClaimFittingVo;
import ins.sino.claimcar.losscar.vo.PrpLCaseComponentVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarCompVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarInfoVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarMainVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarRepairVo;
import ins.sino.claimcar.lossprop.vo.PropQueryReslutVo;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.service.RegistService;
import ins.sino.claimcar.regist.vo.PrpLCItemKindVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.SpringProperties;

import com.alibaba.dubbo.config.annotation.Service;

@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"})
@Path("lossCarService")
public class LossCarServiceSpringImpl implements LossCarService {
	
	@Autowired
	DeflossService deflossService;
	@Autowired
	DatabaseDao databaseDao;
	@Autowired
	BaseDaoService baseDaoService;
	@Autowired
	WfTaskHandleService wfTaskHandleService;
	@Autowired
	ClaimTaskService claimTaskService;
	@Autowired
	WfFlowQueryService wfFlowQueryService;
	@Autowired
	private WfTaskHandleService taskHandleService;
	@Autowired
	private CarLossAdjustService carLossAdjustService;
	
	@Autowired
	private CompensateTaskService compensateTaskService;
	@Autowired
	CaseCompService caseCompService;
	@Autowired
	AssignService assignService;
	@Autowired
	ClaimFittingInterService claimFittingInterService;
	@Autowired
	CarLossQueryService carLossQueryService;
	@Autowired
	ClaimFittingCertaService fittingCertaService;
	@Autowired
	ClaimFittingCheckPriceService fittingPriceService;
	@Autowired
	ClaimFittingCheckLossService fittingCheckService;
	@Autowired
	RegistService registService;
	@Autowired
	ClaimToJyService claimToJyService;
	@Autowired
	JyDlossService jyDlossService;
	@Autowired
	JyDLChkService jyDLChkService;
	@Autowired
	PolicyViewService policyViewService;
	
	@Override
	public PrpLDlossCarMainVo findLossCarMainById(Long id) {
		PrpLDlossCarMainVo lossCarMainVo = deflossService.findDeflossByPk(id);
		if(lossCarMainVo ==null){
			return null;
		}
		PrpLDlossCarInfoVo carInfoVo = deflossService.findDefCarInfoByPk(lossCarMainVo.getCarId());
		carInfoVo.setSerialNo(lossCarMainVo.getSerialNo());
		lossCarMainVo.setLossCarInfoVo(carInfoVo);
		
		return lossCarMainVo;
	}

	@Override
	public List<PrpLDlossCarMainVo> findLossCarMainByRegistNo(String registNo) {
		List<PrpLDlossCarMainVo> lossCarMainList = deflossService.findLossCarMainByRegistNo(registNo);
		if(lossCarMainList!=null && lossCarMainList.size()>0){
			for(PrpLDlossCarMainVo lossCarMainVo : lossCarMainList){
				PrpLDlossCarInfoVo carInfoVo = deflossService.findDefCarInfoByPk(lossCarMainVo.getCarId());
				carInfoVo.setSerialNo(lossCarMainVo.getSerialNo());
				lossCarMainVo.setLossCarInfoVo(carInfoVo);
			}
		}
		
		return lossCarMainList;
	}

	@Override
	public ResultPage<PrpLDlossCarMainVo> findLossCarMainPageByRegistNo(String registNo,int start,int length) {
		// TODO Auto-generated method stub
		SqlJoinUtils sqlUtil=new SqlJoinUtils();
		sqlUtil.append(" FROM PrpLDlossCarMain lossCar");
		sqlUtil.append(" WHERE lossCar.registNo= ?");
		sqlUtil.append(" and lossCar.underWriteFlag= ?");
		sqlUtil.addParamValue(registNo);
		sqlUtil.addParamValue(VeriFlag.PASS);// 核损通过
		String sql = sqlUtil.getSql();
		Object[] values = sqlUtil.getParamValues();
		
		Page<Object[]> page = databaseDao.findPageByHql(sql,start/length+1,length,values);
		
		List<PrpLDlossCarMainVo> resultVoList=new ArrayList<PrpLDlossCarMainVo>();
		for(int i = 0; i<page.getResult().size(); i++ ){
			PrpLDlossCarMainVo resultVo = new PrpLDlossCarMainVo();
			List<Object[]> obj = page.getResult();

			PrpLDlossCarMain lossCarMain = (PrpLDlossCarMain)obj.toArray()[i];
			Beans.copy().from(lossCarMain).to(resultVo);
			resultVoList.add(resultVo);
		}
		ResultPage<PrpLDlossCarMainVo> resultPage = new ResultPage<PrpLDlossCarMainVo> (start, length, page.getTotalCount(), resultVoList);

		
		return resultPage;
	}

	/**
	 * 根据报案号查询定损车辆信息
	 * 
	 * <pre></pre>
	 * @param registNo
	 * @return
	 * @modified: ☆ZhouYanBin(2016年4月9日 下午6:24:53): <br>
	 */
	@Override
	public List<PrpLDlossCarInfoVo> findPrpLDlossCarInfoVoListByRegistNo(String registNo) {
		List<PrpLDlossCarInfoVo> prpLDlossCarInfoVoList = null;
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo",registNo);
		List<PrpLDlossCarInfo> prpLDlossCarInfoList = databaseDao.findAll(PrpLDlossCarInfo.class,queryRule);
		if(prpLDlossCarInfoList != null && !prpLDlossCarInfoList.isEmpty()){
			prpLDlossCarInfoVoList = Beans.copyDepth().from(prpLDlossCarInfoList).toList(PrpLDlossCarInfoVo.class);
		}
		return prpLDlossCarInfoVoList;
	}

	/**
	 * 根据逐渐查找唯一数据
	 * 
	 * <pre></pre>
	 * @param Id
	 * @return
	 * @modified: ☆ZhouYanBin(2016年4月12日 上午9:07:24): <br>
	 */
	@Override
	public PrpLDlossCarInfoVo findPrpLDlossCarInfoVoById(Long id) {
		PrpLDlossCarInfoVo prpLDlossCarInfoVo = null;
		PrpLDlossCarInfo prpLDlossCarInfo = databaseDao.findByPK(PrpLDlossCarInfo.class,id);
		if(prpLDlossCarInfo != null){
			prpLDlossCarInfoVo = Beans.copyDepth().from(prpLDlossCarInfo).to(PrpLDlossCarInfoVo.class);
		}
		return prpLDlossCarInfoVo;
	}

	@Override
	public void updateDlossCarMain(PrpLDlossCarMainVo dlossCarMainVo) {
		PrpLDlossCarMain DlossCarMain = databaseDao.findByPK(PrpLDlossCarMain.class,dlossCarMainVo.getId());
		if(DlossCarMain!=null){
			Beans.copy().from(dlossCarMainVo).excludeNull().to(DlossCarMain);
		}
		// databaseDao.update(PrpLDlossCarMain.class,DlossCarMain);
		deflossService.updateLossCarMain(DlossCarMain);
	}

	/**
	 * 根据条件查询数据
	 * 
	 * <pre></pre>
	 * @param registNo
	 * @param lossState
	 * @param underWriteFlag
	 * @param serialNo
	 * @return
	 * @modified: ☆ZhouYanBin(2016年4月26日 下午3:57:10): <br>
	 */
	@Override
	public List<PrpLDlossCarMainVo> findPrpLDlossCarMainVoListByCondition(String registNo,List<String> lossState,List<String> underWriteFlag,String serialNo) {
		List<PrpLDlossCarMainVo> prpLDlossCarMainVo = null;
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo",registNo);
		if(lossState != null && !"".equals(lossState)){
			queryRule.addIn("lossState",lossState);	
		}
		if(underWriteFlag != null && !"".equals(underWriteFlag)){
			queryRule.addIn("underWriteFlag",underWriteFlag);	
		}
		if(serialNo != null && !"".equals(serialNo)){
			queryRule.addEqual("serialNo",serialNo);
		}
		
		queryRule.addEqual("validFlag","1");
		
		List<PrpLDlossCarMain> prpLDlossCarMainList = databaseDao.findAll(PrpLDlossCarMain.class,queryRule);
		if(prpLDlossCarMainList != null && !prpLDlossCarMainList.isEmpty()){
			prpLDlossCarMainVo = Beans.copyDepth().from(prpLDlossCarMainList).toList(PrpLDlossCarMainVo.class);
		}
		return prpLDlossCarMainVo;
	}


	/**
	 * 根据QueryRule查找记录
	 * 
	 * <pre></pre>
	 * @param queryRule
	 * @return
	 * @modified: ☆ZhouYanBin(2016年4月27日 下午5:03:55): <br>
	 */
	@Override
	public List<PrpLDlossCarMainVo> findPrpLDlossCarMainVoListByRule(QueryRule queryRule) {
		List<PrpLDlossCarMainVo> prpLDlossCarMainVoList = null;
		
		List<PrpLDlossCarMain> prpLDlossCarMainList = databaseDao.findAll(PrpLDlossCarMain.class,queryRule);
		if(prpLDlossCarMainList != null && !prpLDlossCarMainList.isEmpty()){
			prpLDlossCarMainVoList = Beans.copyDepth().from(prpLDlossCarMainList).toList(PrpLDlossCarMainVo.class);
		}
		return prpLDlossCarMainVoList;
	}

	/**
	 * 通过报案号和车辆序号查找定损信息 ☆yangkun(2016年1月21日 下午6:37:23): <br>
	 */
	public List<PrpLDlossCarMainVo> findLossCarMainBySerialNo(String registNo,Integer serialNo){
		return deflossService.findLossCarMainBySerialNo(registNo,serialNo);
	}
	
	/**
	 * 修改定损 最后一个核损任务 发起理算任务
	 * @param registNo
	 * @return
	 * @modified: ☆YangKun(2016年7月15日 下午6:38:33): <br>
	 */
	public List<PrpLWfTaskVo> modifyToSubMitComp(String registNo,SysUserVo userVo) {
		List<PrpLWfTaskVo> wfTaskVoList = new ArrayList<PrpLWfTaskVo>();
		
		List<String> nodeList = new ArrayList<String>();
		nodeList.add(FlowNode.DLoss.name());
		nodeList.add(FlowNode.PLoss.name());
		nodeList.add(FlowNode.VLoss.name());
		nodeList.add(FlowNode.VPrice.name());
		nodeList.add(FlowNode.Certi.name());
		
		if(!wfTaskHandleService.existTaskByNodeList(registNo,nodeList)){
			List<PrpLClaimVo> claimList = claimTaskService.findClaimListByRegistNo(registNo);
			PrpLWfTaskVo certiTaskVo = wfFlowQueryService.findCertiByRegistNo(registNo).get(0);
			for(PrpLClaimVo prpLClaimVo : claimList){
				if(prpLClaimVo.getCancelTime() !=null){
					continue;
				}
				WfTaskSubmitVo submitVo = new WfTaskSubmitVo();
				FlowNode subNode =null ; 
				submitVo.setCurrentNode(FlowNode.Certi);
				if(Risk.isDQZ(prpLClaimVo.getRiskCode())){
					submitVo.setNextNode(FlowNode.CompeCI);
					subNode = FlowNode.CompeCI;
				}else{
					submitVo.setNextNode(FlowNode.CompeBI);
					subNode = FlowNode.CompeBI;
				}
//				PrpLWfTaskVo comTaskVo = wfFlowQueryService.findWfTaskVo(registNo,subNode,"1").get(0);
				submitVo.setFlowId(certiTaskVo.getFlowId());
				submitVo.setFlowTaskId(certiTaskVo.getTaskId());
				submitVo.setComCode(prpLClaimVo.getComCode());
				submitVo.setTaskInUser(userVo.getUserCode());
				submitVo.setTaskInKey(prpLClaimVo.getClaimNo());
				submitVo.setHandleIdKey(prpLClaimVo.getClaimNo());
				// 不指定处理人
				submitVo.setAssignCom(prpLClaimVo.getComCode());
//				submitVo.setAssignUser(comTaskVo.getAssignUser());

				PrpLWfTaskVo wfTaskVo = wfTaskHandleService.addCompenTask(prpLClaimVo,submitVo);
				wfTaskVoList.add(wfTaskVo);
			}
		}
		
		return wfTaskVoList;
		
	}

	/**
	 * 核赔通过创建PrpLCaseComponent表
	 */
	@Override
	public void createCaseComponent(List<PrpLCaseComponentVo> caseVoList) {
		caseCompService.createCaseComponent(caseVoList);
	}
	
	// 理算注销是发起定损
	public String carModifyLaunch(Long lossId, SysUserVo sysUserVo){
		
		return carLossAdjustService.carModifyLaunch(lossId, sysUserVo);
	
	}

	@Override
	public List<PrpLCaseComponentVo> findCaseCompList(String compCode,
			String frameNo) {
		
		return caseCompService.findCaseCompList(compCode, frameNo);
	}

	@Override
	public String carAdditionLaunch(Long id, SysUserVo userVo) {
		return carLossAdjustService.carAdditionLaunch(id,userVo);
	}

	@Override
	public String sendXMLData(ClaimFittingVo claimFittingVo,SysUserVo userVo) throws Exception {
		String url = "";
		PrpLRegistVo registVo = registService.findRegistByRegistNo(claimFittingVo.getRegistNo());
		String JY_TimeStamp = SpringProperties.getProperty("JY_TIMESTAMP");
		Date timeStamp = DateUtils.strToDate(JY_TimeStamp, DateUtils.YToSec);
		// 如果报案日期大于时间戳则请求精友二代
		try {
			if (registVo.getReportTime().getTime() > timeStamp.getTime()) {

				if ("certa".equals(claimFittingVo.getOperateType())) {
					url = jyDlossService.dlossAskService(claimFittingVo, userVo);
				} else if ("verifyPrice".equals(claimFittingVo.getOperateType())) {
					url = claimToJyService.priceToJy(claimFittingVo, userVo);
				} else if ("verifyLoss".equals(claimFittingVo.getOperateType())) {
					url = claimToJyService.vlossToJy(claimFittingVo, userVo);
				} else if ("DLChk".equals(claimFittingVo.getOperateType())) {
					// 复检请求精友接口URL
					url = jyDLChkService.dLChkAskService(claimFittingVo, userVo);
				}
			} else {
				url = claimFittingInterService.sendXMLData(claimFittingVo);
			}
		} catch (Exception e) {
			throw e;
		}

		return url;
	}

	@Override
	public ResultPage<CarQueryReslutVo> findPageForAdjust(CarQueryVo carQueryVo) throws Exception {
		return carLossQueryService.findPageForAdjust(carQueryVo);
	}
	
	@Override
	public ResultPage<PropQueryReslutVo> findPropPageForAdjust(CarQueryVo queryVo) throws Exception {
		return carLossQueryService.findPropPageForAdjust(queryVo);
	}

	@Override
	public String doTransData(String operateType, String strData) throws Exception {
		String flag = null;
		
		if("certa".equals(operateType)){	
			flag = fittingCertaService.doTransData(strData);
		}else if("verifyPrice".equals(operateType)){	
			flag= fittingPriceService.doTransData(strData);
		}else if("verifyLoss".equals(operateType)){	
			flag= fittingCheckService.doTransData(strData);
		}
		return flag;
	}
	@Override
	public PrpLDlossCarMainVo findDlossCarMainVoByRegistNoAndId(String registNo,Long id){
		 PrpLDlossCarMainVo prpLDlossCarMainVo = null;
		 QueryRule queryRule = QueryRule.getInstance();
		 queryRule.addEqual("registNo",registNo);
		 queryRule.addEqual("id",id);
		 PrpLDlossCarMain carMainPo = databaseDao.findUnique(PrpLDlossCarMain.class,queryRule);
		 if(carMainPo!=null){
			 prpLDlossCarMainVo = new PrpLDlossCarMainVo();
			 Beans.copy().from(carMainPo).to(prpLDlossCarMainVo);
		 }
		 return prpLDlossCarMainVo;
	 }
	
	/**
	 * 报案提交时，历次出险出现过全车盗抢、推定全损，报案提交需给出软提示
	 * @author WLL
	 */
	@Override
	public Boolean checkCarAllLossHisByPolicyNo(String PolicyNo){
		Boolean checkCarAllLossFlag = false;
		SqlJoinUtils sqlUtils = new SqlJoinUtils();
		sqlUtils.append(" select * from PrpLDlossCarMain where ");
		sqlUtils.append(" deflossCarType = '1' and  cetainLossType in ('02', '03') and  underWriteFlag = '1' ");
		sqlUtils.append(" and registNo in ( select registNo from PrpLCMain where policyNo = ? )  ");
		sqlUtils.addParamValue(PolicyNo);
		
		String sql = sqlUtils.getSql();
		Object[] values = sqlUtils.getParamValues();
		System.out.println(sql);
		
		List<Object[]> allLossCarMainList = null;
		try{
			allLossCarMainList = baseDaoService.findListBySql(sql,values);
		}catch(Exception e){
			e.printStackTrace();
		}
		if(allLossCarMainList!=null&&allLossCarMainList.size()>0){
			checkCarAllLossFlag = true;
		}
		
		 return checkCarAllLossFlag;
	 }
	
	@Override
	public String checkCarLossHisByPolicyNo(String PolicyNo){
		
		SqlJoinUtils sqlUtils = new SqlJoinUtils();
		sqlUtils.append(" select * from PrpLDlossCarMain where ");
		sqlUtils.append(" deflossCarType = '1' and  cetainLossType in ('02') and  underWriteFlag = '1' ");
		sqlUtils.append(" and registNo in ( select registNo from PrpLCMain where policyNo = ? )  ");
		sqlUtils.addParamValue(PolicyNo);
		
		String sql = sqlUtils.getSql();
		Object[] values = sqlUtils.getParamValues();
		System.out.println(sql);
		
		List<Object[]> list_1 = null;
		try{
			list_1 = baseDaoService.findListBySql(sql,values);
		}catch(Exception e){
			e.printStackTrace();
		}
		if(list_1!=null&&list_1.size()>0){
			return "02";
		}
		
		SqlJoinUtils sqlUtils_2 = new SqlJoinUtils();
		sqlUtils_2.append(" select * from PrpLDlossCarMain where ");
		sqlUtils_2.append(" deflossCarType = '1' and  cetainLossType in ('03') and  underWriteFlag = '1' ");
		sqlUtils_2.append(" and registNo in ( select registNo from PrpLCMain where policyNo = ? )  ");
		sqlUtils_2.addParamValue(PolicyNo);
		
		String sql_2 = sqlUtils_2.getSql();
		Object[] values_2 = sqlUtils_2.getParamValues();
		
		List<Object[]> list_2 = null;
		try{
			list_2 = baseDaoService.findListBySql(sql_2,values_2);
		}catch(Exception e){
			e.printStackTrace();
		}
		if(list_2!=null&&list_2.size()>0){
			return "03";
		}
		
		 return "0";
	 }
	
	public PrpLDlossCarInfoVo findDefCarInfoByPk(Long id){
		return deflossService.findDefCarInfoByPk(id);
	}
	
	public void updateLossCarInfo(PrpLDlossCarInfoVo lossCarInfo){
		deflossService.updateLossCarInfo(lossCarInfo);
	}

    @Override
    public List<PrpLDlossCarMainVo> findLossCarMainByScheduleDeflossId(String registNo,Long scheduleDeflossId) {
        List<PrpLDlossCarMainVo> prpLDlossCarMainVoList = null;
        QueryRule queryRule = QueryRule.getInstance();
        queryRule.addEqual("registNo",registNo);
        queryRule.addEqual("scheduleDeflossId",scheduleDeflossId);
        List<PrpLDlossCarMain> prpLDlossCarMainList = databaseDao.findAll(PrpLDlossCarMain.class,queryRule);
        if(prpLDlossCarMainList != null && !prpLDlossCarMainList.isEmpty()){
            prpLDlossCarMainVoList = Beans.copyDepth().from(prpLDlossCarMainList).toList(PrpLDlossCarMainVo.class);
        }
        return prpLDlossCarMainVoList;
    }
    
    @Override
    public List<PrpLDlossCarMainVo> findLossCarMainByUnderWriteFlag(String registNo,String underWriteFlag,String flag){
		// List<PrpLDlossCarMainVo> lossCarMainVoList = new ArrayList<PrpLDlossCarMainVo>();
		// QueryRule queryRule = QueryRule.getInstance();
		// queryRule.addEqual("registNo",registNo);
		// if("1".equals(flag)){
		// queryRule.addEqual("underWriteFlag",underWriteFlag);
		// }else if("0".equals(flag)){
		// queryRule.addNotEqual("underWriteFlag",underWriteFlag);
		// }
		// List<PrpLDlossCarMain> prpLDlossCarMainList = databaseDao.findAll(PrpLDlossCarMain.class,queryRule);
		// if(prpLDlossCarMainList!=null&& !prpLDlossCarMainList.isEmpty()){
		// lossCarMainVoList = Beans.copyDepth().from(prpLDlossCarMainList).toList(PrpLDlossCarMainVo.class);
		// }
		// return lossCarMainVoList;
		return deflossService.findLossCarMainByUnderWriteFlag(registNo,underWriteFlag,flag);
    }

	@Override
	public boolean findJyInfoByDefLossMainId(Long defLossMainId) {
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("lossCarMainId",defLossMainId);
		queryRule.addAscOrder("createTime");
		
		List<PrpLDlossCarMainHis> carMainHisList = databaseDao.findAll(PrpLDlossCarMainHis.class,queryRule);
		if(carMainHisList != null && carMainHisList.size() >0){
			for(PrpLDlossCarMainHis vo : carMainHisList){
				SqlJoinUtils sqlUtil = new SqlJoinUtils();
				sqlUtil.append("SELECT LOSSMAINHISID FROM PrpLDlossCarMaterialHis WHERE LOSSMAINHISID = ? ");
				sqlUtil.addParamValue(vo.getId());
				String sql = sqlUtil.getSql();
				Object[] values = sqlUtil.getParamValues();
				// 执行查询
				List<Object[]> objects = baseDaoService.getAllBySql(sql,values);
				if(objects != null && objects.size() > 0){
					return true;
				}
				SqlJoinUtils sqlUtil1 = new SqlJoinUtils();
				sqlUtil1.append("SELECT LOSSMAINHISID FROM PrpLDlossCarCompHis WHERE LOSSMAINHISID = ? ");
				sqlUtil1.addParamValue(vo.getId());
				String sql1 = sqlUtil1.getSql();
				Object[] values1 = sqlUtil1.getParamValues();
				// 执行查询
				List<Object[]> objects1 = baseDaoService.getAllBySql(sql1,values1);
				if(objects1 != null && objects1.size() > 0){
					return true;
				}
				SqlJoinUtils sqlUtil2 = new SqlJoinUtils();
				sqlUtil2.append("SELECT LOSSMAINHISID FROM PrpLDlossCarRepairHis WHERE LOSSMAINHISID = ? ");
				sqlUtil2.addParamValue(vo.getId());
				String sql2 = sqlUtil2.getSql();
				Object[] values2 = sqlUtil2.getParamValues();
				// 执行查询
				List<Object[]> objects2 = baseDaoService.getAllBySql(sql2,values2);
				if(objects2!= null && objects2.size() > 0){
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public List<PrpLDlossCarMainVo> findPrpLDlossCarMainVoByOther(
			String registNo, Integer serialNo, String licenseNo) {
		return deflossService.findPrpLDlossCarMainVoByOther(registNo,serialNo,licenseNo);
	}
	
	@Override
	public void saveJyDeflossInfo(PrpLDlossCarMainVo lossCarMainVo, PrpLDlossCarInfoVo lossCarInfoVo) throws Exception{
		// 保存数据
		deflossService.saveByJy2Defloss(lossCarMainVo);
		// 车辆信息保存
		deflossService.saveOrUpdateDefCarInfo(lossCarInfoVo);
	}
	
	@Override
	public void saveByJyDeflossCheck(PrpLDlossCarMainVo lossCarMainVo,String type){
		deflossService.saveByJyDeflossCheck(lossCarMainVo, type);
	}


	@Override
	public void updateJyDlossCarMain(PrpLDlossCarMainVo prpLDlossCarMainVo) {
		// 更新数据
		deflossService.updateDefloss(prpLDlossCarMainVo);
	}

	@Override
	public List<String> kindCodes(String registNo){
		List<PrpLCItemKindVo> itemKindVoList = policyViewService.findItemKinds(registNo,null);
		List<String> kindCodeList=new ArrayList<String>();
		if(itemKindVoList!=null && itemKindVoList.size()>0){
			for(PrpLCItemKindVo itemKindVo:itemKindVoList){
				if(!itemKindVo.getKindCode().endsWith("M")
						&& !CodeConstants.KINDCODE.KINDCODE_BZ.equals(itemKindVo.getKindCode())
	                    && !CodeConstants.NOSUBRISK_MAP.containsKey(itemKindVo.getKindCode())){
					kindCodeList.add(itemKindVo.getKindCode());
				}
			}
		}
		return kindCodeList;
	}
	@Override
	public void saveOrUpdateCarComp(List<PrpLDlossCarCompVo> prpLDlossCarCompVos,PrpLDlossCarMainVo dlossCarMainVo) {
		deflossService.saveOrUpdateCarComp(prpLDlossCarCompVos,dlossCarMainVo);
	}

	@Override
	public PrpLDlossCarMainVo findLossCarMainbyId(Long id) {
		
		PrpLDlossCarMainVo lossCarMainVo = new PrpLDlossCarMainVo();
		lossCarMainVo = deflossService.findDeflossByPk(id);
		return lossCarMainVo;
	}

	@Override
	public PrpLDlossCarMainVo findLossCarMainByRegistNoAndPaId(String registNo,Long paId) {
		PrpLDlossCarMainVo lossCarMainVo = null;
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo",registNo);
		queryRule.addEqual("paId",paId);
		List<PrpLDlossCarMain> prpLDlossCarMainList = databaseDao.findAll(PrpLDlossCarMain.class,queryRule);
		if(prpLDlossCarMainList != null && prpLDlossCarMainList.size()>0){
			lossCarMainVo=new PrpLDlossCarMainVo();
			lossCarMainVo=Beans.copyDepth().from(prpLDlossCarMainList.get(0)).to(PrpLDlossCarMainVo.class);
		}
		return lossCarMainVo;
	}

	@Override
	public PrpLDlossCarMainVo findLossCarMainByRegistNoAndLicenseNo(String registNo, String licenseNo) {
		PrpLDlossCarMainVo lossCarMainVo = null;
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo",registNo);
		queryRule.addEqual("licenseNo",licenseNo);
		List<PrpLDlossCarMain> prpLDlossCarMainList = databaseDao.findAll(PrpLDlossCarMain.class,queryRule);
		if(prpLDlossCarMainList != null && prpLDlossCarMainList.size()>0){
			lossCarMainVo=new PrpLDlossCarMainVo();
			lossCarMainVo=Beans.copyDepth().from(prpLDlossCarMainList.get(0)).to(PrpLDlossCarMainVo.class);
		}
		return lossCarMainVo;
	}

	@Override
	public void updateAndSaveAndDelSons(PrpLDlossCarMainVo prpLDlossCarMainVo) {
		if(prpLDlossCarMainVo.getId()!=null){
			PrpLDlossCarMain prpLDlossCarMain=databaseDao.findByPK(PrpLDlossCarMain.class,prpLDlossCarMainVo.getId());
			if(prpLDlossCarMain!=null){
				Beans.copy().from(prpLDlossCarMainVo).excludeNull().to(prpLDlossCarMain);
				List<PrpLDlossCarComp> carcompList=prpLDlossCarMain.getPrpLDlossCarComps();
				prpLDlossCarMain.setPrpLDlossCarComps(null);
				if(carcompList!=null && carcompList.size()>0){
					for(PrpLDlossCarComp comp:carcompList){
						databaseDao.deleteByPK(PrpLDlossCarComp.class, comp.getId());
					}
				}
				List<PrpLDlossCarRepair> carrepairList=prpLDlossCarMain.getPrpLDlossCarRepairs();
				prpLDlossCarMain.setPrpLDlossCarRepairs(null);
				if(carrepairList!=null && carrepairList.size()>0){
					for(PrpLDlossCarRepair repair:carrepairList){
						databaseDao.deleteByPK(PrpLDlossCarRepair.class, repair.getId());
					}
				}
				//车辆信息
				if(prpLDlossCarMain.getCarId()!=null){
					PrpLDlossCarInfo prpLDlossCarInfo=databaseDao.findByPK(PrpLDlossCarInfo.class,prpLDlossCarMain.getCarId());
					if(prpLDlossCarInfo!=null && prpLDlossCarMainVo.getLossCarInfoVo()!=null){
						Beans.copy().from(prpLDlossCarMainVo.getLossCarInfoVo()).excludeNull().to(prpLDlossCarInfo);
					}
					
				}
				//配件
				List<PrpLDlossCarComp> prpLDlossCarCompList=new ArrayList<PrpLDlossCarComp>(0);
				if(prpLDlossCarMainVo.getPrpLDlossCarComps()!=null && prpLDlossCarMainVo.getPrpLDlossCarComps().size()>0){
					for(PrpLDlossCarCompVo CarCompVo:prpLDlossCarMainVo.getPrpLDlossCarComps()){
						PrpLDlossCarComp prpLDlossCarComp=new PrpLDlossCarComp();
						Beans.copy().from(CarCompVo).excludeNull().to(prpLDlossCarComp);
						prpLDlossCarComp.setPrpLDlossCarMain(prpLDlossCarMain);
						prpLDlossCarCompList.add(prpLDlossCarComp);
					}
				}
				//工时
				List<PrpLDlossCarRepair> prpLDlossCarRepairList=new ArrayList<PrpLDlossCarRepair>(0);
				if(prpLDlossCarMainVo.getPrpLDlossCarRepairs()!=null && prpLDlossCarMainVo.getPrpLDlossCarRepairs().size()>0){
					for(PrpLDlossCarRepairVo repairVo:prpLDlossCarMainVo.getPrpLDlossCarRepairs()){
						PrpLDlossCarRepair prpLDlossCarRepair=new PrpLDlossCarRepair();
						Beans.copy().from(repairVo).excludeNull().to(prpLDlossCarRepair);
						prpLDlossCarRepair.setPrpLDlossCarMain(prpLDlossCarMain);
						prpLDlossCarRepairList.add(prpLDlossCarRepair);
					}
				}
				prpLDlossCarMain.setPrpLDlossCarComps(prpLDlossCarCompList);
				prpLDlossCarMain.setPrpLDlossCarRepairs(prpLDlossCarRepairList);
			}
		}
		
	}
}
