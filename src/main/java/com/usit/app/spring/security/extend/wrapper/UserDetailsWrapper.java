package com.usit.app.spring.security.extend.wrapper;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

public abstract class UserDetailsWrapper implements UserDetails {

    /**
     *
     */
    private static final long serialVersionUID = -552527362248678048L;

    private Collection<GrantedAuthority> authorities = AuthorityUtils.NO_AUTHORITIES;

    private boolean accountNonExpired = true;
    private boolean accountNonLocked = true;
    private boolean credentialsNonExpired = true;
    private boolean enabled = true;
    private List<String> roles;

    private Map extraInfo = new HashMap();

    /**
     * <pre>
     * getAuthorities
     * </pre>
     * @param N/A
     * @return Collection<GrantedAuthority>
     */
    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    /**
     * <pre>
     * setAuthorities
     * </pre>
     * @param Collection<GrantedAuthority> authorities
     * @return N/A
     */
    public void setAuthorities(Collection<GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    @Override
    public abstract String getUsername();

    @Override
    public abstract String getPassword();

    public List<String> getRoles() {
        return this.roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    /**
     * <pre>
     * isAccountNonExpired
     * </pre>
     * @param N/A
     * @return boolean
     */
    @Override
    public boolean isAccountNonExpired() {
        return this.accountNonExpired;
    }

    /**
     * <pre>
     * setAccountNonExpired
     * </pre>
     * @param boolean accountNonExpired
     * @return N/A
     */
    public void setAccountNonExpired(boolean accountNonExpired)
    {
        this.accountNonExpired = accountNonExpired;
    }

    /**
     * <pre>
     * isAccountNonLocked
     * </pre>
     * @param N/A
     * @return boolean
     */
    @Override
    public boolean isAccountNonLocked() {
        return this.accountNonLocked;
    }

    /**
     * <pre>
     * setAccountNonLocked
     * </pre>
     * @param boolean accountNonLocked
     * @return N/A
     */
    public void setAccountNonLocked(boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }

    /**
     * <pre>
     * isCredentialsNonExpired
     * </pre>
     * @param N/A
     * @return boolean
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return this.credentialsNonExpired;
    }

    /**
     * <pre>
     * setCredentialsNonExpired
     * </pre>
     * @param boolean credentialsNonExpired
     * @return N/A
     */
    public void setCredentialsNonExpired(boolean credentialsNonExpired) {
        this.credentialsNonExpired = credentialsNonExpired;
    }

    /**
     * <pre>
     * isEnabled
     * </pre>
     * @param N/A
     * @return boolean
     */
    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    /**
     * <pre>
     * setEnabled
     * </pre>
     * @param boolean enabled
     * @return N/A
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * <pre>
     * putExtraInfo
     * </pre>
     * @param String key, Object value
     * @return N/A
     */
    public void putExtraInfo(String key, Object value) {
        this.extraInfo.put(key, value);
    }

    /**
     * <pre>
     * getExtraInfo
     * </pre>
     * @param String key
     * @return Object
     */
    public Object getExtraInfo(String key) {
        return this.extraInfo.get(key);
    }

    /**
     * <pre>
     * addExtraInfo
     * </pre>
     * @param Map addInfo
     * @return N/A
     */
    public void addExtraInfo(Map addInfo) {
        Set entrySet = addInfo.entrySet();
        for (Iterator iterator = entrySet.iterator(); iterator.hasNext();) {
            Map.Entry entry = (Map.Entry) iterator.next();
            putExtraInfo((String) entry.getKey(), entry.getValue());
        }
    }

    /**
     * <pre>
     * hashCode
     * </pre>
     * @param N/A
     * @return N/A
     */
    @Override
    public int hashCode()
    {
        int prime = 31;
        int result = 1;
        result = 31 * result + (this.accountNonExpired ? 1231 : 1237);
        result = 31 * result + (this.accountNonLocked ? 1231 : 1237);
        result = 31
                * result
                + (this.authorities == null ? 0 : this.authorities
                        .hashCode());
        result = 31 * result + (this.credentialsNonExpired ? 1231 : 1237);
        result = 31
                * result
                + (this.extraInfo == null ? 0 : this.extraInfo
                        .hashCode());
        return result;
    }

    /**
     * <pre>
     * equals
     * </pre>
     * @param Object obj
     * @return boolean
     */
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        UserDetailsWrapper other = (UserDetailsWrapper) obj;
        if (this.accountNonExpired != other.accountNonExpired) {
            return false;
        }
        if (this.accountNonLocked != other.accountNonLocked) {
            return false;
        }
        if (this.authorities == null) {
            if (other.authorities != null) {
                return false;
            }
        } else if (!this.authorities.equals(other.authorities)) {
            return false;
        }
        if (this.credentialsNonExpired != other.credentialsNonExpired) {
            return false;
        }
        if (this.extraInfo == null) {
            if (other.extraInfo != null) {
                return false;
            }
        } else if (!this.extraInfo.equals(other.extraInfo)) {
            return false;
        }
        return true;
    }
}
