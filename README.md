# Bowling Score Application

Bowling Score is an application that receives a text file with data from a bowling match and returns the result of the game.

## Installation

Download it using ```git clone https://github.com/tomasmendesr/bowling-app.git```

The application is made with:
- Java 8
- Maven

## Usage
First you need to compile the project. To do so, go to the root folder and run the following maven command:

`mvn clean install package`

Then just run it using:

`mvn exec:java -Dexec.args="fileDirectory"`

Where fileDirectory is the location of the file. For example "C:\Users\users\workspace\bowling-app\exampleFile.txt"

This file should has the following format:

```
Jeff 10
John 3
John 7
Jeff 7
Jeff 3
John 6
John 3
Jeff 9
Jeff 0
John 10
Jeff 10
John 8
John 1
Jeff 0
Jeff 8
John 10
Jeff 8
Jeff 2
John 10
Jeff F
Jeff 6
John 9
John 0
Jeff 10
John 7
John 3
Jeff 10
John 4
John 4
Jeff 10
Jeff 8
Jeff 1
John 10
John 9
John 0
```

## License
[Jobsity](https://www.jobsity.com/)
