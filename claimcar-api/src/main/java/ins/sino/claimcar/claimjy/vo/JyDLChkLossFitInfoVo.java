package ins.sino.claimcar.claimjy.vo;

import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * <pre></pre>
 * @author ★LiYi
 */
@XStreamAlias("LossFitInfo")
public class JyDLChkLossFitInfoVo implements Serializable {

	@XStreamImplicit(itemFieldName = "Item", keyFieldName = "Item")
	private List<JyDLChkLossFitInfoItemVo> itemVo;

	public List<JyDLChkLossFitInfoItemVo> getItemVo() {
		return itemVo;
	}

	public void setItemVo(List<JyDLChkLossFitInfoItemVo> itemVo) {
		this.itemVo = itemVo;
	}

	public JyDLChkLossFitInfoVo(){
		super();
	}

}
