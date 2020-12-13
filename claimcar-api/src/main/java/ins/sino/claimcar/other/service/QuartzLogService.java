package ins.sino.claimcar.other.service;

import ins.framework.common.ResultPage;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.other.vo.PrplQuartzLogVo;

import java.util.List;


public interface QuartzLogService {
    
    /**
     * 保存或更新日志
     * <pre></pre>
     * @param prplQuartzLogVo
     * @param sysUserVo
     * @modified:
     * ☆LinYi(2018年3月22日 上午11:42:48): <br>
     */
    public void saveOrUpdateQuartzLog(PrplQuartzLogVo prplQuartzLogVo,SysUserVo sysUserVo);
    
    /**
     * 查询日志
     * <pre></pre>
     * @param Id
     * @return
     * @modified:
     * ☆LinYi(2018年3月23日 下午3:02:17): <br>
     */
    public PrplQuartzLogVo findQuartzLogById(Long id);
    
    /**
     * 通过报案号、任务类型查询日志
     * <pre></pre>
     * @param registNo
     * @param jobType
     * @modified:
     * ☆LinYi(2018年3月22日 下午2:55:24): <br>
     */
    public PrplQuartzLogVo findQuartzLogByRegistNoAndJobType(String registNo,String jobType);
    
    /**
     * 查询第一次强制立案失败案件
     * <pre></pre>
     * @param prplQuartzLogVo
     * @return
     * @modified:
     * ☆LinYi(2018年3月22日 下午3:42:34): <br>
     */
    public List<PrplQuartzLogVo> findClaimForceFirstFail();
    
    /**
     * 日志表分页查询方法
     * <pre></pre>
     * @param prplQuartzLogVo
     * @param start
     * @param length
     * @return
     * @modified:
     * ☆LinYi(2018年3月22日 下午4:42:51): <br>
     */
    public ResultPage<PrplQuartzLogVo> findClaimForceForPage(PrplQuartzLogVo prplQuartzLogVo,int start,int length) throws Exception;


}