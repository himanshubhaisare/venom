## Requirements
JRE 8. Download here if you dont have it https://docs.oracle.com/javase/8/docs/technotes/guides/install/install_overview.html

## How to run application

mini venmo supports two modes of execution. `cd` into the directory where you extracted the code and executable.

1. to start interactive mode just type `./mini-venmo.sh`


    $./mini-venmo.sh
    mini venmo started in interactive mode. Enter help to see manual.
    > help
    user <name> : to create a user. e.g. user Himanshu
    add <user> <card number> : to add a credit card on user. e.g. add Himanshu 5555555555554444 
    balance <user> : to view balance of a user e.g. balance Himanshu 
    pay <actor> <target> <$amount> <note> : pay someone e.g. pay Himanshu Lisa $10.50 for coffee 
    feed <user> : shows activity feed of a user e.g. feed Himanshu 
    help : brings up manual 
    close : closes mini-venmo

2. from a file of newline-delimited commands, when provided with one argument. Following example will accept input from input.txt file and will create output in a file named output.txt


    $./mini-venmo.sh input.txt
   
## How to run tests


    $./mini-venmo-test.sh
    
## Design decisions

1. After reading the requirements and the list of input, output; I realized that there are three main resources
    * User
    * Card
    * Payment
  
    The resources are represented by there own classes in resources package
2. Each resource gets their own service class where the application logic/ business logic is handled. Venmo is implemented as an orchestrator that uses other services. All services are found in service package
3. Validators are run before creating and storing records in database. They are under validator package.
4. Database is in memory which does not persist after the application is closed. Users and Cards are stored in maps while payments are stored in list.
5. All commands and errors are available as string constants in constants package.
6. Did not use any testing framework because the application was fairly simple to test. Tests are included in tests package.
7. Scripts `mini-venmo.sh` and `mini-venmo-test.sh` simply run `venmo.jar` and `venmo-test.jar` respectively.

## Choice of Language : Java
Java runs on almost all environments. I used `java 8` for this project. I chose java because of I feel comfortable programming in java. It is a strongly typed, forces me to make less syntax errors and it supports both procedural as well as functional programming styles which gives me more flexibility as a programmer. 
