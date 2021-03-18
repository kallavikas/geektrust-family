package com.geektrust.familytree.service.impl;


import static com.geektrust.familytree.enums.ErrorCodes.CHILD_ADDITION_FAILED;
import static com.geektrust.familytree.enums.ErrorCodes.CHILD_ADDITION_SUCCEEDED;
import static com.geektrust.familytree.enums.ErrorCodes.NONE;
import static com.geektrust.familytree.enums.ErrorCodes.NOT_YET_IMPLEMENTED;
import static com.geektrust.familytree.enums.ErrorCodes.PERSON_NOT_FOUND;
import static com.geektrust.familytree.enums.ErrorCodes.PROVIDE_VALID_RELATION;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.geektrust.familytree.dto.Member;
import com.geektrust.familytree.enums.ErrorCodes;
import com.geektrust.familytree.enums.Gender;
import com.geektrust.familytree.enums.RelationShip;
import com.geektrust.familytree.service.FamilyService;

public class FamilyServiceImpl implements FamilyService {

	private static final String FEMALE = "Female";

	private Member familyHead;

	/**
	 * Add head of the family.
	 * 
	 * @param name   - name of the member
	 * @param gender - gender
	 */
	@Override
	public void addFamilyHead(String name, String gender) {
		Gender g = (FEMALE.equals(gender)) ? Gender.Female : Gender.Male;
		this.familyHead = new Member(name, g, null, null);
	}

	/**
	 * Add spouse to a member iff {@link Member} is not null and do not have spouse
	 * already.
	 * 
	 * @param memberName - member whose spouse to be added
	 * @param spouseName
	 * @param gender
	 */
	public void addSpouse(String memberName, String spouseName, String gender) {
		Member member = searchMember(familyHead, memberName);
		if (member != null && member.getSpouse() == null) {
			Gender g = (FEMALE.equals(gender)) ? Gender.Female : Gender.Male;
			Member sp = new Member(spouseName, g, null, null);
			sp.addSpouse(member);
			member.addSpouse(sp);
		}
	}

	/**
	 * Add child to a member iff {@link Member} is not null and member is female.
	 * 
	 * @param motherName - member whose child to be added
	 * @param childName
	 * @param gender
	 * @return
	 */
	public ErrorCodes addchild(String motherName, String childName, String gender) {
		ErrorCodes result;
		Member member = searchMember(familyHead, motherName);
		if (member == null) {
			result = PERSON_NOT_FOUND;
		} else if (childName == null || gender == null) {
			result = CHILD_ADDITION_FAILED;
		} else if (member.getGender() == Gender.Female) {
			Gender g = (FEMALE.equals(gender)) ? Gender.Female : Gender.Male;
			Member child = new Member(childName, g, member.getSpouse(), member);
			member.addChild(child);
			result = CHILD_ADDITION_SUCCEEDED;
		} else {
			result = CHILD_ADDITION_FAILED;
		}
		return result;
	}

	/**
	 * Search a {@link Member} with name as person. Find member's name corresponding
	 * to given relationship
	 * 
	 * @param person
	 * @param relationship
	 * @return
	 */
	public String getRelationship(String person, String relationship) {

		String relations;
		Member member = searchMember(familyHead, person);
		if (member == null) {
			relations = PERSON_NOT_FOUND.getValue();
		} else if (relationship == null) {
			relations = PROVIDE_VALID_RELATION.getValue();
		} else {
			relations = getRelationship(member, relationship);
		}

		return relations;

	}

	/**
	 * Find members name corresponding to given relationship to given {@link Member}
	 * 
	 * @param member       : whose relatives to be found
	 * @param relationship : relation to be foind
	 * @return list of names of relatives
	 */
	private String getRelationship(Member member, String relationship) {
		String relations = "" ;
		switch (RelationShip.getEnum(relationship)) {
		case DAUGHTER:
			relations = member.searchChild(Gender.Female);
			break;

		case SON:
			relations = member.searchChild(Gender.Male);
			break;

		case SIBLINGS:
			relations = member.searchSiblings();
			break;

		case SISTER_IN_LAW:
			relations = searchInLaws(member, Gender.Female);
			break;

		case BROTHER_IN_LAW:
			relations = searchInLaws(member, Gender.Male);
			break;

		case MATERNAL_AUNT:
			if (member.getMother() != null)
				relations = member.getMother().searchAuntOrUncle(Gender.Female);
			break;

		case PATERNAL_AUNT:
			if (member.getFather() != null)
				relations = member.getFather().searchAuntOrUncle(Gender.Female);
			break;

		case MATERNAL_UNCLE:
			if (member.getMother() != null)
				relations = member.getMother().searchAuntOrUncle(Gender.Male);
			break;

		case PATERNAL_UNCLE:
			if (member.getFather() != null)
				relations = member.getFather().searchAuntOrUncle(Gender.Male);
			break;

		default:
			relations = NOT_YET_IMPLEMENTED.getValue();
			break;
		}

		return ("".equals(relations)) ? NONE.getValue() : relations;

	}

	/**
	 * Search {@link Member} sister-in law or brother -in law.
	 * 
	 * @param member
	 * @param gender
	 * @return
	 */
	private String searchInLaws(Member member, Gender gender) {
		String personName = member.getName();
		StringBuilder sb = new StringBuilder("");
		String res = "";

		// search spouse mother children
		if (member.getSpouse() != null && member.getSpouse().getMother() != null) {
			res = member.getSpouse().getMother().getChildren()
					.stream().filter(child->!child.getName().equals(member.getSpouse().getName())&& child.getGender().equals(gender)).map(Member::getName).collect(Collectors.joining(" "));
		}
		sb.append(res);

		// search mother children 
		res = "";
		if (member.getMother() != null) {
			res = member.getMother().getChildren()
					.stream().filter(child->!child.getName().equals(personName)&&child.getSpouse()!=null&& child.getSpouse().getGender().equals(gender)).map(child->child.getSpouse().getName()).collect(Collectors.joining(" "));
		}
		sb.append(res);

		return sb.toString().trim();
	}

	
	/**
	 * Search {@link Member} object with name as memberName. Returns null in case
	 * not found.
	 * 
	 * @param head
	 * @param memberName
	 * @return {@link Member} object
	 */
	private Member searchMember(Member head, String memberName) {
		if (memberName == null || head == null) {
			return null;
		}

		Member member = null;
		if (memberName.equals(head.getName())) {
			return head;
		} else if (head.getSpouse() != null && memberName.equals(head.getSpouse().getName())) {
			return head.getSpouse();
		}

		List<Member> childlist = new ArrayList<>();
		if (head.getGender() == Gender.Female) {
			childlist = head.getChildren();
		} else if (head.getSpouse() != null) {
			childlist = head.getSpouse().getChildren();
		}

		for (Member m : childlist) {
			member = searchMember(m, memberName);
			if (member != null) {
				break;
			}
		}
		return member;
	}

}