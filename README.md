# Akka actor versioning system example project

This project shows how to use AVS. It contains two subprojects, to make sure that the setup you are running has separate classpaths. Subprojects are:
- test - actor system with AVS enabled
- sender - project with a set of actor classes that can be swapped in test

# Usage
1. Make sure that you have latest version of AVS published to your ivy repo.
2. Run test system with ```sbt run```
3. Run sbt in sender project
4. In sbt console type: ```swap-actor-impl {FQN of actor class to replace} {FQN of new actor class}```, f.e. ```swap-actor-impl org.virtuslab.test.PrinterActor org.virtuslab.test.PrinterActor2```
