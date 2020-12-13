package ins.sino.claimcar.GDPower.service;

import com.alibaba.dubbo.common.utils.Assert;
import com.sinosoft.api.service.ServiceInterface;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import com.thoughtworks.xstream.io.xml.XppDriver;
import ins.framework.lang.Springs;
import ins.platform.common.service.facade.BaseDaoService;
import ins.platform.utils.ClaimBaseCoder;
import ins.platform.utils.DateUtils;
import ins.platform.utils.SqlJoinUtils;
import ins.sino.claimcar.GDPower.utils.MD5Utils;
import ins.sino.claimcar.GDPower.vo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description:
 * @Author: Gusheng Huang
 * @Date: Create in 下午3:56 19-2-19
 * @Modified By:
 */
public class GDPowerGridClaimImpl  implements ServiceInterface {
    private static Logger logger = LoggerFactory.getLogger(GDPowerGridClaimImpl.class);

    @Autowired
    private BaseDaoService baseDaoService;

    @Override
    public Object service(String s, Object o) {
        init();
        GDPowerResponse response = new GDPowerResponse();
        String requestType = "";
        try {
            XStream stream = new XStream(new XppDriver(new XmlFriendlyNameCoder("-_", "_")));
            stream.autodetectAnnotations(true);
            stream.setMode(XStream.NO_REFERENCES);
            stream.aliasSystemAttribute(null, "class");// 去掉 class属性
            stream.processAnnotations(GDPowerRequest.class);
            stream.processAnnotations(GDPowerResponse.class);
            GDPowerRequest reqVo = (GDPowerRequest) o;
            Assert.notNull(reqVo,"请求信息为空 ");
            Assert.notNull(reqVo.getReqHead(),"请求信息头部为空 ");
            ReqHead head = reqVo.getReqHead();
            requestType = head.getRequestType();
            if (!"GDInsure003".equals(head.getRequestType())) {
                throw new IllegalArgumentException("请求头参数错误  ");
            }
            Assert.notNull(reqVo.getReqBody(), "请求信息Body信息为空 ");
            ReqBody body = reqVo.getReqBody();

            // 校验Token
            String decode = MD5Utils.getMD5(body.getNewDate() + MD5Utils.GD_POWER_KEY);
            if (!decode.equals(body.getToken())) {
                throw new IllegalArgumentException("Token校验不一致");
            }

            // 请求20s内有效
            long disSes = new Date().getTime() - Long.parseLong(body.getNewDate());
            int  d = (int) (disSes / (1000));
            if(d > 20 || d < -20){
                throw new Exception("本次请求已过期, 限制有效时间为20秒");
            }



            // 处理数据
            String reqXml= ClaimBaseCoder.objToXml(reqVo);//请求的Xml
            logger.info("广东电网理赔数据同步接口----->"+reqXml);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            Date startTime = dateFormat.parse(body.getStartTime().trim());
            Date endTime = dateFormat.parse(body.getEndTime().trim());

            // 查询起止时间不能超过三天
            long div = startTime.getTime() - endTime.getTime();
            int timeDiff = (int) (div / (1000 * 60));
            if (timeDiff >= 4321 || d <= -4320) {
                throw new Exception("查询起止时间不能超过三天");
            }

            List<ResData> resData = findGDPowerGridCarClaimInfo(startTime, endTime);
            response = initResponse(resData, requestType);
            // 返回报文
            String resXml=ClaimBaseCoder.objToXmlUtf(response);
            logger.info("广东电网理赔数据同步接口返回XML----->"+resXml);
        } catch (Exception e) {
            ResHead head = new ResHead();//头部信息
            head.setResponseType(requestType);
            head.setResponseCode("1");
            head.setErrorMessage(e.getMessage());
            response.setHead(head);
            e.printStackTrace();
            logger.info("错误信息"+e.getMessage());
        }
        return response;
    }


    public List<ResData> findGDPowerGridCarClaimInfo(Date startDate, Date endDate) throws Exception {//对状态没有查询条件
        String sqlStr = "select "
                + "d.licenseno, "
                + "r.damagetime, "
                + "r.damageaddress, "
                + "r.damagecode, "
                + "c.reporttime, "
                + "c.claimno, "
                + "c.claimtime, "
                + "s.endcasedate, "
                + "d.lflag, "
                + "d.repairfactoryname, "
                + "(select SuM(NVL(msate.sumamt, 0)) + SuM(NVL(msate.sumfee, 0)) from prplcompensate msate where msate.underwriteflag in ('1', '3')  and msate.registno = r.registno and msate.caseno = s.endcaseno) "
                + "FROM "
                + "PrpLRegist r, "
                + "PrpLDlossCarMain d, "
                + "PrpLClaim c, "
                + "PrpLEndCase s "
                + "where r.registno = d.registno "
                + "and r.registno = c.registno "
                //+ "and c.claimno = s.claimno "
                + "and r.registno = s.registno "

                + "and s.endcasedate = (select max(endcasedate) "
                + "from PrpLEndCase B "
                + "where c.claimno = B.claimno) "

                + "and s.endcasedate >= to_date('"+ DateUtils.dateToStr(startDate,"yyyyMMdd")+"', 'yyyy/MM/dd') "
                + "and s.endcasedate < to_date('"+DateUtils.dateToStr(endDate,"yyyyMMdd")+"', 'yyyy/MM/dd') "

                + "and d.deflosscartype = '1' "
                + "and exists "
                + "(select 1 "
                + "from prplcmain a "
                + "where a.registno = r.registno "
                + "and exists "
                + "(select 1 "
                + "from PrpLCItemCar t "
                + "where a.policyno = t.policyno "
                + "and exists (select 1 "
                + "from GDPowerGridCarInfo ci "
                + "where ci.engineno = t.engineno "
                + "and ci.frameno = t.frameno))) ";

        SqlJoinUtils sqlUtil = new SqlJoinUtils();

        sqlUtil.append(sqlStr);

        String sql = sqlUtil.getSql();
        logger.info("广东电网理赔数据同步接口SQL:" + sql);
        List<Object[]> result = baseDaoService.findListBySql(sql,sqlUtil.getParamValues());
        // 对象转换
        List<ResData> resultList = new ArrayList<ResData>();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        for(int i = 0; i < result.size(); i++ ){
            ResData resultVo = new ResData();
            Object[] obj = (Object[])result.get(i);
            resultVo.setCarNum((String)obj[0]);
            resultVo.setBeThreatenedTime(df.format(obj[1]));
            resultVo.setBeThreatenedAddress((String)obj[2]);
            resultVo.setBeThreatenedReason((String)obj[3]);
            resultVo.setReportTime(df.format(obj[4]));
            resultVo.setCaseNumber((String)obj[5]);
            resultVo.setCaseTime(df.format(obj[6]).trim());
            resultVo.setClosingTime(df.format(obj[7]));
            resultVo.setClaimType((String)obj[8]);
            resultVo.setFactory((String)obj[9]);
            resultVo.setClaimAmount((BigDecimal)obj[10]);
            resultList.add(resultVo);
        }
        return resultList;
    }


    /**
     * 构造返回报文
     * @param resData
     * @return
     */
    private GDPowerResponse initResponse(List<ResData> resData, String requestType) {
        GDPowerResponse response = new GDPowerResponse();

        ResHead head = new ResHead();
        response.setHead(head);
        head.setResponseCode(requestType);
        head.setResponseCode("0");
        ResBody body = new ResBody();

        ResDataList resDataList = new ResDataList();

        resDataList.setData(resData);
        body.setResDataList(resDataList);
        response.setBody(body);
        return response;
    }

    //手动注入
    private void init(){
        if(baseDaoService==null){
            baseDaoService = (BaseDaoService) Springs.getBean(BaseDaoService.class);
        }
    }
}
