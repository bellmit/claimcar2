package ins.sino.claimcar.claimjy.vo;

import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * <pre></pre>
 * @author ★LiYi
 */
@XStreamAlias("LossOuterRepairInfo")
public class JyDLChkLossOuterRepairInfoVo implements Serializable {

	@XStreamImplicit(itemFieldName = "Item", keyFieldName = "Item")
	private List<JyDLChkLossOuterRepairInfoItemVo> itemVo;

	public List<JyDLChkLossOuterRepairInfoItemVo> getItemVo() {
		return itemVo;
	}

	public void setItemVo(List<JyDLChkLossOuterRepairInfoItemVo> itemVo) {
		this.itemVo = itemVo;
	}

	public JyDLChkLossOuterRepairInfoVo(){
		super();
	}

}
