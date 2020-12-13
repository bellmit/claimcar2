package ins.sino.claimcar.vat.vo;

import java.io.Serializable;
import java.util.List;

public class UntyingResDtoVo implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String resultCode;//处理结果
	private String resultMsg;//处理结果描述
	private List<UntyingInvoiceResDtoVo> untyingInvoiceDtoList;//vat发票id信息集合
	public String getResultCode() {
		return resultCode;
	}
	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}
	public String getResultMsg() {
		return resultMsg;
	}
	public void setResultMsg(String resultMsg) {
		this.resultMsg = resultMsg;
	}
	public List<UntyingInvoiceResDtoVo> getUntyingInvoiceDtoList() {
		return untyingInvoiceDtoList;
	}
	public void setUntyingInvoiceDtoList(
			List<UntyingInvoiceResDtoVo> untyingInvoiceDtoList) {
		this.untyingInvoiceDtoList = untyingInvoiceDtoList;
	}
	
	
}
