# CardGame

## Introduction
This project is a simple desktop application that allows the user to play card games, similar to the classic game application that was included in previous versions of the Windows operating system. Currently, the only game implemented and fully playable is Blackjack.

## Technology
This application is created with the Java programming language, specifically Java 17. JavaFX is used to create the graphical user interface. Additionally, the application contains in-app documentation written in HTML. Both the JavaFX FXML and the HTML are styled with CSS. Dependency management and build automation are handled by Maven.

## How to play
As the application is created with Java and managed by Maven, it is required to have a Java Development Kit (JDK) installed, as well as Maven. Since a command line interface will be used in the following steps, make sure to add both the Java and Maven installations to the operating system's Path environment variable before proceeding.

### Windows:
1. Download the project zip file and unzip it. 
2. Launch the Windows command prompt (cmd).
3. Change directories using the command `cd <directory>`, where &lt;directory&gt; is the path of the directory you have moved the unzipped files to in the first step.
4. Execute the command `mvn clean javafx:run` to build and launch the application.
