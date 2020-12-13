package ins.sino.claimcar.subrogationsh.platform.service;

import java.util.ArrayList;
import java.util.List;

import ins.framework.utils.Beans;
import ins.sino.claimcar.carplatform.constant.RequestType;
import ins.sino.claimcar.carplatform.controller.PlatformController;
import ins.sino.claimcar.carplatform.util.PlatformFactory;
import ins.sino.claimcar.carplatform.vo.BiResponseHeadVo;
import ins.sino.claimcar.carplatform.vo.CiClaimPlatformLogVo;
import ins.sino.claimcar.subrogation.sh.vo.CopyInformationResultVo;
import ins.sino.claimcar.subrogation.sh.vo.CopyInformationSubrogationViewVo;
import ins.sino.claimcar.subrogation.sh.vo.SubrogationSHQueryVo;
import ins.sino.claimcar.subrogationsh.platform.vo.CopyInformationResBodyVo;
import ins.sino.claimcar.subrogationsh.platform.vo.CopyInformationSubrogationListVo;
import ins.sino.claimcar.subrogationsh.platform.vo.SubrogationSHCopyInformationBasePartVo;
import ins.sino.claimcar.subrogationsh.platform.vo.SubrogationSHCopyInformationBodyVo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 上海代位送平台服务类 
 * @author ★Luwei
 */
@Service("subrogationSHToPlatformService")
public class SubrogationSHToPlatformService {

	private Logger logger = LoggerFactory.getLogger(SubrogationSHToPlatformService.class);
	
	/**
	 * 上海商业发送代位求偿信息抄回
	 * @param registNo - 报案号
	 * @param claimSeqNo - 理赔编码
	 * @modified:
	 * ☆Luwei(2017年3月7日 上午10:18:11): <br>
	 */
	public List<CopyInformationResultVo> sendCopyInformationToSubrogationSH(SubrogationSHQueryVo queryVo) throws Exception{
		//创建平台实例
		PlatformController controller = PlatformFactory.getInstance
				(queryVo.getComCode(),RequestType.SubrogationClaim_SH);
		
		//组装bodyVo
		SubrogationSHCopyInformationBodyVo bodyVo = new SubrogationSHCopyInformationBodyVo();
		
		SubrogationSHCopyInformationBasePartVo basePartVo = new SubrogationSHCopyInformationBasePartVo();
		basePartVo.setReportNo(queryVo.getRegistNo());
		basePartVo.setClaimCode(queryVo.getClaimSeqNo());
		
		bodyVo.setBasePartVo(basePartVo);
		
		//发送平台
		CiClaimPlatformLogVo logVo = controller.callPlatform(bodyVo);
		logger.info("=====上海商业发送代位求偿信息抄回==返回状态=="+logVo.getErrorMessage());
		
		List<CopyInformationResultVo> resultVo = new ArrayList<CopyInformationResultVo>();
		
		//请求平台返回头部
		BiResponseHeadVo resHeadVo = new BiResponseHeadVo();
//		CopyInformationResBodyVo resBodyVo = new CopyInformationResBodyVo();
		try{
			resHeadVo = controller.getHeadVo(BiResponseHeadVo.class);
			if ("1".equals(resHeadVo.getResponseCode())) {
				CopyInformationResBodyVo resBodyVo = controller.getBodyVo(CopyInformationResBodyVo.class);
				List<CopyInformationSubrogationListVo> listVo = resBodyVo.getSubrogationList();
				if (listVo != null && !listVo.isEmpty()) {
					for (CopyInformationSubrogationListVo vo : listVo) {
						CopyInformationResultVo result = new CopyInformationResultVo();
						result.setResponseCode(resHeadVo.getResponseCode());
						result.setErrorMessage(resHeadVo.getErrorDesc());
						CopyInformationSubrogationViewVo viewVo = Beans.copyDepth().from(vo).to(CopyInformationSubrogationViewVo.class);
						result.setSubrogationViewVo(viewVo);
						resultVo.add(result);
					}
				} else {
					setResultVo(resultVo,resHeadVo);
				}
			} else {
				setResultVo(resultVo,resHeadVo);
			}
		}catch(Exception e){
			// TODO: handle exception
			logger.info("======返回头部信息异常===="+e.getMessage());
			resHeadVo.setErrorDesc(logVo.getErrorMessage());
			setResultVo(resultVo,resHeadVo);
			throw new IllegalArgumentException("==平台返回异常信息：=="+logVo.getErrorMessage());
		}
		
		return resultVo;
	}
	
	//设置错误的返回信息
	private void setResultVo(List<CopyInformationResultVo> resultVo,BiResponseHeadVo resHeadVo) {
		CopyInformationResultVo result = new CopyInformationResultVo();
		result.setResponseCode(resHeadVo.getResponseCode());
		result.setErrorMessage(resHeadVo.getErrorDesc());
		resultVo.add(result);
	}
	
	
}
