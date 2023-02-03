package xadrez;

import jogoTabuleiro.Peca;
import jogoTabuleiro.Posicao;
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
	
	public boolean[][] movimentosPossiveis(XadrezPosicao posicaoOrigem){
		Posicao posicao = posicaoOrigem.posicionar();
		validacaoPosicaoOrigem(posicao);
		return tabuleiro.peca(posicao).movimentosPossiveis();
	}
	
	public PecaXadrez executarJogada(XadrezPosicao posicaoOrigem, XadrezPosicao posicaoDestino) {
		Posicao origem = posicaoOrigem.posicionar();
		Posicao destino = posicaoDestino.posicionar();
		validacaoPosicaoOrigem(origem);
		validacaoPosicaoDestino(origem, destino);
		Peca pecaCapturada = mover(origem, destino);
		return (PecaXadrez) pecaCapturada;
	}
	
	private Peca mover(Posicao origem, Posicao destino) {
		Peca p = tabuleiro.removerPeca(origem);
		Peca pecaCapturada = tabuleiro.removerPeca(destino);
		tabuleiro.localPeca(p, destino);
		return pecaCapturada;
	}
	
	private void validacaoPosicaoOrigem(Posicao posicao) {
		if(!tabuleiro.posicaoPreenchida(posicao)) {
			throw new XadrezExcecao("Erro! Não existe peça na posição de origem");
		}
		if(!tabuleiro.peca(posicao).seExisteMovimentoPossivel()) {
			throw new XadrezExcecao("Erro! Não existem movimentos possíveis para a peça escolhida.");
		}
	}
	
	private void validacaoPosicaoDestino(Posicao origem, Posicao destino) {
		if(!tabuleiro.peca(origem).movimentoPossivel(destino)) {
			throw new XadrezExcecao("Erro! A peça escolhida não pode se mover para a posição de destino.");
		}
	}
	
	private void novoLocalDaPeca(char coluna, int linha, PecaXadrez peca) {
		tabuleiro.localPeca(peca, new XadrezPosicao(coluna, linha).posicionar());
	}
	
	private void iniciar() {
		novoLocalDaPeca('c', 8, new Rei(tabuleiro, Cor.PRETO));	
		novoLocalDaPeca('d', 8, new Torre(tabuleiro, Cor.BRANCO));
		novoLocalDaPeca('e', 7, new Rei(tabuleiro, Cor.PRETO));
		novoLocalDaPeca('c', 1, new Torre(tabuleiro, Cor.BRANCO));            
		novoLocalDaPeca('d', 2, new Rei(tabuleiro, Cor.PRETO));
		novoLocalDaPeca('e', 2, new Torre(tabuleiro, Cor.BRANCO));
	}
}