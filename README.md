# Inventory Server #

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

This repository is for anyone interested in seeing a demostration of a medium sized [Pestful](http://tailoredshapes.com/blog/2013/04/21/pest/) project. 

### How do I get set up? ###

The project is built using [Maven](http://maven.apache.org/) on top of [Java 8](http://www.oracle.com/technetwork/java/javase/overview/java8-2100321.html).

It also uses vagrant to get you up and running quickly with a 'real' database.

* vagrant up - will give you a working mysql database

This is a developer setup.

If you plan on using this project in production you are expected to:

* configure ProdPersistence

* mvn package: then deploy the jar. [How you deploy is up to you](http://martinfowler.com/bliki/ContinuousDelivery.html)

### Contribution guidelines ###

* Where appropriate pull requests must be covered by tests
* Style: very strong preference for adherence to SOLID principles - no hacks

Great pains have been taken to make the code as modular and isolated as pragmatically possible.

### Why Java? ###

This project used to be about how to write SOLID, practical, Java services (see the project before the Spark work was merged in.), but has since evolved into a show case of how different Java 8 is vs Java 7 and older.

Moving away from small, single purpose objects 
* Fast and reliable refactoring tools (IntelliJ)
* Excellent runtime characteristics for a server.
* Very fast build / test cycle (ms)
* More people can read and understand Java than almost any other language, a feature I value for a reference implementation... but this project might scare some of them. Its different.

#### Modern Java ####

With the last iteration of this project I reached the conclusion that if you follow SOLID to its logical conclusion you end up with a lot of objects that look a lot like closures.

I also learned that its hard to manage those closures when there is only one of them in each file.

Moving to using static functions in interfaces has had the benefit of making the code very testable and, I hope, easier to manager.

At the very least it has meant that I've been able to express the problem in about half the code. 

So you now have a language that was already 'expressive enough' with good SOLID engineering practices, and effectively doubled its 'expressivity'. But it retains the runtime performance we've come to expect from the JVM and has world class tooling. 

Its getting harder to justify using another language for server side development. 


This is still very much a work in progress.



