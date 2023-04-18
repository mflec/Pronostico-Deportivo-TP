package pronosticos_deportivos_tp_final.pronosticosDeportivosJava;

public class ResultadoEnum extends Partido {
	public Equipo ganador;
	public Equipo perdedor;
	public Boolean empate;

	ResultadoEnum(Equipo equipo1, Equipo equipo2, int golesEquipo1, int golesEquipo2) {
		super(equipo1,  golesEquipo1, equipo2, golesEquipo2);
		this.ganador= getGanador();
		this.perdedor=getPerdedor();
		this.empate=getEmpate();
	}

	public Equipo getGanador() {
		if (this.getGolesEquipo1() > this.getGolesEquipo2()) {
			return this.getEquipo1();
		} else if (this.getGolesEquipo1() < this.getGolesEquipo2()) {
			return this.getEquipo2();
		} else {
			this.empate = true;
			return null; // No hay ganador en caso de empate
		}
	}

	public Equipo getPerdedor() {
		if (this.getGolesEquipo1() < this.getGolesEquipo2()) {
			return this.getEquipo1();
		} else if (this.getGolesEquipo1() > this.getGolesEquipo2()) {
			return this.getEquipo2();
		} else {
			return null; // No hay perdedor en caso de empate
		}
	}

	public boolean getEmpate() {
		return this.getGolesEquipo1() == this.getGolesEquipo2();
	}
}
