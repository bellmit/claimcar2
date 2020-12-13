package ins.sino.claimcar.fitting.service;

import ins.platform.utils.DataUtils;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.losscar.service.DeflossService;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarCompVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarInfoVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarMainVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarMaterialVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarRepairVo;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("claimFittingSaveService")
public class ClaimFittingSaveService {
	
	@Autowired
	DeflossService deflossService;
	
	/**
	 * 精友定损接口信息保存
	 * ☆yangkun(2015年12月21日 下午8:20:19): <br>
	 */
	public void saveCertaFittings(PrpLDlossCarMainVo lossCarVo, 
	                              PrpLDlossCarInfoVo carInfoVo )throws Exception{
		
		if(!this.validDataEmpty(lossCarVo)){
			throw new Exception("配件返回信息保存失败，唯一识别id为空！");
		}
		
		if(!this.validSaveCerta(lossCarVo)){
			throw new Exception("配件返回信息保存失败，定损合计金额错误");
		}
		//保存数据
		deflossService.saveByJyDefloss(lossCarVo);
		
		//车辆信息保存
		deflossService.saveOrUpdateDefCarInfo(carInfoVo);
		
	}
	
	/**
	 * 
	 * 精友核价接口信息保存
	 * ☆yangkun(2015年12月25日 下午4:29:16): <br>
	 */
	public void saveCheckPriceFittings(PrpLDlossCarMainVo lossCarMainVo)throws Exception {
		if(!this.validDataEmpty(lossCarMainVo)){
			throw new Exception("配件返回信息保存失败，唯一识别id为空！");
		}
		if(!this.validSavePrice(lossCarMainVo)){
			throw new Exception("配件返回信息保存失败，核价合计金额错误");
		}
		
		//保存数据
		deflossService.saveByJyDeflossCheck(lossCarMainVo,"CheckPrice");
	}
	
	/**
	 * 精友定损接口信息保存
	 * ☆yangkun(2015年12月21日 下午8:20:19): <br>
	 */
	public void saveCheckLossFittings(PrpLDlossCarMainVo lossCarVo)throws Exception{
		
		if(!this.validDataEmpty(lossCarVo)){
			throw new Exception("配件返回信息保存失败，唯一识别id为空！");
		}
		
		if(!this.validSaveLoss(lossCarVo)){
			throw new Exception("配件返回信息保存失败，核损合计金额错误");
		}
		//保存数据
		deflossService.saveByJyDeflossCheck(lossCarVo,"CheckLoss");
		
		
	}
	
	/**
	 * 定损合计验证 
	 * 明细（配件+工时+辅料+其它)
	 * ☆yangkun(2015年12月21日 下午8:15:58): <br>
	 */
	private boolean validSaveCerta(PrpLDlossCarMainVo lossCarMainVo) {
		boolean valid=false;
		
		BigDecimal fitsum = new BigDecimal("0");
		BigDecimal repairsum = new BigDecimal("0");
		BigDecimal assistsum = new BigDecimal("0");
		BigDecimal allsum = new BigDecimal("0");
		List<PrpLDlossCarCompVo> componentList = lossCarMainVo.getPrpLDlossCarComps();
		List<PrpLDlossCarRepairVo> repairList = lossCarMainVo.getPrpLDlossCarRepairs();
		List<PrpLDlossCarMaterialVo> materialList = lossCarMainVo.getPrpLDlossCarMaterials();
		
		if(componentList!=null && componentList.size()>0){
			for(PrpLDlossCarCompVo fit : componentList){
				fitsum=fitsum.add(fit.getSumDefLoss());
			}
		}
		if(repairList!=null && repairList.size()>0){
			for(PrpLDlossCarRepairVo repair : repairList){
				repairsum=repairsum.add(repair.getSumDefLoss());
			}
		}
		if(materialList!=null && materialList.size()>0){
			for(PrpLDlossCarMaterialVo ass : materialList){
				assistsum=assistsum.add(ass.getMaterialFee());
			}
		}
		
		allsum = fitsum.add(repairsum).add(assistsum);
		
		if(allsum.subtract(lossCarMainVo.getSumLossFee()).abs().compareTo(BigDecimal.ZERO)==0){
			valid=true;
		}
		
		return valid;
		
	}

	/**
	 * 为防止精友返回数据为空，造成数据库处理异常，加入此判断
	 * ☆yangkun(2015年12月21日 下午8:07:57): <br>
	 */
	private boolean validDataEmpty(PrpLDlossCarMainVo carlossMainVo) {
		boolean reFlag=true;
		List<PrpLDlossCarCompVo> componentList = carlossMainVo.getPrpLDlossCarComps();
		List<PrpLDlossCarRepairVo> repairList = carlossMainVo.getPrpLDlossCarRepairs();
		List<PrpLDlossCarMaterialVo> materialList = carlossMainVo.getPrpLDlossCarMaterials();
		
		if(componentList!=null && componentList.size()>0){
			for(PrpLDlossCarCompVo compVo : componentList){
				if(compVo.getIndId()==null||"".equals(compVo.getIndId().trim())||"0".equals(compVo.getIndId().trim())){
					reFlag = false;
					break;
				}
			}
		}
		
		if(repairList!=null && repairList.size()>0){
			for(PrpLDlossCarRepairVo repairVo : repairList){
				if(CodeConstants.RepairFlag.INNERREPAIR.equals(repairVo.getRepairFlag())){
					if( repairVo.getRepairId()==null || "".equals(repairVo.getRepairId().trim()) || "0".equals(repairVo.getRepairId().trim())){
						reFlag=false;
						break;
					}
				}
			}
		}
		
		if(materialList!=null && materialList.size()>0){
			for(PrpLDlossCarMaterialVo materVo : materialList){
				if( materVo.getAssistId()==null || "".equals(materVo.getAssistId().trim()) || "0".equals(materVo.getAssistId().trim())){
					reFlag=false;
					break;
				}
			}
		}
		
		return reFlag;
	}

	/**
	 * 
	 * 核价合计验证
	 * 明细（配件+辅料）-残值
	 * ☆yangkun(2015年12月25日 下午4:34:20): <br>
	 */
	public boolean validSavePrice(PrpLDlossCarMainVo lossCarMainVo){
		boolean valid=false;
		
		List<PrpLDlossCarCompVo> componentList = lossCarMainVo.getPrpLDlossCarComps();
		List<PrpLDlossCarMaterialVo> materialList = lossCarMainVo.getPrpLDlossCarMaterials();
		
		BigDecimal fitsum = new BigDecimal("0");
		BigDecimal assistsum = new BigDecimal("0");
		BigDecimal allsum = new BigDecimal("0");
		
		if(componentList!=null && componentList.size()>0){
			for(PrpLDlossCarCompVo fit:componentList){
				fitsum=fitsum.add(fit.getSumCheckLoss());
			}
		}
		
		if(materialList!=null && materialList.size()>0){
			for(PrpLDlossCarMaterialVo ass:materialList){
				assistsum=assistsum.add(ass.getAuditMaterialFee());
			}
		}
		
		allsum=fitsum.add(assistsum);
			
		if(allsum.subtract(lossCarMainVo.getSumVeripLoss()).abs().compareTo(BigDecimal.ZERO)==0)	
			valid=true;
		
		return valid;
	}
	
	/**
	 * 核损合计验证
	 * 明细（配件+工时+辅料+其它）-残值
	 * ☆yangkun(2015年12月25日 下午4:42:46): <br>
	 */
	public boolean validSaveLoss(PrpLDlossCarMainVo lossCarMainVo){
		boolean valid=false;
		
		BigDecimal fitsum = new BigDecimal("0");
		BigDecimal repairsum = new BigDecimal("0");
		BigDecimal assistsum = new BigDecimal("0");
		BigDecimal allsum = new BigDecimal("0");
		List<PrpLDlossCarCompVo> componentList = lossCarMainVo.getPrpLDlossCarComps();
		List<PrpLDlossCarRepairVo> repairList = lossCarMainVo.getPrpLDlossCarRepairs();
		List<PrpLDlossCarMaterialVo> materialList = lossCarMainVo.getPrpLDlossCarMaterials();
		
		if( componentList!=null && componentList.size()>0){
			for( PrpLDlossCarCompVo fit:componentList){
				fitsum = fitsum.add(fit.getSumVeriLoss());
			}
		}
		if(repairList!=null && repairList.size()>0){
			for(PrpLDlossCarRepairVo repair:repairList){
				repairsum= repairsum.add(repair.getSumVeriLoss());
			}
		}
		if(materialList!=null && materialList.size()>0){
			for(PrpLDlossCarMaterialVo ass:materialList){
				assistsum=assistsum.add(ass.getAuditLossMaterialFee());
			}
		}
		
		allsum=  fitsum.add(repairsum).add(assistsum).add(DataUtils.NullToZero(lossCarMainVo.getSumVeriOutFee()));
		
		if(allsum.subtract(lossCarMainVo.getSumVeriLossFee()).abs().compareTo(BigDecimal.ZERO)==0){
			valid=true;
		}
		
		return valid;
	}

}



