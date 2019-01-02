package etf.santorini.pd150258d;

import java.awt.*;

public class Igra extends Thread {

	private static Igrac[] igraci = new Igrac[2];
	private static int tekIgrac;
	private Tabla tabla;
	private Label oznStanje;

	public Igra(Tabla tbl, Igrac prvi, Igrac drugi, Label sta) {
		igraci[0] = prvi;
		igraci[1] = drugi;
		tabla = tbl;
		tabla.prazni();
		tekIgrac = 0;
		oznStanje = sta;
		oznStanje.setText("");
		start();
	}

	private static boolean dozvoljenoZaGradnju(Tabla t, Koordinate k) throws Greska {
		if (t.oznaka(k.getRed(), k.getKolona()).equals("0")) {
			return true;
		}
		if (t.oznaka(k.getRed(), k.getKolona()).equals("1")) {
			return true;
		}
		if (t.oznaka(k.getRed(), k.getKolona()).equals("2")) {
			return true;
		}
		if (t.oznaka(k.getRed(), k.getKolona()).equals("3")) {
			return true;
		}
		return false;
	}

	private static Figura privremenoPomerenaFigura;
	private static Koordinate novoIzgradjeno;
	public static Koordinate odlukaPomeranje, odlukaGradnja, staraPozicija;

	public static Figura[] pripremiFigure() {
		Figura[] f = new Figura[4];
		f[0] = igraci[tekIgrac].getFigure()[0];
		f[1] = igraci[tekIgrac].getFigure()[1];
		f[2] = igraci[1 - tekIgrac].getFigure()[0];
		f[3] = igraci[1 - tekIgrac].getFigure()[1];
		return f;
	}

	private static int statickaFunkcijaZadata(Tabla t, Figura[] moje, Figura[] tudje) throws Greska {
		int f;
		int m = privremenoPomerenaFigura.getTrenutnaVisina();
		int l;
		if (t.oznaka(novoIzgradjeno.getRed(), novoIzgradjeno.getKolona()).equals("K")) {
			l = 3;
		} else {
			l = Integer.parseInt(t.oznaka(novoIzgradjeno.getRed(), novoIzgradjeno.getKolona())) - 1;
		}
		int rastojanjeMojih = Math.abs(moje[0].getKoord().getRed() - novoIzgradjeno.getRed())
				+ Math.abs(moje[0].getKoord().getKolona() - novoIzgradjeno.getKolona())
				+ Math.abs(moje[1].getKoord().getRed() - novoIzgradjeno.getRed())
				+ Math.abs(moje[1].getKoord().getKolona() - novoIzgradjeno.getKolona());
		int rastojanjeTudjih = Math.abs(tudje[0].getKoord().getRed() - novoIzgradjeno.getRed())
				+ Math.abs(tudje[0].getKoord().getKolona() - novoIzgradjeno.getKolona())
				+ Math.abs(tudje[1].getKoord().getRed() - novoIzgradjeno.getRed())
				+ Math.abs(tudje[1].getKoord().getKolona() - novoIzgradjeno.getKolona());
		int razlika = Math.abs(rastojanjeMojih - rastojanjeTudjih);
		l = l * razlika;
		f = m + l;
		return f;
	}

	public static int minimaxPrvi(Tabla trenutnoStanje, Figura[] trenFig, int maxDubina, int trenutnaDubina,
			int trenutniIgrac) throws Greska {
		int najboljaVrednost, trenutnaVrednost;

		Koordinate novoPostavljeno;
		Koordinate novoIzgrLok;

		int donja, gornja;

		if (trenutniIgrac == 1) {
			najboljaVrednost = -100000;
			donja = 0;
			gornja = 2;

			if (trenutnaDubina == maxDubina) {
				Figura[] temp1 = new Figura[2];
				Figura[] temp2 = new Figura[2];
				temp1[0] = trenFig[0];
				temp1[1] = trenFig[1];
				temp2[0] = trenFig[2];
				temp2[1] = trenFig[3];
				return statickaFunkcijaZadata(trenutnoStanje, temp1, temp2);
			}

			if (trenFig[0].getTrenutnaVisina() == 3 || trenFig[1].getTrenutnaVisina() == 3
					|| trenFig[2].getTrenutnaVisina() == 3 || trenFig[3].getTrenutnaVisina() == 3) {
				Figura[] temp1 = new Figura[2];
				Figura[] temp2 = new Figura[2];
				temp1[0] = trenFig[0];
				temp1[1] = trenFig[1];
				temp2[0] = trenFig[2];
				temp2[1] = trenFig[3];
				return statickaFunkcijaZadata(trenutnoStanje, temp1, temp2);
			}

		} else {
			najboljaVrednost = 100000;
			donja = 2;
			gornja = 4;

			if (trenutnaDubina == maxDubina) {
				Figura[] temp1 = new Figura[2];
				Figura[] temp2 = new Figura[2];
				temp1[0] = trenFig[2];
				temp1[1] = trenFig[3];
				temp2[0] = trenFig[0];
				temp2[1] = trenFig[1];
				return statickaFunkcijaZadata(trenutnoStanje, temp1, temp2);
			}

			if (trenFig[0].getTrenutnaVisina() == 3 || trenFig[1].getTrenutnaVisina() == 3
					|| trenFig[2].getTrenutnaVisina() == 3 || trenFig[3].getTrenutnaVisina() == 3) {
				Figura[] temp1 = new Figura[2];
				Figura[] temp2 = new Figura[2];
				temp1[0] = trenFig[2];
				temp1[1] = trenFig[3];
				temp2[0] = trenFig[0];
				temp2[1] = trenFig[1];
				return statickaFunkcijaZadata(trenutnoStanje, temp1, temp2);
			}
		}

		for (int f = donja; f < gornja; f++) {

			// System.out.println(trenFig[f].getKoord().getRed()+"
			// "+trenFig[f].getKoord().getKolona()+" "+f);

			for (int i = trenFig[f].getKoord().getRed() - 1; i <= trenFig[f].getKoord().getRed() + 1; i++) {
				for (int j = trenFig[f].getKoord().getKolona() - 1; j <= trenFig[f].getKoord().getKolona() + 1; j++) {
					
					if (i < 0 || i > 4 || j < 0 || j > 4) {
						continue;
					}
					if (i == trenFig[f].getKoord().getRed() && j == trenFig[f].getKoord().getKolona()) {
						continue;
					}
					if (!trenutnoStanje.oznaka(i, j).equals("0") && !trenutnoStanje.oznaka(i, j).equals("1")
							&& !trenutnoStanje.oznaka(i, j).equals("2") && !trenutnoStanje.oznaka(i, j).equals("3")) {
						continue;
					}

					if (!(trenutnoStanje.oznaka(i, j).equals("0") || trenutnoStanje.oznaka(i, j).equals("1")
							|| (trenutnoStanje.oznaka(i, j).equals("2") && trenFig[f].getTrenutnaVisina() >= 1)
							|| (trenutnoStanje.oznaka(i, j).equals("3") && trenFig[f].getTrenutnaVisina() >= 2))) {
						continue;
					}

					Tabla novoStanje = trenutnoStanje.nova();

					Figura[] figureLok = new Figura[4];

					for (int z = 0; z < 4; z++) {
						figureLok[z] = new Figura(
								new Koordinate(trenFig[z].getKoord().getRed(), trenFig[z].getKoord().getKolona()),
								trenFig[z].getOznaka());
						figureLok[z].setTrenutnaVisina(trenFig[z].getTrenutnaVisina());
					}

					novoStanje.postaviOznaku(figureLok[f].getKoord().getRed(), figureLok[f].getKoord().getKolona(),
							Integer.toString(figureLok[f].getTrenutnaVisina()));
					figureLok[f].setKoord(novoStanje.uzmiKoordinate(i, j));
					figureLok[f].setTrenutnaVisina(Integer.parseInt(novoStanje.oznaka(
							novoStanje.uzmiKoordinate(i, j).getRed(), novoStanje.uzmiKoordinate(i, j).getKolona())));
					novoStanje.postaviOznaku(novoStanje.uzmiKoordinate(i, j).getRed(),
							novoStanje.uzmiKoordinate(i, j).getKolona(),
							figureLok[f].getOznaka() + ", Vis: " + figureLok[f].getTrenutnaVisina());

					novoPostavljeno = figureLok[f].getKoord();
					privremenoPomerenaFigura = figureLok[f];

					for (int m = figureLok[f].getKoord().getRed() - 1; m <= figureLok[f].getKoord().getRed() + 1; m++) {
						for (int n = figureLok[f].getKoord().getKolona() - 1; n <= figureLok[f].getKoord().getKolona()
								+ 1; n++) {
							if (m < 0 || m > 4 || n < 0 || n > 4) {
								continue;
							}
							if (m == figureLok[f].getKoord().getRed() && n == figureLok[f].getKoord().getKolona()) {
								continue;
							}
							if (!dozvoljenoZaGradnju(novoStanje, novoStanje.uzmiKoordinate(m, n))) {
								continue;
							}

							int staraVisina = Integer
									.parseInt(novoStanje.oznaka(novoStanje.uzmiKoordinate(m, n).getRed(),
											novoStanje.uzmiKoordinate(m, n).getKolona()));
							if (staraVisina == 3) {
								novoStanje.postaviOznaku(novoStanje.uzmiKoordinate(m, n).getRed(),
										novoStanje.uzmiKoordinate(m, n).getKolona(), "K");
							} else {
								int novaVisina = staraVisina + 1;
								novoStanje.postaviOznaku(novoStanje.uzmiKoordinate(m, n).getRed(),
										novoStanje.uzmiKoordinate(m, n).getKolona(), Integer.toString(novaVisina));
							}

							novoIzgradjeno = novoStanje.uzmiKoordinate(m, n);
							novoIzgrLok = novoIzgradjeno;

							trenutnaVrednost = minimaxPrvi(novoStanje, figureLok, maxDubina, trenutnaDubina + 1,
									1 - trenutniIgrac);

							if (trenutniIgrac == 1 && trenutnaVrednost > najboljaVrednost) {
								System.out.println("1Najbolja:" + najboljaVrednost);
								najboljaVrednost = trenutnaVrednost;
								System.out.println("2Najbolja:" + najboljaVrednost);

								if (trenutnaDubina == 0) {
									staraPozicija = trenFig[f].getKoord();

									odlukaPomeranje = novoPostavljeno;
									System.out.println(
											odlukaPomeranje.getRed() + " " + odlukaPomeranje.getKolona() + "\n");
									odlukaGradnja = novoIzgrLok;
								}
							}
							if (trenutniIgrac == 0 && trenutnaVrednost < najboljaVrednost) {
								najboljaVrednost = trenutnaVrednost;
								// odlukaPomeranje = novoPostavljeno;
								// odlukaGradnja = novoIzgradjeno;
								// staraPozicija = trenFig[f].getKoord();
							}
						}
					}
				}
			}
		}
		return najboljaVrednost;
	}

	private String dostigaoTreciNivo() {
		if (igraci[tekIgrac].figure[0].getTrenutnaVisina() == 3
				|| igraci[tekIgrac].figure[1].getTrenutnaVisina() == 3) {
			return "igrac broj " + tekIgrac;
		}
		return " ";
	}

	private String imaLiGde() throws Greska {
		int mojaVisina0 = igraci[tekIgrac].figure[0].getTrenutnaVisina();
		int mojaVisina1 = igraci[tekIgrac].figure[1].getTrenutnaVisina();
		Koordinate k0 = igraci[tekIgrac].figure[0].getKoord();
		Koordinate k1 = igraci[tekIgrac].figure[1].getKoord();

		boolean nemaGde0 = true;
		boolean nemaGde1 = true;

		for (int i = k0.getRed() - 1; i <= k0.getRed() + 1; i++) {
			for (int j = k0.getKolona() - 1; j <= k0.getKolona() + 1; j++) {
				if (i < 0 || i > 4 || j < 0 || j > 4) {
					continue;
				}
				if (i == k0.getRed() && j == k0.getKolona()) {
					continue;
				}
				if (tabla.oznaka(i, j).equals("0") || tabla.oznaka(i, j).equals("1") || tabla.oznaka(i, j).equals("2")
						|| tabla.oznaka(i, j).equals("3")) {
					int visinaPolja = Integer.parseInt(tabla.oznaka(i, j));
					if (visinaPolja <= mojaVisina0 + 1) {
						nemaGde0 = false;
					}
				}
			}
		}

		for (int i = k1.getRed() - 1; i <= k1.getRed() + 1; i++) {
			for (int j = k1.getKolona() - 1; j <= k1.getKolona() + 1; j++) {
				if (i < 0 || i > 4 || j < 0 || j > 4) {
					continue;
				}
				if (i == k1.getRed() && j == k1.getKolona()) {
					continue;
				}
				if (tabla.oznaka(i, j).equals("0") || tabla.oznaka(i, j).equals("1") || tabla.oznaka(i, j).equals("2")
						|| tabla.oznaka(i, j).equals("3")) {
					int visinaPolja = Integer.parseInt(tabla.oznaka(i, j));
					if (visinaPolja <= mojaVisina1 + 1) {
						nemaGde1 = false;
					}
				}
			}
		}

		if (nemaGde0 && nemaGde1) {
			return "igrac broj: " + (1 - tekIgrac) + " jer igrac " + tekIgrac + " nema slobodnih poteza";
		}

		return " ";
	}

	@Override
	public void run() {
		String stanje = " ";
		try {
			oznStanje.setText("Prvi igrac postavlja figure:");
			igraci[0].postaviFigure();
			oznStanje.setText("Drugi igrac postavlja figure:");
			igraci[1].postaviFigure();
			while (!interrupted() && stanje == " ") {
				stanje = imaLiGde();
				if (stanje != " ")
					break;
				oznStanje.setText("Pomeranje figure: igrac " + tekIgrac);
				igraci[tekIgrac].prviDeoPoteza();
				stanje = dostigaoTreciNivo();
				if (stanje != " ")
					break;
				oznStanje.setText("Gradnja: igrac " + tekIgrac);
				igraci[tekIgrac].drugiDeoPoteza();
				tekIgrac = 1 - tekIgrac;
			}
			oznStanje.setText("Pobednik je " + stanje);
		} catch (InterruptedException | Greska g) {
		}
	}

	public void prekini() {
		interrupt();
	}

}
