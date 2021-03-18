package com.geektrust.familytree.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;

import com.geektrust.familytree.enums.Commands;
import com.geektrust.familytree.enums.ErrorCodes;
import com.geektrust.familytree.service.FamilyService;
import com.geektrust.familytree.service.FileProcessorService;

public class FileProcessorServiceImpl implements FileProcessorService {

	/**
	 * Process file.
	 * 
	 * @param family      - object on which the command to be processed
	 * @param file        - file to be processed
	 * 
	 */
	@Override
	public void processInputFile(FamilyService family, File file) {
		try (Scanner sc = new Scanner(file)) {
			while (sc.hasNextLine()) {
				String command = sc.nextLine();

					processInputCommand(family, command);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("File Not Found!! Please check the file and the location provided!");
		}
	}
	
	/**
	 * Process inputstream and init family tree.
	 * 
	 * @param family      - object on which the command to be processed
	 * @param file        - file to be processed	 */
	@Override
	public void processInputStream(FamilyService family, InputStream inputStream) throws IOException {
		try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
			String command;
			while ((command = br.readLine()) != null) {
				processInitCommand(family, command);
			}
		}
	}

	/**
	 * Process a command and return the output string
	 * 
	 * @param family  - object on which the command to be processed
	 * @param command - input command string to be processed
	 * @return
	 */
	private void processInputCommand(FamilyService family, String command) {
		String[] commandParams = command.split(" ");
		String commandResult;
		switch (Commands.valueOf(commandParams[0])) {
		case ADD_CHILD:
			commandResult = family.addchild(commandParams[1], commandParams[2], commandParams[3]).getValue();
			break;

		case GET_RELATIONSHIP:
			commandResult = family.getRelationship(commandParams[1], commandParams[2]);
			break;

		default:
			commandResult = ErrorCodes.INVALID_COMMAND.getValue();
			break;
		}

		System.out.println(commandResult);
	}

	/**
	 * Process command to initialize family tree.
	 * 
	 * @param family
	 * 
	 * @param command
	 */
	private void processInitCommand(FamilyService family, String command) {
		String[] commandParams = command.split(";");
		switch (Commands.valueOf(commandParams[0])) {
		case ADD_FAMILY_HEAD:
			family.addFamilyHead(commandParams[1], commandParams[2]);
			break;

		case ADD_CHILD:
			family.addchild(commandParams[1], commandParams[2], commandParams[3]);
			break;

		case ADD_SPOUSE:
			family.addSpouse(commandParams[1], commandParams[2], commandParams[3]);
			break;

		default:
			System.out.println("INVALID INIT COMMAND!");
			break;
		}
	}
}
