import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.regex.*;

public class AuditoriaAPI {

    // Função para auditar logs da API
    public static List<String> auditarLogsAPI(String arquivoLog) {
        List<String> alertas = new ArrayList<>();

        try {
            List<String> linhas = Files.readAllLines(Paths.get(arquivoLog));

            // Regex para capturar dados do log
            Pattern pattern = Pattern.compile("(\\d+\\.\\d+\\.\\d+\\.\\d+) - - \\[.*?\\] \"(\\w+) (\\S+) HTTP\\/\\d\\.\\d\" (\\d{3}) - Token: (\\S+)");

            for (String linha : linhas) {
                Matcher matcher = pattern.matcher(linha);

                if (matcher.find()) {
                    String ip = matcher.group(1);
                    String metodoHttp = matcher.group(2);
                    String url = matcher.group(3);
                    int status = Integer.parseInt(matcher.group(4));
                    String token = matcher.group(5);

                    // Verifica tentativas em /admin/ com erro 403 ou sem token
                    if (url.contains("/api/admin/") && (token.equals("-") || status == 403)) {
                        alertas.add("Alerta: Tentativa de acesso não autorizado ao endpoint '" + url +
                                "' pelo IP " + ip + " - Status " + status);
                    }

                    // Verifica requisições sem token com status 401
                    if (token.equals("-") && status == 401) {
                        alertas.add("Alerta: Requisição não autenticada ao endpoint '" + url +
                                "' pelo IP " + ip);
                    }
                }
            }

        } catch (IOException e) {
            System.out.println("Erro ao ler arquivo de log: " + e.getMessage());
        }

        return alertas;
    }

    // Exibir alertas no console
    public static void exibirAlertas(List<String> alertas) {
        if (alertas.isEmpty()) {
            System.out.println("Nenhuma atividade suspeita detectada.");
        } else {
            System.out.println("Alertas detectados:");
            alertas.forEach(alerta -> System.out.println("- " + alerta));
        }
    }

    // Salvar alertas em arquivo
    public static void salvarAlertasEmArquivo(List<String> alertas, String arquivoSaida) {
        try (FileWriter writer = new FileWriter(arquivoSaida, true)) {
            if (alertas.isEmpty()) {
                writer.write("Nenhuma atividade suspeita detectada.\n");
            } else {
                for (String alerta : alertas) {
                    writer.write(alerta + "\n");
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao salvar alertas: " + e.getMessage());
        }
    }

    // Método principal
    public static void main(String[] args) {
        String arquivoLog = "log_api.txt";

        List<String> alertas = auditarLogsAPI(arquivoLog);
        exibirAlertas(alertas);
        salvarAlertasEmArquivo(alertas, "alertas_api.txt");
    }
}
