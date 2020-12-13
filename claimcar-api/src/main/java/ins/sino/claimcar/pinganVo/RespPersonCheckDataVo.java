package ins.sino.claimcar.pinganVo;
import java.util.List;

public class RespPersonCheckDataVo {

    private List<PersonObjectDTO> personObjectDTOList;
    private List<PersonSurveyDTO> personSurveyDTOList;
    public void setPersonObjectDTOList(List<PersonObjectDTO> personObjectDTOList) {
         this.personObjectDTOList = personObjectDTOList;
     }
     public List<PersonObjectDTO> getPersonObjectDTOList() {
         return personObjectDTOList;
     }

    public void setPersonSurveyDTOList(List<PersonSurveyDTO> personSurveyDTOList) {
         this.personSurveyDTOList = personSurveyDTOList;
     }
     public List<PersonSurveyDTO> getPersonSurveyDTOList() {
         return personSurveyDTOList;
     }

}