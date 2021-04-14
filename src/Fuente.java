import java.util.ArrayList;
import java.util.Collections;

/**
 * La clase que se encarga de la codificacion y decodificacion lineal
 * @author angel
 *
 */
public class Fuente {

	private String texto;
	private ArrayList<String> alfabeto = new ArrayList<String>();
	private int bloque, q;
	
	/**
	 * Constructor
	 * @param texto
	 * @param q
	 */
	public Fuente(String texto, int q) {
		this.texto = texto;
		this.q = q;
	}

	/**
	 * Metodo que configura el alfabeto 
	 * @param simTam
	 */
	public void run(int simTam) {
		this.simbolSplit(this.texto, simTam);
	}

	/**
	 * Separacion de los simbolos y almacenamiento
	 * @param text
	 * @param n
	 */
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

	/**
	 * Calcular el tamaño optimo de bloque para codificar
	 * @return
	 */
	private int calcularBloque() {
		double res = Math.log(this.alfabeto.size())/Math.log(this.q);
		double round = Math.ceil(res);
		String redondeo = String.valueOf(round);
		return Integer.parseInt(redondeo.substring(0, redondeo.lastIndexOf('.')));
	}

	/**
	 * Decodificar un mensaje ya corregido
	 * @param cadena
	 * @return
	 */
	public String decode(ArrayList<Short> cadena) {
		
		StringBuilder mensaje = new StringBuilder("");
		ArrayList<Short> bloque = new ArrayList<Short>();
		
		for(int i=0; i<cadena.size(); i++) {
			
			if(i==0) {
				bloque.add(cadena.get(i));
			}else if(i == cadena.size()-1){
				bloque.add(cadena.get(i));
				mensaje.append(qtoString(this.q, bloque));
				bloque = new ArrayList<Short>();
			}else if(i%this.bloque != 0) {
				bloque.add(cadena.get(i));
			}else {
				mensaje.append(qtoString(this.q, bloque));
				bloque = new ArrayList<Short>();
				bloque.add(cadena.get(i));
			}			
		}
		
		while(mensaje.indexOf("  ") != -1) {
			mensaje.replace(mensaje.indexOf("  "),mensaje.indexOf("  ")+2, "\n");
		}
		
		return mensaje.toString();
	}
	
	/**
	 * Pasar un mensaje en qario a string legible
	 * @param q
	 * @param bloque
	 * @return
	 */
	private String qtoString(int q, ArrayList<Short> bloque) {
		int posicion = qToInt(q, bloque);
		return this.alfabeto.get(posicion);
	}
	
	/**
	 * Pasar un numero qario a un integer
	 * @param q
	 * @param cad
	 * @return
	 */	
	private int qToInt(int q, ArrayList<Short> array) {
		
		int resultado = 0;
		
		Collections.reverse(array);
		
		for(int i=array.size()-1; i>=0; i--) {
			resultado += Math.pow(q, i) * Integer.valueOf(array.get(i));
		}

		return resultado;
		
	}
	
	/**
	 * Pasar un numero int a un qario
	 * @param q
	 * @param num
	 * @return
	 */	
	public static ArrayList<Short> intToQ(int q, int num) {
		
		ArrayList<Short> enBase = new ArrayList<Short>();
		int res;
		while(num!=1) {
			res = num%q;
			enBase.add((short)res);
			num = (num-res)/q;
		}
		enBase.add((short)num);
		Collections.reverse(enBase);
		return enBase;
		
	}
	
	/**
	 * Para codificar el mensaje
	 * Aun no está implementado
	 * @param mensaje
	 * @return
	 */
	@Deprecated
	public String code(String mensaje) {
		// TODO Auto-generated method stub
		return null;
	}

}
