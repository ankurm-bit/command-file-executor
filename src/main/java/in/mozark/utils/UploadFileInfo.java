package in.mozark.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class UploadFileInfo {
	private static Logger logger = LoggerFactory.getLogger(UploadFileInfo.class);

	public Map<String, String> getFileNameAndFileType() {
       String propertiesFilename = "config/fileNameAndFileType.properties";
		logger.info("propertiesFilename {} ", propertiesFilename);

		Map<String, String> properties = new HashMap<>();

		try (InputStream stream = new FileInputStream(propertiesFilename)) {
			Properties prop = new Properties() {
				@Override
				public synchronized Object put(Object key, Object value) {
					return properties.put(key.toString(), value.toString());
				}
			};
			prop.load(stream);
		} catch (IOException e) {
			logger.info("exception occurred while reading property file ", e.getMessage());
		}

		logger.info("key value pair read from property file {} ", properties);
		return properties;
	}
}
