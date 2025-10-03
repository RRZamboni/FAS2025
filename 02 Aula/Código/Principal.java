package parte2;


import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Principal {

    private static final int LIMITE_FALHAS = 3;

    // Caminhos
    private static final String CAMINHO_LOG = "C:\\Users\\Rafael\\Desktop\\auth.log";
    private static final String CAMINHO_ALERTAS = "C:\\Users\\Rafael\\Desktop\\alertas.log";

    public static void main(String[] args) {
        // Primeiro, gerar logs de exemplo
        criarLogs();

        // Depois, processar e gerar alertas
        processarLogs();
    }

    // 🔹 Método para gerar logs no formato [data hora] MENSAGEM
    public static void criarLogs() {
        String[] usuarios = {"joao", "maria", "rafael", "ana"};
        Random random = new Random();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CAMINHO_LOG))) {
            for (int i = 0; i < 200; i++) {  // gera 20 linhas de log
                String usuario = usuarios[random.nextInt(usuarios.length)];
                String dataHora = "[" + sdf.format(new Date()) + "] ";

                if (random.nextBoolean()) {
                    bw.write(dataHora + "INFO: Tentativa de login do usuário '" + usuario + "'.");
                } else {
                    bw.write(dataHora + "ERROR: Senha incorreta para o usuário '" + usuario + "'.");
                }
                bw.newLine();

                // Só pra simular passagem de tempo
                //try { Thread.sleep(200); } catch (InterruptedException ignored) {}
            }
            System.out.println("Arquivo de log criado em: " + CAMINHO_LOG);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 🔹 Método para processar os logs (coloca aqui seu código de análise com regex)
    public static void processarLogs() {
    	 // Dicionário (mapa) para contar as falhas de login consecutivas por usuário
        HashMap<String, Integer> falhasConsecutivas = new HashMap<>();

        // Regex para identificar falha de senha: captura o nome do usuário
        Pattern padraoFalha = Pattern.compile("ERROR: Senha incorreta para o usuário '(\\w+)'");
        // Regex para identificar tentativa de login: captura o nome do usuário
        Pattern padraoLogin = Pattern.compile("INFO: Tentativa de login do usuário '(\\w+)'");

        try (
            // Leitor de arquivo (entrada: auth.log)
            BufferedReader br = new BufferedReader(new FileReader(CAMINHO_LOG));
            // Escritor de arquivo (saída: alertas.log)
            BufferedWriter bw = new BufferedWriter(new FileWriter(CAMINHO_ALERTAS))
        ) {
            String linha;

            // Percorre cada linha do arquivo de log
            while ((linha = br.readLine()) != null) {

                // Verifica se a linha indica uma tentativa de login
                Matcher matchLogin = padraoLogin.matcher(linha);
                if (matchLogin.find()) {
                    String usuario = matchLogin.group(1);
                    // Reseta contador de falhas desse usuário ao tentar logar novamente
                    falhasConsecutivas.put(usuario, 0);
                }

                // Verifica se a linha indica uma falha de autenticação
                Matcher matchFalha = padraoFalha.matcher(linha);
                if (matchFalha.find()) {
                    String usuario = matchFalha.group(1);

                    // Incrementa a contagem de falhas consecutivas para esse usuário
                    falhasConsecutivas.put(usuario, falhasConsecutivas.getOrDefault(usuario, 0) + 1);

                    // Se passou do limite, escreve alerta no arquivo
                    if (falhasConsecutivas.get(usuario) >= LIMITE_FALHAS) {
                        String alerta = "Alerta: Múltiplas falhas consecutivas para o usuário '" + usuario + "'";
                        bw.write(alerta);
                        bw.newLine();  // Pula linha no arquivo
                    }
                }
            }

            // Mensagem no console após terminar
            System.out.println("Verificação concluída. Alertas gravados em: " + CAMINHO_ALERTAS);

        } catch (IOException e) {
            e.printStackTrace(); // Em caso de erro, mostra stacktrace
        }
    	
        // Código de análise dos logs que você já tem
    }
}
