package com.ekzameno.ekzameno;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;

/**
 * Jersey application.
 */
public class Application extends ResourceConfig {
    /**
     * Create application.
     */
    public Application() {
        packages("com.ekzameno.ekzameno");
        register(RolesAllowedDynamicFeature.class);
    }
}
