package com.geektrust.familytree.service;

import com.geektrust.familytree.enums.ErrorCodes;

public interface FamilyService {

	void addFamilyHead(String name, String gender);

	void addSpouse(String memberName, String spouseName, String gender);

	ErrorCodes addchild(String motherName, String childName, String gender);

	String getRelationship(String person, String relationship);

}
