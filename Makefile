clean:
	./mvnw clean

unit-test:
	make clean && ./mvnw test -Punit-test

integration-test:
	make clean && ./mvnw test -Pintegration-test

mutation-test:
	./mvnw test-compile org.pitest:pitest-maven:mutationCoverage

mutation-test-with-history:
	./mvnw -DwithHistory test-compile org.pitest:pitest-maven:mutationCoverage

test:
	make clean && ./mvnw test && make mutation-test

docker-image:
	./mvnw spring-boot:build-image