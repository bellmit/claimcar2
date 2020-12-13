package ins.sino.claimcar.interf.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/*减损描述列表*/
@XStreamAlias("SavingDescs")
public class SavingDescsVo {
	/*减损描述，可重复*/
	@XStreamAlias("SavingDesc")
	private String savingDesc;

	public String getSavingDesc() {
		return savingDesc;
	}

	public void setSavingDesc(String savingDesc) {
		this.savingDesc = savingDesc;
	}
	
}
