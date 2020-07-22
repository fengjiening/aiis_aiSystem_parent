package org.jeecg.common.util;

public class CommonReturnMethod<T> {
	String errorMsg;
	int errorCode;
	T result;
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	
	public int getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
	public T getResult() {
		return result;
	}
	public void setResult(T result) {
		this.result = result;
	}
	public CommonReturnMethod<T> retSucc(T object){
		this.result=object;
		this.errorCode=0;
		return this;
	}
	public CommonReturnMethod<T> retError(String errorMsg){
		this.errorCode=1;
		this.errorMsg=errorMsg;
		return this;
	}
	public CommonReturnMethod<T> retError(int errorCode,String errorMsg){
		this.errorCode=errorCode;
		this.errorMsg=errorMsg;
		return this;
	}
	public boolean isError(){
		if(errorCode==0)
			return false;
		else {
			return true;
		}
	}

}
