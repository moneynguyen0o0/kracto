package com.kracto.data;

import java.util.ArrayList;
import java.util.List;

public class Gender {

	public static List<String> getGenders() {
		List<String> genders = new ArrayList<String>();
		genders.add("Female");
		genders.add("Male");
		return genders;
	}

	public static boolean exist(String gender) {
		for (String str : getGenders())
			if (str.equals(gender))
				return true;
		return false;
	}

}