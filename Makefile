# project variables
PROJECT_NAME=performance-appraisal-server
PROJECT_VERSION=latest
DOCKER_REGISTRY=registry.medway.cloud
DOCKER_NAMESPACE=performance-appraisal
DOCKER_IMAGE=$(DOCKER_REGISTRY)/$(DOCKER_NAMESPACE)/$(PROJECT_NAME)
DOCKER_TAG=$(PROJECT_VERSION)

.PHONY: all
all: docker

.PHONY: clean
clean:
	mvn -B -U clean

.PHONY: compile
compile::clean
	mvn -B -U compile -Dmaven.test.skip=true

.PHONY: test
test::clean
	mvn -B -U test

.PHONY: package
package::clean
	mvn -B -U package -Dmaven.test.skip=true

.PHONY: docker
docker::package
	docker build -t $(DOCKER_IMAGE):$(DOCKER_TAG) ./ -f ./Dockerfile

.PHONY: push
push:
	docker push $(DOCKER_IMAGE):$(DOCKER_TAG)

.PHONY: rmi
rmi:
	docker rmi $(DOCKER_IMAGE):$(DOCKER_TAG)
