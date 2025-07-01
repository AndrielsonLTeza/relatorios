@Component
public class StartupRunner {

    private final Modulo1AuthService authService;

    @Value("${modulo1.username}")
    private String username;

    @Value("${modulo1.password}")
    private String password;

    public StartupRunner(Modulo1AuthService authService) {
        this.authService = authService;
    }

    @PostConstruct
    public void onStartup() {
        authService.loginAndGetToken(username, password);
    }
}
