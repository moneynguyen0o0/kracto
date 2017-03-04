package com.kracto.util;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.json.JSONArray;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.kracto.web.constant.Keyword;

public final class Utils {

	public static String toJSON(String string) {
		JSONArray jsonArray = new JSONArray();
		jsonArray.put(string);
		return jsonArray.toString();
	}

	public static String toJSON(List<String> strings) {
		JSONArray jsonArray = new JSONArray();
		for (String string : strings)
			jsonArray.put(string);
		return jsonArray.toString();
	}

	public static List<String> parseJson(String str) {
		if (str != null) {
			List<String> list = new ArrayList<String>();
			JSONArray jsonArray = new JSONArray(str);
			for (int i = 0; i < jsonArray.length(); i++)
				list.add(jsonArray.get(i).toString());
			return list;
		}
		return null;
	}

	public static boolean existValueInList(String value, List<String> list) {
		if (list != null && value != null)
			return list.stream().anyMatch(t -> t.equals(value));
		return false;
	}

	public static String getLocale() {
		return LocaleContextHolder.getLocale().getLanguage();
	}

	public static boolean isVi() {
		return getLocale().equals(Keyword.VI);
	}

	public static String randomToken() {
		return UUID.randomUUID().toString();
	}

	public static String getRequestRemoteAddr() {
		return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest()
				.getRemoteAddr();
	}

}
