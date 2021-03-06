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
	 * ?????????????????????
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
	
	//??????excel
		@RequestMapping(value = "/importExcel.do",method = RequestMethod.POST)
		@ResponseBody
			public ModelAndView importExcel(@RequestParam("importFile") CommonsMultipartFile files,HttpServletRequest request) 
					throws IOException, BiffException, ParseException {
//			    SimpleDateFormat sp=new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
//			    String CreateUser =request.getParameter("CreateUser");
			
			    //????????????
			    String flag = checkFile(files);
			    String state = null;
		    	ModelAndView mav = new ModelAndView();
		    	Workbook book = null;
		    	if(flag.equals("1")){
	    		    //???????????????
	    		    String fileName = files.getOriginalFilename();
		        	InputStream in = files.getInputStream();		        		
	                //??????????????????????????????????????????
		        	if(fileName.endsWith("xls")){
		        		book = new HSSFWorkbook(in);		        		
		        	}else if(fileName.endsWith("xlsx")){
		        		book = new XSSFWorkbook(in);
		        	}
		        	if(book != null){
		        		//??????excel?????????????????????
		        		for(int sheetNum = 0; sheetNum < book.getNumberOfSheets(); sheetNum++){
		        			Sheet sheet = book.getSheetAt(sheetNum);
		        			if(sheet == null){
		        				continue;
		        			}
		        			int firstRowNum = sheet.getFirstRowNum(); //????????????sheet????????????
		        			int lastRowNum = sheet.getLastRowNum();   //????????????sheet????????????
		        			List<PrpdLawFirmVo> dtoList = new ArrayList<PrpdLawFirmVo>();
		        			//???????????????????????????????????????
		        			for(int rowNum = firstRowNum + 1 ; rowNum <= lastRowNum ; rowNum++){
		        				Row row = sheet.getRow(rowNum);      //???????????????
		        				if(row == null){
		        					continue;
		        				}
		        				int firstCellNum = row.getFirstCellNum(); //???????????????????????????
		        				int lastCellNum = row.getPhysicalNumberOfCells(); //????????????????????????
		        				//????????????????????????????????????????????????
		        				String[] cells = new String[row.getPhysicalNumberOfCells()];
		        				Cell[] cellss = new Cell[lastCellNum];
		        				//??????????????????????????????????????????????????????
		        				for(int cellNum = firstCellNum ; cellNum < lastCellNum ; cellNum++){
		    		 				Cell cell = row.getCell(cellNum);
		    		 				cells[cellNum] = getCellValue(cell);
		    		 				cellss[cellNum] = row.getCell(cellNum);
		        				}
		        				
		        				PrpdLawFirmVo dto = new PrpdLawFirmVo();
		        				// ??????excel????????????,??????????????????
		        				dto.setLawFirmCode(cells[0]);     //?????????????????????
		        				dto.setLawFirmName(cells[1]);     //?????????????????????
		        				dto.setLawFirmAddress(cells[2]);  //?????????????????????
		        				dto.setMobileNo(cells[3]);        //?????????????????????
		        				dto.setPrincipal(cells[4]);       //?????????
		        				dto.setContacts(cells[5]);        //?????????
		        				dtoList.add(dto);
		        			}
		        			//?????????????????????????????????
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
		    	
		    	book.close();// ?????????????????????Excel
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
	 * ??????????????????????????????excel??????
	 * @param file
	 * @throws IOException
	 */
	public static String checkFile(CommonsMultipartFile file) throws IOException{

		//???????????????
		String fileName = file.getOriginalFilename();
		if(!fileName.endsWith("xls") && !fileName.endsWith("xlsx")){
			logger.info(fileName + " ??????excel?????????");
			return "7";
		}
		return "1";
	}
	
	/**
	 * ??????????????????????????????????????????String??????
	 * @param cell
	 * @return
	 */
	public static String getCellValue(Cell cell){  
        String cellValue = "";  
        if(cell == null){  
            return cellValue;  
        }  
        //???????????????String?????????????????????1??????1.0?????????  
        if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC){  
            cell.setCellType(Cell.CELL_TYPE_STRING);  
        }  
        //?????????????????????  
        switch (cell.getCellType()){  
            case Cell.CELL_TYPE_NUMERIC: //??????  
                cellValue = String.valueOf(cell.getNumericCellValue());  
                break;  
            case Cell.CELL_TYPE_STRING: //?????????  
                cellValue = String.valueOf(cell.getStringCellValue());  
                break;  
            case Cell.CELL_TYPE_BOOLEAN: //Boolean  
                cellValue = String.valueOf(cell.getBooleanCellValue());  
                break;  
            case Cell.CELL_TYPE_FORMULA: //??????  
                cellValue = String.valueOf(cell.getCellFormula());  
                break;  
            case Cell.CELL_TYPE_BLANK: //??????   
                cellValue = "";  
                break;  
            case Cell.CELL_TYPE_ERROR: //??????  
                cellValue = "????????????";  
                break;  
            default:  
                cellValue = "????????????";  
                break;  
        }  
        return cellValue;  
    }  

	
	//??????excel		
	@RequestMapping(value = "/exportExcel.do")
	@ResponseBody
	public ModelAndView exportExcel(HttpServletResponse response) throws Exception {
		try{
			List<PrpdLawFirmVo> lawfirmList = lawFirmService.findAllPrpdLawFirm();
			
			//??????????????????????????????????????????????????????????????????????????????
			if(lawfirmList.size() > 1000){
				response.setCharacterEncoding("utf-8");
				response.setContentType("Text/plain");
				PrintWriter out = response.getWriter();
				out.print("?????????????????????1000??????????????????????????????");
				out.close();
				return null;
			}
			List<Map<String,Object>> list = createExcelRecord(lawfirmList);
			String fileDir = "ins/sino/claimcar/other/files/lawFirm.xlsx";
			String keys[] = {"LawFirmCode","LawFirmName","LawFirmAddress","MobileNo","Principal","Contacts"};// map??????key
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
			// ??????response?????????????????????????????????
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
	 * ???????????????????????????
	 * @return
	 */
	@RequestMapping("/lawPayCustomEdit.do")
	public ModelAndView lawAccountEdit(HttpServletRequest req){
		ModelAndView mv = new ModelAndView();
		
		SysUserVo userVo = WebUserUtils.getUser();
		String flag = "N";
		
		//????????????
		Map<String, String> bankCodeMap = new HashMap<String, String>();
		List<PrplOldaccbankCodeVo> listVo = payCustomService.findPrplOldaccbankCodeByFlag("1");
		if(listVo != null && listVo.size() > 0){
			for(PrplOldaccbankCodeVo vo : listVo){
				bankCodeMap.put(vo.getBankCode(), vo.getBankName());
			}
		}
		mv.addObject("registNo", "0");   //???????????????????????? registNo?????? ???????????????????????? 0
		mv.addObject("bankCodeMap", bankCodeMap);
		mv.addObject("userVo", userVo);
		mv.addObject("flag", flag);
		mv.setViewName("payCustom/PayCustomEdit");
		return mv;
	}
	
	/**
	 * ?????????????????????????????????????????????????????????????????????????????????????????????
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
	 * ????????????????????????
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
		logger.info("????????? " + payCustomVo.getAccountNo() + "???????????? " + payCustomVo.getPayeeName());

		if("N".equals(flag) || "S".equals(flag)){
			//????????????????????????Map??????
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
