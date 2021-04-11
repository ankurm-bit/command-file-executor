package in.mozark.dto;

import java.io.File;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
public class UploadFileDto implements Serializable {

	private static final long serialVersionUID = 9014943962270784753L;
	
	@JsonProperty("eventAttributes")
	private EventAttributeDto eventAttributes;
	private String eventName;
	private File fileData;
	@JsonProperty("mozark.eventAttributes")
	private MozarkEventAttributeDto mozarkEventAttributeDto;
	public EventAttributeDto getEventAttributeDto() {
		return eventAttributes;
	}
	public void setEventAttributeDto(EventAttributeDto eventAttributes) {
		this.eventAttributes = eventAttributes;
	}
	public String getEventName() {
		return eventName;
	}
	public void setEventName(String eventName) {
		this.eventName = eventName;
	}
	public File getFileData() {
		return fileData;
	}
	public void setFileData(File fileData) {
		this.fileData = fileData;
	}
	public MozarkEventAttributeDto getMozarkEventAttributeDto() {
		return mozarkEventAttributeDto;
	}
	public void setMozarkEventAttributeDto(MozarkEventAttributeDto mozarkEventAttributeDto) {
		this.mozarkEventAttributeDto = mozarkEventAttributeDto;
	}
	@Override
	public String toString() {
		return "UploadFileDto [eventAttributeDto=" + eventAttributes + ", eventName=" + eventName + ", fileData="
				+ fileData + ", mozarkEventAttributeDto=" + mozarkEventAttributeDto + "]";
	}
	
	

}
