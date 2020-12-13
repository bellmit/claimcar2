package ins.sino.claimcar.claimjy.vo.repair;

import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
@XStreamAlias("BODY")
public class JyFactoryBrandAndInfoBodyVo implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@XStreamAlias("FactoryBrand")
	private List<Item> item;
	@XStreamAlias("FactoryInfo")
	private FactoryInfo factoryInfo;
	
	
	public List<Item> getItem() {
		return item;
	}
	public void setItem(List<Item> item) {
		this.item = item;
	}
	public FactoryInfo getFactoryInfo() {
		return factoryInfo;
	}
	public void setFactoryInfo(FactoryInfo factoryInfo) {
		this.factoryInfo = factoryInfo;
	}

}
