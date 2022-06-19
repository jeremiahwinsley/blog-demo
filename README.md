# Example blog

### Development Environment
Set the Intellij run configuration to the `dev` profile,
and edit config/application-dev.properties to set environment-specific properties.

### Build instructions
1. Java 17 must be used to run this build.
2. Run `./gradlew build`.
3. Run `docker-compose up`
4. Application should be available at `http://localhost:8080`.
5. Access admin panel at `http://localhost:8080/admin`
   1. default credentials `admin:password` or `author:password`
