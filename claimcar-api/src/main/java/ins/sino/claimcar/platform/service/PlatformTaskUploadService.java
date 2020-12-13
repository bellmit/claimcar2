package ins.sino.claimcar.platform.service;

import ins.sino.claimcar.carplatform.vo.CiClaimPlatformTaskVo;


public interface PlatformTaskUploadService {

	/**
	 * 自动上传平台
	 * @param platformTaskVo
	 */
	public void uploadPlatform(CiClaimPlatformTaskVo platformTaskVo);
	
	/**
	 * 根据ciClaimPlatformTask表ID来上传平台
	 * @param taskId
	 */
	public void uploadPlatformByTaskId(Long taskId);
	
}
