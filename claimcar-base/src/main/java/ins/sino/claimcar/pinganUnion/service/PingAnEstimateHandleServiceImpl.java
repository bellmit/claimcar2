package ins.sino.claimcar.pinganUnion.service;

import com.alibaba.fastjson.JSON;
import ins.sino.claimcar.claim.service.ClaimTaskExtService;
import ins.sino.claimcar.commom.vo.StringUtils;
import ins.sino.claimcar.pinganUnion.dto.*;
import ins.sino.claimcar.pinganUnion.vo.PingAnDataNoticeVo;
import ins.sino.claimcar.pinganUnion.vo.ResultBean;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description 平安联盟-未决变动信息查询接口业务数据处理入口
 * @Author liuys
 * @Date 2020/7/21 9:06
 */
@Service("pingAnEstimateHandleService")
public class PingAnEstimateHandleServiceImpl implements PingAnHandleService {
    private static Logger logger = LoggerFactory.getLogger(PingAnEstimateHandleServiceImpl.class);

    @Autowired
    private ClaimTaskExtService claimTaskExtService;

    @Override
    public ResultBean pingAnHandle(String registNo, PingAnDataNoticeVo pingAnDataNoticeVo, String respData) {
        logger.info("平安联盟-未决变动信息查询接口业务数据处理入口--registNo={},respData={}", registNo,respData);
        ResultBean resultBean = ResultBean.success();

        try {
            //解析json字符串
            EstimateRespData estimateRespData = JSON.parseObject(respData,EstimateRespData.class);
            //前置校验
            checkData(registNo, estimateRespData);

            //刷新估损金额
            claimTaskExtService.updateClaimFeeForPingAnCase(registNo,estimateRespData);
        }catch (Exception e){
            logger.error("平安联盟-未决变动信息查询接口业务数据处理报错：registNo={},error={}", registNo, ExceptionUtils.getStackTrace(e));
            resultBean = resultBean.fail(e.getMessage());
        }
        return resultBean;
    }

    /**
     * 前置校验
     * @param registNo
     * @param estimateRespData
     */
    private void checkData(String registNo,EstimateRespData estimateRespData) {
        if (StringUtils.isBlank(registNo)){
            throw new IllegalArgumentException("报案号registNo不存在");
        }
        if (estimateRespData == null){
            throw new IllegalArgumentException("解析返回报文内容为空");
        }
    }
}
