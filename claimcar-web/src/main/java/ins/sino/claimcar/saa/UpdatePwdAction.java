package ins.sino.claimcar.saa;

import ins.framework.web.AjaxResult;
import ins.platform.common.web.util.WebUserUtils;
import ins.platform.sysuser.service.facade.SysUserService;
import ins.platform.vo.SysUserVo;

import java.security.MessageDigest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/updatePwd")
public class UpdatePwdAction {
	
	@Autowired
	private SysUserService sysUserService;
	
	@RequestMapping(value = "/initUserInfo.do")
	public ModelAndView initUserInfo(String userCode){
		ModelAndView mav = new ModelAndView();
		mav.addObject("UserCode", userCode);
		mav.setViewName("sysmessage/updatePwd");
		return mav;
	}
	@RequestMapping(value = "/checkOldPwd.do")
	@ResponseBody
	public AjaxResult checkOldPwd(String userCode,String oldPwd){
		String oldpwd =this.toMD5(oldPwd);
		System.out.println(oldpwd);
		boolean flag = sysUserService.findUserInfoByuserCode(userCode, oldpwd);
		AjaxResult ajaxResult = new AjaxResult();
		ajaxResult.setStatus(org.apache.http.HttpStatus.SC_OK);
		ajaxResult.setData(flag);
		
		return ajaxResult;
	}
	
	
	@RequestMapping(value = "/updatePwd.do")
	@ResponseBody
	public AjaxResult updatePwd(String userCode,String oldPwd,String newPwd1){
		String oldpwd = this.toMD5(oldPwd);
		String newpwd = this.toMD5(newPwd1);
		System.out.println(oldpwd);
		boolean flag = sysUserService.findUserInfoByuserCode(userCode, oldpwd);
		if(true == flag){
			sysUserService.updatePwd(userCode, newpwd);
		}
		AjaxResult ajaxResult = new AjaxResult();
		ajaxResult.setStatus(org.apache.http.HttpStatus.SC_OK);
		ajaxResult.setData(flag);
		
		return ajaxResult;
	}
	
	public String toMD5(String plainText) {
		try {
			// 生成实现指定摘要算法的 MessageDigest 对象。
			MessageDigest md = MessageDigest.getInstance("MD5");
			// 使用指定的字节数组更新摘要。
			md.update(plainText.getBytes());
			// 通过执行诸如填充之类的最终操作完成哈希计算。
			byte b[] = md.digest();
			// 生成具体的md5密码到buf数组
			int i;
			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}

			//System.out.println("32位: " + buf.toString().toUpperCase());// 32位的加密
			//System.out.println("16位: " + buf.toString().substring(8, 24).toUpperCase());// 16位的加密，其实就是32位加密后的截取
			return buf.toString().toUpperCase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	@RequestMapping(value = "/initCarID.do")
	public ModelAndView initCarID(String userCode){
		ModelAndView mav = new ModelAndView();
		
		SysUserVo userLoadVo = WebUserUtils.getUser();
		if(userLoadVo != null && !userCode.equals(userLoadVo.getUserCode())){
			throw new IllegalArgumentException("要修改的用户信息不是当前系统登录用户，无修改权限！！！");
		}
		SysUserVo userVo = sysUserService.findByUserCode(userCode);
		mav.addObject("userName", userVo.getUserName());
		mav.addObject("identifyNumber", userVo.getIdentifyNumber());
		mav.addObject("userCode", userCode);
		mav.setViewName("sysmessage/updateCarID");
		return mav;
	}
	
	@RequestMapping(value = "/updateCarID.do")
	@ResponseBody
	public AjaxResult updateCarID(String userCode,String identifyNumber){
		AjaxResult ajaxResult = new AjaxResult();
		try{
			sysUserService.updateIdentifyNumber(userCode, identifyNumber);
			ajaxResult.setStatus(org.apache.http.HttpStatus.SC_OK);
		}catch(Exception e){
			e.printStackTrace();
			ajaxResult.setStatus(org.apache.http.HttpStatus.SC_NO_CONTENT);
		}
		
		return ajaxResult;
	}

}
