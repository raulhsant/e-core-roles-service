# The Project

This is a project proposed as a challenge from the E-Core/Tempo recruitment process.

The idea is to provide a service to enhance the existing roles e teams services, with the concept of team roles.

The roles can be associated to team members, that is, a member x on a team y can have an associated role.  

There are three predefined roles **Developer**, **Product Owner** and **Tester**, in which **Developer** of the 
default one.

The service should be able to:
  1. Create a new role
  2. Assign a role to a team member
  3. Look up a role for a team member
  4. Look up every member with a specific role

When executed, the swagger-ui page for the project can be accessed at:

http://localhost:8080/api/roles-service/swagger-ui/index.html

## The Approach

### Model
To allow every member to have multiple roles but only one per team, the member-team pair was modele as a Membership object, composed by the before mentioned pair and a role.

### Design Decisions

I decided to enforce responsibility separation on the service and did not 
make any type of migration or enrichment of the provided user/team data, except the sanity checks 
that the team exists and the user is a member of it.

This decision was based on the assumption that if someone wants to get the role for a membership, the membership
should already exist on the client side and, if it does not exist, it's the responsibility of the client to enrich 
what they already have with the data that matters to them.

It also renders the synchronization on the three systems databases unnecessary, since every information should be 
retrieved from its owner respectfully by the client.

On regards of the default role, this approach present a caveat. The return of the prerequisite *4* (memberships of 
a role) for the default role **Developer** will *not* return every membership that has a developer role, just the 
ones explicitly declared as so. This way, a membership can be returned by the *Look up a role for a team member* 
call with the developer role, and not be present on the developer role membership list of the *Look up every member 
with a specific role* call.

### Extra endpoints

In total 6 endpoints were implemented, resulting in 2 extra ones:

1. List existing roles
2. Return current role for membership

The first one is basically a helper interface to better understand which roles already exists on the database.

The second one is an interpretation of the *Look up a role for a team member* but without having to define a role.


### Database Used

For this project I decided to use a relational database (MariaDB), in part because I thought it made more sense and in 
part to step out of my comfort zone of working with DynamoDB. 

The great advantage of DynamoDB over MariaDB in this case would be the auto scaling/provisioned throughput option, 
that allows really fast scaling on the database size and key based fast retrieving of data,
but I don't think it would make a lot of difference, even in the long run when the throughput would get higher, a cache would be enough to keep using the SQL 
database.

Furthermore, as there _**is**_ a strong relation between two entities, a relational database is the logical option.  

## Running the tests and the application

A Makefile was built to help on running and building the application.

### Running Tests

#### Unit Tests

To run the unit tests you should simply execute the command:

``` shell script
$ make unit-test
```

**Note:** The project automatically generates a coverage report with jacoco that can be found on `target/site/jacoco/index.html`

#### Mutation Tests

To run the mutation tests you should simply execute the command:

``` shell script
$ make mutation-test
```

**Note:** The project automatically generates a coverage report with pitest that can be found on 
`target/pit-reports/index.html`


#### Unit and Mutation Tests

If you want to run unit and mutation tests with a single command, you can use:

``` shell script
$ make test
```

## Running the project

The recommended way to run this project is through docker-compose.
It will deploy the database (MariaDB), and a helper service (Adminer) to access the database, as well as executing 
the required migrations on the database itself.

To use docker-compose you will need to have docker (or similar, e.g. containerd/nerdctl) and docker-compose 
installed on your machine. After this, it's necessary to build the service's docker image, as I'm not delivering a 
remote one.

### Building Docker Image

To build the docker image you need to run the command:

``` shell script
$ make docker-image
```

It will create an image named `raulhsant/roles-service` with the tag `latest` locally using the 
`spring-boot:build-image` command.

### Executing Project

To execute the project, executes a docker-compose up:

``` shell script
$ docker-compose up -d
```

### Building Image and Executing Project

To build image and execute the project with one command, you can use:

``` shell script
$ ./run-on-docker.sh
```

### Docker infrastructure

The infrastructure that is deployed with the docker-compose command consists in 3 containers and a network that is 
shared by them.

The containers and locally exposed ports are:

1. db: A MariaDB Instance. No ports are exposed to the local machine.
2. adminer: A SQL database management tool. It's exposed on port `8090`. You can access it by clicking 
   [here](http://localhost:8090)
3. roles_service: The roles service itself. It's exposed on port `8080`. You can access it's swagger-ui by clicking
   [here](http://localhost:8080/api/roles-service/swagger-ui/index.html)


## Improvements for Team or User Services

Because I decided to not enrich the membership on my side I didn't use that much of the given services. 
Besides that, for the validation of the membership (team exist and user is a member of it) I saw that, if there is 
no team for the given id, the api returns 200 with "null".

It would be better to return either a 404 or a 204, indicating that the team could not be found or there is no 
content to return.

Another good improvement would be to add a little intelligence to the services and have an endpoint to check if a 
user is a member of a team directly.