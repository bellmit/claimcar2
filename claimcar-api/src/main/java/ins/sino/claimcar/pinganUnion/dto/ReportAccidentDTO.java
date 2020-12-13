package ins.sino.claimcar.pinganUnion.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description 案件事故信息
 * @Author liuys
 * @Date 2020/7/21 15:31
 */
public class ReportAccidentDTO implements Serializable {
    //报案号
    private String reportNo;
    //出险原因第一纬度
    private String accidentCauseLevel1;
    //出险原因第二纬度
    private String accidentCauseLevel2;
    //出险原因第三纬度
    private String accidentCauseLevel3;
    //出险经过
    private String accidentDetail;
    //出险地点城市代码
    private String accidentCityCode;
    //出险地点城市名称
    private String accidentCityName;
    //出险地点
    private String accidentPlace;
    //出险时间
    private Date accidentDate;
    //事故类型  1-单方 2-多方
    private String accidentType;
    //事故详细类型    01-碰撞 02-火烧 03-自然灾害
    private String accidentTypeDetail;
    //事故责任认定    0.未认定1.全责2.主责3.同责4.次责5.无责6.无法认定
    private String accidentResponsibility;
    //出险地经度
    private String damagePlaceGpsX;
    //出险地纬度
    private String damagePlaceGpsY;
    //死亡人数
    private Integer deatToll;
    //受伤人数
    private Integer injuredNumber;

    public String getReportNo() {
        return reportNo;
    }

    public void setReportNo(String reportNo) {
        this.reportNo = reportNo;
    }

    public String getAccidentCauseLevel1() {
        return accidentCauseLevel1;
    }

    public void setAccidentCauseLevel1(String accidentCauseLevel1) {
        this.accidentCauseLevel1 = accidentCauseLevel1;
    }

    public String getAccidentCauseLevel2() {
        return accidentCauseLevel2;
    }

    public void setAccidentCauseLevel2(String accidentCauseLevel2) {
        this.accidentCauseLevel2 = accidentCauseLevel2;
    }

    public String getAccidentCauseLevel3() {
        return accidentCauseLevel3;
    }

    public void setAccidentCauseLevel3(String accidentCauseLevel3) {
        this.accidentCauseLevel3 = accidentCauseLevel3;
    }

    public String getAccidentDetail() {
        return accidentDetail;
    }

    public void setAccidentDetail(String accidentDetail) {
        this.accidentDetail = accidentDetail;
    }

    public String getAccidentCityCode() {
        return accidentCityCode;
    }

    public void setAccidentCityCode(String accidentCityCode) {
        this.accidentCityCode = accidentCityCode;
    }

    public String getAccidentCityName() {
        return accidentCityName;
    }

    public void setAccidentCityName(String accidentCityName) {
        this.accidentCityName = accidentCityName;
    }

    public String getAccidentPlace() {
        return accidentPlace;
    }

    public void setAccidentPlace(String accidentPlace) {
        this.accidentPlace = accidentPlace;
    }

    public Date getAccidentDate() {
        return accidentDate;
    }

    public void setAccidentDate(Date accidentDate) {
        this.accidentDate = accidentDate;
    }

    public String getAccidentType() {
        return accidentType;
    }

    public void setAccidentType(String accidentType) {
        this.accidentType = accidentType;
    }

    public String getAccidentTypeDetail() {
        return accidentTypeDetail;
    }

    public void setAccidentTypeDetail(String accidentTypeDetail) {
        this.accidentTypeDetail = accidentTypeDetail;
    }

    public String getAccidentResponsibility() {
        return accidentResponsibility;
    }

    public void setAccidentResponsibility(String accidentResponsibility) {
        this.accidentResponsibility = accidentResponsibility;
    }

    public String getDamagePlaceGpsX() {
        return damagePlaceGpsX;
    }

    public void setDamagePlaceGpsX(String damagePlaceGpsX) {
        this.damagePlaceGpsX = damagePlaceGpsX;
    }

    public String getDamagePlaceGpsY() {
        return damagePlaceGpsY;
    }

    public void setDamagePlaceGpsY(String damagePlaceGpsY) {
        this.damagePlaceGpsY = damagePlaceGpsY;
    }

    public Integer getDeatToll() {
        return deatToll;
    }

    public void setDeatToll(Integer deatToll) {
        this.deatToll = deatToll;
    }

    public Integer getInjuredNumber() {
        return injuredNumber;
    }

    public void setInjuredNumber(Integer injuredNumber) {
        this.injuredNumber = injuredNumber;
    }
}
