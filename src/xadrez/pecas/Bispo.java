package xadrez.pecas;

import jogoTabuleiro.Posicao;
import jogoTabuleiro.Tabuleiro;
import xadrez.Cor;
import xadrez.PecaXadrez;

public class Bispo extends PecaXadrez {

	public Bispo(Tabuleiro tabuleiro, Cor cor) {
		super(tabuleiro, cor);
	}
	
	public String toString() {
		return "B";
	}

	@Override
	public boolean[][] movimentosPossiveis() {
		boolean [][] mat = new boolean [getTabuleiro().getLinhas()][getTabuleiro().getColunas()];
		Posicao pAux = new Posicao(0, 0);
		
		//Noroeste (Diagonal acima à esquerda)
		pAux.setValores(posicao.getLinha() - 1, posicao.getColuna() - 1);
		while(getTabuleiro().posicaoExistente(pAux) && !getTabuleiro().posicaoPreenchida(pAux)) {
			mat[pAux.getLinha()][pAux.getColuna()] = true;
			pAux.setValores(pAux.getLinha() - 1, pAux.getColuna() - 1);
		}
		if(getTabuleiro().posicaoExistente(pAux) && seExistePecaOponente(pAux)) {
			mat[pAux.getLinha()][pAux.getColuna()] = true;
		}
		
		// Nordeste (Diagonal acima à direita)
		pAux.setValores(posicao.getLinha() - 1, posicao.getColuna() + 1);
		while(getTabuleiro().posicaoExistente(pAux) && !getTabuleiro().posicaoPreenchida(pAux)) {
			mat[pAux.getLinha()][pAux.getColuna()] = true;
			pAux.setValores(pAux.getLinha() - 1, pAux.getColuna() + 1);
		}
		if(getTabuleiro().posicaoExistente(pAux) && seExistePecaOponente(pAux)) {
			mat[pAux.getLinha()][pAux.getColuna()] = true;
		}
		
		// Sudeste (Diagonal abaixo à direita
		pAux.setValores(posicao.getLinha() + 1, posicao.getColuna() + 1);
		while(getTabuleiro().posicaoExistente(pAux) && !getTabuleiro().posicaoPreenchida(pAux)) {
			mat[pAux.getLinha()][pAux.getColuna()] = true;
			pAux.setValores(pAux.getLinha() + 1, pAux.getColuna() + 1);
		}
		if(getTabuleiro().posicaoExistente(pAux) && seExistePecaOponente(pAux)) {
			mat[pAux.getLinha()][pAux.getColuna()] = true;
		}
		
		// Sudoeste (Diagonal abaixo à esquerda
		pAux.setValores(posicao.getLinha() + 1, posicao.getColuna() - 1);
		while(getTabuleiro().posicaoExistente(pAux) && !getTabuleiro().posicaoPreenchida(pAux)) {
			mat[pAux.getLinha()][pAux.getColuna()] = true;
			pAux.setValores(pAux.getLinha() + 1, pAux.getColuna() - 1);
		}
		if(getTabuleiro().posicaoExistente(pAux) && seExistePecaOponente(pAux)) {
			mat[pAux.getLinha()][pAux.getColuna()] = true;
		}
		
		return mat;
	}

}