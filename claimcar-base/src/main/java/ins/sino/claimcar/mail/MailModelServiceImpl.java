package ins.sino.claimcar.mail;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ws.rs.Path;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import ins.sino.claimcar.base.po.*;
import ins.sino.claimcar.claim.vo.PrpsmsMessageVo;
import ins.sino.claimcar.manager.vo.PrpdJSONEmailVo;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.lucene.util.automaton.RegExp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.SpringProperties;

import com.alibaba.dubbo.config.annotation.Service;

import ins.framework.common.ResultPage;
import ins.framework.dao.database.DatabaseDao;
import ins.framework.dao.database.support.Page;
import ins.framework.dao.database.support.QueryRule;
import ins.framework.service.CodeTranService;
import ins.framework.utils.Beans;
import ins.platform.common.web.util.SpringUtils;
import ins.platform.utils.DataUtils;
import ins.platform.utils.SqlJoinUtils;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.CodeConstants.ValidFlag;
import ins.sino.claimcar.check.service.CheckHandleService;
import ins.sino.claimcar.check.vo.PrpLCheckCarVo;
import ins.sino.claimcar.check.vo.PrpLCheckPropVo;
import ins.sino.claimcar.check.vo.PrpLCheckVo;
import ins.sino.claimcar.claim.vo.PrpsmsEmailVo;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.lossperson.service.PersTraceService;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceMainVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceVo;
import ins.sino.claimcar.mail.service.MailModelService;
import ins.sino.claimcar.mail.vo.PrpLMailModelVo;
import ins.sino.claimcar.mail.vo.mailParam;
import ins.sino.claimcar.manager.vo.PrpdEmailVo;
import ins.sino.claimcar.other.service.SendMsgService;
import ins.sino.claimcar.other.vo.SendMsgParamVo;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.vo.PrpLCItemKindVo;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;
@Service(protocol = {"dubbo"}, validation = "true" , registry = {"default"})
@Path(value = "mailModelService")
public class MailModelServiceImpl implements MailModelService {

	private static Logger logger = LoggerFactory.getLogger(MailModelServiceImpl.class);
	@Autowired
	DatabaseDao databaseDao;
	@Autowired
	RegistQueryService registQueryService;
	@Autowired
	CodeTranService codeTranService;
	@Autowired
	SendMsgService sendMsgService;
	@Autowired
	PolicyViewService policyViewService;
	@Autowired
	CheckHandleService checkHandleService;
	@Autowired
	PersTraceService persTraceService;
	
	@Override
	public ResultPage<PrpLMailModelVo> findAllSysMsgModelByHql(
			PrpLMailModelVo mailModelVo, int start, int length) {

		SqlJoinUtils sqlUtil=new SqlJoinUtils();
		sqlUtil.append(" from PrpLMailModel mm where 1=1 ");
		if(StringUtils.isNotBlank(mailModelVo.getModelName())){
			sqlUtil.append(" AND mm.modelName like ?");
			sqlUtil.addParamValue("%"+mailModelVo.getModelName()+"%");
		}
		if(StringUtils.isNotBlank(mailModelVo.getModelType())){
			sqlUtil.append(" AND mm.modelType = ?");
			sqlUtil.addParamValue(mailModelVo.getModelType());
		}
		if(StringUtils.isNotBlank(mailModelVo.getSystemNode())){
			sqlUtil.append(" AND mm.systemNode = ?");
			sqlUtil.addParamValue(mailModelVo.getSystemNode());
		}
		if(StringUtils.isNotBlank(mailModelVo.getValidFlag())){
			sqlUtil.append(" AND mm.validFlag = ?");
			sqlUtil.addParamValue(mailModelVo.getValidFlag());
		}
		//分公司员工只能查自己机构的模板，总公司员工可以查全部机构的模板
		if(StringUtils.isNotBlank(mailModelVo.getComCode()) && !mailModelVo.getComCode().startsWith("0000") && !mailModelVo.getComCode().startsWith("0001")){
			sqlUtil.append(" AND mm.comCode like ?");
			sqlUtil.addParamValue((mailModelVo.getComCode().startsWith("0002") ? mailModelVo.getComCode().substring(0, 4):mailModelVo.getComCode().substring(0, 2))+"%");
		}
		
		String sql = sqlUtil.getSql();
		Object[] values = sqlUtil.getParamValues();
		logger.info("=========findMailModelSql="+sql);
		logger.info("=========values="+ArrayUtils.toString(values));
		
		Page<Object[]> page = databaseDao.findPageByHql(sql,start/length+1,length,values);
		logger.info("=========page="+page.getPageSize());
		// 对象转换
		List<PrpLMailModelVo> resultVoList = new ArrayList<PrpLMailModelVo>();
		for(int i = 0; i<page.getResult().size(); i++ ){
			PrpLMailModelVo resultVo = new PrpLMailModelVo();
			Object obj = page.getResult().get(i);
			PrpLMailModel prpLMailModel = (PrpLMailModel) obj;
			Beans.copy().from(prpLMailModel).to(resultVo);
			resultVoList.add(resultVo);
		}
		ResultPage<PrpLMailModelVo> resultPage = new ResultPage<PrpLMailModelVo>(start, length, page.getTotalCount(), resultVoList);
		
		return resultPage;
	}
	
	@Override
	public PrpLMailModelVo findMailModelByPk(Long id){
		PrpLMailModelVo resultVo = null;
		PrpLMailModel po = databaseDao.findByPK(PrpLMailModel.class, id);
		if(po != null){
			resultVo = new PrpLMailModelVo();
			Beans.copy().from(po).to(resultVo);
		}
		return resultVo ;
	}
	
	@Override
	public void deleteMailModelByPk(Long id){
		if(id!=null){
			databaseDao.deleteByPK(PrpLMailModel.class, id);
		}
	}
	
	@Override
	public String activOrCancel(Long id,String validFlag){
		PrpLMailModel prpLMailModel = databaseDao.findByPK(PrpLMailModel.class, id);
		prpLMailModel.setValidFlag(validFlag);
		String resuleData = "";
		if(CodeConstants.CommonConst.FALSE.equals(validFlag)){
			resuleData = "模板注销成功！";
		}else{
			resuleData = "模板激活成功！";
		}
		return resuleData;
	}
	
	@Override
	public void saveOrUpdateMailModel(PrpLMailModelVo prpLMailModelVo,SysUserVo userVo){
		if(prpLMailModelVo.getId()!=null){
			PrpLMailModel prpLMailModel = databaseDao.findByPK(PrpLMailModel.class, prpLMailModelVo.getId());
			Beans.copy().from(prpLMailModelVo).excludeNull().to(prpLMailModel);
			prpLMailModel.setUpdateUser(userVo.getUserCode());
			prpLMailModel.setUpdateTime(new Date());
			databaseDao.update(PrpLMailModel.class, prpLMailModel);
		}else{
			PrpLMailModel prpLMailModel = new PrpLMailModel();
			Beans.copy().from(prpLMailModelVo).excludeNull().to(prpLMailModel);
			prpLMailModel.setCreateTime(new Date());
			prpLMailModel.setCreateUser(userVo.getUserCode());
			databaseDao.save(PrpLMailModel.class, prpLMailModel);
		}
	}

	/* 
	 * @see ins.sino.claimcar.mail.service.MailModelService#saveOrUpdateEmailVo(ins.sino.claimcar.manager.vo.PrpdEmailVo, ins.platform.vo.SysUserVo)
	 * @param prpdEmailVo
	 * @param user
	 */
	@Override
	public void saveOrUpdateEmailVo(PrpdEmailVo prpdEmailVo,SysUserVo user) {
		// TODO Auto-generated method stub
		
	}

	/* 
	 * @see ins.sino.claimcar.mail.service.MailModelService#searchSendInfo(ins.sino.claimcar.manager.vo.PrpdEmailVo, java.lang.Integer, java.lang.Integer)
	 * @param prpdEmailVo
	 * @param start
	 * @param length
	 * @return
	 */
	@Override
	public ResultPage<PrpdEmailVo> searchSendInfo(PrpdEmailVo prpdEmailVo,Integer start,Integer length) {

		SqlJoinUtils sqlUtil=new SqlJoinUtils();
		sqlUtil.append(" FROM PrpdEmail email  where 1=1");
		if(StringUtils.isNotBlank(prpdEmailVo.getName())){
			sqlUtil.append(" AND email.name = ?");
			sqlUtil.addParamValue(prpdEmailVo.getName());
		}
		if(StringUtils.isNotBlank(prpdEmailVo.getEmail())){
			sqlUtil.append(" AND email.email = ?");
			sqlUtil.addParamValue(prpdEmailVo.getEmail());
		}
		if(StringUtils.isNotBlank(prpdEmailVo.getComCode())){
			sqlUtil.append(" AND email.comCode = ?");
			sqlUtil.addParamValue(prpdEmailVo.getComCode());
		}
	//	sqlUtil.append(" order by email.updateTime desc ");
		String sql = sqlUtil.getSql();
		Object[] values = sqlUtil.getParamValues();
		Page<Object[]> page = databaseDao.findPageByHql(sql,start/length+1,length,values);
		logger.info("邮件地址查询sql=" + sql);
		List<PrpdEmailVo> resultVoList=new ArrayList<PrpdEmailVo>();
		for(int i = 0; i<page.getResult().size(); i++ ){
			PrpdEmailVo resultVo = new PrpdEmailVo();
			Object obj = page.getResult().get(i);
			PrpdEmail prpdEmail = (PrpdEmail)obj;
			Beans.copy().from(prpdEmail).to(resultVo);
			resultVoList.add(resultVo);
		}
		ResultPage<PrpdEmailVo> resultPage = new ResultPage<PrpdEmailVo> (start, length, page.getTotalCount(), resultVoList);
		return resultPage;
	}

	/* 
	 * @see ins.sino.claimcar.mail.service.MailModelService#findAllSenderInfo()
	 * @return
	 */
	@Override
	public List<PrpdEmailVo> findAllSenderInfo(String comCode) {
		QueryRule queryRule = QueryRule.getInstance();
		if(StringUtils.isNotBlank(comCode)){
			queryRule.addEqual("comCode", comCode);
		}
		List<PrpdEmail> list = databaseDao.findAll(PrpdEmail.class,queryRule);
		List<PrpdEmailVo> prpdEmailVoList = new ArrayList<PrpdEmailVo>();
		prpdEmailVoList = Beans.copyDepth().from(list).toList(PrpdEmailVo.class);
		return prpdEmailVoList;
	}

	/* 
	 * @see ins.sino.claimcar.mail.service.MailModelService#updateSenderInfo(java.util.List, java.lang.String)
	 * @param list
	 * @param userCode
	 */
	@Override
	public void updateSenderInfo(List<PrpdEmailVo> list,String userCode) {
		//如果已有的ID不存在参数中，既是删除
		String comCode = null;
		if(!userCode.startsWith("0000") && !userCode.startsWith("0001")){
			if(list != null && !list.isEmpty()){
				PrpdEmailVo prplEmailVo = list.get(0);
				comCode = prplEmailVo.getComCode();
			}
		}
		List<PrpdEmailVo> PrpdEmailVoList=this.findAllSenderInfo(comCode);
		for(PrpdEmailVo prpdEmailVo:PrpdEmailVoList){
			Boolean isDelete = true;
			for(PrpdEmailVo vo:list){
				if(vo!=null && prpdEmailVo.getId().equals(vo.getId())){
					isDelete = false;
					break;
				}
			}
			if(isDelete){
				databaseDao.deleteByPK(PrpdEmail.class, prpdEmailVo.getId());
			}
		}
		Date date = new Date();
		for(PrpdEmailVo prpdEmailVo:list){
			PrpdEmail prpdEmail = new PrpdEmail();
			//如果ID不空，即更新
			if(prpdEmailVo!=null&&prpdEmailVo.getId() != null){
				prpdEmail = databaseDao.findByPK(PrpdEmail.class, prpdEmailVo.getId());
				prpdEmail.setName(prpdEmailVo.getName());
				prpdEmail.setEmail(prpdEmailVo.getEmail());
				prpdEmail.setCaseType(prpdEmailVo.getCaseType());
				prpdEmail.setComCode(prpdEmailVo.getComCode());
				prpdEmail.setUpdateTime(date);
				prpdEmail.setUpdateUser(userCode);
				databaseDao.update(PrpdEmail.class, prpdEmail);
			}else if(prpdEmailVo!=null){//如果ID为空，就保存
				Beans.copy().from(prpdEmailVo).to(prpdEmail);
				prpdEmail.setValidFlag(ValidFlag.VALID);
				prpdEmail.setCreateTime(date);
				prpdEmail.setCreateUser(userCode);
				databaseDao.save(PrpdEmail.class, prpdEmail);
			}
		}
		
	}

	/* 
	 * @see ins.sino.claimcar.mail.service.MailModelService#findAllSmsMessageByHql(ins.sino.claimcar.claim.vo.PrpsmsEmailVo, java.lang.Integer, java.lang.Integer)
	 * @param prpsmsEmailVo
	 * @param start
	 * @param length
	 * @return
	 */
	@Override
	public ResultPage<PrpsmsEmailVo> findAllSmsEmailByHql(PrpsmsEmailVo prpsmsEmailVo,Integer start,Integer length) throws Exception {
		SqlJoinUtils sqlUtil=new SqlJoinUtils();
	    sqlUtil.append("FROM PrpsmsEmail sms  where 1=1 ");
		if(StringUtils.isNotBlank(prpsmsEmailVo.getBusinessNo())){
			sqlUtil.append("AND sms.businessNo= ? ");
			sqlUtil.addParamValue(prpsmsEmailVo.getBusinessNo().trim());
		}
		if(StringUtils.isNotBlank(prpsmsEmailVo.getEmail())){
			sqlUtil.append("AND sms.email like ? ");
		    sqlUtil.addParamValue(prpsmsEmailVo.getEmail().trim()+"%");
		 }
		if(StringUtils.isNotBlank(prpsmsEmailVo.getComCode())){
			sqlUtil.append("AND sms.comCode like ? ");
		    sqlUtil.addParamValue(prpsmsEmailVo.getComCode().trim()+"%");
		 }
		if(StringUtils.isBlank(prpsmsEmailVo.getBusinessNo()) ||
				(StringUtils.isNotBlank(prpsmsEmailVo.getBusinessNo())&&prpsmsEmailVo.getBusinessNo().length() <21)){
			if(prpsmsEmailVo.getCreateTimeStart()!=null && prpsmsEmailVo.getCreateTimeEnd()!=null){
				sqlUtil.andDate(prpsmsEmailVo,"sms","createTime");
			}
			
		}
		sqlUtil.append("order by sms.createTime DESC ");
		String sql =sqlUtil.getSql();
		logger.info("邮件查询sql=" + sql);
		Object[] values = sqlUtil.getParamValues();
		logger.info("=========values="+ArrayUtils.toString(values));
		Page<Object[]> page = databaseDao.findPageByHql(sql,start/length+1,length,values);
		
		// 对象转换
	    List<PrpsmsEmailVo> resultVoList=new ArrayList<PrpsmsEmailVo>();
			for(int i = 0; i<page.getResult().size(); i++ ){
				PrpsmsEmailVo resultVo=new PrpsmsEmailVo();
				Object obj = page.getResult().get(i);
				PrpsmsEmail prpsmsEmail = (PrpsmsEmail)obj;
				Beans.copy().from(prpsmsEmail).excludeNull().to(resultVo);
				if("1".equals(resultVo.getStatus())){
					resultVo.setStatus("推送平台成功");
				}
				if("0".equals(resultVo.getStatus())){
					resultVo.setStatus("推送平台失败");
				}
				resultVoList.add(resultVo);
			}

		ResultPage<PrpsmsEmailVo> resultPage = new ResultPage<PrpsmsEmailVo> (start, length, page.getTotalCount(), resultVoList);
	    return resultPage;
	}

	/* 
	 * @see ins.sino.claimcar.mail.service.MailModelService#insertSmsEmail(ins.sino.claimcar.claim.vo.PrpsmsEmailVo, ins.platform.vo.SysUserVo)
	 * @param prpsmsEmailVo
	 * @param userVo
	 * @return
	 */
	@Override
	public void insertSmsEmail(PrpsmsEmailVo prpsmsEmailVo,SysUserVo userVo) {
		if(prpsmsEmailVo != null){
			PrpsmsEmail smsEmail = new PrpsmsEmail();
			Beans.copy().from(prpsmsEmailVo).to(smsEmail);
			smsEmail.setCreateTime(new Date());
			smsEmail.setUserCode(userVo.getUserCode());
			databaseDao.save(PrpsmsEmail.class,smsEmail);
		}
	}
	
	@Override
	public List<PrpdEmailVo> sendMail(PrpLWfTaskVo wfTaskVo,SysUserVo userVo){
		PrpLRegistVo registVo = registQueryService.findByRegistNo(wfTaskVo.getRegistNo());
		String comCode = registVo.getComCode();
		List<String> comcodes = new ArrayList<String>();
		comCode = comCode.startsWith("00") ? comCode.substring(0, 4)+"0000":comCode.substring(0, 2)+"000000";
		comcodes.add(comCode);
		comcodes.add(CodeConstants.headOffice);
		String caseType = FlowNode.ChkBig.name().equals(wfTaskVo.getNodeCode()) ? CodeConstants.Mail_CaseType.DLoss:CodeConstants.Mail_CaseType.PLoss;
		List<PrpdEmailVo> prpdEmailVoList = findMailAddr(comcodes, caseType);
		String modelType =  FlowNode.ChkBig.name().equals(wfTaskVo.getNodeCode()) ? "1":"2";
		String systemNode = FlowNode.ChkBig.name().equals(wfTaskVo.getNodeCode()) ? "1":"2";
		PrpLMailModelVo modelVo = findMailModel(comCode, modelType, systemNode);
		
		if(prpdEmailVoList!=null&&prpdEmailVoList.size()>0&&modelVo!=null){
			String toAddress = "";
			mailParam paramVo = getMailParam(wfTaskVo,registVo, comCode);
			String mailSubject = codeTranService.transCode("ComCodeLv2",comCode)+(FlowNode.ChkBig.name().equals(wfTaskVo.getNodeCode()) ? "车物重案上报":"人伤重案上报")+wfTaskVo.getRegistNo();
			String mailBody = getMailBody(modelVo.getContent(), paramVo);
			String username= SpringProperties.getProperty("mailusername");
			String password= SpringProperties.getProperty("mailpassword");
			String sendNick = SpringProperties.getProperty("mailNick");
			String requestXML = "";
			String responContent = "";
			String status = CodeConstants.CommonConst.FALSE;
			for(PrpdEmailVo prpdEmailVo:prpdEmailVoList){
				try{
					toAddress = prpdEmailVo.getEmail();
					requestXML = CodeConstants.MailXML_Head+"<arg0>"+toAddress+"</arg0><arg1>"+mailSubject+"</arg1><arg2>"+mailBody+"</arg2><arg3>"
							+username+ "</arg3><arg4>"+ password +"</arg4><arg5>"+ sendNick +"</arg5>" + CodeConstants.MailXML_End;
					HttpPost httpPost = new HttpPost(SpringProperties.getProperty("Mail_URL"));
					httpPost.setHeader("Content-Type", "text/xml;charset=UTF-8");
			        if(requestXML!=null&& !requestXML.trim().equals("")){
			            StringEntity requestEntity = new StringEntity(requestXML,Charset.forName("UTF-8"));
			            httpPost.setEntity(requestEntity);
			            logger.info(wfTaskVo.getRegistNo()+"===========================发送邮件请求报文："+requestXML);
			        }
			        DefaultHttpClient httpClient = new DefaultHttpClient();
			        HttpResponse response = httpClient.execute(httpPost);
			        HttpEntity httpEntity = response.getEntity();
			        responContent = EntityUtils.toString(httpEntity);
			        // 返回报文中是否有返回状态
			        if (responContent.indexOf("<return>") != -1) {
						int startIndex = responContent.indexOf("<return>");
						int endIndex = responContent.indexOf("</return>");
						// 如果返回报文中<return>标签内容小写为true，方可证明邮件发送成功，其他情况一致认定为发送失败
						if ("true".equals(responContent.substring(startIndex+"<return>".length(), endIndex).toLowerCase())) {
							status = CodeConstants.CommonConst.TRUE;
						}
					}

					responContent = new String(responContent.getBytes("UTF-8"), "UTF-8");
					logger.info(wfTaskVo.getRegistNo()+"===========================发送邮件返回报文："+responContent);
				}catch(Exception e){
					logger.error(wfTaskVo.getRegistNo()+"发送邮件失败："+e);
					status = CodeConstants.CommonConst.FALSE;
					return null;
				}finally{
					//保存
			        PrpsmsEmailVo prpsmsEmailVo = new PrpsmsEmailVo();
			        prpsmsEmailVo.setBusinessNo(wfTaskVo.getRegistNo());
			        if(StringUtils.isNotBlank(mailBody)){
				        String mailBodyStr = mailBody.replaceAll("\r\n|\r|\n|\t","");
				        prpsmsEmailVo.setSendText(mailBodyStr);
			        } else {
			        	prpsmsEmailVo.setSendText(mailBody);
			        }
			        prpsmsEmailVo.setUserCode(userVo.getUserCode());
			        prpsmsEmailVo.setSendNodecode(wfTaskVo.getSubNodeCode());
			        prpsmsEmailVo.setStatus(status);
			        prpsmsEmailVo.setCreateTime(new Date());
			        prpsmsEmailVo.setComCode(comCode);
			        prpsmsEmailVo.setEmail(prpdEmailVo.getEmail());
			        insertSmsEmail(prpsmsEmailVo, userVo);
				}
			}
	        return prpdEmailVoList;
		}
		return null;
	}
	@Override
	public List<PrpdEmailVo> sendMailNew(PrpLWfTaskVo wfTaskVo,SysUserVo userVo){
		PrpLRegistVo registVo = registQueryService.findByRegistNo(wfTaskVo.getRegistNo());
		String comCode = registVo.getComCode();
		List<String> comcodes = new ArrayList<String>();
		comCode = comCode.startsWith("00") ? comCode.substring(0, 4)+"0000":comCode.substring(0, 2)+"000000";
		comcodes.add(comCode);
		comcodes.add(CodeConstants.headOffice);
		String caseType = FlowNode.ChkBig.name().equals(wfTaskVo.getNodeCode()) ? CodeConstants.Mail_CaseType.DLoss:CodeConstants.Mail_CaseType.PLoss;
		List<PrpdEmailVo> prpdEmailVoList = findMailAddr(comcodes, caseType);
		String modelType =  FlowNode.ChkBig.name().equals(wfTaskVo.getNodeCode()) ? "1":"2";
		String systemNode = FlowNode.ChkBig.name().equals(wfTaskVo.getNodeCode()) ? "1":"2";
		PrpLMailModelVo modelVo = findMailModel(comCode, modelType, systemNode);

		if(prpdEmailVoList!=null&&prpdEmailVoList.size()>0&&modelVo!=null){
			String toAddress = "";
			mailParam paramVo = getMailParam(wfTaskVo,registVo, comCode);
			String mailSubject = codeTranService.transCode("ComCodeLv2",comCode)+(FlowNode.ChkBig.name().equals(wfTaskVo.getNodeCode()) ? "车物重案上报":"人伤重案上报")+wfTaskVo.getRegistNo();//主题
			String mailBody = getMailBody(modelVo.getContent(), paramVo);//内容
			String status = CodeConstants.CommonConst.FALSE;//状态
			for(PrpdEmailVo prpdEmailVo:prpdEmailVoList){
				try{
					toAddress = prpdEmailVo.getEmail();
					boolean flag = this.sendNewEmail(toAddress,mailSubject,mailBody,registVo.getRegistNo());
					if(flag){
						status = CodeConstants.CommonConst.TRUE;
					}
				}catch(Exception e){
					logger.error(wfTaskVo.getRegistNo()+"发送邮件失败："+e);
					status = CodeConstants.CommonConst.FALSE;
					return null;
				}finally{
					//保存
					PrpsmsEmailVo prpsmsEmailVo = new PrpsmsEmailVo();
					prpsmsEmailVo.setBusinessNo(wfTaskVo.getRegistNo());
					if(StringUtils.isNotBlank(mailBody)){
						String mailBodyStr = mailBody.replaceAll("\r\n|\r|\n|\t","");
						prpsmsEmailVo.setSendText(mailBodyStr);
					} else {
						prpsmsEmailVo.setSendText(mailBody);
					}
					prpsmsEmailVo.setUserCode(userVo.getUserCode());
					prpsmsEmailVo.setSendNodecode(wfTaskVo.getSubNodeCode());
					prpsmsEmailVo.setStatus(status);
					prpsmsEmailVo.setCreateTime(new Date());
					prpsmsEmailVo.setComCode(comCode);
					prpsmsEmailVo.setEmail(prpdEmailVo.getEmail());
					insertSmsEmail(prpsmsEmailVo, userVo);
				}
			}
			return prpdEmailVoList;
		}
		return null;
	}

	public String getMailTable(List<PrpdEmailVo> prpdEmailVoList){
		String mailTableStr = null;
		if(prpdEmailVoList!=null && prpdEmailVoList.size()>0){
			StringBuffer sb = new StringBuffer();
			sb.append(CodeConstants.MailTable_Head);
			for(PrpdEmailVo vo:prpdEmailVoList){
				sb.append("      <tr>\n");
				sb.append("         <td>").append(vo.getName()).append("</td>\n");
				sb.append("         <td>").append(vo.getEmail()).append("</td>\n");
				sb.append("      </tr>\n");
			}
			sb.append(CodeConstants.MailTable_End);
			mailTableStr = sb.toString();
		}else{
			mailTableStr = "未发送邮件！";
		}
		return mailTableStr;
	}
	
	private String getMailBody(String message,mailParam paramVo){
		int firstLocation = message.indexOf("{");
		int secondLocation = message.indexOf("}");
		if(firstLocation >= 0 && secondLocation > firstLocation){
			String param = message.substring(firstLocation, secondLocation+1);
			message = this.reParam(message, param, paramVo);
			message = this.getMailBody(message, paramVo);
		}
		
		return message;
	}
	
	public String reParam(String message,String param,mailParam paramVo){
		if("{报案号}".equals(param)){
			message = message.replace(param, paramVo.getRegistNo());
		}else if("{承保机构}".equals(param)){
			message = message.replace(param, codeTranService.transCode("ComCodeLv2",paramVo.getComCode()));
		}else if("{保单号码}".equals(param)){
			message = message.replace(param, paramVo.getPolicyNo());
		}else if("{承保险别}".equals(param)){
			message = message.replace(param, sendMsgService.getKindName(paramVo.getRegistNo()));
		}else if("{承保限额}".equals(param)){
		    StringBuffer stringBuffer = new StringBuffer();
		    if(paramVo.getPrpCItemKinds() != null&&paramVo.getPrpCItemKinds().size() > 0){	        
                for(PrpLCItemKindVo prpLCItemKindVo:paramVo.getPrpCItemKinds()){
				// 车上人员险（乘客，司机）, 盗抢险,第三者责任险 ,车损险
                    if("Y".equals(prpLCItemKindVo.getCalculateFlag())
                    		&&("D11".equals(prpLCItemKindVo.getKindCode())||"D12".equals(prpLCItemKindVo.getKindCode())
                    				||"G".equals(prpLCItemKindVo.getKindCode())||"B".equals(prpLCItemKindVo.getKindCode())
                    				||"A".equals(prpLCItemKindVo.getKindCode()))){
                    	BigDecimal amount = prpLCItemKindVo.getAmount();
						BigDecimal tenThousand = new BigDecimal(10000);
						amount = amount!=null ? amount.divide(tenThousand, 2, RoundingMode.HALF_UP):BigDecimal.ZERO;
					stringBuffer.append(prpLCItemKindVo.getKindName()+":"+amount+"（万）,");
                    }
                }
            }
		    if(stringBuffer.length()>0){
		    	stringBuffer = new StringBuffer(stringBuffer.substring(0, stringBuffer.length()-1));
		    }
            message = message.replace(param, stringBuffer.toString());
	}else if("{出险经过}".equals(param)){
			message = message.replace(param, paramVo.getDangerRemark());
		}else if("{查勘意见}".equals(param)){
			message = message.replace(param, paramVo.getCheckOpinion());
		}else if("{大案上报意见}".equals(param)){
			message = message.replace(param, paramVo.getReportOpinion());
		}else if("{大案审核意见}".equals(param)){
			message = message.replace(param, paramVo.getVerifyOpinion());
		}else if("{车物大案上报金额为系统立案金额}".equals(param)){
			message = message.replace(param, "车物大案上报金额为"+paramVo.getDLAmount());
		}else if("{人伤大案上报金额为人伤立案金额}".equals(param)){
			message = message.replace(param, "人伤大案上报金额为"+paramVo.getPLAmount());
		}
		else{
			message = message.replace(param, "");
		}
		return message;
	}
	
	@Override
	public List<PrpdEmailVo> findMailAddr(List<String> comCodes,String caseType){
		List<PrpdEmailVo> voList = null;
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addIn("comCode", comCodes);
		queryRule.addEqual("caseType", caseType);
		queryRule.addEqual("validFlag", CodeConstants.CommonConst.TRUE);
		
		List<PrpdEmail> poList = databaseDao.findAll(PrpdEmail.class, queryRule);
		if(poList!=null && poList.size()>0){
			voList = new ArrayList<PrpdEmailVo>();
			voList = Beans.copyDepth().from(poList).toList(PrpdEmailVo.class);
		}
		return voList;
	}
	
	public PrpLMailModelVo findMailModel(String comCode,String modelType,String systemNode){
		PrpLMailModelVo voList = null;
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("comCode", comCode);
		queryRule.addEqual("modelType", modelType);
		queryRule.addEqual("systemNode", systemNode);
		queryRule.addEqual("validFlag", CodeConstants.CommonConst.TRUE);
		queryRule.addDescOrder("createTime");
		
		List<PrpLMailModel> poList = databaseDao.findAll(PrpLMailModel.class, queryRule);
		if(poList!=null && poList.size()>0){
			voList = new PrpLMailModelVo();
			Beans.copy().from(poList.get(0)).to(voList);
		}else if(!"00000000".equals(comCode)){
			//如果分公司查不到模板就查总公司的
			QueryRule queryRule_2 = QueryRule.getInstance();
			queryRule_2.addEqual("comCode", "00000000");
			queryRule_2.addEqual("modelType", modelType);
			queryRule_2.addEqual("systemNode", systemNode);
			queryRule_2.addEqual("validFlag", CodeConstants.CommonConst.TRUE);
			queryRule_2.addDescOrder("createTime");
			
			List<PrpLMailModel> prpdEmailList = databaseDao.findAll(PrpLMailModel.class, queryRule_2);
			if(prpdEmailList!=null && prpdEmailList.size()>0){
				voList = new PrpLMailModelVo();
				Beans.copy().from(prpdEmailList.get(0)).to(voList);
			}
		}
		return voList;
	}
	
	private mailParam getMailParam(PrpLWfTaskVo wfTaskVo,PrpLRegistVo registVo,String comCode){
		mailParam paramVo = new mailParam();
		PrpLCMainVo prpLCMainVo=policyViewService.getPolicyInfo(registVo.getRegistNo(), registVo.getPolicyNo());
		PrpLCheckVo checkVo = checkHandleService.queryPrpLCheckVo(registVo.getRegistNo());
		paramVo.setRegistNo(wfTaskVo.getRegistNo());
		paramVo.setComCode(comCode);
		paramVo.setKindCode(sendMsgService.getKindName(registVo.getRegistNo()));
		paramVo.setPolicyNo(registVo.getPolicyNo());
		paramVo.setDangerRemark(StringUtils.isNotBlank(registVo.getPrpLRegistExt().getDangerRemark())?registVo.getPrpLRegistExt().getDangerRemark():"");
		paramVo.setCheckOpinion(StringUtils.isNotBlank(checkVo.getPrpLCheckTask().getContexts())?checkVo.getPrpLCheckTask().getContexts():"");
		if(FlowNode.ChkBig.name().equals(wfTaskVo.getNodeCode())){
			// 车物大案上报意见
			paramVo.setReportOpinion(StringUtils.isNotBlank(checkVo.getPrpLCheckTask().getClaimText())?checkVo.getPrpLCheckTask().getClaimText():"");
			// 车物大案审核通过意见
			paramVo.setVerifyOpinion(StringUtils.isNotBlank(checkVo.getPrpLCheckTask().getVerifyCheckContext())?checkVo.getPrpLCheckTask().getVerifyCheckContext():"");
			BigDecimal dLAmount = BigDecimal.ZERO;
			if(checkVo.getPrpLCheckTask().getPrpLCheckCars()!=null && checkVo.getPrpLCheckTask().getPrpLCheckCars().size()>0){
				for(PrpLCheckCarVo carVo:checkVo.getPrpLCheckTask().getPrpLCheckCars()){
					dLAmount = dLAmount.add(DataUtils.NullToZero(carVo.getLossFee()));
				}
			}
			if(checkVo.getPrpLCheckTask().getPrpLCheckProps()!=null && checkVo.getPrpLCheckTask().getPrpLCheckProps().size()>0){
				for(PrpLCheckPropVo propVo:checkVo.getPrpLCheckTask().getPrpLCheckProps()){
					dLAmount = dLAmount.add(DataUtils.NullToZero(propVo.getLossFee()));
				}
			}
			paramVo.setDLAmount(dLAmount);
		}else{
			PrpLDlossPersTraceMainVo persTraceMainVo = persTraceService.findPersTraceMainVoById(Long.valueOf(wfTaskVo.getHandlerIdKey()));
			List<PrpLDlossPersTraceVo> perstraceVoList = persTraceService.findPersTraceVo(registVo.getRegistNo(), persTraceMainVo.getId());
			BigDecimal plAmount = BigDecimal.ZERO;
			// 人伤大案上报意见
			paramVo.setReportOpinion(StringUtils.isNotBlank(persTraceMainVo.getPayOpinions())?persTraceMainVo.getPayOpinions():"");
			// 人伤大案审核通过意见
			paramVo.setVerifyOpinion(StringUtils.isNotBlank(persTraceMainVo.getMajorcaseOption())?persTraceMainVo.getMajorcaseOption():"");
			if(perstraceVoList!=null && perstraceVoList.size()>0){
				for(PrpLDlossPersTraceVo perstraceVo:perstraceVoList){
					plAmount = plAmount.add(DataUtils.NullToZero(perstraceVo.getSumReportFee()));
				}
			}
			paramVo.setPLAmount(plAmount);
		}
		
		
		return paramVo;
	}

	/* 
	 * @see ins.sino.claimcar.mail.service.MailModelService#existsMailModel(java.lang.Long, java.lang.String, java.lang.String, java.lang.String)
	 * @param id
	 * @param modelType
	 * @param validFlag
	 * @param comCode
	 * @return
	 */
	@Override
	public boolean existsMailModel(Long id,String modelType,String comCode) {
		boolean flag = false;
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("comCode", comCode);
		queryRule.addEqual("modelType", modelType);
		queryRule.addEqual("validFlag", CodeConstants.CommonConst.TRUE);
		List<PrpLMailModel> poList = databaseDao.findAll(PrpLMailModel.class, queryRule);
		if(poList != null &&  !poList.isEmpty()){
			if(id != null){
				for(PrpLMailModel mailModel:poList){
					if(id == mailModel.getId()){
						continue;
					} else{
						flag = true;
					}
				}
			} else{
				flag = true;
			}
		} 
		return flag;
	}

	@Override
	public List<PrpdEmailVo> findMailAddressByCaseType(String caseType) {
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("caseType", caseType);
		queryRule.addEqual("validFlag", "1");
		List<PrpdEmail> prpdEmails = databaseDao.findAll(PrpdEmail.class,queryRule);
		List<PrpdEmailVo> prpdEmailVos = new ArrayList<PrpdEmailVo>();
		if(prpdEmails!=null && prpdEmails.size()>0){
			prpdEmailVos = Beans.copyDepth().from(prpdEmails).toList(PrpdEmailVo.class);
		}
		return prpdEmailVos;
	}

	/**
	 * 发送邮件(最新)，对接邮件平台
	 * @param subject
	 * @param content
	 * @return
	 */
	private boolean sendNewEmail(String toAddress,String subject,String content,String registNo){
		boolean flag= true; //默认成功
		//获取连接信息
		String appCode = SpringProperties.getProperty("appCode");
		String appSecret = SpringProperties.getProperty("appSecret");
		String mailNick = SpringProperties.getProperty("mailNick");
		//组装邮箱数据
		PrpdJSONEmailVo newMailVo = new PrpdJSONEmailVo();
		newMailVo.setEmailNickName(mailNick);
		newMailVo.setAppCode(appCode);
		newMailVo.setAppSecret(appSecret);
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddhhmmss");
		String dateTime = df.format(new Date());
		newMailVo.setAtTime(dateTime);
		newMailVo.setIsIm("1");//是否即时消息：1-是，0-否
		newMailVo.setEmailTo(toAddress);//收件人地址
		newMailVo.setSubject(subject);
		newMailVo.setContent(content);
		newMailVo.setBizType("CC");//业务类型
		newMailVo.setBizNo("CC");
		//发送并接受返回数据
		String responsejson = "";
		String requestjson = null;
		ObjectMapper om = new ObjectMapper();
		try {
			requestjson = om.writeValueAsString(newMailVo);//转JSON格式
			logger.info("邮件发送内容=============："+requestjson);
			responsejson = this.sendEmailJSON(requestjson,registNo);
			logger.info("邮件返回内容=============："+responsejson);
			JsonParser parser = new JsonParser();
			JsonObject jsonobject = parser.parse(responsejson).getAsJsonObject();// 解析返回的json报文
			String result = jsonobject.get("result").getAsString();
			if(!"0".equals(result)){   //非0表示失败
				flag= false;
			}
		} catch (Exception e) {
			flag= false;
			logger.info("邮件发送异常："+e.getMessage());
		}
		return flag;
	}
	/**
	 * 使用JSON格式发送邮件
	 *
	 * @param sendData      发送报文
	 * @param registNo       报案号
	 * @return 收付响应数据
	 * @throws Exception 送收付异常
	 */
	private String sendEmailJSON(String sendData, String registNo) throws Exception {
		if (sendData == null || sendData.trim().length() == 0) {
			logger.info("报案号{}发送邮件数据为空",registNo);
			throw new Exception("业务号：" + registNo + " 推送邮件数据为空!");
		}

		BufferedReader bfreader = null;
		HttpURLConnection httpURLConnection = null;
		OutputStream outputStream = null;
		DataOutputStream out = null;
		StringBuilder buffer = new StringBuilder();

		try {
			URL url = new URL(SpringProperties.getProperty("MailJSON_URL"));
			httpURLConnection = (HttpURLConnection) url.openConnection();
			httpURLConnection.setDoInput(true);
			httpURLConnection.setDoOutput(true);
			httpURLConnection.setRequestMethod("POST");
			// post方式不能使用缓存
			httpURLConnection.setUseCaches(false);
			// 配置本次连接的Content-Type，配置为text/xml
			httpURLConnection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
			// 维持长连接
			httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
			httpURLConnection.setConnectTimeout(20 * 1000);
			httpURLConnection.setReadTimeout(20 * 1000);
			httpURLConnection.setAllowUserInteraction(true);
			httpURLConnection.connect();
		} catch (Exception ex) {
			logger.info("业务号：{} 连接邮件地址失败！", registNo, ex);
			throw new Exception("连接邮件地址失败，请稍候再试！", ex);
		}
		try {
			outputStream = httpURLConnection.getOutputStream();
			out = new DataOutputStream(outputStream);
			out.write(sendData.getBytes("utf-8"));
			out.flush();
			bfreader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), "UTF-8"));
			String strLine = "";
			while ((strLine = bfreader.readLine()) != null) {
				buffer.append(strLine);
			}
		} catch (Exception e) {
			logger.info("业务号：{} 读取邮件接口返回数据失败！", registNo, e);
			throw new Exception("读取邮件接口返回数据失败！", e);
		} finally {
			try {
				if (bfreader != null) {
					bfreader.close();
				}

				if (out != null) {
					out.close();
				}

				if (outputStream != null) {
					outputStream.close();
				}
			} catch (Exception e) {
				logger.info("邮件发送流关闭异常！", e);
				e.printStackTrace();
			}

		}
		return buffer.toString();
	}
}
