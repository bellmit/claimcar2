package ins.sino.claimcar.pinganunion.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.google.gson.Gson;
import ins.sino.claimcar.carplatform.util.StringUtil;
import ins.sino.claimcar.pinganUnion.service.PingAnHandleService;
import ins.sino.claimcar.pinganUnion.vo.PingAnDataNoticeVo;
import ins.sino.claimcar.pinganUnion.vo.ResultBean;
import ins.sino.claimcar.pinganunion.vo.payCallback.UnionPayCallbackResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.Path;

/**
 * description: PingAnPayCallbackHandleServiceImpl 平安联盟中心-支付结果回调接口
 * date: 2020/8/18 9:13
 * author: lk
 * version: 1.0
 */
@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"}, group = "pingAnPayCallbackHandleService")
    @Path("pingAnPayCallbackHandleService")
    public class PingAnPayCallbackHandleServiceImpl implements PingAnHandleService {
    private static Logger logger = LoggerFactory.getLogger(PingAnPayCallbackHandleServiceImpl.class);

    /**
     * 接口具体业务处理方法
     *
     * @param respData 平安接口返回数据
     * @return 数据处理结果
     */
    @Override
    public ResultBean pingAnHandle(String registNo, PingAnDataNoticeVo pingAnDataNoticeVo, String respData) {
        //无返回数据，只要返回处理成功就可以
        return ResultBean.success();
    }
}