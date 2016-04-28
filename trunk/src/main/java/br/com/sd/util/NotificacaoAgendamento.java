package br.com.sd.util;

import java.io.Serializable;

import javax.servlet.ServletContext;

import org.apache.wicket.protocol.http.WebApplication;

public class NotificacaoAgendamento implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4924692043481181392L;

	
	private static final String NOTIFICATION_COUNT = "notificacaoCount";
	
	public static void notificar() {
		ServletContext context = WebApplication.get().getServletContext();
		
		if (context != null) {
			Long count = (Long) context.getAttribute(NOTIFICATION_COUNT);
			if (count != null) {
				count = new Long(count.longValue() + 1);				
			} else {
				count = new Long(1);
			}			
			context.setAttribute(NOTIFICATION_COUNT, count);
		}
	}
	
	
	public static long consumir() {
		long ret = 0;
		ServletContext context = WebApplication.get().getServletContext();
		
		if (context != null) {
			Long count = (Long) context.getAttribute(NOTIFICATION_COUNT);
			if (count != null) {
				ret = count.longValue();
				count = new Long(0);
				context.setAttribute(NOTIFICATION_COUNT, count);
			}						
		}
		return ret;
	}
}
