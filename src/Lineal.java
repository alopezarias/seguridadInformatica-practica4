
/**
 * La clase que se encarga de la codificación lineal y la decodificación
 * @author angel
 *
 */
public class Lineal {

	private int[][] matriz;
	private int[][] generadora = null;
	
	/**
	 * Constructor de la clase
	 * @param matriz
	 * @param q
	 */
	public Lineal(int[][] matriz, int q) {
		this.matriz = matriz;
		this.generadora = calcularGeneradora(this.matriz);
	}

	/**
	 * Para obtener la matriz generadora
	 * @return
	 */
	public int[][] getGeneradora(){
		if(this.generadora == null) {
			this.generadora = calcularGeneradora(this.matriz);
		}
		return this.generadora;
	}

	/**
	 * Aun no implementado
	 * Para codificar un mensaje
	 * @param code
	 * @return
	 */
	@Deprecated
	public char[] code(String code) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Decodificación del mensaje
	 * @param corregido
	 * @return
	 */
	public String decode(String corregido) {
		
		StringBuilder cadena = new StringBuilder("");
		int bloqueCodificado = matriz.length + matriz[0].length;
		int mensaje = matriz.length;
		
		for(int i=0; i<corregido.length(); i++) {
			
			if(i == 0 || i%bloqueCodificado < mensaje) {
				cadena.append(corregido.charAt(i));
			}
			
		}
		
		return cadena.toString();
	}
	
	/**
	 * Calcular la matriz generadora para poder después codificar algo
	 * @param A
	 * @return
	 */
	public int[][] calcularGeneradora(int[][] A) {
		
		int[][] G = new int[A.length][A.length+A[0].length];
		
		for(int i=0; i<G.length; i++) {
			for(int j=0; j<G[0].length; j++) {
				if(j<A.length) {
					if(i==j) {
						G[i][j] = 1;
					}else {
						G[i][j] = 0;
					}
				}else {
					G[i][j] = A[i][j-A.length];
				}
			}
		}
		
		return G;
	}					
}
