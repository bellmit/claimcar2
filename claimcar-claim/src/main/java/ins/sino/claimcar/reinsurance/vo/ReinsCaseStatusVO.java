package ins.sino.claimcar.reinsurance.vo;

import ins.platform.utils.xstreamconverters.SinosoftDateConverter;

import java.io.Serializable;
import java.util.Date;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;
/**
 * �ٱ�����᰸/ע��/�����Լ��ؿ��ⰸҵ��ReinsCaseStatusVO
 * @author luows
 * @since (2016��7��26�� ����9:11:16): <br>
 */ 
@XStreamAlias("ReinsCaseStatus")
public class ReinsCaseStatusVO implements Serializable  {
	private static final long serialVersionUID = 1L;
	@XStreamAlias("businessType")
	private String businessType;		// �ⰸ����  ����1�� ����3 ���᰸/�᰸/ע��/����/���ؿ��ⰸ 0
	@XStreamAlias("claimNo")
	private String claimNo;				// ������
	@XStreamConverter(value = SinosoftDateConverter.class, strings = {"yyyyMMdd"})
	private Date operateDate;		// ����ʱ��
	@XStreamAlias("operateComCode")
	private String operateComCode;		//������
	@XStreamAlias("operaterCode")
	private String operaterCode;		// ������Ա����

	public ReinsCaseStatusVO(){
		
	}
	public String getClaimNo() {
		return this.claimNo;
	}

	public void setClaimNo(String claimNo) {
		this.claimNo = claimNo;
	}

	public String getOperateComCode() {
		return this.operateComCode;
	}

	public void setOperateComCode(String operateComCode) {
		this.operateComCode = operateComCode;
	}

	public Date getOperateDate() {
		return this.operateDate;
	}

	public void setOperateDate(Date operateDate) {
		this.operateDate = operateDate;
	}

	public String getOperaterCode() {
		return this.operaterCode;
	}

	public void setOperaterCode(String operaterCode) {
		this.operaterCode = operaterCode;
	}

	public String getBusinessType() {
		return this.businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	/*public String getBusinessTypeCode() {
		return getBusinessType().getBusinessTypeCode();
	}
 

	public static class BusinessType {
		private String businessTypeCode;
		private BusinessType(String businessTypeCode) {
			this.businessTypeCode = businessTypeCode;
		}

		private String getBusinessTypeCode() {
			return this.businessTypeCode;
		}
		
		public static BusinessType ENDCASE = new BusinessType("0");

		public static BusinessType CANCLE = new BusinessType("1");

		public static BusinessType REFUSE = new BusinessType("2");

		public static BusinessType REOPEN = new BusinessType("3");

		
	}*/
}
