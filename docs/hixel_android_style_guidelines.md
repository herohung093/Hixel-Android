[TOC]

# Introduction

This style guide only lists areas that we have run into over the course of developing the Hixel CorpReport Android app, for a more comprehensive guide consider, [Android Official Code Style](https://source.android.com/setup/contribute/code-style), [Google Java Style Guide](http://google.github.io/styleguide/javaguide.html), [Ribot Style Guide](https://github.com/ribot/android-guidelines/blob/master/project_and_code_guidelines.md). All of which this style guide borrows heavily from.

# 1. Style Guide

## 1.2 File naming

### 1.2.1 Class files

Use UpperCamelCase, classes that extend an Android component should end with the name of that component. E.g. `SignInActivity`.

#### 1.2.2 Resource files

Are always to be written in __lowercase_underscore__, and as specific as possible.

##### 1.2.2.2 Layout files

Follow the format:

__what:__ Indicate the resource, e.g. activity

__where:__ The resource logically belongs in the app, e.g. main. If used in multiple screens 	use _all_.

__description:__ to differentiate which element it is on screen, e.g. title.

__size (optional):__ Use a precise size, e.g. 24dp.

| Component      | Class Name           | Layout Name                |
| -------------- | -------------------- | -------------------------- |
| Activity       | MainActivity         | activity_main.xml          |
| Fragment       | ChartFragment        | fragment_chart.xml         |
| Dialog         | ChangePasswordDialog | dialog_change_password.xml |
| AdaperViewItem | ---                  | item_company.xml           |

##### 1.2.2.3 Menu files

Use the `menu_` prefix and then the name of the component that uses the menu.

##### 1.2.2.4 Values files

Resource files in the values folder should be __plural__, e.g ```styles.xml```

## 2. Code Guidelines

### 2.1 Java language rules

Refer the [Google Java Style Guide](http://google.github.io/styleguide/javaguide.html#s4.5-line-wrapping) for any Java related questions.

### 2.2 Java style rules

#### 2.2.1 Field definitions and naming

Put the at the __top of the file__, and __do not__ follow the normal convention of m-prefixes. Instead call the class what it is, e.g. ```Toolbar toolbar = binding.toolbar;```.

#### 2.2.2 Treat acronyms as words

```java
// Good
XmlHttpRequest;
String url;
long id;

// Bad
XMLHTTPRequest;
String URL;
long ID;
```

#### 2.2.3 Use spaces for indentation

Use __4 spaces__ for indent on blocks and __8 spaces__ for indents on line wraps.

#### 2.2.4 Use standard brace style

Braces go on the same line as the code before it, don't put them on seperate lines.

#### 2.2.5 Annotations

- ```@Override```: use this whenever you are overriding the decleration or implementation from a super-class.
- ```@SupressWarnings```: only when very necessary, e.g. failes "impossible to eliminate" test.

#### 2.2.6 Limit variable scope

Keep variable scope to a minimum, and try to always declare the variable at the point it is first used.

#### 2.2.7 Order import statements

1. Android imports
2. Third party imports
3. Java and javax
4. Project imports

Also ensure that they are alphabetically ordered by group and there is one blank line between each group.

#### 2.2.8 Logging

Use logs sparingly, they __should not be present in a release__ as it slows down performance substantially.

#### 2.2.9 Class member ordering

Try and keep things logical and consistent, following order is reccommended:

1. Constants
2. Fields
3. Constructors
4. Override methods and callbacks (public and private)
5. Public methods
6. Private methods
7. Inner classes and interfaces

If the class extends and Android component, order the override methods so that they __match the component's lifecycle__. E.g.

```java
public class MainActivity extends Activity {

	//Order matches Activity lifecycle
    @Override
    public void onCreate() {}

    @Override
    public void onResume() {}

    @Override
    public void onPause() {}

    @Override
    public void onDestroy() {}

}
```

#### 2.2.10 Parameter ordering in methods

If ```Context``` is present then it should be the first parameter, and callbacks should always be the last parameter.

#### 2.2.11 String constants, naming, and values

Always define it as ```static final```, and prefix as follows:

| Element            | Field Name Prefix |
| ------------------ | ----------------- |
| SharedPreferences  | `PREF_`           |
| Bundle             | `BUNDLE_`         |
| Fragment Arguments | `ARGUMENT_`       |
| Intent Extra       | `EXTRA_`          |
| Intent Action      | `ACTION_`         |

#### 2.2.15 Line length limit

Don't exceed __100 characters__, if ou do reduce the length by:

- Extract a local variable or method.
- Apply line-wrapping.

Exceptions to this are:

- Lines that cannot be split, e.g. long URLs in comments.
- `package` and `import` statements.

##### 2.2.15.1 Line-wrapping strategies

__Break at operators__

```Java
int longLine = aVeryLongNamedVariable 
		+ andAnotherLongNamedVariable;
```

__Break at assignment__

```java
int longLine = 
    aVeryLongNamedVariable + andAnotherLongNamedVariable;
```

__Method chain__

```java
Client.get()
      .something();
```

__Long parameters__

```java
methodToWrap(paramater1, 
       paramater2, 
       paramater3, 
       paramater4);
```

### 2.3 XML style rules

#### 2.3.1 Use self closing tags

If there is no content for the element, then use a self closing tag.

#### 2.3.2 Resource naming

##### 2.3.2.1 ID naming

| Element   | Prefix  |
| --------- | ------- |
| TextView  | text_   |
| ImageView | image_  |
| Button    | button_ |
| Menu      | menu_   |

##### 2.3.2.2 Strings

In general the prefix should be the section that the string belongs to, e.g. `forgot_password_hint`. If it __does not__ belong to a section, use the following:

| Prefix  | Description                   |
| ------- | ----------------------------- |
| error_  | An error message              |
| msg_    | A regular information message |
| title_  | A title, e.g. "CorpReport"    |
| action_ | An action, e.g. "Save"        |

##### 2.3.2.3 Styles and themes

Write them using __UpperCamelCase__.

#### 2.3.3 Attributes ordering

1. View Id
2. Style
3. Layout width and height
4. Other layout attributes, sorted alphabetically
5. Remaining attributes, sorted alphabetically