package au.com.seek.automatedcounter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Created by madhukar on 8/08/19.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrafficData {
    private Integer count;
    private LocalDateTime dateTime;
}
