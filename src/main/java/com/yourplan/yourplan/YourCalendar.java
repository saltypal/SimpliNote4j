package com.yourplan.yourplan;

import java.time.*;
import java.util.Scanner;


public class YourCalendar{
    private LocalDate local; //LocalDate is a class that represents a date without a time zone in the ISO-8601 calendar system, such as 2007-12-03. https://docs.oracle.com/javase/8/docs/api/java/time/LocalDate.html
    
    // instantiate an object for my EVENTS class.

    // The entire project is Object dependent. 

    /*
     * Functions of the calendar:
     * 1. Print the days.
     * 2. If the day is today, represent with > <
     * 3. If there is any even on that day, represent with *, for repetititve, represent with * *
     * 4. Allow The user to check out different months, years, Go to specific date.

 The Package used: java.time.*
      * The classes to be used: 
      LocalDate: getYear(), getMonth(), lengthofMonth()
      Create an instance of the class LocalDate by LocalDate.now() 
      withDayOfMonth(....)
    Navigate between months using plus and minus. The current month is used as a reference point.

       * Functions to be implemented:
       * 1. Display Current month.
       * 2. Navigate between months
       * 3. Navigate between years.
       * 4. Go to specific Date. 

         Implementation of Month print.
        * 1. the first of the month.
          2. number of days in the month

FACTS ABOUT THE JAVA.TIME
 * Factory Method Pattern

Uses static factory methods (of(), now(), parse()) instead of public constructors
Constructors are private - you can't use new LocalDate()
Gives more flexibility in object creation

Immutable Value Type

All objects are immutable (cannot be changed after creation)
All methods that would "modify" the date return new objects instead
This is why you must write local = local.plusMonths(1) rather than just local.plusMonths(1)

Methods return new LocalDate instances, enabling method chaining

*
 * We will be using for loop to print values.
 * Now, what day should the month start from? that is decided by the First of the month function. This function will initialise the value of start to the number which shows the day to start with.
 * So first till the value of i, it will print blank spaces.
 * What to do if it reaches end of week i.e, it reaches sunday? for this we will be using the same logic that circular loop uses. 
 *         if i == a multiple of 6(Why 6? because Sunday is 6) then next 
 */

    // LOCALDATE  WHEN USED GIVES AN OBJECT. THAT OBJECT HAS METHODS WHICH CAN BE USED.

   int FirstOftheMonth(){ // this function returns the value of start for which the equivalent week starts
       
    LocalDate firstOfMonth = local.withDayOfMonth(1);
  
    return firstOfMonth.getDayOfWeek().getValue(); // 1 = Monday, 7 = Sunday
    
    }

    void presentMonth(){
        local = LocalDate.now(); 
        PrintCalendar();
    }

    void nextMonth(){
        local = local.plusMonths(1);
        PrintCalendar(); 
    }

    void prevMonth(){
        local = local.minusMonths(1);
        PrintCalendar();
    }
      
    void goToMonth(int month) {
        local = LocalDate.of(local.getYear(), month, 1);
        PrintCalendar();
    }

    void goToYear(int year) {
        local = LocalDate.of(year, local.getMonth(), 1);
        PrintCalendar();
    }
    void goToMonthAndYear(int year, int month) {
        local = LocalDate.of(year, month, 1);
        PrintCalendar();
    }

    void PrintCalendar(){
        // initializing to the current month and year.
        int N = local.lengthOfMonth();
        System.out.println("\t Year: "+local.getYear() +" Month :"+ local.getMonth());
        System.out.println("Mon Tue Wed Thu Fri Sat Sun ");
        //                       0     1     2     3     4     5     6
         int start = FirstOftheMonth();
         int today = local.getDayOfMonth();

        if(start!=1){
             for(int i=1; i<start;i++ ){
            System.out.print(" -- ");
            }
        }
        int day  = 1;
        for (int i = start; i <= N+start-1; i++) {
            if(i%7==1){ // if the calendar reaches 7, it will go to next line
                System.out.print("\n");
            }     
                if(day<10 && day == today){
                    System.out.printf("  @%d", day);
                }else if(day<10){
                        System.out.printf("   %d", day);
                } else if(day>10 && day == today){
                    System.out.printf(" @%d", day);
                }else{
                    System.out.printf("  %d", day);
                }
                day++;
        }
        System.out.println("\n");
    
    }
     
    
     // We always need to return the value of the month viewed, year viewed
     
    

}


