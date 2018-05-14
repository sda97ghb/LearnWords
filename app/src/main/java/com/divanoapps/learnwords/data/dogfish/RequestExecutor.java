package com.divanoapps.learnwords.data.dogfish;

import java.util.Map;

/**
 * Created by dmitry on 13.05.18.
 */

public interface RequestExecutor {
    ApiResponse request(Map<String, Object> body);
}
