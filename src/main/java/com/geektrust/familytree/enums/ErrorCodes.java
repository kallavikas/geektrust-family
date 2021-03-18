package com.geektrust.familytree.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCodes {
	NOT_YET_IMPLEMENTED("NOT_YET_IMPLEMENTED"),
	PERSON_NOT_FOUND("PERSON_NOT_FOUND"),
	PROVIDE_VALID_RELATION("PROVIDE VALID RELATION"),
	CHILD_ADDITION_FAILED("CHILD_ADDITION_FAILED"),
	CHILD_ADDITION_SUCCEEDED("CHILD_ADDITION_SUCCEEDED"),
	NONE("NONE"),
	INVALID_COMMAND("INVALID_COMMAND");
	private final String value;
}
