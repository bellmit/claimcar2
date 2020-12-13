package ins.sino.claimcar.manager.vo;

import java.io.Serializable;
import java.util.List;

/**
 * 基础平台银行大类信息响应数据实体
 *
 * @author maofengning
 * @date 2020/7/4 11:29
 */
public class BankClassResponseDto implements Serializable {

    private static final long serialVersionUID = -5630843974935174019L;
    /** 响应代码 */
    private int code;
    /** 提示信息 */
    private String msg;
    /** 结果数据 */
    private List<BankClassDetailDto> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<BankClassDetailDto> getData() {
        return data;
    }

    public void setData(List<BankClassDetailDto> data) {
        this.data = data;
    }
}
