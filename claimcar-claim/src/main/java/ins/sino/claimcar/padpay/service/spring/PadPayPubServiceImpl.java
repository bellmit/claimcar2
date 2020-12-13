/******************************************************************************
 * CREATETIME : 2016年4月7日 下午4:51:16
 ******************************************************************************/
package ins.sino.claimcar.padpay.service.spring;

import ins.framework.dao.database.DatabaseDao;
import ins.framework.dao.database.support.QueryRule;
import ins.framework.utils.Beans;
import ins.platform.utils.SqlJoinUtils;
import ins.sino.claimcar.claim.vo.PrpLPadPayMainVo;
import ins.sino.claimcar.claim.vo.PrpLPadPayPersonVo;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.service.WfFlowQueryService;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.padpay.po.PrpLPadPayMain;
import ins.sino.claimcar.padpay.po.PrpLPadPayPerson;
import ins.sino.claimcar.pay.service.PadPayPubService;
import ins.sino.claimcar.pay.service.PadPayService;

import java.util.List;

import javax.ws.rs.Path;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;

/**
 * 垫付 API 服务 实现类
 * @author ★Luwei
 */
@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"})
@Path("padPayPubService")
public class PadPayPubServiceImpl implements PadPayPubService {

	@Autowired
	DatabaseDao databaseDao;

	@Autowired
	PadPayService padPayService;
	
	@Autowired
	private WfFlowQueryService wfFlowQueryService;

	/**
	 * 根据报案号查询垫付主表
	 * @param registNo
	 * @return
	 */
	@Override
	public PrpLPadPayMainVo queryPadPay(String registNo,String compeNo) {
		PrpLPadPayMainVo payMainVo = null;
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo",registNo);
//		queryRule.addEqual("compensateNo",compeNo);
		List<PrpLPadPayMain> padPayMain = databaseDao.findAll(PrpLPadPayMain.class,queryRule);
		if(padPayMain!=null&&padPayMain.size()>0){
			payMainVo = Beans.copyDepth().from(padPayMain.get(0)).to(PrpLPadPayMainVo.class);
		}
		return payMainVo;
	}

	@Override
	public PrpLPadPayMainVo findPadPay(String registNo) {
		PrpLPadPayMainVo PadPayVo = null;
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo",registNo);
		queryRule.addSql(" (underwriteFlag <> '7' or underwriteFlag is null) ");
		queryRule.addAscOrder("createTime");
		List<PrpLPadPayMain> padPayMain = databaseDao.findAll(PrpLPadPayMain.class,queryRule);
		if (padPayMain != null && padPayMain.size() > 0) {
			PadPayVo = Beans.copyDepth().from(padPayMain.get(0)).to(PrpLPadPayMainVo.class);
		}
		return PadPayVo;
	}
	
	@Override
	public PrpLPadPayMainVo queryByPolicyNo(String registNo,String policyNo){
		QueryRule queryRule = QueryRule.getInstance();
		PrpLPadPayMainVo PadPayVo = null;
		queryRule.addEqual("registNo",registNo);
		queryRule.addEqual("policyNo",policyNo);
		List<PrpLPadPayMain> padPayMain = databaseDao.findAll(PrpLPadPayMain.class,queryRule);
		if (padPayMain != null && padPayMain.size() > 0) {
			PadPayVo = Beans.copyDepth().from(padPayMain.get(0)).to(PrpLPadPayMainVo.class);
		}
		return PadPayVo;
	}

	@Override
	public void updatePadPay(PrpLPadPayMainVo padPayVo) {
		PrpLPadPayMain padPay = databaseDao.findByPK(PrpLPadPayMain.class,padPayVo.getId());
		if (padPay != null) {
			Beans.copy().from(padPayVo).excludeNull().to(padPay);
			databaseDao.update(PrpLPadPayMain.class,padPay);
		}
		List<PrpLPadPayPersonVo> personVoList = padPayVo.getPrpLPadPayPersons();
		if ( personVoList != null && !personVoList.isEmpty() ) {
			for (PrpLPadPayPersonVo personVo : personVoList) {
				PrpLPadPayPerson personPo = databaseDao.findByPK(PrpLPadPayPerson.class,personVo.getId());
				if ( personPo != null ) {
					Beans.copy().from(personVo).excludeNull().to(personPo);
					databaseDao.update(PrpLPadPayPerson.class,personPo);
				}
			}
		}
	}

	@Override
	public boolean isPadPayAllPassed(String registNo) {
		boolean padPayResult = true;
		List<PrpLPadPayMainVo> prpLPadPayMainVoList = padPayService
				.findPadPayMainByRegistNo(registNo);
		if (prpLPadPayMainVoList != null && prpLPadPayMainVoList.size() > 0) {
            for(PrpLPadPayMainVo prpLPadPayMainVo:prpLPadPayMainVoList){
            	if(!"1".equals(prpLPadPayMainVo.getUnderwriteFlag())
            			&& !"7".equals(prpLPadPayMainVo.getUnderwriteFlag())){//未核赔通过并且未注销
            		padPayResult = false;
            		break;
            	}
            }
		}
		if (padPayResult) {// 判断是否有未接收的垫付任务
			List<PrpLWfTaskVo> prpLWfTaskVoList = wfFlowQueryService
					.findUnAcceptTask(registNo, FlowNode.PadPay.name());
			if (prpLWfTaskVoList != null && prpLWfTaskVoList.size() > 0) {
				padPayResult = false;
			}
		}
		return padPayResult;
	}

	@Override
	public int saveOrUpdateSettleNo(String settleNo,String accountNo,String operateType, String compensateNo,String serialNo){
		SqlJoinUtils sqlUtil=new SqlJoinUtils();
		sqlUtil.append(" from PrpLPadPayPerson person where 1=1 ");
		sqlUtil.append(" and person.serialNo=? ");
		sqlUtil.addParamValue(serialNo);
		sqlUtil.append(" and person.prpLPadPayMain.compensateNo=? ");
		sqlUtil.addParamValue(compensateNo);
		sqlUtil.append(" and exists(select 1 from PrpLPayCustom custom where custom.id=person.payeeId) ");
		//operateType==0是销毁结算单号
		if("0".equals(operateType)){
			sqlUtil.append(" and person.settleNo=? ");
			sqlUtil.addParamValue(settleNo);
		}
		String sql = sqlUtil.getSql();
		Object[] values = sqlUtil.getParamValues();
		
		List<PrpLPadPayPerson> PrpLPadPayPersonList = databaseDao.findAllByHql(PrpLPadPayPerson.class,sql, values);
		int count = 0;
		if(PrpLPadPayPersonList!=null && PrpLPadPayPersonList.size()>0){
			if("1".equals(operateType)){
				for(PrpLPadPayPerson po:PrpLPadPayPersonList){
					count++;
					po.setSettleNo(settleNo);
				}
			}else if("0".equals(operateType)){
				for(PrpLPadPayPerson po:PrpLPadPayPersonList){
					count++;
					po.setSettleNo(null);
				}
			}
		}
		return count;
	}

	@Override
	public PrpLPadPayMainVo queryPadPayByCompeNo(String registNo, String compeNo) {
		PrpLPadPayMainVo payMainVo = null;
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo",registNo);
 		queryRule.addEqual("compensateNo",compeNo);
		List<PrpLPadPayMain> padPayMain = databaseDao.findAll(PrpLPadPayMain.class,queryRule);
		if(padPayMain!=null&&padPayMain.size()>0){
			payMainVo = Beans.copyDepth().from(padPayMain.get(0)).to(PrpLPadPayMainVo.class);
		}
		return payMainVo;
	}

	@Override
	public void updatePadPay(PrpLPadPayPersonVo padPayPersonVo) {
		PrpLPadPayPerson personPo = databaseDao.findByPK(PrpLPadPayPerson.class,padPayPersonVo.getId());
		if ( personPo != null ) {
			Beans.copy().from(padPayPersonVo).excludeNull().to(personPo);
			databaseDao.update(PrpLPadPayPerson.class,personPo);
		}
	}
}
