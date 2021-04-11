package in.mozark.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
public class ResponseDto implements Serializable {

	private static final long serialVersionUID = -7973195673301350256L;

	private String responseMessage;
	private String responseCode;
	private Object responseDetails;
	public String getResponseMessage() {
		return responseMessage;
	}
	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}
	public String getResponseCode() {
		return responseCode;
	}
	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}
	public Object getResponseDetails() {
		return responseDetails;
	}
	public void setResponseDetails(Object responseDetails) {
		this.responseDetails = responseDetails;
	}
	@Override
	public String toString() {
		return "ResposeDto [responseMessage=" + responseMessage + ", responseCode=" + responseCode
				+ ", responseDetails=" + responseDetails + "]";
	}
	
	
}
