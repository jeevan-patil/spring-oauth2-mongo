package xyz.jeevan.springoauth.util;

import java.util.Collection;

/**
 * 
 * @author jeevan
 * @date 03-Sep-2016 11:34:50 pm
 * @purpose
 *
 */
public final class CollectionUtil {

	private CollectionUtil() {
	}

	public static boolean isEmpty(Collection<?> coll) {
		return (coll == null || coll.size() == 0);
	}
}
