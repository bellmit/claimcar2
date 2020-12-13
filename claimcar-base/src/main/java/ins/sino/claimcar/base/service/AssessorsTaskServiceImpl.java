/******************************************************************************
* CREATETIME : 2019年8月14日 上午9:34:43
******************************************************************************/
package ins.sino.claimcar.base.service;

import java.math.BigDecimal;
import java.util.Date;

import javax.ws.rs.Path;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;

import ins.platform.utils.DataUtils;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.CodeConstants;
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
import ins.sino.claimcar.other.service.AssessorService;
import ins.sino.claimcar.other.service.AssessorsTaskService;
import ins.sino.claimcar.other.vo.PrpLAssessorVo;


/**
 * <pre></pre>
 * @author ★XHY
 */
@Service(protocol = { "dubbo" }, validation = "true", registry = { "default" }, timeout = 1000000)
@Path("assessorTaskService")
public class AssessorsTaskServiceImpl implements AssessorsTaskService {
    @Autowired
    private DeflossHandleService deflossHandleService;
    @Autowired
    private PersTraceHandleService persTraceHandleService;
    @Autowired
    private PropLossHandleService propLossHandleService;
    
    @Autowired
    private CheckHandleService checkHandleService;
    
    @Autowired
    private AssessorService assessorService;
	
    @Override
	public void addAssTaskOfCheck(String registNo, SysUserVo userVo) {
		checkHandleService.saveAssessors(registNo, userVo);
	}


	@Override
	public void addAssTaskOfDLoss(Object mainVo,SysUserVo userVo,String type,String taskType) {
			String registNo  = null;
			BigDecimal veriLoss = null;
			Integer serialNo = null;
			String intermCode = null;
			if(CodeConstants.CommonConst.FALSE.equals(type)){//生成新任务
				deflossHandleService.saveAssessorsTask(mainVo, userVo,taskType);
			}else if(CodeConstants.CommonConst.TRUE.equals(type)){//回写任务字段
				if (CodeConstants.CheckTaskType.CAR.equals(taskType)){//车辆定损
					PrpLDlossCarMainVo lossCarMainVo= (PrpLDlossCarMainVo)mainVo;
					registNo = lossCarMainVo.getRegistNo();
					veriLoss = lossCarMainVo.getSumVeriLossFee();
					serialNo = lossCarMainVo.getSerialNo();
					intermCode = lossCarMainVo.getIntermCode();
									
				} else if(CodeConstants.CheckTaskType.PERS.equals(taskType)){//人伤
					PrpLDlossPersTraceMainVo persTranceMainVo = (PrpLDlossPersTraceMainVo) mainVo;
					registNo = persTranceMainVo.getRegistNo();
					veriLoss = BigDecimal.ZERO;
					intermCode = persTranceMainVo.getIntermediaryInfoId();
					if(persTranceMainVo.getPrpLDlossPersTraces() != null){
						for(PrpLDlossPersTraceVo prpLDlossPersTrace : persTranceMainVo.getPrpLDlossPersTraces() ){
							if(CodeConstants.CommonConst.TRUE.equals(prpLDlossPersTrace.getValidFlag())){
								veriLoss = veriLoss.add(DataUtils.NullToZero(prpLDlossPersTrace.getSumVeriDefloss()));
							}
						}
					}
				} else if(CodeConstants.CheckTaskType.PROP.equals(taskType)){//人伤定损
					PrpLdlossPropMainVo lossPropMainVo = (PrpLdlossPropMainVo)mainVo;
					registNo = lossPropMainVo.getRegistNo();
					veriLoss = lossPropMainVo.getSumVeriLoss();
					serialNo = lossPropMainVo.getSerialNo();
					intermCode = lossPropMainVo.getInterMediaryinfoId();
				}
				//更新公估费任务表
				PrpLAssessorVo prpLAssessorOldVo=assessorService.findAssessorByLossId(registNo,taskType,serialNo,intermCode);
				if(prpLAssessorOldVo!=null){
					  prpLAssessorOldVo.setUnderWriteFlag(AcheckUnderWriteFlag.Verify);
					  prpLAssessorOldVo.setUpdateUser(userVo.getUserCode());
					  prpLAssessorOldVo.setUpdateTime(new Date());
					  prpLAssessorOldVo.setVeriLoss(veriLoss);
					  prpLAssessorOldVo.setLossDate(new Date());
					  prpLAssessorOldVo.setUnderWriteDate(new Date());
					  assessorService.saveOrUpdatePrpLAssessor(prpLAssessorOldVo,userVo);
				 }
			}
		
	}

}
