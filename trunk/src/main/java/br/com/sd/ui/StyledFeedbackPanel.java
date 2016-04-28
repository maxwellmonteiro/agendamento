package br.com.sd.ui;

import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.markup.html.panel.FeedbackPanel;

public class StyledFeedbackPanel extends FeedbackPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1689453359466054457L;

	public StyledFeedbackPanel(String id) {
		super(id);
		// TODO Auto-generated constructor stub
	}
	
	
	@Override
	protected String getCSSClass(FeedbackMessage message) {
		if (message.getLevel() == FeedbackMessage.ERROR) {
			return "alert alert-danger";

		} else if (message.getLevel() == FeedbackMessage.INFO) {
			return "alert alert-info";

		} else if (message.getLevel() == FeedbackMessage.SUCCESS) {
            return "alert alert-success";

        } else if (message.getLevel() == FeedbackMessage.WARNING) {
            return "alert alert-warning";

        } else {
			return "";
		}
	}
}
