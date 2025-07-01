@Service
public class Modulo1Service {

    private final RestTemplate modulo1RestTemplate;

    @Value("${modulo1.url}")
    private String modulo1Url;

    public Modulo1Service(RestTemplate modulo1RestTemplate) {
        this.modulo1RestTemplate = modulo1RestTemplate;
    }

    public SomeResponse callProtectedEndpoint() {
        String url = modulo1Url + "/api/protected-resource";

        ResponseEntity<SomeResponse> response = modulo1RestTemplate.getForEntity(url, SomeResponse.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        }

        throw new RuntimeException("Erro ao acessar recurso protegido do modulo1");
    }
}
