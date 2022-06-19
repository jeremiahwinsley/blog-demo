package net.permutated.blog.config;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ADMIN,
    AUTHOR,
    USER;

    @Override
    public String getAuthority() {
        return String.format("ROLE_%s", this.name());
    }
}
