package com.geektrust.familytree.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum RelationShip {
	PATERNAL_UNCLE("Paternal-Uncle"),
	MATERNAL_UNCLE("Maternal-Uncle"),
	PATERNAL_AUNT("Paternal-Aunt"),
	MATERNAL_AUNT("Maternal-Aunt"),
	SISTER_IN_LAW("Sister-In-Law"),
	BROTHER_IN_LAW("Brother-In-Law"),
	SON("Son"),
	DAUGHTER("Daughter"),
	SIBLINGS("Siblings");
	private String value;
	public static RelationShip getEnum(String input)
	{
		List<RelationShip> collect = Arrays.stream(RelationShip.values()).filter(x->x.value.equals(input)).collect(Collectors.toList());
		return collect.isEmpty()?null:collect.get(0);
		
	}
}
