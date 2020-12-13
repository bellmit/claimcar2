package ins.sino.claimcar.pinganUnion.enums;

/**
 * @Description 数据通知环节枚举
 * @Author liuys
 * @Date 2020/7/20 10:22
 */
public enum PingAnDataTypeEnum {
    CODE_01("01", "案件基本信息", "pingAnRegistHandleService", "00"),
    CODE_02("02", "车查勘信息", "pingAnCarCheckService", "01"),
    CODE_03("03", "人查勘信息", "pingAnPersonCheckService", "02"),
    CODE_04("04", "车定损信息及明细", "pingAnCarDlossService", "02"), 
    CODE_05("05", "物定损信息及明细", "pingAnPropDlossService", "02"),
    CODE_06("06", "人定损信息及明细", "pingAnPersonDlossService", "03"),
    CODE_07("07", "立案信息", "pingAnRegisterHandleService", "01"),
    CODE_08("08", "理算支付信息", "pingAnPaymentHandleService", "01"),
    CODE_09("09", "支付重下发信息", "pingAnPaymentHandleService", "08"),
    CODE_10("10", "结案信息", "pingAnEndCaseHandleService", "11"),
    CODE_11("11", "未决变动信息", "pingAnEstimateHandleService", "01"),
    CODE_12("12", "垫付信息", "pingAnPadPayHandleService", "01"),
    CODE_13("13", "预赔信息", "pingAnPrePayHandleService", "01"),
    CODE_14("14", "案件重开信息", "pingAnMultClaimApplyInfoService", "10"),
    CODE_15("15", "诉讼信息", "pingAnLawsuitInfoService", "01"),
    CODE_16("16", "调查信息", "pingAnInvestigationInfoService", "01"),
    CODE_17("17", "保单先结", "pingAnPreEndCaseHandleService", "01"),
    CODE_18("18", "单证完成", "pingAnDocCompleteInfoService", "02"),
    CODE_19("19", "影像附件", "pingAnImageService", "01"),
    CODE_20("20", "反洗钱信息", "pingAnPaymentClientInfoService", "01"),
    CODE_dh01("DH01", "支付结果回调", "pingAnPayCallbackHandleService", "00")
    ;

    private String code;
    private String desc;
    private String handleClassName;
    //父级节点Code "00" 无父级环节
    private String fatherNodeCode;

    PingAnDataTypeEnum(String code, String desc, String handleClassName, String fatherNodeCode) {
        this.code = code;
        this.desc = desc;
        this.handleClassName = handleClassName;
        this.fatherNodeCode = fatherNodeCode;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getHandleClassName() {
        return handleClassName;
    }

    public void setHandleClassName(String handleClassName) {
        this.handleClassName = handleClassName;
    }

    public String getFatherNodeCode() {
        return fatherNodeCode;
    }

    public void setFatherNodeCode(String fatherNodeCode) {
        this.fatherNodeCode = fatherNodeCode;
    }

    /**
     * 获取枚举类型
     *
     * @param code
     */
    public static PingAnDataTypeEnum getEnumByCode(String code) {
        PingAnDataTypeEnum[] enums = PingAnDataTypeEnum.values();
        for (PingAnDataTypeEnum en : enums) {
            if (en.getCode().equals(code)) {
                return en;
            }
        }
        return null;
    }
}
