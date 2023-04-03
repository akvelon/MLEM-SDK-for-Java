# MLEM API Client v.1.0.2

## What is MLEM?

MLEM, developed by iterative.ai - is an open-source tool that allows users to simplify machine learning model packaging and deployment. With MLEM, users can run their machine learning models anywhere, by wrapping models as a Python package or Docker Image, or deploying them to Heroku (SageMaker, Kubernetes, and more platforms coming soon).
MLEM is written in Python, and runs as a microservice with applications calling for a simple HTTP API. This may concern some users who did not develop their applications in Python, which is why we decided to take a closer look at using models on different platforms.

Our client currently supports the **0.4.7 version of MLEM**.

## What is the Java Client for MLEM?

Our Java HTTP client is an essential tool for software developers looking to incorporate machine learning into their Java-based applications. With this client, developers can harness the power of MLEM's machine learning capabilities without needing a deep understanding of the underlying technology.

The client is designed as a library, providing a simple and easy-to-use interface for connecting to the MLEM API and integrating its model functionality into Java projects. It offers a range of features, including request body validation using schemas received from the MLEM server, as well as a standard logging interface to streamline the development process.

Whether you're building intelligent applications for business, research, or personal use, our Java HTTP client provides a reliable and efficient solution for integrating machine learning into your Java-based projects. With its easy-to-use interface and robust feature set, our client is the perfect tool for anyone looking to leverage the power of MLEM's machine learning capabilities.

The client offers two methods for using MLEM technologies with given rules: predict and call.
- `predict` makes an asynchronous request for the "predict" method
- `call` makes an asynchronous request for any methods (including "predict")

This is the core functionality of MLEM client for Java apps. Having a stable application with minimum functionality is a good way to support it now and make for easier improvements in the future.

⚠️ There is a [.NET client](https://github.com/akvelon/MLEM-SDK-for-C-Sharp) that does the same for .NET projects.

⚠️ There is a [technology article](https://akvelon.com/akvelon-enables-non-python-apps-to-integrate-machine-learning-models-with-mlem) about Java and .Net clients.

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
Record recordType = new Record();
// add the test data.
recordType.addColumn("sepal length (cm)", 1.2);
recordType.addColumn("sepal width (cm)", 2.4);
recordType.addColumn("petal length (cm)", 3.3);
recordType.addColumn("petal width (cm)", 4.1);
   
// add it to RecordSet.
RecordSet recordSet = new RecordSet();
recordSet.addRecord(recordType);
 
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
7) **Expected recordType name**
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
8) **Expected recordType type**
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
- LightGBM
- XGBoost
- Torch
- TensorFlow

Some of them are in `com.akvelon.client.model.request.typical` package.

They are built using `LearnModelScript.py` scripts for each model.

## Supported request object data types
Mlem client support the following types:
- Pandas types: dataframe
- Numpy types: ndarray
- List type
- TensorFlow types: torch
- Primitive types: float64, int (int8, int16,int32), uint (uint8, uint16, uint32, uint64), bool, str

Please note that mlem [already sorts](https://github.com/iterative/mlem/blob/afb18dba1cbc3e69590caa2f2a93f99dcdddf1f1/mlem/contrib/pandas.py#L161) dataframe fields in right order - so there is no need to worry about columns order for this datatype (treat it just like a dictionary type).


## Shared resources

Some resources should be the same in .NET and Java clients. So, this repository uses [git submodules](https://git-scm.com/book/en/v2/Git-Tools-Submodules) to get files like `Error_messages.json` and `Log_messages.json` from another private reporsitory - see README.md file in `[root]\ResourcesGenerator\CommonResources` for more details.
This file is located in `[root]\ResourcesGenerator\CommonResources` folder, that is a clone of that private repository in fact. So, this folder can contain any shared files defined there.

Regard to this Java repository, the client doesn't use these `.json` files directly. For example, `Error_messages.json` file converts into `\Resources\EM.java` file via `[root]\Update_resources.cmd` script; the same for `Log_messages.json` file and `[root]\MlemApi\Resources\LM.java`. After cloning of the repository you don't need to do anything additional to get the resources. They are already defined in `EM.java` and `LM.java` files.

To update resources do the following:
- Open `[root]\ResourcesGenerator\CommonResources\Error_messages.json` file (or `Log_messages.json`)
- Make some changes, it will affect the both .NET and Java clients
- Open `[root]\ResourcesGenerator\CommonResources` folder via git, here is the submodule local repository
- Make a commit and push it to the `main` branch (make sure you have necessary permissions)
- Next you need to update submodule dependencies. Just run `[root]\Update_resources.cmd` script. It will update `[root]\MlemApi\Resources\EM.java` and `[root]\MlemApi\Resources\LM.java` files and add submodule changes to the git index of the current .NET repository
- Commit and push the new changes

## Conclusion

MLEM makes the process of packaging and deployment of machine learning models much easier. Java client developed by Akvelon make it possible to integrate MLEM models to non-Python projects.
Use the client with your existing or new applications:
Web (Spring), Mobile (Android), Desktop (Swing).
Forget about the need to create spaghetti code and get access to the advantages of MLEM’s features in your apps with the http clients developed by Akvelon's engineers!

## What's Changed in 1.0.2 ?
Our project has recently undergone several upgrades to improve its functionality and performance.

1) JClient tested and upgraded to support the latest data types supported by MLEM, including LightGBM, XGBoost, Torch, and TensorFlow. This ensures that our client is compatible with the latest machine learning frameworks and can handle a wide range of data types.

2) The JClient code generator improved to create more specific request and response bodies. This makes it easier for users to understand and work with the generated code, improving the overall user experience.

3) The client's main method optimized and refactored to improve its performance and make it more efficient. This helps to reduce processing times and improve the overall speed and responsiveness of the client.

4) The jar file building process customized to ensure that our software is delivered to users in a more streamlined and efficient manner.

5) JClient supports the 0.4.7 version of MLEM 