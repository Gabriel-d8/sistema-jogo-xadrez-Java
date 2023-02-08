package xadrez;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import jogoTabuleiro.Peca;
import jogoTabuleiro.Posicao;
import jogoTabuleiro.Tabuleiro;
import xadrez.pecas.Bispo;
import xadrez.pecas.Cavalo;
import xadrez.pecas.Peao;
import xadrez.pecas.Rainha;
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
			throw new XadrezExcecao("Erro! Você não pode se colocar em cheque.");
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
		
		//Movimentação especial de Castling
		if(p instanceof Rei && destino.getColuna() == origem.getColuna() + 2) {
			Posicao origemTorre = new Posicao(origem.getLinha(), origem.getColuna() + 3);
			Posicao destinoTorre = new Posicao(origem.getLinha(), origem.getColuna() + 1);
			PecaXadrez torre = (PecaXadrez)tabuleiro.removerPeca(origemTorre);
			tabuleiro.localPeca(torre, destinoTorre);
			torre.incrementarContagemMovimento();
		}
		if(p instanceof Rei && destino.getColuna() == origem.getColuna() - 2) {
			Posicao origemTorre = new Posicao(origem.getLinha(), origem.getColuna() - 4);
			Posicao destinoTorre = new Posicao(origem.getLinha(), origem.getColuna() - 1);
			PecaXadrez torre = (PecaXadrez)tabuleiro.removerPeca(origemTorre);
			tabuleiro.localPeca(torre, destinoTorre);
			torre.incrementarContagemMovimento();
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
		
		//Movimentação especial de Castling
		if(p instanceof Rei && destino.getColuna() == origem.getColuna() + 2) {
			Posicao origemTorre = new Posicao(origem.getLinha(), origem.getColuna() + 3);
			Posicao destinoTorre = new Posicao(origem.getLinha(), origem.getColuna() + 1);
			PecaXadrez torre = (PecaXadrez)tabuleiro.removerPeca(destinoTorre);
			tabuleiro.localPeca(torre, origemTorre);
			torre.decrementarContagemMovimento();
		}
		if(p instanceof Rei && destino.getColuna() == origem.getColuna() - 2) {
			Posicao origemTorre = new Posicao(origem.getLinha(), origem.getColuna() - 4);
			Posicao destinoTorre = new Posicao(origem.getLinha(), origem.getColuna() - 1);
			PecaXadrez torre = (PecaXadrez)tabuleiro.removerPeca(destinoTorre);
			tabuleiro.localPeca(torre, origemTorre);
			torre.decrementarContagemMovimento();		
		}
	}
	
	private void validacaoPosicaoOrigem(Posicao posicao) {
		if(!tabuleiro.posicaoPreenchida(posicao)) {
			throw new XadrezExcecao("Erro! Não existe peça na posição de origem");
		}
		if(jogadorAtual != ((PecaXadrez)tabuleiro.peca(posicao)).getCor()) {
			throw new XadrezExcecao("Erro! A peça escolhida não é sua.");
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
		throw new IllegalStateException("Erro! Não existe este Rei " + cor + " no tabuleiro");
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
		novoLocalDaPeca('a', 1, new Torre(tabuleiro, Cor.BRANCO));	
		novoLocalDaPeca('b', 1, new Cavalo(tabuleiro, Cor.BRANCO));	
		novoLocalDaPeca('c', 1, new Bispo(tabuleiro, Cor.BRANCO));	
		novoLocalDaPeca('d', 1, new Rainha(tabuleiro, Cor.BRANCO));
		novoLocalDaPeca('e', 1, new Rei(tabuleiro, Cor.BRANCO, this));
		novoLocalDaPeca('f', 1, new Bispo(tabuleiro, Cor.BRANCO));
		novoLocalDaPeca('g', 1, new Cavalo(tabuleiro, Cor.BRANCO));	
		novoLocalDaPeca('h', 1, new Torre(tabuleiro, Cor.BRANCO));
		novoLocalDaPeca('a', 2, new Peao(tabuleiro, Cor.BRANCO));	
		novoLocalDaPeca('b', 2, new Peao(tabuleiro, Cor.BRANCO));
		novoLocalDaPeca('c', 2, new Peao(tabuleiro, Cor.BRANCO));
		novoLocalDaPeca('d', 2, new Peao(tabuleiro, Cor.BRANCO));	
		novoLocalDaPeca('e', 2, new Peao(tabuleiro, Cor.BRANCO));
		novoLocalDaPeca('f', 2, new Peao(tabuleiro, Cor.BRANCO));
		novoLocalDaPeca('g', 2, new Peao(tabuleiro, Cor.BRANCO));	
		novoLocalDaPeca('h', 2, new Peao(tabuleiro, Cor.BRANCO));
		      
		novoLocalDaPeca('a', 8, new Torre(tabuleiro, Cor.PRETO)); 
		novoLocalDaPeca('b', 8, new Cavalo(tabuleiro, Cor.PRETO)); 
		novoLocalDaPeca('c', 8, new Bispo(tabuleiro, Cor.PRETO));
		novoLocalDaPeca('d', 8, new Rainha(tabuleiro, Cor.PRETO));
		novoLocalDaPeca('e', 8, new Rei(tabuleiro, Cor.PRETO, this));
		novoLocalDaPeca('f', 8, new Bispo(tabuleiro, Cor.PRETO));
		novoLocalDaPeca('g', 8, new Cavalo(tabuleiro, Cor.PRETO)); 
		novoLocalDaPeca('h', 8, new Torre(tabuleiro, Cor.PRETO));            
		novoLocalDaPeca('a', 7, new Peao(tabuleiro, Cor.PRETO));
		novoLocalDaPeca('b', 7, new Peao(tabuleiro, Cor.PRETO));            
		novoLocalDaPeca('c', 7, new Peao(tabuleiro, Cor.PRETO));
		novoLocalDaPeca('d', 7, new Peao(tabuleiro, Cor.PRETO));            
		novoLocalDaPeca('e', 7, new Peao(tabuleiro, Cor.PRETO));
		novoLocalDaPeca('f', 7, new Peao(tabuleiro, Cor.PRETO));            
		novoLocalDaPeca('g', 7, new Peao(tabuleiro, Cor.PRETO));
		novoLocalDaPeca('h', 7, new Peao(tabuleiro, Cor.PRETO));            
	}
	
}