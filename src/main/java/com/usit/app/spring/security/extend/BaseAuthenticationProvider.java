package com.usit.app.spring.security.extend;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.authentication.dao.SaltSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.Assert;

import com.usit.app.spring.security.PasswordValidator;
import com.usit.app.spring.security.domain.SignedMember;

public class BaseAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    private static final String TempEnc = null;

    @Autowired
    private HttpServletRequest request;


    private AuthenticationUserDetailsService<Authentication> userDetailService;
    private PasswordValidator passwordValidator;
    private SaltSource saltSource;

    /**
     * <pre>
     * setUserDetailService
     * </pre>
     * @param AuthenticationUserDetailsService<Authentication> userDetailService
     * @return N/A
     */
    public void setUserDetailService(
            AuthenticationUserDetailsService<Authentication> userDetailService) {
        logger.debug("BaseAuthenticationProvider.setUserDetailService" + userDetailService.getClass().getName());
        this.userDetailService = userDetailService;
    }

    /**
     * <pre>
     * getUserDetailService
     * </pre>
     * @param N/A
     * @return AuthenticationUserDetailsService
     */
    public AuthenticationUserDetailsService<Authentication> getUserDetailService() {
        return this.userDetailService;
    }

    /**
     * <pre>
     * getPasswordValidator
     * </pre>
     * @param N/A
     * @return PasswordValidator
     */
    public PasswordValidator getPasswordValidator() {
        return this.passwordValidator;
    }

    /**
     * <pre>
     * setPasswordValidator
     * </pre>
     * @param PasswordValidator passwordValidator
     * @return N/A
     */
    public void setPasswordValidator(PasswordValidator passwordValidator) {
        this.passwordValidator = passwordValidator;
    }

    /**
     * <pre>
     * getSaltSource
     * </pre>
     * @param N/A
     * @return SaltSource
     */
    public SaltSource getSaltSource() {
        return this.saltSource;
    }

    /**
     * <pre>
     * setSaltSource
     * </pre>
     * @param SaltSource saltSource
     * @return N/A
     */
    public void setSaltSource(SaltSource saltSource) {
        this.saltSource = saltSource;
    }

    /**
     * <pre>
     * doAfterPropertiesSet
     * </pre>
     * @param N/A
     * @return N/A
     */
    @Override
    protected void doAfterPropertiesSet() throws Exception {
        Assert.notNull(this.userDetailService,
                "A UserDetailService must be set");
    }

    /**
     * <pre>
     * retrieveUser
     * </pre>
     * @param String username, UsernamePasswordAuthenticationToken authentication
     * @return UserDetails
     */
    @Override
    protected UserDetails retrieveUser(String username,
            UsernamePasswordAuthenticationToken authentication)
            throws AuthenticationException {
        UserDetails loadedUser;
        try
        {
            loadedUser = getUserDetailService().loadUserDetails(authentication);
        } catch (UsernameNotFoundException notFound) {
            throw notFound;
        } catch (Exception repositoryProblem) {
            throw new AuthenticationServiceException(
                    repositoryProblem.getMessage(), repositoryProblem);
        }

        if (loadedUser == null) {
            throw new AuthenticationServiceException(
                    "UserDetailsService returned null, which is an interface contract violation");
        }

        return loadedUser;
    }

    /**
     * <pre>
     * additionalAuthenticationChecks
     * </pre>
     * @param UserDetails userDetails, UsernamePasswordAuthenticationToken authentication
     * @return N/A
     */
    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails,
            UsernamePasswordAuthenticationToken authentication)
            throws AuthenticationException {
        Object salt = null;

        if (this.saltSource != null) {
            salt = this.saltSource.getSalt(userDetails);
        }

        if (authentication.getCredentials() == null) {
            this.logger.debug("Authentication failed: no credentials provided");

            throw new BadCredentialsException(
                    this.messages.getMessage(
                            "AbstractUserDetailsAuthenticationProvider.badCredentials",
                            "Bad credentials"));
        }

        String presentedPassword = authentication.getCredentials().toString();

        SignedMember signeduser = (SignedMember) userDetails;
        String sLoginPass = signeduser.getPassword();
//        presentedPassword = sLoginPass;

        logger.debug("@@@ additionalAuthenticationChecks =====================");
        logger.debug("@@@ LoginPass="+sLoginPass);
        logger.debug("@@@");
        ////////////////////////////////

        Map<String, Object> logInfo = new HashMap<String, Object>();
        logInfo.put("loginId", authentication.getPrincipal());
        logInfo.put("admId", authentication.getPrincipal());
        logInfo.put("loginPassword", presentedPassword);

        logger.debug("@@@ request.RemoteAddr="+request.getRemoteAddr());
        String sEncIp = "127.0.0.1";//TODO XecureDbUtil.syncEncryptNormal(request.getRemoteAddr());

        logInfo.put("loginIpAddress", sEncIp);
        logInfo.put("LOGIN_IP_ADDRESS", sEncIp);

        if (!this.passwordValidator.validatePassword(userDetails.getPassword(),
                        presentedPassword, salt)) {
            this.logger.debug("Authentication failed: password does not match stored value");


            logInfo.put("loginContents", "비밀번호 오류");
            int failCount = 0;
            try {

//            	loginService.updatePswdErrCnt(logInfo);
//                Map<String, Object> failCountMap = loginService.getPswdErrCnt(logInfo);
//                failCount = ((BigDecimal)failCountMap.get("PASSWORD_ERR_CNT")).intValue();

                //TODO: 로그인 이력

            } catch (Exception e) {
                logger.error("LOG테이블 Insert 중 Error $$$$$$$$$$$");
                e.printStackTrace();
            }

            if(failCount >= 5){
                throw new LockedException("해당 계정은 사용할 수 없습니다.[계정 로그인 시도 오류 5회 초과]");
            }

            throw new BadCredentialsException(
                            this.messages.getMessage(
                                            "AbstractUserDetailsAuthenticationProvider.badCredentials",
                                            "ID 혹은 비밀번호 " + failCount + "회 입력오류입니다... 5회 오류시 해당 계정사용이 제한됩니다."));
        }
        else
        {

        }
    }
}
