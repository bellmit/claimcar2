package ins.sino.claimcar.realtimequery.web.servlet.vo;

import java.io.Serializable;

public class ImageInfoVo implements Serializable {
    private String imgurl;//保险公司照片路径
    private String modifyTime;  // 修改时间
    private String pageSize;   //大小

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(String modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }
}
