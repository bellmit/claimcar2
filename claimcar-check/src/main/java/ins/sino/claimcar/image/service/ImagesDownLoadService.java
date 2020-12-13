/******************************************************************************
 * CREATETIME : 2015年12月29日 下午12:39:56
 ******************************************************************************/
package ins.sino.claimcar.image.service;

import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.sunyardimage.vo.common.ImageBaseRootVo;
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

}
