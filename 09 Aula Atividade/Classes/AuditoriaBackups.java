
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AuditoriaBackups {

    // Constantes
    private static final String ARQUIVO_LOG = "log_backups.txt"; // Novo nome do arquivo de log
    private static final String ARQUIVO_ALERTAS = "alertas_logbackup.txt";
    private static final Duration INTERVALO_ESPERADO = Duration.ofHours(2);
    
    // Regex e Formatador de Data/Hora
    private static final Pattern LOG_PATTERN = 
        Pattern.compile("(\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}) - (INFO|ERROR) - (.*)");
    
    private static final DateTimeFormatter DATE_TIME_FORMATTER = 
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Monitora e audita os logs de backup lendo diretamente de um arquivo.
     * @param arquivoLog O caminho do arquivo de log.
     * @return Uma lista de strings contendo os alertas detectados.
     */
    public static List<String> auditarBackups(String arquivoLog) {
        List<String> alertas = new ArrayList<>();
        LocalDateTime ultimaExecucao = null;

        // Usa 'try-with-resources' para garantir que o BufferedReader e FileReader sejam fechados
        try (BufferedReader br = new BufferedReader(new FileReader(arquivoLog))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                Matcher matcher = LOG_PATTERN.matcher(linha);
                
                if (matcher.find()) {
                    LocalDateTime timestamp = LocalDateTime.parse(matcher.group(1), DATE_TIME_FORMATTER);
                    String level = matcher.group(2);
                    String message = matcher.group(3);

                    // 1. Verificar Falha (ERROR)
                    if (level.equals("ERROR")) {
                        if (message.contains("Falha ao realizar o backup")) {
                            alertas.add("Alerta: Falha no backup em " + timestamp.format(DATE_TIME_FORMATTER) + ".");
                        }
                    }

                    // 2. Verificar Início (INFO - Backup iniciado) e Atraso
                    if (level.equals("INFO") && message.equals("Backup iniciado.")) {
                        if (ultimaExecucao != null) {
                            Duration diferenca = Duration.between(ultimaExecucao, timestamp);
                            if (diferenca.compareTo(INTERVALO_ESPERADO) > 0) {
                                alertas.add("Alerta: Atraso na execução do backup. Último backup em " + 
                                            ultimaExecucao.format(DATE_TIME_FORMATTER) + 
                                            ". Nova execução em " + timestamp.format(DATE_TIME_FORMATTER) + ".");
                            }
                        }
                        ultimaExecucao = timestamp;
                    }
                }
            }
        } catch (IOException e) {
            alertas.add("ERRO FATAL: Não foi possível ler o arquivo de log: " + arquivoLog + ". " + e.getMessage());
        }

        return alertas;
    }

    /**
     * Salva os alertas em um arquivo.
     */
    public static void salvarAlertasEmArquivo(List<String> alertas, String arquivoSaida) {
        // Usa `FileWriter(..., true)` para habilitar o modo de append (adicionar ao final)
        try (PrintWriter pw = new PrintWriter(new FileWriter(arquivoSaida, true))) { 
            if (alertas.isEmpty()) {
                pw.println("Nenhum alerta. Todos os backups foram executados corretamente.");
            } else {
                for (String alerta : alertas) {
                    pw.println(alerta);
                }
            }
            System.out.println("\nAlertas salvos em: " + arquivoSaida);
        } catch (IOException e) {
            System.err.println("Erro ao salvar alertas no arquivo: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        System.out.println("--- Auditoria de Backups a partir de Arquivo ---");
        
        // 1. Executar a auditoria lendo do arquivo externo
        List<String> alertas = auditarBackups(ARQUIVO_LOG);
        
        // 2. Exibir alertas
        if (alertas.isEmpty()) {
            System.out.println("Nenhum alerta detectado.");
        } else {
            System.out.println("Alertas detectados:");
            for (String alerta : alertas) {
                System.out.println("- " + alerta);
            }
        }
        
        // 3. Salvar alertas
        salvarAlertasEmArquivo(alertas, ARQUIVO_ALERTAS);
    }
}