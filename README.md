** How to deploy and access rest apis **

## How to deploy project

1. Clone the  [ebi repository](https://bitbucket.org/rrbadam/ebi.git) using git clone
2. Move to folder /ebi under checkout
3. Open command prompt at current location you are at
4. To generate spring boot jar run ***gradle clean bootJar***. This will generate ebi-v1.jar in /ebi/build/libs/ folder
5. To create docker image execute command ***docker build -t persons-interface-image .***. This will create image in your local images hub
6. To create running containers execute command ***docker-compose up -d***
7. This will launch a Spring rest api application and a mongodb instance as datastore
8. Application will be running on port 9080

---


## Rest apis

1. After creating containers above you can access the rest apis [swagger documentation](http://[host]:9080/swagger-ui.html#/person45controller) @ http://[host]:9080/swagger-ui.html#/person45controller
2. Apis are present for below operations and can be accessed using Basic authentication and credentials are userName: **user1** and password: **user1Pass**
   
    *    To get all persons available in registry
    *    To get a person based on id
    *    To create a new person entry in registry
    *    To update a person entry based on id
    *    To delete a person
    
3. Swagger UI is integrated with the project for accessing full documentation of apis

---


## Testing

Junits have been implemented with full coverage

---

## Deployment

1. Deployment is automated using
   
    *    Docker image build
    *    Docker Compose

---

## Security

1. Security is implemented in project using Basic authentication and can be extended to others

---

## Documentation

1. Swagger UI is integrated that gives full insights about apis
2. Method level documentation is also maintained

---

## Scalabilty

1. Since application is dockerized, we can scale app based on our load and requirements

---

## Others
1. Internationalization is implemented and validation responses can be served based on user locale
2. Exception Handling is generalized
3. Environment specific data is maintained in application.properties
4. Mongodb is used as back-end datastore

---
