package etf.santorini.pd150258d;

import java.util.Random;


public class Racunar extends Igrac {

	public static final int DUBINA = 3;
	
	public Racunar(String ozn, Tabla tbl) {
		super(ozn, tbl);
	}

	@Override
	public void postaviFigure() throws InterruptedException, Greska {
		Random randomBr = new Random();
		int red = randomBr.nextInt(5);
		int kol = randomBr.nextInt(5);
		Koordinate koor = new Koordinate(red, kol);
		while (!tabla.oznaka(koor.getRed(), koor.getKolona()).equals("0")) {
			red = randomBr.nextInt(5);
			kol = randomBr.nextInt(5);
			koor = new Koordinate(red, kol);
		}
		figure[0] = new Figura(koor, oznaka + "F0");
		tabla.postaviOznaku(koor.getRed(), koor.getKolona(),
				figure[0].getOznaka() + ", Vis: " + figure[0].getTrenutnaVisina());

		red = randomBr.nextInt(5);
		kol = randomBr.nextInt(5);
		koor = new Koordinate(red, kol);
		while (!tabla.oznaka(koor.getRed(), koor.getKolona()).equals("0")) {
			red = randomBr.nextInt(5);
			kol = randomBr.nextInt(5);
			koor = new Koordinate(red, kol);
		}
		figure[1] = new Figura(koor, oznaka + "F1");
		tabla.postaviOznaku(koor.getRed(), koor.getKolona(),
				figure[1].getOznaka() + ", Vis: " + figure[1].getTrenutnaVisina());
	}

	@Override
	public void prviDeoPoteza() throws InterruptedException, Greska {
		
		Igra.minimaxPrvi(tabla, Igra.pripremiFigure(), DUBINA, 0, 1);
		
		izabranaFigura = dohvatiFiguru(Igra.staraPozicija);
		
		tabla.postaviOznaku(Igra.staraPozicija.getRed(), Igra.staraPozicija.getKolona(),
				Integer.toString(izabranaFigura.getTrenutnaVisina()));
		izabranaFigura.setKoord(Igra.odlukaPomeranje);	
		//System.out.println(Igra.odlukaPomeranje.getRed()+"bla"+Igra.odlukaPomeranje.getKolona());
		izabranaFigura.setTrenutnaVisina(
				Integer.parseInt(tabla.oznaka(Igra.odlukaPomeranje.getRed(), Igra.odlukaPomeranje.getKolona())));
		
		tabla.postaviOznaku(Igra.odlukaPomeranje.getRed(), Igra.odlukaPomeranje.getKolona(),
				izabranaFigura.getOznaka() + ", Vis: " + izabranaFigura.getTrenutnaVisina());
	}

	@Override
	public void drugiDeoPoteza() throws InterruptedException, Greska {
		
		int staraVisina = Integer.parseInt(tabla.oznaka(Igra.odlukaGradnja.getRed(), Igra.odlukaGradnja.getKolona()));
		if (staraVisina == 3) {
			tabla.postaviOznaku(Igra.odlukaGradnja.getRed(), Igra.odlukaGradnja.getKolona(), "K");
		} else {
			int novaVisina = staraVisina + 1;
			tabla.postaviOznaku(Igra.odlukaGradnja.getRed(), Igra.odlukaGradnja.getKolona(), Integer.toString(novaVisina));
		}
	}

}
