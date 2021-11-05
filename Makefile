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
	@curl -H "Authorization: Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6ImludGVsbGlhcy1rZXktaWQifQ.eyJzY29wZSI6WyJhbnkiXSwiZXhwIjoxNjM2MDY3NzkxLCJhdXRob3JpdGllcyI6WyJST0xFX1VTRVIiXSwianRpIjoiYWVmMWI4ZGEtOTg1Yy00Zjc1LTk2MTMtYTNkYmM4MTVjYWQ2IiwiY2xpZW50X2lkIjoiaW50ZWxsaWFzLWNsaWVudCJ9.MiM-MwvwfyjzMDvnZ0SHfLIOm6x3jA-ZR8HhsManlpW3QGAYl1Ic-hbl1J1Qrxr7zQ_G_WaEqhJs3I7CX9HknmKm9qty4DOjN4TCK1sNuWgrbE4vZaEzEZGiYuE6iE2smOQQjxNm6hPlAbBNqkkNm2a58sa6B6bdVuGm8X_EIPMH2ZEWfVmyKxyMkWXYvcODC5MbtlXAe08q5TgTIYWWvKU4GZXnxu-g3I3e3V9NgrhKg4610qW_fev54VH2Pr2sg7xkjtJQefb-59g1tOejrEDqbhipdw4mne3g0EymW8mp7inl9frw8CIbuaMLnNC90LoPmWGePD1EvlAU6tPNmg" localhost:8082/resource/profile

up: ms-resource-server-up ms-authorization-server-up

down: ms-authorization-server-down ms-resource-server-down
