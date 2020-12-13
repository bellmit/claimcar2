package ins.sino.claimcar.common.web.action;

import ins.framework.web.AjaxResult;
import ins.platform.common.web.util.WebUserUtils;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.carYxImage.service.CarXyImageService;
import ins.sino.claimcar.regist.web.action.RegistEditAction;

import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.SpringProperties;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.sunyard.insurance.encode.client.EncodeAccessParam;
@Controller
@RequestMapping("/yxImageUpload")
public class YxImageUploadAction {
	public Logger logger = LoggerFactory.getLogger(YxImageUploadAction.class);
	@Autowired
	CarXyImageService carXyImageService;
	/**
	 * 获取影像查看请求参数
	 * @param bussNo
	 * @return
	 * @modified:
	 * ☆yzy(2019年3月14日 下午2:07:26): <br>
	 */
	@RequestMapping(value="/urlReqQueryParam")
	public ModelAndView urlReqQueryParam(String bussNo) {
		ModelAndView mv = new ModelAndView();
		//获取请求的xml参数
		try {
		SysUserVo user = WebUserUtils.getUser();
		String reqXml=carXyImageService.getReqQueryParam(user,CodeConstants.APPROLE,bussNo);
		String key=SpringProperties.getProperty("YX_key");
		String Qurl=SpringProperties.getProperty("YX_QUrl");
		//生成请求参数
		String param = EncodeAccessParam.getEncodeParam("format=xml&code="+CodeConstants.YXQUERYCODE+"&xml="+reqXml,20*1000,key);
		String url = Qurl+"?data="+param;
		mv.addObject("url",url);
		logger.info("信雅达接口调用参数:"+"format=xml&code="+CodeConstants.YXQUERYCODE+"&xml="+reqXml);
		} catch (Exception e) {
			logger.info("获取影像查看报错", e);
		}
		mv.setViewName("image/ImgView");
		return mv;
	}

	
	/**
	 * 获取影像上传请求参数
	 * @param bussNo
	 * @return
	 * @modified:
	 * ☆yzy(2019年3月15日 上午午9:00:30): <br>
	 */
	@RequestMapping(value = "/urlReqUploadParam")
	public ModelAndView urlReqUploadParam(String bussNo) { 
		ModelAndView mv = new ModelAndView();
		try {
			//获取请求的xml参数
			SysUserVo user = WebUserUtils.getUser();
			String reqXml=carXyImageService.getReqUploadParam(user,CodeConstants.APPROLE,bussNo,CodeConstants.YXQUERYPARAM,null);
			String key=SpringProperties.getProperty("YX_key");
			String Qurl=SpringProperties.getProperty("YX_QUrl");
			//生成请求参数
			String param = EncodeAccessParam.getEncodeParam("format=xml&code="+CodeConstants.YXUPDATECODE+"&xml="+reqXml,20*1000,key);
			String url = Qurl+"?data="+param;
			mv.addObject("url",url);
			logger.info("信雅达接口调用参数:"+"format=xml&code="+CodeConstants.YXUPDATECODE+"&xml="+reqXml);
		} catch (Exception e) {
			logger.info("获取影像上传报错", e);
		}
		mv.setViewName("image/ImgUpload");
		return mv;
	}
}
