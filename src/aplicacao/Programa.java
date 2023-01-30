package aplicacao;

import xadrez.Regras;

public class Programa {

	public static void main(String[] args) {
		
		Regras regras = new Regras();
		UI.exibirTabuleiro(regras.getPecas());
		
	}

}