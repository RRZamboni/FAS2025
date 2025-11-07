import java.io.File;
import java.util.List;

import javax.swing.JOptionPane;

public class Principal 
{

	public static void main(String[] args) 
	{
		//Configurar para a leitura do arquivo de log
		final String ARQUIVO_LOG = "log_banco_dados.txt";
		
		//Verificar se arquivo foi acessado
		File logFile = new File(ARQUIVO_LOG); 
		
		if(!logFile.exists())
		{
			JOptionPane.showMessageDialog(null,
					    "ERRO: Arquivo não encontrado!!! \n "
					  + "Verificar diretório:" 
					  + logFile.getAbsolutePath());
			
			//Interromper a execução
			return;
		}
		
		
		//Instanciando a classe Auditor
		Auditor auditor = new Auditor();
		
		//Lista para salvar os 'erros'
		List<String> naoConforme = 
				  auditor.auditarLog(ARQUIVO_LOG);
		
		//Apresentando os resultados
		String titulo = "Relatório de Auditoria BD";
		String mensagem;
		int tipoMensagem;
		
		if(!naoConforme.isEmpty())
		{
			//Se houver inconformidades
			StringBuilder sb = new StringBuilder();
			sb.append("CONSULTAS EM NÃO CONFIRMIDADE")
			    .append(naoConforme.size()).append("\n \n");
			
			for(String alerta : naoConforme)
			{
				sb.append(alerta).append("\n");
			}
			
			mensagem = sb.toString();
			tipoMensagem = JOptionPane.WARNING_MESSAGE;
			
			//Mostrando a mensagem 
			JOptionPane.showMessageDialog(null, mensagem,
									titulo,tipoMensagem);
			
		}
		else
		{
			JOptionPane.showMessageDialog(null,
					"Todas as consultas estão em comformidade");
		}
	}

}
