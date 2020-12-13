package ins.sino.claimcar.genilex.vo.scoreVo;

import ins.platform.utils.xstreamconverters.SinosoftDateConverter;

import java.io.Serializable;
import java.util.Date;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;

@XStreamAlias("Requestor")
public class Requestor implements Serializable {

	/**  */
	private static final long serialVersionUID = 6606450331852430142L;
	
    @XStreamAlias("Reference") 
    private String reference;
    
    @XStreamAlias("Timestamp")
	@XStreamConverter(value = SinosoftDateConverter.class, strings = {"yyyy-MM-dd HH:mm:ss"})
    private Date timestamp;
    
    @XStreamAlias("AccountInfo") 
    private AccountInfo accountInfo;

    
    public String getReference() {
        return reference;
    }

    
    public void setReference(String reference) {
        this.reference = reference;
    }

    
    public Date getTimestamp() {
        return timestamp;
    }

    
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    
    public AccountInfo getAccountInfo() {
        return accountInfo;
    }

    
    public void setAccountInfo(AccountInfo accountInfo) {
        this.accountInfo = accountInfo;
    }
    
    
}
