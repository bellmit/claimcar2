package ins.sino.claimcar.manager.service;

import ins.framework.dao.database.support.Page;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.manager.vo.PrpLPayCustomVo;
import ins.sino.claimcar.manager.vo.PrpLRepairFactoryVo;
import ins.sino.claimcar.manager.vo.PrpdCheckBankMainVo;
import ins.sino.claimcar.manager.vo.PrpdIntermBankVo;
import ins.sino.claimcar.manager.vo.PrpdIntermMainVo;
import ins.sino.claimcar.manager.vo.PrpdIntermServerVo;
import ins.sino.claimcar.manager.vo.PrpdIntermUserVo;
import ins.sino.claimcar.manager.vo.PrpdcheckBankVo;
import ins.sino.claimcar.manager.vo.PrpdcheckServerVo;
import ins.sino.claimcar.manager.vo.PrpdcheckUserVo;
import ins.sino.claimcar.manager.vo.SysMsgContentVo;

import java.util.List;
import java.util.Map;


public interface ManagerService {
	
	public PrpdIntermMainVo findIntermByCode(String intermCode,String comCode);
	
	public PrpdIntermMainVo findIntermByComCode(String comCode);
	
	public PrpdIntermMainVo findIntermById(Long id);
	
	public List<PrpdIntermMainVo> findIntermListByComCode(String comCode);
	/*根据HQL查询PrpdIntermMainVo表*/
	public List<PrpdIntermMainVo> findIntermListByHql(String comCode);
	
    public PrpdCheckBankMainVo findCheckByCode(String checkCode,String comCode);
	
	public PrpdCheckBankMainVo findCheckByComCode(String comCode);
	
	public PrpdCheckBankMainVo findCheckById(Long id);
	
	public List<PrpdCheckBankMainVo> findCheckListByComCode(String comCode);
	/*根据HQL查询PrpdCheckBankMainVo表*/
	public List<PrpdCheckBankMainVo> findCheckListByHql(String comCode);
	
	/**
	 * caseType=0是正常案件，1我方代查勘，2代我方查勘
	 * @param comCode
	 * @param caseType
	 * @return
	 */
	public List<PrpdIntermMainVo> findIntermListByCaseType(String comCode,String caseType);
	public List<PrpdCheckBankMainVo> findCheckListByCaseType(String comCode,String caseType);
	public Page<PrpLRepairFactoryVo> findRepariFactory(PrpLRepairFactoryVo PrpLRepairFactoryVo,int start,int length,String index);
	
	/**
	 * 根据出险地区、品牌查询出所有修理厂信息
	 */
	public List<PrpLRepairFactoryVo> findFactoryByArea(String areaCode,String brandName);
	
	/**
	 * 查询修理厂信息 ☆yangkun(2016年3月8日 下午8:00:41): <br>
	 */
	public PrpLRepairFactoryVo findRepariFactoryByCode(String factoryCode,String factoryType);
	
	public PrpLRepairFactoryVo findRepariFactoryById(String factoryId);
	
	
	/**
	 * 通过用户查找是否是公估人员
	 * @param userCode
	 * @return
	 */
	public PrpdIntermMainVo findIntermByUserCode(String userCode);
	
	/**
	 * 通过用户查找是否是查勘人员
	 * @param userCode
	 * @return
	 */
	public PrpdCheckBankMainVo findCheckByUserCode(String userCode);
	
	/**
	 * 根据id查询收款人信息
	 * @param id
	 */
	public PrpLPayCustomVo findPayCustomVoById(Long id);
	
	/**
	 * 根据报案号查询收款人信息List
	 * @param registNo
	 */
	public List<PrpLPayCustomVo> findPayCustomVoByRegistNo(String registNo);
	
	/**
	 * 根据msgReceiver查询相关留言回复表，根据留言回复表查询出回复给该用户的留言
	 */
	public List<SysMsgContentVo> findMsgByUser(String userCode);
	
	public PrpdIntermMainVo findIntermById(String id);
	
	public PrpdCheckBankMainVo findCheckById(String id);
	/**
	 * 通过ID查公估费银行信息
	 * @param id
	 * @return
	 */
	public PrpdIntermBankVo findPrpdIntermBankVoById(Long id);
	
	/**
	 * 通过ID查查勘费银行信息
	 * @param id
	 * @return
	 */
	public PrpdcheckBankVo findPrpdcheckBankVoById(Long id);
	
	public Map<String, String> findUserCode(String comCode, String userInfo,String gradeId);
	
	public PrpdIntermMainVo saveOrUpdateInterm(PrpdIntermMainVo intermMainVo,PrpdIntermBankVo intermBankVo, List<PrpdIntermUserVo> intermUserVos,SysUserVo userVo) throws Exception;

	public PrpdCheckBankMainVo saveOrUpdatecheckBank(PrpdCheckBankMainVo checkBankMainVo,PrpdcheckBankVo checkBankVo, List<PrpdcheckUserVo> checkUserVos,SysUserVo userVo) throws Exception;
	
	public void saveOrUpdateIntermServer(PrpdIntermMainVo reIntermMainVo, List<PrpdIntermServerVo> intermServerVos);
	
	public void saveOrUpdateCheckServer(PrpdCheckBankMainVo reCheckBankMainVo, List<PrpdcheckServerVo> checkServerVos);
	/**
	 * 查询公估信息
	 * 
	 * @param intermMainVo
	 * @param start
	 * @param length
	 * @return
	 */
	public Page<PrpdIntermMainVo> find(PrpdIntermMainVo intermMainVo, int start, int length);
	
	/**
	 * 查询查勘信息
	 * 
	 * @param PrpdCheckBankMainVo
	 * @param start
	 * @param length
	 * @return
	 */
	public Page<PrpdCheckBankMainVo> find(PrpdCheckBankMainVo checkBankMainVo, int start, int length);
	
	public String existIntermCode(String intermCode);
	
	public PrpLPayCustomVo adjustExistSamePayCusDifName(PrpLPayCustomVo payCustomVo);
	/**
	 * 通过公估费主表查对应公估机构的银行账号
	 * @param itermMianId
	 * @return
	 */
	public List<PrpdIntermBankVo> findPrpdIntermBankVosByIntermId(String itermMianId);
	
	/**
	 * 通过查勘费主表查对应查勘机构的银行账号
	 * @param PrpdcheckBankVo
	 * @return
	 */
	public List<PrpdcheckBankVo> findPrpdcheckBankVosByCheckId(String checkMianId);
	/**
	 * 通过银行账号和公估机构Id，查询该公估机构下对应的银行账号
	 * @param intermCode
	 * @param AccountNo
	 * @return
	 */
	public PrpdIntermBankVo findPrpdIntermBankVosByIntermMainIdAndAccountNo(Long intermMainId,String accountNo);
	
	/**
	 * 通过银行账号和查勘机构Id，查询该查勘机构下对应的银行账号
	 * @param checkMainId
	 * @param AccountNo
	 * @return
	 */
	public PrpdcheckBankVo findPrpdcheckBankVosByCheckMainIdAndAccountNo(Long checkMainId,String accountNo);

	/**
	 * 根据查勘费类型进行查勘费补录
	 * @param taskType
	 * @param mainId
	 * @param userVo 
	 * @modified:
	 * ☆XiaoHuYao(2019年8月13日 下午6:30:13): <br>
	 */
	public void saveCheckFee(String taskType,Long mainId,String registNo, SysUserVo userVo);

	/**
	 * 根据公估费类型进行公估费任务补录
	 * @param taskType
	 * @param mainId
	 * @param registNo
	 * @param userVo
	 * @modified:
	 * ☆XiaoHuYao(2019年8月14日 下午2:31:54): <br>
	 */
	public void saveAssessors(String taskType,Long mainId,String registNo,SysUserVo userVo);

	/**
	 * 查看公估录入的银行卡号是否已经存在查勘机构了
	 * @param accountNo
	 * @return
	 * @modified:
	 * ☆XiaoHuYao(2019年8月26日 上午11:11:32): <br>
	 */
	String existAccountAtCheckmBank(String accountNo);

	/**
	 * 判断是否已经生成了查勘费任务
	 * @param taskType
	 * @param mainId
	 * @param registNo
	 * @param userCode 
	 * @return
	 * @modified:
	 * ☆XiaoHuYao(2019年8月27日 下午4:56:03): <br>
	 */
	public boolean findExistACheck(String taskType,Long mainId,String registNo, String userCode);

	/**
	 * 判断是否已经生成了公估费任务
	 * @param taskType
	 * @param mainId
	 * @param registNo
	 * @param userCode
	 * @return
	 * @modified:
	 * ☆XiaoHuYao(2019年8月27日 下午5:19:52): <br>
	 */
	public boolean findExistAsessor(String taskType,Long mainId,String registNo,String userCode);
	
	/**
	 * 判断是否需要提交总公司审核
	 * 用于在人伤首次跟踪提交人伤跟踪审核、人伤后续跟踪提交人伤跟踪审核、人伤首次跟踪提交人伤费用审核、人伤后续跟踪提交人伤费用审核、
	 * 车辆定损提交核价、车辆定损提交核损、车辆核价提交核损、财产定损提交财产核损、理算提交核赔、理算冲销提交核赔
	 * 预付提交核赔、预付冲销提交核赔和垫付提交核赔等节点
	 * @param registNo
	 * @param taskid
	 * @return
	 * @throws Exception 
	 * @modified:
	 * ☆XiaoHuYao(2019年8月29日 下午2:39:32): <br>
	 */
	public String isSubmitHeadOffice(Map<String,Object> params) throws Exception;
}
