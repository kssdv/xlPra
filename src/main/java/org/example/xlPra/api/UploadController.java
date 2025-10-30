package org.example.xlPra.api;

import org.example.xlPra.domain.upload.service.UploadService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class UploadController {
	
	private final UploadService uploadService;
	
	public UploadController(UploadService uploadService) {
		this.uploadService = uploadService;
	}
	
	@GetMapping("/upload")
    public String uploadPage() {

        return "upload";
    }
	
	@PostMapping("/upload")
    public String uploadProcess(@RequestParam("file") MultipartFile file) throws Exception {

        uploadService.uploadXlsx(file);
        
        return "redirect:/upload";
    }
}
