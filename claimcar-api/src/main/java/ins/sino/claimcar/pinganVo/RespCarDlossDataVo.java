package ins.sino.claimcar.pinganVo;
import java.util.List;



public class RespCarDlossDataVo {

    private CarObjectDTO carObjectDTO;
    private CarLossVO carLossVO;
    private List<CarLossDetailVO> carLossDetailVOList;
    private ThirdInfoVo thirdInfo;
    public void setCarObjectDTO(CarObjectDTO carObjectDTO) {
         this.carObjectDTO = carObjectDTO;
     }
     public CarObjectDTO getCarObjectDTO() {
         return carObjectDTO;
     }

    public void setCarLossVO(CarLossVO carLossVO) {
         this.carLossVO = carLossVO;
     }
     public CarLossVO getCarLossVO() {
         return carLossVO;
     }

    public void setCarLossDetailVOList(List<CarLossDetailVO> carLossDetailVOList) {
         this.carLossDetailVOList = carLossDetailVOList;
     }
     public List<CarLossDetailVO> getCarLossDetailVOList() {
         return carLossDetailVOList;
     }

    public void setThirdInfo(ThirdInfoVo thirdInfo) {
         this.thirdInfo = thirdInfo;
     }
     public ThirdInfoVo getThirdInfo() {
         return thirdInfo;
     }

}