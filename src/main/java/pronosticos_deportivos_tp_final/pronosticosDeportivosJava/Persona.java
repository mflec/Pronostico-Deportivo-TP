package pronosticos_deportivos_tp_final.pronosticosDeportivosJava;

import java.util.ArrayList;
import java.util.List;

public class Persona {
	public String nombre;
	public int puntosTotales;
	public List<Pronostico> pronosticos;

	
	public Persona(String nombre) {
		this.nombre = nombre;
		this.pronosticos = new ArrayList<Pronostico>();

	};
	
	public String getNombre() {
		return this.nombre;
	}
	
	public void getPronosticos(int puntos, int puntosExtra) {
		System.out.println("Pronosticos de "+ nombre);
		for (Pronostico pronostico : pronosticos) {
			this.puntosTotales= this.puntosTotales + pronostico.puntos(puntos);
		}
		
		int totalPronostico = pronosticos.size();
		boolean totalAciertos = this.puntosTotales == totalPronostico;

		if(totalAciertos) {
			System.out.println("Acertó todos los pronosticos. Gana " + puntosExtra + " puntos.");
			this.puntosTotales= this.puntosTotales + puntosExtra;
		}
		
		System.out.println("Su puntuación total es " + this.puntosTotales);
		System.out.println();
	};
	
	public int getPuntosTotales() {
		System.out.println("Puntos totales de " + this.nombre + " : " + this.puntosTotales);
		return this.puntosTotales;
	}
	
	public void setPronostico(Pronostico pronostico) {
		this.pronosticos.add(pronostico);
	}
}
