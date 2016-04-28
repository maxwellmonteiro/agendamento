package br.com.sd.ui;

import javax.servlet.http.HttpSession;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.protocol.http.servlet.ServletWebRequest;
import org.apache.wicket.request.cycle.RequestCycle;

import br.com.sd.entity.Usuario;
import br.com.sd.service.UsuarioService;
import br.com.sd.service.impl.UsuarioServiceImpl;

public class LoginPage extends WebPage {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2751980674647174402L;

	private StyledFeedbackPanel feedbackPanel;
	private Form<Usuario> form;
	private TextField<String> tfNome;
	private PasswordTextField tfSenha;
	private Button btnAutenticar;
	
	private Usuario entity = new Usuario();
	private UsuarioService usuarioService = new UsuarioServiceImpl();
	
	public Form<Usuario> getForm() {
		if (form == null) {
			form = new Form<>("form", new PropertyModel<Usuario>(this, "entity"));
		}
		
		return form;
	}
	
	public StyledFeedbackPanel getFeedbackPanel() {
		feedbackPanel = new StyledFeedbackPanel("feedback");
		
		return feedbackPanel;
	}
	
	public TextField<String> getTfNome() {
		tfNome = new TextField<String>("login", new PropertyModel<String>(entity, "login"));
		tfNome.setRequired(true);
		
		return tfNome;
	}
		
	public PasswordTextField getTfSenha() {
		tfSenha = new PasswordTextField("senha", new PropertyModel<String>(entity, "senha"));
		tfSenha.setRequired(true);
		
		return tfSenha;
	}
	
	public Button getBtnAutenticar() {
		btnAutenticar = new Button("autenticar") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onSubmit() {
				if (autenticar()) {
					setResponsePage(new AgendamentoListPage());
				} else {
					error("Usuário ou senha inválidos.");
				}
			}
		};
		
		return btnAutenticar;
	}
	
	public boolean autenticar() {
		Usuario usuario;
		
		usuario = usuarioService.buscarPorLoginSenha(entity.getLogin(), entity.getSenha());
		
		if (usuario != null) {
			HttpSession session = ((ServletWebRequest) RequestCycle.get().getRequest()).getContainerRequest().getSession();
			session.setAttribute("usuario", usuario);
			return true;
		} 
		return false;
	}
	
	public LoginPage() {
		add(getForm());
		add(getFeedbackPanel());
		getForm().add(getTfNome());
		getForm().add(getTfSenha());
		getForm().add(getBtnAutenticar());
	}
}
