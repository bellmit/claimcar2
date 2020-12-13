package ins.sino.claimcar.invoice.vo;

import java.util.List;

import com.sinosoft.arch5service.dto.prpcar002.BasePartPrpcar002;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("Body")
public class BodyReceiptTask implements java.io.Serializable{


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@XStreamImplicit(itemFieldName = "BasePart")
	private List<BasePartReceiptTask> baseParts;

	public List<BasePartReceiptTask> getBaseParts() {
		return baseParts;
	}

	public void setBaseParts(List<BasePartReceiptTask> baseParts) {
		this.baseParts = baseParts;
	}


}
