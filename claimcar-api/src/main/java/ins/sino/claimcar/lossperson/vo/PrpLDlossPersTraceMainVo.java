package ins.sino.claimcar.lossperson.vo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Custom VO class of PO PrpLDlossPersTraceMain
 */
public class PrpLDlossPersTraceMainVo extends PrpLDlossPersTraceMainVoBase implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	private List<PrpLDlossPersTraceVo> prpLDlossPersTraces = new ArrayList<PrpLDlossPersTraceVo>();

	private String sumReportFee;

	private String sumRealFee;

	private String sumDetractionFee;

	private String sumdefLoss;

	private String sumVeriReportFee;

	private String sumVeriRealFee;

	private String sumVeriDetractionFee;

	private String sumVeriDefloss;
	
	private BigDecimal assessorFee;

	public BigDecimal getAssessorFee() {
		return assessorFee;
	}

	public void setAssessorFee(BigDecimal assessorFee) {
		this.assessorFee = assessorFee;
	}

	public String getSumReportFee() {
		return sumReportFee;
	}

	public void setSumReportFee(String sumReportFee) {
		this.sumReportFee = sumReportFee;
	}

	public String getSumRealFee() {
		return sumRealFee;
	}

	public void setSumRealFee(String sumRealFee) {
		this.sumRealFee = sumRealFee;
	}

	public String getSumDetractionFee() {
		return sumDetractionFee;
	}

	public void setSumDetractionFee(String sumDetractionFee) {
		this.sumDetractionFee = sumDetractionFee;
	}

	public String getSumdefLoss() {
		return sumdefLoss;
	}

	public void setSumdefLoss(String sumdefLoss) {
		this.sumdefLoss = sumdefLoss;
	}

	public String getSumVeriReportFee() {
		return sumVeriReportFee;
	}

	public void setSumVeriReportFee(String sumVeriReportFee) {
		this.sumVeriReportFee = sumVeriReportFee;
	}

	public String getSumVeriRealFee() {
		return sumVeriRealFee;
	}

	public void setSumVeriRealFee(String sumVeriRealFee) {
		this.sumVeriRealFee = sumVeriRealFee;
	}

	public String getSumVeriDetractionFee() {
		return sumVeriDetractionFee;
	}

	public void setSumVeriDetractionFee(String sumVeriDetractionFee) {
		this.sumVeriDetractionFee = sumVeriDetractionFee;
	}

	public String getSumVeriDefloss() {
		return sumVeriDefloss;
	}

	public void setSumVeriDefloss(String sumVeriDefloss) {
		this.sumVeriDefloss = sumVeriDefloss;
	}

	public List<PrpLDlossPersTraceVo> getPrpLDlossPersTraces() {
		return prpLDlossPersTraces;
	}

	public void setPrpLDlossPersTraces(
			List<PrpLDlossPersTraceVo> prpLDlossPersTraces) {
		this.prpLDlossPersTraces = prpLDlossPersTraces;
	}
}
