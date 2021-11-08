set-up:
	@docker network create oauth2-network

clean:
	@docker network rm oauth2-network

ms-authorization-server-build:
	@./gradlew clean ms-authorization-server:build

ms-authorization-server-up: ms-authorization-server-build
	@docker-compose -f configs/ms-authorization-server/docker-compose.yaml up --build --force-recreate -d

ms-authorization-server-down:
	@docker-compose -f configs/ms-authorization-server/docker-compose.yaml down

ms-authorization-server-restart: ms-authorization-server-down ms-authorization-server-up

ms-resource-server-build:
	@./gradlew clean ms-resource-server:build

ms-resource-server-up: ms-resource-server-build
	@docker-compose -f configs/ms-resource-server/docker-compose.yaml up --build --force-recreate -d

ms-resource-server-down:
	@docker-compose -f configs/ms-resource-server/docker-compose.yaml down

ms-resource-server-restart: ms-resource-server-down ms-resource-server-up

send-authentication-request:
	@curl -u intellias-client:intellias-secret localhost:8081/sso-auth-server/oauth/token -d grant_type=client_credentials -d scope=any

send-resource-request:
	@curl -H "Authorization: Bearer ${ACCESS_TOKEN}" localhost:8082/resource/profile

up: ms-resource-server-up ms-authorization-server-up

down: ms-authorization-server-down ms-resource-server-down
