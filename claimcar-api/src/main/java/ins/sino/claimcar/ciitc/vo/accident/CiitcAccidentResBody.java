package ins.sino.claimcar.ciitc.vo.accident;

import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
@XStreamAlias("Body")
public class CiitcAccidentResBody implements Serializable{
	private static final long serialVersionUID = 1L;
	@XStreamAlias("ResInformation")
	private List<ResInformation> tasklist;
	
	

}
