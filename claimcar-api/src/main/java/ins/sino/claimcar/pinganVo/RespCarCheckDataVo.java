/**
  * Copyright 2020 bejson.com 
  */
package ins.sino.claimcar.pinganVo;
import java.util.List;

public class RespCarCheckDataVo {

    private List<CarObjectDTO> carObjectDTOList;
    private List<CarSurveyDTO> carSurveyDTOList;
    public void setCarObjectDTOList(List<CarObjectDTO> carObjectDTOList) {
         this.carObjectDTOList = carObjectDTOList;
     }
     public List<CarObjectDTO> getCarObjectDTOList() {
         return carObjectDTOList;
     }

    public void setCarSurveyDTOList(List<CarSurveyDTO> carSurveyDTOList) {
         this.carSurveyDTOList = carSurveyDTOList;
     }
     public List<CarSurveyDTO> getCarSurveyDTOList() {
         return carSurveyDTOList;
     }

}