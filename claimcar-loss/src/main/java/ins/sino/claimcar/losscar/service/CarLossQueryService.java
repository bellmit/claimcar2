package ins.sino.claimcar.losscar.service;

import ins.framework.common.ResultPage;
import ins.framework.dao.database.DatabaseDao;
import ins.framework.dao.database.support.Page;
import ins.platform.utils.SqlJoinUtils;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.claim.po.PrpLClaim;
import ins.sino.claimcar.common.constants.LossType;
import ins.sino.claimcar.flow.po.PrpLWfTaskQuery;
import ins.sino.claimcar.losscar.po.PrpLDlossCarInfo;
import ins.sino.claimcar.losscar.po.PrpLDlossCarMain;
import ins.sino.claimcar.losscar.vo.CarQueryReslutVo;
import ins.sino.claimcar.losscar.vo.CarQueryVo;
import ins.sino.claimcar.lossprop.po.PrpLdlossPropMain;
import ins.sino.claimcar.lossprop.vo.PropQueryReslutVo;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service(value = "carLossQueryService")
public class CarLossQueryService {
	
	private static Logger logger = LoggerFactory.getLogger(CarLossQueryService.class);

	@Autowired
	private DatabaseDao databaseDao;
	/**
	 * 查询可以定损修改的定损任务
	 * 定损任务已经核损通过
	 	 该案件未发起理算或者理算任务已经注销
		 该定损没有损余回收任务 TODO
		该定损没有发起复检，即prpldeflossMain的recheck字段为0
	 * @param queryVo
	 * @return
	 * @modified  <br>
	 */
	public ResultPage<CarQueryReslutVo> findPageForAdjust(CarQueryVo queryVo) throws Exception {
		SqlJoinUtils sqlUtil = new SqlJoinUtils();
		if("modify".equals(queryVo.getDeflossFlag())){//修改定损
			sqlUtil = this.findPageForModify(queryVo);
		}else{
			sqlUtil = this.findPageForAdd(queryVo);
		}
		// 可后几几位查询
		sqlUtil.andReverse(queryVo,"qry",7,"registNo","policyNo","frameNo");

		sqlUtil.andEquals(queryVo,"main","mercyFlag");
		
		sqlUtil.andEquals(queryVo,"info");

		sqlUtil.andLike2(queryVo,"qry","insuredName","licenseNo");
		// 其他特殊条件

		// 排序
		sqlUtil.append(" ORDER BY main.mercyFlag DESC, main.updateTime DESC");
		// 开始记录数
		int start = queryVo.getStart();
		// 查询记录数量
		int length = queryVo.getLength();

		String sql = sqlUtil.getSql();
		Object[] values = sqlUtil.getParamValues();
		logger.debug("taskQrySql="+sql);
		logger.debug("ParamValues="+ArrayUtils.toString(values));
//		System.out.println("taskQrySql="+sql);
//		System.out.println("ParamValues="+ArrayUtils.toString(values));
		Page<Object[]> page = databaseDao.findPageByHql(sql,start/length+1,length,values);

		// 对象转换
		List<CarQueryReslutVo> resultVoList = new ArrayList<CarQueryReslutVo>();
		for(int i = 0; i<page.getResult().size(); i++ ){

			Object[] obj = page.getResult().get(i);
			PrpLDlossCarMain mainPo = (PrpLDlossCarMain)obj[0];
			PrpLWfTaskQuery queryPo = (PrpLWfTaskQuery)obj[1];
			PrpLDlossCarInfo carInfo = (PrpLDlossCarInfo)obj[2];
			PrpLClaim claim = (PrpLClaim)obj[3];
			CarQueryReslutVo resultVo = new CarQueryReslutVo();
			// 将这过两个Po转换为Vo
			resultVo.setLossId(mainPo.getId());// 定损任务号
			resultVo.setLicenseNo(carInfo.getLicenseNo());// 车牌号

			// 下面属性少就不用copy了
			resultVo.setRegistNo(queryPo.getRegistNo());// 报案号
			resultVo.setModelName(carInfo.getModelName());
			resultVo.setDeflossFlag(queryVo.getDeflossFlag());//定损方式
			resultVo.setMercyFlag(mainPo.getMercyFlag());// 案件紧急程度
			resultVo.setCustomerLevel(queryPo.getCustomerLevel());// 客户
			resultVo.setIsMajorCase(queryPo.getIsMajorCase());
			resultVo.setInsuredName(queryPo.getInsuredName());//被保险人
			resultVo.setIsAlarm(claim.getIsAlarm());
			resultVo.setTpFlag(claim.getTpFlag());
			resultVo.setIsSubRogation(claim.getIsSubRogation());
			resultVo.setIsClaimSelf(claim.getCaseFlag());
			
			
			resultVoList.add(resultVo);
		}

		ResultPage<CarQueryReslutVo> resultPage = new ResultPage<CarQueryReslutVo>(start,length,page.getTotalCount(),resultVoList);

		return resultPage;

	}
	
	/**
	 * 定损修改查询
	 * 定损任务已经核损通过
	 * 该案件未发起理算或者理算任务已经注销
	 * 该定损没有损余回收任务
	 * 该定损没有发起复检，即prpldeflossMain的recheck字段为0 
	 * ☆yangkun(2016年2月23日 上午11:58:50): <br>
	 */
	private SqlJoinUtils findPageForModify(CarQueryVo queryVo){
		SqlJoinUtils sqlUtil = new SqlJoinUtils();
		sqlUtil.append(" FROM PrpLDlossCarMain main,PrpLWfTaskQuery qry,PrpLDlossCarInfo info ,PrpLClaim claim ");
		// 查询核损完成，未理算的数据
		sqlUtil.append("Where main.registNo=qry.registNo  And main.carId = info.id ");
		sqlUtil.append(" And qry.policyNo = claim.policyNo And main.registNo = claim.registNo ");
		sqlUtil.append("And  main.underWriteFlag=? ");
		sqlUtil.addParamValue("1");
		
		sqlUtil.append("And main.lossState =? ");
		sqlUtil.addParamValue("00");
		
		sqlUtil.append("And (main.recycleFlag is null or main.recycleFlag =? )");
		sqlUtil.addParamValue("0");
		
		sqlUtil.append("And (main.reCheckFlag is null or main.reCheckFlag =?)");
		sqlUtil.addParamValue("0");
		//加入机构限制
		sqlUtil.andTopComSql("main","makeCom",queryVo.getComCode());
		
		return sqlUtil;
	}
	
	/**
	 * 原定损任务已经核损通过
	 * 该案件已经核赔通过
	 * 追加定损发起后未处理完成前，不能再次发起追加定损
	 * ☆yangkun(2016年2月23日 下午8:08:55): <br>
	 */
	private SqlJoinUtils findPageForAdd(CarQueryVo queryVo){
		SqlJoinUtils sqlUtil = new SqlJoinUtils();
		sqlUtil.append(" FROM PrpLDlossCarMain main,PrpLWfTaskQuery qry,PrpLDlossCarInfo info ,PrpLClaim claim ");
		// 查询核损完成，未理算的数据 ,PrpLClaim claim
		sqlUtil.append("Where main.registNo=qry.registNo  And main.carId = info.id  And  main.underWriteFlag=? ");
		sqlUtil.addParamValue("1");
		
		sqlUtil.append(" And qry.policyNo = claim.policyNo And main.registNo = claim.registNo ");
		//追加定损  不存在追加定损未处理完的任务
		sqlUtil.append("And main.lossState !=?");
		sqlUtil.addParamValue("00");
		
		sqlUtil.append("And main.deflossSourceFlag != ?");
		sqlUtil.addParamValue(CodeConstants.defLossSourceFlag.ADDTIONALDEFLOSS);
		
		sqlUtil.append("And main.reLossCarId is null ");//原定损任务
		sqlUtil.append("And not exists (select 1 from PrpLDlossCarMain addMain where main.carId= addMain.carId ");
		//sqlUtil.append("And addMain.underWriteFlag !=? ");
		//sqlUtil.addParamValue("1");
		sqlUtil.append("And addMain.lossState =?)");//追加的定损任务未赔付过 需要排除
		sqlUtil.addParamValue("00");
		
		//加入机构限制
		sqlUtil.andTopComSql("main","makeCom",queryVo.getComCode());
		
		
		return sqlUtil;
	}

	/**
	 * 查询可以定损修改的定损任务
	 * @param queryVo
	 * @return
	 * @modified: ☆LiuPing(2016年2月3日 ): <br>
	 */
	public ResultPage<PropQueryReslutVo> findPropPageForAdjust(CarQueryVo queryVo) throws Exception {

		SqlJoinUtils sqlUtil = new SqlJoinUtils();
		if("modify".equals(queryVo.getDeflossFlag())){//修改定损
			sqlUtil = this.findPropPageForModify(queryVo);
		}else{
			sqlUtil = this.findPropPageForAdd(queryVo);
		}
		
		// 可后几几位查询
		sqlUtil.andReverse(queryVo,"qry",7,"registNo","policyNo","frameNo");

		sqlUtil.andEquals(queryVo,"qry","mercyFlag");

		sqlUtil.andLike2(queryVo,"qry","insuredName","licenseNo");
		// 其他特殊条件

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
		List<PropQueryReslutVo> resultVoList = new ArrayList<PropQueryReslutVo>();
		for(int i = 0; i<page.getResult().size(); i++ ){

			Object[] obj = page.getResult().get(i);
			PrpLdlossPropMain mainPo = (PrpLdlossPropMain)obj[0];
			PrpLWfTaskQuery queryPo = (PrpLWfTaskQuery)obj[1];
			PropQueryReslutVo resultVo = new PropQueryReslutVo();
			// 将这过两个Po转换为Vo
			resultVo.setLossId(mainPo.getId());// 定损任务号
			resultVo.setLicense(mainPo.getLicense());// 车牌号
			resultVo.setLossType(LossType.codeOf(mainPo.getLossType()).getName());// 损失方 0地面1标的3三者
			resultVo.setLossTypeName(LossType.codeOf(mainPo.getLossType()).getName2());// *项目损失项,损失方为0,取值为地面财产损失,1取值为标的车财产损失,3取值为三者车财产损失*/
			resultVo.setCusTypeCode(null);// 客户类型

			// 下面属性少就不用copy了
			resultVo.setMercyFlag(queryPo.getMercyFlag());// 案件紧急程度
			resultVo.setCustomerLevel(queryPo.getCustomerLevel());// 客户登记
			resultVo.setRegistNo(queryPo.getRegistNo());// 报案号
			resultVo.setPolicyNo(queryPo.getPolicyNo());// 保单号
			resultVo.setPolicyNoLink(queryPo.getPolicyNoLink());// 关联保单号
			resultVo.setInsuredName(queryPo.getInsuredName());// 被保险人
			resultVo.setComCodePly(queryPo.getComCodePly());// 承保机构
			resultVo.setDeflossFlag(queryVo.getDeflossFlag());
			resultVoList.add(resultVo);
		}

		ResultPage<PropQueryReslutVo> resultPage = new ResultPage<PropQueryReslutVo>(start,length,page.getTotalCount(),resultVoList);

		return resultPage;

	}
	
	private SqlJoinUtils findPropPageForAdd(CarQueryVo queryVo) {
		SqlJoinUtils sqlUtil = new SqlJoinUtils();
		sqlUtil.append(" FROM PrpLdlossPropMain main,PrpLWfTaskQuery qry ");
		// 查询核损完成，未理算的数据
		sqlUtil.append("Where main.registNo=qry.registNo  And  main.underWriteFlag=? ");
		sqlUtil.addParamValue("1");
		
		//追加定损  不存在追加定损未处理完的任务
		sqlUtil.append("And main.lossState !=?");
		sqlUtil.addParamValue("00");
		
		sqlUtil.append("And main.deflossSourceFlag != ?");
		sqlUtil.addParamValue(CodeConstants.defLossSourceFlag.ADDTIONALDEFLOSS);
		
		sqlUtil.append("And main.reLossPropId is null ");
		sqlUtil.append("And not exists (select 1 from PrpLdlossPropMain addMain where addMain.reLossPropId= main.id ");
		//sqlUtil.append("And addMain.underWriteFlag !=? ");
		//sqlUtil.addParamValue("1");
		
		sqlUtil.append("And addMain.lossState =?)");
		sqlUtil.addParamValue("00");
		
		//加入机构限制
		sqlUtil.andTopComSql("main","makeCom",queryVo.getComCode());
		
		return sqlUtil;
	}
	
	
	private SqlJoinUtils findPropPageForModify(CarQueryVo queryVo) {
		/*
		 * 查询sql大概为
		Select  Main.* From prpLdlossPropMain main ,prplwftaskquery qry  
		Where main.registNo=qry.registNo And main.lossState='00' And  main.underWriteFlag='1'  and main.auditstatus='submitVloss'
		And registNo=? And   mercyFlag=?   And license=? And policyNo=?    And frameNo=? And  insuredName Like ?
		 */
		
		SqlJoinUtils sqlUtil = new SqlJoinUtils();
		sqlUtil.append(" FROM PrpLdlossPropMain main,PrpLWfTaskQuery qry ");
		// 查询核损完成，未理算的数据
		sqlUtil.append("Where main.registNo=qry.registNo And main.lossState=? And  main.underWriteFlag=? and main.auditStatus=? ");
		sqlUtil.addParamValue("00");
		sqlUtil.addParamValue("1");
		sqlUtil.addParamValue("submitVloss");
		
		//加入机构限制
		sqlUtil.andTopComSql("main","makeCom",queryVo.getComCode());
		return sqlUtil;
	}
}
