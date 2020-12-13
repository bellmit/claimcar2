/******************************************************************************
 * CREATETIME : 2016年1月19日 上午11:37:25
 ******************************************************************************/
package ins.sino.claimcar.flow.web.action;

import freemarker.core.ParseException;
import ins.framework.common.ResultPage;
import ins.framework.web.AjaxResult;
import ins.framework.web.util.ResponseUtils;
import ins.platform.common.web.util.WebUserUtils;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.claim.service.ClaimTaskService;
import ins.sino.claimcar.claim.vo.PrpLClaimVo;
import ins.sino.claimcar.flow.service.SysMsgContentService;
import ins.sino.claimcar.flow.service.SysMsgReceiverService;
import ins.sino.claimcar.manager.vo.SysMsgContentVo;
import ins.sino.claimcar.manager.vo.SysUtiBulletinVo;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;


/**
 * <pre>
 * 案件备注
 * </pre>
 * 
 * @author ★LiuPing
 * @CreateTime 2016年1月19日
 */
@Controller
@RequestMapping("/sysMsg")
public class SysMsgAction {

	// 服务装载
	@Autowired
	private SysMsgContentService sysMsgContentService;
	@Autowired
	private SysMsgReceiverService sysMsgReceiverService;
	@Autowired
	private  ClaimTaskService claimTaskService;

	/**
	 * 初始化案件备注页面
	 * 
	 * @param bussNo
	 * @return
	 */
	@RequestMapping(value = "/initRegistMsg.do")
	public ModelAndView initReportMsg(HttpServletRequest request) {
		String bussNo = request.getParameter("bussNo");
		String mid = request.getParameter("msgId");
		Long msgId = null;
		if (!StringUtils.isBlank(mid) && !mid.equals("undefined")) {
			msgId = Long.parseLong(mid);
		}
		ModelAndView mv = new ModelAndView();
		List<SysMsgContentVo> reSysMsgContentVos = sysMsgContentService
				.findSysMsg(bussNo);
		if (msgId != null) {
			// 如果url请求中包含msgId，则表示是首页查看留言信息，此时将该条留言的floorIndex传到页面上操作
			SysMsgContentVo msgVo = new SysMsgContentVo();
			for (int i = 0; i < reSysMsgContentVos.size(); i++) {
				msgVo = reSysMsgContentVos.get(i);
				if (msgVo.getId().equals(msgId)) {
					int focusInd = reSysMsgContentVos.get(i).getFloorIndex();
					mv.addObject("focusInd", focusInd);
				}
			}
		}
		mv.addObject("sysMsgContentVos", reSysMsgContentVos);
		mv.setViewName("sysmessage/RegistMessageList");
		return mv;
	}

	/**
	 * 保存案件备注信息并返回添加
	 * 
	 * @param sysMsgContentVo
	 * @return
	 */
	@RequestMapping(value = "/saveSysMsg.do")
	@ResponseBody
	public AjaxResult saveSysMsg(SysMsgContentVo sysMsgContentVo) {

		SysMsgContentVo msgVo = sysMsgContentService
				.saveSysMsg(sysMsgContentVo);
		Long reId = msgVo.getId();
		AjaxResult ajaxResult = new AjaxResult();
		ajaxResult.setStatus(org.apache.http.HttpStatus.SC_OK);
		ajaxResult.setData(reId);

		return ajaxResult;
	}

	/**
	 * 
	 * 添加一条案件备注的ajax请求
	 * 
	 * @param reId
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value = "/addReBox.ajax")
	@ResponseBody
	public ModelAndView addReBox(Long reId)
			throws ParseException {
		ModelAndView mv = new ModelAndView();
		SysMsgContentVo MsgVo = new SysMsgContentVo();
		if (reId != null) {
			MsgVo = sysMsgContentService.findSysMsgByPK(reId);
		}
		mv.setViewName("sysmessage/RegistMessageList_ReBox");
		mv.addObject("sysMsgContent", MsgVo);
		return mv;
	}

	/**
	 * 在首页加载之后查询该用户的留言通知消息并显示
	 * 
	 * @param userCode
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value = "/findMsgCont.ajax")
	@ResponseBody
	public ModelAndView findMsgCont(String userCode) throws ParseException {
		ModelAndView mv = new ModelAndView();
		List<SysMsgContentVo> MsgVoList = new ArrayList<SysMsgContentVo>();
		// 根据当前系统操作员信息查询留言信息
		MsgVoList = sysMsgReceiverService.findMsgByUser(userCode);
		mv.addObject("sysMsgContents", MsgVoList);
		mv.setViewName("sysmessage/SysMsgReceList");

		return mv;
	}

	/**
	 * 根据报案号查询立案是否全部注销
	 * @param registNo
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value = "/checkCaseCancel.do")
	@ResponseBody
	public AjaxResult checkCaseCancel(String registNo) throws ParseException {
		AjaxResult ar = new AjaxResult();
		List<PrpLClaimVo> claimVoList = claimTaskService.findClaimListByRegistNo(registNo);
		if(claimVoList!=null&&claimVoList.size()>0){
			for(PrpLClaimVo claimVo:claimVoList){
				if(claimVo.getCancelTime()==null){
					ar.setStatus(HttpStatus.SC_OK);
					ar.setData("N");//存在没有注销的立案
					return ar;
				}
			}
		}else{
			// 不存在立案信息  可使用案件备注
			ar.setStatus(HttpStatus.SC_OK);
			ar.setData("N");//存在没有注销的立案
			return ar;
		}
		ar.setStatus(HttpStatus.SC_OK);
		ar.setData("Y");
		return ar;
	}
	
	/**
	 * 初始化系统消息页面
	 * 
	 * @param bussNo
	 * @return
	 */
	@RequestMapping(value = "/sysMsgReceEdit.do")
	public ModelAndView sysMsgReceEdit(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("sysmessage/SysMsgReceEdit");
		return mv;
	}
	
	
	/**
	 * 查询系统公告
	 * <pre></pre>
	 * @return
	 * @throws ParseException
	 * @modified:
	 * ☆WLL(2016年10月24日 下午6:03:48): <br>
	 */
	@RequestMapping(value = "/findSysUtiBulletin.ajax")
	@ResponseBody
	public ModelAndView findSysUtiBulletin() throws ParseException {
		ModelAndView mv = new ModelAndView();
		List<SysUtiBulletinVo> SysUtiBulletinVoList = new ArrayList<SysUtiBulletinVo>();
		SysUtiBulletinVoList = sysMsgReceiverService.findAllSysUtiBulletin();
		// 只显示前四条记录 其他点击查看更多
		if(SysUtiBulletinVoList!=null&&SysUtiBulletinVoList.size()>4){
			SysUtiBulletinVoList = SysUtiBulletinVoList.subList(0,4);
		}
		mv.addObject("sysUtiBulletinVoList", SysUtiBulletinVoList);
		mv.setViewName("sysmessage/SysUtiBulletinList");

		return mv;
	}
	/**
	 * 点击查看更多系统公告界面初始化
	 * <pre></pre>
	 * @return
	 * @modified:
	 * ☆LuLiang(2016年10月26日 上午11:47:27): <br>
	 */
	@RequestMapping(value = "/initMoreSysUtiBulletinList.do")
	@ResponseBody
	public ModelAndView initMoreSysUtiBulletin(){
		ModelAndView mv = new ModelAndView();
		mv.setViewName("sysmessage/SysUtiBulletinQueryList");

		return mv;

	}
	/**
	 * 查询更多系统公告
	 * <pre></pre>
	 * @return
	 * @throws ParseException
	 * @modified:
	 * ☆WLL(2016年10月24日 下午9:06:48): <br>
	 */
	@RequestMapping(value = "/findMoreSysUtiBulletin.do")
	@ResponseBody
	public String findMoreSysUtiBulletin() throws ParseException {
		ResultPage<SysUtiBulletinVo> resultPage = sysMsgReceiverService.findMoreSysUtiBulletin();
		String jsonData = ResponseUtils.toDataTableJson(resultPage, "bulletinId","title", "inputTime");
		return jsonData;

	}
	/**
	 * 点击查看更多案件备注界面初始化
	 * <pre></pre>
	 * @return
	 * @modified:
	 * ☆LuLiang(2016年10月26日 上午11:47:27): <br>
	 */
	@RequestMapping(value = "/initMoreSysMsgList.do")
	@ResponseBody
	public ModelAndView initMoreSysMsgList(){
		ModelAndView mv = new ModelAndView();
		mv.setViewName("sysmessage/SysMsgReceQueryList");

		return mv;

	}
	/**
	 * 点击查看更多案件备注
	 * <pre></pre>
	 * @return
	 * @throws ParseException
	 * @modified:
	 * ☆WLL(2016年10月24日 下午9:06:48): <br>
	 */
	@RequestMapping(value = "/findMoreSysMsg.do")
	@ResponseBody
	public String findMoreSysMsg() throws ParseException {
		SysUserVo userVo = WebUserUtils.getUser();
		ResultPage<SysMsgContentVo> resultPage = sysMsgReceiverService.findMoreSysMsg(userVo.getUserCode());
		String jsonData = ResponseUtils.toDataTableJson(resultPage, "id","bussNo", "msgContents","createDate","createUser:UserCode","userComCode:ComCode");
		return jsonData;

	}
	
}
