# JavaEmailProvider-UniTo
Simple Java email-provider.

## How is it
This project simulate communication between more mail clients through a server that manage connections.

## How it works
This project is runnable with Intellij Idea, or others IDEs.
The components are:
* Client
* Server
* File storage

### Client
It needs to open more clients to have a more complete experience.
The mail is opened randomly from a mail list, so it's possible that it will open the same mail 2 times.

### Server
It needs to open only one server to manages clients.
Thanks to a pool thread it is possible to manage many clients without overloading the server. It is a scalable system!

### File storage
It's a Json file that is only read by the server.
Server read information in the Json file and send to clients new mails or others information.

### Mails of test
I created 5 mail of different format to test:

* katherine.johnson@unito.it
* doodo345@mymail.com
* hford@gmail.com
* riccardoventurini@yahoo.it
* francois.marshal@usmb.fr