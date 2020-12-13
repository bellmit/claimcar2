package ins.sino.claimcar.longrise.services;


/**
 * 案件信息
 * 
 * @author wz
 */
public class CaseBean
{

    private String instype="无";// 保险类别:产险1寿险2
    private String inssort="无";// 险种
    private String phase="无";// 阶段:承保1立案2结案3
    
    private String policyno="无";// 保单号
    private String caseno="无";// 案件号
    private String endorsementno="无";// 批单号

    private String name="无";// 立案(投保、申请)人姓名
    private String mobile="无";// 立案(投保,申请)人手机号码
    private String idcard="无";// 立案(投保,申请)人身份证号

    private String insuredname="无";// 被保险人姓名;
    private String insuredidcard="无";// 被保险人身份证号
    private String insuredmobile="无";// 被保险人联系方式;

    private String carbrandno="无";// 车牌号;
    private String carframeno="无";// 车架号;
    private String dutybrandno="无";// 三责车辆车牌号
    private String dutyframeno="无";// 三责车辆车架号

    private String channel="无";// 销售渠道
    
    private String effectivedate="无";// 保单生效日期
    private String starttime="无";// 立案、出险日期
    private String address="无";// 出险地点(医院)
    private String accidentype="无";// 出险类型
    
    private String closetime="无";// 结案时间
    private String result="无";// 理赔结果
    private Double moneys;// 赔款（损失,保额）金额
    
    private String ispeoplehurt="无";// 是否人伤
    private String claimsnum="无";// 理赔次数
    private String areaid="无"; //地区

    private String istatus;// 返回状态
    private String resultdesc;// 返回信息

    
    public String getInstype()
    {
        return instype;
    }
    public void setInstype(String instype)
    {
        this.instype = instype;
    }
    public String getInssort()
    {
        return inssort;
    }
    public void setInssort(String inssort)
    {
        this.inssort = inssort;
    }
    public String getPhase()
    {
        return phase;
    }
    public void setPhase(String phase)
    {
        this.phase = phase;
    }
    public String getPolicyno()
    {
        return policyno;
    }
    public void setPolicyno(String policyno)
    {
        this.policyno = policyno;
    }
    public String getCaseno()
    {
        return caseno;
    }
    public void setCaseno(String caseno)
    {
        this.caseno = caseno;
    }
    public String getEndorsementno()
    {
        return endorsementno;
    }
    public void setEndorsementno(String endorsementno)
    {
        this.endorsementno = endorsementno;
    }
    public String getName()
    {
        return name;
    }
    public void setName(String name)
    {
        this.name = name;
    }
    public String getMobile()
    {
        return mobile;
    }
    public void setMobile(String mobile)
    {
        this.mobile = mobile;
    }
    public String getIdcard()
    {
        return idcard;
    }
    public void setIdcard(String idcard)
    {
        this.idcard = idcard;
    }
    public String getInsuredname()
    {
        return insuredname;
    }
    public void setInsuredname(String insuredname)
    {
        this.insuredname = insuredname;
    }
    public String getInsuredidcard()
    {
        return insuredidcard;
    }
    public void setInsuredidcard(String insuredidcard)
    {
        this.insuredidcard = insuredidcard;
    }
    public String getInsuredmobile()
    {
        return insuredmobile;
    }
    public void setInsuredmobile(String insuredmobile)
    {
        this.insuredmobile = insuredmobile;
    }
    public String getCarbrandno()
    {
        return carbrandno;
    }
    public void setCarbrandno(String carbrandno)
    {
        this.carbrandno = carbrandno;
    }
    public String getCarframeno()
    {
        return carframeno;
    }
    public void setCarframeno(String carframeno)
    {
        this.carframeno = carframeno;
    }
    public String getDutybrandno()
    {
        return dutybrandno;
    }
    public void setDutybrandno(String dutybrandno)
    {
        this.dutybrandno = dutybrandno;
    }
    public String getDutyframeno()
    {
        return dutyframeno;
    }
    public void setDutyframeno(String dutyframeno)
    {
        this.dutyframeno = dutyframeno;
    }
    public String getChannel()
    {
        return channel;
    }
    public void setChannel(String channel)
    {
        this.channel = channel;
    }
    public String getEffectivedate()
    {
        return effectivedate;
    }
    public void setEffectivedate(String effectivedate)
    {
        this.effectivedate = effectivedate;
    }
    public String getStarttime()
    {
        return starttime;
    }
    public void setStarttime(String starttime)
    {
        this.starttime = starttime;
    }
    public String getAddress()
    {
        return address;
    }
    public void setAddress(String address)
    {
        this.address = address;
    }
    public String getAccidentype()
    {
        return accidentype;
    }
    public void setAccidentype(String accidentype)
    {
        this.accidentype = accidentype;
    }
    public String getClosetime()
    {
        return closetime;
    }
    public void setClosetime(String closetime)
    {
        this.closetime = closetime;
    }
    public String getResult()
    {
        return result;
    }
    public void setResult(String result)
    {
        this.result = result;
    }
    public Double getMoneys()
    {
        return moneys;
    }
    public void setMoneys(Double moneys)
    {
        this.moneys = moneys;
    }
    public String getIspeoplehurt()
    {
        return ispeoplehurt;
    }
    public void setIspeoplehurt(String ispeoplehurt)
    {
        this.ispeoplehurt = ispeoplehurt;
    }
    public String getClaimsnum()
    {
        return claimsnum;
    }
    public void setClaimsnum(String claimsnum)
    {
        this.claimsnum = claimsnum;
    }
    public String getAreaid()
    {
        return areaid;
    }
    public void setAreaid(String areaid)
    {
        this.areaid = areaid;
    }
    public String getIstatus()
    {
        return istatus;
    }
    public void setIstatus(String istatus)
    {
        this.istatus = istatus;
    }
    public String getResultdesc()
    {
        return resultdesc;
    }
    public void setResultdesc(String resultdesc)
    {
        this.resultdesc = resultdesc;
    }
   
}
