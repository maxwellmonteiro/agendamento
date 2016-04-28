package br.com.sd.service.impl;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;

import br.com.sd.entity.Usuario;
import br.com.sd.service.UsuarioService;

public class UsuarioServiceImpl extends EntityServiceImpl<Usuario>implements UsuarioService {	

	/**
	 * 
	 */
	private static final long serialVersionUID = -4054570883619118299L;

	@SuppressWarnings("unchecked")
	@Override
	public Usuario buscarPorLoginSenha(String login, String senha) {
		Usuario usuario = null;
		List<Integer> ret;
		try {			
			Query query = openSession().createSQLQuery("select u.num_id from USUARIO u where u.txt_login = :login and u.txt_senha = PASSWORD(:senha)");
			query.setString("login", login);
			query.setString("senha", senha);
			ret = query.list();
			if (ret.size() > 0) {				
				usuario = openSession().get(Usuario.class, ret.get(0).longValue());
			}
		} catch (HibernateException e) {
			throw e;
		}
		finally {
			closeSession();
		}
		return usuario;
	}

}
