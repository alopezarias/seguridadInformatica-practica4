import java.util.ArrayList;

/**
 * El corrector se encarga de corregir el ruido del mensaje recibido
 * @author angel
 *
 */
public class Corrector {

	private int[][] H;
	private ArrayList<ArrayList<Short>> tablaErrores = new ArrayList<ArrayList<Short>>();
	private ArrayList<ArrayList<Integer>> tablaSindromes = new ArrayList<ArrayList<Integer>>();
	private ArrayList<Short> lista = new ArrayList<Short>();
	private int bloque;
	private static short q;
	
	/**
	 * Constructor de la clase
	 * @param matriz
	 * @param q
	 */
	public Corrector(int[][] matriz, int q_ario) {
		q = (short)q_ario;
		this.bloque = matriz.length+matriz[0].length;
		this.H = calcularMatrizControl(matriz, q);
		rellenarTablaErrores(this.bloque);
	}
	
	/**
	 * Getter de la matriz de control
	 * @return
	 */
	public int[][] getMatrizControl(){
		return this.H;
	}

	/**
	 * Calcula la matriz de control haciendo la traspuesta y la identidad
	 * @param matriz
	 * @param q
	 * @return
	 */
	private int[][] calcularMatrizControl(int[][] matriz, int q) {
		
		int filas, columnas;
		filas = matriz[0].length;
		columnas = matriz.length;
		H = new int[filas][columnas+filas];
		
		for(int i=0; i<H.length; i++) {
			for(int j=0; j<H[0].length; j++) {
				
				if(j<columnas) {
					
					H[i][j] = qNegativo(matriz[j][i], q);
					
				}else {
					if(j-columnas==i)
						H[i][j] = 1;
					else
						H[i][j] = 0;
				}
				
			}
		}
		
		return H;
	}

	/**
	 * Metodo para corregir un mensaje entero
	 * @param mensaje
	 * @return
	 */
	public ArrayList<Short> corregir(String mensaje) {
		
		ArrayList<Short> corregido = new ArrayList<Short>();
		ArrayList<Short> aux = new ArrayList<Short>();
		ArrayList<Short> cadena = new ArrayList<Short>();
		int siguiente = 0;
		
		//mensaje = mensaje.substring(mensaje.indexOf("[")+1, mensaje.lastIndexOf("]"));
		String[] elementos = mensaje.split(",");
		for(String s : elementos) {
			lista.add(Short.valueOf(s));
		}
		
		siguiente = lista.size()-(lista.size()%(this.bloque));
		
		for(int i=0; i<siguiente; i++) {
			
			if(i == 0 || i%this.bloque != 0) {
				cadena.add(lista.get(i));
			}else {
				aux = corregirCadena(cadena);
				for(Short s:aux) {
					corregido.add(s);
				}
				aux = new ArrayList<Short>();
				cadena = new ArrayList<Short>();
				cadena.add(lista.get(i));
			}
			
			if(i==siguiente-1) {
				aux = corregirCadena(cadena);
				for(Short s:aux) {
					corregido.add(s);
				}
				aux = new ArrayList<Short>();
				cadena = new ArrayList<Short>();
			}
		}
		
		for(int j=siguiente; j<lista.size(); j++) {
			corregido.add(lista.get(j));
		}
		
		//System.out.println(corregido.toString());
		return corregido;
	}
	
	/**
	 * Metodo para corregir de 15 en 15 digitos, o la long del bloque
	 * @param cadena
	 * @return
	 */
	private ArrayList<Short> corregirCadena(ArrayList<Short> cadena) {
		ArrayList<Integer> sindrome = calcularSindrome(cadena);
		ArrayList<Short> error = obtenerErrorPatron(sindrome);
		ArrayList<Short> palabra = corregirError(cadena, error);
		return palabra;
	}

	/**
	 * Para calcular el sindrome de un vector
	 * @param mensaje
	 * @return
	 */
	private ArrayList<Integer> calcularSindrome(ArrayList<Short> mensaje) {
		return multiplicarMatriz(this.H, arrayToVector(mensaje), q);
	}

	/**
	 * Para pasar de un string a un vector
	 * @param cadena
	 * @return
	 */
//	private int[][] stringToVector(String cadena) {
//		int [][] vector = new int[cadena.length()][1];
//		
//		for(int i=0; i<cadena.length(); i++) {
//			vector[i][0] = Integer.valueOf(cadena.charAt(i));
//		}
//		
//		return vector;
//	}
	
	private int[][] arrayToVector(ArrayList<Short> array){
		int[][] vector = new int[array.size()][1];
		
		for(int i=0; i<array.size(); i++) {
			vector[i][0] = (int) array.get(i);
		}
		return vector;
	}
	
	/**
	 * Para multiplicar matrices
	 * @param mat1
	 * @param mat2
	 * @return
	 */
	public static ArrayList<Integer> multiplicarMatriz(int[][] mat1, int[][] mat2, short q) {
		int resultado;
		ArrayList<Integer> matriz = new ArrayList<Integer>();
		
		for(int i=0; i<mat1.length; i++) {
			for(int k=0; k<mat2[0].length; k++) {
				resultado = 0;
				for(int j=0; j<mat2.length; j++) {
					resultado += mat1[i][j] * mat2[j][k];
				}
				if(resultado>q)
					matriz.add(resultado%q);
				else
					matriz.add(resultado);
				resultado = 0;
			}
		}
		return matriz;
	}
	
	/**
	 * Pasar un array a un string
	 * @param array
	 * @return
	 */
//	private String arrayToString(ArrayList<Integer> array) {
//		StringBuilder cadena = new StringBuilder("");
//		for(Integer i: array) {
//			cadena.append(i);
//		}
//		return cadena.toString();
//	}
	
	/**
	 * Rellenar la tabla incompleta de sindromes
	 * @param bloque
	 */
	private void rellenarTablaErrores(int bloque) {
		int t = 2;//calcularCapacidadCorrectora(this.H);
		for(int i=0; i<=t; i++) {
			rellenarTablaPeso(i, bloque);
		}
		rellenarTablaSindromes();
	}
	
	/**
	 * Rellenar la tabla segun el peso que le pasemos
	 * @param p
	 * @param b
	 */
	@SuppressWarnings("unchecked")
	private void rellenarTablaPeso(int p, int b) {
		
		ArrayList<Short> error = new ArrayList<Short>();
		ArrayList<Short> e;
		
		for(int a=0; a<b; a++) {
			error.add((short) 0);
		}
		
		e = (ArrayList<Short>) error.clone();
		
		if(p == 0) {
			this.tablaErrores.add(e);
		}else if(p==1){
			for(int i=0; i<b; i++) {
				for(short j=1; j<q; j++) {
					e.set(i, j);
					this.tablaErrores.add(e);
					e = (ArrayList<Short>) error.clone();
				}
			}
		}else if(p==2) {
			for(int i=0; i<b-1; i++) {
				for(int j=i+1; j<b; j++) {
					for(short n=1; n<q; n++) {
						for(short m=1; m<q; m++) {
							e.set(i, n);
							e.set(j, m);
							this.tablaErrores.add(e);
							e = (ArrayList<Short>) error.clone();
						}
					}
				}
			}
		}
	}
	
	/**
	 * Rellenar los sindromes en funcion de los errores patron
	 */
	private void rellenarTablaSindromes() {
		
		for(ArrayList<Short> s: this.tablaErrores) {
			ArrayList<Integer> sindrome = multiplicarMatriz(this.H, arrayToVector(s), q);
			this.tablaSindromes.add(sindrome);
		}
		
		
	}

	/**
	 * Obtener el error patron asociado a un sindrome de menor peso
	 * @param sindrome
	 * @return
	 */
	private ArrayList<Short> obtenerErrorPatron(ArrayList<Integer> sindrome) {
		
		int indice = -1;
		//int indice_aux = 0;
		//boolean encontrado = true;

		for(int i=0; i<tablaSindromes.size(); i++) {
			if(tablaSindromes.get(i).equals(sindrome)) {
				indice = i;
				break;
			}
		}
		
		try {
			
			ArrayList<Short> error = this.tablaErrores.get(indice);
			return error;
		
		}catch(Exception e) {
			System.out.println("Se está buscando el síndrome --> " + sindrome.toString());
		}
		return null;
	}
	
	/**
	 * Corregir el error de un mensaje con su error patron
	 * @param mensaje
	 * @param error
	 * @return
	 */
	private ArrayList<Short> corregirError(ArrayList<Short> mensaje, ArrayList<Short> error) {
		
		//System.out.println(mensaje.size() + " - " + error.size());
		for(int i=0; i<mensaje.size(); i++) {
			
			if(error.get(i) != 0) {
				mensaje.set(i, restarAB(mensaje.get(i),error.get(i)));
			}
		}
		
		return mensaje;
	}
	
	private Short restarAB(short a, short b) {
		
		int resultado = (int)a - (int)b;
		while(resultado<0) {
			resultado += q;
		}
		return (short)(resultado%q);
	}
	
	/**
	 * Pasar a negativo un numero en una base
	 * @param elemento
	 * @param q
	 * @return
	 */
	private int qNegativo(int elemento, int q) {
		
		int valor = -elemento;
		
		while(valor<0) {
			valor +=q;
		}
		
		return valor;
	}
}
