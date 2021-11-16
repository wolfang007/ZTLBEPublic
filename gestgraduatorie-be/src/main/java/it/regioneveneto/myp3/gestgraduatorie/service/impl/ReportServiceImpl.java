package it.regioneveneto.myp3.gestgraduatorie.service.impl;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import it.regioneveneto.myp3.gestgraduatorie.repository.ServiceContributoRepository;
import it.regioneveneto.myp3.gestgraduatorie.service.ReportService;
import it.regioneveneto.myp3.gestgraduatorie.utils.ObjectMapperUtils;
import it.regioneveneto.myp3.gestgraduatorie.web.dto.Bean;
import it.regioneveneto.myp3.gestgraduatorie.web.dto.IstanzaDTO;
import it.regioneveneto.myp3.gestgraduatorie.web.dto.IstanzaFilterDTO;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleWriterExporterOutput;

@Service
public class ReportServiceImpl implements ReportService {
	
	private static final Logger logger = LoggerFactory.getLogger(ReportServiceImpl.class);
    public static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyyMMdd");
    
    public static final SimpleDateFormat FORMAT_DATA = new SimpleDateFormat("dd/MM/yyyy");
	@Value("${gestgraduatorie.report.templateBasePath}")
    private String templateBasePath;
    

    @Value("${gestgraduatorie.report.templateElencoIstanze}")
    private String templateElencoIstanze;
    
    @Value("${gestgraduatorie.report.templateElencoIstanzeNoBando}")
    private String templateElencoIstanzeNoBando;
    
	@Value("${gestgraduatorie.report.images.basePath}")
    private String imagesBasePath;

	@Value("${gestgraduatorie.report.images.fileNameCompanylogo}")
    private String fileNameCompanylogo;
	
    @Value("${gestgraduatorie.report.templateCompilato}")
    private boolean templateCompilato;
    
    @Autowired
    public Environment env;

  
    @Autowired
	ServiceContributoRepository contributoRepo;

	@Override
	public byte[] getElencoIstanze(IstanzaFilterDTO filterRichiesta, String ente, String report) throws Exception {
		
	
        String templateReport = templateBasePath + "/";
        String imagesReportBasePath = imagesBasePath + "/";
        //a seconda se è presente l'id del bando carico un template piuttosto che un altro
        templateReport += filterRichiesta.getIdBando() != null ? templateElencoIstanze : templateElencoIstanzeNoBando;
        Map<String, Object> parameters =  new HashMap<>();    
	
		
		
		//String comune = contributoRepo.findByCodiceIstat(codice_istat_s).get().getDenominazione();
		//logger.info("comune : " + comune);
		String logo = ente + ".png";
		logger.info("logo : " + logo);
		
		
		List<IstanzaDTO> richiestaList = ObjectMapperUtils.mapAll(
										contributoRepo.getRichieste(filterRichiesta, ente), 
										IstanzaDTO.class);
		
		int rows = richiestaList.size();
		logger.info("numero di righe ritornate : " + rows);
		parameters.put("fileNameCompanylogo",imagesReportBasePath+logo);

            
        if(richiestaList == null || richiestaList.isEmpty()) {
        	logger.info("La query non prodotto alcun risultato");
        	
        	throw new Exception(" La query non prodotto alcun risultato");
        	
        }else {
        	logger.info("getAnagraficaCittadinoRicercaManuale --> "+ richiestaList.toString());
        }
        if(report.equals("PDF"))
        	return creaReportPDF(templateReport,parameters,richiestaList,"MANUALE");
        else if(report.equals("XLS"))
        	return creaReportXLS(templateReport,parameters,richiestaList,"MANUALE");
        else
        	return creaReportCSV(templateReport,parameters,richiestaList,"MANUALE");
	}
	
private byte[] creaReportPDF(String templateReport, Map<String, Object> parameters, List<IstanzaDTO> istanzeList,String queryType) throws Exception {
		
		long startTime = System.nanoTime();
		long endTime = 0L;
		//File pdfFile = new File(System.getProperty("java.io.tmpdir") + "invioReport" + startTime + ".pdf");
		ByteArrayOutputStream ostream = new ByteArrayOutputStream();
		try {

			//if (pdfFile.createNewFile()) {
				JasperPrint jasperPrint;
				if (templateCompilato) {
					jasperPrint = JasperFillManager.fillReport(templateReport,parameters,getDataSource(istanzeList,queryType));

				} else {
					JasperReport jasperReport = JasperCompileManager.compileReport(templateReport);
					jasperPrint = JasperFillManager.fillReport(jasperReport,parameters,getDataSource(istanzeList,queryType));
				}
				
				JRPdfExporter cdu = new JRPdfExporter();
				cdu.setExporterInput(new SimpleExporterInput(jasperPrint));
				cdu.setExporterOutput(new SimpleOutputStreamExporterOutput(ostream));
				cdu.exportReport();
			//}

			endTime = System.nanoTime();
			logger.info("TIME CREA PDF: "
					+ ((double) (endTime - startTime) / 1_000_000_000) + "secondi");

		} catch (JRException  ex) {
			logger.error(ex.getMessage());
			throw ex;
		}
		ostream.close();
		return ostream.toByteArray();
	}

/**
 * <code>creaReportXLS</code>        metodopo privato per la creazione di report di tipo XLS
 * 
 * @param templateReport             il template per visualizzare il report
 * @param parameters                 la mappa dei paramentri da passare al report
 * @param istanzeList                la lista dei risultati da visualizzare nel report
 * @param queryType                  la tipologia di report
 * @return
 * @throws Exception
 */
private byte[] creaReportXLS(String templateReport, Map<String, Object> parameters,List<IstanzaDTO> istanzeList,String queryType) throws Exception {
	
	long startTime = System.nanoTime();
	long endTime = 0L;
	//File xlsFile = new File(System.getProperty("java.io.tmpdir") + "invioReport" + startTime + ".xls");
	ByteArrayOutputStream ostream = new ByteArrayOutputStream();
	try {

		//if (xlsFile.createNewFile()) {
			JasperPrint jasperPrint;
			if (templateCompilato) {
				jasperPrint = JasperFillManager.fillReport(templateReport,parameters,getDataSource(istanzeList,queryType));

			} else {
				JasperReport jasperReport = JasperCompileManager.compileReport(templateReport);
				jasperPrint = JasperFillManager.fillReport(jasperReport,parameters,getDataSource(istanzeList,queryType));
			}
			
			JRXlsExporter exporter = new JRXlsExporter();
            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(ostream));
            exporter.exportReport();
			
		//}

		endTime = System.nanoTime();
		logger.info("TIME CREA XLS: "
				+ ((double) (endTime - startTime) / 1_000_000_000) + "secondi");

	} catch (JRException ex) {
		logger.error(ex.getMessage());
		throw ex;
	}
	ostream.close();
	return ostream.toByteArray();
}
/**
 * <code>creaReportCSV</code>        metodopo privato per la creazione di report di tipo CSV
 * 
 * @param templateReport             il template per visualizzare il report
 * @param parameters                 la mappa dei paramentri da passare al report
 * @param istanzeList                la lista dei risultati da visualizzare nel report
 * @param queryType                  la tipologia di report
 * @return
 * @throws Exception
 */
private byte[] creaReportCSV(String templateReport, Map<String, Object> parameters,List<IstanzaDTO> istanzeList,String queryType) throws Exception {
	
	long startTime = System.nanoTime();
	long endTime = 0L;
	//File csvFile = new File(System.getProperty("java.io.tmpdir") + "invioReport" + startTime + ".csv");
	ByteArrayOutputStream ostream = new ByteArrayOutputStream();
	try {

		//if (csvFile.createNewFile()) {
			JasperPrint jasperPrint;
			if (templateCompilato) {
				jasperPrint = JasperFillManager.fillReport(templateReport,parameters,getDataSource(istanzeList,queryType));

			} else {
				JasperReport jasperReport = JasperCompileManager.compileReport(templateReport);
				jasperPrint = JasperFillManager.fillReport(jasperReport,parameters,getDataSource(istanzeList,queryType));
			}
			
			JRCsvExporter exporter = new JRCsvExporter();
            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            exporter.setExporterOutput(new SimpleWriterExporterOutput(ostream));
            exporter.exportReport();

		//}

		endTime = System.nanoTime();
		logger.info("TIME CREA csv: "
				+ ((double) (endTime - startTime) / 1_000_000_000) + "secondi");

	} catch (JRException  ex) {
		logger.error(ex.getMessage());
		throw ex;
	}
	ostream.close();
	return ostream.toByteArray();
}


private void createXLSSheet () throws Exception {
	
	
	
}

/**
 * <code>getDataSource</code>         crea il JRDataSource da dare in pasto al metodo FillReport
 * 
 * @param istanzeList                 la lista delle istanze
 * @param queryType                   il tipo di report
 * @return
 */
private  JRDataSource getDataSource(List<IstanzaDTO> istanzeList,String queryType) {
	Collection<Bean> coll = new ArrayList<Bean>();
	
	for (IstanzaDTO istanza : istanzeList) {	
		coll.add(new Bean(istanza.getIdBando() != null ? Long.toString(istanza.getIdBando()) : "", null, istanza.getDenominazioneBando(), istanza.getNumeroProtocollo(), StringUtils.capitalize(istanza.getNome()),StringUtils.capitalize(istanza.getCognome()),
				istanza.getCodiceFiscale().toUpperCase(),StringUtils.capitalize(istanza.getPunteggio())));
		
	}

	return new JRBeanCollectionDataSource(coll);
}

@Override
public Collection<Bean> getElencoDtoIstanze(IstanzaFilterDTO filterRichiesta, String ente)
		throws Exception {
	
	List<IstanzaDTO> richiestaList = ObjectMapperUtils.mapAll(contributoRepo.getRichieste(filterRichiesta, ente), IstanzaDTO.class);
	
   Collection<Bean> coll = new ArrayList<Bean>();
	
	for (IstanzaDTO istanza : richiestaList) {	
		coll.add(new Bean(istanza.getIdBando() != null ? Long.toString(istanza.getIdBando()) : "", istanza.getId() != null ? Long.toString(istanza.getId()) : "",  istanza.getDenominazioneBando(), istanza.getNumeroProtocollo(), StringUtils.capitalize(istanza.getNome()),StringUtils.capitalize(istanza.getCognome()),
				istanza.getCodiceFiscale().toUpperCase(),StringUtils.capitalize(istanza.getPunteggio())));
		
	}
	
	return coll;
}
    
}



