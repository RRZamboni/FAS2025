import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VerificadorConformidade {

    // Regex para CPF mascarado: 7 asteriscos seguidos de 4 dígitos
    private static final Pattern CPF_MASCARADO_PATTERN = Pattern.compile("^\\*{7}\\d{4}$");
    
    // Regex para E-mail criptografado: Começa com 'ENC(' e termina com ')'
    private static final Pattern EMAIL_CRIPTOGRAFADO_PATTERN = Pattern.compile("^ENC\\(.+\\)$");

    /**
     * Verifica se o CPF está mascarado corretamente (*******1234).
     */
    public boolean verificarCpfMascarado(String cpf) {
        Matcher matcher = CPF_MASCARADO_PATTERN.matcher(cpf);
        return matcher.matches();
    }

    /**
     * Simula a verificação de e-mail criptografado (formato ENC(...)).
     */
    public boolean verificarEmailCriptografado(String email) {
        Matcher matcher = EMAIL_CRIPTOGRAFADO_PATTERN.matcher(email);
        return matcher.matches();
    }

    /**
     * Verifica a conformidade de uma lista de usuários.
     * @param usuarios A lista de usuários a ser verificada.
     * @return Uma lista de strings descrevendo os motivos de não conformidade.
     */
    public List<String> verificarConformidade(List<Usuario> usuarios) {
        List<String> naoConformes = new ArrayList<>();

        for (Usuario usuario : usuarios) {
            String nome = usuario.getNome();
            String cpf = usuario.getCpf();
            String email = usuario.getEmail();

            // 1. Verificar se o CPF está mascarado
            if (!verificarCpfMascarado(cpf)) {
                naoConformes.add(nome + " - CPF não está mascarado corretamente: " + cpf);
            }

            // 2. Verificar se o e-mail está criptografado
            if (!verificarEmailCriptografado(email)) {
                naoConformes.add(nome + " - E-mail não está criptografado: " + email);
            }
        }

        return naoConformes;
    }
}