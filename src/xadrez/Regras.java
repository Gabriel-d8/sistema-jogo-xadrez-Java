package xadrez;

import jogoTabuleiro.Tabuleiro;
import xadrez.pecas.Rei;
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
	
	private void novoLocalDaPeca(char coluna, int linha, PecaXadrez peca) {
		tabuleiro.localPeca(peca, new XadrezPosicao(coluna, linha).posicionar());
	}
	
	private void iniciar() {
		novoLocalDaPeca('b', 6, new Rei(tabuleiro, Cor.PRETO));				  //TESTE	
		novoLocalDaPeca('e', 3, new Torre(tabuleiro, Cor.BRANCO));            //TESTE
	}
}