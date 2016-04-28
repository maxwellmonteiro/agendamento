package br.com.sd.ui;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;

public class SuccessPage extends WebPage {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8013954224797125934L;

	private WebMarkupContainer wmcMsg;
	
	protected String msg;
	
	public WebMarkupContainer getWmcMsg() {
		wmcMsg = new WebMarkupContainer("msg") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onComponentTagBody(MarkupStream markupStream, ComponentTag openTag) {
				replaceComponentTagBody(markupStream, openTag, msg);
			}
		};
		
		return wmcMsg;
	}
	
	public SuccessPage(String msg) {
		this.msg = msg;
		
		add(getWmcMsg());
	}
}
