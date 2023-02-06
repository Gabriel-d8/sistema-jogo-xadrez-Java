package aplicacao;

import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import xadrez.Cor;
import xadrez.PecaXadrez;
import xadrez.Regras;
import xadrez.XadrezPosicao;

public class UI {

	// https://stackoverflow.com/questions/5762491/how-to-print-color-in-console-using-system-out-println

	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_BLACK = "\u001B[30m";
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_GREEN = "\u001B[32m";
	public static final String ANSI_YELLOW = "\u001B[33m";
	public static final String ANSI_BLUE = "\u001B[34m";
	public static final String ANSI_PURPLE = "\u001B[35m";
	public static final String ANSI_CYAN = "\u001B[36m";
	public static final String ANSI_WHITE = "\u001B[37m";

	public static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
	public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
	public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
	public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
	public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
	public static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
	public static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
	public static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";
	
	public static void limparTela() {
		System.out.println("\033[H\033[2J");
		System.out.flush();
	}
	
	public static XadrezPosicao lerPosicao(Scanner sc) {
		try {
			String s = sc.nextLine();
			char coluna = s.charAt(0);
			int linha = Integer.parseInt(s.substring(1));
			return new XadrezPosicao(coluna, linha);
		}
		catch (RuntimeException e) {
			throw new InputMismatchException("Erro! Digite valores válidos entre a1 e h8");
		}
	}
	
	public static void exibirPartida(Regras partida, List<PecaXadrez> capturadas) {
		exibirTabuleiro(partida.getPecas());
		System.out.println();
		exibirPecasCapturadas(capturadas);
		System.out.println();
		System.out.println("Vez " + partida.getVez());
		if (!partida.getChequeMate()) {
			System.out.println("Aguardando o jogador " + partida.getJogadorAtual());
			if(partida.getCheque()) {
				System.out.println("CHEQUE!"); 
			}
		}	
		else {
			System.out.println("CHEQUE MATE!");
			System.out.println("Vencedor: " + partida.getJogadorAtual());
		}
	}

	public static void exibirTabuleiro(PecaXadrez[][] pecas) {
		for (int i = 0; i < pecas.length; i++) {
			System.out.print((8 - i) + " ");
			for (int j = 0; j < pecas.length; j++) {
				exibirPeca(pecas[i][j], false);
			}
			System.out.println();
		}
		System.out.println("  a b c d e f g h");
	}
	
	public static void exibirTabuleiro(PecaXadrez[][] pecas, boolean[][] movimentosPossiveis) {
		for (int i = 0; i < pecas.length; i++) {
			System.out.print((8 - i) + " ");
			for (int j = 0; j < pecas.length; j++) {
				exibirPeca(pecas[i][j], movimentosPossiveis[i][j]);
			}
			System.out.println();
		}
		System.out.println("  a b c d e f g h");
	}
	
	private static void exibirPeca(PecaXadrez peca, boolean background) {
		if(background) {
			System.out.println(ANSI_BLUE_BACKGROUND);
		}
		if (peca == null) {
			System.out.print("-" + ANSI_RESET);
		} 
		else {
			if (peca.getCor() == Cor.BRANCO) {
				System.out.println(ANSI_WHITE + peca + ANSI_RESET);
			}
			else {
				System.out.println(ANSI_YELLOW + peca + ANSI_RESET);
			}
		}
		System.out.print(" "); 
	}
	
	private static void exibirPecasCapturadas(List<PecaXadrez> capturadas) {
		List<PecaXadrez> brancas = capturadas.stream().filter(x -> x.getCor() == Cor.BRANCO).collect(Collectors.toList());
		List<PecaXadrez> pretas = capturadas.stream().filter(x -> x.getCor() == Cor.PRETO).collect(Collectors.toList());
		System.out.println("Peças capturadas:");
		System.out.print("Brancas:");
		System.out.print(ANSI_WHITE);
		System.out.println(Arrays.toString(brancas.toArray()));
		System.out.print(ANSI_RESET);
		System.out.print("Pretas:");
		System.out.print(ANSI_BLACK);
		System.out.println(Arrays.toString(pretas.toArray()));
		System.out.print(ANSI_RESET);
	}

}