![Java CI](https://github.com/kill9zombie/keycloak_jsonlog_eventlistener/workflows/Java%20CI/badge.svg)

# Keycloak JSON Log Eventlistener

Primarily written for the [Jboss Keycloak docker image](https://hub.docker.com/r/jboss/keycloak), it will output Keycloak events as JSON into the keycloak server log.

The idea is to parse logs once they get to [logstash](https://www.elastic.co/logstash) via [journalbeat](https://www.elastic.co/guide/en/beats/journalbeat/current/index.html).

Tested with Keycloak version 8.0.1

## Configuration

If you want to change the default log prefix (which is "JSON_EVENT::"), set the following environmental variable:

    KEYCLOAK_JSONLOG_PREFIX

.. for example:

    sudo docker run -it --rm -v "$(pwd)/jsonlog-event-listener.jar:/opt/jboss/keycloak/standalone/deployments/jsonlog-event-listener.jar" -e 'KEYCLOAK_USER=admin' -e 'KEYCLOAK_PASSWORD=admin' -e 'KEYCLOAK_JSONLOG_PREFIX=FOO' -p 8080:8080 jboss/keycloak:8.0.1

Once the event handler is enabled for the realm (in the admin console see Events -> Config), you should see something like this in the server log:

    16:30:11,662 INFO  [org.keycloak.events] (default task-6) FOO{"type":"ADMIN_EVENT","operationType":"CREATE","realmId":"master","clientId":"01b98c9f-a2c3-418a-9c5e-a766cd51c4a7","userId":"6f87b3d2-108f-4234-8fd2-9fdccf700fce","ipAddress":"172.17.0.1","resourceType":"USER","resourcePath":"users/633ff1ce-a695-4c07-a083-1af595a839e8"}
    16:30:18,063 INFO  [org.keycloak.events] (default task-8) FOO{"type":"ADMIN_EVENT","operationType":"ACTION","realmId":"master","clientId":"01b98c9f-a2c3-418a-9c5e-a766cd51c4a7","userId":"6f87b3d2-108f-4234-8fd2-9fdccf700fce","ipAddress":"172.17.0.1","resourceType":"USER","resourcePath":"users/633ff1ce-a695-4c07-a083-1af595a839e8/reset-password"}


## Examples

    15:58:08,150 INFO  [org.keycloak.events] (default task-7) JSON_EVENT::{"type":"LOGIN","realmId":"master","clientId":"security-admin-console","userId":"804807dd-c991-4712-8ec1-c6d4ff3f0e73","ipAddress":"172.17.0.1","auth_method":"openid-connect","auth_type":"code","redirect_uri":"http://localhost:8080/auth/admin/master/console/","consent":"no_consent_required","code_id":"d1a1cc64-4b31-43e6-a083-cc84d7a45cb3","username":"admin"}

    15:58:08,312 INFO  [org.keycloak.events] (default task-7) JSON_EVENT::{"type":"CODE_TO_TOKEN","realmId":"master","clientId":"security-admin-console","userId":"804807dd-c991-4712-8ec1-c6d4ff3f0e73","ipAddress":"172.17.0.1","token_id":"733a2efb-4094-4dc1-90a4-194858ce08b0","grant_type":"authorization_code","refresh_token_type":"Refresh","scope":"openid profile email","refresh_token_id":"fe31ebc2-2621-434d-a1e9-300c03b66568","code_id":"d1a1cc64-4b31-43e6-a083-cc84d7a45cb3","client_auth_method":"client-secret"}

    15:58:14,940 INFO  [org.keycloak.events] (default task-4) JSON_EVENT::{"type":"AdminEvent","operationType":"UPDATE","realmId":"master","clientId":"beec8c69-34b2-4d7e-870d-c84e008fab23","userId":"804807dd-c991-4712-8ec1-c6d4ff3f0e73","ipAddress":"172.17.0.1","resourceType":"REALM","resourcePath":"events/config"}

    15:58:23,428 INFO  [org.keycloak.events] (default task-4) JSON_EVENT::{"type":"LOGOUT","realmId":"master","userId":"804807dd-c991-4712-8ec1-c6d4ff3f0e73","ipAddress":"172.17.0.1","redirect_uri":"http://localhost:8080/auth/admin/master/console/#/realms/test/events-settings"}
