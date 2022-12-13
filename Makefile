clean:
	./mvnw clean

unit-test:
	./mvnw clean test

verify:
	./mvnw clean verify

mutation-test:
	./mvnw test-compile org.pitest:pitest-maven:mutationCoverage

mutation-test-with-history:
	./mvnw -DwithHistory test-compile org.pitest:pitest-maven:mutationCoverage

test:
	make unit-test && make mutation-test

docker-image:
	make clean && ./mvnw spring-boot:build-image -Dmaven.test.skip.exec -Dspring-boot.build-image.imageName=raulhsant/roles-service