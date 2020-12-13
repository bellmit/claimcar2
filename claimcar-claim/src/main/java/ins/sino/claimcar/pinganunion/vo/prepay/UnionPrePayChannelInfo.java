package ins.sino.claimcar.pinganunion.vo.prepay;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;


import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 联盟中心响应数据-预赔对象
 *
 * @author mfn
 * @date 2020/7/20 18:39
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UnionPrePayChannelInfo implements Serializable {
    private static final long serialVersionUID = -8373928344591176221L;

    /**  预赔主表ID(关联预赔主表)    是否非空：Y  编码：N */
    private String idClmPrepayInfo;
    /**  通道号    是否非空：Y  编码：N */
    private String idClmChannelProcess;
    /**  通道任务类型  代码定义3.15  是否非空：Y  编码：Y */
    private String lossType;
    /**  通道预赔金额  医疗费直付人伤专用字段  是否非空：N  编码：N */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private BigDecimal payAmount;
    /**  医疗项下损失金额    是否非空：N  编码：N */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private BigDecimal medLoss;
    /**  死亡伤残项下损失金额    是否非空：N  编码：N */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private BigDecimal deathLoss;
    /**  人伤责任系数    是否非空：N  编码：N */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private BigDecimal dutyCoefficient;
    /**  分摊比例_分子    是否非空：N  编码：N */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private BigDecimal rateMolecular;
    /**  分摊比例_分母    是否非空：N  编码：N */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private BigDecimal rateDenominator;
    /**  医疗项下其它交强险承担金额    是否非空：N  编码：N */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private BigDecimal otherForceMedFee;
    /**  死亡伤残项下其它交强险承担金额    是否非空：N  编码：N */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private BigDecimal otherForceDeathFee;
    /**  是否按单人交强险医疗限额赔付  Y 按照限额赔付  N不按照限额赔付  是否非空：N  编码：Y */
    private String isForceDeathLimit;
    /**  是否按单人交强险死亡伤残限额赔付  Y 按照限额赔付  N不按照限额赔付  是否非空：N  编码：Y */
    private String isForceMedLimit;
    /**  单人交强险医疗限额    是否非空：N  编码：N */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private BigDecimal forceMedLimit;
    /**  单人交强险死亡伤残限额    是否非空：N  编码：N */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private BigDecimal forceDeathLimit;
    /**  车物发票损失金额  车物通道专用字段  是否非空：N  编码：N */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private BigDecimal lossAmount;
    /**  车物发票施救费金额  车物通道专用字段  是否非空：N  编码：N */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private BigDecimal rescueAmount;
    /**  修理厂编码  车物通道专用字段  是否非空：N  编码：N */
    private String garageCode;

    public String getIdClmPrepayInfo() {
        return idClmPrepayInfo;
    }

    public void setIdClmPrepayInfo(String idClmPrepayInfo) {
        this.idClmPrepayInfo = idClmPrepayInfo;
    }

    public String getIdClmChannelProcess() {
        return idClmChannelProcess;
    }

    public void setIdClmChannelProcess(String idClmChannelProcess) {
        this.idClmChannelProcess = idClmChannelProcess;
    }

    public String getLossType() {
        return lossType;
    }

    public void setLossType(String lossType) {
        this.lossType = lossType;
    }

    public BigDecimal getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(BigDecimal payAmount) {
        this.payAmount = payAmount;
    }

    public BigDecimal getMedLoss() {
        return medLoss;
    }

    public void setMedLoss(BigDecimal medLoss) {
        this.medLoss = medLoss;
    }

    public BigDecimal getDeathLoss() {
        return deathLoss;
    }

    public void setDeathLoss(BigDecimal deathLoss) {
        this.deathLoss = deathLoss;
    }

    public BigDecimal getDutyCoefficient() {
        return dutyCoefficient;
    }

    public void setDutyCoefficient(BigDecimal dutyCoefficient) {
        this.dutyCoefficient = dutyCoefficient;
    }

    public BigDecimal getRateMolecular() {
        return rateMolecular;
    }

    public void setRateMolecular(BigDecimal rateMolecular) {
        this.rateMolecular = rateMolecular;
    }

    public BigDecimal getRateDenominator() {
        return rateDenominator;
    }

    public void setRateDenominator(BigDecimal rateDenominator) {
        this.rateDenominator = rateDenominator;
    }

    public BigDecimal getOtherForceMedFee() {
        return otherForceMedFee;
    }

    public void setOtherForceMedFee(BigDecimal otherForceMedFee) {
        this.otherForceMedFee = otherForceMedFee;
    }

    public BigDecimal getOtherForceDeathFee() {
        return otherForceDeathFee;
    }

    public void setOtherForceDeathFee(BigDecimal otherForceDeathFee) {
        this.otherForceDeathFee = otherForceDeathFee;
    }

    public String getIsForceDeathLimit() {
        return isForceDeathLimit;
    }

    public void setIsForceDeathLimit(String isForceDeathLimit) {
        this.isForceDeathLimit = isForceDeathLimit;
    }

    public String getIsForceMedLimit() {
        return isForceMedLimit;
    }

    public void setIsForceMedLimit(String isForceMedLimit) {
        this.isForceMedLimit = isForceMedLimit;
    }

    public BigDecimal getForceMedLimit() {
        return forceMedLimit;
    }

    public void setForceMedLimit(BigDecimal forceMedLimit) {
        this.forceMedLimit = forceMedLimit;
    }

    public BigDecimal getForceDeathLimit() {
        return forceDeathLimit;
    }

    public void setForceDeathLimit(BigDecimal forceDeathLimit) {
        this.forceDeathLimit = forceDeathLimit;
    }

    public BigDecimal getLossAmount() {
        return lossAmount;
    }

    public void setLossAmount(BigDecimal lossAmount) {
        this.lossAmount = lossAmount;
    }

    public BigDecimal getRescueAmount() {
        return rescueAmount;
    }

    public void setRescueAmount(BigDecimal rescueAmount) {
        this.rescueAmount = rescueAmount;
    }

    public String getGarageCode() {
        return garageCode;
    }

    public void setGarageCode(String garageCode) {
        this.garageCode = garageCode;
    }
}
