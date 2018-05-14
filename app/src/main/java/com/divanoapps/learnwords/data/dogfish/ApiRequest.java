package com.divanoapps.learnwords.data.dogfish;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
//@Repeatable(ApiRequests.class)
public @interface ApiRequest {
    String method();
    String entity();
}
