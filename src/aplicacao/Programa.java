package aplicacao;

import java.util.InputMismatchException;
import java.util.Scanner;

import xadrez.PecaXadrez;
import xadrez.Regras;
import xadrez.XadrezExcecao;
import xadrez.XadrezPosicao;

public class Programa {

	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		
		Regras regras = new Regras();
		
		while(true) {
			try {
				UI.limparTela();
				UI.exibirTabuleiro(regras.getPecas());
				System.out.println();
				System.out.print("Posição de origem: ");
				XadrezPosicao origem = UI.lerPosicao(sc);
				
				System.out.println();                                 //TESTE: Movimentação de jogada simples
				System.out.print("Posição de destino: ");
				XadrezPosicao destino = UI.lerPosicao(sc);
				
				PecaXadrez pecaCapturada = regras.executarJogada(origem, destino);
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