package in.mozark.service.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import in.mozark.dto.EventAttributeDto;
import in.mozark.dto.MozarkEventAttributeDto;
import in.mozark.dto.ResponseDto;
import in.mozark.dto.UploadFileDto;
import in.mozark.service.EventService;
import in.mozark.utils.ConnectingMozarkAPI;
import in.mozark.utils.UploadFileInfo;

@Service
public class EventServiceImpl implements EventService {
	private static Logger logger = LoggerFactory.getLogger(EventServiceImpl.class);

	@Autowired
	private UploadFileInfo uploadFileInfo;

	@Value("${file.upload.directory}")
	private String fileUploadDirectory;

	@Autowired
	private ConnectingMozarkAPI connectingMozarkAPI;

	@Override
	public ResponseDto upload(UploadFileDto uploadFileDto) {
		logger.info("upload {} ", uploadFileDto);
		ResponseDto response = new ResponseDto();
		try {
			EventAttributeDto eventAttributeDto = uploadFileDto.getEventAttributeDto();
			logger.info("eventAttributeDto {} ", eventAttributeDto);

			File folder = new File(fileUploadDirectory + eventAttributeDto.getTestId() + "/");

			File[] files = folder.listFiles();

			if (files.length > 0) {
				Map<String, String> fileNameAndFileType = uploadFileInfo.getFileNameAndFileType();
				logger.info("fileNameAndFileType {} ", fileNameAndFileType);

				Map<String, ResponseDto> uploadResponse = connectUploadAPI(files, fileNameAndFileType,
						uploadFileDto);
			  logger.info("uploadResponse {} ",uploadResponse);
			  response.setResponseCode("00");
			  response.setResponseMessage("sucess");
			  response.setResponseDetails(uploadResponse);
			} else {
				response.setResponseCode("01");
				response.setResponseMessage("No files present with given testId - " + eventAttributeDto.getTestId());
			}

		} catch (Exception e) {
			logger.info("exception occurred in upload {} ", e.getMessage());
			response.setResponseCode("E1");
			response.setResponseMessage("Exception occurred in upload");
		}

		return response;
	}

	private Map<String, ResponseDto> connectUploadAPI(File[] files, Map<String, String> fileNameAndFileType,
			UploadFileDto inputUploadFileDto) {
		logger.info("fileNameAndFileType {} ", fileNameAndFileType);
		ResponseDto response = new ResponseDto();
		Map<String, ResponseDto> uploadAndDeletResponse = new HashMap<String, ResponseDto>();
		String fileName = "";
		for (File file : files) {

			try {
				
				String eventName = inputUploadFileDto.getEventName();
				logger.info("eventName - {} ",eventName);
				
				EventAttributeDto inputEventAttributeDto = inputUploadFileDto.getEventAttributeDto();
				logger.info("eventAttributeDto {} ", inputEventAttributeDto);
				
				String testId = inputEventAttributeDto.getTestId();
				logger.info("testId - {} ",testId);
				
				MozarkEventAttributeDto inputMozarkAttributeDto = inputUploadFileDto.getMozarkEventAttributeDto();
				logger.info("inputMozarkAttributeDto - {} ",inputMozarkAttributeDto);
				
				fileName = file.getName();
				logger.info("fileName {} ", fileName);

				String fileType = fileNameAndFileType.get(fileName);
				logger.info("fileType {} ", fileType);
				
				if(!StringUtils.isEmpty(fileType)) {
					UploadFileDto uploadFileDto = new UploadFileDto();
					uploadFileDto.setEventName(eventName);
					uploadFileDto.setFileData(file);

					EventAttributeDto eventAttributeDto = new EventAttributeDto();

					eventAttributeDto.setFileName(fileName);
					eventAttributeDto.setFileType(fileType);
					eventAttributeDto.setTestId(testId);
					uploadFileDto.setEventAttributeDto(eventAttributeDto);
					
					MozarkEventAttributeDto mozarkEventAttributeDto = new MozarkEventAttributeDto();
					mozarkEventAttributeDto.setUserId(inputMozarkAttributeDto.getUserId());
					uploadFileDto.setMozarkEventAttributeDto(mozarkEventAttributeDto);

					logger.info("uploadFileDto {} ", uploadFileDto.toString());
					ResponseDto uploadAPIResponse = connectingMozarkAPI.connectingUploadAPI(uploadFileDto);
					logger.info("uploadAPIResponse {} ", uploadAPIResponse);

					if (uploadAPIResponse.getResponseCode().equalsIgnoreCase("00")) {
						File fileToBeDeleted = new File(fileUploadDirectory + testId + "/" + fileName);
						if (fileToBeDeleted.delete()) {
							logger.info("file deleted successfully ");
							response.setResponseCode("00");
							response.setResponseMessage("sucess");
						} else {
							logger.info("file didn't deleted successfully");
							response.setResponseCode("01");
							response.setResponseMessage("failed");
						}
						
					}
						
				}else {
					response.setResponseCode("99");
					response.setResponseMessage("File Type is not available");
					
				}
				uploadAndDeletResponse.put(fileName, response);

			} catch (Exception e) {
				logger.error("Exception occurred in connectUploadAPI {} ", e.getMessage());
				response.setResponseCode("E1");
				response.setResponseMessage("exception occuured ");
				uploadAndDeletResponse.put(fileName, response);
			}
		}

		return uploadAndDeletResponse;
	}

	@Override
	public ResponseDto executeCommand(UploadFileDto uploadFileDto) {
		logger.info("uploadFileDto {} ",uploadFileDto);
		ResponseDto responseDto = new ResponseDto();
		
		EventAttributeDto eventAttributeDto  = uploadFileDto.getEventAttributeDto();
		logger.info("eventAttributeDto {} ",eventAttributeDto);
		
		
		try {
			
	        
	       String action =  eventAttributeDto.getAction();
	       logger.info("action {} ",action);
	       if(!StringUtils.isEmpty(action) && action.equalsIgnoreCase("start")) {
	    	   String command = eventAttributeDto.getCommand();
	   		   logger.info("command {} ",command);
	    	   Process process =Runtime.getRuntime().exec(command);
				
				long pid = -1;
	            try {
	              if (process.getClass().getName().equals("java.lang.UNIXProcess")) {
	                Field f = process.getClass().getDeclaredField("pid");
	                f.setAccessible(true);
	                pid = f.getLong(process);
	                f.setAccessible(false);
	              }
	            } catch (Exception e) {
	            	logger.error("exception occurred while getting process id - ",e.getMessage());
	              pid = -1;
	            }
	            
		        String processId = String.valueOf(pid);
		        logger.info("processId {} ",processId);
	    	   File folderCreate = new File(fileUploadDirectory+eventAttributeDto.getTestId()+"/");
				if (!folderCreate.exists()) {
					if (folderCreate.mkdirs()) {
						logger.info("Folder structure created" + fileUploadDirectory+eventAttributeDto.getTestId()+"/");
					}
				} else {
					logger.info("Folder structure already exist");
				}

	    	   
	    	   File file = new File(fileUploadDirectory+eventAttributeDto.getTestId()+"/"+eventAttributeDto.getTestId()+".txt");
				if (file.exists()) {
					BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
					writer.write(processId);
					writer.close();
				} else {
					FileOutputStream fout = null;
					try {

						fout = new FileOutputStream(file);
						byte b[] = processId.getBytes();
						fout.write(b);

						logger.info("success... file has written ");
					} catch (Exception e) {
						logger.error("Exception occured while file ", e.getMessage());
					} finally {
						fout.close();
					}
				}
	       }else if(!StringUtils.isEmpty(action) && action.equalsIgnoreCase("stop")) {
	    	  
	    	  List<String> storedProcessId = new ArrayList<String>(); 
	    	   try  
				{
					File file = new File(fileUploadDirectory + eventAttributeDto.getTestId() + "/"
							+ eventAttributeDto.getTestId() + ".txt");
					FileReader fr = new FileReader(file);
					BufferedReader br = new BufferedReader(fr);
					String line;
					while ((line = br.readLine()) != null) {
						storedProcessId.add(line);
					}
					fr.close();
                   logger.info("Stored Process Id {} ",storedProcessId);
                   
                   if(!CollectionUtils.isEmpty(storedProcessId)) {
                	   storedProcessId.stream().forEach(id -> {
                		  
							try {

								Runtime rt = Runtime.getRuntime();
								if (System.getProperty("os.name").toLowerCase().indexOf("windows") > -1) {
									 Process p = rt.exec("taskkill " +id);
									 p.destroy();
									 logger.info("killed process id - ",id);
								}else {
									Process p = rt.exec("kill -9 " +id);
									p.destroy();
									logger.info("killed process id - ",id);
								}
							    
							} catch (IOException e) {
								logger.info("exception occured while killing - " + e.getMessage());
							}
                		  
                	   });
                	   ResponseDto uploadFileResponse = upload(uploadFileDto);
                	   logger.info("uploadFileResponse {} ",uploadFileResponse);
                   }
				}  
			   catch(IOException e)  
			   {  
			   logger.info("exception occurred while reading processId - ",e.getMessage()); 
			   }  
	       }
	        
	        
			
			responseDto.setResponseCode("00");
			responseDto.setResponseMessage("success");
			
		}catch(Exception e) {
			logger.info("exception occurred in - ",e.getMessage());
			responseDto.setResponseCode("E!");
			responseDto.setResponseMessage("Exception occurred");
		}
		return responseDto;
	}

	
		

	
}
