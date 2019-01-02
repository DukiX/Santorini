package etf.santorini.pd150258d;

import java.awt.*;
import java.awt.event.*;

public class Santorini extends Frame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Font font = new Font(null, Font.BOLD, 20);

	private class IgracPanel extends Panel {

		private static final long serialVersionUID = 1L;
		private CheckboxGroup grupa = new CheckboxGroup();
		private Checkbox izborCovek = new Checkbox("Covek", true, grupa);
		private Checkbox izborRacunar = new Checkbox("Racunar", false, grupa);

		private String oznaka;
		private Igrac igrac;

		IgracPanel(String ozn) {
			oznaka = ozn;
			Label ntp = new Label(ozn, Label.CENTER);
			ntp.setFont(font);
			ItemListener osluskivac = new ItemListener() {

				@Override
				public void itemStateChanged(ItemEvent e) {
					if ((Checkbox) e.getSource() == izborCovek) {
						igrac = new Covek(oznaka, tabla);
					} else {
						igrac = new Racunar(oznaka, tabla); // racunar treba
					}
				}
			};

			izborCovek.addItemListener(osluskivac);
			izborRacunar.addItemListener(osluskivac);
			setLayout(new GridLayout(3, 1));
			add(ntp);
			add(izborCovek);
			add(izborRacunar);
			igrac = new Covek(oznaka, tabla);
		}

	}

	private Panel ploca = new Panel();
	private Tabla tabla = new Tabla(ploca);
	private IgracPanel prvi = new IgracPanel("P0");
	private IgracPanel drugi = new IgracPanel("P1");
	private Igra igra;
	private Label stanje = new Label("", Label.CENTER);

	public void popuniProzor() {
		add(ploca, "Center");
		add(prvi, "West");
		add(drugi, "East");

		Button dugme = new Button("Nova igra");
		dugme.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (igra != null)
					igra.prekini();
				igra = new Igra(tabla, prvi.igrac, drugi.igrac, stanje);
			}
		});
		add(dugme, "South");
		stanje.setFont(font);
		add(stanje, "North");
	}

	public Santorini() {
		super("Santorini");
		setBounds(300, 300, 800, 500);
		popuniProzor();
		setVisible(true);

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent w) {
				if (igra != null) {
					igra.prekini();
				}
				dispose();
			}
		});
	}

	public static void main(String[] varg) {
		new Santorini();
	}

}
