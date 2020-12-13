package ins.sino.claimcar.pinganunion.vo.payCallback;

import java.io.Serializable;

/**
 * description: UnionPayCallbackResponseDto 支付结果回调接口-返回数据
 * date: 2020/8/18 9:09
 * author: lk
 * version: 1.0
 */
public class UnionPayCallbackResponseDto implements Serializable {
    private static final long serialVersionUID = -4775230569310562004L;
    /**
     * 返回代码    是否非空：Y  编码：Y
     */
    private String code;
    /**
     * 返回提示信息  是否非空：Y  编码：N
     */
    private String msg;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
