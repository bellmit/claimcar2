/******************************************************************************
* CREATETIME : 2016年9月27日 下午9:39:01
******************************************************************************/
package ins.platform.common.service.utils;

import com.alibaba.dubbo.rpc.RpcContext;

/**
 * 服务层的用户工具类，仅服务层使用
 * @author ★LiuPing
 * @CreateTime 2016年9月27日
 */
public class ServiceUserUtils {

	public static final String Key_UserCode = "UserCode";
	public static final String Key_UserName = "UserName";
	public static final String Key_ComCode = "ComCode";


	public static String getUserCode() {
		RpcContext content = RpcContext.getContext();
		if(content==null) return null;
		return content.getAttachment(Key_UserCode);
	}

	public static String getComCode() {
		RpcContext content = RpcContext.getContext();
		if(content==null) return null;
		return content.getAttachment(Key_ComCode);
	}

	public static String getUserName() {
		RpcContext content = RpcContext.getContext();
		if(content==null) return null;
		return content.getAttachment(Key_UserName);
	}

}
