/******************************************************************************
 * CREATETIME : 2016年8月4日 下午7:14:01
 ******************************************************************************/
package ins.sino.claimcar.founder.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * <pre></pre>
 * @author ★Luwei
 */
@XStreamAlias("OUTDATE")
public class CancelDflossTaskReqOutDateVo {

	/** 报案号 **/
	@XStreamAlias("ClmNo")
	private String clmNo;

	/** 每个调度任务的唯一标示 **/
	@XStreamAlias("ExamineId")
	private String examineId;

	/**
	 * @return 返回 clmNo。
	 */
	public String getClmNo() {
		return clmNo;
	}

	/**
	 * @param clmNo 要设置的 clmNo。
	 */
	public void setClmNo(String clmNo) {
		this.clmNo = clmNo;
	}

	/**
	 * @return 返回 examineId。
	 */
	public String getExamineId() {
		return examineId;
	}

	/**
	 * @param examineId 要设置的 examineId。
	 */
	public void setExamineId(String examineId) {
		this.examineId = examineId;
	}

}
