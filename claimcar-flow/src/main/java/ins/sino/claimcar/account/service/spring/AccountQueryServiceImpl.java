package ins.sino.claimcar.account.service.spring;

import ins.framework.common.ResultPage;
import ins.framework.dao.database.DatabaseDao;
import ins.framework.dao.database.support.Page;
import ins.framework.dao.database.support.QueryRule;
import ins.framework.utils.Beans;
import ins.platform.common.service.facade.BaseDaoService;
import ins.platform.utils.DateUtils;
import ins.platform.utils.SqlJoinUtils;
import ins.platform.vo.SysCodeDictVo;
import ins.sino.claimcar.account.po.PrpDAccRollBackAccount;
import ins.sino.claimcar.account.po.PrpLPayBank;
import ins.sino.claimcar.claim.po.*;
import ins.sino.claimcar.claim.vo.PrpLPaymentVo;
import ins.sino.claimcar.flow.po.PrpLWfTaskQuery;
import ins.sino.claimcar.flow.service.WfTaskQueryService;
import ins.sino.claimcar.flow.vo.PrpLWfTaskQueryVo;
import ins.sino.claimcar.manager.po.PrpLPayCustom;
import ins.sino.claimcar.other.service.AccountQueryService;
import ins.sino.claimcar.other.vo.PrpDAccRollBackAccountVo;
import ins.sino.claimcar.other.vo.PrpLPayBankVo;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Path;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.container.Main;

/**
 * <pre>账号审核修改查询实现类</pre>
 * @author ★Luwei
 */
@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"})
@Path("accountQueryService")
public class AccountQueryServiceImpl implements AccountQueryService {

	@Autowired
	DatabaseDao databaseDao;
	@Autowired
	BaseDaoService baseDaoService;
	@Autowired
	WfTaskQueryService wfTaskQueryService;
	
	@Override
	public ResultPage<PrpLPayBankVo> search(PrpLWfTaskQueryVo prplWfTaskQueryVo,int start, int length) throws Exception {
		SqlJoinUtils sqlUtil=new SqlJoinUtils();
	    String comCode = prplWfTaskQueryVo.getComCode();

		sqlUtil.append("SELECT * FROM (");
		//预付计算书号费用查询
		sqlUtil.append("SELECT ACC.CERTINO,payCustom.bankName,acc.errorMessage,prePay.serialNo,payCustom.payeeName,");
		sqlUtil.append("compensate.remark,payCustom.accountNo,acc.rollBackTime rollBackTime,payCustom.id,compensate.registNo,");
		sqlUtil.append(" prePay.chargeCode,acc.payType");
		sqlUtil.append(" from PrpLPayCustom payCustom,PrpLPrePay prePay,PrpLCompensate compensate"
				+ ",PrpDAccRollBackAccount acc,PrpLWfTaskQuery query where 1=1 ");
		sqlUtil.append(" and payCustom.id=prePay.payeeId and  compensate.compensateNo=prePay.compensateNo "
				+ "and ((acc.payType =? and  prePay.feeType =? ) or (acc.payType <>? and  prePay.feeType =? and prePay.chargeCode=acc.chargeCode  )) "
				+ " and (prePay.compensateNo=acc.certiNo OR prePay.settleNo = acc.certiNo) and acc.status='2' and query.registNo=compensate.registNo and payCustom.Id=acc.accountId ");
		
		sqlUtil.addParamValue("P50");
		sqlUtil.addParamValue("P");
		sqlUtil.addParamValue("P50");
		sqlUtil.addParamValue("F");
		if(StringUtils.isNotBlank(prplWfTaskQueryVo.getCompensateNo())){
			sqlUtil.append(" and  acc.certiNo like ? ");
			sqlUtil.addParamValue("%"+prplWfTaskQueryVo.getCompensateNo()  );
		}
		if(StringUtils.isNotBlank(prplWfTaskQueryVo.getClaimNo())){
			sqlUtil.append(" and  compensate.claimNo like ? ");
			sqlUtil.addParamValue("%"+prplWfTaskQueryVo.getClaimNo()  );
		}
		if(StringUtils.isNotBlank(prplWfTaskQueryVo.getInsuredName())){
			sqlUtil.append(" and query.insuredName like ? ");
			sqlUtil.addParamValue("%"+prplWfTaskQueryVo.getInsuredName()+"%");
		}
		if(prplWfTaskQueryVo.getTaskInTimeStart() != null && prplWfTaskQueryVo.getTaskInTimeEnd() != null){
			sqlUtil.append(" and acc.rollBackTime >= ? and acc.rollBackTime <= ? ");
			sqlUtil.addParamValue(prplWfTaskQueryVo.getTaskInTimeStart());
			sqlUtil.addParamValue(DateUtils.toDateEnd(prplWfTaskQueryVo.getTaskInTimeEnd()));
		}
		//区分机构多机构
		if(!"0000000000".equals(prplWfTaskQueryVo.getUserCode())){
			sqlUtil.append(" and (instr(?,substr(compensate.comCode,0,2))>0 or instr(?,substr(compensate.comCode,0,4))>0 )");
			sqlUtil.addParamValue(comCode);
			sqlUtil.addParamValue(comCode);
		}
		//赔款查询
		sqlUtil.append(" UNION ALL ");
		sqlUtil.append("SELECT ACC.CERTINO,payCustom.bankName,acc.errorMessage,payment.serialNo,payCustom.payeeName,");
		sqlUtil.append("compensate.remark,payCustom.accountNo,acc.rollBackTime rollBackTime,payCustom.id,compensate.registNo,");
		sqlUtil.append("null,acc.payType");
		sqlUtil.append(" from PrpLPayCustom payCustom,PrpLCompensate compensate,PrpLPayment payment"
				+ ",PrpDAccRollBackAccount acc,PrpLWfTaskQuery query where 1=1 ");
		sqlUtil.append(" and payCustom.id=payment.payeeId and (payment.compensateNo=acc.certiNo OR payment.settleNo = acc.certiNo)and "
				+ " payment.serialNo = acc.serialNo and compensate.compensateNo=payment.compensateNo and acc.status='2' and query.registNo=compensate.registNo ");
		
		if(StringUtils.isNotBlank(prplWfTaskQueryVo.getCompensateNo())){
			sqlUtil.append(" and  acc.certiNo like ? ");
			sqlUtil.addParamValue("%"+prplWfTaskQueryVo.getCompensateNo()  );
		}
		if(StringUtils.isNotBlank(prplWfTaskQueryVo.getClaimNo())){
			sqlUtil.append(" and  compensate.claimNo like ? ");
			sqlUtil.addParamValue("%"+prplWfTaskQueryVo.getClaimNo()  );
		}
		if(StringUtils.isNotBlank(prplWfTaskQueryVo.getInsuredName())){
			sqlUtil.append(" and query.insuredName like ? ");
			sqlUtil.addParamValue("%"+prplWfTaskQueryVo.getInsuredName()+"%");
		}
		if(prplWfTaskQueryVo.getTaskInTimeStart() != null && prplWfTaskQueryVo.getTaskInTimeEnd() != null){
			sqlUtil.append(" and acc.rollBackTime >= ? and acc.rollBackTime <= ? ");
			sqlUtil.addParamValue(prplWfTaskQueryVo.getTaskInTimeStart());
			sqlUtil.addParamValue(DateUtils.toDateEnd(prplWfTaskQueryVo.getTaskInTimeEnd()));
		}
		//区分机构多机构
		if(!"0000000000".equals(prplWfTaskQueryVo.getUserCode())){
			sqlUtil.append(" and (instr(?,substr(compensate.comCode,0,2))>0 or instr(?,substr(compensate.comCode,0,4))>0 )");
			sqlUtil.addParamValue(comCode);
			sqlUtil.addParamValue(comCode);
		}
		//垫付计算书号查询
		sqlUtil.append(" UNION ALL ");
		sqlUtil.append("SELECT ACC.CERTINO,payCustom.bankName,acc.errorMessage,ACC.OLDACCOUNTID,payCustom.payeeName,");
		sqlUtil.append("padPay.remark,payCustom.accountNo,acc.rollBackTime rollBackTime,payCustom.id,padPay.registNo,");
		sqlUtil.append(" '',acc.payType");
		sqlUtil.append(" from PrpLPayCustom payCustom,PrpLPadPayMain padPay,PrpLPadPayPerson padPer"
				+ ",PrpDAccRollBackAccount acc,PrpLWfTaskQuery query where 1=1 ");
		sqlUtil.append(" and payCustom.id=padPer.payeeId and  padPay.id=padPer.PADPAYID "
				+ " and (padPay.compensateNo=acc.certiNo OR padPer.settleNo = acc.certiNo) and acc.status='2' and query.registNo=padPay.registNo and payCustom.accountId=acc.accountId ");
		
		if(StringUtils.isNotBlank(prplWfTaskQueryVo.getCompensateNo())){
			sqlUtil.append(" and  acc.certiNo like ? ");
			sqlUtil.addParamValue("%"+prplWfTaskQueryVo.getCompensateNo()  );
		}
		if(StringUtils.isNotBlank(prplWfTaskQueryVo.getClaimNo())){
			sqlUtil.append(" and  padPay.claimNo like ? ");
			sqlUtil.addParamValue("%"+prplWfTaskQueryVo.getClaimNo()  );
		}
		if(StringUtils.isNotBlank(prplWfTaskQueryVo.getInsuredName())){
			sqlUtil.append(" and query.insuredName like ? ");
			sqlUtil.addParamValue("%"+prplWfTaskQueryVo.getInsuredName()+"%");
		}
		if(prplWfTaskQueryVo.getTaskInTimeStart() != null && prplWfTaskQueryVo.getTaskInTimeEnd() != null){
			sqlUtil.append(" and acc.rollBackTime >= ? and acc.rollBackTime <= ? ");
			sqlUtil.addParamValue(prplWfTaskQueryVo.getTaskInTimeStart());
			sqlUtil.addParamValue(DateUtils.toDateEnd(prplWfTaskQueryVo.getTaskInTimeEnd()));
		}
		//区分机构多机构
		if(!"0000000000".equals(prplWfTaskQueryVo.getUserCode())){
			sqlUtil.append(" and (instr(?,substr(padPay.comCode,0,2))>0 or instr(?,substr(padPay.comCode,0,4))>0 )");
			sqlUtil.addParamValue(comCode);
			sqlUtil.addParamValue(comCode);
		}	
		//理算费用
		sqlUtil.append(" UNION ALL ");
		sqlUtil.append("SELECT ACC.CERTINO,payCustom.bankName,acc.errorMessage,ACC.serialNo,payCustom.payeeName,");
		sqlUtil.append("compensate.remark,payCustom.accountNo,acc.rollBackTime rollBackTime,payCustom.id,compensate.registNo,");
		sqlUtil.append(" charge.chargeCode,acc.payType");
		sqlUtil.append(" from PrpLPayCustom payCustom,PrpLCompensate compensate,PrpLCharge charge"
				+ ",PrpDAccRollBackAccount acc,PrpLWfTaskQuery query where 1=1 ");
		sqlUtil.append(" and payCustom.id=charge.payeeId and (charge.compensateNo=acc.certiNo OR charge.settleNo=acc.certiNo) and charge.chargeCode=acc.chargeCode and"
				+ "  compensate.compensateNo=charge.compensateNo and acc.status='2' and query.registNo=compensate.registNo and payCustom.id=acc.accountId ");
		
		if(StringUtils.isNotBlank(prplWfTaskQueryVo.getCompensateNo())){
			sqlUtil.append(" and  acc.certiNo like ? ");
			sqlUtil.addParamValue("%"+prplWfTaskQueryVo.getCompensateNo()  );
		}
		if(StringUtils.isNotBlank(prplWfTaskQueryVo.getClaimNo())){
			sqlUtil.append(" and  compensate.claimNo like ? ");
			sqlUtil.addParamValue("%"+prplWfTaskQueryVo.getClaimNo()  );
		}
		if(StringUtils.isNotBlank(prplWfTaskQueryVo.getInsuredName())){
			sqlUtil.append(" and query.insuredName like ? ");
			sqlUtil.addParamValue("%"+prplWfTaskQueryVo.getInsuredName()+"%");
		}
		if(prplWfTaskQueryVo.getTaskInTimeStart() != null && prplWfTaskQueryVo.getTaskInTimeEnd() != null){
			sqlUtil.append(" and acc.rollBackTime >= ? and acc.rollBackTime <= ? ");
			sqlUtil.addParamValue(prplWfTaskQueryVo.getTaskInTimeStart());
			sqlUtil.addParamValue(DateUtils.toDateEnd(prplWfTaskQueryVo.getTaskInTimeEnd()));
		}
		//区分机构多机构
		if(!"0000000000".equals(prplWfTaskQueryVo.getUserCode())){
			sqlUtil.append(" and (instr(?,substr(compensate.comCode,0,2))>0 or instr(?,substr(compensate.comCode,0,4))>0 )");
			sqlUtil.addParamValue(comCode);
			sqlUtil.addParamValue(comCode);
		}
		
		//排序
		sqlUtil.append(" ) a Order By a.rollBackTime desc");
		
		String sql = sqlUtil.getSql();
		Object[] values = sqlUtil.getParamValues();
		System.out.println(sql);
		System.out.println(sqlUtil.getParamValues());
		Page<Object[]> page = baseDaoService.pagedSQLQuery(sql,start,length,values);
		//对象转换
		List<PrpLPayBankVo> payBankVoList = new ArrayList<PrpLPayBankVo>();
		for(int i=0;i<page.getResult().size();i++){
			PrpLPayBankVo payBankVo = new PrpLPayBankVo();
			Object[] obj = page.getResult().get(i);
			
			payBankVo.setCompensateNo(obj[0]!=null ? (String)obj[0]:null);
			payBankVo.setBankName(obj[1]!=null ? (String)obj[1]:null);
			payBankVo.setErrorMessage(obj[2]!=null ? (String)obj[2]:null);
			payBankVo.setSerialNo(obj[3]!=null ? (String)obj[3]:null);
			payBankVo.setAccountName(obj[4]!=null ? (String)obj[4]:null);
			payBankVo.setRemark(obj[5]!=null ? (String)obj[5]:null);
			payBankVo.setAccountNo(obj[6]!=null ? (String)obj[6]:null);
			payBankVo.setAppTime(obj[7]!=null ? (Date)obj[7]:null);
			payBankVo.setFlag(obj[8]!=null ? obj[8].toString():null);
			payBankVo.setRegistNo(obj[9]!=null ? (String)obj[9]:null);
			payBankVo.setChargeCode(obj[10]!=null ? (String)obj[10]:null);
			payBankVo.setPayType(obj[11]!=null ? (String)obj[11]:null);
			payBankVoList.add(payBankVo);
		}
		
		ResultPage<PrpLPayBankVo> resultPage = new ResultPage<PrpLPayBankVo>(start, length, page.getTotalCount(), payBankVoList);
		return resultPage;
	}
	
	
	@Override
	public ResultPage<PrpLPayBankVo> returnTticketInfoSearch(PrpLWfTaskQueryVo prplWfTaskQueryVo, int start, int length)throws ParseException {
		SqlJoinUtils sqlUtil=new SqlJoinUtils();
		sqlUtil.append(" from PrpLPayCustom payCustom,PrpLCompensate compensate,PrpLPayment payment"
				+ ",PrpLWfTaskQuery query,PrpDAccRollBackAccount acc where 1=1 ");
		sqlUtil.append(" and payCustom.id=payment.payeeId and payment.prpLCompensate.compensateNo=compensate.compensateNo and "
				+ " acc.accountId=payCustom.accountId and compensate.compensateNo=acc.certiNo and (acc.status=1 or acc.status=2) and query.registNo=compensate.registNo ");
		
		//P60是赔款
		sqlUtil.append(" and  acc.payType = ? ");
		sqlUtil.addParamValue("P60");
		if(StringUtils.isNotBlank(prplWfTaskQueryVo.getCompensateNo())){
			sqlUtil.append(" and  compensate.compensateNo like ? ");
			sqlUtil.addParamValue("%"+prplWfTaskQueryVo.getCompensateNo()  );
		}
		if(StringUtils.isNotBlank(prplWfTaskQueryVo.getClaimNo())){
			sqlUtil.append(" and  compensate.claimNo like ? ");
			sqlUtil.addParamValue("%"+prplWfTaskQueryVo.getClaimNo()  );
		}
		if(StringUtils.isNotBlank(prplWfTaskQueryVo.getInsuredName())){
			sqlUtil.append(" and query.insuredName like ? ");
			sqlUtil.addParamValue("%"+prplWfTaskQueryVo.getInsuredName()+"%");
		}
		if(prplWfTaskQueryVo.getTaskInTimeStart() != null && prplWfTaskQueryVo.getTaskInTimeEnd() != null){
			sqlUtil.append(" and acc.rollBackTime >= ? and acc.rollBackTime <= ? ");
			sqlUtil.addParamValue(prplWfTaskQueryVo.getTaskInTimeStart());
			sqlUtil.addParamValue(DateUtils.toDateEnd(prplWfTaskQueryVo.getTaskInTimeEnd()));
		}

	   //区分机构
		String comCode = prplWfTaskQueryVo.getComCode();
	    if(!"0000000000".equals(prplWfTaskQueryVo.getUserCode())){
	    	List<SysCodeDictVo> sysCodeList=wfTaskQueryService.findPermitcompanyByUserCode(prplWfTaskQueryVo.getUserCode());
			if(sysCodeList!=null && sysCodeList.size()>0){
				comCode=sysCodeList.get(0).getCodeCode();
			}
			comCode=subString(comCode);
		    sqlUtil.append(" and compensate.comCode like ? ");
		    sqlUtil.addParamValue(comCode+"%");
		 }
		//处理人是自动案件
		sqlUtil.append(" and compensate.createUser = ? ");
		sqlUtil.addParamValue("AUTO");
		
		//排序
		sqlUtil.append(" Order By acc.rollBackTime desc");
		String sql = sqlUtil.getSql();
		Object[] values = sqlUtil.getParamValues();
		System.out.println(sql);
		System.out.println(sqlUtil.getParamValues());
		Page<Object[]> page = databaseDao.findPageByHql(sql,start/length+1,length,values);
		//对象转换
		List<PrpLPayBankVo> payBankVoList = new ArrayList<PrpLPayBankVo>();
		List<PrpLPayBankVo> payBankByCharge = new ArrayList<PrpLPayBankVo>();
		List<PrpLPayBankVo> payBankBySettleNo = new ArrayList<PrpLPayBankVo>();
		for(int i=0;i<page.getResult().size();i++){
			PrpLPayBankVo payBankVo = new PrpLPayBankVo();
			Object[] obj = page.getResult().get(i);
			PrpLCompensate compensate = (PrpLCompensate)obj[1];
			PrpLWfTaskQuery query = (PrpLWfTaskQuery)obj[3];//取被保险人
			Beans.copy().from(compensate).to(payBankVo);
			payBankVo.setInsuredName(query.getInsuredName());

			PrpLPayCustom payCustom = (PrpLPayCustom)obj[0];//取赔款人信息
			payBankVo.setBankName(payCustom.getBankName());
			payBankVo.setAccountName(payCustom.getPayeeName());
			payBankVo.setAccountNo(payCustom.getAccountNo());
			payBankVo.setFlag(String.valueOf(payCustom.getId()));
			
			PrpDAccRollBackAccount accRollBackAccount = (PrpDAccRollBackAccount)obj[4];
			payBankVo.setAppTime(accRollBackAccount.getRollBackTime());
			payBankVo.setPayType(accRollBackAccount.getPayType());
			payBankVo.setErrorMessage(accRollBackAccount.getErrorMessage());
			payBankVoList.add(payBankVo);
		}
		/*payBankByPrePay = this.searchByPrePayCharge(prplWfTaskQueryVo);
		if(payBankByPrePay.size() > 0){
			for(PrpLPayBankVo payBankByPreVo:payBankByPrePay){
				payBankVoList.add(payBankByPreVo);
			}
		}
		payBankByPadPay = this.searchByPadPay(prplWfTaskQueryVo);
		if(payBankByPadPay.size() > 0){
			for(PrpLPayBankVo payBankByPreVo:payBankByPadPay){
				payBankVoList.add(payBankByPreVo);
			}
		}*/
		payBankByCharge = this.returnTticketInfoSearchByCharge(prplWfTaskQueryVo);
		int times = 0;
		if(payBankByCharge.size() > 0){
			for(PrpLPayBankVo payBankByPreVo:payBankByCharge){
				Boolean bl = true;
				for(PrpLPayBankVo payBankVo:payBankVoList){
					if(payBankVo.getFlag().equals(payBankByPreVo.getFlag()) && 
							payBankVo.getPayType().equals(payBankByPreVo.getPayType())){
						bl = false;
						break;
					}
				}
				if(bl){
					payBankVoList.add(payBankByPreVo);
				}else{
					times = times+1;
				}
			}
		}
		payBankBySettleNo = this.returnTticketInfoSearchBySettleNo(prplWfTaskQueryVo);
		if(payBankBySettleNo.size()>0){
			for(PrpLPayBankVo payBankVo:payBankBySettleNo){
				payBankVoList.add(payBankVo);
			}
		}
		
		ResultPage<PrpLPayBankVo> resultPage = new ResultPage<PrpLPayBankVo>(start, length, page.getTotalCount()+payBankByCharge.size()+payBankBySettleNo.size()-times, payBankVoList);
		return resultPage;
	}
	
	//理算费用
	public List<PrpLPayBankVo> searchByCharge(PrpLWfTaskQueryVo prplWfTaskQueryVo) throws ParseException {
		SqlJoinUtils sqlUtil=new SqlJoinUtils();
		sqlUtil.append(" from PrpLPayCustom payCustom,PrpLCompensate compensate,PrpLCharge charge"
				+ ",PrpDAccRollBackAccount acc,PrpLWfTaskQuery query where 1=1 ");
		sqlUtil.append(" and payCustom.id=charge.payeeId and charge.prpLCompensate.compensateNo=compensate.compensateNo and charge.chargeCode=acc.chargeCode and"
				+ "  compensate.compensateNo=acc.certiNo and (acc.status=1 or acc.status=2) and query.registNo=compensate.registNo and payCustom.accountId=acc.accountId ");
		
		if(StringUtils.isNotBlank(prplWfTaskQueryVo.getCompensateNo())){
			sqlUtil.append(" and  compensate.compensateNo like ? ");
			sqlUtil.addParamValue("%"+prplWfTaskQueryVo.getCompensateNo()  );
		}
		if(StringUtils.isNotBlank(prplWfTaskQueryVo.getClaimNo())){
			sqlUtil.append(" and  compensate.claimNo like ? ");
			sqlUtil.addParamValue("%"+prplWfTaskQueryVo.getClaimNo()  );
		}
		if(StringUtils.isNotBlank(prplWfTaskQueryVo.getInsuredName())){
			sqlUtil.append(" and query.insuredName like ? ");
			sqlUtil.addParamValue("%"+prplWfTaskQueryVo.getInsuredName()+"%");
		}
		if(prplWfTaskQueryVo.getTaskInTimeStart() != null && prplWfTaskQueryVo.getTaskInTimeEnd() != null){
			sqlUtil.append(" and acc.rollBackTime >= ? and acc.rollBackTime <= ? ");
			sqlUtil.addParamValue(prplWfTaskQueryVo.getTaskInTimeStart());
			sqlUtil.addParamValue(DateUtils.toDateEnd(prplWfTaskQueryVo.getTaskInTimeEnd()));
		}
//		if(StringUtils.isNotBlank(prplWfTaskQueryVo.getTaskInUser())){
//			sqlUtil.append(" and compensate.underwriteUser like ? ");
//			sqlUtil.addParamValue("%"+prplWfTaskQueryVo.getTaskInUser()+"%");
//		}
		//区分机构
		if(!"0000000000".equals(prplWfTaskQueryVo.getUserCode())){
			String comCode = prplWfTaskQueryVo.getComCode();
			if(comCode.startsWith("00")){
				comCode = comCode.substring(0, 4);
			}else{
				comCode = comCode.substring(0, 2);
			}
			sqlUtil.append(" and compensate.comCode like ? ");
			sqlUtil.addParamValue(comCode+"%");
		}
		//查询自己处理的案件
		/*if(!"0000000000".equals(prplWfTaskQueryVo.getUserCode())){
			sqlUtil.append(" and compensate.createUser = ? ");
			sqlUtil.addParamValue(prplWfTaskQueryVo.getUserCode());
		}*/
		//排序
		sqlUtil.append(" Order By acc.rollBackTime desc");
		String sql = sqlUtil.getSql();
		Object[] values = sqlUtil.getParamValues();
		Page<Object[]> page = databaseDao.findPageByHql(sql,0/10+1,20,values);
		//对象转换
		List<PrpLPayBankVo> payBankVoList = new ArrayList<PrpLPayBankVo>();
		for(int i=0;i<page.getResult().size();i++){
			PrpLPayBankVo payBankVo = new PrpLPayBankVo();
			Object[] obj = page.getResult().get(i);
			PrpLCompensate compensate = (PrpLCompensate)obj[1];
			PrpLWfTaskQuery query = (PrpLWfTaskQuery)obj[4];//取被保险人
			PrpLCharge charge = (PrpLCharge)obj[2];
			Beans.copy().from(compensate).to(payBankVo);
			payBankVo.setInsuredName(query.getInsuredName());
			payBankVo.setPayeeId(charge.getPayeeId());
			payBankVo.setChargeCode(charge.getChargeCode());

			PrpLPayCustom payCustom = (PrpLPayCustom)obj[0];//取赔款人信息
			payBankVo.setBankName(payCustom.getBankName());
			payBankVo.setAccountName(payCustom.getPayeeName());
			payBankVo.setAccountNo(payCustom.getAccountNo());
			payBankVo.setFlag(String.valueOf(payCustom.getId()));
			
			PrpDAccRollBackAccount accRollBackAccount = (PrpDAccRollBackAccount)obj[3];
			Date appTime = accRollBackAccount.getRollBackTime();
			payBankVo.setAppTime(appTime);
			payBankVo.setPayType(accRollBackAccount.getPayType());
			payBankVo.setErrorMessage(accRollBackAccount.getErrorMessage());
			payBankVoList.add(payBankVo);
		}
		return payBankVoList;
	}
	
	//理算费用退票修改
	public List<PrpLPayBankVo> returnTticketInfoSearchByCharge(PrpLWfTaskQueryVo prplWfTaskQueryVo) throws ParseException {
			SqlJoinUtils sqlUtil=new SqlJoinUtils();
			sqlUtil.append(" from PrpLPayCustom payCustom,PrpLCompensate compensate,PrpLCharge charge"
					+ ",PrpDAccRollBackAccount acc,PrpLWfTaskQuery query where 1=1 ");
			sqlUtil.append(" and payCustom.id=charge.payeeId and charge.prpLCompensate.compensateNo=compensate.compensateNo and charge.chargeCode=acc.chargeCode and"
					+ "  compensate.compensateNo=acc.certiNo and (acc.status=1 or acc.status=2) and query.registNo=compensate.registNo and payCustom.accountId=acc.accountId ");
			
			if(StringUtils.isNotBlank(prplWfTaskQueryVo.getCompensateNo())){
				sqlUtil.append(" and  compensate.compensateNo like ? ");
				sqlUtil.addParamValue("%"+prplWfTaskQueryVo.getCompensateNo()  );
			}
			if(StringUtils.isNotBlank(prplWfTaskQueryVo.getClaimNo())){
				sqlUtil.append(" and  compensate.claimNo like ? ");
				sqlUtil.addParamValue("%"+prplWfTaskQueryVo.getClaimNo()  );
			}
			if(StringUtils.isNotBlank(prplWfTaskQueryVo.getInsuredName())){
				sqlUtil.append(" and query.insuredName like ? ");
				sqlUtil.addParamValue("%"+prplWfTaskQueryVo.getInsuredName()+"%");
			}
			if(prplWfTaskQueryVo.getTaskInTimeStart() != null && prplWfTaskQueryVo.getTaskInTimeEnd() != null){
				sqlUtil.append(" and acc.rollBackTime >= ? and acc.rollBackTime <= ? ");
				sqlUtil.addParamValue(prplWfTaskQueryVo.getTaskInTimeStart());
				sqlUtil.addParamValue(DateUtils.toDateEnd(prplWfTaskQueryVo.getTaskInTimeEnd()));
			}
	
			//自动理算退票修改标识
			sqlUtil.append(" and compensate.handler1Code = ? ");
			sqlUtil.addParamValue("AUTO");
			//区分机构
			String comCode = prplWfTaskQueryVo.getComCode();
			if(!"0000000000".equals(prplWfTaskQueryVo.getUserCode())){
				List<SysCodeDictVo> sysCodeList=wfTaskQueryService.findPermitcompanyByUserCode(prplWfTaskQueryVo.getUserCode());
				if(sysCodeList!=null && sysCodeList.size()>0){
					comCode=sysCodeList.get(0).getCodeCode();
				}
				comCode=subString(comCode);
				sqlUtil.append(" and compensate.comCode like ? ");
				sqlUtil.addParamValue(comCode+"%");
			}
			//排序
			sqlUtil.append(" Order By acc.rollBackTime desc");
			String sql = sqlUtil.getSql();
			Object[] values = sqlUtil.getParamValues();
			Page<Object[]> page = databaseDao.findPageByHql(sql,0/10+1,20,values);
			//对象转换
			List<PrpLPayBankVo> payBankVoList = new ArrayList<PrpLPayBankVo>();
			for(int i=0;i<page.getResult().size();i++){
				PrpLPayBankVo payBankVo = new PrpLPayBankVo();
				Object[] obj = page.getResult().get(i);
				PrpLCompensate compensate = (PrpLCompensate)obj[1];
				PrpLWfTaskQuery query = (PrpLWfTaskQuery)obj[4];//取被保险人
				PrpLCharge charge = (PrpLCharge)obj[2];
				Beans.copy().from(compensate).to(payBankVo);
				payBankVo.setInsuredName(query.getInsuredName());
				payBankVo.setPayeeId(charge.getPayeeId());
				payBankVo.setChargeCode(charge.getChargeCode());
		
				PrpLPayCustom payCustom = (PrpLPayCustom)obj[0];//取赔款人信息
				payBankVo.setBankName(payCustom.getBankName());
				payBankVo.setAccountName(payCustom.getPayeeName());
				payBankVo.setAccountNo(payCustom.getAccountNo());
				payBankVo.setFlag(String.valueOf(payCustom.getId()));
				
				PrpDAccRollBackAccount accRollBackAccount = (PrpDAccRollBackAccount)obj[3];
				Date appTime = accRollBackAccount.getRollBackTime();
				payBankVo.setAppTime(appTime);
				payBankVo.setPayType(accRollBackAccount.getPayType());
				payBankVo.setErrorMessage(accRollBackAccount.getErrorMessage());
				payBankVoList.add(payBankVo);
			}
			return payBankVoList;
		}
	
	//预付计算书号费用查询
	public List<PrpLPayBankVo> searchByPrePayCharge(PrpLWfTaskQueryVo prplWfTaskQueryVo) throws ParseException {
		SqlJoinUtils sqlUtil=new SqlJoinUtils();
		sqlUtil.append(" from PrpLPayCustom payCustom,PrpLPrePay prePay,PrpLCompensate compensate"
				+ ",PrpDAccRollBackAccount acc,PrpLWfTaskQuery query,PrpLWfTaskOut taskOut where 1=1 ");
		sqlUtil.append(" and payCustom.id=prePay.payeeId and  compensate.compensateNo=prePay.compensateNo "
				+ "and ((acc.payType =? and  prePay.feeType =? ) or (acc.payType <>? and  prePay.feeType =? and prePay.chargeCode=acc.chargeCode  )) "
				+ " and prePay.compensateNo=acc.certiNo and (acc.status=1 or acc.status=2) and query.registNo=compensate.registNo and payCustom.accountId=acc.accountId "
				+" and taskOut.compensateNo=prePay.compensateNo ");
		
		sqlUtil.addParamValue("P50");
		sqlUtil.addParamValue("P");
		sqlUtil.addParamValue("P50");
		sqlUtil.addParamValue("F");
		sqlUtil.append(" and  taskOut.nodeCode = ? ");
		sqlUtil.addParamValue("PrePay");
		if(StringUtils.isNotBlank(prplWfTaskQueryVo.getCompensateNo())){
			sqlUtil.append(" and  compensate.compensateNo like ? ");
			sqlUtil.addParamValue("%"+prplWfTaskQueryVo.getCompensateNo()  );
		}
		if(StringUtils.isNotBlank(prplWfTaskQueryVo.getClaimNo())){
			sqlUtil.append(" and  compensate.claimNo like ? ");
			sqlUtil.addParamValue("%"+prplWfTaskQueryVo.getClaimNo()  );
		}
		if(StringUtils.isNotBlank(prplWfTaskQueryVo.getInsuredName())){
			sqlUtil.append(" and query.insuredName like ? ");
			sqlUtil.addParamValue("%"+prplWfTaskQueryVo.getInsuredName()+"%");
		}
		if(prplWfTaskQueryVo.getTaskInTimeStart() != null && prplWfTaskQueryVo.getTaskInTimeEnd() != null){
			sqlUtil.append(" and acc.rollBackTime >= ? and acc.rollBackTime <= ? ");
			sqlUtil.addParamValue(prplWfTaskQueryVo.getTaskInTimeStart());
			sqlUtil.addParamValue(DateUtils.toDateEnd(prplWfTaskQueryVo.getTaskInTimeEnd()));
		}
//		if(StringUtils.isNotBlank(prplWfTaskQueryVo.getTaskInUser())){
//			sqlUtil.append(" and compensate.underwriteUser like ? ");
//			sqlUtil.addParamValue("%"+prplWfTaskQueryVo.getTaskInUser()+"%");
//		}
		//区分机构
		if(!"0000000000".equals(prplWfTaskQueryVo.getUserCode())){
			String comCode = prplWfTaskQueryVo.getComCode();
			if(comCode.startsWith("00")){
				comCode = comCode.substring(0, 4);
			}else{
				comCode = comCode.substring(0, 2);
			}
			sqlUtil.append(" and taskOut.assignCom like ? ");
			sqlUtil.addParamValue(comCode+"%");
		}
		//查询自己处理的案件
		/*if(!"0000000000".equals(prplWfTaskQueryVo.getUserCode())){
			sqlUtil.append(" and taskOut.handlerUser = ? ");
			sqlUtil.addParamValue(prplWfTaskQueryVo.getUserCode());
		}*/
		//排序
		sqlUtil.append(" Order By acc.rollBackTime desc");
		String sql = sqlUtil.getSql();
		Object[] values = sqlUtil.getParamValues();
		Page<Object[]> page = databaseDao.findPageByHql(sql,0/10+1,20,values);
		Map<String,String> preMap = new HashMap<String,String>();
		//对象转换
		List<PrpLPayBankVo> payBankVoList = new ArrayList<PrpLPayBankVo>();
		for(int i=0;i<page.getResult().size();i++){
			PrpLPayBankVo payBankVo = new PrpLPayBankVo();
			Object[] obj = page.getResult().get(i);
			PrpLCompensate compensate = (PrpLCompensate)obj[2];
			PrpLWfTaskQuery query = (PrpLWfTaskQuery)obj[4];//取被保险人
			PrpLPrePay prePay = (PrpLPrePay)obj[1];
			if(prePay.getFeeType().equals("P")){
				String key = prePay.getCompensateNo().trim()+"-"+ prePay.getPayeeId();
				if(preMap.containsKey(key)){
					continue;
				}else{
					preMap.put(key, "1");
				}
			}
			Beans.copy().from(compensate).to(payBankVo);
			payBankVo.setInsuredName(query.getInsuredName());
			payBankVo.setPayeeId(prePay.getPayeeId());
			payBankVo.setChargeCode(prePay.getChargeCode());
			PrpLPayCustom payCustom = (PrpLPayCustom)obj[0];//取赔款人信息
			payBankVo.setBankName(payCustom.getBankName());
			payBankVo.setAccountName(payCustom.getPayeeName());
			payBankVo.setAccountNo(payCustom.getAccountNo());
			payBankVo.setFlag(String.valueOf(payCustom.getId()));
			
			PrpDAccRollBackAccount accRollBackAccount = (PrpDAccRollBackAccount)obj[3];
			Date appTime = accRollBackAccount.getRollBackTime();
			payBankVo.setAppTime(appTime);
			payBankVo.setPayType(accRollBackAccount.getPayType());
			payBankVo.setErrorMessage(accRollBackAccount.getErrorMessage());
			payBankVoList.add(payBankVo);
		}
		return payBankVoList;
	}
	
	
	//垫付计算书号查询
	public List<PrpLPayBankVo> searchByPadPay(PrpLWfTaskQueryVo prplWfTaskQueryVo) throws ParseException {
		SqlJoinUtils sqlUtil=new SqlJoinUtils();
		sqlUtil.append(" from PrpLPayCustom payCustom,PrpLPadPayMain padPay,PrpLPadPayPerson padPer"
				+ ",PrpDAccRollBackAccount acc,PrpLWfTaskQuery query,PrpLWfTaskOut taskOut where 1=1 ");
		sqlUtil.append(" and payCustom.id=padPer.payeeId and  padPay.id=padPer.prpLPadPayMain.id "
				+ " and padPay.compensateNo=acc.certiNo and (acc.status=1 or acc.status=2) and query.registNo=padPay.registNo and payCustom.accountId=acc.accountId "
				+" and taskOut.compensateNo=padPay.compensateNo ");
		
		sqlUtil.append(" and  taskOut.nodeCode = ? ");
		sqlUtil.addParamValue("PadPay");
		if(StringUtils.isNotBlank(prplWfTaskQueryVo.getCompensateNo())){
			sqlUtil.append(" and  padPay.compensateNo like ? ");
			sqlUtil.addParamValue("%"+prplWfTaskQueryVo.getCompensateNo()  );
		}
		if(StringUtils.isNotBlank(prplWfTaskQueryVo.getClaimNo())){
			sqlUtil.append(" and  padPay.claimNo like ? ");
			sqlUtil.addParamValue("%"+prplWfTaskQueryVo.getClaimNo()  );
		}
		if(StringUtils.isNotBlank(prplWfTaskQueryVo.getInsuredName())){
			sqlUtil.append(" and query.insuredName like ? ");
			sqlUtil.addParamValue("%"+prplWfTaskQueryVo.getInsuredName()+"%");
		}
		if(prplWfTaskQueryVo.getTaskInTimeStart() != null && prplWfTaskQueryVo.getTaskInTimeEnd() != null){
			sqlUtil.append(" and acc.rollBackTime >= ? and acc.rollBackTime <= ? ");
			sqlUtil.addParamValue(prplWfTaskQueryVo.getTaskInTimeStart());
			sqlUtil.addParamValue(DateUtils.toDateEnd(prplWfTaskQueryVo.getTaskInTimeEnd()));
		}
//		if(StringUtils.isNotBlank(prplWfTaskQueryVo.getTaskInUser())){
//			sqlUtil.append(" and padPay.underwriteUser like ? ");
//			sqlUtil.addParamValue("%"+prplWfTaskQueryVo.getTaskInUser()+"%");
//		}
		//区分机构
		if(!"0000000000".equals(prplWfTaskQueryVo.getUserCode())){
			String comCode = prplWfTaskQueryVo.getComCode();
			if(comCode.startsWith("00")){
				comCode = comCode.substring(0, 4);
			}else{
				comCode = comCode.substring(0, 2);
			}
			sqlUtil.append(" and taskOut.assignCom like ? ");
			sqlUtil.addParamValue(comCode+"%");
		}
		/*//查询自己处理的案件
		if(!"0000000000".equals(prplWfTaskQueryVo.getUserCode())){
			sqlUtil.append(" and taskOut.handlerUser = ? ");
			sqlUtil.addParamValue(prplWfTaskQueryVo.getUserCode());
		}*/
		//排序
		sqlUtil.append(" Order By acc.rollBackTime desc");
		String sql = sqlUtil.getSql();
		Object[] values = sqlUtil.getParamValues();
		Page<Object[]> page = databaseDao.findPageByHql(sql,0/10+1,10,values);
		//对象转换
		List<PrpLPayBankVo> payBankVoList = new ArrayList<PrpLPayBankVo>();
		for(int i=0;i<page.getResult().size();i++){
			PrpLPayBankVo payBankVo = new PrpLPayBankVo();
			Object[] obj = page.getResult().get(i);
			PrpLWfTaskQuery query = (PrpLWfTaskQuery)obj[4];//取被保险人
			PrpLPadPayMain padPay = (PrpLPadPayMain)obj[1];
			PrpLPadPayPerson padPayPerson = (PrpLPadPayPerson)obj[2];
			Beans.copy().from(padPay).to(payBankVo);
			payBankVo.setInsuredName(query.getInsuredName());
			payBankVo.setPayeeId(padPayPerson.getPayeeId());
			PrpLPayCustom payCustom = (PrpLPayCustom)obj[0];//取赔款人信息
			payBankVo.setBankName(payCustom.getBankName());
			payBankVo.setAccountName(payCustom.getPayeeName());
			payBankVo.setAccountNo(payCustom.getAccountNo());
			payBankVo.setFlag(String.valueOf(payCustom.getId()));
			
			PrpDAccRollBackAccount accRollBackAccount = (PrpDAccRollBackAccount)obj[3];
			Date appTime = accRollBackAccount.getRollBackTime();
			payBankVo.setAppTime(appTime);
			payBankVo.setPayType(accRollBackAccount.getPayType());
			payBankVo.setErrorMessage(accRollBackAccount.getErrorMessage());
			payBankVoList.add(payBankVo);
		}
		return payBankVoList;
	}
	
	public List<PrpLPayBankVo> searchBySettleNo(PrpLWfTaskQueryVo prplWfTaskQueryVo){
		SqlJoinUtils sqlUtil=new SqlJoinUtils();
		String comCode = prplWfTaskQueryVo.getComCode();
		if(comCode.startsWith("00")){
			comCode = comCode.substring(0, 4);
		}else{
			comCode = comCode.substring(0, 2);
		}
		sqlUtil.append(" from PrpDAccRollBackAccount acc where 1=1 ");
		sqlUtil.append(" and (acc.status=1 or acc.status=2) ");
		if(StringUtils.isNotBlank(prplWfTaskQueryVo.getCompensateNo())){
			sqlUtil.append(" and acc.certiNo like ? ");
			sqlUtil.addParamValue("%"+prplWfTaskQueryVo.getCompensateNo()  );
		}
		sqlUtil.append(" and (exists(select 1 from  PrpLPayment payment,PrpLCompensate compensate_a where ");
		sqlUtil.append(" payment.prpLCompensate.compensateNo=compensate_a.compensateNo and payment.settleNo=acc.certiNo ");
		//查询自己处理的案件
		if(!"0000000000".equals(prplWfTaskQueryVo.getUserCode())){
			sqlUtil.append(" and compensate_a.comCode like ? ");
			sqlUtil.addParamValue(comCode+"%");
		}
		sqlUtil.append(" ) or exists(select 1 from PrpLCompensate compensate_b,PrpLCharge charge where ");
		sqlUtil.append(" charge.prpLCompensate.compensateNo=compensate_b.compensateNo and charge.settleNo=acc.certiNo ");
		if(!"0000000000".equals(prplWfTaskQueryVo.getUserCode())){
			sqlUtil.append(" and compensate_b.createUser = ? ");
			sqlUtil.addParamValue(prplWfTaskQueryVo.getUserCode());
		}
		sqlUtil.append(" ) or exists(select 1 from PrpLPrePay prePay,PrpLWfTaskOut taskOut_c where ");
		sqlUtil.append(" prePay.settleNo=acc.certiNo and prePay.compensateNo=taskOut_c.compensateNo and taskOut_c.nodeCode = ? ");
		sqlUtil.addParamValue("PrePay");
		if(!"0000000000".equals(prplWfTaskQueryVo.getUserCode())){
			sqlUtil.append(" and taskOut_c.handlerUser = ? ");
			sqlUtil.addParamValue(prplWfTaskQueryVo.getUserCode());
		}
		sqlUtil.append(" ) or exists(select 1 from PrpLPadPayPerson person,PrpLWfTaskOut taskOut_d where ");
		sqlUtil.append(" person.settleNo=acc.certiNo and person.prpLPadPayMain.compensateNo=taskOut_d.compensateNo  ");
		sqlUtil.append(" and taskOut_d.nodeCode = ? ");
		sqlUtil.addParamValue("PadPay");
		if(!"0000000000".equals(prplWfTaskQueryVo.getUserCode())){
			sqlUtil.append(" and taskOut_d.assignCom like ? ");
			sqlUtil.addParamValue(comCode+"%");
		}
		sqlUtil.append(" )) Order By acc.rollBackTime desc");
		String sql = sqlUtil.getSql();
		Object[] values = sqlUtil.getParamValues();
		List<PrpDAccRollBackAccount> accList = databaseDao.findAllByHql(PrpDAccRollBackAccount.class, sql, values);
		List<PrpLPayBankVo> payBankVoList = new ArrayList<PrpLPayBankVo>();
		if(accList!=null && accList.size()>0){
			for(PrpDAccRollBackAccount accVo:accList){
				PrpLPayBankVo payBankVo = new PrpLPayBankVo();
				Beans.copy().from(accVo).to(payBankVo);
				payBankVo.setCompensateNo(accVo.getCertiNo());
				payBankVo.setAccountNo(accVo.getAccountCode());
				payBankVo.setAppTime(accVo.getRollBackTime());
				payBankVo.setBankName(accVo.getBankCode());
				payBankVo.setAccountId(accVo.getOldAccountId());
				payBankVoList.add(payBankVo);
			}
		}
		
		return payBankVoList;
	}
	
	public List<PrpLPayBankVo> returnTticketInfoSearchBySettleNo(PrpLWfTaskQueryVo prplWfTaskQueryVo){
		SqlJoinUtils sqlUtil=new SqlJoinUtils();
		sqlUtil.append(" from PrpDAccRollBackAccount acc where 1=1 ");
		sqlUtil.append(" and acc.status in(?,?) ");
		sqlUtil.addParamValue("1");
		sqlUtil.addParamValue("2");
		if(StringUtils.isNotBlank(prplWfTaskQueryVo.getCompensateNo())){
			sqlUtil.append(" and acc.certiNo like ? ");
			sqlUtil.addParamValue("%"+prplWfTaskQueryVo.getCompensateNo());
		}
		sqlUtil.append(" and (exists(select 1 from  PrpLPayment payment,PrpLCompensate compensate_a where ");
		sqlUtil.append(" payment.prpLCompensate.compensateNo=compensate_a.compensateNo and payment.settleNo=acc.certiNo ");
		//查询自己自动理算退票案件
		sqlUtil.append(" and compensate_a.handler1Code = ? ");
		sqlUtil.addParamValue("AUTO");
		
		 //区分机构
		String comCode = prplWfTaskQueryVo.getComCode();
	    if(!"0000000000".equals(prplWfTaskQueryVo.getUserCode())){
	    	List<SysCodeDictVo> sysCodeList=wfTaskQueryService.findPermitcompanyByUserCode(prplWfTaskQueryVo.getUserCode());
			if(sysCodeList!=null && sysCodeList.size()>0){
				comCode=sysCodeList.get(0).getCodeCode();
			}
			comCode=subString(comCode);
		    sqlUtil.append(" and compensate_a.comCode like ? ");
		    sqlUtil.addParamValue(comCode+"%");
		 }
		sqlUtil.append(" ) or exists(select 1 from PrpLCompensate compensate_b,PrpLCharge charge where ");
		sqlUtil.append(" charge.prpLCompensate.compensateNo=compensate_b.compensateNo and charge.settleNo=acc.certiNo ");
		//查询自己自动理算退票案件
		sqlUtil.append(" and compensate_b.handler1Code = ? ");
		sqlUtil.addParamValue("AUTO");
		
		//区分机构
	    if(!"0000000000".equals(prplWfTaskQueryVo.getUserCode())){
		    sqlUtil.append(" and compensate_b.comCode like ? ");
		    sqlUtil.addParamValue(comCode+"%");
		 }
		sqlUtil.append(" )) Order By acc.rollBackTime desc");
		String sql = sqlUtil.getSql();
		Object[] values = sqlUtil.getParamValues();
		List<PrpDAccRollBackAccount> accList = databaseDao.findAllByHql(PrpDAccRollBackAccount.class, sql, values);
		List<PrpLPayBankVo> payBankVoList = new ArrayList<PrpLPayBankVo>();
		if(accList!=null && accList.size()>0){
			for(PrpDAccRollBackAccount accVo:accList){
				PrpLPayBankVo payBankVo = new PrpLPayBankVo();
				Beans.copy().from(accVo).to(payBankVo);
				payBankVo.setCompensateNo(accVo.getCertiNo());
				payBankVo.setAccountNo(accVo.getAccountCode());
				payBankVo.setAppTime(accVo.getRollBackTime());
				payBankVo.setBankName(accVo.getBankCode());
				payBankVo.setAccountId(accVo.getOldAccountId());
				payBankVoList.add(payBankVo);
			}
		}
		
		return payBankVoList;
	}
	
	/* 
	 * @see ins.sino.claimcar.pay.service.AccountQueryService#searchByhandle(ins.sino.claimcar.flow.vo.PrpLWfTaskQueryVo, int, int)
	 * @param prplWfTaskQueryVo
	 * @param start
	 * @param length
	 * @return
	 */
	@Override
	public List<PrpLPayBankVo> searchByhandle(PrpLWfTaskQueryVo prplWfTaskQueryVo,int start, int length){
		QueryRule qr = QueryRule.getInstance();
		qr.addEqual("compensateNo", prplWfTaskQueryVo.getCompensateNo());
		qr.addEqual("claimNo", prplWfTaskQueryVo.getClaimNo());
		qr.addEqual("insuredName", prplWfTaskQueryVo.getInsuredName());
		qr.addEqual("createUser", prplWfTaskQueryVo.getTaskInUser());
		List<PrpLPayBank> payBankList = databaseDao.findAll(PrpLPayBank.class, qr);
		List<PrpLPayBankVo> payBankVoList = new ArrayList<PrpLPayBankVo>();
		if(payBankList != null){
			payBankVoList = Beans.copyDepth().from(payBankList).toList(PrpLPayBankVo.class);
		}
		return payBankVoList;
	}
	
	
	
	@Override
	public ResultPage<PrpLPayBankVo> searchByHandle(PrpLWfTaskQueryVo taskQueryVo,int start, int length){
		SqlJoinUtils sqlUtil=new SqlJoinUtils();
		sqlUtil.append(" from PrpLPayBank payBank,PrpLClaim claim where 1=1 and payBank.claimNo=claim.claimNo ");
		if(StringUtils.isNotBlank(taskQueryVo.getCompensateNo())){
			sqlUtil.append(" and  payBank.compensateNo like ? ");
			sqlUtil.addParamValue("%"+taskQueryVo.getCompensateNo()  );
		}
		if(StringUtils.isNotBlank(taskQueryVo.getClaimNo())){
			sqlUtil.append(" and  payBank.claimNo like ? ");
			sqlUtil.addParamValue("%"+taskQueryVo.getClaimNo()  );
		}
		if(StringUtils.isNotBlank(taskQueryVo.getInsuredName())){
			sqlUtil.append(" and payBank.insuredName like ? ");
			sqlUtil.addParamValue("%"+taskQueryVo.getInsuredName()+"%");
		}
		if(StringUtils.isNotBlank(taskQueryVo.getTaskInUser())){
			sqlUtil.append(" and payBank.createUser like ? ");
			sqlUtil.addParamValue("%"+taskQueryVo.getTaskInUser()+"%");
		}
		
		//账号修改审核
		if(StringUtils.isNotBlank(taskQueryVo.getIncludeLower()) 
				&& "1".equals(taskQueryVo.getIncludeLower())){
			//待增加字段
			sqlUtil.append(" and payBank.isVerify = ? ");
			sqlUtil.addParamValue("1");
			
			if("0".equals(taskQueryVo.getHandleStatus())){//审核未处理
				sqlUtil.append(" and payBank.verifyStatus in (?,?) ");
				sqlUtil.addParamValue("0");
				sqlUtil.addParamValue("1");//正在审核
			}else{//审核已处理（3-自动审核，2-完成审核）
				sqlUtil.append(" and payBank.verifyStatus in (?,?) ");
				sqlUtil.addParamValue("2");
				sqlUtil.addParamValue("3");
			}
		}

		//区分机构多机构
		String comCode = taskQueryVo.getComCode();
		if(!"0000000000".equals(taskQueryVo.getUserCode())){
			sqlUtil.append(" and  (instr(?,substr(claim.comCode,0,2))>0 or instr(?,substr(claim.comCode,0,4))>0 )");
			sqlUtil.addParamValue(comCode);
			sqlUtil.addParamValue(comCode);
		}
		sqlUtil.append(" Order By payBank.createTime desc ");
		String sql = sqlUtil.getSql();
		Object[] values = sqlUtil.getParamValues();
		Page<Object[]> page = databaseDao.findPageByHql(sql,start/length+1,length,values);
		
		List<PrpLPayBankVo> payBankVoList = new ArrayList<PrpLPayBankVo>();
		for(int i=0;i<page.getResult().size();i++){
			PrpLPayBankVo payBankVo = new PrpLPayBankVo();
			Object[] obj = page.getResult().get(i);
			Beans.copy().from(obj[0]).to(payBankVo);
			PrpLPaymentVo paymentVo = findPaymentVo(payBankVo.getCompensateNo(), payBankVo.getPayeeId());
			if (paymentVo.getSerialNo() != null) {
				payBankVo.setSerialNo(paymentVo.getSerialNo());
			}
			payBankVoList.add(payBankVo);
		}
		ResultPage<PrpLPayBankVo> resultPage = new ResultPage<PrpLPayBankVo>(start, length, page.getTotalCount(), payBankVoList);
		return resultPage;
	}

	/**
	 * 根据计算书号和收款人ID查询理算赔款表
	 * @param compensateNo
	 * @param payeeId
	 * @return
	 */
	private PrpLPaymentVo findPaymentVo(String compensateNo, Long payeeId){
		QueryRule qr = QueryRule.getInstance();
		qr.addEqual("prpLCompensate.compensateNo", compensateNo);
		qr.addEqual("payeeId", payeeId);
		List<PrpLPayment> paymentList = databaseDao.findAll(PrpLPayment.class, qr);
		PrpLPaymentVo paymentVo = new PrpLPaymentVo();
		if(paymentList!=null && paymentList.size()>0){
			Beans.copy().from(paymentList.get(0)).to(paymentVo);
		}
		return paymentVo;
	}
	
	@Override
	public PrpDAccRollBackAccountVo findRollBackAccountById(Long id) {
		PrpDAccRollBackAccount prpDAccRollBackAccount = databaseDao.findByPK(PrpDAccRollBackAccount.class, id);
		
		PrpDAccRollBackAccountVo vo =new PrpDAccRollBackAccountVo();
		if(prpDAccRollBackAccount != null){
			Beans.copy().from(prpDAccRollBackAccount).to(vo);
		}
		
		return vo;
	}

	@Override
	public List<PrpDAccRollBackAccountVo> findPrpDAccRollBackAccountVosByCertiNoAndPayTypeAndAccountId(
			String certiNo, String payType, String accountId) {
		QueryRule qr = QueryRule.getInstance();
		qr.addEqual("certiNo",certiNo);
		qr.addEqual("payType",payType);
		qr.addEqual("accountId",accountId);
		List<PrpDAccRollBackAccount> backAccountPoList = databaseDao.findAll(PrpDAccRollBackAccount.class, qr);
		List<PrpDAccRollBackAccountVo> backAccountVoList = new ArrayList<PrpDAccRollBackAccountVo>();
		if(backAccountPoList != null && backAccountPoList.size()>0){
			backAccountVoList = Beans.copyDepth().from(backAccountPoList).toList(PrpDAccRollBackAccountVo.class);
		}
		return backAccountVoList;
	}

	@Override
	public ResultPage<PrpLPayBankVo> returnTticketInfoSearchByHandle(PrpLWfTaskQueryVo taskQueryVo, int start, int length) {
		SqlJoinUtils sqlUtil=new SqlJoinUtils();
		sqlUtil.append(" from PrpLPayBank payBank,PrpLClaim claim,PrpLCompensate compensate where 1=1 and payBank.claimNo=claim.claimNo and compensate.compensateNo=payBank.compensateNo ");
		if(StringUtils.isNotBlank(taskQueryVo.getCompensateNo())){
			sqlUtil.append(" and  payBank.compensateNo like ? ");
			sqlUtil.addParamValue("%"+taskQueryVo.getCompensateNo()  );
		}
		if(StringUtils.isNotBlank(taskQueryVo.getClaimNo())){
			sqlUtil.append(" and  payBank.claimNo like ? ");
			sqlUtil.addParamValue("%"+taskQueryVo.getClaimNo()  );
		}
		if(StringUtils.isNotBlank(taskQueryVo.getInsuredName())){
			sqlUtil.append(" and payBank.insuredName like ? ");
			sqlUtil.addParamValue("%"+taskQueryVo.getInsuredName()+"%");
		}
		//自动理算退票修改标识
		sqlUtil.append(" and compensate.createUser = ? ");
		sqlUtil.addParamValue("AUTO");
		
		
		//账号修改审核
		if(StringUtils.isNotBlank(taskQueryVo.getIncludeLower()) 
				&& "1".equals(taskQueryVo.getIncludeLower())){
			//待增加字段
			sqlUtil.append(" and payBank.isVerify = ? ");
			sqlUtil.addParamValue("1");
			
			if("0".equals(taskQueryVo.getHandleStatus())){//审核未处理
				sqlUtil.append(" and payBank.verifyStatus in (?,?) ");
				sqlUtil.addParamValue("0");
				sqlUtil.addParamValue("1");//正在审核
			}else{//审核已处理（3-自动审核，2-完成审核）
				sqlUtil.append(" and payBank.verifyStatus in (?,?) ");
				sqlUtil.addParamValue("2");
				sqlUtil.addParamValue("3");
			}
		}
		
		//区分机构
		String comCode = taskQueryVo.getComCode();
		if(!"0000000000".equals(taskQueryVo.getUserCode())){
			List<SysCodeDictVo> sysCodeList=wfTaskQueryService.findPermitcompanyByUserCode(taskQueryVo.getUserCode());
			if(sysCodeList!=null && sysCodeList.size()>0){
				comCode=sysCodeList.get(0).getCodeCode();
			}
			comCode=subString(comCode);
			sqlUtil.append(" and claim.comCode like ? ");
			sqlUtil.addParamValue(comCode+"%");
		}
		
		sqlUtil.append(" Order By payBank.createTime desc ");
		String sql = sqlUtil.getSql();
		Object[] values = sqlUtil.getParamValues();
		Page<Object[]> page = databaseDao.findPageByHql(sql,start/length+1,length,values);
		
		List<PrpLPayBankVo> payBankVoList = new ArrayList<PrpLPayBankVo>();
		for(int i=0;i<page.getResult().size();i++){
			PrpLPayBankVo payBankVo = new PrpLPayBankVo();
			Object[] obj = page.getResult().get(i);
			Beans.copy().from(obj[0]).to(payBankVo);
//			payBankVo = (PrpLPayBankVo) obj;
			payBankVoList.add(payBankVo);
		}
		ResultPage<PrpLPayBankVo> resultPage = new ResultPage<PrpLPayBankVo>(start, length, page.getTotalCount(), payBankVoList);
		return resultPage;
	}

	/**
	 * 截取机构代码
	 * @param comCode
	 * @return
	 */
	private String subString(String comCode){
	    String valueStr = comCode.substring(0, 2);
		if(StringUtils.isBlank(comCode)) return "";
		if(comCode.equals("00000000")){
			return comCode;
		}else if(comCode.endsWith("000000")){
			valueStr = comCode.substring(0,2);
		}else{
			valueStr = comCode.substring(0,4);
		}
		return valueStr;
	}
}
