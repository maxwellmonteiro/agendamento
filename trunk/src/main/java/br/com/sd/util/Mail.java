package br.com.sd.util;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

public class Mail {
	
	private static String HOST = "mail.agendapramim.net.br"; 
	private static String USER = "mail@agendapramim.net.br";
	private static String PASSWORD = "hy1@47ak2b";
	
	public static final String SUBJECT = "Confirmação de Agendamento - Sobrancelhas Design";
	
	public static final String BODY = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html;charset=UTF-8\"></head>" +
			"<body><font size=\"5\">Seu agendamento para <b>%s</b> na <b>Sobrancelhas Design - Taguatinga</b> foi confirmado. A tolerância é de 5 minutos. Estaremos lhe esperando :*</font>" +
			"<br><p>Endereço: QS 03, Ed. Pátio Capital Lj. 25 térreo. Próximo ao Taguatinga Shopping, ao lado da Feibox</p><p>Telefone: 3042-4704 WhatsApp: 8273-1394.</p>" +
			"<hr><h5>Este é um e-mail automático, por favor, não responda.</h5></body></html>";
	
	public static void enviarHtml(String subject, String htmlMsg, String destino) throws EmailException {
		HtmlEmail mail = new HtmlEmail();
		mail.setHostName(HOST);
		mail.setAuthenticator(new DefaultAuthenticator(USER, PASSWORD));
		mail.setFrom(USER, "Sobrancelhas Design - Taguatinga");
		mail.setSubject(subject);
		mail.setHtmlMsg(htmlMsg);
		mail.addTo(destino);
		mail.send();
	}
}
