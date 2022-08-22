## Java MLEM Client

---

### Introduction

There is a MLEM technology that helps you package and deploy machine learning models. It saves ML models in a standard format that can be used in a variety of production scenarios such as real-time REST serving or batch processing.

---

### Client description

Java MLEM client provides API for using MLEM technologies in your code with given schema. There are two methods for making requests:
1) **/predict**:
- sensd /predict post request with given body; 
- can handle the exception;
- can return a JsonNode response wrapped in the CompletableFuture object;
- works asynchronously

2) **/call**: 
- sends post request with given method and body;
- can handle the exception;
- can return a JsonNode or Object response wrapped in the CompletableFuture object;
- works asynchronously

---