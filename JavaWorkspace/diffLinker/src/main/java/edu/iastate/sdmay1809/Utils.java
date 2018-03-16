package edu.iastate.sdmay1809;

public class Utils {

	@SafeVarargs
	public static <T> T coalesce(T... params) {
		if(params == null) {
			return null;
		}
		for (T param : params) {
			if(param != null) {
				return param;
			}
		}
		return null;
	}
	
}
