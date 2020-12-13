package ins.sino.claimcar.trafficplatform.service;

import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.check.vo.PrpLCheckCarInfoVo;

import java.util.List;


public interface CarRiskImagesUpdateService {

   /**
    * 图片上传接口
    * <pre></pre>
    * @param registNo
    * @param userVo
    * @param riskCode
    * @param claimSequenceNo
    * @modified:
    * ☆zhujunde(2018年7月2日 上午9:24:29): <br>
    */
    public void carRiskImagesUpdate(String registNo,SysUserVo userVo,String riskCode,String claimSequenceNo);
    
    /**
     * 根据报案号跟车牌号查询
     * <pre></pre>
     * @param registNo
     * @param licenseNo
     * @return
     * @modified:
     * ☆zhujunde(2017年12月19日 上午11:30:30): <br>
     */
    public List<PrpLCheckCarInfoVo> findPrpLCheckCarInfoVoListByOther(String registNo,String licenseNo);
}
