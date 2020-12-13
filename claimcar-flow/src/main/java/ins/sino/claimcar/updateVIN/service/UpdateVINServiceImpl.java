package ins.sino.claimcar.updateVIN.service;

import ins.framework.common.ResultPage;
import ins.framework.dao.database.DatabaseDao;
import ins.framework.dao.database.support.Page;
import ins.platform.utils.SqlJoinUtils;
import ins.sino.claimcar.carplatform.po.ClaimVinNoHis;
import ins.sino.claimcar.check.service.UpdateVINService;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.vo.PrpLWfTaskQueryVo;
import ins.sino.claimcar.updateVIN.po.PrpLRegist;
import ins.sino.claimcar.updateVIN.po.PrpLRegistExt;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Path;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;

@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"})
@Path("updateVINService")
public class UpdateVINServiceImpl implements UpdateVINService{
	
	@Autowired
	DatabaseDao databaseDao;
	
	public ResultPage<PrpLWfTaskQueryVo> search(PrpLWfTaskQueryVo prplWfTaskQueryVo,
			int start,int length) throws ParseException{
		
		SqlJoinUtils sqlUtil=new SqlJoinUtils();
		String comCode = "";
		
		sqlUtil.append(" from PrpLRegist regist,PrpLRegistExt ext ");
		sqlUtil.append(" where 1=1 and regist.registNo=ext.prpLRegist.registNo " );
		sqlUtil.append(" and exists(select 1 from PrpLWfTaskOut wfOut where wfOut.registNo=regist.registNo ");
		sqlUtil.append(" and wfOut.subNodeCode = ? and wfOut.handlerStatus = ? )");//out表有已处理的查勘任务
		sqlUtil.append(" and not exists(select 1 from PrpLWfTaskIn wfIn where wfIn.registNo=regist.registNo ");
		sqlUtil.append(" and wfIn.subNodeCode = ? )" ); //in表没有查勘任务
		
		sqlUtil.addParamValue(FlowNode.Chk.name());
		sqlUtil.addParamValue("3");
		sqlUtil.addParamValue(FlowNode.Chk.name());
		
		if(StringUtils.isNotBlank(prplWfTaskQueryVo.getRegistNo())){
			sqlUtil.append(" and  regist.registNo = ? ");
			sqlUtil.addParamValue(prplWfTaskQueryVo.getRegistNo());
		}
		if(StringUtils.isNotBlank(prplWfTaskQueryVo.getPolicyNo())){
			sqlUtil.append(" and  regist.policyNo like ? ");
			sqlUtil.addParamValue("%"+prplWfTaskQueryVo.getPolicyNo()  );
		}
		if(StringUtils.isNotBlank(prplWfTaskQueryVo.getLicenseNo())){
			sqlUtil.append(" and  ext.licenseNo = ? ");
			sqlUtil.addParamValue(prplWfTaskQueryVo.getLicenseNo()  );
		}
		if(StringUtils.isNotBlank(prplWfTaskQueryVo.getInsuredName())){
			sqlUtil.append(" and  ext.insuredName like ? ");
			sqlUtil.addParamValue("%"+prplWfTaskQueryVo.getInsuredName()+"%"  );
		}
		//区分机构，排除系统管理员
		if(!"0000000000".equals(prplWfTaskQueryVo.getUserCode())){
			if(prplWfTaskQueryVo.getComCode().startsWith("00")){
				comCode = prplWfTaskQueryVo.getComCode().substring(0, 4);
			}else{
				comCode = prplWfTaskQueryVo.getComCode().substring(0, 2);
			}
			sqlUtil.append(" and  regist.comCode like ? ");
			sqlUtil.addParamValue(comCode+"%"  );
		}
		if("3".equals(prplWfTaskQueryVo.getHandleStatus())){
			sqlUtil.append(" and  regist.updateVINFlag = ? ");
			sqlUtil.addParamValue("1");
			sqlUtil.append(" Order By regist.updateVINTime desc ");
		}else{
			sqlUtil.append(" Order By regist.reportTime desc ");
		}
		String sql = sqlUtil.getSql();
		Object[] values = sqlUtil.getParamValues();
		Page<Object[]> page = databaseDao.findPageByHql(sql,start/length+1,length,values);
		//对象转换
		List<PrpLWfTaskQueryVo> queryVoList = new ArrayList<PrpLWfTaskQueryVo>();
		for(int i=0;i<page.getResult().size();i++){
			Object[] obj = page.getResult().get(i);
			PrpLWfTaskQueryVo queryVo = new PrpLWfTaskQueryVo();
			PrpLRegist registPo = (PrpLRegist) obj[0];
			PrpLRegistExt registExtPo = (PrpLRegistExt) obj[1];
			queryVo.setRegistNo(registPo.getRegistNo());
			queryVo.setPolicyNo(registPo.getPolicyNo());
			queryVo.setInsuredName(registExtPo.getInsuredName());
			queryVo.setLicenseNo(registExtPo.getLicenseNo());
			queryVo.setOperateTime(registPo.getUpdateVINTime());
			queryVo.setOperateUser(registPo.getUpdateVINUser());
			queryVoList.add(queryVo);
		}
		ResultPage<PrpLWfTaskQueryVo> resultPage = 
				new ResultPage<PrpLWfTaskQueryVo>(start,length,page.getTotalCount(),queryVoList);
		return resultPage;
	}

	@Override
	public void saveClaimvinnohis(ClaimVinNoHis claimvinNoHis) {
		databaseDao.save(ClaimVinNoHis.class,claimvinNoHis);
	}

}
