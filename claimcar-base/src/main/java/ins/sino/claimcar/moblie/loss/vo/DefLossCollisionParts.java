package ins.sino.claimcar.moblie.loss.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("COLLISIONPARTS")
public class DefLossCollisionParts implements Serializable {
	private static final long serialVersionUID = -2893324612331120396L;
	@XStreamAlias("COLLISIONWAY")
	private String collisionWay;//损失部位 理赔转换
	@XStreamAlias("COLLISIONDEGREE")
	private String collisionDegree;//损失程度 理赔转换
	public String getCollisionWay() {
		return collisionWay;
	}
	public void setCollisionWay(String collisionWay) {
		this.collisionWay = collisionWay;
	}
	public String getCollisionDegree() {
		return collisionDegree;
	}
	public void setCollisionDegree(String collisionDegree) {
		this.collisionDegree = collisionDegree;
	}
}
