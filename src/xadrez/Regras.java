package xadrez;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import jogoTabuleiro.Peca;
import jogoTabuleiro.Posicao;
import jogoTabuleiro.Tabuleiro;
import xadrez.pecas.Rei;
import xadrez.pecas.Torre;

public class Regras {

	private int vez;
	private Cor jogadorAtual;
	private Tabuleiro tabuleiro;
	private boolean cheque;
	private boolean chequeMate;
	
	private List<Peca> pecasNoTabuleiro = new ArrayList<>();
	private List<Peca> pecasCapturadas = new ArrayList<>();
	
	public Regras() {
		tabuleiro = new Tabuleiro(8, 8);
		vez = 1;
		jogadorAtual = Cor.BRANCO;
		iniciar();
	}
	
	public int getVez() {
		return vez;
	}

	public Cor getJogadorAtual() {
		return jogadorAtual;
	}

	public boolean getCheque() {
		return cheque;
	}
	
	public boolean getChequeMate() {
		return chequeMate;
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
		
		if(testeCheque(jogadorAtual)) {
			desfazerMovimento(origem, destino, pecaCapturada);
			throw new XadrezExcecao("Erro! Voc� n�o pode se colocar em cheque.");
		}
		
		cheque = (testeCheque(oponente(jogadorAtual))) ? true : false;
		
		if(testeChequeMate(oponente(jogadorAtual))) {
			chequeMate = true;
		}
		else {
		proximoAJogar();
		}
		
		return (PecaXadrez) pecaCapturada;
	}
	
	private Peca mover(Posicao origem, Posicao destino) {
		PecaXadrez p = (PecaXadrez) tabuleiro.removerPeca(origem);
		p.incrementarContagemMovimento();
		Peca pecaCapturada = tabuleiro.removerPeca(destino);
		tabuleiro.localPeca(p, destino);
		
		if(pecaCapturada != null) {
			pecasNoTabuleiro.remove(pecaCapturada);
			pecasCapturadas.add(pecaCapturada);
		}
		
		return pecaCapturada;
	}
	
	private void desfazerMovimento(Posicao origem, Posicao destino, Peca pecaCapturada) {
		PecaXadrez p = (PecaXadrez) tabuleiro.removerPeca(destino);
		p.decrementarContagemMovimento();
		tabuleiro.localPeca(p, origem);
		
		if(pecaCapturada != null) {
			tabuleiro.localPeca(pecaCapturada, destino);
			pecasNoTabuleiro.add(pecaCapturada);
		}
	}
	
	private void validacaoPosicaoOrigem(Posicao posicao) {
		if(!tabuleiro.posicaoPreenchida(posicao)) {
			throw new XadrezExcecao("Erro! N�o existe pe�a na posi��o de origem");
		}
		if(jogadorAtual != ((PecaXadrez)tabuleiro.peca(posicao)).getCor()) {
			throw new XadrezExcecao("Erro! A pe�a escolhida n�o � sua.");
		}
		if(!tabuleiro.peca(posicao).seExisteMovimentoPossivel()) {
			throw new XadrezExcecao("Erro! N�o existem movimentos poss�veis para a pe�a escolhida.");
		}
	}
	
	private void validacaoPosicaoDestino(Posicao origem, Posicao destino) {
		if(!tabuleiro.peca(origem).movimentoPossivel(destino)) {
			throw new XadrezExcecao("Erro! A pe�a escolhida n�o pode se mover para a posi��o de destino.");
		}
	}
	
	private void proximoAJogar() {
		vez++;
		jogadorAtual = (jogadorAtual == Cor.BRANCO) ? Cor.PRETO : Cor.BRANCO;
	}
	
	private Cor oponente(Cor cor) {
		return (cor == cor.BRANCO) ? cor.PRETO : cor.BRANCO;
	}
	
	private PecaXadrez rei(Cor cor) {
		List<Peca> list = pecasNoTabuleiro.stream().filter(x -> ((PecaXadrez) x).getCor() == cor).collect(Collectors.toList());
		for(Peca p: list) {
			if(p instanceof Rei) {
				return (PecaXadrez) p;
			}
		}
		throw new IllegalStateException("Erro! N�o existe este Rei " + cor + " no tabuleiro");
	}
	
	private boolean testeCheque(Cor cor) {
		Posicao posicaoRei = rei(cor).getXadrezPosicao().posicionar();
		List<Peca> pecasOponentes = pecasNoTabuleiro.stream().filter(x -> ((PecaXadrez) x).getCor() == oponente(cor)).collect(Collectors.toList());
		for (Peca p : pecasOponentes) {
			boolean[][] mat = p.movimentosPossiveis();
			if(mat[posicaoRei.getLinha()][posicaoRei.getColuna()]) {
				return true;
			}
		}
		return false;
	}	
	
	private boolean testeChequeMate(Cor cor) {
		if (!testeCheque(cor)) {
			return false;
		}
		List<Peca> list = pecasNoTabuleiro.stream().filter(x -> ((PecaXadrez) x).getCor() == cor).collect(Collectors.toList());
		for (Peca p : list) {
			boolean [][] mat = p.movimentosPossiveis();
			for (int i=0; i < tabuleiro.getLinhas(); i++) {
				for (int j=0; j < tabuleiro.getColunas(); j++) {
					if (mat[i][j]) {
						Posicao origem = ((PecaXadrez)p).getXadrezPosicao().posicionar();
						Posicao destino = new Posicao(i, j);
						Peca pecaCapturada = mover(origem, destino);
						boolean testeCheque = testeCheque(cor);
						desfazerMovimento(origem, destino, pecaCapturada);
						if (!testeCheque) {
							return false;
						}
					}
				}
			}
		}
		return true;
	}
	
	private void novoLocalDaPeca(char coluna, int linha, PecaXadrez peca) {
		tabuleiro.localPeca(peca, new XadrezPosicao(coluna, linha).posicionar());
		pecasNoTabuleiro.add(peca);
	}
	
	private void iniciar() {
		//Posicionamento para implementar l�gica de cheque-mate.
		novoLocalDaPeca('h', 7, new Torre(tabuleiro, Cor.BRANCO));	
		novoLocalDaPeca('d', 1, new Torre(tabuleiro, Cor.BRANCO));
		novoLocalDaPeca('e', 1, new Rei(tabuleiro, Cor.BRANCO));
		      
		novoLocalDaPeca('b', 8, new Torre(tabuleiro, Cor.PRETO));            
		novoLocalDaPeca('a', 8, new Rei(tabuleiro, Cor.PRETO));
	}
	
}