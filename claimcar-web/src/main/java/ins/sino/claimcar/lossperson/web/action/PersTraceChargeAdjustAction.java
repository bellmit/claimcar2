/******************************************************************************
 * CREATETIME : 2016年2月2日 上午10:35:04
 ******************************************************************************/
package ins.sino.claimcar.lossperson.web.action;

import ins.framework.common.ResultPage;
import ins.framework.web.AjaxResult;
import ins.framework.web.support.ObjectToMapCallback;
import ins.framework.web.util.ResponseUtils;
import ins.sino.claimcar.CodeConstants.YN01;
import ins.sino.claimcar.flow.service.WfTaskHandleService;
import ins.sino.claimcar.lossperson.service.PersTraceHandleService;
import ins.sino.claimcar.lossperson.service.PersTraceService;
import ins.sino.claimcar.lossperson.vo.ChargeAdjustQueryResultVo;
import ins.sino.claimcar.lossperson.vo.ChargeAdjustQueryVo;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * 费用审核修改
 * 
 * @author ★XMSH
 */
@Controller
@RequestMapping(value = "/persTraceChargeAdjust")
public class PersTraceChargeAdjustAction {

	private static Logger logger = LoggerFactory.getLogger(PersTraceChargeAdjustAction.class);

	@Autowired
	private PersTraceService persTraceService;
	@Autowired
	private WfTaskHandleService wfTaskHandleService;
	@Autowired
	private PersTraceHandleService persTraceHandleService;

	/**
	 * 查询费用审核通过的任务
	 * 
	 * @return
	 * @modified: ☆XMSH(2016年2月22日 上午10:51:22): <br>
	 */
	@RequestMapping(value = "/preQueryList.do")
	public ModelAndView preQueryList() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("lossperson/persTraceChargeAdjust/ChargeAdjustQueryList");
		return mv;
	}

	@RequestMapping(value = "/ChargeAdjustQuery.do")
	@ResponseBody
	public String ChargeAdjustQuery(ChargeAdjustQueryVo queryVo) throws Exception {
		ResultPage<ChargeAdjustQueryResultVo> resultPage = persTraceService.findPageForChargeAdjust(queryVo);
		String jsonData = ResponseUtils.toDataTableJson(resultPage,searchCallBack(),"mercyFlag","customerLevel","registNo","policyNo","licenseNo","insuredName","remark");
		logger.debug("ChargeAdjustQuery.jsonData="+jsonData);

		return jsonData;
	}

	/**
	 * 提交费用审核修改
	 * 
	 * @param persTraceMainId
	 * @return
	 * @modified: ☆XMSH(2016年2月23日 下午4:36:16): <br>
	 */
	@RequestMapping(value = "/submitNextNode.do")
	@ResponseBody
	public AjaxResult submitNextNode(Long persTraceMainId) throws Exception {
		AjaxResult ajaxResult = new AjaxResult();
		try{
			persTraceHandleService.submitPLChargeAdjust(persTraceMainId);
			ajaxResult.setStatus(HttpStatus.SC_OK);
		}catch(Exception e){
			ajaxResult.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			ajaxResult.setStatusText(e.toString());
			logger.info("====提交费用审核修改失败======"+e.toString());
//			e.printStackTrace();
		}
		return ajaxResult;
	}

	private ObjectToMapCallback searchCallBack() {
		ObjectToMapCallback callBack = new ObjectToMapCallback() {

			public Map<String,Object> toMap(Object object) {
				Map<String,Object> dataMap = new HashMap<String,Object>();
				ChargeAdjustQueryResultVo resultVo = (ChargeAdjustQueryResultVo)object;
				String registNo = resultVo.getRegistNo();
				// String regNoUrl = "/claimcarperson/loadAjaxPage/loadSubmitChargeAdjust.ajax?persTraceMainId="+resultVo.getPersTraceMainId();

				StringBuffer regHtml = new StringBuffer();
				regHtml.append("<a href='javascript:void(0)' onclick='submitNextNode(this)' >");
				regHtml.append(registNo);
				regHtml.append("</a>");
				regHtml.append("<input type='hidden' name='persTraceMainId' value='"+resultVo.getPersTraceMainId()+"'>");
				dataMap.put("registNoHtml",regHtml.toString());

				StringBuffer bussTagHtml = new StringBuffer();
				bussTagHtml.append("<span class='badge label-danger radius '>vip</span>");
				if(YN01.Y.equals(resultVo.getMercyFlag())){
					bussTagHtml.append("<span class='badge label-warning radius' title='紧急'>急</span>");
				}
				dataMap.put("bussTagHtml",bussTagHtml.toString());

				String policyNoUrl = "/claimcar/policyView/policyView.do?registNo="+resultVo.getRegistNo();
				StringBuffer plyNoHtml = new StringBuffer();
				plyNoHtml.append("<a href='"+policyNoUrl+"' target='_blank' >");
				plyNoHtml.append(resultVo.getPolicyNo());
				if(StringUtils.isNotBlank(resultVo.getPolicyNoLink())){
					plyNoHtml.append("<br>");
					plyNoHtml.append(resultVo.getPolicyNoLink());
				}
				plyNoHtml.append("</a>");
				dataMap.put("policyNoHtml",plyNoHtml.toString());

				return dataMap;
			}
		};
		return callBack;
	}

}
