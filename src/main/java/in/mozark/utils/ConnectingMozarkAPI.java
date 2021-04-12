package in.mozark.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import in.mozark.dto.ResponseDto;
import in.mozark.dto.UploadFileDto;

@Component
public class ConnectingMozarkAPI {

	private static Logger logger = LoggerFactory.getLogger(ConnectingMozarkAPI.class);

	@Value("${mozark.upload.api}")
	private String mozarkUploadAPI;

	public ResponseDto connectingUploadAPI(UploadFileDto uploadFileDto) {
		logger.info("connectingUploadAPI {} ", uploadFileDto);
		ResponseDto response = new ResponseDto();
		try {
   		 HttpHeaders headers = new HttpHeaders();
   		 headers.setContentType(MediaType.MULTIPART_FORM_DATA);
   		 
   		 FileSystemResource file = new FileSystemResource(uploadFileDto.getFileData());
   		 MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
   		 body.add("eventName", uploadFileDto.getEventName());
   		 body.add("file",file);
   		 body.add("eventAttributes", uploadFileDto.getEventAttributeDto());
   		 body.add("mozark.eventAttributes", uploadFileDto.getMozarkEventAttributeDto());
   		 logger.info("request body {} ",body.toString());
   		 HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
   		 RestTemplate restTemplate = new RestTemplate();
   		 logger.info("mozarkUploadAPI {} ",mozarkUploadAPI);
   		 ResponseEntity<Object> uploadResponse = restTemplate.exchange(mozarkUploadAPI, HttpMethod.POST, requestEntity, Object.class);
   		 logger.info("Response From Upload API: "+uploadResponse.getBody());
   		 
   		 if(uploadResponse!=null && uploadResponse.getStatusCodeValue()==200) {
   			response.setResponseCode("00");
   			response.setResponseMessage("sucess");
   		 }else {
   			response.setResponseCode("01");
   			response.setResponseMessage("failed");
   		 }
		} catch (Exception e) {
			logger.info("exception occurred while connecting to upload API {} ", e.getMessage());
			response.setResponseCode("E1");
			response.setResponseMessage("exception occurred ");
		}
		return response;
	}
}
