package br.com.sd.util;

import java.io.Serializable;
import java.util.regex.Pattern;

public class Util implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8804607619733229550L;
	
	public boolean validarEmail(String email) {
		final String EMAIL_PATTERN = 
				"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
				+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

		final Pattern pattern = Pattern.compile(EMAIL_PATTERN);
		
		return pattern.matcher(email).matches();
	}

}
