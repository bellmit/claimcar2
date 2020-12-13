package ins.sino.claimcar.GDPower.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.List;

/**
 * @Description:
 * @Author: Gusheng Huang
 * @Date: Create in 上午10:46 19-2-21
 * @Modified By:
 */
@XStreamAlias("DataList")
public class ResDataList {

    @XStreamImplicit(itemFieldName = "Data")
    private List<ResData> data;

    public List<ResData> getData() {
        return data;
    }

    public void setData(List<ResData> data) {
        this.data = data;
    }
}
