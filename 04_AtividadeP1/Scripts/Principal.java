import javax.swing.JOptionPane;

public class Principal {
    public static void main(String[] args) {
        String[] opcoes = {"Gerar Log", "Analisar Log", "Gerar + Analisar", "Sair"};

        while (true) {
            int escolha = JOptionPane.showOptionDialog(
                null,
                "O que deseja fazer?",
                "Sistema de Auditoria - Força Bruta",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                opcoes,
                opcoes[0]
            );

            if (escolha == 0) {
                // Gerar log
                GerarLogAleatorio gerador = new GerarLogAleatorio();
                gerador.gerar();
                JOptionPane.showMessageDialog(null, "Log gerado com sucesso!");
            } else if (escolha == 1) {
                // Analisar log
                AnaliseRelatorio analise = new AnaliseRelatorio();
                analise.analisar();
                JOptionPane.showMessageDialog(null, "Relatório analisado.\nVerifique o arquivo alertas.log");
            } else if (escolha == 2) {
                // Gerar + Analisar
                GerarLogAleatorio gerador = new GerarLogAleatorio();
                gerador.gerar();
                AnaliseRelatorio analise = new AnaliseRelatorio();
                analise.analisar();
                JOptionPane.showMessageDialog(null, "Log gerado e analisado.\nRelatório salvo em alertas.log");
            } else {
                // Sair
                JOptionPane.showMessageDialog(null, "Encerrando o sistema...");
                break;
            }
        }
    }
}
