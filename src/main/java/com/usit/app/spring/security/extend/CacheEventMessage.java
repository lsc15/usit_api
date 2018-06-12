package com.usit.app.spring.security.extend;

import java.util.List;

import org.springframework.context.ApplicationEvent;

import com.usit.domain.ResourceRole;

public class CacheEventMessage extends ApplicationEvent {
  /**
     *
     */
    private static final long serialVersionUID = 183061493693729654L;
/**
   * Create a new ApplicationEvent.
   *
   * @param source the object on which the event initially occurred (never {@code null})
   */
  final List<ResourceRole> resourceRole;

  public CacheEventMessage(Object source, final List<ResourceRole> resourceRole) {
    super(source);
    this.resourceRole = resourceRole;
  }

  public List<ResourceRole> getResourceRole() {
    return resourceRole;
  }
}
