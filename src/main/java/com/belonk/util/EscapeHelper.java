package com.belonk.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>Created by sun on 2016/5/13.
 *
 * @author sunfuchang03@126.comfuchang03@126.com
 * @since 1.0
 */
public class EscapeHelper {
	//~ Static fields/initializers =====================================================================================
	private static final Logger LOG = LoggerFactory.getLogger(EscapeHelper.class);

	//~ Instance fields ================================================================================================

	//~ Methods ========================================================================================================

	/**
	 * 实现javascript的escape函数功能。
	 *
	 * @param src 源字符串
	 * @return
	 */
	public static String escape(String src) {
		if (src == null || "".equals(src))
			return "";
		int i;
		char j;
		StringBuffer tmp = new StringBuffer();
		tmp.ensureCapacity(src.length() * 6);
		for (i = 0; i < src.length(); i++) {
			j = src.charAt(i);
			if (Character.isDigit(j) || Character.isLowerCase(j)
					|| Character.isUpperCase(j))
				tmp.append(j);
			else if (j < 256) {
				tmp.append("%");
				if (j < 16)
					tmp.append("0");
				tmp.append(Integer.toString(j, 16));
			} else {
				tmp.append("%u");
				tmp.append(Integer.toString(j, 16));
			}
		}
		return tmp.toString();
	}

	/**
	 * 实现javascript的unescape的功能。
	 *
	 * @param src 源字符串
	 * @return
	 */
	public static String unescape(String src) {
		if (src == null || "".equals(src))
			return "";
		StringBuffer tmp = new StringBuffer();
		tmp.ensureCapacity(src.length());
		int lastPos = 0, pos = 0;
		char ch;
		while (lastPos < src.length()) {
			pos = src.indexOf("%", lastPos);
			if (pos == lastPos) {
				if (src.charAt(pos + 1) == 'u') {
					ch = (char) Integer.parseInt(src.substring(pos + 2, pos + 6), 16);
					tmp.append(ch);
					lastPos = pos + 6;
				} else {
					ch = (char) Integer.parseInt(src
							.substring(pos + 1, pos + 3), 16);
					tmp.append(ch);
					lastPos = pos + 3;
				}
			} else {
				if (pos == -1) {
					tmp.append(src.substring(lastPos));
					lastPos = src.length();
				} else {
					tmp.append(src.substring(lastPos, pos));
					lastPos = pos;
				}
			}
		}
		return tmp.toString();
	}
}
