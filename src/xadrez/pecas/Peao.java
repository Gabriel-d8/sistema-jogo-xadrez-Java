package xadrez.pecas;

import jogoTabuleiro.Posicao;
import jogoTabuleiro.Tabuleiro;
import xadrez.Cor;
import xadrez.PecaXadrez;

public class Peao extends PecaXadrez {

	public Peao(Tabuleiro tabuleiro, Cor cor) {
		super(tabuleiro, cor);
	}

	@Override
	public String toString() {
		return "P";
	}	
	
	@Override
	public boolean[][] movimentosPossiveis() {
		boolean mat [][] = new boolean [getTabuleiro().getLinhas()][getTabuleiro().getColunas()];
		Posicao pAux = new Posicao(0, 0);
		
		if (getCor() == Cor.BRANCO) {
			pAux.setValores(posicao.getLinha() - 1, posicao.getColuna());
			if(getTabuleiro().posicaoExistente(pAux) && !getTabuleiro().posicaoPreenchida(pAux)) {
				mat[pAux.getLinha()][pAux.getColuna()] = true;
			}
			pAux.setValores(posicao.getLinha() - 2, posicao.getColuna());
			Posicao pAux2 = new Posicao(posicao.getLinha() - 1, posicao.getColuna());
			if(getTabuleiro().posicaoExistente(pAux) && !getTabuleiro().posicaoPreenchida(pAux) && getTabuleiro().posicaoExistente(pAux2) && !getTabuleiro().posicaoPreenchida(pAux2) && getContagemMovimentos() == 0) {
				mat[pAux.getLinha()][pAux.getColuna()] = true;
			}
			pAux.setValores(posicao.getLinha() - 1, posicao.getColuna() -1);
			if(getTabuleiro().posicaoExistente(pAux) && seExistePecaOponente(pAux)) {
				mat[pAux.getLinha()][pAux.getColuna()] = true;
			}
			pAux.setValores(posicao.getLinha() - 1, posicao.getColuna() +1);
			if(getTabuleiro().posicaoExistente(pAux) && seExistePecaOponente(pAux)) {
				mat[pAux.getLinha()][pAux.getColuna()] = true;
			}	
		}
		else {
			pAux.setValores(posicao.getLinha() + 1, posicao.getColuna());
			if(getTabuleiro().posicaoExistente(pAux) && !getTabuleiro().posicaoPreenchida(pAux)) {
				mat[pAux.getLinha()][pAux.getColuna()] = true;
			}
			pAux.setValores(posicao.getLinha() + 2, posicao.getColuna());
			Posicao pAux2 = new Posicao(posicao.getLinha() + 1, posicao.getColuna());
			if(getTabuleiro().posicaoExistente(pAux) && !getTabuleiro().posicaoPreenchida(pAux) && getTabuleiro().posicaoExistente(pAux2) && !getTabuleiro().posicaoPreenchida(pAux2) && getContagemMovimentos() == 0) {
				mat[pAux.getLinha()][pAux.getColuna()] = true;
			}
			pAux.setValores(posicao.getLinha() + 1, posicao.getColuna() -1);
			if(getTabuleiro().posicaoExistente(pAux) && seExistePecaOponente(pAux)) {
				mat[pAux.getLinha()][pAux.getColuna()] = true;
			}
			pAux.setValores(posicao.getLinha() + 1, posicao.getColuna() +1);
			if(getTabuleiro().posicaoExistente(pAux) && seExistePecaOponente(pAux)) {
				mat[pAux.getLinha()][pAux.getColuna()] = true;
			}		
		}
		
		return mat;
	}
	
}