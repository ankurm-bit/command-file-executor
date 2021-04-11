package in.mozark.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import in.mozark.dto.UploadFileDto;
import in.mozark.service.EventService;

@RestController
public class EventController {

	private static Logger logger = LoggerFactory.getLogger(EventController.class);

	@Autowired
	private EventService eventService;

	@PostMapping("/upload")
	public ResponseEntity<?> uploadFilesFromTestId(@RequestBody UploadFileDto uploadFileDto) {
		logger.info("uploadFilesFromTestId {} ", uploadFileDto);
		return new ResponseEntity<>(eventService.upload(uploadFileDto), HttpStatus.OK);
	}
	
	@PostMapping("/command")
	public ResponseEntity<?> executeGivenCommand(@RequestBody UploadFileDto uploadFileDto){
		logger.info("executeGivenCommand {} ",uploadFileDto);
		return new ResponseEntity<>(eventService.executeCommand(uploadFileDto),HttpStatus.OK);
	}
}
