import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class GerarLogAleatorio {
    private static final int NUM_LINHAS = 5000;
    private static final String ARQUIVO_SAIDA = "C:\\Users\\Rafael\\Desktop\\forca.log";
    private static final DateTimeFormatter TS_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    private static final String[] USERS = {"Alice","Bruno","Marlon","Pedro","Don Ramon","Claudio"};
    private static final String[] IPS = {"192.168.1.10","10.0.0.5","203.0.113.12","8.8.8.8"};
    private static final double PROB_INFO = 0.7;

    private static final String USUARIO_ALVO_ATAQUE = "alice";
    private static final String IP_ORIGEM_ATAQUE = "203.0.113.12";
    private static final int NUM_RAJADAS = 5;
    private static final int TAMANHO_RAJA = 20;
    private static final int INTERVALO_RAJA = 1000;

    public void gerar() {
        Random rnd = new Random();
        LocalDateTime tsAtual = LocalDateTime.now();

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARQUIVO_SAIDA))) {
            int rajadaCount = 0;
            int linha = 0;

            while (linha < NUM_LINHAS) {
                if (rajadaCount < NUM_RAJADAS && linha > 0 && linha % INTERVALO_RAJA == 0) {
                    for (int i = 0; i < TAMANHO_RAJA && linha < NUM_LINHAS; i++) {
                        tsAtual = tsAtual.plusSeconds(1);
                        String log = buildLogLine(tsAtual, IP_ORIGEM_ATAQUE, "ERROR",
                                "Senha incorreta para o usuário '" + USUARIO_ALVO_ATAQUE + "'");
                        bw.write(log);
                        bw.newLine();
                        linha++;
                    }
                    rajadaCount++;
                    continue;
                }

                String usuario = USERS[rnd.nextInt(USERS.length)];
                String ip = IPS[rnd.nextInt(IPS.length)];
                tsAtual = tsAtual.plusSeconds(1 + rnd.nextInt(5));
                boolean isInfo = rnd.nextDouble() < PROB_INFO;

                if (isInfo) {
                    bw.write(buildLogLine(tsAtual, ip, "INFO", "Tentativa de login do usuário '" + usuario + "'"));
                } else {
                    bw.write(buildLogLine(tsAtual, ip, "ERROR", "Senha incorreta para o usuário '" + usuario + "'"));
                }
                bw.newLine();
                linha++;
            }

            System.out.println("Arquivo '" + ARQUIVO_SAIDA + "' gerado com sucesso.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String buildLogLine(LocalDateTime ts, String ip, String tipo, String mensagem) {
        return TS_FMT.format(ts) + " - " + ip + " - " + tipo + ": " + mensagem;
    }
}
