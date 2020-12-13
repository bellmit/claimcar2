package ins.sino.claimcar.pinganUnion.service;

import com.alibaba.fastjson.JSON;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.pinganUnion.dto.PreEndCaseRespData;
import ins.sino.claimcar.pinganUnion.vo.PingAnDataNoticeVo;
import ins.sino.claimcar.pinganUnion.vo.ResultBean;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description 平安联盟-保单先结查询接口入口
 * @Author liuys
 * @Date 2020/7/21 9:03
 */
@Service("pingAnPreEndCaseHandleService")
public class PingAnPreEndCaseHandleServiceImpl extends PingAnEndCaseHandleServiceImpl implements PingAnHandleService {
    private static Logger logger = LoggerFactory.getLogger(PingAnPreEndCaseHandleServiceImpl.class);

    @Override
    public ResultBean pingAnHandle(String registNo, PingAnDataNoticeVo pingAnDataNoticeVo, String respData) {
        logger.info("平安联盟-保单先结查询接口业务数据处理入口--registNo={},respData={}", registNo,respData);
        ResultBean resultBean = ResultBean.success();

        try {
            //解析json字符串
            List<PreEndCaseRespData> preEndCaseDTOList = JSON.parseArray(respData, PreEndCaseRespData.class);//保单先结信息
            //基本校验
            checkData(registNo, preEndCaseDTOList);
            //创建默认用户
            SysUserVo userVo = new SysUserVo();
            userVo.setUserCode("AUTO");
            userVo.setUserName("AUTO");

            //根据赔付结论进行业务流程操作
            for (PreEndCaseRespData preEndCaseDTO : preEndCaseDTOList) {
                String policyNo = CodeConstants.PINGAN_UNION.POLICY_PREFIX + preEndCaseDTO.getPolicyNo();
                if ("1".equals(preEndCaseDTO.getIndemnityConclusion())) {//1-赔付
                    writeBackEndCaseDate(registNo, policyNo, preEndCaseDTO.getCaseTimes(),preEndCaseDTO.getEndCaseDate());
                } else if ("2".equals(preEndCaseDTO.getIndemnityConclusion())) {//2-零结
                    zeroAutoEndCase(registNo,policyNo,preEndCaseDTO.getEndCaseDate(),"零结",userVo);
                } else if ("3".equals(preEndCaseDTO.getIndemnityConclusion())) {//3-拒赔
                    writeBackEndCaseDate(registNo, policyNo, preEndCaseDTO.getCaseTimes(),preEndCaseDTO.getEndCaseDate());
                } else if ("4".equals(preEndCaseDTO.getIndemnityConclusion())) {//4-立案注销
                    registerCancel(registNo,policyNo,"立案注销",preEndCaseDTO.getEndCaseDate(),preEndCaseDTO.getEndCaseDate(),userVo);
                } else if ("5".equals(preEndCaseDTO.getIndemnityConclusion())) {//5-报案注销
                    reportCancel(registNo,"报案注销",preEndCaseDTO.getEndCaseDate(),userVo);
                }
            }
        }catch (Exception e){
            logger.error("平安联盟-保单先结查询接口业务数据处理报错：registNo={},error={}", registNo, ExceptionUtils.getStackTrace(e));
            resultBean = resultBean.fail(e.getMessage());
        }

        return resultBean;
    }

    /**
     * 校验数据是否合法
     */
    private void checkData (String registNo, List<PreEndCaseRespData> preEndCaseDTOList) {
        if (StringUtils.isBlank(registNo)){
            throw new IllegalArgumentException("报案号registNo不存在");
        }
        if (CollectionUtils.isEmpty(preEndCaseDTOList)) {
            throw new IllegalArgumentException("保单先结信息为空");
        }
        for (PreEndCaseRespData preEndCaseDTO : preEndCaseDTOList) {
            if (StringUtils.isBlank(preEndCaseDTO.getPolicyNo())) {
                throw new IllegalArgumentException("保单先结信息policyNo为空");
            }
            if (StringUtils.isBlank(preEndCaseDTO.getCaseStatus())) {
                throw new IllegalArgumentException("保单先结信息caseStatus为空");
            }
            if (StringUtils.isBlank(preEndCaseDTO.getIndemnityConclusion())) {
                throw new IllegalArgumentException("保单先结信息indemnityConclusion为空");
            }
        }
    }
}

