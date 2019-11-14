# ThreeTenKTX

[![](https://jitpack.io/v/basshelal/ThreeTenKTX.svg)](https://jitpack.io/#basshelal/ThreeTenKTX) ![minAPI 21](https://img.shields.io/badge/minAPI-21-green.svg)


Kotlin extensions for JSR-310, for Java 8 and [ThreeTenABP](https://github.com/JakeWharton/ThreeTenABP).

## Installation

Add the JitPack repository to your **root** `build.gradle` at the end of repositories:

```gradle
allprojects {
    repositories {
        ...
        maven { url "https://jitpack.io" }
    }
}
```

Add the dependency in your **app module** `build.gradle` file:

```gradle
dependencies {
    implementation "com.github.basshelal.ThreeTenKTX:java8:0.1.1" // for Java 8 
    implementation "com.github.basshelal.ThreeTenKTX:threetenabp:0.1.1" // for ThreeTenABP
}
```

## Example

### Without ThreeTenKTX

```kotlin
Duration.between(LocalDate.now().atTime(17, 0), LocalDate.now().plusDays(1).atTime(2, 0))
```

### With ThreeTenKTX

```kotlin
(today at 5.pm) till (tomorrow at 2.am)
```