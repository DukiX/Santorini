package etf.santorini.pd150258d;

import java.awt.*;
import java.awt.event.*;

public class Santorini extends Frame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Font font = new Font(null, Font.BOLD, 20);
	
	public static int DUBINA = 3;
	public static boolean rezimKorakPoKorak;

	private class IgracPanel extends Panel {

		private static final long serialVersionUID = 1L;
		private CheckboxGroup grupa = new CheckboxGroup();
		private Checkbox izborCovek = new Checkbox("Covek", true, grupa);
		private Checkbox izborRacunar = new Checkbox("Racunar", false, grupa);
		private Checkbox izborRacunarAlfaBeta = new Checkbox("RacunarAlfaBeta", false, grupa);
		private Checkbox izborRacunarNapredni = new Checkbox("RacunarNapredni", false, grupa);

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
					}else if((Checkbox) e.getSource() == izborRacunarAlfaBeta){
						igrac = new RacunarAlfaBeta(oznaka, tabla); // racunar treba
					}else if((Checkbox) e.getSource() == izborRacunarNapredni){
						igrac = new RacunarNapredni(oznaka, tabla);
					}else {
						igrac = new Racunar(oznaka, tabla); // racunar treba
					}
					if(prvi.igrac.getClass() != Covek.class && drugi.igrac.getClass() != Covek.class){
						korakpokorak.setEnabled(true);
					}else{
						korakpokorak.setState(false);
						korakpokorak.setEnabled(false);
						slkorak.setEnabled(false);
						rezimKorakPoKorak=false;
					}
					if (igra != null)
						igra.prekini();
					igra = new Igra(tabla, prvi.igrac, drugi.igrac, stanje);
				}
			};

			izborCovek.addItemListener(osluskivac);
			izborRacunar.addItemListener(osluskivac);
			izborRacunarAlfaBeta.addItemListener(osluskivac);
			izborRacunarNapredni.addItemListener(osluskivac);
			setLayout(new GridLayout(5, 1));
			add(ntp);
			add(izborCovek);
			add(izborRacunar);
			add(izborRacunarAlfaBeta);
			add(izborRacunarNapredni);
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
		Button reset = new Button("Start");
		reset.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				//if (igra != null)
					//igra.prekini();
				igra.start();
			}
		});
		Panel p = new Panel();
		p.setLayout(new GridLayout(1, 3));
		korakpokorak =new Checkbox("Korak po korak");
		korakpokorak.setEnabled(false);
		korakpokorak.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED){
					slkorak.setEnabled(true);
					rezimKorakPoKorak=true;
				}
				if(e.getStateChange() == ItemEvent.DESELECTED){
					slkorak.setEnabled(false);
					rezimKorakPoKorak=false;
					Igra.metrika.setText("");
				}
			}
		});
		p.add(korakpokorak);
		p.add(Igra.metrika);
		slkorak = new Button("Sledeci korak");
		slkorak.setEnabled(false);
		slkorak.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				igra.poteraj();
			}
		});
		p.add(slkorak);
		Panel plo = new Panel();
		plo.setLayout(new GridLayout(2, 1));
		plo.add(p);
		Panel pl = new Panel();
		pl.setLayout(new GridLayout(2, 1));
		pl.add(dugme);
		pl.add(reset);
		plo.add(pl);
		add(plo, "South");
		stanje.setFont(font);
		add(stanje, "North");
	}

	private Checkbox korakpokorak;
	private Button slkorak;
	
	private void dodajMeni(){
		MenuBar traka = new MenuBar();
		setMenuBar(traka);
		Menu meni = new Menu("Dubina pretrage");
		traka.add(meni);
		MenuItem stavka = new MenuItem("1");
		stavka.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DUBINA = Integer.parseInt(((MenuItem)e.getSource()).getLabel());
				if (igra != null)
					igra.prekini();
				igra = new Igra(tabla, prvi.igrac, drugi.igrac, stanje);
			}
		});
		meni.add(stavka);
		meni.addSeparator();
		
		stavka = new MenuItem("2");
		stavka.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DUBINA = Integer.parseInt(((MenuItem)e.getSource()).getLabel());
				if (igra != null)
					igra.prekini();
				igra = new Igra(tabla, prvi.igrac, drugi.igrac, stanje);
			}
		});
		meni.add(stavka);
		meni.addSeparator();
		
		stavka = new MenuItem("3");
		stavka.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DUBINA = Integer.parseInt(((MenuItem)e.getSource()).getLabel());
				if (igra != null)
					igra.prekini();
				igra = new Igra(tabla, prvi.igrac, drugi.igrac, stanje);
			}
		});
		meni.add(stavka);
		meni.addSeparator();
		
		stavka = new MenuItem("4");
		stavka.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DUBINA = Integer.parseInt(((MenuItem)e.getSource()).getLabel());
				if (igra != null)
					igra.prekini();
				igra = new Igra(tabla, prvi.igrac, drugi.igrac, stanje);
			}
		});
		meni.add(stavka);
		meni.addSeparator();
		
		stavka = new MenuItem("5");
		stavka.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DUBINA = Integer.parseInt(((MenuItem)e.getSource()).getLabel());
				if (igra != null)
					igra.prekini();
				igra = new Igra(tabla, prvi.igrac, drugi.igrac, stanje);
			}
		});
		meni.add(stavka);
		
		meni = new Menu("Ucitavanje");
		traka.add(meni);
		
		stavka = new MenuItem("Ucitaj iz fajla");
		stavka.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					if (igra != null)
						igra.prekini();
					igra = new Igra(tabla, prvi.igrac, drugi.igrac, stanje);
					igra.ucitajIzFajla();
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		});
		meni.add(stavka);
		
		/*stavka = new MenuItem("Nastavi");
		stavka.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				igra.poteraj();
			}
		});
		meni.add(stavka);*/
	}
	
	public Santorini() {
		super("Santorini");
		rezimKorakPoKorak=false;
		setBounds(300, 300, 800, 500);
		popuniProzor();
		dodajMeni();
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
		
		igra = new Igra(tabla, prvi.igrac, drugi.igrac, stanje);
	}

	public static void main(String[] varg) {
		new Santorini();
	}

}
