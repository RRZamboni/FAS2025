import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GeradorDeDados {
    private static final String[] NOMES = {"João", "Maria", "Paulo", "Ana", "Carlos", "Isabela", "Ricardo", "Juliana"};
    private static final String[] SOBRENOMES = {"Silva", "Oliveira", "Souza", "Santos", "Ferreira", "Almeida", "Pereira", "Costa"};
    private static final Random random = new Random();

    /**
     * Gera um CPF (apenas formato, sem validação real de dígitos verificadores)
     * e o mascara parcialmente na maioria das vezes.
     * @param conforme Indica se o CPF deve ser mascarado corretamente (true) ou não (false) na maioria das vezes.
     * @return String contendo o CPF.
     */
    private String gerarCpf(boolean conforme) {
        String baseCpf = String.format("%011d", random.nextLong(1_000_000_000L)); // 11 dígitos

        if (conforme && random.nextInt(10) < 8) { // 80% de chance de mascarar corretamente
            return "*******" + baseCpf.substring(7); // *******XXXX
        } else if (!conforme && random.nextInt(10) < 8) { // 80% de chance de não mascarar ou mascarar errado
            if (random.nextBoolean()) {
                return baseCpf; // CPF Completo (Não Conforme)
            } else {
                return "**" + baseCpf.substring(2); // Máscara Incorreta (Não Conforme)
            }
        } else {
            return "*******" + baseCpf.substring(7); // Mascara corretamente mesmo se "não conforme" (simula falha de processo)
        }
    }

    /**
     * Gera um e-mail aleatório e o criptografa (simulação) na maioria das vezes.
     * @param conforme Indica se o e-mail deve ser criptografado (true) ou não (false) na maioria das vezes.
     * @return String contendo o E-mail.
     */
    private String gerarEmail(String nome, String sobrenome, boolean conforme) {
        String baseEmail = nome.toLowerCase() + "." + sobrenome.toLowerCase() + "@example.com";

        if (conforme && random.nextInt(10) < 8) { // 80% de chance de criptografar corretamente
            // Simulação de criptografia
            return "ENC(" + Integer.toHexString(baseEmail.hashCode()) + ")";
        } else if (!conforme && random.nextInt(10) < 8) { // 80% de chance de não criptografar
            return baseEmail; // E-mail Completo (Não Conforme)
        } else {
            // Criptografa corretamente mesmo se "não conforme" (simula falha de processo)
            return "ENC(" + Integer.toHexString(baseEmail.hashCode()) + ")";
        }
    }

    public List<Usuario> gerarUsuarios(int quantidade) {
        List<Usuario> usuarios = new ArrayList<>();
        for (int i = 0; i < quantidade; i++) {
            String nome = NOMES[random.nextInt(NOMES.length)];
            String sobrenome = SOBRENOMES[random.nextInt(SOBRENOMES.length)];
            
            // Alterna entre conforme (par) e não conforme (ímpar) para garantir variedade
            boolean deveSerConforme = (i % 2 == 0); 
            
            String cpf = gerarCpf(deveSerConforme);
            String email = gerarEmail(nome, sobrenome, deveSerConforme);
            
            usuarios.add(new Usuario(nome + " " + sobrenome, cpf, email));
        }
        return usuarios;
    }
}