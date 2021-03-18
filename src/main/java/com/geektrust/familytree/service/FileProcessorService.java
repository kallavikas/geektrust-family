package com.geektrust.familytree.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public interface FileProcessorService {

	void processInputStream(FamilyService family, InputStream inputStream) throws IOException;

	void processInputFile(FamilyService family, File file);

}
