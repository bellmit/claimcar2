package ins.sino.claimcar.claimjy.vo;

import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * <pre></pre>
 * @author ★LiYi
 */
@XStreamAlias("LossRepairInfo")
public class JyDLChkLossRepairInfoVo implements Serializable {

	@XStreamImplicit(itemFieldName = "Item", keyFieldName = "Item")
	private List<JyDLChkLossRepairInfoItemVo> itemVo;

	public List<JyDLChkLossRepairInfoItemVo> getItemVo() {
		return itemVo;
	}

	public void setItemVo(List<JyDLChkLossRepairInfoItemVo> itemVo) {
		this.itemVo = itemVo;
	}

	public JyDLChkLossRepairInfoVo(){
		super();
	}

}
