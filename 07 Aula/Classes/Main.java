import javax.swing.JOptionPane;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

public class Main {

    private static final String ARQUIVO_DADOS = "dados_usuarios.txt";
    private static final String ARQUIVO_RELATORIO = "relatorio_conformidade.txt";
    private static List<Usuario> usuarios = new ArrayList<>();
    
    // Instâncias das classes de serviço
    private static final GeradorDeDados gerador = new GeradorDeDados();
    private static final VerificadorConformidade verificador = new VerificadorConformidade();

    public static void main(String[] args) {
        // Garantir que a aplicação Swing seja iniciada na thread de eventos (boa prática)
        javax.swing.SwingUtilities.invokeLater(Main::exibirMenu);
    }

    private static void exibirMenu() {
        Object[] opcoes = {
            "1. Gerar Dados Aleatórios", 
            "2. Verificar Conformidade", 
            "3. Sair"
        };
        
        int escolha;
        do {
            // Exibe o menu principal com opções de botões
            escolha = JOptionPane.showOptionDialog(
                null, 
                "Selecione uma opção:", 
                "Sistema de Conformidade de Dados",
                JOptionPane.DEFAULT_OPTION, 
                JOptionPane.QUESTION_MESSAGE, 
                null, 
                opcoes, 
                opcoes[0] // Opção padrão
            );

            // O retorno é o índice do array 'opcoes'
            switch (escolha) {
                case 0: // 1. Gerar Dados Aleatórios
                    gerarDados();
                    break;
                case 1: // 2. Verificar Conformidade
                    verificarEGerarRelatorio();
                    break;
                case 2: // 3. Sair ou Cancelar (clique no X ou 'cancelar')
                    JOptionPane.showMessageDialog(null, "Encerrando o sistema. Tchau!");
                    break;
                default:
                    // Caso o usuário feche a janela (cancelar)
                    JOptionPane.showMessageDialog(null, "Encerrando o sistema. Tchau!");
                    escolha = 2; // Força a saída do loop
                    break;
            }
        } while (escolha != 2);
    }

    private static void gerarDados() {
        try {
            // Solicita ao usuário a quantidade de usuários
            String input = JOptionPane.showInputDialog(
                null, 
                "Quantos usuários aleatórios deseja gerar?", 
                "Gerar Dados", 
                JOptionPane.QUESTION_MESSAGE
            );
            
            if (input == null) return; // Usuário clicou em Cancelar
            
            int quantidade = Integer.parseInt(input);
            
            if (quantidade <= 0) {
                 JOptionPane.showMessageDialog(null, "A quantidade deve ser maior que zero.", "Erro", JOptionPane.ERROR_MESSAGE);
                 return;
            }

            // Geração e salvamento
            usuarios = gerador.gerarUsuarios(quantidade);
            salvarDadosUsuarios(usuarios, ARQUIVO_DADOS);
            
            JOptionPane.showMessageDialog(
                null, 
                "✅ Dados de " + usuarios.size() + " usuários gerados e salvos em:\n" + ARQUIVO_DADOS,
                "Sucesso",
                JOptionPane.INFORMATION_MESSAGE
            );
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Entrada inválida. Por favor, digite um número inteiro.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void verificarEGerarRelatorio() {
        if (usuarios.isEmpty()) {
            // Tentativa de carregar dados existentes, caso o usuário não tenha gerado
            // (Para um projeto real, você precisaria implementar a leitura do arquivo aqui)
             int resposta = JOptionPane.showConfirmDialog(
                null, 
                "Não há dados de usuários na memória. Deseja gerar novos dados agora?", 
                "Dados Ausentes", 
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
            );
            
            if (resposta == JOptionPane.YES_OPTION) {
                gerarDados();
                // Se os dados foram gerados, continuamos a verificação
                if (usuarios.isEmpty()) return;
            } else {
                return;
            }
        }

        // Realiza a verificação
        List<String> naoConformes = verificador.verificarConformidade(usuarios);
        
        // Salva o relatório
        salvarRelatorio(naoConformes, ARQUIVO_RELATORIO);

        // Exibe o resultado da verificação
        String mensagem;
        if (naoConformes.isEmpty()) {
            mensagem = "🎉 Todos os usuários (" + usuarios.size() + ") estão em conformidade com a proteção de dados.";
        } else {
            String listaNaoConformes = String.join("\n- ", naoConformes.subList(0, Math.min(5, naoConformes.size())));
            
            mensagem = "🚨 Usuários em não conformidade: " + naoConformes.size() + " ocorrências.\n"
                     + "Detalhes (Primeiros 5):\n- " + listaNaoConformes + "\n\n"
                     + "Relatório completo salvo em:\n" + ARQUIVO_RELATORIO;
        }

        JOptionPane.showMessageDialog(
            null, 
            mensagem, 
            "Relatório de Conformidade",
            JOptionPane.INFORMATION_MESSAGE
        );
    }
    
    // Métodos de I/O (Mantidos do Main original)

    /**
     * Salva a lista de usuários no formato CSV.
     */
    private static void salvarDadosUsuarios(List<Usuario> usuarios, String nomeArquivo) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(nomeArquivo))) {
            writer.write("nome,cpf,email\n");
            for (Usuario usuario : usuarios) {
                writer.write(usuario.toString() + "\n");
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Erro ao salvar dados dos usuários: " + e.getMessage(), "Erro de I/O", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Salva o relatório de não conformidade em um arquivo.
     */
    private static void salvarRelatorio(List<String> naoConformes, String nomeArquivo) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(nomeArquivo))) {
            if (naoConformes.isEmpty()) {
                writer.write("Todos os usuários estão em conformidade com a proteção de dados.\n");
            } else {
                writer.write("Usuários em não conformidade com a proteção de dados:\n");
                for (String usuario : naoConformes) {
                    writer.write("- " + usuario + "\n");
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Erro ao salvar relatório: " + e.getMessage(), "Erro de I/O", JOptionPane.ERROR_MESSAGE);
        }
    }
}