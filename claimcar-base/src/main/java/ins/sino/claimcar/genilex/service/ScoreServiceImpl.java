package ins.sino.claimcar.genilex.service;

import ins.platform.common.util.ConfigUtil;
import ins.platform.utils.ClaimBaseCoder;
import ins.platform.vo.PrpLConfigValueVo;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.claim.service.ClaimTaskService;
import ins.sino.claimcar.claim.vo.PrpLClaimVo;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.service.WfTaskHandleService;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.genilex.vo.scoreVo.AccountInfo;
import ins.sino.claimcar.genilex.vo.scoreVo.AccountNumberStatus;
import ins.sino.claimcar.genilex.vo.scoreVo.FraudScore;
import ins.sino.claimcar.genilex.vo.scoreVo.PushFraud;
import ins.sino.claimcar.genilex.vo.scoreVo.Requestor;
import ins.sino.claimcar.genilex.vo.scoreVo.ResponseResult;
import ins.sino.claimcar.genilex.vo.scoreVo.ResponseSummary;
import ins.sino.claimcar.genilex.vo.scoreVo.ScoreReqVo;
import ins.sino.claimcar.genilex.vo.scoreVo.ScoreResVo;
import ins.sino.claimcar.hnbxrest.BaseServlet;
import ins.sino.claimcar.recloss.service.PrpLSurveyService;
import ins.sino.claimcar.recloss.vo.PrpLSurveyVo;
import ins.sino.claimcar.regist.service.PolicyViewService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.SpringProperties;
import org.springframework.orm.hibernate4.HibernateJdbcException;

import com.alibaba.dubbo.common.utils.Assert;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.XStreamException;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import com.thoughtworks.xstream.io.xml.XppDriver;

/**
 * 调度结果通知接口
 * 
 * <pre></pre>
 * @author ★LinYi
 */
public class ScoreServiceImpl extends BaseServlet {

	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(ScoreServiceImpl.class);

    @Autowired
    ScoreSaveService scoreSaveService;
    @Autowired
    PrpLSurveyService prpLSurveyService;
    @Autowired
	WfTaskHandleService wfTaskHandleService;
    @Autowired
    ClaimTaskService claimTaskService;
	@Autowired
	PolicyViewService policyViewService;
    public ScoreServiceImpl() {
        super();
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    doPost(request,response);
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        init();
        
        request.setCharacterEncoding("UTF-8");
	    response.setContentType("text/html;charset=UTF-8");
        String xml = "";
        InputStreamReader read = new InputStreamReader(request.getInputStream(),"UTF-8");
        BufferedReader bufferedReader = new BufferedReader(read);
        String temp = "";
        while(( temp = bufferedReader.readLine() )!=null){
        	xml += temp;
        }
        read.close();
        LOGGER.debug(xml);
        XStream stream = new XStream(new XppDriver(new XmlFriendlyNameCoder("-_","_")));
        stream.autodetectAnnotations(true);
        stream.setMode(XStream.NO_REFERENCES);
        stream.aliasSystemAttribute(null,"class");// 去掉 class属性
        stream.processAnnotations(ScoreReqVo.class);
        ScoreReqVo reqPacket = null;
        String resultDesc = "";
        String resultCode = "";
        ScoreResVo resVo = new ScoreResVo();
        Requestor requestorRes = new Requestor();
        ResponseSummary responseSummary = new ResponseSummary();
        AccountNumberStatus accountNumberStatus = new AccountNumberStatus();
        ResponseResult responseResult = new ResponseResult();
        PrintWriter writer = response.getWriter();
        LOGGER.debug(xml);
        
        try{
        	reqPacket = ClaimBaseCoder.xmlToObj(xml, ScoreReqVo.class);
        	LOGGER.info("评分接口接收报文: \n"+xml);
            Assert.notNull(reqPacket," 请求信息为空  ");
            
            PushFraud  pushFraud = reqPacket.getBody().getPushFraud();
            Requestor requestorReq = pushFraud.getRequestor();
            BigDecimal flowTaskId = new BigDecimal(requestorReq.getReference());
            PrpLWfTaskVo prpLWfTaskVo = wfTaskHandleService.queryTask(flowTaskId.doubleValue());
            String registNo = prpLWfTaskVo.getRegistNo();
            List<FraudScore> reqFraudScores = pushFraud.getFraudScores();
            for(FraudScore fraudScore :reqFraudScores){
            	fraudScore.setReference(flowTaskId.toString());
            }
            //保存评分数据start===========================
            Long fraudScoreId = null;
            
        	fraudScoreId = scoreSaveService.saveScore(reqFraudScores);
            resultCode = "0041";
            resultDesc = "数据保存成功";
            
            FraudScore fraudScore = reqFraudScores.get(0);
            String presetValue = SpringProperties.getProperty("FRAUD_SCORE");
			//调查任务开关控制调整(注意：由于已经停用精励联讯，所以该接口改动无法测试)		
    		String policyNoComCode = policyViewService.getPolicyComCode(registNo);
    		PrpLConfigValueVo configValueVo = ConfigUtil.findConfigByCode(CodeConstants.SURVEY,policyNoComCode);
            if(Double.valueOf(fraudScore.getFraudScore())>Double.valueOf(presetValue)
            		&& CodeConstants.IsSingleAccident.YES.equals(configValueVo.getConfigValue())){//发起调查
            	List<PrpLClaimVo> prpLClaimVoList = claimTaskService.findClaimListByRegistNo(registNo);
                boolean isEndCase = true;
                if(prpLClaimVoList!=null&&prpLClaimVoList.size()>0){// 判断另一个立案是否结案或者注销拒赔
                    for(PrpLClaimVo prpLClaimVo:prpLClaimVoList){
                        if(!(( prpLClaimVo.getEndCaseTime()!=null&&StringUtils.isNotBlank(prpLClaimVo.getEndCaserCode())&&StringUtils
                                .isNotBlank(prpLClaimVo.getCaseNo()) )||( "2".equals(prpLClaimVo.getCancelCode())||"0"
                                .equals(prpLClaimVo.getCancelCode()) ))){
                        	isEndCase = false;
                        }
                    }
                }else{
                	isEndCase = false;
                }
                boolean isExist = wfTaskHandleService.existTaskInBySubNodeCode(registNo,FlowNode.Survey.toString());
        		if(!isExist&&!isEndCase){
        			PrpLSurveyVo surveyVo = new PrpLSurveyVo();
        			surveyVo.setIsAutoTrigger("1");
            		surveyVo.setFlowId(prpLWfTaskVo.getFlowId());
            		surveyVo.setFraudScoreId(fraudScoreId);
            		surveyVo.setRegistNo(prpLWfTaskVo.getRegistNo());
            		surveyVo.setNodeCode(prpLWfTaskVo.getNodeCode());
            		String isMinorCases = "";//小额
            		if("true".equals(fraudScore.getIsSmallAmount())){
            			isMinorCases = "1";
            		}else if ("false".equals(fraudScore.getIsSmallAmount())) {
            			isMinorCases = "0";
					}
            		surveyVo.setIsMinorCases(isMinorCases);
            		String isInjuryCases = "";//人伤
            		if("true".equals(fraudScore.getIsInjured())){
            			isInjuryCases = "1";
            		}else if ("false".equals(fraudScore.getIsInjured())) {
            			isInjuryCases = "0";
					}
            		surveyVo.setIsInjuryCases(isInjuryCases);
            		surveyVo.setReasonDesc(fraudScore.getRuleDesc());
            		SysUserVo sysUserVo = new SysUserVo();
            		sysUserVo.setUserCode("AUTO");
            		sysUserVo.setUserName("AUTO");
                    prpLSurveyService.saveSurvey(surveyVo, sysUserVo, flowTaskId);
        		}
            }
            //保存评分数据end===========================
            AccountInfo accountInfo = requestorReq.getAccountInfo();
            requestorRes.setReference(requestorReq.getReference());
            requestorRes.setTimestamp(requestorReq.getTimestamp());
            resVo.setRequestor(requestorRes);
            if("claim_user".equals(accountInfo.getUserName()) && "claim_psd".equals(accountInfo.getUserPswd())){
                accountNumberStatus.setResultCode("9200");
                accountNumberStatus.setResultDesc("授权通过");
            }else if(!"claim_user".equals(accountInfo.getUserName())){
                accountNumberStatus.setResultCode("9990");
                accountNumberStatus.setResultDesc("用户不存在，请联系管理员");
            }else if("claim_psd".equals(accountInfo.getUserPswd())){
                accountNumberStatus.setResultCode("9991");
                accountNumberStatus.setResultDesc("用户名密码错误，请联系管理员");
            }else{
                accountNumberStatus.setResultCode("9992");
                accountNumberStatus.setResultDesc("用户名无权该操作，请联系管理员");
            }
            
        } catch(XStreamException e){
            resultCode = new String("9990");
            resultDesc = new String("XML解析异常");
            LOGGER.error("XML解析异常", e);
            returnDate(resultCode,resultDesc,responseSummary,accountNumberStatus,responseResult,stream,response,writer,resVo);
            return;
        } catch(HibernateJdbcException e){
            resultCode = "9991";
            resultDesc = "评分插入数据库失败";
            LOGGER.error("评分插入数据库失败", e);
            returnDate(resultCode,resultDesc,responseSummary,accountNumberStatus,responseResult,stream,response,writer,resVo);
            return;
        }catch(Exception e){
            resultCode = "9999";
            resultDesc = "系统内部错误";
            LOGGER.error("系统内部错误", e);
            returnDate(resultCode,resultDesc,responseSummary,accountNumberStatus,responseResult,stream,response,writer,resVo);
            return;
        }
        returnDate(resultCode,resultDesc,responseSummary,accountNumberStatus,responseResult,stream,response,writer,resVo);
	}
	
	/**
	 * zhubin  组织返回数据
	 * @param resultCode
	 * @param resultDesc
	 * @param stream
	 * @param response
	 * @param writer
	 * @param resVo
	 */
	private void returnDate(String resultCode,String resultDesc,ResponseSummary responseSummary,AccountNumberStatus accountNumberStatus, ResponseResult responseResult,XStream stream,HttpServletResponse response,PrintWriter writer,ScoreResVo resVo){
		responseResult.setResultCode(resultCode);
        responseResult.setResultDesc(resultDesc);
        responseSummary.setResponseResult(responseResult);
        responseSummary.setAccountNumberStatus(accountNumberStatus);
        resVo.setResponseSummary(responseSummary);
        stream.processAnnotations(ScoreResVo.class);
        LOGGER.info("评分接口返回报文=========：\n"+stream.toXML(resVo));
        response.setCharacterEncoding("UTF-8");//设置Response的编码方式为UTF-8  
        response.setHeader("Content-type","text/html;charset=UTF-8");//向浏览器发送一个响应头，设置浏览器的解码方式为UTF-8,其实设置了本句，也默认设置了Response的编码方式为UTF-8，但是开发中最好两句结合起来使用  
        //response.setContentType("text/html;charset=UTF-8");同上句代码作用一样  
        writer.write(stream.toXML(resVo));   
	}
}
