/******************************************************************************
 * CREATETIME : 2016年8月16日 下午4:51:42
 ******************************************************************************/
package ins.sino.claimcar.base.service;

import ins.framework.common.ResultPage;
import ins.framework.dao.database.DatabaseDao;
import ins.framework.dao.database.support.Page;
import ins.framework.dao.database.support.QueryRule;
import ins.framework.utils.Beans;
import ins.platform.common.service.facade.BaseDaoService;
import ins.platform.common.service.facade.BillNoService;
import ins.platform.common.service.facade.CodeTranService;
import ins.platform.utils.DateUtils;
import ins.platform.utils.SqlJoinUtils;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.CodeConstants.AssessorUnderWriteFlag;
import ins.sino.claimcar.CodeConstants.TaskType;
import ins.sino.claimcar.base.po.PrpDAccRollBackAccount;
import ins.sino.claimcar.base.po.PrpLAcheck;
import ins.sino.claimcar.base.po.PrpLAssessor;
import ins.sino.claimcar.base.po.PrpLAssessorFee;
import ins.sino.claimcar.base.po.PrpLAssessorMain;
import ins.sino.claimcar.base.po.PrplInterrmAudit;
import ins.sino.claimcar.check.service.CheckHandleService;
import ins.sino.claimcar.claim.service.ClaimTaskService;
import ins.sino.claimcar.claim.vo.PrpLClaimVo;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.service.WfTaskHandleService;
import ins.sino.claimcar.flow.vo.PrpLWfTaskQueryVo;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.flow.vo.WfTaskSubmitVo;
import ins.sino.claimcar.losscar.service.DeflossHandleService;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarMainVo;
import ins.sino.claimcar.lossperson.service.PersTraceHandleService;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceMainVo;
import ins.sino.claimcar.lossprop.service.PropLossHandleService;
import ins.sino.claimcar.lossprop.vo.PrpLdlossPropMainVo;
import ins.sino.claimcar.manager.service.ManagerService;
import ins.sino.claimcar.manager.vo.PrpdIntermBankVo;
import ins.sino.claimcar.manager.vo.PrpdIntermMainVo;
import ins.sino.claimcar.other.service.AccountQueryService;
import ins.sino.claimcar.other.service.AcheckTaskService;
import ins.sino.claimcar.other.service.AssessorService;
import ins.sino.claimcar.other.service.AssessorsTaskService;
import ins.sino.claimcar.other.vo.AssessorQueryResultVo;
import ins.sino.claimcar.other.vo.AssessorQueryVo;
import ins.sino.claimcar.other.vo.PrpDAccRollBackAccountVo;
import ins.sino.claimcar.other.vo.PrpLAcheckVo;
import ins.sino.claimcar.other.vo.PrpLAssessorFeeVo;
import ins.sino.claimcar.other.vo.PrpLAssessorMainVo;
import ins.sino.claimcar.other.vo.PrpLAssessorVo;
import ins.sino.claimcar.other.vo.PrpLPayBankVo;
import ins.sino.claimcar.other.vo.PrplInterrmAuditVo;
import ins.sino.claimcar.other.vo.PrplQuartzLogVo;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.ws.rs.Path;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;

/**
 * @author ★XMSH
 */
@Service(protocol = { "dubbo" }, validation = "true", registry = { "default" }, timeout = 1000000)
@Path("assessorService")
public class AssessorServiceImpl implements AssessorService {

	private static Logger logger = LoggerFactory
			.getLogger(AssessorServiceImpl.class);

	@Autowired
	DatabaseDao databaseDao;
	@Autowired
	BaseDaoService baseDaoService;
	@Autowired
	WfTaskHandleService wfTaskHandleService;
	@Autowired
	BillNoService billNoService;
	@Autowired
	ClaimTaskService claimTaskService;
	@Autowired
	private ManagerService managerService;
	@Autowired
	private CodeTranService codeTranService;
	@Autowired
	private PolicyViewService policyViewService;
	@Autowired
	private AccountQueryService accountQueryService;
	@Autowired
	private RegistQueryService registQueryService;
	



	/*
	 * @see
	 * ins.sino.claimcar.base.service.AssessorService#findAssessorVoByPk(java
	 * .lang.Long)
	 * 
	 * @param id
	 * 
	 * @return
	 */
	@Override
	public PrpLAssessorVo findAssessorVoByPk(Long id) {
		PrpLAssessorVo assessorVo = null;
		PrpLAssessor assessor = databaseDao.findByPK(PrpLAssessor.class, id);
		if (assessor != null) {
			assessorVo = new PrpLAssessorVo();
			Beans.copy().from(assessor).to(assessorVo);
		}
		return assessorVo;
	}
	
	

	/*
	 * @see
	 * ins.sino.claimcar.base.service.AssessorService#findAssessorMainVoByTaskNo
	 * (java.lang.String)
	 * 
	 * @param taskNo
	 * 
	 * @return
	 */
	@Override
	public PrpLAssessorMainVo findAssessorMainVoByTaskNo(String taskNo) {
		QueryRule qr = QueryRule.getInstance();
		qr.addEqual("taskId", taskNo);
		PrpLAssessorMainVo assessorMainVo = null;
		PrpLAssessorMain assessorMain = databaseDao.findUnique(
				PrpLAssessorMain.class, qr);
		if (assessorMain != null) {
			assessorMainVo = Beans.copyDepth().from(assessorMain)
					.to(PrpLAssessorMainVo.class);
		}
		return assessorMainVo;
	}

	/*
	 * @see
	 * ins.sino.claimcar.base.service.AssessorService#saveOrUpdatePrpLAssessor
	 * (ins.sino.claimcar.base.vo.PrpLAssessorVo)
	 * 
	 * @param assessorVo
	 */
	@Override
	public void saveOrUpdatePrpLAssessor(PrpLAssessorVo assessorVo,
			SysUserVo userVo) {
		PrpLAssessor assessor = null;
		Date now = new Date();
		if (assessorVo.getId() == null) {// 新增
			assessor = new PrpLAssessor();
			Beans.copy().from(assessorVo).to(assessor);
			assessor.setCreateTime(now);
			assessor.setCreateUser(userVo.getUserCode());
			assessor.setUpdateTime(now);
			assessor.setUpdateUser(userVo.getUserCode());
		} else {// 更新
			assessor = databaseDao.findByPK(PrpLAssessor.class,
					assessorVo.getId());
			Beans.copy().excludeNull().from(assessorVo).to(assessor);
			assessor.setUpdateTime(now);
			assessor.setUpdateUser(userVo.getUserCode());
		}

		databaseDao.save(PrpLAssessor.class, assessor);
	}

	/*
	 * @see
	 * ins.sino.claimcar.base.service.AssessorService#findAssessorByLossId(java
	 * .lang.String, java.lang.String, java.lang.Integer, java.lang.String)
	 * 
	 * @param registNo
	 * 
	 * @param taskType
	 * 
	 * @param serialNo
	 * 
	 * @param intermcode
	 * 
	 * @return
	 */
	@Override
	public PrpLAssessorVo findAssessorByLossId(String registNo,
			String taskType, Integer serialNo, String intermcode) {
		PrpLAssessorVo assessorVo = null;
		QueryRule qr = QueryRule.getInstance();
		qr.addEqual("registNo", registNo);
		qr.addEqual("taskType", taskType);
		qr.addEqual("intermcode", intermcode);
		qr.addNotEqual("underWriteFlag","7");
		if (serialNo != null) {
			qr.addEqual("serialNo", serialNo.toString());
		}

		List<PrpLAssessor> assessorList = databaseDao.findAll(
				PrpLAssessor.class, qr);
		if (assessorList != null && !assessorList.isEmpty()) {
			List<PrpLAssessorVo> assessorVoList = Beans.copyDepth()
					.from(assessorList).toList(PrpLAssessorVo.class);
			assessorVo = assessorVoList.get(0);
		}

		return assessorVo;
	}

	@Override
	public PrpLAssessorVo findAssessorByLossId(String registNo, String taskType, String intermcode) {
		PrpLAssessorVo assessorVo = null;
		QueryRule qr = QueryRule.getInstance();
		qr.addEqual("registNo", registNo);
		qr.addEqual("taskType", taskType);
		qr.addEqual("intermcode", intermcode);

		List<PrpLAssessor> assessorList = databaseDao.findAll(
				PrpLAssessor.class, qr);
		if (assessorList != null && !assessorList.isEmpty()) {
			List<PrpLAssessorVo> assessorVoList = Beans.copyDepth()
					.from(assessorList).toList(PrpLAssessorVo.class);
			assessorVo = assessorVoList.get(0);
		}

		return assessorVo;
	}

	/*
	 * @see
	 * ins.sino.claimcar.base.service.AssessorService#getDatas(ins.sino.claimcar
	 * .base.vo.AssessorQueryVo)
	 * 
	 * @param queryVo
	 * 
	 * @return
	 * 
	 * @throws Exception
	 */
	@Override
	public List<AssessorQueryResultVo> getDatas(AssessorQueryVo queryVo)
			throws Exception {
		
		SqlJoinUtils sqlUtil = getSqlJoinUtils(queryVo);
		
		String sql = sqlUtil.getSql();
		Object[] values = sqlUtil.getParamValues();
		logger.debug("taskQrySql=" + sql);
		System.out.println(sql);
		System.out.println(ArrayUtils.toString(values));
		logger.debug("ParamValues=" + ArrayUtils.toString(values));
		List<Object[]> objects = baseDaoService.getAllBySql(sql, values);
		// 对象转换
		List<AssessorQueryResultVo> resultVoList = new ArrayList<AssessorQueryResultVo>();
		for (int i = 0; i < objects.size(); i++) {

			Object[] obj = objects.get(i);

			AssessorQueryResultVo resultVo = new AssessorQueryResultVo();
			resultVo.setRegistNo(obj[1] == null ? "" : obj[1].toString());
			resultVo.setPolicyNo(obj[3] == null ? "" : obj[3].toString());
			resultVo.setInsureCode(obj[12] == null ? "" : obj[12].toString());
			resultVo.setInsureName(obj[13] == null ? "" : obj[13].toString());
			resultVo.setIntermCode(obj[35] == null ? "" : obj[35].toString());
			resultVo.setIntermName(obj[36] == null ? "" : obj[36].toString());
			
			resultVo.setTaskDetail(obj[38] == null ? "" : obj[38].toString());
			resultVo.setAssessorId(obj[39] == null ? "" : obj[39].toString());
			resultVo.setAssessorFee(obj[42] == null ? "" : obj[42].toString());
			resultVo.setVeriLoss(obj[43] == null ? "" : obj[43].toString());
			String assessorId = obj[39].toString();
			
			

			SqlJoinUtils sqlUtil1 = new SqlJoinUtils();
			sqlUtil1.append("Select max(lossDate) From PrpLAssessor as assessor where 1=1 ");
			List<Long> paramValues = new ArrayList<Long>();
			if (assessorId.indexOf(",") > 0) {
				String[] id = assessorId.split(",");
				String maskid = SqlJoinUtils.arrayToMask(id);
				sqlUtil1.append("and id in (" + maskid + ") ");
				for (String idx : id) {
					paramValues.add(Long.decode(idx));
				}
			} else {
				sqlUtil1.append("and id=?");
				paramValues.add(Long.decode(assessorId));
			}
			String hql = sqlUtil1.getSql();
			Date lossDate = databaseDao.findUniqueByHql(Date.class, hql,
					paramValues.toArray());
			resultVo.setTaskType(queryVo.getTaskType()); // 设置结果集任务类型
			resultVo.setLossDate(lossDate);
			resultVo.setUserName(this.findHandlerName(assessorId));
			resultVo.setPhotoCount(this.getImageCount(obj[1].toString(),assessorId));
			if ("0".equals(queryVo.getCaseType())) {// 案件类型,0-正常案件,1-我方代查勘,2-代我方查勘
				resultVo.setIsSurvey("否");// 是否代查勘案件
			} else {
				resultVo.setIsSurvey("是");// 是否代查勘案件
			}
			resultVoList.add(resultVo);
		}

		return resultVoList;
	}

	/*
	 * @see
	 * ins.sino.claimcar.base.service.AssessorService#findPageForAssessor(ins
	 * .sino.claimcar.base.vo.AssessorQueryVo)
	 * 
	 * @param queryVo
	 * 
	 * @return
	 * 
	 * @throws Exception
	 */
	@Override
	public ResultPage<AssessorQueryResultVo> findPageForAssessor(
			AssessorQueryVo queryVo) throws Exception {

		SqlJoinUtils sqlUtil = getSqlJoinUtils(queryVo);
		
		// 开始记录数
		int start = queryVo.getStart();
		// 查询记录数量
		int length = queryVo.getLength();

		String sql = sqlUtil.getSql();
		Object[] values = sqlUtil.getParamValues();

		logger.info("taskQrySql=" + sql);
		System.out.println("taskQrySql=" + sql);
		logger.info("ParamValues=" + ArrayUtils.toString(values));
		System.out.println("ParamValues=" + ArrayUtils.toString(values));

		Page<Object[]> page = baseDaoService.pagedSQLQuery(sql, start, length,
				values);

		long pageLengthX = page.getTotalCount();

		// 对象转换
		List<AssessorQueryResultVo> resultVoList = new ArrayList<AssessorQueryResultVo>();
		for (int i = 0; i < page.getResult().size(); i++) {

			Object[] obj = page.getResult().get(i);
			AssessorQueryResultVo resultVo = new AssessorQueryResultVo();
			resultVo.setRegistNo(obj[1].toString());
			resultVo.setPolicyNo(obj[3].toString());
			resultVo.setInsureCode(obj[12].toString());
			resultVo.setInsureName(obj[13] == null ? "" : obj[13].toString());
			resultVo.setIntermCode(obj[35].toString());
			resultVo.setIntermName(obj[36].toString());
			resultVo.setTaskDetail(obj[38].toString());
			if (obj[37] != null) {
				SimpleDateFormat format = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				Date d = format.parse(obj[37].toString());
				resultVo.setEndCaseTime(d);
			}
			String assessorId = obj[39].toString();
			resultVo.setAssessorFee(obj[42] == null ? "" : obj[42].toString());
			resultVo.setVeriLoss(obj[43] == null ? "" : obj[43].toString());
			SqlJoinUtils sqlUtil1 = new SqlJoinUtils();
			sqlUtil1.append("Select max(lossDate) From PrpLAssessor as assessor where 1=1 ");
			List<Long> paramValues = new ArrayList<Long>();
			if (assessorId.indexOf(",") > 0) {
				String[] id = assessorId.split(",");
				String maskid = SqlJoinUtils.arrayToMask(id);
				sqlUtil1.append("and id in (" + maskid + ") ");
				for (String idx : id) {
					paramValues.add(Long.decode(idx));
				}
			} else {
				sqlUtil1.append("and id=?");
				paramValues.add(Long.decode(assessorId));
			}
			String hql = sqlUtil1.getSql();
			logger.info("hql={}",hql);
			Date lossDate = databaseDao.findUniqueByHql(Date.class, hql,
					paramValues.toArray());
			resultVo.setUserName(this.findHandlerName(assessorId));
			resultVo.setPhotoCount(this.getImageCount(obj[1].toString(),assessorId)); 
			resultVo.setLossDate(lossDate);
			resultVoList.add(resultVo);
		}
		// }
		ResultPage<AssessorQueryResultVo> resultPage = new ResultPage<AssessorQueryResultVo>(
				start, length, pageLengthX, resultVoList);

		return resultPage;
	}

	private String findHandlerName(String assessorId) {
		SqlJoinUtils sqlUtil = new SqlJoinUtils();
		sqlUtil.append(" From PrpLAssessor as assessor where 1=1 ");
		List<Long> paramValue = new ArrayList<Long>();
		if (assessorId.indexOf(",") > 0) {
			String[] id = assessorId.split(",");
			String maskid = SqlJoinUtils.arrayToMask(id);
			sqlUtil.append("and id in (" + maskid + ") ");
			for (String idx : id) {
				paramValue.add(Long.decode(idx));
			}
		} else {
			sqlUtil.append("and id=?");
			paramValue.add(Long.decode(assessorId));
		}
		String hql2 = sqlUtil.getSql();
		System.out.println("=========" + hql2);
		StringBuffer userName = new StringBuffer();
		List<PrpLAssessor> assessorList = databaseDao.findAllByHql(
				PrpLAssessor.class, sqlUtil.getSql(), paramValue.toArray());
		int i = 0;
		if (assessorList != null && assessorList.size() > 0) {

			for (PrpLAssessor assessor : assessorList) {
				if (i > 0) {
					userName.append(",");
				}
				// if("0".equals(assessor.getTaskType())){
				// userName.append("查勘：");
				// }else if("1".equals(assessor.getTaskType())){
				// userName.append("车辆定损：");
				// }else if("2".equals(assessor.getTaskType())){
				// userName.append("财产定损：");
				// }else if("3".equals(assessor.getTaskType())){
				// userName.append("人伤：");
				// }
				userName.append(codeTranService.findCodeName("UserCode",
						assessor.getCreateUser()));
				i++;
			}
		}
		return userName.toString();
	}

	/*
	 * @see
	 * ins.sino.claimcar.base.service.AssessorService#applyAssessorTask(java
	 * .util.List, ins.platform.vo.SysUserVo)
	 * 
	 * @param objects
	 * 
	 * @param userVo
	 * 
	 * @return
	 * 
	 * @throws Exception
	 */
	@Override
	public Double applyAssessorTask(List<List<Object>> objects, SysUserVo userVo)
			throws Exception {

		PrpLAssessorMainVo mainVo = new PrpLAssessorMainVo();
		String taskId = billNoService.getAssessorNo(userVo.getComCode(), "01");// 暂时写死，到时候从excel取
		mainVo.setTaskId(taskId);
		mainVo.setIntermcode(objects.get(0).get(3).toString());// 获取首条公估机构
		mainVo.setIntermname(objects.get(0).get(4).toString());// 获取公估机构名称
		// 校验公估机构
		String comCode = "";
		String intemId = "";
		String IntermNameDetail = "";
		List<PrpdIntermMainVo> prpdIntermlist = managerService
				.findIntermListByHql(userVo.getComCode());
		if (prpdIntermlist == null || prpdIntermlist.size() == 0) {
			throw new RuntimeException("第" + 2 + "行数据校验不通过，该公估机构下无有效案件！");
		}
		BigDecimal sumAmount = new BigDecimal(0);
		List<PrpLAssessorFeeVo> assessorFeeVos = new ArrayList<PrpLAssessorFeeVo>();
		// 解析读取出来的数据
		for (int i = 0; i < objects.size(); i++) {
			List<Object> object = objects.get(i);
			String registNo = object.get(0).toString();
			String policyNo = object.get(1).toString();
			String insureName = object.get(2).toString();
			String intermCode = object.get(3).toString();
			String intermName = object.get(4).toString();
			String lossDate = object.get(5).toString();
			String payment = object.get(6).toString();
			String taskStatus = object.get(7).toString(); // 案件状态 验证
			String taskDetail = object.get(8).toString();
			String amount = object.get(11).toString();
			String isSurvey = object.get(16).toString();
			sumAmount = sumAmount.add(new BigDecimal(amount));
			String ids = object.get(12).toString();
			String taskType = object.get(13).toString();
			// 修改定损公估表的核损标志--1，发起公估任务
			if ("".equals(comCode)) {
				comCode = objects.get(0).get(3).toString();
			}

			if (!comCode.equals(intermCode)) {
				throw new RuntimeException("第" + (i + 2)
						+ "行数据校验不通过，二级机构错误;报案号：" + registNo);
			}

			if ("".equals(intermCode) || "".equals(ids) || "".equals(taskType)) {
				throw new RuntimeException("第" + (i + 2)
						+ "行数据校验不通过，请不要修改隐藏域;报案号：" + registNo);
			}

			if (!"是".equals(isSurvey)) {// 代查勘案件在公估费导入时，不进行公估机构有效性检验
				boolean flag = true;
				for (PrpdIntermMainVo interm : prpdIntermlist) {
					if (comCode.equals(interm.getIntermCode())) {
						flag = false;
					}
				}
				if (flag) {
					throw new RuntimeException("第" + (i + 2)
							+ "行数据校验不通过，公估机构验证不通过;报案号：" + registNo);
				}
			}

			if (!"未注销".equals(taskStatus)) { // 案件状态 常量 "未注销"
				throw new RuntimeException("第" + (i + 2)
						+ "行数据校验不通过，案件状态为\"未注销\";报案号：" + registNo);
			}

			if (!"0.00".equals(payment)) { // 已赔付金额
				throw new RuntimeException("第" + (i + 2)
						+ "行数据校验不通过，已赔付金额错误;报案号：" + registNo);
			}

			String kindCode = "";
			HashMap<String, String> map = new HashMap<String, String>();
			if (ids.indexOf(",") > -1) {
				for (String id : ids.split(",")) {
					PrpLAssessorVo assessorVo = updatePrpLAssessor(
							Long.valueOf(id), registNo, intermCode,
							AssessorUnderWriteFlag.IntermTask, i, amount,
							userVo);
					if (StringUtils.isBlank(intemId)
							|| StringUtils.isBlank(IntermNameDetail)) {
						intemId = assessorVo.getIntermId() + "";
						IntermNameDetail = assessorVo.getIntermNameDetail();
					} else if (StringUtils.isNotBlank(intemId)) {
						if (!intemId.equals(assessorVo.getIntermId() + "")) {
							throw new IllegalArgumentException("第" + (i + 2)
									+ "行数据校验不通过，三级机构不一致！;报案号：" + registNo);
						}
					}
					if (TaskType.CHK.equals(assessorVo.getTaskType())) {//
						map.put(TaskType.CHK, assessorVo.getKindCode());
					} else if (TaskType.CAR.equals(assessorVo.getTaskType())) {
						if (map.containsKey(TaskType.CAR)
								&& "1".equals(assessorVo.getSerialNo())) {// 如果包含车辆险别且为标的车时覆盖
							map.put(TaskType.CAR, assessorVo.getKindCode());
						} else {
							map.put(TaskType.CAR, assessorVo.getKindCode());
						}
					} else if (TaskType.PROP.equals(assessorVo.getTaskType())) {
						if (map.containsKey(TaskType.PROP)
								&& "1".equals(assessorVo.getSerialNo())) {// 如果包含车辆险别且为标的车时覆盖
							map.put(TaskType.PROP, assessorVo.getKindCode());
						} else {
							map.put(TaskType.PROP, assessorVo.getKindCode());
						}
					}
				}
			} else {
				PrpLAssessorVo assessorVo = updatePrpLAssessor(
						Long.valueOf(ids), registNo, intermCode,
						AssessorUnderWriteFlag.IntermTask, i, amount, userVo);
				if (StringUtils.isBlank(intemId)) {
					intemId = assessorVo.getIntermId() + "";
				} else {
					if (!intemId.equals(assessorVo.getIntermId() + "")) {
						throw new IllegalArgumentException("第" + (i + 2)
								+ "行数据校验不通过，三级机构不一致！;报案号：" + registNo);
					}
				}
				if (StringUtils.isBlank(IntermNameDetail)) {
					IntermNameDetail = assessorVo.getIntermNameDetail();
				}
				kindCode = assessorVo.getKindCode();
			}

			if (StringUtils.isBlank(kindCode)) {
				if (map.containsKey(TaskType.CHK)) {
					kindCode = map.get(TaskType.CHK);
				} else if (map.containsKey(TaskType.CAR)) {
					kindCode = map.get(TaskType.CAR);
				} else if (map.containsKey(TaskType.PROP)) {
					kindCode = map.get(TaskType.PROP);
				}
			}

			// 导入数据到公估主表和子表
			PrpLAssessorFeeVo assessorFeeVo = new PrpLAssessorFeeVo();

			assessorFeeVo.setPolicyNo(policyNo);
			PrpLClaimVo claimVo = claimTaskService.getClaimVo(registNo,
					"BZ".equals(kindCode) ? "11" : "12");
			if (claimVo != null) {
				if (StringUtils.isNotBlank(claimVo.getClaimNo())) {
					PrpLCMainVo cmainVo = policyViewService
							.findPolicyInfoByPaltform(registNo,
									claimVo.getPolicyNo());
					if (cmainVo == null) {
						throw new RuntimeException("第" + (i + 2)
								+ "无效报案,保单表无数据：" + registNo);
					}
					String compensateNo = billNoService.getCompensateNo(cmainVo
							.getComCode(),
							claimVo.getClaimNo().substring(12, 15));
					assessorFeeVo.setClaimNo(claimVo.getClaimNo());
					assessorFeeVo.setCompensateNo(compensateNo);
					assessorFeeVo.setPolicyNo(claimVo.getPolicyNo());
				}
			} else {
				throw new IllegalArgumentException("第" + (i + 2)
						+ "行数据校验不通过，案件险别错误请通知管理！;报案号：" + registNo);
			}
			assessorFeeVo.setRegistNo(registNo);
			assessorFeeVo.setInsurename(insureName);
			assessorFeeVo.setInsurecode(intermCode);
			assessorFeeVo.setKindCode(kindCode);
			assessorFeeVo.setPayAmount(new BigDecimal(payment));
			assessorFeeVo.setAmount(new BigDecimal(amount));
			assessorFeeVo.setTaskIds(ids);
			assessorFeeVo.setTaskDetail(taskDetail);
			assessorFeeVo.setCreateTime(new Date());
			assessorFeeVo.setCreateUser(userVo.getUserCode());
			assessorFeeVo.setUpdateTime(new Date());
			assessorFeeVo.setUpdateUser(userVo.getUserCode());
			assessorFeeVos.add(assessorFeeVo);
		}
		mainVo.setIntermId(Long.valueOf(intemId));
		mainVo.setIntermNameDetail(IntermNameDetail);
		mainVo.setComCode(userVo.getComCode());
		mainVo.setSumAmount(sumAmount);
		mainVo.setPrpLAssessorFees(assessorFeeVos);
		mainVo.setCreateUser(userVo.getUserCode());
		mainVo.setCreateTime(new Date());
		mainVo.setUpdateUser(userVo.getUserCode());
		mainVo.setUpdateTime(new Date());

		Long id = this.saveOrUpdatePrpLAssessorMain(mainVo, userVo);
		mainVo.setId(id);
		// 发起公估费任务
		WfTaskSubmitVo submitVo = new WfTaskSubmitVo();
		submitVo.setComCode(userVo.getComCode());
		submitVo.setTaskInUser(userVo.getUserCode());
		submitVo.setAssignCom(userVo.getComCode());
		submitVo.setAssignUser(userVo.getUserCode());

		PrpLWfTaskVo taskVo = wfTaskHandleService.addAssessorTask(mainVo,
				submitVo);
		return taskVo.getTaskId().doubleValue();
	}

	/*
	 * @see
	 * ins.sino.claimcar.base.service.AssessorService#updatePrpLAssessor(java
	 * .lang.Long, java.lang.String, java.lang.String, java.lang.String, int)
	 * 
	 * @param id
	 * 
	 * @param registNo
	 * 
	 * @param intermCode
	 * 
	 * @param underWriteFlag
	 * 
	 * @param index
	 * 
	 * @return
	 * 
	 * @throws Exception
	 */
	@Override
	public PrpLAssessorVo updatePrpLAssessor(Long id, String registNo,
			String intermCode, String underWriteFlag, int index, String amount,
			SysUserVo userVo) throws Exception {

		PrpLAssessorVo assessorVo = null;
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("id", id);
		queryRule.addEqual("registNo", registNo);
		queryRule.addEqual("intermcode", intermCode);
		PrpLRegistVo registVo = registQueryService.findByRegistNo(registNo);
		if (registVo != null) {
			String insurComCode = registVo.getComCode();
			String userComCode = userVo.getComCode();
			insurComCode = insurComCode.substring(0, 2).equals("00") ? insurComCode
					.substring(0, 4) : insurComCode.substring(0, 2);
			userComCode = userComCode.substring(0, 2).equals("00") ? userComCode
					.substring(0, 4) : userComCode.substring(0, 2);
			if (!insurComCode.equals(userComCode)) {
				throw new RuntimeException("第" + (index + 2)
						+ "行，只有承保机构才能导入对应案件公估费;报案号：" + registNo);
			}
		}
		try { // 验证录入金额 不小于0
			int amountFee = Integer.parseInt(amount.substring(0,
					amount.indexOf(".")));
			if (amountFee < 0) {
				throw new RuntimeException("第" + (index + 2) + "费用金额不能小于0;报案号："
						+ registNo);
			}

		} catch (Exception e) {
			throw new RuntimeException("第" + (index + 2) + "费用金额不能小于0的数字;报案号："
					+ registNo);
		}
		PrpLAssessor prpLAssessor = databaseDao.findUnique(PrpLAssessor.class,
				queryRule);
		if (prpLAssessor != null) {
			if (AssessorUnderWriteFlag.Verify.equals(prpLAssessor
					.getUnderWriteFlag())) {// 未导入
				prpLAssessor.setUpdateTime(new Date());
				prpLAssessor.setUpdateUser(userVo.getUserCode());
				prpLAssessor.setUnderWriteFlag(underWriteFlag);// 提交公估费任务
				databaseDao.update(PrpLAssessor.class, prpLAssessor);
			} else {
				throw new RuntimeException("第" + (index + 2)
						+ "行数据已经导入过，请使用最新的导出数据进行导入;报案号：" + registNo);
			}
			assessorVo = new PrpLAssessorVo();
			Beans.copy().from(prpLAssessor).to(assessorVo);
		} else {
			throw new RuntimeException("第" + (index + 2)
					+ "行数据校验不通过，请使用导出的正确数据进行导入;报案号：" + registNo);
		}

		return assessorVo;
	}

	/*
	 * @see
	 * ins.sino.claimcar.base.service.AssessorService#saveOrUpdatePrpLAssessorMain
	 * (ins.sino.claimcar.base.vo.PrpLAssessorMainVo, ins.platform.vo.SysUserVo)
	 * 
	 * @param mainVo
	 * 
	 * @param userVo
	 * 
	 * @return
	 */
	@Override
	public Long saveOrUpdatePrpLAssessorMain(PrpLAssessorMainVo mainVo,
			SysUserVo userVo) {
		PrpLAssessorMain assessorMain = null;
		Date date = new Date();
		if (mainVo.getId() == null) {// 新增
			assessorMain = Beans.copyDepth().from(mainVo)
					.to(PrpLAssessorMain.class);
			assessorMain.setCreateTime(date);
			assessorMain.setCreateUser(userVo.getUserCode());
			assessorMain.setUpdateUser(userVo.getUserCode());
			assessorMain.setUpdateTime(date);

			// 设置主子表关系
			for (PrpLAssessorFee assessorFee : assessorMain
					.getPrpLAssessorFees()) {
				assessorFee.setPrpLAssessorMain(assessorMain);
				assessorFee.setCreateTime(date);
				assessorFee.setCreateUser(userVo.getUserCode());
				assessorFee.setUpdateTime(date);
				assessorFee.setUpdateUser(userVo.getUserCode());
			}
		} else {
			assessorMain = databaseDao.findByPK(PrpLAssessorMain.class,
					mainVo.getId());
			Beans.copy().excludeNull().from(mainVo).to(assessorMain);
			assessorMain.setUpdateUser(userVo.getUserCode());
			assessorMain.setUpdateTime(date);

			List<PrpLAssessorFeeVo> feeVos = mainVo.getPrpLAssessorFees();
			List<PrpLAssessorFee> assessorFees = assessorMain
					.getPrpLAssessorFees();
			mergeList(feeVos, assessorFees, "id", PrpLAssessorFee.class);
			// 设置主子表关系
			for (PrpLAssessorFee assessorFee : assessorFees) {
				assessorFee.setPrpLAssessorMain(assessorMain);
				assessorFee.setUpdateTime(date);
				assessorFee.setUpdateUser(userVo.getUserCode());
			}
		}
		databaseDao.save(PrpLAssessorMain.class, assessorMain);
		return assessorMain.getId();
	}

	public List<Map<String, Object>> createExcelRecord(
			List<AssessorQueryResultVo> results) throws Exception {
		List<Map<String, Object>> listmap = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sheetName", "sheet1");
		listmap.add(map);
		for (AssessorQueryResultVo resultVo : results) {
			Map<String, Object> mapValue = new HashMap<String, Object>();
			mapValue.put("registNo", resultVo.getRegistNo());
			mapValue.put("policyNo", resultVo.getPolicyNo());
			mapValue.put("insureName", resultVo.getInsureName());
			mapValue.put("intermName", resultVo.getIntermName());
			mapValue.put("intermCode", resultVo.getIntermCode());
			mapValue.put("claimNo", resultVo.getClaimNo());
			mapValue.put("compensateNo", resultVo.getCompensateNo());
			mapValue.put("userName", resultVo.getUserName());
			mapValue.put("photoCount", resultVo.getPhotoCount());
			mapValue.put(
					"kindCode",
					codeTranService.transCode("KindCode",
							resultVo.getKindCode()));

			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			mapValue.put("lossDate", df.format(resultVo.getLossDate()));
			mapValue.put("taskDetail", resultVo.getTaskDetail());
			String amount = "0.00";
			String payAmount = "0.00";
			if ("audio".equals(resultVo.getTaskType())) { // 审核通过更新费用金额 已赔付金额
				amount = queryAmount(resultVo.getRegistNo(),
						resultVo.getAssessorId());
				payAmount = queryPayAmount(resultVo.getRegistNo(),
						resultVo.getAssessorId());
			}
			mapValue.put("amount", amount);
			mapValue.put("payment", payAmount);
			String status = "未注销";
			if ("9".equals(resultVo.getWorkStatus())) {
				status = "已注销";
			}
			mapValue.put("taskStatus", status);
			mapValue.put("id", resultVo.getAssessorId());
			mapValue.put("taskType", resultVo.getTaskType());
			mapValue.put("assessorFee", resultVo.getAssessorFee());
			mapValue.put("veriLoss", resultVo.getVeriLoss());
			mapValue.put("isSurvey", resultVo.getIsSurvey());
			listmap.add(mapValue);
		}
		return listmap;
	}

	private void mergeList(List voList, List poList, String idName,
			Class paramClass) {
		Map<Object, Object> map = new HashMap<Object, Object>();
		Map<Integer, Object> keyMap = new HashMap<Integer, Object>();
		Map<Object, Object> poMap = new HashMap<Object, Object>();

		for (int i = 0, count = voList.size(); i < count; i++) {
			Object element = voList.get(i);
			if (element == null) {
				continue;
			}
			Object key;
			try {
				key = PropertyUtils.getProperty(element, idName);
				map.put(key, element);
				keyMap.put(i, key);
			} catch (Exception e) {
				throw new IllegalArgumentException(e);
			}
		}

		for (Iterator it = poList.iterator(); it.hasNext();) {
			Object element = (Object) it.next();
			try {
				Object key = PropertyUtils.getProperty(element, idName);
				poMap.put(key, null);
				if (!map.containsKey(key)) {
					// delete(element);
					databaseDao.deleteByObject(paramClass, element);
					it.remove();
				} else {
					Beans.copy().from(map.get(key)).excludeNull().to(element);
				}
			} catch (Exception e) {
				throw new IllegalArgumentException(e);
			}
		}
		for (int i = 0, count = voList.size(); i < count; i++) {
			Object element = voList.get(i);
			if (element == null) {
				continue;
			}
			try {
				Object poElement = paramClass.newInstance();
				Object key = keyMap.get(i);
				if (key == null || !poMap.containsKey(key)) {
					Method setMethod;
					Beans.copy().from(element).to(poElement);
					poList.add(poElement);
				}
			} catch (Exception e) {
				throw new IllegalArgumentException(e);
			}
		}
	}

	/*
	 * @see
	 * ins.sino.claimcar.other.service.AssessorService#submitCancel(java.lang
	 * .Double)
	 * 
	 * @param taskId
	 * 
	 * @throws Exception
	 */
	@Override
	public void submitCancel(Double flowTaskId, SysUserVo userVo)
			throws Exception {
		// TODO Auto-generated method stub
		// 注销工作流任务
		wfTaskHandleService.cancelTask(userVo.getUserCode(), new BigDecimal(
				flowTaskId));
		// 回写主表审核状态
		PrpLWfTaskVo wfTaskVo = wfTaskHandleService.queryTask(flowTaskId);

		PrpLAssessorMainVo assessorMainVo = findAssessorMainVoByTaskNo(wfTaskVo
				.getRegistNo());
		assessorMainVo.setUnderWriteFlag("7");// 注销
		assessorMainVo.setUnderWriteDate(new Date());
		assessorMainVo.setUnderwriteuser(userVo.getUserCode());
		saveOrUpdatePrpLAssessorMain(assessorMainVo, userVo);

		// 更新定损主表数据未导入
		for (PrpLAssessorFeeVo assessorFeeVo : assessorMainVo
				.getPrpLAssessorFees()) {
			if (assessorFeeVo.getTaskIds().indexOf(",") > 0) {
				for (String id : assessorFeeVo.getTaskIds().split(",")) {
					updateAssessor(Long.valueOf(id),
							AssessorUnderWriteFlag.Verify);
				}
			} else {
				updateAssessor(Long.valueOf(assessorFeeVo.getTaskIds()),
						AssessorUnderWriteFlag.Verify);
			}
		}
	}

	private void updateAssessor(Long id, String underWriteFlag) {

		PrpLAssessor assessor = databaseDao.findByPK(PrpLAssessor.class, id);
		assessor.setUnderWriteFlag(underWriteFlag);
		databaseDao.update(PrpLAssessor.class, assessor);
	}

	@Override
	public void updateAssessorFee(List<PrpLAssessorFeeVo> feeVoList) {
		if (feeVoList != null && feeVoList.size() > 0) {
			for (PrpLAssessorFeeVo feeVo : feeVoList) {
				PrpLAssessorFee fee = databaseDao.findByPK(
						PrpLAssessorFee.class, feeVo.getId());
				if (fee != null) {
					fee.setAmount(feeVo.getAmount());
					fee.setRemark(feeVo.getRemark());
					databaseDao.update(PrpLAssessorFee.class, fee);
				}
			}
		}
	}

	@Override
	public void updateSumAmountFee(PrpLAssessorMainVo mainVo) {
		if (mainVo != null) {
			PrpLAssessorMain main = databaseDao.findByPK(
					PrpLAssessorMain.class, mainVo.getId());
			if (main != null) {
				main.setSumAmount(mainVo.getSumAmount());
				databaseDao.update(PrpLAssessorMain.class, main);
			}
		}

	}

	@Override
	/**
	 *  根据报案号查询 已赔付 金额
	 */
	public String queryPayAmount(String registNo, String assessorId) {
		String sql = "from PrpLAssessorMain where 1=1 AND id = ?";
		int sum = 0;
		if (StringUtils.isNotBlank(registNo)
				&& StringUtils.isNotBlank(assessorId)) {
			// List<PrpLAssessorFee> sumList =
			// databaseDao.findAllByHql(PrpLAssessorFee.class, sql, assessorId);
			PrpLAssessorMain main = databaseDao.findUniqueByHql(
					PrpLAssessorMain.class, sql, Long.parseLong(assessorId));
			if (main != null && main.getPrpLAssessorFees().size() > 0) {
				for (PrpLAssessorFee fee : main.getPrpLAssessorFees()) {
					if (registNo.equals(fee.getRegistNo())) {
						sum += fee.getPayAmount().intValue();
					}
				}
				return sum + ".00";
			}
			return "0.00";
		} else {
			return "0.00";
		}
	}

	@Override
	public void updateUnderWriteFlag(PrpLAssessorMainVo mainVo) {
		if (mainVo != null) {
			PrpLAssessorMain main = databaseDao.findByPK(
					PrpLAssessorMain.class, mainVo.getId());
			if (main != null) {
				main.setUnderWriteFlag(AssessorUnderWriteFlag.IntermVerify); // 公估费审核通过
				main.setUnderWriteDate(new Date());
				main.setUnderwriteuser(mainVo.getUnderwriteuser());
				databaseDao.update(PrpLAssessorMain.class, main);
			}
		}
	}

	@Override
	public PrpLAssessorFeeVo findAssessorFeeVoByComp(String registNo,
			String compensateNo) {
		QueryRule qr = QueryRule.getInstance();
		qr.addEqual("compensateNo", compensateNo);
		qr.addEqual("registNo", registNo);
		PrpLAssessorFeeVo assessorFeeVo = null;
		// PrpLAssessorFee assessorFee =
		// databaseDao.findUnique(PrpLAssessorFee.class,qr);
		List<PrpLAssessorFee> assessorFeeList = databaseDao.findAll(
				PrpLAssessorFee.class, qr);
		if (assessorFeeList != null && assessorFeeList.size() > 0) {
			for (PrpLAssessorFee assessorFeePo : assessorFeeList) {
				if ("3".equals(assessorFeePo.getPrpLAssessorMain()
						.getUnderWriteFlag())) {
					assessorFeeVo = Beans.copyDepth().from(assessorFeePo)
							.to(PrpLAssessorFeeVo.class);
				}
			}
		}
		return assessorFeeVo;
	}

	@Override
	// 结案保存结案时间到公估任务表 取第一个结案时间
	public void saveEndCaseTimeToAssessor(String registNo, Date date) {
		// PrpLAssessorVo assessorVo = null;
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo", registNo);
		List<PrpLAssessor> assessorList = databaseDao.findAll(
				PrpLAssessor.class, queryRule);
		if (assessorList != null && assessorList.size() > 0) {
			for (PrpLAssessor assessor : assessorList) {
				if (assessor.getEndCaseTime() == null) {
					assessor.setEndCaseTime(date);
					databaseDao.update(PrpLAssessor.class, assessor);
				}
			}
		}
	}

	@Override
	public PrpLAssessorFeeVo findAssessorFeeVoByComp(String compensateNo) {
		QueryRule qr = QueryRule.getInstance();
		qr.addEqual("compensateNo", compensateNo);
		PrpLAssessorFeeVo assessorFeeVo = null;
		List<PrpLAssessorFee> assessorFeeList = databaseDao.findAll(PrpLAssessorFee.class, qr);
		// 查询审核通过的公估费审核任务
		if (assessorFeeList != null && assessorFeeList.size() > 0) {
			for (PrpLAssessorFee assessorFeePo : assessorFeeList) {
				if ("3".equals(assessorFeePo.getPrpLAssessorMain().getUnderWriteFlag())) {
					assessorFeeVo = Beans.copyDepth().from(assessorFeePo).to(PrpLAssessorFeeVo.class);
					PrpLAssessorMainVo assessorMainVo = new PrpLAssessorMainVo();
					Beans.copy().from(assessorFeePo.getPrpLAssessorMain()).excludeNull().to(assessorMainVo);
					assessorFeeVo.setAssessorMainVo(assessorMainVo);
					continue;
				}
			}
		}
		return assessorFeeVo;
	}

	@Override
	public void updateAssessorFee(PrpLAssessorFeeVo feeVo) { // 公估数据写回
		// TODO Auto-generated method stub
		PrpLAssessorFee fee = databaseDao.findByPK(PrpLAssessorFee.class,
				feeVo.getId());
		// fee.setAddTaxRate(feeVo.getAddTaxRate());
		// fee.setAddTaxValue(feeVo.getAddTaxValue());
		// fee.setNoTaxValue(feeVo.getNoTaxValue());
		// fee.setInvoiceType(feeVo.getInvoiceType());
		Beans.copy().from(feeVo).excludeNull().to(fee);
		fee.setUpdateTime(new Date());

		// PrpLAssessorFee assessorFee =
		// Beans.copyDepth().from(feeVo).to(PrpLAssessorFee.class);
		if (fee != null) {
			databaseDao.update(PrpLAssessorFee.class, fee);
		}
	}

	@Override
	public List<AssessorQueryResultVo> findAssessorMainVo(String taskNo) {
		QueryRule qr = QueryRule.getInstance();
		qr.addEqual("taskId", taskNo);
		// PrpLAssessorMainVo assessorMainVo = null;
		PrpLAssessorMain assessorMain = databaseDao.findUnique(
				PrpLAssessorMain.class, qr);
		// if(assessorMain!=null){
		// assessorMainVo =
		// Beans.copyDepth().from(assessorMain).to(PrpLAssessorMainVo.class);
		// }
		// 对象转换
		List<AssessorQueryResultVo> resultVoList = new ArrayList<AssessorQueryResultVo>();
		if (assessorMain != null && assessorMain.getPrpLAssessorFees() != null
				&& assessorMain.getPrpLAssessorFees().size() > 0) {
			List<PrpLAssessorFee> fee = assessorMain.getPrpLAssessorFees();
			// /{"registNo","policyNo","insureName","intermCode","intermName","lossDate","payment","taskStatus","taskDetail","amount","id","taskType","assessorFee","veriLoss"};
			for (int i = 0; i < fee.size(); i++) {
				AssessorQueryResultVo resultVo = new AssessorQueryResultVo();
				resultVo.setRegistNo(fee.get(i).getRegistNo());
				resultVo.setPolicyNo(fee.get(i).getPolicyNo());
				resultVo.setInsureName(fee.get(i).getInsurename());
				resultVo.setIntermCode(fee.get(i).getInsurecode());
				resultVo.setIntermName(assessorMain.getIntermname());
				resultVo.setClaimNo(fee.get(i).getClaimNo());
				resultVo.setCompensateNo(fee.get(i).getCompensateNo());
				resultVo.setKindCode(fee.get(i).getKindCode());
				resultVo.setTaskDetail(fee.get(i).getTaskDetail());
				resultVo.setAssessorId(assessorMain.getId().toString());

				String assessorId = fee.get(i).getTaskIds(); // 查询最大的核损通过时间
				SqlJoinUtils sqlUtil1 = new SqlJoinUtils();
				sqlUtil1.append("Select max(lossDate) From PrpLAssessor as assessor where 1=1 ");
				List<Long> paramValues = new ArrayList<Long>();
				if (assessorId.indexOf(",") > 0) {
					String[] id = assessorId.split(",");
					String maskid = SqlJoinUtils.arrayToMask(id);
					sqlUtil1.append("and id in (" + maskid + ") ");
					for (String idx : id) {
						paramValues.add(Long.decode(idx));
					}
				} else {
					sqlUtil1.append("and id=?");
					paramValues.add(Long.decode(assessorId));
				}
				String hql = sqlUtil1.getSql();
				Date lossDate = databaseDao.findUniqueByHql(Date.class, hql,
						paramValues.toArray());
				resultVo.setLossDate(lossDate);

				resultVo.setTaskType("audio"); // assessorid
				resultVoList.add(resultVo);
			}
		}
		return resultVoList;
	}

	@Override
	public String queryAmount(String registNo, String assessorId) {
		// String sql = "from PrpLAssessorFee where 1=1 AND registNo = ? and ";
		String sql = "from PrpLAssessorMain where 1=1 AND id = ?";
		int sum = 0;
		if (StringUtils.isNotBlank(registNo)
				&& StringUtils.isNotBlank(assessorId)) {
			// List<PrpLAssessorFee> sumList =
			// databaseDao.findAllByHql(PrpLAssessorFee.class, sql, assessorId);
			PrpLAssessorMain main = databaseDao.findUniqueByHql(
					PrpLAssessorMain.class, sql, Long.parseLong(assessorId));
			if (main != null && main.getPrpLAssessorFees().size() > 0) {
				for (PrpLAssessorFee fee : main.getPrpLAssessorFees()) {
					if (registNo.equals(fee.getRegistNo())) {
						sum += fee.getAmount().intValue();
					}
				}
				return sum + ".00";
			}
			return "0.00";
		} else {
			return "0.00";
		}
	}

	@Override
	public PrpLAssessorMainVo findAsseMainVoByCompNo(String compensateNo) {
		QueryRule qr = QueryRule.getInstance();
		qr.addEqual("compensateNo", compensateNo);
		PrpLAssessorMainVo assessorMainVo = null;
		List<PrpLAssessorFee> assessorFeeList = databaseDao.findAll(
				PrpLAssessorFee.class, qr);
		// 查询审核通过的公估费审核任务
		if (assessorFeeList != null && assessorFeeList.size() > 0) {
			for (PrpLAssessorFee assessorFeePo : assessorFeeList) {
				if (assessorFeePo.getPrpLAssessorMain() != null
						&& "3".equals(assessorFeePo.getPrpLAssessorMain()
								.getUnderWriteFlag())) {
					assessorMainVo = Beans.copyDepth()
							.from(assessorFeePo.getPrpLAssessorMain())
							.to(PrpLAssessorMainVo.class);
					continue;
				}
			}

		}
		return assessorMainVo;
	}

	@Override
	public List<PrpLAssessorFeeVo> findPrpLAssessorFeeVoByRegistNo(
			String registNo) {
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo", registNo);
		List<PrpLAssessorFee> listPo = databaseDao.findAll(
				PrpLAssessorFee.class, queryRule);
		List<PrpLAssessorFeeVo> listVo = new ArrayList<PrpLAssessorFeeVo>();
		if (listPo != null && listPo.size() > 0) {
			for (PrpLAssessorFee po : listPo) {
				PrpLAssessorFeeVo vo = new PrpLAssessorFeeVo();
				Beans.copy().from(po).to(vo);
				if (po.getPrpLAssessorMain() != null) {
					vo.setAssessMainId(po.getPrpLAssessorMain().getId());
					vo.setTaskNumber(po.getPrpLAssessorMain().getTaskId());
					vo.setIntermname(po.getPrpLAssessorMain().getIntermname());
				}

				listVo.add(vo);
			}
		}
		return listVo;
	}

	@Override
	public PrpLAssessorMainVo findAssessorMainVoById(Long id) {
		PrpLAssessorMain prpLAssessorMain = databaseDao.findByPK(
				PrpLAssessorMain.class, id);
		PrpLAssessorMainVo vo = new PrpLAssessorMainVo();
		if (prpLAssessorMain != null) {
			Beans.copy().from(prpLAssessorMain).to(vo);
		}

		return vo;
	}

	@Override
	public ResultPage<PrpLPayBankVo> search(PrpLWfTaskQueryVo prplWfTaskQueryVo, int start, int length, String handleStatus) throws ParseException {
		SqlJoinUtils sqlUtil = new SqlJoinUtils();
		sqlUtil.append(" from PrpDAccRollBackAccount a,PrpLAssessorFee b ,PrpLAssessorMain c where 1=1 and (a.certiNo=b.compensateNo or a.certiNo=b.endNo) and b.prpLAssessorMain.id=c.id ");
		sqlUtil.append(" and  a.payType = ? ");
		sqlUtil.addParamValue("P67");
		// 任务状态
		if (StringUtils.isNotBlank(handleStatus)) {
			if ("0".equals(handleStatus)) {
				sqlUtil.append(" and (a.status= ? or a.status=? ) ");
				sqlUtil.addParamValue("1");
				sqlUtil.addParamValue("2");
			} else {
				sqlUtil.append(" and a.status= ? ");
				sqlUtil.addParamValue("0");
			}
		}
		// 业务号
		if (StringUtils.isNotBlank(prplWfTaskQueryVo.getBussNo()) && (prplWfTaskQueryVo.getBussNo().startsWith("P") || prplWfTaskQueryVo.getBussNo().startsWith("G") || prplWfTaskQueryVo.getBussNo().startsWith("J"))) {
			sqlUtil.append(" and a.certiNo= ? ");
			sqlUtil.addParamValue(prplWfTaskQueryVo.getBussNo());
		} else if (StringUtils.isNotBlank(prplWfTaskQueryVo.getBussNo()) && !prplWfTaskQueryVo.getBussNo().startsWith("P") && !prplWfTaskQueryVo.getBussNo().startsWith("G") && !prplWfTaskQueryVo.getBussNo().startsWith("J")) {
			sqlUtil.append(" and b.compensateNo= ? ");
			sqlUtil.addParamValue(prplWfTaskQueryVo.getBussNo());
		}
		// 立案号
		if (StringUtils.isNotBlank(prplWfTaskQueryVo.getClaimNo())) {
			sqlUtil.append(" and b.claimNo= ? ");
			sqlUtil.addParamValue(prplWfTaskQueryVo.getClaimNo());
		}
		// 公估机构intermCode
		if (StringUtils.isNotBlank(prplWfTaskQueryVo.getIntermCode())) {
			sqlUtil.append(" and c.intermId= ? ");
			sqlUtil.addParamValue(Long.valueOf(prplWfTaskQueryVo.getIntermCode()));
		}

		// 申请日期
		if (prplWfTaskQueryVo.getTaskInTimeStart() != null && prplWfTaskQueryVo.getTaskInTimeEnd() != null) {
			sqlUtil.append(" and a.rollBackTime >= ? and a.rollBackTime <= ? ");
			sqlUtil.addParamValue(prplWfTaskQueryVo.getTaskInTimeStart());
			sqlUtil.addParamValue(DateUtils.toDateEnd(prplWfTaskQueryVo.getTaskInTimeEnd()));
		}

		if (StringUtils.isNotBlank(prplWfTaskQueryVo.getComCode())) {
			sqlUtil.append("AND c.comCode LIKE ? ");
			String comCode = prplWfTaskQueryVo.getComCode();
			if (comCode.startsWith("00")) {
				comCode = comCode.substring(0, 4);
			} else {
				comCode = comCode.substring(0, 2);
			}
			sqlUtil.addParamValue((comCode.equals("0000") ? "" : comCode) + "%");
		}
		// 排序
		sqlUtil.append(" Order By a.rollBackTime desc");
		String sql = sqlUtil.getSql();
		Object[] values = sqlUtil.getParamValues();
		System.out.println(sql);
		System.out.println(sqlUtil.getParamValues());
		logger.info("公估费退票查询SQL：" + sql +"\n查询参数：" + Arrays.toString(sqlUtil.getParamValues()));
		Page<Object[]> page = databaseDao.findPageByHql(sql, start / length + 1, length, values);
		List<PrpLPayBankVo> payBankVoList = new ArrayList<PrpLPayBankVo>();
		for (int i = 0; i < page.getResult().size(); i++) {
			Object[] obj = page.getResult().get(i);
			PrpLPayBankVo bankVo = new PrpLPayBankVo();

			/*
			 * {"data" : "bussNo"}, //业务号 {"data" : "bankNameName"},//开户银行
			 * {"data" : "accountNo"}, //银行账户 {"data" : "remark"},//修改原因 {"data"
			 * : "appTime"},//申请日期 {"data" : "payTypeName"}//收付原因
			 */
			PrpDAccRollBackAccount account = (PrpDAccRollBackAccount) obj[0];
			PrpLAssessorFee assesorFee = (PrpLAssessorFee) obj[1];
			PrpLAssessorMain assesorMain = (PrpLAssessorMain) obj[2];
			if (assesorFee != null && StringUtils.isNotBlank(assesorFee.getEndNo())) {
				bankVo.setBussNo(assesorFee.getEndNo());
				bankVo.setRegistNo(assesorFee.getRegistNo());
			} else {
				if (assesorFee != null && StringUtils.isNotBlank(assesorFee.getCompensateNo())) {
					bankVo.setBussNo(assesorFee.getCompensateNo());
					bankVo.setRegistNo(assesorFee.getRegistNo());
				}
			}
			if (account != null) {
				bankVo.setRemark(account.getErrorMessage());
				bankVo.setAppTime(account.getRollBackTime());
				bankVo.setPayType(account.getPayType());
				bankVo.setBackaccountId(String.valueOf(account.getId()));
			}
			if (assesorMain != null) {
				PrpdIntermMainVo itermMainVo = managerService.findIntermById(assesorMain.getIntermId());
				if (itermMainVo != null) {
					bankVo.setIntermCode(String.valueOf(itermMainVo.getId()));
					List<PrpdIntermBankVo> bankVos = managerService.findPrpdIntermBankVosByIntermId(String.valueOf(itermMainVo.getId()));

					if (bankVos != null && bankVos.size() > 0) {
						for (PrpdIntermBankVo vo : bankVos) {
							// 通过收付那边返回的accountId配对,
							if (account != null && StringUtils.isNotBlank(account.getAccountId()) && StringUtils.isNotBlank(""+vo.getId())) {
								if (account.getAccountId().equals(vo.getId().toString())) {
									bankVo.setAccountId(String.valueOf(vo.getId()));
									bankVo.setAccountName(vo.getAccountName());
									bankVo.setBankName(vo.getBankOutlets());
									bankVo.setAccountNo(vo.getAccountNo());
									break;
								}
							}
						}
					}
				}
			}
			payBankVoList.add(bankVo);

		}
		// 去掉结算码重复的且申请时间相同的PrpLPayBankVo
		List<PrpLPayBankVo> BankVos = new ArrayList<PrpLPayBankVo>();
		int i = 0;
		int j = 0;
		for (PrpLPayBankVo vo1 : payBankVoList) {
			if (i == 0) {
				BankVos.add(vo1);
			}
			if (i >= 1) {
				for (PrpLPayBankVo vo2 : BankVos) {
					if (vo2.getBussNo().equals(vo1.getBussNo()) && vo2.getAppTime().getTime() == vo1.getAppTime().getTime()) {
						j = 1;
						break;
					}
				}
				if (j == 0) {
					BankVos.add(vo1);
				}
				j = 0;
			}
			i++;
		}

		ResultPage<PrpLPayBankVo> resultPage = new ResultPage<PrpLPayBankVo>(start, length, page.getTotalCount(), BankVos);
		return resultPage;
	}

	@Override
	public List<PrpLAssessorFeeVo> findPrpLAssessorFeeVoByCompensateNoOrEndNo(
			String bussNo) {
		QueryRule queryRule = QueryRule.getInstance();
		if (StringUtils.isNotBlank(bussNo) && (bussNo.startsWith("P") || bussNo.startsWith("G") || bussNo.startsWith("J"))) {
			queryRule.addEqual("endNo", bussNo);
		} else {
			queryRule.addEqual("compensateNo", bussNo);
		}
		List<PrpLAssessorFee> feeLists = databaseDao.findAll(
				PrpLAssessorFee.class, queryRule);
		List<PrpLAssessorFeeVo> listVos = new ArrayList<PrpLAssessorFeeVo>();
		if (feeLists != null && feeLists.size() > 0) {
			for (PrpLAssessorFee fee : feeLists) {
				PrpLAssessorFeeVo feeVo = new PrpLAssessorFeeVo();
				Beans.copy().from(fee).to(feeVo);
				listVos.add(feeVo);
			}
		}

		return listVos;
	}

	@Override
	public int updateSettleNo(String compensateNo, String settleNo, String operateType) {
		QueryRule qr = QueryRule.getInstance();
		qr.addEqual("compensateNo", compensateNo);
		if ("0".equals(operateType)) {
			qr.addEqual("endNo", settleNo);
		}
		List<PrpLAssessorFee> prpLAssessorFeeList = databaseDao.findAll(PrpLAssessorFee.class, qr);
		int count = 0;
		if (prpLAssessorFeeList != null && prpLAssessorFeeList.size() > 0) {
			for (PrpLAssessorFee prpLAssessorFee : prpLAssessorFeeList) {
				if ("1".equals(operateType)) {
					count++;
					prpLAssessorFee.setEndNo(settleNo);
				} else if ("0".equals(operateType)) {
					count++;
					prpLAssessorFee.setEndNo(null);
				}
			}
		}

		return count;
	}

	/**
	 * 公估费退票审核页面查询方法
	 */
	@Override
	public ResultPage<PrpLPayBankVo> assessorAuditSearch(
			PrpLWfTaskQueryVo prplWfTaskQueryVo, int start, int length,
			String handleStatus) throws ParseException {
		SqlJoinUtils sqlUtil = new SqlJoinUtils();
		sqlUtil.append("FROM PrplInterrmAudit a,PrpLAssessorFee b where 1=1 and (a.bussNo=b.compensateNo or a.bussNo=b.endNo)");
		sqlUtil.append("AND a.isautoAudit != ? ");
		sqlUtil.addParamValue("0");
		if (StringUtils.isNotBlank(handleStatus)) {
			if ("0".equals(handleStatus)) {
				sqlUtil.append("AND a.status= ? ");
				sqlUtil.addParamValue("0");
			} else {
				sqlUtil.append("AND a.status= ? ");
				sqlUtil.addParamValue("1");
			}
		}

		// 业务号
		if (StringUtils.isNotBlank(prplWfTaskQueryVo.getBussNo())
				&& (prplWfTaskQueryVo.getBussNo().startsWith("P") || prplWfTaskQueryVo.getBussNo().startsWith("G")
				|| prplWfTaskQueryVo.getBussNo().startsWith("J"))) {
			sqlUtil.append(" and a.bussNo= ? ");
			sqlUtil.addParamValue(prplWfTaskQueryVo.getBussNo());
		} else if (StringUtils.isNotBlank(prplWfTaskQueryVo.getBussNo())
				&& !prplWfTaskQueryVo.getBussNo().startsWith("P") && !prplWfTaskQueryVo.getBussNo().startsWith("G")
				&& !prplWfTaskQueryVo.getBussNo().startsWith("J")) {
			sqlUtil.append(" and b.compensateNo= ? ");
			sqlUtil.addParamValue(prplWfTaskQueryVo.getBussNo());
		}

		// 立案号
		if (StringUtils.isNotBlank(prplWfTaskQueryVo.getClaimNo())) {
			sqlUtil.append(" and b.claimNo= ? ");
			sqlUtil.addParamValue(prplWfTaskQueryVo.getClaimNo());
		}

		if (StringUtils.isNotBlank(prplWfTaskQueryVo.getIntermCode())) {
			sqlUtil.append(" and a.intermCode= ? ");
			sqlUtil.addParamValue(prplWfTaskQueryVo.getIntermCode());
		}

		// 流入日期
		if (prplWfTaskQueryVo.getTaskInTimeStart() != null
				&& prplWfTaskQueryVo.getTaskInTimeEnd() != null) {
			sqlUtil.append(" and a.createTime >= ? and a.createTime <= ? ");
			sqlUtil.addParamValue(prplWfTaskQueryVo.getTaskInTimeStart());
			sqlUtil.addParamValue(DateUtils.toDateEnd(prplWfTaskQueryVo
					.getTaskInTimeEnd()));
		}
		// 机构代码
		if (StringUtils.isNotBlank(prplWfTaskQueryVo.getComCode())) {
			sqlUtil.append("AND a.comCode LIKE ? ");
			String comCode = prplWfTaskQueryVo.getComCode();
			if (comCode.startsWith("00")) {
				comCode = comCode.substring(0, 4);
			} else {
				comCode = comCode.substring(0, 2);
			}
			sqlUtil.addParamValue((comCode.equals("0000") ? "" : comCode) + "%");
		}
		// 排序
		sqlUtil.append(" Order By a.createTime desc");
		String sql = sqlUtil.getSql();
		Object[] params = sqlUtil.getParamValues();
		System.out.println(sql);
		System.out.println(sqlUtil.getParamValues());
		Page<Object[]> page = databaseDao.findPageByHql(sql,
				start / length + 1, length, params);
		List<PrpLPayBankVo> payBankVoList = new ArrayList<PrpLPayBankVo>();
		for (int i = 0; i < page.getResult().size(); i++) {
			Object[] obj = page.getResult().get(i);
			PrpLPayBankVo bankVo = new PrpLPayBankVo();
			PrplInterrmAudit interrmAudit = (PrplInterrmAudit) obj[0];
			PrpLAssessorFee assesorFee = (PrpLAssessorFee) obj[1];
			if (assesorFee != null
					&& StringUtils.isNotBlank(assesorFee.getEndNo())) {
				bankVo.setBussNo(assesorFee.getEndNo());
				bankVo.setRegistNo(assesorFee.getRegistNo());
			} else {
				if (assesorFee != null
						&& StringUtils.isNotBlank(assesorFee.getCompensateNo())) {
					bankVo.setBussNo(assesorFee.getCompensateNo());
					bankVo.setRegistNo(assesorFee.getRegistNo());
				}
			}
			if (interrmAudit != null) {
				PrpDAccRollBackAccountVo accountVo = accountQueryService
						.findRollBackAccountById(interrmAudit
								.getBackAccountId());
				if (accountVo != null) {
					bankVo.setRemark(accountVo.getErrorMessage());
					bankVo.setAppTime(accountVo.getRollBackTime());
					bankVo.setPayType(accountVo.getPayType());
					bankVo.setBackaccountId(String.valueOf(accountVo.getId()));
				}

				bankVo.setAccountId(String.valueOf(interrmAudit.getOldBankId()));// 公估费退票银行表的Id
				bankVo.setAccountName(interrmAudit.getAccountName());// 收款方户名
				bankVo.setBankName(interrmAudit.getBankoutLets());// 银行行号
				bankVo.setAccountNo(interrmAudit.getAccountCode());// 收款方银行账号
				bankVo.setAuditId(String.valueOf(interrmAudit.getId()
						.longValue()));// 公估费退票表Id
				bankVo.setIntermCode(interrmAudit.getIntermCode());// 公估机构
			}

			payBankVoList.add(bankVo);

		}
		// 去掉结算码重复的且申请时间相同的PrpLPayBankVo
		List<PrpLPayBankVo> BankVos = new ArrayList<PrpLPayBankVo>();
		int i = 0;
		int j = 0;
		for (PrpLPayBankVo vo1 : payBankVoList) {
			if (i == 0) {
				BankVos.add(vo1);
			}
			if (i >= 1) {
				for (PrpLPayBankVo vo2 : BankVos) {
					if (vo2.getBussNo().equals(vo1.getBussNo())
							&& vo2.getAppTime().getTime() == vo1.getAppTime()
									.getTime()) {
						j = 1;
						break;
					}
				}
				if (j == 0) {
					BankVos.add(vo1);
				}
				j = 0;
			}
			i++;
		}
		ResultPage<PrpLPayBankVo> resultPage = new ResultPage<PrpLPayBankVo>(
				start, length, page.getTotalCount(), BankVos);
		return resultPage;
	}

	@Override
	public PrplInterrmAuditVo findPrplInterrmAuditVoById(Long id) {
		PrplInterrmAudit auditPo = databaseDao.findByPK(PrplInterrmAudit.class,
				new BigDecimal(id));
		PrplInterrmAuditVo auditVo = new PrplInterrmAuditVo();
		if (auditPo != null) {
			Beans.copy().from(auditPo).to(auditVo);
		}
		return auditVo;
	}

	@Override
	public void updateOrSaveOfPrplInterrmAudit(PrplInterrmAuditVo auditVo)
			throws Exception {

		if (auditVo != null) {
			if (auditVo.getId() != null) {
				PrplInterrmAudit auditPo = databaseDao.findByPK(
						PrplInterrmAudit.class, auditVo.getId());
				Beans.copy().from(auditVo).excludeNull().to(auditPo);
				databaseDao.update(PrplInterrmAudit.class, auditPo);
			} else {
				PrplInterrmAudit audit = new PrplInterrmAudit();
				Beans.copy().from(auditVo).to(audit);
				databaseDao.save(PrplInterrmAudit.class, audit);
			}
		} else {
			throw new IllegalArgumentException("保存或更新失败,公估费退票审核表传入数据为空");
		}

	}

	@Override
	public void updateAuditIdAndIsHaveAuditAndAuditFlagOfPrpdaccrollbackaccount(
			Long backId, Long auditId, String isHaveAudit, String auditFlag)
			throws Exception {
		Long aId = auditId;
		PrpDAccRollBackAccount backAccountPo = databaseDao.findByPK(
				PrpDAccRollBackAccount.class, backId);
		if (backAccountPo != null) {
			backAccountPo.setAuditId(auditId);
			backAccountPo.setIsHaveAudit(isHaveAudit);
			backAccountPo.setAuditFlag(auditFlag);
			databaseDao.update(PrpDAccRollBackAccount.class, backAccountPo);
		} else {
			throw new IllegalArgumentException(
					"更新AuditId和IsHaveAudit字段失败！退票表Id为" + aId);
		}

	}

	@Override
	public void updateStatusOfPrpdaccrollbackaccount(String status, Long backId)
			throws Exception {
		Long aId = backId;
		PrpDAccRollBackAccount backAccountPo = databaseDao.findByPK(
				PrpDAccRollBackAccount.class, backId);
		if (backAccountPo != null) {
			backAccountPo.setStatus(status);
			databaseDao.update(PrpDAccRollBackAccount.class, backAccountPo);
		} else {
			throw new IllegalArgumentException("更新status字段失败！退票表Id为" + aId);
		}

	}

	@Override
	public PrplInterrmAuditVo findPrplInterrmAuditVoByBackAccountId(
			Long backAccountId) {
		QueryRule rule = QueryRule.getInstance();
		rule.addEqual("backAccountId", backAccountId);
		rule.addDescOrder("createTime");
		List<PrplInterrmAudit> lists = databaseDao.findAll(
				PrplInterrmAudit.class, rule);
		PrplInterrmAuditVo auditVo = new PrplInterrmAuditVo();
		if (lists != null && lists.size() > 0) {
			Beans.copy().from(lists.get(0)).to(auditVo);
		}

		return auditVo;
	}

	@Override
	public void updateInfoFlagOfPrpdaccrollbackaccount(String infoFlag,
			Long backId) throws Exception {
		Long aId = backId;
		PrpDAccRollBackAccount backAccountPo = databaseDao.findByPK(
				PrpDAccRollBackAccount.class, backId);
		if (backAccountPo != null) {
			backAccountPo.setInfoFlag(infoFlag);
			;
			databaseDao.update(PrpDAccRollBackAccount.class, backAccountPo);
		} else {
			throw new IllegalArgumentException("更新infoFlag字段失败！退票表Id为" + aId);
		}

	}

	public int getImageCount(String registNo, String assessorId) {
		Map<String, String> idMap = new HashMap<String, String>();
		List<Long> idList = new ArrayList<Long>();
		if(assessorId.indexOf(",")>0){
			String[] id = assessorId.split(",");
			for(String idx:id){
				if(!idMap.containsKey(idx)){
					idMap.put(idx, idx);
					idList.add(Long.decode(idx));
				}
			}
		}else{
			idMap.put(assessorId, assessorId);
			idList.add(Long.decode(assessorId));
		}
		int count = 0;
		QueryRule qr = QueryRule.getInstance();
		qr.addEqual("registNo", registNo);
		qr.addIn("id", idList);
		List<PrpLAssessor> prpLAssessors = databaseDao.findAll(PrpLAssessor.class, qr);
		if (prpLAssessors != null && prpLAssessors.size() > 0) {
			List<PrpLAssessorVo> assessorVoList = Beans.copyDepth().from(prpLAssessors).toList(PrpLAssessorVo.class);
			Map<String, String> createUserMap = new HashMap<String, String>();
			for(PrpLAssessorVo prpLAssessorVo : assessorVoList){
		    	if ((prpLAssessorVo != null && prpLAssessorVo.getSumImgs() != null)
		    			&& (!createUserMap.containsKey(prpLAssessorVo.getCreateUser()))) {//排除相同的公估人员
					count = count + prpLAssessorVo.getSumImgs();
					createUserMap.put(prpLAssessorVo.getCreateUser(), prpLAssessorVo.getCreateUser());
				}
		    }
		}
	    	//照片数量查询修改成 根据公估工号查询对应案件上传照片工号的照片数量。不管环节。
			/*SqlJoinUtils sqlUtil = new SqlJoinUtils();
			sqlUtil.append("select * From ImageFileIndex imageFile where 1=1 and imageFile.userCode in( ");
			if(assessorId.indexOf(",")>0){
				String[] id = assessorId.split(",");
				String maskid = SqlJoinUtils.arrayToMask(id);
				sqlUtil.append("select CreateUser From  prplassessor assessor where assessor.id in ("+maskid+") )");
				for(String idx:id){
					sqlUtil.addParamValue(Long.decode(idx));
				}
			}else{
				sqlUtil.append("select CreateUser From  prplassessor assessor where assessor.id=?)");
				sqlUtil.addParamValue(Long.decode(assessorId));
			}
			if(StringUtils.isNotBlank(registNo)){
				sqlUtil.append(" and imageFile.bussNo = ? ");
				sqlUtil.addParamValue(registNo);
			}
			String sql = sqlUtil.getSql();
			Object[] values = sqlUtil.getParamValues();
			long count = 0;
			try {
				List<Object[]> list1 = baseDaoService.findListBySql(sql, values);
				count = list1.size();
			} catch (Exception e) {
				e.printStackTrace();
			}*/
	        return count;
	    }

	@Override
	public PrplInterrmAuditVo findPrplInterrmAuditVoByRegistNoAndBussNoAndStatus(
			String registNo, String bussNo, String status) {
		QueryRule rule = QueryRule.getInstance();
		rule.addEqual("registNo", registNo);
		rule.addEqual("bussNo", bussNo);
		rule.addEqual("status", status);
		List<PrplInterrmAudit> audits = databaseDao.findAll(
				PrplInterrmAudit.class, rule);
		PrplInterrmAuditVo auditVo = new PrplInterrmAuditVo();
		if (audits != null && audits.size() > 0) {
			Beans.copy().from(audits.get(0)).to(auditVo);
		}

		return auditVo;
	}

	@Override
	public PrpLAssessorVo findPrpLAssessorVo(String registNo, String taskType,String underWriteFlag, String licenseNo) {
		QueryRule query=QueryRule.getInstance();
		query.addEqual("registNo", registNo);
		query.addEqual("taskType", taskType);
		query.addEqual("underWriteFlag",underWriteFlag);
	  if(StringUtils.isNotBlank(licenseNo) && "1".equals(taskType)){
		query.addEqual("licenseNo",licenseNo);	
	  }
	   query.addDescOrder("createTime");
	  List<PrpLAssessor>  assessors= databaseDao.findAll(PrpLAssessor.class, query);
	  PrpLAssessorVo vo=null;
	  if(assessors!=null && assessors.size()>0){
		  PrpLAssessor po= assessors.get(0);
		  vo=new PrpLAssessorVo();
		  Beans.copy().from(po).to(vo);
	  }
		return vo;
	}

	@Override
	public void updatePrpLAssessor(PrpLAssessorVo prpLAssessorVo) {
		if(prpLAssessorVo !=null && prpLAssessorVo.getId()!=null){
			PrpLAssessor assessor=new PrpLAssessor();
			Beans.copy().from(prpLAssessorVo).to(assessor);
			databaseDao.update(PrpLAssessor.class, assessor);
		}
		
	}
	@Override
	public SqlJoinUtils getSqlJoinUtils(AssessorQueryVo queryVo) throws Exception{
		SqlJoinUtils sqlUtil = new SqlJoinUtils();
		sqlUtil.append("SELECT * FROM prplwftaskquery qry,");
		sqlUtil.append("(SELECT c.registno reportno,c.intermcode,c.intermname,c.caseTime1 caseTime,to_char(wmsys.wm_concat(c.detail)) taskdetail,to_char(wmsys.wm_concat(c.taskid)) assessorid,to_char(wmsys.wm_concat(c.underwriteflag)) taskflag,max(c.lossdate) lossdate,to_char(wmsys.wm_concat(c.assessorFee)) assessorFee,to_char(wmsys.wm_concat(c.veriLoss)) veriLoss FROM ");
		sqlUtil.append("(SELECT b.*,decode(b.licenseno,'',b.taskname,b.taskname||'('||b.licenseno||')') detail FROM ");
		sqlUtil.append("(SELECT a.registno,a.intermcode,a.intermname,a.tasktype,a.endcasetime caseTime1,a.taskname,to_char(wmsys.wm_concat(a.underwriteflag)) underwriteflag,to_char(wmsys.wm_concat(a.licenseno)) licenseno,to_char(wmsys.wm_concat(a.id)) taskid,max(a.lossdate) lossdate,to_char(wmsys.wm_concat(a.assessorFee)) assessorFee,to_char(wmsys.wm_concat(a.veriLoss)) veriLoss FROM ");
		sqlUtil.append("(SELECT assessor.*,decode(tasktype,'0','查勘','1','车辆定损','2','财产定损','人伤跟踪') taskName FROM prplassessor assessor ");
		sqlUtil.append("WHERE 1=1 and underwriteflag in (?,?) ");
		sqlUtil.addParamValue("0");// 公估定损
		sqlUtil.addParamValue("1");// 公估核损
		// 查询增加公估机构
		/*
		 * if(StringUtils.isNotBlank(queryVo.getComCode())){
		 * sqlUtil.append("AND assessor.comCode LIKE ? "); String comCode =
		 * queryVo.getComCode(); if(comCode.startsWith("00")){ comCode =
		 * comCode.substring(0, 4); }else{ comCode = comCode.substring(0, 2); }
		 * sqlUtil.addParamValue((comCode.equals("0000")?"":comCode) + "%"); }
		 */
		String comCode = queryVo.getComCode();
		if (comCode.startsWith("00")) {
			comCode = comCode.substring(0, 4);
		} else {
			comCode = comCode.substring(0, 2);
		}
		// 案件类型,0-正常案件,1-我方代查勘,2-代我方查勘
		if ("0".equals(queryVo.getCaseType())) {
			sqlUtil.append(" and assessor.comCode LIKE ? and exists(select 1 from PrpLRegist reg where reg.registNo=assessor.registNo and reg.comCode like ?) ");
		} else if ("1".equals(queryVo.getCaseType())) {
			sqlUtil.append(" and assessor.comCode LIKE ? and exists(select 1 from PrpLRegist reg where reg.registNo=assessor.registNo and reg.comCode not like ?) ");
		} else {
			sqlUtil.append(" and assessor.comCode not LIKE ? and exists(select 1 from PrpLRegist reg where reg.registNo=assessor.registNo and reg.comCode like ?) ");
		}
		sqlUtil.addParamValue(comCode + "%");
		sqlUtil.addParamValue(comCode + "%");

		String taskType = queryVo.getTaskType();
		if (taskType.indexOf(",") > 0) {
			String[] str = taskType.split(",");
			String maskStr = SqlJoinUtils.arrayToMask(str);
			sqlUtil.append(" AND assessor.tasktype in (" + maskStr + ") ");
			for (String s : str) {
				sqlUtil.addParamValue(s);
			}
		} else {
			sqlUtil.append(" AND assessor.tasktype = ? ");
			sqlUtil.addParamValue(taskType);
		}
		// 按照三级机构查询
		sqlUtil.append(" and assessor.intermid = ? ");
		sqlUtil.addParamValue(Long.parseLong(queryVo.getIntermCode()));

		sqlUtil.andEquals(queryVo, "assessor", "vin");

		sqlUtil.append(") a ");
		sqlUtil.append("GROUP BY a.registno,a.intermcode,a.intermname,a.tasktype,a.endcasetime,a.taskname) b ) c ");
		sqlUtil.append("GROUP BY c.registno,c.intermcode,c.caseTime1,c.intermname) d ");
		sqlUtil.append("WHERE qry.registno=d.reportno AND instr(taskflag,?)=0 ");
		sqlUtil.addParamValue("0");// 发起过定损任务查询不出来

		sqlUtil.andReverse(queryVo, "qry", 7, "registNo");

		if (queryVo.getLossDateStart() != null) {
			sqlUtil.andDate(queryVo, "d", "lossDate");
		}
		if (queryVo.getCaseTimeStart() != null) {
			sqlUtil.andDate(queryVo, "d", "caseTime");
		}

		sqlUtil.andLike2(queryVo, "qry", "insuredName", "licenseNo");
		String policyNo = queryVo.getPolicyNo();
		if (StringUtils.isNotBlank(policyNo) && policyNo.length() > 2) {
			String policyNoRev = StringUtils.reverse(policyNo.toString())
					.trim();
			sqlUtil.append("AND (qry.policyNoRev LIKE ? Or qry.policyNoLink LIKE ? ) ");
			sqlUtil.addParamValue(policyNoRev + "%");
			sqlUtil.addParamValue("%" + policyNo + "%");
		}
		sqlUtil.append(" order by lossDate desc ");
		return sqlUtil;
	}

	@Override
	public List<PrpLAssessorVo> findListLAssessorVo(String registNo,
			String createUser) {
		QueryRule query = QueryRule.getInstance();
		query.addEqual("registNo", registNo);
		query.addEqual("createUser", createUser);
		List<PrpLAssessor> assessors = databaseDao.findAll(PrpLAssessor.class,query);
		List<PrpLAssessorVo> assessorVos = null;
		if (assessors != null && assessors.size() > 0) {
			assessorVos = Beans.copyDepth().from(assessors).toList(PrpLAssessorVo.class);
		}
		return assessorVos;
	}




	@Override
	public ResultPage<AssessorQueryResultVo> findSupplymentPageForAssessor(AssessorQueryVo queryVo) throws Exception {
		SqlJoinUtils sqlUtil = new SqlJoinUtils();
		sqlUtil.append("select p.registno,"
				+ "(select policy.policyno from prplcmain  policy  where policy.registno = p.registno and  rownum =1)"
				+ ", handleruser,p.underwriteenddate,"
				+ "  (select policy.insuredname from prplcmain  policy  where policy.registno = p.registno and  rownum =1),"
				+ "  case  when tasktype = '0' then  taskname  when tasktype = '1' then taskname || '(' || "
				+ " (select licenseno   from prpldlosscarmain carmain  where handleridkey = carmain.id) || ')'"
				+ " when tasktype = '2' then taskname || '(' || p.itemname || ')' else '人伤' end as taskname,  handleridkey,tasktype ");
		sqlUtil.append(" from (select ROW_NUMBER() OVER(PARTITION BY t.registno, handleruser, "
				+ " case when nodecode = 'PLoss' and  itemname is null then '人伤'  else itemname end  "
				+ " ORDER BY handlertime DESC) rn,"
				+ " itemname,t.registno,handleruser,handleridkey,TASKNAME,"
				+ " case  when SUBNODECODE = 'Chk' then '0' "
				+ " when SUBNODECODE = 'DLCar' or  subnodecode= 'DLCarAdd' then '1' "
				+ " when SUBNODECODE = 'DLProp'  or  subnodecode= 'DLPropAdd' then '2' else  '3' end as tasktype, "
				+ " case when SUBNODECODE = 'Chk' then "
				+ "  chk.chksubmittime  "
				+ " when SUBNODECODE = 'DLCar' or  subnodecode= 'DLCarAdd' then "
				+ "  carmain.underwriteenddate "
				+ " when SUBNODECODE = 'DLProp'  or  subnodecode= 'DLPropAdd' then "
				+ " propmain.underwriteenddate  "
				+ "  else permain.UNDWRTFEEENDDATE end as underwriteenddate "
				+ " from prplwftaskout t ");

		sqlUtil.append(" left join prplcheck chk on chk.id = t.handleridkey "
				+ " left join prpldlosscarmain carmain on carmain.id = t.handleridkey"
				+ " left join prpldlosspropmain propmain on propmain.id = t.handleridkey"
				+ " left join prpldlossperstracemain permain on permain.id = t.handleridkey");
		sqlUtil.append(" where (SUBNODECODE in (?, ?, ?,?,?,?,?)) ");
		sqlUtil.addParamValue(FlowNode.DLCar.name());
		sqlUtil.addParamValue(FlowNode.DLProp.name());
		sqlUtil.addParamValue(FlowNode.PLFirst.name());
		sqlUtil.addParamValue(FlowNode.PLNext.name());
		sqlUtil.addParamValue(FlowNode.Chk.name());
		sqlUtil.addParamValue(FlowNode.DLCarAdd.name());
		sqlUtil.addParamValue(FlowNode.DLPropAdd.name());
		sqlUtil.append(" and workstatus = ? ");
		sqlUtil.addParamValue(CodeConstants.WorkStatus.END);
		sqlUtil.append(" and handlerstatus =  ? ");
		sqlUtil.addParamValue(CodeConstants.HandlerStatus.END);
		sqlUtil.append(" and HANDLERUSER  = ? ");  
		sqlUtil.addParamValue(queryVo.getHandlerCode());
		sqlUtil.append(" and not exists (select 1 from PrpLAssessor b  where t.registno = b.registno  and t.handleridkey = b.LOSSID and b.createuser= t.handleruser ) "
				+ "and (case  when SUBNODECODE = 'Chk' then  chk.chksubmittime "
				+ " when SUBNODECODE = 'DLCar' or  subnodecode= 'DLCarAdd' then carmain.underwriteenddate"
				+ " when SUBNODECODE = 'DLProp'  or  subnodecode= 'DLPropAdd' then  propmain.underwriteenddate  else permain.UNDWRTFEEENDDATE end) >=?"
				+ "  and (case  when SUBNODECODE = 'Chk' then  chk.chksubmittime "
				+ " when SUBNODECODE = 'DLCar' or  subnodecode= 'DLCarAdd' then carmain.underwriteenddate"
				+ " when SUBNODECODE = 'DLProp'  or  subnodecode= 'DLPropAdd' then  propmain.underwriteenddate  else permain.UNDWRTFEEENDDATE end)<=? ");
		sqlUtil.addParamValue(queryVo.getLossDateStart());
		sqlUtil.addParamValue(DateUtils.addDays(queryVo.getLossDateEnd(),Integer.valueOf(CodeConstants.CommonConst.TRUE))); 
		
		if(queryVo.getCaseTimeStart() != null && queryVo.getCaseTimeEnd() != null){
			sqlUtil.append(" and exists   (select 1 from prplendcase endcase where endcase.registno = t.registno"
					+ " and endcase.ENDCASEDATE >= ?  and endcase.ENDCASEDATE <= ? ) ");
			sqlUtil.addParamValue(queryVo.getCaseTimeStart());
			sqlUtil.addParamValue(queryVo.getCaseTimeEnd());
			
		}
		sqlUtil.append(" ) p where rn = 1 "
				+ " order by underwriteenddate desc");
	
		
		// 开始记录数
		int start = queryVo.getStart();
		// 查询记录数量
		int length = queryVo.getLength();

		String sql = sqlUtil.getSql();
		Object[] values = sqlUtil.getParamValues();

		logger.info("查询公估费补录的taskQrySql=" + sql + "，参数ParamValues =" + ArrayUtils.toString(values));
		long startTime = System.currentTimeMillis();
		Page<Object[]> page = baseDaoService.pagedSQLQuery(sql, start, length,
				values);
		logger.info("查询公估费补录耗时时间=" + (System.currentTimeMillis()- startTime));

		long pageLengthX = page.getTotalCount();

		// 对象转换
		List<AssessorQueryResultVo> resultVoList = new ArrayList<AssessorQueryResultVo>();
		for (int i = 0; i < page.getResult().size(); i++) {

			Object[] obj = page.getResult().get(i);
			AssessorQueryResultVo resultVo = new AssessorQueryResultVo();
			resultVo.setRegistNo(String.valueOf(obj[0]));
			resultVo.setPolicyNo(String.valueOf(obj[1]));
			resultVo.setUserName(String.valueOf(obj[2]));
			if (obj[3] != null) {
				SimpleDateFormat format = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				if(obj[3] != null){
					Date d = format.parse(obj[3].toString());
					resultVo.setLossDate(d);
				} 
			}
			resultVo.setInsureName(obj[4] == null ? "" : obj[4].toString());
			resultVo.setTaskDetail(String.valueOf(obj[5]));
			resultVo.setBussId(Long.valueOf(String.valueOf(obj[6])));
			resultVo.setTaskType(String.valueOf(obj[7]));
			resultVoList.add(resultVo);
		}
		ResultPage<AssessorQueryResultVo> resultPage = new ResultPage<AssessorQueryResultVo>(
				start, length, pageLengthX, resultVoList);

		return resultPage;
	}



	/* 
	 * @see ins.sino.claimcar.other.service.AssessorService#findAssessorByParams(ins.sino.claimcar.other.vo.PrpLAssessorVo)
	 * @param prpLAssessorVo
	 * @return
	 */
	@Override
	public PrpLAssessorVo findAssessorByParams(PrpLAssessorVo assessorVo) {
		QueryRule rule = QueryRule.getInstance();
		rule.addEqual("registNo", assessorVo.getRegistNo());
		if( assessorVo.getLossId() != null){
			rule.addEqual("bussiId", assessorVo.getLossId());
		}
		if(assessorVo.getTaskType() != null){
			rule.addEqual("taskType", assessorVo.getTaskType());
		}
		if(assessorVo.getLicenseNo() != null){
			rule.addEqual("licenseNo", assessorVo.getLicenseNo());
		}
		if(assessorVo.getSerialNo() != null){
			rule.addEqual("serialNo",assessorVo.getSerialNo());
		}
		if(assessorVo.getIntermcode() != null){
			rule.addEqual("intermcode",assessorVo.getIntermcode());
		}
		List<PrpLAssessor> acheckPos = databaseDao.findAll(PrpLAssessor.class,rule);
		PrpLAssessorVo prpLAssessorVo = null;
		if (acheckPos != null && !acheckPos.isEmpty()) {
			prpLAssessorVo = new PrpLAssessorVo();
			Beans.copy().from(acheckPos.get(0)).to(prpLAssessorVo);
		}
		return prpLAssessorVo;
	}



	

}
