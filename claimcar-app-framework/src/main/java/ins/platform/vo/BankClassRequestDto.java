package ins.platform.vo;

import java.io.Serializable;

/**
 * 银行大类信息请求数据实体
 *
 * @author maofengning
 * @date 2020/7/4 11:26
 */
public class BankClassRequestDto implements Serializable {

    private static final long serialVersionUID = -5859352780380316901L;

    /** 银行大类代码 */
    private String bankCode;
    /** 银行大类名称 */
    private String bankName;

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
}
