package ins.sino.claimcar.interf.service;

import ins.framework.lang.Springs;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.claim.service.CompensateTaskService;
import ins.sino.claimcar.interf.vo.ItemVo;
import ins.sino.claimcar.interf.vo.PayReturnVo;
import ins.sino.claimcar.interf.vo.SettleBasePartVo;
import ins.sino.claimcar.other.service.AccountInfoService;
import ins.sino.claimcar.other.service.AcheckService;
import ins.sino.claimcar.other.service.AssessorService;
import ins.sino.claimcar.pay.service.PadPayPubService;

import javax.jws.WebService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.alibaba.dubbo.common.utils.Assert;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import com.thoughtworks.xstream.io.xml.XppDriver;

@WebService(serviceName="updateSettleNo")
public class UpdateSettleNoService  extends SpringBeanAutowiringSupport{

	private AccountInfoService accountInfoService;
	private PadPayPubService padPayPubService;
	private CompensateTaskService compensateTaskService;
	private AssessorService assessorService;
	private AcheckService acheckService;
	
	private static Logger logger = LoggerFactory.getLogger(UpdateSettleNoService.class);
	
	public String updateSettleNoForXml(String xml){
		this.init();
		logger.info("更新结算单号报文： "+xml);
		System.out.println("================================更新结算单号报文"+xml);
		SettleBasePartVo settleBasePartVo = new SettleBasePartVo();
		PayReturnVo payReturnVo = new PayReturnVo();
		payReturnVo.setErrorMessage("成功");
		payReturnVo.setResponseCode(true);
		XStream stream = new XStream(new XppDriver(new XmlFriendlyNameCoder("-_","_")));
		stream.autodetectAnnotations(true);
		stream.setMode(XStream.NO_REFERENCES);
		stream.aliasSystemAttribute(null,"class");// 去掉 class属性
		try{
			stream.processAnnotations(SettleBasePartVo.class);
			settleBasePartVo = (SettleBasePartVo)stream.fromXML(xml);
			this.updateSettleNo(settleBasePartVo);
		}catch(Exception e){
			payReturnVo.setResponseCode(false);
			payReturnVo.setErrorMessage(e.getMessage());
			logger.error("更新结算单号异常", e);
		}
		stream.processAnnotations(PayReturnVo.class);
		return stream.toXML(payReturnVo);
	}
	
	public void updateSettleNo(SettleBasePartVo settleBasePartVo){
		Assert.notNull(adjustContent(settleBasePartVo.getSettleNo()),"结算单号为空！");
		Assert.notNull(adjustContent(settleBasePartVo.getAccountcode()),"账号为空！");
		Assert.notNull(adjustContent(settleBasePartVo.getPayRefReason()),"收付原因为空！");
		Assert.notNull(adjustContent(settleBasePartVo.getOperateType()),"操作类型为空！");
		
		if(settleBasePartVo.getItemVoList()!=null && settleBasePartVo.getItemVoList().size()>0){
			for(ItemVo itemVo:settleBasePartVo.getItemVoList()){
				Assert.notNull(adjustContent(itemVo.getCertiNo()),"计算书号为空！");
				
				if("P67".equals(settleBasePartVo.getPayRefReason())){//公估费
					assessorService.updateSettleNo(itemVo.getCertiNo(), settleBasePartVo.getSettleNo(), settleBasePartVo.getOperateType());
				} else if(CodeConstants.PayReason.CHECKFEE_PAY_Res.equals(settleBasePartVo.getPayRefReason())){//查勘费
					acheckService.updateSettleNo(itemVo.getCertiNo(),settleBasePartVo.getSettleNo(), settleBasePartVo.getOperateType());
				}
				else if(itemVo.getCertiNo().startsWith("D")){//垫付
					padPayPubService.saveOrUpdateSettleNo(settleBasePartVo.getSettleNo(), settleBasePartVo.getAccountcode(), 
							settleBasePartVo.getOperateType(), itemVo.getCertiNo(), itemVo.getSerialNo().toString());
				}else{
					compensateTaskService.saveOrUpdateSettleNo(settleBasePartVo.getSettleNo(), settleBasePartVo.getAccountcode(), 
							settleBasePartVo.getOperateType(), itemVo.getCertiNo(), itemVo.getSerialNo().toString(), settleBasePartVo.getPayRefReason());
				}
			}
		}
	}
	
	private String adjustContent(String content){
		String reContent = null;
		if(content!=null&&!"".equals(content)&&!content.isEmpty()){
			reContent = content;
		}
		return reContent;
	}
	
	private void init(){
		if(accountInfoService==null){
			accountInfoService=(AccountInfoService)Springs.getBean(AccountInfoService.class);
		}
		if(padPayPubService==null){
			padPayPubService=(PadPayPubService)Springs.getBean(PadPayPubService.class);
		}
		if(compensateTaskService==null){
			compensateTaskService=(CompensateTaskService)Springs.getBean(CompensateTaskService.class);
		}
		if(assessorService==null){
			assessorService=(AssessorService)Springs.getBean(AssessorService.class);
		}
		if(acheckService==null){
			acheckService=(AcheckService)Springs.getBean(AcheckService.class);
		}
	}
}
