package cn.apisium.beelogin;

public class GsonResult {
	public boolean result = false;
	public String rawResult = "";
	public String reason = "";
	public int statuId = 0;

	public GsonResult(boolean result, String rawResult, String reason, int statuId) {
		this.result = result;
		this.rawResult = rawResult;
		this.statuId = statuId;
	}
}
