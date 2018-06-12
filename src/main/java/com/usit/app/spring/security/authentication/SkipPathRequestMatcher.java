package com.usit.app.spring.security.authentication;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.web.util.matcher.AndRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.Assert;

public class SkipPathRequestMatcher implements RequestMatcher {
    private OrRequestMatcher matchers;
    private AndRequestMatcher processMatchers;
//    private RequestMatcher processingMatcher;

//    public SkipPathRequestMatcher(List<String> pathsToSkip, String processingPath) {
    public SkipPathRequestMatcher(List<String> pathsToSkip, List<String> pathsToProcess) {
        Assert.notNull(pathsToSkip);

        List<RequestMatcher> m = pathsToSkip.stream().map(path -> new AntPathRequestMatcher(path)).collect(Collectors.toList());
        matchers = new OrRequestMatcher(m);
        List<RequestMatcher> m2 = pathsToProcess.stream().map(path -> new AntPathRequestMatcher(path)).collect(Collectors.toList());
        processMatchers = new AndRequestMatcher(m2);
//        processingMatcher = new AntPathRequestMatcher(processingPath);
    }

    @Override
    public boolean matches(HttpServletRequest request) {
        if (matchers.matches(request)) {
            return false;
        }
        return processMatchers.matches(request);
//        return processingMatcher.matches(request) ? true : false;
    }
}