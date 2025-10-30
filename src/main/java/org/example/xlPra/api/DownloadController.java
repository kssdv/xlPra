package org.example.xlPra.api;

import java.nio.charset.StandardCharsets;

import org.example.xlPra.domain.service.DownloadService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DownloadController {
	
	private final DownloadService downloadService;
	
	public DownloadController(DownloadService downloadService) {
		this.downloadService = downloadService;
	}
	
	// 시작일 끝일 사이의 데이터만 받아서 반환을 하는데 이번에는 간단하게 구현
	@GetMapping("/download")
	public ResponseEntity<?> downlaodProcess() {
		
		// 엑셀 데이터는 byte[]로 받음
        byte[] excelBytes = downloadService.downloadXlsx();
		
		// 응답할 파일명, 인코딩 후 공백은 %20으로 치환
        String fileName = "엑셀파일.xlsx";
        String encodedFileName = java.net.URLEncoder.encode(fileName, StandardCharsets.UTF_8).replaceAll("\\+", "%20");

        // 응답 헤더 (엑셀 파일로)
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=\"" + encodedFileName + "\"");
        headers.add("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

		return new ResponseEntity<>(HttpStatus.OK);
	}
}
