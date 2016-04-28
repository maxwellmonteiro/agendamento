package br.com.sd.ui;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.mail.EmailException;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import br.com.sd.entity.Agendamento;
import br.com.sd.service.AgendamentoService;
import br.com.sd.service.impl.AgendamentoServiceImpl;
import br.com.sd.util.Mail;
import br.com.sd.util.NotificacaoAgendamento;

public class AgendamentoListPage extends WebPage {

	/**
	 * 
	 */
	private static final long serialVersionUID = -412470257738119137L;

	private Form<Agendamento> form;
	private StyledFeedbackPanel feedbackPanel;
	private TextField<String> tfData;
	private ListView<Agendamento> lvAgendamento;
	private AbstractLink btnBuscar;
	
	private TextField<String> tfNotificar;
	
	protected List<Agendamento> entities;	
	private AgendamentoService entityService = new AgendamentoServiceImpl();
	
	protected String notificar;
	
	protected String data;
	
	public Form<Agendamento> getForm() {
		if (form == null) {
			form = new Form<Agendamento>("form");
		}
		
		return form;
	}
	
	public FeedbackPanel getFeedbackPanel() {
		feedbackPanel = new StyledFeedbackPanel("feedback");
		
		return feedbackPanel;
	}
	
	public TextField<String> getTfData() {
		tfData = new TextField<>("data", new PropertyModel<String>(this, "data"));
		
		return tfData;
	}
	
	public ListView<Agendamento> getLvAgendamento() {
		lvAgendamento = new ListView<Agendamento>("grid", new PropertyModel<List<Agendamento>>(this, "entities")) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(final ListItem<Agendamento> item) {
				item.add(new Label("nome", new PropertyModel<>(item.getModelObject(), "nome")));
				item.add(new Label("data", new PropertyModel<String>(item.getModelObject(), "data")));
				item.add(new Label("telefone", new PropertyModel<>(item.getModelObject(), "telefone")));
				item.add(new Label("email", new PropertyModel<>(item.getModelObject(), "email")));
				item.add(new Label("observacao", new PropertyModel<>(item.getModelObject(), "observacao")));
				item.add(new Label("lbConfirmado", Model.of("Confirmado")) {
					private static final long serialVersionUID = 1L;

					@Override
					public boolean isVisible() {
						return item.getModelObject().getConfirmado().booleanValue();
					}
				});
				item.add(new SubmitLink("btnConfirmar") {
					private static final long serialVersionUID = 1L;

					@Override
					public void onSubmit() {
						confirmar(item.getModelObject());
					}		
					
					@Override
					public boolean isVisible() {
						return !item.getModelObject().getConfirmado().booleanValue();
					}
				});				
			}
		};
		
		return lvAgendamento;
	}
	
	private void processarNotificacoes() {
		long count = NotificacaoAgendamento.consumir();
		
		if (count > 0) {
			if (count > 1) {
				notificar = String.format("Existem %d novos agendamentos feitos!", count);
			} else {
				notificar = String.format("Existe %d novo agendamento feito!", count);
			}
		} else {
			notificar = "";
		}
	}
	
	public AbstractLink getBtnBuscar() {
		btnBuscar = new SubmitLink("buscar") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onSubmit() {
				entities = find();
				processarNotificacoes();
			}
		};
		
		return btnBuscar;
	}
	
	public TextField<String> getTfNotificar() {
		tfNotificar = new TextField<String>("notificar", new PropertyModel<String>(this, "notificar"));
		
		return tfNotificar;
	}
	
	public AgendamentoListPage() {
		entities = find();
		processarNotificacoes();
		
		add(getFeedbackPanel());
		add(getForm());
		
		getForm().add(getTfData());
		getForm().add(getLvAgendamento());		
		getForm().add(getTfNotificar());
		getForm().add(getBtnBuscar());
	}
	
	private void confirmar(Agendamento entity) {
		if (entity.getData().before(Calendar.getInstance().getTime())) {
			error("Horário agendado já passou!");
			return;
		}
		
		SimpleDateFormat sdf1 = new SimpleDateFormat("EEEE");
		SimpleDateFormat sdf2 = new SimpleDateFormat("dd");
		SimpleDateFormat sdf3 = new SimpleDateFormat("MMMM");
		SimpleDateFormat sdf4 = new SimpleDateFormat("HH:mm");
		
		String hora = String.format("%s, %s de %s às %s", sdf1.format(entity.getData()), sdf2.format(entity.getData()), sdf3.format(entity.getData()), sdf4.format(entity.getData()));
		
		
		try {
			if (entity.getEmail() != null && !entity.getEmail().isEmpty()) {
				Mail.enviarHtml(Mail.SUBJECT, String.format(Mail.BODY, hora), entity.getEmail());
			}
		} catch (EmailException e) {
			e.printStackTrace();
			warn("Agendamento foi confirmado, mas houve um erro ao enviar o e-mail. Entre em contato com o cliente.");
		}
		
		entity.setConfirmado(new Boolean(true));
		entityService.salvar(entity);
		
	}
	
	private List<Agendamento> find() {
		List<Agendamento> ags;
		final SimpleDateFormat sdf =  new SimpleDateFormat("dd/MM/yyyy");
		Date data = null;
		
		if (this.data != null) {
			try {			
				data = sdf.parse(this.data);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			Calendar ini = Calendar.getInstance();
			
			ini.setTime(data);
			ini.set(Calendar.HOUR_OF_DAY, 0);
			ini.set(Calendar.MINUTE, 0);		
			
			ags = entityService.buscarPorData(ini.getTime());
		} else {
			Calendar ini = Calendar.getInstance();
			
			ini.set(Calendar.HOUR_OF_DAY, 0);
			ini.set(Calendar.MINUTE, 0);
			
			ags = entityService.buscarPorData(ini.getTime());
		}
		
		return ags;
	}
	
}
