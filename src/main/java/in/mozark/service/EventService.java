package in.mozark.service;

import in.mozark.dto.ResponseDto;
import in.mozark.dto.UploadFileDto;

public interface EventService {

	ResponseDto upload(UploadFileDto uploadFileDto);
	ResponseDto executeCommand(UploadFileDto uploadFileDto);
}
