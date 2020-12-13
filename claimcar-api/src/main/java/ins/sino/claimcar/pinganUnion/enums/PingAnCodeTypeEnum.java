package ins.sino.claimcar.pinganUnion.enums;

/**
 * description: PingAnCodeTypeEnum 平安字典数据类型枚举
 * date: 2020/7/22 9:41
 * author: lk
 * version: 1.0
 */
public enum PingAnCodeTypeEnum {
    XZ("planCode", "险种"),
    XB("dutyCode", "险别"),
    CLAIMREASON("accidentCause", "出险原因"),
    JG("deptCode", "机构"),
    HPZL("licensePlateType", "号牌种类"),
    JGZL("priceType", "价格品类"),
    CLJGFL("carCategoryCode", "车辆结构分类"),
    ZJLX("certificateType", "证件类型"),
    JSZLX("licenseType", "驾驶证类型"),
    BDCLZLBM("carKindCodeBd", "车辆种类编码(标的车)"),
    SZCLZLBM("carKindCodeSz", "车辆种类编码(三者车)"),
    ZJCX("quasiDrivingType", "准驾车型"),
    FJLX("documentType", "附件类型"),
    PKLX("claimType", "赔付类型"),
    TDRWLX("lossType", "通道任务类型"),
    JPYYBM("refuseReasonType", "拒赔原因编码"),
    ZYDBM("disputType", "争议点编码"),
    RSJBFYLX("personFeeType", "人伤基本费用类型"),
    PJBQLX("fitLabelCode", "配件标签类型"),
    SSDW("lossUnit", "损失单位"),
    DCJSLX("decreaseType", "调查减损类型"),
    TDLYHJ("sourceType", "提调来源环节"),
    TDJPYY("exRefuseReason", "提调拒赔原因"),
    BSYY("loseLawsuitReason", "败诉原因"),
    SFDM("provinceCode", "省份代码"),
    SQDM("cityCode", "市区代码"),
//    LJZXSQYY("caseCancelReason", "零结注销申请原因"),
    KHXXLX("customerInfoType", "客户信息类型"),
    TTZJLX("orgCerType", "团体证件类型"),
    ZYLX("careerType", "职业类型"),
    SYRFS("beneficiaryWay", "受益人方式"),
    GYSX("stateAttribute", "国有属性"),
    HBBZ("currencyCode", "货币币种"),
    SFLX("identityType", "身份类型"),
    TWOJGDM("twoDeptCode", "2位机构代码"),
    SGLX("accidentType", "事故类型"),
    SGXXLX("accidentTypeDetail", "事故详细类型"),
    SGZRRD("accidentResponsibility", "事故责任认定"),
    DCCLRJS("investigationUserEole", "调查处理人角色"),
    YWLY("businessSourceCode", "业务来源"),
    YWLYXF("businessSourceDetailCode", "业务来源细分"),
    KHLX("clientType", "客户类型")
//    SCPTJKLX("interfaceType", "上传平台接口类型"),
//    SCPTJKXZLX("interfaceRiskType", "上传平台接口险种类型"),
    ;

    private String codeType;
    private String codeTypeName;

    PingAnCodeTypeEnum(String codeType, String codeTypeName) {
        this.codeType = codeType;
        this.codeTypeName = codeTypeName;
    }

    public String getCodeType() {
        return codeType;
    }

    public void setCodeType(String codeType) {
        this.codeType = codeType;
    }

    public String getCodeTypeName() {
        return codeTypeName;
    }

    public void setCodeTypeName(String codeTypeName) {
        this.codeTypeName = codeTypeName;
    }

}
