package ins.sino.claimcar.trafficplatform.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
@XStreamAlias("LossPartData")
public class LossPartDataVo implements Serializable{
	
private static final long serialVersionUID = 1L;

@XStreamAlias("LossPart")
private String lossPart;

public String getLossPart() {
	return lossPart;
}

public void setLossPart(String lossPart) {
	this.lossPart = lossPart;
}

}
