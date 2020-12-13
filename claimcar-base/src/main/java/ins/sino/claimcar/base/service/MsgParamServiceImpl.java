package ins.sino.claimcar.base.service;

import ins.framework.dao.database.DatabaseDao;
import ins.framework.utils.Beans;
import ins.sino.claimcar.base.po.SysMsgParam;
import ins.sino.claimcar.other.service.MsgParamService;
import ins.sino.claimcar.other.vo.SysMsgParamVo;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;



@Service(protocol = {"dubbo"}, validation = "true" , registry = {"default"})
@Path(value = "MsgParamSvice")
public class MsgParamServiceImpl implements MsgParamService {
	@Autowired
	DatabaseDao databaseDao;
	private static Logger logger = LoggerFactory
			.getLogger(MsgParamServiceImpl.class);
	/**
	 * 根据主键find SysMsgParamVo
	 * 
	 * @param id
	 * @return ParamVo
	 */
	@Override
	public List<SysMsgParamVo> findSysMsgParamVo() {

		List<SysMsgParamVo> paramVos = new ArrayList<SysMsgParamVo>();
		SysMsgParamVo sysMsgParamVo=new SysMsgParamVo();
		List<SysMsgParam> sysMsgParam = databaseDao.findAll(SysMsgParam.class);
		if(sysMsgParam!=null||sysMsgParam.size()>0){
			for(int i=1;i<=sysMsgParam.size();i++){
				paramVos=Beans.copyDepth().from(sysMsgParam).toList(SysMsgParamVo.class);
			}
		}
			logger.debug("size"+sysMsgParam.size());

		return paramVos;

	}
	


}
