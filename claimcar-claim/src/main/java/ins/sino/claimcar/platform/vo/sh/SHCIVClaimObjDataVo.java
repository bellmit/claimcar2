/******************************************************************************
 * CREATETIME : 2016年5月31日 下午7:09:11
 ******************************************************************************/
package ins.sino.claimcar.platform.vo.sh;

import ins.sino.claimcar.carplatform.adapter.DateXmlAdapterSH;
import java.util.Date;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * 上海物损损失情况
 * @author ★XMSH
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class SHCIVClaimObjDataVo {

	@XmlElement(name = "OBJECT_DESC")
	private String objectDesc;// 损失物品描述

	@XmlElement(name = "LOSS_NUM")
	private Integer lossNum;// 损失数量

	@XmlElement(name = "LOSS_AMOUNT")
	private Double lossAmount;// 损失金额（单价）

	@XmlElement(name = "MAIN_THIRD", required = true)
	private String mainThird;// 是否承保车辆

	@XmlElement(name = "SURVEY_TYPE", required = true)
	private String surveyType;// 现场类别

	@XmlElement(name = "SURVEY_NAME")
	private String surveyName;// 查勘人员姓名

	@XmlElement(name = "SURVEY_START_TIME")
	@XmlJavaTypeAdapter(DateXmlAdapterSH.class)
	private Date surveyStartTime;// 查勘开始时间

	@XmlElement(name = "SURVEY_END_TIME")
	@XmlJavaTypeAdapter(DateXmlAdapterSH.class)
	private Date surveyEndTime;// 查勘结束时间

	@XmlElement(name = "SURVEY_PLACE")
	private String surveyPlace;// 查勘地点

	@XmlElement(name = "SURVEY_DES")
	private String surveyDes;// 查勘情况说明

	@XmlElement(name = "ESTIMATE_NAME", required = true)
	private String estimateName;// 定损人员代码

	@XmlElement(name = "ESTIMATE_START_TIME")
	@XmlJavaTypeAdapter(DateXmlAdapterSH.class)
	private Date estimateStartTime;// 定损开始时间

	@XmlElement(name = "ESTIMATE_END_TIME")
	@XmlJavaTypeAdapter(DateXmlAdapterSH.class)
	private Date estimateEndTime;// 定损结束时间

	@XmlElement(name = "ASSESOR_NAME")
	private String assesorName;// 核损人员姓名

	@XmlElement(name = "ASSESOR_START_TIME")
	@XmlJavaTypeAdapter(DateXmlAdapterSH.class)
	private Date assesorStartTime;// 核损开始时间

	@XmlElement(name = "ASSESOR_END_TIME")
	@XmlJavaTypeAdapter(DateXmlAdapterSH.class)
	private Date assesorEndTime;// 核损结束时间

	/* 牛强 2017-03-15 改 */
	@XmlElement(name = "ProtectProperty")
	private String protectProperty;// 财产属性

	@XmlElement(name = "CheckerCode")
	private String checkerCode;// 查勘人员代码

	@XmlElement(name = "CheckerCertiCode")
	private String checkerCertiCode;// 查勘人员身份证号

	@XmlElement(name = "UnderWriteCode")
	private String underWriteCode;// 核损人员代码

	@XmlElement(name = "UnderWriteCertiCode")
	private String underWriteCertiCode;// 核损人员身份证号

	@XmlElement(name = "UnderDefLoss")
	private double underDefLoss;// 核损金额
	
	/**
	 * @return 返回 objectDesc 损失物品描述
	 */
	public String getObjectDesc() {
		return objectDesc;
	}

	/**
	 * @param objectDesc 要设置的 损失物品描述
	 */
	public void setObjectDesc(String objectDesc) {
		this.objectDesc = objectDesc;
	}

	/**
	 * @return 返回 lossNum 损失数量
	 */
	public Integer getLossNum() {
		return lossNum;
	}

	/**
	 * @param lossNum 要设置的 损失数量
	 */
	public void setLossNum(Integer lossNum) {
		this.lossNum = lossNum;
	}

	/**
	 * @return 返回 lossAmount 损失金额（单价）
	 */
	public Double getLossAmount() {
		return lossAmount;
	}

	/**
	 * @param lossAmount 要设置的 损失金额（单价）
	 */
	public void setLossAmount(Double lossAmount) {
		this.lossAmount = lossAmount;
	}

	/**
	 * @return 返回 mainThird 是否承保车辆
	 */
	public String getMainThird() {
		return mainThird;
	}

	/**
	 * @param mainThird 要设置的 是否承保车辆
	 */
	public void setMainThird(String mainThird) {
		this.mainThird = mainThird;
	}

	/**
	 * @return 返回 surveyType 现场类别
	 */
	public String getSurveyType() {
		return surveyType;
	}

	/**
	 * @param surveyType 要设置的 现场类别
	 */
	public void setSurveyType(String surveyType) {
		this.surveyType = surveyType;
	}

	/**
	 * @return 返回 surveyName 查勘人员姓名
	 */
	public String getSurveyName() {
		return surveyName;
	}

	/**
	 * @param surveyName 要设置的 查勘人员姓名
	 */
	public void setSurveyName(String surveyName) {
		this.surveyName = surveyName;
	}

	/**
	 * @return 返回 surveyStartTime 查勘开始时间
	 */
	public Date getSurveyStartTime() {
		return surveyStartTime;
	}

	/**
	 * @param surveyStartTime 要设置的 查勘开始时间
	 */
	public void setSurveyStartTime(Date surveyStartTime) {
		this.surveyStartTime = surveyStartTime;
	}

	/**
	 * @return 返回 surveyEndTime 查勘结束时间
	 */
	public Date getSurveyEndTime() {
		return surveyEndTime;
	}

	/**
	 * @param surveyEndTime 要设置的 查勘结束时间
	 */
	public void setSurveyEndTime(Date surveyEndTime) {
		this.surveyEndTime = surveyEndTime;
	}

	/**
	 * @return 返回 surveyPlace 查勘地点
	 */
	public String getSurveyPlace() {
		return surveyPlace;
	}

	/**
	 * @param surveyPlace 要设置的 查勘地点
	 */
	public void setSurveyPlace(String surveyPlace) {
		this.surveyPlace = surveyPlace;
	}

	/**
	 * @return 返回 surveyDes 查勘情况说明
	 */
	public String getSurveyDes() {
		return surveyDes;
	}

	/**
	 * @param surveyDes 要设置的 查勘情况说明
	 */
	public void setSurveyDes(String surveyDes) {
		this.surveyDes = surveyDes;
	}

	/**
	 * @return 返回 estimateName 定损人员代码
	 */
	public String getEstimateName() {
		return estimateName;
	}

	/**
	 * @param estimateName 要设置的 定损人员代码
	 */
	public void setEstimateName(String estimateName) {
		this.estimateName = estimateName;
	}

	/**
	 * @return 返回 estimateStartTime 定损开始时间
	 */
	public Date getEstimateStartTime() {
		return estimateStartTime;
	}

	/**
	 * @param estimateStartTime 要设置的 定损开始时间
	 */
	public void setEstimateStartTime(Date estimateStartTime) {
		this.estimateStartTime = estimateStartTime;
	}

	/**
	 * @return 返回 estimateEndTime 定损结束时间
	 */
	public Date getEstimateEndTime() {
		return estimateEndTime;
	}

	/**
	 * @param estimateEndTime 要设置的 定损结束时间
	 */
	public void setEstimateEndTime(Date estimateEndTime) {
		this.estimateEndTime = estimateEndTime;
	}

	/**
	 * @return 返回 assesorName 核损人员姓名
	 */
	public String getAssesorName() {
		return assesorName;
	}

	/**
	 * @param assesorName 要设置的 核损人员姓名
	 */
	public void setAssesorName(String assesorName) {
		this.assesorName = assesorName;
	}

	/**
	 * @return 返回 assesorStartTime 核损开始时间
	 */
	public Date getAssesorStartTime() {
		return assesorStartTime;
	}

	/**
	 * @param assesorStartTime 要设置的 核损开始时间
	 */
	public void setAssesorStartTime(Date assesorStartTime) {
		this.assesorStartTime = assesorStartTime;
	}

	/**
	 * @return 返回 assesorEndTime 核损结束时间
	 */
	public Date getAssesorEndTime() {
		return assesorEndTime;
	}

	/**
	 * @param assesorEndTime 要设置的 核损结束时间
	 */
	public void setAssesorEndTime(Date assesorEndTime) {
		this.assesorEndTime = assesorEndTime;
	}

	public String getProtectProperty() {
		return protectProperty;
	}

	public void setProtectProperty(String protectProperty) {
		this.protectProperty = protectProperty;
	}

	public String getCheckerCode() {
		return checkerCode;
	}

	public void setCheckerCode(String checkerCode) {
		this.checkerCode = checkerCode;
	}

	public String getCheckerCertiCode() {
		return checkerCertiCode;
	}

	public void setCheckerCertiCode(String checkerCertiCode) {
		this.checkerCertiCode = checkerCertiCode;
	}

	public String getUnderWriteCode() {
		return underWriteCode;
	}

	public void setUnderWriteCode(String underWriteCode) {
		this.underWriteCode = underWriteCode;
	}

	public String getUnderWriteCertiCode() {
		return underWriteCertiCode;
	}

	public void setUnderWriteCertiCode(String underWriteCertiCode) {
		this.underWriteCertiCode = underWriteCertiCode;
	}

	public double getUnderDefLoss() {
		return underDefLoss;
	}

	public void setUnderDefLoss(double underDefLoss) {
		this.underDefLoss = underDefLoss;
	}

}
