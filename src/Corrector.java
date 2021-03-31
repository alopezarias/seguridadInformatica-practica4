import java.util.ArrayList;

/**
 * El corrector se encarga de corregir el ruido del mensaje recibido
 * @author angel
 *
 */
public class Corrector {

	private int[][] H;
	private ArrayList<String> tablaErrores = new ArrayList<String>();
	private ArrayList<String> tablaSindromes = new ArrayList<String>();
	private ArrayList<Integer> lista = new ArrayList<Integer>();
	private int bloque;
	
	/**
	 * Constructor de la clase
	 * @param matriz
	 * @param q
	 */
	public Corrector(int[][] matriz, int q) {
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
					
					H[i][j] = qNegativo(matriz[j][i], 2);
					
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
	 * Pasar a negativo un numero en una base
	 * @param elemento
	 * @param q
	 * @return
	 */
	private int qNegativo(int elemento, int q) {
		//elemento = -elemento;
//		if(elemento == 1) {
//			elemento = 0;
//		}else {
//			elemento = 1;
//		}
		return elemento%q;
	}

	/**
	 * Metodo para corregir un mensaje entero
	 * @param mensaje
	 * @return
	 */
	public String corregir(String mensaje) {
		
		StringBuilder corregido = new StringBuilder("");
		StringBuilder cadena = new StringBuilder("");
		int siguiente = 0;
		
		mensaje = mensaje.substring(mensaje.indexOf("[")+1, mensaje.lastIndexOf("]"));
		String[] elementos = mensaje.split(",");
		for(String s : elementos) {
			lista.add(Integer.valueOf(s));
		}
		
		siguiente = lista.size()-(lista.size()%(this.bloque));
		
		for(int i=0; i<siguiente; i++) {
			
			if(i == 0 || i%this.bloque != 0) {
				cadena.append(lista.get(i));
			}else {
				corregido.append(corregirCadena(cadena.toString()));
				cadena = new StringBuilder("");
				cadena.append(lista.get(i));
			}
			
			if(i==siguiente-1) {
				corregido.append(corregirCadena(cadena.toString()));
				cadena = new StringBuilder("");
			}
		}
		
		for(int j=siguiente; j<lista.size(); j++) {
			corregido.append(lista.get(j));
		}
		
		return corregido.toString();
	}
	
	/**
	 * Metodo para corregir de 15 en 15 digitos, o la long del bloque
	 * @param cadena
	 * @return
	 */
	private String corregirCadena(String cadena) {
		ArrayList<Integer> sindrome = calcularSindrome(cadena);
		ArrayList<Integer> error = obtenerErrorPatron(sindrome);
		String palabra = corregirError(cadena, error);
		return palabra;
	}

	/**
	 * Para calcular el sindrome de un vector
	 * @param mensaje
	 * @return
	 */
	private ArrayList<Integer> calcularSindrome(String mensaje) {
		return multiplicarMatriz(this.H, stringToVector(mensaje));
	}

	/**
	 * Para pasar de un string a un vector
	 * @param cadena
	 * @return
	 */
	private int[][] stringToVector(String cadena) {
		int [][] vector = new int[cadena.length()][1];
		
		for(int i=0; i<cadena.length(); i++) {
			vector[i][0] = Integer.valueOf(cadena.charAt(i));
		}
		
		return vector;
	}
	
	/**
	 * Para multiplicar matrices
	 * @param mat1
	 * @param mat2
	 * @return
	 */
	public static ArrayList<Integer> multiplicarMatriz(int[][] mat1, int[][] mat2) {
		int resultado;
		ArrayList<Integer> matriz = new ArrayList<Integer>();
		
		for(int i=0; i<mat1.length; i++) {
			for(int k=0; k<mat2[0].length; k++) {
				resultado = 0;
				for(int j=0; j<mat2.length; j++) {
					resultado += mat1[i][j] * mat2[j][k];
				}
				matriz.add(resultado%2);
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
	private String arrayToString(ArrayList<Integer> array) {
		StringBuilder cadena = new StringBuilder("");
		for(Integer i: array) {
			cadena.append(i);
		}
		return cadena.toString();
	}
	
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
	private void rellenarTablaPeso(int p, int b) {
		
		StringBuilder error = new StringBuilder("000000000000000");
		
		if(p == 0) {
			this.tablaErrores.add(error.toString());
		}else if(p==1){
			for(int i=0; i<b; i++) {
				error.setCharAt(i, '1');
				this.tablaErrores.add(error.toString());
				error = new StringBuilder("000000000000000");
			}
		}else if(p==2) {
			for(int i=0; i<b-1; i++) {
				for(int j=i+1; j<b; j++) {
					error.setCharAt(i, '1');
					error.setCharAt(j, '1');
					this.tablaErrores.add(error.toString());
					error = new StringBuilder("000000000000000");
				}
			}
			
		}
		
	}
	
	/**
	 * Rellenar los sindromes en funcion de los errores patron
	 */
	private void rellenarTablaSindromes() {
		
		for(String s: this.tablaErrores) {
			ArrayList<Integer> sindrome = multiplicarMatriz(this.H, stringToVector(s));
			this.tablaSindromes.add(arrayToString(sindrome));
		}
		
		
	}

	/**
	 * Obtener el error patron asociado a un sindrome de menor peso
	 * @param sindrome
	 * @return
	 */
	private ArrayList<Integer> obtenerErrorPatron(ArrayList<Integer> sindrome) {
		
		StringBuilder s = new StringBuilder("");
		int indice = -1;
		for(Integer i:sindrome) {
			s.append(i);
		}
		String sindromeABuscar = s.toString();
		for(String sind : this.tablaSindromes) {
			if(sind.compareTo(sindromeABuscar) == 0) {
				indice = this.tablaSindromes.indexOf(sind);
				break;
			}
		}
		try {
		String error = this.tablaErrores.get(indice);
		ArrayList<Integer> Error = new ArrayList<Integer>();
		
		for(int i=0; i<error.length(); i++) {
			Error.add(Integer.valueOf(String.valueOf(error.charAt(i))));
		}
		
		return Error;
		}catch(Exception e) {
			System.out.println("Se está buscando el síndrome --> " + s.toString());
		}
		return null;
	}
	
	/**
	 * Corregir el error de un mensaje con su error patron
	 * @param mensaje
	 * @param error
	 * @return
	 */
	private String corregirError(String mensaje, ArrayList<Integer> error) {
		
		StringBuilder cadena = new StringBuilder(mensaje);
		
		for(int i=0; i<mensaje.length(); i++) {
			if(error.get(i) == 1) {
				if(cadena.charAt(i) == '0')
					cadena.setCharAt(i, '1');
				else if(cadena.charAt(i) == '1')
					cadena.setCharAt(i, '0');
			}
		}
		
		return cadena.toString();
	}
}
