
# wget -N http://central.maven.org/maven2/io/swagger/swagger-codegen-cli/3.0.0-rc1/swagger-codegen-cli-3.0.0-rc1.jar
wget -N http://central.maven.org/maven2/io/swagger/swagger-codegen-cli/2.4.0/swagger-codegen-cli-2.4.0.jar


JAR=swagger-codegen-cli-3.0.0-rc1.jar
JAR=swagger-codegen-cli-2.4.0.jar

# project initially seeded with code generator
# java -jar $JAR meta -p hello.codegen

java -jar $JAR generate -c swagger-config.json -l spring --library spring-boot -i swagger.yml

