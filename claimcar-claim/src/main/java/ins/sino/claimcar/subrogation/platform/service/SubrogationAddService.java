/******************************************************************************
 * CREATETIME : 2016年3月16日 下午3:32:55
 ******************************************************************************/
package ins.sino.claimcar.subrogation.platform.service;

import ins.framework.dao.database.DatabaseDao;
import ins.framework.dao.database.support.QueryRule;
import ins.framework.utils.Beans;
import ins.sino.claimcar.carplatform.constant.RequestType;
import ins.sino.claimcar.carplatform.controller.PlatformController;
import ins.sino.claimcar.carplatform.util.PlatformFactory;
import ins.sino.claimcar.carplatform.vo.BiResponseHeadVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;
import ins.sino.claimcar.subrogation.platform.vo.SubrogationCheckBaseVo;
import ins.sino.claimcar.subrogation.platform.vo.SubrogationCheckDataVo;
import ins.sino.claimcar.subrogation.platform.vo.SubrogationCheckReqBodyVo;
import ins.sino.claimcar.subrogation.platform.vo.SubrogationCheckResBodyVo;
import ins.sino.claimcar.subrogation.po.PrpLPlatCheck;
import ins.sino.claimcar.subrogation.po.PrpLPlatCheckSub;
import ins.sino.claimcar.subrogation.po.PrpLPlatLock;
import ins.sino.claimcar.subrogation.po.PrpLRecoveryOrPay;
import ins.sino.claimcar.subrogation.vo.CheckInfoDataVo;
import ins.sino.claimcar.subrogation.vo.PrpLPlatCheckVo;
import ins.sino.claimcar.subrogation.vo.PrpLPlatLockVo;
import ins.sino.claimcar.subrogation.vo.PrpLRecoveryOrPayVo;
import ins.sino.claimcar.subrogation.vo.SCheckQueryVo;
import ins.sino.claimcar.subrogation.vo.SubrogationCheckVo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 互审信息 数据库方法
 * @author ★YangKun
 * @CreateTime 2016年3月16日
 */

@Service("subrogationAddService")
public class SubrogationAddService {

	private Logger logger = LoggerFactory.getLogger(SubrogationAddService.class);

	@Autowired
	private DatabaseDao databaseDao;

	/**
	 * 互审信息是先删除后插入的，或者以后调整为 更新结算码更新数据 也行
	 * @param checkList
	 */
	public void save(List<SubrogationCheckVo> checkList) {
		
		for (SubrogationCheckVo checkVo : checkList) {
			PrpLPlatCheck prpLPlatCheck = this.findSubrogationCheckPo(checkVo.getRecoveryCode());
			if(prpLPlatCheck!=null){
				Beans.copy().from(checkVo).to(prpLPlatCheck);
				try {
					if(checkVo.getRecoverDStart()!=null){//201608041432
						logger.debug(checkVo.getRecoverDStart());
						SimpleDateFormat format = new SimpleDateFormat("yyyymmddHHmm");   
						Date start = format.parse(checkVo.getRecoverDStart());
						prpLPlatCheck.setRecoverdStart(start);
					}
				} catch (ParseException e) {
				}
				
				List<PrpLPlatCheckSub> checkSubList = prpLPlatCheck.getPrpLPlatCheckSubs();
				for(CheckInfoDataVo checkInfoVo : checkVo.getPrpLPlatCheckSubs()){
					for(PrpLPlatCheckSub checkSub : checkSubList){
						if(checkInfoVo.getCheckOwnType().equals(checkSub.getCheckOwnType())){
							Beans.copy().from(checkInfoVo).to(checkSub);
						}
					}
				}
				
			}else{
				prpLPlatCheck = Beans.copyDepth().from(checkVo).to(PrpLPlatCheck.class);
				try {
					if(checkVo.getRecoverDStart()!=null){//201608041432
						logger.debug(checkVo.getRecoverDStart());
						SimpleDateFormat format = new SimpleDateFormat("yyyymmddHHmm");   
						Date start = format.parse(checkVo.getRecoverDStart());
						prpLPlatCheck.setRecoverdStart(start);
					}
				} catch (ParseException e) {
				}
				
				for (PrpLPlatCheckSub sub : prpLPlatCheck.getPrpLPlatCheckSubs()) {
					sub.setRecoveryCode(prpLPlatCheck.getRecoveryCode());
					sub.setPrpLPlatCheck(prpLPlatCheck);
				}
			}
			
			databaseDao.save(PrpLPlatCheck.class, prpLPlatCheck);
		}
		
	}


	public List<PrpLPlatCheckVo> findByOther(SubrogationCheckVo CheckVo) {
		List<Object> paramValues = new ArrayList<Object>();
		StringBuffer queryString = new StringBuffer(
				"from PrpLPlatCheck c where 1=1 ");
		if (StringUtils.isNotBlank(CheckVo.getRecoveryCode())) {
			queryString.append("and c.recoveryCode  = ? ");
			paramValues.add(CheckVo.getRecoveryCode());
		}
		if (StringUtils.isNotBlank(CheckVo.getRecoverReportNo())) {
			queryString.append(" and c.recoverReportNo  = ? ");
			paramValues.add(CheckVo.getRecoverReportNo());
		}
		if (StringUtils.isNotBlank(CheckVo.getCompensateReportNo())) {
			queryString.append(" and c.compensateReportNo  = ? ");
			paramValues.add(CheckVo.getCompensateReportNo());
		}
		List<PrpLPlatCheck> oldTCheckPo = databaseDao.findAllByHql(
				PrpLPlatCheck.class, queryString.toString(),
				paramValues.toArray());
		List<PrpLPlatCheckVo> checkVoList = new ArrayList<PrpLPlatCheckVo>();
		PrpLPlatCheckVo checkVo = new PrpLPlatCheckVo();
		for (int i = 0; i < oldTCheckPo.size(); i++) {
			PrpLPlatCheck prpcheck = oldTCheckPo.get(i);
			// po转vo
			checkVo = Beans.copyDepth().from(prpcheck)
					.to(PrpLPlatCheckVo.class);
			checkVoList.add(checkVo);
		}
		return checkVoList;
	}
	
	public List<SubrogationCheckVo> findSubrogationCheck(String recoveryCode){
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("recoveryCode", recoveryCode);
		List<PrpLPlatCheck> platCheckList = databaseDao.findAll(PrpLPlatCheck.class, queryRule);
		
		List<SubrogationCheckVo> checkVoList = new ArrayList<SubrogationCheckVo>();
		if(platCheckList!=null && !platCheckList.isEmpty()){
			checkVoList = 	Beans.copyDepth().from(platCheckList).toList(SubrogationCheckVo.class);
		}
		
		return checkVoList;
	}
	
	public PrpLPlatCheck findSubrogationCheckPo(String recoveryCode){
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("recoveryCode", recoveryCode);
		PrpLPlatCheck platCheckPo = databaseDao.findUnique(PrpLPlatCheck.class, queryRule);
		
		return platCheckPo;
	}
}
