package etf.santorini.pd150258d;

public class Covek extends Igrac {

	public Covek(String ozn, Tabla tbl) {
		super(ozn, tbl);
	}

	@Override
	public void postaviFigure() throws InterruptedException, Greska {
		Koordinate koor = tabla.uzmiPritisnuto();
		while(!tabla.oznaka(koor.getRed(), koor.getKolona()).equals("0")){
			koor = tabla.uzmiPritisnuto();
		}
		figure[0] = new Figura(koor,oznaka+"F0");
		tabla.postaviOznaku(koor.getRed(),koor.getKolona(), figure[0].getOznaka()+", Vis: "+figure[0].getTrenutnaVisina());
		
		koor = tabla.uzmiPritisnuto();
		while(!tabla.oznaka(koor.getRed(), koor.getKolona()).equals("0")){
			koor = tabla.uzmiPritisnuto();
		}
		figure[1] = new Figura(koor,oznaka+"F1");
		tabla.postaviOznaku(koor.getRed(),koor.getKolona(), figure[1].getOznaka()+", Vis: "+figure[1].getTrenutnaVisina());
	}

	@Override
	public void prviDeoPoteza() throws InterruptedException, Greska {
		Koordinate izabranaFig = tabla.uzmiPritisnuto();
		boolean a;
		boolean b;
		while(true){
			a=true;
			b=true;
			try{
				a = !tabla.oznaka(izabranaFig.getRed(), izabranaFig.getKolona()).substring(0, 4).equals(figure[0].getOznaka().substring(0, 4));
				b = !tabla.oznaka(izabranaFig.getRed(), izabranaFig.getKolona()).substring(0, 4).equals(figure[1].getOznaka().substring(0, 4));
			}catch(Exception e){}
			if(!a || !b) break;
			izabranaFig = tabla.uzmiPritisnuto();
		}
		
		izabranaFigura = dohvatiFiguru(izabranaFig);
		
		Koordinate novaPozicija = tabla.uzmiPritisnuto();
		
		while(dalekaPozicija(novaPozicija, izabranaFig) ||
				!dozvoljena(novaPozicija)){
			
			a=true;
			b=true;
			try{
				a = !tabla.oznaka(novaPozicija.getRed(), novaPozicija.getKolona()).substring(0, 4).equals(figure[0].getOznaka().substring(0, 4));
				b = !tabla.oznaka(novaPozicija.getRed(), novaPozicija.getKolona()).substring(0, 4).equals(figure[1].getOznaka().substring(0, 4));
			}catch(Exception e){}
			
			if(!a || !b) {
				izabranaFig=novaPozicija;
				izabranaFigura = dohvatiFiguru(izabranaFig);
			}
			
			novaPozicija = tabla.uzmiPritisnuto();
		}
		
		tabla.postaviOznaku(izabranaFig.getRed(),izabranaFig.getKolona(), Integer.toString(izabranaFigura.getTrenutnaVisina()));
		izabranaFigura.setKoord(novaPozicija);
		izabranaFigura.setTrenutnaVisina(Integer.parseInt(tabla.oznaka(novaPozicija.getRed(), novaPozicija.getKolona())));
		tabla.postaviOznaku(novaPozicija.getRed(), novaPozicija.getKolona(), izabranaFigura.getOznaka()+", Vis: "+izabranaFigura.getTrenutnaVisina());
	}
	
	@Override
	public void drugiDeoPoteza()throws InterruptedException, Greska{
		Koordinate izabranoPolje = tabla.uzmiPritisnuto();
		while(dalekaPozicija(izabranoPolje, izabranaFigura.getKoord())||
				!dozvoljenoZaGradnju(izabranoPolje)){
			izabranoPolje = tabla.uzmiPritisnuto();
		}
		int staraVisina = Integer.parseInt(tabla.oznaka(izabranoPolje.getRed(), izabranoPolje.getKolona()));
		if(staraVisina==3){
			tabla.postaviOznaku(izabranoPolje.getRed(), izabranoPolje.getKolona(), "K");
		}else{
			int novaVisina = staraVisina+1;
			tabla.postaviOznaku(izabranoPolje.getRed(), izabranoPolje.getKolona(), Integer.toString(novaVisina));
		}
	}
	

}
