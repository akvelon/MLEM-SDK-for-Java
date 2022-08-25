## Java MLEM Client

---

### Introduction

There is a MLEM technology that helps you package and deploy machine learning models. 
It saves ML models in a standard format that can be used in a variety of production scenarios such as real-time REST serving or batch processing.

MlemClient is an unofficial Java library for the MLEM API.
With MlemClient, you can easily integrate your Java application with the MLEM service. 

MlemClient is featuring:<br>
✔ Works on any Java Platform version 5 or later<br>
✔ No additional jars required<br>
✔ Request validation support<br>
✔ 100% MLEM API compatible<br>
✔ JavaDoc ready

---

### How to use

Just add MlemClient.jar to your application classpath.
If you are familiar with Java language, looking into the JavaDoc should be the shortest way for you to get started.
MlemHttpClient interface is the one you may want to look at first.

---

### Source Code

The archive MlemClient.jar contains jar file along with source code.

You can also browse the project repository at: <br>
https://git.akvelon.net:9443/internal/mlem-java/-/tree/develop  

Or you can check out the latest source code anonymously as follows:<br>
git clone https://git.akvelon.net:9443/internal/mlem-java.git

---

### Client description

Java MlemClient provides API for using MLEM technologies in your code with given schema. There are two methods for making requests:

1 **/predict**:
- sends /predict post request with given body;
- can handle the exception;
- returns a JsonNode response wrapped in the CompletableFuture object;
- works asynchronously;
- validates the parameters by schema;

2 **/call**:
- sends post request with given method and body;
- can handle the exception;
- returns a JsonNode or Object response wrapped in the CompletableFuture object;
- works asynchronously;
- validates the parameters by schema;

---

### Code Examples