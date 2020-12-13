package ins.platform.vo;

import java.io.Serializable;

/**
 * 基础平台市信息请求实体类
 *
 * @author maofengning
 * @date 2020/7/3 17:55
 */
public class CityRequestDto implements Serializable {

    private static final long serialVersionUID = -543948050875915897L;

    /** 省代码 */
    private String provinceCode;
    /** 市代码 */
    private String cityCode;
    /** 市名称 */
    private String cityName;

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
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
}
