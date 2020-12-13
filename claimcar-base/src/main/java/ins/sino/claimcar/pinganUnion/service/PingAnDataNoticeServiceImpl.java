package ins.sino.claimcar.pinganUnion.service;

import com.alibaba.dubbo.config.annotation.Service;
import ins.framework.dao.database.DatabaseDao;
import ins.framework.dao.database.support.QueryRule;
import ins.framework.utils.Beans;
import ins.sino.claimcar.pinganUnion.enums.DealStatusEnum;
import ins.sino.claimcar.pinganUnion.enums.PingAnDataTypeEnum;
import ins.sino.claimcar.pinganUnion.po.PingAnDataNotice;
import ins.sino.claimcar.pinganUnion.vo.PingAnDataNoticeVo;
import ins.sino.claimcar.pinganUnion.vo.ResultBean;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.Path;
import java.util.Date;
import java.util.List;

/**
 * @Description 描述
 * @Author liuys
 * @Date 2020/7/20 14:14
 */
@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"})
@Path("pingAnDataNoticeService")
public class PingAnDataNoticeServiceImpl implements PingAnDataNoticeService{
    private static Logger logger = LoggerFactory.getLogger(PingAnDataNoticeServiceImpl.class);

    @Autowired
    private DatabaseDao databaseDao;

    @Autowired
    private PingAnApiService pingAnApiService;

    @Autowired
    private SessionFactory sessionFactory;

    /**
     * 保存及更新数据下发通知记录
     * @param pingAnDataNoticeVo
     */
    @Override
    public PingAnDataNoticeVo saveOrUpdateDataNotice(PingAnDataNoticeVo pingAnDataNoticeVo) {
        PingAnDataNotice pingAnDataNotice = null;

        if (pingAnDataNoticeVo.getId() == null){
            pingAnDataNotice = new PingAnDataNotice();
            Beans.copy().from(pingAnDataNoticeVo).to(pingAnDataNotice);
        }else {
            pingAnDataNotice = databaseDao.findByPK(PingAnDataNotice.class,pingAnDataNoticeVo.getId());
            Beans.copy().from(pingAnDataNoticeVo).excludeNull().to(pingAnDataNotice);
        }
        databaseDao.save(PingAnDataNotice.class, pingAnDataNotice);
        pingAnDataNoticeVo.setId(pingAnDataNotice.getId());
        return pingAnDataNoticeVo;
    }

    @Override
    public PingAnDataNoticeVo queryById(Long id) {
        PingAnDataNotice pingAnDataNotice = databaseDao.findByPK(PingAnDataNotice.class, id);
        if (pingAnDataNotice == null){
            return null;
        }
        PingAnDataNoticeVo pingAnDataNoticeVo = new PingAnDataNoticeVo();
        Beans.copy().from(pingAnDataNotice).to(pingAnDataNoticeVo);
        return pingAnDataNoticeVo;
    }

    @Override
    public void untreatedDataNoticesHandle() {
        //查询未处理成功的数据，失败次数小于6 加悲观锁 for update nowait
        String sql = "FROM PingAnDataNotice AS pingAnDataNotice WHERE STATUS != " + DealStatusEnum.SUCCESS.getCode() + "  AND FAILTIMES <= 5 ORDER BY CREATETIME ASC";
        Query query = sessionFactory.getCurrentSession().createQuery(sql);
        query.setLockMode("pingAnDataNotice", LockMode.UPGRADE_NOWAIT);
        List<PingAnDataNotice> pingAnDataNotices = null;
        try {
            pingAnDataNotices = query.list();
            if (null != pingAnDataNotices && !pingAnDataNotices.isEmpty()) {
                //开始处理该批数据
                for (PingAnDataNotice pingAnDataNotice : pingAnDataNotices) {
                    if (null != pingAnDataNotice) {
                        try {
                            Long id = pingAnDataNotice.getId();
                            String reportNo = pingAnDataNotice.getReportNo();
                            String pushType = pingAnDataNotice.getPushType();
                            Integer failTimes = pingAnDataNotice.getFailTimes();
                            logger.info("开始处理平安数据下发通知数据： id【" + id + "】，案件号【" + reportNo + "】，环节【" + pushType + "】已失败次数【" + failTimes + "】");
                            //根据数据通知环节Code获取对应的枚举类
                            PingAnDataTypeEnum dataTypeEnum = PingAnDataTypeEnum.getEnumByCode(pushType);
                            PingAnDataNoticeVo pingAnDataNoticeVo = new PingAnDataNoticeVo();
                            Beans.copy().from(pingAnDataNotice).to(pingAnDataNoticeVo);
                            if (null == dataTypeEnum) {
                                pingAnDataNoticeVo.setStatus(DealStatusEnum.FAIL.getCode());
                                pingAnDataNoticeVo.setRemark("数据通知环节类型不存在");
                                pingAnDataNoticeVo.setUpdateTime(new Date());
                                pingAnDataNoticeVo.setFailTimes(failTimes + 1);
                                saveOrUpdateDataNotice(pingAnDataNoticeVo);
                                continue;
                            }
                            //过滤上一个环节处理失败或未处理的数据 （不包括 "00" 无父级环节的）
                            String fatherNodeCode = dataTypeEnum.getFatherNodeCode();
                            if ("00".equals(fatherNodeCode)) {
                                doService(pingAnDataNoticeVo);
                            }else {
                                // 校验该案件上一个处理环节是否成功
                                if (checkFatherLinkIsSuccess(reportNo, fatherNodeCode)) {
                                    doService(pingAnDataNoticeVo);
                                } else {
                                    pingAnDataNoticeVo.setRemark("该案件的上一个环节【" + fatherNodeCode + "】未处理成功!");
                                    pingAnDataNoticeVo.setStatus(DealStatusEnum.FAIL.getCode());
                                    pingAnDataNoticeVo.setUpdateTime(new Date());
                                    pingAnDataNoticeVo.setFailTimes(failTimes + 1);
                                    saveOrUpdateDataNotice(pingAnDataNoticeVo);
                                    logger.error("id【" + id + "】，案件号【" + reportNo + "】，环节【" + pushType + "】该案件的上一个环节【" + fatherNodeCode + "】未处理成功！");
                                }
                            }
                        } catch (Exception e) {
                            logger.error("PingAnTaskService methodName : doBusinessProcess 平安数据处理异常===========", e);
                        }
                    }
                }
            } else {
                logger.info("PingAnTaskService methodName : doBusinessProcess 暂无平安数据需要处理，执行结束！===========");
            }
        } catch (HibernateException h) {
            logger.error("PingAnTaskService methodName : doBusinessProcess 平安数据处理异常，该数据或已在处理中===========", h);
        }

    }

    /**
     * 调用 平安联盟接口请求入口
     * @param untreatedDataNotice 未处理成功的数据
     */
    private void doService(PingAnDataNoticeVo untreatedDataNotice) {
        ResultBean resultBean = pingAnApiService.service(untreatedDataNotice);
        //更新处理结果
        if (resultBean.isSuccess()){
            untreatedDataNotice.setStatus(DealStatusEnum.SUCCESS.getCode());
        }else {
            untreatedDataNotice.setStatus(DealStatusEnum.FAIL.getCode());
            untreatedDataNotice.setFailTimes(untreatedDataNotice.getFailTimes() + 1);
        }
        String message = resultBean.getMessage();
        if (StringUtils.isNotBlank(message)) {
            if (message.length() > 200) {
                message = message.substring(0, 200);
            }
        }
        untreatedDataNotice.setRemark(message);
        untreatedDataNotice.setUpdateTime(new Date());
        saveOrUpdateDataNotice(untreatedDataNotice);
    }

    /**
     * description: checkFatherLinkIsSuccess 校验案件上一个环节是否处理成功
     * version: 1.0
     * date: 2020/8/3 17:14
     * author: lk
     *
     * @param reportNo 案件号
 * @param fatherNodeCode 父级节点
     * @return java.lang.Boolean
     */
    private Boolean checkFatherLinkIsSuccess(String reportNo, String fatherNodeCode) {
        QueryRule queryRule = QueryRule.getInstance();
        queryRule.addEqual("reportNo", reportNo);
        queryRule.addEqual("pushType", fatherNodeCode);
        queryRule.addEqual("status", DealStatusEnum.SUCCESS.getCode());
        List<PingAnDataNotice> successDatas = databaseDao.findAll(PingAnDataNotice.class, queryRule);
        if (null != successDatas && !successDatas.isEmpty()) {
            return true;
        }else {
            return false;
        }

    }




}
