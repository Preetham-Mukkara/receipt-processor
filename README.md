## How to run this application
Run these two commands: 
1) docker build -t app .
2) docker run -p 8080:8080 app

The first command will build a docker image named app and the second command will run the app image and expose the localhost 8080 port.
For example, after executing both the above commands you should be able to hit the GET /receipts/{id}/points endpoint via http://localhost:8080/receipts/{id}/points and the POST endpoint using a similar format.

## Micronaut 4.2.4 Documentation

- [User Guide](https://docs.micronaut.io/4.2.4/guide/index.html)
- [API Reference](https://docs.micronaut.io/4.2.4/api/index.html)
- [Configuration Reference](https://docs.micronaut.io/4.2.4/guide/configurationreference.html)
- [Micronaut Guides](https://guides.micronaut.io/index.html)
---

- [Shadow Gradle Plugin](https://plugins.gradle.org/plugin/com.github.johnrengelman.shadow)
- [Micronaut Gradle Plugin documentation](https://micronaut-projects.github.io/micronaut-gradle-plugin/latest/)
- [GraalVM Gradle Plugin documentation](https://graalvm.github.io/native-build-tools/latest/gradle-plugin.html)
## Feature serialization-jackson documentation

- [Micronaut Serialization Jackson Core documentation](https://micronaut-projects.github.io/micronaut-serialization/latest/guide/)


## Feature micronaut-aot documentation

- [Micronaut AOT documentation](https://micronaut-projects.github.io/micronaut-aot/latest/guide/)


