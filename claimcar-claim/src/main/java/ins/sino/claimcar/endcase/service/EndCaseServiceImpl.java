package ins.sino.claimcar.endcase.service;

import ins.framework.common.ResultPage;
import ins.framework.dao.database.DatabaseDao;
import ins.framework.dao.database.support.Page;
import ins.framework.dao.database.support.QueryRule;
import ins.framework.utils.Beans;
import ins.platform.common.service.facade.BaseDaoService;
import ins.platform.common.service.facade.BillNoService;
import ins.platform.utils.DataUtils;
import ins.platform.utils.SqlJoinUtils;



import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.certify.service.CertifyPubService;
import ins.sino.claimcar.certify.vo.PrpLCertifyMainVo;
import ins.sino.claimcar.check.service.CheckTaskService;
import ins.sino.claimcar.check.vo.PrpLCheckCarInfoVo;
import ins.sino.claimcar.check.vo.PrpLCheckCarVo;
import ins.sino.claimcar.claim.service.CompensateTaskService;
import ins.sino.claimcar.claim.vo.PrpLCompensateVo;
import ins.sino.claimcar.constant.Risk;
import ins.sino.claimcar.endcase.po.PrpLEndCase;
import ins.sino.claimcar.endcase.vo.PrpLEndCaseVo;
import ins.sino.claimcar.losscar.service.LossCarService;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarMainVo;
import ins.sino.claimcar.other.vo.RecPayLaunchResultVo;
import ins.sino.claimcar.other.vo.RecPayLaunchVo;
import ins.sino.claimcar.regist.service.RegistQueryService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;

/**
 * 结案服务类
 * @author ★Luwei
 */
@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"})
@Path("endCaseService")
public class EndCaseServiceImpl implements EndCaseService {
	
	private static Logger logger = LoggerFactory.getLogger(EndCaseServiceImpl.class);

	/** 注入服务 */
	@Autowired
	BaseDaoService baseDaoService;
	@Autowired
	DatabaseDao databaseDao;

	@Autowired
	RegistQueryService registQueryService;

	@Autowired
	CompensateTaskService compensateTaskService;

	@Autowired
	BillNoService billNoService;

	@Autowired
	LossCarService lossCarService;

	@Autowired
	CheckTaskService checkTaskService;
	@Autowired
	CertifyPubService certifyPubService;

	/* 
	 * @see ins.sino.claimcar.endcase.service.EndCaseService#queryEndCaseVo(java.lang.String, java.lang.String)
	 * @param registNo
	 * @param claimNo
	 * @return
	 */
	@Override
	public PrpLEndCaseVo queryEndCaseVo(String registNo,String claimNo) {
		PrpLEndCaseVo endCaseVo = null;
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo",registNo);
		queryRule.addEqual("claimNo",claimNo);
		queryRule.addDescOrder("endCaseDate");
		List<PrpLEndCase> endCasePos = databaseDao.findAll(PrpLEndCase.class,queryRule);
		if(endCasePos!=null&&endCasePos.size()>0){
			
			endCaseVo = Beans.copyDepth().from(endCasePos.get(0)).to(PrpLEndCaseVo.class);
		}
		return endCaseVo;
	}
	
	@Override
	public List<PrpLEndCaseVo> searchEndCaseVo(String registNo,String claimNo){
		List<PrpLEndCaseVo> endCaseVoList = new ArrayList<PrpLEndCaseVo>();
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo",registNo);
		queryRule.addEqual("claimNo",claimNo);
		queryRule.addDescOrder("endCaseDate");
		List<PrpLEndCase> endCasePos = databaseDao.findAll(PrpLEndCase.class,queryRule);
		if(endCasePos!=null&&endCasePos.size()>0){
			endCaseVoList = Beans.copyDepth().from(endCasePos).toList(PrpLEndCaseVo.class);
		}
		return endCaseVoList;
	}
	
	@Override
	public List<PrpLEndCaseVo> findEndCaseVo(String registNo) {
		List<PrpLEndCaseVo> endCaseVoList = new ArrayList<PrpLEndCaseVo>();
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo",registNo);
		queryRule.addDescOrder("endCaseDate");
		List<PrpLEndCase> endCasePos = databaseDao.findAll(PrpLEndCase.class,queryRule);
		if(endCasePos!=null&&endCasePos.size()>0){
			endCaseVoList = Beans.copyDepth().from(endCasePos).toList(PrpLEndCaseVo.class);
		}
		return endCaseVoList;

	}

	//
	/* 
	 * @see ins.sino.claimcar.endcase.service.EndCaseService#getEndCaseNum(java.lang.String, java.lang.String, java.lang.String)
	 * @param registNo
	 * @param claimNo
	 * @param compeNo
	 * @return
	 */
	@Override
	public int getEndCaseNum(String registNo,String claimNo,String compeNo) {
		QueryRule queryRule = QueryRule.getInstance();
		if(StringUtils.isNotBlank(registNo)){
			queryRule.addEqual("registNo",registNo);
		}
		if(StringUtils.isNotBlank(claimNo)){
			queryRule.addEqual("claimNo",claimNo);
		}
		if(StringUtils.isNotBlank(compeNo)){
			queryRule.addEqual("compensateNo",compeNo);
		}
		List<PrpLEndCase> endCasePos = databaseDao.findAll(PrpLEndCase.class,queryRule);
		return endCasePos.size();
	}

	/* 
	 * @see ins.sino.claimcar.endcase.service.EndCaseService#getDLossCarInfo(java.lang.String)
	 * @param registNo
	 * @return
	 */
	@Override
	public PrpLDlossCarMainVo getDLossCarInfo(String registNo) {
		PrpLDlossCarMainVo dlossCarVo = null;
		List<PrpLDlossCarMainVo> dlossCarVoList = lossCarService.findLossCarMainByRegistNo(registNo);
		if(dlossCarVoList!=null&&dlossCarVoList.size()>0){
			for(PrpLDlossCarMainVo dlossCar:dlossCarVoList){
				if(dlossCar.getSerialNo()==1&& // 什么鬼的定损表，待定
				CodeConstants.ValidFlag.VALID.equals(dlossCar.getValidFlag())&&CodeConstants.LossParty.TARGET
						.equals(dlossCar.getDeflossCarType())){
					dlossCarVo = new PrpLDlossCarMainVo();
					dlossCarVo = dlossCar;
				}
			}
		}
		return dlossCarVo;
	}

	// 获取查勘标的车号牌底色
	/* 
	 * @see ins.sino.claimcar.endcase.service.EndCaseService#getCarLinceColor(java.lang.String)
	 * @param registNo
	 * @return
	 */
	@Override
	public String getCarLinceColor(String registNo) {
		PrpLCheckCarVo checkCarVo = checkTaskService.findCheckCarBySerialNo(registNo,1);
		PrpLCheckCarInfoVo carInfo = checkCarVo.getPrpLCheckCarInfo();
		return carInfo.getLicenseColor();
	}

	/* 
	 * @see ins.sino.claimcar.endcase.service.EndCaseService#saveEndCase(ins.sino.claimcar.claim.vo.PrpLCompensateVo, java.lang.String)
	 * @param compeVo
	 * @param userCode
	 * @return
	 */
	@Override
	public Long saveEndCase(PrpLCompensateVo compeVo,String userCode) {
		PrpLEndCaseVo endCaseVo = new PrpLEndCaseVo();

		endCaseVo.setPolicyNo(compeVo.getPolicyNo());
		endCaseVo.setRegistNo(compeVo.getRegistNo());
		endCaseVo.setClaimNo(compeVo.getClaimNo());
		endCaseVo.setCompensateNo(compeVo.getCompensateNo());

		// 生成endCaseNo(结案号)
		endCaseVo.setEndCaseNo(billNoService.getEndCaseNo
		(compeVo.getComCode(),compeVo.getRiskCode()));

		// 生成归档号
		PrpLCertifyMainVo certifyMainVo=certifyPubService.findPrpLCertifyMainVoByRegistNo(compeVo.getRegistNo());
		double sumAmt=DataUtils.NullToZero(compeVo.getSumAmt()).doubleValue()+DataUtils.NullToZero(compeVo.getSumFee()).doubleValue();
		if(certifyMainVo!=null){
			if("1101".equals(compeVo.getRiskCode()) && !"1".equals(certifyMainVo.getIsJQFraud()) ){
				if(sumAmt>0){
					QueryRule queryRule = QueryRule.getInstance();
					queryRule.addEqual("registNo",compeVo.getRegistNo());
					queryRule.addEqual("compensateNo",compeVo.getCompensateNo());
					List<PrpLEndCase> oldEndCase = databaseDao.findAll(PrpLEndCase.class,queryRule);
					if(oldEndCase!=null&&oldEndCase.size()>0){
						endCaseVo.setRegressNo(oldEndCase.get(0).getRegressNo());
					}else{
						endCaseVo.setRegressNo(billNoService.getRegressNo(compeVo.getComCode()));
					}
				}
				
			}else if(!"1101".equals(compeVo.getRiskCode()) && !"1".equals(certifyMainVo.getIsSYFraud())){
				if(sumAmt>0){
					QueryRule queryRule = QueryRule.getInstance();
					queryRule.addEqual("registNo",compeVo.getRegistNo());
					queryRule.addEqual("compensateNo",compeVo.getCompensateNo());
					List<PrpLEndCase> oldEndCase = databaseDao.findAll(PrpLEndCase.class,queryRule);
					if(oldEndCase!=null&&oldEndCase.size()>0){
						endCaseVo.setRegressNo(oldEndCase.get(0).getRegressNo());
					}else{
						endCaseVo.setRegressNo(billNoService.getRegressNo(compeVo.getComCode()));
					}
				}
			}else if("1101".equals(compeVo.getRiskCode()) && "1".equals(certifyMainVo.getIsJQFraud()) ){
				QueryRule queryRule = QueryRule.getInstance();
				queryRule.addEqual("registNo",compeVo.getRegistNo());
				queryRule.addEqual("compensateNo",compeVo.getCompensateNo());
				List<PrpLEndCase> oldEndCase = databaseDao.findAll(PrpLEndCase.class,queryRule);
				if(oldEndCase!=null&&oldEndCase.size()>0){
					endCaseVo.setRegressNo(oldEndCase.get(0).getRegressNo());
				}else{
					endCaseVo.setRegressNo(billNoService.getRegressNo(compeVo.getComCode()));
				}
			}else if(!"1101".equals(compeVo.getRiskCode()) && "1".equals(certifyMainVo.getIsSYFraud())){
				QueryRule queryRule = QueryRule.getInstance();
				queryRule.addEqual("registNo",compeVo.getRegistNo());
				queryRule.addEqual("compensateNo",compeVo.getCompensateNo());
				List<PrpLEndCase> oldEndCase = databaseDao.findAll(PrpLEndCase.class,queryRule);
				if(oldEndCase!=null&&oldEndCase.size()>0){
					endCaseVo.setRegressNo(oldEndCase.get(0).getRegressNo());
				}else{
					endCaseVo.setRegressNo(billNoService.getRegressNo(compeVo.getComCode()));
				}
			}
		}
		
		
		endCaseVo.setRiskCode(compeVo.getRiskCode());
		endCaseVo.setIsAutoEndCase(CodeConstants.ValidFlag.VALID);// 是否自动结案
		endCaseVo.setEndcaseType(Risk.DQZ.equals(compeVo.getRiskCode()) ? "1" : "2");// 结案类型
		Date date = new Date();
		endCaseVo.setEndCaseDate(date);
		endCaseVo.setValidFlag(CodeConstants.ValidFlag.VALID);
		endCaseVo.setCreateUser(userCode);
		endCaseVo.setCreateTime(date);

		PrpLEndCase endCase = new PrpLEndCase();
		Beans.copy().from(endCaseVo).to(endCase);
		databaseDao.save(PrpLEndCase.class,endCase);
		return endCase.getId();
	}

	/*
	 *【【平安联盟对接使用】】
	 * @see ins.sino.claimcar.endcase.service.EndCaseService#saveEndCase(ins.sino.claimcar.claim.vo.PrpLCompensateVo, java.lang.String)
	 * @param compeVo
	 * @param userCode
	 * @return
	 */
	@Override
	public Long saveEndCaseForPingAn(PrpLCompensateVo compeVo,String userCode, Date endCaseDate) {
		PrpLEndCaseVo endCaseVo = new PrpLEndCaseVo();

		endCaseVo.setPolicyNo(compeVo.getPolicyNo());
		endCaseVo.setRegistNo(compeVo.getRegistNo());
		endCaseVo.setClaimNo(compeVo.getClaimNo());
		endCaseVo.setCompensateNo(compeVo.getCompensateNo());

		// 生成endCaseNo(结案号)
		endCaseVo.setEndCaseNo(billNoService.getEndCaseNo
				(compeVo.getComCode(),compeVo.getRiskCode()));

		// 生成归档号
		PrpLCertifyMainVo certifyMainVo=certifyPubService.findPrpLCertifyMainVoByRegistNo(compeVo.getRegistNo());
		double sumAmt=DataUtils.NullToZero(compeVo.getSumAmt()).doubleValue()+DataUtils.NullToZero(compeVo.getSumFee()).doubleValue();
		if(certifyMainVo!=null){
			if("1101".equals(compeVo.getRiskCode()) && !"1".equals(certifyMainVo.getIsJQFraud()) ){
				if(sumAmt>0){
					QueryRule queryRule = QueryRule.getInstance();
					queryRule.addEqual("registNo",compeVo.getRegistNo());
					queryRule.addEqual("compensateNo",compeVo.getCompensateNo());
					List<PrpLEndCase> oldEndCase = databaseDao.findAll(PrpLEndCase.class,queryRule);
					if(oldEndCase!=null&&oldEndCase.size()>0){
						endCaseVo.setRegressNo(oldEndCase.get(0).getRegressNo());
					}else{
						endCaseVo.setRegressNo(billNoService.getRegressNo(compeVo.getComCode()));
					}
				}

			}else if(!"1101".equals(compeVo.getRiskCode()) && !"1".equals(certifyMainVo.getIsSYFraud())){
				if(sumAmt>0){
					QueryRule queryRule = QueryRule.getInstance();
					queryRule.addEqual("registNo",compeVo.getRegistNo());
					queryRule.addEqual("compensateNo",compeVo.getCompensateNo());
					List<PrpLEndCase> oldEndCase = databaseDao.findAll(PrpLEndCase.class,queryRule);
					if(oldEndCase!=null&&oldEndCase.size()>0){
						endCaseVo.setRegressNo(oldEndCase.get(0).getRegressNo());
					}else{
						endCaseVo.setRegressNo(billNoService.getRegressNo(compeVo.getComCode()));
					}
				}
			}else if("1101".equals(compeVo.getRiskCode()) && "1".equals(certifyMainVo.getIsJQFraud()) ){
				QueryRule queryRule = QueryRule.getInstance();
				queryRule.addEqual("registNo",compeVo.getRegistNo());
				queryRule.addEqual("compensateNo",compeVo.getCompensateNo());
				List<PrpLEndCase> oldEndCase = databaseDao.findAll(PrpLEndCase.class,queryRule);
				if(oldEndCase!=null&&oldEndCase.size()>0){
					endCaseVo.setRegressNo(oldEndCase.get(0).getRegressNo());
				}else{
					endCaseVo.setRegressNo(billNoService.getRegressNo(compeVo.getComCode()));
				}
			}else if(!"1101".equals(compeVo.getRiskCode()) && "1".equals(certifyMainVo.getIsSYFraud())){
				QueryRule queryRule = QueryRule.getInstance();
				queryRule.addEqual("registNo",compeVo.getRegistNo());
				queryRule.addEqual("compensateNo",compeVo.getCompensateNo());
				List<PrpLEndCase> oldEndCase = databaseDao.findAll(PrpLEndCase.class,queryRule);
				if(oldEndCase!=null&&oldEndCase.size()>0){
					endCaseVo.setRegressNo(oldEndCase.get(0).getRegressNo());
				}else{
					endCaseVo.setRegressNo(billNoService.getRegressNo(compeVo.getComCode()));
				}
			}
		}


		endCaseVo.setRiskCode(compeVo.getRiskCode());
		endCaseVo.setIsAutoEndCase(CodeConstants.ValidFlag.VALID);// 是否自动结案
		endCaseVo.setEndcaseType(Risk.DQZ.equals(compeVo.getRiskCode()) ? "1" : "2");// 结案类型
		Date date = new Date();
		//endCaseVo.setEndCaseDate(date);
		endCaseVo.setEndCaseDate(endCaseDate);//结案时间从外面传入
		endCaseVo.setValidFlag(CodeConstants.ValidFlag.VALID);
		endCaseVo.setCreateUser(userCode);
		endCaseVo.setCreateTime(date);

		PrpLEndCase endCase = new PrpLEndCase();
		Beans.copy().from(endCaseVo).to(endCase);
		databaseDao.save(PrpLEndCase.class,endCase);
		return endCase.getId();
	}
	
	public List<PrpLEndCaseVo>  findEndCaseByEndCaseDate(Date beginDate,Date endDate) {
		List<PrpLEndCaseVo> endCaseVoList = new ArrayList<PrpLEndCaseVo>();
		SqlJoinUtils sqlUtil=new SqlJoinUtils();
		sqlUtil.append(" from PrpLEndCase endCase");
		sqlUtil.append(" where 1=1 and exists(select 1 from PrpLPayment payment  " );
		sqlUtil.append(" where endCase.compensateNo=payment.prpLCompensate.compensateNo and payment.payStatus = ? ");
		sqlUtil.append(" and payment.payTime is not null and payment.payTime between ? and ? ) ");
		sqlUtil.append(" and not exists (select 1 from PrpLPayment pay ");
		sqlUtil.append(" where endCase.compensateNo=pay.prpLCompensate.compensateNo ");
		sqlUtil.append(" and (pay.payStatus is null or pay.payStatus != ? )) ");
		
		
		sqlUtil.addParamValue("1");
		sqlUtil.addParamValue(beginDate);
		sqlUtil.addParamValue(endDate);
		sqlUtil.addParamValue("1");
		
		String sql = sqlUtil.getSql();
		Object[] values = sqlUtil.getParamValues();
		List<PrpLEndCase> resultList = databaseDao.findAllByHql(PrpLEndCase.class, sql, values);
		if(resultList != null){
			endCaseVoList = Beans.copyDepth().from(resultList).toList(PrpLEndCaseVo.class);
		}
		
		return endCaseVoList;
	}

	@Override
	public ResultPage<RecPayLaunchResultVo> find(RecPayLaunchVo recPayLaunchVo,
			int start, int length,String comCode) throws Exception {
		SqlJoinUtils sqlUtil = new SqlJoinUtils();
		sqlUtil.append("select MERCYFLAG,REGISTNO,POLICYNO,b.CLAIMNO,RISKCODE,ENDCASEDATE from");
		sqlUtil.append("(select ENDCASENO,claimno,max(ENDCASEDATE) ENDCASEDATE from prplendcase  group by claimno,ENDCASENO) a,prplclaim b ");
		sqlUtil.append("where a.claimno=b.claimno and b.caseno=a.ENDCASENO");
		sqlUtil.andDate(recPayLaunchVo,"a","endCaseDate");
		sqlUtil.andComSql("b", "COMCODE", comCode);
		if(StringUtils.isNotBlank(recPayLaunchVo.getClaimNo())){
			sqlUtil.append(" AND a.CLAIMNO = ? ");
			sqlUtil.addParamValue(recPayLaunchVo.getClaimNo());
		}
		if(StringUtils.isNotBlank(recPayLaunchVo.getPolicyNo())){
			sqlUtil.append(" AND POLICYNO = ? ");
			sqlUtil.addParamValue(recPayLaunchVo.getPolicyNo());
		}
		if(StringUtils.isNotBlank(recPayLaunchVo.getRegistNo())){
			sqlUtil.append(" AND REGISTNO = ? ");
			sqlUtil.addParamValue(recPayLaunchVo.getRegistNo());
		}
		if(StringUtils.isNotBlank(recPayLaunchVo.getMercyFlag())){
			sqlUtil.append(" AND b.mercyflag = ? ");
			sqlUtil.addParamValue(recPayLaunchVo.getMercyFlag());
		}
		if(StringUtils.isNotBlank(recPayLaunchVo.getComCode())){
			sqlUtil.append(" AND b.COMCODE = ? ");
			sqlUtil.addParamValue(recPayLaunchVo.getComCode());
		}
		String sql = sqlUtil.getSql();
		Object[] values = sqlUtil.getParamValues();

		logger.debug("taskQrySql="+sql);
		System.out.println("taskQrySql="+sql);
		logger.debug("ParamValues="+ArrayUtils.toString(values));
		System.out.println("ParamValues="+ArrayUtils.toString(values));

		Page<Object[]> page = baseDaoService.pagedSQLQuery(sql,start,length,values);
		long pageLengthX = page.getTotalCount();
		List<RecPayLaunchResultVo> resultVoList = new ArrayList<RecPayLaunchResultVo>();
		for(int i = 0; i<page.getResult().size(); i++ ){
			Object[] obj = page.getResult().get(i);
			RecPayLaunchResultVo resultVo = new RecPayLaunchResultVo();
			resultVo.setMercyFlagName(obj[0].toString().equals("0")?"一般":"紧急");
			resultVo.setRegistNoHtml(obj[1].toString());
			resultVo.setPolicyNoHtml(obj[2].toString());
			resultVo.setClaimNo(obj[3].toString());
			resultVo.setPolicyType(obj[4].toString().equals("1101")?"交强":"商业");
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
	        Date date = format.parse(obj[5].toString());
			resultVo.setEndTime(date);
			resultVoList.add(resultVo);
		}
		ResultPage<RecPayLaunchResultVo> resultPage = new ResultPage<RecPayLaunchResultVo>(
				start,length,pageLengthX,resultVoList);
		return resultPage;
	}
	@Override
	public void saveConfirmCode(Long id,String confirmCode){
		if(id != null){
			String sql = "UPDATE PRPLENDCASE SET CONFIRMCODE='" + confirmCode + "' WHERE ID="+id;
			 baseDaoService.executeSQL(sql);
		}
	}

	@Override
	public List<PrpLEndCaseVo> findEndCaseVoByRegistNoAndCompeNo(String registNo,String compensateNo){
		List<PrpLEndCaseVo> endCaseVoList = new ArrayList<PrpLEndCaseVo>();
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo",registNo);
		queryRule.addEqual("compensateNo",compensateNo);
		queryRule.addDescOrder("endCaseDate");
		List<PrpLEndCase> endCasePos = databaseDao.findAll(PrpLEndCase.class,queryRule);
		if(endCasePos!=null&&endCasePos.size()>0){
			endCaseVoList = Beans.copyDepth().from(endCasePos).toList(PrpLEndCaseVo.class);
		}
		return endCaseVoList;
	}

	@Override
	public void saveOrUpdateEndCase(PrpLEndCaseVo prpLEndCaseVo){
		PrpLEndCase prpLEndCase;
		if (prpLEndCaseVo.getId() == null){
			prpLEndCase = new PrpLEndCase();
			Beans.copy().from(prpLEndCaseVo).to(prpLEndCase);
		}else {
			prpLEndCase = databaseDao.findByPK(PrpLEndCase.class,prpLEndCaseVo.getId());
			Beans.copy().from(prpLEndCaseVo).excludeNull().to(prpLEndCase);
		}
		databaseDao.save(PrpLEndCase.class,prpLEndCase);;
	}
}
