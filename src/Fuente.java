import java.util.ArrayList;

public class Fuente {

	private String texto;
	private ArrayList<String> alfabeto = new ArrayList<String>();
	private int bloque, q;
	
	public Fuente(String texto, int q) {
		this.texto = texto;
		this.q = q;
	}

	public void run(int simTam) {
		this.simbolSplit(this.texto, simTam);
	}

	private void simbolSplit(String text, int n) {

		StringBuffer finalText = new StringBuffer("");

		for (int i = 1; i <= text.length(); i++) {

			finalText.append(texto.charAt(i - 1));
			if (i != 0 && i % n == 0 && i != texto.length()) {
				finalText.append("\t");
			}

		}

		this.texto = finalText.toString();
		finalText = new StringBuffer("");

		String[] simbDif = this.texto.split("\t");

		for (String simb : simbDif) {
			this.alfabeto.add(simb);
		}
		
		this.bloque = calcularBloque();
	}

	private int calcularBloque() {
		double res = Math.log(this.alfabeto.size())/Math.log(this.q);
		double round = Math.ceil(res);
		String redondeo = String.valueOf(round);
		return Integer.parseInt(redondeo.substring(0, redondeo.lastIndexOf('.')));
	}

	public String decode(String cadena) {
		
		StringBuilder mensaje = new StringBuilder(""), bloque = new StringBuilder("");
		
		for(int i=0; i<cadena.length(); i++) {
			
			if(i==0) {
				bloque.append(cadena.charAt(i));
			}else if(i == cadena.length()-1){
				bloque.append(cadena.charAt(i));
				mensaje.append(qtoString(this.q, bloque.toString()));
				bloque = new StringBuilder("");
			}else if(i%this.bloque != 0) {
				bloque.append(cadena.charAt(i));
			}else {
				mensaje.append(qtoString(this.q, bloque.toString()));
				bloque = new StringBuilder("");
				bloque.append(cadena.charAt(i));
			}			
		}
		
		while(mensaje.indexOf("  ") != -1) {
			mensaje.replace(mensaje.indexOf("  "),mensaje.indexOf("  ")+2, "\n");
		}
		
		return mensaje.toString();
	}
	
	private String qtoString(int q, String bloque) {
		int posicion = qToInt(q, bloque);
		return this.alfabeto.get(posicion);
	}
	
	private int qToInt(int q, String cad) {
		
		int resultado = 0;
		StringBuilder cadena = new StringBuilder(cad);
		cadena.reverse();
		
		for(int i=0; i<cadena.length(); i++) {
			if(cadena.charAt(i) == '1') {
				resultado += Math.pow(q, i);
			}
		}
		return resultado;
	}
	
	@Deprecated
	public String code(String mensaje) {
		// TODO Auto-generated method stub
		return null;
	}

}
