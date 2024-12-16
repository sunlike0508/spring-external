package hello.external;


import java.util.Map;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OsEnv {

    public static void main(String[] args) {
        Map<String, String> getenv = System.getenv();

        getenv.forEach((key, value) -> {
            log.info("key={} value={}", key, value);
        });
    }
}
