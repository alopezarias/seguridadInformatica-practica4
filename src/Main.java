import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

	private static Fuente fuente;
	private static Corrector corrector;
	private static Lineal lineal;
	private static String mensaje;
	
	public static Scanner in = new Scanner(System.in);
	
	public static void main(String[] args) {
		
		System.out.println(inicioPrograma());
		String texto, matriz;
		int[][] mat;
		int q;
		try {
			texto = introducirArchivo("Fuente");
			texto = texto.substring(texto.indexOf("\"") + 1, texto.lastIndexOf("\""));
			matriz = introducirArchivo("Matriz");
			mat = stringAMatriz(matriz);
			mensaje = introducirArchivo("Mensaje");
			q = 2;//escogerSplit();
			fuente = new Fuente(texto, q);
			lineal = new Lineal(mat, q);
			corrector = new Corrector(mat, q);
			int n = 1;//escogerSplit();
			fuente.run(n);
			System.out.println(matrizToString(corrector.getMatrizControl()));
			opciones();
			System.out.println(finPrograma());
		} catch (IOException e) {
			e.printStackTrace();
		}	
//		try {
//			lineal = new Lineal(stringAMatriz(introducirArchivo()), 2);
//			int[][] mat = lineal.getGeneradora();
//			System.out.println(matrizToString(mat));
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
//		int[][] mat1 = new int[2][3];
//		int[][] mat2 = new int[3][2];
//		int var = 1;
//		
//		for(int i=0; i<mat1.length; i++) {
//			for(int j=0; j<mat1[0].length; j++) {
//				mat1[i][j] = var++;
//			}
//		}
//		var = 1;
//		for(int i=0; i<mat2.length; i++) {
//			for(int j=0; j<mat2[0].length; j++) {
//				mat2[i][j] = var++;
//			}
//		}
//		
//		ArrayList<Integer> res = Corrector.multiplicarMatriz(mat1, mat2);
//		System.out.println(res.toString());

	}
	
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
	
	private static int[][] stringAMatriz(String matriz) {
		
		int[][] resultado = null;
		String fila;
		String[] columnas;
		String mat = matriz.substring(matriz.indexOf("{")+1, matriz.lastIndexOf("}"));
		String[] filas = mat.split(";");
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

	private static String inicioPrograma() {
		StringBuffer cad = new StringBuffer("");
		cad.append("CODIFICACION BINARIA LINEAL CON RUIDO");
		cad.append("\n");
		return cad.toString();
	}
	
	private static String introducirArchivo(String asunto) throws IOException {
		System.out.println("\nINTRODUCE LA RUTA ABSOLUTA DEL ARCHIVO (<"+asunto+">): \n");
		String ruta = in.nextLine();
		String linea;
		StringBuffer contenido = new StringBuffer("");

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

		return contenido.toString();
	}
	
	private static int escogerSplit() {
		System.out.println("ESCOGE LA Q DEL CÓDIGO Q-ARIO: \n");
		String l = in.nextLine();
		boolean b = false;
		while (!b) {
			try {
				Integer.parseInt(l);
				b = true;
			} catch (NumberFormatException excepcion) {
				System.out.println("INTRODUCE UN NUMERO, POR FAVOR: \n");
				l = in.nextLine();
			}
		}

		return Integer.valueOf(l);
	}
	
	public static void opciones() {
		imprimirMenu();
		String menu = in.nextLine();
		int opc = Integer.valueOf(menu);

		if (opc != 0) {
			if (opc == 1) {
				System.out.println(lineal.code(fuente.code(mensaje)));
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
	
	private static void imprimirMenu() {
		System.out.println("------------");
		System.out.println("MENU GENERAL");
		System.out.println("------------");
		System.out.println("1) CODIFICAR");
		System.out.println("2) DECODIFICAR");
		System.out.println("0) PARA SALIR");
		System.out.println("-------------");
	}
	
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
