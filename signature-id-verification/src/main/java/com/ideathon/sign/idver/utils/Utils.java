package com.ideathon.sign.idver.utils;

import static com.ideathon.sign.idver.domain.Constants.JPEG_BYTE64_PREFIX;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.UUID;
import java.util.zip.ZipInputStream;

import javax.xml.bind.DatatypeConverter;

import org.springframework.util.FileCopyUtils;

public class Utils {

	private Utils() {}

	public static String inputStreamToBase64(Optional<InputStream> inputStream) {
	    if (inputStream.isPresent()) {
	        ByteArrayOutputStream output = new ByteArrayOutputStream();
	        try {
				FileCopyUtils.copy(inputStream.get(), output);
			} catch (IOException e) {
				e.printStackTrace();
			}
	        return Optional.ofNullable(JPEG_BYTE64_PREFIX + DatatypeConverter.printBase64Binary(output.toByteArray())).get();
	    }

	    return "";
	}
	
	public static boolean isZipFile(String string) throws IOException, FileNotFoundException {
		return new ZipInputStream(new FileInputStream(string)).getNextEntry() != null;
	}

	public static String id() {
		return UUID.randomUUID().toString();
	}
	
}
