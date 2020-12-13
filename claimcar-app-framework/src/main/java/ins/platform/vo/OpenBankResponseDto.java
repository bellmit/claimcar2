package ins.platform.vo;

import java.io.Serializable;
import java.util.List;

/**
 * 基础平台返回开户行响应数据实体
 *
 * @author maofengning
 * @date 2020/7/6 11:14
 */
public class OpenBankResponseDto implements Serializable {

    private static final long serialVersionUID = -2507346465264676815L;

    /** 响应代码 */
    private int code;
    /** 提示信息 */
    private String msg;
    /** 结果数据 */
    private List<OpenBankDetailDto> data;

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

    public List<OpenBankDetailDto> getData() {
        return data;
    }

    public void setData(List<OpenBankDetailDto> data) {
        this.data = data;
    }
}
