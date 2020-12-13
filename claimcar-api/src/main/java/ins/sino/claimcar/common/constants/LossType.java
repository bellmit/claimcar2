/******************************************************************************
* CREATETIME : 2016年2月3日 下午2:30:54
******************************************************************************/
package ins.sino.claimcar.common.constants;



/**
 * 损失类型
 * @author ★LiuPing
 * @CreateTime 2016年2月3日
 */
public enum LossType {
	L0("0","地面","地面财产损失"), 
	L1("1","标的","标的车财产损失"), 
	L2("2","三者","三者车财产损失"),
	L3("3","三者","三者车财产损失"), ;

	private String code;
	private String name;
	private String name2;

	private LossType(String code,String name,String name2){
		this.code = code;
		this.name = name;
		this.name2 = name2;
	}

	public String getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

	public String getName2() {
		return name2;
	}

	public boolean equals(String code) {
		return this.getCode().equals(code);
	}

	/**
	 * 将String code 转换为枚举
	 * @param code
	 * @return
	 * @modified: ☆LiuPing(2016年1月16日 ): <br>
	 */
	public static LossType codeOf(String code) {
		return LossType.valueOf("L"+code);
	}

}
