package ins.sino.claimcar.pinganUnion.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import ins.platform.common.web.util.SpringUtils;
import ins.platform.utils.Base64EncodedUtil;
import ins.sino.claimcar.claimcarYJ.Util.vo.HttpClientJsonUtil;
import ins.sino.claimcar.pinganUnion.enums.PingAnDataTypeEnum;
import ins.sino.claimcar.pinganUnion.util.PingAnMD5Utils;
import ins.sino.claimcar.pinganUnion.vo.PingAnDataNoticeVo;
import ins.sino.claimcar.pinganUnion.vo.ResultBean;
import ins.sino.claimcar.regist.service.RegistService;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.SpringProperties;

import javax.ws.rs.Path;
import java.util.HashMap;
import java.util.Map;


/**
 * @Description 平安联盟接口请求服务类
 * @Author liuys
 * @Date 2020/7/20 18:53
 */
@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"})
@Path("pingAnApiService")
public class PingAnApiServiceImpl implements PingAnApiService {
    private static Logger logger = LoggerFactory.getLogger(PingAnApiServiceImpl.class);

    @Autowired
    private RegistService registService;
    @Autowired
    private PingAnCompensateHandleService pingAnCompensateHandleService;

    /**
     * 平安联盟接口请求入口
     * @param pingAnDataNoticeVo
     * @return
     *
     */
    @Override
    public ResultBean service(PingAnDataNoticeVo pingAnDataNoticeVo) {
        ResultBean resultBean = new ResultBean();

        //根据数据通知环节Code获取对应的枚举类
        PingAnDataTypeEnum dataTypeEnum = PingAnDataTypeEnum.getEnumByCode(pingAnDataNoticeVo.getPushType());
        if (dataTypeEnum == null){
            resultBean = resultBean.fail("数据通知环节类型不存在");
            return resultBean;
        }

        try {
            //获取固定参数
            String siteCode = SpringProperties.getProperty("pingan_siteCode");//HS理赔系统分配给保险平台的接入编码
            String insuranceCompanyNo = SpringProperties.getProperty("pingan_insuranceCompanyNo");//保险公司
            String operatorUm = SpringProperties.getProperty("pingan_operatorUm");//操作员UM
            String requestUrl = SpringProperties.getProperty("pingan_url") + SpringProperties.getProperty("pingan_" + dataTypeEnum.getCode()+"_url");//请求URL
            //解析查询入参
            Map<String,Object> paramMap = JSON.parseObject(pingAnDataNoticeVo.getParamObj(),Map.class);
            //如果是08通知环节并且赔付类型为"赔付"，走理算自定义实现类
            if (dataTypeEnum == PingAnDataTypeEnum.CODE_08){
                String claimType = (String)paramMap.get("claimType");//赔付类型
				if ("1".equals(claimType)) {
					// 理算
					resultBean = pingAnCompensateHandleService.pingAnHandle(pingAnDataNoticeVo);
					return resultBean;
				} else {
					// 预付、垫付支付信息
					requestUrl = SpringProperties.getProperty("pingan_url") + SpringProperties.getProperty("pingan_" + dataTypeEnum.getCode() + "_01_url");// 请求URL
				}
            }

            //组装请求报文
            Map<String,Object> reqDataMap = new HashMap<String, Object>();
            reqDataMap.put("insuranceCompanyNo", insuranceCompanyNo);//保险公司
            reqDataMap.put("operatorUm", operatorUm);//操作员UM
            reqDataMap.put("paramObj", paramMap);//查询入参数据,第一个数据下发通知接口拿到
            String jsonString = JSON.toJSONString(reqDataMap);
            //对请求报文进数字签名
            String reqData = Base64EncodedUtil.encode(jsonString);
            String signature = PingAnMD5Utils.sign(siteCode + reqData);//数字签名
            //组装接口请求参数
            Map<String,String> param = new HashMap<String, String>();
            param.put("siteCode", siteCode);//HS理赔系统分配给保险平台的接入编码
            param.put("reqData", reqData);//请求数据报文，json格式，采用base64编码
            param.put("signature", signature);//数字签名
            String paramJsonString = JSON.toJSONString(param);
            //发起请求
            logger.info("{}接口请求参数-requestUrl={},明文reqData={},密文param={}", dataTypeEnum.getDesc(), requestUrl, jsonString, paramJsonString);
            String result = HttpClientJsonUtil.postUtf(requestUrl, paramJsonString ,"UTF-8");

            //解析返回报文
            JSONObject obj = JSON.parseObject(result);
            String code = obj.getString("code");//响应码
            String msg = obj.getString("msg");//响应具体信息
            String respData = obj.getString("respData");//响应数据，Base64编码，传给具体处理类需进行解码
            String respSignature = obj.getString("signature");//数字签名

            //请求成功进行数字签名验证
            if ("200".equals(code)) {
                //验证数字签名是否正确
                String data = code + msg + respData;
                Boolean isVerified = PingAnMD5Utils.verifySign(data, respSignature);
                //验签通过后，获取具体业务实现类进行处理业务
                if (isVerified) {
                    PingAnHandleService pingAnHandleService = (PingAnHandleService) SpringUtils.getObject(dataTypeEnum.getHandleClassName());
                    if (pingAnHandleService == null){
                        throw new IllegalArgumentException("未找到具体业务实现类");
                    }
                    //获取内部报案号
                    PrpLRegistVo prpLRegistVo = registService.findRegistByPaicReportNo(pingAnDataNoticeVo.getReportNo());
                    String registNo = prpLRegistVo != null ? prpLRegistVo.getRegistNo() : null;

                    resultBean = pingAnHandleService.pingAnHandle(registNo, pingAnDataNoticeVo, Base64EncodedUtil.decode(respData));
                } else {
                    resultBean = resultBean.fail("接口返回报文验签失败");
                    logger.error("{}-{}接口返回报文验签失败,id={},reportNo={}", dataTypeEnum.getCode(), dataTypeEnum.getDesc(), pingAnDataNoticeVo.getId(), pingAnDataNoticeVo.getReportNo());
                }
            }else {
                resultBean = resultBean.fail(code+":"+msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("{}-{}接口请求失败,id={},reportNo={},错误信息：{}", dataTypeEnum.getCode(), dataTypeEnum.getDesc(),pingAnDataNoticeVo.getId(), pingAnDataNoticeVo.getReportNo(), ExceptionUtils.getStackTrace(e));
            resultBean = resultBean.fail(e.getMessage());
        }

        return resultBean;
    }
}
