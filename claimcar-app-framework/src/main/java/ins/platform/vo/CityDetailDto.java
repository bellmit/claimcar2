package ins.platform.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * 基础平台开户银行市信息数据实体
 *
 * @author maofengning
 * @date 2020/7/3 17:31
 */
public class CityDetailDto implements Serializable {
    private static final long serialVersionUID = 3569882082892579080L;

    /** 主键ID */
    private Integer cityId;
    /** 市代码 */
    private String cityCode;
    /** 市名称 */
    private String cityName;
    /** 省代码 */
    private String provinceCode;
    /** 有效标识 */
    private Integer invalidFlag;
    /** 创建者 */
    private String createdUser;
    /** 创建时间 */
    private String createdTime;
    /** 更新者 */
    private String updatedUser;
    /** 更新时间 */
    private String updatedTime;

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public Integer getInvalidFlag() {
        return invalidFlag;
    }

    public void setInvalidFlag(Integer invalidFlag) {
        this.invalidFlag = invalidFlag;
    }

    public String getCreatedUser() {
        return createdUser;
    }

    public void setCreatedUser(String createdUser) {
        this.createdUser = createdUser;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public String getUpdatedUser() {
        return updatedUser;
    }

    public void setUpdatedUser(String updatedUser) {
        this.updatedUser = updatedUser;
    }

    public String getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(String updatedTime) {
        this.updatedTime = updatedTime;
    }
}
