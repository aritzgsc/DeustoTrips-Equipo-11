package domain;

import java.time.LocalDateTime;
import java.util.Objects;

public class Mensaje {

	private int id;
    private LocalDateTime fechaHora;
    private String texto;
    private Cliente emisor;
    private Cliente receptor;
    private int idApartamento;
    private boolean leido;

    public Mensaje(int id, LocalDateTime fechaHora, String texto, Cliente emisor, Cliente receptor, int idApartamento, boolean leido) {
        this.id = id;
        this.fechaHora = fechaHora;
        this.texto = texto;
        this.emisor = emisor;
        this.receptor = receptor;
        this.idApartamento = idApartamento;
        this.leido = leido;
    }

	public LocalDateTime getFechaHora() {
		return fechaHora;
	}

	public void setFechaHora(LocalDateTime fechaHora) {
		this.fechaHora = fechaHora;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public Cliente getEmisor() {
		return emisor;
	}

	public void setEmisor(Cliente emisor) {
		this.emisor = emisor;
	}

	public Cliente getReceptor() {
		return receptor;
	}

	public void setReceptor(Cliente receptor) {
		this.receptor = receptor;
	}

	public int getIdApartamento() {
		return idApartamento;
	}

	public void setIdApartamento(int idApartamento) {
		this.idApartamento = idApartamento;
	}

	public boolean isLeido() {
		return leido;
	}

	public void setLeido(boolean leido) {
		this.leido = leido;
	}

	public int getId() {
		return id;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Mensaje other = (Mensaje) obj;
		return id == other.id;
	}
	
}
