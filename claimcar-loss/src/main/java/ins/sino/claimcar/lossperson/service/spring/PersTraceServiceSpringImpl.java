/******************************************************************************
 * CREATETIME : 2016年1月6日 上午9:04:55
 ******************************************************************************/
package ins.sino.claimcar.lossperson.service.spring;

import ins.framework.common.ResultPage;
import ins.framework.dao.database.DatabaseDao;
import ins.framework.dao.database.support.Page;
import ins.framework.dao.database.support.QueryRule;
import ins.framework.utils.Beans;
import ins.platform.common.service.facade.CodeTranService;
import ins.platform.utils.DataUtils;
import ins.platform.utils.SqlJoinUtils;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.CodeConstants.ModifyFlag;
import ins.sino.claimcar.flow.po.PrpLWfTaskQuery;
import ins.sino.claimcar.flow.service.WfTaskHandleService;
import ins.sino.claimcar.lossperson.po.PrpLDlossPersExt;
import ins.sino.claimcar.lossperson.po.PrpLDlossPersHospital;
import ins.sino.claimcar.lossperson.po.PrpLDlossPersInjured;
import ins.sino.claimcar.lossperson.po.PrpLDlossPersNurse;
import ins.sino.claimcar.lossperson.po.PrpLDlossPersRaise;
import ins.sino.claimcar.lossperson.po.PrpLDlossPersTrace;
import ins.sino.claimcar.lossperson.po.PrpLDlossPersTraceFee;
import ins.sino.claimcar.lossperson.po.PrpLDlossPersTraceFeeHis;
import ins.sino.claimcar.lossperson.po.PrpLDlossPersTraceHis;
import ins.sino.claimcar.lossperson.po.PrpLDlossPersTraceMain;
import ins.sino.claimcar.lossperson.service.PersTraceService;
import ins.sino.claimcar.lossperson.vo.ChargeAdjustQueryResultVo;
import ins.sino.claimcar.lossperson.vo.ChargeAdjustQueryVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersExtVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersHospitalVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersInjuredVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersNurseVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersRaiseVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceFeeVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceHisVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceMainVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceVo;

import java.lang.reflect.Method;
import java.math.BigDecimal;
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
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.alibaba.dubbo.config.annotation.Service;

/**
 * <pre></pre>
 * 
 * @author ★XMSH
 */
@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"})
@Path("persTraceService")
public class PersTraceServiceSpringImpl implements PersTraceService {

	private static Logger logger = LoggerFactory.getLogger(PersTraceServiceSpringImpl.class);

	@Autowired
	private DatabaseDao databaseDao;
	@Autowired
	private WfTaskHandleService wfTaskHandleService;
	@Autowired
	private CodeTranService codeTranService;

	@Override
	public Long saveOrUpdatePersTrace(PrpLDlossPersTraceVo persTraceVo,SysUserVo userVo) throws Exception {
		// TODO Auto-generated method stub
		PrpLDlossPersTrace persTrace = null;
		if(persTraceVo.getId()==null){// 新增
			persTrace = Beans.copyDepth().from(persTraceVo).to(PrpLDlossPersTrace.class);
			setPkRelation(persTrace,userVo);
		}else{// 更新
			persTrace = databaseDao.findByPK(PrpLDlossPersTrace.class,persTraceVo.getId());
			for(PrpLDlossPersTraceFeeVo traceFeeVo : persTraceVo.getPrpLDlossPersTraceFees()){
				if(traceFeeVo.getId()==null){//新增的费用
					traceFeeVo.setModifyFlag(ModifyFlag.ADD);
				}else{
					if(ModifyFlag.NONE.equals(traceFeeVo.getModifyFlag())){
						PrpLDlossPersTraceFee traceFee = databaseDao.findByPK(PrpLDlossPersTraceFee.class,traceFeeVo.getId());
						//审核过的费用判断是否修改
						if(traceFeeVo.getVeriDefloss()!=null||traceFeeVo.getVeriDetractionFee()!=null||traceFeeVo.getVeriReportFee()!=null||traceFeeVo.getVeriRealFee()!=null ){
							boolean modifyFlag = true;
//							boolean modifyFlag = DomainUtils.beanEquals(traceFeeVo,traceFee,"unitAmount","quantity","woundRate","reportFee","realFee","detractionfee","defloss","remark");
							
							String woundRate = traceFeeVo.getWoundRate();
							if(StringUtils.isEmpty(woundRate)){
								woundRate = "0";
							}
							String rate = traceFee.getWoundRate();
							if(StringUtils.isEmpty(rate)){
								rate = "0";
							}
							
							if(DataUtils.NullToZero(traceFeeVo.getUnitAmount()).compareTo(DataUtils.NullToZero(traceFee.getUnitAmount()))!=0){
								modifyFlag=false;
							}else if(DataUtils.NullToZero(traceFeeVo.getQuantity()).compareTo(DataUtils.NullToZero(traceFee.getQuantity()))!=0) {
								modifyFlag=false;
							}else if(DataUtils.NullToZero(traceFeeVo.getReportFee()).compareTo(DataUtils.NullToZero(traceFee.getReportFee()))!=0){
								modifyFlag=false;
							}else if(DataUtils.NullToZero(traceFeeVo.getRealFee()).compareTo(DataUtils.NullToZero(traceFee.getRealFee()))!=0){
								modifyFlag=false;
							}else if(DataUtils.NullToZero(traceFeeVo.getDetractionfee()).compareTo(DataUtils.NullToZero(traceFee.getDetractionfee()))!=0){
								modifyFlag=false;
							}else if(DataUtils.NullToZero(traceFeeVo.getDefloss()).compareTo(DataUtils.NullToZero(traceFee.getDefloss()))!=0){
								modifyFlag=false;
							}else if(!(traceFeeVo.getRemark()==null?"":traceFeeVo.getRemark()).equals(traceFee.getRemark()==null?"":traceFee.getRemark())){
								modifyFlag=false;
							}else if(new BigDecimal(woundRate).compareTo(new BigDecimal(rate))!=0){
								modifyFlag=false;
							}
							//修改过
							if(!modifyFlag){
								traceFeeVo.setModifyFlag(ModifyFlag.MODIFY);
							}
						}
					}
				}
			}
			mergeAllList(persTrace,persTraceVo);
			setPkRelation(persTrace,userVo);
		}

		databaseDao.save(PrpLDlossPersTrace.class,persTrace);
		return persTrace.getId();
	}

	@Override
	public List<PrpLDlossPersTraceMainVo> findPersTraceMainVo(String registNo) {
		List<PrpLDlossPersTraceMain> persTraceMains = null;

		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo",registNo);

		persTraceMains = databaseDao.findAll(PrpLDlossPersTraceMain.class,queryRule);
		List<PrpLDlossPersTraceMainVo> persTraceMainVos = null;

		if(persTraceMains!=null&&persTraceMains.size()>0){
			persTraceMainVos = Beans.copyDepth().from(persTraceMains).toList(PrpLDlossPersTraceMainVo.class);
		}
		return persTraceMainVos;
	}

	@Override
	public PrpLDlossPersTraceMainVo findPersTraceMainVoById(Long id) {
		// TODO Auto-generated method stub
		PrpLDlossPersTraceMain persTraceMain = databaseDao.findByPK(PrpLDlossPersTraceMain.class,id);

		PrpLDlossPersTraceMainVo persTraceMainVo = null;
		if(persTraceMain!=null){
			persTraceMainVo = Beans.copyDepth().from(persTraceMain).to(PrpLDlossPersTraceMainVo.class);
		}
		List<PrpLDlossPersTraceVo> persTraceVos = findPersTraceVo(persTraceMainVo.getRegistNo(),persTraceMainVo.getId());
		persTraceMainVo.setPrpLDlossPersTraces(persTraceVos);
		return persTraceMainVo;
	}

	@Override
	public PrpLDlossPersTraceVo findPersTraceVo(Long id) {
		// TODO Auto-generated method stub

		PrpLDlossPersTrace persTrace = databaseDao.findByPK(PrpLDlossPersTrace.class,id);
        
		PrpLDlossPersTraceVo perTraceVo = null;
		if(persTrace!=null){
			perTraceVo = new PrpLDlossPersTraceVo();
			perTraceVo = Beans.copyDepth().from(persTrace).to(PrpLDlossPersTraceVo.class);
		}
		return perTraceVo;
	}

	@Override
	public void AvtiveOrCanCelPersTrace(PrpLDlossPersTraceVo persTraceVo) {
		// TODO Auto-generated method stub

		PrpLDlossPersTrace persTrace = databaseDao.findByPK(PrpLDlossPersTrace.class,persTraceVo.getId());

		Beans.copy().from(persTraceVo).includes("validFlag").to(persTrace);

		databaseDao.update(PrpLDlossPersTrace.class,persTrace);
	}

	@Override
	public void updatePersTraceFee(PrpLDlossPersTraceFeeVo feeVo) {
		if(feeVo.getId()!=null){
			PrpLDlossPersTraceFee persTraceFee = databaseDao.findByPK(PrpLDlossPersTraceFee.class,feeVo.getId());
			Beans.copy()
					.from(feeVo)
					.includes("unitAmount","quantity","woundRate","reportFee","realFee","detractionfee","defloss",
							"remark").to(persTraceFee);
			databaseDao.update(PrpLDlossPersTraceFee.class,persTraceFee);
		}
	}

	@Override
	public void updatePersTraceVeriFee(PrpLDlossPersTraceFeeVo feeVo) {
		if(feeVo.getId()!=null){
			PrpLDlossPersTraceFee persTraceFee = databaseDao.findByPK(PrpLDlossPersTraceFee.class,feeVo.getId());

			Beans.copy()
					.from(feeVo)
					.includes("veriUnitAmount","veriQuantity","veriWoundRate","veriReportFee","veriRealFee","veriLevel","status","modifyFlag",
							"veriDetractionFee","veriDefloss","veriRemark").to(persTraceFee);

			databaseDao.update(PrpLDlossPersTraceFee.class,persTraceFee);
		}
	}

	@Override
	public List<PrpLDlossPersTraceVo> findPersTraceVo(String registNo,Long persTraceMainId) {
		// TODO Auto-generated method stub
		List<PrpLDlossPersTrace> persTraces = null;

		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo",registNo);
		queryRule.addEqual("persTraceMainId",persTraceMainId);

		persTraces = databaseDao.findAll(PrpLDlossPersTrace.class,queryRule);
		List<PrpLDlossPersTraceVo> persTraceVos = null;

		if(persTraces!=null&&persTraces.size()>0){
			persTraceVos = Beans.copyDepth().from(persTraces).toList(PrpLDlossPersTraceVo.class);
		}
		return persTraceVos;
	}
   
	@Override
	public List<PrpLDlossPersTraceVo> findPersTraceVoByRegistNo(String registNo) {
		// TODO Auto-generated method stub
		List<PrpLDlossPersTrace> persTraces = null;
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo",registNo);
		persTraces = databaseDao.findAll(PrpLDlossPersTrace.class,queryRule);
		List<PrpLDlossPersTraceVo> persTraceVos = null;
		if(persTraces!=null &&  persTraces.size()>0){
			persTraceVos = Beans.copyDepth().from(persTraces).toList(PrpLDlossPersTraceVo.class);
		}
		return persTraceVos;
	}
	
	
	@Override
	public List<PrpLDlossPersInjuredVo> findPersInjuredVo(String registNo,String certiCode) {
		// TODO Auto-generated method stub
		List<PrpLDlossPersInjured> persInjureds = null;
		
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo",registNo);
		queryRule.addEqual("certiType","1");
		queryRule.addEqual("certiCode",certiCode);
		
		persInjureds = databaseDao.findAll(PrpLDlossPersInjured.class,queryRule);
		List<PrpLDlossPersInjuredVo> persInjuredVos = null;
		
		if(persInjureds!=null&&persInjureds.size()>0){
			persInjuredVos = Beans.copyDepth().from(persInjureds).toList(PrpLDlossPersInjuredVo.class);
		}
		return persInjuredVos;
	}
	
	@Override
	public Long saveOrUpdatePersTraceMain(PrpLDlossPersTraceMainVo persTraceMainVo) {
		// TODO Auto-generated method stub

		PrpLDlossPersTraceMain persTraceMain = null;
		if(StringUtils.isNotBlank(persTraceMainVo.getPlfCertiCode())){
			//人伤定损员证去空格和制表符  防止送平台校验不通过
			persTraceMainVo.setPlfCertiCode(persTraceMainVo.getPlfCertiCode().replaceAll("\\s*",""));
		}
		if(StringUtils.isNotBlank(persTraceMainVo.getOperatorCertiCode())){
			//人伤定损员证去空格和制表符  防止送平台校验不通过
			persTraceMainVo.setOperatorCertiCode(persTraceMainVo.getOperatorCertiCode().replaceAll("\\s*",""));
		}
		if(StringUtils.isNotBlank(persTraceMainVo.getVerifyCertiCode())){
			//人伤核损员证去空格和制表符  防止送平台校验不通过
			persTraceMainVo.setVerifyCertiCode(persTraceMainVo.getVerifyCertiCode().replaceAll("\\s*",""));
		}
		if(persTraceMainVo.getId()==null){
			persTraceMain = new PrpLDlossPersTraceMain();
			Beans.copy().from(persTraceMainVo).to(persTraceMain);
		}else{
			persTraceMain = databaseDao.findByPK(PrpLDlossPersTraceMain.class,persTraceMainVo.getId());
			Beans.copy().from(persTraceMainVo).excludeNull().to(persTraceMain);
			persTraceMain.setIsDeroVerifyAmout(persTraceMainVo.getIsDeroVerifyAmout());
			persTraceMain.setIsDeroAmout(persTraceMainVo.getIsDeroAmout());
		}
		databaseDao.save(PrpLDlossPersTraceMain.class,persTraceMain);

		return persTraceMain.getId();
	}

	@Override
	public Long saveOrUpdateTraceMain(PrpLDlossPersTraceMainVo persTraceMainVo) {
		// TODO Auto-generated method stub

		PrpLDlossPersTraceMain persTraceMain = null;
		if(StringUtils.isNotBlank(persTraceMainVo.getPlfCertiCode())){
			//人伤定损员证去空格和制表符  防止送平台校验不通过
			persTraceMainVo.setPlfCertiCode(persTraceMainVo.getPlfCertiCode().replaceAll("\\s*",""));
		}
		if(StringUtils.isNotBlank(persTraceMainVo.getOperatorCertiCode())){
			//人伤定损员证去空格和制表符  防止送平台校验不通过
			persTraceMainVo.setOperatorCertiCode(persTraceMainVo.getOperatorCertiCode().replaceAll("\\s*",""));
		}
		if(StringUtils.isNotBlank(persTraceMainVo.getVerifyCertiCode())){
			//人伤核损员证去空格和制表符  防止送平台校验不通过
			persTraceMainVo.setVerifyCertiCode(persTraceMainVo.getVerifyCertiCode().replaceAll("\\s*",""));
		}
		if(persTraceMainVo.getId()==null){
			persTraceMain = new PrpLDlossPersTraceMain();
			Beans.copy().from(persTraceMainVo).to(persTraceMain);
		}else{
			persTraceMain = databaseDao.findByPK(PrpLDlossPersTraceMain.class,persTraceMainVo.getId());
			Beans.copy().from(persTraceMainVo).excludeNull().to(persTraceMain);
		}
		databaseDao.save(PrpLDlossPersTraceMain.class,persTraceMain);

		return persTraceMain.getId();
	}

	
	
	/**
	 * 保存人伤轨迹
	 */
	@Override
	public void savePersTraceHis(PrpLDlossPersTraceVo persTraceVo) {

		PrpLDlossPersTraceHis persTraceHis = new PrpLDlossPersTraceHis();

		Beans.copy().from(persTraceVo).to(persTraceHis);
		persTraceHis.setId(null);
		persTraceHis.setInjuredId(persTraceVo.getId());

		List<PrpLDlossPersTraceFeeVo> persTraceFeeVos = persTraceVo.getPrpLDlossPersTraceFees();
		List<PrpLDlossPersTraceFeeHis> persTraceFeeHises = Beans.copyDepth().from(persTraceFeeVos)
				.toList(PrpLDlossPersTraceFeeHis.class);

		for(PrpLDlossPersTraceFeeHis persTraceFeeHis:persTraceFeeHises){
			persTraceFeeHis.setPrpLDlossPersTraceHis(persTraceHis);
			persTraceFeeHis.setPersTraceFeeId(persTraceFeeHis.getId());
			persTraceFeeHis.setCreateTime(new Date());
			persTraceFeeHis.setUpdateTime(new Date());
			persTraceFeeHis.setId(null);
		}
		persTraceHis.setUpdateTime(new Date());
		persTraceHis.setPrpLDlossPersTraceFeeHises(persTraceFeeHises);

		databaseDao.save(PrpLDlossPersTraceHis.class,persTraceHis);
	}
	
	@Override
	public void updatePersTraceHis(PrpLDlossPersTraceHisVo persTraceHisVo) {
		Assert.notNull(persTraceHisVo.getId(),"id不能为空！");
		PrpLDlossPersTraceHis persTraceHis = databaseDao.findByPK(PrpLDlossPersTraceHis.class,persTraceHisVo.getId());
		PrpLDlossPersTrace persTrace = databaseDao.findByPK(PrpLDlossPersTrace.class,persTraceHisVo.getInjuredId());
		
		Beans.copy().from(persTraceHisVo).excludeNull().to(persTraceHis);
		
//		persTraceHis.setSumVeriReportFee(persTrace.getSumVeriReportFee());
//		persTraceHis.setSumVeriRealFee(persTrace.getSumVeriRealFee());
//		persTraceHis.setSumVeriDetractionFee(persTrace.getSumVeriDetractionFee());
//		persTraceHis.setSumVeriDefloss(persTrace.getSumVeriDefloss());
		
		Beans.copy().from(persTrace).includes("sumVeriReportFee","sumVeriRealFee","sumVeriDetractionFee","sumVeriDefloss").to(persTraceHis);
		
		for(PrpLDlossPersTraceFeeHis persTraceFeeHis : persTraceHis.getPrpLDlossPersTraceFeeHises()){
			PrpLDlossPersTraceFee persTraceFee = databaseDao.findByPK(PrpLDlossPersTraceFee.class,persTraceFeeHis.getPersTraceFeeId());
			Beans.copy().from(persTraceFee).includes("veriUnitAmount","veriQuantity","veriWoundRate","veriReportFee","veriRealFee","veriLevel","status",
					"veriDetractionFee","veriDefloss","veriRemark").to(persTraceFeeHis);
			persTraceFeeHis.setPrpLDlossPersTraceHis(persTraceHis);
		}
		databaseDao.update(PrpLDlossPersTraceHis.class,persTraceHis);
	}
	
	private List<PrpLDlossPersTraceHis> find(Long persTraceMainId,String flag) {
		
		return null;
	}
	
	
	@Override
	public List<PrpLDlossPersTraceHisVo> findPersTraceHisVo(Long persTraceMainId,String flag) {
		// 查询最新的跟踪轨迹记录（排除掉改次以前的）
		List<PrpLDlossPersTraceHisVo> hisVoList = new ArrayList<PrpLDlossPersTraceHisVo>();
		
		PrpLDlossPersTraceMainVo mainVo = findPersTraceMainVoById(persTraceMainId);
		List<PrpLDlossPersTraceVo> traceVoList = mainVo.getPrpLDlossPersTraces();
		if ( traceVoList != null && !traceVoList.isEmpty() ) {
			for ( PrpLDlossPersTraceVo traceVo : traceVoList ) {
				QueryRule queryRule = QueryRule.getInstance();
				queryRule.addEqual("persTraceMainId",persTraceMainId);
				queryRule.addEqual("injuredId",traceVo.getId());
				queryRule.addEqual("flag",flag);
				queryRule.addDescOrder("createTime");
				List<PrpLDlossPersTraceHis> hisList = 
						databaseDao.findAll(PrpLDlossPersTraceHis.class,queryRule);
				if ( hisList != null && !hisList.isEmpty() ) {
					hisVoList.add(Beans.copyDepth().from(hisList.get(0)).to(PrpLDlossPersTraceHisVo.class));
				}
			}
		}
		return hisVoList;
	}

	@Override
	public List<PrpLDlossPersTraceHisVo> findPersTraceHisVo(String registNo,Long persTraceMainId,String personName) {
		List<PrpLDlossPersTraceHis> persTraceHises = null;

		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo",registNo);
		queryRule.addEqual("persTraceMainId",persTraceMainId);
		if(StringUtils.isNotBlank(personName)){
			queryRule.addEqual("personName",personName);
		}
		queryRule.addAscOrder("updateTime");

		persTraceHises = databaseDao.findAll(PrpLDlossPersTraceHis.class,queryRule);
		List<PrpLDlossPersTraceHisVo> persTraceHisVos = null;

		if(persTraceHises!=null&&persTraceHises.size()>0){
			persTraceHisVos = Beans.copyDepth().from(persTraceHises).toList(PrpLDlossPersTraceHisVo.class);
		}
		return persTraceHisVos;
	}

	@Override
	public ResultPage<ChargeAdjustQueryResultVo> findPageForChargeAdjust(ChargeAdjustQueryVo queryVo) throws Exception {
		/*
		 * 查询sql大概为 Select Main.* From prpLdlossPropMain main ,prplwftaskquery qry Where main.registNo=qry.registNo And main.lossState='00' And main.underWriteFlag='2' And registNo=? And mercyFlag=?
		 * And license=? And policyNo=? And frameNo=? And insuredName Like ?
		 */
		SqlJoinUtils sqlUtil = new SqlJoinUtils();
		sqlUtil.append(" FROM PrpLDlossPersTraceMain main,PrpLWfTaskQuery qry ");
		// 查询核损完成，未理算的数据
		sqlUtil.append("Where main.registNo=qry.registNo And  main.underwriteFlag=? And main.lossState =? And main.caseProcessType <>? ");

		sqlUtil.addParamValue("2");
		sqlUtil.addParamValue("00");
		sqlUtil.addParamValue("10");//无需赔付不能发起费用审核修改

		// 可后几几位查询
		sqlUtil.andReverse(queryVo,"qry",7,"registNo","licenseNo");
//		sqlUtil.andReverse(queryVo,"qry",6,"policyNo");
		sqlUtil.andEquals(queryVo,"qry","mercyFlag");
		sqlUtil.andEquals(queryVo,"qry","riskCode");

		// sqlUtil.andLike2(queryVo,"qry","personName");
		// sqlUtil.andLike2(queryVo,"qry","hospitalName");
		sqlUtil.andLike2(queryVo,"qry","insuredName");
		
		String policyNo = queryVo.getPolicyNo();
		if(StringUtils.isNotBlank(policyNo)&&policyNo.length()>2){
			String policyNoRev = StringUtils.reverse(policyNo.toString()).trim();
			sqlUtil.append("AND qry.policyNoRev LIKE ?  ");
			sqlUtil.addParamValue(policyNoRev+"%");
		}

		// 人伤首次跟踪查询中，入院时间查询条件
		if(queryVo.getInHospitalDateStart()!=null||queryVo.getInHospitalDateEnd()!=null){
			sqlUtil.append(" AND exists  (select  1 from PrpLDlossPersHospital hsp where hsp.registNo=qry.registNo ");
			sqlUtil.andDate(queryVo,"hsp","inHospitalDate");
			sqlUtil.append(")");
		}
		
		if(!StringUtils.isBlank(queryVo.getHospitalName())){
			sqlUtil.append(" AND exists  (select  1 from PrpLDlossPersHospital hsp where hsp.registNo=qry.registNo ");
			sqlUtil.andLike2(queryVo,"hsp","hospitalName");
			sqlUtil.append(")");
		}
		
		if(!StringUtils.isBlank(queryVo.getPersonName())){
			sqlUtil.append(" AND exists  (select  1 from PrpLDlossPersTrace persTrace where persTrace.persTraceMainId=main.id ");
			sqlUtil.andLike2(queryVo,"persTrace.prpLDlossPersInjured","personName");
			sqlUtil.append(")");
		}

		// 排序
		sqlUtil.append(" ORDER BY qry.mercyFlag DESC, main.updateTime DESC");

		// 开始记录数
		int start = queryVo.getStart();
		// 查询记录数量
		int length = queryVo.getLength();

		String sql = sqlUtil.getSql();
		Object[] values = sqlUtil.getParamValues();
		logger.debug("taskQrySql="+sql);
		logger.debug("ParamValues="+ArrayUtils.toString(values));

		Page<Object[]> page = databaseDao.findPageByHql(sql,start/length+1,length,values);

		// 对象转换
		List<ChargeAdjustQueryResultVo> resultVoList = new ArrayList<ChargeAdjustQueryResultVo>();
		for(int i = 0; i<page.getResult().size(); i++ ){

			Object[] obj = page.getResult().get(i);
			PrpLDlossPersTraceMain persTraceMainPo = (PrpLDlossPersTraceMain)obj[0];
			PrpLWfTaskQuery queryPo = (PrpLWfTaskQuery)obj[1];

			List<PrpLDlossPersTraceVo> persTraceVos = this.findPersTraceVo(persTraceMainPo.getRegistNo(),
					persTraceMainPo.getId());

			String remark = "";
			if(persTraceVos!=null&&persTraceVos.size()>0){
				for(PrpLDlossPersTraceVo persTraceVo:persTraceVos){
					remark += codeTranService.transCode("LossItemType",persTraceVo.getPrpLDlossPersInjured()
							.getLossItemType())+"-"+persTraceVo.getPrpLDlossPersInjured().getPersonName()+";";
				}
			}

			ChargeAdjustQueryResultVo resultVo = new ChargeAdjustQueryResultVo();

			resultVo.setPersTraceMainId(persTraceMainPo.getId());
			resultVo.setMercyFlag(queryPo.getMercyFlag());// 案件紧急程度
			resultVo.setCustomerLevel(queryPo.getCustomerLevel()==null ? "" : queryPo.getCustomerLevel());// 客户等级
			resultVo.setRegistNo(queryPo.getRegistNo());// 报案号
			resultVo.setPolicyNo(queryPo.getPolicyNo());// 保单号
			resultVo.setPolicyNoLink(queryPo.getPolicyNoLink());
			resultVo.setLicenseNo(queryPo.getLicenseNo());// 车牌号
			resultVo.setInsuredCode(queryPo.getInsuredCode());// 被保险人代码
			resultVo.setInsuredName(queryPo.getInsuredName());// 被保险人名字
			resultVo.setRemark(remark);
			resultVoList.add(resultVo);
		}

		ResultPage<ChargeAdjustQueryResultVo> resultPage = new ResultPage<ChargeAdjustQueryResultVo>(
				start,length,page.getTotalCount(),resultVoList);

		return resultPage;
	}

	@Override
	public void updatePersTraceSumAmount(PrpLDlossPersTraceVo persTraceVo) {
		if(persTraceVo.getId()!=null){
			PrpLDlossPersTrace persTrace = databaseDao.findByPK(PrpLDlossPersTrace.class,persTraceVo.getId());

			Beans.copy().from(persTraceVo).includes("sumReportFee","sumRealFee","sumDetractionFee","sumdefLoss")
					.to(persTrace);

			databaseDao.update(PrpLDlossPersTrace.class,persTrace);
		}

	}

	@Override
	public void updatePersTraceVeriSumAmount(PrpLDlossPersTraceVo persTraceVo) {
		if(persTraceVo.getId()!=null){
			PrpLDlossPersTrace persTrace = databaseDao.findByPK(PrpLDlossPersTrace.class,persTraceVo.getId());

			Beans.copy().from(persTraceVo)
					.includes("sumVeriReportFee","sumVeriRealFee","sumVeriDetractionFee","sumVeriDefloss","undwrtCode","undwrtTime","undwrtDesc")
					.to(persTrace);

			databaseDao.update(PrpLDlossPersTrace.class,persTrace);
		}

	}

	@Override
	public PrpLDlossPersTraceMainVo calculateSumAmt(PrpLDlossPersTraceMainVo persTaceMainVo){
		if(persTaceMainVo == null || StringUtils.isEmpty(persTaceMainVo.getRegistNo())){
			return persTaceMainVo;
		}
		String registNo = persTaceMainVo.getRegistNo();
		List<PrpLDlossPersTraceVo> persTaceVoList = this.findPersTraceVoByRegistNo(registNo);
		if(persTaceVoList != null && persTaceVoList.size() > 0){
			BigDecimal sumReportFee = new BigDecimal("0");
			BigDecimal sumRealFee = new BigDecimal("0");
			BigDecimal sumDetractionFee = new BigDecimal("0");
			BigDecimal sumdefLoss = new BigDecimal("0");
			BigDecimal sumVeriReportFee = new BigDecimal("0");
			BigDecimal sumVeriRealFee = new BigDecimal("0");
			BigDecimal sumVeriDetractionFee = new BigDecimal("0");
			BigDecimal sumVeriDefloss = new BigDecimal("0");
			for(PrpLDlossPersTraceVo persTaceVo : persTaceVoList){
				sumReportFee = sumReportFee.add(NullToZero(persTaceVo.getSumReportFee()));
				sumRealFee = sumRealFee.add(NullToZero(persTaceVo.getSumRealFee()));
				sumDetractionFee = sumDetractionFee.add(NullToZero(persTaceVo.getSumDetractionFee()));
				sumdefLoss = sumdefLoss.add(NullToZero(persTaceVo.getSumdefLoss()));
				sumVeriReportFee = sumVeriReportFee.add(NullToZero(persTaceVo.getSumVeriReportFee()));
				sumVeriRealFee = sumVeriRealFee.add(NullToZero(persTaceVo.getSumVeriRealFee()));
				sumVeriDetractionFee = sumVeriDetractionFee.add(NullToZero(persTaceVo.getSumVeriDetractionFee()));
				sumVeriDefloss = sumVeriDefloss.add(NullToZero(persTaceVo.getSumVeriDefloss()));
			}
			persTaceMainVo.setSumReportFee(sumReportFee.toString());
			persTaceMainVo.setSumRealFee(sumRealFee.toString());
			persTaceMainVo.setSumDetractionFee(sumDetractionFee.toString());
			persTaceMainVo.setSumdefLoss(sumdefLoss.toString());
			persTaceMainVo.setSumVeriReportFee(sumVeriReportFee.toString());
			persTaceMainVo.setSumVeriRealFee(sumVeriRealFee.toString());
			persTaceMainVo.setSumVeriDetractionFee(sumVeriDetractionFee.toString());
			persTaceMainVo.setSumVeriDefloss(sumVeriDefloss.toString());
		}
		return persTaceMainVo;
	}
	
	private static BigDecimal NullToZero(BigDecimal strNum) {
		return strNum==null ? new BigDecimal("0") : strNum;
	} 
	
	/**
	 * 设置主子表关系
	 * 
	 * @param prpLDlossPersTraceMain
	 * @throws Exception 
	 * @modified: ☆XMSH(2016年1月11日 下午2:40:45): <br>
	 */
	public void setPkRelation(PrpLDlossPersTrace persTrace,SysUserVo userVo) throws Exception {

		persTrace.setPrpLDlossPersInjured(persTrace.getPrpLDlossPersInjured());
		persTrace.getPrpLDlossPersInjured().setPrpLDlossPersTrace(persTrace);

		persTrace.setComCode(userVo.getComCode());

		if(persTrace.getId()==null){// 新增
			persTrace.setValidFlag(CodeConstants.ValidFlag.VALID);
			persTrace.getPrpLDlossPersInjured().setValidFlag(CodeConstants.ValidFlag.VALID);
			persTrace.setCreateUser(userVo.getUserCode());
			persTrace.setUpdateTime(new Date());
			persTrace.setUpdateUser(userVo.getUserCode());
			persTrace.setOperatorCode(userVo.getUserCode());
			// 受伤部位
			persTrace.getPrpLDlossPersInjured().setCreateUser(userVo.getUserCode());
			persTrace.getPrpLDlossPersInjured().setCreateTime(new Date());
			persTrace.getPrpLDlossPersInjured().setUpdateTime(new Date());
			persTrace.getPrpLDlossPersInjured().setUpdateUser(userVo.getUserCode());
			persTrace.getPrpLDlossPersInjured().setInputTime(new Date());
		}else{
			persTrace.setUpdateUser(userVo.getUserCode());
			persTrace.setUpdateTime(new Date());
			// 受伤部位
			persTrace.getPrpLDlossPersInjured().setUpdateUser(userVo.getUserCode());
			persTrace.getPrpLDlossPersInjured().setUpdateTime(new Date());
		}

		for(PrpLDlossPersTraceFee persTraceFee:persTrace.getPrpLDlossPersTraceFees()){
			persTraceFee.setPrpLDlossPersTrace(persTrace);
			if(persTraceFee.getId()==null){// 新增
				persTraceFee.setValidFlag(CodeConstants.ValidFlag.VALID);
				persTraceFee.setCreateUser(userVo.getUserCode());
				persTraceFee.setCreateTime(new Date());
				persTraceFee.setUpdateUser(userVo.getUserCode());
				persTraceFee.setUpdateTime(new Date());
			}else{
				persTraceFee.setUpdateUser(userVo.getUserCode());
				persTraceFee.setUpdateTime(new Date());
			}
		}

		for(PrpLDlossPersExt persExt:persTrace.getPrpLDlossPersInjured().getPrpLDlossPersExts()){
			persExt.setPrpLDlossPersInjured(persTrace.getPrpLDlossPersInjured());

			if(persExt.getId()==null){
				persExt.setValidFlag(CodeConstants.ValidFlag.VALID);
				persExt.setCreateTime(new Date());
				persExt.setCreateUser(userVo.getUserCode());
			}else{
				persExt.setUpdateTime(new Date());
				persExt.setUpdateUser(userVo.getUserCode());
			}
		}
		for(PrpLDlossPersHospital persHospital:persTrace.getPrpLDlossPersInjured().getPrpLDlossPersHospitals()){
			persHospital.setPrpLDlossPersInjured(persTrace.getPrpLDlossPersInjured());
			if(persHospital.getId()==null){
				persHospital.setValidFlag(CodeConstants.ValidFlag.VALID);
				persHospital.setCreateUser(userVo.getUserCode());
				persHospital.setCreateTime(new Date());
			}else{
				persHospital.setUpdateUser(userVo.getUserCode());
				persHospital.setUpdateTime(new Date());
			}
		}
		for(PrpLDlossPersNurse persNurse:persTrace.getPrpLDlossPersInjured().getPrpLDlossPersNurses()){
			persNurse.setPrpLDlossPersInjured(persTrace.getPrpLDlossPersInjured());

			if(persNurse.getId()==null){
				persNurse.setValidFlag(CodeConstants.ValidFlag.VALID);
				persNurse.setInputTime(new Date());
			}
		}
		for(PrpLDlossPersRaise persRaise:persTrace.getPrpLDlossPersInjured().getPrpLDlossPersRaises()){
			persRaise.setPrpLDlossPersInjured(persTrace.getPrpLDlossPersInjured());
			if(persRaise.getId()==null){
				persRaise.setValidFlag(CodeConstants.ValidFlag.VALID);
				persRaise.setInputTime(new Date());
			}

		}
	}

	public void mergeAllList(PrpLDlossPersTrace persTrace,PrpLDlossPersTraceVo persTraceVo) {

		// 修改人员跟踪表
		Beans.copy().from(persTraceVo).excludeNull().to(persTrace);
		Beans.copy().from(persTraceVo).includes("sumReportFee","sumRealFee","sumDetractionFee","sumdefLoss").to(persTrace);

		// 修改涉案人员伤亡表
		PrpLDlossPersInjuredVo persInjuredVo = persTraceVo.getPrpLDlossPersInjured();
		PrpLDlossPersInjured persInjured = persTrace.getPrpLDlossPersInjured();
		Beans.copy().from(persInjuredVo).excludeNull().to(persInjured);
		Beans.copy().from(persInjuredVo).includes("deathTime").to(persInjured);

		// 受伤部位
		List<PrpLDlossPersExtVo> persExtVos = persInjuredVo.getPrpLDlossPersExts();
		List<PrpLDlossPersExt> persExts = persInjured.getPrpLDlossPersExts();
		mergeList(persExtVos,persExts,"id",PrpLDlossPersExt.class);

		// 住院情况
		List<PrpLDlossPersHospitalVo> persHospitalVos = persInjuredVo.getPrpLDlossPersHospitals();
		List<PrpLDlossPersHospital> persHospitals = persInjured.getPrpLDlossPersHospitals();
		mergeList(persHospitalVos,persHospitals,"id",PrpLDlossPersHospital.class);

		// 本次跟踪记录
		List<PrpLDlossPersTraceFeeVo> persTraceFeeVos = persTraceVo.getPrpLDlossPersTraceFees();
		List<PrpLDlossPersTraceFee> persTraceFees = persTrace.getPrpLDlossPersTraceFees();
		mergeList(persTraceFeeVos,persTraceFees,"id",PrpLDlossPersTraceFee.class);

		// 护理人员信息
		List<PrpLDlossPersRaiseVo> persRaiseVos = persInjuredVo.getPrpLDlossPersRaises();
		List<PrpLDlossPersRaise> persRaises = persInjured.getPrpLDlossPersRaises();
		mergeList(persRaiseVos,persRaises,"id",PrpLDlossPersRaise.class);

		// 被抚养人信息
		List<PrpLDlossPersNurseVo> persNurseVos = persInjuredVo.getPrpLDlossPersNurses();
		List<PrpLDlossPersNurse> persNurses = persInjured.getPrpLDlossPersNurses();
		mergeList(persNurseVos,persNurses,"id",PrpLDlossPersNurse.class);

	}

	public void mergeList(List voList,List poList,String idName,Class paramClass) {
		Map<Object,Object> map = new HashMap<Object,Object>();
		Map<Integer,Object> keyMap = new HashMap<Integer,Object>();
		Map<Object,Object> poMap = new HashMap<Object,Object>();

		for(int i = 0,count = voList.size(); i<count; i++ ){
			Object element = voList.get(i);
			if(element==null){
				continue;
			}
			Object key;
			try{
				key = PropertyUtils.getProperty(element,idName);
				map.put(key,element);
				keyMap.put(i,key);
			}
			catch(Exception e){
				throw new IllegalArgumentException(e);
			}
		}

		for(Iterator it = poList.iterator(); it.hasNext();){
			Object element = (Object)it.next();
			try{
				Object key = PropertyUtils.getProperty(element,idName);
				poMap.put(key,null);
				if( !map.containsKey(key)){
					// delete(element);
					databaseDao.deleteByObject(paramClass,element);
					it.remove();
				}else{
					Beans.copy().from(map.get(key)).excludeNull().to(element);
				}
			}
			catch(Exception e){
				throw new IllegalArgumentException(e);
			}
		}
		for(int i = 0,count = voList.size(); i<count; i++ ){
			Object element = voList.get(i);
			if(element==null){
				continue;
			}
			try{
				Object poElement = paramClass.newInstance();
				Object key = keyMap.get(i);
				if(key==null|| !poMap.containsKey(key)){
					Method setMethod;
					Beans.copy().from(element).to(poElement);
					poList.add(poElement);
				}
			}
			catch(Exception e){
				throw new IllegalArgumentException(e);
			}
		}
	}

	@Override
	public PrpLDlossPersTraceMainVo findPersTraceMainVobyId(Long id) {

		PrpLDlossPersTraceMain persTraceMain = null;
		if(id != null && !id.equals("")){
			 persTraceMain = databaseDao.findByPK(PrpLDlossPersTraceMain.class,id);
		}
		PrpLDlossPersTraceMainVo persTraceMainVo = null;
		if(persTraceMain!=null){
			persTraceMainVo = Beans.copyDepth().from(persTraceMain).to(PrpLDlossPersTraceMainVo.class);
		}
		return persTraceMainVo;
	}

	@Override
	public void updateAndsaveAndDelSonsPrpLDlossPersTrace(PrpLDlossPersTraceVo prpLDlossPersTraceVo) {
		if(prpLDlossPersTraceVo.getId()!=null){
			PrpLDlossPersTrace prpLDlossPersTrace=databaseDao.findByPK(PrpLDlossPersTrace.class, prpLDlossPersTraceVo.getId());
			if(prpLDlossPersTrace!=null){
				Beans.copy().from(prpLDlossPersTraceVo).excludeNull().to(prpLDlossPersTrace);
				List<PrpLDlossPersTraceFee> traceFeepos=prpLDlossPersTrace.getPrpLDlossPersTraceFees();
				prpLDlossPersTrace.setPrpLDlossPersTraceFees(null);
				if(traceFeepos!=null && traceFeepos.size()>0){
					for(PrpLDlossPersTraceFee traceFee:traceFeepos){
						traceFee.setPrpLDlossPersTrace(null);
						databaseDao.deleteByObject(PrpLDlossPersTraceFee.class, traceFee);
					}
					
				}
				List<PrpLDlossPersTraceFee> prpLDlossPersTraceFees=new ArrayList<PrpLDlossPersTraceFee>(0);
				if(prpLDlossPersTraceVo.getPrpLDlossPersTraceFees()!=null && prpLDlossPersTraceVo.getPrpLDlossPersTraceFees().size()>0){
					for(PrpLDlossPersTraceFeeVo traceFeeVo:prpLDlossPersTraceVo.getPrpLDlossPersTraceFees()){
						PrpLDlossPersTraceFee prpLDlossPersTraceFee=new PrpLDlossPersTraceFee();
						Beans.copy().from(traceFeeVo).excludeNull().to(prpLDlossPersTraceFee);
						prpLDlossPersTraceFee.setPrpLDlossPersTrace(prpLDlossPersTrace);
						prpLDlossPersTraceFees.add(prpLDlossPersTraceFee);
					}
				}
				prpLDlossPersTrace.setPrpLDlossPersTraceFees(prpLDlossPersTraceFees);
			}
		}
		
	}

	@Override
	public void updateAndsaveAndDelSonsPrpLDlossPersInjured(PrpLDlossPersInjuredVo prpLDlossPersInjuredVo) {
		if(prpLDlossPersInjuredVo.getId()!=null){
			PrpLDlossPersInjured prpLDlossPersInjured=databaseDao.findByPK(PrpLDlossPersInjured.class, prpLDlossPersInjuredVo.getId());
			if(prpLDlossPersInjured!=null){
				Beans.copy().from(prpLDlossPersInjuredVo).excludeNull().to(prpLDlossPersInjured);
				List<PrpLDlossPersExt> prpLDlossPersExts=prpLDlossPersInjured.getPrpLDlossPersExts();
				List<PrpLDlossPersRaise> prpLDlossPersRaises=prpLDlossPersInjured.getPrpLDlossPersRaises();
				List<PrpLDlossPersHospital> prpLDlossPersHospitals=prpLDlossPersInjured.getPrpLDlossPersHospitals();
				prpLDlossPersInjured.setPrpLDlossPersExts(null);
				prpLDlossPersInjured.setPrpLDlossPersHospitals(null);
				prpLDlossPersInjured.setPrpLDlossPersRaises(null);
				if(prpLDlossPersExts!=null && prpLDlossPersExts.size()>0){
					for(PrpLDlossPersExt extpo:prpLDlossPersExts){
						 extpo.setPrpLDlossPersInjured(null);
						 databaseDao.deleteByObject(PrpLDlossPersExt.class, extpo);
					}
				}
				if(prpLDlossPersRaises!=null && prpLDlossPersRaises.size()>0){
					for(PrpLDlossPersRaise extpo:prpLDlossPersRaises){
						 extpo.setPrpLDlossPersInjured(null);
						 databaseDao.deleteByObject(PrpLDlossPersRaise.class, extpo);
					}
				}
				if(prpLDlossPersHospitals!=null && prpLDlossPersHospitals.size()>0){
					for(PrpLDlossPersHospital extpo:prpLDlossPersHospitals){
						 extpo.setPrpLDlossPersInjured(null);
						 databaseDao.deleteByObject(PrpLDlossPersHospital.class,extpo);
					}
				}
				List<PrpLDlossPersExt> prpLDlossPersExtList=new ArrayList<PrpLDlossPersExt>(0);
				if(prpLDlossPersInjuredVo.getPrpLDlossPersExts()!=null && prpLDlossPersInjuredVo.getPrpLDlossPersExts().size()>0){
					for(PrpLDlossPersExtVo extVo:prpLDlossPersInjuredVo.getPrpLDlossPersExts()){
						PrpLDlossPersExt extpo=new PrpLDlossPersExt();
						Beans.copy().from(extVo).excludeNull().to(extpo);
						extpo.setPrpLDlossPersInjured(prpLDlossPersInjured);
						prpLDlossPersExtList.add(extpo);
						//databaseDao.save(PrpLDlossPersExt.class, extpo);
					}
				}
				prpLDlossPersInjured.setPrpLDlossPersExts(prpLDlossPersExtList);
				List<PrpLDlossPersRaise> prpLDlossPersRaiseList=new ArrayList<PrpLDlossPersRaise>(0);
				if(prpLDlossPersInjuredVo.getPrpLDlossPersRaises()!=null && prpLDlossPersInjuredVo.getPrpLDlossPersRaises().size()>0){
					for(PrpLDlossPersRaiseVo raiseVo:prpLDlossPersInjuredVo.getPrpLDlossPersRaises()){
						PrpLDlossPersRaise raisepo=new PrpLDlossPersRaise();
						Beans.copy().from(raiseVo).excludeNull().to(raisepo);
						raisepo.setPrpLDlossPersInjured(prpLDlossPersInjured);
						prpLDlossPersRaiseList.add(raisepo);
						//databaseDao.save(PrpLDlossPersRaise.class, raisepo);
					}
				}
				prpLDlossPersInjured.setPrpLDlossPersRaises(prpLDlossPersRaiseList);
				List<PrpLDlossPersHospital> prpLDlossPersHospitalList=new ArrayList<PrpLDlossPersHospital>(0);
				if(prpLDlossPersInjuredVo.getPrpLDlossPersHospitals()!=null && prpLDlossPersInjuredVo.getPrpLDlossPersHospitals().size()>0){
					for(PrpLDlossPersHospitalVo hisptalVo:prpLDlossPersInjuredVo.getPrpLDlossPersHospitals()){
						PrpLDlossPersHospital hisptalpo=new PrpLDlossPersHospital();
						Beans.copy().from(hisptalVo).excludeNull().to(hisptalpo);
						hisptalpo.setPrpLDlossPersInjured(prpLDlossPersInjured);
						prpLDlossPersHospitalList.add(hisptalpo);
						//databaseDao.save(PrpLDlossPersHospital.class, hisptalpo);
					}
				}
				prpLDlossPersInjured.setPrpLDlossPersHospitals(prpLDlossPersHospitalList);
			}
			
		}
		
	}

	

}
