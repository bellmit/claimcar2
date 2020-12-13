/**
  * Copyright 2020 bejson.com 
  */
package ins.sino.claimcar.pinganVo;
import java.util.List;

/**
 * Auto-generated: 2020-07-20 16:51:44
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class RespPropDlossDataVo {

    private PropertyObjectDTO propertyObjectDTO;
    private PropertyLossDTO propertyLossDTO;
    private List<PropertyLossDetailDTO> propertyLossDetailDTOList;
    public void setPropertyObjectDTO(PropertyObjectDTO propertyObjectDTO) {
         this.propertyObjectDTO = propertyObjectDTO;
     }
     public PropertyObjectDTO getPropertyObjectDTO() {
         return propertyObjectDTO;
     }

    public void setPropertyLossDTO(PropertyLossDTO propertyLossDTO) {
         this.propertyLossDTO = propertyLossDTO;
     }
     public PropertyLossDTO getPropertyLossDTO() {
         return propertyLossDTO;
     }

    public void setPropertyLossDetailDTOList(List<PropertyLossDetailDTO> propertyLossDetailDTOList) {
         this.propertyLossDetailDTOList = propertyLossDetailDTOList;
     }
     public List<PropertyLossDetailDTO> getPropertyLossDetailDTOList() {
         return propertyLossDetailDTOList;
     }

}