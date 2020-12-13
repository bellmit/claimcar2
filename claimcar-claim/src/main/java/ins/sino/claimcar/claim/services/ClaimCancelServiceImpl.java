package ins.sino.claimcar.claim.services;
import ins.framework.dao.database.DatabaseDao;
import ins.framework.dao.database.DatabaseFindDao;
import ins.framework.dao.database.support.QueryRule;
import ins.framework.utils.Beans;
import ins.platform.common.service.facade.BaseDaoService;
import ins.platform.common.util.CodeTranUtil;
import ins.platform.utils.DataUtils;
import ins.platform.utils.SqlJoinUtils;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.carinterface.vo.ClaimInterfaceLogVo;
import ins.sino.claimcar.claim.po.PrpLCompensate;
import ins.sino.claimcar.claim.po.PrpLCompensateExt;
import ins.sino.claimcar.claim.po.PrpLcancelTrace;
import ins.sino.claimcar.claim.po.PrpLrejectClaimText;
import ins.sino.claimcar.claim.service.ClaimCancelRecoverService;
import ins.sino.claimcar.claim.service.ClaimCancelService;
import ins.sino.claimcar.claim.service.ClaimTaskService;
import ins.sino.claimcar.claim.service.ClaimToReinsuranceService;
import ins.sino.claimcar.claim.service.CompensateTaskService;
import ins.sino.claimcar.claim.service.OtherInterfaceAsyncService;
import ins.sino.claimcar.claim.vo.PrpLClaimVo;
import ins.sino.claimcar.claim.vo.PrpLCompensateVo;
import ins.sino.claimcar.claim.vo.PrpLPadPayMainVo;
import ins.sino.claimcar.claim.vo.PrpLcancelTraceVo;
import ins.sino.claimcar.claim.vo.PrpLrejectClaimTextVo;
import ins.sino.claimcar.constant.Risk;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.service.WfFlowQueryService;
import ins.sino.claimcar.flow.service.WfTaskHandleService;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.pay.service.PadPayService;
import ins.sino.claimcar.platform.service.SendCancelToPlatformService;
import ins.sino.claimcar.platform.service.SendCancelToSHPlatformService;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.vo.PolicyEndorseInfoVo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;

/**
 * @author CMQ
 */
@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"})
@Path("claimCancelService")
public class ClaimCancelServiceImpl implements ClaimCancelService {

	@Autowired
	private DatabaseDao databaseDao;
	@Autowired
	private PadPayService padPayService;
	@Autowired
	private SendCancelToPlatformService sendCancelToAll;
	@Autowired
	private SendCancelToSHPlatformService sendCancelToSH;
	@Autowired
	private PolicyViewService policyViewService;
	@Autowired
	private ClaimToReinsuranceService claimToReinsuranceService;
	@Autowired
	private ClaimTaskService claimTaskService;
	@Autowired
	private WfTaskHandleService wfTaskHandleService;
	@Autowired
	private CompensateTaskService compensateTaskService;
	@Autowired
	private WfFlowQueryService wfFlowQueryService;
	@Autowired
	OtherInterfaceAsyncService otherInterfaceAsyncService;
	@Autowired
	BaseDaoService baseDaoService;
	@Override
	public BigDecimal save(PrpLcancelTraceVo prpLcancelTraceVo,SysUserVo userVo,String currentNode) {
		
		PrpLcancelTrace prpLcancelTrace = new PrpLcancelTrace();
		Beans.copy().from(prpLcancelTraceVo).excludeNull().to(prpLcancelTrace);
		String dealReasoon = prpLcancelTraceVo.getDealReasoon();
		/*
		 * if(dealReasoon.equals("1")||dealReasoon.equals("2")){ prpLcancelTrace.setTextType("01".trim()); }else{ prpLcancelTrace.setTextType("02".trim()); }
		 */
		prpLcancelTrace.setTextType("01".trim());
		prpLcancelTrace.setApplyDate(new Date());
		prpLcancelTrace.setApplyUserCode(userVo.getUserCode());
		prpLcancelTrace.setOperaToRCode(userVo.getUserCode());
		prpLcancelTrace.setInputTime(new Date());
		prpLcancelTrace.setMakeCom(userVo.getComCode());
		prpLcancelTrace.setComCode(userVo.getComCode());
		prpLcancelTrace.setValidFlag("1");
		prpLcancelTrace.setStatus("0");
		prpLcancelTrace.setInsertTimeForHis(new Date());
		prpLcancelTrace.setOperateTimeForHis(new Date());
		prpLcancelTrace.setFlags("0");// 提交标志
		
		databaseDao.save(PrpLcancelTrace.class,prpLcancelTrace);
		BigDecimal Id = prpLcancelTrace.getId();
		// 根据保存报案、立案注销（恢复）轨迹表返回的id保存立案注销/拒赔申请/审核意见表PrpLrejectClaimText

		PrpLrejectClaimText prpLrejectClaimText = new PrpLrejectClaimText();
		prpLrejectClaimText.setClaimNo(prpLcancelTraceVo.getClaimNo());
		prpLrejectClaimText.setRegistNo(prpLcancelTraceVo.getRegistNo());
		prpLrejectClaimText.setPrplcancelTraceId(Id);
		// prpLrejectClaimText.setOperatorNode("");//
		prpLrejectClaimText.setOperatorCode(userVo.getUserCode());
		prpLrejectClaimText.setOperatorName(userVo.getUserName());
		prpLrejectClaimText.setComCode(userVo.getComCode());
		prpLrejectClaimText.setOperateDate(new Date());
		prpLrejectClaimText.setOpinionCode("");// 意见代码
		if(dealReasoon.equals("1")||dealReasoon.equals("2")){
			prpLrejectClaimText.setOpinionName("01-案件注销 ");
			prpLrejectClaimText.setOpinionCode("01");// 意见代码
		}else{
			prpLrejectClaimText.setOpinionName("02-案件拒赔 ");
			prpLrejectClaimText.setOpinionCode("02");// 意见代码
		}
		prpLrejectClaimText.setValidFlag("1");
		/*
		 * prpLrejectClaimText.setStatus("0");//提交标志
		 */prpLrejectClaimText.setInsertTimeForHis(new Date());
		prpLrejectClaimText.setOperateTimeForHis(new Date());
		prpLrejectClaimText.setStationName(currentNode);
		String applyReason = CodeTranUtil.transCode("ApplyReason", prpLcancelTraceVo.getApplyReason());
		prpLrejectClaimText.setDescription(applyReason);
		databaseDao.save(PrpLrejectClaimText.class,prpLrejectClaimText);
		return prpLrejectClaimText.getId();
	}

	@Override
	public BigDecimal saveTrace(PrpLcancelTraceVo prpLcancelTraceVo,SysUserVo userVo,String currentNode) {

		PrpLcancelTrace prpLcancelTrace = new PrpLcancelTrace();
		Beans.copy().from(prpLcancelTraceVo).excludeNull().to(prpLcancelTrace);
		String dealReasoon = prpLcancelTraceVo.getDealReasoon();

		databaseDao.save(PrpLcancelTrace.class,prpLcancelTrace);
		BigDecimal Id = prpLcancelTrace.getId();
		// 根据保存报案、立案注销（恢复）轨迹表返回的id保存立案注销/拒赔申请/审核意见表PrpLrejectClaimText
		PrpLrejectClaimText prpLrejectClaimText = new PrpLrejectClaimText();
		prpLrejectClaimText.setClaimNo(prpLcancelTraceVo.getClaimNo());
		prpLrejectClaimText.setRegistNo(prpLcancelTraceVo.getRegistNo());
		prpLrejectClaimText.setPrplcancelTraceId(Id);
		// prpLrejectClaimText.setOperatorNode("");//
		prpLrejectClaimText.setOperatorCode(userVo.getUserCode());
		prpLrejectClaimText.setOperatorName(userVo.getUserName());
		prpLrejectClaimText.setComCode(userVo.getComCode());
		prpLrejectClaimText.setOperateDate(new Date());
		prpLrejectClaimText.setOpinionCode("");// 意见代码
		if(dealReasoon.equals("1")||dealReasoon.equals("2")){
			prpLrejectClaimText.setOpinionName("01-案件注销 ");
			prpLrejectClaimText.setOpinionCode("01");// 意见代码
		}else{
			prpLrejectClaimText.setOpinionName("02-案件拒赔 ");
			prpLrejectClaimText.setOpinionCode("02");// 意见代码
		}
		prpLrejectClaimText.setValidFlag("1");
		/*
		 * prpLrejectClaimText.setStatus("0");//提交标志
		 */prpLrejectClaimText.setInsertTimeForHis(new Date());
		prpLrejectClaimText.setOperateTimeForHis(new Date());
		prpLrejectClaimText.setStationName(currentNode);
		String applyReason = CodeTranUtil.transCode("ApplyReason", prpLcancelTraceVo.getApplyReason());
		prpLrejectClaimText.setDescription(applyReason);
		databaseDao.save(PrpLrejectClaimText.class,prpLrejectClaimText);
		return prpLrejectClaimText.getId();
	}

	@Override
	public BigDecimal updates(PrpLcancelTraceVo prpLcancelTraceVo,SysUserVo userVo,String currentNode) {

		PrpLcancelTrace prpLcancelTrace = new PrpLcancelTrace();
		Beans.copy().from(prpLcancelTraceVo).excludeNull().to(prpLcancelTrace);
		String dealReasoon = prpLcancelTraceVo.getDealReasoon();
		/*
		 * if (dealReasoon.equals("1") || dealReasoon.equals("2")) { prpLcancelTrace.setTextType("01".trim()); } else { prpLcancelTrace.setTextType("02".trim()); }
		 */
		prpLcancelTrace.setTextType("01".trim());
		prpLcancelTrace.setApplyDate(new Date());
		prpLcancelTrace.setApplyUserCode(userVo.getUserCode());
		prpLcancelTrace.setOperaToRCode(userVo.getUserCode());
		prpLcancelTrace.setInputTime(new Date());
		prpLcancelTrace.setMakeCom(userVo.getComCode());
		prpLcancelTrace.setComCode(userVo.getComCode());
		prpLcancelTrace.setValidFlag("1");
		prpLcancelTrace.setStatus("0");
		prpLcancelTrace.setInsertTimeForHis(new Date());
		prpLcancelTrace.setOperateTimeForHis(new Date());
		prpLcancelTrace.setFlags("0");// 提交标志
		prpLcancelTrace.setId(prpLcancelTraceVo.getId());
	
		databaseDao.update(PrpLcancelTrace.class,prpLcancelTrace);
		BigDecimal Id = prpLcancelTrace.getId();
		// 根据保存报案、立案注销（恢复）轨迹表返回的id保存立案注销/拒赔申请/审核意见表PrpLrejectClaimText

		PrpLrejectClaimText prpLrejectClaimText = new PrpLrejectClaimText();
		prpLrejectClaimText.setClaimNo(prpLcancelTraceVo.getClaimNo());
		prpLrejectClaimText.setRegistNo(prpLcancelTraceVo.getRegistNo());
		prpLrejectClaimText.setPrplcancelTraceId(Id);
		// prpLrejectClaimText.setOperatorNode("");//
		prpLrejectClaimText.setOperatorCode(userVo.getUserCode());
		prpLrejectClaimText.setOperatorName(userVo.getUserName());
		prpLrejectClaimText.setComCode(userVo.getComCode());
		prpLrejectClaimText.setOperateDate(new Date());
		prpLrejectClaimText.setOpinionCode("");// 意见代码
		if(dealReasoon.equals("1")||dealReasoon.equals("2")){
			prpLrejectClaimText.setOpinionName("01-案件注销 ");
			prpLrejectClaimText.setOpinionCode("01");// 意见代码
		}else{
			prpLrejectClaimText.setOpinionName("02-案件拒赔 ");
			prpLrejectClaimText.setOpinionCode("02");// 意见代码
		}
		prpLrejectClaimText.setValidFlag("1");
		/*
		 * prpLrejectClaimText.setStatus("0");//提交标志
		 */prpLrejectClaimText.setInsertTimeForHis(new Date());
		prpLrejectClaimText.setOperateTimeForHis(new Date());
		prpLrejectClaimText.setStationName(currentNode);
		String applyReason = CodeTranUtil.transCode("ApplyReason", prpLcancelTraceVo.getApplyReason());
		prpLrejectClaimText.setDescription(applyReason);
		databaseDao.save(PrpLrejectClaimText.class,prpLrejectClaimText);
		return prpLrejectClaimText.getId();
	}

	// 暂存
	@Override
	public BigDecimal zhanCun(PrpLcancelTraceVo prpLcancelTraceVo,SysUserVo userVo) {

		PrpLcancelTrace prpLcancelTrace = new PrpLcancelTrace();
		Beans.copy().from(prpLcancelTraceVo).excludeNull().to(prpLcancelTrace);
		String dealReasoon = prpLcancelTraceVo.getDealReasoon();
		/*if(dealReasoon.equals("1")||dealReasoon.equals("2")){
			prpLcancelTrace.setTextType("01".trim());
		}else{
			prpLcancelTrace.setTextType("02".trim());
		}*/
		prpLcancelTrace.setTextType("01".trim());
		prpLcancelTrace.setApplyDate(new Date());
		prpLcancelTrace.setApplyUserCode(userVo.getUserCode());
		prpLcancelTrace.setOperaToRCode(userVo.getUserCode());
		prpLcancelTrace.setInputTime(new Date());
		prpLcancelTrace.setMakeCom(userVo.getComCode());
		prpLcancelTrace.setComCode(userVo.getComCode());
		prpLcancelTrace.setValidFlag("1");
		prpLcancelTrace.setStatus("0");
		prpLcancelTrace.setInsertTimeForHis(new Date());
		prpLcancelTrace.setOperateTimeForHis(new Date());
		prpLcancelTrace.setFlags("1");// 暂存标志
		// Beans.copy().from(prpLcancelTraceVo).excludeNull().to(prpLcancelTrace);
		databaseDao.save(PrpLcancelTrace.class,prpLcancelTrace);
		/*
		 * BigDecimal Id = prpLcancelTrace.getId(); //根据保存报案、立案注销（恢复）轨迹表返回的id保存立案注销/拒赔申请/审核意见表PrpLrejectClaimText
		 * 
		 * PrpLrejectClaimText prpLrejectClaimText= new PrpLrejectClaimText(); prpLrejectClaimText.setClaimNo(prpLcancelTraceVo.getClaimNo());
		 * prpLrejectClaimText.setRegistNo(prpLcancelTraceVo.getRegistNo()); prpLrejectClaimText.setPrplcancelTraceId(Id); //prpLrejectClaimText.setOperatorNode("");//
		 * prpLrejectClaimText.setOperatorCode(SecurityUtils.getUserCode()); prpLrejectClaimText.setOperatorName(SecurityUtils.getUserName());
		 * prpLrejectClaimText.setComCode(SecurityUtils.getComCode()); prpLrejectClaimText.setComname(SecurityUtils.getComName()); prpLrejectClaimText.setOperateDate(new Date());
		 * prpLrejectClaimText.setOpinionCode("");//意见代码 if (dealReasoon.equals("1") || dealReasoon.equals("2")) { prpLrejectClaimText.setOpinionName("01-案件注销 ");
		 * prpLrejectClaimText.setOpinionCode("01");//意见代码 } else { prpLrejectClaimText.setOpinionName("02-案件拒赔 "); prpLrejectClaimText.setOpinionCode("02");//意见代码 }
		 * prpLrejectClaimText.setValidFlag("1"); //prpLrejectClaimText.setStatus("1");//暂存标志 prpLrejectClaimText.setInsertTimeForHis(new Date()); prpLrejectClaimText.setOperateTimeForHis(new Date());
		 * System.out.println(prpLrejectClaimText.getOperateTimeForHis()+"321321==="); databaseDao.save(PrpLrejectClaimText.class, prpLrejectClaimText);
		 */
		return prpLcancelTrace.getId();
	}

	// 根据立案号查询

	/* 
	 * @see ins.sino.claimcar.claim.services.ClaimCancelService#findId(java.lang.String, java.lang.String)
	 * @param riskCode
	 * @param claimNo
	 * @return
	 */
	@Override
	public BigDecimal findId(String riskCode,String claimNo) {
		SqlJoinUtils sqlUtil = new SqlJoinUtils();
		sqlUtil.append("SELECT ID from PrpLcancelTrace WHERE 1=1 ");
		sqlUtil.append("AND claimNo=");
		sqlUtil.append("'"+claimNo+"' ORDER BY inputTime Desc");
		String sql = sqlUtil.getSql();
		Object[] values = sqlUtil.getParamValues();
		// 执行查询
		List<Object[]> objects = baseDaoService.getAllBySql(sql,values);
		if(objects.size() > 0){
			Object id = objects.get(0);
			return (BigDecimal) id;
		}else{
			return null;
		}	
	}

	// 更新申请原因
	@Override
	public BigDecimal savePrpLrejectClaimText(PrpLcancelTraceVo prpLcancelTraceVo,SysUserVo userVo) {

		/* PrpLcancelTrace prpLcancelTrace= new PrpLcancelTrace(); */

		// String dealReasoon = prpLcancelTraceVo.getDealReasoon();
		BigDecimal Id = prpLcancelTraceVo.getId();
		// 根据保存报案、立案注销（恢复）轨迹表返回的id保存立案注销/拒赔申请/审核意见表PrpLrejectClaimText

		PrpLrejectClaimText prpLrejectClaimText = new PrpLrejectClaimText();
		prpLrejectClaimText.setClaimNo(prpLcancelTraceVo.getClaimNo());
		prpLrejectClaimText.setRegistNo(prpLcancelTraceVo.getRegistNo());
		prpLrejectClaimText.setPrplcancelTraceId(Id);
		// prpLrejectClaimText.setOperatorNode("");//
		prpLrejectClaimText.setOperatorCode(userVo.getUserCode());
		prpLrejectClaimText.setOperatorName(userVo.getUserName());
		prpLrejectClaimText.setComCode(userVo.getComCode());
		prpLrejectClaimText.setOperateDate(new Date());
		prpLrejectClaimText.setOpinionCode("");// 意见代码
		prpLcancelTraceVo.getOpinionCode();

		PrpLcancelTrace prpLcancelTrace = databaseDao.findByPK(PrpLcancelTrace.class,Id);

		if(prpLcancelTraceVo.getOpinionCode().equals("03")){
			prpLrejectClaimText.setOpinionName("03-提交上级");
			prpLcancelTrace.setStatus("0");
		}else if(prpLcancelTraceVo.getOpinionCode().equals("04")){
			prpLrejectClaimText.setOpinionName("04-审核退回");
			prpLcancelTrace.setStatus("2");
		}else if(prpLcancelTraceVo.getOpinionCode().equals("05")){
			prpLcancelTrace.setStatus("1");
			prpLrejectClaimText.setOpinionName("05-审核通过");
		}

		prpLrejectClaimText.setDescription(prpLcancelTraceVo.getDescription());
		/*
		 * if (dealReasoon.equals("1") || dealReasoon.equals("2")) { prpLrejectClaimText.setOpinionName("01-案件注销 "); } else { prpLrejectClaimText.setOpinionName("02-案件拒赔 "); }
		 */
		prpLrejectClaimText.setValidFlag("1");
		prpLrejectClaimText.setInsertTimeForHis(new Date());
		prpLrejectClaimText.setOperateTimeForHis(new Date());
		prpLrejectClaimText.setOpinionName(prpLcancelTraceVo.getOpinionName());

		// 增加岗位环节；退回代码的原因
		prpLrejectClaimText.setReasonCode(prpLcancelTraceVo.getReasonCode());
		prpLrejectClaimText.setStationName(prpLcancelTraceVo.getStationName());
		databaseDao.save(PrpLrejectClaimText.class,prpLrejectClaimText);
		return prpLrejectClaimText.getId();
	}

	/*
	 * //更新 public void updateLawSuit(PrpLLawSuitVo lawSuitVo){//更新保存医院信息 PrpLLawSuit lawSuit = new PrpLLawSuit(); Beans.copy().from(lawSuitVo).to(lawSuit); prpDHospital.setUpdateTime(new Date());
	 * prpDHospital.setUpdateCode(SecurityUtils.getUserCode()); databaseDao.update(PrpLLawSuit.class, lawSuit); }
	 * 
	 * public List<PrpLLawSuitVo> findByRegistNo(String RegistNo){ QueryRule queryRule=QueryRule.getInstance(); queryRule.addEqual("registNo", RegistNo); List<PrpLLawSuit> lawSuitList=
	 * databaseDao.findAll(PrpLLawSuit.class,queryRule); List<PrpLLawSuitVo> lawSuitVoList = new ArrayList<PrpLLawSuitVo>(); for(int i=0;i<lawSuitList.size();i++){ PrpLLawSuitVo
	 * lawSuitVo=Beans.copyDepth().from(lawSuitList.get(i)).to(PrpLLawSuitVo.class); lawSuitVoList.add(lawSuitVo); } return lawSuitVoList;
	 * 
	 * }
	 */

	// 根据轨迹表id查询

	@Override
	public List<PrpLrejectClaimTextVo> findById(BigDecimal id) {
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("prplcancelTraceId",id);
		queryRule.addDescOrder("operateDate");
		List<PrpLrejectClaimText> prpLrejectClaimTexts = databaseDao.findAll(PrpLrejectClaimText.class,queryRule);
		List<PrpLrejectClaimTextVo> prpListVos = new ArrayList<PrpLrejectClaimTextVo>();
		for(int i = 0; i<prpLrejectClaimTexts.size(); i++ ){
			PrpLrejectClaimTextVo prpLcancelTraceVo = Beans.copyDepth().from(prpLrejectClaimTexts.get(i))
					.to(PrpLrejectClaimTextVo.class);
			prpListVos.add(prpLcancelTraceVo);
		}

		return prpListVos;

	}

	//
	@Override
	public PrpLcancelTraceVo findByCancelTraceId(BigDecimal id) {
		PrpLcancelTrace prpLcancelTrace = databaseDao.findByPK(PrpLcancelTrace.class,id);

		PrpLcancelTraceVo prpLcancelTraceVo = Beans.copyDepth().from(prpLcancelTrace).to(PrpLcancelTraceVo.class);
		return prpLcancelTraceVo;

	}

	//
	@Override
	public PrpLrejectClaimTextVo findByCancelClaimTextId(BigDecimal id) {
		PrpLrejectClaimText prpLrejectClaimText = databaseDao.findByPK(PrpLrejectClaimText.class,id);
		if(prpLrejectClaimText==null){
			return null;
		}
		PrpLrejectClaimTextVo prpLrejectClaimTextVo = Beans.copyDepth().from(prpLrejectClaimText)
				.to(PrpLrejectClaimTextVo.class);
		return prpLrejectClaimTextVo;

	}

	// 更新
	@Override
	public void updateCancelTrace(PrpLcancelTraceVo prpLcancelTraceVo) {
		PrpLcancelTrace prpLcancelTrace = databaseDao.findByPK(PrpLcancelTrace.class,prpLcancelTraceVo.getId());
		prpLcancelTrace.setAandelCode(prpLcancelTraceVo.getDescription());
		prpLcancelTrace.setFlag(prpLcancelTraceVo.getFlag());
		prpLcancelTrace.setReasonCode(prpLcancelTraceVo.getReasonCode());
		databaseDao.update(PrpLcancelTrace.class,prpLcancelTrace);

	}

	// 公共按钮暂存处理更新
	@Override
	public void claimInitZhanC(PrpLcancelTraceVo prpLcancelTraceVo) {
		PrpLcancelTrace prpLcancelTrace = databaseDao.findByPK(PrpLcancelTrace.class,prpLcancelTraceVo.getId());
		prpLcancelTrace.setApplyReason(prpLcancelTraceVo.getApplyReason());
		prpLcancelTrace.setDealReasoon(prpLcancelTraceVo.getDealReasoon());
		prpLcancelTrace.setRemarks(prpLcancelTraceVo.getRemarks());
		if(prpLcancelTraceVo.getDealReasoon().equals("3")||prpLcancelTraceVo.getDealReasoon().equals("4"))
		{
			prpLcancelTrace.setSwindleReason(prpLcancelTraceVo.getSwindleReason());
			prpLcancelTrace.setSwindleType(prpLcancelTraceVo.getSwindleType());
			prpLcancelTrace.setSwindleSum(prpLcancelTraceVo.getSwindleSum());
		}
		;
		databaseDao.update(PrpLcancelTrace.class,prpLcancelTrace);

	}

	// 根据立案号查询

	@Override
	public PrpLcancelTraceVo findByClaimNo(String claimNo) {
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("claimNo",claimNo);
		queryRule.addDescOrder("inputTime");
		List<PrpLcancelTrace> prpLcancelTraceList = databaseDao.findAll(PrpLcancelTrace.class,queryRule);
		List<PrpLcancelTraceVo> prpLcancelTraceVos = new ArrayList<PrpLcancelTraceVo>();
		for(int i = 0; i<prpLcancelTraceList.size(); i++ ){
			PrpLcancelTraceVo prpLcancelTraceVo = Beans.copyDepth().from(prpLcancelTraceList.get(i))
					.to(PrpLcancelTraceVo.class);
			prpLcancelTraceVos.add(prpLcancelTraceVo);
		}
		if(prpLcancelTraceVos.size()>0){
			return prpLcancelTraceVos.get(0);

		}else{
			return null;
		}

	}

	// 再次申请更新状态位
	@Override
	public void updateCancelDTrace(PrpLcancelTraceVo prpLcancelTraceVo) {
		PrpLcancelTrace prpLcancelTrace = databaseDao.findByPK(PrpLcancelTrace.class,prpLcancelTraceVo.getId());
		/*
		 * prpLcancelTrace.setAandelCode(prpLcancelTraceVo.getDescription()); prpLcancelTrace.setFlag(prpLcancelTraceVo.getFlag());
		 */
		prpLcancelTrace.setFlags(prpLcancelTraceVo.getFlags());
		databaseDao.update(PrpLcancelTrace.class,prpLcancelTrace);

	}

	// 发起
	@Override
	public BigDecimal saveF(PrpLcancelTraceVo prpLcancelTraceVo,SysUserVo userVo) {

		PrpLcancelTrace prpLcancelTrace = new PrpLcancelTrace();
		Beans.copy().from(prpLcancelTraceVo).excludeNull().to(prpLcancelTrace);
		String dealReasoon = prpLcancelTraceVo.getDealReasoon();
		prpLcancelTrace.setTextType("01".trim());
		prpLcancelTrace.setApplyDate(new Date());
		prpLcancelTrace.setApplyUserCode(userVo.getUserCode());
		prpLcancelTrace.setOperaToRCode(userVo.getUserCode());
		prpLcancelTrace.setInputTime(new Date());
		prpLcancelTrace.setMakeCom(userVo.getComCode());
		prpLcancelTrace.setComCode(userVo.getComCode());
		prpLcancelTrace.setValidFlag("1");
		prpLcancelTrace.setStatus("0");
		prpLcancelTrace.setInsertTimeForHis(new Date());
		prpLcancelTrace.setOperateTimeForHis(new Date());
		prpLcancelTrace.setFlags(prpLcancelTraceVo.getFaQi());// 提交标志
		
		databaseDao.save(PrpLcancelTrace.class,prpLcancelTrace);
		BigDecimal Id = prpLcancelTrace.getId();
		// 根据保存报案、立案注销（恢复）轨迹表返回的id保存立案注销/拒赔申请/审核意见表PrpLrejectClaimText

		PrpLrejectClaimText prpLrejectClaimText = new PrpLrejectClaimText();
		prpLrejectClaimText.setClaimNo(prpLcancelTraceVo.getClaimNo());
		prpLrejectClaimText.setRegistNo(prpLcancelTraceVo.getRegistNo());
		prpLrejectClaimText.setPrplcancelTraceId(Id);
		// prpLrejectClaimText.setOperatorNode("");//
		prpLrejectClaimText.setOperatorCode(userVo.getUserCode());
		prpLrejectClaimText.setOperatorName(userVo.getUserName());
		prpLrejectClaimText.setComCode(userVo.getComCode());
		prpLrejectClaimText.setOperateDate(new Date());
		prpLrejectClaimText.setOpinionCode("");// 意见代码
		if(dealReasoon.equals("1")||dealReasoon.equals("2")){
			prpLrejectClaimText.setOpinionName("01-案件注销 ");
			prpLrejectClaimText.setOpinionCode("01");// 意见代码
		}else{
			prpLrejectClaimText.setOpinionName("02-案件拒赔 ");
			prpLrejectClaimText.setOpinionCode("02");// 意见代码
		}
		prpLrejectClaimText.setValidFlag("1");
		/*
		 * prpLrejectClaimText.setStatus("0");//提交标志
		 */prpLrejectClaimText.setInsertTimeForHis(new Date());
		prpLrejectClaimText.setOperateTimeForHis(new Date());
		/*prpLrejectClaimText.setStationName(prpLcancelTraceVo.getStationName());
		String applyReason = CodeTranUtil.transCode("ApplyReason", prpLcancelTraceVo.getApplyReason());
		prpLrejectClaimText.setDescription(applyReason);*/
		System.out.println(prpLrejectClaimText.getOperateTimeForHis()+"321321===");
		databaseDao.save(PrpLrejectClaimText.class,prpLrejectClaimText);
		return prpLrejectClaimText.getId();
	}

	@Override
	public BigDecimal updatesF(PrpLcancelTraceVo prpLcancelTraceVo,SysUserVo userVo) {

		PrpLcancelTrace prpLcancelTrace = new PrpLcancelTrace();

		String dealReasoon = prpLcancelTraceVo.getDealReasoon();
		System.out.println("==dealReasoon-=-="+dealReasoon);
		/*
		 * if (dealReasoon.equals("1") || dealReasoon.equals("2")) { prpLcancelTrace.setTextType("01".trim()); } else { prpLcancelTrace.setTextType("02".trim()); }
		 */
		prpLcancelTrace.setTextType("01".trim());
		prpLcancelTrace.setApplyDate(new Date());
		prpLcancelTrace.setApplyUserCode(userVo.getUserCode());
		prpLcancelTrace.setOperaToRCode(userVo.getUserCode());
		prpLcancelTrace.setInputTime(new Date());
		prpLcancelTrace.setMakeCom(userVo.getComCode());
		prpLcancelTrace.setComCode(userVo.getComCode());
		prpLcancelTrace.setValidFlag("1");
		prpLcancelTrace.setStatus("0");
		prpLcancelTrace.setInsertTimeForHis(new Date());
		prpLcancelTrace.setOperateTimeForHis(new Date());
		prpLcancelTrace.setFlags("0");// 提交标志
		prpLcancelTrace.setId(prpLcancelTraceVo.getId());
		Beans.copy().from(prpLcancelTraceVo).excludeNull().to(prpLcancelTrace);
		databaseDao.update(PrpLcancelTrace.class,prpLcancelTrace);
		BigDecimal Id = prpLcancelTrace.getId();
		// 根据保存报案、立案注销（恢复）轨迹表返回的id保存立案注销/拒赔申请/审核意见表PrpLrejectClaimText

		PrpLrejectClaimText prpLrejectClaimText = new PrpLrejectClaimText();
		prpLrejectClaimText.setClaimNo(prpLcancelTraceVo.getClaimNo());
		prpLrejectClaimText.setRegistNo(prpLcancelTraceVo.getRegistNo());
		prpLrejectClaimText.setPrplcancelTraceId(Id);
		// prpLrejectClaimText.setOperatorNode("");//
		prpLrejectClaimText.setOperatorCode(userVo.getUserCode());
		prpLrejectClaimText.setOperatorName(userVo.getUserName());
		prpLrejectClaimText.setComCode(userVo.getComCode());
		prpLrejectClaimText.setOperateDate(new Date());
		prpLrejectClaimText.setOpinionCode("");// 意见代码
		if(dealReasoon.equals("1")||dealReasoon.equals("2")){
			prpLrejectClaimText.setOpinionName("01-案件注销 ");
			prpLrejectClaimText.setOpinionCode("01");// 意见代码
		}else{
			prpLrejectClaimText.setOpinionName("02-案件拒赔 ");
			prpLrejectClaimText.setOpinionCode("02");// 意见代码
		}
		prpLrejectClaimText.setValidFlag("1");
		/*
		 * prpLrejectClaimText.setStatus("0");//提交标志
		 */prpLrejectClaimText.setInsertTimeForHis(new Date());
		prpLrejectClaimText.setOperateTimeForHis(new Date());
		databaseDao.save(PrpLrejectClaimText.class,prpLrejectClaimText);
		return prpLrejectClaimText.getId();
	}

	/**
	 * @param claimNo
	 * @param riskCode
	 * @modified: ☆Luwei(2016年6月8日 下午3:47:56): <br>
	 */
	@Override
	public List<PrpLcancelTraceVo> queryCancelTrace(String claimNo) {
		List<PrpLcancelTraceVo> cancelTraceVo = null;
		QueryRule qr = QueryRule.getInstance();
		qr.addEqual("claimNo",claimNo);
		List<PrpLcancelTrace> cancelPos = databaseDao.findAll(PrpLcancelTrace.class,qr);
		if(cancelPos!=null&&cancelPos.size()>0){
			cancelTraceVo = new ArrayList<PrpLcancelTraceVo>();
			cancelTraceVo = Beans.copyDepth().from(cancelPos).toList(PrpLcancelTraceVo.class);
		}
		return cancelTraceVo;
	}

	/**
	 * 核赔回写
	 * @param cancelTraceVo
	 * @modified: ☆Luwei(2016年6月8日 下午3:48:07): <br>
	 */
	@Override
	public void vclaimBackWrite(List<PrpLcancelTraceVo> cancelTraceVos) {
		for(PrpLcancelTraceVo cancelTraceVo:cancelTraceVos){
			PrpLcancelTrace cancelTrace = databaseDao.findByPK
					(PrpLcancelTrace.class,cancelTraceVo.getId());
			Beans.copy().from(cancelTraceVo).includeNull().to(cancelTrace);
			databaseDao.update(PrpLcancelTrace.class,cancelTrace);
		}
	}

	// public List<PrpLrejectClaimTextVo> queryRejectClaimText(String claimNo){
	// List<PrpLrejectClaimTextVo> rejectClaimTextVo = null;
	// QueryRule qr = QueryRule.getInstance();
	// qr.addEqual("claimNo",claimNo);
	// List<PrpLrejectClaimText> rejectClaimPos =
	// databaseDao.findAll(PrpLrejectClaimText.class,qr);
	// if(rejectClaimPos!=null&&rejectClaimPos.size()>0){
	// rejectClaimTextVo = new ArrayList<PrpLrejectClaimTextVo>();
	// rejectClaimTextVo = Beans.copyDepth().from(rejectClaimPos).toList(PrpLrejectClaimTextVo.class);
	// }
	// return rejectClaimTextVo;
	// }
	//
	// public void rejectClaimBackWrite(List<PrpLrejectClaimTextVo> rejectClaimTextVos) {
	// for(PrpLrejectClaimTextVo rejectClaimTextVo : rejectClaimTextVos){
	// PrpLrejectClaimText rejectClaimText = databaseDao.findByPK
	// (PrpLrejectClaimText.class,rejectClaimTextVo.getId());
	// Beans.copy().from(rejectClaimTextVo).includeNull().to(rejectClaimText);
	// databaseDao.update(PrpLcancelTrace.class,rejectClaimText);
	// }
	// }
	/* 
	 * @see ins.sino.claimcar.claim.services.ClaimCancelService#validCheckClaim(java.lang.String)
	 * @param registNo
	 * @return
	 */
	@Override
	public String validCheckClaim(String registNo,String claimNo) {
		// 查询报案号下预付表核赔状态 预算主表同为PrpLCompensateVo-CompensateType为Y
				QueryRule qr = QueryRule.getInstance();
				qr.addEqual("registNo", registNo);
		qr.addEqual("compensateType","Y");// 预付
		qr.addEqual("underwriteFlag","1");// 核赔通过
				List<PrpLCompensate> prePayList = databaseDao.findAll(PrpLCompensate.class, qr);
				if(prePayList.size() > 0 ){
					for(PrpLCompensate po : prePayList){
						if(claimNo.equals(po.getClaimNo())){
					return "有核赔通过的预付计算书，不能进行立案注销/拒赔的申请！";
						}
					}
					
				}
		// 垫付任务核赔通过or未通过
				int flags = 0;
				int compensateFlags = 0;
				List<PrpLPadPayMainVo> padPays = padPayService.findPadPayMainByRegistNo(registNo);
				for (PrpLPadPayMainVo padPay : padPays) {
					if(padPay.getUnderwriteFlag()!=null){
						if(padPay.getUnderwriteFlag().equals("1") && claimNo.equals(padPay.getClaimNo())){
							flags = flags + 1;
						}
					}
				}
				if(flags > 0){
			return "有核赔通过的垫付计算书，不能进行立案注销/拒赔的申请!";
				}
				
		// 有核赔通过的实赔计算书，不能进行立案注销/拒赔的申请
				QueryRule qr1 = QueryRule.getInstance();
				qr1.addEqual("registNo", registNo);
		qr1.addEqual("underwriteFlag","1");// 核赔通过
		qr1.addEqual("compensateType","N");// 实赔
				List<PrpLCompensate> CompensateList = databaseDao.findAll(PrpLCompensate.class, qr1);
				
				if(CompensateList.size() > 0){
					for(PrpLCompensate po : CompensateList){
						if(claimNo.equals(po.getClaimNo())){
					return "有核赔通过的实赔计算书，不能进行立案注销/拒赔的申请!";
						}
					}
					
				}
				
		// 工作流有理算任务，且业务表存在非注销标志的数据不能注销
				boolean state = wfTaskHandleService.existTaskByNode(registNo, FlowNode.Compe.toString());
		if(state){// 工作流有理算数据
			// 查询理算数据
					List<PrpLCompensateVo> prpLCompensateVos = compensateTaskService.queryCompensateByRegistNo(registNo);
					if(prpLCompensateVos != null && prpLCompensateVos.size() > 0){
						for(PrpLCompensateVo vo : prpLCompensateVos){
							if((!CodeConstants.UnderWriteFlag.CANCELFLAG.equals(vo.getUnderwriteFlag())) && claimNo.equals(vo.getClaimNo())){
						return "存在理算任务，不能进行立案注销/拒赔的申请!";
							}
						}
			}else{// 工作流有理算任务，业务表没数据，工作流理算任务的状态为非注销。
						PrpLClaimVo claimVo = claimTaskService.findClaimVoByClaimNo(claimNo);
						if("11".equals(claimVo.getRiskCode().substring(0, 2))){
							List<PrpLWfTaskVo> prpLWfTaskVo = wfFlowQueryService.findPrpWfTaskVo(registNo, FlowNode.Compe.toString(), FlowNode.CompeCI.toString());
							if(prpLWfTaskVo.size() > 0 && prpLWfTaskVo != null){
								for(PrpLWfTaskVo vo : prpLWfTaskVo){
							if( !( CodeConstants.WorkStatus.CANCEL.equals(vo.getWorkStatus())||CodeConstants.WorkStatus.CHANGE.equals(vo
									.getWorkStatus())||CodeConstants.WorkStatus.TURN.equals(vo.getWorkStatus()) )){
								return "存在理算任务，不能进行立案注销/拒赔的申请!";
									}
								}
							}
						}else{
							List<PrpLWfTaskVo> prpLWfTaskVo = wfFlowQueryService.findPrpWfTaskVo(registNo, FlowNode.Compe.toString(), FlowNode.CompeBI.toString());
							if(prpLWfTaskVo.size() > 0 && prpLWfTaskVo != null){
								for(PrpLWfTaskVo vo : prpLWfTaskVo){
							if( !( CodeConstants.WorkStatus.CANCEL.equals(vo.getWorkStatus())||CodeConstants.WorkStatus.CHANGE.equals(vo
									.getWorkStatus())||CodeConstants.WorkStatus.TURN.equals(vo.getWorkStatus()) )){
								return "存在理算任务，不能进行立案注销/拒赔的申请!";
									}
								}
							}
						}
						
					}
				}
				return "1";

	}
	
	// 发起注销
	@Override
	public BigDecimal cancelUpdates(PrpLcancelTraceVo prpLcancelTraceVo,SysUserVo userVo) {

		PrpLcancelTrace prpLcancelTrace = new PrpLcancelTrace();
		Beans.copy().from(prpLcancelTraceVo).excludeNull().to(prpLcancelTrace);
		String dealReasoon = prpLcancelTraceVo.getDealReasoon();
		/*
		 * if (dealReasoon.equals("1") || dealReasoon.equals("2")) { prpLcancelTrace.setTextType("01".trim()); } else { prpLcancelTrace.setTextType("02".trim()); }
		 */
		prpLcancelTrace.setTextType("01".trim());
		prpLcancelTrace.setApplyDate(new Date());
		prpLcancelTrace.setApplyUserCode(userVo.getUserCode());
		prpLcancelTrace.setOperaToRCode(userVo.getUserCode());
		prpLcancelTrace.setInputTime(new Date());
		prpLcancelTrace.setMakeCom(userVo.getComCode());
		prpLcancelTrace.setComCode(userVo.getComCode());
		prpLcancelTrace.setValidFlag("1");
		prpLcancelTrace.setStatus("9");// 发起注销标志
		prpLcancelTrace.setInsertTimeForHis(new Date());
		prpLcancelTrace.setOperateTimeForHis(new Date());
		prpLcancelTrace.setFlags("11");// 发起注销标志
		prpLcancelTrace.setId(prpLcancelTraceVo.getId());
	
		databaseDao.update(PrpLcancelTrace.class,prpLcancelTrace);
		BigDecimal Id = prpLcancelTrace.getId();
		
		return Id;
	}

	@Override
	public void sendToClaimPlatform(PrpLcancelTraceVo prpLcancelTraceVo) {
		String claimNo = prpLcancelTraceVo.getClaimNo();
		String registNo = prpLcancelTraceVo.getRegistNo();
		
		PrpLClaimVo claimVo = claimTaskService.findClaimVoByClaimNo(claimNo);
		String cause = prpLcancelTraceVo.getApplyReason();// 注销，拒赔原因
		// ValidFlag:1-正常，0-注销，2-拒赔 ,
		String cancelCause = "21";// 平台：21-保险公司注销案件，
		if("2".equals(claimVo.getValidFlag())){
			cancelCause = "22";// 22-保险公司拒赔
		}else if(StringUtils.isNotBlank(cause)&&cause.startsWith("0")){// 除外责任
			cancelCause = "02";
		}else if(StringUtils.isNotBlank(cause)&&cause.startsWith("9")){// 0-注销
			cancelCause = "99";// 其他
		}
		try{
			String reqType = Risk.DQZ.equals(prpLcancelTraceVo.getRiskCode()) ? "11" : "12";
			String comCode = policyViewService.findPolicyComCode(registNo,reqType);

			if(comCode.startsWith("22")){// 上海平台
				sendCancelToSH.sendClaimCancelTo_SH(claimNo);
			}else{// 全国平台
				Double sum = DataUtils.NullToZero(prpLcancelTraceVo.getSwindleSum()).doubleValue();
				sendCancelToAll.sendClaimCancelToPlatform(claimVo.getRiskCode(),registNo,cancelCause,sum);
			}
		}catch(Exception e){}

		// 再保处理注销业务 niuqian businessType=1
		try{
			ClaimInterfaceLogVo claimInterfaceLogVo = new ClaimInterfaceLogVo(); // 填写日志表
			claimInterfaceLogVo.setClaimNo(claimNo);
			claimInterfaceLogVo.setRegistNo(registNo);
			claimInterfaceLogVo.setComCode(claimVo.getComCode()!=null?claimVo.getComCode():"0000000000");
			claimInterfaceLogVo.setCreateUser(claimVo.getCreateUser()!=null?claimVo.getCreateUser():"0000000000");
			claimInterfaceLogVo.setCreateTime(new Date());
			claimInterfaceLogVo.setOperateNode(FlowNode.Cancel.getName());
			
			otherInterfaceAsyncService.TransDataForReinsCaseVo("1",claimVo,claimInterfaceLogVo);
		}catch(Exception e){
			e.printStackTrace();
			// throw new IllegalArgumentException("再保处理注销业务 业务失败！businessType=1 <br/>"+e);
		}
	}

	@Override
	public PrpLcancelTraceVo findPrpLcancelTraceVo(String riskCode,
			String claimNo, String textType) {
		
		SqlJoinUtils sqlUtil = new SqlJoinUtils();
		sqlUtil.append("SELECT ID from PrpLcancelTrace WHERE 1=1 ");
		sqlUtil.append("AND claimNo=");
		sqlUtil.append("'"+claimNo+"'"+" AND textType = '"+textType+"'" +" ORDER BY inputTime Desc");
		String sql = sqlUtil.getSql();
		Object[] values = sqlUtil.getParamValues();
		// 执行查询
		List<Object[]> objects = baseDaoService.getAllBySql(sql,values);
		if(objects.size() > 0){
			Object id = objects.get(0);
			PrpLcancelTrace po = databaseDao.findByPK(PrpLcancelTrace.class, (BigDecimal) id);
			PrpLcancelTraceVo prpLcancelTraceVo = Beans.copyDepth().from(po).to(PrpLcancelTraceVo.class);
			return prpLcancelTraceVo;
		}else{
			return null;
		}	
	}

    @Override
    public List<PrpLrejectClaimTextVo> findByOpinionCode(String opinionCode,String claimNo) {
        QueryRule queryRule = QueryRule.getInstance();
        queryRule.addEqual("claimNo",claimNo);
        queryRule.addEqual("opinionCode", opinionCode);
        queryRule.addDescOrder("operateDate");
        queryRule.addDescOrder("id");
        List<PrpLrejectClaimText> prpLrejectClaimTextList = databaseDao.findAll(PrpLrejectClaimText.class,queryRule);
        List<PrpLrejectClaimTextVo> prpLrejectClaimTextVoList = new ArrayList<PrpLrejectClaimTextVo>();
        if(prpLrejectClaimTextList != null && prpLrejectClaimTextList.size() > 0 ){
            prpLrejectClaimTextVoList = Beans.copyDepth().from(prpLrejectClaimTextList).toList(PrpLrejectClaimTextVo.class);
        }
        return prpLrejectClaimTextVoList;
    }
}
