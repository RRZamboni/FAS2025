import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class VerificacaoPermissoes {

    private static final String ARQUIVO_RELATORIO_NOME = "relatorio_auditoria.txt";
    private static final Path ARQUIVO_PERMISSOES = Paths.get(GeradorMassaDeDados.ARQUIVO_PERMISSOES_NOME);
    private static final Path ARQUIVO_RELATORIO = Paths.get(ARQUIVO_RELATORIO_NOME);

    /**
     * Realiza a auditoria do arquivo de permissões.
     * @return O relatório final como String.
     */
    public static String executarAuditoria() {
        try {
            List<String> usuariosNaoConformes = verificarConformidade(ARQUIVO_PERMISSOES);
            return gerarRelatorio(usuariosNaoConformes);
        } catch (IOException e) {
            return "Erro: Não foi possível ler o arquivo de permissões. Certifique-se de que ele foi gerado.\n" + 
                   "Caminho esperado: " + ARQUIVO_PERMISSOES.toAbsolutePath() + "\n" +
                   "Detalhes: " + e.getMessage();
        }
    }

    // Função para verificar conformidade (Lógica central)
    private static List<String> verificarConformidade(Path arquivo) throws IOException {
        List<String> naoConformes = new ArrayList<>();
        List<String> linhas = Files.readAllLines(arquivo);

        for (int i = 1; i < linhas.size(); i++) { // Pular o cabeçalho
            String linha = linhas.get(i).trim();

            if (!linha.isEmpty()) {
                String[] partes = linha.split(",");
                if (partes.length == 2) {
                    String usuario = partes[0].trim();
                    String permissao = partes[1].trim();

                    // Lógica: Se a permissão for 'admin' E o usuário NÃO for o 'admin' especial
                    if (permissao.equals("admin") && !usuario.equals("admin")) {
                        naoConformes.add(usuario);
                    }
                }
            }
        }
        return naoConformes;
    }

    // Função para gerar o relatório em console e arquivo
    private static String gerarRelatorio(List<String> usuariosNaoConformes) throws IOException {
        StringBuilder relatorio = new StringBuilder();

        if (usuariosNaoConformes.isEmpty()) {
            relatorio.append("Todos os usuários estão em conformidade.\n");
        } else {
            relatorio.append("--- Relatório de Não Conformidade ---\n");
            relatorio.append("Usuários encontrados com permissão 'admin' sem ser o 'admin' especial:\n");
            for (String usuario : usuariosNaoConformes) {
                relatorio.append("- ").append(usuario).append("\n");
            }
            relatorio.append("Total de Não Conformidades: ").append(usuariosNaoConformes.size()).append("\n");
        }
        
        // Salva o relatório no arquivo
        Files.writeString(ARQUIVO_RELATORIO, relatorio.toString());
        relatorio.append("\nRelatório de auditoria salvo em: ").append(ARQUIVO_RELATORIO.toAbsolutePath());

        return relatorio.toString();
    }
}