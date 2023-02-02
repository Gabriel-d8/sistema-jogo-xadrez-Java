package jogoTabuleiro;

public class Tabuleiro {

	private int linhas;
	private int colunas;
	private Peca[][] pecas;
	
	public Tabuleiro(int linhas, int colunas) {
		if(linhas < 1 || colunas < 1) {
			throw new TabuleiroExcecao("Erro ao criar o tabuleiro! É necessário que haja no mínimo 1 linha e 1 coluna.");    
		}
		this.linhas = linhas;
		this.colunas = colunas;
		this.pecas = new Peca[linhas][colunas];
	}

	public int getLinhas() {
		return linhas;
	}

	public int getColunas() {
		return colunas;
	}                              

	public Peca peca(int linha, int coluna) {
		if (!posicaoExistente(linha, coluna)) {
			throw new TabuleiroExcecao("Posição inexistente!");
		}
		return pecas[linha][coluna];
	}
	
	public Peca peca(Posicao posicao) {
		if (!posicaoExistente(posicao)) {
			throw new TabuleiroExcecao("Posição inexistente!");
		}
		return pecas[posicao.getLinha()] [posicao.getColuna()];
	}
	
	public void localPeca(Peca peca, Posicao posicao) {
		if (posicaoPreenchida(posicao)) {
			throw new TabuleiroExcecao("Posição '" + posicao + "' inválida! Já existe uma peça na posição escolhida.");
		}
		pecas[posicao.getLinha()][posicao.getColuna()] = peca;
		peca.posicao = posicao;
	}
	
	public Peca removerPeca(Posicao posicao) {
		if (!posicaoExistente(posicao)) {
			throw new TabuleiroExcecao("Posição inexistente!");
		}
		if (peca(posicao) == null) {
			return null;
		}
		Peca aux = peca(posicao);
		aux.posicao = null;
		pecas[posicao.getLinha()][posicao.getColuna()] = null;
		return aux;
	}
	
	private boolean posicaoExistente(int linha, int coluna) {
		return linha >= 0 && linha < linhas && coluna >= 0 && coluna < colunas;
	}
	
	public boolean posicaoExistente(Posicao posicao) {
		return posicaoExistente(posicao.getLinha(), posicao.getColuna());
	}
	
	public boolean posicaoPreenchida(Posicao posicao) {
		if (!posicaoExistente(posicao)) {
			throw new TabuleiroExcecao("Posição inexistente!");
		}
		return peca(posicao) != null;
	}
	
}