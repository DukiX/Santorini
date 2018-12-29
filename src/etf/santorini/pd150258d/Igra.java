package etf.santorini.pd150258d;
import java.awt.*;

public class Igra extends Thread {

	private Igrac[] igraci = new Igrac[2];
	private int tekIgrac;
	private Tabla tabla;
	private Label oznStanje;
	
	public Igra(Tabla tbl,Igrac prvi, Igrac drugi,Label sta) {
		igraci[0]=prvi;
		igraci[1]=drugi;
		tabla=tbl;
		tabla.prazni();
		tekIgrac=0;
		oznStanje = sta;
		oznStanje.setText("");
		start();
	}

	private String dostigaoTreciNivo(){
		if(igraci[tekIgrac].figure[0].getTrenutnaVisina()==3 || 
				igraci[tekIgrac].figure[1].getTrenutnaVisina()==3){
			return "igrac broj "+tekIgrac;
		}
		return " ";
	}
	
	private String imaLiGde() throws Greska{
		int mojaVisina0 = igraci[tekIgrac].figure[0].getTrenutnaVisina();
		int mojaVisina1 = igraci[tekIgrac].figure[1].getTrenutnaVisina();
		Koordinate k0 =igraci[tekIgrac].figure[0].getKoord();
		Koordinate k1 =igraci[tekIgrac].figure[1].getKoord();
		
		boolean nemaGde0=true;
		boolean nemaGde1=true;
		
		for(int i = k0.getRed()-1;i<=k0.getRed()+1;i++){
			for(int j = k0.getKolona()-1;j<=k0.getKolona()+1;j++){
				if(i<0 || i>4 || j<0 || j>4){
					continue;
				}
				if(i==k0.getRed() && j==k0.getKolona()){
					continue;
				}
				if(tabla.oznaka(i, j).equals("0") || tabla.oznaka(i, j).equals("1") || tabla.oznaka(i, j).equals("2") ||
						tabla.oznaka(i, j).equals("3")){
					int visinaPolja = Integer.parseInt(tabla.oznaka(i, j));
					if(visinaPolja<=mojaVisina0+1){
						nemaGde0=false;	
					}
				}
			}
		}
		
		for(int i = k1.getRed()-1;i<=k1.getRed()+1;i++){
			for(int j = k1.getKolona()-1;j<=k1.getKolona()+1;j++){
				if(i<0 || i>4 || j<0 || j>4){
					continue;
				}
				if(i==k1.getRed() && j==k1.getKolona()){
					continue;
				}
				if(tabla.oznaka(i, j).equals("0") || tabla.oznaka(i, j).equals("1") || tabla.oznaka(i, j).equals("2") ||
						tabla.oznaka(i, j).equals("3")){
					int visinaPolja = Integer.parseInt(tabla.oznaka(i, j));
					if(visinaPolja<=mojaVisina1+1){
						nemaGde1=false;	
					}
				}
			}
		}
		
		if(nemaGde0 && nemaGde1){
			return "igrac broj: "+(1-tekIgrac)+" jer igrac "+tekIgrac+" nema slobodnih poteza";
		}
		
		return " ";
	}
	
	@Override
	public void run(){
		String stanje=" ";
		try{
			oznStanje.setText("Prvi igrac postavlja figure:");
			igraci[0].postaviFigure();
			oznStanje.setText("Drugi igrac postavlja figure:");
			igraci[1].postaviFigure();
			while(!interrupted() && stanje == " "){
				stanje = imaLiGde();
				if(stanje != " ") break;
				oznStanje.setText("Pomeranje figure: igrac "+tekIgrac);
				igraci[tekIgrac].prviDeoPoteza();
				stanje = dostigaoTreciNivo();
				if(stanje != " ") break;
				oznStanje.setText("Gradnja: igrac "+tekIgrac);
				igraci[tekIgrac].drugiDeoPoteza();
				tekIgrac = 1-tekIgrac;
			}
			oznStanje.setText("Pobednik je "+stanje);
		}catch(InterruptedException | Greska g){}
	}
	
	public void prekini(){
		interrupt();
	}
	
}
