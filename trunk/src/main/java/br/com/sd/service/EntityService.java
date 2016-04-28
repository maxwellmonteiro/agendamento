package br.com.sd.service;

import java.io.Serializable;
import java.util.List;

public interface EntityService<T extends Serializable> extends Serializable {
	T salvar(T entity);
	List<T> buscarTodos();
}
