## MLEM JClient

---

### Introduction

Welcome to MLEM JClient - a MLEM technology that helps you package and deploy machine learning models.
It saves ML models in a standard format that can be used in a variety of production scenarios such as real-time REST
serving or batch processing.

MLEM JClient is an unofficial Java library for the MLEM API.
With JClient, you can easily integrate your Java application with the MLEM service.

Features of MLEM JClient:<br>
✔ Works on any Java Platform version 5 or later<br>
✔ No additional jars required<br>
✔ Request validation support<br>
✔ 100% MLEM API compatible<br>
✔ JavaDoc ready

---

### How to use

Just add MlemJClient.jar to your application classpath.
If you are familiar with Java language, looking into the JavaDoc should be the shortest way for you to get started.
MlemJClient.java interface is the one you may want to look at first.

---

### Source Code

The archive MlemJClient.jar contains jar file along with source code.

You can also browse the project repository at: <br>
https://git.akvelon.net:9443/internal/mlem-java/-/tree/develop

Or you can check out the latest source code as follows:<br>
git clone https://git.akvelon.net:9443/internal/mlem-java.git

---

### Client description

MLEM JClient provides API for using MLEM technologies in your code with given schema. There are three methods for
making requests: <br>
1) **/interface.json**:

- sends /interface.json get request;
- can handle the exception;
- returns a schema wrapped in the CompletableFuture object;
- works asynchronously;

2) **/predict**:

- sends /predict post request with given body;
- can have a Json or Request object as a body;
- can handle the exception;
- returns a JsonNode response wrapped in the CompletableFuture object;
- works asynchronously;
- get the validation rules that apply to the request;

3) **/call**:

- sends post request with given method and body;
- can have a Json or Request object as a body;
- can handle the exception;
- returns a JsonNode or Object response wrapped in the CompletableFuture object;
- works asynchronously;
- get the validation rules that apply to the request;

---

### Code Examples

1) **Create the MlemJClient object:**<br>

```java
// init host, create ExecutorService and :System.Logger implementation
String HOST_URL = "http://example-mlem-get-started-app.herokuapp.com/";
ExecutorService executorService = Executors.newFixedThreadPool(10);
System.Logger LOGGER = System.getLogger("logger name here");
// create the client
MlemJClient mlemClient = MlemJClientFactory.createMlemJClient(executorService,HOST_URL,LOGGER);
```

2) **Send the /interface.json request**
```java
// send the /inteface.json request.
CompletableFuture<JsonNode> future = clientWithExecutor.interfaceJsonAsync();
// handle the exception and get the response
JsonNode response = future
    .exceptionally(throwable -> {
        RestException restException = (RestException) throwable.getCause();
        assertResponseException(restException);
        return null;
    })
    .get();
```
        
3) **Build the request**

```java 
// create the Record object:
Record record = new Record();
// add the test data
record.addColumn("sepal length (cm)", 1.2);
record.addColumn("sepal width (cm)", 2.4);
record.addColumn("petal length (cm)", 3.3);
record.addColumn("petal width (cm)", 4.1);
   
//add it to RecordSet:
RecordSet recordSet = new RecordSet();
recordSet.addRecord(record);
 
// create the Request object and add the recordSet object with propertyName:
Request request = new Request();
request.addParameter(propertyName, recordSet);
 ```

4) **Send the /predict request:**

```java 
// send the /predict request.
CompletableFuture<JsonNode> future = mlemClient.predict(request);
// get the response
JsonNode response1 = future.get();
//to handle an exception use exceptionally method
JsonNode response2 = future
    .exceptionally(throwable -> {
        RestException restException = (RestException) throwable.getCause();
        return null;
    })
    .get();
```
So, for the /predict request with body:
```json
{"data":{"values":[{"sepal length (cm)":0,"sepal width (cm)":0,"petal length (cm)":0,"petal width (cm)":0}]}}
```
The response will be: 
```json 
[0]
```

4) **Send the /call request:**
```java 
// send the /predict_proba request.
CompletableFuture<JsonNode> future = mlemClient.call("predict_proba", request);
// get the response and handle the exception
JsonNode response = future
    .exceptionally(throwable -> {
        RestException restException = (RestException) throwable.getCause();
        return null;
    })
    .get();
```
So, for the /predict_proba request with body:
```json
{"X":{"values":[{"sepal length (cm)":0,"sepal width (cm)":0,"petal length (cm)":0,"petal width (cm)":0}]}}
```
The response will be:
```json 
[0]
```