package com.usit.app.spring.security.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;

/**
 * <pre>
 * 인증객체 관련 유틸(SecurityUtils)
 * </pre>
 * @since 2013. 12. 05.
 * @param N/A
 * @return N/A
 * @throws Exception
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 * </pre>
 */
public class SecurityUtils
{

	/**
     * <pre>
     * getSignedUserId
     * </pre>
     * @param N/A
     * @return String
     */
	public static String getSignedUserId()
    {
      String userId = null;
      UserDetails signedUser = getSignedUser();
      if (signedUser != null) {
        userId = signedUser.getUsername();
      }
      return userId;
    }

	/**
     * <pre>
     * getSignedUser
     * </pre>
     * @param N/A
     * @return UserDetails
     */
    public static UserDetails getSignedUser()
    {
      SecurityContext context = SecurityContextHolder.getContext();
      if (context == null) {
        return null;
      }

      Authentication auth = context.getAuthentication();

      if (auth == null) {
        return null;
      }

      Object principal = auth.getPrincipal();

      if (principal == null)
        return null;
      if ((principal instanceof UserDetails)) {
        return (UserDetails)principal;
      }

      return null;
    }

	/**
     * <pre>
     * getUserRoles
     * </pre>
     * @param N/A
     * @return String[]
     */
    public static String[] getUserRoles()
    {
      return getUserRoles(getSignedUser());
    }

	/**
     * <pre>
     * getUserRoles
     * </pre>
     * @param UserDetails user
     * @return String[]
     */
    public static String[] getUserRoles(UserDetails user)
    {
      if (user == null) {
        return null;
      }

      Collection authSet = user.getAuthorities();
      if (authSet.size() <= 0) {
        return null;
      }

      String[] userRoles = new String[authSet.size()];
      int i = 0;
      for (Iterator iterator = authSet.iterator(); iterator.hasNext(); ) {
        GrantedAuthority grantedAuthority = (GrantedAuthority)iterator.next();
        userRoles[(i++)] = grantedAuthority.getAuthority();
      }

      return userRoles;
    }

	/**
     * <pre>
     * hasRole
     * </pre>
     * @param String role
     * @return boolean
     */
    public static boolean hasRole(String role)
    {
      if (!StringUtils.hasText(role)) {
        return false;
      }

      String[] roles = getUserRoles();
      if ((roles == null) || (roles.length <= 0)) {
        return false;
      }

      return 0 <= Arrays.binarySearch(roles, role);
    }
}
