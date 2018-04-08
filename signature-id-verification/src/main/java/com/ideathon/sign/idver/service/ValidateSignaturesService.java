package com.ideathon.sign.idver.service;

import static com.ideathon.sign.idver.domain.Constants.ARCH_MODEL_PROPERTY;
import static com.ideathon.sign.idver.domain.Constants.OS;
import static com.ideathon.sign.idver.domain.Constants.OS_NAME_PROPERTY;
import static com.ideathon.sign.idver.utils.Utils.id;
import static com.ideathon.sign.idver.utils.Utils.inputStreamToBase64;
import static com.ideathon.sign.idver.utils.Utils.isZipFile;
import static java.lang.System.getProperty;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.apache.log4j.Logger;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.Scalar;
import org.opencv.features2d.DMatch;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.FeatureDetector;
import org.opencv.features2d.Features2d;
import org.opencv.highgui.Highgui;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import com.ideathon.sign.idver.ApplicationProperties;
import com.ideathon.sign.idver.domain.OriginalBlueprint;
import com.ideathon.sign.idver.domain.VerificationResult;

@Service
@EnableConfigurationProperties(value = {ApplicationProperties.class})
public class ValidateSignaturesService {

	private static Logger LOG = Logger.getLogger(ValidateSignaturesService.class);
	private FeatureDetector featureDetector;
	private DescriptorExtractor descriptorExtractor;
	private DescriptorMatcher descriptorMatcher;
	private float nndrRatio = 0.7f;
	private ApplicationProperties applicationProperties;

	public ValidateSignaturesService(ApplicationProperties applicationProperties) {
		this.applicationProperties = applicationProperties;
		loadRequiredDlls();
		configureSURF();
	}
	
	public List<VerificationResult> verifyOriginalVsTestSubjects(String originalPath, String testSubjectPath) {
		OriginalBlueprint originalBlueprint = createBlueprintForInput(applicationProperties.getStorageOriginalDir() + originalPath);
		try {
			return verifyTestSubjects(applicationProperties.getStorageUploadDir() + testSubjectPath, originalBlueprint);
		} catch (IOException e) {
			LOG.error("Error occurred ", e);
		}

		return null;
	}

	private List<VerificationResult> verifyTestSubjects(String string, OriginalBlueprint originalBlueprint)
			throws FileNotFoundException, IOException {
		List<VerificationResult> returnList = new ArrayList<>();
		File dir = null;
		if (isZipFile(string)) {
			LOG.info("Incoming is zip file" + string);
			String id = id();
			StorageService.unzip(string, applicationProperties.getStorageUnzipPath() + id + "/");
			Files.walk(Paths.get(new File(applicationProperties.getStorageUnzipPath() + id + "/").toURI())).forEach(file -> {
				matchIndividualFIle(originalBlueprint, returnList, file);
			});

		} else {
			LOG.info("Incoming is not zip file" + string);
			returnList.add(matchEachFile(originalBlueprint, string));
		}

		if (dir != null)
			dir.delete();

		return returnList;
	}

	private void matchIndividualFIle(OriginalBlueprint originalBlueprint, List<VerificationResult> returnList,
			Path filePath) {
		LOG.info(filePath.toFile().getAbsolutePath());
		if (filePath.toFile().isDirectory()) {
			return;
		}
		try {
			returnList.add(matchEachFile(originalBlueprint, filePath.toFile().getAbsolutePath()));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
	}

	private VerificationResult matchEachFile(OriginalBlueprint originalBlueprint, String bookScene) throws FileNotFoundException {

		LOG.info("Now Matching " + bookScene);
		Mat sceneImage = Highgui.imread(bookScene, Highgui.CV_LOAD_IMAGE_COLOR);
		MatOfKeyPoint sceneKeyPoints = new MatOfKeyPoint();
		MatOfKeyPoint sceneDescriptors = new MatOfKeyPoint();
		featureDetector.detect(sceneImage, sceneKeyPoints);
		descriptorExtractor.compute(sceneImage, sceneKeyPoints, sceneDescriptors);
		
		List<MatOfDMatch> matches = new LinkedList<MatOfDMatch>();
		descriptorMatcher.knnMatch(originalBlueprint.getObjectDescriptors(), sceneDescriptors, matches, 2);

		LOG.info("Calculating good match list...");
		LinkedList<DMatch> goodMatchesList = buildGoodMatchesList(matches);

		if (goodMatchesList.size() >= applicationProperties.getMatchIndexThreshold()) {
			LOG.info("Object Found!!! for" + bookScene + " with match count " + goodMatchesList.size());
			return buildVerificationResult(originalBlueprint, bookScene, goodMatchesList, true);

		} else {
			return buildVerificationResult(originalBlueprint, bookScene, goodMatchesList, false);
		}

	}

	private LinkedList<DMatch> buildGoodMatchesList(List<MatOfDMatch> matches) {
		LinkedList<DMatch> goodMatchesList = new LinkedList<DMatch>();
		matches.forEach(matOfMatch -> {
			DMatch[] dmatcharray = matOfMatch.toArray();
			if (dmatcharray[0].distance <= dmatcharray[1].distance * nndrRatio) {
				goodMatchesList.addLast(dmatcharray[0]);
			}
		});
		
		return goodMatchesList;
	}


	private VerificationResult buildVerificationResult(
			OriginalBlueprint originalBlueprint, 
			String bookScene,
			LinkedList<DMatch> goodMatchesList, 
			boolean result) throws FileNotFoundException {
		return new VerificationResult(
				originalBlueprint.getOriginalPath(), 
				bookScene.contains("\\") ? bookScene.substring(bookScene.lastIndexOf("\\") ) : bookScene, 
			    result, 
			    goodMatchesList.size(), 
				inputStreamToBase64(Optional.of(new FileInputStream(bookScene))),
				result ? "amber" : "red");
	}

	private OriginalBlueprint createBlueprintForInput(String originalPath) {

		Mat objectImage = Highgui.imread(originalPath, Highgui.CV_LOAD_IMAGE_COLOR);
		MatOfKeyPoint objectKeyPoints = new MatOfKeyPoint();
		featureDetector.detect(objectImage, objectKeyPoints);
		MatOfKeyPoint objectDescriptors = new MatOfKeyPoint();
		descriptorExtractor.compute(objectImage, objectKeyPoints, objectDescriptors);
		Mat outputImage = new Mat(objectImage.rows(), objectImage.cols(), Highgui.CV_LOAD_IMAGE_COLOR);
		Scalar newKeypointColor = new Scalar(255, 0, 0);
		Features2d.drawKeypoints(objectImage, objectKeyPoints, outputImage, newKeypointColor, 0);
		return new OriginalBlueprint(originalPath, objectImage, objectKeyPoints, objectDescriptors, newKeypointColor);
	}

	

	private void loadRequiredDlls() {
		if (getProperty(OS_NAME_PROPERTY).toUpperCase().contains(OS)) {
			if (getProperty(ARCH_MODEL_PROPERTY).endsWith("64")) {
				System.load(new File(applicationProperties.getStorageLibsX64()
						+ System.mapLibraryName(applicationProperties.getDll())).getAbsolutePath());
			} else {
				System.load(new File(applicationProperties.getStorageLibsX86()
						+ System.mapLibraryName(applicationProperties.getDll())).getAbsolutePath());
			}
		}

		LOG.info("Required DLL loaded from path");
	}
	
	private void configureSURF() {
		featureDetector = FeatureDetector.create(FeatureDetector.SURF);
		descriptorExtractor = DescriptorExtractor.create(DescriptorExtractor.SURF);
		descriptorMatcher = DescriptorMatcher.create(DescriptorMatcher.FLANNBASED);
	}
}
