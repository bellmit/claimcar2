package ins.sino.claimcar.pay.web.action;

import ins.framework.common.ResultPage;
import ins.framework.web.bind.annotation.FormModel;
import ins.framework.web.util.ResponseUtils;
import ins.platform.common.web.util.WebUserUtils;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.claim.vo.PrpLClaimVo;
import ins.sino.claimcar.claim.vo.PrpLCompensateVo;
import ins.sino.claimcar.flow.vo.PrpLWfTaskQueryVo;
import ins.sino.claimcar.flow.vo.WfTaskQueryResultVo;
import ins.sino.claimcar.pay.service.PayTaskQueryService;

import java.lang.reflect.InvocationTargetException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 预付、垫付任务发起查询
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/padpay")
public class PayTaskQueryAction {
	
	
	@Autowired 
	private PayTaskQueryService padPayService;
	
	
	/**
	 * 垫付任务发起查询,查询已立案的数据
	 * @param model
	 * @param prpLClaimVo
	 * @param prplwftaskqueryvo
	 * @param start
	 * @param length
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/padPayFinds.do", method = RequestMethod.POST)
	@ResponseBody
	public String padPayFind(Model model,
			@FormModel("claimVo") PrpLClaimVo prpLClaimVo,// 页面组装VO
			@FormModel("wfTaskQueryVo") PrpLWfTaskQueryVo prplwftaskqueryvo,// 页面组装VO
			@RequestParam(value = "start", defaultValue = "0") Integer start,// 分页开始位置
			@RequestParam(value = "length", defaultValue = "10") Integer length)
			throws Exception {
		prplwftaskqueryvo.setComCode(WebUserUtils.getComCode());
		ResultPage<WfTaskQueryResultVo> page = padPayService.find(prpLClaimVo,
				prplwftaskqueryvo, start, length);
		String jsonData = ResponseUtils.toDataTableJson(page, "registNo",
				"policyNo", "mercyFlag:MercyFlag", "insuredName",
				"comCodePly:ComCode", "claimNo");
		return jsonData;

	}
	
	
	/**
	 * 预付任务发起查询,查询已立案的数据
	 * @param model
	 * @param prpLClaimVo
	 * @param prplwftaskqueryvo
	 * @param start
	 * @param length
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/prePayFinds.do", method = RequestMethod.POST)
	@ResponseBody
	public String prePayFind(Model model,
			@FormModel("claimVo") PrpLClaimVo prpLClaimVo,// 页面组装VO
			@FormModel("wfTaskQueryVo") PrpLWfTaskQueryVo prplwftaskqueryvo,// 页面组装VO
			@RequestParam(value = "start", defaultValue = "0") Integer start,// 分页开始位置
			@RequestParam(value = "length", defaultValue = "10") Integer length)
			throws Exception {
		
		prplwftaskqueryvo.setComCode(WebUserUtils.getComCode());
		ResultPage<WfTaskQueryResultVo> page = padPayService.findByPre(
				prpLClaimVo, prplwftaskqueryvo, start, length);
		String jsonData = ResponseUtils.toDataTableJson(page, "registNo",
				"createUser:UserCode", "mercyFlag:MercyFlag", "insuredName",
				"comCodePly:ComCode", "riskCode:RiskCode", "claimNo", "isSubRogation");
		return jsonData;

	}
	
	/**
	 * 预付冲销发起查询
	 * @param model
	 * @param prpLCompensateVo
	 * @param prplwftaskqueryvo
	 * @param start
	 * @param length
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	@RequestMapping(value = "/prePayWriteOffFinds.do", method = RequestMethod.POST)
	@ResponseBody
	public String prePayWriteOffFinds(Model model,
			@FormModel("compensateVo") PrpLCompensateVo prpLCompensateVo,// 页面组装VO
			@FormModel("wfTaskQueryVo") PrpLWfTaskQueryVo prplwftaskqueryvo,// 页面组装VO
			@RequestParam(value = "start", defaultValue = "0") Integer start,// 分页开始位置
			@RequestParam(value = "length", defaultValue = "10") Integer length)
			throws IllegalAccessException, InvocationTargetException,
			NoSuchMethodException {
		SysUserVo userVo = WebUserUtils.getUser();
		prplwftaskqueryvo.setComCode(userVo.getComCode());
		ResultPage<WfTaskQueryResultVo> page = padPayService.findByPreWriteOff(
				prpLCompensateVo, prplwftaskqueryvo, start, length);
		String jsonData = ResponseUtils.toDataTableJson(page, "registNo",
				"createUser:UserCode", "mercyFlag:MercyFlag","sumPay","sumFee",
				"comCodePly:ComCode", "claimNo", "underwriteDate",
				"compensateNo", "riskCode");
		System.out.println(jsonData);
		return jsonData;

	}
}
