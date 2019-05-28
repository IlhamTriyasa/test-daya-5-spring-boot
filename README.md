# test-daya-5-spring-boot
Detail Task : 
CRUD
Employee Table :
==========================================
Field           Type       Description
==========================================
employeeId    String .   ID/Primary Key
email .       String     Email
fullName      String     Full Name
 
Please create api for these actions :
 1. Add
    Add new employee *(DONE)
 2. Update
    Update existing employee *(DONE)
 3. Delete
    Delete existing employee *(DONE)
 4. FindOne
    Find existing employee using employeeId *(DONE)
 5. Paging
    List All employee using pagination. *(DONE)
    Eq : total employees are 100, we want to get the data only data from 20 to 30


Reshaping
* Create a configuration file to store the database connection configuration. Create two configuration files (example development and production). *(DONE)
* Please make the Delete function asynchronous *(NOT YET)
* Please include in the Paging function, a parameter called keyword, this parameter will be used to search to the employeeId, email and fullName column. *(DONE)
* Please create a unit testing for FindOne and  Add functions. *(NOT YET)
 
 Security
* Please create an authentication for the API *(DONE)
* Please create an Audit Trail to track which API invoked, by who and record the timestamp *(DONE)
* Please create a function to get the summarize of the invoked API by who and date. *(DONE)
* Please create a scheduler to get the data in 3 (c) emailed to admin *(DONE)
