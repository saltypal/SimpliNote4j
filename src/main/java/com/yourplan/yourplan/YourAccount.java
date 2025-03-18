package com.yourplan.yourplan;/*
 * This file is completely related to management of the user file. 
 * OpenOption	
An object that configures how to open or create a file.

The program manages a login interface that deals with account. 
if account exists, it loads the file.
it passes that file to the different programs and makes them use that file as source to read and make changes.


ok i got a crazy new idea. I will create on java file whihc maintains accounts. 
for each account it is one object. 
that one object holds data about the user and his calendar events.
so i will pass that object through YourEvents and then Put in new info/ remove info/ modify info

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
    private List<YourEvents> events;

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
    List<YourEvents> getList(){return events;}
    
 }

public class YourAccount implements Serializable{
  private ArrayList<Account> AccountList;
    Scanner scn = new Scanner(System.in);
    YourAccount(){
      Scanner Scn = new Scanner(System.in); // Initialize Scanner
        AccountList = new ArrayList<>(); // Load the accounts list
    }


// this is to load the accounts list.
//    private ArrayList<Account> loadAccounts() {
//        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(SAVE_FILE))) {
//            return (ArrayList<Account>) ois.readObject();
//        } catch (FileNotFoundException e) {
//            System.out.println("No accounts file found. Creating new accounts list.");
//            return new ArrayList<>();
//        } catch (IOException | ClassNotFoundException e) {
//            System.out.println("Error loading accounts: " + e.getMessage());
//            return new ArrayList<>();
//        }
//    }

    public Account AccountLogin(){
        boolean run = true;
        while(run)
        System.out.println("Please enter your username: ");
        String inputName = scn.nextLine();
        /*
        Circumstances under which i need to return an error:
        1. Account doesn't exist
        2. Account list is itself empty.


        AccountList.stream(): Converts the AccountList to a stream.
        .anyMatch(account -> ...): Checks if any element in the stream matches the given predicate.
        account -> account.getUsername().equals(inputName): The predicate checks if the username of the account is equal to the inputName.
         */
        if (AccountList.isEmpty()){

            System.out.printf("No accounts found. Create new account?");
            CreateAccount();
            System.out.println("Account Creation successful. Going to main menu.");
        } else if(AccountList.stream().anyMatch(account -> account.getUsername()!=(inputName))) {

            System.out.printf("No accounts found. Create new account?");
            CreateAccount();
            System.out.println("Account Creation successful. Going to main menu.");
        } else {
            Account existingAccount = AccountList.stream().filter(account -> account.getUsername().equals(inputName)).findFirst().get();
            System.out.println("Please enter your password: ");
            String inputPWD = scn.nextLine();
            if (existingAccount.getPWD().equals(inputPWD)) {
                System.out.println("Login successful.");
                run = false;
                 return existingAccount;
            } else {
                System.out.println("Incorrect password.");
                System.out.println("Going to main menu.");
                return null;
            }
        }
        return null;
    }

   public void CreateAccount(){
        System.out.println("Create New Account: ");
        System.out.println("Please enter your username: ");
        String inputName = scn.nextLine();
        System.out.println("Please enter your password: ");
        String inputPWD = scn.nextLine();
        Account newAccount = new Account(inputName, inputName, inputPWD);
        AccountList.add(newAccount);
        System.out.println("Account Creation successful. Going to main menu.");
        AccountLogin();;
    }

    void RemoveAccount(){
        System.out.println("Please enter the username of the account you want to remove: ");
        String inputName = scn.nextLine();
        if (AccountList.stream().anyMatch(account -> account.getUsername().equals(inputName))) {
            System.out.println("Enter the password of the account you want to remove: ");
            String inputPWD = scn.nextLine();
            if (AccountList.stream().anyMatch(account -> account.getPWD().equals(inputPWD))) {
                    AccountList.remove(AccountList.stream().anyMatch(account -> account.getPWD().equals(inputPWD)));
                    System.out.println("Account removed successfully.");
                }else{
                    System.out.println("Incorrect password. Try again.");
                }
        } else {
            System.out.println("Account not found.");
        }
    }

    ArrayList<Account> GetList(){
        return AccountList; // this is to return the list of accounts in the entire machine.

    }
    

}