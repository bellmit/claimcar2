package ins.sino.claimcar.subrogation.service;



import ins.sino.claimcar.subrogation.vo.PrpLPlatLockVo;
import ins.sino.claimcar.subrogation.vo.PrpLSubrogationMainVo;
import ins.sino.claimcar.subrogation.vo.PrpLSubrogationPersonVo;

import java.util.List;


public interface SubrogationService {
	public Long saveSubrogationInfo(PrpLSubrogationMainVo subrogationMainVo);
    public PrpLSubrogationMainVo find(String registNo);
    public void deleteSubrogationInfo(PrpLSubrogationMainVo subrogationMainVo);
    
    /**
     * 查询被锁定信息
     * <pre></pre>
     * @param recoveryOrPayFlag 1追偿 (锁定对方车辆) 2 清付(被对方锁定)
     * @param registNo
     * @param oppoentLicensePlateNo 责任对方车牌号
     * @param policyType 保单类型  1商业 2交强
     * @return
     * @modified:
     * ☆ZhouYanBin(2016年4月26日 上午8:45:34): <br>
     */
    public List<PrpLPlatLockVo> findByPrpLPlatLockVo(String recoveryOrPayFlag,String registNo,String oppoentLicensePlateNo,String policyType);
    
    /**
     * 根据报案号查询信息
     * <pre></pre>
     * @param registNo
     * @return
     * @modified:
     * ☆ZhouYanBin(2016年4月26日 下午3:13:03): <br>
     */
    public List<PrpLPlatLockVo> findPrpLPlatLockVoByRegistNo(String registNo,String recoveryOrPayFlag);
    
    /**
     * <pre>查询追偿清付信息列表</pre>
     * @param registNo
     * @return
     * @modified:
     * ☆Luwei(2016年9月29日 上午10:57:45): <br>
     */
    public List<PrpLPlatLockVo> findPlatLockVoByPayFlag(String registNo);
    
    /**
     * 根据主键查询数据
     * <pre></pre>
     * @return
     * @modified:
     * ☆ZhouYanBin(2016年5月3日 下午2:39:58): <br>
     */
    public PrpLPlatLockVo findPrpLPlatLockVoById(Long id);
	public void saveSubrogationPers(List<PrpLSubrogationPersonVo> subrogationPersonVoList);
}
