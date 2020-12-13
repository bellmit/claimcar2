package ins.sino.claimcar.common.service;

import ins.framework.dao.database.DatabaseDao;
import ins.framework.dao.database.support.QueryRule;
import ins.framework.utils.Beans;
import ins.sino.claimcar.common.po.PrpLHandReason;
import ins.sino.claimcar.flow.service.WfTaskHandleService;
import ins.sino.claimcar.recloss.service.WfHandReasonServices;
import ins.sino.claimcar.recloss.vo.PrpLHandReasonVo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;

@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"})
@Path("wfHandReasonServices")
public class WfHandReasonServicesImpl implements WfHandReasonServices {

	@Autowired
	DatabaseDao databaseDao;

	// 服务装载
	@Autowired
	private WfTaskHandleService wfTaskHandleService;

	private void sss() {

		// 提交工作流 submitVo

		// wfTaskHandleService.addSimpleTask(taskVo,submitVo)
	}

	/*
	 * @see ins.sino.claimcar.common.service.WfHandReasonServices#saveSysMsg(ins.sino.claimcar.common.vo.PrpLHandReasonVo)
	 * 
	 * @param prpLHandReasonVo
	 */
	@Override
	public void saveSysMsg(PrpLHandReasonVo prpLHandReasonVo) {

		PrpLHandReason prpLHandReasonPo = null;
		prpLHandReasonPo = databaseDao.findByPK(PrpLHandReason.class,prpLHandReasonVo.getTaskId());
		if(prpLHandReasonPo!=null){// genxin
			prpLHandReasonVo.setUpdateTime(new Date());
			// prpLHandReasonVo.setUpdateUser(SecurityUtils.getComName());
			Beans.copy().from(prpLHandReasonVo).excludeNull().to(prpLHandReasonPo);
			prpLHandReasonPo.setReasonDuty(prpLHandReasonVo.getReasonDuty());
			prpLHandReasonPo.setReasonNode(prpLHandReasonVo.getReasonNode());
			// Beans.copy().from(prpLHandReasonVo).to(prpLHandReasonPo);
			databaseDao.save(PrpLHandReason.class,prpLHandReasonPo);
		}else{
			prpLHandReasonVo.setReasonDuty(prpLHandReasonVo.getReasonDuty());
			prpLHandReasonVo.setReasonNode(prpLHandReasonVo.getReasonNode());
			/*
			 * prpLHandReasonVo.setComCode(SecurityUtils.getComCode()); prpLHandReasonVo.setCreateUser(SecurityUtils.getUserName());
			 */
			prpLHandReasonVo.setCreateTime(new Date());
			prpLHandReasonPo = new PrpLHandReason();
			Beans.copy().from(prpLHandReasonVo).excludeNull().to(prpLHandReasonPo);
			databaseDao.save(PrpLHandReason.class,prpLHandReasonPo);
		}

	}

	/*
	 * 根据id查询
	 */
	/*
	 * @see ins.sino.claimcar.common.service.WfHandReasonServices#findHandReasonById(java.math.BigDecimal)
	 * 
	 * @param taskId
	 * 
	 * @return
	 */
	@Override
	public PrpLHandReasonVo findHandReasonById(BigDecimal taskId) {
		QueryRule queryRule = QueryRule.getInstance();
		// Long intermID = Long.parseLong(id);
		queryRule.addEqual("taskId",taskId);
		List<PrpLHandReason> list = databaseDao.findAll(PrpLHandReason.class,queryRule);
		// 获取当前序号下的保单相关对象数组

		PrpLHandReasonVo plyVo = new PrpLHandReasonVo();
		if(list.size()>0){
			// 复制到Vo对象中
			PrpLHandReason prf = list.get(0);
			Beans.copy().from(prf).to(plyVo);
		}
		return plyVo;
	}

}
