package br.com.sd.service.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;

import br.com.sd.entity.Agendamento;
import br.com.sd.service.AgendamentoService;

public class AgendamentoServiceImpl extends EntityServiceImpl<Agendamento>implements AgendamentoService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8780598540072920173L;

	@SuppressWarnings("unchecked")
	@Override
	public List<Agendamento> buscarPorData(Date ini, Date fim) {
		Query query = openSession().createQuery("select a from Agendamento a where a.data between :ini and :fim order by a.data");
		query.setTimestamp("ini", ini);
		query.setTimestamp("fim", fim);
		
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Agendamento> buscarPorData(Date ini) {
		Query query = openSession().createQuery("select a from Agendamento a where a.data >= :ini order by a.data");
		query.setTimestamp("ini", ini);
		
		return query.list();
	}
}
