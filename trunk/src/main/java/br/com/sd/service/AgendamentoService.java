package br.com.sd.service;

import java.util.Date;
import java.util.List;

import br.com.sd.entity.Agendamento;

public interface AgendamentoService extends EntityService<Agendamento>{
	public List<Agendamento> buscarPorData(Date ini, Date fim);
	public List<Agendamento> buscarPorData(Date ini);
}
