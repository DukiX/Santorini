package etf.santorini.pd150258d;

public abstract class Igrac {
	protected String oznaka;
	protected Tabla tabla;
	protected Figura[] figure = new Figura[2];
	Figura izabranaFigura;
	
	public Igrac(String ozn,Tabla tbl) {
		oznaka=ozn;
		tabla=tbl;
	}
	
	protected Figura dohvatiFiguru(Koordinate k){
		for(int i = 0;i<2;i++){
			if(figure[i].getKoord() == k){
				return figure[i];
			}
		}
		return null;
	}
	
	protected boolean dozvoljena(Koordinate k) throws Greska{
		if(tabla.oznaka(k.getRed(), k.getKolona()).equals("0")){
			return true;
		}
		if(tabla.oznaka(k.getRed(), k.getKolona()).equals("1")){
			return true;
		}
		if(tabla.oznaka(k.getRed(), k.getKolona()).equals("2") && izabranaFigura.getTrenutnaVisina()>=1){
			return true;
		}
		if(tabla.oznaka(k.getRed(), k.getKolona()).equals("3") && izabranaFigura.getTrenutnaVisina()>=2){
			return true;
		}
		return false;
	}
	
	protected boolean dozvoljenoZaGradnju(Koordinate k) throws Greska{
		if(tabla.oznaka(k.getRed(), k.getKolona()).equals("0")){
			return true;
		}
		if(tabla.oznaka(k.getRed(), k.getKolona()).equals("1")){
			return true;
		}
		if(tabla.oznaka(k.getRed(), k.getKolona()).equals("2")){
			return true;
		}
		if(tabla.oznaka(k.getRed(), k.getKolona()).equals("3")){
			return true;
		}
		return false;
	}
	
	protected boolean dalekaPozicija(Koordinate dst,Koordinate src){
		if((dst.getRed()>(src.getRed()+1))||
				(dst.getRed()<(src.getRed()-1))||
				(dst.getKolona()>(src.getKolona()+1))||
				(dst.getKolona()<(src.getKolona()-1))){
			return true;
		}
		return false;
	}
	
	public abstract void postaviFigure()  throws InterruptedException,Greska;
	
	public abstract void prviDeoPoteza() throws InterruptedException, Greska;
	public abstract void drugiDeoPoteza() throws InterruptedException, Greska ;
}
