package ins.sino.claimcar.jiangxicourt.vo;

import java.math.BigDecimal;
import java.util.Date;


public class Data implements java.io.Serializable{

	private static final long serialVersionUID = 1L;
	private String FY_RWLSH;          //流水号
	private String BX_BDH;            //保单号
	private Integer BX_LPQK_SFYJ;     //理赔事项是否已决；1：是，2：否
	private String BX_LPQK_PFSJ;        //赔付时间
	private BigDecimal BX_LPQK_PFJE;  //赔付金额
	private String BX_LPQK_ZFFS;      //支付方式
	private String BX_LPQK_KHYH;      //开户银行
	private String BX_LPQK_KHHZH;     //开户行账号
	private String BX_XTMC;           //系统名称
	
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
	public Integer getBX_LPQK_SFYJ() {
		return BX_LPQK_SFYJ;
	}
	public void setBX_LPQK_SFYJ(Integer bX_LPQK_SFYJ) {
		BX_LPQK_SFYJ = bX_LPQK_SFYJ;
	}
	public String getBX_LPQK_PFSJ() {
		return BX_LPQK_PFSJ;
	}
	public void setBX_LPQK_PFSJ(String bX_LPQK_PFSJ) {
		BX_LPQK_PFSJ = bX_LPQK_PFSJ;
	}
	public BigDecimal getBX_LPQK_PFJE() {
		return BX_LPQK_PFJE;
	}
	public void setBX_LPQK_PFJE(BigDecimal bX_LPQK_PFJE) {
		BX_LPQK_PFJE = bX_LPQK_PFJE;
	}
	public String getBX_LPQK_ZFFS() {
		return BX_LPQK_ZFFS;
	}
	public void setBX_LPQK_ZFFS(String bX_LPQK_ZFFS) {
		BX_LPQK_ZFFS = bX_LPQK_ZFFS;
	}
	public String getBX_LPQK_KHYH() {
		return BX_LPQK_KHYH;
	}
	public void setBX_LPQK_KHYH(String bX_LPQK_KHYH) {
		BX_LPQK_KHYH = bX_LPQK_KHYH;
	}
	public String getBX_LPQK_KHHZH() {
		return BX_LPQK_KHHZH;
	}
	public void setBX_LPQK_KHHZH(String bX_LPQK_KHHZH) {
		BX_LPQK_KHHZH = bX_LPQK_KHHZH;
	}
	public String getBX_XTMC() {
		return BX_XTMC;
	}
	public void setBX_XTMC(String bX_XTMC) {
		BX_XTMC = bX_XTMC;
	}

	

	
	
	
}
