package ins.sino.claimcar.base.web.action;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import ins.platform.utils.ExportExcelUtils;
import ins.platform.utils.ImportExcelUtil;
import ins.sino.claimcar.other.service.ShangHaiDataService;
import ins.sino.claimcar.other.vo.ShangHaiDataVo;

/**
 * 上海数据统计
 * @author huanggs
 * @createDate 2017.5.8
 * 
 */
@Controller
@RequestMapping("/shangHaiData")
public class ShangHaiDataAction {

	@Autowired
	private ShangHaiDataService shangHaiDataService;

	/**
	 * 上海数据统计界面
	 */
	@RequestMapping("/queryDataView.do")
	public ModelAndView queryView() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("base/shanghai/MonthDataQueryList");
		return mav;
	}

	/**
	 * 查询
	 * @throws Exception
	 */
	@RequestMapping("/shangHaiQuery.do")
	@ResponseBody
	public ModelAndView shangHaiQuery(@RequestParam(value = "countStartDate" , required = true) Date countStartDate,
			@RequestParam(value = "countEndDate", required = true) Date countEndDate) throws Exception {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("base/shanghai/MonthDataQueryList");
		List<String> result = shangHaiDataService.findShangHaiCountData(countStartDate, countEndDate);
		mav.addObject("countStartDate", countStartDate);
		mav.addObject("countEndDate", countEndDate);
		mav.addObject("resultList", result);
		mav.addObject("currentTime", new Date());
		return mav;
	}

	/**
	 * 导出excel
	 * @throws Exception 
	 */
	@RequestMapping("/exportExcel.do")
	@ResponseBody
	public String exportExcel(@RequestParam(value = "countStartDate" , required = true) Date countStartDate,
			@RequestParam(value = "countEndDate" , required = true) Date countEndDate,HttpServletResponse response) throws Exception {
		//交强报案-50,商业报案-80,交强结案-52,商业结案-82,交强注销-54,商业注销-83
		String[] requestType = {  "50", "80" , "52", "82" , "54", "83" };
		
		String fileDir = "ins/sino/claimcar/other/files/shangHaiDataTemplate.xls";
		File file = ExportExcelUtils.getExcelDemoFile(fileDir);
		////初始化WorkBook
		FileInputStream fis = new FileInputStream(file);  
		Workbook  wb = new ImportExcelUtil().getWorkbook(fis, file.getName());    
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        //查询综合数据
       List<String> resultSheet1 = shangHaiDataService.findShangHaiCountData(countStartDate, countEndDate);
       
        SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
		String startDate = sdf.format(countStartDate);
		String endDate = sdf.format(countEndDate);
		String today = sdf.format(new Date());
        resultSheet1.add(0,startDate+"至"+endDate);
        resultSheet1.add(0,today);
        String firstSheet = "月数据统计表";
        ExportExcelUtils.writeToCell(wb, firstSheet,2,7, resultSheet1.get(0));
        for(int k = 1;k < resultSheet1.size(); k ++){
        	ExportExcelUtils.writeToCell(wb, firstSheet,5,k, resultSheet1.get(k));
        }
        		
		
		//循环写入sheet
		for (int i = 0; i < requestType.length; i++) {
			//查询数据
			List<ShangHaiDataVo> results = shangHaiDataService.getDetailByType(countStartDate, countEndDate, requestType[i]);	
			//整理数据格式,为写入excel做准备
			List<Map<String,Object>> list = shangHaiDataService.createExcelRecord(results, requestType[i]);
			
			String sheetName = (String) list.get(0).get("sheetName");
			Set keySet = list.get(1).keySet();
			 String[] keys = new String[keySet.size()];
			 keySet.toArray(keys);
			 
			try{			
		        ExportExcelUtils.writeNewExcelForShanghai(wb, sheetName, list, keys);
			}
			catch(IOException e){
				e.printStackTrace();
			}
		}
		wb.write(os);
			
		//准备下载
			byte[] content = os.toByteArray();
			InputStream is = new ByteArrayInputStream(content);
			// 设置response参数，可以打开下载页面
			response.reset();

			String fileName = "上海理赔月数据统计.xls";
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(fileName, "utf-8"));
			ServletOutputStream out = response.getOutputStream();
			BufferedInputStream bis = null;
			BufferedOutputStream bos = null;
			try{
				bis = new BufferedInputStream(is);
				bos = new BufferedOutputStream(out);
				byte[] buff = new byte[2048];
				int bytesRead;
				// Simple read/write loop.
				while( -1!=( bytesRead = bis.read(buff,0,buff.length) )){
					bos.write(buff,0,bytesRead);
				}
			}
			catch(final IOException e){
				throw e;
			}
			finally{
				if(bis!=null) bis.close();
				if(bos!=null) bos.close();
		}
		return null;
	}
	
}
