/******************************************************************************
* CREATETIME : 2015年11月26日 上午9:22:20
******************************************************************************/
package ins.sino.claimcar.regist.web.action;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * 临时保单Action
 * <pre></pre>
 * @author ★ZhangYu
 */
@Controller
@RequestMapping("/tempPolicy")
public class TempPolicyAction {

	private static Logger logger = LoggerFactory.getLogger(TempPolicyAction.class);
	
	/**
	 * 无保单报案编辑临时保单
	 * <pre></pre>
	 * @param model
	 * @return
	 * @modified:
	 * ☆ZhangYu(2015年11月26日 上午9:32:50): <br>
	 */
	@RequestMapping(value = "/edit.do")
	public String edit(Model model) {
		model.addAttribute("reportDate",new Date());
		return "policy/tempPolicy/TempPolicyEdit";
	}
}
