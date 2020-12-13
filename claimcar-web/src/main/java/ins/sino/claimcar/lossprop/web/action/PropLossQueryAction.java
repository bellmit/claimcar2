/******************************************************************************
* CREATETIME : 2016年2月3日 上午10:18:22
******************************************************************************/
package ins.sino.claimcar.lossprop.web.action;

import ins.framework.common.ResultPage;
import ins.framework.web.support.ObjectToMapCallback;
import ins.framework.web.util.ResponseUtils;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.CodeConstants.YN01;
import ins.sino.claimcar.losscar.service.LossCarService;
import ins.sino.claimcar.losscar.vo.CarQueryVo;
import ins.sino.claimcar.lossprop.service.PropLossQueryService;
import ins.sino.claimcar.lossprop.vo.PropQueryReslutVo;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * 财产定损查询Action，查询可追加定损，可修改定损的任务
 * @author ★LiuPing
 * @CreateTime 2016年2月3日
 */
@Controller
@RequestMapping(value = "/propQuery")
public class PropLossQueryAction {

	private static Logger logger = LoggerFactory.getLogger(PropLossQueryAction.class);

	@Autowired
	private PropLossQueryService propLossQueryService;
	@Autowired
	private LossCarService lossCarService;
	
	/**
	 * 查询可以修改定损的任务
	 * @return
	 * @modified: ☆LiuPing(2016年2月3日 ): <br>
	 */
	@RequestMapping(value = "/forModifyList")
	public ModelAndView forModifyList() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("propQuery/ForModifyList");
		return mv;
	}
	
	/**
	 * 查询可以发起追加定损的任务
	 * @CreateTime 2016年2月17日 下午5:42:02
	 * @author lichen
	 */
	@RequestMapping(value = "/forAddList")
	public ModelAndView forAddList() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("propQuery/ForAddList");
		return mv;
	}
	
	
	@RequestMapping(value = "/forModifySeach")
	@ResponseBody
	public String forModifySeach(CarQueryVo propQueryVo,SysUserVo loginUser) throws Exception {
		propQueryVo.setComCode(loginUser.getComCode());
		ResultPage<PropQueryReslutVo> resultPage = lossCarService.findPropPageForAdjust(propQueryVo);
		String jsonData = ResponseUtils.toDataTableJson(resultPage,searchCallBack(),"mercyFlag","registNo","policyNo","lossType","lossTypeName",
				"insuredName","cusTypeCode","comCodePly:ComCode","license","lossId");
		logger.debug("forModifySeach.jsonData="+jsonData);
		return jsonData;
	}

	private ObjectToMapCallback searchCallBack() {
		ObjectToMapCallback callBack = new ObjectToMapCallback() {

			public Map<String,Object> toMap(Object object) {
				Map<String,Object> dataMap = new HashMap<String,Object>();
				PropQueryReslutVo resultVo = (PropQueryReslutVo)object;
				String registNo = resultVo.getRegistNo();
				String policyNo = resultVo.getPolicyNo();
				String policyNoLink = resultVo.getPolicyNoLink();
				String regNoUrl = "../propQueryOrLaunch/propModifyLaunchEdit?businessId="+resultVo.getLossId()+"&deflossFlag="+resultVo.getDeflossFlag();

				StringBuffer regHtml = new StringBuffer();
				regHtml.append("<a href='"+regNoUrl+"' >");
				regHtml.append(registNo);
				regHtml.append("</a>");
				dataMap.put("registNoHtml",regHtml.toString());

				String plyNoUrl = "/claimcar/policyView/policyView.do";
				plyNoUrl += "?registNo="+registNo;

				StringBuffer plyNoHtml = new StringBuffer();
				plyNoHtml.append("<a href='").append(plyNoUrl).append("'   target='_blank' >");
				plyNoHtml.append(policyNo);
				if(StringUtils.isNotBlank(policyNoLink)){
					plyNoHtml.append("<br>");
					plyNoHtml.append(policyNoLink);
				}
				plyNoHtml.append("</a>");
				dataMap.put("policyNoHtml",plyNoHtml.toString());

				StringBuffer bussTagHtml = new StringBuffer();
				bussTagHtml.append("<span class='badge label-danger radius '>vip</span>");
				if(YN01.Y.equals(resultVo.getMercyFlag())){
					bussTagHtml.append("<span class='badge label-warning radius' title='紧急'>急</span>");
				}

				dataMap.put("bussTag",bussTagHtml.toString());

				return dataMap;
			}
		};
		return callBack;
	}
}
