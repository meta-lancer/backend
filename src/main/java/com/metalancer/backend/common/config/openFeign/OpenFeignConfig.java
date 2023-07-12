package com.metalancer.backend.common.config.openFeign;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = {"com.metalancer.backend"})
public class OpenFeignConfig {

}