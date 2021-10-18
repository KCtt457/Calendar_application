# CSC207 Final Project: Calendar Application

This application was created as part of the final project for the winter 2020 session of CSC207H1 - Software Design at
the University of Toronto. 

Created by: Gabriel Libozada Anover, Kimlin Ann-Marie Chin, Rikki Hung, Alvin Hao Yang Liang, Wuyue Lu, Cordelia Min, Shengye Niu

---------------------------------------------------
### SETUP

1. Navigate to the phase2/src/GUI
2. Run the main method located inside the MainWindow Class
3. Sign up for an account (or alternatively use the default login)  
    - Default login
        - username: user 
        - password: password  
4. You are now free personalize your own calendar, and use the calendar as you normally would!  
    -changes to your calendars will be saved for the next time you use it

---------------------------------------------------
### NOTABLE FEATURES

1. Calendar:  
    -You can make multiple accounts and calendars to suit your needs  
    -Switch the application background between light and dark mode depending on your preference  
    
2. Events:  
    -Create, organize and edit events for your calendars  
    -Postpone and duplicate your events   
    -Interact with other users on the platform by sharing events   
    -Respond to event invites to update your Calendar  
    
3. Memos and alerts:  
    -Create memos to add additional information to your event(s)  
    -Set alerts to remind you of when certain events will occur  

Demonstration video: https://youtu.be/sthMUE9fnfc
    
---------------------------------------------------
### KNOWN ISSUES

"I can't run the file cause, my program cannot locate the users.csv file"
- We found that this was a problem with UNIX/Mac OSX machines, the way to resolve this
    involves moving the users.csv up a node in the file structure
    
"I get a 'SEVERE: Cannot read from input.' warning with the java.io.InvalidClassException being 
thrown when logging into an already created user account (e.g. if using the default username and password)"
- This is because the code for one or more of the classes in CalendarSystem was updated after 
this account was created and has not been accessed since so when loading from the save file the 
data does not match up. This message does not affect the running of the calendar so you can continue 
to use it as normal. However, since the data could not be read from the save file, it would be a 
new blank (no data - no events, memos, alerts, etc.) calendar. You should not get this message when using 
a new account that you create
    
---------------------------------------------------
### NOTES

1. When editing a postponed event, if you do not set the time to a valid start/end date and time,
then you cannot make changes to the name or tag

2. Usernames cannot contain any underscores

3. Names for new calendars cannot contain the character "." or spaces
