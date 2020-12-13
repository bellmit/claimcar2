package ins.sino.claimcar.vat.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import ins.framework.common.ResultPage;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.claim.vo.PrpLChargeVo;
import ins.sino.claimcar.claim.vo.PrpLPaymentVo;
import ins.sino.claimcar.claim.vo.PrpLPrePayVo;
import ins.sino.claimcar.other.vo.AssessorQueryResultVo;
import ins.sino.claimcar.other.vo.AssessorQueryVo;
import ins.sino.claimcar.other.vo.PrpLAssessorFeeVo;
import ins.sino.claimcar.other.vo.PrpLCheckFeeVo;
import ins.sino.claimcar.vat.vo.PrpLbillcontVo;
import ins.sino.claimcar.vat.vo.PrpLbillinfoVo;
import ins.sino.claimcar.vat.vo.PrplAcbillcontVo;
import ins.sino.claimcar.vat.vo.VatQueryViewVo;

public interface BilllclaimService {
 /**
  * 保存或更新公估费与查勘费发票任务表
  * @param prplAcbillcontVo
  */
 public void saveOrUpdatePrplAcbillcont(PrplAcbillcontVo prplAcbillcontVo);
 /**
  * 通过发票Id查询公估费与查勘费发票任务表
  * @param billId
  * @return
  */
 public PrplAcbillcontVo findPrplAcbillcontVoByBillId(String billId);
 
 /**
  * 保存或更新批次-发票-计算书关系表
  * @param prpLbillcontVo
  */
 public void saveOrUpdatePrplbillcont(PrpLbillcontVo prpLbillcontVo);
 /**
  * 通过发票Id查询批次-发票-计算书关系表
  * @param billId
  * @return
  */
 public PrpLbillcontVo findPrpLbillcontVoByBillId(String billId);
 
 
 /**
  * 保存或更新发票信息表
  * @param prpLbillinfoVo
  */
 public void saveOrUpdatePrpLbillinfoVo(PrpLbillinfoVo prpLbillinfoVo);
 
 /**
  * 通过发票Id查询批次-发票-计算书关系表
  * @param billId
  * @return
  */
 public PrpLbillinfoVo findPrpLbillinfoVoByParams(String billNo,String billCode);
 /**
  * 通过vat发票Id查询发票信息
  * @param vatbillId
  * @return
  */
 public PrpLbillinfoVo findPrpLbillinfoVoByVatId(String vatbillId);
 /**
  *查询自己名下的待办任务数
  * @param comCode
  * @return
  */
 public int findRegisterTask(String comCode);
 /**
  * 发票计算书管理页面查询功能
  * @param queryVo
  * @return
  */
 public ResultPage<VatQueryViewVo> findBillPageForBillInfo(VatQueryViewVo queryVo);
 
 /**
  * 计算书与发票关联页面查询功能
  * @param queryVo
  * @return
  */
 public ResultPage<VatQueryViewVo> findVatPageForCompInfo(VatQueryViewVo queryVo);
 
 /**
  * 发票与计算书关联页面查询功能
  * @param queryVo
  * @return
  */
 public ResultPage<VatQueryViewVo> findVatPageForBillInfo(VatQueryViewVo queryVo);
 
 /**
  * 发票登记功能查询
  * @param queryVo
  * @return
  */
 public ResultPage<VatQueryViewVo> findVatPageForBillRegister(VatQueryViewVo queryVo);
 /**
  * 发票推送功能查询
  * @param queryVo
  * @return
  */
 public ResultPage<VatQueryViewVo> findVatPageForBillSend(VatQueryViewVo queryVo);
 /**
  * 发票登记信息查询
  * @param idsArray
  * @return
  */
 public List<VatQueryViewVo> findVatRgisterInfo(String[] idsArray);
 /**
  * 创建excle
  * @param vatQueryViewVos
  * @return
  */
 public List<Map<String,Object>> createExcelRecord(List<VatQueryViewVo> vatQueryViewVos);
 /**
  * excle--导入发票登记任务验证
  * @param objects
  * @param userVo
  * @return
  */
 public String applyBillTask(List<List<Object>> objects, SysUserVo userVo)throws Exception;
 /**
  * 通过id查询公估费与查勘费任务发票表
  * @param id
  * @return
  */
 public PrplAcbillcontVo findPrplAcbillcontById(Long id);
 
 /**
  * 通过id查询发票与计算书关系表
  * @param id
  * @return
  */
 public PrpLbillcontVo findPrpLbillcontById(Long id);
 
 /**
  * 通过id查询发票表
  * @param id
  * @return
  */
 public PrpLbillinfoVo findPrpLbillinfoById(Long id);
 
 /**
  * 通过id查询预付表
  * @param id
  * @return
  */
 public PrpLPrePayVo findPrpLPrePayById(Long id);
 
 /**
  * 通过id查询实赔赔款
  * @param id
  * @return
  */
 public PrpLPaymentVo findPrpLPaymentById(Long id);
 
 /**
  * 通过id查询实赔费用
  * @param id
  * @param num
  * @return
  */
 public PrpLChargeVo findPrpLChargeById(Long id);
 
 /**
  * 更新预付表
  * @param id
  * @param num
  * @return
  */
 public void updatePrpLPrePay(Long id,BigDecimal registernum,String bindFlag);
 
 /**
  * 更新实赔赔款
  * @param id
  * @param num
  * @return
  */
 public void updatePrpLPayment(Long id,BigDecimal registernum,String bindFlag);
 
 /**
  * 更新实赔费用
  * @param id
  * @return
  */
 public void updatePrpLCharge(Long id,BigDecimal registernum,String bindFlag);
 
 /**
  * 更新公估费发票业务号
  * @param id
  * @return
  */
 public void updatePrpLAssessorFee(String billNo,String billCode);
 
 /**
  * 更新查勘费发票业务号
  * @param id
  * @return
  */
 public void updatePrpLCheckFee(String billNo,String billCode);
 /**
  * 通过发票号与发票代码查询有效已登记的记录
  * @param billNo
  * @param BillCode
  * @return
  */
 public List<PrpLbillcontVo> findPrpLbillcontByBillNoAndBillCode(String billNo,String BillCode);
 /**
  * 通过任务号查询公估费或查勘费的关系的发票信息
  * @param taskNo
  * @return
  */
 public List<VatQueryViewVo> findPrpLbillinfoVoByTaskNo(String taskNo);
 
 /**
  * 通过发票号与发票代码与任务号(公估或查勘)查询有效已登记的记录
  * @param billNo
  * @param BillCode
  * @param taskNo
  * @return
  */
 public List<PrplAcbillcontVo> findPrplAcbillcontByBillNoAndBillCode(String billNo,String BillCode,String taskNo);
 /**
  * 根据linkBillNo查询公估费用表
  * @param linkBillNo
  * @return
  */
 public List<PrpLAssessorFeeVo> findPrpLAssessorFeeBylinkBillNo(String linkBillNo);
 
 /**
  * 根据linkBillNo查询查勘费用表
  * @param linkBillNo
  * @return
  */
 public List<PrpLCheckFeeVo> findPrpLCheckFeeBylinkBillNo(String linkBillNo);
 
 /**
  * 根据id查询公估费用表
  * @param id
  * @return
  */
 public PrpLAssessorFeeVo findPrpLAssessorFeeById(Long id);
 
 /**
  * 根据id查询查勘费用表
  * @param id
  * @return
  */
 public PrpLCheckFeeVo findPrpLCheckFeeById(Long id);
 
 
 /**
  * 更新公估费发票业务号
   * @param id
  * @param linkBillNo
  * @return
  */
 public void savePrpLAssessorFee(Long id,String linkBillNo);
 
 
 
 /**
  * 更新查勘费发票业务号
  * @param id
  * @param linkBillNo
  * @return
  */
 public void savePrpLCheckFee(Long id,String linkBillNo);
}
