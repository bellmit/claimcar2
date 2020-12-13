package ins.sino.claimcar.ilog.rule.service.spring;

import ins.framework.dao.database.DatabaseDao;
import ins.framework.dao.database.support.Page;
import ins.framework.dao.database.support.QueryRule;
import ins.framework.utils.Beans;
import ins.sino.claimcar.ilog.rule.po.PrpLRuleBaseInfo;
import ins.sino.claimcar.ilog.rule.po.PrpLRuleDetailInfo;
import ins.sino.claimcar.ilog.rule.service.IlogRuleService;
import ins.sino.claimcar.ilog.rule.vo.IlogDataProcessingVo;
import ins.sino.claimcar.ilog.rule.vo.PrpLRuleBaseInfoVo;
import ins.sino.claimcar.ilog.rule.vo.PrpLRuleDetailInfoVo;
import ins.sino.claimcar.ilogFinalpowerInfo.po.IlogFinalPowerInfo;
import ins.sino.claimcar.ilogFinalpowerInfo.vo.IlogFinalPowerInfoVo;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Path;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;

@Service(protocol = { "dubbo" }, validation = "true", registry = { "default" }, timeout = 100000)
@Path("ilogRuleService")
public class IlogRuleServiceImpl implements IlogRuleService{
	
	@Autowired
	private DatabaseDao databaseDao;
	
	public void saveRuleInfo(PrpLRuleBaseInfoVo   prpLRuleBaseInfoVo,List<PrpLRuleDetailInfoVo> prpLRuleDetailInfoVoList){
//		QueryRule queryRule = QueryRule.getInstance();
//		queryRule.addEqual("businessNo",prpLRuleBaseInfoVo.getBusinessNo());
//		queryRule.addEqual("id.ruleNode",prpLRuleBaseInfoVo.getId().getRuleNode());
//		queryRule.addEqual("OperateType",prpLRuleBaseInfoVo.getOperateType());
//		List<PrpLRuleBaseInfo> lRuleBaseInfos = databaseDao.findAll(PrpLRuleBaseInfo.class,queryRule);
//		if(lRuleBaseInfos!=null&&lRuleBaseInfos.size()>0){
//			for(PrpLRuleBaseInfo prpLRuleBaseInfo:lRuleBaseInfos){
//				List<PrpLRuleBaseInfoHis> prpLRuleBaseInfoHisList = new ArrayList<PrpLRuleBaseInfoHis>();
//				List<PrpLRuleDetailInfoHis> prpLRuleDetailInfoHisList = new ArrayList<PrpLRuleDetailInfoHis>();
//				PrpLRuleBaseInfoHis prpLRuleBaseInfoHis = new PrpLRuleBaseInfoHis();
//				Beans.copy().from(prpLRuleBaseInfo).to(prpLRuleBaseInfoHis);
//				prpLRuleBaseInfoHisList.add(prpLRuleBaseInfoHis);
//				String ruleId=prpLRuleBaseInfo.getId().getRuleId();
//				String ruleNode=prpLRuleBaseInfo.getId().getRuleNode();
//				QueryRule queryRulede = QueryRule.getInstance();
//				queryRulede.addEqual("ruleId",ruleId);
//				queryRulede.addEqual("id.ruleNode",ruleNode);
//				List<PrpLRuleDetailInfo> lRuleDetailInfos = databaseDao.findAll(PrpLRuleDetailInfo.class,queryRulede);
//				if(lRuleDetailInfos!=null&&lRuleDetailInfos.size()>0){
//					for(PrpLRuleDetailInfo prpLRuleDetailInfo:lRuleDetailInfos){
//						PrpLRuleDetailInfoHis prpLRuleDetailInfoHis = new PrpLRuleDetailInfoHis();
//						Beans.copy().from(prpLRuleDetailInfo).to(prpLRuleDetailInfoHis);
//						prpLRuleDetailInfoHisList.add(prpLRuleDetailInfoHis);
//					}
//				}
//				databaseDao.saveAll(PrpLRuleBaseInfoHis.class,prpLRuleBaseInfoHisList);
//				if(prpLRuleDetailInfoHisList!=null&&prpLRuleDetailInfoHisList.size()>0){
//					databaseDao.saveAll(PrpLRuleDetailInfoHis.class,prpLRuleDetailInfoHisList);
//				}
//				databaseDao.deleteByObject(PrpLRuleBaseInfo.class,prpLRuleBaseInfo);
//				if(lRuleDetailInfos!=null&&lRuleDetailInfos.size()>0){
//					databaseDao.deleteAll(PrpLRuleDetailInfo.class,lRuleDetailInfos);
//				}
//			}
//		}
		//规则信息表保存
		PrpLRuleBaseInfo prpLRuleBaseInfo =Beans.copyDepth().from(prpLRuleBaseInfoVo).to(PrpLRuleBaseInfo.class);
		databaseDao.save(PrpLRuleBaseInfo.class,prpLRuleBaseInfo);
		//规则信息明细表保存
		if(prpLRuleDetailInfoVoList!=null&&prpLRuleDetailInfoVoList.size()>0){
			 List<PrpLRuleDetailInfo> lRuleDetailInfoList   = Beans.copyDepth().from(prpLRuleDetailInfoVoList).toList(PrpLRuleDetailInfo.class);
			 databaseDao.saveAll(PrpLRuleDetailInfo.class, lRuleDetailInfoList);
		}
	}

	public IlogDataProcessingVo findRuleInfo(String businessNo,String ruleNode){
		IlogDataProcessingVo ilogDataViewVo =new IlogDataProcessingVo();
		PrpLRuleBaseInfoVo prpLRuleBaseInfoVo=new PrpLRuleBaseInfoVo();
		List<PrpLRuleDetailInfoVo> prpLRuleDetailInfoVoList =new ArrayList<PrpLRuleDetailInfoVo>();
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("businessNo",businessNo);
		queryRule.addEqual("id.ruleNode",ruleNode);
//		queryRule.addEqual("OperateType",operateType);
//		queryRule.addEqual("TaskId",taskId);
		queryRule.addDescOrder("createTime");
		List<PrpLRuleBaseInfo> lRuleBaseInfos = databaseDao.findAll(PrpLRuleBaseInfo.class,queryRule);
		if(lRuleBaseInfos!=null&&lRuleBaseInfos.size()>0){
			PrpLRuleBaseInfo prpLRuleBaseInfo=lRuleBaseInfos.get(0);//获取最新那条规则信息
			Beans.copy().from(prpLRuleBaseInfo).to(prpLRuleBaseInfoVo);
			ilogDataViewVo.setPrpLRuleBaseInfoVo(prpLRuleBaseInfoVo);
		}
		if(lRuleBaseInfos!=null&&lRuleBaseInfos.size()>0){
			//获取对应节点所有的明细信息
			for(PrpLRuleBaseInfo prpLRuleBaseInfo:lRuleBaseInfos){
			String ruleId=prpLRuleBaseInfo.getId().getRuleId();
			QueryRule queryRulede = QueryRule.getInstance();
			queryRulede.addEqual("ruleId",ruleId);
			queryRulede.addEqual("id.ruleNode",ruleNode);
			queryRulede.addDescOrder("createTime");
			List<PrpLRuleDetailInfo> lRuleDetailInfos = databaseDao.findAll(PrpLRuleDetailInfo.class,queryRulede);
			if(lRuleDetailInfos!=null&&lRuleDetailInfos.size()>0){
				for(PrpLRuleDetailInfo prpLRuleDetailInfo:lRuleDetailInfos){
					PrpLRuleDetailInfoVo prpLRuleDetailInfoVo = new PrpLRuleDetailInfoVo();
					Beans.copy().from(prpLRuleDetailInfo).to(prpLRuleDetailInfoVo);
					prpLRuleDetailInfoVoList.add(prpLRuleDetailInfoVo);
				}
			}
			ilogDataViewVo.setPrpLRuleDetailInfoVoList(prpLRuleDetailInfoVoList);
			}
		}
		return ilogDataViewVo;
	}
	
	public PrpLRuleBaseInfoVo findRuleBaseInfo(String businessNo,String ruleNode,String riskCode,String licenseNo,String taskId){
		PrpLRuleBaseInfoVo prpLRuleBaseInfoVo =null;
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("businessNo",businessNo);
		queryRule.addEqual("id.ruleNode",ruleNode);
//		if("Compe".equals(ruleNode)||"VClaim".equals(ruleNode)){
//			queryRule.addEqual("riskCode",riskCode);
//		}
		if("VClaim".equals(ruleNode)){
            queryRule.addEqual("riskCode",riskCode);
        }
		//核价 车辆 和财产的核损 需要根据车牌号做显示
		if("VPrice".equals(ruleNode)||"VLCar".equals(ruleNode)||"VLProp".equals(ruleNode)){
			queryRule.addEqual("licenseNo",licenseNo);
		}
		// 预付   预付冲销    垫付   拒赔核赔  理算冲销 只显示当前任务节点关联的核赔的规则信息
		if("PrePay".equals(ruleNode)||"PrePayWf".equals(ruleNode)||"PadPay".equals(ruleNode)||"Cancel".equals(ruleNode)||"CompeWf".equals(ruleNode)){
			queryRule.addEqual("taskId",taskId);
		}
		
		queryRule.addDescOrder("createTime");
		List<PrpLRuleBaseInfo> lRuleBaseInfos = databaseDao.findAll(PrpLRuleBaseInfo.class,queryRule);
		if(lRuleBaseInfos!=null&&lRuleBaseInfos.size()>0){
			prpLRuleBaseInfoVo =new PrpLRuleBaseInfoVo();
			PrpLRuleBaseInfo prpLRuleBaseInfo=lRuleBaseInfos.get(0);//获取最新那条规则信息
			Beans.copy().from(prpLRuleBaseInfo).to(prpLRuleBaseInfoVo);
		}
		return prpLRuleBaseInfoVo;
	}
	
	@Override
    public IlogDataProcessingVo findDetailInfoList(String businessNo,String ruleNode,String riskCode,String licenseNo,String taskId){
        IlogDataProcessingVo ilogDataProcessingVo = new IlogDataProcessingVo();
        List<PrpLRuleDetailInfoVo> prpLRuleDetailInfoVos = new ArrayList<PrpLRuleDetailInfoVo>();
        Map<String,List<PrpLRuleDetailInfoVo>> map = new HashMap<String,List<PrpLRuleDetailInfoVo>>();
        List<PrpLRuleDetailInfoVo> vPriceDetailInfoVoList = new ArrayList<PrpLRuleDetailInfoVo>();
        List<PrpLRuleDetailInfoVo> vLCarDetailInfoVoList = new ArrayList<PrpLRuleDetailInfoVo>();
        List<PrpLRuleDetailInfoVo> vLPropDetailInfoVoList = new ArrayList<PrpLRuleDetailInfoVo>();
        List<PrpLRuleDetailInfoVo> certiDetailInfoVoList = new ArrayList<PrpLRuleDetailInfoVo>();
        List<PrpLRuleDetailInfoVo> compeDetailInfoVoList = new ArrayList<PrpLRuleDetailInfoVo>();
        List<PrpLRuleDetailInfoVo> pLossDetailInfoVoList =new ArrayList<PrpLRuleDetailInfoVo>();
        List<PrpLRuleDetailInfoVo> vClaimDetailInfoVoList = new ArrayList<PrpLRuleDetailInfoVo>();

        map.put("VPrice",vPriceDetailInfoVoList);
        map.put("VLCar",vLCarDetailInfoVoList);
        map.put("VLProp",vLPropDetailInfoVoList);   
        map.put("Certi",certiDetailInfoVoList);
        map.put("Compe",compeDetailInfoVoList);
        map.put("VClaim",vClaimDetailInfoVoList);
        map.put("PLVerify",pLossDetailInfoVoList);
        map.put("PLCharge",pLossDetailInfoVoList);
        map.put("PrePay",vClaimDetailInfoVoList);
        map.put("PrePayWf",vClaimDetailInfoVoList);
        map.put("PadPay",vClaimDetailInfoVoList);
        map.put("Cancel",vClaimDetailInfoVoList);
        map.put("CompeWf",vClaimDetailInfoVoList);
        QueryRule queryRule = QueryRule.getInstance();
        queryRule.addEqual("businessNo",businessNo);
        
        
        if("VPrice".equals(ruleNode)||"VLCar".equals(ruleNode)||"VLProp".equals(ruleNode)){
            queryRule.addEqual("licenseNo",licenseNo);//核价 车辆定损 财产定损 只显示对应车牌号所有核价规则信息
        }
        if("PrePay".equals(ruleNode)||"PrePayWf".equals(ruleNode)||"PadPay".equals(ruleNode)||"Cancel".equals(ruleNode)||"CompeWf".equals(ruleNode)){
            queryRule.addEqual("id.ruleNode","VClaim");
            if(!"".equals(taskId)){
                queryRule.addEqual("taskId",taskId);
            }
        }
        queryRule.addNotEqual("triggerNode","VClaim");
        queryRule.addDescOrder("createTime");
        List<PrpLRuleBaseInfo> lRuleBaseInfos = databaseDao.findAll(PrpLRuleBaseInfo.class,queryRule);
        if(lRuleBaseInfos!=null&&lRuleBaseInfos.size()>0){
            String rNode = null;
            for(PrpLRuleBaseInfo prpLRuleBaseInfo:lRuleBaseInfos){
                rNode = prpLRuleBaseInfo.getId().getRuleNode();
                //可以把查询条件移到这里进去区分过滤
                if("VClaim".equals(ruleNode)&&"VClaim".equals(rNode)){//核赔需要区分险种
                    if(!riskCode.equals(prpLRuleBaseInfo.getRiskCode())){
                        continue;
                    }
                }
                if("3".equals(prpLRuleBaseInfo.getUnderwriterFlag())){
                    PrpLRuleDetailInfoVo prpLRuleDetailInfoVo = new PrpLRuleDetailInfoVo();
                    prpLRuleDetailInfoVo.setTriggerNode(prpLRuleBaseInfo.getTriggerNode());
                    prpLRuleDetailInfoVo.setCreateTime(prpLRuleBaseInfo.getCreateTime());
                    prpLRuleDetailInfoVo.setOperatorCode(prpLRuleBaseInfo.getOperatorCode());
                    prpLRuleDetailInfoVo.setRuleLibrary("");
                    prpLRuleDetailInfoVo.setRuleEntryDetail("");
                    if("VPrice".equals(rNode)){
                        prpLRuleDetailInfoVo.setRuleType("01");//车辆核价转人工
                    }else if ("Certi".equals(rNode)) {
                        prpLRuleDetailInfoVo.setRuleType("31");//单证转人工
                    }else if ("Compe".equals(rNode)) {
                        prpLRuleDetailInfoVo.setRuleType("41");//理算转人工
                    }else if ("VClaim".equals(rNode)) {
                        prpLRuleDetailInfoVo.setRuleType("51");//核赔转人工
                    }else if ("VLCar".equals(rNode)) {
                        prpLRuleDetailInfoVo.setRuleType("11");//车物核损转人工
                    }else if ("VLProp".equals(rNode)){//
                        prpLRuleDetailInfoVo.setRuleType("11");//车物核损转人工
                    }else if ("PLVerify".equals(rNode)||"PLCharge".equals(rNode)) {
                        prpLRuleDetailInfoVo.setRuleType("21");//人伤核损转人工
                    }else {//"PrePay".equals(ruleNode)||"PrePayWf".equals(ruleNode)||"PadPay".equals(ruleNode)||"Cancel".equals(ruleNode)||"CompeWf".equals(ruleNode)
                        prpLRuleDetailInfoVo.setRuleType("99");//其他
                    }
                    prpLRuleDetailInfoVo.setRuleResult("3");//默认转人工
                    prpLRuleDetailInfoVo.setRuleContent(prpLRuleBaseInfo.getRuleContent());
                    map.get(rNode).add(prpLRuleDetailInfoVo);
                }
                String ruleId=prpLRuleBaseInfo.getId().getRuleId();
                QueryRule queryRulede = QueryRule.getInstance();
                queryRulede.addEqual("id.ruleId",ruleId);
                queryRulede.addDescOrder("id.serialNo");
                List<PrpLRuleDetailInfo> lRuleDetailInfos = databaseDao.findAll(PrpLRuleDetailInfo.class,queryRulede);
                if(lRuleDetailInfos!=null&&lRuleDetailInfos.size()>0){
                    for(PrpLRuleDetailInfo prpLRuleDetailInfo:lRuleDetailInfos){
                        PrpLRuleDetailInfoVo prpLRuleDetailInfoVo = new PrpLRuleDetailInfoVo();
                        Beans.copy().from(prpLRuleDetailInfo).to(prpLRuleDetailInfoVo);
                        prpLRuleDetailInfoVos.add(prpLRuleDetailInfoVo);
                    }
                    map.get(rNode).addAll(prpLRuleDetailInfoVos);
                    prpLRuleDetailInfoVos.clear();
                }
            }
        }
        if("VPrice".equals(ruleNode)){
            ilogDataProcessingVo.setvPriceDetailInfoVoList(map.get("VPrice"));
        }else if ("VLCar".equals(ruleNode)) {
            ilogDataProcessingVo.setvLCarDetailInfoVoList(map.get("VLCar"));
            ilogDataProcessingVo.setvPriceDetailInfoVoList(map.get("VPrice"));
        }else if ("VLProp".equals(ruleNode)){//
            ilogDataProcessingVo.setvLPropDetailInfoVoList(map.get("VLProp"));
        }else if ("Certi".equals(ruleNode)) {
            ilogDataProcessingVo.setCertiDetailInfoVoList(map.get("Certi"));
        }else if ("Compe".equals(ruleNode)) {
            ilogDataProcessingVo.setCompeDetailInfoVoList(map.get("Compe"));
            ilogDataProcessingVo.setCertiDetailInfoVoList(map.get("Certi"));
        }else if ("VClaim".equals(ruleNode)) {
            ilogDataProcessingVo.setvClaimDetailInfoVoList(map.get("VClaim"));
            ilogDataProcessingVo.setCompeDetailInfoVoList(map.get("Compe"));
            ilogDataProcessingVo.setCertiDetailInfoVoList(map.get("Certi"));
        }else if ("PLVerify".equals(ruleNode)||"PLCharge".equals(ruleNode)) {
            ilogDataProcessingVo.setpLossDetailInfoVoList(map.get(ruleNode));
        }else {//"PrePay".equals(ruleNode)||"PrePayWf".equals(ruleNode)||"PadPay".equals(ruleNode)||"Cancel".equals(ruleNode)||"CompeWf".equals(ruleNode)
            ilogDataProcessingVo.setvClaimDetailInfoVoList(map.get(ruleNode));
        }
        return ilogDataProcessingVo;
    }
	
	
	public String findRuleContent(String ruleId,String serialNo,String ruleNode){
		String ruleContent="";
		QueryRule queryRulede = QueryRule.getInstance();
		queryRulede.addEqual("id.ruleId",ruleId);
		queryRulede.addEqual("id.serialNo",serialNo);
		queryRulede.addEqual("id.ruleNode",ruleNode);
		List<PrpLRuleDetailInfo> ruleDetailInfos = databaseDao.findAll(PrpLRuleDetailInfo.class,queryRulede);
			if(ruleDetailInfos!=null&&ruleDetailInfos.size()>0){
				PrpLRuleDetailInfo ruleDetailInfo =ruleDetailInfos.get(0);
				ruleContent=ruleDetailInfo.getRuleContent();
			}
		return ruleContent;
	}
	
	
	/* 兜底权限管理 start */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Page findIlogFinalPowerForPage(
			IlogFinalPowerInfoVo ilogFinalPowerInfo, Integer start, Integer length) {
		
		
		List<Object> ParamValues = new ArrayList<Object>();
		// hql查询语句
		StringBuffer queryString = new StringBuffer(" from IlogFinalPowerInfo io where 1 = 1 ");
		if( StringUtils.isNotBlank(ilogFinalPowerInfo.getUserCode())){
			queryString.append(" and io.userCode = ? ");
			ParamValues.add(ilogFinalPowerInfo.getUserCode());
		}
		if(StringUtils.isNotBlank(ilogFinalPowerInfo.getUserName())){
			queryString.append(" and io.userName = ? ");
			ParamValues.add(ilogFinalPowerInfo.getUserName());
		}
		
//		queryString.append(" Order by io.id ");
		//执行查询
		Page page = databaseDao.findPageByHql(queryString.toString(), (start / length + 1), length, ParamValues.toArray());
		
		for(int i = 0; i < page.getResult().size(); i++){
			IlogFinalPowerInfoVo ilogFinalPowerInfovo = new IlogFinalPowerInfoVo();
			IlogFinalPowerInfo info = (IlogFinalPowerInfo)page.getResult().get(i);
			ilogFinalPowerInfovo = Beans.copyDepth().from(info).to(IlogFinalPowerInfoVo.class);
			
			page.getResult().set(i, ilogFinalPowerInfovo);
		}
		
		return page;
	}

	@Override
	public IlogFinalPowerInfoVo findById(Long id) {

		IlogFinalPowerInfoVo ilogfinalpower = null;	
		IlogFinalPowerInfo iloginfo = databaseDao.findByPK(IlogFinalPowerInfo.class, id);
		if(iloginfo != null){
		ilogfinalpower = Beans.copyDepth().from(iloginfo).to(IlogFinalPowerInfoVo.class);
		}
		return ilogfinalpower;
	}

	
	@Override
	public Long ilogFinalUpdate(IlogFinalPowerInfoVo ilogfinal) throws Exception {
		
		IlogFinalPowerInfo info = null;
		ilogfinal.setUpdateTime(new Date());
		info =  Beans.copyDepth().from(ilogfinal).to(IlogFinalPowerInfo.class);
		if (info.getId() != 0) {
			databaseDao.update(IlogFinalPowerInfo.class, info);
		}
		
		return info.getId();
	}

	@Override
	public void deleteilog(Long id) throws Exception {
		
		databaseDao.deleteByPK(IlogFinalPowerInfo.class, id);
		
	}

	@Override
	public void addIlog(IlogFinalPowerInfoVo info) throws Exception {
		
		IlogFinalPowerInfo infos = null;
		if (info != null) {
			info.setInputTime(new Date());
			info.setUpdateTime(info.getInputTime());
			infos = Beans.copyDepth().from(info).to(IlogFinalPowerInfo.class);
			databaseDao.save(IlogFinalPowerInfo.class, infos);
		}
		
	}

	@Override
	public IlogFinalPowerInfoVo findByUserCode(String userCode) {
		IlogFinalPowerInfoVo powerInfoVo = null;
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("userCode", userCode);
		List<IlogFinalPowerInfo> powerInfo = databaseDao.findAll(IlogFinalPowerInfo.class, queryRule);
		
		if (powerInfo != null && powerInfo.size() > 0) {
			powerInfoVo = Beans.copyDepth().from(powerInfo.get(0)).to(IlogFinalPowerInfoVo.class);
		}
		
		return powerInfoVo;
	}
	/* 兜底权限管理  end  */
}
