package com.usit.app.spring.security.extend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.usit.domain.ResourceRole;

@Component
public class CacheManager implements ApplicationListener<CacheEventMessage> {

  private Map<String, List<ResourceRole>> authorities;
  private List<ResourceRole> authorities2;

  public Map<String, List<ResourceRole>> getAuthorities() {
    return authorities;
  }

  public List<ResourceRole> getAuthorities2() {
    return authorities2;
  }

  public List<ResourceRole> getAuthoritie(String key) {
    return authorities.get(key);
  }

  @Override
  public void onApplicationEvent(CacheEventMessage event) {
      authorities2 = event.getResourceRole();
//	  authorities = event.getResourceRole().stream().collect(groupingBy(ResourceRole::getReqUri, toList()));

    // grouping
    // url => roles

//    String url;
//    for (UserRoleDto userRoleDto : event.getUserRoleDto()) {
//      url = userRoleDto.getUrl();
//      if (this.urlRoles.containsKey(url)) {
//        List<String> roles = this.urlRoles.get(url);
//        roles.add(userRoleDto.getRoleName());
//
//      } else {
//        List<String> roles = new ArrayList<>();
//        roles.add(userRoleDto.getRoleName());
//        this.urlRoles.put(url, roles);
//      }
//    }
  }
}
