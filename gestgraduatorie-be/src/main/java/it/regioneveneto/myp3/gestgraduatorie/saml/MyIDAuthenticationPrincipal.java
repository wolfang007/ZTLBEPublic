package it.regioneveneto.myp3.gestgraduatorie.saml;

import java.util.Collection;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

public class MyIDAuthenticationPrincipal implements Authentication {

    /**
	 * 
	 */
	private static final long serialVersionUID = -1345130879711314776L;
	private final Collection<? extends GrantedAuthority> authorities;
    private final MyIdUserDetails principal;
    private boolean authenticated;


    public MyIDAuthenticationPrincipal(MyIdUserDetails principal, boolean authenticated, Collection<? extends GrantedAuthority> authorities){
        this.authenticated=authenticated;
        this.principal=principal;
        this.authorities=authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return principal;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public void setAuthenticated(boolean b) throws IllegalArgumentException {
        this.authenticated=b;
    }

    @Override
    public String getName() {
        return principal.getUsername();
    }


    @Override
    public String toString() {
        return "MyIDAuthenticationPrincipal{" +
                "authorities=" + authorities +
                ", principal=" + principal +
                ", authenticated=" + authenticated +
                '}';
    }
}
