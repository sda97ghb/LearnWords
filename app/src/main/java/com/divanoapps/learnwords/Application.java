package com.divanoapps.learnwords;

import com.divanoapps.learnwords.data.api2.Api2;
import com.divanoapps.learnwords.data.api2.ServiceExecutor;

public class Application {
    public static final Api2 api = ServiceExecutor.create(Api2.class);

    public static final String FAKE_EMAIL = "sda97g@gmail.com";
}
