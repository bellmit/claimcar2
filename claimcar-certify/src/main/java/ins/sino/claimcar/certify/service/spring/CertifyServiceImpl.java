package ins.sino.claimcar.certify.service.spring;

import ins.framework.dao.database.DatabaseDao;
import ins.framework.dao.database.support.QueryRule;
import ins.framework.utils.Beans;
import ins.platform.utils.SqlJoinUtils;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.carplatform.constant.RequestType;
import ins.sino.claimcar.carplatform.vo.CiClaimPlatformLogVo;
import ins.sino.claimcar.certify.po.PrpLCertifyCode;
import ins.sino.claimcar.certify.po.PrpLCertifyDirect;
import ins.sino.claimcar.certify.po.PrpLCertifyItem;
import ins.sino.claimcar.certify.po.PrpLCertifyMain;
import ins.sino.claimcar.certify.service.CertifyService;
import ins.sino.claimcar.certify.vo.PrpLCertifyCodeVo;
import ins.sino.claimcar.certify.vo.PrpLCertifyDirectVo;
import ins.sino.claimcar.certify.vo.PrpLCertifyItemVo;
import ins.sino.claimcar.certify.vo.PrpLCertifyMainVo;
import ins.sino.claimcar.claim.service.ClaimTaskService;
import ins.sino.claimcar.claim.vo.PrpLClaimVo;
import ins.sino.claimcar.common.constants.CertifyTypeCode;
import ins.sino.claimcar.constant.Risk;
import ins.sino.claimcar.platform.service.PlatformReUploadService;
import ins.sino.claimcar.platform.util.SendPlatformUtil;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;

/**
 * 
 * @author dengkk
 * @CreateTime Mar 8, 2016
 */
@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"})
@Path(value = "certifyService")
public class CertifyServiceImpl implements CertifyService {
	
	private static Logger logger = LoggerFactory.getLogger(CertifyServiceImpl.class);
	
	@Autowired
	private DatabaseDao databaseDao;
	@Autowired
	private PolicyViewService policyViewService;
	@Autowired
	PlatformReUploadService reUploadService;
	@Autowired
	ClaimTaskService claimTaskService;
	
	/* 
	 * @see ins.sino.claimcar.certify.service.CertifyService#findAllPrpLCertifyCodeList()
	 * @return
	 */
	@Override
	public List<PrpLCertifyCodeVo> findAllPrpLCertifyCodeList(){
		List<PrpLCertifyCodeVo> prpLCertifyCodeVoList= new ArrayList<PrpLCertifyCodeVo>();
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("upCertifyCode","0");//单证大类
		queryRule.addAscOrder("flag");
		List<PrpLCertifyCode> prpLCertifyCodeList = databaseDao.findAll(PrpLCertifyCode.class,queryRule);
		prpLCertifyCodeVoList = Beans.copyDepth().from(prpLCertifyCodeList).toList(PrpLCertifyCodeVo.class);
		for(PrpLCertifyCodeVo prpLCertifyCodeVo:prpLCertifyCodeVoList){
			QueryRule rule = QueryRule.getInstance();
			String certifyTypeCode = prpLCertifyCodeVo.getCertifyTypeCode();
		    rule.addEqual("upCertifyCode",certifyTypeCode);//单证小类
		    queryRule.addAscOrder("flag");
		    List<PrpLCertifyCode> certifyCodeList = databaseDao.findAll(PrpLCertifyCode.class,rule);
		    prpLCertifyCodeVo.setPrpLCertifyCodeVoList(Beans.copyDepth().from(certifyCodeList).toList(PrpLCertifyCodeVo.class)); 
		}
		return prpLCertifyCodeVoList;
	}
	
	/* 
	 * @see ins.sino.claimcar.certify.service.CertifyService#findPrpLCertifyMainVo(java.lang.String)
	 * @param registNo
	 * @return
	 */
	@Override
	public PrpLCertifyMainVo findPrpLCertifyMainVo(String registNo){
		PrpLCertifyMainVo prpLCertifyMainVo = new PrpLCertifyMainVo();
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo",registNo);
		PrpLCertifyMain prpLCertifyMain = databaseDao.findUnique(PrpLCertifyMain.class,queryRule);
		if(prpLCertifyMain == null){
			return null;
		}else{
		   prpLCertifyMainVo = Beans.copyDepth().from(prpLCertifyMain).to(PrpLCertifyMainVo.class);
		   return prpLCertifyMainVo;
		}
	}
	
	/* 
	 * @see ins.sino.claimcar.certify.service.CertifyService#findPrpLCertifyMainVoByPK(java.lang.Long)
	 * @param id
	 * @return
	 */
	@Override
	public PrpLCertifyMainVo findPrpLCertifyMainVoByPK(Long id){
		PrpLCertifyMainVo prpLCertifyMainVo = new PrpLCertifyMainVo();
		PrpLCertifyMain prpLCertifyMain = databaseDao.findByPK(PrpLCertifyMain.class,id);
		prpLCertifyMainVo = Beans.copyDepth().from(prpLCertifyMain).to(PrpLCertifyMainVo.class);
		return prpLCertifyMainVo;
	}
	
	/* 
	 * @see ins.sino.claimcar.certify.service.CertifyService#saveAllPrpLCertifyItem(java.util.List)
	 * @param prpLCertifyItemVoList
	 */
	@Override
	public void saveAllPrpLCertifyItem(List<PrpLCertifyItemVo> prpLCertifyItemVoList) throws Exception {
		List<PrpLCertifyItem> prpLCertifyItemList = new ArrayList<PrpLCertifyItem>();
		String registNo = prpLCertifyItemVoList.get(0).getRegistNo();
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo",registNo);
		PrpLCertifyMain prpLCertifyMain = databaseDao.findUnique(PrpLCertifyMain.class,queryRule);
		prpLCertifyItemList = Beans.copyDepth().from(prpLCertifyItemVoList).toList(PrpLCertifyItem.class);
		if(prpLCertifyMain != null){
		  for(PrpLCertifyItem prpLCertifyItem:prpLCertifyItemList){
			  prpLCertifyItem.setPrpLCertifyMain(prpLCertifyMain);
		  }
		}
		databaseDao.saveAll(PrpLCertifyItem.class,prpLCertifyItemList);
	}
	
	@Override
	public void savePrpLCertifyItem(List<PrpLCertifyItemVo> prpLCertifyItemVoList){
		List<PrpLCertifyItem> prpLCertifyItemList = new ArrayList<PrpLCertifyItem>();
		String registNo = prpLCertifyItemVoList.get(0).getRegistNo();
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo",registNo);
		PrpLCertifyMain prpLCertifyMain = databaseDao.findUnique(PrpLCertifyMain.class,queryRule);
		for(PrpLCertifyItemVo certifyItemVo:prpLCertifyItemVoList){
			if(!existCertifyType(certifyItemVo.getRegistNo(), certifyItemVo.getCertifyTypeCode())){
				PrpLCertifyItem certifyItem = new PrpLCertifyItem();
				certifyItem = Beans.copyDepth().from(certifyItemVo).to(PrpLCertifyItem.class);
				prpLCertifyItemList.add(certifyItem);
			}
		}
		if(prpLCertifyMain != null){
		  for(PrpLCertifyItem prpLCertifyItem:prpLCertifyItemList){
			  prpLCertifyItem.setPrpLCertifyMain(prpLCertifyMain);
		  }
		}
		databaseDao.saveAll(PrpLCertifyItem.class,prpLCertifyItemList);
	}
	
	/**
	 * 根据报案号和单证分类代码判断是否存在于PrpLCertifyItem表
	 * @param registNo
	 * @param certifyTypeCode
	 * @return
	 */
	public Boolean existCertifyType(String registNo,String certifyTypeCode){
		Boolean existCertifyType;
		QueryRule queryRule = QueryRule.getInstance();
    	queryRule.addEqual("registNo",registNo);
    	queryRule.addEqual("certifyTypeCode",certifyTypeCode);
    	List<PrpLCertifyItem> certifyItemList =databaseDao.findAll(PrpLCertifyItem.class,queryRule);
    	if(certifyItemList!=null && certifyItemList.size()>0){
    		existCertifyType = true;
    	}else{
    		existCertifyType = false;
    	}
    	return existCertifyType;
	}
	
	/* 
	 * @see ins.sino.claimcar.certify.service.CertifyService#findPrpLCertifyItemVoList(java.util.Map)
	 * @param map
	 * @return
	 */
	@Override
	public List<PrpLCertifyItemVo> findPrpLCertifyItemVoList(Map<String,String> map){
		List<PrpLCertifyItemVo>  prpLCertifyItemVoList = new ArrayList<PrpLCertifyItemVo>();
		QueryRule queryRule = QueryRule.getInstance();
		for(String key:map.keySet()){
			queryRule.addEqual(key,map.get(key));
		}
		List<PrpLCertifyItem> prpLCertifyItemList = databaseDao.findAll(PrpLCertifyItem.class,queryRule);
		if(prpLCertifyItemList == null || prpLCertifyItemList.size() == 0){
			return null;
		}else{
			prpLCertifyItemVoList = Beans.copyDepth().from(prpLCertifyItemList).toList(PrpLCertifyItemVo.class);
			return prpLCertifyItemVoList;
		}
		
	}
	
	/* 
	 * @see ins.sino.claimcar.certify.service.CertifyService#saveAllPrpLCertifyDirect(java.util.List)
	 * @param prpLCertifyDirectVoList
	 */
	@Override
	public void saveAllPrpLCertifyDirect(List<PrpLCertifyDirectVo> prpLCertifyDirectVoList) throws Exception{
		List<PrpLCertifyDirect> prpLCertifyDirectList = new ArrayList<PrpLCertifyDirect>();
		prpLCertifyDirectList = Beans.copyDepth().from(prpLCertifyDirectVoList).toList(PrpLCertifyDirect.class);
	    for(PrpLCertifyDirect prpLCertifyDirect:prpLCertifyDirectList){
	    	String registNo = prpLCertifyDirect.getRegistNo();
	    	String certifyTypeCode = prpLCertifyDirect.getTypeCode();//单证类型
	    	QueryRule queryRule = QueryRule.getInstance();
	    	queryRule.addEqual("registNo",registNo);
	    	queryRule.addEqual("certifyTypeCode",certifyTypeCode);
	    	//关联单证大类
	    	PrpLCertifyItem certifyItem =databaseDao.findUnique(PrpLCertifyItem.class,queryRule);
	        prpLCertifyDirect.setPrpLCertifyItem(certifyItem);
	    }
	    databaseDao.saveAll(PrpLCertifyDirect.class,prpLCertifyDirectList);
	}
	
	/* 
	 * @see ins.sino.claimcar.certify.service.CertifyService#updatePrpLCertifyMainVo(ins.sino.claimcar.certify.vo.PrpLCertifyMainVo)
	 * @param prpLCertifyMainVo
	 */
	@Override
	public void updatePrpLCertifyMainVo(PrpLCertifyMainVo prpLCertifyMainVo){
		Long id = prpLCertifyMainVo.getId();
		PrpLCertifyMain prpLCertifyMain = databaseDao.findByPK(PrpLCertifyMain.class,id);
		Beans.copy().excludeNull().from(prpLCertifyMainVo).to(prpLCertifyMain);
		databaseDao.update(PrpLCertifyMain.class,prpLCertifyMain);
	}
	
	/* 
	 * @see ins.sino.claimcar.certify.service.CertifyService#updatePrpLCertifyItemList(java.util.List)
	 * @param prpLCertifyItemVoList
	 */
	@Override
	public void updatePrpLCertifyItemList(List<PrpLCertifyItemVo> prpLCertifyItemVoList){
		for(PrpLCertifyItemVo prpLCertifyItemVo:prpLCertifyItemVoList){
			Long id = prpLCertifyItemVo.getId();
			PrpLCertifyItem prpLCertifyItem = databaseDao.findByPK(PrpLCertifyItem.class,id);
			Beans.copy().excludeNull().from(prpLCertifyItemVo).to(prpLCertifyItem);
			databaseDao.update(PrpLCertifyItem.class,prpLCertifyItem);
		}
	}
	
	/* 
	 * @see ins.sino.claimcar.certify.service.CertifyService#deleteAllCertifyDirect(java.lang.String, java.util.List)
	 * @param registNo
	 * @param deleteCertifyDirect
	 */
	@Override
	public void deleteAllCertifyDirect(String registNo,List<Long> deleteCertifyDirect) throws Exception {
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo",registNo);
    	queryRule.addIn("id",deleteCertifyDirect);
    	List<PrpLCertifyDirect> deleteList = databaseDao.findAll(PrpLCertifyDirect.class,queryRule);
    	if(deleteList != null && deleteList.size() > 0){
			databaseDao.deleteAll(PrpLCertifyDirect.class,deleteList);
		}
	}
	
	/* 
	 * @see ins.sino.claimcar.certify.service.CertifyService#deleteAllCertifyItem(java.lang.String, java.util.List, java.util.List)
	 * @param registNo
	 * @param deleteCertifyDirect
	 * @param deleteCertifyItem
	 */
	@Override
	public void deleteAllCertifyItem(String registNo,List<Long> deleteCertifyDirect,List<String> deleteCertifyItem) throws Exception {
		List<PrpLCertifyItem> list = new ArrayList<PrpLCertifyItem>();
		for(String item:deleteCertifyItem){
			SqlJoinUtils sqlUtil=new SqlJoinUtils();
			sqlUtil.append(" FROM PrpLCertifyDirect pd");
			sqlUtil.append(" WHERE pd.typeCode = ?");
			sqlUtil.addParamValue(item);
			sqlUtil.append(" AND pd.registNo = ?");
			sqlUtil.addParamValue(registNo);
			sqlUtil.append(" AND pd.id not in (");
			for(int i= 0;i< deleteCertifyDirect.size();i++){
				if(i == 0){
					sqlUtil.append("?");
				}else{
					sqlUtil.append(",?");
				}
				sqlUtil.addParamValue(deleteCertifyDirect.get(i));
			}
			sqlUtil.append(")");
			String sql = sqlUtil.getSql();
			List<PrpLCertifyDirect> certifyDirectList = databaseDao.findAllByHql(PrpLCertifyDirect.class,sql,sqlUtil.getParamValues());
			if(certifyDirectList == null || certifyDirectList.size() == 0){
				QueryRule queryR = QueryRule.getInstance();
				queryR.addEqual("certifyTypeCode",item);
				queryR.addEqual("registNo",registNo);
				PrpLCertifyItem prpLCertifyItem = databaseDao.findUnique(PrpLCertifyItem.class,queryR);
				list.add(prpLCertifyItem);
			}
		}
		if(list.size() > 0){
			databaseDao.deleteAll(PrpLCertifyItem.class,list);
		}
	}
	
	/* 
	 * @see ins.sino.claimcar.certify.service.CertifyService#findMaxItemLossCodeOfOtherDirect(java.lang.String, java.lang.String)
	 * @param typeCode
	 * @param registNo
	 * @return
	 */
	@Override
	public long findMaxItemLossCodeOfOtherDirect(String typeCode,String registNo){
//		SqlJoinUtils sqlUtil=new SqlJoinUtils();
//		// 系统中这段代码执行时报错 ORA-01722  禅道10857
//		sqlUtil.append(" SELECT MAX(to_number(substr(pd.lossItemCode, 5))) FROM PrpLCertifyDirect pd");
//		sqlUtil.append(" WHERE pd.typeCode = ?");
//		sqlUtil.append(" AND pd.registNo = ?");
//		sqlUtil.append(" AND substr(pd.lossItemCode, 5, 6) != ?");
//		sqlUtil.addParamValue(typeCode);
//		sqlUtil.addParamValue(registNo);
//		sqlUtil.addParamValue("0");
//		Object maxCode = databaseDao.findUniqueBySql(String.class,sqlUtil.getSql(),sqlUtil.getParamValues());
		
		List<PrpLCertifyDirectVo> certifyDirectVos = null;
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo",registNo);
		queryRule.addEqual("typeCode", typeCode);
		List<PrpLCertifyDirect> certifyDirects = databaseDao.findAll(PrpLCertifyDirect.class, queryRule);
		
		long maxCode = 0;
		if(certifyDirects == null || certifyDirects.size() == 0){
			return maxCode;
		}
		certifyDirectVos = Beans.copyDepth().from(certifyDirects).toList(PrpLCertifyDirectVo.class);
		for (int i = 0; i < certifyDirectVos.size(); i++) {
			PrpLCertifyDirectVo certifyDirect = certifyDirectVos.get(i);
			if (certifyDirect != null && certifyDirect.getLossItemCode() != null && certifyDirect.getLossItemCode().length() >= 5) {
				try {
					long maxCodeTmp = Long.parseLong(certifyDirect.getLossItemCode().substring(4));
					if (maxCode < maxCodeTmp) {
						maxCode = maxCodeTmp;
					}
				} catch (Exception e) {
					logger.info(registNo + "自定义单证lossItemCode[" + i + "]=" + certifyDirect.getLossItemCode() + "最大序号(第5位及后续内容)转换成Long类型异常！", e);
					e.printStackTrace();
				}
			}
		}
		return maxCode;
	}
	
	/* 
	 * @see ins.sino.claimcar.certify.service.CertifyService#saveOtherPrpLCertifyDirect(java.util.List)
	 * @param otherPrpLCertifyDirect
	 */
	@Override
	public void saveOtherPrpLCertifyDirect(List<PrpLCertifyDirectVo> otherPrpLCertifyDirect,SysUserVo userVo) throws Exception {
		List<PrpLCertifyDirect> prpLCertifyDirectList = new ArrayList<PrpLCertifyDirect>();
		prpLCertifyDirectList = Beans.copyDepth().from(otherPrpLCertifyDirect).toList(PrpLCertifyDirect.class);
		QueryRule queryRule = QueryRule.getInstance();
		String registNo = prpLCertifyDirectList.get(0).getRegistNo();
		queryRule.addEqual("registNo",registNo);
    	queryRule.addEqual("certifyTypeCode",CertifyTypeCode.C099.toString());
    	PrpLCertifyItem prpLCertifyItem = databaseDao.findUnique(PrpLCertifyItem.class,queryRule);
    	if(prpLCertifyItem != null){
    		for(PrpLCertifyDirect prpLCertifyDirect:prpLCertifyDirectList){
    			prpLCertifyDirect.setPrpLCertifyItem(prpLCertifyItem);
    		}
    		databaseDao.saveAll(PrpLCertifyDirect.class,prpLCertifyDirectList);
    	}else{
    		QueryRule rule = QueryRule.getInstance();
    		rule.addEqual("registNo",registNo);
    		PrpLCertifyMain prpLCertifyMain = databaseDao.findUnique(PrpLCertifyMain.class,rule);
    		Date nowDate = new Date();
    		prpLCertifyItem = new  PrpLCertifyItem();
			prpLCertifyItem.setCertifyTypeCode(CertifyTypeCode.C099.toString());
			prpLCertifyItem.setCertifyTypeName("其他");
			prpLCertifyItem.setRegistNo(registNo);
			prpLCertifyItem.setDirectFlag("0");//未收集齐全
			prpLCertifyItem.setValidFlag("1");//有效
			prpLCertifyItem.setCreateTime(nowDate);
			prpLCertifyItem.setCreateUser(userVo.getUserCode());
			prpLCertifyItem.setUpdateTime(nowDate);
			prpLCertifyItem.setUpdateUser(userVo.getUserCode());
			for(PrpLCertifyDirect prpLCertifyDirect:prpLCertifyDirectList){
	    		prpLCertifyDirect.setPrpLCertifyItem(prpLCertifyItem);
	    	}
	    	prpLCertifyItem.setPrpLCertifyDirects(prpLCertifyDirectList);
	    	prpLCertifyItem.setPrpLCertifyMain(prpLCertifyMain);
	    	databaseDao.save(PrpLCertifyItem.class,prpLCertifyItem);
    	}
	}
	
	/**
	 * 查找上传图片类型
	 * @return
	 *//*
	public List<PrpLCertifyCodeVo> findImgPrpLCertifyCodeList(){
		List<PrpLCertifyCodeVo> prpLCertifyCodeVoList= new ArrayList<PrpLCertifyCodeVo>();
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("upCertifyCode","P");//单证大类
		List<PrpLCertifyCode> prpLCertifyCodeList = databaseDao.findAll(PrpLCertifyCode.class,queryRule);
		prpLCertifyCodeVoList = Beans.copyDepth().from(prpLCertifyCodeList).toList(PrpLCertifyCodeVo.class);
		return prpLCertifyCodeVoList;
	}*/
	/* 
	 * @see ins.sino.claimcar.certify.service.CertifyService#findPrpLCertifyDirectVoList(java.util.Map)
	 * @param map
	 * @return
	 */
	@Override
	public List<PrpLCertifyDirectVo> findPrpLCertifyDirectVoList(Map<String,String> map){
		// 因为新影像上线之后，lossItemCode数据组成发生变化，包含了二级编码（单证大类编码），需要兼容新旧lossItemCode
		QueryRule queryRule = null;
		List<PrpLCertifyDirect> prpLCertifyDirectList = new ArrayList<PrpLCertifyDirect>();
		if (map != null) {
			String regsitNo = map.get("registNo");
			String lossItemCode = map.get("lossItemCode");
			if (lossItemCode != null) {
				String[] lossItemCodeArr = lossItemCode.split(",");
				for (int i = 0; i < lossItemCodeArr.length; i++) {
					queryRule = QueryRule.getInstance();
					if (regsitNo != null && !"".equals(regsitNo) && lossItemCodeArr[i] != null && !"".equals(lossItemCodeArr[i])) {
						queryRule.addEqual("registNo", regsitNo);
						queryRule.addLike("lossItemCode", "%" + lossItemCodeArr[i] + "%");
						List<PrpLCertifyDirect> certifyDirectList = databaseDao.findAll(PrpLCertifyDirect.class, queryRule);
						for (PrpLCertifyDirect direct : certifyDirectList) {
							prpLCertifyDirectList.add(direct);
						}
					}
				}
			}
		}
		
//		for(String key:map.keySet()){
//			if("in".equals(key)){
//				String val[] = map.get(key).split(",");
//				List<String> paramList = new ArrayList<String>();
//				for(int i = 1 ;i < val.length;i ++){
//					paramList.add(val[i]);
//				}
//				queryRule.addIn(val[0],paramList);
//			}else{
//				queryRule.addEqual(key,map.get(key));
//			}
//		}
//		List<PrpLCertifyDirect> prpLCertifyDirectList = databaseDao.findAll(PrpLCertifyDirect.class,queryRule);
		if (prpLCertifyDirectList == null || prpLCertifyDirectList.size() == 0) {
			return null;
		}
		List<PrpLCertifyDirectVo> prpLCertifyDirectVoList = new ArrayList<PrpLCertifyDirectVo>();
		prpLCertifyDirectVoList = Beans.copyDepth().from(prpLCertifyDirectList).toList(PrpLCertifyDirectVo.class);
		return prpLCertifyDirectVoList;
	}
	
	
	/* 
	 * @see ins.sino.claimcar.certify.service.CertifyService#submitCertify(ins.sino.claimcar.certify.vo.PrpLCertifyMainVo)
	 * @param prpLCertifyMainVo
	 * @return
	 */
	@Override
	public PrpLCertifyMainVo submitCertify(PrpLCertifyMainVo prpLCertifyMainVo) {
		/**
		 * 提交主表的时候判断是否有已经勾选的单证信息，如果有和主表关联起来。起因是查勘未提交之前可以勾选单证，
		 * 那时候还没有生成单证任务
		 */
		PrpLCertifyMainVo certifyMainVo;
		PrpLCertifyMain prpLCertifyMain = new PrpLCertifyMain();
		String registNo = prpLCertifyMainVo.getRegistNo();
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo",registNo);
		List<PrpLCertifyMain> certifymains=databaseDao.findAll(PrpLCertifyMain.class, queryRule);
		if(certifymains!=null && certifymains.size()>0){//防止同一时刻两个线程保存了相同的单证主表数据，造成脏数据
			certifyMainVo = Beans.copyDepth().from(certifymains.get(0)).to(PrpLCertifyMainVo.class);
		}else{
			List<PrpLCertifyItem> prpLCertifyItemList = databaseDao.findAll(PrpLCertifyItem.class,queryRule);
			prpLCertifyMain = Beans.copyDepth().from(prpLCertifyMainVo).to(PrpLCertifyMain.class);
			if(prpLCertifyItemList != null){
				for(PrpLCertifyItem prpLCertifyItem:prpLCertifyItemList){
					prpLCertifyItem.setPrpLCertifyMain(prpLCertifyMain);
				}
				prpLCertifyMain.setPrpLCertifyItems(prpLCertifyItemList);
			}
			databaseDao.save(PrpLCertifyMain.class,prpLCertifyMain);
			certifyMainVo = Beans.copyDepth().from(prpLCertifyMain).to(PrpLCertifyMainVo.class);
		}
		
		return certifyMainVo;
	}

    @Override
    public List<PrpLCertifyCodeVo> findCertifyCodeVoByMustUpload(String certifyTypeCode) {
        QueryRule queryRule = QueryRule.getInstance();
        queryRule.addEqual("certifyTypeCode",certifyTypeCode);
        List<PrpLCertifyCode> prpLCertifyCodeList = databaseDao.findAll(PrpLCertifyCode.class,queryRule);
        List<PrpLCertifyCodeVo> prpLCertifyCodeVoList = new ArrayList<PrpLCertifyCodeVo>();
        prpLCertifyCodeVoList = Beans.copyDepth().from(prpLCertifyCodeList).toList(PrpLCertifyCodeVo.class);
        return prpLCertifyCodeVoList; 
    }

	@Override
	public boolean isPassPlatform(String registNo) {
		List<PrpLCMainVo> prplCMainList = policyViewService.getPolicyAllInfo(registNo);
		String flag = "0";//0表示不需查旧理赔，1表示需要查旧理赔
		if(registNo.length()==21){
			flag = "1";
		}
		
		for(PrpLCMainVo prplCMain:prplCMainList){
			if(SendPlatformUtil.isMor(prplCMain)){
				String comCode = prplCMain.getComCode();
				String  riskCode = prplCMain.getRiskCode();
				//上海
				if(comCode.startsWith("22")){
					//上海报案节点
					String reqType_regist = Risk.DQZ.equals(riskCode) ? 
							RequestType.RegistInfoCI_SH.getCode() : RequestType.RegistInfoBI_SH.getCode();
					CiClaimPlatformLogVo registLog = reUploadService.findPlatformLog(reqType_regist, registNo, "0", flag);
					
					//上海立案
					String reqType_claim = Risk.DQZ.equals(riskCode) ? 
							RequestType.ClaimInfoCI_SH.getCode() : RequestType.ClaimInfoBI_SH.getCode();
					CiClaimPlatformLogVo claimLog = reUploadService.findPlatformLog(reqType_claim, registNo, "0", flag);
					
					//上海查勘定核损
					String reqType_loss = Risk.DQZ.equals(riskCode) ? 
							RequestType.LossInfoCI_SH.getCode() : RequestType.LossInfoBI_SH.getCode();
					CiClaimPlatformLogVo lossLog = reUploadService.findPlatformLog(reqType_loss, registNo, "0", flag);
					if(registLog!=null || claimLog!=null || lossLog!=null){
						return false;
					}
				}else{

					PrpLClaimVo claimVo = claimTaskService.findprpLClaimVoListByRegistAndPolicyNo(registNo, prplCMain.getPolicyNo(), null).get(0);
					
					String reqType_regist = Risk.DQZ.equals(riskCode) ? 
							RequestType.RegistInfoCI.getCode() : RequestType.RegistInfoBI.getCode();
					CiClaimPlatformLogVo registLog = reUploadService.findPlatformLog(reqType_regist, registNo, "0", flag);
					
					String reqType_claim = Risk.DQZ.equals(riskCode) ? 
							RequestType.ClaimCI.getCode() : RequestType.ClaimBI.getCode();
					CiClaimPlatformLogVo claimLog = reUploadService.findPlatformLog(reqType_claim, claimVo.getClaimNo(), "0", flag);
					
					String reqType_check = Risk.DQZ.equals(riskCode) ? 
							RequestType.CheckCI.getCode() : RequestType.CheckBI.getCode();
					CiClaimPlatformLogVo checkLog = reUploadService.findPlatformLog(reqType_check, registNo, "0", flag);
					
					String reqType_loss = Risk.DQZ.equals(riskCode) ? 
							RequestType.LossInfoCI.getCode() : RequestType.LossInfoBI.getCode();
					CiClaimPlatformLogVo lossLog = reUploadService.findPlatformLog(reqType_loss, registNo, "0", flag);
					if(registLog!=null || claimLog!=null || checkLog!=null || lossLog!=null){
						return false;
					}
				}
			}
		}	
		return true;
	}

}
