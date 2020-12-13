package ins.sino.claimcar.mail.service;

import java.util.List;

import ins.framework.common.ResultPage;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.claim.vo.PrpsmsEmailVo;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.mail.vo.PrpLMailModelVo;
import ins.sino.claimcar.manager.vo.PrpdAddresseeVo;
import ins.sino.claimcar.manager.vo.PrpdEmailVo;

public interface MailModelService {

	
	public ResultPage<PrpLMailModelVo> findAllSysMsgModelByHql(PrpLMailModelVo mailModelVo, int start, int length);
	
	public PrpLMailModelVo findMailModelByPk(Long id);
	
	public void deleteMailModelByPk(Long id);
	
	public String activOrCancel(Long id,String validFlag);
	
	public void saveOrUpdateMailModel(PrpLMailModelVo prpLMailModelVo,SysUserVo userVo);
	
	public List<PrpdEmailVo> findMailAddr(List<String> comCodes,String caseType);
	
	/**
	 * 把收件人集合转换成页面表格
	 * @param prpdEmailVoList
	 * @return
	 */
	public String getMailTable(List<PrpdEmailVo> prpdEmailVoList);

	/**
	 * <pre></pre>
	 * @param prpdEmailVo
	 * @param user
	 * @modified:
	 * ☆XiaoHuYao(2019年3月14日 下午6:11:06): <br>
	 */
	public void saveOrUpdateEmailVo(PrpdEmailVo prpdEmailVo,SysUserVo user);

	/**
	 * <pre></pre>
	 * @param prpdEmailVo
	 * @param start
	 * @param length
	 * @return
	 * @modified:
	 * ☆XiaoHuYao(2019年3月14日 下午6:50:46): <br>
	 */
	public ResultPage<PrpdEmailVo> searchSendInfo(PrpdEmailVo prpdEmailVo,Integer start,Integer length);

	/**
	 * <pre></pre>
	 * @param comCode 
	 * @return
	 * @modified:
	 * ☆XiaoHuYao(2019年3月14日 下午10:14:12): <br>
	 */
	public List<PrpdEmailVo> findAllSenderInfo(String comCode);


	/**
	 * <pre></pre>
	 * @param list
	 * @param userCode
	 * @modified:
	 * ☆XiaoHuYao(2019年3月14日 下午10:35:51): <br>
	 */
	void updateSenderInfo(List<PrpdEmailVo> list,String userCode);

	/**
	 * <pre></pre>
	 * @param prpsmsEmailVo
	 * @param start
	 * @param length
	 * @return
	 * @throws Exception 
	 * @modified:
	 * ☆XiaoHuYao(2019年3月19日 下午3:00:55): <br>
	 */
	public ResultPage<PrpsmsEmailVo> findAllSmsEmailByHql(PrpsmsEmailVo prpsmsEmailVo,Integer start,Integer length) throws Exception;
	/**
	 * <pre></pre>
	 * @param prpsmsEmailVo
	 * @param start
	 * @param length
	 * @return
	 * @throws Exception 
	 * @modified:
	 * ☆XiaoHuYao(2019年3月19日 下午3:00:55): <br>
	 */
	public void insertSmsEmail(PrpsmsEmailVo prpsmsEmailVo,SysUserVo userVo);
	
	
	public List<PrpdEmailVo> sendMail(PrpLWfTaskVo wfTaskVo,SysUserVo userVo);

	public List<PrpdEmailVo> sendMailNew(PrpLWfTaskVo wfTaskVo,SysUserVo userVo);
	/**
	 * <pre></pre>
	 * @param id
	 * @param modelType
	 * @param validFlag
	 * @param comCode
	 * @return
	 * @modified:
	 * ☆XiaoHuYao(2019年4月14日 上午11:14:56): <br>
	 */
	public boolean existsMailModel(Long id,String modelType,String comCode);

	/** 通过类型查询 */
	public List<PrpdEmailVo> findMailAddressByCaseType(String caseType);
}
