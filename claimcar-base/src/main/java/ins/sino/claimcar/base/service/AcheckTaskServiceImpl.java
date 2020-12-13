package ins.sino.claimcar.base.service;

import ins.framework.service.CodeTranService;
import ins.platform.utils.DataUtils;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.CodeConstants.AcheckUnderWriteFlag;
import ins.sino.claimcar.CodeConstants.CheckTaskType;
import ins.sino.claimcar.check.service.CheckHandleService;
import ins.sino.claimcar.losscar.service.DeflossHandleService;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarMainVo;
import ins.sino.claimcar.lossperson.service.PersTraceHandleService;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceMainVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceVo;
import ins.sino.claimcar.lossprop.service.PropLossHandleService;
import ins.sino.claimcar.lossprop.vo.PrpLdlossPropMainVo;
import ins.sino.claimcar.manager.service.ManagerService;
import ins.sino.claimcar.other.service.AcheckService;
import ins.sino.claimcar.other.service.AcheckTaskService;
import ins.sino.claimcar.other.vo.PrpLAcheckVo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
@Service(protocol = { "dubbo" }, validation = "true", registry = { "default" }, timeout = 1000000)
@Path("acheckTaskService")
public class AcheckTaskServiceImpl implements AcheckTaskService{
	private static Logger logger = LoggerFactory.getLogger(AcheckTaskServiceImpl.class);
    @Autowired
    private CheckHandleService checkHandleService;
    @Autowired
    private CodeTranService codeTranService;
    @Autowired
    private ManagerService managerService;
    
    @Autowired
	private AcheckService acheckService;
    
    @Autowired
    private DeflossHandleService deflossHandleService;
    @Autowired
    private PersTraceHandleService persTraceHandleService;
    @Autowired
    private PropLossHandleService propLossHandleService;
	
	@Override
	public void addCheckFeeTaskOfCheck(String registNo, SysUserVo userVo) {
		checkHandleService.saveCheckFee(registNo, userVo);
	}

	@Override
	public void addCheckFeeTaskOfDcar(PrpLDlossCarMainVo lossCarMainVo,SysUserVo userVo, String type) {
		if("0".equals(type)){//生成新任务
			deflossHandleService.saveAcheckFeeTask(lossCarMainVo, userVo);
		}else if("1".equals(type)){//回写任务字段
			 PrpLAcheckVo prpLAcheckOldVo=acheckService.findAcheckByLossId(lossCarMainVo.getRegistNo(),CheckTaskType.CAR,lossCarMainVo.getSerialNo(),lossCarMainVo.getCheckCode());
			  if(prpLAcheckOldVo!=null){
				prpLAcheckOldVo.setUnderWriteFlag(AcheckUnderWriteFlag.Verify);
				prpLAcheckOldVo.setUpdateUser(userVo.getUserCode());
				prpLAcheckOldVo.setUpdateTime(new Date());
				prpLAcheckOldVo.setVeriLoss(lossCarMainVo.getSumVeriLossFee());
				prpLAcheckOldVo.setLossDate(new Date());
				prpLAcheckOldVo.setUnderWriteDate(new Date());
				acheckService.saveOrUpdatePrpLAcheck(prpLAcheckOldVo,userVo);
			 }
		}
		
	}

	@Override
	public void addCheckFeeTaskOfDpers(PrpLDlossPersTraceMainVo persTraceMainVo, SysUserVo userVo,String type) {
		if("0".equals(type)){//生成新任务
			persTraceHandleService.saveAcheckFeeTask(persTraceMainVo, userVo);
		}else if("1".equals(type)){//回写任务字段
			List<PrpLAcheckVo> acheckVos= acheckService.findAcheckByRegistNoAndTaskType(persTraceMainVo.getRegistNo(),CheckTaskType.PERS);
			if(acheckVos!=null && acheckVos.size()>0){
				for(PrpLAcheckVo checkVo:acheckVos){
					checkVo.setUnderWriteFlag(AcheckUnderWriteFlag.Verify);
					checkVo.setUpdateUser(userVo.getUserCode());
					checkVo.setUpdateTime(new Date());
					checkVo.setUnderWriteDate(new Date());
					BigDecimal veriLoss = new BigDecimal("0");
					if(persTraceMainVo.getPrpLDlossPersTraces() != null){
						for(PrpLDlossPersTraceVo prpLDlossPersTrace : persTraceMainVo.getPrpLDlossPersTraces() ){
							if("1".equals(prpLDlossPersTrace.getValidFlag())){
								veriLoss = veriLoss.add(DataUtils.NullToZero(prpLDlossPersTrace.getSumVeriDefloss()));
							}
						}
					}
					checkVo.setVeriLoss(veriLoss);
					checkVo.setLossDate(new Date());
					acheckService.saveOrUpdatePrpLAcheck(checkVo,userVo);
				}
			}
		}
	}

	@Override
	public void addCheckFeeTaskOfDprop(PrpLdlossPropMainVo lossPropMainVo,SysUserVo userVo, String type) {
		if("0".equals(type)){//生成新任务
			propLossHandleService.saveAcheckFeeTask(lossPropMainVo, userVo);
		}else if("1".equals(type)){//回写任务字段
			 PrpLAcheckVo prpLAcheckOldVo=acheckService.findAcheckByLossId(lossPropMainVo.getRegistNo(),CheckTaskType.PROP,lossPropMainVo.getSerialNo(),lossPropMainVo.getCheckCode());
			  if(prpLAcheckOldVo!=null){
				prpLAcheckOldVo.setUnderWriteFlag(AcheckUnderWriteFlag.Verify);
				prpLAcheckOldVo.setUpdateUser(userVo.getUserCode());
				prpLAcheckOldVo.setUpdateTime(new Date());
				prpLAcheckOldVo.setVeriLoss(lossPropMainVo.getSumVeriLoss());
				prpLAcheckOldVo.setLossDate(new Date());
				prpLAcheckOldVo.setUnderWriteDate(new Date());
				acheckService.saveOrUpdatePrpLAcheck(prpLAcheckOldVo,userVo);
			 }
		}
		
	}
	
}
