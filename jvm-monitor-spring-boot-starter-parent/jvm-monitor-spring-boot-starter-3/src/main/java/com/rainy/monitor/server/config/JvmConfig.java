package com.rainy.monitor.server.config;

import com.rainy.monitor.server.controller.ServerController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JvmConfig {

    @Bean
    public ServerController serverController() {
        return new ServerController();
    }

}
