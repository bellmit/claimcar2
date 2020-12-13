package ins.sino.claimcar.carYxImage.service;









import java.io.File;
import java.util.List;
import java.util.Map;

import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.TypeNode;
public interface CarXyImageService {

/**
 * 影像查看报文组织
 * @param user
 * @param role
 * @param bussNo
 * @return
 */
 public String getReqQueryParam(SysUserVo user,String role,String bussNo);
 /**
  * 
  * @param registNo
  * @return
  */
 public TypeNode buildTree(String registNo);
 
 /**
  * 影像查看报文组织
  * @param user
  * @param role
  * @param bussNo
  * @param flags
  * @param handlerStatus
  * @return
  */
  public String getReqUploadParam(SysUserVo user,String role,String bussNo,String flags,String handlerStatus);
  
  /**
   * 影像资源请求接口
   * @param user
   * @param bussNo
   * @return
   */
   public String reqResourceFromXy(SysUserVo user,String bussNo,String url);
   
   /**
	 * 上传人伤赔偿标准图到信雅达影像系统
	 * @param bussNo
	 * @param File
	 * @param flagType类型
	 * @return
	 */
	public String upImageToXyd(String bussNo,Map<String,File> maps,String flagType,String path);
	/**
	 * 发票图片上传到信雅达影像系统(一般实赔与预付)
	 * @param bussNo
	 * @param maps
	 * @param flagType
	 * @param bussTypeMaps--业务类型
	 * @param codeMaps--费用编码
	 * @param payIdMaps--收款人id
	 * @return
	 */
	public String upBillImageToXyd(String bussNo,Map<String,File> maps,String flagType,Map<String,String> bussTypeMaps,Map<String,String> codeMaps,Map<String,String> payIdMaps,String path);
	/**
	 * 发票图片上传到信雅达影像系统(公估费与查勘费)
	 * @param bussNo
	 * @param maps
	 * @param flagType
	 * @param bussTypeMaps
	 * @param path
	 * @return
	 */
	public String upasscheckBillImageToXyd(String bussNo,Map<String,File> maps,String flagType,Map<String,String> bussTypeMaps,String path);
	
	/**
	 * 上传专用发票信息到信雅达影像系统
	 * @param bussNo
	 * @param urls
	 * @param flagType类型
	 * @return
	 */
	public String upBillImageInfoToXyd(String bussNo,List<String> urls,String flagType,String path);
}
