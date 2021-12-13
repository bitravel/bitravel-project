package com.bitravel.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TagUtil {
	
	public static String getText(String content) {

		Pattern SCRIPTS = Pattern.compile("<script([^'\"]|\"[^\"]*\"|'[^']*')*?</script>",Pattern.DOTALL);

		Pattern STYLE = Pattern.compile("<style[^>]*>.*</style>",Pattern.DOTALL);

		Pattern TAGS = Pattern.compile("<(/)?([a-zA-Z0-9]*)(\\s[a-zA-Z0-9]*=[^>]*)?(\\s)*(/)?>");

		Pattern ENTITY_REFS = Pattern.compile("&[^;]+;");

		Pattern WHITESPACE = Pattern.compile("\\s\\s+");
		
		Matcher m;
		m = SCRIPTS.matcher(content);
		content = m.replaceAll("");
		m = STYLE.matcher(content);
		content = m.replaceAll("");
		m = TAGS.matcher(content);
		content = m.replaceAll("");
		m = ENTITY_REFS.matcher(content);
		content = m.replaceAll("");
		m = WHITESPACE.matcher(content);
		content = m.replaceAll(" "); 		
		return content;

	}
}
