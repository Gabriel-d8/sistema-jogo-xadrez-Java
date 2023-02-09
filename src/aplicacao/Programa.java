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
		Regras regrasPartida = new Regras();
		List<PecaXadrez> capturadas = new ArrayList<>();
		
		while (!regrasPartida.getChequeMate()) {
			try {
				UI.limparTela();
				UI.exibirPartida(regrasPartida, capturadas);
				System.out.println();
				System.out.print("Digite a osi��o de origem da pe�a: ");
				XadrezPosicao origem = UI.lerPosicao(sc);
				
				System.out.println();                                 
				System.out.print("Digite a posi��o de destino da pe�a: ");
				XadrezPosicao destino = UI.lerPosicao(sc);
				
				boolean [][] movimentosPossiveis = regrasPartida.movimentosPossiveis(origem);
				UI.limparTela();
				UI.exibirTabuleiro(regrasPartida.getPecas(), movimentosPossiveis);
				
				PecaXadrez pecaCapturada = regrasPartida.executarJogada(origem, destino);
				
				if (pecaCapturada != null) {
					capturadas.add(pecaCapturada);
				}
				
				if (regrasPartida.getPromocao() != null) {
					System.out.print("Digite a pe�a que ser� promovida (B/C/Ra/T: ");
					String tipoDaPeca = sc.nextLine().toUpperCase();
					while (!tipoDaPeca.equals("Re") && !tipoDaPeca.equals("T") &&  !tipoDaPeca.equals("C") && !tipoDaPeca.equals("B")) {
						System.out.print("Digite a pe�a que ser� promovida (B/C/Ra/T: ");
						tipoDaPeca = sc.nextLine().toUpperCase();			
					}
					regrasPartida.substituirPecaPromovida(tipoDaPeca);
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
		UI.exibirPartida(regrasPartida, capturadas);
	}

}