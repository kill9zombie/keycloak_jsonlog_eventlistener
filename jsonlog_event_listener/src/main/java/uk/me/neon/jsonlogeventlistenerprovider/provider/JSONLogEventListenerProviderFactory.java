package uk.me.neon.jsonlogeventlistenerprovider.provider;

import org.jboss.logging.Logger;
import org.keycloak.Config;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.EventListenerProviderFactory;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;



public class JSONLogEventListenerProviderFactory implements EventListenerProviderFactory {

    public static final String ID = "jboss-logging";

    private static final Logger logger = Logger.getLogger("org.keycloak.events");

    private static final String jsonlog_prefix_env_var = "KEYCLOAK_JSONLOG_PREFIX";
    private static final String jsonlog_show_groups_env_var = "KEYCLOAK_JSONLOG_SHOW_GROUPS";
    private static final String jsonlog_show_attributes_env_var = "KEYCLOAK_JSONLOG_SHOW_ATTRIBUTES";

    String prefix = "JSON_EVENT::";
    Boolean optShowGroups = false;
    Boolean optShowAttributes = false;

    @Override
    public EventListenerProvider create(KeycloakSession session) {
        return new JSONLogEventListenerProvider(session, logger, prefix, optShowGroups, optShowAttributes);
    }

    @Override
    public void init(Config.Scope scope) {
        String env_prefix = System.getenv(jsonlog_prefix_env_var);
        String envShowGroups = System.getenv(jsonlog_show_groups_env_var);
        String envShowAttributes = System.getenv(jsonlog_show_attributes_env_var);
        if (env_prefix != null) {
            prefix = env_prefix;
        }

        if (envShowGroups != null) {
            this.optShowGroups = (envShowGroups.startsWith("true")) ? true : false;
        }

        if (envShowAttributes != null) {
            this.optShowAttributes = (envShowAttributes.startsWith("true")) ? true : false;
        }
    }

    @Override
    public void postInit(KeycloakSessionFactory keycloakSessionFactory) {

    }

    @Override
    public void close() {

    }

    @Override
    public String getId() {
        return "jsonlog_event_listener";
    }
}
