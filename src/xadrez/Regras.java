package xadrez;

import java.security.InvalidParameterException;
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
	private PecaXadrez enPassantVulneravel;
	private PecaXadrez promocao;
	
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
	
	public PecaXadrez getEnPassantVulneravel() {
		return enPassantVulneravel;
	}
	
	public PecaXadrez getPromocao() {
		return promocao;
	}
	
	public PecaXadrez [][] getPecas(){
		PecaXadrez [][] matriz = new PecaXadrez [tabuleiro.getLinhas()][tabuleiro.getColunas()];
		for (int i=0; i<tabuleiro.getLinhas(); i++) {
			for (int j=0; j<tabuleiro.getColunas(); j++) {
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
		
		if (testeCheque(jogadorAtual)) {
			desfazerMovimento(origem, destino, pecaCapturada);
			throw new XadrezExcecao("Erro! Voc� n�o pode se colocar em cheque.");
		}
		
		PecaXadrez pecaMovida = (PecaXadrez)tabuleiro.peca(destino);
		
		//Movimento especial de Promo��o
		promocao = null;
		if (pecaMovida instanceof Peao) {
			if (pecaMovida.getCor() == Cor.BRANCO && destino.getLinha() == 0 || pecaMovida.getCor() == Cor.PRETO && destino.getLinha() == 7) {
				promocao = (PecaXadrez)tabuleiro.peca(destino);
				promocao = substituirPecaPromovida("Ra");
				
			}
		}
		
		cheque = (testeCheque(oponente(jogadorAtual))) ? true : false;
		
		if(testeChequeMate(oponente(jogadorAtual))) {
			chequeMate = true;
		}
		else {
		proximoAJogar();
		}
		
		//Movimento especial de En Passant.
		if(pecaMovida instanceof Peao && (destino.getLinha() == origem.getLinha() - 2 || destino.getLinha() == origem.getLinha() + 2)) {
			enPassantVulneravel = pecaMovida;
		}
		else {
			enPassantVulneravel = null;
		}
		
		return (PecaXadrez) pecaCapturada;
	}
	
	public PecaXadrez substituirPecaPromovida(String tipoDaPeca) {
		if (promocao == null) {
			throw new IllegalStateException("Erro! N�o h� pe�a para ser promovida.");
		}
		if (!tipoDaPeca.equals("Re") && !tipoDaPeca.equals("T") &&  !tipoDaPeca.equals("C") && !tipoDaPeca.equals("B")) {
			throw new InvalidParameterException("Erro! Tipo de pe�a inv�lida para a promo��o");
		}
		
		Posicao posicao = promocao.getXadrezPosicao().posicionar();
		Peca p = tabuleiro.removerPeca(posicao);
		pecasNoTabuleiro.remove(p);
		
		PecaXadrez novaPeca = novaPeca(tipoDaPeca, promocao.getCor());
		tabuleiro.localPeca(novaPeca, posicao);
		pecasNoTabuleiro.add(novaPeca);
		
		return novaPeca;
	}
	
	private PecaXadrez novaPeca(String tipoDaPeca, Cor cor) {
		if (tipoDaPeca.equals("Ra")) return new Rainha(tabuleiro, cor);
		if (tipoDaPeca.equals("B")) return new Bispo(tabuleiro, cor);
		if (tipoDaPeca.equals("C")) return new Cavalo(tabuleiro, cor);
		return new Torre(tabuleiro, cor);	
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
		
		//Movimento especial de Castling
		if (p instanceof Rei && destino.getColuna() == origem.getColuna() + 2) {
			Posicao origemTorre = new Posicao(origem.getLinha(), origem.getColuna() + 3);
			Posicao destinoTorre = new Posicao(origem.getLinha(), origem.getColuna() + 1);
			PecaXadrez torre = (PecaXadrez)tabuleiro.removerPeca(origemTorre);
			tabuleiro.localPeca(torre, destinoTorre);
			torre.incrementarContagemMovimento();
		}
		if (p instanceof Rei && destino.getColuna() == origem.getColuna() - 2) {
			Posicao origemTorre = new Posicao(origem.getLinha(), origem.getColuna() - 4);
			Posicao destinoTorre = new Posicao(origem.getLinha(), origem.getColuna() - 1);
			PecaXadrez torre = (PecaXadrez)tabuleiro.removerPeca(origemTorre);
			tabuleiro.localPeca(torre, destinoTorre);
			torre.incrementarContagemMovimento();
		}
		
		//Movimento especial de En Passant
		if (p instanceof Peao) {
			if(origem.getColuna() != destino.getColuna() && pecaCapturada == null) {
				Posicao posicaoPeao;
				if(p.getCor() == Cor.BRANCO) {
					posicaoPeao = new Posicao(destino.getLinha() - 1, destino.getColuna());
				}
				else {
					posicaoPeao = new Posicao(destino.getLinha() + 1, destino.getColuna());
				}
				pecaCapturada = tabuleiro.removerPeca(posicaoPeao);
				pecasCapturadas.add(pecaCapturada);
				pecasNoTabuleiro.remove(pecaCapturada);
			}
		}
		
		return pecaCapturada;
	}
	
	private void desfazerMovimento(Posicao origem, Posicao destino, Peca pecaCapturada) {
		PecaXadrez p = (PecaXadrez) tabuleiro.removerPeca(destino);
		p.decrementarContagemMovimento();
		tabuleiro.localPeca(p, origem);
		
		if (pecaCapturada != null) {
			tabuleiro.localPeca(pecaCapturada, destino);
			pecasNoTabuleiro.add(pecaCapturada);
		}
		
		//Movimento especial de Castling
		if (p instanceof Rei && destino.getColuna() == origem.getColuna() + 2) {
			Posicao origemTorre = new Posicao(origem.getLinha(), origem.getColuna() + 3);
			Posicao destinoTorre = new Posicao(origem.getLinha(), origem.getColuna() + 1);
			PecaXadrez torre = (PecaXadrez)tabuleiro.removerPeca(destinoTorre);
			tabuleiro.localPeca(torre, origemTorre);
			torre.decrementarContagemMovimento();
		}
		if (p instanceof Rei && destino.getColuna() == origem.getColuna() - 2) {
			Posicao origemTorre = new Posicao(origem.getLinha(), origem.getColuna() - 4);
			Posicao destinoTorre = new Posicao(origem.getLinha(), origem.getColuna() - 1);
			PecaXadrez torre = (PecaXadrez)tabuleiro.removerPeca(destinoTorre);
			tabuleiro.localPeca(torre, origemTorre);
			torre.decrementarContagemMovimento();		
		}
		
		//Movimento especial de En Passant
		if (p instanceof Peao) {
			if(origem.getColuna() != destino.getColuna() && pecaCapturada == enPassantVulneravel) {
				PecaXadrez peao = (PecaXadrez)tabuleiro.removerPeca(destino); 
				Posicao posicaoPeao;
				if(p.getCor() == Cor.BRANCO) {
					posicaoPeao = new Posicao(3, destino.getColuna());
				}
				else {
					posicaoPeao = new Posicao(4, destino.getColuna());
				}
				tabuleiro.localPeca(peao, posicaoPeao);
			}
		}		
	}
	
	private void validacaoPosicaoOrigem(Posicao posicao) {
		if (!tabuleiro.posicaoPreenchida(posicao)) {
			throw new XadrezExcecao("Erro! N�o existe pe�a na posi��o de origem");
		}
		if (jogadorAtual != ((PecaXadrez)tabuleiro.peca(posicao)).getCor()) {
			throw new XadrezExcecao("Erro! A pe�a escolhida n�o � sua.");
		}
		if (!tabuleiro.peca(posicao).seExisteMovimentoPossivel()) {
			throw new XadrezExcecao("Erro! N�o existem movimentos poss�veis para a pe�a escolhida.");
		}
	}
	
	private void validacaoPosicaoDestino(Posicao origem, Posicao destino) {
		if (!tabuleiro.peca(origem).movimentoPossivel(destino)) {
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
		for (Peca p: list) {
			if (p instanceof Rei) {
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
			if (mat[posicaoRei.getLinha()][posicaoRei.getColuna()]) {
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
		novoLocalDaPeca('a', 2, new Peao(tabuleiro, Cor.BRANCO, this));	
		novoLocalDaPeca('b', 2, new Peao(tabuleiro, Cor.BRANCO, this));
		novoLocalDaPeca('c', 2, new Peao(tabuleiro, Cor.BRANCO, this));
		novoLocalDaPeca('d', 2, new Peao(tabuleiro, Cor.BRANCO, this));	
		novoLocalDaPeca('e', 2, new Peao(tabuleiro, Cor.BRANCO, this));
		novoLocalDaPeca('f', 2, new Peao(tabuleiro, Cor.BRANCO, this));
		novoLocalDaPeca('g', 2, new Peao(tabuleiro, Cor.BRANCO, this));	
		novoLocalDaPeca('h', 2, new Peao(tabuleiro, Cor.BRANCO, this));
		      
		novoLocalDaPeca('a', 8, new Torre(tabuleiro, Cor.PRETO)); 
		novoLocalDaPeca('b', 8, new Cavalo(tabuleiro, Cor.PRETO)); 
		novoLocalDaPeca('c', 8, new Bispo(tabuleiro, Cor.PRETO));
		novoLocalDaPeca('d', 8, new Rainha(tabuleiro, Cor.PRETO));
		novoLocalDaPeca('e', 8, new Rei(tabuleiro, Cor.PRETO, this));
		novoLocalDaPeca('f', 8, new Bispo(tabuleiro, Cor.PRETO));
		novoLocalDaPeca('g', 8, new Cavalo(tabuleiro, Cor.PRETO)); 
		novoLocalDaPeca('h', 8, new Torre(tabuleiro, Cor.PRETO));            
		novoLocalDaPeca('a', 7, new Peao(tabuleiro, Cor.PRETO, this));
		novoLocalDaPeca('b', 7, new Peao(tabuleiro, Cor.PRETO, this));            
		novoLocalDaPeca('c', 7, new Peao(tabuleiro, Cor.PRETO, this));
		novoLocalDaPeca('d', 7, new Peao(tabuleiro, Cor.PRETO, this));            
		novoLocalDaPeca('e', 7, new Peao(tabuleiro, Cor.PRETO, this));
		novoLocalDaPeca('f', 7, new Peao(tabuleiro, Cor.PRETO, this));            
		novoLocalDaPeca('g', 7, new Peao(tabuleiro, Cor.PRETO, this));
		novoLocalDaPeca('h', 7, new Peao(tabuleiro, Cor.PRETO, this));            
	}
	
}