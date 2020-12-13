package ins.sino.claimcar.pinganUnion.servlet;

import ins.sino.claimcar.pinganUnion.service.PingAnApiService;
import ins.sino.claimcar.pinganUnion.service.PingAnDataNoticeService;
import ins.sino.claimcar.pinganUnion.vo.PingAnDataNoticeVo;
import ins.sino.claimcar.pinganUnion.vo.ResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description 描述
 * @Author liuys
 * @Date 2020/7/28 16:25
 */
@RestController
public class PingAnTestController {
    @Autowired
    private PingAnApiService pingAnApiService;
    @Autowired
    private PingAnDataNoticeService pingAnDataNoticeService;

    @RequestMapping("/testHandleService/{id}")
    public ResultBean testHandleService(@PathVariable("id") Long id){
        ResultBean resultBean = ResultBean.success();

        PingAnDataNoticeVo pingAnDataNoticeVo = pingAnDataNoticeService.queryById(id);
        if (pingAnDataNoticeVo == null){
            resultBean = resultBean.fail("数据下发通知记录不存在");
        }else {
            resultBean = pingAnApiService.service(pingAnDataNoticeVo);
        }
        return resultBean;
    }
}
