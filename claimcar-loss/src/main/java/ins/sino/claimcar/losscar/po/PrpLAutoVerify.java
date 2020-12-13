package ins.sino.claimcar.losscar.po;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@TableGenerator(name = "utiSequence", table = "UTISEQUENCE", pkColumnName = "GenName", valueColumnName = "GenValue", pkColumnValue = "PK_PrpLAutoVerify", allocationSize = 10)
@Table(name = "PRPLAUTOVERIFY")
public class PrpLAutoVerify {

	private static final long serialVersionUID = 1L;
	private Long id;
	private String userCode;
	private String comCode;
	private String validFlag;
	private BigDecimal money;
	private String node;
	
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "utiSequence")   //自动生成主键
	@Column(name = "ID", unique = true, nullable = false, precision=13, scale=0)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@Column(name = "USERCODE", nullable = false, length=20)
	public String getUserCode() {
		return userCode;
	}
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}
	@Column(name = "COMCODE", nullable = false, length=10)
	public String getComCode() {
		return comCode;
	}
	public void setComCode(String comCode) {
		this.comCode = comCode;
	}
	@Column(name = "VALIDFLAG", nullable = false, length=2)
	public String getValidFlag() {
		return validFlag;
	}
	public void setValidFlag(String validFlag) {
		this.validFlag = validFlag;
	}
	@Column(name = "MONEY", nullable = false, precision=14)
	public BigDecimal getMoney() {
		return money;
	}
	public void setMoney(BigDecimal money) {
		this.money = money;
	}
	@Column(name = "NODE", nullable = false, length=30)
	public String getNode() {
		return node;
	}
	public void setNode(String node) {
		this.node = node;
	}
}
