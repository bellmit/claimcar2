/**
 * 
 */
package ins.sino.claimcar.claim.services;

import ins.framework.dao.database.DatabaseDao;
import ins.framework.dao.database.support.QueryRule;
import ins.framework.utils.Beans;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.claim.po.PrpLcancelTrace;
import ins.sino.claimcar.claim.po.PrpLrejectClaimText;
import ins.sino.claimcar.claim.service.ClaimCancelRecoverService;
import ins.sino.claimcar.claim.vo.PrpLcancelTraceVo;
import ins.sino.claimcar.claim.vo.PrpLrejectClaimTextVo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;

/**
 * @author CMQ
 *
 */
@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"})
@Path("claimCancelRecoverService")
public class ClaimCancelRecoverServiceImpl implements ClaimCancelRecoverService {
	
	@Autowired
	private DatabaseDao databaseDao;

	/* 
	 * @see ins.sino.claimcar.claim.services.ClaimCancelRecoverService#save(ins.sino.claimcar.claim.vo.PrpLcancelTraceVo)
	 * @param prpLcancelTraceVo
	 * @return
	 */
	@Override
	public BigDecimal save(PrpLcancelTraceVo prpLcancelTraceVo,SysUserVo userVo,String currentNode){
		/*PrpLcancelTrace prpLcancelTrace= new PrpLcancelTrace();

		String dealReasoon = prpLcancelTraceVo.getDealReasoon();
		if(dealReasoon.equals("1")||dealReasoon.equals("2")){
			prpLcancelTrace.setTextType("01".trim());
		}else{
			prpLcancelTrace.setTextType("02".trim());
		}
		prpLcancelTrace.setApplyDate(new Date());
		prpLcancelTrace.setApplyUserCode(SecurityUtils.getUserCode());
		prpLcancelTrace.setOperaToRCode(SecurityUtils.getUserCode());
		prpLcancelTrace.setInputTime(new Date());
		prpLcancelTrace.setMakeCom(SecurityUtils.getComCode());
		prpLcancelTrace.setComCode(SecurityUtils.getComCode());
		prpLcancelTrace.setValidFlag("1");
		prpLcancelTrace.setStatus("0");
		prpLcancelTrace.setInsertTimeForHis(new Date());
		prpLcancelTrace.setOperateTimeForHis(new Date());
		prpLcancelTrace.setFlags("0");//提交标志
		Beans.copy().from(prpLcancelTraceVo).excludeNull().to(prpLcancelTrace);*/
		PrpLcancelTrace prpLcancelTrace = new PrpLcancelTrace();

		String dealReasoon = prpLcancelTraceVo.getDealReasoon();
		prpLcancelTrace.setTextType("02".trim());
		prpLcancelTrace.setApplyDate(new Date());
		prpLcancelTrace.setApplyUserCode(userVo.getUserCode());
		prpLcancelTrace.setOperaToRCode(userVo.getUserCode());
		prpLcancelTrace.setInputTime(new Date());
		prpLcancelTrace.setMakeCom(userVo.getComCode());
		prpLcancelTrace.setComCode(userVo.getComCode());
		prpLcancelTrace.setValidFlag("1");
		prpLcancelTrace.setStatus("0");
		prpLcancelTrace.setInsertTimeForHis(new Date());
		prpLcancelTrace.setOperateTimeForHis(new Date());
		prpLcancelTrace.setFlags("2");// 提交标志
		prpLcancelTraceVo.setFlags("2");// 提交标志
		prpLcancelTrace.setId(prpLcancelTraceVo.getId());
		prpLcancelTrace.setCancelCode(prpLcancelTraceVo.getDescription());
		Beans.copy().from(prpLcancelTraceVo).excludeNull().to(prpLcancelTrace);
		databaseDao.save(PrpLcancelTrace.class, prpLcancelTrace);
		/*BigDecimal Id = prpLcancelTrace.getId();
		//根据保存报案、立案注销（恢复）轨迹表返回的id保存立案注销/拒赔申请/审核意见表PrpLrejectClaimText
		
		PrpLrejectClaimText prpLrejectClaimText= new PrpLrejectClaimText();
		prpLrejectClaimText.setClaimNo(prpLcancelTraceVo.getClaimNo());
		prpLrejectClaimText.setRegistNo(prpLcancelTraceVo.getRegistNo());
		prpLrejectClaimText.setPrplcancelTraceId(Id);
		//prpLrejectClaimText.setOperatorNode("");//
		prpLrejectClaimText.setOperatorCode(SecurityUtils.getUserCode());
		prpLrejectClaimText.setOperatorName(SecurityUtils.getUserName());
		prpLrejectClaimText.setComCode(SecurityUtils.getComCode());
		prpLrejectClaimText.setComname(SecurityUtils.getComName());
		prpLrejectClaimText.setOperateDate(new Date());
		prpLrejectClaimText.setOpinionCode("");//意见代码
		if (dealReasoon.equals("1") || dealReasoon.equals("2")) {
			prpLrejectClaimText.setOpinionName("01-案件注销 ");
			prpLrejectClaimText.setOpinionCode("01");//意见代码
		} else {
			prpLrejectClaimText.setOpinionName("02-案件拒赔 ");
			prpLrejectClaimText.setOpinionCode("02");//意见代码
		}
		prpLrejectClaimText.setValidFlag("1");
		prpLrejectClaimText.setStatus("0");//提交标志
		prpLrejectClaimText.setInsertTimeForHis(new Date());
		prpLrejectClaimText.setOperateTimeForHis(new Date());
		System.out.println(prpLrejectClaimText.getOperateTimeForHis()+"321321===");
		databaseDao.save(PrpLrejectClaimText.class, prpLrejectClaimText);
		return prpLrejectClaimText.getId();*/
		BigDecimal Id = prpLcancelTrace.getId();
		// 根据保存报案、立案注销（恢复）轨迹表返回的id保存立案注销/拒赔申请/审核意见表PrpLrejectClaimText

		PrpLrejectClaimText prpLrejectClaimText = new PrpLrejectClaimText();
		prpLrejectClaimText.setClaimNo(prpLcancelTraceVo.getClaimNo());
		prpLrejectClaimText.setRegistNo(prpLcancelTraceVo.getRegistNo());
		prpLrejectClaimText.setPrplcancelTraceId(Id);
		// prpLrejectClaimText.setOperatorNode("");//
		prpLrejectClaimText.setOperatorCode(userVo.getUserCode());
		prpLrejectClaimText.setOperatorName(userVo.getUserName());
		prpLrejectClaimText.setComCode(userVo.getComCode());
		prpLrejectClaimText.setOperateDate(new Date());
		prpLrejectClaimText.setOpinionCode("");// 意见代码
		prpLrejectClaimText.setValidFlag("1");
		prpLrejectClaimText.setInsertTimeForHis(new Date());
		prpLrejectClaimText.setOperateTimeForHis(new Date());
		//System.out.println("currentNode====="+currentNode);
		if(!"N".equals(currentNode)){
			prpLrejectClaimText.setStationName(currentNode);
			prpLrejectClaimText.setDescription(prpLcancelTraceVo.getDescription());
			//System.out.println("currentNodegggggggggg====="+currentNode);
		}
		
		databaseDao.save(PrpLrejectClaimText.class, prpLrejectClaimText);
		return prpLrejectClaimText.getId();
		}
	
	/* 
	 * @see ins.sino.claimcar.claim.services.ClaimCancelRecoverService#updates(ins.sino.claimcar.claim.vo.PrpLcancelTraceVo)
	 * @param prpLcancelTraceVo
	 * @return
	 */
	@Override
	public BigDecimal updates(PrpLcancelTraceVo prpLcancelTraceVo,SysUserVo userVo,String currentNode) {

		PrpLcancelTrace prpLcancelTrace = new PrpLcancelTrace();

		String dealReasoon = prpLcancelTraceVo.getDealReasoon();
		prpLcancelTrace.setTextType("02".trim());
		prpLcancelTrace.setApplyDate(new Date());
		prpLcancelTrace.setApplyUserCode(userVo.getUserCode());
		prpLcancelTrace.setOperaToRCode(userVo.getUserCode());
		prpLcancelTrace.setInputTime(new Date());
		prpLcancelTrace.setMakeCom(userVo.getComCode());
		prpLcancelTrace.setComCode(userVo.getComCode());
		prpLcancelTrace.setValidFlag("1");
		prpLcancelTrace.setStatus("0");
		prpLcancelTrace.setInsertTimeForHis(new Date());
		prpLcancelTrace.setOperateTimeForHis(new Date());
		prpLcancelTrace.setFlags("2");// 提交标志
		prpLcancelTrace.setId(prpLcancelTraceVo.getId());
		prpLcancelTrace.setCancelCode(prpLcancelTraceVo.getDescription());
		prpLcancelTraceVo.setFlags("2");// 提交标志
		Beans.copy().from(prpLcancelTraceVo).excludeNull().to(prpLcancelTrace);
		databaseDao.update(PrpLcancelTrace.class, prpLcancelTrace);
		BigDecimal Id = prpLcancelTrace.getId();
		// 根据保存报案、立案注销（恢复）轨迹表返回的id保存立案注销/拒赔申请/审核意见表PrpLrejectClaimText

		PrpLrejectClaimText prpLrejectClaimText = new PrpLrejectClaimText();
		prpLrejectClaimText.setClaimNo(prpLcancelTraceVo.getClaimNo());
		prpLrejectClaimText.setRegistNo(prpLcancelTraceVo.getRegistNo());
		prpLrejectClaimText.setPrplcancelTraceId(Id);
		// prpLrejectClaimText.setOperatorNode("");//
		prpLrejectClaimText.setOperatorCode(userVo.getUserCode());
		prpLrejectClaimText.setOperatorName(userVo.getUserName());
		prpLrejectClaimText.setComCode(userVo.getComCode());
		prpLrejectClaimText.setOperateDate(new Date());
		prpLrejectClaimText.setOpinionCode("");// 意见代码
	/*	if (dealReasoon.equals("1") || dealReasoon.equals("2")) {
			prpLrejectClaimText.setOpinionName("01-案件注销 ");
			prpLrejectClaimText.setOpinionCode("01");// 意见代码
		} else {
			prpLrejectClaimText.setOpinionName("02-案件拒赔 ");
			prpLrejectClaimText.setOpinionCode("02");// 意见代码
		}*/
		prpLrejectClaimText.setValidFlag("1");
		/*
		 * prpLrejectClaimText.setStatus("0");//提交标志
		 */prpLrejectClaimText.setInsertTimeForHis(new Date());
		prpLrejectClaimText.setOperateTimeForHis(new Date());
		prpLrejectClaimText.setDescription(prpLcancelTraceVo.getDescription());
		prpLrejectClaimText.setStationName(currentNode);
		databaseDao.save(PrpLrejectClaimText.class, prpLrejectClaimText);
		return prpLrejectClaimText.getId();
	}
	
	//恢复暂存
		/* 
		 * @see ins.sino.claimcar.claim.services.ClaimCancelRecoverService#zhanCun(ins.sino.claimcar.claim.vo.PrpLcancelTraceVo)
		 * @param prpLcancelTraceVo
		 * @return
		 */
		@Override
		public BigDecimal zhanCun(PrpLcancelTraceVo prpLcancelTraceVo,SysUserVo userVo){
				
				PrpLcancelTrace prpLcancelTrace= new PrpLcancelTrace();

				prpLcancelTrace.setTextType("02".trim());
				
				prpLcancelTrace.setApplyDate(new Date());
				prpLcancelTrace.setApplyUserCode(userVo.getUserCode());
				prpLcancelTrace.setOperaToRCode(userVo.getUserCode());
				prpLcancelTrace.setInputTime(new Date());
				prpLcancelTrace.setMakeCom(userVo.getComCode());
				prpLcancelTrace.setComCode(userVo.getComCode());
				prpLcancelTrace.setValidFlag("1");
				prpLcancelTrace.setStatus("0");
				prpLcancelTrace.setInsertTimeForHis(new Date());
				prpLcancelTrace.setOperateTimeForHis(new Date());
				prpLcancelTraceVo.setFlags("3");//恢复暂存标志
				prpLcancelTrace.setCancelCode(prpLcancelTraceVo.getDescription());
				Beans.copy().from(prpLcancelTraceVo).excludeNull().to(prpLcancelTrace);
				databaseDao.save(PrpLcancelTrace.class, prpLcancelTrace);
				/*BigDecimal Id = prpLcancelTrace.getId();
				//根据保存报案、立案注销（恢复）轨迹表返回的id保存立案注销/拒赔申请/审核意见表PrpLrejectClaimText
				
				PrpLrejectClaimText prpLrejectClaimText= new PrpLrejectClaimText();
				prpLrejectClaimText.setClaimNo(prpLcancelTraceVo.getClaimNo());
				prpLrejectClaimText.setRegistNo(prpLcancelTraceVo.getRegistNo());
				prpLrejectClaimText.setPrplcancelTraceId(Id);
				//prpLrejectClaimText.setOperatorNode("");//
				prpLrejectClaimText.setOperatorCode(SecurityUtils.getUserCode());
				prpLrejectClaimText.setOperatorName(SecurityUtils.getUserName());
				prpLrejectClaimText.setComCode(SecurityUtils.getComCode());
				prpLrejectClaimText.setComname(SecurityUtils.getComName());
				prpLrejectClaimText.setOperateDate(new Date());
				prpLrejectClaimText.setOpinionCode("");//意见代码
				if (dealReasoon.equals("1") || dealReasoon.equals("2")) {
					prpLrejectClaimText.setOpinionName("01-案件注销 ");
					prpLrejectClaimText.setOpinionCode("01");//意见代码
				} else {
					prpLrejectClaimText.setOpinionName("02-案件拒赔 ");
					prpLrejectClaimText.setOpinionCode("02");//意见代码
				}
				prpLrejectClaimText.setValidFlag("1");
				//prpLrejectClaimText.setStatus("1");//暂存标志
				prpLrejectClaimText.setInsertTimeForHis(new Date());
				prpLrejectClaimText.setOperateTimeForHis(new Date());
				System.out.println(prpLrejectClaimText.getOperateTimeForHis()+"321321===");
				databaseDao.save(PrpLrejectClaimText.class, prpLrejectClaimText);*/
				return prpLcancelTrace.getId();
				}
	
	
	
	//根据立案号查询
	
	/* 
	 * @see ins.sino.claimcar.claim.services.ClaimCancelRecoverService#findId(java.lang.String, java.lang.String)
	 * @param riskCode
	 * @param claimNo
	 * @return
	 */
	@Override
	public BigDecimal findId(String riskCode,String claimNo){
		QueryRule queryRule=QueryRule.getInstance();
		queryRule.addEqual("claimNo", claimNo);
		/*queryRule.addEqual("textType", "02");*/
		queryRule.addLike("textType", "02 ");
		queryRule.addDescOrder("inputTime");
		List<PrpLcancelTrace> prpLcancelTraceList= databaseDao.findAll(PrpLcancelTrace.class,queryRule);
		List<PrpLcancelTraceVo> prpLcancelTraceVos = new ArrayList<PrpLcancelTraceVo>();
		for(int i=0;i<prpLcancelTraceList.size();i++){
			PrpLcancelTraceVo prpLcancelTraceVo=Beans.copyDepth().from(prpLcancelTraceList.get(i)).to(PrpLcancelTraceVo.class);
			prpLcancelTraceVos.add(prpLcancelTraceVo);
		}
		if (prpLcancelTraceVos.size()>0) {
			return prpLcancelTraceVos.get(0).getId();
			
		} else {
			return null;
		}
		
	}
	
	//更新申请原因
	/* 
	 * @see ins.sino.claimcar.claim.services.ClaimCancelRecoverService#savePrpLrejectClaimText(ins.sino.claimcar.claim.vo.PrpLcancelTraceVo)
	 * @param prpLcancelTraceVo
	 * @return
	 */
	@Override
	public BigDecimal savePrpLrejectClaimText(PrpLcancelTraceVo prpLcancelTraceVo,SysUserVo userVo){

		String dealReasoon = prpLcancelTraceVo.getDealReasoon();
		System.out.println("==dealReasoon-=-="+dealReasoon+prpLcancelTraceVo.getDescription());
		BigDecimal Id = prpLcancelTraceVo.getId();
		//根据保存报案、立案注销（恢复）轨迹表返回的id保存立案注销/拒赔申请/审核意见表PrpLrejectClaimText
		
		PrpLrejectClaimText prpLrejectClaimText= new PrpLrejectClaimText();
		prpLrejectClaimText.setClaimNo(prpLcancelTraceVo.getClaimNo());
		prpLrejectClaimText.setRegistNo(prpLcancelTraceVo.getRegistNo());
		prpLrejectClaimText.setPrplcancelTraceId(Id);
		//prpLrejectClaimText.setOperatorNode("");//
		prpLrejectClaimText.setOperatorCode(userVo.getUserCode());
		prpLrejectClaimText.setOperatorName(userVo.getUserName());
		prpLrejectClaimText.setComCode(userVo.getComCode());
		prpLrejectClaimText.setOperateDate(new Date());
		prpLrejectClaimText.setOpinionCode("");//意见代码
		prpLcancelTraceVo.getOpinionCode();
		
		PrpLcancelTrace prpLcancelTrace = databaseDao.findByPK(PrpLcancelTrace.class, Id);
		
		
		if(prpLcancelTraceVo.getOpinionCode().equals("03")){
			prpLrejectClaimText.setOpinionName("03-提交上级");
			prpLcancelTrace.setStatus("0");
		} else if (prpLcancelTraceVo.getOpinionCode().equals("04")) {
			prpLrejectClaimText.setOpinionName("04-审核退回");
			prpLcancelTrace.setStatus("2");
		} else if(prpLcancelTraceVo.getOpinionCode().equals("05")){
			prpLcancelTrace.setStatus("1");
			prpLrejectClaimText.setOpinionName("05-审核通过");
		}
		
		prpLrejectClaimText.setDescription(prpLcancelTraceVo.getDescription());
	/*	if (dealReasoon.equals("1") || dealReasoon.equals("2")) {
			prpLrejectClaimText.setOpinionName("01-案件注销 ");
		} else {
			prpLrejectClaimText.setOpinionName("02-案件拒赔 ");
		}*/
		prpLrejectClaimText.setValidFlag("1");
		prpLrejectClaimText.setInsertTimeForHis(new Date());
		prpLrejectClaimText.setOperateTimeForHis(new Date());
		prpLrejectClaimText.setOpinionName(prpLcancelTraceVo.getOpinionName());
		prpLrejectClaimText.setStationName(prpLcancelTraceVo.getStationName());
		System.out.println(prpLrejectClaimText.getOperateTimeForHis()+"321321==="+prpLrejectClaimText.getInsertTimeForHis());
		databaseDao.save(PrpLrejectClaimText.class, prpLrejectClaimText);
		return prpLrejectClaimText.getId();
		}
	
	
	
/*	//更新
		public void updateLawSuit(PrpLLawSuitVo lawSuitVo){//更新保存医院信息
			PrpLLawSuit lawSuit = new PrpLLawSuit();
			Beans.copy().from(lawSuitVo).to(lawSuit);
			prpDHospital.setUpdateTime(new Date());
			prpDHospital.setUpdateCode(SecurityUtils.getUserCode());
			databaseDao.update(PrpLLawSuit.class, lawSuit);
		}
		
		public List<PrpLLawSuitVo> findByRegistNo(String RegistNo){
			QueryRule queryRule=QueryRule.getInstance();
			queryRule.addEqual("registNo", RegistNo);
			List<PrpLLawSuit> lawSuitList= databaseDao.findAll(PrpLLawSuit.class,queryRule);
			List<PrpLLawSuitVo> lawSuitVoList = new ArrayList<PrpLLawSuitVo>();
			for(int i=0;i<lawSuitList.size();i++){
				PrpLLawSuitVo lawSuitVo=Beans.copyDepth().from(lawSuitList.get(i)).to(PrpLLawSuitVo.class);
				lawSuitVoList.add(lawSuitVo);
			}
			return lawSuitVoList;
			
		}*/
		
		
		//根据轨迹表id查询
		
		/* 
		 * @see ins.sino.claimcar.claim.services.ClaimCancelRecoverService#findById(java.math.BigDecimal)
		 * @param id
		 * @return
		 */
		@Override
		public List<PrpLrejectClaimTextVo> findById(BigDecimal id){
			QueryRule queryRule=QueryRule.getInstance();
			queryRule.addEqual("prplcancelTraceId", id);
			queryRule.addDescOrder("operateDate");
			List<PrpLrejectClaimText> prpLrejectClaimTexts= databaseDao.findAll(PrpLrejectClaimText.class,queryRule);
			List<PrpLrejectClaimTextVo> prpListVos = new ArrayList<PrpLrejectClaimTextVo>();
			for(int i=0;i<prpLrejectClaimTexts.size();i++){
				PrpLrejectClaimTextVo prpLcancelTraceVo=Beans.copyDepth().from(prpLrejectClaimTexts.get(i)).to(PrpLrejectClaimTextVo.class);
				prpListVos.add(prpLcancelTraceVo);
			}

			return prpListVos;
			
		}
		
		//
		/* 
		 * @see ins.sino.claimcar.claim.services.ClaimCancelRecoverService#findByCancelTraceId(java.math.BigDecimal)
		 * @param id
		 * @return
		 */
		@Override
		public PrpLcancelTraceVo findByCancelTraceId(BigDecimal id){
			PrpLcancelTrace prpLcancelTrace = databaseDao.findByPK(PrpLcancelTrace.class, id);
			
			PrpLcancelTraceVo prpLcancelTraceVo=Beans.copyDepth().from(prpLcancelTrace).to(PrpLcancelTraceVo.class);
			return prpLcancelTraceVo;
			
		}
		
		
		// 更新
		/* 
		 * @see ins.sino.claimcar.claim.services.ClaimCancelRecoverService#updateCancelTrace(ins.sino.claimcar.claim.vo.PrpLcancelTraceVo)
		 * @param prpLcancelTraceVo
		 */
		@Override
		public void updateCancelTrace(PrpLcancelTraceVo prpLcancelTraceVo) {
			PrpLcancelTrace prpLcancelTrace = databaseDao.findByPK(PrpLcancelTrace.class, prpLcancelTraceVo.getId());
			prpLcancelTrace.setAandelCode(prpLcancelTraceVo.getDescription());
			prpLcancelTrace.setFlag(prpLcancelTraceVo.getFlag());
			databaseDao.update(PrpLcancelTrace.class,prpLcancelTrace);

		}
		
		//公共按钮暂存处理更新
		/* 
		 * @see ins.sino.claimcar.claim.services.ClaimCancelRecoverService#claimInitZhanC(ins.sino.claimcar.claim.vo.PrpLcancelTraceVo)
		 * @param prpLcancelTraceVo
		 */
		@Override
		public void claimInitZhanC(PrpLcancelTraceVo prpLcancelTraceVo) {
			PrpLcancelTrace prpLcancelTrace = databaseDao.findByPK(PrpLcancelTrace.class, prpLcancelTraceVo.getId());
			
			prpLcancelTrace.setCancelCode(prpLcancelTraceVo.getDescription());
			databaseDao.update(PrpLcancelTrace.class,prpLcancelTrace);

		}
		
		
		
		//根据立案号查询
		
		/* 
		 * @see ins.sino.claimcar.claim.services.ClaimCancelRecoverService#findByClaimNo(java.lang.String)
		 * @param claimNo
		 * @return
		 */
		@Override
		public PrpLcancelTraceVo findByClaimNo(String claimNo){
			QueryRule queryRule=QueryRule.getInstance();
			queryRule.addEqual("claimNo", claimNo);
			queryRule.addDescOrder("inputTime");
			List<PrpLcancelTrace> prpLcancelTraceList= databaseDao.findAll(PrpLcancelTrace.class,queryRule);
			List<PrpLcancelTraceVo> prpLcancelTraceVos = new ArrayList<PrpLcancelTraceVo>();
			for(int i=0;i<prpLcancelTraceList.size();i++){
				PrpLcancelTraceVo prpLcancelTraceVo=Beans.copyDepth().from(prpLcancelTraceList.get(i)).to(PrpLcancelTraceVo.class);
				prpLcancelTraceVos.add(prpLcancelTraceVo);
			}
			if (prpLcancelTraceVos.size()>0) {
				return prpLcancelTraceVos.get(0);
			} else {
				return null;
			}
			
		}
		
}
