import java.io.BufferedWriter;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class Logs 
{
	//Método criar logs
	public void CriarLogs()
	{
		//Criando usuários
		String[] usuario = {"Pedro","Rafael","Bruna"};
		
		//Classe para gerar logs aleatórios
		Random random = new Random();
		
		//Caminho do arquivo
		String caminhoLog = "C:\\Users\\Aluno\\Desktop\\Logs\\auth.log";
		
		//Data 
		SimpleDateFormat data = 
				   new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		
		//Tentando criar o arquivo no desktop
		try(BufferedWriter bw =  new BufferedWriter(new FileWriter(caminhoLog))) 
		{
			//Criar 20 linha de log
			for(int i = 0; i < 20; i++)
			{
				//Salvando um usuario aleatoriamente na variável
				String user = usuario[random.nextInt(usuario.length)];
				String dataHora =  data.format(new Date());
				
				//Gerando os logs no arquivo, aleatoriamente
				if(random.nextBoolean())
				{
					bw.write(dataHora + "INFO: Tentativa de login do usuário" + user);
				}
				else
				{
					bw.write(dataHora + "ERROR: Senha incorreta para o usuário" + user);
				}
				bw.newLine();				
			}
			
			System.out.println("Arquivo de log criado em:");
			
		} 
		catch (Exception e) 
		{
			System.out.println("Erro ao criar o arquivo!");
		
		}
		
	}

}
