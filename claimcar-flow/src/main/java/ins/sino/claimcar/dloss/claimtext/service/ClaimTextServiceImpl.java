/******************************************************************************
* CREATETIME : 2015年12月10日 下午3:34:33
* FILE       : ins.sino.claimcar.dloss.ClaimTextService
******************************************************************************/
package ins.sino.claimcar.dloss.claimtext.service;

import ins.framework.dao.database.DatabaseDao;
import ins.framework.dao.database.support.QueryRule;
import ins.framework.utils.Beans;
import ins.platform.schema.SysCodeDict;
import ins.platform.utils.SqlJoinUtils;
import ins.sino.claimcar.common.claimtext.service.ClaimTextService;
import ins.sino.claimcar.common.claimtext.vo.PrpLClaimTextVo;
import ins.sino.claimcar.dloss.claimtext.po.PrpLClaimText;
import ins.sino.claimcar.flow.constant.FlowNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Path;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;


/**
 * <pre></pre>
 * @author ★yangkun
 */
@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"})
@Path("claimTextService")
public class ClaimTextServiceImpl implements ClaimTextService{

	@Autowired
	DatabaseDao databaseDao;
	
	private static Logger logger = LoggerFactory.getLogger(ClaimTextServiceImpl.class); 
	
	public void saveOrUpdte(PrpLClaimTextVo claimTextVo){
		PrpLClaimText claimText = null;
		if(claimTextVo.getId()!=null){
			claimText = databaseDao.findByPK(PrpLClaimText.class,claimTextVo.getId());
			Beans.copy().from(claimTextVo).excludeNull().to(claimText);
			claimText.setContentCode(claimTextVo.getContentCode());
		}else{
			claimText = new PrpLClaimText();
			Beans.copy().from(claimTextVo).to(claimText);
		}
		
		databaseDao.save(PrpLClaimText.class,claimText);
	}
	
	public List<PrpLClaimTextVo> findClaimTextList(Long bussTaskId,String registNo,String bigNode){
		List<PrpLClaimTextVo> claimTextVos = new ArrayList<PrpLClaimTextVo>();
		if(StringUtils.isBlank(registNo)&&(bussTaskId==null||StringUtils.isBlank(bigNode))){
			return claimTextVos;
		}
		SqlJoinUtils sqlUtil = new SqlJoinUtils();
		sqlUtil.append(" FROM PrpLClaimText text Where flag =? ");
		
		sqlUtil.addParamValue("1");
		
		if(StringUtils.isNotBlank(registNo)){
			sqlUtil.append(" And registNo = ?");
			sqlUtil.addParamValue(registNo);
		}
		
		if(StringUtils.isNotBlank(bigNode)&& bussTaskId !=null){
			sqlUtil.append(" And (( text.bussTaskId= ? And bigNode = ? )");
			sqlUtil.addParamValue(bussTaskId);
			sqlUtil.addParamValue(bigNode);
			if(FlowNode.DLCar.name().equals(bigNode)){//定损需要查询复勘数据
				sqlUtil.append(" Or (nodeCode = ? and bussNo=? )");
				sqlUtil.addParamValue("ChkRe");
				sqlUtil.addParamValue(bussTaskId.toString());
			}
			sqlUtil.append(")");
			
		}
		
		sqlUtil.append(" order by createTime asc ");
		String sql = sqlUtil.getSql();
		Object[] values = sqlUtil.getParamValues();
		logger.debug("taskQrySql="+sql);
		logger.debug("ParamValues="+ArrayUtils.toString(values));
		
		List<PrpLClaimText> claimTexts = databaseDao.findAllByHql(PrpLClaimText.class,sql,values);
//		List<Object[]> result = databaseDao.findAllByHql(sql, values);
//		for(int i = 0; i<result.size(); i++ ){ 
//			
//		}
//		QueryRule queryRule = QueryRule.getInstance();
//		queryRule.addEqual("bussTaskId",bussTaskId);
//		if(StringUtils.isNotBlank(registNo)){
//			queryRule.addEqual("registNo",registNo);
//		}
//		if(StringUtils.isNotBlank(bigNode)){
//			queryRule.addEqual("bigNode",bigNode);
//		}
//		queryRule.addEqual("flag","1");
//		queryRule.addDescOrder("createTime");
//		
//		List<PrpLClaimText> claimTexts = databaseDao.findAll(PrpLClaimText.class,queryRule);
		
		for(PrpLClaimText claimText : claimTexts){
			PrpLClaimTextVo claimTextVo = new PrpLClaimTextVo();
			Beans.copy().from(claimText).to(claimTextVo);
			claimTextVos.add(claimTextVo);
		}
		
		return claimTextVos;
	}
	
	/**
	 * flag 节点未完成0,完成之后为1 
	 * @modified:
	 * ☆yangkun(2016年1月19日 上午9:50:26): <br>
	 */
	public PrpLClaimTextVo findClaimTextByNode(Long bussTaskId,String nodeCode,String flag){
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("bussTaskId",bussTaskId);
		queryRule.addEqual("nodeCode",nodeCode);
		queryRule.addEqual("flag",flag);
		queryRule.addDescOrder("inputTime");
		
		List<PrpLClaimText> claimTexts = databaseDao.findAll(PrpLClaimText.class,queryRule);
		if(claimTexts == null || claimTexts.size()==0){
			return null;
		}
		PrpLClaimTextVo claimTextVo = new PrpLClaimTextVo();
		Beans.copy().from(claimTexts.get(0)).to(claimTextVo);
		
		return claimTextVo;
	}

	@Override
	public List<PrpLClaimTextVo> findClaimTextList(Map<String,String> map) {
		List<PrpLClaimTextVo> claimTextVos = new ArrayList<PrpLClaimTextVo>();
		QueryRule queryRule = QueryRule.getInstance();
		if(map==null||map.size()==0||map.containsKey(null)){
			return claimTextVos;
		}
		for (String key : map.keySet()) {
			queryRule.addEqual(key,map.get(key));
		}
		queryRule.addDescOrder("createTime");
		
		List<PrpLClaimText> claimTexts = databaseDao.findAll(PrpLClaimText.class,queryRule);
		for(PrpLClaimText claimText : claimTexts){
			PrpLClaimTextVo claimTextVo = new PrpLClaimTextVo();
			Beans.copy().from(claimText).to(claimTextVo);
			claimTextVos.add(claimTextVo);
		}
		
		return claimTextVos;
	}

	@Override
	public PrpLClaimTextVo findClaimTextByLossCarMainIdAndNodeCode(Long lossCarMainId, String nodeCode) {
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("bussTaskId",lossCarMainId);
		queryRule.addEqual("nodeCode",nodeCode);
		queryRule.addDescOrder("createTime");
		List<PrpLClaimText> claimTexts = databaseDao.findAll(PrpLClaimText.class,queryRule);
		if(claimTexts == null || claimTexts.size()==0){
			return null;
		}
		PrpLClaimTextVo claimTextVo = new PrpLClaimTextVo();
		Beans.copy().from(claimTexts.get(0)).to(claimTextVo);
		
		
		return claimTextVo;
	}

	@Override
	public List<PrpLClaimTextVo> findClaimTextByregistNoAndbussTaskId(String registNo, Long bussTaskId) {
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("bussTaskId",bussTaskId);
		queryRule.addEqual("registNo",registNo);
		queryRule.addDescOrder("createTime");
		List<PrpLClaimTextVo> claimTextVos = new ArrayList<PrpLClaimTextVo>();
		List<PrpLClaimText> claimTexts = databaseDao.findAll(PrpLClaimText.class,queryRule);
		for(PrpLClaimText claimText : claimTexts){
			PrpLClaimTextVo claimTextVo = new PrpLClaimTextVo();
			Beans.copy().from(claimText).to(claimTextVo);
			claimTextVos.add(claimTextVo);
		}
		
		return claimTextVos;
	}
	
}
