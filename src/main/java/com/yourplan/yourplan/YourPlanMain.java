package com.yourplan.yourplan;

import java.util.Scanner;

public class YourPlanMain{
    public static void main(String[] args){
        Scanner scn = new Scanner(System.in);
        // login interface
        System.out.println("Welcome to YourPlan");
        YourAccount YA = new YourAccount();
        Account YourAccountObject = YA.AccountLogin(); // AccountLogin Returns an Object of your current account. Which has to be passed to events to use.
        YourEvents Eventer = new YourEvents(YourAccountObject);

        /*
        Role of Events:
        1. Create Event
        2. View Events
        3. Remove Events
        4. List Events For the calendar to display.
         */

        // CURRENT MONTH IS ALWAYS PRINTED FIRST REGARDLESS OF WHAT ACTION THE USER WANTS TO PERFORM.
        YourCalendar cal = new YourCalendar();
        cal.presentMonth();
        boolean run = true;
        /*Always print current month first.
         * then ask for a change:
         * 1. add/remove event
         * 2. go to different month
         * 3. go to different year*/
        while(run){
            System.out.println("What do you want to perform: "+
                    "1. Next Month   "+
                    "2. Previous Month   "+
                    "3. Specific Month   "+
                    "4. Specific Year   "+
                    "5. Specific Year and Month   "+
                    "EVENTS: "+
                    "6. Create Event"+
                    "7. View Events"+
                    "8. Remove Events"+
                    "9. List "+
                    "0. EXIT");
            int choice = scn.nextInt();
            switch(choice){
                case 0: run = false;
                case 1: {
                    cal.nextMonth();
                    break;
                }
                case 2: {
                    cal.prevMonth();
                    break;
                }
                case 3: {
                    System.out.println("Please enter the month you want to go:" +
                            "Jan Feb Mar Apr May Jun Jul Aug Sep OCt Nov Dec");
                    int lclchoice = scn.nextInt();
                    cal.goToMonth(lclchoice);
                    break;
                }
                case 4: {
                    System.out.println("Please enter the Year you want to go:" );
                    int lclchoice = scn.nextInt();
                    cal.goToYear(lclchoice);
                    break;
                }
                case 5:{System.out.println("Please enter the month you want to go:" +
                        "Jan Feb Mar Apr May Jun Jul Aug Sep OCt Nov Dec");
                    int lclchoice = scn.nextInt();
                    System.out.println("Please enter the Year you want to go:" );
                    int lclchoice2 = scn.nextInt();
                    cal.goToMonthAndYear(lclchoice2, lclchoice);
                    break;

                }
                case 6:{
                    System.out.println("Creating/Adding Events: ");
                    // take input using printf. see how the format of LocalDate date input looks like.
                    //YYYY MM DD
                    System.out.println("Please enter event details: ");

                }
                case 7:{System.out.println("Showing Events: ");
                    Eventer.ShowEvents();
                    break;}
                case 8:{
                    System.out.println("Removing Event: ");
                    Eventer.RemoveEvent();
                    break;
                }

            }



        }
    }
}
