package edu.lab.wsalab;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {

	public static void main(String[] args) {
		String query = ". Is it possible to find an analytical, similar solution of the strong blast wave problem in the\n"
				+ "Newtonian approximation .";
		System.out.println(Character.isDigit(query.charAt(0)));
		;
	}

	public static void test(String[] args) {
		String pattern = "(<DOC>(<DOCNO>.*</DOCNO>)(<TITLE>.*</TITLE>)(<AUTHOR>.*</AUTHOR>)(<BIBLIO>.*</BIBLIO>)(<TEXT>.*</TEXT>)</DOC>)";
		String input = "<DOC><DOCNO>1</DOCNO><TITLE>experimental investigation of the aerodynamics of awing in a slipstream .</TITLE><AUTHOR>brenckman,m.</AUTHOR><BIBLIO>j. ae. scs. 25, 1958, 324.</BIBLIO><TEXT>an experimental study of a wing in a propeller slipstream wasmade in order to determine the spanwise distribution of the liftincrease due to slipstream at different angles of attack of the wingand at different free stream to slipstream velocity ratios .  theresults were intended in part as an evaluation basis for differenttheoretical treatments of this problem .the comparative span loading curves, together with supportingevidence, showed that a substantial part of the lift incrementproduced by the slipstream was due to a /destalling/ or boundary-layer-controleffect .  the integrated remaining lift increment,after subtracting this destalling lift, was found to agreewell with a potential flow theory .an empirical evaluation of the destalling effects was made forthe specific configuration of the experiment .</TEXT></DOC>";
		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(input);
		while (m.find()) {
			int id = Integer.valueOf(getContent(m.group(2)));
			System.out.println(m.group(2));
			System.out.println(m.group(3));
			System.out.println(m.group(4));
			System.out.println(m.group(5));
			System.out.println(m.group(6));
			System.out.println(id);
		}
	}

	private static String getContent(String str) {
		Pattern p2 = Pattern.compile("(<.*>(.*)</.*>)");
		Matcher m2 = p2.matcher(str);
		String content = null;
		while (m2.find()) {
			content = m2.group(2);
		}
		return content.trim();
	}
}
