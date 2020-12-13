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
import ins.sino.claimcar.CodeConstants.AcheckUnderWriteFlag;
import ins.sino.claimcar.CodeConstants.CheckTaskType;
import ins.sino.claimcar.base.po.PrpDAccRollBackAccount;
import ins.sino.claimcar.base.po.PrpLAcheck;
import ins.sino.claimcar.base.po.PrpLCheckmAudit;
import ins.sino.claimcar.claim.service.ClaimTaskService;
import ins.sino.claimcar.claim.vo.PrpLClaimVo;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.po.PrpLAcheckMain;
import ins.sino.claimcar.flow.po.PrpLCheckFee;
import ins.sino.claimcar.flow.service.WfTaskHandleService;
import ins.sino.claimcar.flow.vo.PrpLWfTaskQueryVo;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.flow.vo.WfTaskSubmitVo;
import ins.sino.claimcar.manager.service.ManagerService;
import ins.sino.claimcar.manager.vo.PrpdCheckBankMainVo;
import ins.sino.claimcar.manager.vo.PrpdcheckBankVo;
import ins.sino.claimcar.other.service.AccountQueryService;
import ins.sino.claimcar.other.service.AcheckService;
import ins.sino.claimcar.other.vo.CheckQueryResultVo;
import ins.sino.claimcar.other.vo.CheckQueryVo;
import ins.sino.claimcar.other.vo.PrpDAccRollBackAccountVo;
import ins.sino.claimcar.other.vo.PrpLAcheckMainVo;
import ins.sino.claimcar.other.vo.PrpLAcheckVo;
import ins.sino.claimcar.other.vo.PrpLCheckFeeVo;
import ins.sino.claimcar.other.vo.PrpLCheckmAuditVo;
import ins.sino.claimcar.other.vo.PrpLPayBankVo;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Path;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;

@Service(protocol = { "dubbo" }, validation = "true", registry = { "default" }, timeout = 1000000)
@Path("acheckService")
public class AcheckServiceImpl implements AcheckService{
	private static Logger logger = LoggerFactory.getLogger(AcheckServiceImpl.class);

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
	

	
	
	
	@Override
	public PrpLAcheckVo findAcheckVoByPk(Long id) {
		PrpLAcheckVo acheckVo = null;
		PrpLAcheck acheck = databaseDao.findByPK(PrpLAcheck.class, id);
		if (acheck != null) {
			acheckVo = new PrpLAcheckVo();
			Beans.copy().from(acheck).to(acheckVo);
		}
		return acheckVo;
	}

	@Override
	public PrpLAcheckMainVo findAcheckMainVoByTaskNo(String taskNo) {
		QueryRule qr = QueryRule.getInstance();
		qr.addEqual("taskId", taskNo);
		PrpLAcheckMainVo acheckMainVo = null;
		PrpLAcheckMain acheckMain = databaseDao.findUnique(PrpLAcheckMain.class, qr);
		if (acheckMain != null) {
			acheckMainVo = Beans.copyDepth().from(acheckMain).to(PrpLAcheckMainVo.class);
		}
		return acheckMainVo;
	}

	@Override
	public PrpLAcheckMainVo findAcheckMainVoById(Long id) {
		PrpLAcheckMain prpLAcheckMain = databaseDao.findByPK(PrpLAcheckMain.class, id);
		PrpLAcheckMainVo vo = new PrpLAcheckMainVo();
		if (prpLAcheckMain != null) {
			Beans.copy().from(prpLAcheckMain).to(vo);
		}

		return vo;
	}

	@Override
	public void saveOrUpdatePrpLAcheck(PrpLAcheckVo acheckVo, SysUserVo uservo) {
		PrpLAcheck acheck = null;
		Date now = new Date();
		if (acheckVo.getId() == null) {// 新增
			acheck = new PrpLAcheck();
			Beans.copy().from(acheckVo).to(acheck);
			acheck.setCreateTime(now);
			acheck.setCreateUser(uservo.getUserCode());
			acheck.setUpdateTime(now);
			acheck.setUpdateUser(uservo.getUserCode());
		} else {// 更新
			acheck = databaseDao.findByPK(PrpLAcheck.class,acheckVo.getId());
			Beans.copy().excludeNull().from(acheckVo).to(acheck);
			acheck.setUpdateTime(now);
			acheck.setUpdateUser(uservo.getUserCode());
		}

		databaseDao.save(PrpLAcheck.class, acheck);
	}

	@Override
	public PrpLAcheckVo findAcheckByLossId(String registNo, String taskType,Integer serialNo, String checkcode) {
		PrpLAcheckVo acheckVo = null;
		QueryRule qr = QueryRule.getInstance();
		qr.addEqual("registNo", registNo);
		qr.addEqual("taskType", taskType);
		qr.addEqual("checkcode", checkcode);
		qr.addNotEqual("underWriteFlag","7");
		if (serialNo != null) {
			qr.addEqual("serialNo", serialNo.toString());
		}

		List<PrpLAcheck> acheckList = databaseDao.findAll(PrpLAcheck.class, qr);
		if (acheckList != null && !acheckList.isEmpty()) {
			List<PrpLAcheckVo> acheckVoList = Beans.copyDepth().from(acheckList).toList(PrpLAcheckVo.class);
			acheckVo = acheckVoList.get(0);
		}

		return acheckVo;
	}

	@Override
	public List<CheckQueryResultVo> getDatas(CheckQueryVo queryVo)throws Exception {

		SqlJoinUtils sqlUtil = getSqlJoinUtils(queryVo);
		
		String sql = sqlUtil.getSql();
		Object[] values = sqlUtil.getParamValues();
		logger.debug("taskQrySql=" + sql);
		System.out.println(sql);
		System.out.println(ArrayUtils.toString(values));
		logger.debug("ParamValues=" + ArrayUtils.toString(values));
		List<Object[]> objects = baseDaoService.getAllBySql(sql, values);
		// 对象转换
		List<CheckQueryResultVo> resultVoList = new ArrayList<CheckQueryResultVo>();
		for (int i = 0; i < objects.size(); i++) {

			Object[] obj = objects.get(i);

			CheckQueryResultVo resultVo = new CheckQueryResultVo();
			resultVo.setRegistNo(obj[1] == null ? "" : obj[1].toString());
			resultVo.setPolicyNo(obj[3] == null ? "" : obj[3].toString());
			resultVo.setInsureCode(obj[12] == null ? "" : obj[12].toString());
			resultVo.setInsureName(obj[13] == null ? "" : obj[13].toString());
			resultVo.setCheckCode(obj[35] == null ? "" : obj[35].toString());
			resultVo.setCheckName(obj[36] == null ? "" : obj[36].toString());
			
			resultVo.setTaskDetail(obj[38] == null ? "" : obj[38].toString());
			resultVo.setAcheckId(obj[39] == null ? "" : obj[39].toString());
			resultVo.setCheckFee(obj[42] == null ? "" : obj[42].toString());
			resultVo.setVeriLoss(obj[43] == null ? "" : obj[43].toString());
			String acheckId = obj[39].toString();
			SqlJoinUtils sqlUtil1 = new SqlJoinUtils();
			sqlUtil1.append("Select max(lossDate) From PrpLAcheck as acheck where 1=1 ");
			List<Long> paramValues = new ArrayList<Long>();
			if (acheckId.indexOf(",") > 0) {
				String[] id = acheckId.split(",");
				String maskid = SqlJoinUtils.arrayToMask(id);
				sqlUtil1.append("and id in (" + maskid + ") ");
				for (String idx : id) {
					paramValues.add(Long.decode(idx));
				}
			} else {
				sqlUtil1.append("and id=?");
				paramValues.add(Long.decode(acheckId));
			}
			String hql = sqlUtil1.getSql();
			Date lossDate = databaseDao.findUniqueByHql(Date.class, hql,paramValues.toArray());
			resultVo.setTaskType(queryVo.getTaskType()); // 设置结果集任务类型
			resultVo.setLossDate(lossDate);
			resultVo.setUserName(this.findHandlerName(acheckId));
			resultVo.setPhotoCount(this.getImageCount(obj[1].toString(),acheckId));
			if ("0".equals(queryVo.getCaseType())) {// 案件类型,0-正常案件,1-我方代查勘,2-代我方查勘
				resultVo.setIsSurvey("否");// 是否代查勘案件
			} else {
				resultVo.setIsSurvey("是");// 是否代查勘案件
			}
			resultVoList.add(resultVo);
		}

		return resultVoList;
	}

	private String findHandlerName(String acheckId) {
		SqlJoinUtils sqlUtil = new SqlJoinUtils();
		sqlUtil.append(" From PrpLAcheck where 1=1 ");
		List<Long> paramValue = new ArrayList<Long>();
		if (acheckId.indexOf(",") > 0) {
			String[] id = acheckId.split(",");
			String maskid = SqlJoinUtils.arrayToMask(id);
			sqlUtil.append("and id in (" + maskid + ") ");
			for (String idx : id) {
				paramValue.add(Long.decode(idx));
			}
		} else {
			sqlUtil.append("and id=?");
			paramValue.add(Long.decode(acheckId));
		}
		String hql2 = sqlUtil.getSql();
		System.out.println("=========" + hql2);
		StringBuffer userName = new StringBuffer();
		List<PrpLAcheck> acheckList = databaseDao.findAllByHql(
				PrpLAcheck.class, sqlUtil.getSql(), paramValue.toArray());
		int i = 0;
		if (acheckList != null && acheckList.size() > 0) {

			for (PrpLAcheck acheck : acheckList) {
				if (i > 0) {
					userName.append(",");
				}
				
				userName.append(codeTranService.findCodeName("UserCode",
						acheck.getCreateUser()));
				i++;
			}
		}
		return userName.toString();
	}

	
	public int getImageCount(String registNo, String acheckId) {
		Map<String, String> idMap = new HashMap<String, String>();
		List<Long> idList = new ArrayList<Long>();
		if(acheckId.indexOf(",")>0){
			String[] id = acheckId.split(",");
			for(String idx:id){
				if(!idMap.containsKey(idx)){
					idMap.put(idx, idx);
					idList.add(Long.decode(idx));
				}
			}
		}else{
			idMap.put(acheckId, acheckId);
			idList.add(Long.decode(acheckId));
		}
		int count = 0;
		QueryRule qr = QueryRule.getInstance();
		qr.addEqual("registNo", registNo);
		qr.addIn("id", idList);
		List<PrpLAcheck> prpLAchecks = databaseDao.findAll(PrpLAcheck.class, qr);
		if (prpLAchecks != null && prpLAchecks.size() > 0) {
			List<PrpLAcheckVo> acheckVoList = Beans.copyDepth().from(prpLAchecks).toList(PrpLAcheckVo.class);
			Map<String, String> createUserMap = new HashMap<String, String>();
			for(PrpLAcheckVo prpLAcheckVo : acheckVoList){
		    	if ((prpLAcheckVo != null && prpLAcheckVo.getSumImgs() != null)
		    			&& (!createUserMap.containsKey(prpLAcheckVo.getCreateUser()))) {//排除相同的查勘人员
					count = count + prpLAcheckVo.getSumImgs();
					createUserMap.put(prpLAcheckVo.getCreateUser(), prpLAcheckVo.getCreateUser());
				}
		    }
		}
	    	
	        return count;
	    }
	
	@Override
	public List<CheckQueryResultVo> findAcheckMainVo(String taskNo) {
		QueryRule qr = QueryRule.getInstance();
		qr.addEqual("taskId", taskNo);
		PrpLAcheckMain acheckMain = databaseDao.findUnique(PrpLAcheckMain.class, qr);
		// 对象转换
		List<CheckQueryResultVo> resultVoList = new ArrayList<CheckQueryResultVo>();
		if (acheckMain != null && acheckMain.getPrpLCheckFees() != null && acheckMain.getPrpLCheckFees().size() > 0) {
			List<PrpLCheckFee> fee = acheckMain.getPrpLCheckFees();
			// /{"registNo","policyNo","insureName","checkCode","checkName","lossDate","payment","taskStatus","taskDetail","amount","id","taskType","checkFee","veriLoss"};
			for (int i = 0; i < fee.size(); i++) {
				CheckQueryResultVo resultVo = new CheckQueryResultVo();
				resultVo.setRegistNo(fee.get(i).getRegistNo());
				resultVo.setPolicyNo(fee.get(i).getPolicyNo());
				resultVo.setInsureName(fee.get(i).getInsurename());
				resultVo.setCheckCode(fee.get(i).getInsurecode());
				resultVo.setCheckName(acheckMain.getCheckname());
				resultVo.setClaimNo(fee.get(i).getClaimNo());
				resultVo.setCompensateNo(fee.get(i).getCompensateNo());
				resultVo.setKindCode(fee.get(i).getKindCode());
				resultVo.setTaskDetail(fee.get(i).getTaskDetail());
				resultVo.setAcheckId(acheckMain.getId().toString());

				String acheckId = fee.get(i).getTaskIds(); // 查询最大的核损通过时间
				SqlJoinUtils sqlUtil1 = new SqlJoinUtils();
				sqlUtil1.append("Select max(lossDate) From PrpLAcheck  where 1=1 ");
				List<Long> paramValues = new ArrayList<Long>();
				if (acheckId.indexOf(",") > 0) {
					String[] id = acheckId.split(",");
					String maskid = SqlJoinUtils.arrayToMask(id);
					sqlUtil1.append("and id in (" + maskid + ") ");
					for (String idx : id) {
						paramValues.add(Long.decode(idx));
					}
				} else {
					sqlUtil1.append("and id=?");
					paramValues.add(Long.decode(acheckId));
				}
				String hql = sqlUtil1.getSql();
				Date lossDate = databaseDao.findUniqueByHql(Date.class, hql,
						paramValues.toArray());
				resultVo.setLossDate(lossDate);

				resultVo.setTaskType("audio"); // acheckId
				resultVoList.add(resultVo);
			}
		}
		return resultVoList;
	}

	@Override
	public ResultPage<CheckQueryResultVo> findPageForACheck(CheckQueryVo queryVo)throws Exception {
     SqlJoinUtils sqlUtil = getSqlJoinUtils(queryVo);
		
		// 开始记录数
		int start = queryVo.getStart();
		// 查询记录数量
		int length = queryVo.getLength();

		String sql = sqlUtil.getSql();
		Object[] values = sqlUtil.getParamValues();

		logger.debug("taskQrySql=" + sql);
		System.out.println("taskQrySql=" + sql);
		logger.debug("ParamValues=" + ArrayUtils.toString(values));
		System.out.println("ParamValues=" + ArrayUtils.toString(values));

		Page<Object[]> page = baseDaoService.pagedSQLQuery(sql, start, length,values);

		long pageLengthX = page.getTotalCount();

		// 对象转换
		List<CheckQueryResultVo> resultVoList = new ArrayList<CheckQueryResultVo>();
		for (int i = 0; i < page.getResult().size(); i++) {

			Object[] obj = page.getResult().get(i);
			CheckQueryResultVo resultVo = new CheckQueryResultVo();
			resultVo.setRegistNo(obj[1].toString());
			resultVo.setPolicyNo(obj[3].toString());
			resultVo.setInsureCode(obj[12].toString());
			resultVo.setInsureName(obj[13] == null ? "" : obj[13].toString());
			resultVo.setCheckCode(obj[35].toString());
			resultVo.setCheckName(obj[36].toString());
			resultVo.setTaskDetail(obj[38].toString());
			if (obj[37] != null) {
				SimpleDateFormat format = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				Date d = format.parse(obj[37].toString());
				resultVo.setEndCaseTime(d);
			}
			String acheckId = obj[39].toString();
			resultVo.setCheckFee(obj[42] == null ? "" : obj[42].toString());
			resultVo.setVeriLoss(obj[43] == null ? "" : obj[43].toString());
			SqlJoinUtils sqlUtil1 = new SqlJoinUtils();
			sqlUtil1.append("Select max(lossDate) From PrpLAcheck  where 1=1 ");
			List<Long> paramValues = new ArrayList<Long>();
			if (acheckId.indexOf(",") > 0) {
				String[] id = acheckId.split(",");
				String maskid = SqlJoinUtils.arrayToMask(id);
				sqlUtil1.append("and id in (" + maskid + ") ");
				for (String idx : id) {
					paramValues.add(Long.decode(idx));
				}
			} else {
				sqlUtil1.append("and id=?");
				paramValues.add(Long.decode(acheckId));
			}
			String hql = sqlUtil1.getSql();
			Date lossDate = databaseDao.findUniqueByHql(Date.class, hql,
					paramValues.toArray());
			resultVo.setUserName(this.findHandlerName(acheckId));
			resultVo.setPhotoCount(this.getImageCount(obj[1].toString(),acheckId)); 
			resultVo.setLossDate(lossDate);
			resultVoList.add(resultVo);
		}
		// }
		ResultPage<CheckQueryResultVo> resultPage = new ResultPage<CheckQueryResultVo>(
				start, length, pageLengthX, resultVoList);

		return resultPage;
	}

	@Override
	public Double applyAcheckTask(List<List<Object>> objects, SysUserVo userVo)throws Exception {
		PrpLAcheckMainVo mainVo = new PrpLAcheckMainVo();
		String taskId = billNoService.getCheckFeeNo(userVo.getComCode(), "01");;// 暂时写死，到时候从excel取
		mainVo.setTaskId(taskId);
		mainVo.setCheckcode(objects.get(0).get(3).toString());// 获取首条查勘机构
		mainVo.setCheckname(objects.get(0).get(4).toString());// 获取查勘机构名称
		// 校验查勘机构
		String comCode = "";
		String checkBankMianId = "";
		String CheckNameDetail = "";
		List<PrpdCheckBankMainVo> prpdcheckBankMainlist = managerService.findCheckListByHql(userVo.getComCode());
				
		if (prpdcheckBankMainlist == null || prpdcheckBankMainlist.size() == 0) {
			throw new RuntimeException("第" + 2 + "行数据校验不通过，该查勘机构下无有效案件！");
		}
		BigDecimal sumAmount = new BigDecimal(0);
		List<PrpLCheckFeeVo> acheckFeeVos = new ArrayList<PrpLCheckFeeVo>();
		// 解析读取出来的数据
		for (int i = 0; i < objects.size(); i++) {
			List<Object> object = objects.get(i);
			String registNo = object.get(0).toString();
			String policyNo = object.get(1).toString();
			String insureName = object.get(2).toString();
			String checkCode = object.get(3).toString();
			String checkName = object.get(4).toString();
			String lossDate = object.get(5).toString();
			String payment = object.get(6).toString();
			String taskStatus = object.get(7).toString(); // 案件状态 验证
			String taskDetail = object.get(8).toString();
			String amount = object.get(11).toString();
			String isSurvey = object.get(16).toString();
			sumAmount = sumAmount.add(new BigDecimal(amount));
			String ids = object.get(12).toString();
			String taskType = object.get(13).toString();
			// 修改定损查勘表的核损标志--1，发起查勘任务
			if ("".equals(comCode)) {
				comCode = objects.get(0).get(3).toString();
			}

			if (!comCode.equals(checkCode)) {
				throw new RuntimeException("第" + (i + 2)
						+ "行数据校验不通过，二级机构错误;报案号：" + registNo);
			}

			if ("".equals(checkCode) || "".equals(ids) || "".equals(taskType)) {
				throw new RuntimeException("第" + (i + 2)
						+ "行数据校验不通过，请不要修改隐藏域;报案号：" + registNo);
			}

			if (!"是".equals(isSurvey)) {// 代查勘案件在查勘费导入时，不进行查勘机构有效性检验
				boolean flag = true;
				for (PrpdCheckBankMainVo checkBank : prpdcheckBankMainlist) {
					if (comCode.equals(checkBank.getCheckCode())) {
						flag = false;
					}
				}
				if (flag) {
					throw new RuntimeException("第" + (i + 2)
							+ "行数据校验不通过，查勘机构验证不通过;报案号：" + registNo);
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
					PrpLAcheckVo acheckVo = updatePrpLAcheck(Long.valueOf(id), registNo, checkCode, AcheckUnderWriteFlag.CheckTask, i, amount, userVo);
					if (StringUtils.isBlank(checkBankMianId)
							|| StringUtils.isBlank(CheckNameDetail)) {
						checkBankMianId = acheckVo.getCheckmid()+ "";
						CheckNameDetail = acheckVo.getCheckmnamedetail();
					} else if (StringUtils.isNotBlank(checkBankMianId)) {
						if (!checkBankMianId.equals(acheckVo.getCheckmid() + "")) {
							throw new IllegalArgumentException("第" + (i + 2)
									+ "行数据校验不通过，三级机构不一致！;报案号：" + registNo);
						}
					}
					if (CheckTaskType.CHK.equals(acheckVo.getTaskType())) {//
						map.put(CheckTaskType.CHK, acheckVo.getKindCode());
					} else if (CheckTaskType.CAR.equals(acheckVo.getTaskType())) {
						if (map.containsKey(CheckTaskType.CAR)
								&& "1".equals(acheckVo.getSerialNo())) {// 如果包含车辆险别且为标的车时覆盖
							map.put(CheckTaskType.CAR, acheckVo.getKindCode());
						} else {
							map.put(CheckTaskType.CAR, acheckVo.getKindCode());
						}
					} else if (CheckTaskType.PROP.equals(acheckVo.getTaskType())) {
						if (map.containsKey(CheckTaskType.PROP)
								&& "1".equals(acheckVo.getSerialNo())) {// 如果包含车辆险别且为标的车时覆盖
							map.put(CheckTaskType.PROP, acheckVo.getKindCode());
						} else {
							map.put(CheckTaskType.PROP, acheckVo.getKindCode());
						}
					}
				}
			} else {
				PrpLAcheckVo acheckVo = updatePrpLAcheck(Long.valueOf(ids), registNo, checkCode, AcheckUnderWriteFlag.CheckTask, i, amount, userVo);
				if (StringUtils.isBlank(checkBankMianId)) {
					checkBankMianId = acheckVo.getCheckmid() + "";
				} else {
					if (!checkBankMianId.equals(acheckVo.getCheckmid() + "")) {
						throw new IllegalArgumentException("第" + (i + 2)
								+ "行数据校验不通过，三级机构不一致！;报案号：" + registNo);
					}
				}
				if (StringUtils.isBlank(CheckNameDetail)) {
					CheckNameDetail = acheckVo.getCheckmnamedetail();
				}
				kindCode = acheckVo.getKindCode();
			}

			if (StringUtils.isBlank(kindCode)) {
				if (map.containsKey(CheckTaskType.CHK)) {
					kindCode = map.get(CheckTaskType.CHK);
				} else if (map.containsKey(CheckTaskType.CAR)) {
					kindCode = map.get(CheckTaskType.CAR);
				} else if (map.containsKey(CheckTaskType.PROP)) {
					kindCode = map.get(CheckTaskType.PROP);
				}
			}

			// 导入数据到查勘主表和子表
			PrpLCheckFeeVo checkFeeVo = new PrpLCheckFeeVo();

			checkFeeVo.setPolicyNo(policyNo);
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
					checkFeeVo.setClaimNo(claimVo.getClaimNo());
					checkFeeVo.setCompensateNo(compensateNo);
					checkFeeVo.setPolicyNo(claimVo.getPolicyNo());
				}
			} else {
				throw new IllegalArgumentException("第" + (i + 2)
						+ "行数据校验不通过，案件险别错误请通知管理！;报案号：" + registNo);
			}
			checkFeeVo.setRegistNo(registNo);
			checkFeeVo.setInsurename(insureName);
			checkFeeVo.setInsurecode(checkCode);
			checkFeeVo.setKindCode(kindCode);
			checkFeeVo.setPayAmount(new BigDecimal(payment));
			checkFeeVo.setAmount(new BigDecimal(amount));
			checkFeeVo.setTaskIds(ids);
			checkFeeVo.setTaskDetail(taskDetail);
			checkFeeVo.setCreateTime(new Date());
			checkFeeVo.setCreateUser(userVo.getUserCode());
			checkFeeVo.setUpdateTime(new Date());
			checkFeeVo.setUpdateUser(userVo.getUserCode());
			acheckFeeVos.add(checkFeeVo);
		}
		mainVo.setCheckmId(Long.valueOf(checkBankMianId));
		mainVo.setCheckmNameDetail(CheckNameDetail);
		mainVo.setComCode(userVo.getComCode());
		mainVo.setSumAmount(sumAmount);
		mainVo.setPrpLCheckFees(acheckFeeVos);;
		mainVo.setCreateUser(userVo.getUserCode());
		mainVo.setCreateTime(new Date());
		mainVo.setUpdateUser(userVo.getUserCode());
		mainVo.setUpdateTime(new Date());

		Long id = this.saveOrUpdatePrpLAcheckMain(mainVo, userVo);
		mainVo.setId(id);
		// 发起查勘费任务
		WfTaskSubmitVo submitVo = new WfTaskSubmitVo();
		submitVo.setComCode(userVo.getComCode());
		submitVo.setTaskInUser(userVo.getUserCode());
		submitVo.setAssignCom(userVo.getComCode());
		submitVo.setAssignUser(userVo.getUserCode());

		PrpLWfTaskVo taskVo = wfTaskHandleService.addAcheckTask(mainVo,
				submitVo);
		return taskVo.getTaskId().doubleValue();
	}

	@Override
	public PrpLAcheckVo updatePrpLAcheck(Long id, String registNo,
			String checkCode, String underWriteFlag, int index, String amount,
			SysUserVo userVo) throws Exception {
		PrpLAcheckVo acheckVo = null;
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("id", id);
		queryRule.addEqual("registNo", registNo);
		queryRule.addEqual("checkcode", checkCode);
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
						+ "行，只有承保机构才能导入对应案件查勘费;报案号：" + registNo);
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
		PrpLAcheck prpLAcheck = databaseDao.findUnique(PrpLAcheck.class,
				queryRule);
		if (prpLAcheck != null) {
			if (AcheckUnderWriteFlag.Verify.equals(prpLAcheck
					.getUnderWriteFlag())) {// 未导入
				prpLAcheck.setUpdateTime(new Date());
				prpLAcheck.setUpdateUser(userVo.getUserCode());
				prpLAcheck.setUnderWriteFlag(underWriteFlag);// 提交查勘费任务
				databaseDao.update(PrpLAcheck.class, prpLAcheck);
			} else {
				throw new RuntimeException("第" + (index + 2)
						+ "行数据已经导入过，请使用最新的导出数据进行导入;报案号：" + registNo);
			}
			acheckVo = new PrpLAcheckVo();
			Beans.copy().from(prpLAcheck).to(acheckVo);
		} else {
			throw new RuntimeException("第" + (index + 2)
					+ "行数据校验不通过，请使用导出的正确数据进行导入;报案号：" + registNo);
		}

		return acheckVo;
	}

	@Override
	public Long saveOrUpdatePrpLAcheckMain(PrpLAcheckMainVo mainVo,
			SysUserVo userVo) {
		PrpLAcheckMain acheckMain = null;
		Date date = new Date();
		if (mainVo.getId() == null) {// 新增
			acheckMain = Beans.copyDepth().from(mainVo)
					.to(PrpLAcheckMain.class);
			acheckMain.setCreateTime(date);
			acheckMain.setCreateUser(userVo.getUserCode());
			acheckMain.setUpdateUser(userVo.getUserCode());
			acheckMain.setUpdateTime(date);

			// 设置主子表关系
			for (PrpLCheckFee acheckFee : acheckMain
					.getPrpLCheckFees()) {
				acheckFee.setPrpLAcheckMain(acheckMain);;
				acheckFee.setCreateTime(date);
				acheckFee.setCreateUser(userVo.getUserCode());
				acheckFee.setUpdateTime(date);
				acheckFee.setUpdateUser(userVo.getUserCode());
			}
		} else {
			acheckMain = databaseDao.findByPK(PrpLAcheckMain.class,
					mainVo.getId());
			Beans.copy().excludeNull().from(mainVo).to(acheckMain);
			acheckMain.setUpdateUser(userVo.getUserCode());
			acheckMain.setUpdateTime(date);

			List<PrpLCheckFeeVo> feeVos = mainVo.getPrpLCheckFees();
			List<PrpLCheckFee> acheckFees = acheckMain
					.getPrpLCheckFees();
			mergeList(feeVos, acheckFees, "id", PrpLCheckFee.class);
			// 设置主子表关系
			for (PrpLCheckFee acheckFee : acheckFees) {
				acheckFee.setPrpLAcheckMain(acheckMain);;
				acheckFee.setUpdateTime(date);
				acheckFee.setUpdateUser(userVo.getUserCode());
			}
		}
		databaseDao.save(PrpLAcheckMain.class, acheckMain);
		return acheckMain.getId();
	}

	@Override
	public List<Map<String, Object>> createExcelRecord(
			List<CheckQueryResultVo> results) throws Exception {
		List<Map<String, Object>> listmap = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sheetName", "sheet1");
		listmap.add(map);
		for (CheckQueryResultVo resultVo : results) {
			Map<String, Object> mapValue = new HashMap<String, Object>();
			mapValue.put("registNo", resultVo.getRegistNo());
			mapValue.put("policyNo", resultVo.getPolicyNo());
			mapValue.put("insureName", resultVo.getInsureName());
			mapValue.put("checkName", resultVo.getCheckName());
			mapValue.put("checkCode", resultVo.getCheckCode());
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
						resultVo.getAcheckId());
				payAmount = queryPayAmount(resultVo.getRegistNo(),
						resultVo.getAcheckId());
			}
			mapValue.put("amount", amount);
			mapValue.put("payment", payAmount);
			String status = "未注销";
			if ("9".equals(resultVo.getWorkStatus())) {
				status = "已注销";
			}
			mapValue.put("taskStatus", status);
			mapValue.put("id", resultVo.getAcheckId());
			mapValue.put("taskType", resultVo.getTaskType());
			mapValue.put("checkFee", resultVo.getCheckFee());
			mapValue.put("veriLoss", resultVo.getVeriLoss());
			mapValue.put("isSurvey", resultVo.getIsSurvey());
			listmap.add(mapValue);
		}
		return listmap;
	}

	@Override
	public void submitCancel(Double flowTaskId, SysUserVo userVo)
			throws Exception {
				// 注销工作流任务
				wfTaskHandleService.cancelTask(userVo.getUserCode(), new BigDecimal(
						flowTaskId));
				// 回写主表审核状态
				PrpLWfTaskVo wfTaskVo = wfTaskHandleService.queryTask(flowTaskId);

				PrpLAcheckMainVo acheckMainVo = findAcheckMainVoByTaskNo(wfTaskVo
						.getRegistNo());
				acheckMainVo.setUnderWriteFlag("7");// 注销
				acheckMainVo.setUnderWriteDate(new Date());
				acheckMainVo.setUnderwriteuser(userVo.getUserCode());
				saveOrUpdatePrpLAcheckMain(acheckMainVo, userVo);

				// 更新定损主表数据未导入
				for (PrpLCheckFeeVo checkFeeVo : acheckMainVo
						.getPrpLCheckFees()) {
					if (checkFeeVo.getTaskIds().indexOf(",") > 0) {
						for (String id : checkFeeVo.getTaskIds().split(",")) {
							updateAcheck(Long.valueOf(id),
									AcheckUnderWriteFlag.Verify);
						}
					} else {
						updateAcheck(Long.valueOf(checkFeeVo.getTaskIds()),
								AcheckUnderWriteFlag.Verify);
					}
				}
		
	}

	@Override
	public void updateCheckFee(List<PrpLCheckFeeVo> feeVoList) {
		if (feeVoList != null && feeVoList.size() > 0) {
			for (PrpLCheckFeeVo feeVo : feeVoList) {
				PrpLCheckFee fee = databaseDao.findByPK(
						PrpLCheckFee.class, feeVo.getId());
				if (fee != null) {
					fee.setAmount(feeVo.getAmount());
					fee.setRemark(feeVo.getRemark());
					databaseDao.update(PrpLCheckFee.class, fee);
				}
			}
		}
	}

	@Override
	public void updateSumAmountFee(PrpLAcheckMainVo mainVo) {
		if (mainVo != null) {
			PrpLAcheckMain main = databaseDao.findByPK(
					PrpLAcheckMain.class, mainVo.getId());
			if (main != null) {
				main.setSumAmount(mainVo.getSumAmount());
				databaseDao.update(PrpLAcheckMain.class, main);
			}
		}
		
	}

	@Override
	public void updateUnderWriteFlag(PrpLAcheckMainVo mainVo) {
		if (mainVo != null) {
			PrpLAcheckMain main = databaseDao.findByPK(
					PrpLAcheckMain.class, mainVo.getId());
			if (main != null) {
				main.setUnderWriteFlag(AcheckUnderWriteFlag.CheckVerify); // 查勘费审核通过
				main.setUnderWriteDate(new Date());
				main.setUnderwriteuser(mainVo.getUnderwriteuser());
				databaseDao.update(PrpLAcheckMain.class, main);
			}
		}
		
	}

	@Override
	public String queryPayAmount(String registNo, String acheckId) {
		String sql = "from PrpLAcheckMain where 1=1 AND id = ?";
		int sum = 0;
		if (StringUtils.isNotBlank(registNo)
				&& StringUtils.isNotBlank(acheckId)) {
			PrpLAcheckMain main = databaseDao.findUniqueByHql(
					PrpLAcheckMain.class, sql, Long.parseLong(acheckId));
			if (main != null && main.getPrpLCheckFees().size() > 0) {
				for (PrpLCheckFee fee : main.getPrpLCheckFees()) {
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
	public String queryAmount(String registNo, String acheckId) {
				String sql = "from PrpLAcheckMain where 1=1 AND id = ?";
				int sum = 0;
				if (StringUtils.isNotBlank(registNo)
						&& StringUtils.isNotBlank(acheckId)) {
					PrpLAcheckMain main = databaseDao.findUniqueByHql(
							PrpLAcheckMain.class, sql, Long.parseLong(acheckId));
					if (main != null && main.getPrpLCheckFees().size() > 0) {
						for (PrpLCheckFee fee : main.getPrpLCheckFees()) {
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
	public PrpLCheckFeeVo findCheckFeeVoByComp(String registNo,String compensateNo) {
			QueryRule qr = QueryRule.getInstance();
			qr.addEqual("compensateNo", compensateNo);
			qr.addEqual("registNo", registNo);
			PrpLCheckFeeVo checkFeeVo = null;
			List<PrpLCheckFee> acheckFeeList = databaseDao.findAll(
					PrpLCheckFee.class, qr);
			if (acheckFeeList != null && acheckFeeList.size() > 0) {
				for (PrpLCheckFee prpLCheckFee : acheckFeeList) {
					if ("3".equals(prpLCheckFee.getPrpLAcheckMain()
							.getUnderWriteFlag())) {
						checkFeeVo = Beans.copyDepth().from(prpLCheckFee).to(PrpLCheckFeeVo.class);
					}
				}
			}
			return checkFeeVo;
	}
	// 结案保存结案时间到查勘任务表 取第一个结案时间
	@Override
	public void saveEndCaseTimeToAcheck(String registNo, Date Date) {
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo", registNo);
		List<PrpLAcheck> acheckList = databaseDao.findAll(PrpLAcheck.class, queryRule);
		if (acheckList != null && acheckList.size() > 0) {
			for (PrpLAcheck acheck : acheckList) {
				if (acheck.getEndCaseTime() == null) {
					acheck.setEndCaseTime(Date);
					databaseDao.update(PrpLAcheck.class, acheck);
				}
			}
		}
		
	}

	@Override
	public PrpLCheckFeeVo findCheckFeeVoByComp(String compensateNo) {
		QueryRule qr = QueryRule.getInstance();
		qr.addEqual("compensateNo", compensateNo);
		PrpLCheckFeeVo checkFeeVo = null;
		List<PrpLCheckFee> checkFeeList = databaseDao.findAll(PrpLCheckFee.class, qr);
		// 查询审核通过的查勘费审核任务
		if (checkFeeList != null && checkFeeList.size() > 0) {
			for (PrpLCheckFee checkFeePo : checkFeeList) {
				if ("3".equals(checkFeePo.getPrpLAcheckMain().getUnderWriteFlag())) {
					checkFeeVo = Beans.copyDepth().from(checkFeePo).to(PrpLCheckFeeVo.class);
					continue;
				}
			}
		}
		return checkFeeVo;
	}

	@Override
	public void updateCheckFee(PrpLCheckFeeVo feeVo) {
		PrpLCheckFee fee = databaseDao.findByPK(PrpLCheckFee.class,feeVo.getId());
		Beans.copy().from(feeVo).excludeNull().to(fee);       
		if (fee != null) {
			fee.setUpdateTime(new Date());
			databaseDao.update(PrpLCheckFee.class, fee);
		}
	}

	@Override
	public PrpLAcheckMainVo findAcheckMainVoByCompNo(String compensateNo) {
		QueryRule qr = QueryRule.getInstance();
		qr.addEqual("compensateNo", compensateNo);
		PrpLAcheckMainVo acheckMainVo = null;
		List<PrpLCheckFee> acheckFeeList = databaseDao.findAll(PrpLCheckFee.class, qr);
		// 查询审核通过的查勘费审核任务
		if (acheckFeeList != null && acheckFeeList.size() > 0) {
			for (PrpLCheckFee checkFeePo : acheckFeeList) {
				if (checkFeePo.getPrpLAcheckMain() != null
						&& "3".equals(checkFeePo.getPrpLAcheckMain().getUnderWriteFlag())) {
					acheckMainVo = Beans.copyDepth().from(checkFeePo.getPrpLAcheckMain())
							.to(PrpLAcheckMainVo.class);
					continue;
				}
			}

		}
		return acheckMainVo;
	}

	@Override
	public List<PrpLCheckFeeVo> findPrpLCheckFeeVoByRegistNo(String registNo) {
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo", registNo);
		List<PrpLCheckFee> listPo = databaseDao.findAll(PrpLCheckFee.class, queryRule);
		List<PrpLCheckFeeVo> listVo = new ArrayList<PrpLCheckFeeVo>();
		if (listPo != null && listPo.size() > 0) {
			for (PrpLCheckFee po : listPo) {
				PrpLCheckFeeVo vo = new PrpLCheckFeeVo();
				Beans.copy().from(po).to(vo);
				if (po.getPrpLAcheckMain() != null) {
					vo.setAcheckMainId(po.getPrpLAcheckMain().getId());
					vo.setTaskNumber(po.getPrpLAcheckMain().getTaskId());
					vo.setCheckname(po.getPrpLAcheckMain().getCheckname());
				}

				listVo.add(vo);
			}
		}
		return listVo;
	}
	/**
	 * 判断是否要加时间范围控制
	 * <pre></pre>
	 * @param taskQueryVo
	 * @return
	 * @modified:
	 * ☆XiaoHuYao(2019年8月7日 下午5:23:22): <br>
	 */
	private boolean  addTimeControl(PrpLWfTaskQueryVo taskQueryVo){
		String bussNo = taskQueryVo.getBussNo(); //业务号
		String claimNo = taskQueryVo.getClaimNo(); //立案号
			//如果报案号、保单号、标的车牌号、被保险人、车架号、立案号符合添加忽略出险时间查询
		if((StringUtils.isNotBlank(bussNo))
			|| (StringUtils.isNotBlank(claimNo))){
			return false;
		}	
		return true;
	}
	@Override
	public ResultPage<PrpLPayBankVo> search(PrpLWfTaskQueryVo prplWfTaskQueryVo, int start, int length,String handleStatus) throws ParseException {

		SqlJoinUtils sqlUtil = new SqlJoinUtils();
		sqlUtil.append(" from PrpDAccRollBackAccount a,PrpLCheckFee b ,PrpLAcheckMain c where 1=1 and (a.certiNo=b.compensateNo or a.certiNo=b.endNo) and b.prpLAcheckMain.id=c.id ");
		sqlUtil.append(" and  a.payType = ? ");
		sqlUtil.addParamValue(CodeConstants.PayReason.CHECKFEE_PAY_Res);
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
		// 查勘机构checkCode
		if (StringUtils.isNotBlank(prplWfTaskQueryVo.getCheckCode())) {
			sqlUtil.append(" and c.checkmId= ? ");
			sqlUtil.addParamValue(Long.valueOf(prplWfTaskQueryVo.getCheckCode()));
		}
		if(addTimeControl(prplWfTaskQueryVo)){
			// 申请日期
			if (prplWfTaskQueryVo.getTaskInTimeStart() != null && prplWfTaskQueryVo.getTaskInTimeEnd() != null) {
				sqlUtil.append(" and a.rollBackTime >= ? and a.rollBackTime <= ? ");
				sqlUtil.addParamValue(prplWfTaskQueryVo.getTaskInTimeStart());
				sqlUtil.addParamValue(DateUtils.toDateEnd(prplWfTaskQueryVo.getTaskInTimeEnd()));
			}
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
		logger.info("查勘费退票修改sql={},查询参数={}",sql,sqlUtil.getParamValues());
		Page<Object[]> page = databaseDao.findPageByHql(sql, start / length + 1, length, values);
		List<PrpLPayBankVo> payBankVoList = new ArrayList<PrpLPayBankVo>();
		for (int i = 0; i < page.getResult().size(); i++) {
			Object[] obj = page.getResult().get(i);
			PrpLPayBankVo bankVo = new PrpLPayBankVo();

			/*
			 * {"data" : "bussNo"}, //业务号 {"data" : "bankNameName"},//开户银行
			 * {"data" : "accountNo"}, //银行账户 {"data" : "remark"},//修改原因 {"data"
			 * : "appTime"},//申请日期 {"data" : "payTypeName"}//收付原因
			 */PrpDAccRollBackAccount account = (PrpDAccRollBackAccount) obj[0];
			 PrpLCheckFee checkFee = (PrpLCheckFee) obj[1];
			 PrpLAcheckMain aCheckMain = (PrpLAcheckMain) obj[2];
			if (checkFee != null && StringUtils.isNotBlank(checkFee.getEndNo())) {
				bankVo.setBussNo(checkFee.getEndNo());
				bankVo.setRegistNo(checkFee.getRegistNo());
			} else {
				if (checkFee != null && StringUtils.isNotBlank(checkFee.getCompensateNo())) {
					bankVo.setBussNo(checkFee.getCompensateNo());
					bankVo.setRegistNo(checkFee.getRegistNo());
				}
			}
			if (account != null) {
				bankVo.setRemark(account.getErrorMessage());
				bankVo.setAppTime(account.getRollBackTime());
				bankVo.setPayType(account.getPayType());
				bankVo.setBackaccountId(String.valueOf(account.getId()));
			}
			if (aCheckMain != null) {
				PrpdCheckBankMainVo checkBankMainVo = managerService.findCheckById(aCheckMain.getCheckmId());
				if (checkBankMainVo != null) {
					bankVo.setCheckCode(String.valueOf(checkBankMainVo.getId()));
					List<PrpdcheckBankVo> bankVos = managerService.findPrpdcheckBankVosByCheckId(String.valueOf(checkBankMainVo.getId()));

					if (bankVos != null && bankVos.size() > 0) {
						for (PrpdcheckBankVo vo : bankVos) {
							// 通过收付那边返回的accountId配对,
							if (account != null && StringUtils.isNotBlank(account.getAccountId()) && StringUtils.isNotBlank(vo.getId().toString())) {
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
	public ResultPage<PrpLPayBankVo> checkFeeAuditSearch(
			PrpLWfTaskQueryVo prplWfTaskQueryVo, int start, int length,
			String handleStatus) throws ParseException {
		SqlJoinUtils sqlUtil = new SqlJoinUtils();
		sqlUtil.append("FROM PrpLCheckmAudit a,PrpLCheckFee b where 1=1 and (a.bussNo=b.compensateNo or a.bussNo=b.endNo)");
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
			sqlUtil.append(" and a.checkCode= ? ");
			sqlUtil.addParamValue(prplWfTaskQueryVo.getCheckCode());
		}
		if(addTimeControl(prplWfTaskQueryVo)){
			// 流入日期
			if (prplWfTaskQueryVo.getTaskInTimeStart() != null
					&& prplWfTaskQueryVo.getTaskInTimeEnd() != null) {
				sqlUtil.append(" and a.createTime >= ? and a.createTime <= ? ");
				sqlUtil.addParamValue(prplWfTaskQueryVo.getTaskInTimeStart());
				sqlUtil.addParamValue(DateUtils.toDateEnd(prplWfTaskQueryVo
						.getTaskInTimeEnd()));
			}
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
		logger.info("查勘费退票审核查询sql={},参数={}",sql,sqlUtil.getParamValues());
		Page<Object[]> page = databaseDao.findPageByHql(sql,
				start / length + 1, length, params);
		List<PrpLPayBankVo> payBankVoList = new ArrayList<PrpLPayBankVo>();
		for (int i = 0; i < page.getResult().size(); i++) {
			Object[] obj = page.getResult().get(i);
			PrpLPayBankVo bankVo = new PrpLPayBankVo();
			PrpLCheckmAudit checkAudit = (PrpLCheckmAudit) obj[0];
			PrpLCheckFee checkFee = (PrpLCheckFee) obj[1];
			if (checkFee != null
					&& StringUtils.isNotBlank(checkFee.getEndNo())) {
				bankVo.setBussNo(checkFee.getEndNo());
				bankVo.setRegistNo(checkFee.getRegistNo());
			} else {
				if (checkFee != null
						&& StringUtils.isNotBlank(checkFee.getCompensateNo())) {
					bankVo.setBussNo(checkFee.getCompensateNo());
					bankVo.setRegistNo(checkFee.getRegistNo());
				}
			}
			if (checkAudit != null) {
				PrpDAccRollBackAccountVo accountVo = accountQueryService
						.findRollBackAccountById(checkAudit
								.getBackAccountId());
				if (accountVo != null) {
					bankVo.setRemark(accountVo.getErrorMessage());
					bankVo.setAppTime(accountVo.getRollBackTime());
					bankVo.setPayType(accountVo.getPayType());
					bankVo.setBackaccountId(String.valueOf(accountVo.getId()));
				}

				bankVo.setAccountId(String.valueOf(checkAudit.getOldBankId()));// 查勘费退票银行表的Id
				bankVo.setAccountName(checkAudit.getAccountName());// 收款方户名
				bankVo.setBankName(checkAudit.getBankoutLets());// 银行行号
				bankVo.setAccountNo(checkAudit.getAccountCode());// 收款方银行账号
				bankVo.setAuditId(String.valueOf(checkAudit.getId()
						.longValue()));// 查勘费退票表Id
				bankVo.setCheckCode(checkAudit.getCheckCode());// 公估机构
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
	public List<PrpLCheckFeeVo> findPrpLCheckFeeVoByBussNo(
			String bussNo) {
		QueryRule queryRule = QueryRule.getInstance();
		if (StringUtils.isNotBlank(bussNo) && (bussNo.startsWith("P")
				|| bussNo.startsWith("G") || bussNo.startsWith("J"))) {
			queryRule.addEqual("endNo", bussNo);
		} else {
			queryRule.addEqual("compensateNo", bussNo);
		}
		List<PrpLCheckFee> feeLists = databaseDao.findAll(
				PrpLCheckFee.class, queryRule);
		List<PrpLCheckFeeVo> listVos = new ArrayList<PrpLCheckFeeVo>();
		if (feeLists != null && feeLists.size() > 0) {
			for (PrpLCheckFee fee : feeLists) {
				PrpLCheckFeeVo feeVo = new PrpLCheckFeeVo();
				Beans.copy().from(fee).to(feeVo);
				listVos.add(feeVo);
			}
		}

		return listVos;
	}

	@Override
	public int updateSettleNo(String compensateNo, String settleNo,String operateType) {
		QueryRule qr = QueryRule.getInstance();
		qr.addEqual("compensateNo", compensateNo);
		if ("0".equals(operateType)) {
			qr.addEqual("endNo", settleNo);
		}
		List<PrpLCheckFee> PrpLCheckFeeList = databaseDao.findAll(PrpLCheckFee.class, qr);
		int count = 0;
		if (PrpLCheckFeeList != null && PrpLCheckFeeList.size() > 0) {
			for (PrpLCheckFee prpLCheckFee : PrpLCheckFeeList) {
				if ("1".equals(operateType)) {
					count++;
					prpLCheckFee.setEndNo(settleNo);
				} else if ("0".equals(operateType)) {
					count++;
					prpLCheckFee.setEndNo(null);
				}
			}
		}
		return count;
	}

	@Override
	public PrpLCheckmAuditVo findPrpLCheckmAuditVoById(Long id) {
		PrpLCheckmAudit auditPo = databaseDao.findByPK(PrpLCheckmAudit.class,
				new BigDecimal(id));
		PrpLCheckmAuditVo auditVo = new PrpLCheckmAuditVo();
		if (auditPo != null) {
			Beans.copy().from(auditPo).to(auditVo);
		}
		return auditVo;
	}

	@Override
	public void updateOrSaveOfPrpLCheckmAudit(PrpLCheckmAuditVo auditVo)throws Exception {
		if (auditVo != null) {
			if (auditVo.getId() != null) {
				PrpLCheckmAudit auditPo = databaseDao.findByPK(
						PrpLCheckmAudit.class, auditVo.getId());
				Beans.copy().from(auditVo).excludeNull().to(auditPo);
				databaseDao.update(PrpLCheckmAudit.class, auditPo);
			} else {
				PrpLCheckmAudit audit = new PrpLCheckmAudit();
				Beans.copy().from(auditVo).to(audit);
				databaseDao.save(PrpLCheckmAudit.class, audit);
			}
		} else {
			throw new IllegalArgumentException("保存或更新失败,查勘费退票审核表传入数据为空");
		}

		
	}

	@Override
	public void updatAccrollbackaccount(
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
	public void updateInfoFlagOfPrpdaccrollbackaccount(String infoFlag,
			Long backId) throws Exception {
		Long aId = backId;
		PrpDAccRollBackAccount backAccountPo = databaseDao.findByPK(
				PrpDAccRollBackAccount.class, backId);
		if (backAccountPo != null) {
			backAccountPo.setInfoFlag(infoFlag);
			databaseDao.update(PrpDAccRollBackAccount.class, backAccountPo);
		} else {
			throw new IllegalArgumentException("更新infoFlag字段失败！退票表Id为" + aId);
		}
		
	}

	@Override
	public PrpLCheckmAuditVo findCheckFeeAuditByAccountId(
			Long backAccountId) {
		QueryRule rule = QueryRule.getInstance();
		rule.addEqual("backAccountId", backAccountId);
		rule.addDescOrder("createTime");
		List<PrpLCheckmAudit> lists = databaseDao.findAll(
				PrpLCheckmAudit.class, rule);
		PrpLCheckmAuditVo auditVo = new PrpLCheckmAuditVo();
		if (lists != null && lists.size() > 0) {
			Beans.copy().from(lists.get(0)).to(auditVo);
		}

		return auditVo;
	}

	@Override
	public PrpLCheckmAuditVo findPrpLCheckmAuditVoByRegistNoAndBussNoAndStatus(
			String registNo, String bussNo, String status) {
		QueryRule rule = QueryRule.getInstance();
		rule.addEqual("registNo", registNo);
		rule.addEqual("bussNo", bussNo);
		rule.addEqual("status", status);
		List<PrpLCheckmAudit> audits = databaseDao.findAll(
				PrpLCheckmAudit.class, rule);
		PrpLCheckmAuditVo auditVo = new PrpLCheckmAuditVo();
		if (audits != null && audits.size() > 0) {
			Beans.copy().from(audits.get(0)).to(auditVo);
		}

		return auditVo;
	}

	@Override
	public PrpLAcheckVo findPrpLAcheckVo(String registNo, String taskType,
			String underWriteFlag, String licenseNo) {
		QueryRule query=QueryRule.getInstance();
		query.addEqual("registNo", registNo);
		query.addEqual("taskType", taskType);
		query.addEqual("underWriteFlag",underWriteFlag);
	  if(StringUtils.isNotBlank(licenseNo) && "1".equals(taskType)){
		query.addEqual("licenseNo",licenseNo);	
	  }
	   query.addDescOrder("createTime");
	  List<PrpLAcheck>  achecks= databaseDao.findAll(PrpLAcheck.class, query);
	  PrpLAcheckVo vo=null;
	  if(achecks!=null && achecks.size()>0){
		  PrpLAcheck po= achecks.get(0);
		  vo=new PrpLAcheckVo();
		  Beans.copy().from(po).to(vo);
	  }
		return vo;
	}

	@Override
	public void updatePrpLAcheck(PrpLAcheckVo PrpLAcheckVo) {
		if(PrpLAcheckVo !=null && PrpLAcheckVo.getId()!=null){
			PrpLAcheck acheck=new PrpLAcheck();
			Beans.copy().from(PrpLAcheckVo).to(acheck);
			databaseDao.update(PrpLAcheck.class, acheck);
		}
		
	}

	@Override
	public SqlJoinUtils getSqlJoinUtils(CheckQueryVo queryVo) throws Exception {
		SqlJoinUtils sqlUtil = new SqlJoinUtils();
		sqlUtil.append("SELECT * FROM prplwftaskquery qry,");
		sqlUtil.append("(SELECT c.registno reportno,c.checkcode,c.checkname,c.caseTime1 caseTime,to_char(wmsys.wm_concat(c.detail)) taskdetail,to_char(wmsys.wm_concat(c.taskid)) acheckid,to_char(wmsys.wm_concat(c.underwriteflag)) taskflag,max(c.lossdate) lossdate,to_char(wmsys.wm_concat(c.checkfee)) checkfee,to_char(wmsys.wm_concat(c.veriLoss)) veriLoss FROM ");
		sqlUtil.append("(SELECT b.*,decode(b.licenseno,'',b.taskname,b.taskname||'('||b.licenseno||')') detail FROM ");
		sqlUtil.append("(SELECT a.registno,a.checkcode,a.checkname,a.tasktype,a.endcasetime caseTime1,a.taskname,to_char(wmsys.wm_concat(a.underwriteflag)) underwriteflag,to_char(wmsys.wm_concat(a.licenseno)) licenseno,to_char(wmsys.wm_concat(a.id)) taskid,max(a.lossdate) lossdate,to_char(wmsys.wm_concat(a.checkfee)) checkfee,to_char(wmsys.wm_concat(a.veriLoss)) veriLoss FROM ");
		sqlUtil.append("(SELECT acheck.*,decode(tasktype,'0','查勘','1','车辆定损','2','财产定损','人伤跟踪') taskName FROM PrpLAcheck acheck ");
		sqlUtil.append("WHERE 1=1 and underwriteflag in (?,?) ");
		sqlUtil.addParamValue("0");// 查勘费定损
		sqlUtil.addParamValue("1");// 查勘费核损
		String comCode = queryVo.getComCode();
		if (comCode.startsWith("00")) {
			comCode = comCode.substring(0, 4);
		} else {
			comCode = comCode.substring(0, 2);
		}
		sqlUtil.append(" and acheck.comCode LIKE ? ");
		sqlUtil.addParamValue(comCode + "%");

		String taskType = queryVo.getTaskType();
		if (taskType.indexOf(",") > 0) {
			String[] str = taskType.split(",");
			String maskStr = SqlJoinUtils.arrayToMask(str);
			sqlUtil.append(" AND acheck.tasktype in (" + maskStr + ") ");
			for (String s : str) {
				sqlUtil.addParamValue(s);
			}
		} else {
			sqlUtil.append(" AND acheck.tasktype = ? ");
			sqlUtil.addParamValue(taskType);
		}
		// 按照三级机构查询
		sqlUtil.append(" and acheck.checkcode = ? ");
		sqlUtil.addParamValue(queryVo.getCheckCode());

		//sqlUtil.andEquals(queryVo, "acheck", "vin");

		sqlUtil.append(") a ");
		sqlUtil.append("GROUP BY a.registno,a.checkcode,a.checkname,a.tasktype,a.endcasetime,a.taskname) b ) c ");
		sqlUtil.append("GROUP BY c.registno,c.checkcode,c.caseTime1,c.checkname) d ");
		sqlUtil.append("WHERE qry.registno=d.reportno AND instr(taskflag,?)=0 ");
		sqlUtil.addParamValue("0");// 发起过定损任务查询不出来

		sqlUtil.andReverse(queryVo, "qry", 7, "registNo");

		if (queryVo.getLossDateStart() != null && StringUtils.isBlank(queryVo.getLicenseNo()) && StringUtils.isBlank(queryVo.getPolicyNo()) && StringUtils.isBlank(queryVo.getRegistNo())) {
			sqlUtil.andDate(queryVo, "d", "lossDate");
		}
		if (queryVo.getCaseTimeStart() != null && StringUtils.isBlank(queryVo.getLicenseNo()) && StringUtils.isBlank(queryVo.getPolicyNo()) && StringUtils.isBlank(queryVo.getRegistNo())) {
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
	public List<PrpLAcheckVo> findListLAcheckVo(String registNo,
			String createUser) {
		QueryRule query = QueryRule.getInstance();
		query.addEqual("registNo", registNo);
		query.addEqual("createUser", createUser);
		List<PrpLAcheck> achecks = databaseDao.findAll(PrpLAcheck.class,query);
		List<PrpLAcheckVo> acheckVos = null;
		if (achecks != null && achecks.size() > 0) {
			acheckVos = Beans.copyDepth().from(achecks).toList(PrpLAcheckVo.class);
		}
		return acheckVos;
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
	
	private void updateAcheck(Long id, String underWriteFlag) {

		PrpLAcheck acheck = databaseDao.findByPK(PrpLAcheck.class, id);
		acheck.setUnderWriteFlag(underWriteFlag);
		databaseDao.update(PrpLAcheck.class, acheck);
	}

	/* 
	 * @see ins.sino.claimcar.other.service.AcheckService#findSupplymentPageForCheckFee(ins.sino.claimcar.other.vo.AssessorQueryVo)
	 * @param queryVo
	 * @return
	 */
	@Override
	public ResultPage<CheckQueryResultVo> findSupplymentPageForCheckFee(CheckQueryVo queryVo) throws Exception {
		SqlJoinUtils sqlUtil = new SqlJoinUtils();
		sqlUtil.append("select p.registno,"
				+ "(select policy.policyno from prplcmain  policy  where policy.registno = p.registno and  rownum =1)"
				+ ", handleruser,p.underwriteenddate,"
				+ "  (select policy.insuredname from prplcmain  policy  where policy.registno = p.registno and  rownum =1),"
				+ "  case  when tasktype = '0' then  taskname  when tasktype = '1' then taskname || '(' || "
				+ " (select licenseno   from prpldlosscarmain carmain  where handleridkey = carmain.id) || ')'"
				+ " when tasktype = '2' then taskname || '(' || p.itemname || ')' else '人伤' end as taskname,tasktype , handleridkey ");
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
				+ " when SUBNODECODE = 'DLProp'  or subnodecode= 'DLPropAdd' then "
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
		sqlUtil.append(" and not exists (select 1 from PrpLAcheck b  where t.registno = b.registno and b.createuser = t.handleruser and t.handleridkey = b.bussiId ) "
				+ " and (case  when SUBNODECODE = 'Chk' then  chk.chksubmittime "
				+ " when SUBNODECODE = 'DLCar' or  subnodecode= 'DLCarAdd' then carmain.underwriteenddate"
				+ " when SUBNODECODE = 'DLProp' or  subnodecode= 'DLPropAdd' then  propmain.underwriteenddate  else permain.UNDWRTFEEENDDATE end) >=?"
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
		sqlUtil.append(" ) p where rn = 1  order by underwriteenddate desc");
	
		
		// 开始记录数
		int start = queryVo.getStart();
		// 查询记录数量
		int length = queryVo.getLength();

		String sql = sqlUtil.getSql();
		Object[] values = sqlUtil.getParamValues();

		logger.info("查询查勘费补录的taskQrySql=" + sql + "，参数ParamValues =" + ArrayUtils.toString(values));
		long startTime = System.currentTimeMillis();
		Page<Object[]> page = baseDaoService.pagedSQLQuery(sql, start, length,
				values);
		logger.info("查询查勘费补录耗时时间=" + (System.currentTimeMillis()- startTime));

		long pageLengthX = page.getTotalCount();

		// 对象转换
		List<CheckQueryResultVo> resultVoList = new ArrayList<CheckQueryResultVo>();
		for (int i = 0; i < page.getResult().size(); i++) {

			Object[] obj = page.getResult().get(i);
			CheckQueryResultVo resultVo = new CheckQueryResultVo();
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
			resultVo.setTaskType(String.valueOf(obj[6]));
			resultVo.setMainId(Long.valueOf(String.valueOf(obj[7])));
			resultVoList.add(resultVo);
		}
		ResultPage<CheckQueryResultVo> resultPage = new ResultPage<CheckQueryResultVo>(
				start, length, pageLengthX, resultVoList);

		return resultPage;
	}

	/* 
	 * @see ins.sino.claimcar.other.service.AcheckService#findACheckmAuditByParams(java.lang.String, java.lang.String, java.lang.String)
	 * @param registNo
	 * @param compensateNo
	 * @param true1
	 * @return
	 */
	@Override
	public PrpLCheckmAuditVo findACheckmAuditByParams(String registNo, String bussNo, String status) {
		QueryRule rule = QueryRule.getInstance();
		rule.addEqual("registNo", registNo);
		rule.addEqual("bussNo", bussNo);
		rule.addEqual("status", status);
		List<PrpLCheckmAudit> audits = databaseDao.findAll(
				PrpLCheckmAudit.class, rule);
		PrpLCheckmAuditVo auditVo = new PrpLCheckmAuditVo();
		if (audits != null && audits.size() > 0) {
			Beans.copy().from(audits.get(0)).to(auditVo);
		}

		return auditVo;
	}

	@Override
	public List<PrpLAcheckVo> findAcheckByRegistNoAndTaskType(String registNo,String taskType) {
		QueryRule qr = QueryRule.getInstance();
		qr.addEqual("registNo", registNo);
		qr.addEqual("taskType", taskType);
		List<String> values=new ArrayList<String>();
		values.add("0");
		values.add("1");
		qr.addIn("underWriteFlag", values);
        List<PrpLAcheck> acheckList = databaseDao.findAll(PrpLAcheck.class, qr);
        List<PrpLAcheckVo> acheckVoList=null;
		if (acheckList != null && !acheckList.isEmpty()) {
			acheckVoList=new ArrayList<PrpLAcheckVo>();
			acheckVoList = Beans.copyDepth().from(acheckList).toList(PrpLAcheckVo.class);
		}
		
		return acheckVoList; 
	}

	/* 
	 * @see ins.sino.claimcar.other.service.AcheckService#findAcheckByParams(ins.sino.claimcar.other.vo.PrpLAcheckVo)
	 * @param prplAcheck
	 */
	@Override
	public PrpLAcheckVo findAcheckByParams(PrpLAcheckVo prplAcheck) {
		QueryRule rule = QueryRule.getInstance();
		rule.addEqual("registNo", prplAcheck.getRegistNo());
		if( prplAcheck.getBussiId() != null){
			rule.addEqual("bussiId", prplAcheck.getBussiId());
		}
		if(prplAcheck.getTaskType() != null){
			rule.addEqual("taskType", prplAcheck.getTaskType());
		}
		if(prplAcheck.getLicenseNo() != null){
			rule.addEqual("licenseNo", prplAcheck.getLicenseNo());
		}
		if(prplAcheck.getCheckcode() != null){
			rule.addEqual("checkcode", prplAcheck.getCheckcode());
		}
		List<PrpLAcheck> acheckPos = databaseDao.findAll(
				PrpLAcheck.class, rule);
		PrpLAcheckVo prplAcheckVo = null;
		if (acheckPos != null && !acheckPos.isEmpty()) {
			prplAcheckVo = new PrpLAcheckVo();
			Beans.copy().from(acheckPos.get(0)).to(prplAcheckVo);
		}
		return prplAcheckVo;
	}
}
