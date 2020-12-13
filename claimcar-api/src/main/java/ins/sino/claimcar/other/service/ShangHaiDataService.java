package ins.sino.claimcar.other.service;


import java.util.Date;
import java.util.List;
import java.util.Map;

import ins.sino.claimcar.other.vo.ShangHaiDataVo;

/**
 * @author huanggs
 */
public interface ShangHaiDataService {
	
	/**
	 * 上海月数据统计
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List<String> findShangHaiCountData(Date startDate,Date endDate) throws Exception;
	
	/**
	 * 查询详细数据
	 * @param startDate
	 * @param endDate
	 * @param requestType
	 * @return
	 * @throws Exception
	 */
	public List<ShangHaiDataVo> getDetailByType(Date startDate, Date endDate, String requestType) throws Exception;
	
	/**
	 * 讲查询到的数据整理为方便写入Excel的数据
	 * @param results
	 * @param requestType
	 * @return
	 * @throws Exception
	 */
	public List<Map<String,Object>> createExcelRecord(List<ShangHaiDataVo> results,String requestType) throws Exception ;
	

}
