package test;

import java.util.ArrayList;
import java.util.List;

import ins.sino.claimcar.interf.vo.PayReturnVo;
import ins.sino.claimcar.invoice.vo.BasePartReceiptTask;
import ins.sino.claimcar.invoice.vo.BodyReceiptTask;
import ins.sino.claimcar.invoice.vo.HeadRemote;
import ins.sino.claimcar.invoice.vo.ReceiptTaskDto;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import com.thoughtworks.xstream.io.xml.XppDriver;

import base.BaseTestTrueRollback;

public class ReceiptTaskServiceWriteBackTest extends BaseTestTrueRollback{
	@Autowired
     ins.sino.claimcar.invoice.services.ReceiptTaskService ReceiptTaskService;
	@Test
	public void test() throws Exception{
		ReceiptTaskDto rtd = new ReceiptTaskDto();
		BodyReceiptTask body = new BodyReceiptTask();
		List<BasePartReceiptTask> baseParts = new ArrayList<BasePartReceiptTask>();
		BasePartReceiptTask bprt = new BasePartReceiptTask();
		bprt.setBusinessNo("71307002016120200000002");
		bprt.setSerialNo(1);
		bprt.setInvoicetype("004");
		bprt.setSumAmountNT(5);
		bprt.setSumAmountTax(5);
		bprt.setTaxRate(5);
		baseParts.add(bprt);
		body.setBaseParts(baseParts);
		HeadRemote  head = new HeadRemote();
		head.setRequestType("compensate");
		rtd.setBody(body);
		rtd.setHead(head);
		// vo  转 xml
		XStream stream = new XStream(new XppDriver(new XmlFriendlyNameCoder("-_","_")));
		stream.autodetectAnnotations(true);
		stream.setMode(XStream.NO_REFERENCES);
		stream.aliasSystemAttribute(null,"class");// 去掉 class属性
//		stream.processAnnotations(ReceiptTaskDto.class);
//		ReceiptTaskDto basePartVo = (ReceiptTaskDto)stream.fromXML(xml);
		stream.processAnnotations(ReceiptTaskDto.class);
		System.out.println("ttttt       "+stream.toXML(rtd));
		ReceiptTaskService.service( stream.toXML(rtd));
	}
}
