import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class VerificacaoPermissoes 
{
	//Caminhos para gerar os relatórios
	private static final String ARQUIVO_NOME = "relatorio_auditoria.txt";
	private static final Path ARQUIVO_PERMISSOES = Paths.get(
			       GeradorMassaDeDados.ARQUIVO_PERMISSOES_NOME);
	private static final Path ARQUIVO_RELATORIO = Paths.get(ARQUIVO_NOME);
	
	
	//Métodos
	public static String executarAuditoria()
	{
		try 
		{
			//Lista dos usuarios não conformes
			List<String> usuarioNaoConformes = verificarConformidade(ARQUIVO_PERMISSOES);
			return gerarRelatorio(usuarioNaoConformes);
		}
		catch (IOException e) 
		{
			return "ERRO: Não foi possível criar o arquivo!!!";
		}
	}
	
	public static List<String> verificarConformidade(Path arquivo) throws IOException
	{
		//Lista vazia para receber os não conformes
		List<String> naoConformes = new ArrayList<String>();
		List<String> linhas = Files.readAllLines(arquivo);
		
		//Percorrendo as linhas do arquivo
		for(int i=1;i < linhas.size(); i++)
		{
			//Recebendo valor da linha do arquivo
			String linha = linhas.get(i).trim();
			
			if(!linha.isEmpty())
			{
				//Separando o nome e função(usuário) do arquivo
				String[] partes = linha.split(",");
				
				if(partes.length == 2)
				{
					String usuario = partes[0];
					String permissao = partes[1];
							
					// Se a permissão for 'admin' e o usuário não for 'admin'
					if(permissao.equals("admin") && !usuario.equals("admin"))
					{
						naoConformes.add(usuario);
					}
				}
			}
			
			return naoConformes;
		}//métodos
		
		
		
	}
	
	public static String gerarRelatorio(List<String> usuariosNaoConformes) throws IOException
	{
		
	}
	
	
	
	
	
	
	
	
	
	
	

}



