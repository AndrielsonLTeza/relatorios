@Service
public class Modulo1AuthService {

    private final RestTemplate restTemplate;

    @Value("${modulo1.url}")
    private String modulo1Url; // ex: http://localhost:8081

    private String token; // para armazenar o token em memória (simples, pode ser melhorado)

    public Modulo1AuthService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public String loginAndGetToken(String username, String password) {
        String loginEndpoint = modulo1Url + "/api/login"; // ajuste conforme seu endpoint

        Map<String, String> loginRequest = new HashMap<>();
        loginRequest.put("username", username);
        loginRequest.put("password", password);

        ResponseEntity<Map> response = restTemplate.postForEntity(loginEndpoint, loginRequest, Map.class);

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            this.token = (String) response.getBody().get("token"); // ajuste a chave conforme o JSON de resposta
            return token;
        }

        throw new RuntimeException("Falha ao obter token do modulo1");
    }

    public String getToken() {
        if (token == null) {
            throw new RuntimeException("Token não está disponível. Faça login antes.");
        }
        return token;
    }
}
