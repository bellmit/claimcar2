package ins.sino.claimcar.court.dlclaim.xyvo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.converters.extended.ToAttributedValueConverter;

@XStreamAlias("LEAF")
@XStreamConverter(value=ToAttributedValueConverter.class, strings={"values"})
public class LeafVo implements Serializable{

   private static final long serialVersionUID = 1L;
   private String values;
   public String getValues() {
	return values;
   }
  public void setValues(String values) {
	this.values = values;
  }
   
}
