package aplicacao;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import xadrez.PecaXadrez;
import xadrez.Regras;
import xadrez.XadrezExcecao;
import xadrez.XadrezPosicao;

public class Programa {

	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		Regras regras = new Regras();
		List<PecaXadrez> capturadas = new ArrayList<>();
		
		while(true) {
			try {
				UI.limparTela();
				UI.exibirPartida(regras, capturadas);
				System.out.println();
				System.out.print("Posição de origem: ");
				XadrezPosicao origem = UI.lerPosicao(sc);
				
				System.out.println();                                 
				System.out.print("Posição de destino: ");
				XadrezPosicao destino = UI.lerPosicao(sc);
				
				boolean [][] movimentosPossiveis = regras.movimentosPossiveis(origem);
				UI.limparTela();
				UI.exibirTabuleiro(regras.getPecas(), movimentosPossiveis);
				
				PecaXadrez pecaCapturada = regras.executarJogada(origem, destino);
				
				if (pecaCapturada != null) {
					capturadas.add(pecaCapturada);
				}
			}
			catch(XadrezExcecao e) {
				System.out.println(e.getMessage());
				sc.nextLine();
			}
			catch(InputMismatchException e) {
				System.out.println(e.getMessage());
				sc.nextLine();
			}
		}
		
	}

}