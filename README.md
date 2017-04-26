3# Inventory Server #

A common feature of online games is the 'inventory' which provides a list of all of the items 'owned' by an object.

It is frequently used in MMOs.

This project attempts to make scaling this service easier by making the developer work a little harder up front,
by using persistent objects. The expected out come is that the datastore is significantly smaller, due to massive
inventory and item reuse. Another benefit is caching, the majority of calls to the service can be cached indefinitely.

The service provides the notions of

* A _user_: A root object that has a collection of inventories
* An _inventory_: Items that can be collected under a single category, eg: Player attributes, achievements, rock collection
* An _item_: A datum in an inventory, eg: First name, Sword of Justice

There is currently no authentication, but it should be easy to add.

### What is this repository for? ###

This repository is for anyone interested in seeing a demostration of a medium sized [Pestful](http://tailoredshapes.com/blog/2013/04/21/pest/) project. It doubles as the backend for my forthcoming game. 

### How do I get set up? ###

The project is built using [Maven](http://maven.apache.org/) on top of [Java 8](http://www.oracle.com/technetwork/java/javase/overview/java8-2100321.html).

It also uses vagrant to get you up and running quickly with a 'real' database.

* vagrant up - will give you a working mysql database
* mvn jetty:run - will give you a single instance of the app

This is a developer setup.

If you plan on using this project in production you are expected to:

* configure persistence.xml for your datastore
* mvn package: then deploy the war. [How you deploy is up to you](http://martinfowler.com/bliki/ContinuousDelivery.html)

### Contribution guidelines ###

* Where appropriate pull requests must be covered by tests
* Style: very strong preference for adherence to SOLID principles - no hacks

Great pains have been taken to make the code as modular and isolated as pragmatically possible.

### Why Java? ###

Java was the first language I used in a job where my title included the word 'software'.

It is also the language that most of my degree was in so I'm very comfortable thinking in it.

It has strengths that interested me for this project:

* Fast and reliable refactoring tools (IntelliJ)
* Excellent runtime characteristics for a server.
* Very fast build / test cycle (ms)
* More people can read and understand Java than almost any other language, a feature I value for a reference implementation.

But thats not the full reason.

I'm an [ex-ThoughtWorker](http://thoughtworks.com). Code quality is in my blood. I wanted to provide a concrete example
of what I believe good Java should look like:

* Using Dependency Injection to simplify composition over inheritance.
* Using Dependency Injection to prevent objects being passed up and down the call stack
* Using Java standards to allow for easy portability between vendors.

This is still very much a work in progress.



