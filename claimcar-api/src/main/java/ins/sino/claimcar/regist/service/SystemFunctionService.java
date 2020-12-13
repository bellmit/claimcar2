package ins.sino.claimcar.regist.service;

import ins.framework.common.ResultPage;
import ins.sino.claimcar.flow.vo.PrpLWfTaskQueryVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;

public interface SystemFunctionService {
	/**
	 * 查询河南快赔解锁案件
	 * @param taskQueryVo
	 * @param start
	 * @param length
	 * @return
	 */
	public ResultPage<PrpLRegistVo> findRegistPage(PrpLWfTaskQueryVo taskQueryVo,int start,int length )throws Exception;
	/**
	 * 更新报案表河南快赔标志位
	 * @param registNo
	 */
	public void updateIsQuickCase(String registNo)throws Exception;

}
