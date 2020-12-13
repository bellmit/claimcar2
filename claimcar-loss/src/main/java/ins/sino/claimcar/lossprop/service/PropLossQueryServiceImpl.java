/******************************************************************************
* CREATETIME : 2016年2月3日 上午10:32:15
******************************************************************************/
package ins.sino.claimcar.lossprop.service;

import ins.framework.common.ResultPage;
import ins.framework.dao.database.DatabaseDao;
import ins.framework.dao.database.support.Page;
import ins.platform.utils.SqlJoinUtils;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.common.constants.LossType;
import ins.sino.claimcar.flow.po.PrpLWfTaskQuery;
import ins.sino.claimcar.lossprop.po.PrpLdlossPropMain;
import ins.sino.claimcar.lossprop.vo.PropQueryReslutVo;
import ins.sino.claimcar.lossprop.vo.PropQueryVo;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Path;

import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;


/**
 * 财产定损各种查询
 * @author ★LiuPing
 * @CreateTime 2016年2月3日
 */
@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"}, timeout = 1000000)
@Path("propLossQueryService")
public class PropLossQueryServiceImpl implements PropLossQueryService {

	private static Logger logger = LoggerFactory.getLogger(PropLossQueryService.class);

	@Autowired
	private DatabaseDao databaseDao;
	/* (non-Javadoc)
	 * @see ins.sino.claimcar.lossprop.service.PropLossQueryService#findPageForAdjust(ins.sino.claimcar.lossprop.vo.PropQueryVo)
	 */
	@Override
	public ResultPage<PropQueryReslutVo> findPageForAdjust(PropQueryVo queryVo) throws Exception {

		SqlJoinUtils sqlUtil = new SqlJoinUtils();
		if("modify".equals(queryVo.getDeflossFlag())){//修改定损
			sqlUtil = this.findPageForModify(queryVo);
		}else{
			sqlUtil = this.findPageForAdd(queryVo);
		}
		
		// 可后几几位查询
		sqlUtil.andReverse(queryVo,"qry",7,"registNo","policyNo","licenseNo","frameNo");

		sqlUtil.andEquals(queryVo,"qry","mercyFlag");

		sqlUtil.andLike2(queryVo,"qry","insuredName");
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
	private SqlJoinUtils findPageForAdd(PropQueryVo queryVo) {
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
		sqlUtil.andComSql("main","makeCom",queryVo.getComCode());
		
		return sqlUtil;
	}
	
	
	private SqlJoinUtils findPageForModify(PropQueryVo queryVo) {
		/*
		 * 查询sql大概为
		Select  Main.* From prpLdlossPropMain main ,prplwftaskquery qry  
		Where main.registNo=qry.registNo And main.lossState='00' And  main.underWriteFlag='1'  
		And registNo=? And   mercyFlag=?   And license=? And policyNo=?    And frameNo=? And  insuredName Like ?
		 */
		
		SqlJoinUtils sqlUtil = new SqlJoinUtils();
		sqlUtil.append(" FROM PrpLdlossPropMain main,PrpLWfTaskQuery qry ");
		// 查询核损完成，未理算的数据
		sqlUtil.append("Where main.registNo=qry.registNo And main.lossState=? And  main.underWriteFlag=? ");
		sqlUtil.addParamValue("00");
		sqlUtil.addParamValue("1");
		
		//加入机构限制
		sqlUtil.andComSql("main","makeCom",queryVo.getComCode());
		return sqlUtil;
	}

}
