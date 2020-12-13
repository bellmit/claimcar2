package ins.platform.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * 开户银行省信息实体
 *
 * @author maofengning
 * @date 2020/7/2 17:51
 */
public class ProvinceDetailDto implements Serializable {
    private static final long serialVersionUID = -4440514198024085767L;

    /**主键ID */
    private Integer provinceId;
    /**省代码 */
    private String provinceCode;
    /**省名称 */
    private String provinceName;
    /**有效标识 */
    private Integer invalidFlag;
    /**创建者 */
    private String createdUser;
    /**创建时间 */
    private String createdTime;
    /**更新者 */
    private String updatedUser;
    /**更新时间 */
    private String updatedTime;

    public Integer getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(Integer provinceId) {
        this.provinceId = provinceId;
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
