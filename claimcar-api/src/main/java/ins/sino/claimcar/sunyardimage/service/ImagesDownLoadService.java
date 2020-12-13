/******************************************************************************
 * CREATETIME : 2015年12月29日 下午12:39:56
 ******************************************************************************/
package ins.sino.claimcar.sunyardimage.service;

import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.sunyardimage.vo.common.ImageBaseRootVo;
import ins.sino.claimcar.sunyardimage.vo.response.ResNumRootVo;
import ins.sino.claimcar.sunyardimage.vo.response.ResRootVo;



/**
 * 影像资源请求接口
 * <pre></pre>
 * @author ★zhujunde
 */
public interface ImagesDownLoadService{
	/**
	 * 影像资源请求接口
	 * <pre></pre>
	 * @param vo
	 * @param url
	 * @param requestType
	 * @return
	 * @modified:
	 * ☆zhujunde(2019年3月8日 下午6:18:09): <br>
	 */
    public ResRootVo imagesDownLoad(ImageBaseRootVo vo,String url,String requestType);
    /**
     * 通过接口查询某个批次下每个资料类型拥有的影像数量
     * @param user
     * @param role
     * @param bussNo
     * @param assessorId
     * @param url
     * @param appName
     * @param appCode
     * @return
     */
    public ResNumRootVo getReqImageNum(SysUserVo user,String role,String bussNo,String assessorId,String url,String appName,String appCode);

}
