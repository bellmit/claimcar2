package ins.sino.claimcar.pinganUnion.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description 推修信息
 * @Author liuys
 * @Date 2020/7/21 18:44
 */
public class RecommendRepairDTO implements Serializable {
    //报案号
    private String reportNo;
    //通道表ID
    private String idClmChannelProcess;
    //车牌号
    private String carMark;
    //推荐人
    private String recommendPeople;
    //推荐来源  E理赔：01，报案：02，推修组：03，好车主：04，电话分布式：06，视频分布式：07，AI自助理赔：08
    private String recommendSource;
    //推荐结果  成功：Y,失败：N
    private String recommendResult;
    //推修方式  送修：01，返修：02
    private String recommendRepairType;
    //车行任务状态    好伙伴反馈，00:新任务; 01：待定; 02：同意到店; 03：拒绝到店; 04：确认到店
    private String taskStatus;
    //客户意愿  E手持反馈，00:未沟通; 01：待定; 02：同意到店; 03：拒绝到店
    private String clientStatus;
    //是否推修大案    Y:是N:否
    private String majorCase;
    //定损修理厂
    private String assessGarageCode;
    //推修修理厂
    private String recommendGarageCode;
    //推修完成时间
    private String finishTime;
    //推修次数
    private String recommendTimes;
    //删除时间
    private Date deletedTime;
    //推修有效标志    0：失效，1：有效
    private String isValid;
    //手机号码
    private String mobile;

    public String getReportNo() {
        return reportNo;
    }

    public void setReportNo(String reportNo) {
        this.reportNo = reportNo;
    }

    public String getIdClmChannelProcess() {
        return idClmChannelProcess;
    }

    public void setIdClmChannelProcess(String idClmChannelProcess) {
        this.idClmChannelProcess = idClmChannelProcess;
    }

    public String getCarMark() {
        return carMark;
    }

    public void setCarMark(String carMark) {
        this.carMark = carMark;
    }

    public String getRecommendPeople() {
        return recommendPeople;
    }

    public void setRecommendPeople(String recommendPeople) {
        this.recommendPeople = recommendPeople;
    }

    public String getRecommendSource() {
        return recommendSource;
    }

    public void setRecommendSource(String recommendSource) {
        this.recommendSource = recommendSource;
    }

    public String getRecommendResult() {
        return recommendResult;
    }

    public void setRecommendResult(String recommendResult) {
        this.recommendResult = recommendResult;
    }

    public String getRecommendRepairType() {
        return recommendRepairType;
    }

    public void setRecommendRepairType(String recommendRepairType) {
        this.recommendRepairType = recommendRepairType;
    }

    public String getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }

    public String getClientStatus() {
        return clientStatus;
    }

    public void setClientStatus(String clientStatus) {
        this.clientStatus = clientStatus;
    }

    public String getMajorCase() {
        return majorCase;
    }

    public void setMajorCase(String majorCase) {
        this.majorCase = majorCase;
    }

    public String getAssessGarageCode() {
        return assessGarageCode;
    }

    public void setAssessGarageCode(String assessGarageCode) {
        this.assessGarageCode = assessGarageCode;
    }

    public String getRecommendGarageCode() {
        return recommendGarageCode;
    }

    public void setRecommendGarageCode(String recommendGarageCode) {
        this.recommendGarageCode = recommendGarageCode;
    }

    public String getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(String finishTime) {
        this.finishTime = finishTime;
    }

    public String getRecommendTimes() {
        return recommendTimes;
    }

    public void setRecommendTimes(String recommendTimes) {
        this.recommendTimes = recommendTimes;
    }

    public Date getDeletedTime() {
        return deletedTime;
    }

    public void setDeletedTime(Date deletedTime) {
        this.deletedTime = deletedTime;
    }

    public String getIsValid() {
        return isValid;
    }

    public void setIsValid(String isValid) {
        this.isValid = isValid;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
