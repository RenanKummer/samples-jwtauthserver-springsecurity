# JWT Auth Server with Spring Security

This sample application explores how to create an authentication and authorization server 
that uses JWT tokens implemented with Spring Security framework.

## Local Setup

### Pre-requirements

- Git
- Docker
- [Java SDK 21](https://aws.amazon.com/corretto/)
- [IntelliJ IDEA](https://www.jetbrains.com/idea/)

### How to run?

1. Clone this repository to your computer.

```shell
# Option 1: SSH
% git clone git@github.com:RenanKummer/samples-jwtauthserver-springsecurity.git

# Option 2: HTTPS
% git clone https://github.com/RenanKummer/samples-jwtauthserver-springsecurity.git
```

2. Start Docker engine.


3. Open a terminal and run the following commands:

```shell
# Navigate to project root directory.
% cd /path/to/project

# Download and boot dependency containers with Docker Compose.
% docker-compose up
```

4. Open the project directory in IntelliJ.


5. Create run configuration.
    1. Open `Run / Edit Configurations...`.
    2. Press _Add New Configuration_ and select _Spring Boot_.
    3. Fill the required fields:
        - **Name**: Local
        - **Build and run > JDK**: Your JDK
        - **Build and run > Classpath**: `samples-jwtauthserver-springsecurity.main`
        - **Build and run > Main class**: `samples.jwtauthserver.springsecurity.JwtAuthServerApplication`


6. Run application with `Local` configuration.
