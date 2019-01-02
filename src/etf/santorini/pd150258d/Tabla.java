package etf.santorini.pd150258d;

import java.awt.*;
import java.awt.event.*;

public class Tabla implements Cloneable {
	private Panel ploca;
	private Dugme[][] dugmad = new Dugme[5][5];
	private Koordinate pritisnutoKoord;

	private static class Dugme extends Button {
		private static final long serialVersionUID = 1L;
		public Koordinate k;

		public Dugme(int r, int kol) {
			super("0");
			k = new Koordinate(r, kol);
		}
	}

	public Tabla nova() {
		Tabla t = new Tabla(new Panel());
		t.ploca = new Panel();
		t.ploca.setLayout(new GridLayout(5, 5));

		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				t.ploca.add(t.dugmad[i][j] = new Dugme(i, j));
				t.dugmad[i][j].setLabel(dugmad[i][j].getLabel());
			}
		}
		//t.pritisnutoKoord = new Koordinate(pritisnutoKoord.getRed(), pritisnutoKoord.getKolona());
		return t;
	}

	public Tabla(Panel plo) {
		(ploca = plo).setLayout(new GridLayout(5, 5));
		ploca.setEnabled(false);
		Font font = new Font(null, Font.BOLD, 20);
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				ploca.add(dugmad[i][j] = new Dugme(i, j));
				dugmad[i][j].setFont(font);
				dugmad[i][j].addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						pritisnutoKoord = ((Dugme) e.getSource()).k;
						dalje();
					}
				});
			}
		}
	}

	private synchronized void dalje() {
		notify();
	}

	public synchronized Koordinate uzmiPritisnuto() throws InterruptedException {
		ploca.setEnabled(true);
		wait();
		ploca.setEnabled(false);
		return pritisnutoKoord;
	}

	public Koordinate uzmiKoordinate(int red, int kol) {
		return dugmad[red][kol].k;
	}

	public void postaviOznaku(int red, int kol, String oznaka) throws Greska {
		if (red < 0 || red >= 5 || kol < 0 || kol >= 5)
			throw new Greska();
		dugmad[red][kol].setLabel(oznaka);
	}

	public void prazni() {
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				dugmad[i][j].setLabel("0");
			}
		}
	}

	public String oznaka(int red, int kol) throws Greska {
		if (red < 0 || red >= 5 || kol < 0 || kol >= 5)
			throw new Greska();
		return dugmad[red][kol].getLabel();
	}
}
