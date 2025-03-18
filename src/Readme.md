# YourPlan
This is a project to make a planner and a calendar application that is simple and easy to use.

I know what it feels like to not be productive all because you couldn't plan it right.
All that changes now.
YourPlan is an app designed to help you plan, make to-do lists and make sure you actually complete your tasks by having a checklist.

This application has the ability to sync your data with your outlook or your gmail calender too.

The application has things like
- Creating new tasks and categorizing.
- Giving different levels of priorities and sorting them depending on their due dates or any user defined parameter.
- TimeTrack: A timer to keep track of how long are you productive or how long did you waste time.
- Diary Mode: Before Bedtime Diary.
- Voice to Text plan maker (LLM WRAPPING): You want to make plans from a voice memo that has what you planned for? yeah you can do that too.


https://stackoverflow.com/questions/49531447/from-command-line-to-gui

Since Java Applets are outdated (deprecated in Java 9 and removed in Java 11), I recommend using Swing or JavaFX instead.

Swing (Good for lightweight GUI applications)
JavaFX (Modern and feature-rich)

Convert CLI to GUI
Since your program already works in CLI, you mainly need to:

Replace console inputs/outputs (System.out.println, Scanner) with GUI elements like buttons, labels, and text fields.


ok i got a crazy new idea. I will create on java file whihc maintains accounts.
for each account it is one object.
that one object holds data about the user and his calendar events.
so i will pass that object through YourEvents and then Put in new info/ remove info/ modify info

This object not only holds my calendar shit, but also my to do list shit which i will be making in the future

you can store Java objects in SQL databases, but you typically need to serialize the object into a format like JSON or a byte array (BLOB) and store it in a database column, then deserialize it back into a Java object when retrievin



Below is an example of a detailed README explanation covering the design and functionality implemented in your three files:

---
---

# Project Overview

This project is a personal calendar and account management system. It has been designed around the idea that each user account is managed as a single object that holds both calendar events and (in the future) a to‐do list. The system uses Java object serialization to persist data, and the core modules include:

- **YourAccount.java** – Manages user account data, including login, account creation, and storing user-specific information (such as calendar events).
- **YourEvents.java** – Defines the events model used within each account; every event is represented as an object that contains details like title, start and end dates, description, and type.
- **MyCalendar.java** – Implements the calendar display logic, printing the current month with proper formatting; this module can eventually be tied to the account’s event data.

---

# Detailed Explanation

## 1. YourAccount.java

**Purpose:**  
This file handles the management of user account data. It is designed to support a login interface and account persistence through serialization.

**Key Points:**
- **User-Centric Design:**  
  Each user is represented as an object. This account object is intended to hold data relating to the user’s profile, calendar events, and (in the future) to-do items.

- **Persistence Strategy:**  
  The account object is serialized to a file (or could be stored as JSON/BLOB in a database) so that when a user logs in, the corresponding account data is loaded and passed to other parts of the system.

- **Workflow Functions:**
    - **New Account Creation:** When a new user is registered, a new account object is created and stored in an array (or a list) of accounts.
    - **Existing Account Load:** On login, the system checks if the account exists; if it does, the account file is loaded and its object is passed to other modules (like YourEvents and MyCalendar) for further operations.

- **Comments in the File:**  
  The comments explain the design logic behind using a single object per account and the future plan to include both calendar events and to-do items in that object.

## 2. YourEvents.java

**Purpose:**  
This file defines the `YourEvents` class that manages event data. Each event is represented as a separate object.

**Key Points:**
- **Serializable Event Objects:**  
  The class implements `Serializable` so that event objects can be written to and loaded from files. This mechanism “physically” stores the object’s state (event details like Title, StartDate, EndDate, description, and Type).

- **Multiple Constructors:**  
  The file includes several constructors to create events under different circumstances:
    - A full constructor where all fields are provided (Title, StartDate, EndDate, about, and Type).
    - Shorter versions assume defaults (for instance, if the event ends on the same day, the EndDate is assumed to be the StartDate).

- **Getter Methods:**  
  The class provides getters for each field to allow retrieval of event details. This is essential for when the events are displayed on the calendar or used in other modules.

- **File Writing Intent:**  
  Although the file writing methods aren’t fully implemented in the snippet, comments indicate that the intended strategy is to write events in CSV format (or via serialization). There’s also a planned function “ForTheCalendar,” which implies that a method will load only the event dates (possibly into an array) to quickly check whether an event occurs on a given day (using binary search).

- **Flexibility for Future Changes:**  
  The comments suggest exploring two approaches:
    - Writing each event individually as objects.
    - Using a single serialized object to maintain updates (create, delete, modify) across the entire account’s event list.

## 3. MyCalendar.java

**Purpose:**  
This file manages the display of the calendar. It is responsible for rendering a monthly calendar view that will eventually integrate with event data from the user account.

**Key Points:**
- **Calendar Logic:**  
  The `zCalendar` class encapsulates the logic to display the current month:
    - It uses Java’s `LocalDate` to determine the current month and year.
    - It calculates the number of days in the month using `lengthOfMonth()`.
    - The start of the month (first day) is determined by adjusting the date to the first of the month and computing the day-of-week value for layout.

- **Display Formatting:**  
  The printing methods output the days of the week and then iterate over the days of the month:
    - It introduces spaces before the first day to align the calendar correctly.
    - The formatting uses `printf` to ensure that single-digit and double-digit dates are aligned correctly.

- **Integration with Events (Future Work):**  
  Although the current snippet does not tie calendar display logic with event data, future plans include checking the stored events in a user account (via `YourEvents`) and highlighting or annotating days with events.

---

# Summary

- **User Account Management:**
    - **YourAccount.java** centralizes user management, login, account creation, and persistence. It will serve as the primary model holding user-specific data (calendar events and later to-do items).

- **Event Handling:**
    - **YourEvents.java** defines event objects that are created, modified, and removed as needed. It handles serialization to persist the events file and outlines a CSV-based approach as an alternative.

- **Calendar Display:**
    - **MyCalendar.java** implements the logic to display a monthly calendar using Java’s date APIs. It will eventually interface with the event data stored in the user's account.

This modular, object-based design is a solid foundation for your application. It works well for small- to medium-scale projects and allows easy integration with databases or scalable storage solutions in the future.

---

You can include this detailed explanation in your README file to document the structure, design decisions, and workflow of your project.