# ?= : 변수가 설정되어 있지 않을 때만 기본값으로 사용
ENV ?= dev
RUN_MODE ?= local # 기본 실행 모드는 local

# ==================================================================================== #
# Main Commands (실행 모드에 따라 동작 변경)
# ==================================================================================== #

## Spring Boot 앱 실행 (local/docker 모드)
run:
ifeq ($(RUN_MODE), local)
	make local-run
else ifeq ($(RUN_MODE), docker)
	make docker-run
endif

## Spring Boot 앱 중지 (local/docker 모드)
stop:
ifeq ($(RUN_MODE), local)
	@echo "로컬 모드에서는 Ctrl+C 로 직접 중지하세요."
else ifeq ($(RUN_MODE), docker)
	make api-down
endif

## 테스트 코드 실행
test:
	make local-code-test


# ==================================================================================== #
# Local Development Helper Commands
# ==================================================================================== #

## (Local Dev) Spring Boot 앱을 로컬에서 직접 실행 (포그라운드)
local-run:
	./mvnw spring-boot:run -Dspring-boot.run.profiles=local

## (Local Dev) 개발에 필요한 외부 서비스(DB, Redis)만 실행
start-deps:
	docker compose up -d postgres redis

## (Local Dev) 개발 외부 서비스(DB, Redis) 중지
stop-deps:
	docker compose stop postgres redis


# ==================================================================================== #
# Docker Compose Commands
# ==================================================================================== #

## Docker Compose로 모든 서비스 빌드
service-build:
	docker compose build

## Docker Compose로 모든 서비스 시작 (백그라운드)
service-up:
	docker compose up -d

## Docker Compose로 모든 서비스 중지
service-down:
	docker compose down

## Docker Compose로 모든 서비스 중지 및 볼륨 삭제 (DB 데이터 초기화)
service-clean:
	docker compose down -v


# ==================================================================================== #
# Application Container Commands
# ==================================================================================== #
## (Docker) Docker Compose로 Spring Boot 앱 시작
docker-run:
	make service-up

api-up:
	docker compose start demoapplication
api-down:
	docker compose stop demoapplication
api-restart:
	docker compose restart demoapplication
api-log:
	docker compose logs -f demoapplication


# ==================================================================================== #
# Maven Commands
# ==================================================================================== #
dependency-tree:
	./mvnw dependency:tree


# ==================================================================================== #
# Helper Commands
# ==================================================================================== #
help:
	@grep -E '^[a-zA-Z_-]+:.*?## .*$$' $(MAKEFILE_LIST) | sort | awk 'BEGIN {FS = ":.*?## "}; {printf "\033[36m%-30s\033[0m %s\n", $$1, $$2}'

.PHONY: run stop test local-run start-deps stop-deps docker-run service-build service-up service-down service-clean api-up api-down api-restart api-log dependency-tree help