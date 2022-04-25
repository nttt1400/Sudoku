public class SudokuAlgo {
	/*
	 * 
	 * L2 - PO, TP n 2 
	 * Auteur : Nous
	 * 
	 */
	static final int n = 4 ;		// taille des regions
	static final String chaine = "0123456789ABCDEFG";	
	
	/*
	 * Terminologie
	 * 
	 * m est un plateau (de sudoku) si
	 * 	- m est un int [][] ne contenant que des entiers compris entre 0 et 9
	 * 	- m.length = n^2
	 *  - m[i].length = n^2 pour tous les i de 0 a n^2-1
	 *  
	 */

	static String enClair (char [][] m) {
		/*
		 * Prerequis : m est un plateau de sudoku
		 * Resultat : une chaine dont l'affichage permet de visualiser m
		 * 
		 */
		String r = "" ;		
		for (int i = 0; i < n*n ; i++) {
			for (int j = 0; j < n*n ; j++) {
				r = r + m[i][j] + " " ;
				if (j%n == n-1) {r = r + "  ";}
			}
			if (i%n == n-1) {r = r + "\n";}
			r = r + "\n";
		}		
		r = r + " " ;		
		return r ;
	} // enClair
	
	static char [][] aPartirDe (String s) {
		/*
		 * Prerequis : s est une chaine contenant au moins n^4 chiffres decimaux
		 * Resultat : un plateau de sudoku initialise avec les n^4 premiers chiffres
		 * decimaux de s (les chiffres sont consideres comme ranges par lignes).
		 */
		char [][] m = new char [n*n][n*n] ;
		int k = 0 ;
		for (int i = 0; i < m.length ; i++) {
			for (int j = 0; j < m[i].length ; j++) {
				while (chaine.indexOf(s.charAt(k))==-1) {k++;}
				m[i][j] = (char) s.charAt(k) ;
				k++ ;
			}			
		}
		return m ;
	} // aPartirDe
	
	static boolean presentLigne (char [][] m, char v, int i) {
		/*
		 * Prerequis :
		 *  - m est un plateau de sudoku
		 *  - v est compris entre 1 et n^2
		 *  - i est compris entre 0 et n^2-1
		 * Resultat : dans m, v est present dans la ligne i
		 * 
		 */
		for (int j = 0; j < n*n; j++) {
			if (v==m[i][j]) 
				return true;
		}
		return false ; 
	} // presentLigne
	
	static boolean presentColonne (char [][] m, char v, int j) {
		/*
		 * Prerequis :
		 *  - m est un plateau de sudoku
		 *  - v est compris entre 1 et n^2
		 *  - j est compris entre 0 et n^2-1
		 * Resultat : dans m, v est present dans la colonne j
		 * 
		 */
		for (int i=0; i < n*n; i++) {
			if (v == m[i][j]) 
				return true;
		}
		return false ;
	} // presentColonne
	
	static boolean presentRegion (char [][] m, char v, int i, int j) {
		/*
		 * Prerequis :
		 *  - m est un plateau de sudoku
		 *  - v est compris entre 1 et n^2
		 *  - i et j sont compris entre 0 et n^2-1
		 * Resultat : dans m, v est present dans la region contenant la case <i, j>
		 * 
		 */
		int Re1=((i/n)%n);
		int Re2=((j/n)%n);
		for (int a=n*(Re1);a<n*(Re1+1);a++) {
			for (int b=n*(Re2);b<n*(Re2+1);b++) {
				if (v==m[a][b]) 
					return true;
			}
		}
		return false ; 
	} // presentRegion
	
	static boolean [] lesPossiblesEn (char [][] m, int i, int j) {
		/*
		 * Prerequis :
		 *  - m est un plateau de sudoku
		 *  - i et j sont compris entre 0 et n^2-1
		 *  - m[i][j] vaut 0
		 * Resultat : un tableau r de longueur n^2+1 tel que, dans la tranche r[1..n^2]
		 * r[v] indique si v peut etre place en <i, j>
		 * 
		 */
		
		boolean[]r = new boolean[n*n+1];
		for (int a=1;a<=n*n;a++) {
			char v = chaine.charAt(a);
			if (presentRegion(m,v,i,j)==true || presentColonne(m,v,j)==true || presentLigne(m,v,i)==true) { 
				r[a]=false;
			}
			else
				r[a]=true;
		}
		return r ;
	} // lesPossiblesEn
	
	static String enClair (boolean[] t) {
		/*
		 * Prerequis : t.length != 0
		 * Resultat :
		 * une chaine contenant tous les indices i de la tranche [1..t.length-1] tels
		 * que t[i] soit vrai
		 */
		String r = "{" ;
		for (int i = 1; i < t.length; i++) {
			if (t[i]) {r = r + i + ", " ; }
		}
		if (r.length() != 1) {r = r.substring(0, r.length()-2);}
		return r + "}" ;
	} // enClair
	
	static char toutSeul (char [][] m, int i, int j) {
		/*
		 * Prerequis :
		 *  - m est un plateau de sudoku
		 *  - i et j sont compris entre 0 et n^2-1
		 *  - m[i][j] vaut 0
		 * Resultat :
		 *  - v 	si la seule valeur possible pour <i, j> est v
		 *  - -1 	dans les autres cas
		 * 
		 */
		char c= ' ';
		boolean[]r=new boolean[n*n+1];
		int cpt=0;
		for (int a=1;a<=n*n;a++) {
			char v = chaine.charAt(a);
			if (presentRegion(m,v,i,j) == true || presentColonne(m,v,j) == true || presentLigne(m,v,i) == true) 
				r[a] = false;
			else {
				r[a] = true; 
				cpt += 1;
				c = chaine.charAt(a);
			}
			
			
		}
		if (cpt==1) {
			return c;
		}
		else return c='!';
	} // toutSeul
	
	static void essais (String grille) {
		/*
		 * Prerequis : grille represente une grille de sudoku
		 * (les chiffres sont consideres comme ranges par lignes)
		 * 
		 * Effet :
		 * 1) affiche en clair la grille
		 * 2) affecte, tant que faire se peut, toutes les cases pour lesquelles il n'y
		 *    a qu'une seule possibilite
		 * 3) affiche en clair le resultat final
		 */
		char[][] m = aPartirDe(grille);
		System.out.println("Probleme\n\n"+enClair(m));
		
		int cpt=0;
		int etape=0;
		do {
			cpt=0;
			for (int a=0;a<n*n;a++) {
			
				for(int b=0;b<n*n;b++) {
					char c=toutSeul(m,a,b);
					if(m[a][b]=='0') {
						if(c!='!') {
							m[a][b]=c;
							cpt+=1;
							etape+=1;
						}
					}
				}
			}
		}
		while(cpt!=0);
		System.out.println("Il se peut qu'on ait avance\n\n"+enClair(m) + " on a rajouté "+ etape +" nombres");
	} // essais
	
	public static void main(String[] args) {
		String grille1 = 
			"040 001 006 \n" +
			"007 900 800 \n" +
			"190 086 074 \n" +
			"            \n" +
			"200 690 010 \n" +
			"030 405 090 \n" +
			"060 017 003 \n" +
			"            \n" +
			"910 750 042 \n" +
			"008 002 700 \n" +
			"400 300 080   " ;
		String grille2 = 
			"030 000 006 \n" +
			"000 702 300 \n" +
			"104 038 000 \n" +
			"            \n" +
			"300 020 810 \n" +
			"918 000 265 \n" +
			"062 050 007 \n" +
			"            \n" +
			"000 140 708 \n" +
			"001 209 000 \n" +
			"800 000 020   " ;
		
		String grille3=
				  "9010G850030BA70D\r\n"
				+ "00A00000D52E8B00\r\n"
				+ "G50706000C0029F3\r\n"
				+ "006823DA9400051C\r\n"
				+ "A003B000100000DF\r\n"
				+ "208C000006000090\r\n"
				+ "1006007309D0C2AB\r\n"
				+ "000500C2000A3080\r\n"
				+ "0B09A0007G001000\r\n"
				+ "E6GD0B403A009008\r\n"
				+ "0A0000G00000D306\r\n"
				+ "5800000F000D4007\r\n"
				+ "6D5000A127CGB800\r\n"
				+ "827G00B00010F0C4\r\n"
				+ "004B3C2D00000600\r\n"
				+ "309AE0800B4F0D01";
		essais(grille3);
	}	
}