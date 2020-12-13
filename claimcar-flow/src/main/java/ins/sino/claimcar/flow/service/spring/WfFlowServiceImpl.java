package ins.sino.claimcar.flow.service.spring;

import ins.framework.common.ResultPage;
import ins.framework.dao.database.DatabaseDao;
import ins.framework.dao.database.support.Page;
import ins.framework.dao.database.support.QueryRule;
import ins.framework.service.CodeTranService;
import ins.framework.utils.Beans;
import ins.platform.saa.vo.SaaFactorPowerVo;
import ins.platform.utils.SqlJoinUtils;
import ins.platform.vo.SqlParamVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.claim.po.PrpLCompensate;
import ins.sino.claimcar.commom.vo.StringUtils;
import ins.sino.claimcar.flow.po.PrpLWfTaskIn;
import ins.sino.claimcar.flow.po.PrpLWfTaskOut;
import ins.sino.claimcar.flow.po.PrpLWfTaskQuery;
import ins.sino.claimcar.flow.service.WfFlowService;
import ins.sino.claimcar.flow.vo.PrpLWfTaskQueryVo;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.flow.vo.WfFlowNodeVo;
import ins.sino.claimcar.flow.vo.WfTaskQueryResultVo;
import ins.sino.claimcar.updateVIN.po.PrpLRegist;
import ins.sino.claimcar.updateVIN.po.PrpLpolicyNoView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Path;

import org.antlr.analysis.SemanticContext.AND;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
/**
 * 
 * @author dengkk
 *
 */
@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"})
@Path(value = "wfFlowService")
public class WfFlowServiceImpl implements WfFlowService {
	
	private static Logger logger = LoggerFactory.getLogger(WfFlowServiceImpl.class);

	@Autowired
	private DatabaseDao databaseDao;
	@Autowired
	private CodeTranService codeTranService;
	/* 
	 * @see ins.sino.claimcar.flow.service.WfFlowService#findAllWfTaskByFlowId(java.lang.String)
	 * @param flowId
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<WfFlowNodeVo> findAllWfTaskByFlowId(String flowId) throws Exception {
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("flowId",flowId);
		List<PrpLWfTaskIn> prpLWfTaskInList = databaseDao.findAll(PrpLWfTaskIn.class, queryRule);
		List<PrpLWfTaskOut> prpLWfTaskOutList = databaseDao.findAll(PrpLWfTaskOut.class, queryRule);
		List<WfFlowNodeVo> inList = new ArrayList<WfFlowNodeVo>();
		inList = Beans.copyDepth().from(prpLWfTaskInList).toList(WfFlowNodeVo.class);
		List<WfFlowNodeVo> outList = new ArrayList<WfFlowNodeVo>();
		outList = Beans.copyDepth().from(prpLWfTaskOutList).toList(WfFlowNodeVo.class);
		inList.addAll(outList);
		return inList;
	}
	private boolean  addTimeControl(PrpLWfTaskQueryVo taskQueryVo){
		String registNo = taskQueryVo.getRegistNo(); //报案号
		String policyNo = taskQueryVo.getPolicyNo(); //保单号
		String licenseNo = taskQueryVo.getLicenseNo(); //标的车牌号
		String insuredName = taskQueryVo.getInsuredName(); //被保险人
		String claimNo = taskQueryVo.getClaimNo(); //车架号
		String frameNo = taskQueryVo.getFrameNo(); //立案号
		if(taskQueryVo.getHandleStatus().equals("0")){
			//如果报案号、保单号、标的车牌号、被保险人、车架号、立案号符合添加忽略出险时间查询
			if((StringUtils.isNotBlank(registNo) && registNo.trim().length() >= 4)
				|| (StringUtils.isNotBlank(policyNo) && policyNo.trim().length()>=4) 
				|| (StringUtils.isNotBlank(licenseNo) && licenseNo.trim().length() >= 4)
				|| (StringUtils.isNotBlank(claimNo) && claimNo.trim().length()>= 21 )
				|| (StringUtils.isNotBlank(insuredName) && insuredName.trim().length() >= 2)
				|| (StringUtils.isNotBlank(frameNo) && frameNo.trim().length()>= 4)){
				return false;
			}	
		} else{
			//如果报案号、保单号、标的车牌号、被保险人、车架号、立案号符合添加则忽略出险时间查询
			if((StringUtils.isNotBlank(registNo) && registNo.trim().length() >= 21)
				|| (StringUtils.isNotBlank(policyNo) && policyNo.trim().length()>=21)
				|| (StringUtils.isNotBlank(licenseNo) && licenseNo.trim().length() >= 6)
				|| (StringUtils.isNotBlank(claimNo) && claimNo.trim().length()>= 21)
				|| (StringUtils.isNotBlank(insuredName) && insuredName.trim().length() >= 2)
				|| (StringUtils.isNotBlank(frameNo) && frameNo.trim().length()>= 10)){
				return false;
			} 
		}
		return true;
	}
	/* 
	 * @see ins.sino.claimcar.flow.service.WfFlowService#findFlowForPage(ins.sino.claimcar.flow.vo.PrpLWfTaskQueryVo)
	 * @param taskQueryVo
	 * @return
	 * @throws Exception
	 */
	@Override
	public ResultPage<WfTaskQueryResultVo> findFlowForPage(PrpLWfTaskQueryVo taskQueryVo,Map<String,List<SaaFactorPowerVo>> factorPowerMap) throws Exception {

		SqlJoinUtils sqlUtil=new SqlJoinUtils();
		sqlUtil.append(" FROM PrpLWfTaskQuery pq,PrpLWfMain pm WHERE pq.flowId = pm.flowId");
		//三者车信息
		if(!StringUtils.isBlank(taskQueryVo.getThirdLicenseNo()) || !StringUtils.isBlank(taskQueryVo.getThirdEngineNo())  || !StringUtils.isBlank(taskQueryVo.getThirdFrameNo()) ){
			sqlUtil.append(" AND exists(from PrpLDlossCarInfo c WHERE pq.registNo=c.registNo AND pq.licenseNo <> c.licenseNo");
			if(!StringUtils.isBlank(taskQueryVo.getThirdLicenseNo())){
				sqlUtil.append(" AND c.licenseNo like ?");
				sqlUtil.addParamValue(taskQueryVo.getThirdLicenseNo()+"%");
			//	sqlUtil.addParamValue("%"+taskQueryVo.getThirdLicenseNo()+"%");
			}
			if(!StringUtils.isBlank(taskQueryVo.getThirdEngineNo())){
				sqlUtil.append(" AND c.engineNo like ?");
				sqlUtil.addParamValue(taskQueryVo.getThirdEngineNo()+"%");
				//sqlUtil.addParamValue("%"+taskQueryVo.getThirdEngineNo()+"%");
			}
	        if(!StringUtils.isBlank(taskQueryVo.getThirdFrameNo())){
	        	sqlUtil.append(" AND c.frameNo like ?");
	        	sqlUtil.addParamValue(taskQueryVo.getThirdFrameNo()+"%");
	        	//sqlUtil.addParamValue("%"+taskQueryVo.getThirdFrameNo()+"%");
			}
			sqlUtil.append(")");
		}
		//标的车发动机号
		if(!StringUtils.isBlank(taskQueryVo.getEngineNo())){
			sqlUtil.append(" and exists(from PrpLCItemCar i where pq.registNo=i.registNo and pq.licenseNo=i.licenseNo ");
			sqlUtil.append(" and i.engineNo like ?");
			sqlUtil.addParamValue(taskQueryVo.getEngineNo()+"%");
		//	sqlUtil.addParamValue("%"+taskQueryVo.getEngineNo()+"%");
			sqlUtil.append(")");
		}
		//立案号
		String claimNo = taskQueryVo.getClaimNo();
		if(!StringUtils.isBlank(claimNo)){
			claimNo = claimNo.trim();
			if(claimNo.length() >0 && claimNo.length()<= 20){//如果立案号没有输入完整则进行模糊匹配，否则进行精准查询
				sqlUtil.append(" AND exists(FROM PrpLClaim pc WHERE pc.flowId = pm.flowId AND pc.claimNo like ?)");
				sqlUtil.addParamValue(claimNo+"%");
			} else{
				//相等条件
				sqlUtil.append(" AND exists(FROM PrpLClaim pc WHERE pc.flowId = pm.flowId AND pc.claimNo = ?)");
				sqlUtil.addParamValue(claimNo);
			}
		}
		
		String  table_query="pq";

		//相等条件
		sqlUtil.andEquals(taskQueryVo,table_query,"tpFlag");
		
		if(StringUtils.isBlank(taskQueryVo.getRegistNo()) || taskQueryVo.getRegistNo().length() != 22){
			//工作流状态
			String flowStatus = taskQueryVo.getFlowStatus();
			if("1".equals(flowStatus)){//正常流转
				sqlUtil.append(" AND pm.flowStatus = ?");
				sqlUtil.addParamValue("N");
			}else if("2".equals(flowStatus)){//结束流程
				sqlUtil.append(" AND pm.flowStatus In (?,?)");
				sqlUtil.addParamValue("C");//已注销
				sqlUtil.addParamValue("E");//已完成
			}
		}
		if(addTimeControl(taskQueryVo)){
			//日期条件
			sqlUtil.andDate(taskQueryVo,table_query,"reportTime","damageTime");
		}
		//如果报案号反转字段小于7位为则进行模糊查询，否则进行精准查询(因为报案号反转字段只有后六位倒叙)
		sqlUtil.andReverse(taskQueryVo,table_query,20,"registNo");
		//如果标的车车架号小于17位，则进行模糊查询，否则进行精准查询
		if (StringUtils.isNotBlank(taskQueryVo.getFrameNo())) {
			sqlUtil.andReverse(taskQueryVo,table_query,16,"frameNo");
		}
		if (StringUtils.isNotBlank(taskQueryVo.getInsuredName())) {
			sqlUtil.append(" AND pq.insuredName LIKE ?");
			sqlUtil.addParamValue(taskQueryVo.getInsuredName() + "%");
		}
		//如果车牌号反转字段小于7为则进行模糊查询，否则进行精准查询
		if (StringUtils.isNotBlank(taskQueryVo.getLicenseNo())) {
			// sqlUtil.append(" AND pq.licenseNo LIKE ?");
			sqlUtil.andReverse(taskQueryVo,table_query,6,"licenseNo");
		}
		
		//增加归属机构查询
		if(StringUtils.isNotBlank(taskQueryVo.getComCode())){
			if("0002".equals(taskQueryVo.getComCode().substring(0, 4))){//深圳
				if(taskQueryVo.getComCode().endsWith("0000")){//分公司
					sqlUtil.append(" AND pq.comCodePly LIKE ?");
					sqlUtil.addParamValue(taskQueryVo.getComCode().substring(0, 4)+"%");
				}else{//支公司
					sqlUtil.append(" AND pq.comCodePly LIKE ?");
					sqlUtil.addParamValue(taskQueryVo.getComCode().substring(0, 6)+"%");
				}
			}else{
				sqlUtil.andComSql(table_query,"comCodePly",taskQueryVo.getComCode());
			}
		}
		
		//是否有人伤
		if("1".equals(taskQueryVo.getIsPerson())){
			/*sqlUtil.append(" AND ( (exists(SELECT 1 FROM PrpLWfTaskOut out where out.nodeCode=? and out.registNo = pq.registNo )) or (exists(SELECT 1 FROM PrpLWfTaskIn prpLWfTaskIn where prpLWfTaskIn.nodeCode=? and prpLWfTaskIn.registNo = pq.registNo))  )");
			sqlUtil.addParamValue(FlowNode.PLoss.name());
			sqlUtil.addParamValue(FlowNode.PLoss.name());*/
		    sqlUtil.append(" AND  exists(SELECT 1 FROM PrpLScheduleItems scheduleItems where scheduleItems.serialNo=? and scheduleItems.registNo = pq.registNo ) ");
            sqlUtil.addParamValue("-1");
		}
		
		
		
		// 其他特殊条件
		// "policyNo" 特殊条件控制
		String policyNo = taskQueryVo.getPolicyNo();
		if(StringUtils.isNotBlank(policyNo)&&policyNo.length()>2){
			String policyNoRev = StringUtils.reverse(policyNo.toString()).trim();
			sqlUtil.append(" AND (pq.policyNoLink LIKE ? OR");
			sqlUtil.addParamValue(policyNo+"%");
			if(policyNo.trim().length() <= 20){
				sqlUtil.append(" pq.policyNoRev LIKE ? )");
				sqlUtil.addParamValue(policyNoRev+"%");
			} else{
				sqlUtil.append(" pq.policyNoRev = ? )");
				sqlUtil.addParamValue(policyNoRev);
			}
			
		}
		//公估机构案件
		if(StringUtils.isNotBlank(taskQueryVo.getIntermFlag()) && "1".equals(taskQueryVo.getIntermFlag())){
			sqlUtil.append(" AND exists (FROM PrpLWfTaskUserInfo pui WHERE pui.flowId = pm.flowId AND pui.userCode in "
					+ "(SELECT userCode FROM PrpdIntermUser))");
		//当前登录人为公估工号，只能查看自己的案子	
		}else if(StringUtils.isNotBlank(taskQueryVo.getIntermFlag()) && "2".equals(taskQueryVo.getIntermFlag())){
			sqlUtil.append(" AND exists (FROM PrpLWfTaskUserInfo pui WHERE pui.flowId = pm.flowId AND pui.userCode = ?)");
			sqlUtil.addParamValue(taskQueryVo.getUserCode());
		}
		//司内代查勘案件
		if("1".equals(taskQueryVo.getSubCheckFlag())){
			sqlUtil.append(" and (exists (select 1 from PrpLWfTaskIn tin where tin.flowId=pm.flowId and tin.subCheckFlag= ?) ");
			sqlUtil.append(" or exists(select 1 from PrpLWfTaskOut tout where tout.flowId=pm.flowId and tout.subCheckFlag= ?)) ");
			sqlUtil.addParamValue("1");
			sqlUtil.addParamValue("1");
		}
		//公估查勘案件
		if("1".equals(taskQueryVo.getAssessSubCheckFlag())){
			sqlUtil.append(" and (exists (select 1 from PrpLWfTaskIn tin where tin.flowId=pm.flowId and tin.subCheckFlag= ?) ");
			sqlUtil.append(" or exists(select 1 from PrpLWfTaskOut tout where tout.flowId=pm.flowId and tout.subCheckFlag= ?)) ");
			sqlUtil.addParamValue("2");
			sqlUtil.addParamValue("2");
		}
		//排除旧理赔案件
		sqlUtil.append(" and exists (select 1 from  PrpLRegist regist where pq.registNo = regist.registNo and regist.flag is null )");
		
		
		//操作人员
		if(factorPowerMap !=null && !factorPowerMap.isEmpty()){
			List<SaaFactorPowerVo> factorPowerVoList = factorPowerMap.get("FF_COMCODE");
			if(factorPowerVoList == null || factorPowerVoList.size() == 0){
			}else{
				boolean isMainCom = false;
				for(SaaFactorPowerVo factorPowerVo:factorPowerVoList){
					String comCode = factorPowerVo.getDataValue();
					if("0001%".equals(comCode)){//排除总公司
						isMainCom = true;
						break;
					}
				}
				if(!isMainCom){
					sqlUtil.append(" AND ((");
					for(int i = 0; i < factorPowerVoList.size(); i++){
						SaaFactorPowerVo factorPowerVo = factorPowerVoList.get(i);
						String comCode = factorPowerVo.getDataValue();
						if(i > 0){
							sqlUtil.append("  OR  ");
						}
						sqlUtil.append(this.genSubPowerHql(factorPowerVo,"comCode","pm"));// 得到alias. fieldCode = ?);
						sqlUtil.addParamValue(comCode);
					}
					sqlUtil.append(" )");
					sqlUtil.append(" OR exists(FROM PrpLWfTaskUserInfo pu WHERE pu.flowId = pm.flowId AND pu.userCode = ?)");
					sqlUtil.addParamValue(taskQueryVo.getUserCode());
					sqlUtil.append(" )");
				}
			}
		}

		//排序
		sqlUtil.append(" ORDER BY pq.reportTime DESC");
		//开始记录数
		int start = taskQueryVo.getStart();
		//查询记录数量
		int length = taskQueryVo.getLength();

		String sql = sqlUtil.getSql();
		logger.info("taskQrySql="+sql);
		
		Page<Object[]> page = databaseDao.findPageByHql(sql,start/length+1,length,sqlUtil.getParamValues());

		// 对象转换
		List<WfTaskQueryResultVo> resultVoList=new ArrayList<WfTaskQueryResultVo>();
		for(int i = 0; i<page.getResult().size(); i++ ){

			WfTaskQueryResultVo resultVo=new WfTaskQueryResultVo();
			Object[] obj = page.getResult().get(i);
			PrpLWfTaskQuery prpLWfTaskQuery = (PrpLWfTaskQuery)obj[0];

			Beans.copy().from(prpLWfTaskQuery).to(resultVo);
			resultVoList.add(resultVo);
		}

		ResultPage<WfTaskQueryResultVo> resultPage = new ResultPage<WfTaskQueryResultVo> (start, length, page.getTotalCount(), resultVoList);

		return resultPage;
		
	}
	
	/* 
	 * @see ins.sino.claimcar.flow.service.WfFlowService#findPrpLWfTaskQueryByFlowId(java.lang.String)
	 * @param flowId
	 * @return
	 */
	@Override
	public PrpLWfTaskQueryVo findPrpLWfTaskQueryByFlowId(String flowId){
		PrpLWfTaskQueryVo prpLWfTaskQueryVo = new PrpLWfTaskQueryVo();
		PrpLWfTaskQuery prpLWfTaskQuery = databaseDao.findByPK(PrpLWfTaskQuery.class,flowId);
		Beans.copy().from(prpLWfTaskQuery).to(prpLWfTaskQueryVo);
		return prpLWfTaskQueryVo;
	}
	
	private String genSubPowerHql(SaaFactorPowerVo factorPowerVo,String fieldCode,String alias) {

		SqlParamVo subPowerSqlVo = new SqlParamVo();
		String dataOper = factorPowerVo.getDataOper().toLowerCase();
		if("=".equals(dataOper)||"<".equals(dataOper)||">".equals(dataOper)||"<=".equals(dataOper)||">=".equals(dataOper)||"like".equals(dataOper)){// 不同操作符不同处理
			subPowerSqlVo.getSql().append(alias).append(".").append(fieldCode).append(" ").append(dataOper).append(" ? ");
		}
		return subPowerSqlVo.getSql().toString();

	}
    @Override
    public PrpLWfTaskVo findPrpLWfTaskQueryByTaskId(BigDecimal taskId) {
        PrpLWfTaskIn oldTaskInPo = databaseDao.findByPK(PrpLWfTaskIn.class,taskId);
        PrpLWfTaskVo taskVo = null;
        if(oldTaskInPo!=null){
            taskVo = Beans.copyDepth().from(oldTaskInPo).to(PrpLWfTaskVo.class);
        }else{
            PrpLWfTaskOut oldTaskOutPo = databaseDao.findByPK(PrpLWfTaskOut.class,taskId);
            taskVo = Beans.copyDepth().from(oldTaskOutPo).to(PrpLWfTaskVo.class);
        }
        return taskVo;
    }
	@Override
	public ResultPage<WfTaskQueryResultVo> findOldFlowForPage(PrpLWfTaskQueryVo taskQueryVo,Map<String, List<SaaFactorPowerVo>> factorPowerMap)throws Exception {

		SqlJoinUtils sqlUtil=new SqlJoinUtils();
		sqlUtil.append(" FROM PrpLRegist regist,PrpLpolicyNoView poview WHERE 1=1 and regist.registNo=poview.registNo ");
		sqlUtil.append(" AND regist.flag = ?");
		sqlUtil.addParamValue("oldClaim");
		//三者车车牌号，车架号信息
		if(!StringUtils.isBlank(taskQueryVo.getThirdLicenseNo()) || !StringUtils.isBlank(taskQueryVo.getThirdFrameNo()) ){
			sqlUtil.append(" AND exists(from PrpLRegistCarLoss c WHERE regist.registNo=c.prpLRegist.registNo and c.lossparty=? ");
			sqlUtil.addParamValue("3");
			if(!StringUtils.isBlank(taskQueryVo.getThirdLicenseNo())){
				sqlUtil.append(" AND c.licenseNo like ?");
				sqlUtil.addParamValue(taskQueryVo.getThirdLicenseNo().trim()+"%");
			}
	        if(!StringUtils.isBlank(taskQueryVo.getThirdFrameNo())){
	        	sqlUtil.append(" AND c.frameNo like ?");
	        	sqlUtil.addParamValue(taskQueryVo.getThirdFrameNo().trim()+"%");
			}
			sqlUtil.append(")");
		}
		
		
		//三者车发动机号
		if(StringUtils.isNotBlank(taskQueryVo.getThirdEngineNo())){
			sqlUtil.append(" AND exists(from PrpLCItemCar ci,PrpLRegistCarLoss loss where ci.licenseNo=loss.licenseNo and ci.registNo=loss.prpLRegist.registNo and ci.registNo=regist.registNo  and loss.lossparty=? ");
			sqlUtil.addParamValue("3");
			sqlUtil.append(" AND ci.engineNo like ?");
			sqlUtil.addParamValue(taskQueryVo.getThirdEngineNo()+"%");
			sqlUtil.append(")");
		}
		//标的车牌号，车架号信息
		if(!StringUtils.isBlank(taskQueryVo.getLicenseNo()) || !StringUtils.isBlank(taskQueryVo.getFrameNo()) ){
			sqlUtil.append(" AND exists(from PrpLRegistCarLoss prcl WHERE regist.registNo=prcl.prpLRegist.registNo and prcl.lossparty= ? ");
			sqlUtil.addParamValue("1");
			if(!StringUtils.isBlank(taskQueryVo.getLicenseNo())){
				if(taskQueryVo.getLicenseNo().trim().length() <7){
					sqlUtil.append(" AND prcl.licenseNo like ?");
					sqlUtil.addParamValue(taskQueryVo.getLicenseNo().trim() + "%");
				} else{
					sqlUtil.append(" AND prcl.licenseNo = ? ");
					sqlUtil.addParamValue(taskQueryVo.getLicenseNo().trim());
				}
			}
	        if(!StringUtils.isBlank(taskQueryVo.getFrameNo())){
	        	if(taskQueryVo.getLicenseNo().trim().length() <17){
		        	sqlUtil.append(" AND prcl.frameNo like ?");
		        	sqlUtil.addParamValue(taskQueryVo.getFrameNo().trim() + "%");
				} else{
					sqlUtil.append(" AND prcl.frameNo = ? ");
					sqlUtil.addParamValue(taskQueryVo.getFrameNo().trim());
				}
			}
			sqlUtil.append(")");
		}
		//标的车发动机号
		if(StringUtils.isNotBlank(taskQueryVo.getEngineNo())){
			sqlUtil.append(" AND exists(from PrpLCItemCar ci,PrpLRegistCarLoss carloss where ci.licenseNo=carloss.licenseNo and ci.registNo=carloss.prpLRegist.registNo and ci.registNo=regist.registNo and carloss.lossparty=? ");
			sqlUtil.addParamValue("1");
			sqlUtil.append(" AND ci.engineNo like ?");
			sqlUtil.addParamValue(taskQueryVo.getEngineNo().trim()+"%");
			sqlUtil.append(")");
		}
	
		//立案号
		String claimNo = taskQueryVo.getClaimNo();
		if(!StringUtils.isBlank(claimNo)){
			if(claimNo.trim().length()<20){
				sqlUtil.append(" AND exists(FROM PrpLClaim pc WHERE pc.registNo = regist.registNo AND pc.claimNo like ?)");
				sqlUtil.addParamValue(claimNo.trim()+"%");
			} else{
				sqlUtil.append(" AND exists(FROM PrpLClaim pc WHERE pc.registNo = regist.registNo AND pc.claimNo = ?)");
				sqlUtil.addParamValue(claimNo.trim());
			}
		}
		
		//工作流状态
		String flowStatus = taskQueryVo.getFlowStatus();
		if("1".equals(flowStatus)){//正常流转
			sqlUtil.append(" AND (regist.cancelFlag !=? OR exists(FROM PrpLClaim cl where cl.registNo=regist.registNo AND cl.validFlag=? AND cl.caseNo is null ))");
			sqlUtil.addParamValue("C");
			sqlUtil.addParamValue("1");
		}else if("2".equals(flowStatus)){//结束流程
			sqlUtil.append(" AND (regist.cancelFlag =? OR exists(FROM PrpLClaim cl where cl.registNo=regist.registNo AND (cl.validFlag=? OR cl.caseNo is not null )))");
			sqlUtil.addParamValue("C");
			sqlUtil.addParamValue("0");
		}
		String  table_query="regist";

		//忽略日期条件
		if(addTimeControl(taskQueryVo)){
			//日期条件
			sqlUtil.andDate(taskQueryVo,table_query,"reportTime","damageTime");
		}
        if(StringUtils.isNotBlank(taskQueryVo.getRegistNo())){
        	if(taskQueryVo.getRegistNo().trim().length()>0 && taskQueryVo.getRegistNo().trim().length()<21 ){
    			sqlUtil.append(" AND regist.registNo LIKE ?");
    			sqlUtil.addParamValue(taskQueryVo.getRegistNo().trim()+"%");
        	}  else{
        		sqlUtil.append(" AND regist.registNo = ? ");
    			sqlUtil.addParamValue(taskQueryVo.getRegistNo().trim());
        	}
		}
		if(StringUtils.isNotBlank(taskQueryVo.getInsuredName())){
			sqlUtil.append(" AND poview.insuredname like ? ");
			sqlUtil.addParamValue(taskQueryVo.getInsuredName().trim()+"%");
		}
		//增加归属机构查询
		if(StringUtils.isNotBlank(taskQueryVo.getComCode())){
			if("0002".equals(taskQueryVo.getComCode().substring(0, 4))){//深圳
				if(taskQueryVo.getComCode().endsWith("0000")){//分公司
					sqlUtil.append(" AND regist.comCode LIKE ?");
					sqlUtil.addParamValue(taskQueryVo.getComCode().substring(0, 4)+"%");
				}else{//支公司
					sqlUtil.append(" AND regist.comCode LIKE ?");
					sqlUtil.addParamValue(taskQueryVo.getComCode().substring(0, 6)+"%");
				}
			}else{
				sqlUtil.andComSql(table_query,"comCode",taskQueryVo.getComCode());
			}
		}
		
		//是否有人伤
		if("1".equals(taskQueryVo.getIsPerson())){
		    sqlUtil.append(" AND  exists(SELECT 1 FROM PrpLScheduleItems scheduleItems where scheduleItems.serialNo=? and scheduleItems.registNo =regist.registNo ) ");
            sqlUtil.addParamValue("-1");
		}
		
		
		
		// 其他特殊条件
		// "policyNo" 特殊条件控制
		String policyNo = taskQueryVo.getPolicyNo();
		if(StringUtils.isNotBlank(policyNo)&&policyNo.length()>2){
			if(policyNo.trim().length()<21){
				//String policyNoRev = StringUtils.reverse(policyNo.toString()).trim();
				sqlUtil.append(" AND (poview.policyNo LIKE ? Or poview.policyNoLink LIKE ? ) ");
				sqlUtil.addParamValue(policyNo.trim() + "%");
				sqlUtil.addParamValue(policyNo.trim() + "%");	
			} else{
				sqlUtil.append(" AND (poview.policyNo = ? Or poview.policyNoLink = ? ) ");
				sqlUtil.addParamValue(policyNo.trim());
				sqlUtil.addParamValue(policyNo.trim());
			}
		}
		//操作人员
		if(factorPowerMap !=null && !factorPowerMap.isEmpty()){
			List<SaaFactorPowerVo> factorPowerVoList = factorPowerMap.get("FF_COMCODE");
			if(factorPowerVoList == null || factorPowerVoList.size() == 0){
			}else{
				boolean isMainCom = false;
				for(SaaFactorPowerVo factorPowerVo:factorPowerVoList){
					String comCode = factorPowerVo.getDataValue();
					if("0001%".equals(comCode)){//排除总公司
						isMainCom = true;
						break;
					}
				}
				if(!isMainCom){
					sqlUtil.append(" AND (");
					for(int i = 0; i < factorPowerVoList.size(); i++){
						SaaFactorPowerVo factorPowerVo = factorPowerVoList.get(i);
						String comCode = factorPowerVo.getDataValue();
						if(i > 0){
							sqlUtil.append("  OR  ");
						}
						sqlUtil.append(this.genSubPowerHql(factorPowerVo,"comCode","regist"));// 得到alias. fieldCode = ?);
						sqlUtil.addParamValue(comCode);
					}
					sqlUtil.append(" )");
				}
			}
		}

		//排序
		sqlUtil.append(" ORDER BY regist.reportTime DESC");
		//开始记录数
		int start = taskQueryVo.getStart();
		//查询记录数量
		int length = taskQueryVo.getLength();

		String sql = sqlUtil.getSql();
		logger.info("taskQrySql="+sql);
		
		Page<Object[]> page = databaseDao.findPageByHql(sql,start/length+1,length,sqlUtil.getParamValues());

		// 对象转换
		List<WfTaskQueryResultVo> resultVoList=new ArrayList<WfTaskQueryResultVo>();
		for(int i = 0; i<page.getResult().size(); i++ ){
			WfTaskQueryResultVo resultVo=new WfTaskQueryResultVo();
			Object[] obj = page.getResult().get(i);
			PrpLRegist prpLregist= (PrpLRegist)obj[0];
			PrpLpolicyNoView prpLpolicyNoView=(PrpLpolicyNoView)obj[1];
			resultVo.setRegistNo(prpLregist.getRegistNo());
			resultVo.setPolicyNo(prpLpolicyNoView.getPolicyNo());
			resultVo.setPolicyNoLink(prpLpolicyNoView.getPolicyNoLink());
			resultVo.setLicenseNo(prpLpolicyNoView.getLicenseNo());
			resultVo.setInsuredName(prpLpolicyNoView.getInsuredname());
			resultVo.setDamageTime(prpLregist.getDamageTime());
			resultVo.setReportTime(prpLregist.getReportTime());
			resultVo.setComCodePly(prpLregist.getComCode());
			resultVo.setComCode(prpLregist.getComCode());
			resultVo.setRiskCode(prpLregist.getRiskCode());
			resultVo.setDamageAddress(prpLregist.getDamageAddress());
			resultVo.setHandlerUser(codeTranService.transCode("UserCode",prpLregist.getCreateUser()));
			resultVo.setSerialNo(prpLregist.getRiskCode());
			resultVoList.add(resultVo);
		}

		ResultPage<WfTaskQueryResultVo> resultPage = new ResultPage<WfTaskQueryResultVo> (start, length, page.getTotalCount(), resultVoList);

		return resultPage;
		
	}

	@Override
	public boolean findValidPrePay(String claimNo) {
		QueryRule query=QueryRule.getInstance();
		query.addEqual("claimNo", claimNo);
		query.addEqual("compensateType",CodeConstants.CompensateType.prepay_type);
		query.addNotEqual("underwriteFlag",CodeConstants.UnderWriteFlag.CANCELFLAG);
		 List<PrpLCompensate> compensates=databaseDao.findAll(PrpLCompensate.class,query);
		 if(compensates!=null && compensates.size()>0){
			 return true;
		 }else{
			 QueryRule queryruleIn=QueryRule.getInstance();
			 queryruleIn.addEqual("claimNo", claimNo);
			 queryruleIn.addLike("nodeCode","PrePay%");
			 List<PrpLWfTaskIn> wftaskIns=databaseDao.findAll(PrpLWfTaskIn.class,queryruleIn);
			 if(wftaskIns!=null && wftaskIns.size()>0){
				 return true;
			 }
			 QueryRule queryruleout=QueryRule.getInstance();
			 queryruleout.addEqual("claimNo", claimNo);
			 queryruleout.addLike("nodeCode","PrePay%");
			 queryruleout.addEqual("workStatus","3");
			 List<PrpLWfTaskIn> wftaskOuts=databaseDao.findAll(PrpLWfTaskIn.class,queryruleout);
			 if(wftaskOuts!=null && wftaskOuts.size()>0){
				 return true;
			 }
		 }
		 
		return false;
	}
}
