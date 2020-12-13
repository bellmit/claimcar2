package ins.sino.claimcar.moblie.logUtil;
/**
 * 与移动查勘接口交互 接口类型定义
 * <pre></pre>
 * @author ★niuqiang
 */

public enum MobileRequestType {
    registBaseInfo_Mobile("001","报案基本信息接口"),
    policyBaseInfo_Mobile("002","保单基本信息接口"),
    historyClaim_Mobile("003","历史赔案接口"),
    commonMarkQuery_moblie("004","公共信息查询接口"),
    bigCasePredictQuery_Mobile("005","大案预报信息查询接口"),
    checkRecordQUery_Mobile("006","查勘记录信息查询接口"),
    checkInfoInit_Mobile("007","查勘信息初始化接口"),
    checkSumitSync_Mobile("008","移动端查勘提交同步接口"),
    lossInfoInit_Mobile("009","定损信息初始化"),
    lossSubmitSync_Mobile("010","移动端定损提交同步接口"),
    wfWorkInfoView_Mobile("013","工作流查看接口"),
    caseListQuery_Mobile("014","案件列表查询接口"),
    caseDetailQuery_Mobile("015","案件详情查询接口"),
    msgNotifiedClaim_Mobile("016","移动端通知理赔接口"),
    sendMsgToMobile_Mobile("017","理赔通知终端接口"),
    saveCertiDirectory_Mobile("020","单证目录保存接口")
    ;
    
    private String code;
    private String name;
    
    private MobileRequestType(String code,String name){
        this.code = code;
        this.name = name;
    }
    
    public String getCode() {
        return code;
    }
    
    public String getName() {
        return name;
    }

}
