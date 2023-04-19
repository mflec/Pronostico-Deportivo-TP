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
		this.equipo = null;
	}

	public int puntos() {
		System.out.print(partido.getEquipo1().getNombre() + " vs " + partido.getEquipo2().getNombre() + ": ");
		if (this.resultado.empate) {
			if (this.equipo == null) {
				System.out.println("Acertó. Hubo un empate");
				return 1;
			} else {
				System.out.println("No acertó. Hubo un empate." + " No ganó " + equipo.getNombre());
				return 0;
			}
		} else if (this.equipo == null && !this.resultado.empate) {
			System.out.println("No acertó. No hubo un empate");
			return 0;
		} else if (resultado.ganador.equals(equipo)) {
			System.out.println("Acertó. El ganador es " + equipo.getNombre());
			return 1;
		} else {
			System.out.println("No acertó. El perdedor es " + equipo.getNombre());
			return 0;
		}
	}
}
