package ins.sino.claimcar.carchild.schedule.service;

import ins.framework.lang.Springs;
import ins.sino.claimcar.carchild.service.spring.CarchildServiceImpl;
import ins.sino.claimcar.carchild.vo.PrplCarchildScheduleVo;
import ins.sino.claimcar.carchild.vo.ScheduleBodyVo;
import ins.sino.claimcar.carchild.vo.ScheduleInfoReqVo;
import ins.sino.claimcar.carchild.vo.ScheduleInfoResVo;
import ins.sino.claimcar.carchild.vo.ScheduleTaskInfoVo;
import ins.sino.claimcar.carchildCommon.vo.CarchildHeadVo;
import ins.sino.claimcar.carchildCommon.vo.CarchildResponseHeadVo;

import java.text.SimpleDateFormat;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.common.utils.Assert;
import com.sinosoft.api.service.ServiceInterface;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import com.thoughtworks.xstream.io.xml.XppDriver;

/**
 * 调度结果通知接口
 * 
 * <pre></pre>
 * @author ★LinYi
 */
public class ScheduleInfoServiceImpl implements ServiceInterface {

    private static Logger logger = LoggerFactory.getLogger(ScheduleInfoServiceImpl.class);
    
    @Autowired
    CarchildServiceImpl carchildServiceImpl;

    @Override
    public Object service(String arg0,Object arg1) {
        init();
        XStream stream = new XStream(new XppDriver(new XmlFriendlyNameCoder("-_","_")));
        stream.autodetectAnnotations(true);
        stream.setMode(XStream.NO_REFERENCES);
        stream.aliasSystemAttribute(null,"class");// 去掉 class属性
        stream.processAnnotations(ScheduleInfoReqVo.class);
        ScheduleInfoReqVo reqPacket = (ScheduleInfoReqVo)arg1;
        String xml = stream.toXML(reqPacket);
        logger.info("调度结果通知接口接收报文: \n"+xml);
        Assert.notNull(reqPacket," 请求信息为空  ");
        CarchildHeadVo head = reqPacket.getHead();
        if( (!"CT_002".equals(head.getRequestType())) && (!"MTA_002".equals(head.getRequestType()))|| !"claim_user".equals(head.getUser())|| !"claim_psd".equals(head.getPassWord())){
            throw new IllegalArgumentException("请求头参数错误  ");
        }
        ScheduleInfoResVo resVo = new ScheduleInfoResVo();

        CarchildResponseHeadVo responseHead = new CarchildResponseHeadVo();
        try{
            ScheduleBodyVo body = reqPacket.getBody();

            ScheduleTaskInfoVo taskInfo = body.getTaskInfo();
            
            //checkScheduleInfo(taskInfo);
            //保存车童网/民太安调度信息
            PrplCarchildScheduleVo prplCarchildScheduleVo = null;
            if(taskInfo!=null){
                prplCarchildScheduleVo = new PrplCarchildScheduleVo();
                prplCarchildScheduleVo.setRegistNo(taskInfo.getRegistNo());
                prplCarchildScheduleVo.setTaskId(taskInfo.getTaskId());
                prplCarchildScheduleVo.setNodeType(taskInfo.getNodeType());
                prplCarchildScheduleVo.setHandlerName(taskInfo.getHandlerName());
                prplCarchildScheduleVo.setHandlerPhone(taskInfo.getHandlerPhone());
                prplCarchildScheduleVo.setTraceLink(taskInfo.getTraceLink());
                prplCarchildScheduleVo.setOptionType(taskInfo.getOptionType());
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                prplCarchildScheduleVo.setTimeStamp(formatter.parse(taskInfo.getTimeStamp()));
                String requestType = reqPacket.getHead().getRequestType();
                String sourceType = requestType.substring(0,requestType.indexOf("_"));
                prplCarchildScheduleVo.setSourceType(sourceType);
            }
            carchildServiceImpl.saveScheduleInformation(prplCarchildScheduleVo);
            
            responseHead.setResponseType(head.getRequestType());
            responseHead.setErrNo("1");
            responseHead.setErrMsg("Success");
            resVo.setHead(responseHead);
        }
        catch(Exception e){
            responseHead.setResponseType(head.getRequestType());
            responseHead.setErrNo("2");
            responseHead.setErrMsg(e.getMessage());
            resVo.setHead(responseHead);
            logger.info("====================调度结果通知接口报错信息："+e.getMessage());
            e.printStackTrace();
        }

        stream.processAnnotations(ScheduleInfoResVo.class);
        logger.info("调度结果通知接口返回报文=========：\n"+stream.toXML(resVo));
        return resVo;
    }

    
    
    /**
     * 校验
     * <pre></pre>
     * @param taskInfo
     * @modified:
     * ☆LinYi(2017年10月10日 下午2:46:19): <br>
     */
    private void checkScheduleInfo(ScheduleTaskInfoVo taskInfo) {

        if(StringUtils.isBlank(taskInfo.getRegistNo())){
            throw new IllegalArgumentException("报案号不能为空");
        }
        if(StringUtils.isBlank(taskInfo.getTaskId())){
            throw new IllegalArgumentException("任务ID不能为空");
        }
        if(StringUtils.isBlank(taskInfo.getNodeType())){
            throw new IllegalArgumentException("任务节点类型不能为空");
        }
        if(StringUtils.isBlank(taskInfo.getHandlerName())){
            throw new IllegalArgumentException("公估师姓名不能为空");
        }
        if(StringUtils.isBlank(taskInfo.getHandlerPhone())){
            throw new IllegalArgumentException("公估师电话不能为空");
        }
        if(StringUtils.isBlank(taskInfo.getOptionType())){
            throw new IllegalArgumentException("操作不能为空");
        }
        if(taskInfo.getTimeStamp()==null){
            throw new IllegalArgumentException("时间戳不能为空");
        }

    }

    private void init() {
        if(carchildServiceImpl==null){
            carchildServiceImpl = (CarchildServiceImpl)Springs.getBean(CarchildServiceImpl.class);
        }
    }
}
