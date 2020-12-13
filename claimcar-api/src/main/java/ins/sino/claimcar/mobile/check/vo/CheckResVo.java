package ins.sino.claimcar.mobile.check.vo;

import ins.sino.claimcar.mobileCheckCommon.vo.MobileCheckBody;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
/**
 * 报案基本信息（快赔请求理赔）
 * @author zjd
 *
 */
@XStreamAlias("CHECK")
public class CheckResVo extends MobileCheckBody implements Serializable{
	
	/**  */
	private static final long serialVersionUID = 1L;
	
	@XStreamAlias("LOSSID")
	private String lossId; //理赔车辆查勘ID
	
	@XStreamAlias("LOSSSERIALNO")
	private String lossSerialNo; //移动终端车辆查勘序号

    @XStreamAlias("CARID")
    private String carId; //理赔车辆ID
    
    public String getLossId() {
        return lossId;
    }

    
    public void setLossId(String lossId) {
        this.lossId = lossId;
    }

    
    public String getLossSerialNo() {
        return lossSerialNo;
    }

    
    public void setLossSerialNo(String lossSerialNo) {
        this.lossSerialNo = lossSerialNo;
    }


    
    public String getCarId() {
        return carId;
    }


    
    public void setCarId(String carId) {
        this.carId = carId;
    }
	


}
