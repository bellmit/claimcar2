package ins.sino.claimcar.regist.service.spring;

import ins.framework.common.ResultPage;
import ins.framework.dao.database.DatabaseDao;
import ins.framework.dao.database.support.Page;
import ins.framework.dao.database.support.QueryRule;
import ins.framework.exception.BusinessException;
import ins.framework.utils.Beans;
import ins.platform.common.service.facade.BaseDaoService;
import ins.platform.utils.SqlJoinUtils;
import ins.sino.claimcar.claim.vo.PrpLClaimVo;
import ins.sino.claimcar.manager.vo.PrpLPayCustomVo;
import ins.sino.claimcar.regist.po.PrpDClaimAvg;
import ins.sino.claimcar.regist.po.PrpLCInsured;
import ins.sino.claimcar.regist.po.PrpLCItemCar;
import ins.sino.claimcar.regist.po.PrpLCItemKind;
import ins.sino.claimcar.regist.po.PrpLCMain;
import ins.sino.claimcar.regist.po.PrpLClaimDeduct;
import ins.sino.claimcar.regist.po.PrpLRegist;
import ins.sino.claimcar.regist.po.PrpLRegistCarLoss;
import ins.sino.claimcar.regist.po.PrpLRegistExt;
import ins.sino.claimcar.regist.po.PrpLRegistPropLoss;
import ins.sino.claimcar.regist.po.PrpLRegistRiskInfo;
import ins.sino.claimcar.regist.po.Prpphead;
import ins.sino.claimcar.regist.po.Prppmain;
import ins.sino.claimcar.regist.po.Prpptext;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.vo.PrpCcarDriverVo;
import ins.sino.claimcar.regist.vo.PrpDClaimAvgVo;
import ins.sino.claimcar.regist.vo.PrpDagentVo;
import ins.sino.claimcar.regist.vo.PrpLCInsuredVo;
import ins.sino.claimcar.regist.vo.PrpLCItemCarVo;
import ins.sino.claimcar.regist.vo.PrpLCItemKindVo;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLClaimDeductVo;
import ins.sino.claimcar.regist.vo.PrpLRegistCarLossVo;
import ins.sino.claimcar.regist.vo.PrpLRegistExtVo;
import ins.sino.claimcar.regist.vo.PrpLRegistPropLossVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;
import ins.sino.claimcar.regist.vo.PrppMainVo;
import ins.sino.claimcar.regist.vo.PrppheadSubVo;
import ins.sino.claimcar.regist.vo.PrppheadVo;
import ins.sino.claimcar.regist.vo.PrpptextVo;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Path;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.alibaba.dubbo.config.annotation.Service;

/**
 * 
 * @author Think
 *
 */
@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"},timeout = 1000000)
@Path("registQueryService")
public class RegistQueryServiceImpl implements RegistQueryService{
	private static final Log LOGGER = LogFactory.getLog(RegistQueryServiceImpl.class);
	@Autowired
	DatabaseDao databaseDao;
	@Autowired
	private PolicyViewService policyViewService;
	@Autowired
	BaseDaoService baseDaoService;
	
	@Override
	public PrpLRegistVo findByRegistNo(String registNo) {
		Assert.notNull(registNo,"id must have value.");
		PrpLRegist po = databaseDao.findByPK(PrpLRegist.class,registNo);
		if(po == null){
			return null;
		}
		
		PrpLRegistVo vo = Beans.copyDepth().from(po).to(PrpLRegistVo.class);
		return vo;
	}

	@Override
	public PrpLRegistCarLossVo findRegistCarLossById(Long id) {
		PrpLRegistCarLossVo vo = new PrpLRegistCarLossVo();
		Assert.notNull(id,"id must have value.");
		PrpLRegistCarLoss po = databaseDao.findByPK(PrpLRegistCarLoss.class,id);
		vo = Beans.copyDepth().from(po).to(PrpLRegistCarLossVo.class);
		return vo;
	}

	@Override
	public PrpLRegistPropLossVo findRegistPropLossById(Long id) {
		PrpLRegistPropLossVo vo = new PrpLRegistPropLossVo();
		Assert.notNull(id,"id must have value.");
		PrpLRegistPropLoss po = databaseDao.findByPK(PrpLRegistPropLoss.class,id);
		vo = Beans.copyDepth().from(po).to(PrpLRegistPropLossVo.class);
		return vo;
	}

	/**
	 * 根据报案号查找免赔率列表
	 */
	@Override
	public List<PrpLClaimDeductVo> findClaimDeductVoByRegistNo(String registNo) {
		List<PrpLClaimDeductVo> claimDeductVoList = null;
		List<PrpLClaimDeduct> claimDeductPoList = new ArrayList<PrpLClaimDeduct>();
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo",registNo);
		claimDeductPoList = databaseDao.findAll(PrpLClaimDeduct.class,queryRule);
		if(claimDeductPoList!=null&&claimDeductPoList.size()>0){
			claimDeductVoList = new ArrayList<PrpLClaimDeductVo>();
			claimDeductVoList = Beans.copyDepth().from(claimDeductPoList).toList(PrpLClaimDeductVo.class);
		}
		return claimDeductVoList;
	}
	
	/**
	 * 根据报案号查找已勾选的免赔条件
	 */
	@Override
	public List<PrpLClaimDeductVo> findIsCheckClaimDeducts(String registNo) {
		List<PrpLClaimDeductVo> claimDeductVoList = null;
		List<PrpLClaimDeduct> claimDeductPoList = new ArrayList<PrpLClaimDeduct>();
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo",registNo);
		queryRule.addEqual("isCheck","1");
		claimDeductPoList = databaseDao.findAll(PrpLClaimDeduct.class,queryRule);
		if(claimDeductPoList!=null&&claimDeductPoList.size()>0){
			claimDeductVoList = new ArrayList<PrpLClaimDeductVo>();
			claimDeductVoList = Beans.copyDepth().from(claimDeductPoList).toList(PrpLClaimDeductVo.class);
		}
		return claimDeductVoList;
	}

	/**
	 * 查勘更新免赔率
	 */
	@Override
	public void updateClaimDeduct(List<PrpLClaimDeductVo> claimDeductVos) {
		// 更新List
		for(PrpLClaimDeductVo claimDeductVo : claimDeductVos){
			PrpLClaimDeduct claimDeductPo = this.findClaimDeductByPK(claimDeductVo.getId());
			if(StringUtils.isEmpty(claimDeductVo.getIsCheck())){
				claimDeductVo.setIsCheck("0");
			}
			Beans.copy().from(claimDeductVo).excludeNull().to(claimDeductPo);
			databaseDao.update(PrpLClaimDeduct.class,claimDeductPo);
		}
	}

	private PrpLClaimDeduct findClaimDeductByPK(Long id){
		return databaseDao.findByPK(PrpLClaimDeduct.class,id);
	}

	@Override
	public void updatePrpLRegistExt(PrpLRegistExtVo registExt) {
		PrpLRegistExt registExtPo = new PrpLRegistExt();
		if(registExt.getRegistNo()!=null){
			Beans.copy().from(registExt).excludeNull().to(registExtPo);
			databaseDao.update(PrpLRegistExt.class,registExtPo);
		}
	}


	/**
	 * 根据条件查找案均值
	 * 
	 * <pre></pre>
	 * @param riskCode 险种代码
	 * @param kindCode 先别代码
	 * @param avgType 案均类型
	 * @return
	 * @modified: ☆ZhouYanBin(2016年3月21日 上午10:54:48): <br>
	 */
	public List<PrpDClaimAvgVo> findForceClaimAverageValue(String riskCode,String kindCode,String avgType,Integer year,String comcode) {
		List<PrpDClaimAvgVo> prpDClaimAvgVoList = null;
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("riskCode",riskCode);
		queryRule.addEqual("kindCode",kindCode);
		queryRule.addEqual("avgYear",BigDecimal.valueOf(year));
		comcode = comcode.substring(0,2);
		queryRule.addLike("comCode",comcode+"%");
		if(avgType != null){
			queryRule.addEqual("avgType",avgType);
		}
		List<PrpDClaimAvg> prpDClaimAvgList = databaseDao.findAll(PrpDClaimAvg.class,queryRule);
		if(prpDClaimAvgList!=null&& !prpDClaimAvgList.isEmpty()){
			prpDClaimAvgVoList = Beans.copyDepth().from(prpDClaimAvgList).toList(PrpDClaimAvgVo.class);
		}else{
			//案均值需要实现若当前年度无案均值则需要自动查找前一年度的案均值并赋值
			QueryRule queryRule2 = QueryRule.getInstance();
			queryRule2.addEqual("riskCode",riskCode);
			queryRule2.addEqual("kindCode",kindCode);
			queryRule2.addEqual("avgYear",BigDecimal.valueOf(year-1));
			comcode = comcode.substring(0,2);
			queryRule2.addLike("comCode",comcode+"%");
			if(avgType != null){
				queryRule2.addEqual("avgType",avgType);
			}
			
			prpDClaimAvgList = databaseDao.findAll(PrpDClaimAvg.class,queryRule2);
			if(prpDClaimAvgList!=null&& !prpDClaimAvgList.isEmpty()){
				prpDClaimAvgVoList = Beans.copyDepth().from(prpDClaimAvgList).toList(PrpDClaimAvgVo.class);
			}
		}
		
		return prpDClaimAvgVoList;
	}

 
	@Override
	public ResultPage<PrpLRegistVo> findRegistPageByRegistNo(String registNo,String policyNo,String insuredName,String licenseNo,int start,int length) {
		// TODO Auto-generated method stub
		
		SqlJoinUtils sqlUtil=new SqlJoinUtils();
		sqlUtil.append(" FROM PrpLRegist regist,PrpLRegistExt registExt");
		sqlUtil.append(" WHERE regist.registNo = registExt.registNo");
		
		if(StringUtils.isNotBlank(registNo)){
			sqlUtil.append(" AND regist.registNo = ?");
			sqlUtil.addParamValue(registNo);
		}
		if(StringUtils.isNotBlank(policyNo)){
			sqlUtil.append(" AND regist.policyNo = ?");
			sqlUtil.addParamValue(policyNo);
		}
		if(StringUtils.isNotBlank(insuredName)){
			sqlUtil.append(" AND registExt.insuredName = ?");
			sqlUtil.addParamValue(insuredName);
		}
		if(StringUtils.isNotBlank(licenseNo)){
			sqlUtil.append(" AND registExt.licenseNo = ?");
			sqlUtil.addParamValue(licenseNo);
		}
		
		String sql = sqlUtil.getSql();
		Object[] values = sqlUtil.getParamValues();
		
		Page<Object[]> page = databaseDao.findPageByHql(sql,start/length+1,length,values);
		
		List<PrpLRegistVo> resultVoList=new ArrayList<PrpLRegistVo>();
		for(int i = 0; i<page.getResult().size(); i++ ){
			PrpLRegistVo registVo = new PrpLRegistVo();
			PrpLRegistExtVo registExtVo = new PrpLRegistExtVo();
			Object[] obj = page.getResult().get(i);

			PrpLRegist prpLRegist = (PrpLRegist)obj[0];
			PrpLRegistExt prpLRegistExt = (PrpLRegistExt)obj[1];
			Beans.copy().from(prpLRegist).to(registVo);
			Beans.copy().from(prpLRegistExt).to(registExtVo);
			registVo.setPrpLRegistExt(registExtVo);
			resultVoList.add(registVo);
		}
		ResultPage<PrpLRegistVo> resultPage = new ResultPage<PrpLRegistVo> (start, length, page.getTotalCount(), resultVoList);

		return resultPage;
	}

	@Override
	public ResultPage<PrpLCMainVo> findPrpLCMainPageByRegistNo(String registNo,
			String policyNo, Date taskInTimeStart, Date taskInTimeEnd,
			int start, int length) {
		SqlJoinUtils sqlUtil=new SqlJoinUtils();
		sqlUtil.append(" FROM PrpLCMain main ");
    	sqlUtil.append(" where 1=1");
		
		if(StringUtils.isNotBlank(registNo)){
			sqlUtil.append(" AND main.registNo like ? ");
			sqlUtil.addParamValue("%"+registNo+"%");
		}
		
		if(StringUtils.isNotBlank(policyNo)){
			sqlUtil.append(" AND main.policyNo like ? ");
			sqlUtil.addParamValue("%"+policyNo+"%");
		}
		
//		sqlUtil.append(" AND main.endDate >= ? AND main.endDate <= ? ");
//		sqlUtil.addParamValue(taskInTimeStart);
//		sqlUtil.addParamValue(taskInTimeEnd);
		
		String sql = sqlUtil.getSql();
		Object[] values = sqlUtil.getParamValues();
		Page<Object[]> page = databaseDao.findPageByHql(sql,start/length+1,length,values);
		//对象转换
		List<PrpLCMainVo> resultVoList=new ArrayList<PrpLCMainVo>();
		for(int i = 0; i<page.getResult().size(); i++ ){
            
			PrpLCMainVo resultVo=new PrpLCMainVo();
			Object obj = page.getResult().get(i);

			
			PrpLCMain prpLCMain = (PrpLCMain)obj;
			Beans.copy().from(prpLCMain).excludeNull().to(resultVo);
			
			List<PrppheadVo> prppheadVoList=new ArrayList<PrppheadVo>();
		    //System.out.print("++++++++++++++++++++++++++++++++++");
			if(prpLCMain != null){
		       prppheadVoList=this.findByPolicyNo(prpLCMain.getPolicyNo());
					
			}
			StringBuffer regNoUrl = new StringBuffer();
			StringBuffer regEndorseNo = new StringBuffer();
			if(prppheadVoList!=null && prppheadVoList.size()>0){
			for(PrppheadVo PrppheadVo : prppheadVoList){
				regNoUrl.append(PrppheadVo.getEndorseNo());
				regNoUrl.append("<br>");
				regEndorseNo.append(PrppheadVo.getEndorseNo()+"_");
				}
			}
			
			
			resultVo.setPingendorseNo(regEndorseNo.toString());
		    resultVo.setEndorseNo(regNoUrl.toString());
		    resultVoList.add(resultVo);
			}
		ResultPage<PrpLCMainVo> resultPage = new ResultPage<PrpLCMainVo> (start, length, page.getTotalCount(), resultVoList);
		return resultPage;
	}
	
	
	/**
	 * 根据报案号查询报案时保单险别表PrpLCItemKind
	 * @param registNo
	 * @return
	 * @author lanlei
	 */
	@Override
	public List<PrpLCItemKindVo> findCItemKindListByRegistNo(String registNo) {

		List<PrpLCItemKind> poList = new ArrayList<PrpLCItemKind>();
		List<PrpLCItemKindVo> voList = new ArrayList<PrpLCItemKindVo>();
		QueryRule qr = QueryRule.getInstance();
		qr.addEqual("registNo", registNo);
		poList = databaseDao.findAll(PrpLCItemKind.class, qr);

		for (PrpLCItemKind po : poList) {
			if (po != null) {
				voList.add(Beans.copyDepth().from(po).to(PrpLCItemKindVo.class));
			}
		}
		return voList;
	}
	
	@Override
	public List<PrppheadVo> findByPolicyNo(String policyNo) {
		List<PrppheadVo>  prppheadVoList=new ArrayList<PrppheadVo>();
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("policyNo",policyNo);
		List<Prpphead> PrppheadList = databaseDao.findAll(Prpphead.class,queryRule);
        if(PrppheadList !=null && PrppheadList.size()>0){
        	for(Prpphead prpphead : PrppheadList){
        		PrppheadVo prppheadVo=Beans.copyDepth().from(prpphead).to(PrppheadVo.class);
        		prppheadVoList.add(prppheadVo);
        	}
        	}
	
		return prppheadVoList;
	}
	
	/**
	 * 根据险种代码组织可选免赔条件
	 * 
	 * <pre></pre>
	 * @param registNo 报案号
	 * @return
	 * @modified: ☆ZhouYanBin(2016年4月18日 上午10:31:28): <br>
	 */
	@Override
	public Map<String,Double> getDeductRate(String registNo) {
		Map<String,Double> dutyDeductRateMap = new HashMap<String,Double>();
		if(registNo == null || "".equals(registNo)){
			LOGGER.error("报案号"+registNo+"为空");
			throw new BusinessException("报案号"+registNo+"为空",false);
		}
		
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo",registNo);
		List<PrpLClaimDeduct> prpLClaimDeductList = databaseDao.findAll(PrpLClaimDeduct.class,queryRule);
		if(prpLClaimDeductList == null || prpLClaimDeductList.isEmpty()){
			return dutyDeductRateMap;
		}
		
		
		List<PrpLCItemKindVo> prpLCItemKindVoList = policyViewService.findItemKindVoListByRegistNoAndPolicyNo(registNo,null);
		if(prpLCItemKindVoList == null || prpLCItemKindVoList.isEmpty()){
			LOGGER.error("该案件不存在险别信息，报案号"+registNo);
			throw new BusinessException("该案件不存在险别信息，报案号"+registNo,false);
		}
		for(PrpLCItemKindVo prpLCItemKindVo:prpLCItemKindVoList){
			// 是否承保了Ｍ险
			boolean isMKindCode = policyViewService.isMKindCode(registNo,prpLCItemKindVo.getKindCode().trim());
			String kindcode = prpLCItemKindVo.getKindCode().trim();
			if(isMKindCode){
				// 此处的险别可能包括新老险别，为了保证不把新险别中的不计免赔条款放进去，不想每个险别分开判断
				// 后期如果险别规则有变化，请做调整(例如map里面应该是A，而不是AM)
				if(kindcode.length() > 1 && kindcode.trim().endsWith("M")) {
					kindcode = kindcode.substring(0,kindcode.length() -1);
				}
				dutyDeductRateMap.put(kindcode,Double.valueOf(0));
			}else{
				for(PrpLClaimDeduct prpLClaimDeduct:prpLClaimDeductList){
					// dutyDeductRateMap存在险别对应的免赔，追加进去，没有直接put进去
					if(kindcode.equals(prpLClaimDeduct.getKindCode().trim())){
						if(dutyDeductRateMap.get(kindcode) != null){
							double deductRate = dutyDeductRateMap.get(kindcode);
							dutyDeductRateMap.put(kindcode,(deductRate + prpLClaimDeduct.getDeductRate().doubleValue()));
						}else{
							dutyDeductRateMap.put(kindcode,prpLClaimDeduct.getDeductRate().doubleValue());
						}
					}
				}
			}
		}
		
		return dutyDeductRateMap;
	}
	
	/**
	 * 根据报案号获得涉案标的车信息,优先获取商业险车辆信息
	 * @param registNo
	 * @return
	 */
	public PrpLCItemCarVo findCItemCarByRegistNo(String registNo){
		PrpLCItemCarVo itemCarVo = new PrpLCItemCarVo();
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo", registNo);
		List<PrpLCItemCar> itemCarList = databaseDao.findAll(PrpLCItemCar.class,queryRule);
		// 一张保单承保一辆标的车
		if(itemCarList!=null && !itemCarList.isEmpty()){
			PrpLCItemCar itemCar =null;
			if(itemCarList.size()==1){
				itemCar = itemCarList.get(0);
			}else{
				for(PrpLCItemCar itemCarPo : itemCarList){
					if(!"1101".equals(itemCarPo.getRiskCode())){
						itemCar = itemCarPo;
						break;
					}
				}
			}
			Beans.copy().from(itemCar).to(itemCarVo);
		}
		return itemCarVo;
	}

	// 根据报案号查询报案信息扩展表--PrpLRegistExt
	public PrpLRegistExtVo getPrpLRegistExtInfo(String registNo) {
		PrpLRegistExtVo vo = new PrpLRegistExtVo();
		PrpLRegistExt po = databaseDao.findByPK(PrpLRegistExt.class, registNo);

		vo = Beans.copyDepth().from(po).to(PrpLRegistExtVo.class);
		return vo;
	}
	
	/**
	 * 根据报案时间查询数据
	 * 
	 * <pre></pre>
	 * @param beginTime
	 * @param endTime
	 * @return
	 * @modified: ☆ZhouYanBin(2016年5月13日 上午10:42:56): <br>
	 */
	@Override
	public List<PrpLRegistVo> findPrpLRegistVoListByReportTime(Date beginTime,Date endTime){
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addBetween("reportTime",beginTime,endTime);
		queryRule.addIsNull("cancelTime");
		queryRule.addEqual("registTaskFlag","1");
		
		// queryRule.addEqual("registNo","4000000201612060000246");
		List<PrpLRegist> registList = databaseDao.findAll(PrpLRegist.class,queryRule);
		
		List<PrpLRegistVo> prpLRegistVoList = Beans.copyDepth().from(registList).toList(PrpLRegistVo.class);
		
		return prpLRegistVoList;
	}
	
	public List<PrpLRegistVo> findRegistByQueryReportTime(Date beginTime ,Date endTime,String subComcode){
		
		QueryRule queryRule = QueryRule.getInstance();
		List<PrpLRegistVo> prpLRegistVoList = new ArrayList<PrpLRegistVo>();
		queryRule.addBetween("reportTime",beginTime,endTime);
		queryRule.addIsNull("cancelTime");
		queryRule.addSql("  comCode like '"+subComcode+"%' ");
		List<PrpLRegist> registList = databaseDao.findAll(PrpLRegist.class,queryRule);
		
		prpLRegistVoList = Beans.copyDepth().from(registList).toList(PrpLRegistVo.class);
		
		return prpLRegistVoList;
	}

	/* 
	 * @see ins.sino.claimcar.regist.service.RegistQueryService#findRiskInfoByRegistNo(java.lang.String)
	 * @param registNo
	 * @return
	 */
	
	@Override
	public int findRiskInfoByRegistNo(String registNo,String classType) {
		int resultValue = 0;
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo",registNo);
		if("1".equals(classType)){
			queryRule.addEqual("factorcode","CI-CXCS-00");
		}else{
			queryRule.addEqual("factorcode","BI-CXCS-00");
		}
		List<PrpLRegistRiskInfo> riskInfo = databaseDao.findAll(PrpLRegistRiskInfo.class,queryRule);
		if(riskInfo!=null&&riskInfo.size()>0){
			resultValue = Integer.parseInt(riskInfo.get(0).getFactorvalue());
		}
		return resultValue;
	}

	@Override
	public List<PrpLRegistVo> findPrpLRegistByPolicyNo(String policyNo) {
		List<PrpLRegistVo>  PrpLRegistVoList=new ArrayList<PrpLRegistVo>();
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("policyNo",policyNo);
		List<PrpLRegist> PrpLRegistList = databaseDao.findAll(PrpLRegist.class,queryRule);
        if(PrpLRegistList !=null && PrpLRegistList.size()>0){
        	for(PrpLRegist prpLRegist: PrpLRegistList){
        		PrpLRegistVo prpLRegistVo=Beans.copyDepth().from(prpLRegist).to(PrpLRegistVo.class);
        		PrpLRegistVoList.add(prpLRegistVo);
        	}
        }
		return PrpLRegistVoList;
	}
	
	@Override
	public List<PrpLRegistVo> findPrpLRegist(String policyNo) {
		List<PrpLRegistVo> prpLRegistVos = new ArrayList<PrpLRegistVo>();
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("policyNo",policyNo);
		queryRule.addDescOrder("damageTime");
		List<PrpLRegist> PrpLRegistList = databaseDao.findAll(PrpLRegist.class,queryRule);
        if(PrpLRegistList !=null && !PrpLRegistList.isEmpty()){
        	prpLRegistVos = Beans.copyDepth().from(PrpLRegistList).toList(PrpLRegistVo.class);
        }
		return prpLRegistVos;
	}

	@Override
	public List<PrppheadSubVo> findPrppheadSubByPolicyNo(String policyNo) {
		List<PrppheadSubVo>  PrppheadSubVoList = new ArrayList<PrppheadSubVo>();
		
		SqlJoinUtils sqlUtil = new SqlJoinUtils();
		sqlUtil.append("SELECT pauseDate,pauseHour,comebackDate,comebackHour FROM ywuser.PrppheadSub WHERE policyNo = ? ");
		sqlUtil.addParamValue(policyNo);
		// 查询参数
		String sql = sqlUtil.getSql();
		LOGGER.debug("SQL:"+sql);
		Object[] values = sqlUtil.getParamValues();
		//执行查询
		List<Object[]> objects = baseDaoService.getAllBySql(sql,values);
	/*	List<PrppheadSub> prppheadSubList = databaseDao.findAllBySql(PrppheadSub.class, sql, values);
		 if(prppheadSubList !=null && prppheadSubList.size()>0){
	        	for(PrppheadSub prppheadSub : prppheadSubList){
	        		PrppheadSubVo headSubVo=Beans.copyDepth().from(prppheadSub).to(PrppheadSubVo.class);
	        		PrppheadSubVoList.add(headSubVo);
	        	}
	        	}*/
		 if(objects !=null && objects.size()>0){
        	for(int i=0;i<objects.size();i++){
        		/*PrppheadSubVo subVo = new PrppheadSubVo();
        		Beans.copy().from(sub).to(subVo);*/
        		PrppheadSubVo headSubVo=new PrppheadSubVo();
        		Object[] obj = objects.get(i);
        		if(obj[0] != null){
        			headSubVo.setPauseDates(obj[0].toString());
        		}
        		if(obj[1] != null){
        			headSubVo.setPauseHours(obj[1].toString());
        		}
        		if(obj[2] != null){
        			headSubVo.setComebackDates(obj[2].toString());
        		}
        		if(obj[3] != null){
        			headSubVo.setComebackHours(obj[3].toString());
        		}
        		PrppheadSubVoList.add(headSubVo);
        		//PrppheadSubVoList.add(subVo);
//        		PrppheadSubVo headSubVo = new 
//        		//PrppheadSubVo PrppheadSubVo headSubVo=Beans.copyDepth().from(headSub).to(PrppheadSubVo.class);
//        		PrppheadSubVoList.add(headSubVo);
        	}
	      }
		  
		  
		/*QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("policyNo",policyNo);
		List<PrppheadSub> prppheadSubList = databaseDao.findAll(PrppheadSub.class,queryRule);
        if(prppheadSubList !=null && prppheadSubList.size()>0){
        	for(PrppheadSub prppheadSub : prppheadSubList){
        		PrppheadSubVo headSubVo=Beans.copyDepth().from(prppheadSub).to(PrppheadSubVo.class);
        		PrppheadSubVoList.add(headSubVo);
        	}
        	}*/
	
		return PrppheadSubVoList;
	}

	@Override
	public List<PrpLCInsuredVo> findPrpCinsuredNatureByPolicyNo(String policyNo) {
		List<PrpLCInsuredVo>  prpLCInsuredVoList = new ArrayList<PrpLCInsuredVo>();
		
		SqlJoinUtils sqlUtil = new SqlJoinUtils();
		sqlUtil.append("SELECT SEX,AGE,INSUREDFLAG FROM PRPCINSUREDNATURE WHERE policyNo = ? ");
		sqlUtil.addParamValue(policyNo);
		// 查询参数
		String sql = sqlUtil.getSql();
		LOGGER.debug("SQL:"+sql);
		Object[] values = sqlUtil.getParamValues();
		//执行查询
		List<Object[]> objects = baseDaoService.getAllBySql(sql,values);
		 if(objects !=null && objects.size()>0){
        	for(int i=0;i<objects.size();i++){
        		PrpLCInsuredVo prpLCInsuredVo=new PrpLCInsuredVo();
        		Object[] obj = objects.get(i);
        		if(obj[0] != null){
        			prpLCInsuredVo.setSex(obj[0].toString());
        		}
        		if(obj[1] != null){
        			prpLCInsuredVo.setAge(((BigDecimal) obj[1]).longValue());
        		}
        		if(obj[2] != null){
        			prpLCInsuredVo.setInsuredFlag((String) obj[2]);
        		}
        		prpLCInsuredVoList.add(prpLCInsuredVo);
        	}
	      }
	
		return prpLCInsuredVoList;
	}
	
	@Override
	public List<PrpLCInsuredVo> findPrpLCInsuredVoByPolicyNoAndFlag(
			String policyNo, String insuredFlag) {
		List<PrpLCInsuredVo>  prpLCInsuredVoList = new ArrayList<PrpLCInsuredVo>();
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("policyNo",policyNo);
		queryRule.addEqual("insuredFlag", insuredFlag);
		List<PrpLCInsured> prplCinsuredList = databaseDao.findAll(PrpLCInsured.class, queryRule);
		if(prplCinsuredList != null && prplCinsuredList.size() > 0){
			prpLCInsuredVoList = Beans.copyDepth().from(prplCinsuredList).toList(PrpLCInsuredVo.class);
		}
		return prpLCInsuredVoList;
	}

		@Override
		public List<PrpptextVo> findPrppTextByPolicyNo(String endorseNo) {
			
			SqlJoinUtils sqlUtil = new SqlJoinUtils();
			List<PrpptextVo>  prppTextVoList = new ArrayList<PrpptextVo>();
			List<Prpptext>  prppTextPoList = new ArrayList<Prpptext>();
			sqlUtil.append("SELECT * FROM ywuser.Prpptext WHERE 1=1 ");
					sqlUtil.append("AND endorseNo =? ");
					sqlUtil.addParamValue(endorseNo);
					// 查询参数
					String sql = sqlUtil.getSql();
					Object[] values = sqlUtil.getParamValues();
					// 执行查询
					List<Object[]> objects = baseDaoService.getAllBySql(sql,values);
					//prppTextPoList = databaseDao.findAllBySql(Prpptext.class, sql,values);
					// 对象转换
					for(int i = 0; i<objects.size(); i++ ){
						Object[] obj = objects.get(i);
						PrpptextVo prppTextVo= new PrpptextVo(); 
						prppTextVo.setEndorseNo(obj[0]==null ? "" : obj[0].toString());
						prppTextVo.setPolicyNo(obj[1]==null ? "" : obj[1].toString());
						prppTextVo.setLineNo(obj[2]==null ? "" : obj[2].toString());
						prppTextVo.setEndorseText(obj[3]==null ? "" : obj[3].toString());
						prppTextVo.setFlag(obj[4]==null ? "" : obj[4].toString());
						prppTextVoList.add(prppTextVo);
					}
					
			
		/*	QueryRule queryRule = QueryRule.getInstance();
			queryRule.addEqual("endorseNo",endorseNo);
			queryRule.addEqual("policyNo", "200020020161206004120");
			queryRule.addAscOrder("lineNo");
			List<Prpptext> prpptextList = databaseDao.findAll(Prpptext.class,queryRule);
	        if(prpptextList !=null && prpptextList.size()>0){
	        	for(Prpptext prppText : prpptextList){
	        		PrpptextVo prppTextVo=Beans.copyDepth().from(prppText).to(PrpptextVo.class);
	        		prppTextVoList.add(prppTextVo);
	        	}
	        	}*/
	
			return prppTextVoList;
		}


    @Override
    public List<PrppheadVo> findByOther(String policyNo,List<String> endorType,String sort) {
        List<PrppheadVo>  prppheadVoList=new ArrayList<PrppheadVo>();
        QueryRule queryRule = QueryRule.getInstance();
        queryRule.addEqual("policyNo",policyNo);
        if(endorType.size()>0){
            //queryRule.addEqual("endorType",endorType);
            queryRule.addIn("endorType",endorType);
        }
        
        if(StringUtils.isNotBlank(sort)){
            queryRule.addAscOrder(sort);
        }
        
        List<Prpphead> PrppheadList = databaseDao.findAll(Prpphead.class,queryRule);
        if(PrppheadList !=null && PrppheadList.size()>0){
            for(Prpphead prpphead : PrppheadList){
                if(prpphead.getValidDate()!=null){
                    PrppheadVo prppheadVo=Beans.copyDepth().from(prpphead).to(PrppheadVo.class);
                    prppheadVoList.add(prppheadVo);
                }
            }
            }
        return prppheadVoList;
    }

    @Override
    public List<PrppheadSubVo> findPrppheadSubVoByOther(String policyNo,List<String> endorseNo) {
        List<PrppheadSubVo>  PrppheadSubVoList = new ArrayList<PrppheadSubVo>();
        
        SqlJoinUtils sqlUtil = new SqlJoinUtils();
        sqlUtil.append("SELECT pauseDate,pauseHour,comebackDate,comebackHour FROM ywuser.PrppheadSub WHERE 1=1 ");
        
        if(StringUtils.isNotBlank(policyNo)){
            sqlUtil.append(" AND policyNo = ? ");
            sqlUtil.addParamValue(policyNo);
        }
        
        if(endorseNo.size() > 0){
            sqlUtil.append(" AND endorseNo in (  ");
            for(int i=0; endorseNo.size()>i ;i++){
                if(i<1){
                    sqlUtil.append("?");
                    sqlUtil.addParamValue(endorseNo.get(i));
                }else{
                    sqlUtil.append(",?");
                    sqlUtil.addParamValue(endorseNo.get(i));
                }
            }
            sqlUtil.append(" )");
        }
        // 查询参数
        String sql = sqlUtil.getSql();
        LOGGER.debug("SQL:"+sql);
        Object[] values = sqlUtil.getParamValues();
        //执行查询
        List<Object[]> objects = baseDaoService.getAllBySql(sql,values);
         if(objects !=null && objects.size()>0){
            for(int i=0;i<objects.size();i++){
                PrppheadSubVo headSubVo=new PrppheadSubVo();
                Object[] obj = objects.get(i);
                if(obj[0] != null){
                    headSubVo.setPauseDates(obj[0].toString());
                }
                if(obj[1] != null){
                    headSubVo.setPauseHours(obj[1].toString());
                }
                if(obj[2] != null){
                    headSubVo.setComebackDates(obj[2].toString());
                }
                if(obj[3] != null){
                    headSubVo.setComebackHours(obj[3].toString());
                }
                PrppheadSubVoList.add(headSubVo);
            }
          }
        return PrppheadSubVoList;
    }
	

	@Override
	public List<PrppMainVo> findprppMainByPolicyNo(String endorseNo) {
		List<PrppMainVo>  prppMainVoList = new ArrayList<PrppMainVo>();
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("endorseNo",endorseNo);
		List<Prppmain> PrppmainList = databaseDao.findAll(Prppmain.class,queryRule);
        if(PrppmainList !=null && PrppmainList.size()>0){
        	for(Prppmain prppmain : PrppmainList){
        		PrppMainVo prppMainVo=Beans.copyDepth().from(prppmain).to(PrppMainVo.class);
        		prppMainVoList.add(prppMainVo);
        	}
        	}
	
		return prppMainVoList;
	}

	@Override
	public List<PrpLRegistVo> findOldRegistByPolicyNo(String policyNo) {
		SqlJoinUtils sqlUtil = new SqlJoinUtils();
		List<PrpLRegistVo> prpLRegistVoList = new ArrayList<PrpLRegistVo>();
		sqlUtil.append("SELECT REGISTNO,LICENSENO,DAMAGECODE,DAMAGESTARTDATE,DAMAGEADDRESS,REPORTDATE,LFLAG FROM ywuser.prplregist WHERE 1=1 ");
		sqlUtil.append("AND policyNo =? ");
		sqlUtil.addParamValue(policyNo);
		// 查询参数
		String sql = sqlUtil.getSql();
		Object[] values = sqlUtil.getParamValues();
		// 执行查询
		List<Object[]> objects = baseDaoService.getAllBySql(sql,values);
		// 对象转换
		for(int i = 0; i<objects.size(); i++ ){
			Object[] obj = objects.get(i);
			PrpLRegistVo prpLRegistVo= new PrpLRegistVo(); 
			prpLRegistVo.setRegistNo(obj[0]==null ? "" : obj[0].toString());
			prpLRegistVo.setLicense(obj[1]==null ? "" : obj[1].toString());
			prpLRegistVo.setDamageCode(obj[2]==null ? "" : obj[2].toString());
			prpLRegistVo.setDamageTime(obj[3]==null ? null : (Date)obj[3]);
			prpLRegistVo.setDamageAddress(obj[4]==null ? "" : obj[4].toString());
			prpLRegistVo.setReportTime(obj[5]==null ? null : (Date)obj[5]);
			prpLRegistVo.setFlag(obj[6]==null ? "" : obj[6].toString());
			prpLRegistVoList.add(prpLRegistVo);
		}
		return prpLRegistVoList;
	}

	@Override
	public List<PrpLPayCustomVo> findOldPrpjlinkaccountByCertiNo(String certiNo) {
		SqlJoinUtils sqlUtil = new SqlJoinUtils();
		List<PrpLPayCustomVo> prpLPayCustomVoList = new ArrayList<PrpLPayCustomVo>();
		sqlUtil.append("SELECT ACCOUNTNO,CERTINO FROM ywuser.prpjlinkaccount WHERE 1=1 ");
		sqlUtil.append("AND certiNo =? ");
		sqlUtil.addParamValue(certiNo);
		// 查询参数
		String sql = sqlUtil.getSql();
		Object[] values = sqlUtil.getParamValues();
		// 执行查询
		List<Object[]> objects = baseDaoService.getAllBySql(sql,values);
		// 对象转换
		for(int i = 0; i<objects.size(); i++ ){
			Object[] obj = objects.get(i);
			PrpLPayCustomVo prpLPayCustomVo = new PrpLPayCustomVo();
			System.out.println(objects.get(i)[0]+"=============");
			prpLPayCustomVo.setAccountNo(obj[0]==null ? "" : obj[0].toString());
			prpLPayCustomVoList.add(prpLPayCustomVo);
		}
		return prpLPayCustomVoList;
	}

	@Override
	public List<PrpLPayCustomVo> findOldAccountByAccountNo(String AccountNo) {
		SqlJoinUtils sqlUtil = new SqlJoinUtils();
		List<PrpLPayCustomVo> prpLPayCustomVoList = new ArrayList<PrpLPayCustomVo>();
		sqlUtil.append("SELECT CLIENTTYPE,ACCOUNTNAME,IDENTIFYNUMBER,NAMEOFBANK,ACCOUNTNO,TELEPHONE FROM ywuser.accrecaccount WHERE 1=1 ");
		sqlUtil.append("AND accountNo =? ");
		sqlUtil.addParamValue(AccountNo);
		// 查询参数
		String sql = sqlUtil.getSql();
		Object[] values = sqlUtil.getParamValues();
		// 执行查询
		List<Object[]> objects = baseDaoService.getAllBySql(sql,values);
		// 对象转换
		for(int i = 0; i<objects.size(); i++ ){
			Object[] obj = objects.get(i);
			PrpLPayCustomVo prpLPayCustomVo = new PrpLPayCustomVo();
			prpLPayCustomVo.setPayObjectKind(obj[0]==null ? "" : obj[0].toString());
			prpLPayCustomVo.setPayeeName(obj[1]==null ? "" : obj[1].toString());
			prpLPayCustomVo.setCertifyNo(obj[2]==null ? "" : obj[2].toString());
			prpLPayCustomVo.setBankName(obj[3]==null ? "" : obj[3].toString());
			prpLPayCustomVo.setAccountNo(obj[4]==null ? "" : obj[4].toString());
			prpLPayCustomVo.setPayeeMobile(obj[5]==null ? "" : obj[5].toString());
			prpLPayCustomVoList.add(prpLPayCustomVo);
		}
		return prpLPayCustomVoList;
	}

	@Override
	public List<PrpLRegistVo> findOldPrpLRegistRPolicy(String registNo) {
		SqlJoinUtils sqlUtil = new SqlJoinUtils();
		List<PrpLRegistVo> prpLRegistVoList = new ArrayList<PrpLRegistVo>();
		sqlUtil.append("SELECT REGISTNO,POLICYNO,RISKCODE FROM ywuser.prpLRegistRPolicy WHERE 1=1 ");
		sqlUtil.append("AND registNo =? ");
		sqlUtil.addParamValue(registNo);
		// 查询参数
		String sql = sqlUtil.getSql();
		Object[] values = sqlUtil.getParamValues();
		// 执行查询
		List<Object[]> objects = baseDaoService.getAllBySql(sql,values);
		// 对象转换
		for(int i = 0; i<objects.size(); i++ ){
			Object[] obj = objects.get(i);
			PrpLRegistVo prpLRegistVo= new PrpLRegistVo(); 
			prpLRegistVo.setRegistNo(obj[0]==null ? "" : obj[0].toString());
			prpLRegistVo.setPolicyNo(obj[1]==null ? "" : obj[1].toString());
			prpLRegistVo.setRiskCode(obj[2]==null ? "" : obj[2].toString());
			prpLRegistVoList.add(prpLRegistVo);
		}
		return prpLRegistVoList;
	}

	@Override
	public List<PrpLRegistCarLossVo> findOldPrpLthirdCarLoss(String registNo,String serialNo) {
		SqlJoinUtils sqlUtil = new SqlJoinUtils();
		List<PrpLRegistCarLossVo> prpLRegistCarLossVoList = new ArrayList<PrpLRegistCarLossVo>();
		sqlUtil.append("SELECT PARTCODE,PARTNAME FROM ywuser.PrpLthirdCarLoss WHERE 1=1 ");
		sqlUtil.append("AND registNo =? ");
		sqlUtil.addParamValue(registNo);
		if(StringUtils.isNotBlank(serialNo)){
			sqlUtil.append("AND serialNo =? ");
			sqlUtil.addParamValue(serialNo);
		}
		// 查询参数
		String sql = sqlUtil.getSql();
		Object[] values = sqlUtil.getParamValues();
		// 执行查询
		List<Object[]> objects = baseDaoService.getAllBySql(sql,values);
		// 对象转换
		for(int i = 0; i<objects.size(); i++ ){
			Object[] obj = objects.get(i);
			PrpLRegistCarLossVo prpLRegistCarLossVo= new PrpLRegistCarLossVo(); 
			prpLRegistCarLossVo.setLosspart(obj[0]==null ? "" : obj[0].toString());
			prpLRegistCarLossVo.setLoss(obj[1]==null ? "" : obj[1].toString());
			prpLRegistCarLossVoList.add(prpLRegistCarLossVo);
		}
		return prpLRegistCarLossVoList;
	}

	@Override
	public List<PrpLClaimVo> findOldPrpLClaimVo(String registNo) {
		SqlJoinUtils sqlUtil = new SqlJoinUtils();
		List<PrpLClaimVo> prpLClaimVoList = new ArrayList<PrpLClaimVo>();
		sqlUtil.append("SELECT CANCELDATE,ENDCASEDATE FROM ywuser.prplclaim WHERE 1=1 ");
		sqlUtil.append("AND registNo =? ");
		sqlUtil.addParamValue(registNo);
		sqlUtil.append("order by EndCaseDate desc");
		// 查询参数
		String sql = sqlUtil.getSql();
		Object[] values = sqlUtil.getParamValues();
		// 执行查询
		List<Object[]> objects = baseDaoService.getAllBySql(sql,values);
		// 对象转换
		for(int i = 0; i<objects.size(); i++ ){
			Object[] obj = objects.get(i);
			PrpLClaimVo prpLClaimVo= new PrpLClaimVo(); 
			prpLClaimVo.setCancelTime(obj[0]==null ? null : (Date)obj[0]);
			prpLClaimVo.setEndCaseTime(obj[1]==null ? null : (Date)obj[1]);
			prpLClaimVoList.add(prpLClaimVo);
		}
		return prpLClaimVoList;
	}
    public List<PrpLCItemKindVo> findItemKindVo(String registNo,String kindCode){
    	List<PrpLCItemKindVo> prpLCItemKindVoList = new ArrayList<PrpLCItemKindVo>();
    	QueryRule queryRule = QueryRule.getInstance();
    	queryRule.addEqual("registNo", registNo);
    	queryRule.addEqual("kindCode", kindCode);
    	List<PrpLCItemKind> PrpLCItemKindList = databaseDao.findAll(PrpLCItemKind.class,queryRule);
    	if(PrpLCItemKindList!=null && PrpLCItemKindList.size()>0){
    		prpLCItemKindVoList = Beans.copyDepth().from(PrpLCItemKindList).toList(PrpLCItemKindVo.class);
    	}
    	return prpLCItemKindVoList;
    }

    @Override
    public List<PrpLRegistVo> findPrpLRegistPageVoListByReportTime(Date beginTime,Date endTime,int start,int length) {
        // TODO Auto-generated method stub
        SqlJoinUtils sqlUtil=new SqlJoinUtils();
        sqlUtil.append(" FROM PrpLRegist regist");
        sqlUtil.append(" WHERE regist.cancelTime is null and (regist.reportTime >= ? and regist.reportTime < ?)"
                + "and regist.registTaskFlag= ?");
/*        sqlUtil.append(" and (regist.reportTime >= ?");
        sqlUtil.append(" and regist.reportTime <= ?)");*/
        /*sqlUtil.append(" and regist.registTaskFlag= ?");*/
        sqlUtil.addParamValue(beginTime);
        sqlUtil.addParamValue(endTime);
        sqlUtil.addParamValue("1");
        String sql = sqlUtil.getSql();
        Object[] values = sqlUtil.getParamValues();
        
        Page<Object[]> page = databaseDao.findPageByHql(sql,start/length+1,length,values);
        
        List<PrpLRegistVo> resultVoList = new ArrayList<PrpLRegistVo>();
        for(int i = 0; i<page.getResult().size(); i++ ){
            PrpLRegistVo resultVo = new PrpLRegistVo();
            List<Object[]> obj = page.getResult();

            PrpLRegist prpLRegist = (PrpLRegist)obj.toArray()[i];
            Beans.copy().from(prpLRegist).to(resultVo);
            resultVoList.add(resultVo);
        }
        return resultVoList;
    }
    @Override
    public List<PrpCcarDriverVo> findPrpCcarDriver(String policyNo) {
        
        SqlJoinUtils sqlUtil = new SqlJoinUtils();
        List<PrpCcarDriverVo>  prpCcarDriverVos = new ArrayList<PrpCcarDriverVo>();
        sqlUtil.append("SELECT DRIVERNAME,IDENTIFYNUMBER,DRIVINGLICENSENO,ACCEPTLICENSEDATE,SEX,MARRIAGE,DRIVERADDRESS"
                + " FROM YWUSER.PRPCCARDRIVER WHERE 1=1 ");
        sqlUtil.append("AND policyNo =? ");
        sqlUtil.addParamValue(policyNo);
        // 查询参数
        String sql = sqlUtil.getSql();
        Object[] values = sqlUtil.getParamValues();
        // 执行查询
        List<Object[]> objects = baseDaoService.getAllBySql(sql,values);
        // 对象转换
        for(int i = 0; i<objects.size(); i++ ){
            Object[] obj = objects.get(i);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-M-dd HH:mm:ss"); 
            PrpCcarDriverVo prpCcarDriverVo= new PrpCcarDriverVo(); 
            prpCcarDriverVo.setDriverName(obj[0]==null ? "" : obj[0].toString());
            prpCcarDriverVo.setIdentifynumber(obj[1]==null ? "" : obj[1].toString());
            prpCcarDriverVo.setDrivingLicenseNo(obj[2]==null ? "" : obj[2].toString());
            if(obj[4] != null){
                try{
                    prpCcarDriverVo.setAcceptLicenseDate(format.parse(obj[4].toString()));
                }
                catch(ParseException e){
                	LOGGER.error("设置AcceptLicenseDate报错", e);
                }
            }
            prpCcarDriverVo.setSex(obj[5]==null ? "" : obj[5].toString());
            prpCcarDriverVo.setMarriage(obj[6]==null ? "" : obj[6].toString());
            prpCcarDriverVo.setDriverAddress(obj[7]==null ? "" : obj[7].toString());
            prpCcarDriverVos.add(prpCcarDriverVo);
        }
        return prpCcarDriverVos;
    }
    //

    @Override
    public List<PrpDagentVo> findPrpdagent(String agentCode) {
        
        SqlJoinUtils sqlUtil = new SqlJoinUtils();
        List<PrpDagentVo>  prpDagentVos = new ArrayList<PrpDagentVo>();
        sqlUtil.append("SELECT AGENTTYPE,AGENTCODE,AGENTNAME,PERMITNO FROM YWUSER.PRPDAGENT WHERE 1=1 ");
        sqlUtil.append("AND AGENTCODE =? ");
        sqlUtil.addParamValue(agentCode);
        // 查询参数
        String sql = sqlUtil.getSql();
        Object[] values = sqlUtil.getParamValues();
        // 执行查询
        List<Object[]> objects = baseDaoService.getAllBySql(sql,values);
        // 对象转换
        for(int i = 0; i<objects.size(); i++ ){
            Object[] obj = objects.get(i);
            PrpDagentVo prpDagentVo= new PrpDagentVo(); 
            prpDagentVo.setAgentType(obj[0]==null ? "" : obj[0].toString());
            prpDagentVo.setAgentCode(obj[1]==null ? "" : obj[1].toString());
            prpDagentVo.setAgentName(obj[2]==null ? "" : obj[2].toString());
            prpDagentVo.setPermitNo(obj[3]==null ? "" : obj[3].toString());
            prpDagentVos.add(prpDagentVo);
        }
        return prpDagentVos;
    }

    @Override
    public PrpLCMainVo findPrpCmainByPolicyNo(String policyNo) {
        PrpLCMainVo prpLCMainVo=new PrpLCMainVo();
        QueryRule queryRule = QueryRule.getInstance();
        queryRule.addEqual("policyNo",policyNo);
        List<PrpLCMain> prpLCMainList = databaseDao.findAll(PrpLCMain.class,queryRule);
        if(prpLCMainList !=null && prpLCMainList.size()>0){
            for(PrpLCMain prpLCMain : prpLCMainList){
                prpLCMainVo = Beans.copyDepth().from(prpLCMainList.get(0)).to(PrpLCMainVo.class);
            }
        }
        return prpLCMainVo;
    }

	@Override
	public List<PrpLCItemCarVo> findCItemCarByOther(String registNo,String riskCode) {
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo", registNo);
		queryRule.addLike("riskCode", riskCode+"%");
		List<PrpLCItemCar> itemCarList = databaseDao.findAll(PrpLCItemCar.class,queryRule);
		List<PrpLCItemCarVo> prpLCItemCarVoList = new ArrayList<PrpLCItemCarVo>();
		prpLCItemCarVoList = Beans.copyDepth().from(itemCarList).toList(PrpLCItemCarVo.class);

		return prpLCItemCarVoList;
	}

	@Override
	public List<PrpLRegistVo> findPrpLRegistByPolicyNos(List<String> policyNos) {
		List<PrpLRegistVo> prpLRegistVos = new ArrayList<PrpLRegistVo>();
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addIn("policyNo",policyNos);
		//案子没有被注销
		queryRule.addIsNull("cancelFlag");
		queryRule.addDescOrder("damageTime");
		List<PrpLRegist> PrpLRegistList = databaseDao.findAll(PrpLRegist.class,queryRule);
        if(PrpLRegistList !=null && !PrpLRegistList.isEmpty()){
        	prpLRegistVos = Beans.copyDepth().from(PrpLRegistList).toList(PrpLRegistVo.class);
        }
		return prpLRegistVos;
	}

	@Override
	public List<PrpLRegistVo> findPrpLRegistVosByRegistNos(String[] registNos) {
		List<PrpLRegistVo> prpLRegistVos = new ArrayList<PrpLRegistVo>();
		if(registNos!=null&&registNos.length>0){
			List<String> registNosList = Arrays.asList(registNos);
			QueryRule queryRule = QueryRule.getInstance();
			queryRule.addIn("registNo",registNosList);
			List<PrpLRegist> prplRegists = databaseDao.findAll(PrpLRegist.class,queryRule);
			if(prplRegists!=null&& !prplRegists.isEmpty()){
				for(PrpLRegist regist:prplRegists){
					PrpLRegistVo prpLRegistVo = Beans.copyDepth().from(regist).to(PrpLRegistVo.class);
					prpLRegistVos.add(prpLRegistVo);
				}
			}
		}
		return prpLRegistVos;
	}

    @Override
    public List<PrpLRegistVo> findValidCase(List<String> policies,Date reportTime){
    	Calendar calendar = Calendar.getInstance();
    	calendar.setTime(reportTime);
    	calendar.add(Calendar.DAY_OF_MONTH, -2);
    	Date startTime = calendar.getTime();
    	SqlJoinUtils sqlUtil = new SqlJoinUtils();
    	sqlUtil.append(" select * from prplregist r where 1=1 ");
    	if(policies!=null && policies.size()>0){
    		sqlUtil.append(" and r.policyNo in ( ");
    		for(int i=0;i<policies.size();i++){
    			if(i==0){
    				sqlUtil.append("?");
    				sqlUtil.addParamValue(policies.get(i));
    			}else{
    				sqlUtil.append(",?");
    				sqlUtil.addParamValue(policies.get(i));
    			}
    		}
    		sqlUtil.append(" ) ");
    	}
    	sqlUtil.append(" and r.reportTime >= ? and r.reportTime <= ? ");
    	sqlUtil.addParamValue(startTime);
    	sqlUtil.addParamValue(reportTime);
    	
    	sqlUtil.append(" and exists(select 1 from prplwfmain m where r.registNo = m.registNo ");
    	sqlUtil.append(" and m.flowStatus = ? ) ");
    	sqlUtil.addParamValue("N");
    	
    	String sql = sqlUtil.getSql();
		Object[] values = sqlUtil.getParamValues();
		List<PrpLRegist> registPoList = baseDaoService.getAllBySql(PrpLRegist.class, sql, values);
		
		if(registPoList!=null && registPoList.size()>0){
			List<PrpLRegistVo> registVoList = Beans.copyDepth().from(registPoList).toList(PrpLRegistVo.class);
			return registVoList;
		}
		return null;
    }

	@Override
	public List<PrpLRegistVo> findPrpLRegistVosByTime(Date beginTime,Date endTime) {
		SqlJoinUtils sqluUtils = new SqlJoinUtils();
		sqluUtils.append(" FROM PrpLRegist reg where 1=1 ");
		sqluUtils.append(" and reg.reportTime >= ? ");
		sqluUtils.append(" and reg.reportTime <= ? ");
		sqluUtils.append(" and reg.registTaskFlag = ? ");
		sqluUtils.append(" and not exists (select 1 from PrpLClaim b where b.registNo=reg.registNo)");
		sqluUtils.append(" and reg.paicreportno is null");//过滤到平安联盟案件，平安联盟案件等立案通知过来再进行自动结案
		sqluUtils.addParamValue(beginTime);
		sqluUtils.addParamValue(endTime);
		sqluUtils.addParamValue("1");
		System.out.println("++++++++++++++++++++++++++++++++++"+sqluUtils.getSql());
		List<PrpLRegist> prpLRegistList = databaseDao.findAllByHql(PrpLRegist.class,sqluUtils.getSql(),sqluUtils.getParamValues());
		List<PrpLRegistVo> prpLRegistVoList =new ArrayList<PrpLRegistVo>();
		if(prpLRegistList!=null && prpLRegistList.size()>0){
			prpLRegistVoList = Beans.copyDepth().from(prpLRegistList).toList(PrpLRegistVo.class);
		}
		
		return prpLRegistVoList;
	}

}
