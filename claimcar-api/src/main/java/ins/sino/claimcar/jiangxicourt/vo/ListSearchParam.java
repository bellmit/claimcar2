package ins.sino.claimcar.jiangxicourt.vo;

public class ListSearchParam implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private String FY_RWLSH;  //任务流水号
	private String BX_BDH;    //保单号
	
	public String getFY_RWLSH() {
		return FY_RWLSH;
	}
	public void setFY_RWLSH(String fY_RWLSH) {
		FY_RWLSH = fY_RWLSH;
	}
	public String getBX_BDH() {
		return BX_BDH;
	}
	public void setBX_BDH(String bX_BDH) {
		BX_BDH = bX_BDH;
	}
}
