package xadrez.pecas;

import jogoTabuleiro.Posicao;
import jogoTabuleiro.Tabuleiro;
import xadrez.Cor;
import xadrez.PecaXadrez;
import xadrez.Regras;

public class Peao extends PecaXadrez {
	
	private Regras partida;

	public Peao(Tabuleiro tabuleiro, Cor cor, Regras partida) {
		super(tabuleiro, cor);
		this.partida = partida;
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
			
			//Movimento especial de En Passant
			if(posicao.getLinha() == 3) {
				Posicao esquerda = new Posicao(posicao.getLinha(), posicao.getColuna() - 1);
				if(getTabuleiro().posicaoExistente(esquerda) && seExistePecaOponente(esquerda) && getTabuleiro().peca(esquerda) == partida.getEnPassantVulneravel()) {
					mat[esquerda.getLinha() - 1][esquerda.getColuna()] =  true;
				}
				Posicao direita = new Posicao(posicao.getLinha(), posicao.getColuna() + 1);
				if(getTabuleiro().posicaoExistente(direita) && seExistePecaOponente(direita) && getTabuleiro().peca(direita) == partida.getEnPassantVulneravel()) {
					mat[direita.getLinha() - 1][direita.getColuna()] =  true;
				}	
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
			
			//Movimento especial de En Passant
			if(posicao.getLinha() == 4) {
				Posicao esquerda = new Posicao(posicao.getLinha(), posicao.getColuna() - 1);
				if(getTabuleiro().posicaoExistente(esquerda) && seExistePecaOponente(esquerda) && getTabuleiro().peca(esquerda) == partida.getEnPassantVulneravel()) {
					mat[esquerda.getLinha() + 1][esquerda.getColuna()] =  true;
				}
				Posicao direita = new Posicao(posicao.getLinha(), posicao.getColuna() + 1);
				if(getTabuleiro().posicaoExistente(direita) && seExistePecaOponente(direita) && getTabuleiro().peca(direita) == partida.getEnPassantVulneravel()) {
					mat[direita.getLinha() + 1][direita.getColuna()] =  true;
				}	
			}
		}
		
		return mat;
	}
	
}