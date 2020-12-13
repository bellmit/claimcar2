package ins.sino.claimcar.lossprop.service;

import ins.framework.dao.database.DatabaseDao;
import ins.framework.dao.database.support.QueryRule;
import ins.framework.utils.Beans;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.service.WfTaskHandleService;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.lossprop.po.PrpLdlossPropFee;
import ins.sino.claimcar.lossprop.po.PrpLdlossPropFeeHis;
import ins.sino.claimcar.lossprop.po.PrpLdlossPropMain;
import ins.sino.claimcar.lossprop.po.PrpLdlossPropMainHis;
import ins.sino.claimcar.lossprop.vo.PrpLdlossPropFeeVo;
import ins.sino.claimcar.lossprop.vo.PrpLdlossPropMainHisVo;
import ins.sino.claimcar.lossprop.vo.PrpLdlossPropMainVo;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.Path;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;

@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"}, timeout = 1000000)
@Path("propLossService")
public class PropLossServiceImpl implements PropLossService {

	@Autowired
	private DatabaseDao databaseDao;
	@Autowired
	WfTaskHandleService wfTaskHandleService;
	
	/* (non-Javadoc)
	 * @see ins.sino.claimcar.lossprop.service.PropLossService#findPoByKey(java.lang.Long)
	 */
	private PrpLdlossPropMain findPoByKey(Long id){
		return databaseDao.findByPK(PrpLdlossPropMain.class, id);
	}
	
	/* (non-Javadoc)
	 * @see ins.sino.claimcar.lossprop.service.PropLossService#findVoByRegistNo(java.lang.String)
	 */
	@Override
	public PrpLdlossPropMainVo findVoByRegistNo(String registNo){
		PrpLdlossPropMainVo prpLdlossPropMainVo=null;
		QueryRule queryRule=QueryRule.getInstance();
		queryRule.addEqual("registNo", registNo);
		List<PrpLdlossPropMain> dlossPropMainPos = 
				databaseDao.findAll(PrpLdlossPropMain.class, queryRule);
		if(dlossPropMainPos!=null&&dlossPropMainPos.size()>0){
			prpLdlossPropMainVo = Beans.copyDepth().from(dlossPropMainPos.get(0)).
					to(PrpLdlossPropMainVo.class);;
		}
		return prpLdlossPropMainVo;
	}
	
	/* (non-Javadoc)
	 * @see ins.sino.claimcar.lossprop.service.PropLossService#findPropVoListByRegistNo(java.lang.String)
	 */
	@Override
	public List<PrpLdlossPropFeeVo> findPropVoListByRegistNo(String registNo){
		List<PrpLdlossPropFeeVo> dlossPropFeeVos = null;
		QueryRule queryRule=QueryRule.getInstance();
		queryRule.addEqual("registNo", registNo);
		List<PrpLdlossPropFee> dlossPropFeePos = 
				databaseDao.findAll(PrpLdlossPropFee.class, queryRule);
		if(dlossPropFeePos!=null&&dlossPropFeePos.size()>0){
			dlossPropFeeVos =  Beans.copyDepth().from(dlossPropFeePos).toList(PrpLdlossPropFeeVo.class);
		}
		return dlossPropFeeVos;
	}
	
	/* (non-Javadoc)
	 * @see ins.sino.claimcar.lossprop.service.PropLossService#findPropMainByRegistNo(java.lang.String)
	 */
	@Override
	public List<PrpLdlossPropMainVo> findPropMainByRegistNo(String registNo){
		List<PrpLdlossPropMainVo> prpLdlossPropMainVos = null;
		QueryRule queryRule=QueryRule.getInstance();
		queryRule.addEqual("registNo", registNo);
		List<PrpLdlossPropMain> prpLdlossPropMains =
				databaseDao.findAll(PrpLdlossPropMain.class, queryRule);
		if(prpLdlossPropMains != null && prpLdlossPropMains.size() > 0){
			prpLdlossPropMainVos = Beans.copyDepth().from(prpLdlossPropMains).toList(PrpLdlossPropMainVo.class);
		}
		return prpLdlossPropMainVos;
	}
	
	
	/* (non-Javadoc)
	 * @see ins.sino.claimcar.lossprop.service.PropLossService#saveOrUpdatePropMain(ins.sino.claimcar.lossprop.vo.PrpLdlossPropMainVo, ins.platform.vo.SysUserVo)
	 */
	@Override
	public void saveOrUpdatePropMain(PrpLdlossPropMainVo prpLdlossPropMainVo,SysUserVo sysUserVo){
		
		PrpLdlossPropMain prpLdlossPropMain=null;
		//id是空的直接copy对象
		if(prpLdlossPropMainVo.getId()==null){
			prpLdlossPropMain = Beans.copyDepth().from(prpLdlossPropMainVo).to(PrpLdlossPropMain.class);
			//维护主子表关系
			for(PrpLdlossPropFee prpLdlossPropFee:prpLdlossPropMain.getPrpLdlossPropFees()){
				prpLdlossPropFee.setPrpLdlossPropMain(prpLdlossPropMain);
			}		
		}else{
			//id不为空判断为更新，根据id先去数据库查找
			prpLdlossPropMain = databaseDao.findByPK(PrpLdlossPropMain.class, prpLdlossPropMainVo.getId());	
			
			Beans.copy().from(prpLdlossPropMainVo).excludeNull().to(prpLdlossPropMain);
			List<PrpLdlossPropFee> prpLdlossPropFees = prpLdlossPropMain.getPrpLdlossPropFees();
			//遍历子表
			for (Iterator it = prpLdlossPropFees.iterator(); it.hasNext();) {	
				PrpLdlossPropFee prpLdlossPropFee = (PrpLdlossPropFee) it.next();
				//设标志位
				Boolean delFlag = true;
				for(PrpLdlossPropFeeVo prpLdlossPropFeeVo:prpLdlossPropMainVo.getPrpLdlossPropFees()){
					//判断页面页面子表ID和数据库子表ID是否一样 如果一样拷贝
					
					if(prpLdlossPropFee.getId().equals(prpLdlossPropFeeVo.getId())){
						Beans.copy().from(prpLdlossPropFeeVo).excludeNull().to(prpLdlossPropFee);
						delFlag =false;
					}
				}
				if(delFlag){
					
					databaseDao.deleteByPK(PrpLdlossPropFee.class ,prpLdlossPropFee.getId());
					it.remove();
				}
			}
			for(PrpLdlossPropFeeVo prpLdlossPropFeeVo:prpLdlossPropMainVo.getPrpLdlossPropFees()){
				if(prpLdlossPropFeeVo.getId()==null){
					PrpLdlossPropFee prpLdlossPropFee = new PrpLdlossPropFee();
					prpLdlossPropFeeVo.setRegistNo(prpLdlossPropMain.getRegistNo());
					Beans.copy().from(prpLdlossPropFeeVo).to(prpLdlossPropFee);
					prpLdlossPropFee.setPrpLdlossPropMain(prpLdlossPropMain);
					prpLdlossPropFees.add(prpLdlossPropFee);
				}
			}
			
		}
		databaseDao.save(PrpLdlossPropMain.class,prpLdlossPropMain);
		prpLdlossPropMainVo.setId(prpLdlossPropMain.getId());//用于设置 PrpLClaimText 表
		
	}
	
//	private PrpLdlossPropMain setPropVerify(PrpLdlossPropMain dlossProp){
//		String userCode = ServiceUserUtils.getUserCode();
//		String userName = ServiceUserUtils.getUserName();
//		String comCode = ServiceUserUtils.getComCode();
//		Date date = new Date();
//		dlossProp.setUnderWriteName(userName);
//		dlossProp.setUnderWriteIdCard("123456**************");
//		dlossProp.setUnderWriteCode(userCode);
//		dlossProp.setUnderWriteCom(comCode);
//		dlossProp.setUnderWriteEndDate(date);
//		dlossProp.setUnderWriteFlag("1");
//		return dlossProp;
//	}
	
	/* (non-Javadoc)
	 * @see ins.sino.claimcar.lossprop.service.PropLossService#findVoByKey(java.lang.Long)
	 */
	@Override
	public PrpLdlossPropMainVo findVoByKey(Long id){
		PrpLdlossPropMain  prpLdlossPropMain=null;
		prpLdlossPropMain=databaseDao.findByPK(PrpLdlossPropMain.class, id);
		PrpLdlossPropMainVo prpLdlossPropMainVo=null;
		prpLdlossPropMainVo=Beans.copyDepth().from(prpLdlossPropMain).to(PrpLdlossPropMainVo.class);
		return prpLdlossPropMainVo;
	}
	
	/* (non-Javadoc)
	 * @see ins.sino.claimcar.lossprop.service.PropLossService#findPropMainVoById(java.lang.Long)
	 */
	@Override
	public PrpLdlossPropMainVo findPropMainVoById(Long id){
		PrpLdlossPropMain propMain = databaseDao.findByPK(PrpLdlossPropMain.class,id);
		if(propMain == null){
			return null;
		}
		PrpLdlossPropMainVo propMainVo = Beans.copyDepth().from(propMain).to(PrpLdlossPropMainVo.class);
		return propMainVo;
	}
	
	/* (non-Javadoc)
	 * @see ins.sino.claimcar.lossprop.service.PropLossService#findPropFeeVoById(java.lang.Long)
	 */
	@Override
	public Long findPropFeeVoById(Long id){
		PrpLdlossPropFee propFee = databaseDao.findByPK(PrpLdlossPropFee.class,id);
		if(propFee == null){
			return null;
		}
		
		return propFee.getPrpLdlossPropMain().getId();
	}
	
	
	/* (non-Javadoc)
	 * @see ins.sino.claimcar.lossprop.service.PropLossService#updatePropMain(ins.sino.claimcar.lossprop.vo.PrpLdlossPropMainVo)
	 */
	@Override
	public void updatePropMain(PrpLdlossPropMainVo propMainVo){
		PrpLdlossPropMain propMain = databaseDao.findByPK(PrpLdlossPropMain.class,propMainVo.getId());
		Beans.copy().from(propMainVo).excludeNull().to(propMain);
		
		databaseDao.update(PrpLdlossPropMain.class,propMain);
		propMainVo.setId(propMain.getId());
	}
	
	/* (non-Javadoc)
	 * @see ins.sino.claimcar.lossprop.service.PropLossService#dLPropModVlaid(java.lang.String, java.lang.String)
	 */
	@Override
	public String dLPropModVlaid(String registNo,String lossId){
		String retData = "ok";
		PrpLdlossPropMain propMain = databaseDao.findByPK(PrpLdlossPropMain.class,Long.valueOf(lossId));
		if(wfTaskHandleService.existTaskByNodeCode(registNo,FlowNode.VLoss,lossId,"0")){
			return "该案件核损未处理，请核对！";
		}
		if(wfTaskHandleService.existTaskByNodeCode(registNo,FlowNode.Compe,registNo,"")){
			return "该案件已发起理算，不能发起定损修改！";
		}
//		if(propMain.getReLossPropId()==null&&wfTaskHandleService.existTaskByNodeCode(registNo,FlowNode.RecLoss,lossId,"")){
//			return "该案件已发起损余回收，不能发起定损修改！";
//		}
		return retData;
	}
	
	/* (non-Javadoc)
	 * @see ins.sino.claimcar.lossprop.service.PropLossService#judgeRecLoss(ins.sino.claimcar.flow.vo.PrpLWfTaskVo)
	 */
	@Override
	public String judgeRecLoss(PrpLWfTaskVo wfTaskVo){
		String flag = "t";
		//定损追加不能发起损余回收
		if(wfTaskVo.getSubNodeCode().equals("DLPropAdd")){
			flag = "f";
		}
		//如果定损处理发起了损余回收，则定损修改不能发起
		if(wfTaskVo.getSubNodeCode().equals("DLPropMod")&&
				wfTaskHandleService.existTaskByNodeCode
				(wfTaskVo.getRegistNo(), FlowNode.RecLossProp, wfTaskVo.getTaskInKey(), "")){
			flag = "f";
		}
		return flag;
	}

	/* (non-Javadoc)
	 * @see ins.sino.claimcar.lossprop.service.PropLossService#findLossPropNoComp(java.lang.String, java.lang.Integer)
	 */
	@Override
	public List<PrpLdlossPropMainVo> findLossPropNoComp(String registNo,Integer serialNo) {
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo",registNo);
		queryRule.addEqual("serialNo",serialNo);
		queryRule.addEqual("lossState", "00");
		
		List<PrpLdlossPropMain> lossPropMainList = databaseDao.findAll(PrpLdlossPropMain.class,queryRule);
		List<PrpLdlossPropMainVo> lossPropMainVoList = Beans.copyDepth().from(lossPropMainList).toList(PrpLdlossPropMainVo.class);
		
		return lossPropMainVoList;
	}
	
	@Override
	public void savePropHis(PrpLdlossPropMainVo lossPropMainVo){
		PrpLdlossPropMainHis lossPropMainHis=new PrpLdlossPropMainHis();
		Date date = new Date();
		Beans.copy().from(lossPropMainVo).to(lossPropMainHis);//将lossPropMainVo 转换成 po
		lossPropMainHis.setId(null);
		lossPropMainHis.setPropMainId(lossPropMainVo.getId());
		lossPropMainHis.setCreateTime(date);
		lossPropMainHis.setCreateUser(lossPropMainVo.getUpdateUser());
		lossPropMainHis.setUpdateTime(date);
		lossPropMainHis.setUpdateUser(lossPropMainVo.getUpdateUser());
		lossPropMainHis.setSumDefLoss(lossPropMainVo.getSumDefloss());
		
		List<PrpLdlossPropFeeVo> propFeeList = lossPropMainVo.getPrpLdlossPropFees();
		if(propFeeList != null && propFeeList.size() > 0){
			List<PrpLdlossPropFeeHis> propFeeHisList =  Beans.copyDepth().from(propFeeList).toList(PrpLdlossPropFeeHis.class);
			for(PrpLdlossPropFeeHis propFeeHis:propFeeHisList){
				propFeeHis.setPropFeeId(propFeeHis.getId());
				propFeeHis.setId(null);
				propFeeHis.setPrpLdlossPropMainHis(lossPropMainHis);
			}
			lossPropMainHis.setPrpLdlossPropFeeHises(propFeeHisList);
		}
		
		databaseDao.save(PrpLdlossPropMainHis.class, lossPropMainHis);
	}
	
	@Override
	public PrpLdlossPropMainHisVo findPropHisByPropMainId(Long propMainId){
		QueryRule queryRule=QueryRule.getInstance();
		queryRule.addEqual("propMainId", propMainId);
		queryRule.addDescOrder("createTime");
		List<PrpLdlossPropMainHis>  lossPropMainHisList = databaseDao.findAll(PrpLdlossPropMainHis.class, queryRule);
		if(lossPropMainHisList != null && lossPropMainHisList.size() > 0){
			PrpLdlossPropMainHisVo lossPropMainHisVo = 
					Beans.copyDepth().from(lossPropMainHisList.get(0)).to(PrpLdlossPropMainHisVo.class);
			return lossPropMainHisVo;
		}
		return null;
	}
	
	@Override
	public PrpLdlossPropFeeVo findLossPropFeeVoById(Long id){
		PrpLdlossPropFee prpLdlossPropFee = databaseDao.findByPK(PrpLdlossPropFee.class, id);
		if(prpLdlossPropFee != null){
			PrpLdlossPropFeeVo propFeeVo = new PrpLdlossPropFeeVo();
			Beans.copy().from(prpLdlossPropFee).to(propFeeVo);
			return propFeeVo;
		}else{
			return null;
		}
	}

	@Override
	public List<PrpLdlossPropMainVo> findLossPropBySerialNo(String registNo, Integer serialNo) {
		List<PrpLdlossPropMainVo> lossPropMainVoList = new ArrayList<PrpLdlossPropMainVo>();
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo",registNo);
		queryRule.addEqual("serialNo",serialNo);
		List<PrpLdlossPropMain> lossPropMainList = databaseDao.findAll(PrpLdlossPropMain.class,queryRule);
		if(lossPropMainList!=null&&!lossPropMainList.isEmpty()){
			lossPropMainVoList = Beans.copyDepth().from(lossPropMainList).toList(PrpLdlossPropMainVo.class);
		}
		return lossPropMainVoList;
	}

	@Override
	public PrpLdlossPropMainVo findVoByRegistNoAndPaid(String registNo,Long paId) {
		PrpLdlossPropMainVo prpLdlossPropMainVo=null;
		QueryRule queryRule=QueryRule.getInstance();
		queryRule.addEqual("registNo", registNo);
		queryRule.addEqual("paId", paId);
		List<PrpLdlossPropMain> dlossPropMainPos = databaseDao.findAll(PrpLdlossPropMain.class, queryRule);
		if(dlossPropMainPos!=null && dlossPropMainPos.size()>0){
			prpLdlossPropMainVo = Beans.copyDepth().from(dlossPropMainPos.get(0)).to(PrpLdlossPropMainVo.class);
		}
		return prpLdlossPropMainVo;
	}
	
	
}
