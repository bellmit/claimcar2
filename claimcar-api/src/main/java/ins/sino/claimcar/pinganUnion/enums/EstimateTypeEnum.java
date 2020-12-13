package ins.sino.claimcar.pinganUnion.enums;

public enum EstimateTypeEnum {
    AVG_ESTIMATE("01", "案均未决"),
    ADJUST_ESTIMATE("03", "人工调整未决"),
    DELAY01_ESTIMATE("05", "延迟未决1"),
    DELAY02_ESTIMATE("06", "延迟未决2"),
    CLAIM_ESTIMATE("08", "立案时点未决"),
    SYSTEM_FIX_ESTIMATE("09", "系统修正未决"),
    UNDERWRITE_ESTIMATE("10", "核赔未决"),
    ;

    private String code;
    private String desc;

    EstimateTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
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

    /**
     * 获取枚举类型
     *
     * @param code
     */
    public static EstimateTypeEnum getEnumByCode(String code) {
        EstimateTypeEnum[] enums = EstimateTypeEnum.values();
        for (EstimateTypeEnum en : enums) {
            if (en.getCode().equals(code)) {
                return en;
            }
        }
        return null;
    }

    /**
     * 获取描述
     *
     * @param code
     */
    public static String getDescByCode(String code) {
        EstimateTypeEnum[] enums = EstimateTypeEnum.values();
        for (EstimateTypeEnum en : enums) {
            if (en.getCode().equals(code)) {
                return en.getDesc();
            }
        }
        return null;
    }
}
