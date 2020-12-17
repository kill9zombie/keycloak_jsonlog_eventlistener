.PHONY = build

build:
	mvn -B package --file jsonlog_event_listener/pom.xml
