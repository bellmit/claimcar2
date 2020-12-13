package ins.sino.claimcar.pinganUnion.service;

import com.alibaba.fastjson.JSON;
import ins.sino.claimcar.claim.service.ClaimTaskExtService;
import ins.sino.claimcar.claim.vo.PrpLClaimVo;
import ins.sino.claimcar.constant.Risk;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.service.WfFlowQueryService;
import ins.sino.claimcar.flow.service.WfTaskHandleService;
import ins.sino.claimcar.pinganUnion.dto.*;
import ins.sino.claimcar.pinganUnion.vo.PingAnDataNoticeVo;
import ins.sino.claimcar.pinganUnion.vo.ResultBean;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description 平安联盟-立案信息查询接口业务数据处理入口
 * @Author liuys
 * @Date 2020/7/21 9:00
 */
@Service("pingAnRegisterHandleService")
public class PingAnRegisterHandleServiceImpl implements PingAnHandleService {
    private static Logger logger = LoggerFactory.getLogger(PingAnRegisterHandleServiceImpl.class);

    @Autowired
    private WfFlowQueryService wfFlowQueryService;
    @Autowired
    private RegistQueryService registQueryService;
    @Autowired
    private PolicyViewService policyViewService;
    @Autowired
    private WfTaskHandleService wfTaskHandleService;
    @Autowired
    private ClaimTaskExtService claimTaskExtService;

    @Override
    public ResultBean pingAnHandle(String registNo, PingAnDataNoticeVo pingAnDataNoticeVo, String respData) {
        logger.info("平安联盟-立案信息查询接口业务数据处理入口--registNo={},respData={}", registNo,respData);

        ResultBean resultBean = ResultBean.success();
        try {
            //解析json字符串
            RegisterRespData registerRespData = JSON.parseObject(respData,RegisterRespData.class);
            //基本校验
            checkData(registNo,registerRespData);

            //自动立案,out不存在立案流程则可以自动立案
            if(CollectionUtils.isEmpty(wfTaskHandleService.findTaskOutVo(registNo,FlowNode.Claim.name()))){
                autoClaim(registNo,registerRespData);
            }else {
                resultBean.success("已立案成成功不要重复立案操作",null);
            }
        }catch (Exception e){
            logger.error("平安联盟-立案信息查询接口业务数据处理报错：registNo={},error={}", registNo, ExceptionUtils.getStackTrace(e));
            resultBean = resultBean.fail(e.getMessage());
        }

        return resultBean;
    }

    /**
     * 自动立案
     * @param registNo
     * @param registerRespData
     */
    private void autoClaim(String registNo, RegisterRespData registerRespData){
        WholeCaseBaseDTO wholeCaseBaseDTO = registerRespData.getWholeCaseBaseDTO();
        //查询报案数据
        PrpLRegistVo registVo = registQueryService.findByRegistNo(registNo);
        String flowId = registVo.getFlowId();

        //判断一下立案节点是否已经处理完成,处理完成不再处理
        List<PrpLCMainVo> cMainVoList = policyViewService.getPolicyAllInfo(registNo);
        if(CollectionUtils.isNotEmpty(cMainVoList)){
            for(PrpLCMainVo cMainVo : cMainVoList){
                FlowNode subNode = FlowNode.ClaimBI;
                if(Risk.DQZ.equals(cMainVo.getRiskCode())){
                    subNode = FlowNode.ClaimCI;
                }
                if(!wfTaskHandleService.existTaskInBySubNodeCode(registNo,subNode.toString())){
                    throw new IllegalArgumentException(subNode.name()+"环节WFTaskIn流程记录不存在");
                }

                //保存立案数据
                PrpLClaimVo prpLClaimVo = claimTaskExtService.autoClaimForPingAnCase(registNo,cMainVo.getPolicyNo(),flowId,wholeCaseBaseDTO.getRegisterDate());
                //立案提交工作流
                wfTaskHandleService.submitClaim(flowId,prpLClaimVo);
            }
        }
    }

    /**
     * 校验数据是否合法
     */
    private void checkData(String registNo,
                           RegisterRespData registerRespData) {
        if (StringUtils.isBlank(registNo)){
            throw new IllegalArgumentException("报案号registNo不存在");
        }
        if (registerRespData == null){
            throw new IllegalArgumentException("解析返回报文内容为空");
        }
        if (registerRespData.getWholeCaseBaseDTO() == null){
            throw new IllegalArgumentException("案件基本信息wholeCaseBaseDTO为空");
        }
        if (StringUtils.isBlank(registerRespData.getWholeCaseBaseDTO().getReportNo())){
            throw new IllegalArgumentException("案件号reportNo为空");
        }
        if (StringUtils.isBlank(registerRespData.getWholeCaseBaseDTO().getIsRegister())){
            throw new IllegalArgumentException("是否立案isRegister为空");
        }
        if (registerRespData.getWholeCaseBaseDTO().getRegisterDate() == null){
            throw new IllegalArgumentException("立案时间registerDate为空");
        }
    }
}
