package ins.sino.claimcar.hnbxrest.service;

import ins.sino.claimcar.hnbxrest.vo.PrplQuickUserVo;

import java.util.List;


public interface QuickUserService {
    
    /**
     * 查询所有有效快赔人员
     * <pre></pre>
     * @return
     * @modified:
     * ☆LinYi(2017年9月22日 下午2:39:39): <br>
     */
    public List<PrplQuickUserVo> findAll();

    /**
     * 根据接受任务次数查询快赔人员
     * <pre></pre>
     * @return
     * @modified:
     * ☆LinYi(2017年9月22日 下午2:38:55): <br>
     */
    public PrplQuickUserVo findQuickUser();
    
    /**
     * 通过userCode查询快赔用户
     * <pre></pre>
     * @param userCode
     * @return
     * @modified:
     * ☆LinYi(2017年9月22日 下午4:01:30): <br>
     */
    public PrplQuickUserVo findQuickUserByUserCode(String userCode);
    
    /**
     * 更新快赔人员任务接受次数
     * <pre></pre>
     * @param userCode
     * @modified:
     * ☆LinYi(2017年9月22日 下午4:12:39): <br>
     */
    public void updateTimes(PrplQuickUserVo prplQuickUserVo);
    
   /**
    * 
    * <pre></pre>
    * @param isGBFlag 是否联共保调度人员 1：是
    * @return
    * @modified:
    * ☆LiYi(2018年10月18日 上午10:03:37): <br>
    */
    public PrplQuickUserVo findQuickUserByGBFlag(String isGBFlag,String comCode);
    
    public List<PrplQuickUserVo> findQuickUserVosByGBFlag(String isGBFlag,String comCode);
}
