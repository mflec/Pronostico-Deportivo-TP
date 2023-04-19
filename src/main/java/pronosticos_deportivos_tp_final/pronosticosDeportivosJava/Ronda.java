package pronosticos_deportivos_tp_final.pronosticosDeportivosJava;

import java.util.List;
import java.util.Objects;
import java.util.ArrayList;

public class Ronda {
	private String nro;
	public List<Partido> partidos;

	public Ronda(String nro) {
		this.nro = nro;
		this.partidos = new ArrayList<Partido>();
	}
	
	// Getters
	public String getNumber() {
		return this.nro;
	}

	public Partido getPartido(Equipo equipo1, Equipo equipo2) {
		for (Partido partido : partidos) {
			if (partido.getEquipo1().equals(equipo1) && partido.getEquipo2().equals(equipo2) ) {
				return partido;
			}
		}
		return null;
	}

	public List<Partido> getPartidos() {
		for (Partido partido : partidos) {
			System.out.print("Partido ");
			System.out.print(partido.getEquipo1().getNombre() + " vs ");
			System.out.println(partido.getEquipo2().getNombre());
		}
		return this.partidos;
	}
	
	// Setters

	public void setPartido(Partido partido) {
		this.partidos.add(partido);
	}
	
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ronda)) return false;
        Ronda ronda = (Ronda) o;
        return Objects.equals(nro, ronda.nro);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nro);
    }
	
}
