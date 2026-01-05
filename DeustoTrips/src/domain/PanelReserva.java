package domain;

import java.time.LocalDate;

import javax.swing.JPanel;

public abstract class PanelReserva extends JPanel implements Comparable<PanelReserva> {

	private static final long serialVersionUID = 1L;

	private LocalDate fechaInicioReserva;

	public LocalDate getFechaInicioReserva() {
		return fechaInicioReserva;
	}

	public void setFechaInicioReserva(LocalDate fechaInicioReserva) {
		this.fechaInicioReserva = fechaInicioReserva;
	}

	@Override
	public int compareTo(PanelReserva o) {
		return o.fechaInicioReserva.compareTo(fechaInicioReserva);
	}
	
}
