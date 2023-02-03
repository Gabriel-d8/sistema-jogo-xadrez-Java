package xadrez.pecas;

import jogoTabuleiro.Posicao;
import jogoTabuleiro.Tabuleiro;
import xadrez.Cor;
import xadrez.PecaXadrez;

public class Rei extends PecaXadrez {

	public Rei(Tabuleiro tabuleiro, Cor cor) {
		super(tabuleiro, cor);
	}

	@Override
	public String toString() {
		return "R";
	}

	private boolean podeMover(Posicao posicao) {
		PecaXadrez p = (PecaXadrez)getTabuleiro().peca(posicao);
		return p == null || p.getCor() != getCor();
	}
	
	@Override
	public boolean[][] movimentosPossiveis() {
		boolean [][] mat = new boolean [getTabuleiro().getLinhas()][getTabuleiro().getColunas()];
		Posicao pAux = new Posicao(0, 0);
		
		//Acima
		pAux.setValores(posicao.getLinha() - 1, posicao.getColuna());
		if(getTabuleiro().posicaoExistente(pAux) && podeMover(pAux)) {
			mat[pAux.getLinha()][pAux.getColuna()] = true;
		}
		
		//Abaixo
		pAux.setValores(posicao.getLinha() + 1, posicao.getColuna());
		if(getTabuleiro().posicaoExistente(pAux) && podeMover(pAux)) {
			mat[pAux.getLinha()][pAux.getColuna()] = true;
		}
		
		//À esquerda
		pAux.setValores(posicao.getLinha(), posicao.getColuna() - 1);
		if(getTabuleiro().posicaoExistente(pAux) && podeMover(pAux)) {
			mat[pAux.getLinha()][pAux.getColuna()] = true;
		}
		
		//À direita
		pAux.setValores(posicao.getLinha(), posicao.getColuna() + 1);
		if(getTabuleiro().posicaoExistente(pAux) && podeMover(pAux)) {
			mat[pAux.getLinha()][pAux.getColuna()] = true;
		}
		
		//Noroeste (Diagonal acima à esquerda)
		pAux.setValores(posicao.getLinha() - 1, posicao.getColuna() - 1);
		if(getTabuleiro().posicaoExistente(pAux) && podeMover(pAux)) {
			mat[pAux.getLinha()][pAux.getColuna()] = true;
		}			
		
		//Nordeste (Diagonal acima à direita)
		pAux.setValores(posicao.getLinha() - 1, posicao.getColuna() + 1);
		if(getTabuleiro().posicaoExistente(pAux) && podeMover(pAux)) {
			mat[pAux.getLinha()][pAux.getColuna()] = true;
		}
		
		//Sudoeste (Diagonal abaixo à esquerda)
		pAux.setValores(posicao.getLinha() + 1, posicao.getColuna() - 1);
		if(getTabuleiro().posicaoExistente(pAux) && podeMover(pAux)) {
			mat[pAux.getLinha()][pAux.getColuna()] = true;
		}
		
		//Sudeste (Diagonal abaixo à direita)
		pAux.setValores(posicao.getLinha() + 1, posicao.getColuna() + 1);
		if(getTabuleiro().posicaoExistente(pAux) && podeMover(pAux)) {
			mat[pAux.getLinha()][pAux.getColuna()] = true;
		}
		
		return mat;
	}
	
}