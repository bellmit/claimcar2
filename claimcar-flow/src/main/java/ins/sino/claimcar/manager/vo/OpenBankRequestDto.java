package ins.sino.claimcar.manager.vo;

import java.io.Serializable;

/**
 * 基础数据平台开户行信息请求实体
 *
 * @author maofengning
 * @date 2020/7/6 11:02
 */
public class OpenBankRequestDto implements Serializable {

    private static final long serialVersionUID = -2507346465264676815L;

    /** 银行大类代码 */
    private String bankCode;
    /** 省代码 */
    private String provinceCode;
    /** 市代码 */
    private String cityCode;
    /** 开户行名称 */
    private String unitedName;
    /** 联行号 */
    private String unitedCode;

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

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

    public String getUnitedName() {
        return unitedName;
    }

    public void setUnitedName(String unitedName) {
        this.unitedName = unitedName;
    }

    public String getUnitedCode() {
        return unitedCode;
    }

    public void setUnitedCode(String unitedCode) {
        this.unitedCode = unitedCode;
    }
}
