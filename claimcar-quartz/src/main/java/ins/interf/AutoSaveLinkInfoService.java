/******************************************************************************
 * CREATETIME : 2016年8月9日 下午4:41:09
 ******************************************************************************/
package ins.interf;


import ins.framework.lang.Springs;
import ins.sino.claimcar.policyLinkage.service.LinkageService;

import javax.jws.WebService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

/**
 * 警保每日整点
 * <pre></pre>
 * @author ★zhujunde
 */

@WebService(targetNamespace="http://interf/autoSaveLinkInfoService/",serviceName="autoSaveLinkInfoService")
public class AutoSaveLinkInfoService extends SpringBeanAutowiringSupport {
        private static Logger logger = LoggerFactory.getLogger(ClaimAvgFloatService.class);
        @Autowired
        LinkageService linkageService;
        public void autoSaveLinkInfo(){
            if(linkageService == null){
                linkageService = (LinkageService)Springs.getBean(LinkageService.class);
            }
            logger.info("警报开始========================================");
            if(linkageService == null){
                logger.info("linkageServicenull警报开始========================================");
            }
            linkageService.autoSaveLinkInfo(); 
        }
    }
