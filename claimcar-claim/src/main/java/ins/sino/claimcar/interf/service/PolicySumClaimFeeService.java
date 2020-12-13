package ins.sino.claimcar.interf.service;

import ins.framework.lang.Springs;
import ins.platform.utils.DataUtils;
import ins.sino.claimcar.claim.service.ClaimService;
import ins.sino.claimcar.claim.vo.PrpLClaimVo;
import ins.sino.claimcar.interf.vo.BasePartVo;
import ins.sino.claimcar.interf.vo.ClaimFeeVo;
import ins.sino.claimcar.interf.vo.PolicySumClaimRequestVo;
import ins.sino.claimcar.interf.vo.PolicySumClaimReturnVo;

import java.math.BigDecimal;
import java.util.List;

import javax.jws.WebService;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.alibaba.dubbo.common.utils.Assert;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import com.thoughtworks.xstream.io.xml.XppDriver;

@WebService(serviceName="policySumClaim")
public class PolicySumClaimFeeService extends SpringBeanAutowiringSupport{

	private ClaimService claimService;
	
	private static Logger logger = LoggerFactory.getLogger(PolicySumClaimFeeService.class);
	
	public String policySumClaimFee(String xml) {
		this.init();
		logger.info("保单未决请求报文： "+xml);
		PolicySumClaimReturnVo returnVo = new PolicySumClaimReturnVo();
		returnVo.setErrorMessage("成功");
		returnVo.setResponseCode(true);
		XStream stream = new XStream(new XppDriver(new XmlFriendlyNameCoder("-_","_")));
		stream.autodetectAnnotations(true);
		stream.setMode(XStream.NO_REFERENCES);
		stream.aliasSystemAttribute(null,"class");// 去掉 class属性
		try{
			stream.processAnnotations(PolicySumClaimRequestVo.class);
			PolicySumClaimRequestVo basePartVo = (PolicySumClaimRequestVo)stream.fromXML(xml);
			this.findPolcyNoSumClaim(basePartVo,returnVo);
		}
		catch(Exception e){
			returnVo.setResponseCode(false);
			returnVo.setErrorMessage(e.getMessage().toString());
			logger.error("保单未决报文异常", e);
		}
		stream.processAnnotations(PolicySumClaimReturnVo.class);
		return stream.toXML(returnVo);
	}
	
	private void findPolcyNoSumClaim(PolicySumClaimRequestVo basePartVo,PolicySumClaimReturnVo returnVo) throws Exception{
		String policyNo = basePartVo.getPolicyNo();
		Assert.notNull(adjustContent(policyNo),"保单号不能为空");
		
		ClaimFeeVo claimFeeVo = new ClaimFeeVo();
		
		BigDecimal sumClaimFee = BigDecimal.ZERO;
		BigDecimal sumRescueFee = BigDecimal.ZERO;
		List<PrpLClaimVo> claimVoList = claimService.findSumClaimByPolicyNo(policyNo);
		if(claimVoList!=null && !claimVoList.isEmpty()){
			for(PrpLClaimVo claimVo : claimVoList){
				sumClaimFee = sumClaimFee.add(DataUtils.NullToZero(claimVo.getSumClaim()));
				sumRescueFee = sumRescueFee.add(DataUtils.NullToZero(claimVo.getSumRescueFee()));
			}
		}
		
		claimFeeVo.setPolicyNo(policyNo);
		claimFeeVo.setSumClaim(sumClaimFee.toString());
		claimFeeVo.setSumRescueFee(sumRescueFee.toString());
		
		returnVo.setClaimFeeVo(claimFeeVo);
	}
	
	private void init() {
		if(claimService==null){
			claimService = (ClaimService)Springs.getBean(ClaimService.class);
		}
	}
	
	private String adjustContent(String content){
		String reContent = null;
		if(StringUtils.isNotBlank(content)){
			reContent = content;
		}
		return reContent;
	}
	
}
