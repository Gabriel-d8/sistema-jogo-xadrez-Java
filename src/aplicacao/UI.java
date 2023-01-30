package aplicacao;

import xadrez.PecaXadrez;

public class UI {
	
	public static void exibirTabuleiro(PecaXadrez[][] pecas) {
		for(int i=0; i<pecas.length; i++) {
			System.out.print((8 - i) + " ");
			for(int j=0; j<pecas.length; j++) {
				exibirPeca(pecas[i][j]);
			}
			System.out.println();                // Quebra de linha 
		}
		System.out.println("  a b c d e f g h");
	}
	
	private static void exibirPeca(PecaXadrez peca) {
		if(peca == null) {
			System.out.print("-");
		} else {
			System.out.print(peca);
		}
		System.out.print(" ");          // Feita para que as peças não fiquem grudadas umas às outras
	}

}