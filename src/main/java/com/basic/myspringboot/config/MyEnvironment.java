package com.basic.myspringboot.config;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Builder
public class MyEnvironment {
    private String mode;
}
