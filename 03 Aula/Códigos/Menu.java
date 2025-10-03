import javax.swing.JOptionPane;

public class Menu 
{

	public static void main(String[] args) 
	{
		//Opções do menu
		String[] opcoes = {"Gerar Log","Analisar Log","Sair"};
		
		
		while(true) 
		{
			int escolha = JOptionPane.showOptionDialog(
					null,
					"Bem vindo, Escolha uma opção",
					"Ataque de força bruta",
					JOptionPane.DEFAULT_OPTION,
					JOptionPane.INFORMATION_MESSAGE, 
					null, 
					opcoes, 
					opcoes[0]);
			
			if(escolha == 0)
			{
				//Gerar o Log
				GerarLogAleatorio gerador = 
						new GerarLogAleatorio();
				gerador.gerar();
				JOptionPane.showMessageDialog(null,
						"Logs criados com sucesso");
			}
			else if(escolha == 1)
			{
				//Analisar Log
				AnaliseRelatorio analise = new AnaliseRelatorio();
				analise.analisar();
				JOptionPane.showMessageDialog(null, 
						"Relatório Analisado. \n Verifique o arquivo 'alertas.log'!");
			}
			else 
			{
				JOptionPane.showMessageDialog(null,
						"Encerrendo o sistema...");
				break;
			}
		}
	}
}
