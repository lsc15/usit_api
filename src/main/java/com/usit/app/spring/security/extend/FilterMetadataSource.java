package com.usit.app.spring.security.extend;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AndRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;

import com.usit.app.spring.security.service.ResourceMetaService;
import com.usit.domain.ResourceRole;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class FilterMetadataSource implements FilterInvocationSecurityMetadataSource, InitializingBean {

  @Autowired
  private ResourceMetaService resourceMetaService;

  @Autowired
  private CacheManager cacheManager;

  @Override
  public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
      log.debug("FilterMetadataSource.getAttributes");
      FilterInvocation fi = (FilterInvocation) object;
      HttpServletRequest req = fi.getHttpRequest();

      String url = fi.getRequestUrl();
      String method = fi.getRequest().getMethod();
      log.debug("url:{}", url);
      log.debug("method:{}", method);

      List<ResourceRole> authorities2 = cacheManager.getAuthorities2();
//      Map<String, List<ResourceRole>> authorities = cacheManager.getAuthorities();

//      Set<String> keys = authorities.keySet();

      boolean matchFound = false;
      List<ResourceRole> matchedResourceRole = new ArrayList<ResourceRole>();

      for(ResourceRole role : authorities2) {
          String uri = role.getReqUri();
          RequestMatcher _m = new AntPathRequestMatcher(uri);
          AndRequestMatcher matcher = new AndRequestMatcher(_m);
          boolean matched = matcher.matches(req);
          log.debug("url:{}", uri);
          log.debug("matched:{}", matched);
          if(matched) {
              if(method.equals(role.getReqMethod())){
                  log.debug("method matched:{}:{}", method, role.getReqMethod());
//              matchFound = matched;
                  matchedResourceRole.add(role);
//              break;
              } else {
                  log.debug("method not matched:{}:{}", method, role.getReqMethod());

              }
          }
      }

//      for(String key : keys) {
//
//          Arrays.asList(key);
//          List<RequestMatcher> _m = Arrays.asList(key).stream().map(path -> new AntPathRequestMatcher(path)).collect(Collectors.toList());
//          AndRequestMatcher matcher = new AndRequestMatcher(_m);
//          boolean matched = matcher.matches(req);
//          log.debug("key(url):{}", key);
//          log.debug("matched:{}", matched);
//          if(matched) {
//              matchFound = matched;
//              url = key;
//              break;
//          }
//      }

      log.debug("matched url:{}", url);

//      List<ResourceRole> resourceRole = cacheManager.getAuthorities().get(url);
//      if (resourceRole == null) {
//          return null;
//      }

      if(matchedResourceRole.size() < 1) {
          String[] stockArr = new String[] {"UNCATEGORIZED_URL"};
          return SecurityConfig.createList(stockArr);
      }

//      log.debug("resourceRole.size():{}", resourceRole.size());
//      List<String> roles = resourceRole.stream().map(ResourceRole::getRoleId).collect(Collectors.toList());
      List<String> roles = matchedResourceRole.stream().map(ResourceRole::getRoleId).collect(Collectors.toList());
//
//      List<RequestMatcher> m = roles.stream().map(path -> new AntPathRequestMatcher(path)).collect(Collectors.toList());
//      AndRequestMatcher matcher = new AndRequestMatcher(m);
//      boolean matched = matcher.matches(req);
//      log.debug("matched:{}", matched);

      String[] stockArr = new String[roles.size()];
      stockArr = roles.toArray(stockArr);

      return SecurityConfig.createList(stockArr);
  }

  @Override
  public Collection<ConfigAttribute> getAllConfigAttributes() {
    return null;
  }

  @Override
  public boolean supports(Class<?> clazz) {
    return FilterInvocation.class.isAssignableFrom(clazz);
  }

  public void reload() {
      resourceMetaService.findAllResources();
  }

  @Override
  public void afterPropertiesSet() throws Exception {
      log.debug("FilterMetadataSource.afterPropertiesSet");
      resourceMetaService.findAllResources();
  }
}
