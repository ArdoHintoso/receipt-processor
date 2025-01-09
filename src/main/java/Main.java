import config.AppConfig;
import org.slf4j.Logger;
import utils.LoggerUtil;

public class Main {
    private static final Logger logger = LoggerUtil.getLogger(Main.class);
    private static final int PORT = 7070;

    public static void main(String[] args) {
        logger.info("Starting Fetch Rewards Receipt Processor application");

        AppConfig appConfig = new AppConfig();
        appConfig.start(PORT);

        logger.info("Application started successfully on port {}", PORT);
    }
}
