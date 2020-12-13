package ins.sino.claimcar.job.service;

import ins.sino.claimcar.policyLinkage.service.LinkageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("autoSaveLinkInfoService")
public class AutoSaveLinkInfoService {
	 @Autowired
	LinkageService linkageService;
	public void test() throws Exception{
		//linkageService.testQuartz();
		linkageService.autoSaveLinkInfo();
		System.out.println("哈哈！我一点都不饿^_^--我被调用了-----------------------");
	}
}
