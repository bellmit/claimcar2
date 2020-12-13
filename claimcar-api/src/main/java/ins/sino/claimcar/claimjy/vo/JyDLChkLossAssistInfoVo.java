package ins.sino.claimcar.claimjy.vo;

import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * <pre></pre>
 * @author ★LiYi
 */
@XStreamAlias("LossAssistInfo")
public class JyDLChkLossAssistInfoVo implements Serializable {

	@XStreamImplicit(itemFieldName = "Item", keyFieldName = "Item")
	private List<JyDLChkLossAssistInfoItemVo> itemVo;

	public List<JyDLChkLossAssistInfoItemVo> getItemVo() {
		return itemVo;
	}

	public void setItemVo(List<JyDLChkLossAssistInfoItemVo> itemVo) {
		this.itemVo = itemVo;
	}

	public JyDLChkLossAssistInfoVo(){
		super();
	}

}
