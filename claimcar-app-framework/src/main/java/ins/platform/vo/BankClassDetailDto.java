package ins.platform.vo;

import java.io.Serializable;

/**
 * 基础平台银行大类详细信息
 *
 * @author maofengning
 * @date 2020/7/4 11:31
 */
public class BankClassDetailDto implements Serializable {

    private static final long serialVersionUID = 8665178540528643726L;

    /** 银行大类主键 */
    private String bankId;
    /** 银行大类代码 */
    private String bankCode;
    /** 银行大类名称 */
    private String bankName;
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

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
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
