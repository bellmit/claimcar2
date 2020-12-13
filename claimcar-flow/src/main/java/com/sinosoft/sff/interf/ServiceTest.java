package com.sinosoft.sff.interf;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import com.thoughtworks.xstream.io.xml.XppDriver;


public class ServiceTest {

public static void main(String[] args) throws RemoteException {
	
		ClaimReturnTicketProxy claimTransSffProxy=new ClaimReturnTicketProxy();
		AccRecAccountVo accountVo=new AccRecAccountVo();
		List<AccRecAccountVo> accountVosList=new ArrayList<AccRecAccountVo>();
		AccMainVo accMainVo=new AccMainVo();
		accMainVo.setCertiNo("71203002014120100087501");
		accMainVo.setOperateComCode("深圳分公司");
		accMainVo.setModifyCode("1000000135");
		accMainVo.setModifyTime("2016/12/11");
		accMainVo.setPaytype("c");
		accMainVo.setStatus("0");
		accountVo.setAccountCode("5300164321360510012312");
		accountVo.setBankCode("04");
		accountVo.setCurrency("01");
		accountVo.setProvincial("530000");
		accountVo.setCity("530000");
		accountVo.setNameOfBank("肯德基");
		accountVo.setAccountName("肯德基");
		accountVo.setTelephone("15087471897");
		accountVo.setClientType("1");
		accountVo.setClientNo("5120320140206122");
		accountVo.setClientName("王会23");
		accountVo.setAccountType("1");
		accountVo.setDefaultFlag("0");
		accountVo.setCreateCode("1200000211");
		accountVo.setCreateBranch("12030000");
		accountVo.setValidStatus("1");
		accountVo.setFlag("105736500018");
		accountVo.setIdentifyType("01");
		accountVo.setIdentifyNumber("530103195910072523");
		accountVo.setAccType("2");
		accountVo.setActType("101");
		accountVosList.add(accountVo);
		accMainVo.setAccountVo(accountVosList);
		XStream stream = new XStream(new XppDriver(new XmlFriendlyNameCoder("-_","_")));
		stream.autodetectAnnotations(true);
		stream.setMode(XStream.NO_REFERENCES);
		stream.aliasSystemAttribute(null,"class");// ȥ�� class����z
		String xml=null;
		xml=stream.toXML(accMainVo);
		System.out.println(xml);
//		String xString=claimTransSffProxy.transDataForXml(xml);
//		System.out.println(xString);
	}

}
