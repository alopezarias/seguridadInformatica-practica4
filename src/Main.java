import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * Esta clase se encarga de recoger los datos del ejercicio y de mostrarlos
 * por pantalla
 * @author angel
 *
 */
public class Main {

	private static Fuente fuente;
	private static Corrector corrector;
	private static Lineal lineal;
	private static String mensaje;
	public static Scanner in = new Scanner(System.in);
	private static String data;
	
	public static void main(String[] args) {
		
		System.out.println(inicioPrograma());
		String texto, matriz;
		int[][] mat;
		int q;
		try {
			introducirArchivo("resources/data");
			texto = seleccionarDelArchivo("alfabeto");//introducirArchivo("Fuente");
			//texto = texto.substring(texto.indexOf("\"") + 1, texto.lastIndexOf("\""));
			matriz = seleccionarDelArchivo("matriz");//introducirArchivo("Matriz");
			mat = stringAMatriz(matriz);
			mensaje = seleccionarDelArchivo("mensaje");//introducirArchivo("Mensaje");
			q = Integer.valueOf(seleccionarDelArchivo("q"));//7;//escogerSplit("Q-ario");
			fuente = new Fuente(texto, q);
			lineal = new Lineal(mat, q);
			corrector = new Corrector(mat, q);
			int n = 1;//escogerSplit();
			fuente.run(n);
			//System.out.println(matrizToString(corrector.getMatrizControl()));
			opciones();
			System.out.println(finPrograma());
		} catch (IOException e) {
			e.printStackTrace();
		}	
		
		//System.out.println(Fuente.intToQ(11, 126).toString());
	}
	
	public static String seleccionarDelArchivo(String id) {

		switch (id) {
		case "alfabeto":
			return data.substring(data.indexOf("\"") + 1, data.indexOf("\"", data.indexOf("\"") + 1));
		case "q":
			return data.substring(data.indexOf("p=") + 2, data.indexOf("\n", data.indexOf("p=") + 1));
		case "mensaje":
			return data.substring(data.indexOf("lista=[") + 7, data.lastIndexOf("]"));
		case "matriz":
			return data.substring(data.indexOf("{") + 1, data.indexOf("}"));
		case "d":
			return data.substring(data.indexOf("d=") + 2, data.indexOf("\n", data.indexOf("d=") + 1));
		default:
			return null;
		}
	}
	
	/**
	 * Pasar una matriz a un string simple
	 * @param mat
	 * @return
	 */
	public static String matrizToString(int [][] mat) {
		
		StringBuilder matriz = new StringBuilder("");
		
		for(int i=0; i<mat.length; i++) {
			for(int j=0; j<mat[0].length; j++) {
				matriz.append(mat[i][j]);
			}
			matriz.append("],\n");
		}
		
		return matriz.toString();
	}
	
	/**
	 * Pasar un string a una matriz
	 * @param matriz
	 * @return
	 */
	private static int[][] stringAMatriz(String matriz) {
		
		int[][] resultado = null;
		String fila;
		String[] columnas;
		//String mat = matriz.substring(matriz.indexOf("{")+1, matriz.lastIndexOf("}"));
		String[] filas = matriz.split(";");
		for(int i=0; i<filas.length; i++) {
			fila = filas[i].substring(filas[i].indexOf("[")+1, filas[i].lastIndexOf("]"));
			columnas = fila.split(",");
			
			if(i == 0) {
				resultado = new int[filas.length][columnas.length];
			}
			
			for(int j=0; j<columnas.length; j++) {
				resultado[i][j] = Integer.valueOf(columnas[j]);
			}
		}
		return resultado;
	}

	/**
	 * Cadena de bienvenida
	 * @return
	 */
	private static String inicioPrograma() {
		StringBuffer cad = new StringBuffer("");
		cad.append("CODIFICACION BINARIA LINEAL CON RUIDO");
		cad.append("\n");
		return cad.toString();
	}
	
	/**
	 * Metodo para introducir la ruta del archivo y capturar el texto del mismo
	 * 
	 * @return texto contenido en el archivo
	 * @throws IOException
	 */
	private static void introducirArchivo(String ruta) throws IOException {
		System.out.println("\nINTRODUCE LA RUTA ABSOLUTA DEL ARCHIVO CON EL TEXTO:\n");
		// String ruta = "../resources/data";//in.nextLine();
		String linea;
		StringBuffer contenido = new StringBuffer("");
		//String texto;

		try {
			FileReader f = new FileReader(ruta, StandardCharsets.UTF_8);
			BufferedReader b = new BufferedReader(f);
			while ((linea = b.readLine()) != null) {

				contenido.append(linea);
				contenido.append("\n");

			}
			b.close();
		} catch (IOException e) {
			throw e;
		}

		data = contenido.toString();
	}
	
	/**
	 * Nos permite recoger un numero por pantalla, asegurandonos de que es un
	 * numero lo que se introduce
	 * @return
	 */
//	private static int escogerSplit() {
//		System.out.println("ESCOGE LA Q DEL CÓDIGO Q-ARIO: \n");
//		String l = in.nextLine();
//		boolean b = false;
//		while (!b) {
//			try {
//				Integer.parseInt(l);
//				b = true;
//			} catch (NumberFormatException excepcion) {
//				System.out.println("INTRODUCE UN NUMERO, POR FAVOR: \n");
//				l = in.nextLine();
//			}
//		}
//
//		return Integer.valueOf(l);
//	}
	
	/**
	 * Nos permite escoger entre las diferentes opciones del programa
	 */
	public static void opciones() {
		imprimirMenu();
		String menu = in.nextLine();
		int opc = Integer.valueOf(menu);

		if (opc != 0) {
			if (opc == 1) {
				//System.out.println(lineal.code(fuente.code(mensaje)));
			}
			else if (opc == 2) {
				System.out.println(fuente.decode(lineal.decode(corrector.corregir(mensaje))));
			}
			else {
				System.out.println(" - OPCIÓN INVÁLIDA!! ");
				opciones();
			}
		} else {
			System.exit(0);
		}
	}
	
	/**
	 * Imprime el menu del programa por consola
	 */
	private static void imprimirMenu() {
		System.out.println("------------");
		System.out.println("MENU GENERAL");
		System.out.println("------------");
		System.out.println("1) CODIFICAR");
		System.out.println("2) DECODIFICAR");
		System.out.println("0) PARA SALIR");
		System.out.println("-------------");
	}
	
	/**
	 * Mensaje de despedida del programa
	 * @return
	 */
	private static String finPrograma() {
		StringBuffer cad = new StringBuffer("");
		cad.append("\n");
		cad.append("--------------------------\n");
		cad.append("- FIN EJECUCIÓN PROGRAMA -");
		cad.append("\n");
		cad.append("--------------------------\n");
		cad.append("\n");
		return cad.toString();
	}
}
