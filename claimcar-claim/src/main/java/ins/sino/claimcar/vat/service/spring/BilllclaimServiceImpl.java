package ins.sino.claimcar.vat.service.spring;

import ins.framework.common.ResultPage;
import ins.framework.dao.database.DatabaseDao;
import ins.framework.dao.database.support.Page;
import ins.framework.dao.database.support.QueryRule;
import ins.framework.utils.Beans;
import ins.platform.common.service.facade.BaseDaoService;
import ins.platform.common.service.facade.CodeTranService;
import ins.platform.common.util.ConfigUtil;
import ins.platform.utils.DataUtils;
import ins.platform.utils.SqlJoinUtils;
import ins.platform.vo.PrpLConfigValueVo;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.claim.po.PrpLCharge;
import ins.sino.claimcar.claim.po.PrpLPayment;
import ins.sino.claimcar.claim.po.PrpLPrePay;
import ins.sino.claimcar.claim.vo.PrpLChargeVo;
import ins.sino.claimcar.claim.vo.PrpLPaymentVo;
import ins.sino.claimcar.claim.vo.PrpLPrePayVo;
import ins.sino.claimcar.flow.po.PrpLCheckFee;
import ins.sino.claimcar.other.vo.PrpLAssessorFeeVo;
import ins.sino.claimcar.other.vo.PrpLCheckFeeVo;
import ins.sino.claimcar.vat.po.PrpLAssessorFee;
import ins.sino.claimcar.vat.po.PrpLbillcont;
import ins.sino.claimcar.vat.po.PrpLbillinfo;
import ins.sino.claimcar.vat.po.PrplAcbillcont;
import ins.sino.claimcar.vat.service.BilllclaimService;
import ins.sino.claimcar.vat.vo.PrpLbillcontVo;
import ins.sino.claimcar.vat.vo.PrpLbillinfoVo;
import ins.sino.claimcar.vat.vo.PrplAcbillcontVo;
import ins.sino.claimcar.vat.vo.VatQueryViewVo;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.Path;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"})
@Path("billlclaimService")
public class BilllclaimServiceImpl implements BilllclaimService{
	private static Logger logger = LoggerFactory.getLogger(BilllclaimServiceImpl.class);
	@Autowired
	DatabaseDao databaseDao;
	@Autowired
	BaseDaoService baseDaoService;
	@Autowired
	CodeTranService codeTranService;
	
	@Override
	public void saveOrUpdatePrplbillcont(PrpLbillcontVo prpLbillcontVo) {
		if(prpLbillcontVo.getId()!=null){//更新表数据
			PrpLbillcont prpLbillcont=databaseDao.findByPK(PrpLbillcont.class, prpLbillcontVo.getId());
			if(prpLbillcont!=null){
				Beans.copy().from(prpLbillcontVo).excludeNull().to(prpLbillcont);
			}
		}else{//保存表数据
			PrpLbillcont prpLbillcont=new PrpLbillcont();
			Beans.copy().from(prpLbillcontVo).excludeNull().to(prpLbillcont);
			databaseDao.save(PrpLbillcont.class, prpLbillcont);
		}
	}

	@Override
	public PrpLbillcontVo findPrpLbillcontVoByBillId(String billId) {
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("billId", billId);
		List<PrpLbillcont> prpLbillconts=databaseDao.findAll(PrpLbillcont.class, queryRule);
		PrpLbillcontVo prpLbillcontVo=null;
		if(prpLbillconts!=null && prpLbillconts.size()>0){
			prpLbillcontVo=new PrpLbillcontVo();
			Beans.copy().from(prpLbillconts.get(0)).excludeNull().to(prpLbillcontVo);
		}
		return prpLbillcontVo;
	}

	@Override
	public void saveOrUpdatePrpLbillinfoVo(PrpLbillinfoVo prpLbillinfoVo) {
		if(prpLbillinfoVo.getId()!=null){//更新表数据
			PrpLbillinfo prpLbillinfo=databaseDao.findByPK(PrpLbillinfo.class, prpLbillinfoVo.getId());
			if(prpLbillinfo!=null){
				Beans.copy().from(prpLbillinfoVo).excludeNull().to(prpLbillinfo);
			}
		}else{//保存表数据
			PrpLbillinfo prpLbillinfo=new PrpLbillinfo();
			Beans.copy().from(prpLbillinfoVo).excludeNull().to(prpLbillinfo);
			databaseDao.save(PrpLbillinfo.class, prpLbillinfo);
		}
	}

	@Override
	public PrpLbillinfoVo findPrpLbillinfoVoByParams(String billNo,String billCode) {
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("billNo", billNo);
		queryRule.addEqual("billCode", billCode);
		List<PrpLbillinfo> prpLbillinfos=databaseDao.findAll(PrpLbillinfo.class, queryRule);
		PrpLbillinfoVo prpLbillinfoVo=null;
		if(prpLbillinfos!=null && prpLbillinfos.size()>0){
			prpLbillinfoVo=new PrpLbillinfoVo();
			Beans.copy().from(prpLbillinfos.get(0)).excludeNull().to(prpLbillinfoVo);
		}
		return prpLbillinfoVo;
	}

	@Override
	public PrpLbillinfoVo findPrpLbillinfoVoByVatId(String vatbillId) {
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("vatbillId",vatbillId);
		List<PrpLbillinfo> prpLbillinfos=databaseDao.findAll(PrpLbillinfo.class, queryRule);
		PrpLbillinfoVo prpLbillinfoVo=null;
		if(prpLbillinfos!=null && prpLbillinfos.size()>0){
			prpLbillinfoVo=new PrpLbillinfoVo();
			Beans.copy().from(prpLbillinfos.get(0)).excludeNull().to(prpLbillinfoVo);
		}
		return prpLbillinfoVo;
	}

	@Override
	public int findRegisterTask(String comCode) {
		Long sumTask=0L;//任务数
		if(StringUtils.isBlank(comCode)){
			return sumTask.intValue();
		}
		String subComCode="";
		if(comCode.startsWith("0002")){
			subComCode=comCode.substring(0, 6);
			if("00".equals(comCode.substring(4, 6))){
				subComCode=comCode.substring(0, 4);
			}
		}else{
			subComCode=comCode.substring(0, 4);
			if("00".equals(comCode.substring(2, 4))){
				subComCode=comCode.substring(0, 2);
			}
		}
		//实赔赔款(赔给修理厂)
		SqlJoinUtils sqlUtils1=new SqlJoinUtils();
		sqlUtils1.append("select count(*) from PrpLPayment a where 1=1 "
				+ "and exists(select 1 from PrpLCompensate c where a.compensateNo=c.compensateNo and c.comCode like ? and c.underwriteFlag in (?,?) ) "
				+ "and exists(select 1 from PrpLPayCustom d where d.id=a.payeeId and d.payObjectKind= ? ) "
				+ "and not exists(select 1 from PrpLbillcont f,PrpLbillinfo h where f.billNo=h.billNo and f.billCode=h.billCode "
				+ "and f.bussType=? and f.compensateNo=a.compensateNo and h.sendstatus=? and f.localFeeType=? and f.status=? and f.payId=a.payeeId and h.flag=? ) "
				+ "and a.sumRealPay > ? and a.vatInvoiceFlag=? ");
		sqlUtils1.addParamValue(subComCode+"%");//机构
		sqlUtils1.addParamValue("1");//核赔通过
		sqlUtils1.addParamValue("3");//自核
		sqlUtils1.addParamValue("6");//收款人是修理厂
		sqlUtils1.addParamValue("0");//业务类型-赔款
		sqlUtils1.addParamValue("1");//发票已推送vat
		sqlUtils1.addParamValue("37");//实赔赔款
		sqlUtils1.addParamValue("1");//有效标识
		sqlUtils1.addParamValue("1");//是否增值税专票
		sqlUtils1.addParamValue(new BigDecimal("0"));//赔付金额大于0
		sqlUtils1.addParamValue("1");//增值税专票标志
		String hqlS=sqlUtils1.getSql();
		logger.info("实赔赔款(赔给修理厂)查询sql:"+hqlS);
	    Object[] paramS=sqlUtils1.getParamValues();
	    Long suma=baseDaoService.getCountBySql(hqlS, paramS);
	    //实赔费用
		SqlJoinUtils sqlUtils2=new SqlJoinUtils();
		sqlUtils2.append("select count(*) from PrpLCharge a where 1=1 "
				+ "and exists(select 1 from PrpLCompensate c where a.compensateNo=c.compensateNo and c.comCode like ? and c.underwriteFlag in (?,?) ) "
				+ "and not exists(select 1 from PrpLbillcont f,PrpLbillinfo h where f.billNo=h.billNo and f.billCode=h.billCode "
				+ "and f.bussType=? and f.compensateNo=a.compensateNo and h.sendstatus=? and f.localFeeType=a.chargeCode and f.status=? and f.payId=a.payeeId and h.flag=? ) "
				+ "and a.feeRealAmt > ? and a.vatInvoiceFlag=?  ");
		sqlUtils2.addParamValue(subComCode+"%");//机构
		sqlUtils2.addParamValue("1");//核赔通过
		sqlUtils2.addParamValue("3");//自核
		sqlUtils2.addParamValue("1");//业务类型-费用
		sqlUtils2.addParamValue("1");//发票已推送vat
		sqlUtils2.addParamValue("1");//有效标识
		sqlUtils2.addParamValue("1");//是否增值税专票
		sqlUtils2.addParamValue(new BigDecimal("0"));//赔付金额大于0
		sqlUtils2.addParamValue("1");//增值税专票标志
		String hqlf=sqlUtils2.getSql();
		logger.info("实赔费用查询sql:"+hqlf);
	    Object[] paramf=sqlUtils2.getParamValues();
	    Long sumf=baseDaoService.getCountBySql(hqlf, paramf);
	    //预付赔款(赔给修理厂)
		SqlJoinUtils sqlUtils3=new SqlJoinUtils();
		sqlUtils3.append("select count(*) from PrpLPrePay a where 1=1 "
				+ "and exists(select 1 from PrpLCompensate c where a.compensateNo=c.compensateNo and c.comCode like ? and c.underwriteFlag in (?,?) ) "
				+ "and exists(select 1 from PrpLPayCustom d where d.id=a.payeeId and d.payObjectKind= ? ) "
				+ "and not exists(select 1 from PrpLbillcont f,PrpLbillinfo h where f.billNo=h.billNo and f.billCode=h.billCode "
				+ "and f.bussType=? and f.compensateNo=a.compensateNo and h.sendstatus=? and f.localFeeType=? and f.status=? and f.payId=a.payeeId and h.flag=? ) "
				+ "and a.payAmt > ? and a.feeType=? and a.vatInvoiceFlag=? ");
		sqlUtils3.addParamValue(subComCode+"%");//机构
		sqlUtils3.addParamValue("1");//核赔通过
		sqlUtils3.addParamValue("3");//自核
		sqlUtils3.addParamValue("6");//收款人是修理厂
		sqlUtils3.addParamValue("0");//业务类型-赔款
		sqlUtils3.addParamValue("1");//发票已推送vat
		sqlUtils3.addParamValue("38");//预付赔款
		sqlUtils3.addParamValue("1");//有效标识
		sqlUtils3.addParamValue("1");//是否增值税专票
		sqlUtils3.addParamValue(new BigDecimal("0"));//赔付金额大于0
		sqlUtils3.addParamValue("P");//业务类型-赔款
		sqlUtils3.addParamValue("1");//增值税专票标志
		String hqly=sqlUtils3.getSql();
		logger.info("预付赔款(赔给修理厂)查询sql:"+hqly);
	    Object[] paramy=sqlUtils3.getParamValues();
	    Long sumy=baseDaoService.getCountBySql(hqly, paramy);
	    //预付费用
		SqlJoinUtils sqlUtils4=new SqlJoinUtils();
		sqlUtils4.append("select count(*) from PrpLPrePay a where 1=1 "
				+ "and exists(select 1 from PrpLCompensate c where a.compensateNo=c.compensateNo and c.comCode like ? and c.underwriteFlag in (?,?) ) "
				+ "and not exists(select 1 from PrpLbillcont f,PrpLbillinfo h where f.billNo=h.billNo and f.billCode=h.billCode "
				+ "and f.bussType=? and f.compensateNo=a.compensateNo and h.sendstatus=? and f.localFeeType=a.chargeCode and f.status=? and f.payId=a.payeeId and h.flag=? ) "
				+ "and a.payAmt > ? and a.feeType=? and a.vatInvoiceFlag=? ");
		sqlUtils4.addParamValue(subComCode+"%");////机构
		sqlUtils4.addParamValue("1");//核赔通过
		sqlUtils4.addParamValue("3");//自核
		sqlUtils4.addParamValue("1");//业务类型-费用
		sqlUtils4.addParamValue("1");//发票已推送vat
		sqlUtils4.addParamValue("1");//有效标识
		sqlUtils4.addParamValue("1");//是否增值税专票
		sqlUtils4.addParamValue(new BigDecimal("0"));//赔付金额大于0
		sqlUtils4.addParamValue("F");//业务类型-费用
		sqlUtils4.addParamValue("1");//增值税专票标志
		String hqlyf=sqlUtils4.getSql();
		logger.info("预付费用查询sql:"+hqlyf);
	    Object[] paramyf=sqlUtils4.getParamValues();
	    Long sumyf=baseDaoService.getCountBySql(hqlyf, paramyf);
	    if(suma!=null){
	    	sumTask=sumTask+suma.intValue();
	    }
	    if(sumf!=null){
	    	sumTask=sumTask+sumf.intValue();
	    }
	    if(sumy != null){
	    	sumTask=sumTask+sumy.intValue();
	    }
	    if(sumyf!=null){
	    	sumTask=sumTask+sumyf.intValue();
	    }
	    return sumTask.intValue();
	}

	@Override
	public ResultPage<VatQueryViewVo> findBillPageForBillInfo(VatQueryViewVo queryVo) {
		PrpLConfigValueVo configValuesingVo = ConfigUtil.findConfigByCode(CodeConstants.newBillDate);
		int start=queryVo.getStart();
		int length=queryVo.getLength();
		//权限机构
		String subComCode="";
		if(StringUtils.isNotBlank(queryVo.getComCode())){
			if(queryVo.getComCode().startsWith("0002")){
				subComCode=queryVo.getComCode().substring(0, 6);
				if("00".equals(queryVo.getComCode().substring(4, 6))){
					subComCode=queryVo.getComCode().substring(0, 4);
				}
			}else{
				subComCode=queryVo.getComCode().substring(0, 4);
				if("00".equals(queryVo.getComCode().substring(2, 4))){
					subComCode=queryVo.getComCode().substring(0, 2);
				}
			}
		}
        SqlJoinUtils sqlJoinUtils = new SqlJoinUtils();
        //实赔赔款(修理厂)
        sqlJoinUtils.append(" select c.id,b.registNo,b.compensateNo,'0','37',b.comCode,b.underwriteDate,c.payeeName,c.accountNo,a.sumRealPay,a.regsumAmount,"
        		           + " case when exists(select 1 from PrpLbillcont z,PrpLbillinfo x where z.compensateNo=a.compensateNo and z.billNo=x.billNo and z.billCode=x.billCode  and z.feeType='37' and z.bussType='0' and z.status='1' and z.payId=a.payeeId and x.sendstatus='1' and x.flag='1' "
        		           + ") then '1' when not exists(select 1 from PrpLbillcont z,PrpLbillinfo x where z.compensateNo=a.compensateNo and z.billNo=x.billNo and z.billCode=x.billCode  and z.feeType='37' and z.bussType='0' and z.status='1' and z.payId=a.payeeId and x.sendstatus='1' and x.flag='1' ) and a.vatInvoiceFlag='1' "
        		           + " then '0' else '3' end,a.id as bussId  "
        		           + "from PrpLPayment a,PrpLCompensate b,PrpLPayCustom c where 1=1 ");
        sqlJoinUtils.append(" and a.compensateNo=b.compensateNo and a.payeeId=c.id and c.payObjectKind=? and a.sumRealPay > ? and b.comCode like ? ");
        sqlJoinUtils.addParamValue("6");//收款人为修理厂
        sqlJoinUtils.addParamValue(new BigDecimal("0"));//实赔金额
        sqlJoinUtils.addParamValue(subComCode+"%");
        sqlJoinUtils.append(" and ((b.underwriteDate <= ? and a.regsumAmount is not null) or b.underwriteDate > ?)");
        sqlJoinUtils.addParamValue(DateBillString(configValuesingVo.getConfigValue()));
        sqlJoinUtils.addParamValue(DateBillString(configValuesingVo.getConfigValue()));
        //报案号
        if(StringUtils.isNotBlank(queryVo.getRegistNo())){
        	sqlJoinUtils.append(" and b.registNo=? ");
        	sqlJoinUtils.addParamValue(queryVo.getRegistNo());
        }
        //计算书号
        if(StringUtils.isNotBlank(queryVo.getCompensateNo())){
        	sqlJoinUtils.append(" and b.compensateNo=? ");
        	sqlJoinUtils.addParamValue(queryVo.getCompensateNo());
        }
        //保单号
        if(StringUtils.isNotBlank(queryVo.getPolicyNo())){
        	sqlJoinUtils.append(" and b.policyNo=? ");
        	sqlJoinUtils.addParamValue(queryVo.getPolicyNo());
        }
        //收款人
        if(StringUtils.isNotBlank(queryVo.getPayName())){
        	sqlJoinUtils.append(" and c.payeeName like ? ");
        	sqlJoinUtils.addParamValue(queryVo.getPayName()+"%");
        }
        //收款人账号
        if(StringUtils.isNotBlank(queryVo.getAccountNo())){
        	sqlJoinUtils.append(" and c.accountNo = ? ");
        	sqlJoinUtils.addParamValue(queryVo.getAccountNo());
        }
        //标的车牌号
        if(StringUtils.isNotBlank(queryVo.getLicenseNo())){
        	sqlJoinUtils.append(" and exists(select 1 from PrpLCItemCar d where d.registNo=b.registNo and d.policyNo=b.policyNo and d.licenseNo like ? )");
        	sqlJoinUtils.addParamValue(queryVo.getLicenseNo()+"%");
        }
        //被保险人
        if(StringUtils.isNotBlank(queryVo.getPolicyName())){
        	sqlJoinUtils.append(" and exists(select 1 from PrpLCMain f where f.registNo=b.registNo and f.policyNo=b.policyNo and f.insuredName like ? )");
        	sqlJoinUtils.addParamValue(queryVo.getPolicyName()+"%");
        }
        //发票号码  or 发票代码   or 销方名称   or 纳税人识别号
        if((StringUtils.isNotBlank(queryVo.getBillNo()) || StringUtils.isNotBlank(queryVo.getBillCode()) || StringUtils.isNotBlank(queryVo.getSaleName()) || StringUtils.isNotBlank(queryVo.getSaleNo()))){
        	sqlJoinUtils.append(" and exists(select 1 from PrpLbillcont h,PrpLbillinfo g where h.compensateNo=a.compensateNo and h.billNo=g.billNo and h.billCode=g.billCode  and h.feeType=? and h.bussType=? and h.status=? and h.payId=a.payeeId  and g.flag=? ");
        	sqlJoinUtils.addParamValue("37");//费用类型
        	sqlJoinUtils.addParamValue("0");//业务类型
        	sqlJoinUtils.addParamValue("1");//有效标识
        	sqlJoinUtils.addParamValue("1");//是否增值税专票
        	if(StringUtils.isNotBlank(queryVo.getBillNo())){
        		sqlJoinUtils.append("and g.billNo=? ");//发票号码
        		sqlJoinUtils.addParamValue(queryVo.getBillNo());
        	}
        	if(StringUtils.isNotBlank(queryVo.getBillCode())){
        		sqlJoinUtils.append("and g.billCode=? ");//发票代码
        		sqlJoinUtils.addParamValue(queryVo.getBillCode());
        	}
        	if(StringUtils.isNotBlank(queryVo.getSaleName())){
        		sqlJoinUtils.append("and g.saleName like ? ");//销方名称
        		sqlJoinUtils.addParamValue(queryVo.getSaleName()+"%");
        	}
        	if(StringUtils.isNotBlank(queryVo.getSaleNo())){
        		sqlJoinUtils.append("and g.saleNo=? ");//纳税人识别号
        		sqlJoinUtils.addParamValue(queryVo.getSaleNo());
        	}
        	sqlJoinUtils.append(")");
        }
        //当待办状态不为空
        if(StringUtils.isNotBlank(queryVo.getWorkFlag())){
        	//待办任务(说明：某一个计算书的费用类型，如果其费用有被推过vat,则该条记录为已办任务，如果该费用类型没有被推送过vat并且理算或预付页面是选择是增值税专票,则该条记录为待办任务)
        	if("0".equals(queryVo.getWorkFlag())){
        		sqlJoinUtils.append(" and not exists(select 1 from PrpLbillcont h,PrpLbillinfo g where h.compensateNo=a.compensateNo and h.billNo=g.billNo and h.billCode=g.billCode  and h.feeType=? and h.bussType=? and h.status=? and h.payId=a.payeeId and g.sendstatus=? and g.flag=? ");
            	sqlJoinUtils.addParamValue("37");//费用类型
            	sqlJoinUtils.addParamValue("0");//业务类型
            	sqlJoinUtils.addParamValue("1");//有效标识
            	sqlJoinUtils.addParamValue("1");//已推送vat
            	sqlJoinUtils.addParamValue("1");//是否增值税专票
            	sqlJoinUtils.append(")");
            	sqlJoinUtils.append(" and a.vatInvoiceFlag=? ");
            	sqlJoinUtils.addParamValue("1");//是否增值税专票
        	}else if("1".equals(queryVo.getWorkFlag())){//已办
        		sqlJoinUtils.append(" and exists(select 1 from PrpLbillcont h,PrpLbillinfo g where h.compensateNo=a.compensateNo and h.billNo=g.billNo and h.billCode=g.billCode  and h.feeType=? and h.bussType=? and h.status=? and h.payId=a.payeeId and g.sendstatus=? and g.flag=? ");
            	sqlJoinUtils.addParamValue("37");//费用类型
            	sqlJoinUtils.addParamValue("0");//业务类型
            	sqlJoinUtils.addParamValue("1");//有效标识
            	sqlJoinUtils.addParamValue("1");//已推送vat
            	sqlJoinUtils.addParamValue("1");//是否增值税专票
            	sqlJoinUtils.append(")");
        	}
        	
        }
        
        //报案时间  or 出险时间
        if(queryVo.getReportStartTime()!=null || queryVo.getReportEndTime()!=null || queryVo.getDamageStartTime()!=null || queryVo.getDamageEndTime()!=null){
        	sqlJoinUtils.append(" and exists(select 1 from PrpLRegist k where k.registNo=b.registNo ");
        	if(queryVo.getReportStartTime()!=null){
        		sqlJoinUtils.append(" and k.reportTime >= ? ");
        		sqlJoinUtils.addParamValue(queryVo.getReportStartTime());
        	}
        	if(queryVo.getReportEndTime()!=null){
        		sqlJoinUtils.append(" and k.reportTime <= ? ");
        		sqlJoinUtils.addParamValue(queryVo.getReportEndTime());
        	}
        	if(queryVo.getDamageStartTime()!=null){
        		sqlJoinUtils.append(" and k.damageTime >= ? ");
        		sqlJoinUtils.addParamValue(queryVo.getDamageStartTime());
        	}
        	if(queryVo.getDamageEndTime()!=null){
        		sqlJoinUtils.append(" and k.damageTime <= ? ");
        		sqlJoinUtils.addParamValue(queryVo.getDamageEndTime());
        	}
        	sqlJoinUtils.append(" )");
        }
        //核赔通过时间 or 结案通过时间(因为理赔系统是核赔通过就自动结案的，所以认为核赔通过时间等于结案时间)
        if(queryVo.getUnderwriteStartDate()!=null || queryVo.getUnderwriteEndDate()!=null || queryVo.getEndcaseStartTime()!=null || queryVo.getEndcaseEndTime()!=null){
        	if(queryVo.getUnderwriteStartDate()!=null){
        		sqlJoinUtils.append(" and b.underwriteDate >= ? ");
        		sqlJoinUtils.addParamValue(queryVo.getUnderwriteStartDate());
        	}
        	if(queryVo.getUnderwriteEndDate()!=null){
        		sqlJoinUtils.append(" and b.underwriteDate <= ? ");
        		sqlJoinUtils.addParamValue(queryVo.getUnderwriteEndDate());
        	}
        	if(queryVo.getEndcaseStartTime()!=null){
        		sqlJoinUtils.append(" and b.underwriteDate >= ? ");
        		sqlJoinUtils.addParamValue(queryVo.getEndcaseStartTime());
        	}
        	if(queryVo.getEndcaseEndTime()!=null){
        		sqlJoinUtils.append(" and b.underwriteDate <= ? ");
        		sqlJoinUtils.addParamValue(queryVo.getEndcaseEndTime());
        	}
        }
        //必须核赔通过
        sqlJoinUtils.append(" and b.underwriteFlag in (?,?) ");
        sqlJoinUtils.addParamValue("1");//核赔通过
        sqlJoinUtils.addParamValue("3");//自核
		//联共保
		sqlJoinUtils.append(" and not exists(select 1 from PrpLRegist k where k.registNo=b.registNo and k.ISGBFLAG = ? )");
		sqlJoinUtils.addParamValue("2");//从共
        sqlJoinUtils.append(" union all ");
        
        //实赔费用
        sqlJoinUtils.append("select c.id,b.registNo,b.compensateNo,'1',a.chargeCode,b.comCode,b.underwriteDate,c.payeeName,c.accountNo,a.feeRealAmt,a.regsumAmount,"
        		           + "case when exists(select 1 from PrpLbillcont z,PrpLbillinfo x where z.compensateNo=a.compensateNo and z.billNo=x.billNo and z.billCode=x.billCode  and z.localFeeType=a.chargeCode and z.bussType='1' and z.status='1' and z.payId=a.payeeId and x.sendstatus='1' and x.flag='1' "
        		           + ") then '1' when not exists(select 1 from PrpLbillcont z,PrpLbillinfo x where z.compensateNo=a.compensateNo and z.billNo=x.billNo and z.billCode=x.billCode  and z.localFeeType=a.chargeCode and z.bussType='1' and z.status='1' and z.payId=a.payeeId and x.sendstatus='1' and x.flag='1' ) and a.vatInvoiceFlag='1' "
        		           + " then '0' else '3' end,a.id as bussId "
        		           + "from PrpLCharge a,PrpLCompensate b,PrpLPayCustom c where 1=1 ");
        sqlJoinUtils.append(" and a.compensateNo=b.compensateNo and a.payeeId=c.id and a.feeRealAmt > ? and b.comCode like ? ");
        sqlJoinUtils.addParamValue(new BigDecimal("0"));//实赔金额
        sqlJoinUtils.addParamValue(subComCode+"%");
        sqlJoinUtils.append(" and ((b.underwriteDate <= ? and a.regsumAmount is not null) or b.underwriteDate > ?)");
        sqlJoinUtils.addParamValue(DateBillString(configValuesingVo.getConfigValue()));
        sqlJoinUtils.addParamValue(DateBillString(configValuesingVo.getConfigValue()));
        //报案号
        if(StringUtils.isNotBlank(queryVo.getRegistNo())){
        	sqlJoinUtils.append(" and b.registNo=? ");
        	sqlJoinUtils.addParamValue(queryVo.getRegistNo());
        }
        //计算书号
        if(StringUtils.isNotBlank(queryVo.getCompensateNo())){
        	sqlJoinUtils.append(" and b.compensateNo=? ");
        	sqlJoinUtils.addParamValue(queryVo.getCompensateNo());
        }
        //保单号
        if(StringUtils.isNotBlank(queryVo.getPolicyNo())){
        	sqlJoinUtils.append(" and b.policyNo=? ");
        	sqlJoinUtils.addParamValue(queryVo.getPolicyNo());
        }
        //收款人
        if(StringUtils.isNotBlank(queryVo.getPayName())){
        	sqlJoinUtils.append(" and c.payeeName like ? ");
        	sqlJoinUtils.addParamValue(queryVo.getPayName()+"%");
        }
        //收款人账号
        if(StringUtils.isNotBlank(queryVo.getAccountNo())){
        	sqlJoinUtils.append(" and c.accountNo = ? ");
        	sqlJoinUtils.addParamValue(queryVo.getAccountNo());
        }
        //标的车牌号
        if(StringUtils.isNotBlank(queryVo.getLicenseNo())){
        	sqlJoinUtils.append(" and exists(select 1 from PrpLCItemCar d where d.registNo=b.registNo and d.policyNo=b.policyNo and d.licenseNo like ? )");
        	sqlJoinUtils.addParamValue(queryVo.getLicenseNo()+"%");
        }
        //被保险人
        if(StringUtils.isNotBlank(queryVo.getPolicyName())){
        	sqlJoinUtils.append(" and exists(select 1 from PrpLCMain f where f.registNo=b.registNo and f.policyNo=b.policyNo and f.insuredName like ? )");
        	sqlJoinUtils.addParamValue(queryVo.getPolicyName()+"%");
        }
        //发票号码  or 发票代码   or 销方名称   or 纳税人识别号
        if((StringUtils.isNotBlank(queryVo.getBillNo()) || StringUtils.isNotBlank(queryVo.getBillCode()) || StringUtils.isNotBlank(queryVo.getSaleName()) || StringUtils.isNotBlank(queryVo.getSaleNo()))){
        	sqlJoinUtils.append(" and exists(select 1 from PrpLbillcont h,PrpLbillinfo g where h.compensateNo=a.compensateNo and h.billNo=g.billNo and h.billCode=g.billCode  and h.localFeeType=a.chargeCode and h.bussType=? and h.status=? and h.payId=a.payeeId and g.flag=?  ");
        	sqlJoinUtils.addParamValue("1");//业务类型
        	sqlJoinUtils.addParamValue("1");//有效标识
        	sqlJoinUtils.addParamValue("1");//是否增值税专票
        	if(StringUtils.isNotBlank(queryVo.getBillNo())){
        		sqlJoinUtils.append("and g.billNo=? ");//发票号码
        		sqlJoinUtils.addParamValue(queryVo.getBillNo());
        	}
        	if(StringUtils.isNotBlank(queryVo.getBillCode())){
        		sqlJoinUtils.append("and g.billCode=? ");//发票代码
        		sqlJoinUtils.addParamValue(queryVo.getBillCode());
        	}
        	if(StringUtils.isNotBlank(queryVo.getSaleName())){
        		sqlJoinUtils.append("and g.saleName like ? ");//销方名称
        		sqlJoinUtils.addParamValue(queryVo.getSaleName()+"%");
        	}
        	if(StringUtils.isNotBlank(queryVo.getSaleNo())){
        		sqlJoinUtils.append("and g.saleNo=? ");//纳税人识别号
        		sqlJoinUtils.addParamValue(queryVo.getSaleNo());
        	}
        	sqlJoinUtils.append(")");
        }
        //当待办状态不为空
        if(StringUtils.isNotBlank(queryVo.getWorkFlag())){
        	//待办任务(说明：某一个计算书的费用类型，如果其费用有被推过vat,则该条记录为已办任务，如果该费用类型没有被推送过vat并且理算或预付页面是选择是增值税专票,则该条记录为待办任务)
        	if("0".equals(queryVo.getWorkFlag())){
        		sqlJoinUtils.append(" and not exists(select 1 from PrpLbillcont h,PrpLbillinfo g where h.compensateNo=a.compensateNo and h.billNo=g.billNo and h.billCode=g.billCode  and h.localFeeType=a.chargeCode and h.bussType=? and h.status=? and h.payId=a.payeeId and g.sendstatus=? and g.flag=? ");
            	sqlJoinUtils.addParamValue("1");//业务类型
            	sqlJoinUtils.addParamValue("1");//有效标识
            	sqlJoinUtils.addParamValue("1");//已推送vat
            	sqlJoinUtils.addParamValue("1");//是否增值税专票
            	sqlJoinUtils.append(")");
            	sqlJoinUtils.append(" and a.vatInvoiceFlag=? ");
            	sqlJoinUtils.addParamValue("1");//是否增值税专票
        	}else if("1".equals(queryVo.getWorkFlag())){//已办
        		sqlJoinUtils.append(" and exists(select 1 from PrpLbillcont h,PrpLbillinfo g where h.compensateNo=a.compensateNo and h.billNo=g.billNo and h.billCode=g.billCode  and h.localFeeType=a.chargeCode and h.bussType=? and h.status=? and h.payId=a.payeeId and g.sendstatus=? and g.flag=? ");
            	sqlJoinUtils.addParamValue("1");//业务类型
            	sqlJoinUtils.addParamValue("1");//有效标识
            	sqlJoinUtils.addParamValue("1");//已推送vat
            	sqlJoinUtils.addParamValue("1");//是否增值税专票
            	sqlJoinUtils.append(")");
        	}
        	
        }
        //报案时间  or 出险时间
        if(queryVo.getReportStartTime()!=null || queryVo.getReportEndTime()!=null || queryVo.getDamageStartTime()!=null || queryVo.getDamageEndTime()!=null){
        	sqlJoinUtils.append(" and exists(select 1 from PrpLRegist k where k.registNo=b.registNo ");
        	if(queryVo.getReportStartTime()!=null){
        		sqlJoinUtils.append(" and k.reportTime >= ? ");
        		sqlJoinUtils.addParamValue(queryVo.getReportStartTime());
        	}
        	if(queryVo.getReportEndTime()!=null){
        		sqlJoinUtils.append(" and k.reportTime <= ? ");
        		sqlJoinUtils.addParamValue(queryVo.getReportEndTime());
        	}
        	if(queryVo.getDamageStartTime()!=null){
        		sqlJoinUtils.append(" and k.damageTime >= ? ");
        		sqlJoinUtils.addParamValue(queryVo.getDamageStartTime());
        	}
        	if(queryVo.getDamageEndTime()!=null){
        		sqlJoinUtils.append(" and k.damageTime <= ? ");
        		sqlJoinUtils.addParamValue(queryVo.getDamageEndTime());
        	}
        	sqlJoinUtils.append(" )");
        }
        //核赔通过时间 or 结案通过时间(因为理赔系统是核赔通过就自动结案的，所以认为核赔通过时间等于结案时间)
        if(queryVo.getUnderwriteStartDate()!=null || queryVo.getUnderwriteEndDate()!=null || queryVo.getEndcaseStartTime()!=null || queryVo.getEndcaseEndTime()!=null){
        	if(queryVo.getUnderwriteStartDate()!=null){
        		sqlJoinUtils.append(" and b.underwriteDate >= ? ");
        		sqlJoinUtils.addParamValue(queryVo.getUnderwriteStartDate());
        	}
        	if(queryVo.getUnderwriteEndDate()!=null){
        		sqlJoinUtils.append(" and b.underwriteDate <= ? ");
        		sqlJoinUtils.addParamValue(queryVo.getUnderwriteEndDate());
        	}
        	if(queryVo.getEndcaseStartTime()!=null){
        		sqlJoinUtils.append(" and b.underwriteDate >= ? ");
        		sqlJoinUtils.addParamValue(queryVo.getEndcaseStartTime());
        	}
        	if(queryVo.getEndcaseEndTime()!=null){
        		sqlJoinUtils.append(" and b.underwriteDate <= ? ");
        		sqlJoinUtils.addParamValue(queryVo.getEndcaseEndTime());
        	}
        }
        //必须核赔通过
        sqlJoinUtils.append(" and b.underwriteFlag in (?,?) ");
        sqlJoinUtils.addParamValue("1");//核赔通过
        sqlJoinUtils.addParamValue("3");//自核
		//联共保
		sqlJoinUtils.append(" and not exists(select 1 from PrpLRegist k where k.registNo=b.registNo and k.ISGBFLAG = ? )");
		sqlJoinUtils.addParamValue("2");//从共
        sqlJoinUtils.append(" union all ");
        
        //预付赔款
        sqlJoinUtils.append("select c.id,b.registNo,b.compensateNo,'0','38',b.comCode,b.underwriteDate,c.payeeName,c.accountNo,a.payAmt,a.regsumAmount,"
        		           + " case when exists(select 1 from PrpLbillcont z,PrpLbillinfo x where z.compensateNo=a.compensateNo and z.billNo=x.billNo and z.billCode=x.billCode  and z.localFeeType='38' and z.bussType='0' and z.status='1' and z.payId=a.payeeId and x.sendstatus='1' and x.flag='1' "
        		           + ") then '1' when not exists(select 1 from PrpLbillcont z,PrpLbillinfo x where z.compensateNo=a.compensateNo and z.billNo=x.billNo and z.billCode=x.billCode  and z.localFeeType='38' and z.bussType='0' and z.status='1' and z.payId=a.payeeId and x.sendstatus='1' and x.flag='1' ) and a.vatInvoiceFlag='1' "
        		           + " then '0' else '3' end ,a.id as bussId "
        		           + "from PrpLPrePay a,PrpLCompensate b,PrpLPayCustom c where 1=1 ");
        sqlJoinUtils.append(" and a.compensateNo=b.compensateNo and a.payeeId=c.id and a.feeType=? and c.payObjectKind=? and a.payAmt > ? and b.comCode like ? ");
        sqlJoinUtils.addParamValue("P");//赔款(修理厂)
        sqlJoinUtils.addParamValue("6");//赔款(修理厂)
        sqlJoinUtils.addParamValue(new BigDecimal("0"));//实赔金额
        sqlJoinUtils.addParamValue(subComCode+"%");
        sqlJoinUtils.append(" and ((b.underwriteDate <= ? and a.regsumAmount is not null) or b.underwriteDate > ?)");
        sqlJoinUtils.addParamValue(DateBillString(configValuesingVo.getConfigValue()));
        sqlJoinUtils.addParamValue(DateBillString(configValuesingVo.getConfigValue()));
        //报案号
        if(StringUtils.isNotBlank(queryVo.getRegistNo())){
        	sqlJoinUtils.append(" and b.registNo=? ");
        	sqlJoinUtils.addParamValue(queryVo.getRegistNo());
        }
        //计算书号
        if(StringUtils.isNotBlank(queryVo.getCompensateNo())){
        	sqlJoinUtils.append(" and b.compensateNo=? ");
        	sqlJoinUtils.addParamValue(queryVo.getCompensateNo());
        }
        //保单号
        if(StringUtils.isNotBlank(queryVo.getPolicyNo())){
        	sqlJoinUtils.append(" and b.policyNo=? ");
        	sqlJoinUtils.addParamValue(queryVo.getPolicyNo());
        }
        //收款人
        if(StringUtils.isNotBlank(queryVo.getPayName())){
        	sqlJoinUtils.append(" and c.payeeName like ? ");
        	sqlJoinUtils.addParamValue(queryVo.getPayName()+"%");
        }
        //收款人账号
        if(StringUtils.isNotBlank(queryVo.getAccountNo())){
        	sqlJoinUtils.append(" and c.accountNo = ? ");
        	sqlJoinUtils.addParamValue(queryVo.getAccountNo());
        }
        //标的车牌号
        if(StringUtils.isNotBlank(queryVo.getLicenseNo())){
        	sqlJoinUtils.append(" and exists(select 1 from PrpLCItemCar d where d.registNo=b.registNo and d.policyNo=b.policyNo and d.licenseNo like ? )");
        	sqlJoinUtils.addParamValue(queryVo.getLicenseNo()+"%");
        }
        //被保险人
        if(StringUtils.isNotBlank(queryVo.getPolicyName())){
        	sqlJoinUtils.append(" and exists(select 1 from PrpLCMain f where f.registNo=b.registNo and f.policyNo=b.policyNo and f.insuredName like ? )");
        	sqlJoinUtils.addParamValue(queryVo.getPolicyName()+"%");
        }
        //发票号码  or 发票代码   or 销方名称   or 纳税人识别号
        if((StringUtils.isNotBlank(queryVo.getBillNo()) || StringUtils.isNotBlank(queryVo.getBillCode()) || StringUtils.isNotBlank(queryVo.getSaleName()) || StringUtils.isNotBlank(queryVo.getSaleNo()))){
        	sqlJoinUtils.append(" and exists(select 1 from PrpLbillcont h,PrpLbillinfo g where h.compensateNo=a.compensateNo and h.billNo=g.billNo and h.billCode=g.billCode  and h.localFeeType=? and h.bussType=? and h.status=? and h.payId=a.payeeId and g.flag=? ");
        	sqlJoinUtils.addParamValue("38");//费用类型
        	sqlJoinUtils.addParamValue("0");//业务类型
        	sqlJoinUtils.addParamValue("1");//有效标识
        	sqlJoinUtils.addParamValue("1");//是否增值税专票
        	if(StringUtils.isNotBlank(queryVo.getBillNo())){
        		sqlJoinUtils.append("and g.billNo=? ");//发票号码
        		sqlJoinUtils.addParamValue(queryVo.getBillNo());
        	}
        	if(StringUtils.isNotBlank(queryVo.getBillCode())){
        		sqlJoinUtils.append("and g.billCode=? ");//发票代码
        		sqlJoinUtils.addParamValue(queryVo.getBillCode());
        	}
        	if(StringUtils.isNotBlank(queryVo.getSaleName())){
        		sqlJoinUtils.append("and g.saleName like ? ");//销方名称
        		sqlJoinUtils.addParamValue(queryVo.getSaleName()+"%");
        	}
        	if(StringUtils.isNotBlank(queryVo.getSaleNo())){
        		sqlJoinUtils.append("and g.saleNo=? ");//纳税人识别号
        		sqlJoinUtils.addParamValue(queryVo.getSaleNo());
        	}
        	sqlJoinUtils.append(")");
        }
        //当待办状态不为空
        if(StringUtils.isNotBlank(queryVo.getWorkFlag())){
        	//待办任务(说明：某一个计算书的费用类型，如果其费用有被推过vat,则该条记录为已办任务，如果该费用类型没有被推送过vat并且理算或预付页面是选择是增值税专票,则该条记录为待办任务)
        	if("0".equals(queryVo.getWorkFlag())){
        		sqlJoinUtils.append(" and not exists(select 1 from PrpLbillcont h,PrpLbillinfo g where h.compensateNo=a.compensateNo and h.billNo=g.billNo and h.billCode=g.billCode  and h.localFeeType=? and h.bussType=? and h.status=? and h.payId=a.payeeId and g.sendstatus=? and g.flag=? ");
        		sqlJoinUtils.addParamValue("38");//费用类型
            	sqlJoinUtils.addParamValue("0");//业务类型
            	sqlJoinUtils.addParamValue("1");//有效标识
            	sqlJoinUtils.addParamValue("1");//已推送vat
            	sqlJoinUtils.addParamValue("1");//是否增值税专票
            	sqlJoinUtils.append(")");
            	sqlJoinUtils.append(" and a.vatInvoiceFlag=? ");
            	sqlJoinUtils.addParamValue("1");//是否增值税专票
        	}else if("1".equals(queryVo.getWorkFlag())){//已办
        		sqlJoinUtils.append(" and exists(select 1 from PrpLbillcont h,PrpLbillinfo g where h.compensateNo=a.compensateNo and h.billNo=g.billNo and h.billCode=g.billCode  and h.localFeeType=? and h.bussType=? and h.status=? and h.payId=a.payeeId and g.sendstatus=? and g.flag=? ");
        		sqlJoinUtils.addParamValue("38");//费用类型
            	sqlJoinUtils.addParamValue("0");//业务类型
            	sqlJoinUtils.addParamValue("1");//有效标识
            	sqlJoinUtils.addParamValue("1");//已推送vat
            	sqlJoinUtils.addParamValue("1");//是否增值税专票
            	sqlJoinUtils.append(")");
        	}
        	
        }
        //报案时间  or 出险时间
        if(queryVo.getReportStartTime()!=null || queryVo.getReportEndTime()!=null || queryVo.getDamageStartTime()!=null || queryVo.getDamageEndTime()!=null){
        	sqlJoinUtils.append(" and exists(select 1 from PrpLRegist k where k.registNo=b.registNo ");
        	if(queryVo.getReportStartTime()!=null){
        		sqlJoinUtils.append(" and k.reportTime >= ? ");
        		sqlJoinUtils.addParamValue(queryVo.getReportStartTime());
        	}
        	if(queryVo.getReportEndTime()!=null){
        		sqlJoinUtils.append(" and k.reportTime <= ? ");
        		sqlJoinUtils.addParamValue(queryVo.getReportEndTime());
        	}
        	if(queryVo.getDamageStartTime()!=null){
        		sqlJoinUtils.append(" and k.damageTime >= ? ");
        		sqlJoinUtils.addParamValue(queryVo.getDamageStartTime());
        	}
        	if(queryVo.getDamageEndTime()!=null){
        		sqlJoinUtils.append(" and k.damageTime <= ? ");
        		sqlJoinUtils.addParamValue(queryVo.getDamageEndTime());
        	}
        	sqlJoinUtils.append(" )");
        }
        //核赔通过时间 or 结案通过时间(因为理赔系统是核赔通过就自动结案的，所以认为核赔通过时间等于结案时间)
        if(queryVo.getUnderwriteStartDate()!=null || queryVo.getUnderwriteEndDate()!=null || queryVo.getEndcaseStartTime()!=null || queryVo.getEndcaseEndTime()!=null){
        	if(queryVo.getUnderwriteStartDate()!=null){
        		sqlJoinUtils.append(" and b.underwriteDate >= ? ");
        		sqlJoinUtils.addParamValue(queryVo.getUnderwriteStartDate());
        	}
        	if(queryVo.getUnderwriteEndDate()!=null){
        		sqlJoinUtils.append(" and b.underwriteDate <= ? ");
        		sqlJoinUtils.addParamValue(queryVo.getUnderwriteEndDate());
        	}
        	if(queryVo.getEndcaseStartTime()!=null){
        		sqlJoinUtils.append(" and b.underwriteDate >= ? ");
        		sqlJoinUtils.addParamValue(queryVo.getEndcaseStartTime());
        	}
        	if(queryVo.getEndcaseEndTime()!=null){
        		sqlJoinUtils.append(" and b.underwriteDate <= ? ");
        		sqlJoinUtils.addParamValue(queryVo.getEndcaseEndTime());
        	}
        }
        //必须核赔通过
        sqlJoinUtils.append(" and b.underwriteFlag in (?,?) ");
        sqlJoinUtils.addParamValue("1");//核赔通过
        sqlJoinUtils.addParamValue("3");//自核
		//联共保
		sqlJoinUtils.append(" and not exists(select 1 from PrpLRegist k where k.registNo=b.registNo and k.ISGBFLAG = ? )");
		sqlJoinUtils.addParamValue("2");//从共
        sqlJoinUtils.append(" union all ");
        
        //预付费用
        sqlJoinUtils.append("select c.id,b.registNo,b.compensateNo,'1',a.chargeCode,b.comCode,b.underwriteDate,c.payeeName,c.accountNo,a.payAmt,a.regsumAmount,"
        		           + " case when exists(select 1 from PrpLbillcont z,PrpLbillinfo x where z.compensateNo=a.compensateNo and z.billNo=x.billNo and z.billCode=x.billCode  and z.localFeeType=a.chargeCode and z.bussType='1' and z.status='1' and z.payId=a.payeeId and x.sendstatus='1' and x.flag='1' "
        		           + ") then '1' when not exists(select 1 from PrpLbillcont z,PrpLbillinfo x where z.compensateNo=a.compensateNo and z.billNo=x.billNo and z.billCode=x.billCode  and z.localFeeType=a.chargeCode and z.bussType='1' and z.status='1' and z.payId=a.payeeId and x.sendstatus='1' and x.flag='1' ) and a.vatInvoiceFlag='1' "
        		           + " then '0' else '3' end ,a.id as bussId "
        		           + "from PrpLPrePay a,PrpLCompensate b,PrpLPayCustom c where 1=1 ");
        sqlJoinUtils.append(" and a.compensateNo=b.compensateNo and a.payeeId=c.id and a.feeType=? and a.payAmt > ? and b.comCode like ? ");
        sqlJoinUtils.addParamValue("F");//费用
        sqlJoinUtils.addParamValue(new BigDecimal("0"));//实赔金额
        sqlJoinUtils.addParamValue(subComCode+"%");
        sqlJoinUtils.append(" and ((b.underwriteDate <= ? and a.regsumAmount is not null) or b.underwriteDate > ?)");
        sqlJoinUtils.addParamValue(DateBillString(configValuesingVo.getConfigValue()));
        sqlJoinUtils.addParamValue(DateBillString(configValuesingVo.getConfigValue()));
        //报案号
        if(StringUtils.isNotBlank(queryVo.getRegistNo())){
        	sqlJoinUtils.append(" and b.registNo=? ");
        	sqlJoinUtils.addParamValue(queryVo.getRegistNo());
        }
        //计算书号
        if(StringUtils.isNotBlank(queryVo.getCompensateNo())){
        	sqlJoinUtils.append(" and b.compensateNo=? ");
        	sqlJoinUtils.addParamValue(queryVo.getCompensateNo());
        }
        //保单号
        if(StringUtils.isNotBlank(queryVo.getPolicyNo())){
        	sqlJoinUtils.append(" and b.policyNo=? ");
        	sqlJoinUtils.addParamValue(queryVo.getPolicyNo());
        }
        //收款人
        if(StringUtils.isNotBlank(queryVo.getPayName())){
        	sqlJoinUtils.append(" and c.payeeName like ? ");
        	sqlJoinUtils.addParamValue(queryVo.getPayName()+"%");
        }
        //收款人账号
        if(StringUtils.isNotBlank(queryVo.getAccountNo())){
        	sqlJoinUtils.append(" and c.accountNo = ? ");
        	sqlJoinUtils.addParamValue(queryVo.getAccountNo());
        }
        //标的车牌号
        if(StringUtils.isNotBlank(queryVo.getLicenseNo())){
        	sqlJoinUtils.append(" and exists(select 1 from PrpLCItemCar d where d.registNo=b.registNo and d.policyNo=b.policyNo and d.licenseNo like ? )");
        	sqlJoinUtils.addParamValue(queryVo.getLicenseNo()+"%");
        }
        //被保险人
        if(StringUtils.isNotBlank(queryVo.getPolicyName())){
        	sqlJoinUtils.append(" and exists(select 1 from PrpLCMain f where f.registNo=b.registNo and f.policyNo=b.policyNo and f.insuredName like ? )");
        	sqlJoinUtils.addParamValue(queryVo.getPolicyName()+"%");
        }
        //发票号码  or 发票代码   or 销方名称   or 纳税人识别号
        if((StringUtils.isNotBlank(queryVo.getBillNo()) || StringUtils.isNotBlank(queryVo.getBillCode()) || StringUtils.isNotBlank(queryVo.getSaleName()) || StringUtils.isNotBlank(queryVo.getSaleNo()))){
        	sqlJoinUtils.append(" and exists(select 1 from PrpLbillcont h,PrpLbillinfo g where h.compensateNo=a.compensateNo and h.billNo=g.billNo and h.billCode=g.billCode  and h.localFeeType=a.chargeCode and h.bussType=? and h.status=? and h.payId=a.payeeId ");
        	sqlJoinUtils.addParamValue("1");//业务类型
        	sqlJoinUtils.addParamValue("1");//有效标识
        	if(StringUtils.isNotBlank(queryVo.getBillNo())){
        		sqlJoinUtils.append("and g.billNo=? ");//发票号码
        		sqlJoinUtils.addParamValue(queryVo.getBillNo());
        	}
        	if(StringUtils.isNotBlank(queryVo.getBillCode())){
        		sqlJoinUtils.append("and g.billCode=? ");//发票代码
        		sqlJoinUtils.addParamValue(queryVo.getBillCode());
        	}
        	if(StringUtils.isNotBlank(queryVo.getSaleName())){
        		sqlJoinUtils.append("and g.saleName like ? ");//销方名称
        		sqlJoinUtils.addParamValue(queryVo.getSaleName()+"%");
        	}
        	if(StringUtils.isNotBlank(queryVo.getSaleNo())){
        		sqlJoinUtils.append("and g.saleNo=? ");//纳税人识别号
        		sqlJoinUtils.addParamValue(queryVo.getSaleNo());
        	}
        	sqlJoinUtils.append(")");
        }
        //当待办状态不为空
        if(StringUtils.isNotBlank(queryVo.getWorkFlag())){
        	//待办任务(说明：某一个计算书的费用类型，如果其费用有被推过vat,则该条记录为已办任务，如果该费用类型没有被推送过vat并且理算或预付页面是选择是增值税专票,则该条记录为待办任务)
        	if("0".equals(queryVo.getWorkFlag())){
        		sqlJoinUtils.append(" and not exists(select 1 from PrpLbillcont h,PrpLbillinfo g where h.compensateNo=a.compensateNo and h.billNo=g.billNo and h.billCode=g.billCode  and h.localFeeType=a.chargeCode and h.bussType=? and h.status=? and h.payId=a.payeeId and g.sendstatus=? and g.flag=? ");
            	sqlJoinUtils.addParamValue("1");//业务类型
            	sqlJoinUtils.addParamValue("1");//有效标识
            	sqlJoinUtils.addParamValue("1");//已推送vat
            	sqlJoinUtils.addParamValue("1");//是否增值税专票
            	sqlJoinUtils.append(")");
            	sqlJoinUtils.append(" and a.vatInvoiceFlag=? ");
            	sqlJoinUtils.addParamValue("1");//是否增值税专票
        	}else if("1".equals(queryVo.getWorkFlag())){//已办
        		sqlJoinUtils.append(" and exists(select 1 from PrpLbillcont h,PrpLbillinfo g where h.compensateNo=a.compensateNo and h.billNo=g.billNo and h.billCode=g.billCode  and h.localFeeType=a.chargeCode and h.bussType=? and h.status=? and h.payId=a.payeeId and g.sendstatus=? and g.flag=? ");
            	sqlJoinUtils.addParamValue("1");//业务类型
            	sqlJoinUtils.addParamValue("1");//有效标识
            	sqlJoinUtils.addParamValue("1");//已推送vat
            	sqlJoinUtils.addParamValue("1");//是否增值税专票
            	sqlJoinUtils.append(")");
        	}
        	
        }
        //报案时间  or 出险时间
        if(queryVo.getReportStartTime()!=null || queryVo.getReportEndTime()!=null || queryVo.getDamageStartTime()!=null || queryVo.getDamageEndTime()!=null){
        	sqlJoinUtils.append(" and exists(select 1 from PrpLRegist k where k.registNo=b.registNo ");
        	if(queryVo.getReportStartTime()!=null){
        		sqlJoinUtils.append(" and k.reportTime >= ? ");
        		sqlJoinUtils.addParamValue(queryVo.getReportStartTime());
        	}
        	if(queryVo.getReportEndTime()!=null){
        		sqlJoinUtils.append(" and k.reportTime <= ? ");
        		sqlJoinUtils.addParamValue(queryVo.getReportEndTime());
        	}
        	if(queryVo.getDamageStartTime()!=null){
        		sqlJoinUtils.append(" and k.damageTime >= ? ");
        		sqlJoinUtils.addParamValue(queryVo.getDamageStartTime());
        	}
        	if(queryVo.getDamageEndTime()!=null){
        		sqlJoinUtils.append(" and k.damageTime <= ? ");
        		sqlJoinUtils.addParamValue(queryVo.getDamageEndTime());
        	}
        	sqlJoinUtils.append(" )");
        }
        //核赔通过时间 or 结案通过时间(因为理赔系统是核赔通过就自动结案的，所以认为核赔通过时间等于结案时间)
        if(queryVo.getUnderwriteStartDate()!=null || queryVo.getUnderwriteEndDate()!=null || queryVo.getEndcaseStartTime()!=null || queryVo.getEndcaseEndTime()!=null){
        	if(queryVo.getUnderwriteStartDate()!=null){
        		sqlJoinUtils.append(" and b.underwriteDate >= ? ");
        		sqlJoinUtils.addParamValue(queryVo.getUnderwriteStartDate());
        	}
        	if(queryVo.getUnderwriteEndDate()!=null){
        		sqlJoinUtils.append(" and b.underwriteDate <= ? ");
        		sqlJoinUtils.addParamValue(queryVo.getUnderwriteEndDate());
        	}
        	if(queryVo.getEndcaseStartTime()!=null){
        		sqlJoinUtils.append(" and b.underwriteDate >= ? ");
        		sqlJoinUtils.addParamValue(queryVo.getEndcaseStartTime());
        	}
        	if(queryVo.getEndcaseEndTime()!=null){
        		sqlJoinUtils.append(" and b.underwriteDate <= ? ");
        		sqlJoinUtils.addParamValue(queryVo.getEndcaseEndTime());
        	}
        }
        //必须核赔通过
        sqlJoinUtils.append(" and b.underwriteFlag in (?,?) ");
        sqlJoinUtils.addParamValue("1");//核赔通过
        sqlJoinUtils.addParamValue("3");//自核
		//联共保
		sqlJoinUtils.append(" and not exists(select 1 from PrpLRegist k where k.registNo=b.registNo and k.ISGBFLAG = ?)");
		sqlJoinUtils.addParamValue("2");//从共
        //倒序排序
        sqlJoinUtils.append(" order by underwriteDate desc ");
        String sql = sqlJoinUtils.getSql();
        Object[] values = sqlJoinUtils.getParamValues();
        Page<Object[]> page = new Page<Object[]>();
        try{
            page = baseDaoService.pagedSQLQuery(sql,start,length,values);
        }catch(Exception e){
            e.printStackTrace();
        }

        List<VatQueryViewVo> resultVoList = new ArrayList<VatQueryViewVo>();
        if(page!=null && page.getResult()!=null){
        	 for(int i = 0; i<page.getResult().size(); i++ ){
             	VatQueryViewVo vo = new VatQueryViewVo();
             	 vo.setIndexNo(i+1);//序号
                 Object[] obj = (Object[])page.getResult().get(i);
                 vo.setPayId(obj[0]!=null?obj[0].toString():"");
                 vo.setRegistNo(obj[1]!=null?obj[1].toString():"");
                 vo.setCompensateNo(obj[2]!=null?obj[2].toString():"");
                 vo.setBussType(obj[3]!=null?obj[3].toString():"");
                 if("1".equals(vo.getBussType())){
                	 vo.setBussName("费用");
                 }else{
                	 vo.setBussName("赔款");
                 }
                 vo.setFeeCode(obj[4]!=null?obj[4].toString():"");
                 if(StringUtils.isNotBlank(vo.getFeeCode())){
                	 vo.setFeeName(CodeConstants.backFeeName(vo.getFeeCode()));
                 }
                 vo.setComCode(obj[5]!=null?obj[5].toString():"");
                 if(StringUtils.isNotBlank(vo.getComCode())){
                	 vo.setComName(codeTranService.transCode("ComCode",vo.getComCode())); 
                 }
                 if (obj[6] != null) {
     				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
     				if(obj[6] != null){
     					Date underDate=null;
						try {
							underDate = format.parse(obj[6].toString());
						} catch (ParseException e) {
							e.printStackTrace();
						}
     					vo.setUnderwriteDate(underDate);
     				} 
     			}
                 vo.setPayName(obj[7]!=null?obj[7].toString():"");
                 vo.setAccountNo(obj[8]!=null?obj[8].toString():"");
                 vo.setSumAmt(obj[9]!=null?new BigDecimal(obj[9].toString()):null);
                 vo.setRegisterNum(obj[10]!=null?new BigDecimal(obj[10].toString()):new BigDecimal("0"));
                 if(obj[11]!=null && "1".equals(obj[11].toString())){
                	 vo.setWorkFlag("1");
                	 vo.setWorkName("已办");
                 }else if(obj[11]!=null && "0".equals(obj[11].toString())){
                	 vo.setWorkFlag("0");
                	 vo.setWorkName("待办");
                 }
                 vo.setBussId(obj[12]!=null?obj[12].toString():null);
                 resultVoList.add(vo);
             }
        }
       
        
        ResultPage<VatQueryViewVo> resultPage = new ResultPage<VatQueryViewVo>(start,length,page.getTotalCount(),resultVoList);
        
        return resultPage;
	}

	@Override
	public ResultPage<VatQueryViewVo> findVatPageForCompInfo(VatQueryViewVo queryVo) {
		int start=queryVo.getStart();
		int length=queryVo.getLength();
		SqlJoinUtils sqlJoinUtils = new SqlJoinUtils();
        //计算书与发票关系查询
        sqlJoinUtils.append(" select b.billNo,b.billCode,b.billDate,b.saleName,b.saleNo,b.billNnum,"
        		+ " b.billSnum,b.billSl,b.billNum,a.registerNum from PrpLbillcont a,PrpLbillinfo b where a.billNo=b.billNo and "
        		+ " a.billCode=b.billCode and a.status=? and a.bussType=? and a.localFeeType=? and b.flag=? and a.vidFlag=? and a.linktableId=? and a.compensateNo=? and a.payId=? ");
        sqlJoinUtils.addParamValue("1");//是否有效
        sqlJoinUtils.addParamValue(queryVo.getBussType());//业务类型
        sqlJoinUtils.addParamValue(queryVo.getFeeCode());//费用类型
        sqlJoinUtils.addParamValue("1");//是否真增值税专票
        sqlJoinUtils.addParamValue("1");//是否验真通过
        sqlJoinUtils.addParamValue(Long.valueOf(queryVo.getBussId()));
        sqlJoinUtils.addParamValue(queryVo.getCompensateNo());
        sqlJoinUtils.addParamValue(Long.valueOf(queryVo.getPayId()));
        String sql = sqlJoinUtils.getSql();
        logger.info("计算书与发票关系查询------------------------->:"+sql);
        Object[] values = sqlJoinUtils.getParamValues();
        Page<Object[]> page = new Page<Object[]>();
        try{
            page = baseDaoService.pagedSQLQuery(sql,start,length,values);
        }catch(Exception e){
            e.printStackTrace();
        }

        List<VatQueryViewVo> resultVoList = new ArrayList<VatQueryViewVo>();
        if(page!=null && page.getResult()!=null){
        	 for(int i = 0; i<page.getResult().size(); i++ ){
             	VatQueryViewVo vo = new VatQueryViewVo();
                 Object[] obj = (Object[])page.getResult().get(i);
                 vo.setBillNo(obj[0]!=null?obj[0].toString():"");//发票号码
                 vo.setBillCode(obj[1]!=null?obj[1].toString():"");//发票代码
                 if (obj[2] != null) {
      				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      				if(obj[2] != null){
      					Date underDate=null;
 						try {
 							underDate = format.parse(obj[2].toString());
 						} catch (ParseException e) {
 							e.printStackTrace();
 						}
      					vo.setBillDate(underDate);//开票日期
      				} 
      			}
                 vo.setSaleName(obj[3]!=null?obj[3].toString():"");//销方名称
                 vo.setSaleNo(obj[4]!=null?obj[4].toString():"");//销方纳税人识别号
                 vo.setBillNnum(obj[5]!=null?new BigDecimal(obj[5].toString()):null);//发票不含税金额
                 vo.setBillSnum(obj[6]!=null?new BigDecimal(obj[6].toString()):null);//发票税额
                 vo.setBillSl(obj[7]!=null?new BigDecimal(obj[7].toString()):null);//发票税率
                 if(obj[7]!=null){
                	 vo.setBillSlName(percentChage(new BigDecimal(obj[7].toString())));//税率百分比格式
                 }
                 vo.setBillNum(obj[8]!=null?new BigDecimal(obj[8].toString()):null);//发票价税合计金额
                 vo.setRegisterNum(obj[9]!=null?new BigDecimal(obj[9].toString()):null);//该发票在计算书登记金额
                 resultVoList.add(vo);
             }
        }
       
        
        ResultPage<VatQueryViewVo> resultPage = new ResultPage<VatQueryViewVo>(start,length,page.getTotalCount(),resultVoList);
        
        return resultPage;
	}

	@Override
	public ResultPage<VatQueryViewVo> findVatPageForBillInfo(VatQueryViewVo queryVo) {
		int start=queryVo.getStart();
		int length=queryVo.getLength();
		SqlJoinUtils sqlJoinUtils = new SqlJoinUtils();
        //发票与计算书关系查询
        sqlJoinUtils.append(" select a.registNo,a.compensateNo,a.bussType,a.localFeeType,c.comCode,c.underwriteDate,"
        		+ " d.payeeName,d.accountNo,decode(a.linktableName,'1',(select p.sumRealPay from PrpLPayment p where p.id=a.linktableId ),'2',"
        		+ "(select pre.payAmt from PrpLPrePay pre where pre.id=a.linktableId ),'3',(select che.feeRealAmt from PrpLCharge che where che.id=a.linktableId ),null),"
        		+ "decode(a.linktableName,'1',(select p.regsumAmount from PrpLPayment p where p.id=a.linktableId ),'2',"
        		+ "(select pre.regsumAmount from PrpLPrePay pre where pre.id=a.linktableId ),'3',(select che.regsumAmount from PrpLCharge che where che.id=a.linktableId ),null),a.registerNum,a.id,a.payId,a.linktableId "
        		+ " from PrpLbillcont a,PrpLbillinfo b,PrpLCompensate c,PrpLPayCustom d where a.billNo=b.billNo and "
        		+ " a.billCode=b.billCode and a.status=? and b.flag=? and a.vidFlag=? and a.compensateNo=c.compensateNo and d.id=a.payId and b.billNo=? and b.billCode=? ");
        sqlJoinUtils.addParamValue("1");//是否有效
        sqlJoinUtils.addParamValue("1");//是否真增值税专票
        sqlJoinUtils.addParamValue("1");//验真通过
        sqlJoinUtils.addParamValue(queryVo.getBillNo());//发票号码
        sqlJoinUtils.addParamValue(queryVo.getBillCode());//发票代码 
        //倒序排序
        sqlJoinUtils.append(" order by c.underwriteDate desc ");
        String sql = sqlJoinUtils.getSql();
        logger.info("发票与计算书关系查询------------------------->:"+sql);
        Object[] values = sqlJoinUtils.getParamValues();
        Page<Object[]> page = new Page<Object[]>();
        try{
            page = baseDaoService.pagedSQLQuery(sql,start,length,values);
        }catch(Exception e){
            e.printStackTrace();
        }

        List<VatQueryViewVo> resultVoList = new ArrayList<VatQueryViewVo>();
        if(page!=null && page.getResult()!=null){
        	 for(int i = 0; i<page.getResult().size(); i++ ){
             	VatQueryViewVo vo = new VatQueryViewVo();
             	 vo.setIndexNo(i+1);//序号
                 Object[] obj = (Object[])page.getResult().get(i);
                 vo.setRegistNo(obj[0]!=null?obj[0].toString():"");//报案号
                 vo.setCompensateNo(obj[1]!=null?obj[1].toString():"");//计算书号
                 vo.setBussType(obj[2]!=null?obj[2].toString():"");//业务类型
                 if("1".equals(vo.getBussType())){
                	 vo.setBussName("费用");
                 }else{
                	 vo.setBussName("赔款");
                 }
                 vo.setFeeCode(obj[3]!=null?obj[3].toString():"");//费用类型
                 vo.setFeeName(CodeConstants.backFeeName(vo.getFeeCode()));//费用名称
                 vo.setComCode(obj[4]!=null?obj[4].toString():"");//归属机构
                 if(StringUtils.isNotBlank(vo.getComCode())){
                	 vo.setComName(codeTranService.transCode("ComCode",vo.getComCode())); 
                 }
                 if (obj[5] != null) {
      				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      				if(obj[5] != null){
      					Date underDate=null;
 						try {
 							underDate = format.parse(obj[5].toString());
 						} catch (ParseException e) {
 							e.printStackTrace();
 						}
      					vo.setUnderwriteDate(underDate);
      				} 
      			}
                 vo.setPayName(obj[6]!=null?obj[6].toString():"");//收款人
                 vo.setAccountNo(obj[7]!=null?obj[7].toString():"");//收款人账号
                 vo.setSumAmt(obj[8]!=null?new BigDecimal(obj[8].toString()):null);//费用金额
                 vo.setRegisterNum(obj[9]!=null?new BigDecimal(obj[9].toString()):new BigDecimal("0"));//已登记金额
                 vo.setRegisterNum1(obj[10]!=null?new BigDecimal(obj[10].toString()):new BigDecimal("0"));//该张发票上的登记金额
                 vo.setBillContId(obj[11]!=null?Long.valueOf(obj[11].toString()):null);//计算书关系表Id
                 vo.setPayId(obj[12]!=null?obj[12].toString():null);//收款人表id
                 vo.setBussId(obj[13]!=null?obj[13].toString():null);//业务表Id
                 resultVoList.add(vo);
             }
        }
       
        
        ResultPage<VatQueryViewVo> resultPage = new ResultPage<VatQueryViewVo>(start,length,page.getTotalCount(),resultVoList);
        
        return resultPage;
	}
	/**
	 * 转换为百分比
	 * @param snum
	 * @return
	 */
	private String percentChage(BigDecimal snum) {
		String strPercent="";
		if(snum==null){
			return strPercent;
		}else{
			NumberFormat percent = NumberFormat.getPercentInstance();
			percent.setMaximumFractionDigits(2);
			strPercent=percent.format(snum.doubleValue());
		}
		
		return strPercent;
	}

	@Override
	public ResultPage<VatQueryViewVo> findVatPageForBillRegister(VatQueryViewVo queryVo) {
		int start=queryVo.getStart();
		int length=queryVo.getLength();
		//权限机构
		String subComCode="";
		if(StringUtils.isNotBlank(queryVo.getComCode())){
			if(queryVo.getComCode().startsWith("0002")){
				subComCode=queryVo.getComCode().substring(0, 6);
				if("00".equals(queryVo.getComCode().substring(4, 6))){
					subComCode=queryVo.getComCode().substring(0, 4);
				}
			}else{
				subComCode=queryVo.getComCode().substring(0, 4);
				if("00".equals(queryVo.getComCode().substring(2, 4))){
					subComCode=queryVo.getComCode().substring(0, 2);
				}
			}
		}
		SqlJoinUtils sqlJoinUtils = new SqlJoinUtils();
        //发票登记查询
        sqlJoinUtils.append(" select a.billNo,a.billCode,b.billDate,a.saleName,a.saleNo,b.billNnum,b.billSnum,"
        		+ " b.billSl,b.billNum,a.vidFlag,d.registNo,d.compensateNo,a.bussType,a.feeType,a.localFeeType,"
        		+ " decode(a.linktableName,'1',(select p.sumRealPay from PrpLPayment p where p.id=a.linktableId ),'2',"
                + " (select pre.payAmt from PrpLPrePay pre where pre.id=a.linktableId ),'3',(select che.feeRealAmt from PrpLCharge che where che.id=a.linktableId ),null),"
                + " decode(a.linktableName,'1',(select p.regsumAmount from PrpLPayment p where p.id=a.linktableId ),'2',"
        		+ " (select pre.regsumAmount from PrpLPrePay pre where pre.id=a.linktableId ),'3',(select che.regsumAmount from PrpLCharge che where che.id=a.linktableId ),null),"
                + " d.comCode,d.underwriteDate,c.payeeName,c.accountNo,d.policyNo,b.sendstatus,b.registerStatus,b.backFlag,b.backCauseinfo,a.id,b.id as billId,a.payId,a.linktableId "
        		+ " from PrpLbillcont a,PrpLbillinfo b,PrpLPayCustom c,PrpLCompensate d where 1=1 and "
        		+ " a.billNo=b.billNo and a.billCode=b.billCode and a.status='1' "
        		+ " and b.flag='1' and c.id=a.payId and a.compensateNo=d.compensateNo ");
        
        //发票号码
        if(StringUtils.isNotBlank(queryVo.getBillNo())){
        	sqlJoinUtils.append(" and b.billNo=? ");
        	sqlJoinUtils.addParamValue(queryVo.getBillNo());
        }
        //发票代码
        if(StringUtils.isNotBlank(queryVo.getBillCode())){
        	sqlJoinUtils.append(" and b.billCode=? ");
        	sqlJoinUtils.addParamValue(queryVo.getBillCode());
        }
        //销方名称
        if(StringUtils.isNotBlank(queryVo.getSaleName())){
        	sqlJoinUtils.append(" and b.saleName like ? ");
        	sqlJoinUtils.addParamValue(queryVo.getSaleName()+"%");
        }
        
        //销方纳税人识别号
        if(StringUtils.isNotBlank(queryVo.getSaleNo())){
        	sqlJoinUtils.append(" and b.saleNo =? ");
        	sqlJoinUtils.addParamValue(queryVo.getSaleNo());
        }
        
        //报案号
        if(StringUtils.isNotBlank(queryVo.getRegistNo())){
        	sqlJoinUtils.append(" and d.registNo=? ");
        	sqlJoinUtils.addParamValue(queryVo.getRegistNo());
        }
        //计算书号
        if(StringUtils.isNotBlank(queryVo.getCompensateNo())){
        	sqlJoinUtils.append(" and d.compensateNo=? ");
        	sqlJoinUtils.addParamValue(queryVo.getCompensateNo());
        }
        //保单号
        if(StringUtils.isNotBlank(queryVo.getPolicyNo())){
        	sqlJoinUtils.append(" and d.policyNo=? ");
        	sqlJoinUtils.addParamValue(queryVo.getPolicyNo());
        }
        //权限机构
        if(StringUtils.isNotBlank(subComCode)){
        	sqlJoinUtils.append(" and d.comCode like ? ");
        	sqlJoinUtils.addParamValue(subComCode+"%");
        }
        //收款人
        if(StringUtils.isNotBlank(queryVo.getPayName())){
        	sqlJoinUtils.append(" and c.payeeName like ? ");
        	sqlJoinUtils.addParamValue(queryVo.getPayName()+"%");
        }
        //收款人账号
        if(StringUtils.isNotBlank(queryVo.getAccountNo())){
        	sqlJoinUtils.append(" and c.accountNo = ? ");
        	sqlJoinUtils.addParamValue(queryVo.getAccountNo());
        }
        //标的车牌号
        if(StringUtils.isNotBlank(queryVo.getLicenseNo())){
        	sqlJoinUtils.append(" and exists(select 1 from PrpLCItemCar t where t.registNo=d.registNo and t.policyNo=d.policyNo and t.licenseNo like ? )");
        	sqlJoinUtils.addParamValue(queryVo.getLicenseNo()+"%");
        }
        //被保险人
        if(StringUtils.isNotBlank(queryVo.getPolicyName())){
        	sqlJoinUtils.append(" and exists(select 1 from PrpLCMain f where f.registNo=d.registNo and f.policyNo=d.policyNo and f.insuredName = ? )");
        	sqlJoinUtils.addParamValue(queryVo.getPolicyName());
        }
        //报案时间  or 出险时间
        if(queryVo.getReportStartTime()!=null || queryVo.getReportEndTime()!=null || queryVo.getDamageStartTime()!=null || queryVo.getDamageEndTime()!=null){
        	sqlJoinUtils.append(" and exists(select 1 from PrpLRegist k where k.registNo=d.registNo ");
        	if(queryVo.getReportStartTime()!=null){
        		sqlJoinUtils.append(" and k.reportTime >= ? ");
        		sqlJoinUtils.addParamValue(queryVo.getReportStartTime());
        	}
        	if(queryVo.getReportEndTime()!=null){
        		sqlJoinUtils.append(" and k.reportTime <= ? ");
        		sqlJoinUtils.addParamValue(queryVo.getReportEndTime());
        	}
        	if(queryVo.getDamageStartTime()!=null){
        		sqlJoinUtils.append(" and k.damageTime >= ? ");
        		sqlJoinUtils.addParamValue(queryVo.getDamageStartTime());
        	}
        	if(queryVo.getDamageEndTime()!=null){
        		sqlJoinUtils.append(" and k.damageTime <= ? ");
        		sqlJoinUtils.addParamValue(queryVo.getDamageEndTime());
        	}
        	sqlJoinUtils.append(" )");
        }
        //核赔通过时间 or 结案通过时间(因为理赔系统是核赔通过就自动结案的，所以认为核赔通过时间等于结案时间)
        if(queryVo.getUnderwriteStartDate()!=null || queryVo.getUnderwriteEndDate()!=null || queryVo.getEndcaseStartTime()!=null || queryVo.getEndcaseEndTime()!=null){
        	if(queryVo.getUnderwriteStartDate()!=null){
        		sqlJoinUtils.append(" and d.underwriteDate >= ? ");
        		sqlJoinUtils.addParamValue(queryVo.getUnderwriteStartDate());
        	}
        	if(queryVo.getUnderwriteEndDate()!=null){
        		sqlJoinUtils.append(" and d.underwriteDate <= ? ");
        		sqlJoinUtils.addParamValue(queryVo.getUnderwriteEndDate());
        	}
        	if(queryVo.getEndcaseStartTime()!=null){
        		sqlJoinUtils.append(" and d.underwriteDate >= ? ");
        		sqlJoinUtils.addParamValue(queryVo.getEndcaseStartTime());
        	}
        	if(queryVo.getEndcaseEndTime()!=null){
        		sqlJoinUtils.append(" and d.underwriteDate <= ? ");
        		sqlJoinUtils.addParamValue(queryVo.getEndcaseEndTime());
        	}
        }
        //开票日期
        if(queryVo.getBillStartDate()!=null){
        	sqlJoinUtils.append(" and b.billDate >= ? ");
        	sqlJoinUtils.addParamValue(queryVo.getBillStartDate());
        }
        if(queryVo.getBillEndDate()!=null){
        	sqlJoinUtils.append(" and b.billDate <= ? ");
        	sqlJoinUtils.addParamValue(queryVo.getBillEndDate());
        }
        //验真结果
        if(StringUtils.isNotBlank(queryVo.getVidflag())){
        	sqlJoinUtils.append(" and a.vidFlag= ? ");
        	sqlJoinUtils.addParamValue(queryVo.getVidflag());
        }else{
        	sqlJoinUtils.append(" and a.vidFlag !=? ");
        	sqlJoinUtils.addParamValue("00");
        }
        //绑定状态
        if(StringUtils.isNotBlank(queryVo.getRegisterStatus())){
        	sqlJoinUtils.append(" and b.registerStatus= ? ");
        	sqlJoinUtils.addParamValue(queryVo.getRegisterStatus());
        }
        //必须核赔通过
        sqlJoinUtils.append(" and d.underwriteDate is not null ");
		sqlJoinUtils.append(" and d.underwriteFlag  in (?,?) ");
		sqlJoinUtils.addParamValue("1");//核赔通过
		sqlJoinUtils.addParamValue("3");//自核
        //倒序排序
        sqlJoinUtils.append(" order by d.underwriteDate desc ");
        String sql = sqlJoinUtils.getSql();
        logger.info("发票登记(推送)查询------------------------->:"+sql);
        Object[] values = sqlJoinUtils.getParamValues();
        Page<Object[]> page = new Page<Object[]>();
        try{
            page = baseDaoService.pagedSQLQuery(sql,start,length,values);
        }catch(Exception e){
            e.printStackTrace();
        }

        List<VatQueryViewVo> resultVoList = new ArrayList<VatQueryViewVo>();
        if(page!=null && page.getResult()!=null){
        	 for(int i = 0; i<page.getResult().size(); i++ ){
             	VatQueryViewVo vo = new VatQueryViewVo();
                 Object[] obj = (Object[])page.getResult().get(i);
                 vo.setBillNo(obj[0]!=null?obj[0].toString():"");//发票号码
                 vo.setBillCode(obj[1]!=null?obj[1].toString():"");//发票代码
                 if (obj[2] != null) {
       				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
       				if(obj[2] != null){
       					Date underDate=null;
  						try {
  							underDate = format.parse(obj[2].toString());
  						} catch (ParseException e) {
  							e.printStackTrace();
  						}
       					vo.setBillDate(underDate);//开票日期
       				} 
       			}
                 vo.setSaleName(obj[3]!=null?obj[3].toString():"");//销方名称
                 vo.setSaleNo(obj[4]!=null?obj[4].toString():"");//销方纳税人识别号
                 vo.setBillNnum(obj[5]!=null?new BigDecimal(obj[5].toString()):null);//发票不含税金额
                 vo.setBillSnum(obj[6]!=null?new BigDecimal(obj[6].toString()):null);//发票税额
                 vo.setBillSl(obj[7]!=null?new BigDecimal(obj[7].toString()):null);//税率
                 vo.setBillSlName(percentChage(vo.getBillSl()));//税率百分比
                 vo.setBillNum(obj[8]!=null?new BigDecimal(obj[8].toString()):null);//发票价税合计
                 vo.setVidflag(obj[9]!=null?obj[9].toString():"");//验真状态
                 if("0".equals(vo.getVidflag())){
                	 vo.setVidflagName("失败");
                 }else{
                	 vo.setVidflagName("成功");
                 }
                 vo.setRegistNo(obj[10]!=null?obj[10].toString():"");//报案号
                 vo.setCompensateNo(obj[11]!=null?obj[11].toString():"");//计算书号
                 vo.setBussType(obj[12]!=null?obj[12].toString():"");//赔付类型
                 vo.setBussName(CodeConstants.backBussName(vo.getBussType()));///业务名称
                 vo.setFeeCode(obj[13]!=null?obj[13].toString():"");//费用类型
                 vo.setFeeName(CodeConstants.backFeeName(obj[14]!=null?obj[14].toString():""));
                 if(StringUtils.isNotBlank(vo.getCompensateNo())){
                	 if(vo.getCompensateNo().startsWith("7")){
                		 vo.setPayeeType("实付");
                	 }else{
                		 vo.setPayeeType("预付");
                	 }
                 }
                 vo.setSumAmt(obj[15]!=null?new BigDecimal(obj[15].toString()):new BigDecimal("0"));//赔付金额
                 vo.setRegisterNum(obj[16]!=null?new BigDecimal(obj[16].toString()):new BigDecimal("0"));//已登记金额
                 vo.setComCode(obj[17]!=null?obj[17].toString():"");//归属机构
                 if(StringUtils.isNotBlank(vo.getComCode())){
                	 vo.setComName(codeTranService.transCode("ComCode",vo.getComCode()));//归属机构名称
                 }
                 if (obj[18] != null) {
        				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        				if(obj[18] != null){
        					Date underDate=null;
   						try {
   							underDate = format.parse(obj[18].toString());
   						} catch (ParseException e) {
   							e.printStackTrace();
   						}
        					vo.setUnderwriteDate(underDate);//核赔通过时间
        				} 
        			}
                 
                 vo.setPayName(obj[19]!=null?obj[19].toString():"");//收款人
                 vo.setAccountNo(obj[20]!=null?obj[20].toString():"");//收款人账号
                 vo.setPolicyNo(obj[21]!=null?obj[21].toString():"");//保单号
                 vo.setSendStatus(obj[22]!=null?obj[22].toString():"");//推送状态
                 if("0".equals(vo.getSendStatus())){
                	 vo.setSendStatusName("未推送"); 
                 }else if("1".equals(vo.getSendStatus())){
                	 vo.setSendStatusName("已推送"); 
                 }
                 vo.setRegisterStatus(obj[23]!=null?obj[23].toString():"");//绑定状态
                 vo.setBackFlag(obj[24]!=null?obj[24].toString():"");//打回状态
                 if("1".equals(vo.getBackFlag())){
                	 vo.setBackFlagName("打回(vat主动打回)");
                	 vo.setBackCauseInfo(obj[25]!=null?obj[25].toString():"");//打回原因
                 }
                 vo.setBillContId(obj[26]!=null?Long.valueOf(obj[26].toString()):null);//发票计算书关系表Id
                 vo.setBillId(obj[27]!=null?Long.valueOf(obj[27].toString()):null);//发票表Id
                 vo.setPayId(obj[28]!=null?obj[28].toString():null);//收款人id
                 vo.setBussId(obj[29]!=null?obj[29].toString():null);//业务表id
                 resultVoList.add(vo);
             }
        }
       
        
        ResultPage<VatQueryViewVo> resultPage = new ResultPage<VatQueryViewVo>(start,length,page.getTotalCount(),resultVoList);
        
		return resultPage;
	}

	@Override
	public List<VatQueryViewVo> findVatRgisterInfo(String[] idsArray) {
		SqlJoinUtils sqlJoinUtils = new SqlJoinUtils();
		if(idsArray!=null && idsArray.length>0){
			for(int i=0;i<idsArray.length;i++){
				sqlJoinUtils.append(" select b.billNo,b.billCode,b.billDate,b.saleName,b.saleNo,b.billNnum,b.billSnum,"
		        		+ " b.billSl,b.billNum,a.vidFlag,d.registNo,d.compensateNo,a.bussType,a.feeType,a.localFeeType,"
		        		+ " decode(a.linktableName,'1',(select p.sumRealPay from PrpLPayment p where p.id=a.linktableId ),'2',"
		                + " (select pre.payAmt from PrpLPrePay pre where pre.id=a.linktableId ),'3',(select che.feeRealAmt from PrpLCharge che where che.id=a.linktableId ),null),"
		                + " decode(a.linktableName,'1',(select p.regsumAmount from PrpLPayment p where p.id=a.linktableId ),'2',"
		        		+ " (select pre.regsumAmount from PrpLPrePay pre where pre.id=a.linktableId ),'3',(select che.regsumAmount from PrpLCharge che where che.id=a.linktableId ),null),"
		                + " d.comCode,d.underwriteDate,c.payeeName,c.accountNo,d.policyNo,b.sendstatus,b.registerStatus,a.id,b.id as billId,a.linktableId,a.linktableName "
		        		+ " from PrpLbillcont a,PrpLbillinfo b,PrpLPayCustom c,PrpLCompensate d where 1=1 and "
		        		+ " a.billNo=b.billNo and a.billCode=b.billCode and a.status='1' and a.vidFlag ='1' and  b.registerStatus='0' "
		        		+ " and b.flag='1' and c.id=a.payId and a.compensateNo=d.compensateNo and a.id=? and b.id=? ");
				
				if(i!=idsArray.length-1){
					sqlJoinUtils.append(" union all ");	
				}
				String[] ids=idsArray[i].split("_");
				if(ids!=null && ids.length>0){
					for(int j=0;j<ids.length;j++){
						sqlJoinUtils.addParamValue(Long.valueOf(ids[j]));
					}
				}
			}
		}
		//查询sql
		String sql=sqlJoinUtils.getSql();
		logger.info("发票登记查询------------------------->:"+sql);
		Object[] values = sqlJoinUtils.getParamValues();
		List<Object[]> objects = baseDaoService.getAllBySql(sql, values);
		List<VatQueryViewVo> vatQueryViewVoList=new ArrayList<VatQueryViewVo>();
		if(objects!=null && objects.size()>0){
			for(int i=0;i<objects.size();i++){
				VatQueryViewVo vo=new VatQueryViewVo();
			    Object[] obj=objects.get(i);
			    vo.setIndexNo(i+1);//序号
			    vo.setBillNo(obj[0]!=null?obj[0].toString():"");//发票号码
                vo.setBillCode(obj[1]!=null?obj[1].toString():"");//发票代码
                if (obj[2] != null) {
      				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      				if(obj[2] != null){
      					Date underDate=null;
 						try {
 							underDate = format.parse(obj[2].toString());
 						} catch (ParseException e) {
 							e.printStackTrace();
 						}
      					vo.setBillDate(underDate);//开票日期
      				} 
      			}
                vo.setSaleName(obj[3]!=null?obj[3].toString():"");//销方名称
                vo.setSaleNo(obj[4]!=null?obj[4].toString():"");//销方纳税人识别号
                vo.setBillNnum(obj[5]!=null?new BigDecimal(obj[5].toString()):null);//发票不含税金额
                vo.setBillSnum(obj[6]!=null?new BigDecimal(obj[6].toString()):null);//发票税额
                vo.setBillSl(obj[7]!=null?new BigDecimal(obj[7].toString()):null);//税率
                vo.setBillSlName(percentChage(vo.getBillSl()));//税率百分比
                vo.setBillNum(obj[8]!=null?new BigDecimal(obj[8].toString()):null);//发票价税合计
                vo.setVidflag(obj[9]!=null?obj[9].toString():"");//验真状态
                if("0".equals(vo.getVidflag())){
               	 vo.setVidflagName("失败");
                }else{
               	 vo.setVidflagName("成功");
                }
                vo.setRegistNo(obj[10]!=null?obj[10].toString():"");//报案号
                vo.setCompensateNo(obj[11]!=null?obj[11].toString():"");//计算书号
                vo.setBussType(obj[12]!=null?obj[12].toString():"");//赔付类型
                vo.setBussName(CodeConstants.backBussName(vo.getBussType()));///业务名称
                vo.setFeeCode(obj[13]!=null?obj[13].toString():"");//费用类型
                vo.setFeeName(CodeConstants.backFeeName(obj[14]!=null?obj[14].toString():""));
                if(StringUtils.isNotBlank(vo.getCompensateNo())){
               	 if(vo.getCompensateNo().startsWith("7")){
               		 vo.setPayeeType("实付");
               	 }else{
               		 vo.setPayeeType("预付");
               	 }
                }
                vo.setSumAmt(obj[15]!=null?new BigDecimal(obj[15].toString()):new BigDecimal("0"));//赔付金额
                vo.setRegisterNum(obj[16]!=null?new BigDecimal(obj[16].toString()):new BigDecimal("0"));//已登记金额
                vo.setComCode(obj[17]!=null?obj[17].toString():"");//归属机构
                if(StringUtils.isNotBlank(vo.getComCode())){
               	 vo.setComName(codeTranService.transCode("ComCode",vo.getComCode()));//归属机构名称
                }
                if (obj[18] != null) {
       				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
       				if(obj[18] != null){
       					Date underDate=null;
  						try {
  							underDate = format.parse(obj[18].toString());
  						} catch (ParseException e) {
  							e.printStackTrace();
  						}
       					vo.setUnderwriteDate(underDate);//核赔通过时间
       				} 
       			}
                
                vo.setPayName(obj[19]!=null?obj[19].toString():"");//收款人
                vo.setAccountNo(obj[20]!=null?obj[20].toString():"");//收款人账号
                vo.setPolicyNo(obj[21]!=null?obj[21].toString():"");//保单号
                vo.setSendStatus(obj[22]!=null?obj[22].toString():"");//推送状态
                if("0".equals(vo.getSendStatus())){
               	 vo.setSendStatusName("未推送"); 
                }else if("1".equals(vo.getSendStatus())){
               	 vo.setSendStatusName("已推送"); 
                }
                vo.setRegisterStatus(obj[23]!=null?obj[23].toString():"");//绑定状态
                vo.setBillContId(obj[24]!=null?Long.valueOf(obj[24].toString()):null);//发票计算书关系表Id
                vo.setBillId(obj[25]!=null?Long.valueOf(obj[25].toString()):null);//发票表Id
                vo.setBussId(obj[26]!=null?obj[26].toString():null);
                vo.setBusstableType(obj[27]!=null?obj[27].toString():null);
                vatQueryViewVoList.add(vo);
			}
		}
		return vatQueryViewVoList;
	}

	@Override
	public List<Map<String, Object>> createExcelRecord(List<VatQueryViewVo> vatQueryViewVos) {
		List<Map<String, Object>> listmap = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sheetName", "sheet1");
		listmap.add(map);
		if(vatQueryViewVos!=null && vatQueryViewVos.size()>0){
			for (VatQueryViewVo resultVo : vatQueryViewVos) {
				Map<String, Object> mapValue = new HashMap<String, Object>();
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				mapValue.put("indexNo", resultVo.getIndexNo());
				mapValue.put("billNo", resultVo.getBillNo());
				mapValue.put("billCode", resultVo.getBillCode());
				mapValue.put("billDate",df.format(resultVo.getBillDate()));
				mapValue.put("saleName", resultVo.getSaleName());
				mapValue.put("saleNo", resultVo.getSaleNo());
				mapValue.put("billNnum", resultVo.getBillNnum()!=null?resultVo.getBillNnum().toString():null);
				mapValue.put("billSnum", resultVo.getBillSnum()!=null?resultVo.getBillSnum().toString():null);
				mapValue.put("billSl", resultVo.getBillSlName());
				mapValue.put("billNum", resultVo.getBillNum()!=null?resultVo.getBillNum().toString():null);
				mapValue.put("registerNum",null);
				mapValue.put("registNo", resultVo.getRegistNo());
				mapValue.put("compensateNo", resultVo.getCompensateNo());
				mapValue.put("bussName", resultVo.getBussName());
				mapValue.put("feeName", resultVo.getFeeName());
				mapValue.put("sumAmt", resultVo.getSumAmt()!=null?resultVo.getSumAmt().toString():null);
				mapValue.put("comName", resultVo.getComName());
				mapValue.put("underwriteDate",df.format(resultVo.getUnderwriteDate()));
				mapValue.put("payName",resultVo.getPayName());
				mapValue.put("accountNo",resultVo.getAccountNo());
				mapValue.put("billContId",resultVo.getBillContId()!=null?resultVo.getBillContId().toString():null);
				mapValue.put("billId",resultVo.getBillId()!=null?resultVo.getBillId().toString():null);
				mapValue.put("bussId",resultVo.getBussId()!=null?resultVo.getBussId().toString():null);
				mapValue.put("busstableType",resultVo.getBusstableType());
				mapValue.put("comCode", resultVo.getComCode());
				listmap.add(mapValue);
			}
		}
		
		return listmap;
	}

	@Override
	public String applyBillTask(List<List<Object>> objects, SysUserVo userVo)throws Exception {
		String usercomCode=userVo.getComCode();//登录人机构
		//装配发票表Id
		Set<String> billIdSet=new HashSet<String>();
		//记录发票的总登记金额
		Map<String,BigDecimal> billNumMap=new HashMap<String,BigDecimal>();
		//装配发票与计算书关系表Id
		Set<String> billContIdSet=new HashSet<String>();
		//装配业务表Id
		Set<String> bussIdSet=new HashSet<String>();
		//记录每个费用类型的总登记金额
		Map<String,BigDecimal> feeNumMap=new HashMap<String,BigDecimal>();
		//记录计算书号，费用类型，收款人每个登记金额
		Map<String,BigDecimal> billcontNumMap=new HashMap<String,BigDecimal>();
		//记录计算书号，费用类型，收款人与发票id对应信息
		Map<String,String> billcontInfoMap=new HashMap<String,String>();
		//记录发票号码与id的关系
		Map<String,String> billInfoMap=new HashMap<String,String>();
		//记录发票价税合计金额与发票id的关系
	    Map<String,BigDecimal> billsumMap=new HashMap<String,BigDecimal>();
		// 解析读取出来的数据
		if(objects!=null && objects.size()>0){
			for (int i = 0; i < objects.size(); i++) {
				List<Object> object = objects.get(i);
				String billNo = object.get(1)!=null?object.get(1).toString():null;//发票号码
				String billCode = object.get(2)!=null?object.get(2).toString():null;//发票代码
				String billNum = object.get(9)!=null?object.get(9).toString():null;//价税合计金额
				String registerNum = object.get(10)!=null?object.get(10).toString():null;//登记金额
				String billContId= object.get(20)!=null?object.get(20).toString():null;//发票与计算书关系表id
				String billId= object.get(21)!=null?object.get(21).toString():null;//发票id
				String bussId= object.get(22)!=null?object.get(22).toString():null;//业务表Id
				String busstableType= object.get(23)!=null?object.get(23).toString():null;//业务表类型
				String comCode=object.get(24)!=null?object.get(24).toString():null;//机构代码
				String feeName=object.get(14)!=null?object.get(14).toString():null;//费用名称
				String payName=object.get(18)!=null?object.get(18).toString():null;//收款人
				String compensateNo=object.get(12)!=null?object.get(12).toString():null;//计算书号
				if(StringUtils.isBlank(billNo) || StringUtils.isBlank(billCode)){
					throw new RuntimeException("第" + (i + 2)
							+ "行数据校验不通过，发票代码或发票号码不能为空！");
				}
				if(StringUtils.isNotBlank(registerNum)){
					String numFlag="0";//当为1时，说明是是负数
					try{
						if((new BigDecimal(registerNum)).compareTo( new BigDecimal("0"))<=0){
							numFlag="1";
						}
					}catch(Exception e){
						throw new RuntimeException("第" + (i + 2)
								+ "的登记金额必须为数值型;发票号码：" + billNo);
					}
					if("1".equals(numFlag)){
						throw new RuntimeException("第" + (i + 2)
								+ "的登记金额必须大于0;发票号码：" + billNo);
					}
					
				}
				
				if(StringUtils.isBlank(feeName) || StringUtils.isBlank(payName) || StringUtils.isBlank(compensateNo) ){
					throw new RuntimeException("第" + (i + 2)
							+ "行数据校验不通过，费用类型或收款人或计算书号不能为空！;发票号码：" + billNo);
				}
				if(StringUtils.isBlank(billContId) || StringUtils.isBlank(bussId) || StringUtils.isBlank(busstableType) || StringUtils.isBlank(comCode)){
					throw new RuntimeException("第" + (i + 2)
							+ "行数据校验不通过，隐藏域数据改变，请不要修改隐藏域;发票号码：" + billNo);
				}
				if(usercomCode.startsWith("0002") && "00".equals(usercomCode.substring(4, 6))){
					if(!comCode.startsWith("0002")){
						throw new RuntimeException("第" + (i + 2)
								+ "行数据校验不通过，案件机构权限超出登录人的机构权限;发票号码：" + billNo);
					}
				}else if(usercomCode.startsWith("0002") && !"00".equals(usercomCode.substring(4, 6))){
					if(!usercomCode.substring(0,6).equals(comCode.substring(0,6))){
						throw new RuntimeException("第" + (i + 2)
								+ "行数据校验不通过，案件机构权限超出登录人的机构权限;发票号码：" + billNo);
					}
				}else if(!usercomCode.startsWith("0002") && "00".equals( usercomCode.substring(2,4))){
					if(!comCode.substring(0,2).equals(usercomCode.substring(0, 2))){
						throw new RuntimeException("第" + (i + 2)
								+ "行数据校验不通过，案件机构权限超出登录人的机构权限;发票号码：" + billNo);
					}
				}else if(!usercomCode.startsWith("0002") && !"00".equals( usercomCode.substring(2,4))){
					if(!comCode.substring(0,4).equals(usercomCode.substring(0, 4))){
						throw new RuntimeException("第" + (i + 2)
								+ "行数据校验不通过，案件机构权限超出登录人的机构权限;发票号码：" + billNo);
					}
				}
				//发票与计算书关系表
			    PrpLbillcontVo prpLbillcontVo=this.findPrpLbillcontById(Long.valueOf(billContId));
			    if(prpLbillcontVo==null){
			    	throw new RuntimeException("第" + (i + 2)
							+ "行数据校验不通过，隐藏域(发票与计算书关系表Id)在数据库中不存在，请不要修改隐藏域;发票号码：" + billNo);
			    }else{
			    	if(!billNo.equals(prpLbillcontVo.getBillNo()) || !billCode.equals(prpLbillcontVo.getBillCode())){
			    		throw new RuntimeException("第" + (i + 2)
								+ "行数据校验不通过， 发票号码或发票代码与数据库(发票与计算书关系表)记录不匹配;发票号码：" + billNo);
			    	}
			    	if("0".equals(prpLbillcontVo.getStatus())){
			    		throw new RuntimeException("第" + (i + 2)
								+ "行数据校验不通过,该条记录对应的数据库记录已被置为无效，请重新导出数据操作;发票号码：" + billNo);
			    	}
			    	if(!"1".equals(prpLbillcontVo.getVidFlag())){
			    		throw new RuntimeException("第" + (i + 2)
								+ "行数据校验不通过,该条记录对应的数据库记录为验真不通过记录,验真失败记录不允许操作;发票号码：" + billNo);
			    	}
			    }
			    //发票信息记录
			    PrpLbillinfoVo prpLbillinfoVo=this.findPrpLbillinfoById(Long.valueOf(billId));
			    if(prpLbillinfoVo==null){
			    	throw new RuntimeException("第" + (i + 2)
							+ "行数据校验不通过，隐藏域(发票表Id)在数据库中不存在，请不要修改隐藏域;发票号码：" + billNo);
			    }else{
			    	if(!billNo.equals(prpLbillinfoVo.getBillNo()) || !billCode.equals(prpLbillinfoVo.getBillCode())){
			    		throw new RuntimeException("第" + (i + 2)
								+ "行数据校验不通过， 发票号码或发票代码与数据库(发票表)记录不匹配;发票号码：" + billNo);
			    	}
			    }
			    
			    try{
		    	   //将每条登记金额记录下来
				   if(billContIdSet.add(billContId.toString())){
					   if(StringUtils.isNotBlank(registerNum)){
						   billcontNumMap.put(billContId.toString(),new BigDecimal(registerNum));
						   
					   }
				   }
				   
				   //将同一个计算书下的同一个费用类型的同一个收款人的登记金额累加起来
				   if(bussIdSet.add(busstableType+"_"+bussId.toString())){
					   if(StringUtils.isNotBlank(registerNum)){
						 billcontInfoMap.put(busstableType+"_"+bussId.toString(),"计算书号:"+compensateNo+","+"费用类型:"+feeName+","+"收款人:"+payName);
						 feeNumMap.put(busstableType+"_"+bussId.toString(),new BigDecimal(registerNum));
					   }
				   }else{
					   if(StringUtils.isNotBlank(registerNum)){
						   BigDecimal reginum=feeNumMap.get(busstableType+"_"+bussId.toString());
						   feeNumMap.put(busstableType+"_"+bussId.toString(),reginum.add(new BigDecimal(registerNum)));
					   }
					   
					}
				   
				   //将每张发票的金额累加起来
				   if(billIdSet.add(billId.toString())){
					   billsumMap.put(billId.toString(),new BigDecimal(billNum));
					   if(StringUtils.isNotBlank(registerNum)){
						   billInfoMap.put(billId.toString(), billNo);
						   billNumMap.put(billId.toString(),new BigDecimal(registerNum));
					   }
				   }else{
					   if(StringUtils.isNotBlank(registerNum)){
						   BigDecimal billAmount=billNumMap.get(billId.toString());
						   billNumMap.put(billId.toString(), billAmount.add(new BigDecimal(registerNum)));
					   }
				   }
				   
			    }catch(Exception e){
						throw new RuntimeException("第" + (i + 2)
								+ "行数据校验不通过，登记金额只能填入数值;发票号码：" + billNo);
			    }

				
			}
		}
		
		//发票金额验证
		if(billNumMap!=null && billNumMap.size()>0){
			for(Map.Entry<String,BigDecimal> billfeeNum:billNumMap.entrySet()){
				PrpLbillinfo prpLbillinfo=databaseDao.findByPK(PrpLbillinfo.class,Long.valueOf(billfeeNum.getKey()));
				if("1".equals(prpLbillinfo.getRegisterStatus())){
					throw new RuntimeException("由于长时间未操作，发票号码:"+billInfoMap.get(billfeeNum.getKey())
							+ "已被其他人登记! 请重新导出数据再登记！");
				}
				BigDecimal billsnum=billsumMap.get(billfeeNum.getKey());//价税合计金额
				if(DataUtils.NullToZero(billsnum).compareTo(DataUtils.NullToZero(billfeeNum.getValue()))!=0){
					throw new RuntimeException("发票号码:"+billInfoMap.get(billfeeNum.getKey())
							+ "发票的价税合计金额必须等于登记金额! ");
				}else{
					prpLbillinfo.setRegisterStatus("1");//更新发票登记状态
				}
				
			}
		}
		
			if(feeNumMap!=null && feeNumMap.size()>0){
				for(Map.Entry<String,BigDecimal> feeNum:feeNumMap.entrySet()){
					if(feeNum.getKey().startsWith("1_")){//实付赔款
						String[] a=feeNum.getKey().split("_");
						PrpLPayment prpLPayment=databaseDao.findByPK(PrpLPayment.class, Long.valueOf(a[1]));
						if((DataUtils.NullToZero(feeNum.getValue()).add(DataUtils.NullToZero(prpLPayment.getRegsumAmount()))).compareTo(DataUtils.NullToZero(prpLPayment.getSumRealPay()))==1){
							throw new RuntimeException(billcontInfoMap.get(feeNum.getKey())
									+ ",登记总金额不能大于总赔付金额! ");
						}else{
							prpLPayment.setRegsumAmount((DataUtils.NullToZero(feeNum.getValue()).add(DataUtils.NullToZero(prpLPayment.getRegsumAmount()))));//更新业务表的登记金额
						}
						
					}else if(feeNum.getKey().startsWith("2_")){//预付表
						String[] a=feeNum.getKey().split("_");
						PrpLPrePay prpLPrePay=databaseDao.findByPK(PrpLPrePay.class, Long.valueOf(a[1]));
						if((DataUtils.NullToZero(feeNum.getValue()).add(DataUtils.NullToZero(prpLPrePay.getRegsumAmount()))).compareTo(DataUtils.NullToZero(prpLPrePay.getPayAmt()))==1){
							throw new RuntimeException(billcontInfoMap.get(feeNum.getKey())
									+ ",登记总金额不能大于总赔付金额! ");
						}else{
							prpLPrePay.setRegsumAmount((DataUtils.NullToZero(feeNum.getValue()).add(DataUtils.NullToZero(prpLPrePay.getRegsumAmount()))));//更新业务表的登记金额
						}
						
					}else if(feeNum.getKey().startsWith("3_")){
						String[] a=feeNum.getKey().split("_");
						PrpLCharge prpLCharge=databaseDao.findByPK(PrpLCharge.class, Long.valueOf(a[1]));
						if((DataUtils.NullToZero(feeNum.getValue()).add(DataUtils.NullToZero(prpLCharge.getRegsumAmount()))).compareTo(DataUtils.NullToZero(prpLCharge.getFeeRealAmt()))==1){
							throw new RuntimeException(billcontInfoMap.get(feeNum.getKey())
									+ ",登记总金额不能大于总赔付金额! ");
						}else{
							prpLCharge.setRegsumAmount((DataUtils.NullToZero(feeNum.getValue()).add(DataUtils.NullToZero(prpLCharge.getRegsumAmount()))));//更新业务表的登记金额
						}
					}
				}
			}
			if(billcontNumMap!=null && billcontNumMap.size()>0){
				for(Map.Entry<String,BigDecimal> billContRegiNum:billcontNumMap.entrySet()){
					PrpLbillcont prpLbillcont=databaseDao.findByPK(PrpLbillcont.class, Long.valueOf(billContRegiNum.getKey()));
					prpLbillcont.setRegisterNum(DataUtils.NullToZero(billContRegiNum.getValue()));//更新发票与计算书关系表的登记金额
				}
			}
			
			
		 String	succeedFlag="1";//程序走到这里说明，本次操作成功
	     
		return succeedFlag;
	}

	@Override
	public PrpLbillcontVo findPrpLbillcontById(Long id) {
		QueryRule qr = QueryRule.getInstance();
		qr.addEqual("id",id);
		PrpLbillcontVo prpLbillcontVo = null;
		PrpLbillcont prpLbillcont = databaseDao.findUnique(PrpLbillcont.class, qr);
		if (prpLbillcont != null) {
			prpLbillcontVo =new PrpLbillcontVo();
			prpLbillcontVo = Beans.copyDepth().from(prpLbillcont).to(PrpLbillcontVo.class);
		}
		return prpLbillcontVo;
	}

	@Override
	public PrpLbillinfoVo findPrpLbillinfoById(Long id) {
		QueryRule qr = QueryRule.getInstance();
		qr.addEqual("id",id);
		PrpLbillinfoVo prpLbillinfoVo = null;
		PrpLbillinfo prpLbillinfo = databaseDao.findUnique(PrpLbillinfo.class, qr);
		if (prpLbillinfo != null) {
			prpLbillinfoVo=new PrpLbillinfoVo();
			prpLbillinfoVo = Beans.copyDepth().from(prpLbillinfo).to(PrpLbillinfoVo.class);
		}
		return prpLbillinfoVo;
	}

	@Override
	public PrpLPrePayVo findPrpLPrePayById(Long id) {
		QueryRule qr = QueryRule.getInstance();
		qr.addEqual("id",id);
		PrpLPrePayVo prpLPrePayVo = null;
		PrpLPrePay prpLPrePay = databaseDao.findUnique(PrpLPrePay.class, qr);
		if (prpLPrePay != null) {
			prpLPrePayVo=new PrpLPrePayVo();
			prpLPrePayVo = Beans.copyDepth().from(prpLPrePay).to(PrpLPrePayVo.class);
		}
		return prpLPrePayVo;
	}

	@Override
	public PrpLPaymentVo findPrpLPaymentById(Long id) {
		QueryRule qr = QueryRule.getInstance();
		qr.addEqual("id",id);
		PrpLPaymentVo prpLPaymentVo = null;
		PrpLPayment prpLPayment = databaseDao.findUnique(PrpLPayment.class, qr);
		if (prpLPayment != null) {
			prpLPaymentVo=new PrpLPaymentVo();
			prpLPaymentVo = Beans.copyDepth().from(prpLPayment).to(PrpLPaymentVo.class);
		}
		return prpLPaymentVo;
	}

	@Override
	public PrpLChargeVo findPrpLChargeById(Long id) {
		QueryRule qr = QueryRule.getInstance();
		qr.addEqual("id",id);
		PrpLChargeVo prpLChargeVo = null;
		PrpLCharge prpLCharge = databaseDao.findUnique(PrpLCharge.class, qr);
		if (prpLCharge != null) {
			prpLChargeVo=new PrpLChargeVo();
			prpLChargeVo = Beans.copyDepth().from(prpLCharge).to(PrpLChargeVo.class);
		}
		return prpLChargeVo;
	}

	@Override
	public List<PrpLbillcontVo> findPrpLbillcontByBillNoAndBillCode(String billNo, String BillCode) {
		QueryRule qr = QueryRule.getInstance();
		qr.addEqual("billNo",billNo);
		qr.addEqual("billCode",BillCode);
		qr.addEqual("status","1");
		qr.addEqual("vidFlag","1");
		List<PrpLbillcontVo> prpLbillcontVoList=new ArrayList<PrpLbillcontVo>();
		List<PrpLbillcont> prpLbillcontList=databaseDao.findAll(PrpLbillcont.class, qr);
		if (prpLbillcontList != null && prpLbillcontList.size()>0) {
			for(PrpLbillcont prpLbillcont:prpLbillcontList){
				if(DataUtils.NullToZero(prpLbillcont.getRegisterNum()).compareTo(new BigDecimal(0))==1){
					PrpLbillcontVo PrpLbillcontVo=new PrpLbillcontVo();
					Beans.copy().from(prpLbillcont).to(PrpLbillcontVo);
					prpLbillcontVoList.add(PrpLbillcontVo);
				}
			}
		}
		return prpLbillcontVoList;
	}

	@Override
	public ResultPage<VatQueryViewVo> findVatPageForBillSend(VatQueryViewVo queryVo) {
		int start=queryVo.getStart();
		int length=queryVo.getLength();
		//权限机构
		String subComCode="";
		if(StringUtils.isNotBlank(queryVo.getComCode())){
			if(queryVo.getComCode().startsWith("0002")){
				subComCode=queryVo.getComCode().substring(0, 6);
				if("00".equals(queryVo.getComCode().substring(4, 6))){
					subComCode=queryVo.getComCode().substring(0, 4);
				}
			}else{
				subComCode=queryVo.getComCode().substring(0, 4);
				if("00".equals(queryVo.getComCode().substring(2, 4))){
					subComCode=queryVo.getComCode().substring(0, 2);
				}
			}
		}
		SqlJoinUtils sqlJoinUtils = new SqlJoinUtils();
        //发票(推送)查询
        sqlJoinUtils.append(" select a.billNo,a.billCode,a.billDate,a.saleName,a.saleNo,a.billNnum,a.billSnum,a.billSl,"
        		            + "a.billNum,a.sendstatus,a.id,a.backFlag,a.backCauseinfo from PrpLbillinfo a where 1=1 ");
        	
        sqlJoinUtils.append(" and a.flag='1' and a.registerStatus='1' ");
        
        //发票号码
        if(StringUtils.isNotBlank(queryVo.getBillNo())){
        	sqlJoinUtils.append(" and a.billNo=? ");
        	sqlJoinUtils.addParamValue(queryVo.getBillNo());
        }
        //发票代码
        if(StringUtils.isNotBlank(queryVo.getBillCode())){
        	sqlJoinUtils.append(" and a.billCode=? ");
        	sqlJoinUtils.addParamValue(queryVo.getBillCode());
        }
        //销方名称
        if(StringUtils.isNotBlank(queryVo.getSaleName())){
        	sqlJoinUtils.append(" and a.saleName like ? ");
        	sqlJoinUtils.addParamValue(queryVo.getSaleName()+"%");
        }
        
        //销方纳税人识别号
        if(StringUtils.isNotBlank(queryVo.getSaleNo())){
        	sqlJoinUtils.append(" and a.saleNo =? ");
        	sqlJoinUtils.addParamValue(queryVo.getSaleNo());
        }
        
        sqlJoinUtils.append(" and exists(select 1 from PrpLbillcont b,PrpLPayCustom d where b.billNo=a.billNo "
        		+ " and a.billCode=b.billCode and b.status='1' and b.registerNum>0 and b.vidFlag='1' and b.payId=d.id ");
        
        //报案号
        if(StringUtils.isNotBlank(queryVo.getRegistNo())){
        	sqlJoinUtils.append(" and b.registNo=? ");
        	sqlJoinUtils.addParamValue(queryVo.getRegistNo());
        }
        //计算书号
        if(StringUtils.isNotBlank(queryVo.getCompensateNo())){
        	sqlJoinUtils.append(" and b.compensateNo=? ");
        	sqlJoinUtils.addParamValue(queryVo.getCompensateNo());
        }
        //保单号
        if(StringUtils.isNotBlank(queryVo.getPolicyNo())){
        	sqlJoinUtils.append(" and b.policyNo=? ");
        	sqlJoinUtils.addParamValue(queryVo.getPolicyNo());
        }
        //权限机构
        if(StringUtils.isNotBlank(subComCode)){
        	sqlJoinUtils.append(" and b.comCode like ? ");
        	sqlJoinUtils.addParamValue(subComCode+"%");
        }
        //收款人
        if(StringUtils.isNotBlank(queryVo.getPayName())){
        	sqlJoinUtils.append(" and d.payeeName like ? ");
        	sqlJoinUtils.addParamValue(queryVo.getPayName()+"%");
        }
        //收款人账号
        if(StringUtils.isNotBlank(queryVo.getAccountNo())){
        	sqlJoinUtils.append(" and d.accountNo = ? ");
        	sqlJoinUtils.addParamValue(queryVo.getAccountNo());
        }
        sqlJoinUtils.append(" ) ");
       //标的车牌号
        if(StringUtils.isNotBlank(queryVo.getLicenseNo())){
        	 sqlJoinUtils.append(" and exists(select 1 from PrpLbillcont b,PrpLCItemCar t where b.billNo=a.billNo "
             		           + " and a.billCode=b.billCode and b.registNo=t.registNo and b.policyNo=t.policyNo and b.status='1' and b.registerNum>0 and b.vidFlag='1' and t.licenseNo like ? )");
             sqlJoinUtils.addParamValue(queryVo.getLicenseNo()+"%");
        }
        //被保险人
        if(StringUtils.isNotBlank(queryVo.getPolicyName())){
        	 sqlJoinUtils.append(" and  exists(select 1 from PrpLbillcont b,PrpLCMain f where b.billNo=a.billNo "
   		           + " and a.billCode=b.billCode and b.registNo=f.registNo and b.policyNo=f.policyNo and b.status='1' and b.registerNum>0 and b.vidFlag='1' and f.insuredName like ? )");
             sqlJoinUtils.addParamValue(queryVo.getPolicyName()+"%");
        }
        //开票日期
        if(queryVo.getBillStartDate()!=null){
        	sqlJoinUtils.append(" and a.billDate >= ? ");
        	sqlJoinUtils.addParamValue(queryVo.getBillStartDate());
        }
        if(queryVo.getBillEndDate()!=null){
        	sqlJoinUtils.append(" and a.billDate <= ? ");
        	sqlJoinUtils.addParamValue(queryVo.getBillEndDate());
        }
        //倒序排序
        sqlJoinUtils.append(" order by a.billDate desc ");
        String sql = sqlJoinUtils.getSql();
        logger.info("发票推送查询------------------------->:"+sql);
        Object[] values = sqlJoinUtils.getParamValues();
        Page<Object[]> page = new Page<Object[]>();
        try{
            page = baseDaoService.pagedSQLQuery(sql,start,length,values);
        }catch(Exception e){
            e.printStackTrace();
        }

        List<VatQueryViewVo> resultVoList = new ArrayList<VatQueryViewVo>();
        if(page!=null && page.getResult()!=null){
        	 for(int i = 0; i<page.getResult().size(); i++ ){
             	VatQueryViewVo vo = new VatQueryViewVo();
             	Object[] obj = (Object[])page.getResult().get(i);
                 vo.setBillNo(obj[0]!=null?obj[0].toString():"");//发票号码
                 vo.setBillCode(obj[1]!=null?obj[1].toString():"");//发票代码
                 if (obj[2] != null) {
       				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
       				if(obj[2] != null){
       					Date underDate=null;
  						try {
  							underDate = format.parse(obj[2].toString());
  						} catch (ParseException e) {
  							e.printStackTrace();
  						}
       					vo.setBillDate(underDate);//开票日期
       				} 
       			}
                 vo.setSaleName(obj[3]!=null?obj[3].toString():"");//销方名称
                 vo.setSaleNo(obj[4]!=null?obj[4].toString():"");//销方纳税人识别号
                 vo.setBillNnum(obj[5]!=null?new BigDecimal(obj[5].toString()):null);//发票不含税金额
                 vo.setBillSnum(obj[6]!=null?new BigDecimal(obj[6].toString()):null);//发票税额
                 vo.setBillSl(obj[7]!=null?new BigDecimal(obj[7].toString()):null);//税率
                 vo.setBillSlName(percentChage(vo.getBillSl()));//税率百分比
                 vo.setBillNum(obj[8]!=null?new BigDecimal(obj[8].toString()):null);//发票价税合计
                 vo.setSendStatus(obj[9]!=null?obj[9].toString():"");//推送状态
                 if("0".equals(vo.getSendStatus())){
                	 vo.setSendStatusName("未推送"); 
                 }else if("1".equals(vo.getSendStatus())){
                	 vo.setSendStatusName("已推送"); 
                 }
                 vo.setBillId(obj[10]!=null?Long.valueOf(obj[10].toString()):null);//发票id
                 vo.setBackFlag(obj[11]!=null?obj[11].toString():"");//打回状态
                 if("1".equals(vo.getBackFlag())){
                	 vo.setBackCauseInfo(obj[12]!=null?obj[12].toString():"");//打回原因
                 }else{
                	 vo.setBackCauseInfo("  ");
                 }
                 resultVoList.add(vo);
	}
   }	 
             ResultPage<VatQueryViewVo> resultPage = new ResultPage<VatQueryViewVo>(start,length,page.getTotalCount(),resultVoList);
             
     		return resultPage;
     }

	@Override
	public void updatePrpLPrePay(Long id, BigDecimal registernum,String bindFlag) {
		QueryRule qr = QueryRule.getInstance();
		qr.addEqual("id",id);
		PrpLPrePay prpLPrePay = databaseDao.findUnique(PrpLPrePay.class, qr);
		if(prpLPrePay!=null){
			if("1".equals(bindFlag)){
				prpLPrePay.setRegsumAmount(DataUtils.NullToZero(prpLPrePay.getRegsumAmount()).add(DataUtils.NullToZero(registernum)));
			}else{
				prpLPrePay.setRegsumAmount(DataUtils.NullToZero(prpLPrePay.getRegsumAmount()).subtract(DataUtils.NullToZero(registernum)));
			}
			
		}
	}

	@Override
	public void updatePrpLPayment(Long id, BigDecimal registernum,String bindFlag) {
		QueryRule qr = QueryRule.getInstance();
		qr.addEqual("id",id);
		PrpLPayment prpLPayment = databaseDao.findUnique(PrpLPayment.class, qr);
		if(prpLPayment!=null){
			if("1".equals(bindFlag)){
				prpLPayment.setRegsumAmount(DataUtils.NullToZero(prpLPayment.getRegsumAmount()).add(DataUtils.NullToZero(registernum)));
			}else{
				prpLPayment.setRegsumAmount(DataUtils.NullToZero(prpLPayment.getRegsumAmount()).subtract(DataUtils.NullToZero(registernum)));
			}
			
		}
		
	}

	@Override
	public void updatePrpLCharge(Long id, BigDecimal registernum,String bindFlag) {
		QueryRule qr = QueryRule.getInstance();
		qr.addEqual("id",id);
		PrpLCharge prpLCharge = databaseDao.findUnique(PrpLCharge.class, qr);
		if(prpLCharge!=null){
			if("1".equals(bindFlag)){
				prpLCharge.setRegsumAmount(DataUtils.NullToZero(prpLCharge.getRegsumAmount()).add(DataUtils.NullToZero(registernum)));
			}else{
				prpLCharge.setRegsumAmount(DataUtils.NullToZero(prpLCharge.getRegsumAmount()).subtract(DataUtils.NullToZero(registernum)));
			}
			
		}
		
		
	}

	@Override
	public void saveOrUpdatePrplAcbillcont(PrplAcbillcontVo prplAcbillcontVo) {
		if(prplAcbillcontVo.getId()!=null){//更新表数据
			PrplAcbillcont prplAcbillcont=databaseDao.findByPK(PrplAcbillcont.class, prplAcbillcontVo.getId());
			if(prplAcbillcont!=null){
				Beans.copy().from(prplAcbillcontVo).excludeNull().to(prplAcbillcont);
			}
		}else{//保存表数据
			PrplAcbillcont prplAcbillcont=new PrplAcbillcont();
			Beans.copy().from(prplAcbillcontVo).excludeNull().to(prplAcbillcont);
			databaseDao.save(PrplAcbillcont.class, prplAcbillcont);
		}
		
	}

	@Override
	public PrplAcbillcontVo findPrplAcbillcontVoByBillId(String billId) {
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("billId", billId);
		List<PrplAcbillcont> prplAcbillconts=databaseDao.findAll(PrplAcbillcont.class, queryRule);
		PrplAcbillcontVo prplAcbillcontVo=null;
		if(prplAcbillconts!=null && prplAcbillconts.size()>0){
			prplAcbillcontVo=new PrplAcbillcontVo();
			Beans.copy().from(prplAcbillconts.get(0)).excludeNull().to(prplAcbillcontVo);
		}
		return prplAcbillcontVo;
	}

	@Override
	public PrplAcbillcontVo findPrplAcbillcontById(Long id) {
		QueryRule qr = QueryRule.getInstance();
		qr.addEqual("id",id);
		PrplAcbillcontVo prplAcbillcontVo = null;
		PrplAcbillcont prplAcbillcont = databaseDao.findUnique(PrplAcbillcont.class, qr);
		if (prplAcbillcont != null) {
			prplAcbillcontVo=new PrplAcbillcontVo();
			prplAcbillcontVo = Beans.copyDepth().from(prplAcbillcont).to(PrplAcbillcontVo.class);
		}
		return prplAcbillcontVo;
	}

	@Override
	public List<VatQueryViewVo> findPrpLbillinfoVoByTaskNo(String taskNo) {
		SqlJoinUtils sqlUtils=new SqlJoinUtils();
		sqlUtils.append(" select a.billNo,a.billCode,a.billDate,a.saleName,a.saleNo,a.billNnum,a.billSnum,a.billSl,a.billNum,'1' as vidFlag,a.registerStatus,a.backFlag,a.backCauseinfo,a.creatTime,a.id from PrpLbillinfo a where 1=1 ");
		sqlUtils.append(" and exists(select 1 from PrplAcbillcont b where b.status='1' and b.vidFlag='1' and a.billNo=b.billNo and a.billCode=b.billCode ");
		sqlUtils.append(" and b.taskNo=? ");
		sqlUtils.addParamValue(taskNo);
		sqlUtils.append(" ) ");
		sqlUtils.append(" union all ");
		sqlUtils.append(" select a.billNo,a.billCode,null as billDate,a.saleName,a.saleNo,null as billNnum,null as billSnum,null as billSl,null as billNum,'0' as vidFlag,'0' as registerStatus,null as backFlag,null as backCauseinfo,a.creatTime,a.id from PrplAcbillcont a where a.status='1' and a.vidFlag='0' ");
		sqlUtils.append(" and a.taskNo=? ");
		sqlUtils.addParamValue(taskNo);
		//倒序排序
		sqlUtils.append(" order by creatTime desc ");
		String sql = sqlUtils.getSql();
	    logger.info("发票登记查询(公估/查勘)------------------------->:"+sql);
	    Object[] values = sqlUtils.getParamValues();
	    List<Object[]> objects=baseDaoService.getAllBySql(sql, values);
	    List<VatQueryViewVo> vatQueryViewVoList=new ArrayList<VatQueryViewVo>();
	    if(objects!=null && objects.size()>0){
	    	for(int i=0;i<objects.size();i++){
	    		Object[] obj=objects.get(i);
	    		VatQueryViewVo vatQueryViewVo=new VatQueryViewVo();
	    		vatQueryViewVo.setBillNo(obj[0]!=null?obj[0].toString():null);//发票号码
	    		vatQueryViewVo.setBillCode(obj[1]!=null?obj[1].toString():null);//发票代码
	    		if (obj[2] != null) {
      				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      				if(obj[2] != null){
      					Date underDate=null;
 						try {
 							underDate = format.parse(obj[2].toString());
 						} catch (ParseException e) {
 							e.printStackTrace();
 						}
 						vatQueryViewVo.setBillDate(underDate);//开票日期
      				} 
      			}
	    		vatQueryViewVo.setSaleName(obj[3]!=null?obj[3].toString():null);//销方名称
	    		vatQueryViewVo.setSaleNo(obj[4]!=null?obj[4].toString():null);//纳税人识别号
	    		vatQueryViewVo.setBillNnum(obj[5]!=null?new BigDecimal(obj[5].toString()):null);//发票不含税金额
	    		vatQueryViewVo.setBillSnum(obj[6]!=null?new BigDecimal(obj[6].toString()):null);//发票税额
	    		vatQueryViewVo.setBillSl(obj[7]!=null?new BigDecimal(obj[7].toString()):null);//发票税率
                if(obj[7]!=null){
                	vatQueryViewVo.setBillSlName(percentChage(new BigDecimal(obj[7].toString())));//税率百分比格式
                }
                vatQueryViewVo.setBillNum(obj[8]!=null?new BigDecimal(obj[8].toString()):null);//发票的价税合计
                vatQueryViewVo.setVidflag(obj[9]!=null?obj[9].toString():null);//验真状态
                if("0".equals(vatQueryViewVo.getVidflag())){
                	vatQueryViewVo.setVidflagName("失败");
                }else{
                	vatQueryViewVo.setVidflagName("成功");
                }
                vatQueryViewVo.setRegisterStatus(obj[10]!=null?obj[10].toString():null);
                if("1".equals(vatQueryViewVo.getRegisterStatus())){
                	vatQueryViewVo.setRegisterStatusName("已登记");
                }else{
                	vatQueryViewVo.setRegisterStatusName("未登记");
                }
                vatQueryViewVo.setBackFlag(obj[11]!=null?obj[11].toString():null);//打回标志
                if("1".equals(vatQueryViewVo.getBackFlag())){
                	vatQueryViewVo.setBackCauseInfo(obj[12]!=null?obj[12].toString():null);//打回原因
                }
                vatQueryViewVo.setBillContId(obj[14]!=null?Long.valueOf(obj[14].toString()):null);//发票表或关系表id
                vatQueryViewVoList.add(vatQueryViewVo);
	    	}
	    }
		
		return vatQueryViewVoList;
	}

	@Override
	public List<PrplAcbillcontVo> findPrplAcbillcontByBillNoAndBillCode(String billNo, String BillCode,String taskNo) {
		QueryRule qr = QueryRule.getInstance();
		qr.addEqual("billNo",billNo);
		qr.addEqual("billCode",BillCode);
		qr.addEqual("status","1");
		qr.addEqual("vidFlag","1");
		if(StringUtils.isNotBlank(taskNo)){
		 qr.addEqual("taskNo",taskNo);
		}
		List<PrplAcbillcont> prplAcbillconts=databaseDao.findAll(PrplAcbillcont.class, qr);
		List<PrplAcbillcontVo> prplAcbillcontVos=new ArrayList<PrplAcbillcontVo>();
		if(prplAcbillconts!=null && prplAcbillconts.size()>0){
			prplAcbillcontVos=Beans.copyDepth().from(prplAcbillconts).toList(PrplAcbillcontVo.class);
		}
		return prplAcbillcontVos;
	}

	@Override
	public void updatePrpLAssessorFee(String billNo, String billCode) {
		QueryRule qr = QueryRule.getInstance();
		qr.addEqual("linkBillNo",billNo+"_"+billCode);
		List<PrpLAssessorFee> prpLAssessorFees=databaseDao.findAll(PrpLAssessorFee.class, qr);
		if(prpLAssessorFees!=null && prpLAssessorFees.size()>0){
			for(PrpLAssessorFee po:prpLAssessorFees){
				po.setLinkBillNo(null);//置空
			}
		}
	}

	@Override
	public void updatePrpLCheckFee(String billNo, String billCode) {
		QueryRule qr = QueryRule.getInstance();
		qr.addEqual("linkBillNo",billNo+"_"+billCode);
		List<PrpLCheckFee> prpLCheckFees=databaseDao.findAll(PrpLCheckFee.class, qr);
		if(prpLCheckFees!=null && prpLCheckFees.size()>0){
			for(PrpLCheckFee po:prpLCheckFees){
				po.setLinkBillNo(null);//置空
			}
		}
		
	}

	@Override
	public List<PrpLAssessorFeeVo> findPrpLAssessorFeeBylinkBillNo(String linkBillNo) {
		QueryRule qr = QueryRule.getInstance();
		qr.addEqual("linkBillNo",linkBillNo);
		List<PrpLAssessorFee> prpLAssessorFees=databaseDao.findAll(PrpLAssessorFee.class, qr);
		List<PrpLAssessorFeeVo> prpLAssessorFeeVos=new ArrayList<PrpLAssessorFeeVo>();
		if(prpLAssessorFees!=null && prpLAssessorFees.size()>0){
			prpLAssessorFeeVos=Beans.copyDepth().from(prpLAssessorFees).toList(PrpLAssessorFeeVo.class);
		}
		return prpLAssessorFeeVos;
	}

	@Override
	public List<PrpLCheckFeeVo> findPrpLCheckFeeBylinkBillNo(String linkBillNo) {
		QueryRule qr = QueryRule.getInstance();
		qr.addEqual("linkBillNo",linkBillNo);
		List<PrpLCheckFee> prpLCheckFees=databaseDao.findAll(PrpLCheckFee.class, qr);
		List<PrpLCheckFeeVo> prpLCheckFeeVos=new ArrayList<PrpLCheckFeeVo>();
		if(prpLCheckFees!=null && prpLCheckFees.size()>0){
			prpLCheckFeeVos=Beans.copyDepth().from(prpLCheckFees).toList(PrpLCheckFeeVo.class);
		}
		return prpLCheckFeeVos;
	}

	@Override
	public PrpLAssessorFeeVo findPrpLAssessorFeeById(Long id) {
		QueryRule qr = QueryRule.getInstance();
		qr.addEqual("id",id);
		PrpLAssessorFeeVo prpLAssessorFeeVo = null;
		PrpLAssessorFee prpLAssessorFee = databaseDao.findUnique(PrpLAssessorFee.class, qr);
		if (prpLAssessorFee != null) {
			prpLAssessorFeeVo=new PrpLAssessorFeeVo();
			prpLAssessorFeeVo = Beans.copyDepth().from(prpLAssessorFee).to(PrpLAssessorFeeVo.class);
		}
		return prpLAssessorFeeVo;
	}

	@Override
	public PrpLCheckFeeVo findPrpLCheckFeeById(Long id) {
		QueryRule qr = QueryRule.getInstance();
		qr.addEqual("id",id);
		PrpLCheckFeeVo prpLCheckFeeVo = null;
		PrpLCheckFee prpLCheckFee = databaseDao.findUnique(PrpLCheckFee.class, qr);
		if (prpLCheckFee != null) {
			prpLCheckFeeVo=new PrpLCheckFeeVo();
			prpLCheckFeeVo = Beans.copyDepth().from(prpLCheckFee).to(PrpLCheckFeeVo.class);
		}
		return prpLCheckFeeVo;
	}

	@Override
	public void savePrpLAssessorFee(Long id, String linkBillNo) {
		QueryRule qr = QueryRule.getInstance();
		qr.addEqual("id",id);
		PrpLAssessorFee prpLAssessorFee = databaseDao.findUnique(PrpLAssessorFee.class, qr);
		if(prpLAssessorFee!=null){
			prpLAssessorFee.setLinkBillNo(linkBillNo);
		}
		
	}

	@Override
	public void savePrpLCheckFee(Long id, String linkBillNo) {
		QueryRule qr = QueryRule.getInstance();
		qr.addEqual("id",id);
		PrpLCheckFee prpLCheckFee = databaseDao.findUnique(PrpLCheckFee.class, qr);
		if(prpLCheckFee!=null){
			prpLCheckFee.setLinkBillNo(linkBillNo);
		}
	}
	 /**
	 * 时间转换方法
	 * Date 类型转换 String类型
	 * @param strDate
	 * @return
	 * @throws ParseException2020-05-27 14:45:02
	 */
	private static Date DateBillString(String strDate){
		Date date=null;
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		  if(strDate!=null){
			  try {
				date=format.parse(strDate);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return date;
	}

}
