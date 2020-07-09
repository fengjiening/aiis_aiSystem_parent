package org.jeecg;

import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.Context;
import org.apache.tomcat.util.scan.StandardJarScanner;
import org.jeecg.modules.message.websocket.WebSocket;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.util.WebAppRootListener;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.net.InetAddress;
import java.net.UnknownHostException;

@Slf4j
@EnableSwagger2
@SpringBootApplication
public class JeecgApplication {

  public static void main(String[] args) throws UnknownHostException {

   // ConfigurableApplicationContext application = SpringApplication.run(JeecgApplication.class, args);

      SpringApplication springApplication = new SpringApplication(JeecgApplication.class); //这里填你的Class名字
      ConfigurableApplicationContext run = springApplication.run(args);
      WebSocket.setApplicationContext(run);




    Environment env = WebSocket.applicationContext.getEnvironment();
    String ip = InetAddress.getLocalHost().getHostAddress();
    String port = env.getProperty("server.port");
    String path = env.getProperty("server.servlet.context-path");
    log.info("\n----------------------------------------------------------\n\t" +
        "Application Jeecg-Boot is running! Access URLs:\n\t" +
        "Local: \t\thttp://localhost:" + port + path + "/\n\t" +
        "External: \thttp://" + ip + ":" + port + path + "/\n\t" +
        "Swagger-UI: \t\thttp://" + ip + ":" + port + path + "/doc.html\n" +
        "----------------------------------------------------------");

  }

  /**
  * tomcat-embed-jasper引用后提示jar找不到的问题
  */
  @Bean
  public TomcatServletWebServerFactory tomcatFactory() {
   return new TomcatServletWebServerFactory() {
    @Override
    protected void postProcessContext(Context context) {
     ((StandardJarScanner) context.getJarScanner()).setScanManifest(false);
    }
   };
  }

    @Configuration
    @ComponentScan
    @EnableAutoConfiguration
    public class WebAppRootContext implements ServletContextInitializer {
        @Override
        public void onStartup(ServletContext servletContext) throws ServletException {
            System.out.println("org.apache.tomcat.websocket.textBufferSize");
            servletContext.addListener(WebAppRootListener.class);
            servletContext.setInitParameter("org.apache.tomcat.websocket.textBufferSize","1024000");
        }
  }

}