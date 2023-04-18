package pronosticos_deportivos_tp_final.pronosticosDeportivosJava;

import java.util.List;
import java.util.ArrayList;

public class Ronda {
	private String nro;
	public List<Partido> partidos;

	public Ronda(String nro) {
		this.nro = nro;
		this.partidos = new ArrayList<Partido>();
	}

	public void setPartido(Partido partido) {
		this.partidos.add(partido);
	}
	
	public List<Partido> getPartidos() {
	    for (Partido partido : partidos) {
	        System.out.println("Partido ");
	        System.out.println(partido.getEquipo1().getNombre());
	        System.out.println(partido.getEquipo2().getNombre());
	    }
	    return this.partidos;
	}


	public Partido getPartido(Equipo equipo1, Equipo equipo2) {
		for (Partido partido : partidos) {
			if (partido.getEquipo1().equals(equipo1) && partido.getEquipo2().equals(equipo2) ) {
				System.out.print(equipo1.getNombre() + " vs " + equipo2.getNombre() + ": ");
				return partido;
			}
		}
		return null;
	}

	public int puntos() {
		int totalPuntos = 0;
		return totalPuntos;
	}
}
