package org.example.xlPra.domain.upload.service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.xml.parsers.SAXParserFactory;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStrings;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.example.xlPra.XlPraApplication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

@Service
public class UploadService {
	
	public void uploadXlsx(MultipartFile file) throws Exception {
		
		// 파일 객체롤 얻음 
		OPCPackage pkg = OPCPackage.open(file.getInputStream());
		XSSFReader xssfReader = new XSSFReader(pkg);
		
		List<String> sheetNames = getSheetNames(pkg);
		System.out.println(sheetNames.size());
		for (String name : sheetNames) {
		    System.out.println(name);
		}
		
		// 첫 번째 시트 가져오기 (iter라서 next로 다음 다음 가져오면 됨)
		InputStream sheetStream = xssfReader.getSheetsData().next();

		// 공통 문자열 테이블 가져오기 (공통 문자열을 위의 개별 시트에 저장하지 않고 공통으로 저장)
		SharedStrings sst = xssfReader.getSharedStringsTable();
		SharedStringsTable sstTable = (SharedStringsTable) sst;
		
		// 파서 및 핸들러 등록
		XMLReader parser = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
		parser.setContentHandler(new SheetHandler(sstTable));
		
		parser.parse(new InputSource(sheetStream));
		
	}
	
	private List<String> getSheetNames(OPCPackage pkg) throws Exception {

	    List<String> names = new ArrayList<>();
	    InputStream workbookXml = pkg.getPartsByName(Pattern.compile("/xl/workbook.xml")).get(0).getInputStream();

	    XMLReader parser = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
	    parser.setContentHandler(new DefaultHandler() {
	        @Override
	        public void startElement(String uri, String localName, String name, Attributes attributes) {
	            if ("sheet".equals(name)) {
	                names.add(attributes.getValue("name"));
	            }
	        }
	    });

	    parser.parse(new InputSource(workbookXml));
	    return names;
	}
}
