package com.ideathon.sign.idver.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.google.common.collect.ImmutableList;
import com.ideathon.sign.idver.domain.Result;

@Service
public class OriginalSignatureService {

	public List<Result> getAllAvailableSignatures() {
		String dir = "originals/";
		try {
			return Files.walk(Paths.get(new File(dir).toURI())).filter(filePath -> !filePath.toFile().getName().equals("originals"))
					.map(filePath -> new Result(filePath.toFile().getName(), filePath.toFile().getName()))
					.collect(Collectors.toList());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ImmutableList.of(new Result("None Found", "None Found"));

	}
}
