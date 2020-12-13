package ins.sino.claimcar.policyLink.web.action;

import ins.framework.common.ResultPage;
import ins.framework.web.util.ResponseUtils;
import ins.platform.utils.DateUtils;
import ins.sino.claimcar.policyLinkage.service.LinkageService;
import ins.sino.claimcar.policyLinkage.vo.PolicyLinkQueryVo;
import ins.sino.claimcar.policyLinkage.vo.PolicyLinkResultVo;
import ins.sino.claimcar.policyLinkage.vo.PrpLLinkCaseCarVo;
import ins.sino.claimcar.policyLinkage.vo.PrpLLinkCaseImgVo;
import ins.sino.claimcar.policyLinkage.vo.PrpLLinkCaseInjuredVo;
import ins.sino.claimcar.policyLinkage.vo.PrpLLinkCaseMainVo;

import java.io.File;
import java.io.FileInputStream;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.axis.encoding.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;




@Controller
@RequestMapping("/policyLink")
public class PolicyLinkAction {
    @Autowired
	LinkageService linkageService;
	private static Logger logger = LoggerFactory.getLogger(PolicyLinkAction.class);
	
	// 查询机构信息Action
	@RequestMapping(value = "/policyLinkQueryList.do")
	public ModelAndView policyLinkQueryList() {
		ModelAndView mav = new ModelAndView();
		Date endDate = new Date();
		Date startDate = DateUtils.addDays(endDate, -15);
		
		mav.addObject("timeStart", startDate);
		mav.addObject("timeEnd", endDate);
		
		mav.setViewName("base/policyLink/PolicyLinkQueryList");
		return mav;
	}
	
	@RequestMapping("/policyLinkQuery.do")
	@ResponseBody
	public String policyLinkQuery(PolicyLinkQueryVo queryVo) throws Exception{
		
		ResultPage<PolicyLinkResultVo> resultPage = linkageService.findCaseList(queryVo);
		String jsonData = ResponseUtils.toDataTableJson(resultPage,"caseId","respUserName","respUserPhone",
				"caseType","accidentTime","driverName","phone","hphm","status:Link_Status","isResp");
		System.out.println();
		return jsonData;
	}
	
	@RequestMapping("/policyLinkShowList.do")
	public ModelAndView policyLinkShowList(HttpServletRequest request, HttpServletResponse response) throws Exception{
		String caseId = request.getParameter("caseId");
		ModelAndView mav = new ModelAndView();
		PrpLLinkCaseMainVo mainVo = linkageService.findMainVo(caseId);
		List<PrpLLinkCaseCarVo> carList =linkageService.findCarList(caseId);
		List<PrpLLinkCaseInjuredVo>  injuredList = linkageService.findInjuredList(caseId);
		List<PrpLLinkCaseImgVo> imgList =linkageService.findImgList(caseId);
		
		if(imgList != null && imgList.size() > 0){  //从服务器本地读图片
			for(PrpLLinkCaseImgVo imgVo : imgList){
				//String fileName = "";
				File file = new File(imgVo.getSmallPicUrl());
				System.out.println(file);
				if(file.exists()){
				  FileInputStream inputStream = new FileInputStream(file);  
			        int i = inputStream.available();  
			        //byte数组用于存放图片字节数据  
			        byte[] buff = new byte[i];  
			        inputStream.read(buff);  
			        //记得关闭输入流  
			        inputStream.close();  
			        //设置发送到客户端的响应内容类型  
			       // response.setContentType("image/*");  
			        //OutputStream out = response.getOutputStream();  
			        //out.write(buff);  
			        imgVo.setSmallPicUrl("data:;base64,"+Base64.encode(buff));
			       // System.out.println(imgVo.getSmallPicUrl());
			     //FileImageOutputStream imageOutput = new FileImageOutputStream(new File(path));  
			     //imageOutput.write(buff, 0, buff.length);
//			        for(int j = 0; j < buff.length ; j++){
//			        	System.out.print(buff[j] + " ");
//			        	if(j%10==0){
//			        		System.out.println();
//			        	}
//			        }
			        //关闭响应输出流  
			       // out.close(); 
				}else{
					imgVo.setSmallPicUrl(imgVo.getPicUrl());
				}
				
			}
			
		}
		

		mav.addObject("mainVo", mainVo);
		mav.addObject("carList", carList);
		mav.addObject("injuredList", injuredList);
		mav.addObject("imgList", imgList);
		mav.setViewName("base/policyLink/PolicyLinkShowView");
		return mav;
	}
	@RequestMapping("/showImg.do")
	public ModelAndView showImg(String picUrl,String tags){
		ModelAndView mav = new ModelAndView();
		System.out.println(picUrl);
		System.out.println(tags);
		mav.addObject("url", picUrl);
		mav.addObject("tags", tags);
		mav.setViewName("/base/policyLink/PolicyLinkImg");
		return mav;
	}
}
