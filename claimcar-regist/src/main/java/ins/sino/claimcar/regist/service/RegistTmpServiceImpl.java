package ins.sino.claimcar.regist.service;

import ins.framework.dao.database.DatabaseDao;
import ins.framework.dao.database.support.QueryRule;
import ins.framework.utils.Beans;
import ins.platform.common.service.facade.BillNoService;
import ins.platform.common.service.utils.ServiceUserUtils;
import ins.sino.claimcar.regist.po.*;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;
import ins.sino.claimcar.regist.vo.PrpLTmpCItemCarVo;
import ins.sino.claimcar.regist.vo.PrpLTmpCItemKindVo;
import ins.sino.claimcar.regist.vo.PrpLTmpCMainVo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;


@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"},timeout = 1000000)
@Path(value = "registTmpService")
public class RegistTmpServiceImpl implements RegistTmpService {

	@Autowired
	DatabaseDao databaseDao;
	@Autowired
	BillNoService billNoService;

	/* (non-Javadoc)
	 * @see ins.sino.claimcar.regist.service.RegistTmpService#registTmpSave(ins.sino.claimcar.regist.vo.PrpLTmpCMainVo, java.util.List, ins.sino.claimcar.regist.vo.PrpLTmpCItemCarVo)
	 */
	@Override
	public String registTmpSave(PrpLTmpCMainVo tmpCMainVo,
			List<PrpLTmpCItemKindVo> tmpCItemKindVos,
			PrpLTmpCItemCarVo tmpCItemCarVo,
			PrpLRegistVo prpLRegistVo) {
		List<String> riskCodeList = new ArrayList<String>();
		// 设置更新创建时间
		Date date = new Date();
		String user = ServiceUserUtils.getUserCode();
		if(prpLRegistVo!=null){
		    if("1".equals(prpLRegistVo.getIsQuickCase())){
	            user = "AUTO";
	        }
		}	
		if (tmpCMainVo.getRiskCode().length() > 4) {
			String[] riskCodes = new String[] {};
			riskCodes = tmpCMainVo.getRiskCode().split(",");
			// 如果选中了商业和交强两个险种就生成两条报案号相同保单号不同的数据
			riskCodeList.add(riskCodes[0]);
			riskCodeList.add(riskCodes[1]);
		} else {
			riskCodeList.add(tmpCMainVo.getRiskCode());

		}
		// 生成临时报案号
		String registTmpNo = billNoService.getRegistNo(tmpCMainVo.getComCode(),
				riskCodeList.get(0));
		for (String riskCode : riskCodeList) {
			// 生成临时保单号并赋值给Cmain表对应字段
			String policyTmpNo = billNoService.getPolicyTmp(
						tmpCMainVo.getComCode(), riskCode);

			tmpCMainVo.setCreateTime(date);
			tmpCMainVo.setCreateUser(user);
			tmpCMainVo.setUpdateTime(date);
			tmpCMainVo.setUpdateUser(user);
			tmpCMainVo.setRiskCode(riskCode);
			tmpCMainVo.setClassCode(riskCode.substring(0, 2));
			tmpCMainVo.setPolicySort("11");
			tmpCMainVo.setBusinessNature("11");
			tmpCMainVo.setMakeCom("11");
			tmpCMainVo.setHandlerCode("11");
			tmpCMainVo.setOperatorCode("11");
			tmpCMainVo.setPolicyNo(policyTmpNo);
			tmpCMainVo.setRegistNo(registTmpNo);

			List<PrpLTmpCItemCar> tmpCItemCarPos = new ArrayList<PrpLTmpCItemCar>();
			List<PrpLTmpCItemKind> tmpCItemKindPos = new ArrayList<PrpLTmpCItemKind>();
			PrpLTmpCMain tmpCMainPo = Beans.copyDepth().from(tmpCMainVo)
					.to(PrpLTmpCMain.class);

			tmpCItemCarVo.setItemNo(Long.valueOf(1));
			tmpCItemCarVo.setCreateTime(date);
			tmpCItemCarVo.setCreateUser(user);
			tmpCItemCarVo.setUpdateTime(date);
			tmpCItemCarVo.setUpdateUser(user);
			tmpCItemCarVo.setRiskCode(riskCode);
			tmpCItemCarVo.setPolicyNo(policyTmpNo);
			tmpCItemCarVo.setRegistNo(registTmpNo);
			PrpLTmpCItemCar tmpCItemCarPo = Beans.copyDepth()
					.from(tmpCItemCarVo).to(PrpLTmpCItemCar.class);
			tmpCItemCarPo.setPrpLTmpCMain(tmpCMainPo);
			tmpCItemCarPos.add(tmpCItemCarPo);

			for (PrpLTmpCItemKindVo tmpCItemKindVo : tmpCItemKindVos) {
				if (tmpCItemKindVo != null) {
					tmpCItemKindVo.setCreateTime(date);
					tmpCItemKindVo.setCreateUser(user);
					tmpCItemKindVo.setUpdateTime(date);
					tmpCItemKindVo.setUpdateUser(user);
					tmpCItemKindVo.setRiskCode(riskCode);
					tmpCItemKindVo.setPolicyNo(policyTmpNo);
					tmpCItemKindVo.setRegistNo(registTmpNo);
					PrpLTmpCItemKind tmpCItemKindPo = Beans.copyDepth()
							.from(tmpCItemKindVo).to(PrpLTmpCItemKind.class);
					tmpCItemKindPo.setPrpLTmpCMain(tmpCMainPo);
					tmpCItemKindPos.add(tmpCItemKindPo);
				}
			}


			// 维护主子表关系
			tmpCMainPo.setPrpCItemCars(tmpCItemCarPos);
			tmpCMainPo.setPrpCItemKinds(tmpCItemKindPos);

			databaseDao.save(PrpLTmpCMain.class, tmpCMainPo);
		}
		return registTmpNo;

	}

	/* (non-Javadoc)
	 * @see ins.sino.claimcar.regist.service.RegistTmpService#findTempPolicyByRegistNo(java.lang.String)
	 */
	@Override
	public List<PrpLCMainVo> findTempPolicyByRegistNo(String registNo) {
		List<PrpLCMainVo> returnTempPolicyVos = new ArrayList<PrpLCMainVo>();
		QueryRule qr = QueryRule.getInstance();
		qr.addEqual("registNo", registNo);
		List<PrpLTmpCMain> returnTempPolicyPos = databaseDao.findAll(PrpLTmpCMain.class, qr);
		for (PrpLTmpCMain po : returnTempPolicyPos) {
			PrpLCMainVo vo = Beans.copyDepth().from(po).to(PrpLCMainVo.class);
			//无保转有保点击地图人员报空赋值
			if(vo.getPrpCInsureds()!=null&&vo.getPrpCInsureds().size()>0){
				vo.getPrpCInsureds().get(0).setInsuredName(po.getInsuredName());
			}
			
			returnTempPolicyVos.add(vo);
		}
		return returnTempPolicyVos;
	}

	/* (non-Javadoc)
	 * @see ins.sino.claimcar.regist.service.RegistTmpService#findAreadictByPolicyNo(java.lang.String)
	 */
	@Override
	public List<PrpLCMainVo> findAreadictByPolicyNo(String policyNo) {
		List<PrpLCMainVo> returnPrpLCMainVos = new ArrayList<PrpLCMainVo>();
		QueryRule qr = QueryRule.getInstance();
		qr.addEqual("policyNo", policyNo);
		List<PrpCMain> prpCMains = databaseDao.findAll(PrpCMain.class, qr);
		for (PrpCMain po : prpCMains) {
			PrpLCMainVo vo = Beans.copyDepth().from(po).to(PrpLCMainVo.class);
			
			returnPrpLCMainVos.add(vo);
		}
		return returnPrpLCMainVos;
	}

	/* (non-Javadoc)
	 * @see ins.sino.claimcar.regist.service.RegistTmpService#deleteByRegistNoAndRiskCode(java.lang.String, java.lang.String)
	 */
	@Override
	public List<PrpLCMainVo> deleteByRegistNoAndRiskCode(String registNo,String riskCode) {
		List<PrpLCMainVo> returnTempPolicyVos = new ArrayList<PrpLCMainVo>();
		QueryRule qr = QueryRule.getInstance();
		qr.addEqual("registNo", registNo);
		qr.addLike("riskCode", riskCode+"%");
		List<PrpLTmpCMain> returnTempPolicyPos = databaseDao.findAll(PrpLTmpCMain.class, qr);
		for (PrpLTmpCMain po : returnTempPolicyPos) {
			/*PrpLCMainVo vo = Beans.copyDepth().from(po).to(PrpLCMainVo.class);
			returnTempPolicyVos.add(vo);*/
			databaseDao.deleteByPK(PrpLTmpCMain.class, po.getId());
		}
		return returnTempPolicyVos;
	}

	@Override
	public List<PrpLCMainVo> queryByClaimsequenceNo(String claimsequenceNo) {
		List<PrpLCMainVo> returnPrpLCMainVos = new ArrayList<PrpLCMainVo>();
		QueryRule qr = QueryRule.getInstance();
		qr.addEqual("claimSequenceNo", claimsequenceNo);
		List<PrpLCMain> prpLCMains = databaseDao.findAll(PrpLCMain.class, qr);
		for (PrpLCMain po : prpLCMains) {
			PrpLCMainVo vo = Beans.copyDepth().from(po).to(PrpLCMainVo.class);

			returnPrpLCMainVos.add(vo);
		}
		return returnPrpLCMainVos;
	}
}
