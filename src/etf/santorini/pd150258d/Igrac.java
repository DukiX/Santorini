package etf.santorini.pd150258d;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public abstract class Igrac {
	protected String oznaka;
	protected Tabla tabla;
	protected Figura[] figure = new Figura[2];
	Figura izabranaFigura;
	public static int minMaxVr=0;
	
	public String getOznaka() {
		return oznaka;
	}

	public void setOznaka(String oznaka) {
		this.oznaka = oznaka;
	}

	public static PrintWriter writer;

	public Figura[] getFigure() {
		return figure;
	}

	public void setFigure(Figura[] figure) {
		this.figure = figure;
	}

	public Igrac(String ozn, Tabla tbl) {
		oznaka = ozn;
		tabla = tbl;
		try {
			writer=new PrintWriter("izlaz.txt","UTF-8");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	public static void upisiUFajl(int red,int kolona){
		switch(red){
			case 0:
				writer.print("A");
				break;
			case 1:
				writer.print("B");
				break;
			case 2:
				writer.print("C");
				break;
			case 3:
				writer.print("D");
				break;
			case 4:
				writer.print("E");
				break;
		}
		switch(kolona){
		case 0:
			writer.print("1");
			break;
		case 1:
			writer.print("2");
			break;
		case 2:
			writer.print("3");
			break;
		case 3:
			writer.print("4");
			break;
		case 4:
			writer.print("5");
			break;
		}
	}

	protected Figura dohvatiFiguru(Koordinate k) {
		for (int i = 0; i < 2; i++) {
			if (figure[i].getKoord().getRed() == k.getRed() && figure[i].getKoord().getKolona() == k.getKolona()) {
				return figure[i];
			}
		}
		return null;
	}

	protected boolean dozvoljena(Koordinate k) throws Greska {
		if (tabla.oznaka(k.getRed(), k.getKolona()).equals("0")) {
			return true;
		}
		if (tabla.oznaka(k.getRed(), k.getKolona()).equals("1")) {
			return true;
		}
		if (tabla.oznaka(k.getRed(), k.getKolona()).equals("2") && izabranaFigura.getTrenutnaVisina() >= 1) {
			return true;
		}
		if (tabla.oznaka(k.getRed(), k.getKolona()).equals("3") && izabranaFigura.getTrenutnaVisina() >= 2) {
			return true;
		}
		return false;
	}

	protected boolean dozvoljenoZaGradnju(Koordinate k) throws Greska {
		if (tabla.oznaka(k.getRed(), k.getKolona()).equals("0")) {
			return true;
		}
		if (tabla.oznaka(k.getRed(), k.getKolona()).equals("1")) {
			return true;
		}
		if (tabla.oznaka(k.getRed(), k.getKolona()).equals("2")) {
			return true;
		}
		if (tabla.oznaka(k.getRed(), k.getKolona()).equals("3")) {
			return true;
		}
		return false;
	}

	protected boolean dalekaPozicija(Koordinate dst, Koordinate src) {
		if ((dst.getRed() > (src.getRed() + 1)) || (dst.getRed() < (src.getRed() - 1))
				|| (dst.getKolona() > (src.getKolona() + 1)) || (dst.getKolona() < (src.getKolona() - 1))) {
			return true;
		}
		return false;
	}

	public abstract void postaviFigure() throws InterruptedException, Greska;

	public abstract void prviDeoPoteza() throws InterruptedException, Greska;

	public abstract void drugiDeoPoteza() throws InterruptedException, Greska;
}
