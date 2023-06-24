# JavaEmailProvider-UniTo
Simple Java email-provider.

## How is it
This project simulate comunication between more mail clients through a server that manage connections.

## How it work
This project is runnable with Intellij Idea, or others IDEs.
The components are:
* Client
* Server
* File storage

### Client
It need to open more clients to have a more complete experience.
The mail is opened randomly from a mail list, so it's possible that it will open the same mail 2 times.

### Server
It need to open only one server to manages clients.
Thanks to a pool thread it is possible to manage many clients without overloading the server. It is a scalable system!

### File storage
It's a Json file that is only read by the server.
Server read information in the Json file and send to clients new mails or others informations.

## Libraries that you need to execute this project
It need to download follow libraries:

JavaFx
> https://gluonhq.com/products/javafx/

Json-simple
> https://code.google.com/archive/p/json-simple/downloads

It need to go to *file* > *project structure* > *libraries* and add jar file for json-simple and all jar files in *gluon* > *lib* folder.
