package com.usit.app.spring.security.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.usit.app.spring.security.extend.CacheEventMessage;
import com.usit.domain.ResourceRole;
import com.usit.repository.ResourceRoleRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ResourceMetaServiceImpl implements ResourceMetaService {

  @Autowired
  private ResourceRoleRepository resourceRoleRepository;

  @Autowired
  private ApplicationContext applicationContext;

  @Override
  public void findAllResources() {
      Sort sort = new Sort(Sort.Direction.ASC, "ordSeq");
      List<ResourceRole> authorities = resourceRoleRepository.findAll(sort);

      authorities.stream().forEach(resourceRole -> {
          log.info("role name {} ", resourceRole.getRoleId());
          log.info("url {}", resourceRole.getReqUri());
          log.info("method {}", resourceRole.getReqMethod());
      });
      applicationContext.publishEvent(new CacheEventMessage(this, authorities));
  }
}
