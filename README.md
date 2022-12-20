# MLEM API Client

## What is MLEM?

MLEM, developed by iterative.ai - is an open-source tool that allows users to simplify machine learning model packaging and deployment. With MLEM, users can run their machine learning models anywhere, by wrapping models as a Python package or Docker Image, or deploying them to Heroku (SageMaker, Kubernetes, and more platforms coming soon).
MLEM is written in Python, and runs as a microservice with applications calling for a simple HTTP API. This may concern some users who did not develop their applications in Python, which is why we decided to take a closer look at using models on different platforms.


## What is the Java Client for MLEM?

This Java HTTP client is designed to help software developers use a machine learning approach to create intelligent applications, based on Java, without needing a deep knowledge of MLEM. This client is a library that allow users to connect with MLEM API simply and easily, as well as integrate the MLEM model functionality to their Java project. It also supports request body validation by schema received from the MLEM server, and also offers a standard logging interface to use.

The client provides several methods for using MLEM technologies with given rules. The main of them are `predict` and `call`.
- `predict` makes an asynchronous request for the "predict" method
- `call` makes an asynchronous request for any methods (including "predict")

This is the core functionality of MLEM client for Java apps. Having a stable application with minimum functionality is a good way to support it now and make for easier improvements in the future.

⚠️ There is a [.NET client](https://gitlab.akvelon.net/sdc/mlem-c-sharp/mlem-c-sharp) that does the same for .NET projects.

## Getting Started
Before using the client make sure that you have a deployed mlem model (local or remote). Read [MLEM docs](https://mlem.ai/doc/get-started) to know how to deploy a model. A list of sample models you can find below. Also, clone the repository and build the `MlemApi` project.

After you have a link to a deployed model, prepare your application that will use MLEM capabilities. Just add MlemJClient.jar to your application classpath.
If you are familiar with Java language, looking into the JavaDoc should be the shortest way for you to get started.
MlemJClient.java interface is the one you may want to look at first.


### Code Examples

1) **Create the MlemJClient object:**<br>

```java
// init host.
String HOST_URL = "http://example-mlem-get-started-app.herokuapp.com/";
// create ExecutorService.
ExecutorService executorService = Executors.newFixedThreadPool(10);
// create the simple implementation of System.Logger.
// you can use other libraries for logging: log4j and slf4j.        
System.Logger LOGGER = System.getLogger("logger name here");
// create the client.
MlemJClient mlemClient = MlemJClientFactory.createMlemJClient(executorService, HOST_URL, LOGGER);
```

2) **Create the request body**

```java 
// create the Record object:
Record record = new Record();
// add the test data.
record.addColumn("sepal length (cm)", 1.2);
record.addColumn("sepal width (cm)", 2.4);
record.addColumn("petal length (cm)", 3.3);
record.addColumn("petal width (cm)", 4.1);
   
// add it to RecordSet.
RecordSet recordSet = new RecordSet();
recordSet.addRecord(record);
 
// create the RequestBody object and add the recordSet object with property "data".
RequestBody requestBody = new RequestBody();
requestBody.addParameter("data", recordSet);
 ```

3) **Send the /predict request:**

```java 
// send the /predict request.
CompletableFuture<JsonNode> future = mlemClient.predict(requestBody);
// get the response.
JsonNode response1 = future.get();
//to handle an exception use exceptionally method.
JsonNode response2 = future
    .exceptionally(throwable -> {
        InvalidHttpStatusCodeException invalidHttpStatusCodeException = 
        (InvalidHttpStatusCodeException) throwable.getCause();
        return null;
    })
    .get();
```
So, for the /predict request with body:
```json
{"data":{"values":[{"sepal length (cm)":0.0,"sepal width (cm)":0.0,"petal length (cm)":0.0,"petal width (cm)":0.0}]}}
```
The response will be:
```json 
[0]
```

4) **Send the /call request:**
```java 
// send the /predict_proba request.
CompletableFuture<JsonNode> future = mlemClient.call("predict_proba", requestBody);
// get the response and handle the exception.
JsonNode response = future
    .exceptionally(throwable -> {
        InvalidHttpStatusCodeException invalidHttpStatusCodeException = (InvalidHttpStatusCodeException) throwable.getCause();
        return null;
    })
    .get();
```
So, for the /predict_proba request with body:
```json
{"X":{"values":[{"sepal length (cm)":0.0,"sepal width (cm)":0.0,"petal length (cm)":0.0,"petal width (cm)":0.0}]}}
```
The response will be:
```json 
[0]
```

5) **Expected method name**
```java 
// send the /predict_proba123 request.
CompletableFuture<JsonNode> future = mlemClient.call("predict_proba123", requestBody);
```
The response will be:
```text 
error text: The path predict_proba123 is not found in schema; Available path list: 
[sklearn_predict, predict_proba, predict, sklearn_predict_proba].
```
6) **Expected parameter name**
```java 
// send the /predict request.
CompletableFuture<JsonNode> future = mlemClient.predict(requestBody);
```
So, for the /predict request with body:
```json
{"X":{"values":[{"sepal length (cm)":1.3,"sepal width (cm)":2.3,"petal length (cm)":3.4,"petal width (cm)":4.7}]}}
```
The response will be:
```text 
error text: Actual parameters: X, expected: data
```
7) **Expected record name**
```java 
// send the /predict request.
CompletableFuture<JsonNode> future = mlemClient.predict(requestBody);
```
So, for the /predict request with body:
```json
{"data":{"values":[{"sepal length ":1.1,"sepal width (cm)":2.1,"petal length (cm)":3.1,"petal width (cm)":4.1}]}}
```
The response will be:
```text 
error text: Column name not found: sepal length (cm), for given data: [sepal length =1.1, sepal width (cm)=2.1, petal length (cm)=3.1, petal width (cm)=4.1]
```
8) **Expected record type**
```java 
// send the /predict request.
CompletableFuture<JsonNode> future = mlemClient.predict(requestBody);
```
So, for the /predict request with body:
```json
{"data":{"values":[{"sepal length (cm)":1,"sepal width (cm)":2.1,"petal length (cm)":3.1,"petal width (cm)":4.1}]}}
```
The response will be:
```text 
error text: Expected type for column: sepal length (cm) with value: 1, must be: Float64
```

## Validation

Mlem client provides validation functionality for request objects and response based on api schema of the deployed mlem model. Since the mlem server does not provide a verbosed error in case of data format error - this feature can be useful to catch data format issues easier.
While request object validation allows you to double-check that input data was provided properly to the client, the response validation helps to check that a mlem model returns a response according to the schema.
For the request validation it is required to pass a boolean parameter to the client constructor, so the mlem client will be able to associate relevant data fields from a request object and schema.

You can turn on/off this feature using `validationOn` properties of the mlem client for request and response objects validation respectively (turned off by default).

## Classes generation

Mlem client provides classes generation functionality for request objects and response based on api schema of the deployed mlem model. 
This feature can be useful to send request or handle a response easier.

You can generate classes using `modelgenerator` package and `ExampleGenerator.java` class of the mlem client for request and response objects.

## Sample ML models

There are the following sample models, that can be used for deployment and requests testing.
- RegModel
- Iris
- Digits
- Wine

They are in `com.akvelon.client.model.request.typical` package.

They are built using `LearnModelScript.py` scripts for each model.

## Conclusion

MLEM makes the process of packaging and deployment of machine learning models much easier. Java client developed by Akvelon make it possible to integrate MLEM models to non-Python projects.
Use the client with your existing or new applications:
Web (Spring), Mobile (Android), Desktop (Swing).
Forget about the need to create spaghetti code and get access to the advantages of MLEM’s features in your apps with the http clients developed by Akvelon's engineers!
