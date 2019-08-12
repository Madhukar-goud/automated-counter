# Problem Statement
## The Task

An automated traffic counter sits by a road and counts the number of cars that go past. Every
half-hour the counter outputs the number of cars seen and resets the counter to zero. You are part of a development team that has been asked to implement a system to manage this data the first task required is as follows :
- Write a program that reads a file, where each line contains a timestamp (in
yyyy-mm-ddThh:mm:ss format, i.e. ISO 8601) for the beginning of a half-hour and the number of
cars seen that half hour. An example file is included on page 2. You can assume clean input,
as these files are machine-generated.

## The program should output:

- The number of cars seen in total
- A sequence of lines where each line contains a date (in yyyy-mm-dd format) and the
number of cars seen on that day (eg. 2016-11-23 289) for all days listed in the input file.
- The top 3 half hours with most cars, in the same format as the input file
- The 1.5 hour period with least​ cars (i.e. 3 contiguous half hour records)

## Constraints

- The program must be written in Java. You are welcome (and encouraged) to use any of
the language features of Java 8.
- The program must be accompanied with reasonable levels of unit tests.
- The solution should be developed to professional standards, the code may be used and
extended by your teammates.
- The solution should be deliverable within a couple of hours - please do not spend
excessive amounts of time on this.

# Solution

## The application has 3 main functionalities

- Traffic Data is the main collection we are using for this
- Read the file into list of TrafficData
- Perform operations on the list of Traffic Data to print the required outputs. LoadData class is the main class where all these operations are performed

## Config and Aspect packages

- You can specify the path of the file in application.properties file. AppConfig file picks up the config from this location.
- You can also specify if you want to log the execution time of each method by using the param is.logexecutiontime to true.
	The methods that are annotated by @LogExecution method will log the time taken.
