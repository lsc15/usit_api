package com.usit;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;

import javax.servlet.Filter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.view.BeanNameViewResolver;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import com.google.api.client.util.PemReader;
import com.google.api.client.util.PemReader.Section;
import com.google.api.client.util.SecurityUtils;

//@Configuration
//@EnableAutoConfiguration
//@ComponentScan
@EnableAsync
@SpringBootApplication
@EntityScan(basePackageClasses = {UsitApiApplication.class, Jsr310JpaConverters.class}) // LocalDateTime을 mySql의 datetime 타입으로 전환해주는 class파일
@EnableScheduling
@PropertySource("classpath:config.properties")
public class UsitApiApplication extends SpringBootServletInitializer{

    public static void main(String[] args) throws IOException {
        
        SpringApplication.run(UsitApiApplication.class, args);
        
    }

    @Bean
    public HttpMessageConverter<String> responseBodyConverter() {
        return new StringHttpMessageConverter(Charset.forName("UTF-8"));
    }

    @Bean
    public Filter characterEncodingFilter() {
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding("UTF-8");
        characterEncodingFilter.setForceEncoding(true);
        return characterEncodingFilter;
    }

    @Bean
    public BeanNameViewResolver beanNameViewResolver(){
        BeanNameViewResolver resolver = new BeanNameViewResolver();
        resolver.setOrder(0);
        return resolver;
    }

    @Bean
    public ContentNegotiatingViewResolver contentNegotiatingViewResolver() {
        ContentNegotiatingViewResolver resolver = new ContentNegotiatingViewResolver();
        resolver.setOrder(1);
        return resolver;
    }

    @Bean
    public MappingJackson2JsonView jsonView(){
        return new MappingJackson2JsonView();
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
       return new PropertySourcesPlaceholderConfigurer();
    }

    
    
  
    @Bean
    public TaskScheduler taskScheduler() {
    		return new ConcurrentTaskScheduler(); //single threaded by default
    }
    
//    @Bean public TaskScheduler taskScheduler() { 
//    	ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
//    	taskScheduler.setPoolSize(10); return taskScheduler; 
//    	}

    private static PrivateKey privateKeyFromPkcs8(String privateKeyPem) throws IOException {
        Reader reader = new StringReader(privateKeyPem);
        Section section = PemReader.readFirstSectionAndClose(reader, "PRIVATE KEY");
        if (section == null) {
          throw new IOException("Invalid PKCS8 data.");
        }
        byte[] bytes = section.getBase64DecodedBytes();
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(bytes);
        Exception unexpectedException = null;
        try {
          KeyFactory keyFactory = SecurityUtils.getRsaKeyFactory();
          PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
          return privateKey;
        } catch (NoSuchAlgorithmException exception) {
          unexpectedException = exception;
        } catch (InvalidKeySpecException exception) {
          unexpectedException = exception;
        }
        throw new IOException("Unexpected exception reading PKCS data");
      }
    


}
