package ins.sino.claimcar.moblie.commonmark.caseDetails.vo;


import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("COLLISIONPARTS")
public class CollisionInfoVo {

	private static final long serialVersionUID = 8423652723600188374L;

	@XStreamAlias("COLLISIONWAY")
	private String collisionWay;  //损失部位
	@XStreamAlias("COLLISIONDEGREE")
	private String collisionDegree;  //损失程度
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
