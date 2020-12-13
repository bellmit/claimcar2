package ins.sino.claimcar.manager.service;


import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;

import ins.platform.common.service.facade.BaseDaoService;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.flow.service.DigtalMapService;

@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"})

@Path("DigtalMapService")
public class DigtalMapServiceImpl implements DigtalMapService{
	@Autowired
	private BaseDaoService baseDaoService;
	
	private Logger logger = LoggerFactory.getLogger(DigtalMapServiceImpl.class);

	
	@Override
	public void operateDigtalMap(String userCode,int isOpen) throws Exception {
		//修改电子地图开关
		String sql = "update PrpLConfigValue set configValue =" + isOpen + ", lastModifyCode = " + userCode + ", lastModifyTime =sysdate"
		+ " where configCode='" +  CodeConstants.SWITCHMAP + "'";
		logger.info("修改电子地图的sql:{}",sql);
		 baseDaoService.executeSQL(sql);
	}
		
}
