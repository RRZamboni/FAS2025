import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Auditor {

    /**
     * Função REFORÇADA para verificar se uma consulta é suspeita, baseada nas regras originais.
     */
    private static String verificarConsultaSuspeita(String usuario, String consulta) {
        // Remove espaços extras e converte para maiúsculas para comparação segura
        String consultaUpper = consulta.toUpperCase().trim();
        String usuarioLower = usuario.toLowerCase().trim();

        // -----------------------------------------------------------------
        // Regra 1: Consultas à coluna "senha" são proibidas
        if (consultaUpper.contains("SENHA") || consultaUpper.contains("HASH_SENHA")) {
            return String.format("Consulta suspeita: Usuário '%s' tentou acessar a coluna 'senha'.", usuario);
        }

        // -----------------------------------------------------------------
        // Regra 2: Somente o admin pode realizar "SELECT *"
        // Pattern: ^SELECT\s*\*\s* -> Garante 'SELECT *' no início, ignorando espaços (o problema comum)
        Pattern selectAllPattern = Pattern.compile("^SELECT\\s*\\*"); 
        
        if (selectAllPattern.matcher(consultaUpper).find() && !usuarioLower.equals("admin")) {
            return String.format("Consulta suspeita: Usuário '%s' realizou um 'SELECT *'.", usuario);
        }

        // -----------------------------------------------------------------
        // Regra 3: Consultas DELETE ou UPDATE (ou TRUNCATE) são críticas
        // Pattern: ^(DELETE|UPDATE|TRUNCATE)\s.* -> Identifica comandos no início
        Pattern comandoCriticoPattern = Pattern.compile("^(DELETE|UPDATE|TRUNCATE)\\s.*", Pattern.CASE_INSENSITIVE);
        
        // Aplica o matcher na consulta original trimada
        Matcher matcher = comandoCriticoPattern.matcher(consulta.trim());

        if (matcher.find()) {
            String comando = matcher.group(1).toUpperCase();
            return String.format("Consulta crítica: Usuário '%s' realizou um %s.", usuario, comando);
        }

        return null;
    }

    /**
     * Função para ler o arquivo de log CSV e auditar as entradas.
     */
    public List<String> auditarLog(String caminhoArquivo) {
        List<String> consultasSuspeitas = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(caminhoArquivo))) {
            String linha;
            boolean isHeader = true;

            while ((linha = br.readLine()) != null) {
                if (isHeader) {
                    isHeader = false; 
                    continue;
                }
                
                // CRUCIAL: Remove o comentário no final da linha (ex: <-- SUSPEITA)
                if (linha.contains("<--")) {
                    linha = linha.substring(0, linha.indexOf("<--")).trim();
                }
                
                // Pula linhas vazias
                if (linha.isBlank()) {
                    continue;
                }

                // Divide a linha nos campos (limite 3 para não quebrar a consulta)
                String[] campos = linha.split(",", 3);

                if (campos.length == 3) {
                    // Usa trim() em todos os campos
                    String usuario = campos[0].trim();
                    String consulta = campos[1].trim();
                    String timestamp = campos[2].trim();

                    Usuario log = new Usuario(usuario, consulta, timestamp);

                    String resultado = verificarConsultaSuspeita(log.getUsuario(), log.getConsulta());

                    if (resultado != null) {
                        consultasSuspeitas.add(String.format("- %s - %s", resultado, log.getTimestamp()));
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo de log: " + e.getMessage());
        }

        return consultasSuspeitas;
    }
}