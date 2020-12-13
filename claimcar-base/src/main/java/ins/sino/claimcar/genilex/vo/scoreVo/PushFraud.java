package ins.sino.claimcar.genilex.vo.scoreVo;

import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("PushFraud")
public class PushFraud implements Serializable {

	/**  */
	private static final long serialVersionUID = 6606450331852430142L;
	
    @XStreamAlias("Requestor") 
    private Requestor requestor;
    
    @XStreamAlias("FraudScores") 
    private List<FraudScore> fraudScores;

    
    public Requestor getRequestor() {
        return requestor;
    }

    
    public void setRequestor(Requestor requestor) {
        this.requestor = requestor;
    }

    
    public List<FraudScore> getFraudScores() {
        return fraudScores;
    }

    
    public void setFraudScores(List<FraudScore> fraudScores) {
        this.fraudScores = fraudScores;
    }
    
    
}
