package ins.sino.claimcar.pinganUnion.service;

import com.alibaba.dubbo.config.annotation.Service;
import ins.framework.dao.database.DatabaseDao;
import ins.framework.utils.Beans;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.carplatform.constant.RequestType;
import ins.sino.claimcar.carplatform.controller.PlatformController;
import ins.sino.claimcar.carplatform.service.CiClaimPlatformLogService;
import ins.sino.claimcar.carplatform.util.PlatformFactory;
import ins.sino.claimcar.carplatform.util.XmlUtil;
import ins.sino.claimcar.carplatform.vo.CiClaimPlatformLogVo;
import ins.sino.claimcar.claim.service.ClaimService;
import ins.sino.claimcar.claim.vo.PrpLClaimVo;
import ins.sino.claimcar.pinganUnion.po.PlatformDataNotice;
import ins.sino.claimcar.pinganUnion.vo.PlatformDataNoticeVo;
import ins.sino.claimcar.pinganUnion.vo.ResultBean;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.service.PrpLCMainService;
import ins.sino.claimcar.regist.service.RegistService;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.Path;
import java.util.Map;

/**
 * @Description 描述
 * @Author liuys
 * @Date 2020/7/20 14:14
 */
@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"})
@Path("platformDataNoticeService")
public class PlatformDataNoticeServiceImpl implements PlatformDataNoticeService {
    private static Logger logger = LoggerFactory.getLogger(PlatformDataNoticeServiceImpl.class);

    @Autowired
    private DatabaseDao databaseDao;
    @Autowired
    private CiClaimPlatformLogService ciClaimPlatformLogService;
    @Autowired
    private PrpLCMainService prpLCMainService;
    @Autowired
    private PolicyViewService policyViewService;
    @Autowired
    private RegistService registService;
    @Autowired
    private ClaimService claimService;

    /**
     * 创建上传平台报文推送记录
     *
     * @param platformDataNoticeVo
     */
    @Override
    public PlatformDataNoticeVo saveOrUpdatePlatformDataNotice(PlatformDataNoticeVo platformDataNoticeVo) {
        PlatformDataNotice platformDataNotice = null;
        //先创建请求记录
        if (platformDataNoticeVo.getId() == null){
            platformDataNotice = new PlatformDataNotice();
            Beans.copy().from(platformDataNoticeVo).to(platformDataNotice);
        }else {
            platformDataNotice = databaseDao.findByPK(PlatformDataNotice.class,platformDataNoticeVo.getId());
            Beans.copy().from(platformDataNoticeVo).excludeNull().to(platformDataNotice);
        }
        databaseDao.save(PlatformDataNotice.class, platformDataNotice);
        platformDataNoticeVo.setId(platformDataNotice.getId());
        return platformDataNoticeVo;
    }

    @Override
    public ResultBean requestPlatform(PlatformDataNoticeVo platformDataNoticeVo) {
        ResultBean resultBean = ResultBean.success();

        try {
            //获取内部报案号
            PrpLRegistVo prpLRegistVo = registService.findRegistByPaicReportNo(platformDataNoticeVo.getReportNo());
            if (prpLRegistVo == null){
                throw new IllegalArgumentException("该案件号未报案成功");
            }
            String registNo = prpLRegistVo.getRegistNo();
            String policyNo = CodeConstants.PINGAN_UNION.POLICY_PREFIX + platformDataNoticeVo.getPolicyNo();
            String comCode = platformDataNoticeVo.getComCode();
            //查询抄单主表信息
            PrpLCMainVo prpLCMainVo = policyViewService.getPolicyInfo(registNo,policyNo);
            if (prpLCMainVo == null){
                throw new IllegalArgumentException("案件保单信息不存在");
            }

            //将报文转Map对象
            String reqData = platformDataNoticeVo.getReqData();
            if (StringUtils.isBlank(reqData)){
                throw new IllegalArgumentException("上传平台报文reqData不存在");
            }
            logger.info("平安联盟上传平台-reportNo={},报文={}", platformDataNoticeVo.getReportNo(), reqData);

            //获取请求接口类型
            RequestType requestTypeEnum = RequestType.getEnumByCode(platformDataNoticeVo.getTransType());
            PlatformController controller = PlatformFactory.getInstance(comCode, requestTypeEnum);
            //替换内部字段信息
            reqData = replaceData(requestTypeEnum,registNo,platformDataNoticeVo,reqData);
            Map<String, String> reqDataMap = XmlUtil.readXML(reqData);

            //请求平台
            CiClaimPlatformLogVo logVo = controller.callPlatformForPingAn(reqData,reqDataMap);
            //响应结果处理
            switch (requestTypeEnum) {
                case RegistInfoCI:
                    if (!"0000".equals(logVo.getErrorCode()) && !CodeConstants.ErrorCode.CIRegistRepeat.equals(logVo.getErrorCode())) {
                        logger.info("交强报案上传平台失败！");
                    } else {
                        String claimSeqNo = null;
                        if (CodeConstants.ErrorCode.CIRegistRepeat.equals(logVo.getErrorCode())) {
                            String indexValue = "平台理赔编码：";
                            String lastValue = "。";
                            String errMsg = logVo.getErrorMessage();
                            if (StringUtils.isNotBlank(errMsg)) {
                                int firstIndex = errMsg.indexOf(indexValue) + indexValue.length();
                                int lastIndex = errMsg.lastIndexOf(lastValue);
                                claimSeqNo = errMsg.substring(firstIndex, lastIndex);
                                logVo.setClaimSeqNo(claimSeqNo);
                                logVo.setStatus(CodeConstants.JobStatus.SUCCEED);
                                ciClaimPlatformLogService.platformLogUpdate(logVo);
                            }
                        } else {
                            claimSeqNo = logVo.getClaimSeqNo();
                        }
                        // 更新cMainVo的理赔编号
                        prpLCMainVo.setClaimSequenceNo(claimSeqNo);
                        prpLCMainService.updatePrpLCMain(prpLCMainVo);
                    }
                    break;
                case RegistInfoBI:
                    if(StringUtils.isNotBlank(logVo.getErrorCode())&& !"0000".equals(logVo.getErrorCode()) && !CodeConstants.ErrorCode.BIRegistRepeat.equals(logVo.getErrorCode())){
                        logger.info("商业报案上传平台失败！");
                    }else{
                        String claimSeqNo = null;
                        if(CodeConstants.ErrorCode.BIRegistRepeat.equals(logVo.getErrorCode())){
                            String indexValue = "平台理赔编码：";
                            String lastValue = "。";
                            String errMsg = logVo.getErrorMessage();
                            if(StringUtils.isNotBlank(errMsg)){
                                int firstIndex = errMsg.indexOf(indexValue) + indexValue.length();
                                int lastIndex = errMsg.lastIndexOf(lastValue);
                                claimSeqNo = errMsg.substring(firstIndex,lastIndex);
                                logVo.setClaimSeqNo(claimSeqNo);
                                logVo.setStatus(CodeConstants.JobStatus.SUCCEED);
                                ciClaimPlatformLogService.platformLogUpdate(logVo);
                            }
                        } else{
                            claimSeqNo = logVo.getClaimSeqNo();
                        }
                        // 更新cMainVo的理赔编号
                        prpLCMainVo.setClaimSequenceNo(claimSeqNo);
                        prpLCMainService.updatePrpLCMain(prpLCMainVo);
                    }
                    break;
                case RegistInfoCI_SH:
                    //报案时理赔编码已经存在了
                    if(logVo != null && StringUtils.isNotBlank(logVo.getErrorMessage())
                            && "失败(理赔编码在平台已存在)".equals(logVo.getErrorMessage())){
                        logVo.setStatus(CodeConstants.JobStatus.SECEND);
                        ciClaimPlatformLogService.platformLogUpdate(logVo);
                    }
                    // 更新cMainVo的理赔编号
                    if(StringUtils.isNotBlank(logVo.getClaimSeqNo())){
                        prpLCMainVo.setClaimSequenceNo(logVo.getClaimSeqNo());
                        prpLCMainService.updatePrpLCMain(prpLCMainVo);
                    }
                    break;
                case RegistInfoBI_SH:
                    //报案时理赔编码已经存在了
                    if(logVo != null && StringUtils.isNotBlank(logVo.getErrorMessage()) && "失败(理赔编码在平台已存在)".equals(logVo.getErrorMessage())){
                        logVo.setStatus(CodeConstants.JobStatus.SUCCEED);
                        ciClaimPlatformLogService.platformLogUpdate(logVo);
                    }
                    // 更新cMainVo的理赔编号
                    if(StringUtils.isNotBlank(logVo.getClaimSeqNo())){
                        prpLCMainVo.setClaimSequenceNo(logVo.getClaimSeqNo());
                        prpLCMainService.updatePrpLCMain(prpLCMainVo);
                    }
                    break;
            }

            //设置响应结果
            if (logVo != null && CodeConstants.JobStatus.SUCCEED.equals(logVo.getStatus())){
                resultBean = resultBean.success(logVo.getErrorMessage(), logVo.getResponseXml());
            }else {
                resultBean = resultBean.fail(logVo.getErrorMessage(), logVo.getResponseXml());
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("平安联盟上传平台报错-reportNo={},错误信息：{}", platformDataNoticeVo.getReportNo(), ExceptionUtils.getStackTrace(e));
            resultBean = resultBean.fail(e.getMessage());
        }

        return resultBean;
    }

    /**
     * 替换内部字段信息
     *
     * @param requestTypeEnum
     * @param registNo
     * @param platformDataNoticeVo
     * @param reqData
     * @return
     */
    private String replaceData(RequestType requestTypeEnum, String registNo, PlatformDataNoticeVo platformDataNoticeVo, String reqData) {
        //替换案件号、保单号
        String dhPolicyNo = CodeConstants.PINGAN_UNION.POLICY_PREFIX + platformDataNoticeVo.getPolicyNo();
        reqData = reqData.replace(platformDataNoticeVo.getReportNo(), registNo)
                        .replace(platformDataNoticeVo.getPolicyNo(), dhPolicyNo);
        //替换立案号，如果查询为空也要替换为空字符串，防止未立案但是传平台报文已经过来了，导致立案号传的是平安的
        if (requestTypeEnum == RequestType.ClaimInfoCI_SH || requestTypeEnum == RequestType.ClaimInfoBI_SH
        ||requestTypeEnum == RequestType.ClaimCI || requestTypeEnum == RequestType.ClaimBI){
            PrpLCMainVo prpLCMainVo = prpLCMainService.findPrpLCMain(registNo,dhPolicyNo);
            String dhClaimNo = "";
            PrpLClaimVo prpLClaimVo = claimService.findClaimVoByRegistNoAndPolicyNo(registNo,dhPolicyNo);
            if (prpLClaimVo != null){
                dhClaimNo = prpLClaimVo.getClaimNo();
            }
            reqData = reqData.replace(prpLCMainVo.getClaimNo(),dhClaimNo);
        }

        return reqData;
    }

}