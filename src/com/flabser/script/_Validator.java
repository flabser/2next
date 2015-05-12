package com.flabser.script;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class _Validator {

	private static Matcher matcher;
	private static Pattern pattern;
	
	public static boolean checkRNN_kz(String value) {
		String validate = "^[0-9]{12,12}$";

		pattern = Pattern.compile(validate);
		matcher = pattern.matcher(value);
		
		return matcher.matches();
	}
	
	public static boolean checkCarNumber_kz(String value) {
		String validate = "^([A-Za-z]{1,1}[0-9]{3,3}([A-Za-z]{2,2}||[A-Za-z]{3,3}))$";

		pattern = Pattern.compile(validate);
		matcher = pattern.matcher(value);
		
		return matcher.matches();
	}
	
	public static boolean checkIIN_kz(String value) {
		String validate = "^[0-9]{12,12}$";

		pattern = Pattern.compile(validate);
		matcher = pattern.matcher(value);
		
		return matcher.matches();
	}
	
	public static boolean checkEmail(String value) {
		String validate = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
				+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

		pattern = Pattern.compile(validate);
		matcher = pattern.matcher(value);
		
		return matcher.matches();
	}
	
	public static boolean checkURL(String value){
//        String validate = "(http||https)://((((www\\.)||())[a-z0-9]{2,}\\.[a-z]{2,4})||(([0-9]{1,3}\\.){3,3}[0-9]{1,3}))" +
//                                                  "((:[0-9]{1,})||())((/.*)||())";
		String validate = "(http||https)://(((([a-z0-9]{2,}\\.)||())[a-z0-9]{2,}\\.[a-z]{2,4})||(([0-9]{1,3}\\.){3,3}[0-9]{1,3}))" +
              "((:[0-9]{1,})||())((/.*)||())";
        
        Pattern pattern = Pattern.compile(validate);
        Matcher matcher = pattern.matcher(value);
        
        return matcher.matches();
    }
	
	public static boolean checkPhoneNumber_kz(String value){
        String validate = "([0-9]{5,7})||((87)[0-9]{9,9})";

        Pattern pattern = Pattern.compile(validate); 
        Matcher matcher = pattern.matcher(value.replace("+7", "8").replace(" ", ""));
        
        return matcher.matches();
    }
	
	public static boolean checkDate(String value){
        String validate = "[0-9]{1,2}\\.[0-9]{1,2}\\.[0-9]{2,4}";

        Pattern pattern = Pattern.compile(validate); 
        Matcher matcher = pattern.matcher(value);
        
        return matcher.matches();
    }
	
}
