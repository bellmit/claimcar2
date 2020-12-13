package ins.sino.claimcar.common.service;

import ins.framework.dao.database.DatabaseDao;
import ins.framework.dao.database.support.QueryRule;
import ins.framework.utils.Beans;
import ins.platform.common.service.facade.BillNoService;
import ins.sino.claimcar.common.po.PrpLRecLoss;
import ins.sino.claimcar.common.po.PrpLRecLossDetail;
import ins.sino.claimcar.common.po.PrpLSurvey;
import ins.sino.claimcar.flow.service.WfTaskService;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarCompVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarMainVo;
import ins.sino.claimcar.lossprop.vo.PrpLdlossPropFeeVo;
import ins.sino.claimcar.lossprop.vo.PrpLdlossPropMainVo;
import ins.sino.claimcar.recloss.service.RecLossService;
import ins.sino.claimcar.recloss.vo.PrpLRecLossDetailVo;
import ins.sino.claimcar.recloss.vo.PrpLRecLossVo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;

/**
 * 
 * @author dengkk
 * @CreateTime Feb 23, 2016
 */
@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"})
@Path("recLossService")
public class RecLossServiceImpl implements RecLossService {

	@Autowired
	DatabaseDao databaseDao;
	
	@Autowired
	private WfTaskService wfTaskService;
	
	@Autowired
	BillNoService billNoService;
	
	/* 
	 * @see ins.sino.claimcar.common.service.RecLossService#saveOrUpdate(ins.sino.claimcar.recloss.vo.PrpLRecLossVo)
	 * @param prpLRecLossVo
	 * @return
	 */
	@Override
	public String saveOrUpdate(PrpLRecLossVo prpLRecLossVo) {
		PrpLRecLoss prpLRecLossPo = databaseDao.findByPK(PrpLRecLoss.class,prpLRecLossVo.getPrpLRecLossId());
		if(prpLRecLossPo!=null){
			Beans.copy().from(prpLRecLossVo).excludeNull().to(prpLRecLossPo);
			List<PrpLRecLossDetail> prpLRecLossDetails = prpLRecLossPo.getPrpLRecLossDetails();
			for(int i = 0;i < prpLRecLossDetails.size();i ++){
				PrpLRecLossDetail prpLRecLossDetail = prpLRecLossDetails.get(i);
				for(PrpLRecLossDetailVo prpLRecLossDetailVo:prpLRecLossVo.getPrpLRecLossDetails()){
					if(prpLRecLossDetail.getId().equals(prpLRecLossDetailVo.getId())){
						Beans.copy().from(prpLRecLossDetailVo).excludeNull().to(prpLRecLossDetail);
						break;
					}
				}
			}
			prpLRecLossPo.setPrpLRecLossDetails(prpLRecLossDetails);
		}else{
			prpLRecLossPo = new PrpLRecLoss();
			prpLRecLossPo = Beans.copyDepth().from(prpLRecLossVo).to(PrpLRecLoss.class);
		}
		databaseDao.save(PrpLSurvey.class, prpLRecLossPo);
		return prpLRecLossPo.getPrpLRecLossId();
	}
	
	/* 
	 * @see ins.sino.claimcar.common.service.RecLossService#findRecLossByRecLossId(java.lang.String)
	 * @param recLossId
	 * @return
	 */
	@Override
	public PrpLRecLossVo findRecLossByRecLossId(String recLossId){
		PrpLRecLoss prpLRecLossPo = databaseDao.findByPK(PrpLRecLoss.class,recLossId);
		PrpLRecLossVo prpLRecLossVo = new PrpLRecLossVo();
		prpLRecLossVo = Beans.copyDepth().from(prpLRecLossPo).to(PrpLRecLossVo.class);
		return prpLRecLossVo;
	}
	
	/* 
	 * @see ins.sino.claimcar.common.service.RecLossService#createCarPrpLRecLossVo(ins.sino.claimcar.losscar.vo.PrpLDlossCarMainVo, java.lang.String)
	 * @param prpLDlossCarMainVo
	 * @param operatorCode
	 * @return
	 */
	@Override
	public PrpLRecLossVo createCarPrpLRecLossVo(PrpLDlossCarMainVo prpLDlossCarMainVo,String operatorCode){
		PrpLRecLossVo prpLRecLossVo = new PrpLRecLossVo();
		Date nowDate = new Date();
		//车辆换件信息
		List<PrpLDlossCarCompVo> prpLDlossCarComps = prpLDlossCarMainVo.getPrpLDlossCarComps();
		if(prpLDlossCarComps == null || prpLDlossCarComps.size() == 0){
			return null;
		}
		prpLRecLossVo.setRegistNo(prpLDlossCarMainVo.getRegistNo());
		prpLRecLossVo.setLossMainId(prpLDlossCarMainVo.getId());
		prpLRecLossVo.setRecLossType("1");//车损
		prpLRecLossVo.setRecLossFee(new BigDecimal(0));
		
		/*prpLRecLossVo.setOperatorCode(operatorCode);
		prpLRecLossVo.setOperatorName(operatorName);*/
		List<PrpLRecLossDetailVo> prpLRecLossDetails = new ArrayList<PrpLRecLossDetailVo>();
		for(PrpLDlossCarCompVo prpLDlossCarCompVo:prpLDlossCarComps){
			if("1".equals(prpLDlossCarCompVo.getRecycleFlag())){//损余回收标示为是
				PrpLRecLossDetailVo prpLRecLossDetailVo = new PrpLRecLossDetailVo();
				prpLRecLossDetailVo.setItemName(prpLDlossCarCompVo.getCompName());
				prpLRecLossDetailVo.setItemType(prpLDlossCarCompVo.getCompCode());
				prpLRecLossDetailVo.setKindCode(prpLDlossCarCompVo.getKindCode());
				prpLRecLossDetailVo.setKindName(prpLDlossCarCompVo.getKindName());
				prpLRecLossDetailVo.setLossItemId(prpLDlossCarCompVo.getId().toString());
				prpLRecLossDetailVo.setRecLossCount(prpLDlossCarCompVo.getQuantity());
				prpLRecLossDetailVo.setRecLossInd("0");//是否回收  1已回收；0未回收
				prpLRecLossDetailVo.setSumVeriLoss(prpLDlossCarCompVo.getSumVeriLoss());
				prpLRecLossDetailVo.setCreateTime(nowDate);
				prpLRecLossDetailVo.setCreateUser(operatorCode);
				prpLRecLossDetailVo.setUpdateTime(nowDate);
				prpLRecLossDetailVo.setUpdateUser(operatorCode);
				prpLRecLossDetailVo.setPrpLRecLoss(prpLRecLossVo);
				prpLRecLossDetailVo.setRecLossFee(new BigDecimal(0));
				prpLRecLossDetails.add(prpLRecLossDetailVo);
			}
		}
		if(prpLRecLossDetails.size() == 0){//没有损余回收部件，不发起损余回收
			return null;
		}
		//自动生成损余回收计算号
		prpLRecLossVo.setPrpLRecLossId(billNoService.getRecLossId("01",prpLDlossCarMainVo.getComCode()));//01车损、02物损
		prpLRecLossVo.setPrpLRecLossDetails(prpLRecLossDetails);
		
		prpLRecLossVo.setCreateTime(nowDate);
		prpLRecLossVo.setCreateUser(operatorCode);
		prpLRecLossVo.setUpdateTime(nowDate);
		prpLRecLossVo.setUpdateUser(operatorCode);
		return prpLRecLossVo;
	}
	
	/* 
	 * @see ins.sino.claimcar.common.service.RecLossService#initUpdateCarRecLossInfo(ins.sino.claimcar.losscar.vo.PrpLDlossCarMainVo, ins.sino.claimcar.recloss.vo.PrpLRecLossVo, java.lang.String)
	 * @param prpLDlossCarMainVo
	 * @param prpLRecLossVo
	 * @param operatorCode
	 */
	@Override
	public PrpLRecLossVo initUpdateCarRecLossInfo(PrpLDlossCarMainVo prpLDlossCarMainVo,PrpLRecLossVo prpLRecLossVo,String operatorCode){
		Date nowDate = new Date();
		List<PrpLRecLossDetailVo> prpLRecLossDetails = prpLRecLossVo.getPrpLRecLossDetails();
		List<PrpLDlossCarCompVo> prpLDlossCarComps = prpLDlossCarMainVo.getPrpLDlossCarComps();
		for(PrpLDlossCarCompVo prpLDlossCarCompVo:prpLDlossCarComps){
			if("1".equals(prpLDlossCarCompVo.getRecycleFlag())){//损余回收标示为是
				boolean isExsits = true;
				for(PrpLRecLossDetailVo prpLRecLossDetailVo:prpLRecLossDetails){
					if(!prpLRecLossDetailVo.getLossItemId().equals(prpLDlossCarCompVo.getId().toString())){
						isExsits = false;
					}else{
						isExsits = true;
					}
				}
				if(!isExsits){
					PrpLRecLossDetailVo prpLRecLossDetailVo = new PrpLRecLossDetailVo();
					prpLRecLossDetailVo.setPrpLRecLoss(prpLRecLossVo);
					prpLRecLossDetailVo.setItemName(prpLDlossCarCompVo.getCompName());
					prpLRecLossDetailVo.setItemType(prpLDlossCarCompVo.getCompCode());
					prpLRecLossDetailVo.setKindCode(prpLDlossCarCompVo.getKindCode());
					prpLRecLossDetailVo.setKindName(prpLDlossCarCompVo.getKindName());
					prpLRecLossDetailVo.setLossItemId(prpLDlossCarCompVo.getId().toString());
					prpLRecLossDetailVo.setRecLossCount(prpLDlossCarCompVo.getQuantity());
					prpLRecLossDetailVo.setRecLossInd("0");//是否回收  1已回收；0未回收
					prpLRecLossDetailVo.setSumVeriLoss(prpLDlossCarCompVo.getSumVeriLoss());
					prpLRecLossDetailVo.setCreateTime(nowDate);
					prpLRecLossDetailVo.setCreateUser(operatorCode);
					prpLRecLossDetailVo.setUpdateTime(nowDate);
					prpLRecLossDetailVo.setUpdateUser(operatorCode);
					prpLRecLossDetailVo.setPrpLRecLoss(prpLRecLossVo);
					prpLRecLossDetailVo.setRecLossFee(new BigDecimal(0));
					prpLRecLossDetails.add(prpLRecLossDetailVo);
				}
			}
		}
		return prpLRecLossVo;
	}
	
	/* 
	 * @see ins.sino.claimcar.common.service.RecLossService#createPropPrpLRecLossVo(ins.sino.claimcar.lossprop.vo.PrpLdlossPropMainVo, java.lang.String)
	 * @param lossPropMainVo
	 * @param operatorCode
	 * @return
	 */
	@Override
	public PrpLRecLossVo createPropPrpLRecLossVo(PrpLdlossPropMainVo lossPropMainVo,String operatorCode){
		PrpLRecLossVo prpLRecLossVo = new PrpLRecLossVo();
		Date nowDate = new Date();
		//财产损失信息
		List<PrpLdlossPropFeeVo> prpLdlossPropFees = lossPropMainVo.getPrpLdlossPropFees();
		if(prpLdlossPropFees == null || prpLdlossPropFees.size() == 0){
			return null;
		}
		prpLRecLossVo.setRegistNo(lossPropMainVo.getRegistNo());
		prpLRecLossVo.setLossMainId(lossPropMainVo.getId());
		prpLRecLossVo.setRecLossType("2");//物损
		prpLRecLossVo.setRecLossFee(new BigDecimal(0));
		
	/*	prpLRecLossVo.setOperatorCode(operatorCode);
		prpLRecLossVo.setOperatorName(operatorName);*/
		List<PrpLRecLossDetailVo> prpLRecLossDetails = new ArrayList<PrpLRecLossDetailVo>();
		for(PrpLdlossPropFeeVo prpLdlossPropFeeVo:prpLdlossPropFees){
			if("1".equals(prpLdlossPropFeeVo.getRecycleFlag())){//损余回收标示为是
				PrpLRecLossDetailVo prpLRecLossDetailVo = new PrpLRecLossDetailVo();
				prpLRecLossDetailVo.setItemName(prpLdlossPropFeeVo.getLossItemName());
				prpLRecLossDetailVo.setItemType(prpLdlossPropFeeVo.getLossSpeciesCode());
				prpLRecLossDetailVo.setKindCode(prpLdlossPropFeeVo.getRiskCode());
				prpLRecLossDetailVo.setKindName("");
				prpLRecLossDetailVo.setLossItemId(prpLdlossPropFeeVo.getId().toString());
				prpLRecLossDetailVo.setRecLossCount(prpLdlossPropFeeVo.getLossQuantity().intValue());
				prpLRecLossDetailVo.setRecLossInd("0");//是否回收  1已回收；0未回收
				prpLRecLossDetailVo.setSumVeriLoss(prpLdlossPropFeeVo.getSumVeriLoss());
				prpLRecLossDetailVo.setCreateTime(nowDate);
				prpLRecLossDetailVo.setCreateUser(operatorCode);
				prpLRecLossDetailVo.setUpdateTime(nowDate);
				prpLRecLossDetailVo.setUpdateUser(operatorCode);
				prpLRecLossDetailVo.setPrpLRecLoss(prpLRecLossVo);
				prpLRecLossDetailVo.setRecLossFee(new BigDecimal(0));
				prpLRecLossDetails.add(prpLRecLossDetailVo);
			}
		}
		if(prpLRecLossDetails.size() == 0){
			return null;
		}
		//自动生成损余回收计算号
		prpLRecLossVo.setPrpLRecLossId(billNoService.getRecLossId("02",lossPropMainVo.getComCode()));
		prpLRecLossVo.setPrpLRecLossDetails(prpLRecLossDetails);
		
		prpLRecLossVo.setCreateTime(nowDate);
		prpLRecLossVo.setCreateUser(operatorCode);
		prpLRecLossVo.setUpdateTime(nowDate);
		prpLRecLossVo.setUpdateUser(operatorCode);
		return prpLRecLossVo;
	}
	
	/* 
	 * @see ins.sino.claimcar.common.service.RecLossService#initUpdatePropRecLossInfo(ins.sino.claimcar.lossprop.vo.PrpLdlossPropMainVo, ins.sino.claimcar.recloss.vo.PrpLRecLossVo, java.lang.String)
	 * @param lossPropMainVo
	 * @param prpLRecLossVo
	 * @param operatorCode
	 */
	@Override
	public PrpLRecLossVo initUpdatePropRecLossInfo(PrpLdlossPropMainVo lossPropMainVo,PrpLRecLossVo prpLRecLossVo,String operatorCode){
		Date nowDate = new Date();
		List<PrpLRecLossDetailVo> prpLRecLossDetails = prpLRecLossVo.getPrpLRecLossDetails();
		List<PrpLdlossPropFeeVo> prpLdlossPropFees = lossPropMainVo.getPrpLdlossPropFees();
		if(prpLdlossPropFees == null || prpLdlossPropFees.size() == 0){
			return null;
		}
		for(PrpLdlossPropFeeVo prpLdlossPropFeeVo:prpLdlossPropFees){
			if("1".equals(prpLdlossPropFeeVo.getRecycleFlag())){//损余回收标示为是
				boolean isExsits = true;
				for(PrpLRecLossDetailVo prpLRecLossDetailVo:prpLRecLossDetails){
					if(!prpLRecLossDetailVo.getLossItemId().equals(prpLdlossPropFeeVo.getId().toString())){
						isExsits = false;
					}else{
						isExsits = true;
					}
				}
				if(!isExsits){
					PrpLRecLossDetailVo prpLRecLossDetailVo = new PrpLRecLossDetailVo();
					prpLRecLossDetailVo.setPrpLRecLoss(prpLRecLossVo);
					prpLRecLossDetailVo.setItemName(prpLdlossPropFeeVo.getLossItemName());
					prpLRecLossDetailVo.setItemType(prpLdlossPropFeeVo.getLossSpeciesCode());
					prpLRecLossDetailVo.setKindCode(prpLdlossPropFeeVo.getRiskCode());
					prpLRecLossDetailVo.setKindName("");
					prpLRecLossDetailVo.setLossItemId(prpLdlossPropFeeVo.getId().toString());
					prpLRecLossDetailVo.setRecLossCount(prpLdlossPropFeeVo.getLossQuantity().intValue());
					prpLRecLossDetailVo.setRecLossInd("0");//是否回收  1已回收；0未回收
					prpLRecLossDetailVo.setSumVeriLoss(prpLdlossPropFeeVo.getSumVeriLoss());
					prpLRecLossDetailVo.setCreateTime(nowDate);
					prpLRecLossDetailVo.setCreateUser(operatorCode);
					prpLRecLossDetailVo.setUpdateTime(nowDate);
					prpLRecLossDetailVo.setUpdateUser(operatorCode);
					prpLRecLossDetailVo.setPrpLRecLoss(prpLRecLossVo);
					prpLRecLossDetailVo.setRecLossFee(new BigDecimal(0));
					prpLRecLossDetails.add(prpLRecLossDetailVo);
				}
			}
		}
		return prpLRecLossVo;
	}
	
	/* 
	 * @see ins.sino.claimcar.common.service.RecLossService#findPrpLRecLossListByMainId(java.lang.String)
	 * @param recLossMainId
	 * @return
	 */
	@Override
	public List<PrpLRecLossVo> findPrpLRecLossListByMainId(String recLossMainId){
		List<PrpLRecLossVo> prpLRecLossVoList = null;
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("recLossMainId", recLossMainId);
		List<PrpLRecLoss> prpLRecLossList = databaseDao.findAll(PrpLRecLoss.class, queryRule);
		if(prpLRecLossList != null && !prpLRecLossList.isEmpty()){
			prpLRecLossVoList = Beans.copyDepth().from(prpLRecLossList).toList(PrpLRecLossVo.class);
		}
		return prpLRecLossVoList;
	}
	
	@Override
	public PrpLRecLossVo findPrpLRecLoss(String registNo,String recLossType,Long lossMainId){
		PrpLRecLossVo prpLRecLossVo = null;
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo", registNo);
		queryRule.addEqual("recLossType", recLossType);
		queryRule.addEqual("lossMainId", lossMainId);
		PrpLRecLoss prpLRecLoss = databaseDao.findUnique(PrpLRecLoss.class, queryRule);
		if(prpLRecLoss != null){
			prpLRecLossVo = Beans.copyDepth().from(prpLRecLoss).to(PrpLRecLossVo.class);
		}
		return prpLRecLossVo;
	}

	@Override
	public List<PrpLWfTaskVo> queryTaskList(List<String> handlerIdKeys,String nodeName,String registNo) {
 		return wfTaskService.queryTaskList(handlerIdKeys,nodeName,registNo);
	}

	@Override
	public List<PrpLWfTaskVo> queryTaskList(List<BigDecimal> taskIds) {
		return wfTaskService.queryTaskList(taskIds);
	}
	
}
