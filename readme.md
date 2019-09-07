# Similar Words Generator desktop application

It is a program to generate words similar to given input.

## Description

A project for desktop application for generating words. It bases on library added to Central Repository. There are two core classes: Analyser and Generator. First is responsible for analysing an input, second makes new words.
Builder design pattern is used for creating parameters for Generator. Memento pattern is to restore settings after closing the application.

## Installation

The project uses Java SDK 12 and JavaFX SDK 12. Follow instructions on https://openjfx.io/openjfx-docs/ to add JavaFX to the project.<br/> 
For tests add in VM configurations:<br/>
 -Ddir.test.files="projectRoot\Test\Files".

## Usage

A window application is made to operate the model. Class Controller is responsible for communication between model and application.

![sample](sample.jpg)

## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

Please make sure to update tests as appropriate.

## License
[MIT](https://choosealicense.com/licenses/mit/)