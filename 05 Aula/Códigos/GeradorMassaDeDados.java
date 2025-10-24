import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

public class GeradorMassaDeDados {

    private static final int NUMERO_DE_LINHAS = 5000;
    public static final String ARQUIVO_PERMISSOES_NOME = "usuarios_permissoes_5000.txt";
    private static final Path ARQUIVO_PATH = Paths.get(ARQUIVO_PERMISSOES_NOME);

    /**
     * Gera um arquivo de texto com 5000 linhas de usuários e permissões simuladas.
     * @return Uma mensagem de status sobre a geração.
     */
    public static String gerarArquivoPermissoes() {
        try {
            StringBuilder conteudo = new StringBuilder();
            Random random = new Random();

            // 1. Adiciona o cabeçalho
            conteudo.append("usuario,permissao\n");

            // 2. Adiciona o usuário 'admin' especial
            conteudo.append("admin,admin\n");

            // 3. Gera as linhas de dados simulados
            for (int i = 1; i <= NUMERO_DE_LINHAS; i++) {
                String usuario = "usuario_" + i;
                // Cerca de 50% de chance de ser 'admin', criando não-conformidades
                String permissao = random.nextBoolean() ? "admin" : "regular";
                
                // Formato: usuario,permissao\n
                conteudo.append(usuario).append(",").append(permissao).append("\n");
            }
            
            // Grava todo o conteúdo no arquivo
            Files.writeString(ARQUIVO_PATH, conteudo.toString());
            
            return "Sucesso! Arquivo gerado com " + (NUMERO_DE_LINHAS + 1) + " linhas em:\n" + 
                   ARQUIVO_PATH.toAbsolutePath();

        } catch (IOException e) {
            return "Erro ao gerar o arquivo:\n" + e.getMessage();
        }
    }
}