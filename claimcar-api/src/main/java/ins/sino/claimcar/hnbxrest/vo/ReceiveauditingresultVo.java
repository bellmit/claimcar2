package ins.sino.claimcar.hnbxrest.vo;


public class ReceiveauditingresultVo  implements java.io.Serializable{
	
	private static final long serialVersionUID = 8423652723600188374L;

    private String casenumber;//快处报案号
    private String inscaseno;//保险报案号
    private String datatype;//接口数据类型 0-照片审核结果 1-是否符合在线定损要求
    private String isqualify;//照片是否合格 0-不合格 1-合格
    private String imagetype;//照片类型 11-对方定损照片 12-已方定损照片
    private String isass;//是否符合线上定损要求 0-不符合 1-符合
    private String username;//用户名 附表1——保险公司机构代码
    private String password;//密码 888888a
    private String operateuser;//操作人员
    
    
    public String getInscaseno() {
        return inscaseno;
    }
    
    public void setInscaseno(String inscaseno) {
        this.inscaseno = inscaseno;
    }
    
    public String getDatatype() {
        return datatype;
    }
    
    public void setDatatype(String datatype) {
        this.datatype = datatype;
    }
    
    public String getIsqualify() {
        return isqualify;
    }
    
    public void setIsqualify(String isqualify) {
        this.isqualify = isqualify;
    }
    
    public String getImagetype() {
        return imagetype;
    }
    
    public void setImagetype(String imagetype) {
        this.imagetype = imagetype;
    }
    
    public String getIsass() {
        return isass;
    }
    
    public void setIsass(String isass) {
        this.isass = isass;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }

	public String getCasenumber() {
		return casenumber;
	}

	public void setCasenumber(String casenumber) {
		this.casenumber = casenumber;
	}

	public String getOperateuser() {
		return operateuser;
	}

	public void setOperateuser(String operateuser) {
		this.operateuser = operateuser;
	}
    
}
