package ins.sino.claimcar.pinganUnion.enums;

/**
 * @Description 处理状态
 * @Author liuys
 * @Date 2020/7/20 10:22
 */
public enum DealStatusEnum {
    CREATE("0", "创建"),
    PENDING("1", "处理中"),
    FAIL("-1", "处理失败"),
    SUCCESS("2", "处理成功"),
    ;

    private String code;
    private String desc;

    DealStatusEnum(String code, String desc) {
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
