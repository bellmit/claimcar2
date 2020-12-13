package ins.sino.claimcar.claimjy.vo;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "CollisionParts")
@XmlAccessorType(XmlAccessType.FIELD)
public class CollisionParts implements Serializable{
	private static final long serialVersionUID = 1L;
	@XmlElement(name = "CollisionWay")
	private String collisionWay;//损失部位 理赔转换
	@XmlElement(name = "CollisionDegree")
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
