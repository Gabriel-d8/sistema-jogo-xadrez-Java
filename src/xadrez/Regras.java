package xadrez;

import jogoTabuleiro.Posicao;
import jogoTabuleiro.Tabuleiro;
import xadrez.pecas.Torre;

public class Regras {

	private Tabuleiro tabuleiro;
	
	public Regras() {
		tabuleiro = new Tabuleiro(8, 8);
		iniciar();
	}
	
	public PecaXadrez [][] getPecas(){
		PecaXadrez [][] matriz = new PecaXadrez [tabuleiro.getLinhas()][tabuleiro.getColunas()];
		for(int i=0; i<tabuleiro.getLinhas(); i++) {
			for(int j=0; j<tabuleiro.getColunas(); j++) {
				matriz[i][j] = (PecaXadrez) tabuleiro.peca(i, j);
			}
		}
		return matriz;
	}
	
	private void iniciar() {
		tabuleiro.localPeca(new Torre(tabuleiro, Cor.BRANCO), new Posicao(2, 1));    // Teste: Introdu��o de pe�a no tabuleiro
	}
}