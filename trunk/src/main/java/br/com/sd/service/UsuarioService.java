package br.com.sd.service;

import br.com.sd.entity.Usuario;

public interface UsuarioService extends EntityService<Usuario> {
	public Usuario buscarPorLoginSenha(String login, String senha);
}
