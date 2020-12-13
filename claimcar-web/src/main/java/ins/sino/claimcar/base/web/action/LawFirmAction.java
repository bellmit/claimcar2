package ins.sino.claimcar.base.web.action;

import ins.framework.common.ResultPage;
import ins.framework.web.AjaxResult;
import ins.framework.web.bind.annotation.FormModel;
import ins.framework.web.util.ResponseUtils;
import ins.platform.common.web.util.WebUserUtils;
import ins.platform.utils.ExportExcelUtils;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.claim.vo.PrpdLawFirmVo;
import ins.sino.claimcar.flow.service.PayCustomService;
import ins.sino.claimcar.manager.vo.PrpLPayCustomVo;
import ins.sino.claimcar.manager.vo.PrplOldaccbankCodeVo;
import ins.sino.claimcar.other.service.LawFirmService;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.text.ParseException;
//import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;











//import jxl.Sheet;
//import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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

@Controller
@RequestMapping("/lawFirm")
public class LawFirmAction {
	
	@Autowired
	LawFirmService lawFirmService;
	@Autowired
	private PayCustomService payCustomService;
	
	private static Logger logger = LoggerFactory.getLogger(LawFirmAction.class);
	
	@RequestMapping("/lawFirmList.do")
	@ResponseBody
	public ModelAndView lawFirmList() {
		ModelAndView modelAndView = new ModelAndView();
		
		modelAndView.setViewName("base/lawFirm/LawFirmList");
		return modelAndView;
	}
	
	@RequestMapping("/lawFirmEdit.do")
	@ResponseBody
	public ModelAndView lawFirmEdit(HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView();
		String lid = request.getParameter("lawFirmId");
		String sign =request.getParameter("sign");
		if(lid!=null){
			Long id = Long.parseLong(lid);
			PrpdLawFirmVo prpdLawFirmVo = lawFirmService
					.findPrpdLawFirmVoByPK(id);
			modelAndView.addObject("PrpdLawFirm", prpdLawFirmVo);
			modelAndView.addObject("sign",sign);
		}
	
		modelAndView.setViewName("base/lawFirm/LawFirmEdit");

		return modelAndView;
	}
	
	/**
	 * 添加律师事务所
	 * @return
	 */
	@RequestMapping("/lawFirmAdd.do")
	@ResponseBody
	public ModelAndView lawFirmAdd(){
		ModelAndView mav = new ModelAndView();
		
		mav.addObject("sign", "2");
		mav.setViewName("base/lawFirm/LawFirmEdit");
		return mav;
	}
	
	@RequestMapping("/saveLawFirm.do")
	@ResponseBody
	public AjaxResult saveLawFirm(
			@FormModel(value = "prpdLawFirmVo") PrpdLawFirmVo prpdLawFirmVo) {
		AjaxResult ajaxResult = new AjaxResult();
		Date date = new Date();
		if (prpdLawFirmVo.getId() != null) {
			prpdLawFirmVo.setUpdateTime(date);
			prpdLawFirmVo.setUpdateUser(WebUserUtils.getUserCode());
	    } else {
	    	prpdLawFirmVo.setCreateTime(date);
	    	prpdLawFirmVo.setCreateUser(WebUserUtils.getUserCode());
		}
		prpdLawFirmVo.setComCode(WebUserUtils.getComCode());
		lawFirmService.saveOrUpdatePrpdLawFirm(prpdLawFirmVo);
		ajaxResult.setStatus(org.apache.http.HttpStatus.SC_OK);
		return ajaxResult;
	}
	
	@RequestMapping("/findLawFirm.do")
	@ResponseBody
	public String search(
			@FormModel(value = "prpdLawFirmVo") PrpdLawFirmVo prpdLawFirmVo,
			@RequestParam(value = "start", defaultValue = "0") Integer start,
			@RequestParam(value = "length", defaultValue = "10") Integer length) {
		ResultPage<PrpdLawFirmVo> resultPage = lawFirmService.findAllPrpdLawFirmByHql(prpdLawFirmVo, start, length);
		String jsonData = ResponseUtils.toDataTableJson(resultPage, "id",
				"lawFirmCode", "lawFirmName", "lawFirmAddress",
				"mobileNo","principal","contacts");
		logger.debug(jsonData);
		return jsonData;
	}
	
	@RequestMapping(value = "/importInit.do", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView importInit() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("base/lawFirm/LawFirmImport");

		return mav;
	}
	
	//导入excel
		@RequestMapping(value = "/importExcel.do",method = RequestMethod.POST)
		@ResponseBody
			public ModelAndView importExcel(@RequestParam("importFile") CommonsMultipartFile files,HttpServletRequest request) 
					throws IOException, BiffException, ParseException {
//			    SimpleDateFormat sp=new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
//			    String CreateUser =request.getParameter("CreateUser");
			
			    //校验文件
			    String flag = checkFile(files);
			    String state = null;
		    	ModelAndView mav = new ModelAndView();
		    	Workbook book = null;
		    	if(flag.equals("1")){
	    		    //获取文件名
	    		    String fileName = files.getOriginalFilename();
		        	InputStream in = files.getInputStream();		        		
	                //通过文件名后缀判断文件的版本
		        	if(fileName.endsWith("xls")){
		        		book = new HSSFWorkbook(in);		        		
		        	}else if(fileName.endsWith("xlsx")){
		        		book = new XSSFWorkbook(in);
		        	}
		        	if(book != null){
		        		//获取excel工作簿的所有页
		        		for(int sheetNum = 0; sheetNum < book.getNumberOfSheets(); sheetNum++){
		        			Sheet sheet = book.getSheetAt(sheetNum);
		        			if(sheet == null){
		        				continue;
		        			}
		        			int firstRowNum = sheet.getFirstRowNum(); //获取当前sheet的开始行
		        			int lastRowNum = sheet.getLastRowNum();   //湖区当前sheet的结束行
		        			List<PrpdLawFirmVo> dtoList = new ArrayList<PrpdLawFirmVo>();
		        			//循环除了第一行之外的所有行
		        			for(int rowNum = firstRowNum + 1 ; rowNum <= lastRowNum ; rowNum++){
		        				Row row = sheet.getRow(rowNum);      //获取当前行
		        				if(row == null){
		        					continue;
		        				}
		        				int firstCellNum = row.getFirstCellNum(); //获取当前行的开始列
		        				int lastCellNum = row.getPhysicalNumberOfCells(); //获取当前行的列数
		        				//定义数组，将每一行数据存到数组中
		        				String[] cells = new String[row.getPhysicalNumberOfCells()];
		        				Cell[] cellss = new Cell[lastCellNum];
		        				//循环当前行，获取当前行每个单元格的值
		        				for(int cellNum = firstCellNum ; cellNum < lastCellNum ; cellNum++){
		    		 				Cell cell = row.getCell(cellNum);
		    		 				cells[cellNum] = getCellValue(cell);
		    		 				cellss[cellNum] = row.getCell(cellNum);
		        				}
		        				
		        				PrpdLawFirmVo dto = new PrpdLawFirmVo();
		        				// 获取excel中的数据,并赋值给对象
		        				dto.setLawFirmCode(cells[0]);     //律师事务所代码
		        				dto.setLawFirmName(cells[1]);     //律师事务所名称
		        				dto.setLawFirmAddress(cells[2]);  //律师事务所地址
		        				dto.setMobileNo(cells[3]);        //律师事务所电话
		        				dto.setPrincipal(cells[4]);       //负责人
		        				dto.setContacts(cells[5]);        //联系人
		        				dtoList.add(dto);
		        			}
		        			//没有一条数据则为空文件
		        			if(dtoList.size() < 1){
		        				mav.addObject("infomsg","6");
			    				mav.setViewName("base/lawFirm/LawFirmInfoShow");
			    				return mav;
		        			}
		        			for(int p = 0;p<dtoList.size();p++){
		 						state = lawFirmService.updates(dtoList.get(p));
		 					}
		        		}
		        	}
			    }else{
	                if(flag.equals("7")){
	                	mav.addObject("infomsg","7");
	    				mav.setViewName("base/lawFirm/LawFirmInfoShow");
	    				return mav;
	                }
			    }
		    	
		    	book.close();// 解析完成，关闭Excel
		    	if(state.equals("success")){
					mav.addObject("infomsg","1");
					mav.setViewName("base/lawFirm/LawFirmInfoShow");
					
				 }else{
					 mav.addObject("infomsg","2");
						mav.setViewName("base/lawFirm/LawFirmInfoShow");
				 }
		    return mav;
		}
	
	/**
	 * 校验上传的文件是否为excel文件
	 * @param file
	 * @throws IOException
	 */
	public static String checkFile(CommonsMultipartFile file) throws IOException{

		//获取文件名
		String fileName = file.getOriginalFilename();
		if(!fileName.endsWith("xls") && !fileName.endsWith("xlsx")){
			logger.info(fileName + " 不是excel文件！");
			return "7";
		}
		return "1";
	}
	
	/**
	 * 把每个单元格内的数据都转换成String类型
	 * @param cell
	 * @return
	 */
	public static String getCellValue(Cell cell){  
        String cellValue = "";  
        if(cell == null){  
            return cellValue;  
        }  
        //把数字当成String来读，避免出现1读成1.0的情况  
        if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC){  
            cell.setCellType(Cell.CELL_TYPE_STRING);  
        }  
        //判断数据的类型  
        switch (cell.getCellType()){  
            case Cell.CELL_TYPE_NUMERIC: //数字  
                cellValue = String.valueOf(cell.getNumericCellValue());  
                break;  
            case Cell.CELL_TYPE_STRING: //字符串  
                cellValue = String.valueOf(cell.getStringCellValue());  
                break;  
            case Cell.CELL_TYPE_BOOLEAN: //Boolean  
                cellValue = String.valueOf(cell.getBooleanCellValue());  
                break;  
            case Cell.CELL_TYPE_FORMULA: //公式  
                cellValue = String.valueOf(cell.getCellFormula());  
                break;  
            case Cell.CELL_TYPE_BLANK: //空值   
                cellValue = "";  
                break;  
            case Cell.CELL_TYPE_ERROR: //故障  
                cellValue = "非法字符";  
                break;  
            default:  
                cellValue = "未知类型";  
                break;  
        }  
        return cellValue;  
    }  

	
	//导出excel		
	@RequestMapping(value = "/exportExcel.do")
	@ResponseBody
	public ModelAndView exportExcel(HttpServletResponse response) throws Exception {
		try{
			List<PrpdLawFirmVo> lawfirmList = lawFirmService.findAllPrpdLawFirm();
			
			//避免查询的结果集太大，导致导出时间过长，从而导出失败
			if(lawfirmList.size() > 1000){
				response.setCharacterEncoding("utf-8");
				response.setContentType("Text/plain");
				PrintWriter out = response.getWriter();
				out.print("查询的结果超过1000条，请缩小查询范围！");
				out.close();
				return null;
			}
			List<Map<String,Object>> list = createExcelRecord(lawfirmList);
			String fileDir = "ins/sino/claimcar/other/files/lawFirm.xlsx";
			String keys[] = {"LawFirmCode","LawFirmName","LawFirmAddress","MobileNo","Principal","Contacts"};// map中的key
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			try{
				File file = ExportExcelUtils.getExcelDemoFile(fileDir);
				ExportExcelUtils.writeNewExcel(file,"Sheet1",list,keys,CodeConstants.IsSingleAccident.NOT).write(os);
				
			}
			catch(IOException e){
				e.printStackTrace();
			}
			byte[] content = os.toByteArray();
			InputStream is = new ByteArrayInputStream(content);
			// 设置response参数，可以打开下载页面
			response.reset();

			String fileName = "lawFirm.xlsx";
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-disposition","attachment;filename="+URLEncoder.encode(fileName,"utf-8"));

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
			}catch(final IOException e){
				throw e;
			}
			finally{
				if(bis!=null) bis.close();
				if(bos!=null) bos.close();
			}
		}catch(Exception e){
			throw new Exception(e.getMessage());
		}
		
		return null;
	}
	
	private List<Map<String,Object>> createExcelRecord(List<PrpdLawFirmVo> results) {
		List<Map<String,Object>> listmap = new ArrayList<Map<String,Object>>();
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("sheetName","sheet1");
		listmap.add(map);
		for(PrpdLawFirmVo resultVo:results){
			Map<String,Object> mapValue = new HashMap<String,Object>();
			mapValue.put("LawFirmCode",resultVo.getLawFirmCode());
			mapValue.put("LawFirmName",resultVo.getLawFirmName());
			mapValue.put("LawFirmAddress",resultVo.getLawFirmAddress());
			mapValue.put("MobileNo",resultVo.getMobileNo());
			mapValue.put("Principal",resultVo.getPrincipal());
			mapValue.put("Contacts",resultVo.getContacts());
			listmap.add(mapValue);
		}
		return listmap;
	}
	
	/**
	 * 跳转到银行账户维护
	 * @return
	 */
	@RequestMapping("/lawPayCustomEdit.do")
	public ModelAndView lawAccountEdit(HttpServletRequest req){
		ModelAndView mv = new ModelAndView();
		
		SysUserVo userVo = WebUserUtils.getUser();
		String flag = "N";
		
		//银行数据
		Map<String, String> bankCodeMap = new HashMap<String, String>();
		List<PrplOldaccbankCodeVo> listVo = payCustomService.findPrplOldaccbankCodeByFlag("1");
		if(listVo != null && listVo.size() > 0){
			for(PrplOldaccbankCodeVo vo : listVo){
				bankCodeMap.put(vo.getBankCode(), vo.getBankName());
			}
		}
		mv.addObject("registNo", "0");   //收款人账号信息表 registNo字段 非空，这里统一给 0
		mv.addObject("bankCodeMap", bankCodeMap);
		mv.addObject("userVo", userVo);
		mv.addObject("flag", flag);
		mv.setViewName("payCustom/PayCustomEdit");
		return mv;
	}
	
	/**
	 * 收款人查询页面，在此页面可以选择带入收款人信息到收款人维护页面
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/payCustomSearchList.do")
	@ResponseBody
	public ModelAndView payCustomSearchList(HttpServletRequest request){
		ModelAndView mv = new ModelAndView();
		mv.setViewName("base/lawFirm/LawFirmPayCustomSearchList");
		return mv;
	}
	
	/**
	 * 打开维护银行信息
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/payCustomEdit.do")
	public ModelAndView intermediaryEdit(HttpServletRequest request) {
		String flag = "N";
		String payId = request.getParameter("payId");
		ModelAndView mv = new ModelAndView();
		PrpLPayCustomVo payCustomVo = new PrpLPayCustomVo();
		SysUserVo userVo = WebUserUtils.getUser();
		Long pid = null;
		if (StringUtils.isNotBlank(payId)) {
			pid = Long.parseLong(payId);
		}
		
		if (pid != null) {
			payCustomVo = payCustomService.findPayCustomVoById(pid);
		}
		logger.info("账号： " + payCustomVo.getAccountNo() + "账户名： " + payCustomVo.getPayeeName());

		if("N".equals(flag) || "S".equals(flag)){
			//将查出的数据放入Map中去
			Map<String,String> bankCodeMap = new HashMap<String,String>();
			List<PrplOldaccbankCodeVo> listVo=payCustomService.findPrplOldaccbankCodeByFlag("1");
			if(listVo!=null && listVo.size()>0){
				for(PrplOldaccbankCodeVo vo:listVo){
					bankCodeMap.put(vo.getBankCode(), vo.getBankName());
				}
			}
			mv.addObject("bankCodeMap",bankCodeMap);
		}
		mv.addObject("prpLPayCustom", payCustomVo);
		mv.addObject("flag", flag);
		mv.addObject("registNo", "0");
		mv.addObject("userVo",userVo);
		mv.setViewName("payCustom/PayCustomEdit");

		return mv;
	}


}
