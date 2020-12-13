package ins.sino.claimcar.pinganunion.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.google.gson.Gson;
import ins.sino.claimcar.carplatform.util.StringUtil;
import ins.sino.claimcar.pinganUnion.enums.DealStatusEnum;
import ins.sino.claimcar.pinganUnion.enums.PingAnDataTypeEnum;
import ins.sino.claimcar.pinganUnion.service.PingAnDataNoticeService;
import ins.sino.claimcar.pinganUnion.service.PingAnPayCallBackService;
import ins.sino.claimcar.pinganUnion.vo.PingAnDataNoticeVo;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.Path;
import java.util.Date;
import java.util.Map;

/**
 * description: PingAnPayCallBackServiceImpl
 * date: 2020/8/18 11:12
 * author: lk
 * version: 1.0
 */
@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"})
@Path("pingAnPayCallBackService")
public class PingAnPayCallBackServiceImpl implements PingAnPayCallBackService {
    private static Logger logger = LoggerFactory.getLogger(PingAnPayCallBackServiceImpl.class);
    @Autowired
    private PingAnDataNoticeService pingAnDataNoticeService;

    @Autowired
    private RegistQueryService registQueryService;

    @Override
    public void payCallBackDataBuild(Map<String, Object> callBackDataMap) {
        if (null != callBackDataMap && !callBackDataMap.isEmpty()) {
            Gson gson = new Gson();
            for (Map.Entry<String, Object> data : callBackDataMap.entrySet()) {
                Object param = data.getValue();
                if (null != param) {
                    String paramObj = gson.toJson(param);
                    String registNo = data.getKey();
                    if (StringUtil.isNotBlank(paramObj)) {
                        PrpLRegistVo prpLRegistVo = registQueryService.findByRegistNo(registNo);
                        PingAnDataNoticeVo dataNoticeVo = new PingAnDataNoticeVo();
                        if (null != prpLRegistVo) {
                            String paicReportNo = prpLRegistVo.getPaicReportNo();
                            //校验是否有平安的报案号
                            if (StringUtil.isNotBlank(paicReportNo)) {
                                dataNoticeVo.setReportNo(paicReportNo);
                                dataNoticeVo.setParamObj(paramObj);
                                dataNoticeVo.setPushType(PingAnDataTypeEnum.CODE_dh01.getCode());
                                dataNoticeVo.setStatus(DealStatusEnum.CREATE.getCode());
                                dataNoticeVo.setFailTimes(0);
                                dataNoticeVo.setCreateTime(new Date());
                                dataNoticeVo.setUpdateTime(new Date());
                                dataNoticeVo = pingAnDataNoticeService.saveOrUpdateDataNotice(dataNoticeVo);
                                logger.info("保存平安支付结果回调环节数据成功，dataNoticeVo ={}", gson.toJson(dataNoticeVo));
                            }else {
                                logger.error("保存平安支付结果回调环节数据，报案号【" + registNo + "】，平安报案号【" + paicReportNo + "】平安报案号为空，不是平安处理案件");
                            }
                        }else {
                            logger.error("保存平安支付结果回调环节数据，报案号【" + registNo + "】未查询到报案信息");
                        }
                    }else {
                        logger.info("保存平安支付结果回调环节数据，参数为空===================");
                    }
                }
            }
        }
    }
}
