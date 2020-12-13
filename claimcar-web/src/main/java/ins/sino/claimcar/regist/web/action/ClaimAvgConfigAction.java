/******************************************************************************
 * CREATETIME : 2015年11月28日 下午3:15:33
 ******************************************************************************/
package ins.sino.claimcar.regist.web.action;

import ins.framework.common.ResultPage;
import ins.framework.web.util.ResponseUtils;
import ins.platform.utils.ExportExcelUtils;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.commom.vo.StringUtils;
import ins.sino.claimcar.regist.service.ClaimAvgConfigService;
import ins.sino.claimcar.regist.vo.PrpDClaimAvgVo;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;


/**
 * <pre></pre>
 * @author ★LiuPing
 * @CreateTime 2015年11月28日
 */
@Controller
@RequestMapping("/claimAvgList")
public class ClaimAvgConfigAction {

	private static Logger logger = LoggerFactory.getLogger(ClaimAvgConfigAction.class);
	@Autowired
	private ClaimAvgConfigService claimAvgConfigService;
	

	@RequestMapping(value = "/AvgConfigInit.do", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView init() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("claimAvgConfig/ClaimAvgConfigQueryList");

		return mav;
	}

	// 案均配置
	@RequestMapping(value = "/exportComp.do", method = RequestMethod.GET)
	@ResponseBody
	public void writeExcel(HttpServletResponse response) throws Exception {
		try{
			response.setCharacterEncoding("GBK");
			//File downFile = new File(SpringProperties.getProperty("path.template.avgclaim")+"/案均配置模版.xls");
			//File downFile = new File("D:/ang.xls"); 
			String fileDir = "ins/sino/claimcar/other/files/claimAvgConfigModel.xls";
			File downFile = ExportExcelUtils.getExcelDemoFile(fileDir);
			response.setContentType("binary/octet-stream");
			String filename = "案均配置模版.xls";
			response.setHeader("Content-Disposition","attachment; filename="+new String(filename.getBytes("gb2312"),"iso8859-1"));
			ServletOutputStream sos = response.getOutputStream();
			InputStream ins = new FileInputStream(downFile);
			byte[] bytes = new byte[1024];
			while(true){
				int chunk = ins.read(bytes);
				if(chunk== -1){
					break;
				}
				sos.write(bytes,0,chunk);
			}
			ins.close();
			ins = null;
		}catch(Exception e){}
	
		}
	
	@RequestMapping(value = "/search.do", method = RequestMethod.POST)
	@ResponseBody
	public String search(PrpDClaimAvgVo prpDClaimAvgVo) throws Exception {
		ResultPage<PrpDClaimAvgVo> page = null;
		page = claimAvgConfigService.findTaskForPage(prpDClaimAvgVo,0,10);
		String jsonData = ResponseUtils.toDataTableJson(page,"id","avgYear","comCode","riskCode","kindCode","avgType","avgAmount","validFlag"
				,"createUser","createTime","updateUser","updateTime");
		return jsonData;
	}

	@RequestMapping(value = "/importInit.do", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView importInit() {
		System.out.println("importInit==");
		ModelAndView mav = new ModelAndView();
		mav.setViewName("claimAvgConfig/ClaimAvgConfigImpQueryList");

		return mav;
	}
	
	// 案均配置导入
	@RequestMapping(value = "/importExcel.do",method = RequestMethod.POST)
	@ResponseBody
		public ModelAndView importExcel(@RequestParam("uploadFilezz") CommonsMultipartFile[] files,HttpServletRequest request) throws IOException, BiffException, ParseException {
		SimpleDateFormat sp=new SimpleDateFormat("yyyy:MM:dd HH:mm:ss"); 
		String CreateUser =request.getParameter("CreateUser");
		String[] avgYears = request.getParameterValues("avgYear");
		String[] avgTimes = request.getParameterValues("avgTime");
		String[] comCodes = request.getParameterValues("comCode");
		String state = null;
		ModelAndView mav = new ModelAndView();
		
		
		 for(int i = 0;i<files.length;i++){  
			         if(!files[i].isEmpty()){  
			 			FileInputStream in = (FileInputStream) files[i].getInputStream();
			 			Workbook book = Workbook.getWorkbook(in);
			 			Sheet[] sheets = book.getSheets();// 获取excel所有页
			 			for (int j = 0; j < sheets.length; j++) {
			 				Sheet sheet = book.getSheet(j);// 获取待解析的页
			 				if (sheet == null) {// 如果为null，解析下一页
			 					continue;
			 				}
			 				if (sheet.getName().indexOf("案均值") > -1) {// 判断当前页是否需要解析
			 					// 获取excel中的数据
			 					String comCode = "";// 机构
			 					BigDecimal year =null;// 年度
			 					String riskCode = "";// 险种
			 					String riskItemCode = "";// 险别
			 					String avgType = "";// 案均类型
			 					String avgMt = "";// 案均金额
			 					//Date avgTime =null;// 生效日期
			 					int rows = sheet.getRows();
			 					comCode = comCodes[i];
			 					year = new BigDecimal(avgYears[i]);
			 					Date avgTime = sp.parse(avgTimes[i]);
			 					System.out.println("=rows==="+rows);
			 					List<PrpDClaimAvgVo> dtoList = new ArrayList<PrpDClaimAvgVo>();
			 					try {
			 						for (int k = 1; k < rows; k++) {// 遍历所有行，解析数据
				 						PrpDClaimAvgVo dto = new PrpDClaimAvgVo();
				 						
				 						riskItemCode = sheet.getCell(1, k).getContents();
				 						avgType = sheet.getCell(2, k).getContents();
				 						avgMt = sheet.getCell(3, k).getContents();
				 						riskCode = sheet.getCell(0, k).getContents();
				 						if(avgMt==null||avgMt==""){
				 							mav.addObject("infomsg","3");
				 							mav.setViewName("claimAvgConfig/ClaimAvgConfigShow");
				 							 return mav;
				 						}
				 						BigDecimal avgMtdb = new BigDecimal(avgMt.trim());
				 						// 对象赋值
				 						dto.setAvgAmount(avgMtdb);
				 						dto.setAvgType(avgType);
				 						dto.setAvgYear(year);
				 						if(StringUtils.isNotBlank(comCode)){
				 							if("0002".equals(comCode.substring(0, 4))){
				 								dto.setComCode(comCode.substring(0, 4));
				 							}else{
				 								dto.setComCode(comCode.substring(0, 2));
				 							}
				 						}
				 						dto.setCreateTime(new Date());
				 						dto.setCreateUser(CreateUser);
				 						dto.setUpdateUser(CreateUser);
				 						dto.setUpdateTime(new Date());
				 						dto.setRiskCode(riskCode);
				 						dto.setKindCode(riskItemCode);
				 						dto.setEffectTimes(avgTime);
				 						dto.setValidFlag(CodeConstants.ValidFlag.VALID);
				 						dtoList.add(dto);
				 					}
								} catch (Exception e) {
									// TODO: handle exception
									e.printStackTrace();
									mav.addObject("infomsg","3");
		 							mav.setViewName("claimAvgConfig/ClaimAvgConfigShow");
		 							 return mav;
								}
			 				
			 				// 查询数据库是否存在记录
			 					List<PrpDClaimAvgVo> list=claimAvgConfigService.findAvgConfig(year,comCode,null);
			 					if (list.size() != 0) {// 循环遍历，将以前的数据设置为无效
									for (int d=0;d<list.size();d++) {
										list.get(d).setValidFlag(CodeConstants.ValidFlag.INVALID);
									}
									claimAvgConfigService.updatePrpDClaimAvg(list);
								}
			 					for(int p = 0;p<dtoList.size();p++){
			 						state =claimAvgConfigService.updates(dtoList.get(p));
			 					}
			 				}
			 				
			 			}
			 			book.close();// 解析完成，关闭Excel
			 		}
			 	}
				 if(state.equals("success")){
					mav.addObject("infomsg","1");
					mav.setViewName("claimAvgConfig/ClaimAvgConfigShow");
					
				 }else{
					 mav.addObject("infomsg","2");
						mav.setViewName("claimAvgConfig/ClaimAvgConfigShow");
				 }
		 return mav;
		}
	}
