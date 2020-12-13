package ins.sino.claimcar.GDPower.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * @Description:
 * @Author: Gusheng Huang
 * @Date: Create in 上午10:09 19-2-21
 * @Modified By:
 */
@XStreamAlias("Body")
public class ResBody {
    @XStreamAlias("DataList")
    private ResDataList resDataList;

    public ResDataList getResDataList() {
        return resDataList;
    }

    public void setResDataList(ResDataList resDataList) {
        this.resDataList = resDataList;
    }
}
