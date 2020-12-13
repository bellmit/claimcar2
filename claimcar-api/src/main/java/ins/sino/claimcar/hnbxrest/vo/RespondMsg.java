package ins.sino.claimcar.hnbxrest.vo;


/**
 * 传输类
 * <pre></pre>
 * @author ★LinYi
 */
public class RespondMsg implements java.io.Serializable{
	private static final long serialVersionUID = 1L;
	
	/**
	 * 返回状态码
	 */
	private int restate;
	/**
	 * 错误信息
	 */
	private String redes;
	/**
     * 返回数据
     */
    private Object data;
	
	public static RespondMsg SUCCESS(){
		RespondMsg respondMsg = new RespondMsg();
		respondMsg.setRestate(RspCode.SUCCESS);
		respondMsg.setRedes("成功");
		return respondMsg;
	}
	
	public static RespondMsg SUCCESS(int restate,String redes){
        RespondMsg respondMsg = new RespondMsg();
        respondMsg.setRestate(restate);
        respondMsg.setRedes(redes);
        return respondMsg;
    }
	
	public static RespondMsg SUCCESS(int restate,String redes,Object data){
		RespondMsg respondMsg = new RespondMsg();
		respondMsg.setRestate(restate);
		respondMsg.setRedes(redes);
		respondMsg.setData(data);
		return respondMsg;
	}
	
	
	
	public static RespondMsg ERROR(String redes){
		RespondMsg respondMsg = new RespondMsg();
		respondMsg.setRestate(RspCode.FAIL);
		respondMsg.setRedes(redes);	
		return respondMsg;
	}
	
	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
	
    public int getRestate() {
        return restate;
    }
    
    public void setRestate(int restate) {
        this.restate = restate;
    }
	
    public String getRedes() {
        return redes;
    }
    
    public void setRedes(String redes) {
        this.redes = redes;
    }	
	
}
