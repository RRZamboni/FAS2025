import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PrincipaGUI extends JFrame {

    private JTextArea resultadoArea;
    private JButton botaoGerar;
    private JButton botaoAnalisar;

    public PrincipaGUI() {
        // Configuração da Janela (JFrame)
        setTitle("Sistema de Auditoria de Permissões");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null); // Centraliza a janela

        // Configuração do Painel Principal
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 1. Painel de Botões (Topo)
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        
        botaoGerar = new JButton("1. Gerar Arquivo de 5000 Linhas");
        botaoAnalisar = new JButton("2. Executar Auditoria (Analisar Arquivo)");

        // Adiciona ações aos botões
        botaoGerar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                executarGeracao();
            }
        });

        botaoAnalisar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                executarAnalise();
            }
        });

        buttonPanel.add(botaoGerar);
        buttonPanel.add(botaoAnalisar);
        mainPanel.add(buttonPanel, BorderLayout.NORTH);

        // 2. Área de Resultados (Centro)
        resultadoArea = new JTextArea("Clique em 'Gerar Arquivo' para começar a simulação de dados.");
        resultadoArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        resultadoArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultadoArea);
        
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Adiciona o painel principal à janela
        add(mainPanel);
    }

    /**
     * Método acionado pelo botão "Gerar Arquivo".
     */
    private void executarGeracao() {
        resultadoArea.setText("Gerando arquivo de 5000 linhas, por favor aguarde...\n");
        // Chama o método estático da classe GeradorMassaDeDados
        String resultado = GeradorMassaDeDados.gerarArquivoPermissoes();
        resultadoArea.append(resultado);
    }

    /**
     * Método acionado pelo botão "Executar Auditoria".
     */
    private void executarAnalise() {
        resultadoArea.setText("Executando auditoria do arquivo de permissões...\n");
        // Chama o método estático da classe VerificacaoPermissoes
        String relatorio = VerificacaoPermissoes.executarAuditoria();
        resultadoArea.append(relatorio);
    }

    public static void main(String[] args) {
        // A interface gráfica deve ser criada na Event Dispatch Thread (EDT) do Swing
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new PrincipaGUI().setVisible(true);
            }
        });
    }
}