package com.yourplan.yourplan;/*
 * This file is completely related to management of the user file. 
 * OpenOption	
/////////////////////////////////////////////////////////////////////////////////////////////////////////
IN THIS BRANCH, WE WILL BE FOCUSING ON USING ARRAY INSTEAD OF ARRAY LIST.
EVERY ACCOUTN CAN HOLD 1000 EVENTS. AND THERE ARE 10 ACCOUNTS.
/////////////////////////////////////////////////////////////////////////////////////////////////////////
 *
The program manages a login interface that deals with account. 
if account exists, it loads the file.
it passes that file to the different programs and makes them use that file as source to read and make changes.

////////////////////////////////////////////////////////////////////////////////////////////////////////
THE DATA WILL BE WRITTEN INTO A FILE. EACH FILE WILL HAVE THE NAME OF THE ACCOUNT. AND ONE FILE WILL HAVE THE LIST OF ACCOUNTS.
ONE FILE PER ACCOUNT.
////////////////////////////////////////////////////////////////////////////////////////////////////////


This object not only holds my calendar shit, but also my to do list shit which i will be making in the future

 you can store Java objects in SQL databases,
  but you typically need to serialize the object into a format like JSON or a byte array (BLOB) and store it in a database column, then deserialize it back into a Java object when retrievin

  FUnctions: 
  1. Login 
  new account - new object-> save it, save it to the account list array
  old account - check if account exists wrt account list array
if doesnt exist go to new account. if account exists then return the account's object name to the required java files.

We will be using ArrayList to hold.
In Java, you can store objects of a specific class in an ArrayList using generics. Since a class itself is a data type in Java, 
you can create an ArrayList that stores only objects of that class.
So in our case, the class is YourAccount.
  */

/*
 Idea is that, since You can store a class as a datatype right, so i can store my account details as a class.
 MY account Details class, i can include another class datatype that stores my event details.
 and my accountdetails object i can store in my ArrayList object. Isnt this wild?
 */


 /*
  So.
  An object of YourAccount Holds your account data.
  In YourAccount, we have your username, password, Your events and your tasks
  Now YourEvents is another class which has classes that holds information about your Events.
  The YourCalendar class will access your YourAccount Object with your password and then use the elements inside.
  

  */


/*
https://www.w3resource.com/java-exercises/oop/java-oop-exercise-7.php#:~:text=Also%20define%20a%20class%20called,details%20of%20a%20particular%20customer.&text=The%20above%20class%20has%20three,print%20account%20details%20and%20more.

 */
import org.controlsfx.control.tableview2.filter.filtereditor.SouthFilter;

import java.io.*;
import java.util.*;


class Account{
    // The class will hold the details of the account
    private String ObjName;
    private String Username;
    private String PWD;
    // private boolean exist;
    private Events[] events = new Events[1000]; // This is to hold the events of the account. The maximum number of events that can be held is 1000.

        Account(String ObjName, String Username, String PWD){
            this.ObjName = Username;
            this.Username = Username;
            this.PWD = PWD;
            // No events because when account is created then there is no events.

        }
        Account(){

        }

        //Getters and Setters
        String getObjName(){return ObjName;}
        String getUsername(){return Username;}
        String getPWD(){return PWD;}
        Events[] getList(){return events;}

     }

    public class YourAccount implements Serializable{
      private Account[] AccountList;
      private  int accountCount = 0;
        Scanner scn = new Scanner(System.in);
        YourAccount(){
          Scanner Scn = new Scanner(System.in); // Initialize Scanner
            AccountList = new Account[10]; // Load the accounts list
        }




    public Account[] loadAccounts(Account[] AccountList) {
        System.out.println("Loading accounts...");
        System.out.printf("ONLY DUMMY FUNCTION FOR NOW.");
        AccountList[10] = AccountList[10]; // this is to load the accounts list into the variable.
        return AccountList; // the fucntion can also return an accounts list.
    }

    public Account AccountLogin() {
        loadAccounts(AccountList);
        boolean run = true;
        while (run) {
            System.out.println("Please enter your username: ");
            String inputName = scn.nextLine();
            /*
            Circumstances under which i need to return an error:
            1. Account doesn't exist
            2. Account list is itself empty.
             */


            if (AccountList[0] == null) {
                System.out.printf("No accounts found. Create new account?");
                CreateAccount();
                System.out.println("Account Creation successful. Going to main menu.");
            }
            Account found = null;
            for (int i = 0; i < accountCount; i++) {
                if (AccountList[i] != null && AccountList[i].getUsername().equals(inputName)) {
                    found = AccountList[i];
                    break;
                }
            }
            if (found == null) {
                System.out.println("Account not found. Create new account?");
                CreateAccount();
                System.out.println("Account Creation successful. Going to main menu.");
            } else {
                System.out.println("Please enter your password: ");
                String inputPWD = scn.nextLine();
                if (found.getPWD().equals(inputPWD)) {
                    System.out.println("Login successful.");
                    return found;
                } else {
                    System.out.println("Incorrect password.");
                }
                return null;
            }
            return null;
        }
        return null;
    }

   public void CreateAccount(){
        if(accountCount == 10){
            System.out.println("Maximum number of accounts reached. Remove an account to create a new one.");
        }
        System.out.println("Create New Account: ");
        System.out.println("Please enter your username: ");
        String inputName = scn.nextLine();
        System.out.println("Please enter your password: ");
        String inputPWD = scn.nextLine();
        Account newAccount = new Account(inputName, inputName, inputPWD);
        AccountList[accountCount++]= newAccount;
        System.out.println("Account Creation successful. Going to main menu.");
        AccountLogin();;
    }

    void RemoveAccount(){
        // binary search to find the account
        System.out.println("Please enter the username of the account you want to remove: ");
        String inputName = scn.nextLine();
        for (int i = 0; i < AccountList.length; i++) {
            if(AccountList[i].getUsername().equals(inputName)) {
                System.out.println("please enter the password of the account you want to remove: ");
                String inputPWD = scn.nextLine();
                boolean run1 = true;
                while (run1) {
                    if (AccountList[i].getPWD().equals(inputPWD)) {
                        AccountList[i] = null;
                        System.out.println("Account removed.");
                        accountCount--;
                        run1 = false;

                    } else {
                        System.out.println("Incorrect password.");

                    }
                }
            }
        }
    }

        public void GetList() {
            for (int i = 0; i < AccountList.length; i++) {
                if(AccountList[i] != null){
                    System.out.println("Account Number: "+i+1);
                    System.out.println(AccountList[i].getUsername());
                }
            }
        }

    

}