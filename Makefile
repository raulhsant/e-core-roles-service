clean:
	./mvnw clean

unit-test:
	make clean && ./mvnw test

mutation-test:
	./mvnw test-compile org.pitest:pitest-maven:mutationCoverage

mutation-test-with-history:
	./mvnw -DwithHistory test-compile org.pitest:pitest-maven:mutationCoverage

test:
	make unit-test && make mutation-test