package it.regioneveneto.myp3.gestgraduatorie.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.StringUtils;

import it.regioneveneto.myp3.gestgraduatorie.model.Allegato;
import it.regioneveneto.myp3.gestgraduatorie.repository.ServiceRepository;
//import it.regioneveneto.myp3.gestgraduatorie.model.FileAllegato;
import it.regioneveneto.myp3.mybox.BoxRepository;
import it.regioneveneto.myp3.mybox.BoxRepositoryCreationException;
import it.regioneveneto.myp3.mybox.BoxRepositoryFactory;
import it.regioneveneto.myp3.mybox.ContentMetadata;
import it.regioneveneto.myp3.mybox.RepositoryAccessException;

@Service
public class FileStorageService {

	@Value("${gestgraduatorie.mybox.myBoxConfigurationFilePath}")
	private String myBoxConfigurationFilePath;
//	@Value("${registropg.backetDefault}")
//	private String backetDefault;
	
	@Autowired
	ServiceRepository serviceRepository;

//	@Autowired
//	AnaPgService anaPgService;

	/**
	 * 
	 * @param  file                         il file da da uploadare
	 * @param  bucketName                   il valore di bucketName coincide con il codice ipa dell'utente loggato
	 * @return idMybox                      l'id di mybox relativo al file appena caricato sul repository
	 */
	public String storeFile(MultipartFile file, String bucketName) {
		
		
		
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		String idMybox = "";
		try {
			
			BoxRepository repository = this.getBoxRepository();			
			InputStream inputStream = file.getInputStream();
			ContentMetadata contentMetadata = new ContentMetadata();
			contentMetadata.setFileName(fileName);
			contentMetadata.setLength(file.getSize());
			contentMetadata.setMimeType(file.getContentType());
			idMybox = repository.put(bucketName, inputStream, contentMetadata);
		
		} catch (IOException | RepositoryAccessException | BoxRepositoryCreationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return idMybox;
	}

	

	public ResponseEntity loadFileAsResource(Long id_all, String ente, HttpServletRequest request) throws IOException {
		
		Allegato allegato = serviceRepository.getAllegatoById(id_all,ente);
		String idmybox = allegato.getIdMyBox();
		String userId = allegato.getUserId();
		try {
			BoxRepository repository = this.getBoxRepository();
			InputStream inputStream = repository.get(userId.toLowerCase(),idmybox);

			Resource resource = new InputStreamResource(inputStream);
	        	
	 		return ResponseEntity.ok()
	                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + allegato.getNomeFile())
	                .body(resource);
 		
		} catch (IOException ex) {
			ex.printStackTrace();
        } catch (BoxRepositoryCreationException e) {
			e.printStackTrace();
		} catch (RepositoryAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public BoxRepository getBoxRepository() throws IOException, BoxRepositoryCreationException {
		File configurationFile = new File(this.myBoxConfigurationFilePath);
		if (configurationFile.exists()) {
			InputStream stream = null;
			Exception throwable = null;try {
			Properties properties = new Properties();
			stream = new FileInputStream(configurationFile);
			properties.load(stream);
			return BoxRepositoryFactory.getInstance(properties);
		} catch (Exception e) {
			throwable = e;
		} finally {
			IOUtils.closeQuietly(stream);
		}if (throwable != null)
			throw new RuntimeException(throwable);
		}
		return BoxRepositoryFactory.getInstance();
		
	}
	
}

