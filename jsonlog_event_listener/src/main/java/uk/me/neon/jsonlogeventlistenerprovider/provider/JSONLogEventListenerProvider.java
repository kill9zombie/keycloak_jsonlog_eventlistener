package uk.me.neon.jsonlogeventlistenerprovider.provider;

import org.jboss.logging.Logger;
import org.keycloak.events.Event;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.admin.AdminEvent;
import org.keycloak.models.KeycloakContext;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.UserModel;
import org.keycloak.models.GroupModel;
import org.keycloak.models.RealmModel;
import org.keycloak.sessions.AuthenticationSessionModel;

import java.util.Map;
import java.util.List;
import javax.json.*;

public class JSONLogEventListenerProvider implements EventListenerProvider {

    KeycloakSession session;
    Logger logger;
    String prefix;
    Boolean showGroups;
    Boolean showAttributes;

    public JSONLogEventListenerProvider(KeycloakSession session, Logger logger, String prefix, Boolean optShowGroups, Boolean optShowAttributes) {
        this.session = session;
        this.logger = logger;
        this.prefix = prefix;
        this.showGroups = optShowGroups;
        this.showAttributes = optShowAttributes;
    }

    @Override
    public void onEvent(Event event) {
        StringBuilder sb = new StringBuilder();

        sb.append(prefix);
        sb.append(toJsonString(event));

        logger.log(Logger.Level.INFO, sb.toString());
    }

    @Override
    public void onEvent(AdminEvent adminEvent, boolean b) {


        StringBuilder sb = new StringBuilder();

        sb.append(prefix);
        sb.append(toJsonString(adminEvent));

        logger.log(Logger.Level.INFO, sb.toString());
    }

    @Override
    public void close() {

    }

    private UserModel getUserModelById(String userId) {
        RealmModel realmModel = session.getContext().getRealm();
        return session.users().getUserById(userId, realmModel);
    }

    // Returns a JsonArrayBuilder suitable for inserting
    // into a JsonObjectBuilder later on.
    //
    private JsonArrayBuilder userGroups(UserModel user) {
        JsonArrayBuilder jsonArray = Json.createArrayBuilder();

        // getGroups() is deprecated in v12, so we can tidy
        // this up into a stream reduce
        for (GroupModel group : user.getGroups()) {
            jsonArray.add(group.getName());
        }

        return jsonArray;
    }

    // Returns a JsonObjectBuilder suitable for inserting
    // into another JsonObjectBuilder later on.
    //
    private JsonObjectBuilder userAttributes(UserModel user) {
        JsonObjectBuilder jsonObj = Json.createObjectBuilder();
        Map<String, List<String>> attrs = user.getAttributes();

        for (Map.Entry<String, List<String>> e : attrs.entrySet()) {
            JsonArrayBuilder jsonArray = Json.createArrayBuilder();
            for (String value : e.getValue()) {
                jsonArray.add(value);
            }

            jsonObj.add(e.getKey(), jsonArray);
        }

        return jsonObj;
    }


    private String toJsonString(Event event) {

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
            String userId = event.getUserId().toString();
            obj.add("userId", userId);

            UserModel user = getUserModelById(userId);
            if (user != null) {

                if (showGroups) {
                    obj.add("userGroups", userGroups(user));
                }

                if (showAttributes) {
                    obj.add("userAttributes", userAttributes(user));
                }
            }
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



    private String toJsonString(AdminEvent adminEvent) {
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
                String userId = adminEvent.getAuthDetails().getUserId().toString();
                obj.add("userId", userId);

                UserModel user = getUserModelById(userId);
                if (user != null) {

                    if (showGroups) {
                        obj.add("userGroups", userGroups(user));
                    }

                    if (showAttributes) {
                        obj.add("userAttributes", userAttributes(user));
                    }
                }
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
