package config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by madhukar on 8/08/19.
 */
@Data
@Component
public class AppConfig {

    @Value("${input.file.name}")
    private String inputFileName;

    @Value("${is.logmethodexecutiontime}")
    private boolean isLogMethodExecutionTime;

}
