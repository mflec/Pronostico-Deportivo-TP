package pronosticos_deportivos_tp_final.pronosticosDeportivosJava;

public class Pronostico {
	private Partido partido;
	private Equipo equipo;
	private ResultadoEnum resultado;

	// Constructor en caso de que haya un ganador en el Pronostico
	public Pronostico(Partido partido, Equipo equipo) {
		this.partido = partido;
		this.equipo = equipo;
		this.resultado = partido.resultado();
	}

	// Constructor en caso de empate en el pronostico
	public Pronostico(Partido partido) {
		this.partido = partido;
		this.resultado = partido.resultado();
	}

	public int puntos() {
		if (equipo == null) {
			if(resultado.empate) {				
				System.out.print("Acertó. Hubo un empate\n");
				return 1;
			}
			System.out.print("No acertó. No hubo un empate\n");
			return 0;
		} else if (resultado.ganador.equals(equipo)) {
			System.out.print("Acertó. El ganador es " + equipo.getNombre() + "\n");
			return 1;
		} else {
			System.out.print("No acertó. Pierde " + equipo.getNombre() + "\n");
			return 0;
		}
	}
}
