package com.welb.config;

import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author luoyaozu
 * @title: TomcatConfig
 * @projectName xh-360appraisal-interface
 * @description: tomcat特殊字符处理问题
 * @date 2020/2/2213:06
 */
@Configuration
public class TomcatConfig {
    @Bean
    public ConfigurableServletWebServerFactory webServerFactory() {
        TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();
        factory.addConnectorCustomizers((TomcatConnectorCustomizer) connector -> {
            //允许的特殊字符
            connector.setProperty("relaxedQueryChars", "(),/:;<=>?@[\\]{}");
            connector.setProperty("rejectIllegalHeader", "false");
        });
        return factory;
    }
}
