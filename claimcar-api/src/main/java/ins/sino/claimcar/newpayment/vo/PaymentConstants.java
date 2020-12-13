package ins.sino.claimcar.newpayment.vo;

/**
 * 新收付接口相关常量
 *
 * @author ffsz
 * @date 2020/5/2 17:37
 */
public class PaymentConstants {

    // 收付原因 start
    /** 收付原因 - 赔款(从联方) */
    public static final String PAYREASON_F60 = "F60";
    /** 收付原因 - 付检验费(从联方) */
    public static final String PAYREASON_F61 = "F61";
    /** 收付原因 - 付鉴定费（从联方） */
    public static final String PAYREASON_F611 = "F611";
    /** 收付原因 - 付律师费（从联方） */
    public static final String PAYREASON_F612 = "F612";
    /** 收付原因 - 付诉讼费（从联方） */
    public static final String PAYREASON_F613 = "F613";
    /** 收付原因 - 防灾防损费(从联方) */
    public static final String PAYREASON_F614 = "F614";
    /** 收付原因 - 身份信息查询费（从联方）  */
    public static final String PAYREASON_F615 = "F615";
    /** 收付原因 - 财产保全费（从联方） */
    public static final String PAYREASON_F616 = "F616";
    /** 收付原因 - 评估费（从联方） */
    public static final String PAYREASON_F617 = "F617";
    /** 收付原因 - 强制执行费（从联方） */
    public static final String PAYREASON_F618 = "F618";
    /** 收付原因 - 内部奖励费（从联方） */
    public static final String PAYREASON_F619 = "F619";
    /** 收付原因 - 付查勘费(从联方) */
    public static final String PAYREASON_F62 = "F62";
    /** 收付原因 - 付施救费(从联方) */
    public static final String PAYREASON_F63 = "F63";
    /** 收付原因 - 代查勘费(从联方) */
    public static final String PAYREASON_F64 = "F64";
    /** 收付原因 - 咨询或顾问费(从联方) */
    public static final String PAYREASON_F65 = "F65";
    /** 收付原因 - 差旅费(从联方) */
    public static final String PAYREASON_F66 = "F66";
    /** 收付原因 - 付公估费(从联方) */
    public static final String PAYREASON_F67 = "F67";
    /** 收付原因 - 付法律费(从联方) */
    public static final String PAYREASON_F68 = "F68";
    /** 收付原因 - 付其他费用(从联方) */
    public static final String PAYREASON_F69 = "F69";
    /** 收付原因 - 代付赔款（从联方) */
    public static final String PAYREASON_F6B = "F6B";
    /** 收付原因 - 追偿赔款（从联方） */
    public static final String PAYREASON_F6C = "F6C";
    /** 收付原因 - 他方代付（从联方） */
    public static final String PAYREASON_F6D = "F6D";
    /** 收付原因 - 普通追偿费用（从联方） */
    public static final String PAYREASON_F6H = "F6H";
    /** 收付原因 - 奖励费（从联方） */
    public static final String PAYREASON_F6I = "F6I";
    /** 收付原因 - 垫付赔款（从联方） */
    public static final String PAYREASON_F6K = "F6K";
    /** 收付原因 - 追偿发生的费用（从联方） */
    public static final String PAYREASON_F6S = "F6S";
    /** 收付原因 - 预付赔款 */
    public static final String PAYREASON_P50 = "P50";
    /** 收付原因 - 预付公估费 */
    public static final String PAYREASON_P51 = "P51";
    /** 收付原因 - 预付检测费 */
    public static final String PAYREASON_P510 = "P510";
    /** 收付原因 - 预付鉴定费 */
    public static final String PAYREASON_P511 = "P511";
    /** 收付原因 - 预付律师费 */
    public static final String PAYREASON_P512 = "P512";
    /** 收付原因 - 预付诉讼费 */
    public static final String PAYREASON_P513 = "P513";
    /** 收付原因 - 预付代查勘费 */
    public static final String PAYREASON_P54 = "P54";
    /** 收付原因 - 预付咨询顾问费   */
    public static final String PAYREASON_P55 = "P55";
    /** 收付原因 - 预付差旅费 */
    public static final String PAYREASON_P56 = "P56";
    /** 收付原因 - 赔款 */
    public static final String PAYREASON_P60 = "P60";
    /** 收付原因 - 付检验费 */
    public static final String PAYREASON_P61 = "P61";
    /** 收付原因 - 鉴定费     */
    public static final String PAYREASON_P611 = "P611";
    /** 收付原因 - 律师费     */
    public static final String PAYREASON_P612 = "P612";
    /** 收付原因 - 诉讼费     */
    public static final String PAYREASON_P613 = "P613";
    /** 收付原因 - 防灾防损费 */
    public static final String PAYREASON_P614 = "P614";
    /** 收付原因 - 身份信息查询费 */
    public static final String PAYREASON_P615 = "P615";
    /** 收付原因 - 财产保全费 */
    public static final String PAYREASON_P616 = "P616";
    /** 收付原因 - 评估费 */
    public static final String PAYREASON_P617 = "P617";
    /** 收付原因 - 强制执行费 */
    public static final String PAYREASON_P618 = "P618";
    /** 收付原因 - 付查勘费 */
    public static final String PAYREASON_P62 = "P62";
    /** 收付原因 - 付施救费 */
    public static final String PAYREASON_P63 = "P63";
    /** 收付原因 - 代查勘费 */
    public static final String PAYREASON_P64 = "P64";
    /** 收付原因 - 咨询或顾问费 */
    public static final String PAYREASON_P65 = "P65";
    /** 收付原因 - 差旅费 */
    public static final String PAYREASON_P66 = "P66";
    /** 收付原因 - 付公估费 */
    public static final String PAYREASON_P67 = "P67";
    /** 收付原因 - 法律费及诉讼费 */
    public static final String PAYREASON_P68 = "P68";
    /** 收付原因 - 付其他费用 */
    public static final String PAYREASON_P69 = "P69";
    /** 收付原因 - 代付赔款 */
    public static final String PAYREASON_P6B = "P6B";
    /** 收付原因 - 追偿赔款 */
    public static final String PAYREASON_P6C = "P6C";
    /** 收付原因 - 他方代付 */
    public static final String PAYREASON_P6D = "P6D";
    /** 收付原因 - 清付赔款 */
    public static final String PAYREASON_P6E = "P6E";
    /** 收付原因 - 普通追偿费用 */
    public static final String PAYREASON_P6H = "P6H";
    /** 收付原因 - 奖励费     */
    public static final String PAYREASON_P6I = "P6I";
    /** 收付原因 - 损余收回款 */
    public static final String PAYREASON_P6J = "P6J";
    /** 收付原因 - 垫付赔款   */
    public static final String PAYREASON_P6K = "P6K";
    /** 收付原因 - 追偿发生的费用 */
    public static final String PAYREASON_P6S = "P6S";
    /** 收付原因 - 预付赔款(共保方) */
    public static final String PAYREASON_S50 = "S50";
    /** 收付原因 - 赔款(从共方) */
    public static final String PAYREASON_S60 = "S60";
    /** 收付原因 - 付检验费(从共方) */
    public static final String PAYREASON_S61 = "S61";
    /** 收付原因 - 付律师费（从共方） */
    public static final String PAYREASON_S612 = "S612";
    /** 收付原因 - 防灾防损费(从共方) */
    public static final String PAYREASON_S614 = "S614";
    /** 收付原因 - 付查勘费(从共方) */
    public static final String PAYREASON_S62 = "S62";
    /** 收付原因 - 付施救费(从共方) */
    public static final String PAYREASON_S63 = "S63";
    /** 收付原因 - 代查勘费(从共方) */
    public static final String PAYREASON_S64 = "S64";
    /** 收付原因 - 咨询或顾问费(从共方) */
    public static final String PAYREASON_S65 = "S65";
    /** 收付原因 - 差旅费(从共方) */
    public static final String PAYREASON_S66 = "S66";
    /** 收付原因 - 付公估费(从共方) */
    public static final String PAYREASON_S67 = "S67";
    /** 收付原因 - 付法律费(从共方) */
    public static final String PAYREASON_S68 = "S68";
    /** 收付原因 - 付其他费用(从共方) */
    public static final String PAYREASON_S69 = "S69";
    /** 收付原因 - 追偿发生的费用（从共方） */
    public static final String PAYREASON_S6S = "S6S";
    /** 收付原因 - 理赔进项税(从联方) */
    public static final String PAYREASON_P85CF = "P85CF";
    /** 收付原因 - 收垫付赔款 */
    public static final String PAYREASON_R6A = "R6A";
    // 收付原因  end


    // 赔付类型 start
    /** 赔付类型 - 赔款信息 */
    public static final String LOSSTYPE_0 = "0";
    /** 赔付类型 - 费用信息 */
    public static final String LOSSTYPE_1 = "1";
    // 赔付类型 end


    // 联共保标识 start
    /** 联共保标识 - 非联共保 */
    public static final String COINSFLAG_0 = "0";
    /** 联共保标识 - 主共 */
    public static final String COINSFLAG_1 = "1";
    /** 联共保标识 - 从共 */
    public static final String COINSFLAG_2 = "2";
    /** 联共保标识 - 主联 */
    public static final String COINSFLAG_3 = "3";
    /** 联共保标识 - 联共保标识 - 从联 */
    public static final String COINSFLAG_4 = "4";
    /** 联共保标识 - 主共转主联 */
    public static final String COINSFLAG_5 = "5";
    /** 联共保标识 - 从共转主联 */
    public static final String COINSFLAG_7 = "7";
    // 联共保标识  end


    // 共保类型 start
    /** 共保类型 - 我方 */
    public static final String COINSTYPE_1 = "1";
    /** 共保类型 - 系统内他方 */
    public static final String COINSTYPE_2 = "2";
    /** 共保类型 - 系统外他方 */
    public static final String COINSTYPE_3 = "3";
    // 共保类型  end


    // 业务类型 start
    /** 业务类型 - 赔款 */
    public static final String CERTITYPE_C = "C";
    /** 业务类型 - 预赔 */
    public static final String CERTITYPE_Y = "Y";
    /** 业务类型 - 追偿 */
    public static final String CERTITYPE_Z = "Z";
    /** 业务类型 - 保单 */
    public static final String CERTITYPE_P = "P";
    /** 业务类型 - 批单 */
    public static final String CERTITYPE_E = "E";
    /** 业务类型 - 手续费 */
    public static final String CERTITYPE_S = "S";
    // 业务类型  end


    // 收款方证件类型 start
    /** 收款方证件类型 - 纳税人识别号 */
    public static final String CERTIFICATETYPE_13 = "13";
    /** 收款方证件类型 - 工商注册号码 */
    public static final String CERTIFICATETYPE_11 = "11";
    /** 收款方证件类型 - 组织机构代码 */
    public static final String CERTIFICATETYPE_10 = "10";
    /** 收款方证件类型 - 税务登记证号 */
    public static final String CERTIFICATETYPE_12 = "12";
    /** 收款方证件类型 - 统一社会信用代码 */
    public static final String CERTIFICATETYPE_9 = "9";
    /** 收款方证件类型 - 出生日期（未成年人使用） */
    public static final String CERTIFICATETYPE_8 = "8";
    /** 收款方证件类型 - 出生证 */
    public static final String CERTIFICATETYPE_7 = "7";
    /** 收款方证件类型 - 港澳返乡证 */
    public static final String CERTIFICATETYPE_6 = "6";
    /** 收款方证件类型 - 学生证 */
    public static final String CERTIFICATETYPE_4 = "4";
    /** 收款方证件类型 - 军人证 */
    public static final String CERTIFICATETYPE_3 = "3";
    /** 收款方证件类型 - 台胞证 */
    public static final String CERTIFICATETYPE_5 = "5";
    /** 收款方证件类型 - 护照 */
    public static final String CERTIFICATETYPE_2 = "2";
    /** 收款方证件类型 - 身份证 */
    public static final String CERTIFICATETYPE_1 = "1";
    /** 收款方证件类型 - 其它 */
    public static final String CERTIFICATETYPE_99 = "99";
    // 收款方证件类型  end


    // 卡折类型 start
    /** 卡折类型 - 存折 */
    public static final String BANKCARDTYPE_1 = "1";
    /** 卡折类型 - 借记卡 */
    public static final String BANKCARDTYPE_2 = "110";
    // 卡折类型  end


    // 结算方式 start
    /** 结算方式 - 转账 */
    public static final String PAYWAY_1 = "1";
    /** 结算方式 - 支票 */
    public static final String PAYWAY_3 = "3";
    /** 结算方式 - 现金 */
    public static final String PAYWAY_5 = "5";
    /** 结算方式 - 报盘 */
    public static final String PAYWAY_8 = "8";
    /** 结算方式 - 其他 */
    public static final String PAYWAY_9 = "9";
    // 结算方式  end


    // 共转联标识 start
    /** 共转联标识 - 投保人/代理(份额) */
    public static final String COINSTYPELB_1 = "1";
    /** 共转联标识 - 联共保人(全额) */
    public static final String COINSTYPELB_2 = "2";
    // 共转联标识  end


    // 结算对象标识 start
    /** 4.1.10 结算对象标识 - 主联我方 */
    public static final String COINSPAYTYPE_1 = "1";
    /** 4.1.10 结算对象标识 - 代收(付) */
    public static final String COINSPAYTYPE_2 = "2";
    /** 4.1.10 结算对象标识 - 主联方收(付)从联方 */
    public static final String COINSPAYTYPE_3 = "3";
    /** 4.1.10 结算对象标识 - 从联方收(付) */
    public static final String COINSPAYTYPE_4 = "4";
    // 结算对象标识  end


    // 业务来源 start
    /** 4.1.11 业务来源 - 直接业务 */
    public static final String BUSINESSNATURE_0 = "0";
    /** 4.1.11 业务来源 - 个人代理 */
    public static final String BUSINESSNATURE_1 = "1";
    /** 4.1.11 业务来源 - 专业代理 */
    public static final String BUSINESSNATURE_2 = "2";
    /** 4.1.11 业务来源 - 兼业代理 */
    public static final String BUSINESSNATURE_3 = "3";
    /** 4.1.11 业务来源 - 经纪人一类 */
    public static final String BUSINESSNATURE_4 = "4";
    /** 4.1.11 业务来源 - 经纪人二类 */
    public static final String BUSINESSNATURE_5 = "5";
    /** 4.1.11 业务来源 - 电话业务 */
    public static final String BUSINESSNATURE_6 = "6";
    /** 4.1.11 业务来源 - 网上业务 */
    public static final String BUSINESSNATURE_8 = "8";
    // 业务来源  end

    // 业务板块 start
    /** 4.1.12 业务板块 - 市场业务 */
    public static final String BUSINESSPLATE_0 = "0";
    /** 4.1.12 业务板块 - 电力业务 */
    public static final String BUSINESSPLATE_1 = "1";
    /** 4.1.12 业务板块 - 会员业务 */
    public static final String BUSINESSPLATE_2 = "2";
    // 业务板块  end


    // 业务性质 start
    /** 4.1.13 业务性质 - 普通 */
    public static final String POLICYSORT_0 = "0";
    /** 4.1.13 业务性质 - 定额 */
    public static final String POLICYSORT_1 = "1";
    /** 4.1.13 业务性质 - 暂保单 */
    public static final String POLICYSORT_2 = "2";
    /** 4.1.13 业务性质 - 补录 */
    public static final String POLICYSORT_3 = "3";
    /** 4.1.13 业务性质 - 合同 */
    public static final String POLICYSORT_4 = "4";
    /** 4.1.13 业务性质 - 预约保险 */
    public static final String POLICYSORT_5 = "5";
    /** 4.1.13 业务性质 - 单程定额 */
    public static final String POLICYSORT_6 = "6";
    /** 4.1.13 业务性质 - 定期定额 */
    public static final String POLICYSORT_7 = "7";
    /** 4.1.13 业务性质 - 其他 */
    public static final String POLICYSORT_9 = "9";
    // 业务性质  end

    /** 数据来源 car-车理赔  noCar-非车理赔 */
    public static final String SYSTEMFLAG_CAR = "car";

    // 退回重复标识 0-否 1-是 start
    /** 退回重复标识 0-否 */
    public static final String REPAYTYPE_0 = "0";
    /** 退回重复标识 1-是 */
    public static final String REPAYTYPE_1 = "1";
    // 退回重复标识 0-否 1-是  end

    // 是否直付例外 0-否 1-是 start
    /** 是否直付例外 0-否 */
    public static final String PAYREASONFLAG_0 = "0";
    /** 是否直付例外 1-是 */
    public static final String PAYREASONFLAG_1 = "1";
    // 是否直付例外 0-否 1-是  end

    // 响应代码 00-成功  01-失败  start
    /** 理赔响应状态码 成功：00 */
    public static final String RESP_SUCCESS = "00";
    /** 理赔响应状态码 失败：01 */
    public static final String RESP_FAILED = "01";
    // 响应代码 00-成功  01-失败   end

    // 支付金额类型 P-赔款 F-费用 start
    /** 支付金额类型 P-赔款 */
    public static final String FEETYPE_P = "P";
    /** 支付金额类型 F-费用 */
    public static final String FEETYPE_F = "F";
    // 支付金额类型 P-赔款 F-费用  end

    // 拆分到险别标识 0-未拆分到险别  1-拆分到险别 start
    /** 拆分到险别标识 0-未拆分到险别 */
    public static final String SPLITRISKFLAG_0 = "0";
    /** 拆分到险别标识 1-拆分到险别 */
    public static final String SPLITRISKFLAG_1 = "1";
    // 拆分到险别标识 0-未拆分到险别  1-拆分到险别  end

    // 险类 11-交强险  12-商业险 start
    /** 险类 11-交强险 */
    public static final String CLASSCODE_11 = "11";
    /** 险类 12-商业险 */
    public static final String CLASSCODE_12 = "12";
    // 险类 11-交强险  12-商业险  end

    // 服务识别参数 start
    /** 服务识别参数 paymentRefund-收付退票服务 */
    public static final String PAYMENTREFUND = "paymentRefund";
    /** 服务识别参数 updateSettleNo-更新结算单号服务 */
    public static final String PAYMENTUPDATESETTLENO = "paymentUpdateSettleNo";
    /** 服务识别参数 paymentWriteBack-应收应付回写服务 */
    public static final String PAYMENTWRITEBACKPAY = "paymentWriteBackPay";
    /** 服务识别参数 writebackTax-价税回写服务 */
    public static final String PAYMENTWRITEBACKTAX = "paymentWriteBackTax";
    /** 服务识别参数 writebackTax-价税拆分数据推送理赔 */
    public static final String RECEIVEVATCHARTE = "receiveVatCharge";
    // 服务识别参数  end
}
