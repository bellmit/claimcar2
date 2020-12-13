package ins.platform.shiro.credentials;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;

import ins.framework.cache.CacheManager;
import ins.framework.cache.CacheService;

public class RetryLimitHashedCredentialsMatcher extends HashedCredentialsMatcher {

	private static final CacheService passwordRetryCache = CacheManager.getInstance("passwordRetryCache");

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        String username = (String)token.getPrincipal();
        String key = passwordRetryCache.generateCacheKey(username,"doCredentialsMatch");
        Integer atomicInteger = 1;
		if(passwordRetryCache.containsKey(key)) {
		    atomicInteger = (Integer)passwordRetryCache.getCache(key);
		    System.out.println(atomicInteger);
		}
		passwordRetryCache.putCache(key,++atomicInteger);
        if(atomicInteger > 5) {
            //if retry count > 5 throw
            throw new ExcessiveAttemptsException();
        }

        boolean matches = super.doCredentialsMatch(token, info);
        if(matches) {
            //clear retry count
            passwordRetryCache.clearCache(key);
        }
        return matches;
    }
    /**
     * 清除缓存
     */
   
	public void clearCache(){
		passwordRetryCache.clearAllCacheManager();
	}
}
