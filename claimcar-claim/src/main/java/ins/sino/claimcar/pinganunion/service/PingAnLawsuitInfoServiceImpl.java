package ins.sino.claimcar.pinganunion.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import ins.framework.utils.Beans;
import ins.platform.vo.PrpLLawSuitVo;
import ins.sino.claimcar.claim.po.PrpLLawSuit;
import ins.sino.claimcar.claim.service.LawSiutService;
import ins.sino.claimcar.pinganUnion.service.PingAnHandleService;
import ins.sino.claimcar.pinganUnion.vo.PingAnDataNoticeVo;
import ins.sino.claimcar.pinganUnion.vo.ResultBean;
import ins.sino.claimcar.pinganunion.vo.lawsuit.LawsuitAgentDTO;
import ins.sino.claimcar.pinganunion.vo.lawsuit.LawsuitBaseInfoDTO;
import ins.sino.claimcar.pinganunion.vo.lawsuit.LawsuitDisputeDTO;
import ins.sino.claimcar.pinganunion.vo.lawsuit.LawsuitObjectDTO;
import ins.sino.claimcar.pinganunion.vo.lawsuit.LawsuitResultDTO;
import ins.sino.claimcar.pinganunion.vo.lawsuit.LawsuitSignDTO;
import ins.sino.claimcar.regist.service.RegistService;
import ins.sino.claimcar.regist.vo.PrpLRegistCarLossVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;

/**
 * 
 * @Description: 平安联盟-诉讼信息查询接口业务数据处理入口
 * @author: zhubin
 * @date: 2020年8月3日 上午9:34:04
 */
@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"},group="pingAnLawsuitInfoService")
@Path("pingAnLawsuitInfoService")
public class PingAnLawsuitInfoServiceImpl implements PingAnHandleService {
    private static Logger logger = LoggerFactory.getLogger(PingAnLawsuitInfoServiceImpl.class);

    @Autowired
	private LawSiutService lawsiutService;   
	
	@Autowired
	private RegistService registService; 

    @Override
    public ResultBean pingAnHandle(String registNo,PingAnDataNoticeVo pingAnDataNoticeVo, String respData) {
        logger.info("平安联盟-诉讼信息查询接口业务数据处理入口--respData={}", respData);

        ResultBean resultBean = ResultBean.success();
        try {
            //解析json字符串
            JSONObject jsonObject = JSON.parseObject(respData);
            String lawsuitBaseInfoString = jsonObject.getString("lawsuitBaseInfoDTO");//诉讼基本信息
            String lawsuitObjectString = jsonObject.getString("lawsuitObjectDTOList");//涉诉对象信息
            String lawsuitResultString = jsonObject.getString("lawsuitResultDTOList");//审判结果信息
            String lawsuitSignString = jsonObject.getString("lawsuitSignDTOList");    //诉讼签收信息
            String lawsuitDisputeString = jsonObject.getString("lawsuitDisputeDTOList");//诉讼争议点信息
            String lawsuitAgentString = jsonObject.getString("lawsuitAgentDTOList");    //诉讼代理信息

            //转换成DTO对象
            LawsuitBaseInfoDTO lawsuitBaseInfoDTO = JSON.parseObject(lawsuitBaseInfoString, LawsuitBaseInfoDTO.class);
            List<LawsuitObjectDTO> lawsuitObjectDTOList = JSON.parseArray(lawsuitObjectString, LawsuitObjectDTO.class);
            List<LawsuitResultDTO> lawsuitResultDTOList = JSON.parseArray(lawsuitResultString, LawsuitResultDTO.class);
            List<LawsuitSignDTO> lawsuitSignDTOList = JSON.parseArray(lawsuitSignString, LawsuitSignDTO.class);
            List<LawsuitDisputeDTO> lawsuitDisputeDTOList = JSON.parseArray(lawsuitDisputeString, LawsuitDisputeDTO.class);
            List<LawsuitAgentDTO> lawsuitAgentDTOList = JSON.parseArray(lawsuitAgentString, LawsuitAgentDTO.class);

            //基本校验
            checkData(registNo,lawsuitBaseInfoDTO, lawsuitObjectDTOList,lawsuitResultDTOList,lawsuitSignDTOList,lawsuitDisputeDTOList,lawsuitAgentDTOList);
            
            //数据保存
            saveLawsuitInfo(registNo,lawsuitBaseInfoDTO, lawsuitObjectDTOList,lawsuitResultDTOList,lawsuitSignDTOList,lawsuitDisputeDTOList,lawsuitAgentDTOList);
            

        }catch (Exception e){
        	logger.error("平安联盟-诉讼信息查询接口业务数据处理报错：registNo={},error={}", registNo, ExceptionUtils.getStackTrace(e));
            resultBean = resultBean.fail(e.getMessage());
        }

        return resultBean;
    }

    private void saveLawsuitInfo(String registNo,LawsuitBaseInfoDTO lawsuitBaseInfoDTO,List<LawsuitObjectDTO> lawsuitObjectDTOList,List<LawsuitResultDTO> lawsuitResultDTOList,
    		List<LawsuitSignDTO> lawsuitSignDTOList,List<LawsuitDisputeDTO> lawsuitDisputeDTOList,List<LawsuitAgentDTO> lawsuitAgentDTOList) {
    	PrpLLawSuitVo prplLawSuitVo = new PrpLLawSuitVo();
    	prplLawSuitVo.setRegistNo(registNo);
    	//诉讼信息必须字段：车牌号
    	String licenseNo = "";
    	PrpLRegistVo registVo = registService.findRegistByRegistNo(registNo);
    	List<PrpLRegistCarLossVo> registCarLossesList = registVo.getPrpLRegistCarLosses();
    	if(registCarLossesList != null && registCarLossesList.size() > 0){
    		for (PrpLRegistCarLossVo prpLRegistCarLossVo : registCarLossesList) {
				if("1".equals(prpLRegistCarLossVo.getLossparty())){
					licenseNo = prpLRegistCarLossVo.getLicenseNo();
				}
			}
    		//
    		if(!"".equals(licenseNo)){
    			prplLawSuitVo.setLicenseNo(licenseNo);  
    			//根据上面参数vo组装数据
            	String lawsuitNo = lawsuitBaseInfoDTO.getLawsuitNo();
            	prplLawSuitVo.setEndTime(lawsuitBaseInfoDTO.getLawEndCaseDate());
            	prplLawSuitVo.setEndUser(lawsuitBaseInfoDTO.getLawEndCaseBy());
            	prplLawSuitVo.setCreateUser("AUTO");
            	prplLawSuitVo.setCreateTime(new Date());
            	for (LawsuitDisputeDTO lawsuitDisputeDTO : lawsuitDisputeDTOList) {
        			if(lawsuitNo.equals(lawsuitDisputeDTO.getLawsuitNo())){
        				prplLawSuitVo.setWinOrLostLawsuit(lawsuitDisputeDTO.getWinOrLoseLawsuit().equals(3)?"0":"1");   //1胜诉、2部分胜诉、3败诉
        			}
        		}
            	
            	for (LawsuitResultDTO lawsuitResultDTO : lawsuitResultDTOList) {
        			if(lawsuitNo.equals(lawsuitResultDTO.getLawsuitNo())){
        				if(StringUtils.isNotEmpty(lawsuitResultDTO.getTrialResult())){
        					String trialResult = lawsuitResultDTO.getTrialResult();
        					if("1".equals(trialResult) || "3".equals(trialResult)){
        						prplLawSuitVo.setLitigationResult("1");
        					}else if("2".equals(trialResult)){
        						prplLawSuitVo.setLitigationResult("0");
        					}else{
        						prplLawSuitVo.setLitigationResult("2");
        					}
        				}else{
        					prplLawSuitVo.setLitigationResult("0");
        				}
        				
        				if(StringUtils.isNotEmpty(lawsuitResultDTO.getLawsuitStage())){
        					String stage = lawsuitResultDTO.getLawsuitStage();
        					if("1".equals(stage)){
        						prplLawSuitVo.setTtriallevel("0");
        					}else{
        						prplLawSuitVo.setTtriallevel("1");
        					}
        				}else{
        					prplLawSuitVo.setTtriallevel("0");
        				}
        				
        				prplLawSuitVo.setAttorneyfee(StringUtils.isNotEmpty(lawsuitResultDTO.getLawyerFee())?new BigDecimal(lawsuitResultDTO.getLawyerFee()):new BigDecimal(0));
        				prplLawSuitVo.setEstAmount(StringUtils.isNotEmpty(lawsuitResultDTO.getTotalCosts())?new BigDecimal(lawsuitResultDTO.getTotalCosts()):new BigDecimal(0));
        				if(StringUtils.isNotEmpty(lawsuitResultDTO.getAppellor())){
        					if("2".equals(lawsuitResultDTO.getAppellor())){
        						prplLawSuitVo.setLawsuitType("0");
        					}else{
        						prplLawSuitVo.setLawsuitType("2");
        					}
        				}else{
        					prplLawSuitVo.setLawsuitType("0");    //为空默认 原告
        				}
        			}
        		}
            	
            	for (LawsuitSignDTO lawsuitSignDTO : lawsuitSignDTOList) {
        			if(lawsuitNo.equals(lawsuitSignDTO.getLawsuitNo())){
        				prplLawSuitVo.setPlainTiff(lawsuitSignDTO.getPlaintiff());
        				prplLawSuitVo.setAccused(lawsuitSignDTO.getDefendant());
        				prplLawSuitVo.setSubpoenaTime(lawsuitSignDTO.getSignDate());
        				prplLawSuitVo.setCiName(lawsuitSignDTO.getCourtName());
        				prplLawSuitVo.setCourtNo(lawsuitSignDTO.getCourtCaseNo());
        			}
        		}
            	
            	for (LawsuitAgentDTO lawsuitAgentDTO : lawsuitAgentDTOList) {
        			if(lawsuitNo.equals(lawsuitAgentDTO.getLawsuitNo())){
        				prplLawSuitVo.setFirmname(lawsuitAgentDTO.getPracticeName());    //该字段可能在页面展示不出来，对应页面为下拉框，匹配不上
        				prplLawSuitVo.setLawyers(lawsuitAgentDTO.getLawyerName());
        			}
        		}
            	
            	//将判决/调解金额 赋值为：根据审判级别将  一审判决/调解金额（人伤）+ 一审判决/调解金额（车物损）
        		if("0".equals(prplLawSuitVo.getTtriallevel())){
        			String firstTrialPersonAmount1 = lawsuitBaseInfoDTO.getFirstTrialPersonAmount();
        			BigDecimal bgPerson1 = null;
        			if(StringUtils.isNotEmpty(firstTrialPersonAmount1)){
        				bgPerson1 = new BigDecimal(firstTrialPersonAmount1);
        			}else{
        				bgPerson1 = new BigDecimal(0);
        			}
        			
        			String firstTrialCarAmount1 = lawsuitBaseInfoDTO.getFirstTrialCarAmount();
        			BigDecimal bgCar1 = null;
        			if(StringUtils.isNotEmpty(firstTrialCarAmount1)){
        				bgCar1 = new BigDecimal(firstTrialCarAmount1);
        			}else{
        				bgCar1 = new BigDecimal(0);
        			}
        			prplLawSuitVo.setJudgeAmount(bgPerson1.add(bgCar1));
        		}else{
        			String firstTrialPersonAmount2 = lawsuitBaseInfoDTO.getSecondTrialPersonAmount();
        			BigDecimal bgPerson2 = null;
        			if(StringUtils.isNotEmpty(firstTrialPersonAmount2)){
        				bgPerson2 = new BigDecimal(firstTrialPersonAmount2);
        			}else{
        				bgPerson2 = new BigDecimal(0);
        			}
        			
        			String firstTrialCarAmount2 = lawsuitBaseInfoDTO.getSecondTrialCarAmount();
        			BigDecimal bgCar2 = null;
        			if(StringUtils.isNotEmpty(firstTrialCarAmount2)){
        				bgCar2 = new BigDecimal(firstTrialCarAmount2);
        			}else{
        				bgCar2 = new BigDecimal(0);
        			}
        			prplLawSuitVo.setJudgeAmount(bgPerson2.add(bgCar2));
        		}
            	
            	prplLawSuitVo.setLawsuitWay("0");   //诉讼案件类型
            	prplLawSuitVo.setAmount(new BigDecimal(0));   //诉讼金额
            	//处理方式      如果有律所，律师，就默认普通代理，没有默认自办
            	if(StringUtils.isNotEmpty(prplLawSuitVo.getFirmname())){
            		prplLawSuitVo.setHandleType("2");
            	}else{
            		prplLawSuitVo.setHandleType("0");
            	}
            	prplLawSuitVo.setEvaluation("0");
            	
            	prplLawSuitVo.setLegalduty("0");   //法律岗处理结果 默认0
            	
            	prplLawSuitVo.setToCourtPerson("");
            	prplLawSuitVo.setIsToCourt("0");    //是否出庭应诉  默认是
            	prplLawSuitVo.setUnCourtReason("");
            	
            	prplLawSuitVo.setContentes("");
            	prplLawSuitVo.setEndReport("");
            	
            	try{
            		prplLawSuitVo.setNodeCode("check");
        			lawsiutService.pinganSave(prplLawSuitVo);
        		}catch(Exception e){
        			throw new IllegalArgumentException("平安联盟-诉讼信息查询PingAnLawsuitInfoServiceImpl数据保存失败！");
        		}
    		}else{
    			throw new IllegalArgumentException("平安联盟-诉讼信息查询PingAnLawsuitInfoServiceImpl数据保存失败,没有对应标的车辆信息-车牌号码！");
    		}
    	}else{
    		throw new IllegalArgumentException("平安联盟-诉讼信息查询PingAnLawsuitInfoServiceImpl数据保存失败,没有对应标的车辆信息");
    	}
	}

	/**
     * 校验数据是否合法
     */
    private void checkData(String registNo,LawsuitBaseInfoDTO lawsuitBaseInfoDTO,List<LawsuitObjectDTO> lawsuitObjectDTOList,List<LawsuitResultDTO> lawsuitResultDTOList,
    		List<LawsuitSignDTO> lawsuitSignDTOList,List<LawsuitDisputeDTO> lawsuitDisputeDTOList,List<LawsuitAgentDTO> lawsuitAgentDTOList) {
        if (lawsuitBaseInfoDTO == null){
            throw new IllegalArgumentException("诉讼基本信息lawsuitBaseInfoDTO为空");
        }
        if (CollectionUtils.isEmpty(lawsuitObjectDTOList)){
            throw new IllegalArgumentException("涉诉对象信息lawsuitObjectDTOList为空");
        }
        if (CollectionUtils.isEmpty(lawsuitResultDTOList)){
            throw new IllegalArgumentException("审判结果信息lawsuitResultDTOList为空");
        }
        if (CollectionUtils.isEmpty(lawsuitSignDTOList)){
            throw new IllegalArgumentException("诉讼签收信息lawsuitSignDTOList为空");
        }
        if (CollectionUtils.isEmpty(lawsuitDisputeDTOList)){
            throw new IllegalArgumentException("诉讼争议点信息lawsuitDisputeDTOList为空");
        }
        if (CollectionUtils.isEmpty(lawsuitAgentDTOList)){
            throw new IllegalArgumentException("诉讼代理信息lawsuitAgentDTOList为空");
        }
        
        if (StringUtils.isBlank(registNo)){
            throw new IllegalArgumentException("报案号reportNo为空");
        }
        
    }
    

}
