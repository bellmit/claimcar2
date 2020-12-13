package ins.sino.claimcar.manager.vo;

import java.io.Serializable;

/**
 * TODO
 *
 * @author maofengning
 * @date 2020/7/6 11:11
 */
public class OpenBankDetailDto implements Serializable {

    private static final long serialVersionUID = 131454140267152324L;

    /** 联行号主键 */
    private String unitedId;
    /** 联行号 */
    private String unitedCode;
    /** 联行号名称（开户行） */
    private String unitedName;
    /** 所属银行大类代码 */
    private String bankCode;
    /** 所属银行大类名称 */
    private String bankName;
    /** 省代码 */
    private String provinceCode;
    /** 省名称 */
    private String provinceName;
    /** 市代码 */
    private String cityCode;
    /** 市名称 */
    private String cityName;
    /** 最新修改时间 */
    private String lastModidfiedOn;

    public String getUnitedId() {
        return unitedId;
    }

    public void setUnitedId(String unitedId) {
        this.unitedId = unitedId;
    }

    public String getUnitedCode() {
        return unitedCode;
    }

    public void setUnitedCode(String unitedCode) {
        this.unitedCode = unitedCode;
    }

    public String getUnitedName() {
        return unitedName;
    }

    public void setUnitedName(String unitedName) {
        this.unitedName = unitedName;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
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

    public String getLastModidfiedOn() {
        return lastModidfiedOn;
    }

    public void setLastModidfiedOn(String lastModidfiedOn) {
        this.lastModidfiedOn = lastModidfiedOn;
    }
}
