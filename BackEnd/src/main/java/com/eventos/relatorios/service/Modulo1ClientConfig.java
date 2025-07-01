@Configuration
public class Modulo1ClientConfig {

    @Bean
    public RestTemplate modulo1RestTemplate(Modulo1AuthService authService) {
        RestTemplate restTemplate = new RestTemplate();

        restTemplate.getInterceptors().add((request, body, execution) -> {
            String token = authService.getToken();
            request.getHeaders().add("Authorization", "Bearer " + token);
            return execution.execute(request, body);
        });

        return restTemplate;
    }
}

