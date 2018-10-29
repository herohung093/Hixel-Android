# CorpReport Android App

> The CorpReport Android app automates financial data analysis through data visualisations of XBRL data, and abstracting ratios to company scores enabling users to quickly identify the state of a their portfolio, assess new companies to add to their portfolio, and compare companies against one another using historical data.
>

## Getting Started

Make sure you have the latest Android Studio installed, then all you need to do is clone the repo and you're good to go!

### Requirements

- Java 1.8

- Android SDK 27
- Android SDK Tools
- Android Build Tools v24.0.1
- Android Support Repository

### Installing

Clone the repository and open in Android studio. We only have one build configuration so you should be all good to go from there!

Refer to the [Android Docs](https://developer.android.com/studio/build/building-cmdline) for a list of Gradle commands if you would like to do something different.

## Testing and QA

This project utilises a combination of unit tests, functional tests, and code analysis tools.

### Unit Tests

To run __unit__ tests on your machine:

```java
./gradlew test
```

### Functional Tests

To run __functional__ tests on a connected device:

```java
./gradlew connectedAndroidDevice
```

__NOTE:__ You can also use Android studios build system for any Gradle commands.

### Code Analysis

These are tools to ensure code consistency and quality:

- PMD: Identifies dead/duplicated code, etc.

  ```java
  ./gradlew pmd
  ```

- Findbugs: Analysis byte code to find flaws/bugs in our code.

- Checkstyle:  Ensures we follow our code style guide, we use the [Ribot Checkstyle](https://github.com/ribot/ribot-app-android/blob/master/config/quality/checkstyle/checkstyle-config.xml) with a slight modification for member variables.

  ```java
  ./gradlew checkstyle
  ```

### Further Reading

- [Library documentation](/docs/libraries.md)
- [Architecture documentation](/docs/architecture-guide.md)
- [Code style documentation](/docs/hixel_android_style_guidelines.md)

