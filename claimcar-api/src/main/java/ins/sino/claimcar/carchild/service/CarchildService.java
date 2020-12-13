package ins.sino.claimcar.carchild.service;




import ins.framework.common.ResultPage;
import ins.platform.vo.SysUserVo;



import java.text.ParseException;
import ins.sino.claimcar.carchild.vo.PrplCarchildScheduleVo;
import ins.sino.claimcar.carchild.vo.PrplcarchildregistcancleVo;
import ins.sino.claimcar.carchild.vo.RegistInfoResVo;
import ins.sino.claimcar.carchild.vo.RegistInformationVo;
import ins.sino.claimcar.carchild.vo.RevokeInfoReqVo;
import ins.sino.claimcar.carchild.vo.RevokeInfoResVo;
import ins.sino.claimcar.flow.vo.PrpLWfTaskQueryVo;
import ins.sino.claimcar.carchild.vo.RevokeRestoreInfoReqVo;
import ins.sino.claimcar.carchild.vo.RevokeRestoreInfoResVo;
import ins.sino.claimcar.carinterface.util.BusinessType;
import ins.sino.claimcar.carinterface.vo.ClaimInterfaceLogVo;
import ins.sino.claimcar.claim.vo.PrpLClaimCancelVo;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.manager.vo.PrpdIntermMainVo;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;

import java.util.List;



/**
 * 车童网/民太安Service
 * <pre></pre>
 * @author ★LinYi
 */
public interface CarchildService {

    /**
     * 报案信息接口(理赔请求车童网/民太安)
     * <pre></pre>
     * @param scheduleTask
     * @param registVo
     * @param prpLCMainVoList
     * @param prpLWfTaskVo
     * @return
     * @throws Exception
     * @modified:
     * ☆LinYi(2017年10月19日 下午3:54:20): <br>
     */
	public RegistInfoResVo sendRegistInformation(PrpdIntermMainVo prplIntermMainVo,PrpLRegistVo registVo,List<PrpLCMainVo> prpLCMainVoList,List<PrpLWfTaskVo> prpLWfTaskVoList,RegistInformationVo registInformationVo) throws Exception;
	
	/**
	 * 车童网调度信息保存(车童网/民太安通知理赔)
	 * <pre></pre>
	 * @param scheduleTaskInfoVo
	 * @throws Exception
	 * @modified:
	 * ☆LinYi(2017年10月12日 上午11:46:37): <br>
	 */
	public void saveScheduleInformation(PrplCarchildScheduleVo prplCarchildScheduleVo) throws Exception;
	/**
	 * 车童网,民太安案件注销申请通知理赔信息保存
	 * @param prplcarchildregistcancle
	 */
	public void savePrplcarchildregistcancle(PrplcarchildregistcancleVo prplcarchildregistcancleVo);
	/**
	 * 通过Id查询案件注销申请表
	 * @param id
	 * @return
	 */
    public PrplcarchildregistcancleVo findPrplcarchildregistcancleVoById(Long id);
    /**
     * 车童网,民太安报案注销申请通知理赔信息更新
     * @param prplcarchildregistcancleVo
     */
    public void updatePrplcarchildregistcancle(PrplcarchildregistcancleVo prplcarchildregistcancleVo);
    /**
     * 通过报案号查询案件注销Prplcarchildregistcancle
     * @param registNo
     * @return
     */
    public PrplcarchildregistcancleVo findPrplcarchildregistcancleVoByRegistNo(String registNo);





    
    /**
	  * 车童网,民太安案件注销信息查询
	  * @param prplWfTaskQueryVo
	  * @param start
	  * @param length
	  * @return
	  * @throws ParseException
	  */
	 public abstract ResultPage<PrplcarchildregistcancleVo> search(PrpLWfTaskQueryVo prplWfTaskQueryVo,int start,int length,String handleStatus) throws Exception;
	 /**
	  * 注销申请不通过通知车童网/民太安接口
	  * @param registNo
	  * @param sign
	  * @param userVo
	  * @return
	  */
	 public String sendCaseInfoToCarchild(String registNo,String sign,SysUserVo userVo);

	/**
	 * 通过报案号查询公估师轨迹表
	 * <pre></pre>
	 * @param registNo
	 * @return
	 * @modified:
	 * ☆LinYi(2017年10月19日 上午8:58:35): <br>
	 */
	public List<PrplCarchildScheduleVo> findCarchildScheduleByRegistNo(String registNo);
	

 


	/**
	 * 撤销信息接口（理赔请求车童网/民太安）
	 * <pre></pre>
	 * @param reqVo
	 * @param url
	 * @return
	 * @throws Exception
	 * @modified:
	 * ☆LinYi(2017年10月18日 下午3:11:54): <br>
	 */
    public RevokeInfoResVo sendRevokeInformation(RevokeInfoReqVo reqVo,String url) throws Exception;
    /**
     * 撤销信息恢复接口（理赔请求车童网/民太安）
     * <pre></pre>
     * @param reqVo
     * @param url
     * @return
     * @throws Exception
     * @modified:
     * ☆LinYi(2017年10月18日 下午3:11:13): <br>
     */
    public RevokeRestoreInfoResVo sendRevokeRestoreInformation(RevokeRestoreInfoReqVo reqVo,String url) throws Exception;
    /**
     * 保存日志
     * <pre></pre>
     * @param reqVo
     * @param resVo
     * @param url
     * @param businessType
     * @param userVo
     * @modified:
     * ☆zhujunde(2017年11月1日 下午8:25:56): <br>
     */
    public void saveCTorMTACarchildInterfaceLog(RegistInformationVo registInformationVo,String url,BusinessType businessType,SysUserVo userVo);
    

    /**
     * 立案注销调车童网民太安
     * <pre></pre>
     * @param prpLWfTaskVoList
     * @param pClaimCancelVo
     * @param prpLWfTaskVo
     * @param reqVo
     * @throws Exception
     * @modified:
     * ☆zhujunde(2017年11月8日 下午4:18:35): <br>
     */
    public void organizationCTorMTA(PrpLClaimCancelVo pClaimCancelVo,PrpLWfTaskVo prpLWfTaskVo,SysUserVo userVo) throws Exception;
    
    /**
     * 立案注销恢复调车童网民太安
     * <pre></pre>
     * @param registNo
     * @throws Exception
     * @modified:
     * ☆zhujunde(2017年11月8日 下午4:50:42): <br>
     */
    public void sendClaimCancelRestoreCTorMTA(String registNo,SysUserVo userVo) throws Exception;

    

    
   

    /**
     * 根据传入的prplcarchildregistcancleVo更新
     * <pre></pre>
     * @param prplcarchildregistcancleVo
     * @modified:
     * ☆zhujunde(2017年11月24日 上午11:14:26): <br>
     */
    public void updatePrplcarchildregist(PrplcarchildregistcancleVo prplcarchildregistcancleVo);

    /**
     * 车童民太安报案注销补送
     * <pre></pre>
     * @param registNo
     * @param userVo
     * @modified:
     * ☆zhujunde(2017年11月28日 下午5:41:15): <br>
     */
    public void registCancel(ClaimInterfaceLogVo logVo,SysUserVo userVo);
    
   /**
    * 车童民太安定损注销补送
    * <pre></pre>
    * @param logVo
    * @param userVo
    * @modified:
    * ☆zhujunde(2017年11月29日 上午11:10:56): <br>
    */
    public void scheduleDefLossCancel(ClaimInterfaceLogVo logVo,SysUserVo userVo);
    
    /**
     * 车童民太安定损报案接口补送
     * <pre></pre>
     * @param logVo
     * @param userVo
     * @modified:
     * ☆zhujunde(2017年11月29日 上午11:10:49): <br>
     */
    public void registSendCtOrMta(ClaimInterfaceLogVo logVo,SysUserVo userVo);
    
    /**
     * 车童民太安平级移交补送
     * <pre></pre>
     * @param logVo
     * @param userVo
     * @modified:
     * ☆zhujunde(2017年11月29日 下午5:23:49): <br>
     */
    public void handOverCancelSendCtOrMta(ClaimInterfaceLogVo logVo,SysUserVo userVo);
    
    /**
     * 车童民太安调度改派补送
     * <pre></pre>
     * @param logVo
     * @param userVo
     * @modified:
     * ☆zhujunde(2017年11月30日 上午10:14:14): <br>
     */
    public void scheduleChangeSendCtOrMta(ClaimInterfaceLogVo logVo,SysUserVo userVo);
    
    /**
     * 更加报案号查询PrplcarchildregistcancleVo
     * <pre></pre>
     * @param registNo
     * @modified:
     * ☆zhujunde(2018年1月3日 下午3:23:17): <br>
     */
    public PrplcarchildregistcancleVo findCarchildregistcancleVoByRegistNo(String registNo);
}

