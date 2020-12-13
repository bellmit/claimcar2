/******************************************************************************
* CREATETIME : 2016年8月19日 下午7:09:10
******************************************************************************/
package ins.platform.utils;


import ins.platform.saa.util.CodeConstants;

import java.io.ByteArrayOutputStream;
import java.io.File;  
import java.io.FileInputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;  
  
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.ss.usermodel.Cell;  
import org.apache.poi.ss.usermodel.CellStyle;  
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;  
import org.apache.poi.ss.usermodel.Sheet;  
import org.apache.poi.ss.usermodel.Workbook;

/**
 * @author ★XMSH
 */
public class ExportExcelUtils {

	private static final String PASSWD="123";
	/** 
     * 描述：根据文件路径获取项目中的文件 
     * @param fileDir 文件路径 
     * @return 
     * @throws Exception 
     */  
    public static File getExcelDemoFile(String fileDir) throws Exception{  
        String classDir = null;  
        String fileBaseDir = null;  
        File file = null;  
        classDir = "/"+Thread.currentThread().getContextClassLoader().getResource("/").getPath();  
        fileBaseDir = classDir.substring(1);  
        String filePath = fileBaseDir+fileDir;
        file = new File(filePath);  
        if(!file.exists()){  
            throw new Exception("模板文件不存在！"+filePath);  
        }  
        return file;  
    }  
      
    public static Workbook writeNewExcel(File file,String sheetName,List<Map<String, Object>> list,String []keys,String flags) throws Exception{  
        Workbook wb = null;  

        FileInputStream fis = new FileInputStream(file);  
        wb = new ImportExcelUtil().getWorkbook(fis, file.getName());    //获取工作薄  
        Sheet sheet = wb.getSheet(sheetName);  
        Row row0 = sheet.getRow(0);

        if(CodeConstants.VALID.equals(flags)){
        	sheet.protectSheet(PASSWD);
        }
//        //循环插入数据  
//        CellStyle cs = setSimpleCellStyle(wb);    //Excel单元格样式  
      //设置每行每列的值
        for (short i = 1; i < list.size(); i++) {
            // Row 行,Cell 方格 , Row 和 Cell 都是从0开始计数的
            // 创建一行，在页sheet上
            Row row1 = sheet.createRow((short) i);
            row1.setRowStyle(row0.getRowStyle());
            // 在row行上创建一个方格
            for(short j=0;j<keys.length;j++){
                Cell cell = row1.createCell(j);
                CellStyle cs = setCellStyle(wb,row0.getCell(j).getCellStyle());
                if(CodeConstants.VALID.equals(flags)){
                	if(j==11){
                    	cs.setLocked(false);
                    }
                }
                cell.setCellStyle(cs);
                cell.getCellStyle().setFillBackgroundColor(IndexedColors.WHITE.getIndex());
                Object object = list.get(i).get(keys[j]);
            	cell.setCellValue(( object == null?" ": object.toString() ));
            }
        }
        return wb;  
    }  
      
    /** 
     * 描述：设置简单的Cell样式 
     * @return 
     */  
    public static CellStyle setCellStyle(Workbook wb,CellStyle cs){  
          
    	CellStyle cellStyle = wb.createCellStyle();
    	cellStyle.cloneStyleFrom(cs);
    	cellStyle.setFillBackgroundColor(IndexedColors.WHITE.getIndex());
        return cellStyle;
    }  
    
    /**
     * 提取上海月数据
     * @param wb
     * @param sheetName
     * @param list
     * @param keys
     * @throws Exception
     */
    public static void writeNewExcelForShanghai(Workbook wb ,String sheetName,List<Map<String, Object>> list,String[] keys) throws Exception{  
    	//获取工作薄  
        Sheet sheet = wb.getSheet(sheetName);  
        Row row0 = sheet.getRow(0);
        Map<String,CellStyle> styleMap = new HashMap<String, CellStyle>();
        for(int n = 0 ; n < keys.length; n ++){
        	CellStyle cs = row0.getCell(n).getCellStyle();
        	Font font = wb.createFont();    
            font.setFontName("宋体");    
            font.setFontHeightInPoints((short) 9);
            font.setBold(false);
            cs.setFont(font);
        	styleMap.put(keys[n],cs );
        }
        //设置每行每列的值
        for (short i = 1; i < list.size(); i++) {
            // Row 行,Cell 方格 , Row 和 Cell 都是从0开始计数的
            // 创建一行，在页sheet上
            Row row1 = sheet.createRow((short) i);
            // 在row行上创建一个方格
            for(short j=0;j<keys.length;j++){
                Cell cell = row1.createCell(j);
                cell.setCellStyle(styleMap.get(keys[j]));
                cell.getCellStyle().setFillBackgroundColor(IndexedColors.WHITE.getIndex());
                Object object = list.get(i).get(keys[j]);
            	cell.setCellValue(( object == null?" ": object.toString() ));
            }
        }
    }  
    
    /**
     * 向指定单元格写入
     * @param wb
     * @param sheetName
     * @param rowNo
     * @param columnNo
     * @param value
     */
    public static void writeToCell(Workbook wb, String sheetName ,int rowNo,int columnNo,String value){
    	Sheet sheet = wb.getSheet(sheetName);  
    	Row row = sheet.getRow(rowNo);
    	Cell cell = row.createCell(columnNo);
    	CellStyle cs = setCellStyle(wb,row.getCell(columnNo).getCellStyle());
    	cs.setAlignment(HSSFCellStyle.ALIGN_CENTER);
    	cell.setCellStyle(cs);
    	cell.getCellStyle().setFillBackgroundColor(IndexedColors.WHITE.getIndex());
    	cell.setCellValue(value);
    }
    
    /**
     * 发票导出专用
     * @param file
     * @param sheetName
     * @param list
     * @param keys
     * @param flags
     * @return
     * @throws Exception
     */
    public static Workbook writeNewBillExcel(File file,String sheetName,List<Map<String, Object>> list,String []keys,String flags) throws Exception{  
        Workbook wb = null;  

        FileInputStream fis = new FileInputStream(file);  
        wb = new ImportExcelUtil().getWorkbook(fis, file.getName());    //获取工作薄  
        Sheet sheet = wb.getSheet(sheetName);  
        Row row0 = sheet.getRow(0);

        if(CodeConstants.VALID.equals(flags)){
        	sheet.protectSheet(PASSWD);
        }
//        //循环插入数据  
//        CellStyle cs = setSimpleCellStyle(wb);    //Excel单元格样式  
      //设置每行每列的值
        for (short i = 1; i < list.size(); i++) {
            // Row 行,Cell 方格 , Row 和 Cell 都是从0开始计数的
            // 创建一行，在页sheet上
            Row row1 = sheet.createRow((short) i);
            row1.setRowStyle(row0.getRowStyle());
            // 在row行上创建一个方格
            for(short j=0;j<keys.length;j++){
                Cell cell = row1.createCell(j);
                CellStyle cs = setCellStyle(wb,row0.getCell(j).getCellStyle());
                if(CodeConstants.VALID.equals(flags)){
                	if(j==10){
                    	cs.setLocked(false);
                    }
                }
                cell.setCellStyle(cs);
                cell.getCellStyle().setFillBackgroundColor(IndexedColors.WHITE.getIndex());
                Object object = list.get(i).get(keys[j]);
            	cell.setCellValue(( object == null?" ": object.toString() ));
            }
        }
        return wb;  
    }  
}
