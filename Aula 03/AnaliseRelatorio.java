import java.io.*;
import java.util.*;
import java.util.regex.*;

public class AnaliseRelatorio {
    private static final int LIMITE_FALHAS = 3;
    private static final String CAMINHO_LOG = "C:\\Users\\Rafael\\Desktop\\forca.log";
    private static final String CAMINHO_ALERTAS = "C:\\Users\\Rafael\\Desktop\\alertas.log";

    public void analisar() {
        Map<String, Integer> falhasConsecutivas = new HashMap<>();
        List<String> alertas = new ArrayList<>();

        Pattern padraoLogin = Pattern.compile("INFO: Tentativa de login do usuário '(\\w+)'");
        Pattern padraoFalha = Pattern.compile("ERROR: Senha incorreta para o usuário '(\\w+)'");

        try (BufferedReader br = new BufferedReader(new FileReader(CAMINHO_LOG))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                Matcher matcherLogin = padraoLogin.matcher(linha);
                if (matcherLogin.find()) {
                    String usuario = matcherLogin.group(1);
                    falhasConsecutivas.put(usuario, 0);
                }

                Matcher matcherFalha = padraoFalha.matcher(linha);
                if (matcherFalha.find()) {
                    String usuario = matcherFalha.group(1);
                    int contador = falhasConsecutivas.getOrDefault(usuario, 0) + 1;
                    falhasConsecutivas.put(usuario, contador);

                    if (contador >= LIMITE_FALHAS) {
                        String alerta = "ALERTA: Múltiplas falhas para o usuário '" + usuario + "'";
                        System.out.println(alerta);
                        alertas.add(alerta);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CAMINHO_ALERTAS))) {
            for (String alerta : alertas) {
                bw.write(alerta);
                bw.newLine();
            }
            System.out.println("Relatório gerado em: " + CAMINHO_ALERTAS);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
