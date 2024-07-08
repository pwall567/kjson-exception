# kjson-exception

[![Build Status](https://github.com/pwall567/kjson-exception/actions/workflows/build.yml/badge.svg)](https://github.com/pwall567/kjson-exception/actions/workflows/build.yml)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Kotlin](https://img.shields.io/static/v1?label=Kotlin&message=v1.9.24&color=7f52ff&logo=kotlin&logoColor=7f52ff)](https://github.com/JetBrains/kotlin/releases/tag/v1.9.24)
[![Maven Central](https://img.shields.io/maven-central/v/io.kjson/kjson-exception?label=Maven%20Central)](https://search.maven.org/search?q=g:%22io.kjson%22%20AND%20a:%22kjson-exception%22)

Exception class for multiple JSON projects.

## Background

The library contains a single class &ndash; `JSONException`.
This class has been extracted from the [`kjson-core`](https://github.com/pwall567/kjson-core) library in order to allow
it to be used independently of that library.

## Usage

The class is very straightforward &ndash; to throw a `JSONException` with a simple message:
```kotlin
        throw JSONException("Something went wrong")
```
In this case, the message for the exception will be just the text supplied.

But a common requirement of error reporting in a JSON application is to identify the location within the JSON structure
where the error occurred.
For this purpose, the `JSONException` takes an optional second parameter, a `key`, specified as type `Any?`.
If the `key` is present, and the `toString()` of its value is not empty, it is appended to the message with the word
"`at`", as follows:
```kotlin
        throw JSONException("Something went wrong", "/data/0/id")
```
This will produce the message: "`Something went wrong, at /data/0/id`".

The example shows a [JSON Pointer](https://tools.ietf.org/html/rfc6901) as the key, but the key can be anything with a
useful string representation, for example:
```kotlin
        throw JSONException("Something went wrong", "line $lineNumber")
```
This will produce a message like: "Something went wrong, at line 27".

## Cause

The `withCause()` function may be used to add a "`cause`" (an underlying causative exception) to the `JSONException`:
```kotlin
        try {
            doSomething()
        }
        catch (e: Exception) {
            throw JSONException("Something failed").withCause(e)
        }
```
This may be called once only, and should usually be called immediately after the creation of the exception.

## Derived Classes

The `JSONException` class is `open`, to allow it to be the base of a hierarchy of exception classes.
The `key` property is also `open`, so that derived classes may restrict the `key` to a specific type.
For example:

```kotlin
class ParseException(
    text: String,
    override val key: Coordinates,
) : JSONException(text, key)

data class Coordinates(val line: Int, val column: Int) {
    override fun toString(): String = "line $line, column $column"
}
```

## Dependency Specification

The latest version of the library is 1.2, and it may be obtained from the Maven Central repository.

### Maven
```xml
    <dependency>
      <groupId>io.kjson</groupId>
      <artifactId>kjson-exception</artifactId>
      <version>1.2</version>
    </dependency>
```
### Gradle
```groovy
    implementation 'io.kjson:kjson-exception:1.2'
```
### Gradle (kts)
```kotlin
    implementation("io.kjson:kjson-exception:1.2")
```

Peter Wall

2024-07-08
