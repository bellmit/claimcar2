package ins.sino.claimcar.checkagency.service;

import ins.framework.dao.database.support.Page;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.manager.vo.PrpLPayCustomVo;
import ins.sino.claimcar.manager.vo.PrpdCheckBankMainVo;
import ins.sino.claimcar.manager.vo.PrpdcheckBankVo;
import ins.sino.claimcar.manager.vo.PrpdcheckServerVo;
import ins.sino.claimcar.manager.vo.PrpdcheckUserVo;



import java.util.List;
import java.util.Map;


public interface CheckAgencyService {
	
	public PrpdCheckBankMainVo findCheckBankByCode(String checkCode,String comCode);
	
	public PrpdCheckBankMainVo findCheckBankByComCode(String comCode);
	
	public PrpdCheckBankMainVo findCheckBankById(Long id);
	
	public List<PrpdCheckBankMainVo> findCheckBankListByComCode(String comCode);
	/*根据HQL查询PrpdCheckBankMainVo表*/
	public List<PrpdCheckBankMainVo> findCheckBankListByHql(String comCode);
	/**
	 * caseType=0是正常案件，1我方代查勘，2代我方查勘
	 * @param comCode
	 * @param caseType
	 * @return
	 */
	public List<PrpdCheckBankMainVo> findCheckBankListByCaseType(String comCode,String caseType);
	

	
	
	/**
	 * 通过用户查找是否是查勘人员
	 * @param userCode
	 * @return
	 */
	public PrpdCheckBankMainVo findCheckBankByUserCode(String userCode);
	

	

	
	public PrpdCheckBankMainVo findcheckById(String id);
	/**
	 * 通过ID查查勘费银行信息
	 * @param id
	 * @return
	 */
	public PrpdcheckBankVo findPrpdcheckBankVoById(Long id);
	
	public Map<String, String> findUserCode(String comCode, String userInfo,String gradeId);
	
	public PrpdCheckBankMainVo saveOrUpdateCheck(PrpdCheckBankMainVo checkMainVo,
	                               			PrpdcheckBankVo checkBankVo, List<PrpdcheckUserVo> checkUserVos,SysUserVo userVo) throws Exception;
	
	public void saveOrUpdateCheckServer(PrpdCheckBankMainVo recheckMainVo, List<PrpdcheckServerVo> checkServerVos);
	/**
	 * 查询查勘信息
	 * 
	 * @param checkMainVo
	 * @param start
	 * @param length
	 * @return
	 */
	public Page<PrpdCheckBankMainVo> find(PrpdCheckBankMainVo checkMainVo, int start, int length);
	
	public String existcheckCode(String checkCode);
	
	/**
	 * 通过查勘费主表查对应查勘机构的银行账号
	 * @param itermMianId
	 * @return
	 */
	public List<PrpdcheckBankVo> findPrpdcheckBankVosBycheckId(String checkBankMianId);
	/**
	 * 通过银行账号和查勘机构Id，查询该查勘机构下对应的银行账号
	 * @param checkCode
	 * @param AccountNo
	 * @return
	 */
	public PrpdcheckBankVo findPrpdcheckBankVosBycheckMainIdAndAccountNo(Long checkMainId,String accountNo);

	/**
	 * <pre>查询银行账号是否已经存在于某个公估机构名下</pre>
	 * @param accountNo
	 * @return
	 * @modified:
	 * ☆XiaoHuYao(2019年8月7日 下午6:22:29): <br>
	 */
	public String existAccountAtIntermBank(String accountNo);
	
}
