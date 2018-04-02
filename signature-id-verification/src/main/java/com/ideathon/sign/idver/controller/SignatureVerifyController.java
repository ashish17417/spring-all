package com.ideathon.sign.idver.controller;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ideathon.sign.idver.domain.Result;
import com.ideathon.sign.idver.domain.VerificationResult;
import com.ideathon.sign.idver.service.OriginalSignatureService;
import com.ideathon.sign.idver.service.StorageService;
import com.ideathon.sign.idver.service.ValidateSignaturesService;
 

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping("api/signature")
public class SignatureVerifyController {

	private final StorageService storageService;
	private final OriginalSignatureService originalSignatureService;
	private final ValidateSignaturesService validateSignaturesService;
	
	@Autowired
	public SignatureVerifyController(StorageService storageService, OriginalSignatureService originalSignatureService,
			ValidateSignaturesService validateSignaturesService) {
		super();
		this.storageService = storageService;
		this.originalSignatureService = originalSignatureService;
		this.validateSignaturesService = validateSignaturesService;
	}

	@GetMapping
	@ResponseBody
	public List<Result> getAllOriginalSignatures() {
		return originalSignatureService.getAllAvailableSignatures();
	}
	
	@PostMapping("/post")
	public ResponseEntity<List<VerificationResult>> uploadFileAndVerify(@RequestParam("file") MultipartFile file, String prototype) {
		
		System.out.println("prototype recievd " + prototype);
		try { 
			storageService.store(file);
			System.out.println("You successfully uploaded " + file.getOriginalFilename() + "!");
			List<VerificationResult> ret = validateSignaturesService.verifyOriginalVsTestSubjects(prototype, file.getOriginalFilename());
			ret.sort(new Comparator<VerificationResult>() {
				@Override
				public int compare(VerificationResult o1, VerificationResult o2) {
					return (int) (o2.getMatchCount() - o1.getMatchCount());
				}
			});
			
			ret.stream().forEach(vr -> vr.setOriginalSignature(prototype));
			System.out.println(ret);
			return ResponseEntity.status(HttpStatus.OK).body(ret);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null);
		} finally {
			storageService.deleteAll();
		}
	}
 
}
	

