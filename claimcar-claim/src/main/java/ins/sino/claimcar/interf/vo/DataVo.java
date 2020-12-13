package ins.sino.claimcar.interf.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Data")
public class DataVo {
	/*Key的默认值为Stage*/
	@XStreamAlias("Key")
	private String key;
	/*1-为报案，2-为查勘，3-定损*/
	@XStreamAlias("Value")
	private String value;
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	

}
