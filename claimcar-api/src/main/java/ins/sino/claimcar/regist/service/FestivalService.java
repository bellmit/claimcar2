package ins.sino.claimcar.regist.service;

import ins.sino.claimcar.regist.vo.PrpLfestivalVo;

public interface FestivalService {
/**
 * 通过节日类型查询节假日信息表
 * @param fesvalType
 * @return
 */
public PrpLfestivalVo findPrpLfestivalVoByFestivalType(String festivalType,String year);
}
