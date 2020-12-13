/******************************************************************************
 * CREATETIME : 2016年8月7日 下午3:22:28
 ******************************************************************************/
package ins.framework.dubbo.filter;


import ins.platform.common.service.utils.ServiceUserUtils;
import ins.platform.vo.SysUserVo;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.Filter;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.dubbo.rpc.RpcException;

/**
 * 将当前用户信息，放到RpcContext里面，以便服务端使用当前用户信息
 * @author ★LiuPing
 * @CreateTime 2016年8月7日
 */
@Activate(order = -9999)
public class UserInfoDubboFilter implements Filter {

	private static Logger logger = LoggerFactory.getLogger(UserInfoDubboFilter.class);
	@Override
	public Result invoke(Invoker<?> invoker,Invocation invocation) throws RpcException {
		// System.out.println(" ===exec UserInfoDubboFilte======= @"+new Date()+",=getMethodName="+invocation.getMethodName());
		RpcContext rpcContext = RpcContext.getContext();
		String userCode = rpcContext.getAttachment(ServiceUserUtils.Key_UserCode);
		//System.out.println(userCode +"=========================");
		if(userCode==null){// 当前服务线程线程没有用户，从Web层获取用户信息
			SecurityManager securityManager = ThreadContext.getSecurityManager();
			if(securityManager!=null){
				Subject subject = SecurityUtils.getSubject();
				SysUserVo userVo = (SysUserVo)subject.getPrincipal();
				if(userVo!=null){
					//System.out.println(userVo.getUserCode() +"=========================222");					
					rpcContext.setAttachment(ServiceUserUtils.Key_UserCode,userVo.getUserCode());
					rpcContext.setAttachment(ServiceUserUtils.Key_UserName,userVo.getUserName());
					rpcContext.setAttachment(ServiceUserUtils.Key_ComCode,userVo.getComCode());
				}
			}
		}else{// 当前线程已经有了用户，说明是服务层
				// isService = true;
		}

		try{
			Result result = invoker.invoke(invocation);
			return result;
		}
		finally{
			logger.debug("==UserInfoDubboFilter invoker("+invocation.getMethodName()+"),User="+rpcContext
					.getAttachment(ServiceUserUtils.Key_UserCode));
			
			RpcContext.getContext().clearAttachments();
		}

	}
	// if(result.hasException()&&isService){
	// rollBackTransaction();
	// logger.error(invocation.getMethodName()+"---result.hasException,rollBackTransaction");
	// }

	// 堆栈形式的调用本身会回滚事务，分布式事务回滚，这个处理不了
	// /**
	// * 回滚当前session的事务
	// * @modified: ☆LiuPing(2016年9月30日 ): <br>
	// */
	// private void rollBackTransaction() {
	//
	// HibernateTransactionManager transManager = (HibernateTransactionManager)Springs.getBean("transactionManager");
	// if(transManager!=null){
	// Session session = transManager.getSessionFactory().getCurrentSession();
	// Transaction transaction = session.getTransaction();
	// LocalStatus localStatus = transaction.getLocalStatus();
	// if(transaction.isActive()&&localStatus==LocalStatus.ACTIVE){// 有事务
	// transaction.rollback();// 将事务回滚
	// }
	// }
	// }
	

}
