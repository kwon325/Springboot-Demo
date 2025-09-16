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
else
	@echo "Error: RUN_MODE는 'local' 또는 'docker'만 가능합니다."
endif

## Spring Boot 앱 중지 (local/docker 모드)
stop:
ifeq ($(RUN_MODE), local)
	@echo "로컬 모드에서는 Ctrl+C 로 직접 중지하세요."
else ifeq ($(RUN_MODE), docker)
	make api-down
else
	@echo "Error: RUN_MODE는 'local' 또는 'docker'만 가능합니다."
endif

## Spring Boot 앱 재시작 (local/docker 모드)
restart:
ifeq ($(RUN_MODE), local)
	@echo "로컬 모드는 Ctrl+C로 중지 후 'make run'으로 다시 시작하세요."
else ifeq ($(RUN_MODE), docker)
	make api-restart
else
	@echo "Error: RUN_MODE는 'local' 또는 'docker'만 가능합니다."
endif

## Spring Boot 앱 로그 확인 (docker 모드 전용)
logs:
ifeq ($(RUN_MODE), local)
	@echo "로컬 모드에서는 실행 중인 터미널에서 로그가 바로 보입니다."
else ifeq ($(RUN_MODE), docker)
	make api-log
else
	@echo "Error: RUN_MODE는 'local' 또는 'docker'만 가능합니다."
endif

## 테스트 코드 실행
test:
	make local-code-test


# ==================================================================================== #
# Local Commands
# ==================================================================================== #

## (Local) Spring Boot 앱을 로컬에서 직접 실행 (포그라운드)
local-run:
	./mvnw spring-boot:run -Dspring-boot.run.profiles=local

# ==================================================================================== #
# Docker Commands
# ==================================================================================== #

## (Docker) Docker Compose로 Spring Boot 앱 시작
docker-run:
	make service-up

# ==================================================================================== #
# Docker Compose Commands (기존과 동일)
# ==================================================================================== #

## Docker Compose로 모든 서비스 빌드 (dev/prod 환경 구분)
service-build:
ifeq ($(ENV), dev)
	docker compose build
else ifeq ($(ENV), prod)
	docker compose -f docker-compose.yml -f docker-compose.prod.yml build
endif

## Docker Compose로 모든 서비스 시작 (백그라운드)
service-up:
ifeq ($(ENV), dev)
	docker compose up -d
endif

## Docker Compose로 모든 서비스 중지
service-down:
ifeq ($(ENV), dev)
	docker compose down
endif

## Docker Compose로 모든 서비스 중지 및 볼륨 삭제 (DB 데이터 초기화)
service-clean:
ifeq ($(ENV), dev)
	docker compose down -v
endif


# ==================================================================================== #
# Application Container Commands (기존과 동일)
# ==================================================================================== #
api-up:
	docker compose start demoapplication
api-down:
	docker compose stop demoapplication
api-restart:
	docker compose restart demoapplication
api-log:
	docker compose logs -f demoapplication


# ==================================================================================== #
# Maven Commands (기존과 동일)
# ==================================================================================== #
local-code-test:
	./mvnw test
dependency-tree:
	./mvnw dependency:tree


# ==================================================================================== #
# Helper Commands (기존과 동일)
# ==================================================================================== #
help:
	@grep -E '^[a-zA-Z_-]+:.*?## .*$$' $(MAKEFILE_LIST) | sort | awk 'BEGIN {FS = ":.*?## "}; {printf "\033[36m%-30s\030[0m %s\n", $$1, $$2}'

.PHONY: run stop restart logs test local-run docker-run service-build service-up service-down service-clean api-up api-down api-restart api-log local-code-test dependency-tree help