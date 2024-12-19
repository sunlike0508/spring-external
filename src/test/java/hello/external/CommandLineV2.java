package hello.external;

import java.util.List;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.DefaultApplicationArguments;

@Slf4j
public class CommandLineV2 {

    public static void main(String[] args) {

        for(String arg : args) {
            log.info("arg {}", arg);
        }

        ApplicationArguments appArgs = new DefaultApplicationArguments(args);

        log.info("SourceAges = {}", List.of(appArgs.getSourceArgs()));
        log.info("NoOptionArgs = {}", List.of(appArgs.getNonOptionArgs()));
        log.info("OptionNames={}", appArgs.getOptionNames());

        Set<String> names = appArgs.getOptionNames();

        for(String name : names) {
            log.info("option arg {}={}", name, appArgs.getOptionValues(name));
        }

        List<String> url = appArgs.getOptionValues("url");
        List<String> username = appArgs.getOptionValues("username");
        List<String> password = appArgs.getOptionValues("password");
        List<String> mode = appArgs.getOptionValues("mode");


        log.info("url={}", url);
        log.info("username={}", username);
        log.info("password={}", password);
        log.info("mode={}", mode);
    }
}
