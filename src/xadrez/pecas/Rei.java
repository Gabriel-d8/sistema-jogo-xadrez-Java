package xadrez.pecas;

import jogoTabuleiro.Posicao;
import jogoTabuleiro.Tabuleiro;
import xadrez.Cor;
import xadrez.PecaXadrez;
import xadrez.Regras;

public class Rei extends PecaXadrez {
	
	private Regras partida;

	public Rei(Tabuleiro tabuleiro, Cor cor, Regras partida) {
		super(tabuleiro, cor);
		this.partida = partida;
	}

	@Override
	public String toString() {
		return "Re";
	}

	private boolean podeMover(Posicao posicao) {
		PecaXadrez p = (PecaXadrez)getTabuleiro().peca(posicao);
		return p == null || p.getCor() != getCor();
	}
	
	private boolean testeRoque(Posicao posicao) {
		PecaXadrez p = (PecaXadrez)getTabuleiro().peca(posicao);
		return p != null && p instanceof Torre && p.getCor() == getCor() && p.getContagemMovimentos() == 0;
	}
	
	@Override
	public boolean[][] movimentosPossiveis() {
		boolean [][] mat = new boolean [getTabuleiro().getLinhas()][getTabuleiro().getColunas()];
		Posicao pAux = new Posicao(0, 0);
		
		//Acima
		pAux.setValores(posicao.getLinha() - 1, posicao.getColuna());
		if (getTabuleiro().posicaoExistente(pAux) && podeMover(pAux)) {
			mat[pAux.getLinha()][pAux.getColuna()] = true;
		}
		
		//Abaixo
		pAux.setValores(posicao.getLinha() + 1, posicao.getColuna());
		if (getTabuleiro().posicaoExistente(pAux) && podeMover(pAux)) {
			mat[pAux.getLinha()][pAux.getColuna()] = true;
		}
		
		//À esquerda
		pAux.setValores(posicao.getLinha(), posicao.getColuna() - 1);
		if (getTabuleiro().posicaoExistente(pAux) && podeMover(pAux)) {
			mat[pAux.getLinha()][pAux.getColuna()] = true;
		}
		
		//À direita
		pAux.setValores(posicao.getLinha(), posicao.getColuna() + 1);
		if (getTabuleiro().posicaoExistente(pAux) && podeMover(pAux)) {
			mat[pAux.getLinha()][pAux.getColuna()] = true;
		}
		
		//Noroeste (Diagonal acima à esquerda)
		pAux.setValores(posicao.getLinha() - 1, posicao.getColuna() - 1);
		if (getTabuleiro().posicaoExistente(pAux) && podeMover(pAux)) {
			mat[pAux.getLinha()][pAux.getColuna()] = true;
		}			
		
		//Nordeste (Diagonal acima à direita)
		pAux.setValores(posicao.getLinha() - 1, posicao.getColuna() + 1);
		if (getTabuleiro().posicaoExistente(pAux) && podeMover(pAux)) {
			mat[pAux.getLinha()][pAux.getColuna()] = true;
		}
		
		//Sudoeste (Diagonal abaixo à esquerda)
		pAux.setValores(posicao.getLinha() + 1, posicao.getColuna() - 1);
		if (getTabuleiro().posicaoExistente(pAux) && podeMover(pAux)) {
			mat[pAux.getLinha()][pAux.getColuna()] = true;
		}
		
		//Sudeste (Diagonal abaixo à direita)
		pAux.setValores(posicao.getLinha() + 1, posicao.getColuna() + 1);
		if (getTabuleiro().posicaoExistente(pAux) && podeMover(pAux)) {
			mat[pAux.getLinha()][pAux.getColuna()] = true;
		}
		
		//Movimento especial de Castling
		if (getContagemMovimentos() == 0 && partida.getCheque()) {
			Posicao posicaoTorre1 = new Posicao(posicao.getLinha(), posicao.getColuna() + 3);
			if (testeRoque(posicaoTorre1)) {
				Posicao p1 = new Posicao(posicao.getLinha(), posicao.getColuna() + 1);
				Posicao p2 = new Posicao(posicao.getLinha(), posicao.getColuna() + 2);
				if(getTabuleiro().peca(p1) == null && getTabuleiro().peca(p2) == null) {
					mat[posicao.getLinha()][posicao.getColuna() + 2] = true;
				}
			}
			
			Posicao posicaoTorre2 = new Posicao(posicao.getLinha(), posicao.getColuna() - 4);
			if (testeRoque(posicaoTorre2)) {
				Posicao p1 = new Posicao(posicao.getLinha(), posicao.getColuna() - 1);
				Posicao p2 = new Posicao(posicao.getLinha(), posicao.getColuna() - 2);
				Posicao p3 = new Posicao(posicao.getLinha(), posicao.getColuna() - 3);
				if (getTabuleiro().peca(p1) == null && getTabuleiro().peca(p2) == null && getTabuleiro().peca(p3) == null) {
					mat[posicao.getLinha()][posicao.getColuna() - 2] = true;
				}
			}	
		}
		
		return mat;
	}
	
}