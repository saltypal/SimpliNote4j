package com.yourplan.yourplan;/*
 * THE EVENTS CLASS.
 * 
 * 
 * this class, we will write all the events into a file. so whenever an event is created, it will be written into a file.
 * For every event there is an object being created. The object is destroyed after the event is written into the file.
 * for deleting an event or modifying an event, create an object, do the required and destroyt eh object.
 * 
 * ANOTHER APPROACH:
 * we can use ONLY ONE OBJECT IN THE ENTIRE PROGRAM using the serializable keyword.
 * For this we use the implements keyword. Implements deals with interfaces
 * An interface is a pure blueprint for classes.
 * It defines what methods a class must have, but it doesn’t implement them
 * It’s like saying: "Any animal must be able to make a sound", but how that sound is made depends on the specific animal.
 * Unlike extends (inheritance), there's no limit on how many interfaces a class can implement
 * 
 * Serializable is a special interface in the java.io package that enables object serialization
 *  Allows converting objects to byte streams and back:
 * Write to files
 * Send over networks
 * Store in databases
 *  
 * So in our case, serialization allows use to 'physically' store the object and the data that resides within them
 *  and later reconstructed back into an equivalent object with the same statw.
 * https://docs.oracle.com/javase/8/docs/api/java/io/Serializable.html  
 * 
 * 
 * 
 * The functions needed: 
 * Constructor to take in values, 
 * EventCreator: To take in values.
 * FileWriter: write the event into the file as we speak
 * TypeEvent: If the event is recurring, the function will append the event by adding a day/month/year
 * ForTheCalendar: Returns a value if there is an Event on 'i' day. Helps for the PrintCalendar to print the day.
 * 
 * 
 * https://docs.oracle.com/javase/8/docs/api/java/io/ObjectOutputStream.html
 * https://docs.oracle.com/javase/8/docs/api/java/io/ObjectInputStream.html
 * 
 */


 /*
  * WAIT DA
  EVENTS SHOULD REPRESNT INDIVIDUAL EVENTS
  YOUREVENTS SHOULD FUCKING MANAGE THE EVENETS AND A LIST
  OK NA??????????? FUCKING

  */

import java.io.*;
import java.time.LocalDate;
import java.util.*;
class Events implements Serializable{

    // Remember, Dates can be dealt with the LocalDate class.
     private String Title;
     private LocalDate StartDate;
     private LocalDate EndDate;
     private String about; 
     private char Type;

   

    // to enter the values:
    Events(String Title, LocalDate StartDate, LocalDate EndDate, String about, char Type){
        this.Title = Title;
        this.StartDate = StartDate;
        this.EndDate = EndDate;
        this.about = about;
        this.Type=Type;
    }

    // if the end date isnt specified, it is assumed that the event ends on same day.
    Events(String Title, LocalDate StartDate, String about, char Type){
        this.Title = Title;
        this.StartDate = StartDate;
        this.EndDate = EndDate;
        this.about = about;
        this.Type=Type;
    }
  // Quick create.
    Events(String Title, LocalDate StartDate){
        this.Title = Title;
        this.StartDate = StartDate;
        this.EndDate = EndDate;
        this.about = about;
        this.Type=Type;
    }
// Quick Create 2
    Events(String Title, LocalDate StartDate, char Type){
        this.Title = Title;
        this.StartDate = StartDate;
        this.EndDate = EndDate;
        this.about = about;
        this.Type=Type;
    }

 
    // Lets retrieve the events
    public String getTitle() { return Title; }
    public void setTitle(String Title) { this.Title = Title; }
    public LocalDate getStartDate() { return StartDate; }
    public LocalDate getEndDate() { return EndDate; }
    public String getAbout() { return about; }
    public char getType() { return Type; }


}

public class YourEvents implements Serializable{
    private Events[] listOfEvents;
    public  Scanner scn = new Scanner(System.in);
    private Account existingAccount;
    private int eventCount = 0;

    YourEvents(Account existingAccount){
        // Get the events list from the object existingAccount
        this.existingAccount = existingAccount;
    }

    String retrieveDate(){
        System.out.println("Please enter the Day: ");
        String DD = scn.nextLine();
        System.out.println("Please enter the Month: ");
        String MM = scn.nextLine();
        System.out.println("Please enter the Year: ");
        String YYYY = scn.nextLine();
        System.out.printf("%dd-%d-%d", DD, MM, YYYY);
        return DD+"-"+MM+"-"+YYYY;
    }

    public void ShowEvents(){
        // .get(): Returns the element at the specified position in this list.
        for(int i = 0; i<listOfEvents.length; i++){
//            System.out.println("Event "+(i+1)+": "+listOfEvents[i].getTitle());
            System.out.println("Start Date: "+listOfEvents[i].getStartDate());
            System.out.println("End Date: "+listOfEvents[i].getEndDate());
            System.out.println("About: "+listOfEvents[i].getAbout());
            System.out.println("Type: "+listOfEvents[i].getType());
        }
    }
 
    public void AddEvent(){

        System.out.println("Please enter the event title: ");
        String Title = scn.nextLine();
        System.out.println("Please enter the start date: ");
        String StartDate = retrieveDate();
        System.out.println("Please enter the end date: ");
        String EndDate = retrieveDate();
        System.out.println("Please enter the about: ");
        String about = scn.nextLine();
        System.out.println("Please enter the type: R for recurring, S for single, B for Birthday: ");
        char Type = scn.nextLine().charAt(0);
        // Create a new event object
        Events event = new Events(Title, LocalDate.parse(StartDate), LocalDate.parse(EndDate), about, Type);
        // LocalDate.parse: this helps to get the date in the format YYYY-MM-DD
        listOfEvents[eventCount++]=event;
    }

    public void RemoveEvent(){
        ShowEvents();
        System.out.println("Please enter the event number you want to remove: ");
        int i = scn.nextInt();
        if(i<0 || i>eventCount){
            System.out.println("Invalid event number.");
            return;
        }
       RemoveEvent(i);

    }
    public void RemoveEvent(int i){
        listOfEvents[i]=null;
        eventCount--;
        System.out.println("Event removed successfully.");
    }

    public void ModifyEvent(){
        System.out.println("Please enter the event title: ");
        String Title = scn.nextLine();
        for(int i = 0; i<eventCount; i++){
            if(listOfEvents[i].getTitle()== (Title)){
                RemoveEvent(i);
                System.out.println("Please enter the new event title: ");
                String newTitle = scn.nextLine();
                System.out.println("Please enter the new start date: ");
                String newStartDate = retrieveDate();
                System.out.println("Please enter the new end date: ");
                String newEndDate = retrieveDate();
                System.out.println("Please enter the new about: ");
                String newAbout = scn.nextLine();
                System.out.println("Please enter the new type: R for recurring, S for single, B for Birthday: ");   
                char newType = scn.nextLine().charAt(0);
                // Create a new event object
                Events newEvent = new Events(newTitle, LocalDate.parse(newStartDate), LocalDate.parse(newEndDate), newAbout, newType);
                // LocalDate.parse: this helps to get the date in the format YYYY-MM-DD
                System.out.println("Event modified successfully.");
                return;
            }
        }
        System.out.println("Event not found.");
    }
    public void removeEvent(String Title) {

        for (int i = 0; i < eventCount; i++) {
            if (listOfEvents[i].getTitle().equals(Title)) {
                RemoveEvent(i);
                return;
            }
        }

    }

}
