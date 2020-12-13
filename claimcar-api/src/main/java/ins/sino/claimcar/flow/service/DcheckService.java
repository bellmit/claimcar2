package ins.sino.claimcar.flow.service;

import java.util.List;
import java.util.Map;

import ins.framework.common.ResultPage;
import ins.sino.claimcar.flow.vo.PrpLWfTaskQueryVo;
import ins.sino.claimcar.flow.vo.WfTaskQueryResultVo;

public interface DcheckService {
	
 public List<WfTaskQueryResultVo> search(PrpLWfTaskQueryVo taskQueryVo,String comCode,int start, int length)throws Exception;
 
 public List<WfTaskQueryResultVo> dcheckDatas(PrpLWfTaskQueryVo taskQueryVo,String comCode)throws Exception;
 
 public List<Map<String,Object>> createExcelRecord(List<WfTaskQueryResultVo> resultList)throws Exception;
}
