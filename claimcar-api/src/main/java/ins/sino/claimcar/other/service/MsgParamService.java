package ins.sino.claimcar.other.service;

import ins.sino.claimcar.other.vo.SysMsgParamVo;

import java.util.List;

public interface MsgParamService {

	/**
	 * 根据主键find SysMsgParamVo
	 * 
	 * @param id
	 * @return ParamVo
	 */
	public abstract List<SysMsgParamVo> findSysMsgParamVo();

}