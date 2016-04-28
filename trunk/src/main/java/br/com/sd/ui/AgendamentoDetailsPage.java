package br.com.sd.ui;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.convert.ConversionException;
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;

import br.com.sd.entity.Agendamento;
import br.com.sd.service.AgendamentoService;
import br.com.sd.service.impl.AgendamentoServiceImpl;
import br.com.sd.util.NotificacaoAgendamento;
import br.com.sd.util.Util;

public class AgendamentoDetailsPage extends WebPage {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8777363748639852228L;
	
	private Form<Agendamento> form;
	private StyledFeedbackPanel feedbackPanel;
	
	private TextField<String> tfNome;
	private TextField<String> tfData;
	private DropDownChoice<String> ddcHorario;
	private TextField<String> tfTelefone;
	private TextField<String> tfEmail;
	private TextField<String> tfObservacao;
	
	private AbstractLink btnAgendar;
	private Button btnAtualizarHorario;
	
	private WebMarkupContainer divHorario;
	
	private Agendamento entity = new Agendamento();
	private AgendamentoService entityService = new AgendamentoServiceImpl();
	
	private List<String> horarios = new ArrayList<String>();
	private List<String> h18 = gerarHorarios("09:00:00", "18:00:00");
	private List<String> h17 = gerarHorarios("09:00:00", "17:00:00");
	
	final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	
	public Agendamento getEntity() {
		return entity;
	}
	
	public Form<Agendamento> getForm() {
		if (form == null) {
			form = new Form<Agendamento>("form", new PropertyModel<Agendamento>(this, "form"));
		}
		
		return form;
	}
	
	public FeedbackPanel getFeedbackPanel() {
		feedbackPanel = new StyledFeedbackPanel("feedback");
		
		return feedbackPanel;
	}
	
	public TextField<String> getTfNome() {
		tfNome = new TextField<String>("nome", new PropertyModel<String>(getEntity(), "nome"));
		tfNome.setRequired(true);
		
		return tfNome;
	}
	
	public TextField<String> getTfData() {
		tfData = new TextField<String>("data", new PropertyModel<String>(getEntity(), "data")) {
			private static final long serialVersionUID = 1L;

			/*Para resolver o problema de requisicao cliente com configuracao de data hora modelo americano, pois o default do wicket estava convertendo para MM/dd/yyyy*/
			
			@Override
			public <C> IConverter<C> getConverter(Class<C> type) {
				return new IConverter<C>() {
					private static final long serialVersionUID = 1L;
					
					@SuppressWarnings("unchecked")
					@Override
					public C convertToObject(String arg0, Locale arg1) throws ConversionException {
						try {
							return (C) sdf.parse(arg0);
						} catch (ParseException e) {
							e.printStackTrace();
						}
						return null;
					}

					@Override
					public String convertToString(C arg0, Locale arg1) {
						return sdf.format((Date) arg0);
					}					
				};
			}
		};		
		tfData.setRequired(true);				
		
		return tfData;
	}
	
	public List<String> gerarHorarios(String horaIni, String horaFim) {
		final SimpleDateFormat sdf0 = new SimpleDateFormat("HH:mm:ss");
		List<String> ret = new ArrayList<String>();
		Calendar fim = Calendar.getInstance();
		Calendar ini = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		
		try {
			ini.setTime(sdf0.parse(horaIni));
			fim.setTime(sdf0.parse(horaFim));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		while (ini.compareTo(fim) <= 0) {
			ret.add(sdf.format(ini.getTime()));
			ini.add(Calendar.MINUTE, 30);			
		}
		return ret;
	}
	
	public DropDownChoice<String> getDdcHorario() {
		ddcHorario = new DropDownChoice<String>("horario", new PropertyModel<String>(getEntity(), "horario"), new PropertyModel<List<String>>(this, "horarios"));
		ddcHorario.setRequired(true);
		
		return ddcHorario;
	}
	
	public WebMarkupContainer getDivHorario() {
		if (divHorario == null) {
			divHorario = new WebMarkupContainer("divHorario");
			divHorario.setOutputMarkupId(true);
		}
		
		return divHorario;
	}
	
	public TextField<String> getTfTelefone() {
		tfTelefone = new TextField<String>("telefone", new PropertyModel<String>(getEntity(), "telefone"));
		tfTelefone.setRequired(true);
		
		return tfTelefone;
	}
	
	public TextField<String> getTfEmail() {
		tfEmail = new TextField<String>("email", new PropertyModel<String>(getEntity(), "email"));
		tfEmail.add(new IValidator<String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public void validate(IValidatable<String> validatable) {
				final Util util = new Util();
				if (!util.validarEmail(validatable.getValue())) {
					ValidationError error = new ValidationError();					
					error.setMessage("E-Mail inválido");
					validatable.error(error);
				}				
			}
		});
		tfEmail.setRequired(true);
		
		return tfEmail;
	}
	
	public TextField<String> getTfObservacao() {
		tfObservacao = new TextField<String>("observacao", new PropertyModel<String>(getEntity(), "observacao"));
		
		return tfObservacao;
	}
	
	public AbstractLink getBtnAgendar() {
		btnAgendar = new SubmitLink("agendar") {
			private static final long serialVersionUID = 1L;			
			
			@Override
			public void onSubmit() {
				final SimpleDateFormat _sdf = new SimpleDateFormat("dd/MM/yyyy - HH:mm");
				try {					
					agendar();
					setResponsePage(new SuccessPage(
							String.format("Sua <b>Solicitação de Agendamento</b> para <b>%s</b> foi enviada.<p>Em breve enviaremos um <b>E-Mail confirmando</b> seu agendamento.</p>", _sdf.format(getEntity().getData()))));
					resetFields();					
				} catch (Exception e) {
					error("Ocorreu um erro ao realizar o agendamento. Por favor, entre em contato por Telefone ou WhatsApp");
					e.printStackTrace();
				}
			}
		};
		
		return btnAgendar;
	}	
	
	public Button getBtnAtualizarHorario() {
		btnAtualizarHorario = new AjaxButton("atualizarHorario") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				final Calendar cal = Calendar.getInstance();
				
				try {
					cal.setTime(sdf.parse(tfData.getInput()));
				} catch (ParseException e) {
					e.printStackTrace();
				}
				if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
					horarios.clear();
					horarios.addAll(h17);
				} else {
					horarios.clear();
					horarios.addAll(h18);
				}
				target.add(getDivHorario());
			}
		};
		btnAtualizarHorario.setDefaultFormProcessing(false);
		
		return btnAtualizarHorario;
	}
	
	private void resetFields() {
		entity.setNome(null);
		entity.setData(null);
		entity.setHorario(null);
		entity.setTelefone(null);
		entity.setEmail(null);
		entity.setObservacao(null);
	}

	
	private void agendar() {
		Calendar cal = Calendar.getInstance();
		
		cal.setTime(getEntity().getData());
		cal.set(Calendar.HOUR_OF_DAY, Long.valueOf(getEntity().getHorario().split(":")[0]).intValue());
		cal.set(Calendar.MINUTE, Long.valueOf(getEntity().getHorario().split(":")[1]).intValue());
		getEntity().setData(cal.getTime());
		
		entityService.salvar(getEntity());		
		
		NotificacaoAgendamento.notificar();
	}

	public AgendamentoDetailsPage(PageParameters pageParameters) {
		
		add(getForm());
		add(getFeedbackPanel());
		
		getForm().add(getTfNome());
		getForm().add(getTfData());
		getForm().add(getDivHorario().add(getDdcHorario()));
		getForm().add(getTfTelefone());
		getForm().add(getTfEmail());
		getForm().add(getTfObservacao());
		
		getForm().add(getBtnAgendar());
		getForm().add(getBtnAtualizarHorario());
	}
}
