package uk.me.neon.jsonlogeventlistenerprovider.provider;

import org.jboss.logging.Logger;
import org.keycloak.events.Event;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.admin.AdminEvent;
import org.keycloak.models.KeycloakContext;
import org.keycloak.models.KeycloakSession;
import org.keycloak.sessions.AuthenticationSessionModel;

import java.util.Map;
import javax.json.*;

public class JSONLogEventListenerProvider implements EventListenerProvider {

    KeycloakSession session;
    Logger logger;
    String prefix;

    public JSONLogEventListenerProvider(KeycloakSession session, Logger logger, String prefix) {
        this.session = session;
        this.logger = logger;
        this.prefix = prefix;
    }

    @Override
    public void onEvent(Event event) {

        StringBuilder sb = new StringBuilder();

        sb.append(prefix);
        sb.append(toString(event));

        logger.log(Logger.Level.INFO, sb.toString());
    }

    @Override
    public void onEvent(AdminEvent adminEvent, boolean b) {

        StringBuilder sb = new StringBuilder();

        sb.append(prefix);
        sb.append(toString(adminEvent));

        logger.log(Logger.Level.INFO, sb.toString());
    }

    @Override
    public void close() {

    }

    private String toString(Event event) {

        JsonObjectBuilder obj = Json.createObjectBuilder();

        if (event.getType() != null) {
            obj.add("type", event.getType().toString());
        }

        if (event.getRealmId() != null) {
            obj.add("realmId", event.getRealmId().toString());
        }

        if (event.getClientId() != null) {
            obj.add("clientId", event.getClientId().toString());
        }

        if (event.getUserId() != null) {
            obj.add("userId", event.getUserId().toString());
        }

        if (event.getIpAddress() != null) {
            obj.add("ipAddress", event.getIpAddress().toString());
        }


        if (event.getError() != null) {
            obj.add("error", event.getError().toString());
        }

        if (event.getDetails() != null) {
            for (Map.Entry<String, String> e : event.getDetails().entrySet()) {
                obj.add(e.getKey(), e.getValue().toString());
            }
        }

        return obj.build().toString();

    }



    private String toString(AdminEvent adminEvent) {
        JsonObjectBuilder obj = Json.createObjectBuilder();

        obj.add("type", "ADMIN_EVENT");

        if (adminEvent.getOperationType() != null) {
            obj.add("operationType", adminEvent.getOperationType().toString());
        }

        if (adminEvent.getAuthDetails() != null) {
            if (adminEvent.getAuthDetails().getRealmId() != null) {
                obj.add("realmId", adminEvent.getAuthDetails().getRealmId().toString());
            }

            if (adminEvent.getAuthDetails().getClientId() != null) {
                obj.add("clientId", adminEvent.getAuthDetails().getClientId().toString());
            }

            if (adminEvent.getAuthDetails().getUserId() != null) {
                obj.add("userId", adminEvent.getAuthDetails().getUserId().toString());
            }

            if (adminEvent.getAuthDetails().getIpAddress() != null) {
                obj.add("ipAddress", adminEvent.getAuthDetails().getIpAddress().toString());
            }

        }

        if (adminEvent.getResourceType() != null) {
            obj.add("resourceType", adminEvent.getResourceType().toString());
        }

        if (adminEvent.getResourcePath() != null) {
            obj.add("resourcePath", adminEvent.getResourcePath().toString());
        }

        if (adminEvent.getError() != null) {
            obj.add("error", adminEvent.getError().toString());
        }

        return obj.build().toString();
    }

}
