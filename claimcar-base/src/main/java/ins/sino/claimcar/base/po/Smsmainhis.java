package ins.sino.claimcar.base.po;

// Generated by Hibernate Tools 3.2.5 (sinosoft version), Don't modify!
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * POJO Class Smsmainhis
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "YWUSER.SMSMAINHIS")
public class Smsmainhis implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private String id ;
	private String smsClass;
	private String userCode;
	private String phoneCode;
	private String businessNo;
	private BigDecimal phoneCount;
	private Date sendTime;
	private String sendText;
	private String status;
	private String deleteFlag;
	private String usefulLife;
	private String inceptBack;
	private String gwId;
	private String comCode;
	private BigDecimal repeatTimes;
	private BigDecimal sendLevel;
	private Date mainInputdate;
	private Date inputDate;
	private String failReason;
	private String msgId;
	private String mmsFlag;
	private BigDecimal channelType;

	@Id
	@Column(name = "ID", unique = true, nullable = false, length=50)
	public String getId () {
		return this.id ;
	}

	public void setId (String id           ) {
		this.id = id ;
	}

	@Column(name = "SMSCLASS")
	public String getSmsClass() {
		return this.smsClass;
	}

	public void setSmsClass(String smsClass      ) {
		this.smsClass= smsClass;
	}

	@Column(name = "USERCODE", length=25)
	public String getUserCode    () {
		return this.userCode    ;
	}

	public void setUserCode    (String userCode    ) {
		this.userCode     = userCode    ;
	}

	@Column(name = "PHONECODE", length=2155)
	public String getPhoneCode     () {
		return this.phoneCode     ;
	}

	public void setPhoneCode     (String phoneCode     ) {
		this.phoneCode      = phoneCode     ;
	}

	@Column(name = "BUSINESSNO")
	public String getBusinessNo    () {
		return this.businessNo    ;
	}

	public void setBusinessNo    (String businessNo    ) {
		this.businessNo     = businessNo    ;
	}

	@Column(name = "PHONECOUNT", precision=38, scale=0)
	public BigDecimal getPhoneCount    () {
		return this.phoneCount    ;
	}

	public void setPhoneCount    (BigDecimal phoneCount    ) {
		this.phoneCount     = phoneCount    ;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "SENDTIME", length=7)
	public Date getSendTime      () {
		return this.sendTime      ;
	}

	public void setSendTime      (Date sendTime      ) {
		this.sendTime       = sendTime      ;
	}

	@Column(name = "SENDTEXT", length=1000)
	public String getSendText      () {
		return this.sendText      ;
	}

	public void setSendText      (String sendText      ) {
		this.sendText       = sendText      ;
	}

	@Column(name = "STATUS", length=1)
	public String getStatus        () {
		return this.status        ;
	}

	public void setStatus        (String status        ) {
		this.status         = status        ;
	}

	@Column(name = "DELETEFLAG", length=1)
	public String getDeleteFlag   () {
		return this.deleteFlag   ;
	}

	public void setDeleteFlag   (String deleteFlag   ) {
		this.deleteFlag    = deleteFlag   ;
	}

	@Column(name = "USEFULLIFE", length=1)
	public String getUsefulLife    () {
		return this.usefulLife    ;
	}

	public void setUsefulLife    (String usefulLife    ) {
		this.usefulLife     = usefulLife    ;
	}

	@Column(name = "INCEPTBACK", length=2155)
	public String getInceptBack    () {
		return this.inceptBack    ;
	}

	public void setInceptBack    (String inceptBack    ) {
		this.inceptBack     = inceptBack    ;
	}

	@Column(name = "GWID", length=50)
	public String getGwId          () {
		return this.gwId          ;
	}

	public void setGwId          (String gwId          ) {
		this.gwId           = gwId          ;
	}

	@Column(name = "COMCODE", length=10)
	public String getComCode      () {
		return this.comCode      ;
	}

	public void setComCode      (String comCode      ) {
		this.comCode       = comCode      ;
	}

	@Column(name = "REPEATTIMES", precision=38, scale=0)
	public BigDecimal getRepeatTimes   () {
		return this.repeatTimes   ;
	}

	public void setRepeatTimes   (BigDecimal repeatTimes   ) {
		this.repeatTimes    = repeatTimes   ;
	}

	@Column(name = "SENDLEVEL", precision=38, scale=0)
	public BigDecimal getSendLevel     () {
		return this.sendLevel     ;
	}

	public void setSendLevel     (BigDecimal sendLevel     ) {
		this.sendLevel      = sendLevel     ;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "MAININPUTDATE", length=7)
	public Date getMainInputdate () {
		return this.mainInputdate ;
	}

	public void setMainInputdate (Date mainInputdate ) {
		this.mainInputdate  = mainInputdate ;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "INPUTDATE", length=7)
	public Date getInputDate     () {
		return this.inputDate     ;
	}

	public void setInputDate     (Date inputDate     ) {
		this.inputDate      = inputDate     ;
	}

	@Column(name = "FAILREASON", length=2155)
	public String getFailReason    () {
		return this.failReason    ;
	}

	public void setFailReason    (String failReason    ) {
		this.failReason     = failReason    ;
	}

	@Column(name = "MSGID", length=32)
	public String getMsgId         () {
		return this.msgId         ;
	}

	public void setMsgId         (String msgId         ) {
		this.msgId          = msgId         ;
	}

	@Column(name = "MMSFLAG", length=1)
	public String getMmsFlag       () {
		return this.mmsFlag       ;
	}

	public void setMmsFlag       (String mmsFlag       ) {
		this.mmsFlag        = mmsFlag       ;
	}

	@Column(name = "CHANNELTYPE", precision=0)
	public BigDecimal getChannelType  () {
		return this.channelType  ;
	}

	public void setChannelType  (BigDecimal channelType  ) {
		this.channelType   = channelType  ;
	}
}