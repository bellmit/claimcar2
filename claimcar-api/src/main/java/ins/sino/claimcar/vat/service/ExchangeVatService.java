package ins.sino.claimcar.vat.service;


import java.util.Map;

import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.vat.vo.BillImageViewVo;
import ins.sino.claimcar.vat.vo.BindInvoiceReqDto;
import ins.sino.claimcar.vat.vo.ResmessageVo;
import ins.sino.claimcar.vat.vo.UntyingReqDtoVo;

public interface ExchangeVatService {
   /**
    * 发票信息推送vat接口
    * @param billImageReqVo
    * @param userVo
    * @return 0-表示失败，1-表示这个流程操作成功
    */
	public int sendBillImageToVat(BillImageViewVo billImageViewVo,SysUserVo userVo);
	/**
	 * 发票解绑申请接口
	 * @param untyingReqDtoVo
	 * @param userVo
	 * @return
	 */
	public ResmessageVo sendUntyingInfoToVat(UntyingReqDtoVo untyingReqDtoVo,SysUserVo userVo);
	/**
	 * 发票绑定接口
	 * @param bindInvoiceReqDto
	 * @param userVo
	 * @return
	 */
	public Map<String,String> sendBillRegisterToVat(BindInvoiceReqDto bindInvoiceReqDto,SysUserVo userVo);
}
