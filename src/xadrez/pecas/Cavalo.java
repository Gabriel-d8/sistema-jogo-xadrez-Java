package xadrez.pecas;

import jogoTabuleiro.Posicao;
import jogoTabuleiro.Tabuleiro;
import xadrez.Cor;
import xadrez.PecaXadrez;

public class Cavalo extends PecaXadrez {

	public Cavalo(Tabuleiro tabuleiro, Cor cor) {
		super(tabuleiro, cor);
	}
	
	public String toString(){
		return "C";
	}

	private boolean podeMover(Posicao posicao) {
		PecaXadrez p = (PecaXadrez)getTabuleiro().peca(posicao);
		return p == null || p.getCor() != getCor();
	}	
	
	@Override
	public boolean[][] movimentosPossiveis() {
		boolean mat [][] = new boolean [getTabuleiro().getLinhas()][getTabuleiro().getColunas()];
		Posicao pAux = new Posicao(0, 0);
		
		pAux.setValores(posicao.getLinha() - 1, posicao.getColuna() - 2);
		if(getTabuleiro().posicaoExistente(pAux) && podeMover(pAux)) {
			mat[pAux.getLinha()][pAux.getColuna()] = true;
		}
		
		pAux.setValores(posicao.getLinha() - 2, posicao.getColuna() - 1);
		if(getTabuleiro().posicaoExistente(pAux) && podeMover(pAux)) {
			mat[pAux.getLinha()][pAux.getColuna()] = true;
		}
		
		pAux.setValores(posicao.getLinha() - 2, posicao.getColuna() + 1);
		if(getTabuleiro().posicaoExistente(pAux) && podeMover(pAux)) {
			mat[pAux.getLinha()][pAux.getColuna()] = true;
		}
		
		pAux.setValores(posicao.getLinha() - 1, posicao.getColuna() + 2);
		if(getTabuleiro().posicaoExistente(pAux) && podeMover(pAux)) {
			mat[pAux.getLinha()][pAux.getColuna()] = true;
		}

		pAux.setValores(posicao.getLinha() + 1, posicao.getColuna() + 2);
		if(getTabuleiro().posicaoExistente(pAux) && podeMover(pAux)) {
			mat[pAux.getLinha()][pAux.getColuna()] = true;
		}			
		
		pAux.setValores(posicao.getLinha() + 2, posicao.getColuna() + 1);
		if(getTabuleiro().posicaoExistente(pAux) && podeMover(pAux)) {
			mat[pAux.getLinha()][pAux.getColuna()] = true;
		}
		
		pAux.setValores(posicao.getLinha() + 2, posicao.getColuna() - 1);
		if(getTabuleiro().posicaoExistente(pAux) && podeMover(pAux)) {
			mat[pAux.getLinha()][pAux.getColuna()] = true;
		}
		
		pAux.setValores(posicao.getLinha() + 1, posicao.getColuna() - 2);
		if(getTabuleiro().posicaoExistente(pAux) && podeMover(pAux)) {
			mat[pAux.getLinha()][pAux.getColuna()] = true;
		}		
		
		return mat;
	}

}