package xadrez.pecas;

import jogoTabuleiro.Posicao;
import jogoTabuleiro.Tabuleiro;
import xadrez.Cor;
import xadrez.PecaXadrez;

public class Torre extends PecaXadrez {

	public Torre(Tabuleiro tabuleiro, Cor cor) {
		super(tabuleiro, cor);
	}

	@Override
	public String toString() {
		return "T";
	}
	
	@Override
	public boolean[][] movimentosPossiveis() {
		boolean [][] mat = new boolean [getTabuleiro().getLinhas()][getTabuleiro().getColunas()];
		Posicao pAux = new Posicao(0, 0);
		
		// Acima 
		pAux.setValores(posicao.getLinha() - 1, posicao.getColuna());
		while(getTabuleiro().posicaoExistente(pAux) && !getTabuleiro().posicaoPreenchida(pAux)) {
			mat[pAux.getLinha()][pAux.getColuna()] = true;
			pAux.setLinha(pAux.getLinha() - 1);
		}
		if(getTabuleiro().posicaoExistente(pAux) && seExistePecaOponente(pAux)) {
			mat[pAux.getLinha()][pAux.getColuna()] = true;
		}
		
		// À esquerda
		pAux.setValores(posicao.getLinha(), posicao.getColuna() - 1);
		while(getTabuleiro().posicaoExistente(pAux) && !getTabuleiro().posicaoPreenchida(pAux)) {
			mat[pAux.getLinha()][pAux.getColuna()] = true;
			pAux.setColuna(pAux.getColuna() - 1);
		}
		if(getTabuleiro().posicaoExistente(pAux) && seExistePecaOponente(pAux)) {
			mat[pAux.getLinha()][pAux.getColuna()] = true;
		}
		
		// À direita
		pAux.setValores(posicao.getLinha(), posicao.getColuna() + 1);
		while(getTabuleiro().posicaoExistente(pAux) && !getTabuleiro().posicaoPreenchida(pAux)) {
			mat[pAux.getLinha()][pAux.getColuna()] = true;
			pAux.setColuna(pAux.getColuna() + 1);
		}
		if(getTabuleiro().posicaoExistente(pAux) && seExistePecaOponente(pAux)) {
			mat[pAux.getLinha()][pAux.getColuna()] = true;
		}
		
		// Abaixo
		pAux.setValores(posicao.getLinha() + 1, posicao.getColuna());
		while(getTabuleiro().posicaoExistente(pAux) && !getTabuleiro().posicaoPreenchida(pAux)) {
			mat[pAux.getLinha()][pAux.getColuna()] = true;
			pAux.setLinha(pAux.getLinha() + 1);
		}
		if(getTabuleiro().posicaoExistente(pAux) && seExistePecaOponente(pAux)) {
			mat[pAux.getLinha()][pAux.getColuna()] = true;
		}
		
		return mat;
	}
	
}