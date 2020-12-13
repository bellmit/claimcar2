package ins.sino.claimcar.ilog.rule.service.spring;

import ins.framework.common.DateTime;
import ins.platform.common.service.facade.BillNoService;
import ins.platform.utils.ObjectUtils;
import ins.sino.claimcar.ilog.defloss.vo.LIlogRuleResVo;
import ins.sino.claimcar.ilog.defloss.vo.triggerInformation;
import ins.sino.claimcar.ilog.rule.service.IlogRuleService;
import ins.sino.claimcar.ilog.rule.service.RuleReturnDataSaveService;
import ins.sino.claimcar.ilog.rule.vo.IlogDataProcessingVo;
import ins.sino.claimcar.ilog.rule.vo.PrpLRuleBaseInfoIdVo;
import ins.sino.claimcar.ilog.rule.vo.PrpLRuleBaseInfoVo;
import ins.sino.claimcar.ilog.rule.vo.PrpLRuleDetailInfoIdVo;
import ins.sino.claimcar.ilog.rule.vo.PrpLRuleDetailInfoVo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;

@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"})
@Path("ruleReturnDataSaveService")
public class RuleReturnDataSaveServiceImpl implements RuleReturnDataSaveService{
	@Autowired
	BillNoService billNoService;
	@Autowired
	IlogRuleService ilogRuleService;
	
	public void dealILogResReturnData(LIlogRuleResVo lIlogRuleResVo,IlogDataProcessingVo ilogSaveDataVo) throws Exception{
		try{
			PrpLRuleBaseInfoIdVo prpLRuleBaseInfoIdVo =new PrpLRuleBaseInfoIdVo();
			PrpLRuleBaseInfoVo   prpLRuleBaseInfoVo  =new PrpLRuleBaseInfoVo();
			List<PrpLRuleDetailInfoVo> prpLRuleDetailInfoVoList =new ArrayList<PrpLRuleDetailInfoVo>();
			Date sendTime = DateTime.current();
			//规则返回信息表
			String ruleId=billNoService.getRuleBill(ilogSaveDataVo.getComCode());
			prpLRuleBaseInfoIdVo.setRuleId(ruleId);//主键id
			prpLRuleBaseInfoIdVo.setRuleNode(ilogSaveDataVo.getRuleNode());//规则触发节点
			prpLRuleBaseInfoVo.setId(prpLRuleBaseInfoIdVo);
			prpLRuleBaseInfoVo.setBusinessNo(ilogSaveDataVo.getBusinessNo());//业务号
			prpLRuleBaseInfoVo.setCompensateNo(ilogSaveDataVo.getCompensateNo());//计算书号
			prpLRuleBaseInfoVo.setComCode(ilogSaveDataVo.getComCode());//归属机构
			prpLRuleBaseInfoVo.setOperatorCode(ilogSaveDataVo.getOperatorCode());//操作人员
			prpLRuleBaseInfoVo.setRiskCode(ilogSaveDataVo.getRiskCode());//险种
			prpLRuleBaseInfoVo.setOperateType(ilogSaveDataVo.getOperateType());//操作类型  1自动  2 人工权限
			prpLRuleBaseInfoVo.setRuleState(lIlogRuleResVo.getState());//交互结果 0：交互出现异常 1：交互正常
			prpLRuleBaseInfoVo.setRuleType(ilogSaveDataVo.getRuleType());//规则类型
			prpLRuleBaseInfoVo.setUnderwriterFlag(lIlogRuleResVo.getUnderwriterflag());//1：核价自动通过 2：核价转人工 3：默认转人工 4：当前无审核权限 5：审核通过
			prpLRuleBaseInfoVo.setMinUndwrtNode(lIlogRuleResVo.getMinUndwrtNode());//最低审核通过级别
			prpLRuleBaseInfoVo.setMaxUndwrtNode(lIlogRuleResVo.getMaxUndwrtNode());//分公司最高级
			prpLRuleBaseInfoVo.setCreateTime(sendTime);//交互返回时间
			prpLRuleBaseInfoVo.setTaskId(ilogSaveDataVo.getTaskId());//工作流节点
			prpLRuleBaseInfoVo.setLicenseNo(ilogSaveDataVo.getLicenseNo());//损失方车牌号
			prpLRuleBaseInfoVo.setLossParty(ilogSaveDataVo.getLossParty());//损失方
			prpLRuleBaseInfoVo.setTriggerNode(ilogSaveDataVo.getTriggerNode());//触发节点
			prpLRuleBaseInfoVo.setFlag("");
			prpLRuleBaseInfoVo.setRuleContent(lIlogRuleResVo.getContent());
			int size=0;
			if(!ObjectUtils.isEmpty(lIlogRuleResVo.getTriggerInformation())){
				size = lIlogRuleResVo.getTriggerInformation().size();
			}
			for(int i = 0; i<size; i++ ){
				//规则返回明细信息表
				PrpLRuleDetailInfoIdVo prpLRuleDetailInfoIdVo =new PrpLRuleDetailInfoIdVo();
				PrpLRuleDetailInfoVo prpLRuleDetailInfoVo =new PrpLRuleDetailInfoVo();
				triggerInformation triggerInformation =new triggerInformation();
				triggerInformation=lIlogRuleResVo.getTriggerInformation().get(i);
				prpLRuleDetailInfoIdVo.setRuleId(ruleId);//主键id
				prpLRuleDetailInfoIdVo.setRuleNode(ilogSaveDataVo.getRuleNode());//规则触发节点
				prpLRuleDetailInfoIdVo.setSerialNo(i);//序号
				prpLRuleDetailInfoVo.setId(prpLRuleDetailInfoIdVo);
				prpLRuleDetailInfoVo.setBusinessNo(prpLRuleBaseInfoVo.getBusinessNo());//业务号
				prpLRuleDetailInfoVo.setCreateTime(prpLRuleBaseInfoVo.getCreateTime());//交互返回时间
				prpLRuleDetailInfoVo.setRuleLibrary(triggerInformation.getRuleLibrary());//规则库名称
				prpLRuleDetailInfoVo.setRulePath(triggerInformation.getRulePath());//规则路径
				prpLRuleDetailInfoVo.setRuleContent(triggerInformation.getRuleContent());//规则提示信息
				prpLRuleDetailInfoVo.setRuleEntryDetail(triggerInformation.getRuleEntryDetail());//规则名称
				prpLRuleDetailInfoVo.setRuleResult(prpLRuleBaseInfoVo.getUnderwriterFlag());//返回结果
				prpLRuleDetailInfoVo.setRuleType(triggerInformation.getRuleType());//规则类型 01:车辆核价转人工 02:车辆核价自动通过 03:车辆核价权限 99:其它
				prpLRuleDetailInfoVo.setTriggerNode(prpLRuleBaseInfoVo.getTriggerNode());
				prpLRuleDetailInfoVo.setOperatorCode(prpLRuleBaseInfoVo.getOperatorCode());
				prpLRuleDetailInfoVo.setFlag("");
				prpLRuleDetailInfoVoList.add(prpLRuleDetailInfoVo);
			}
			ilogRuleService.saveRuleInfo(prpLRuleBaseInfoVo,prpLRuleDetailInfoVoList);
		}
		catch(Exception e){
			e.printStackTrace();
			throw new Exception("规则保存异常");
		}
	}
	
}
