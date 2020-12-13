package ins.framework.factory;


import ins.platform.utils.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by Lundy on 2019/2/19.
 */
public class ExecutorFactory {
    public ExecutorFactory() {
        super();
    }
    private static Logger logger= LoggerFactory.getLogger(ExecutorFactory.class);
    private static ThreadPoolExecutor convertExecutor;

    /**
     * 默认线程数
     */
    private static final int CORE_POOL_SIZE=1;
    /**
     * 线程池最大线程数
     */
    private static final int MAXIMUM_POOL_SIZE=4;

    /**
     * 最大存活时间
     */
    private static final int KEEP_ALIVE_TIME=10000;

    private static final TimeUnit TIME_UNIT=TimeUnit.MILLISECONDS;

    /**
     * 初始化
     **/
    public static ThreadPoolExecutor initConverterExecutor(){
        if(!ObjectUtils.isEmpty(convertExecutor)){
            return convertExecutor;
        }
        logger.info("初始化需要的线程-开始...");
        convertExecutor=new ThreadPoolExecutor(CORE_POOL_SIZE,MAXIMUM_POOL_SIZE,KEEP_ALIVE_TIME,TIME_UNIT,new LinkedBlockingQueue<Runnable>(MAXIMUM_POOL_SIZE));
        logger.info("处理器数量/核心线程数:"+CORE_POOL_SIZE);
        logger.info("最大线程数/队列数:"+MAXIMUM_POOL_SIZE);
        logger.info("存活时间:"+KEEP_ALIVE_TIME);
        logger.info("初始化需要的线程-结束...");
        return convertExecutor;
    }

}
