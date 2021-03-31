import java.util.ArrayList;

public class Corrector {

	private int[][] H, matriz;
	private ArrayList<String> tablaErrores = new ArrayList<String>();
	private ArrayList<String> tablaSindromes = new ArrayList<String>();
	private ArrayList<Integer> lista = new ArrayList<Integer>();
	private int q, bloque;
	
	
	public Corrector(int[][] matriz, int q) {
		this.matriz = matriz;
		this.bloque = matriz.length+matriz[0].length;
		this.H = calcularMatrizControl(matriz, q);
		this.q = q;
		rellenarTablaErrores(this.bloque);
	}
	
	public int[][] getMatrizControl(){
		return this.H;
	}

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
	
	private int qNegativo(int elemento, int q) {
		//elemento = -elemento;
//		if(elemento == 1) {
//			elemento = 0;
//		}else {
//			elemento = 1;
//		}
		return elemento%q;
	}

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
	
	private String corregirCadena(String cadena) {
		ArrayList<Integer> sindrome = calcularSindrome(cadena);
		ArrayList<Integer> error = obtenerErrorPatron(sindrome);
		String palabra = corregirError(cadena, error);
		return palabra;
	}

	private ArrayList<Integer> calcularSindrome(String mensaje) {
		return multiplicarMatriz(this.H, stringToVector(mensaje));
	}

	private int[][] stringToVector(String cadena) {
		int [][] vector = new int[cadena.length()][1];
		
		for(int i=0; i<cadena.length(); i++) {
			vector[i][0] = Integer.valueOf(cadena.charAt(i));
		}
		
		return vector;
	}
	
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
	
	private String arrayToString(ArrayList<Integer> array) {
		StringBuilder cadena = new StringBuilder("");
		for(Integer i: array) {
			cadena.append(i);
		}
		return cadena.toString();
	}
	
	private void rellenarTablaErrores(int bloque) {
		int t = 2;//calcularCapacidadCorrectora(this.H);
		for(int i=0; i<=t; i++) {
			rellenarTablaPeso(i, bloque);
		}
		rellenarTablaSindromes();
	}
	
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
	
	private void rellenarTablaSindromes() {
		
		for(String s: this.tablaErrores) {
			ArrayList<Integer> sindrome = multiplicarMatriz(this.H, stringToVector(s));
			this.tablaSindromes.add(arrayToString(sindrome));
		}
		
		
	}

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
