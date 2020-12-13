package ins.sino.claimcar.claimjy.vo.price;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("BODY")
public class JyPriceResBody implements Serializable{
	 private static final long serialVersionUID = 1L;
	 
	 @XStreamAlias("URL")
	 private String url;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	 

}
