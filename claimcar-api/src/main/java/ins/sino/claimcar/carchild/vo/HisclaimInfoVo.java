package ins.sino.claimcar.carchild.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;


/**
 * 历史赔案基础信息
 * <pre></pre>
 * @author ★LinYi
 */
@XStreamAlias("HISCLAIMINFO")
public class HisclaimInfoVo implements Serializable{

    /**  */
    private static final long serialVersionUID = 5878819654774673443L;
    
    @XStreamAlias("POLICYNO")
    private String policyNo;//交强险保单号
    
    @XStreamAlias("BUSIPOLICYNO")
    private String busiPolicyNo;//商业险保单号
    
    @XStreamAlias("HISTORICALACCIDENT")
    private int historiCalAccident;//历史出险次数 
    
    @XStreamAlias("HISTORICALCLAIMTIMES")
    private int historiCalClaimTimes;//历史赔款次数
    
    @XStreamAlias("HISTORICALCLAIMSUM")
    private BigDecimal historiCalClaimSum;//历史赔款总计

    @XStreamAlias("HISCLAIMLIST")
    private List<HisClaimVo> hisClaims;//历史出险信息
    
    
    public String getPolicyNo() {
        return policyNo;
    }

    
    public void setPolicyNo(String policyNo) {
        this.policyNo = policyNo;
    }

    
    public String getBusiPolicyNo() {
        return busiPolicyNo;
    }

    
    public void setBusiPolicyNo(String busiPolicyNo) {
        this.busiPolicyNo = busiPolicyNo;
    }

    
    public int getHistoriCalAccident() {
        return historiCalAccident;
    }

    
    public void setHistoriCalAccident(int historiCalAccident) {
        this.historiCalAccident = historiCalAccident;
    }

    
    public int getHistoriCalClaimTimes() {
        return historiCalClaimTimes;
    }

    
    public void setHistoriCalClaimTimes(int historiCalClaimTimes) {
        this.historiCalClaimTimes = historiCalClaimTimes;
    }

    
    public BigDecimal getHistoriCalClaimSum() {
        return historiCalClaimSum;
    }

    
    public void setHistoriCalClaimSum(BigDecimal historiCalClaimSum) {
        this.historiCalClaimSum = historiCalClaimSum;
    }

    
    public List<HisClaimVo> getHisClaims() {
        return hisClaims;
    }

    
    public void setHisClaims(List<HisClaimVo> hisClaims) {
        this.hisClaims = hisClaims;
    }
    
}
