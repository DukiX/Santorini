package etf.santorini.pd150258d;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;

public class Igra extends Thread {

	private static Igrac[] igraci = new Igrac[2];
	private static int tekIgrac;
	private Tabla tabla;
	private Label oznStanje;
	public static Label metrika = new Label();
	private Font font = new Font(null, Font.BOLD, 20);

	public Igra(Tabla tbl, Igrac prvi, Igrac drugi, Label sta) {
		igraci[0] = prvi;
		igraci[1] = drugi;
		tabla = tbl;
		tabla.prazni();
		tekIgrac = 0;
		oznStanje = sta;
		oznStanje.setText("");
		ucitaneFigurePrvog = false;
		ucitaneFigureDrugog = false;
		metrika.setText("");
		metrika.setFont(font);
		// start();
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

	private static int statickaFunkcijaZadata(Tabla t, Figura[] moje, Figura[] tudje, boolean max) throws Greska {
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
		int razlika = rastojanjeTudjih - rastojanjeMojih;
		l = l * razlika;
		f = m + l;

		if (max) {
			f = -f;
		}

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

			Figura[] temp1 = new Figura[2];
			Figura[] temp2 = new Figura[2];
			temp1[0] = trenFig[0];
			temp1[1] = trenFig[1];
			temp2[0] = trenFig[2];
			temp2[1] = trenFig[3];

			if (!imaLiSlobodnih(trenutnoStanje, temp1[0], temp1[1])) {
				return statickaFunkcijaZadata(trenutnoStanje, temp1, temp2, true);
			}

			if (trenutnaDubina == maxDubina) {
				return statickaFunkcijaZadata(trenutnoStanje, temp1, temp2, true);
			}

			if (trenFig[0].getTrenutnaVisina() == 3 || trenFig[1].getTrenutnaVisina() == 3
					|| trenFig[2].getTrenutnaVisina() == 3 || trenFig[3].getTrenutnaVisina() == 3) {
				return statickaFunkcijaZadata(trenutnoStanje, temp1, temp2, true);
			}

		} else {
			najboljaVrednost = 100000;
			donja = 2;
			gornja = 4;

			Figura[] temp1 = new Figura[2];
			Figura[] temp2 = new Figura[2];
			temp1[0] = trenFig[2];
			temp1[1] = trenFig[3];
			temp2[0] = trenFig[0];
			temp2[1] = trenFig[1];

			if (!imaLiSlobodnih(trenutnoStanje, temp1[0], temp1[1])) {
				return statickaFunkcijaZadata(trenutnoStanje, temp1, temp2, false);
			}

			if (trenutnaDubina == maxDubina) {
				return statickaFunkcijaZadata(trenutnoStanje, temp1, temp2, false);
			}

			if (trenFig[0].getTrenutnaVisina() == 3 || trenFig[1].getTrenutnaVisina() == 3
					|| trenFig[2].getTrenutnaVisina() == 3 || trenFig[3].getTrenutnaVisina() == 3) {
				return statickaFunkcijaZadata(trenutnoStanje, temp1, temp2, false);
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
								// System.out.println("1Najbolja:" +
								// najboljaVrednost);
								najboljaVrednost = trenutnaVrednost;
								// System.out.println("2Najbolja:" +
								// najboljaVrednost);

								if (trenutnaDubina == 0) {
									staraPozicija = trenFig[f].getKoord();

									odlukaPomeranje = novoPostavljeno;
									// System.out.println(
									// odlukaPomeranje.getRed() + " " +
									// odlukaPomeranje.getKolona() + "\n");
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

	private synchronized void stani() throws InterruptedException {
		wait();
	}

	public synchronized void poteraj() {
		notify();
	}

	private boolean ucitaneFigurePrvog = false;
	private boolean ucitaneFigureDrugog = false;

	private Koordinate uzmiKoordinate(String r, String k) {
		int red = 0;
		switch (r) {
		case "A":
			red = 0;
			break;
		case "B":
			red = 1;
			break;
		case "C":
			red = 2;
			break;
		case "D":
			red = 3;
			break;
		case "E":
			red = 4;
			break;

		}
		int kolona = Integer.parseInt(k);
		return new Koordinate(red, kolona - 1);
	}

	public void ucitajIzFajla() throws InterruptedException {
		try {
			BufferedReader reader = new BufferedReader(new FileReader("ulaz.txt"));
			String line = reader.readLine();
			int i = 0;
			Koordinate k;
			while (line != null) {
				String lineLok = line.trim().replaceAll(" +", " ");
				if (i == 0) {
					k = uzmiKoordinate(lineLok.substring(0, 1), lineLok.substring(1, 2));

					igraci[0].figure[0] = new Figura(k, igraci[0].getOznaka() + "F0");
					tabla.postaviOznaku(k.getRed(), k.getKolona(),
							igraci[0].figure[0].getOznaka() + ", Vis: " + igraci[0].figure[0].getTrenutnaVisina());

					Igrac.upisiUFajl(k.getRed(), k.getKolona());

					Igrac.writer.print(" ");

					k = uzmiKoordinate(lineLok.substring(3, 4), lineLok.substring(4, 5));

					igraci[0].figure[1] = new Figura(k, igraci[0].getOznaka() + "F1");
					tabla.postaviOznaku(k.getRed(), k.getKolona(),
							igraci[0].figure[1].getOznaka() + ", Vis: " + igraci[0].figure[1].getTrenutnaVisina());

					Igrac.upisiUFajl(k.getRed(), k.getKolona());
					Igrac.writer.println();

					ucitaneFigurePrvog = true;
					i++;
				} else if (i == 1) {
					k = uzmiKoordinate(lineLok.substring(0, 1), lineLok.substring(1, 2));

					igraci[1].figure[0] = new Figura(k, igraci[1].getOznaka() + "F0");
					tabla.postaviOznaku(k.getRed(), k.getKolona(),
							igraci[1].figure[0].getOznaka() + ", Vis: " + igraci[1].figure[0].getTrenutnaVisina());

					Igrac.upisiUFajl(k.getRed(), k.getKolona());

					Igrac.writer.print(" ");

					k = uzmiKoordinate(lineLok.substring(3, 4), lineLok.substring(4, 5));

					igraci[1].figure[1] = new Figura(k, igraci[1].getOznaka() + "F1");
					tabla.postaviOznaku(k.getRed(), k.getKolona(),
							igraci[1].figure[1].getOznaka() + ", Vis: " + igraci[1].figure[1].getTrenutnaVisina());

					Igrac.upisiUFajl(k.getRed(), k.getKolona());
					Igrac.writer.println();

					ucitaneFigureDrugog = true;
					i++;
				} else {

					k = uzmiKoordinate(lineLok.substring(0, 1), lineLok.substring(1, 2));
					Koordinate novaPozicija = uzmiKoordinate(lineLok.substring(3, 4), lineLok.substring(4, 5));

					Figura izabranaFigura = igraci[tekIgrac].dohvatiFiguru(k);

					tabla.postaviOznaku(k.getRed(), k.getKolona(),
							Integer.toString(izabranaFigura.getTrenutnaVisina()));
					izabranaFigura.setKoord(novaPozicija);
					izabranaFigura.setTrenutnaVisina(
							Integer.parseInt(tabla.oznaka(novaPozicija.getRed(), novaPozicija.getKolona())));
					tabla.postaviOznaku(novaPozicija.getRed(), novaPozicija.getKolona(),
							izabranaFigura.getOznaka() + ", Vis: " + izabranaFigura.getTrenutnaVisina());

					Igrac.upisiUFajl(k.getRed(), k.getKolona());
					Igrac.writer.print(" ");
					Igrac.upisiUFajl(novaPozicija.getRed(), novaPozicija.getKolona());
					Igrac.writer.print(" ");

					Koordinate izabranoPolje = uzmiKoordinate(lineLok.substring(6, 7), lineLok.substring(7, 8));

					int staraVisina = Integer.parseInt(tabla.oznaka(izabranoPolje.getRed(), izabranoPolje.getKolona()));
					if (staraVisina == 3) {
						tabla.postaviOznaku(izabranoPolje.getRed(), izabranoPolje.getKolona(), "K");
					} else {
						int novaVisina = staraVisina + 1;
						tabla.postaviOznaku(izabranoPolje.getRed(), izabranoPolje.getKolona(),
								Integer.toString(novaVisina));
					}

					Igrac.upisiUFajl(izabranoPolje.getRed(), izabranoPolje.getKolona());
					Igrac.writer.println();

					tekIgrac = 1 - tekIgrac;
				}

				line = reader.readLine();
			}
			reader.close();
		} catch (Exception e) {
			oznStanje.setText("Greska pri ucitavanju iz fajla");
			ucitaneFigurePrvog = false;
			ucitaneFigureDrugog = false;
			e.printStackTrace();
		}

	}

	@Override
	public void run() {
		String stanje = " ";
		try {
			oznStanje.setText("Prvi igrac postavlja figure:");
			if (!ucitaneFigurePrvog) {
				igraci[0].postaviFigure();
			}
			oznStanje.setText("Drugi igrac postavlja figure:");
			if (!ucitaneFigureDrugog) {
				igraci[1].postaviFigure();
			}
			while (!interrupted() && stanje == " ") {
				stanje = imaLiGde();
				if (stanje != " ")
					break;
				oznStanje.setText("Pomeranje figure: igrac " + tekIgrac);

				if (Santorini.rezimKorakPoKorak) {
					stani();
				}

				igraci[tekIgrac].prviDeoPoteza();
				stanje = dostigaoTreciNivo();
				if (stanje != " ")
					break;
				oznStanje.setText("Gradnja: igrac " + tekIgrac);

				/*
				 * if (Santorini.rezimKorakPoKorak) {
				 * 
				 * stani(); }
				 */

				igraci[tekIgrac].drugiDeoPoteza();

				metrika.setText(Integer.toString(Igrac.minMaxVr));

				tekIgrac = 1 - tekIgrac;
			}
			oznStanje.setText("Pobednik je " + stanje);
			if (Igrac.writer != null) {
				Igrac.writer.close();
			}
		} catch (InterruptedException | Greska g) {
			if (Igrac.writer != null) {
				Igrac.writer.close();
			}
		}
	}

	public void prekini() {
		interrupt();
	}

	public static boolean imaLiSlobodnih(Tabla tabla, Figura fig0, Figura fig1) throws NumberFormatException, Greska {
		int mojaVisina0 = fig0.getTrenutnaVisina();
		int mojaVisina1 = fig1.getTrenutnaVisina();
		Koordinate k0 = fig0.getKoord();
		Koordinate k1 = fig1.getKoord();

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
			return false;
		}

		return true;
	}

	public static int minimaxAlfaBeta(Tabla trenutnoStanje, Figura[] trenFig, int maxDubina, int trenutnaDubina,
			int trenutniIgrac, int alfa, int beta) throws Greska {
		int najboljaVrednost, trenutnaVrednost;

		// System.out.println("evo " + tekIgrac + " evo");

		Koordinate novoPostavljeno;
		Koordinate novoIzgrLok;

		int donja, gornja;

		int alfaLok = alfa;
		int betaLok = beta;

		if (trenutniIgrac == 1) {
			najboljaVrednost = -100000;
			donja = 0;
			gornja = 2;

			Figura[] temp1 = new Figura[2];
			Figura[] temp2 = new Figura[2];
			temp1[0] = trenFig[0];
			temp1[1] = trenFig[1];
			temp2[0] = trenFig[2];
			temp2[1] = trenFig[3];

			if (!imaLiSlobodnih(trenutnoStanje, temp1[0], temp1[1])) {
				return statickaFunkcijaZadata(trenutnoStanje, temp1, temp2, true);
			}

			if (trenutnaDubina == maxDubina) {
				return statickaFunkcijaZadata(trenutnoStanje, temp1, temp2, true);
			}

			if (trenFig[0].getTrenutnaVisina() == 3 || trenFig[1].getTrenutnaVisina() == 3
					|| trenFig[2].getTrenutnaVisina() == 3 || trenFig[3].getTrenutnaVisina() == 3) {
				return statickaFunkcijaZadata(trenutnoStanje, temp1, temp2, true);
			}

		} else {
			najboljaVrednost = 100000;
			donja = 2;
			gornja = 4;

			Figura[] temp1 = new Figura[2];
			Figura[] temp2 = new Figura[2];
			temp1[0] = trenFig[2];
			temp1[1] = trenFig[3];
			temp2[0] = trenFig[0];
			temp2[1] = trenFig[1];

			if (!imaLiSlobodnih(trenutnoStanje, temp1[0], temp1[1])) {
				return statickaFunkcijaZadata(trenutnoStanje, temp1, temp2, false);
			}

			if (trenutnaDubina == maxDubina) {
				return statickaFunkcijaZadata(trenutnoStanje, temp1, temp2, false);
			}

			if (trenFig[0].getTrenutnaVisina() == 3 || trenFig[1].getTrenutnaVisina() == 3
					|| trenFig[2].getTrenutnaVisina() == 3 || trenFig[3].getTrenutnaVisina() == 3) {
				return statickaFunkcijaZadata(trenutnoStanje, temp1, temp2, false);
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

					// System.out.println(" i" + i + " j" + j + " " +
					// trenFig[f].oznaka + " " + tekIgrac + "\n");

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

							// System.out.println(" m" + m + " n" + n + " " +
							// trenFig[f].oznaka + "\n");

							trenutnaVrednost = minimaxAlfaBeta(novoStanje, figureLok, maxDubina, trenutnaDubina + 1,
									1 - trenutniIgrac, alfaLok, betaLok);

							// System.out
							// .println("trenutna vrednos" + trenutnaVrednost +
							// "dubina" + trenutnaDubina + "\n");

							if (trenutniIgrac == 1 && trenutnaVrednost > najboljaVrednost) {
								// System.out.println("1Najbolja:" +
								// najboljaVrednost);
								najboljaVrednost = trenutnaVrednost;
								// System.out.println("2Najbolja:" +
								// najboljaVrednost);

								if (najboljaVrednost >= betaLok) {
									return najboljaVrednost;
								}

								alfaLok = Math.max(alfaLok, najboljaVrednost);

								if (trenutnaDubina == 0) {
									staraPozicija = trenFig[f].getKoord();

									// System.out.println(staraPozicija.getRed()
									// + " " + staraPozicija.getKolona()
									// + trenFig[f].oznaka + "\n");

									odlukaPomeranje = novoPostavljeno;
									// System.out.println(
									// odlukaPomeranje.getRed() + " " +
									// odlukaPomeranje.getKolona() + "\n");
									odlukaGradnja = novoIzgrLok;
								}
							}
							if (trenutniIgrac == 0 && trenutnaVrednost < najboljaVrednost) {
								najboljaVrednost = trenutnaVrednost;

								if (najboljaVrednost <= alfaLok) {
									return najboljaVrednost;
								}

								betaLok = Math.min(betaLok, najboljaVrednost);

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

	private static int statickaFunkcijaNapredna(Tabla t, Figura[] moje, Figura[] tudje, boolean max) throws Greska {
		int f;
		int m = privremenoPomerenaFigura.getTrenutnaVisina();
		// System.out.println(m);
		int l;
		if (t.oznaka(novoIzgradjeno.getRed(), novoIzgradjeno.getKolona()).equals("K")) {
			l = 3;
		} else {
			l = Integer.parseInt(t.oznaka(novoIzgradjeno.getRed(), novoIzgradjeno.getKolona())) - 1;
		}

		//ako se zaglavio
		if (!imaLiSlobodnih(t, moje[0], moje[1])) {
			if (max) {
				return -250;
			} else {
				return 250;
			}
		}

		//ne gradi kupolu ako niko nije blizu
		if (l == 3 && Math.abs(novoIzgradjeno.getRed() - tudje[0].getKoord().getRed()) > 1
				&& Math.abs(novoIzgradjeno.getKolona() - tudje[0].getKoord().getKolona()) > 1
				&& Math.abs(novoIzgradjeno.getRed() - tudje[1].getKoord().getRed()) > 1
				&& Math.abs(novoIzgradjeno.getKolona() - tudje[1].getKoord().getKolona()) > 1) {
			if (max) {
				return 300;
			} else {
				return -300;
			}
		}

		int rastojanjeMojih = Math.abs(moje[0].getKoord().getRed() - novoIzgradjeno.getRed())
				+ Math.abs(moje[0].getKoord().getKolona() - novoIzgradjeno.getKolona())
				+ Math.abs(moje[1].getKoord().getRed() - novoIzgradjeno.getRed())
				+ Math.abs(moje[1].getKoord().getKolona() - novoIzgradjeno.getKolona());
		int rastojanjeTudjih = Math.abs(tudje[0].getKoord().getRed() - novoIzgradjeno.getRed())
				+ Math.abs(tudje[0].getKoord().getKolona() - novoIzgradjeno.getKolona())
				+ Math.abs(tudje[1].getKoord().getRed() - novoIzgradjeno.getRed())
				+ Math.abs(tudje[1].getKoord().getKolona() - novoIzgradjeno.getKolona());
		int razlika = rastojanjeTudjih - rastojanjeMojih;
		l = l * razlika;
		f = m + l;

		//ako se popeo na vrh
		if (privremenoPomerenaFigura.getTrenutnaVisina() == 3 && max) {
			return -200;
		}

		if (privremenoPomerenaFigura.getTrenutnaVisina() == 3 && !max) {
			return 200;
		}
		
		//ako je necija figura odvojena od ostalih
		/*if ((Math.abs(tudje[0].getKoord().getRed() - moje[0].getKoord().getRed()) > 2
				&& Math.abs(tudje[0].getKoord().getKolona() - moje[0].getKoord().getKolona()) > 2
				&& Math.abs(tudje[0].getKoord().getRed() - moje[1].getKoord().getRed()) > 2
				&& Math.abs(tudje[0].getKoord().getKolona() - moje[1].getKoord().getKolona()) > 2)
				|| (Math.abs(tudje[1].getKoord().getRed() - moje[0].getKoord().getRed()) > 2
						&& Math.abs(tudje[1].getKoord().getKolona() - moje[0].getKoord().getKolona()) > 2
						&& Math.abs(tudje[1].getKoord().getRed() - moje[1].getKoord().getRed()) > 2
						&& Math.abs(tudje[1].getKoord().getKolona() - moje[1].getKoord().getKolona()) > 2)) {
			if(max){
				return 50;
			}else{
				return -50;
			}
		}*/

		if (max) {
			f = -f;
		}
		// System.out.println(f);
		return f;
	}

	public static int minimaxNapredni(Tabla trenutnoStanje, Figura[] trenFig, int maxDubina, int trenutnaDubina,
			int trenutniIgrac, int alfa, int beta) throws Greska {
		int najboljaVrednost, trenutnaVrednost;

		Koordinate novoPostavljeno;
		Koordinate novoIzgrLok;

		int donja, gornja;

		int alfaLok = alfa;
		int betaLok = beta;

		if (trenutniIgrac == 1) {
			najboljaVrednost = -100000;
			donja = 0;
			gornja = 2;

			Figura[] temp1 = new Figura[2];
			Figura[] temp2 = new Figura[2];
			temp1[0] = trenFig[0];
			temp1[1] = trenFig[1];
			temp2[0] = trenFig[2];
			temp2[1] = trenFig[3];

			if (!imaLiSlobodnih(trenutnoStanje, temp1[0], temp1[1])) {
				return statickaFunkcijaNapredna(trenutnoStanje, temp1, temp2, true);
			}

			if (trenutnaDubina == maxDubina) {
				return statickaFunkcijaNapredna(trenutnoStanje, temp1, temp2, true);
			}

			if (trenFig[0].getTrenutnaVisina() == 3 || trenFig[1].getTrenutnaVisina() == 3
					|| trenFig[2].getTrenutnaVisina() == 3 || trenFig[3].getTrenutnaVisina() == 3) {
				return statickaFunkcijaNapredna(trenutnoStanje, temp1, temp2, true);
			}

		} else {
			najboljaVrednost = 100000;
			donja = 2;
			gornja = 4;

			Figura[] temp1 = new Figura[2];
			Figura[] temp2 = new Figura[2];
			temp1[0] = trenFig[2];
			temp1[1] = trenFig[3];
			temp2[0] = trenFig[0];
			temp2[1] = trenFig[1];

			if (!imaLiSlobodnih(trenutnoStanje, temp1[0], temp1[1])) {
				return statickaFunkcijaNapredna(trenutnoStanje, temp1, temp2, false);
			}

			if (trenutnaDubina == maxDubina) {
				return statickaFunkcijaNapredna(trenutnoStanje, temp1, temp2, false);
			}

			if (trenFig[0].getTrenutnaVisina() == 3 || trenFig[1].getTrenutnaVisina() == 3
					|| trenFig[2].getTrenutnaVisina() == 3 || trenFig[3].getTrenutnaVisina() == 3) {
				return statickaFunkcijaNapredna(trenutnoStanje, temp1, temp2, false);
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

							trenutnaVrednost = minimaxNapredni(novoStanje, figureLok, maxDubina, trenutnaDubina + 1,
									1 - trenutniIgrac, alfaLok, betaLok);

							if (trenutniIgrac == 1 && trenutnaVrednost > najboljaVrednost) {
								// System.out.println("1Najbolja:" +
								// najboljaVrednost);
								najboljaVrednost = trenutnaVrednost;
								// System.out.println("Najbolja:" +
								// najboljaVrednost);

								if (najboljaVrednost >= betaLok) {
									return najboljaVrednost;
								}

								alfaLok = Math.max(alfaLok, najboljaVrednost);

								if (trenutnaDubina == 0) {
									staraPozicija = trenFig[f].getKoord();

									odlukaPomeranje = novoPostavljeno;
									// System.out.println(
									// odlukaPomeranje.getRed() + " " +
									// odlukaPomeranje.getKolona() + "\n");
									odlukaGradnja = novoIzgrLok;
								}
							}
							if (trenutniIgrac == 0 && trenutnaVrednost < najboljaVrednost) {
								najboljaVrednost = trenutnaVrednost;

								// System.out.println("Najbolja:" +
								// najboljaVrednost);

								if (najboljaVrednost <= alfaLok) {
									return najboljaVrednost;
								}

								betaLok = Math.min(betaLok, najboljaVrednost);

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

}
