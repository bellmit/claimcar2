package ins.sino.claimcar.base.service;

import ins.framework.common.ResultPage;
import ins.framework.dao.database.DatabaseDao;
import ins.framework.dao.database.support.Page;
import ins.framework.dao.database.support.QueryRule;
import ins.framework.utils.Beans;
import ins.platform.utils.SqlJoinUtils;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.base.po.PrpLClaim;
import ins.sino.claimcar.base.po.PrpdAddressee;
import ins.sino.claimcar.base.po.PrpsmsMessage;
import ins.sino.claimcar.base.po.SysMsgModel;
import ins.sino.claimcar.claim.vo.PrpsmsMessageVo;
import ins.sino.claimcar.manager.vo.PrpdAddresseeVo;
import ins.sino.claimcar.other.service.MsgModelService;
import ins.sino.claimcar.other.vo.SysMsgModelVo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;



@Service(protocol = {"dubbo"}, validation = "true" , registry = {"default"})
@Path(value = "msgModelService")
public class MsgModelServiceImpl implements MsgModelService {
	private static Logger logger = LoggerFactory.getLogger(MsgModelServiceImpl.class);
	@Autowired
	DatabaseDao databaseDao;
	
	/**
	 * 保存或更新短信模板
	 * 
	 * @param SysMsgModelVo
	 * @return sysMsgModel
	 */
	@Override
	public void saveOrUpdateSysMsgModel(
			SysMsgModelVo sysMsgModelVo) {
			
		    SysMsgModel sysMsgModel = Beans.copyDepth().from(sysMsgModelVo).to(SysMsgModel.class);
			
		if (sysMsgModel.getId() != null) {
			databaseDao.update(SysMsgModel.class, sysMsgModel);
		} else {
			databaseDao.save(SysMsgModel.class, sysMsgModel);
			}
			
//		return sysMsgModel;

	}
	
	/**
	 * 根据主键find SysMsgModelVo
	 * 
	 * @param id
	 * @return modelVo
	 */
	@Override
	public SysMsgModelVo findSysMsgModelVoByPK(Long id) {

		SysMsgModelVo modelVo = new SysMsgModelVo();
		if (id != null) {
			SysMsgModel sysMsgModel = databaseDao.findByPK(
					SysMsgModel.class, id);

			modelVo = Beans.copyDepth().from(sysMsgModel)
					.to(SysMsgModelVo.class);
		}

		return modelVo;

	}
	
	@Override
	public ResultPage<SysMsgModelVo> findAllSysMsgModelByHql(
			SysMsgModelVo sysMsgModelVo, int start, int length) {
		/*// 定义参数list，执行查询时需要转换成object数组
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqualIfExist("modelName",sysMsgModelVo.getModelName());
		queryRule.addEqualIfExist("modelType",sysMsgModelVo.getModelType());
		queryRule.addEqualIfExist("systemNode",sysMsgModelVo.getSystemNode());
		queryRule.addEqualIfExist("validFlag",sysMsgModelVo.getValidFlag());
		List<SysMsgModel> sysMsgModelList = databaseDao.findAll(SysMsgModel.class,queryRule);
//		Page<SysMsgModel> page =databaseDao.findPage(SysMsgModel.class, queryRule, start, length);
//		List<SysMsgModel> sysMsgModelList=page.getResult();
		List<SysMsgModelVo> sysMsgModelVos = new ArrayList<SysMsgModelVo>();
		SysMsgModelVo MsgModelVo=new SysMsgModelVo();
		if(sysMsgModelList !=null && sysMsgModelList.size()>0){
				sysMsgModelVos=Beans.copyDepth().from(sysMsgModelList).toList(SysMsgModelVo.class);
		}
		

		return sysMsgModelVos;*/
		
		
			SqlJoinUtils sqlUtil=new SqlJoinUtils();
			sqlUtil.append(" FROM SysMsgModel msg  where 1=1");
//			sqlUtil.append(" where 1=1 ");
			if(StringUtils.isNotBlank(sysMsgModelVo.getModelName())){
				sqlUtil.append(" AND msg.modelName = ?");
				sqlUtil.addParamValue(sysMsgModelVo.getModelName());
			}
			if(StringUtils.isNotBlank(sysMsgModelVo.getModelType())){
				sqlUtil.append(" AND msg.modelType = ?");
				sqlUtil.addParamValue(sysMsgModelVo.getModelType());
				}
			
			if(StringUtils.isNotBlank(sysMsgModelVo.getSystemNode())){
				sqlUtil.append(" AND msg.systemNode = ?");
				sqlUtil.addParamValue(sysMsgModelVo.getSystemNode());
			}
			
			if(StringUtils.isNotBlank(sysMsgModelVo.getValidFlag())){
				sqlUtil.append(" AND msg.validFlag = ?");
				sqlUtil.addParamValue(sysMsgModelVo.getValidFlag());
			}
			
			if(StringUtils.isNotBlank(sysMsgModelVo.getCaseType())){
				sqlUtil.append(" AND msg.caseType = ?");
				sqlUtil.addParamValue(sysMsgModelVo.getCaseType());
			}
			
		    String sql = sqlUtil.getSql();
			Object[] values = sqlUtil.getParamValues();
			logger.info("=========xxsql="+sql);
			logger.info("=========values="+ArrayUtils.toString(values));
		

			Page<Object[]> page = databaseDao.findPageByHql(sql,start/length+1,length,values);
			logger.info("=========page="+page.getPageSize());
	        
			// 对象转换
			List<SysMsgModelVo> resultVoList=new ArrayList<SysMsgModelVo>();
			for(int i = 0; i<page.getResult().size(); i++ ){

				SysMsgModelVo resultVo=new SysMsgModelVo();
				Object obj = page.getResult().get(i);

				/*PrpLWfTaskQuery wfTaskQuery = (PrpLWfTaskQuery)obj[0];//强制转换
				Beans.copy().from(wfTaskQuery).to(resultVo);*/
				
				SysMsgModel sysMsgModel = (SysMsgModel)obj;
				Beans.copy().from(sysMsgModel).excludeNull().to(resultVo);
				resultVoList.add(resultVo);
			}

			ResultPage<SysMsgModelVo> resultPage = new ResultPage<SysMsgModelVo> (start, length, page.getTotalCount(), resultVoList);
			return resultPage;
	}
	
	
	
	@Override
	public ResultPage<PrpdAddresseeVo> searchLeaderInfo(
			PrpdAddresseeVo prpdAddresseeVo, int start, int length){
		SqlJoinUtils sqlUtil=new SqlJoinUtils();
		sqlUtil.append(" FROM PrpdAddressee addressee  where 1=1");
		if(StringUtils.isNotBlank(prpdAddresseeVo.getName())){
			sqlUtil.append(" AND addressee.name = ?");
			sqlUtil.addParamValue(prpdAddresseeVo.getName());
		}
		if(StringUtils.isNotBlank(prpdAddresseeVo.getMobileNo())){
			sqlUtil.append(" AND addressee.mobileNo = ?");
			sqlUtil.addParamValue(prpdAddresseeVo.getMobileNo());
		}
		if(StringUtils.isNotBlank(prpdAddresseeVo.getComCode())){
			sqlUtil.append(" AND addressee.comCode = ?");
			sqlUtil.addParamValue(prpdAddresseeVo.getComCode());
		}

		sqlUtil.append(" AND addressee.flag in (?,?,?)");
		sqlUtil.addParamValue("1");
		sqlUtil.addParamValue("2");
		sqlUtil.addParamValue("3");

		sqlUtil.append(" order by addressee.updateTime desc ");
		String sql = sqlUtil.getSql();
		Object[] values = sqlUtil.getParamValues();
		Page<Object[]> page = databaseDao.findPageByHql(sql,start/length+1,length,values);
		List<PrpdAddresseeVo> resultVoList=new ArrayList<PrpdAddresseeVo>();
		for(int i = 0; i<page.getResult().size(); i++ ){
			PrpdAddresseeVo resultVo = new PrpdAddresseeVo();
			Object obj = page.getResult().get(i);
			PrpdAddressee prpdAddressee = (PrpdAddressee)obj;
			Beans.copy().from(prpdAddressee).to(resultVo);
			resultVoList.add(resultVo);
		}
		ResultPage<PrpdAddresseeVo> resultPage = new ResultPage<PrpdAddresseeVo> (start, length, page.getTotalCount(), resultVoList);
		return resultPage;
	}

	@Override
	public List<PrpdAddresseeVo> searchAutoClaimMsgSender(String flag) {
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("flag", flag);
		queryRule.addEqual("validFlag", "1");
		List<PrpdAddressee> list = databaseDao.findAll(PrpdAddressee.class, queryRule);
		List<PrpdAddresseeVo> prpdAddresseeVoList;
		if (list == null) {
			prpdAddresseeVoList = new ArrayList<PrpdAddresseeVo>();
		} else {
			prpdAddresseeVoList = Beans.copyDepth().from(list).toList(PrpdAddresseeVo.class);
		}
		return prpdAddresseeVoList;
	}

	@Override
	public PrpdAddresseeVo findAutoClaimMsgSenderById(Long id) {
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("id", id);
		queryRule.addEqual("validFlag", "1");
		PrpdAddressee prpdAddressee = databaseDao.findUnique(PrpdAddressee.class, queryRule);
		PrpdAddresseeVo prpdAddresseeVo = null;
		if (prpdAddressee != null) {
			prpdAddresseeVo = Beans.copyDepth().from(prpdAddressee).to(PrpdAddresseeVo.class);
		}
		return prpdAddresseeVo;
	}

	@Override
	public void updateAutoClaimMsgSender(PrpdAddresseeVo prpdAddresseeVo) {
		if (prpdAddresseeVo != null) {
			PrpdAddressee prpdAddressee = Beans.copyDepth().from(prpdAddresseeVo).to(PrpdAddressee.class);
			databaseDao.update(PrpdAddressee.class, prpdAddressee);
		}
	}

	@Override
	public List<PrpdAddresseeVo> findAllLeaderInfo(){
		SqlJoinUtils sqlUtil=new SqlJoinUtils();
		sqlUtil.append(" FROM PrpdAddressee addressee  where 1=1");
		sqlUtil.append(" AND addressee.flag in (?,?,?)");
		sqlUtil.addParamValue("1");
		sqlUtil.addParamValue("2");
		sqlUtil.addParamValue("3");
		sqlUtil.append(" order by addressee.updateTime desc ");
		String sql = sqlUtil.getSql();
		Object[] values = sqlUtil.getParamValues();
		List<PrpdAddressee> list = databaseDao.findAllByHql(PrpdAddressee.class, sql, values);
		List<PrpdAddresseeVo> prpdAddresseeVoList = new ArrayList<PrpdAddresseeVo>();
		prpdAddresseeVoList=Beans.copyDepth().from(list).toList(PrpdAddresseeVo.class);
		return prpdAddresseeVoList;
	}

	/**
	 * 根据主键删除模板类型
	 * 
	 * @param id
	 */
	@Override
	public void deleteSysMsgModelByPK(Long id) {
		if (id != null) {
			databaseDao.deleteByPK(SysMsgModel.class, id);
		}
	}
	
	@Override
	public void updateLeaderInfo(List<PrpdAddresseeVo> list,String userCode){
		//如果已有的ID不存在参数中，既是删除
		List<PrpdAddresseeVo> prpdAddresseeVoList=this.findAllLeaderInfo();
		for(PrpdAddresseeVo prpdAddresseeVo:prpdAddresseeVoList){
			Boolean isDelete = true;
			for(PrpdAddresseeVo vo:list){
				if(vo!=null&&prpdAddresseeVo.getId().equals(vo.getId())){
					isDelete = false;
					break;
				}
			}
			if(isDelete){
				databaseDao.deleteByPK(PrpdAddressee.class, prpdAddresseeVo.getId());
			}
		}
		Date date = new Date();
		for(PrpdAddresseeVo prpdAddresseeVo:list){
			PrpdAddressee prpdAddressee = new PrpdAddressee();
			//如果ID不空，即更新
			if(prpdAddresseeVo!=null&&prpdAddresseeVo.getId() != null){
				prpdAddressee=databaseDao.findByPK(PrpdAddressee.class, prpdAddresseeVo.getId());
				prpdAddressee.setName(prpdAddresseeVo.getName());
				prpdAddressee.setMobileNo(prpdAddresseeVo.getMobileNo());
				prpdAddressee.setComCode(prpdAddresseeVo.getComCode());
				prpdAddressee.setUpdateTime(date);
				prpdAddressee.setUpdateUser(userCode);
				prpdAddressee.setFlag(prpdAddresseeVo.getFlag());
				databaseDao.update(PrpdAddressee.class, prpdAddressee);
			}else if(prpdAddresseeVo!=null){//如果ID为空，就保存
				Beans.copy().from(prpdAddresseeVo).to(prpdAddressee);
				prpdAddressee.setValidFlag("1");
				prpdAddressee.setCreateTime(date);
				prpdAddressee.setCreateUser(userCode);
				databaseDao.save(PrpdAddressee.class, prpdAddressee);
			}
		}
		
		
	}
	
	@Override
	public String activOrCancel(String id,String validFlag){
		SysMsgModel sysMsgModel = databaseDao.findByPK(SysMsgModel.class, Long.valueOf(id));
		String resuleData = "";
		if("1".equals(validFlag)){
			sysMsgModel.setValidFlag("0");
			resuleData = "模板注销成功！";
		}else{
			sysMsgModel.setValidFlag("1");
			resuleData = "模板激活成功！";
		}
		return resuleData;
	}

	/**
	 * 短信查询
	 */
	@Override
	public ResultPage<PrpsmsMessageVo> findAllSmsMessageByHql(
			PrpsmsMessageVo prpsmsMessageVo, int start, int length) throws Exception {
		SqlJoinUtils sqlUtil=new SqlJoinUtils();
	    sqlUtil.append("FROM PrpsmsMessage sms  where 1=1 ");
		if(StringUtils.isNotBlank(prpsmsMessageVo.getBusinessNo())){
			sqlUtil.append("AND sms.businessNo= ? ");
			sqlUtil.addParamValue(prpsmsMessageVo.getBusinessNo().trim());
		}
		//添加 发送节点 查询条件
		if(StringUtils.isNotBlank(prpsmsMessageVo.getSendNodecode())){
			sqlUtil.append(" AND sms.sendNodecode= ?  ");
			sqlUtil.addParamValue(prpsmsMessageVo.getSendNodecode().trim());
		}
		if(StringUtils.isNotBlank(prpsmsMessageVo.getPhoneCode())){
			sqlUtil.append("AND sms.phoneCode like ? ");
		    sqlUtil.addParamValue(prpsmsMessageVo.getPhoneCode().trim()+"%");
		    }
		
		if(prpsmsMessageVo.getTruesendTimeEnd()!=null && prpsmsMessageVo.getTruesendTimeStart()!=null){
			sqlUtil.andDate(prpsmsMessageVo,"sms","truesendTime");
		}
		
		sqlUtil.append("order by sms.truesendTime DESC ");
		String sql =sqlUtil.getSql();
		Object[] values = sqlUtil.getParamValues();
		logger.info("=========xxsql="+sql);
		logger.info("=========values="+ArrayUtils.toString(values));
		Page<Object[]> page = databaseDao.findPageByHql(sql,start/length+1,length,values);
		
		// 对象转换
	    List<PrpsmsMessageVo> resultVoList=new ArrayList<PrpsmsMessageVo>();
			for(int i = 0; i<page.getResult().size(); i++ ){

				PrpsmsMessageVo resultVo=new PrpsmsMessageVo();
				Object obj = page.getResult().get(i);
				PrpsmsMessage prpsmsMessage = (PrpsmsMessage)obj;
						Beans.copy().from(prpsmsMessage).excludeNull().to(resultVo);
						if("1".equals(resultVo.getStatus())){
							resultVo.setStatus("推送平台成功");
						}
						if("0".equals(resultVo.getStatus())){
							resultVo.setStatus("推送平台失败");
						}
						resultVoList.add(resultVo);
				}

	ResultPage<PrpsmsMessageVo> resultPage = new ResultPage<PrpsmsMessageVo> (start, length, page.getTotalCount(), resultVoList);
    return resultPage;
	}

	@Override
	public void saveorUpdatePrpSmsMessage(PrpsmsMessageVo prpsmsMessageVo) {
		PrpsmsMessage prpsmsMessage = Beans.copyDepth().from(prpsmsMessageVo).to(PrpsmsMessage.class);
			
		if (prpsmsMessage.getMisId() != null) {
				databaseDao.update(PrpsmsMessage.class, prpsmsMessage);
		} else {
			databaseDao.save(PrpsmsMessage.class, prpsmsMessage);
			}
	}

    @Override
    public List<PrpsmsMessageVo> findPrpSmsMessageByBusinessNo(String businessNo) {
        QueryRule queryRule = QueryRule.getInstance();
        queryRule.addEqual("businessNo",businessNo);
        List<PrpsmsMessage> prpsmsMessages = databaseDao.findAll(PrpsmsMessage.class,queryRule);
        List<PrpsmsMessageVo> prpsmsMessageVos = new ArrayList<PrpsmsMessageVo>();
        prpsmsMessageVos = Beans.copyDepth().from(prpsmsMessages).toList(PrpsmsMessageVo.class);
        return prpsmsMessageVos;
    }

    @Override
    public PrpsmsMessageVo findPrpSmsMessageByPrimaryKey(BigDecimal misId) {
        PrpsmsMessage prpsmsMessage = databaseDao.findByPK(PrpsmsMessage.class,misId);
        PrpsmsMessageVo prpsmsMessageVo = new PrpsmsMessageVo();
        Beans.copy().from(prpsmsMessage).to(prpsmsMessageVo);
        return prpsmsMessageVo;
    }

	@Override
	public List<PrpsmsMessageVo> findPrpSmsMessagesByStatus(List<String> status) {
		QueryRule queryRule = QueryRule.getInstance();

		Date dNow = new Date();   //当前时间
		Date yesterday = new Date();
		Calendar calendar = Calendar.getInstance(); //得到日历
		calendar.setTime(dNow);//把当前时间赋给日历
		calendar.add(Calendar.DAY_OF_MONTH, -1);  //设置为前一天
		yesterday = calendar.getTime();   //得到前一天的时间

		queryRule.addIn("status", status);
		queryRule.addGreaterThan("createTime", yesterday);
		List<PrpsmsMessage> prpsmsMessages = databaseDao.findAll(PrpsmsMessage.class,queryRule);
		List<PrpsmsMessageVo> prpsmsMessageVos = new ArrayList<PrpsmsMessageVo>();
		if (prpsmsMessages != null) {
		prpsmsMessageVos = Beans.copyDepth().from(prpsmsMessages).toList(PrpsmsMessageVo.class);
		}
		return prpsmsMessageVos;
	}



	@Override
	public List<PrpdAddresseeVo> findLeaderInfoById(Long id) {
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("id",id);
		List<PrpdAddressee> PrpdAddresseeList = databaseDao.findAll(PrpdAddressee.class,queryRule);
		List<PrpdAddresseeVo> prpdAddresseeVoList = new ArrayList<>();
		if (PrpdAddresseeList != null) {
			prpdAddresseeVoList = Beans.copyDepth().from(PrpdAddresseeList).toList(PrpdAddresseeVo.class);
		}
		return prpdAddresseeVoList;
	}

	/**
	 * 添加领导信息
	 * @param list
	 * @param userCode
	 */
	@Override
	public void addLeaderInfo(List<PrpdAddresseeVo> list, String userCode) {
		for (PrpdAddresseeVo prpdAddresseeVo :list){
			PrpdAddressee prpdAddressee = new PrpdAddressee();
			Beans.copy().from(prpdAddresseeVo).to(prpdAddressee);
			prpdAddressee.setCreateUser(userCode);
			prpdAddressee.setCreateTime(new Date());
			prpdAddressee.setValidFlag("1");
			databaseDao.save(PrpdAddressee.class,prpdAddressee);
		}
	}

	/**
	 * 检查是否再一次添加同一个领导信息
	 * @param list
	 * @return
	 */
	@Override
	public boolean checkIfAddAgain(List<PrpdAddresseeVo> list) {

		for (int i = 0; i <list.size() ; i++) {
			PrpdAddresseeVo prpdAddresseeVo = list.get(i);
			for (int j = 0; j < list.size(); j++) {
				if (i!=j && list.get(j).getName().equals(prpdAddresseeVo.getName()) && list.get(j).getMobileNo().equals(prpdAddresseeVo.getMobileNo()) && list.get(j).getComCode().equals(prpdAddresseeVo.getComCode()) && list.get(j).getFlag().equals(prpdAddresseeVo.getFlag())){
					return true;
				}else {
					continue;
				}
			}
		}

		List<PrpdAddressee> prpdAddresseeList = databaseDao.findAll(PrpdAddressee.class);
		for (PrpdAddresseeVo prpdAddresseeVo:list){
			for (PrpdAddressee prpdAddressee :prpdAddresseeList){
				//同一个领导
				if (prpdAddresseeVo.getName().equals(prpdAddressee.getName()) && prpdAddresseeVo.getComCode().equals(prpdAddressee.getComCode()) && prpdAddresseeVo.getMobileNo().equals(prpdAddressee.getMobileNo()) && prpdAddresseeVo.getFlag().equals(prpdAddressee.getFlag())){
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean checkIfExist(List<PrpdAddresseeVo> list) {
		List<PrpdAddressee> prpdAddresseeList = databaseDao.findAll(PrpdAddressee.class);
		for (PrpdAddresseeVo prpdAddresseeVo:list){
			for (PrpdAddressee prpdAddressee :prpdAddresseeList){
				//同一个领导
				if (prpdAddresseeVo.getName().equals(prpdAddressee.getName()) && prpdAddresseeVo.getComCode().equals(prpdAddressee.getComCode()) && prpdAddresseeVo.getMobileNo().equals(prpdAddressee.getMobileNo()) && prpdAddresseeVo.getFlag().equals(prpdAddressee.getFlag())){
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 修改领导信息
	 * @param list
	 * @param userCode
	 */
	@Override
	public void updateLeaderInfo2(List<PrpdAddresseeVo> list, String userCode) {
		for (PrpdAddresseeVo prpdAddresseeVo : list){
			PrpdAddressee prpdAddressee = Beans.copyDepth().from(prpdAddresseeVo).to(PrpdAddressee.class);
			prpdAddressee.setUpdateTime(new Date());
			prpdAddressee.setUpdateUser(userCode);
			databaseDao.update(PrpdAddressee.class,prpdAddressee);
		}
	}

	@Override
	public void deleteLeaderInfoById(Long id) {
		if (id!=null){
			databaseDao.deleteByPK(PrpdAddressee.class,id);
		}

	}
}
