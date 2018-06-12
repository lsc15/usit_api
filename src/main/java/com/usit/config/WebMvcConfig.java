package com.usit.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import com.usit.app.spring.exception.support.FrameworkExceptionHandler;
import com.usit.app.spring.interceptor.BasicInterceptor;
import com.usit.app.spring.ui.adaptor.ComWebArgumentsResolver;
import com.usit.app.spring.ui.adaptor.UiJSONAdaptor;

@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

	@Autowired
	private BasicInterceptor basicInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {

		registry.addInterceptor(basicInterceptor)
		.addPathPatterns("/**/*");
//		.excludePathPatterns("");

	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolverList) {
		resolverList.add(customArgumentResolvers());
	}

	@Bean
	public FrameworkExceptionHandler frameworkExceptionHandler() {
		FrameworkExceptionHandler frameworkExceptionHandler = new FrameworkExceptionHandler();
		frameworkExceptionHandler.setOrder(0);
		return frameworkExceptionHandler;
	}

	public ComWebArgumentsResolver customArgumentResolvers() {
		ComWebArgumentsResolver argsResolver = new ComWebArgumentsResolver();
		argsResolver.setUiAdaptor(new UiJSONAdaptor());
		return argsResolver;
	}

	@Bean
    MappingJackson2JsonView jsonView(){
        return new MappingJackson2JsonView();
    }


}
