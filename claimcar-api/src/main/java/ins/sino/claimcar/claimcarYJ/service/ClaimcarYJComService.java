package ins.sino.claimcar.claimcarYJ.service;

import ins.sino.claimcar.claimcarYJ.vo.PrpLyjlosscarCompVo;
import ins.sino.claimcar.claimcarYJ.vo.PrplyjPartoffersVo;

public interface ClaimcarYJComService {
  /**
   * 阳杰询价通知信息保存
   * @param prpLyjlosscarCompVo
   */
public void savePrpLyjlosscarComp(PrpLyjlosscarCompVo prpLyjlosscarCompVo);
/**
 * 通过配件唯一Id获取阳杰配件表信息
 * @param partenquiryId
 * @return
 */
public PrplyjPartoffersVo findPrplyjPartoffersBypartId(String partenquiryId);

/**
 * 通过阳杰询价车辆Id查询阳杰配件主表
 * @param carId
 * @return
 */
public PrpLyjlosscarCompVo findPrpLyjlosscarCompBypartId(String carId);


/**
 * 阳杰询价通知信息更新
 * @param prpLyjlosscarCompVo
 */
public void updatePrpLyjlosscarComp(PrpLyjlosscarCompVo prpLyjlosscarCompVo);

	
}
