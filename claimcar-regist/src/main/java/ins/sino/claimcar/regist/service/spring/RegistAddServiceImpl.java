package ins.sino.claimcar.regist.service.spring;

import ins.framework.common.ResultPage;
import ins.framework.dao.database.DatabaseDao;
import ins.framework.dao.database.support.Page;
import ins.framework.utils.Beans;
import ins.platform.utils.SqlJoinUtils;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.claim.service.ClaimService;
import ins.sino.claimcar.claim.service.ClaimTaskExtService;
import ins.sino.claimcar.claim.service.ClaimTaskService;
import ins.sino.claimcar.claim.vo.PrpLClaimVo;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.service.WfFlowQueryService;
import ins.sino.claimcar.flow.service.WfTaskHandleService;
import ins.sino.claimcar.regist.po.PrpLRegist;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.service.RegistAddService;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Path;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;

@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"},timeout = 1000000)
@Path("registAddService")
public class RegistAddServiceImpl implements RegistAddService{
	private static final Log logger = LogFactory.getLog(RegistAddServiceImpl.class);
	@Autowired
	DatabaseDao databaseDao;
	@Autowired
	PolicyViewService policyViewService;
	@Autowired
	WfTaskHandleService wfTaskHandleService;
	@Autowired
	WfFlowQueryService wfFlowQueryService;
	@Autowired
	ClaimService claimService;
	@Autowired
	ClaimTaskService claimTaskService;
	@Autowired
	ClaimTaskExtService claimTaskExtService;
	@Autowired
	RegistQueryService registQueryService;
	
	@Override
	public ResultPage<PrpLRegistVo> findRegistAddForPage(PrpLRegistVo prpLRegistVo,Integer start,Integer length,SysUserVo userVo) throws Exception {

		SqlJoinUtils sqlUtil=new SqlJoinUtils();
		sqlUtil.append(" from PrpLRegist a , PrpLRegistExt b ");
		sqlUtil.append(" WHERE a.registNo = b.registNo and a.registTaskFlag = ? ");
		sqlUtil.addParamValue(CodeConstants.RegistTaskFlag.SUBMIT);// 报案状态为已处理

//		sqlUtil.andTopComSql("reg","comCode",userVo.getComCode());
		
		if(StringUtils.isNotBlank(prpLRegistVo.getRegistNo())){
			sqlUtil.append(" and  a.registNo like ? ");
			sqlUtil.addParamValue("%"+prpLRegistVo.getRegistNo().trim()+"%"  );
		}
		if(StringUtils.isNotBlank(prpLRegistVo.getPolicyNo())){
			sqlUtil.append(" and  a.policyNo like ? ");
			sqlUtil.addParamValue("%"+prpLRegistVo.getPolicyNo().trim()+"%"  );
		}
		if(StringUtils.isNotBlank(prpLRegistVo.getRemark())){
			sqlUtil.append(" and  b.insuredName like ? ");
			sqlUtil.addParamValue("%"+prpLRegistVo.getRemark().trim()+"%"  );
		}
		sqlUtil.append(" and exists (select 1 from PrpLCMain where a.registNo = registNo group by registNo having count(*) < 2) ");
		sqlUtil.append(" and exists (select 1 from PrpCMainSub where policyNo = a.policyNo or mainPolicyNo = a.policyNo) ");
		//sqlUtil.append(" and exists (select 1 from PrpLClaim where a.registNo = registNo group by registNo having count(*) < 2) ");
		
		// 排序
		sqlUtil.append(" ORDER BY a.reportTime DESC");
		String sql = sqlUtil.getSql();
		logger.debug("taskQrySql="+sql);
		Page page = databaseDao.findPageByHql(PrpLRegist.class, sql,start/length+1,length,sqlUtil.getParamValues());
		// 对象转换
		List<PrpLRegistVo> resultVoList=new ArrayList<PrpLRegistVo>();
		for(int i = 0; i<page.getResult().size(); i++ ){
			Object[] obj = (Object[])page.getResult().get(i);
			PrpLRegistVo resultVo = Beans.copyDepth().from(obj[0]).to(PrpLRegistVo.class);
			resultVoList.add(resultVo);
		}

		ResultPage<PrpLRegistVo> resultPage = new ResultPage<PrpLRegistVo> (start, length, page.getTotalCount(), resultVoList);

		return resultPage;
		
	}
	
	@Override
	public String isRegistAdd(String registNo) {
		String isRegistAdd = "true";
		
		SqlJoinUtils sqlUtil=new SqlJoinUtils();
		sqlUtil.append(" from PrpLRegist a");
		sqlUtil.append(" WHERE a.registTaskFlag = ? ");
		sqlUtil.addParamValue(CodeConstants.RegistTaskFlag.SUBMIT);// 报案状态为已处理

		if(StringUtils.isNotBlank(registNo)){
			sqlUtil.append(" and  a.registNo like ? ");
			sqlUtil.addParamValue("%"+registNo+"%"  );
		}
		//查询该报案号下只有一张保单，且保单关联表有另一张保单的案件 
		sqlUtil.append(" and exists (select 1 from PrpLCMain where a.registNo = registNo group by registNo having count(*) < 2) ");
		sqlUtil.append(" and exists (select 1 from PrpCMainSub where policyNo = a.policyNo or mainPolicyNo = a.policyNo) ");
//		sqlUtil.append(" and exists (select 1 from PrpLClaim where a.registNo = registNo group by registNo having count(*) < 2) ");
		
		String sql = sqlUtil.getSql();
		Object[] values = sqlUtil.getParamValues();
		System.out.println(sql);
		List<Object[]> registPoList = databaseDao.findAllByHql(sql,values);
		
		if(registPoList!=null&&registPoList.size()>0){
			//该报案号下只有一条立案业务数据（包括有效和无效）
			List<PrpLClaimVo> claimVoList = claimService.findClaimListByRegistNo(registNo);
			if(claimVoList!=null&&claimVoList.size()==1){
				boolean stateCompBI = wfTaskHandleService.existCancelByNAndH(registNo, FlowNode.CompeBI.toString());
				boolean stateCompCI = wfTaskHandleService.existCancelByNAndH(registNo, FlowNode.CompeCI.toString());
				//该报案号在工作流未生成理算节点（理算未处理节点也不允许）,存在理算已注销节点可以补登
				if(stateCompBI||stateCompCI){
					return "该案件已存在理算节点，不符合补登报案要求！";
				}
				boolean stateCase = wfTaskHandleService.existTaskByNode(registNo, FlowNode.EndCas.toString());
				//结案过的案件（工作流存在结案已处理节点的案件不允许补登）
				if(stateCase){
					return "该案件已存在结案节点，不符合补登报案要求！";
				}			

			}else{
				return "该案件立案状态不符合补登报案要求！";
			}
		}else{
			return "该案件保单不符合补登报案要求！";
		}

		return isRegistAdd;
		
	}




}
