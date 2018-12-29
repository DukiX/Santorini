package etf.santorini.pd150258d;

public class Figura {

	String oznaka;
	private Koordinate koord;
	private int trenutnaVisina = 0;
	
	
	public Figura(Koordinate k,String ozn) {
		koord=k;
		oznaka=ozn;
	}

	public String getOznaka() {
		return oznaka;
	}

	public void setOznaka(String oznaka) {
		this.oznaka = oznaka;
	}

	public Koordinate getKoord() {
		return koord;
	}

	public void setKoord(Koordinate koord) {
		this.koord = koord;
	}

	public int getTrenutnaVisina() {
		return trenutnaVisina;
	}

	public void setTrenutnaVisina(int trenutnaVisina) {
		this.trenutnaVisina = trenutnaVisina;
	}

}
