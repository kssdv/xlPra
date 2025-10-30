package org.example.xlPra.domain.download.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

@Service
public class DownloadService {

	public byte[] downloadXlsx() throws IOException {
		
		// 엑셀 Workbook 객체 생성
		Workbook workbook = new XSSFWorkbook();
//		Workbook workbook = new SXSSFWorkbook(100); // 1만개 이상의 행 일 경우 기본값 == 100
		
		// 시트 추가
		Sheet sheet1 = workbook.createSheet("Sheet1");
		Sheet sheet2 = workbook.createSheet("Sheet2");
		Sheet sheet3 = workbook.createSheet("Sheet3");
		
		// 시트1 에 데이터 추가
		// jpa 데이터를 다 조회 함 List<>
		Row sheet1_row0 = sheet1.createRow(0);
		sheet1_row0.createCell(0).setCellValue("안녕1");
		sheet1_row0.createCell(1).setCellValue("안녕2");

		for (int i = 1; i < 11; i++) {
		    Row sheet1_row = sheet1.createRow(i);
		    sheet1_row.createCell(0).setCellValue("데이터1");
		    sheet1_row.createCell(1).setCellValue("데이터2");
		}
		
		// 리턴 할 거
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		workbook.write(outputStream);
		workbook.close();
		
		// 응답
	    return outputStream.toByteArray();
	}
}
