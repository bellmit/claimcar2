package ins.sino.claimcar.policyLinkage.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 
 * <pre></pre>
 * @author ★niuqiang
 */
@XStreamAlias("injuredInfos")  // 人伤
public class CaseDetailInjuredInfosVo implements Serializable{

	/**  */
	private static final long serialVersionUID = 1L;
	
	@XStreamAlias("name") 
	private String name;   //姓名
	
	@XStreamAlias("sfzmhm") 
	private String sfzmhm;   //身份证号
	
	@XStreamAlias("age") 
	private String age;   //年龄
	
	@XStreamAlias("sex") 
	private String sex;   //性别
	
	@XStreamAlias("hj") 
	private String hj;   //户籍类型城镇(1),农村(2),非农(3)
	
	@XStreamAlias("orders") 
	private String orders;   //排序

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSfzmhm() {
		return sfzmhm;
	}

	public void setSfzmhm(String sfzmhm) {
		this.sfzmhm = sfzmhm;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getHj() {
		return hj;
	}

	public void setHj(String hj) {
		this.hj = hj;
	}

	public String getOrders() {
		return orders;
	}

	public void setOrders(String orders) {
		this.orders = orders;
	}
	
	
	
	

}
