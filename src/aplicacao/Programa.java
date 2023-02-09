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
		
		while (!regras.getChequeMate()) {
			try {
				UI.limparTela();
				UI.exibirPartida(regras, capturadas);
				System.out.println();
				System.out.print("Digite a osi��o de origem da pe�a: ");
				XadrezPosicao origem = UI.lerPosicao(sc);
				
				System.out.println();                                 
				System.out.print("Digite a posi��o de destino da pe�a: ");
				XadrezPosicao destino = UI.lerPosicao(sc);
				
				boolean [][] movimentosPossiveis = regras.movimentosPossiveis(origem);
				UI.limparTela();
				UI.exibirTabuleiro(regras.getPecas(), movimentosPossiveis);
				
				PecaXadrez pecaCapturada = regras.executarJogada(origem, destino);
				
				if (pecaCapturada != null) {
					capturadas.add(pecaCapturada);
				}
				
				if (regras.getPromocao() != null) {
					System.out.print("Digite a pe�a que ser� promovida (B/C/Ra/T: ");
					String tipoDaPeca = sc.nextLine();
					regras.substituirPecaPromovida(tipoDaPeca);
				}
			}
			catch (XadrezExcecao e) {
				System.out.println(e.getMessage());
				sc.nextLine();
			}
			catch (InputMismatchException e) {
				System.out.println(e.getMessage());
				sc.nextLine();
			}
		}
		UI.limparTela();
		UI.exibirPartida(regras, capturadas);
	}

}