build:
	./gradlew clean build -x test

up:
	make build
	docker compose --env-file ./env/.env up -d

down:
	docker compose --env-file ./env/.env -f ./compose.yaml down

start:
	docker compose --env-file ./env/.env start gateway_db gateway

stop:
	docker compose --env-file ./env/.env stop gateway_db gateway

fclean:
	rm -rf ./data
	docker compose --env-file ./env/.env -f ./compose.yaml down --rmi all --volumes

clean:
	make down
	docker rmi -f pinting/gateway:1.0.0

re:
	make fclean
	make build
	make up

env_update:
	git submodule update --init --recursive --remote

.PHONY: build up env_update up re
