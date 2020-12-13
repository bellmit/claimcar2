package ins.sino.claimcar.genilex.vo.scoreVo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("soapenv:Body")
public class ScoreReqBodyVo implements Serializable {

	/**  */
	private static final long serialVersionUID = 6606450331852430142L;
	
    @XStreamAlias("PushFraud") 
    private PushFraud pushFraud;

    
    public PushFraud getPushFraud() {
        return pushFraud;
    }

    
    public void setPushFraud(PushFraud pushFraud) {
        this.pushFraud = pushFraud;
    }
    
    
}
