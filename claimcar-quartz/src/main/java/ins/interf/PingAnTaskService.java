package ins.interf;

import ins.framework.lang.Springs;
import ins.sino.claimcar.pinganUnion.service.PingAnDataNoticeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.jws.WebService;

/**
 * description: PingAnTaskService 平安对接 自动任务
 * date: 2020/8/3 10:45
 * author: lk
 * version: 1.0
 */
@WebService(targetNamespace = "http://interf/pingAnTaskService/", serviceName = "pingAnTaskService")
public class PingAnTaskService extends SpringBeanAutowiringSupport {

    @Autowired
    PingAnDataNoticeService pingAnDataNoticeService;

    private Logger logger = LoggerFactory.getLogger(PingAnTaskService.class);

    /**
     * description: doBusinessProcess 平安下发数据通知后，自动根据调用对应接口
     * version: 1.0
     * date: 2020/8/3 11:22
     * author: lk
     *
     * @param
     * @return void
     */
    public void doBusinessProcess() {
        logger.info("PingAnTaskService methodName : doBusinessProcess 平安数据开始处理！");
        init();
        pingAnDataNoticeService.untreatedDataNoticesHandle();
        logger.info("PingAnTaskService methodName : doBusinessProcess 平安数据处理完成！");
    }

    private void init() {
        if (pingAnDataNoticeService == null) {
            pingAnDataNoticeService = Springs.getBean(PingAnDataNoticeService.class);
        }
    }


}
