package ins.sino.claimcar.claimjy.vo;

import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * <pre></pre>
 * @author ★LiYi
 */
@XStreamAlias("LossRepairSumInfo")
public class JyDLChkLossRepairSumInfoVo implements Serializable {

	@XStreamImplicit(itemFieldName = "Item", keyFieldName = "Item")
	private List<JyDLChkLossRepairSumInfoItemVo> itemVo;

	public List<JyDLChkLossRepairSumInfoItemVo> getItemVo() {
		return itemVo;
	}

	public void setItemVo(List<JyDLChkLossRepairSumInfoItemVo> itemVo) {
		this.itemVo = itemVo;
	}

	public JyDLChkLossRepairSumInfoVo(){
		super();
	}

}
