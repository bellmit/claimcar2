package ins.sino.claimcar.subrogation.service.spring;

import ins.sino.claimcar.subrogation.service.PlatLockDubboService;
import ins.sino.claimcar.subrogation.service.PlatLockService;
import ins.sino.claimcar.subrogation.vo.PrpLPlatLockVo;

import java.util.List;

import javax.ws.rs.Path;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;

@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"})

@Path("platLockDubboService")
public class PlatLockDubboServiceImpl implements PlatLockDubboService  {
	
	@Autowired
	private PlatLockService platLockService;

	@Override
	public void savePlatLock(PrpLPlatLockVo platLockVo) {
		platLockService.savePlatLock(platLockVo);
	}

	@Override
	public void firstSavePlatLock(PrpLPlatLockVo platLockVo) {
		platLockService.firstSavePlatLock(platLockVo);
	}

	@Override
	public List<PrpLPlatLockVo> findLockCancelList(String registNo,String recoveryCode) {
		return platLockService.findLockCancelList(registNo,recoveryCode);
	}

	@Override
	public PrpLPlatLockVo findPlatLockVo(String registNo,String recoveryCode) {
		return platLockService.findPlatLockVo(registNo,recoveryCode);
	}

	@Override
	public List<PrpLPlatLockVo> findPlatLockList(String registNo,String recoveryCode) {
		return platLockService.findPlatLockList(registNo,recoveryCode);
	}

	@Override
	public PrpLPlatLockVo findPlatLockByRecoveryCode(String recoveryCode) {
		return platLockService.findPlatLockByRecoveryCode(recoveryCode);
	}

}
