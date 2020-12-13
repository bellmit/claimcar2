package ins.platform.vo;

import java.io.Serializable;

/**
 * TODO
 *
 * @author maofengning
 * @date 2020/7/2 17:48
 */
public class ProvinceRequestDto implements Serializable {
    private static final long serialVersionUID = 3609866314717153043L;
    /** 省代码 */
    private String provinceCode;
    /** 省名称 */
    private String provinceName;

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
}
