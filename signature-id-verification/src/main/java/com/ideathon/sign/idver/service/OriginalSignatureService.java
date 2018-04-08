package com.ideathon.sign.idver.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import static com.ideathon.sign.idver.utils.Utils.inputStreamToBase64;
import com.google.common.collect.ImmutableList;
import com.ideathon.sign.idver.ApplicationProperties;
import com.ideathon.sign.idver.domain.Result;

@Service
@EnableConfigurationProperties(value = {ApplicationProperties.class})
public class OriginalSignatureService {

	private ApplicationProperties applicationProperties;
	private String dir;
	
	public OriginalSignatureService(ApplicationProperties applicationProperties) {
		this.applicationProperties = applicationProperties;
		this.dir = applicationProperties.getStorageOriginalDir();
	}

	public List<Result> getAllAvailableSignatures() {
		try {
			return Files.walk(Paths.get(new File(dir).toURI())).filter(filePath -> !filePath.toFile().getName().equals("originals"))
					.map(filePath -> new Result(filePath.toFile().getName(), inputStreamToBase64(getInputStream(filePath))))
					.collect(Collectors.toList());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ImmutableList.of(new Result("None Found", "None Found"));

	}

	private Optional<InputStream> getInputStream(Path filePath)  {
		try {
			return Optional.of(new FileInputStream(filePath.toFile()));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
	}
}
