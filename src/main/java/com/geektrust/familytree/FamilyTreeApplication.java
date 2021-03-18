package com.geektrust.familytree;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.geektrust.familytree.service.FamilyService;
import com.geektrust.familytree.service.FileProcessorService;
import com.geektrust.familytree.service.impl.FamilyServiceImpl;
import com.geektrust.familytree.service.impl.FileProcessorServiceImpl;

public class FamilyTreeApplication {
	
	private String initFilePath="/initInput.txt";

	private FamilyService familyService;
	
	private FileProcessorService fileProcessorService;
	
	

	public FamilyTreeApplication(FamilyService familyService,
			FileProcessorService fileProcessorService) {
		this.familyService = familyService;
		this.fileProcessorService = fileProcessorService;
	}

	public static void main(String[] args) throws Exception {
		try {
			FamilyTreeApplication obj=new FamilyTreeApplication(
					new FamilyServiceImpl(),new FileProcessorServiceImpl());
			obj.initApp(args[0]);
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("Please enter file location(s)!");
		}
	}
	
	private void initApp(String filePath) throws Exception {
		initFileToProcess(familyService, initFilePath, false);
		initFileToProcess(familyService,filePath , true);		
	}

	/**
	 * Read file to process.
	 * 
	 * @param familyservice
	 * @param filePath
	 * @param isInputFile
	 * @throws IOException 
	 * @throws Exception 
	 * @throws FileNotFoundException
	 */
	public void initFileToProcess(FamilyService familyservice, String filePath, boolean isInputFile) throws IOException {
		if(isInputFile)
			fileProcessorService.processInputFile(familyservice, new File(filePath));
		else
			fileProcessorService.processInputStream(familyservice,getClass().getResourceAsStream(filePath));
	}


}
