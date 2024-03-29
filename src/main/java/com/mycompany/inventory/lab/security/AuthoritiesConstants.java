package com.mycompany.inventory.lab.security;

/**
 * Constants for Spring Security authorities.
 */
public final class AuthoritiesConstants {

    public static final String ADMIN = "ROLE_ADMIN";

    public static final String USER = "ROLE_USER";

    public static final String ANONYMOUS = "ROLE_ANONYMOUS";

    public static final String STUDENT = "ROLE_STUDENT";

    public static final String TUTOR = "ROLE_TUTOR";

    private AuthoritiesConstants() {}
}
