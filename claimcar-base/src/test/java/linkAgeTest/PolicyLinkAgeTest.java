package linkAgeTest;



import ins.sino.claimcar.carinterface.service.ClaimInterfaceLogService;
import ins.sino.claimcar.carinterface.vo.ClaimInterfaceLogVo;
import ins.sino.claimcar.policyLinkage.service.LinkageService;

import java.util.Date;

import org.apache.commons.lang.time.DateUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import base.BaseTestTrueRollback;
//import org.springframework.scheduling.quartz.CronTriggerBean;


public class PolicyLinkAgeTest extends BaseTestTrueRollback{
	@Autowired
	LinkageService linkageService;
	
	@Autowired
	ClaimInterfaceLogService logService;

	//@Test
	public void currentVersionTest() throws Exception {

		linkageService.searchCurrentVersion();

	}
	//@Test
	public void caseInfosTest() throws Exception {
		Date endTime = new Date();
		System.out.println(new Date().getTime());
		endTime = DateUtils.addDays(endTime, -30*2);
		Date startTime = DateUtils.addDays(endTime, -30*4);
		linkageService.searchCaseInfos(startTime.getTime(), endTime.getTime(), null,1);

	}
	
	//@Test
	public void caseDetailsTest() throws Exception{
		
			linkageService.searchCaseDetail("201611250002", null, 1);
		
	}
	@Test
	public void testAutoSave() throws Exception{
		linkageService.autoSaveLinkInfo();
		//linkageService.updateSmallPic();
	}
	
	public void testSave() throws Exception {
		ClaimInterfaceLogVo logVo = new ClaimInterfaceLogVo();
		
		logVo.setRegistNo("0000000000");
		logVo.setBusinessType("00");
		logVo.setCreateUser("000000");
		String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><result><code>0</code><info><data xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"caseAllDetailInfo\"><baseInfo><accidentAddress>湖北省武汉市武昌区中山路278-5号</accidentAddress><accidentDescribe>正常出险</accidentDescribe><accidentTime>2016-11-17 00:00:00</accidentTime><caseNumber>201611090003</caseNumber><caseType>2</caseType><district>420103</district><externaltype>2</externaltype><lat>30.39</lat><lng>110.12</lng><respTime>2016-11-09 12:10:34</respTime><respUserName>王五</respUserName><respUserPhone>18702716094</respUserPhone><startTime>2016-11-09 12:10:34</startTime><status>1</status><surveyMembers>系统管理员</surveyMembers><surveyMembersPhone>root</surveyMembersPhone></baseInfo><caseId>10903</caseId><imgList4><imgId>21356</imgId><orders>1</orders><picUrl>http://wx.chexiaoy.com/hbjb-mng/fileinmix/getimage?time=205561&amp;model=e893b944ed7fec3bfa7ea4dfe18c5ce1&amp;imageId=21356&amp;type=0</picUrl><tags>4</tags><type>0</type></imgList4><imgList4><imgId>21357</imgId><orders>2</orders><picUrl>http://wx.chexiaoy.com/hbjb-mng/fileinmix/getimage?time=205561&amp;model=8d7cebbef951c7044f5801a9d1993607&amp;imageId=21357&amp;type=0</picUrl><tags>4</tags><type>0</type></imgList4><imgList4><imgId>21358</imgId><orders>3</orders><picUrl>http://wx.chexiaoy.com/hbjb-mng/fileinmix/getimage?time=205561&amp;model=02316c9f1681769ed34ae6d5fd2d6c67&amp;imageId=21358&amp;type=0</picUrl><tags>4</tags><type>0</type></imgList4><imgList4><imgId>21359</imgId><orders>4</orders><picUrl>http://wx.chexiaoy.com/hbjb-mng/fileinmix/getimage?time=205561&amp;model=5fe125d131eebdebf010abec903a596e&amp;imageId=21359&amp;type=0</picUrl><tags>4</tags><type>0</type></imgList4><imgList4><imgId>21360</imgId><orders>5</orders><picUrl>http://wx.chexiaoy.com/hbjb-mng/fileinmix/getimage?time=205561&amp;model=df09977d1282be0f08735ddf630d4dd1&amp;imageId=21360&amp;type=0</picUrl><tags>4</tags><type>0</type></imgList4><imgList4><imgId>21361</imgId><orders>6</orders><picUrl>http://wx.chexiaoy.com/hbjb-mng/fileinmix/getimage?time=205561&amp;model=cbc8b5ccc5ac54818c74035b4442438c&amp;imageId=21361&amp;type=0</picUrl><tags>4</tags><type>0</type></imgList4><imgList8><imgId>21362</imgId><orders>7</orders><picUrl>http://wx.chexiaoy.com/hbjb-mng/fileinmix/getimage?time=205561&amp;model=cbdf1c45bf4de52e1889542a04fd3976&amp;imageId=21362&amp;type=0</picUrl><tags>8</tags><type>0</type></imgList8><imgList8><imgId>21363</imgId><orders>8</orders><picUrl>http://wx.chexiaoy.com/hbjb-mng/fileinmix/getimage?time=205561&amp;model=9f4bbd2d0e7086337abf3bbd3a19c7d0&amp;imageId=21363&amp;type=0</picUrl><tags>8</tags><type>0</type></imgList8><injuredInfos><age>35</age><hj>农村</hj><name>何华清</name><orders>1</orders><sex>1</sex><sfzmhm>422103198109020015</sfzmhm></injuredInfos><lossInfo><itemsName>超市货物受损，树被撞断</itemsName></lossInfo><targetCarInfo><driverName>王五</driverName><hphm>鄂AN7EMK</hphm><injureLocation>2,4,6</injureLocation><isResp>1</isResp><jqx>108</jqx><orders>0</orders><phone>18702716094</phone><syx>108</syx><type>2</type></targetCarInfo><thirdCarInfos><driverName>王五</driverName><hphm>鄂AN7EMK</hphm><injureLocation>2,4,6</injureLocation><isResp>1</isResp><jqx>108</jqx><orders>0</orders><phone>18702716094</phone><syx>108</syx><type>2</type></thirdCarInfos></data></info><msg>成功</msg></result>";
		logVo.setResponseXml(xml);
		logService.save(logVo);
	}
	
	//@Test
	@Scheduled(cron = "0 1 * * * ? ")
	public void testTimer() throws Exception {
		System.out.println("aaaa-------我好饿啊！！！！！");

	}
}
