package br.com.sd.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;


@Entity
@Table(name = "AGENDAMENTO")
public class Agendamento implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1870061898139945301L;
	
	@Id 
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "NUM_ID")
	private Long id;
	
	@Column(name = "TXT_NOME", nullable = false, length = 100)
	private String nome;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DAT_DATA", nullable = false)
	private Date data;
	
	@Transient
	private String horario;
	
	@Column(name = "TXT_TELEFONE", length = 50, nullable = false)
	private String telefone;
	
	@Column(name = "TXT_EMAIL", length = 50)
	private String email;
	
	@Column(name = "TXT_OBSERVACAO", length = 100)
	private String observacao;
	
	@Type(type = "org.hibernate.type.YesNoType")
	@Column(name = "IND_CONFIRMADO")
	private Boolean confirmado = false;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public String getHorario() {
		return horario;
	}

	public void setHorario(String horario) {
		this.horario = horario;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public Boolean getConfirmado() {
		return confirmado;
	}

	public void setConfirmado(Boolean confirmado) {
		this.confirmado = confirmado;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Agendamento other = (Agendamento) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}
