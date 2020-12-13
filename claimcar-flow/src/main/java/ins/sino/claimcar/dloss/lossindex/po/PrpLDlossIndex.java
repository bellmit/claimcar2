package ins.sino.claimcar.dloss.lossindex.po;

// Generated by Hibernate Tools 3.2.5 (sinosoft version), Don't modify!
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * POJO Class PrpLDlossIndex
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@TableGenerator(name = "utiSequence", table = "UTISEQUENCE", pkColumnName = "GenName", valueColumnName = "GenValue", pkColumnValue = "PRPLDLOSSINDEX_PK", allocationSize = 10)
@Table(name = "PRPLDLOSSINDEX")
public class PrpLDlossIndex implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private Long bussTaskId;
	private String registNo;
	private String nodeCode;
	private String nodeItem;
	private String handlerCode;
	private String handlerName;
	private String handlerIdNo;
	private String comCode;
	private Date defEndDate;
	private BigDecimal firstVeriLoss;
	private BigDecimal firstDefLoss;
	private String veripHandlerCode;
	private String veripHandlerName;
	private String veripCom;
	private Date veripEnddate;
	private BigDecimal sumVeripLoss;
	private String underWriteCode;
	private String underWriteName;
	private String underWiteIdNo;
	private String underWriteCom;
	private Date underWriteEndDate;
	private BigDecimal sumVeriLossFee;
	private BigDecimal preSumFee;
	private Integer quantity;

	@Id
	@GeneratedValue(strategy=GenerationType.TABLE,generator="utiSequence" )
	@Column(name = "ID", unique = true, nullable = false, precision=12, scale=0)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "BUSSTASKID", nullable = false, precision=12, scale=0)
	public Long getBussTaskId() {
		return this.bussTaskId;
	}

	public void setBussTaskId(Long bussTaskId) {
		this.bussTaskId = bussTaskId;
	}

	@Column(name = "REGISTNO", nullable = false, length=25)
	public String getRegistNo() {
		return this.registNo;
	}

	public void setRegistNo(String registNo) {
		this.registNo = registNo;
	}

	@Column(name = "NODECODE", nullable = false, length=30)
	public String getNodeCode() {
		return this.nodeCode;
	}

	public void setNodeCode(String nodeCode) {
		this.nodeCode = nodeCode;
	}

	@Column(name = "NODEITEM", length=30)
	public String getNodeItem() {
		return this.nodeItem;
	}

	public void setNodeItem(String nodeItem) {
		this.nodeItem = nodeItem;
	}

	@Column(name = "HANDLERCODE", length=10)
	public String getHandlerCode() {
		return this.handlerCode;
	}

	public void setHandlerCode(String handlerCode) {
		this.handlerCode = handlerCode;
	}

	@Column(name = "HANDLERNAME", length=30)
	public String getHandlerName() {
		return this.handlerName;
	}

	public void setHandlerName(String handlerName) {
		this.handlerName = handlerName;
	}

	@Column(name = "HANDLERIDNO", length=20)
	public String getHandlerIdNo() {
		return this.handlerIdNo;
	}

	public void setHandlerIdNo(String handlerIdNo) {
		this.handlerIdNo = handlerIdNo;
	}

	@Column(name = "COMCODE", length=10)
	public String getComCode() {
		return this.comCode;
	}

	public void setComCode(String comCode) {
		this.comCode = comCode;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DEFENDDATE", length=7)
	public Date getDefEndDate() {
		return this.defEndDate;
	}

	public void setDefEndDate(Date defEndDate) {
		this.defEndDate = defEndDate;
	}

	@Column(name = "FIRSTVERILOSS", precision=14)
	public BigDecimal getFirstVeriLoss() {
		return this.firstVeriLoss;
	}

	public void setFirstVeriLoss(BigDecimal firstVeriLoss) {
		this.firstVeriLoss = firstVeriLoss;
	}

	@Column(name = "FIRSTDEFLOSS", precision=14)
	public BigDecimal getFirstDefLoss() {
		return this.firstDefLoss;
	}

	public void setFirstDefLoss(BigDecimal firstDefLoss) {
		this.firstDefLoss = firstDefLoss;
	}

	@Column(name = "VERIPHANDLERCODE", length=10)
	public String getVeripHandlerCode() {
		return this.veripHandlerCode;
	}

	public void setVeripHandlerCode(String veripHandlerCode) {
		this.veripHandlerCode = veripHandlerCode;
	}

	@Column(name = "VERIPHANDLERNAME", length=30)
	public String getVeripHandlerName() {
		return this.veripHandlerName;
	}

	public void setVeripHandlerName(String veripHandlerName) {
		this.veripHandlerName = veripHandlerName;
	}

	@Column(name = "VERIPCOM", length=8)
	public String getVeripCom() {
		return this.veripCom;
	}

	public void setVeripCom(String veripCom) {
		this.veripCom = veripCom;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "VERIPENDDATE", length=7)
	public Date getVeripEnddate() {
		return this.veripEnddate;
	}

	public void setVeripEnddate(Date veripEnddate) {
		this.veripEnddate = veripEnddate;
	}

	@Column(name = "SUMVERIPLOSS", precision=14)
	public BigDecimal getSumVeripLoss() {
		return this.sumVeripLoss;
	}

	public void setSumVeripLoss(BigDecimal sumVeripLoss) {
		this.sumVeripLoss = sumVeripLoss;
	}

	@Column(name = "UNDERWRITECODE", length=10)
	public String getUnderWriteCode() {
		return this.underWriteCode;
	}

	public void setUnderWriteCode(String underWriteCode) {
		this.underWriteCode = underWriteCode;
	}

	@Column(name = "UNDERWRITENAME", length=30)
	public String getUnderWriteName() {
		return this.underWriteName;
	}

	public void setUnderWriteName(String underWriteName) {
		this.underWriteName = underWriteName;
	}

	@Column(name = "UNDERWITEIDNO", length=20)
	public String getUnderWiteIdNo() {
		return this.underWiteIdNo;
	}

	public void setUnderWiteIdNo(String underWiteIdNo) {
		this.underWiteIdNo = underWiteIdNo;
	}

	@Column(name = "UNDERWRITECOM", length=10)
	public String getUnderWriteCom() {
		return this.underWriteCom;
	}

	public void setUnderWriteCom(String underWriteCom) {
		this.underWriteCom = underWriteCom;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UNDERWRITEENDDATE", length=7)
	public Date getUnderWriteEndDate() {
		return this.underWriteEndDate;
	}

	public void setUnderWriteEndDate(Date underWriteEndDate) {
		this.underWriteEndDate = underWriteEndDate;
	}

	@Column(name = "SUMVERILOSSFEE", precision=14)
	public BigDecimal getSumVeriLossFee() {
		return this.sumVeriLossFee;
	}

	public void setSumVeriLossFee(BigDecimal sumVeriLossFee) {
		this.sumVeriLossFee = sumVeriLossFee;
	}

	@Column(name = "PRESUMFEE", precision=14)
	public BigDecimal getPreSumFee() {
		return preSumFee;
	}

	
	public void setPreSumFee(BigDecimal preSumFee) {
		this.preSumFee = preSumFee;
	}

	@Column(name = "QUANTITY")
	public Integer getQuantity() {
		return quantity;
	}

	
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	
	
}