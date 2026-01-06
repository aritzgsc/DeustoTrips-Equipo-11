package domain;

import java.util.Objects;

public class ReservaAp {

	Apartamento apartamento;
	Cliente cliente;
	
	public ReservaAp(Apartamento apartamento, Cliente cliente) {
		
		this.apartamento = apartamento;
		this.cliente = cliente;
		
	}

	public Apartamento getApartamento() {
		return apartamento;
	}

	public void setApartamento(Apartamento apartamento) {
		this.apartamento = apartamento;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	@Override
	public int hashCode() {
		return Objects.hash(apartamento, cliente);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ReservaAp other = (ReservaAp) obj;
		return Objects.equals(apartamento, other.apartamento) && Objects.equals(cliente, other.cliente);
	}
	
}
