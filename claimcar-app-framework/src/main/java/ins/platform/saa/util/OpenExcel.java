package ins.platform.saa.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

public class OpenExcel {
	/**
	 * 从第一行第一列读入excel文件
	 * @param fileName
	 *            文件路径及名称
	 * @param sheetNum
	 *            读入第几个sheet
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public List<Object[]> readExcel(FileInputStream file, int sheetNum) throws FileNotFoundException, IOException {
		return this.readExcel(file, sheetNum, 0, (short) 0);
	}

	/**
	 * 从指定的行和列读入excel文件
	 * @param fileName
	 *            文件路径及名称
	 * @param sheetNum
	 *            读入第几个sheet
	 * @param startRowNum
	 *            从第几行开始读入
	 * @param startCellNum
	 *            从第几列开始读入
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	@SuppressWarnings("deprecation")
	public List<Object[]> readExcel(FileInputStream file, int sheetNum, int startRowNum, int startCellNum)
			throws FileNotFoundException, IOException {
		// 根据excel文件创建一个POIFSFileSystem对象
		POIFSFileSystem fs = new POIFSFileSystem(file);
		// 创建一个HSSFWorkbook对象
		HSSFWorkbook wb = new HSSFWorkbook(fs);
		// 取得第sheetNum个sheet
		HSSFSheet sheet = wb.getSheetAt(sheetNum - 1);
		List<Object[]> rowList = new ArrayList<Object[]>(0);
		if (startRowNum == 0) {
			startRowNum = 1;
		}
		for (int m = startRowNum - 1; m < sheet.getLastRowNum() + 1; m++) {
			// 取得第m行
			HSSFRow row = sheet.getRow(m);
			if (row != null && row.getPhysicalNumberOfCells() > 0) {
				// 取得单元格
				startCellNum = startCellNum - 1;
				// if (startCellNum < row.getFirstCellNum()) {
				// startCellNum = row.getFirstCellNum();
				// }
				Object[] objs = new Object[row.getLastCellNum() - startCellNum];
				for (short n = (short) startCellNum; n < row.getLastCellNum(); n++) {
					HSSFCell cell = row.getCell(n);
					if (cell != null && !"".equals(cell.toString().trim())) {
						// 根据类型不同，使用不同的函数
						int celltype = cell.getCellType();
						if (celltype == HSSFCell.CELL_TYPE_NUMERIC) {
							objs[n - startCellNum] = new Double(cell.getNumericCellValue()).toString();
						} else if (celltype == HSSFCell.CELL_TYPE_STRING) {
							objs[n - startCellNum] = cell.getStringCellValue().trim();
						} else {
							objs[n - startCellNum] = "";
						}
					} else {
						objs[n - startCellNum] = "";
					}
				}
				++startCellNum;
				rowList.add(objs);
			}
		}
		wb.close();
		return rowList;
	}
}