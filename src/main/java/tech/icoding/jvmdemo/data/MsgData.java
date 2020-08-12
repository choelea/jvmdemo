package tech.icoding.jvmdemo.data;
import java.io.Serializable;

public class MsgData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6024748197675706614L;
	
	public static MsgData success = new MsgData("success");
	public MsgData(String msg) {
		super();
		this.msg = msg;
	}
	private String msg;
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
}