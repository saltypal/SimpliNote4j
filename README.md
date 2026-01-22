# 🌟 SimpliNote4j

Hello there! 👋 This is **SimpliNote4j**, a friendly Java application with two super-cool parts:

1. **Local AI Chatbot** – talk to an AI right on your computer  
2. **SimpliNote Productivity App** – make to-do lists, calendars, timers, and smart notes  

Everything runs on your own machine. No secrets go to the cloud. Let’s dive in!

---

## 📖 Table of Contents

1. [Why SimpliNote4j?](#why-simplinote4j)  
2. [Part 1: Local AI Chatbot](#part-1-local-ai-chatbot)  
3. [Part 2: SimpliNote Productivity App](#part-2-simplinote-productivity-app)  
4. [Screenshots](#screenshots)  
5. [Simple Terms Explained](#simple-terms-explained)  
6. [Getting Started](#getting-started)  
7. [Project Structure](#project-structure)  
8. [Team & Credits](#team--credits)  
9. [License](#license)  

---

##  Why SimpliNote4j?

- **Your data stays with you**.  
  We use **Ollama** for AI on your computer. No external APIs, no surprises.  
- **Lightweight** and **fast**. Works well on average laptops.  

---

##  Part 1: Local AI Chatbot

Our chatbot is like a little robot friend living on your computer. You can:

-  **Chat with AI**  
  Type messages and get smart replies.  
-  **Upload images**  
  Show the bot a picture—ask it questions about what it sees!  
-  **Runs locally** with **Google’s Gemma 3 4B** model  
  A “4B” model means it has 4 billion tiny “neurons.” It’s just the right size for your laptop.  
-  **Adjustable temperature**  
  - **Temperature** controls how creative or predictable the AI is.  
  - Low = safe, repeatable answers.  
  - High = wild, creative answers.  
-  **System prompts**  
  You can set a “system message” to tell the AI how to behave (fun, serious, etc.).  
-  **Chat memory**  
  The AI remembers recent messages so it can keep the conversation going.  
-  **JavaFX UI**  
  A simple, clean window to chat—no complicated menus.  

> Everything runs in `User.db`, our little database file that stores **chat history only**.  
> No user accounts yet—just your friendly chats!

---

## Part 2: SimpliNote Productivity App

This part is a **group project** by Akshita D., Harshil R., Prithvi S., and me (saltypal). It helps you plan your day easily:

-  **Calendar**  
  See your month in a neat grid.  
-  **To-Do List**  
  Add tasks, set priorities (low ➖, medium ⚡, high 🔥), mark them done.  
-  **Pomodoro Timer**  
  Work in sprints (25 minutes), then take a break.  
-  **Smart Note-Taking** with local AI  
  Write notes, ask the AI for summaries or insights. All happens on your computer—no cloud needed!

SimpliNote uses **LangChain4j**, a helper library that makes AI features easier to add in Java desktop apps.

---

## Getting Started

1. **Clone the repo**  
   ```bash
   git clone https://github.com/saltypal/SimpliNote4j.git
   cd SimpliNote4j
   ```

2. **Build with Maven**  
   - Linux/macOS:
     ```bash
     ./mvnw clean package
     ```
   - Windows:
     ```powershell
     mvnw.cmd clean package
     ```

3. **Run the App**  
   ```bash
   java -jar target/SimpliNote4j-1.0.0.jar
   ```

That’s it! You now have your AI chatbot and productivity tools running locally.

---

##  Project Structure

```
SimpliNote4j/
│
├─ .gitignore
├─ pom.xml            # Build instructions for Maven
├─ mvnw, mvnw.cmd     # Maven helper scripts
├─ User.db            # Stores chat history
├─ icon.png, ai.png   # App icons and AI illustrations
│
├─ calendar.css       # Styles for calendar
├─ todo.css           # Styles for to-do list
├─ timer.css          # Styles for timer
├─ main.css           # Core styles
│
├─ src/
│  └─ main/
│     └─ java/        # Java code:
│         ├─ ChatApp.java       # Main chat application
│         ├─ MemoryManager.java # Saves and loads chat memory
│         ├─ AIEngine.java      # Talks to Ollama + Gemma 3 4B
│         ├─ NoteTaker.java     # Smart note functions
│         ├─ CalendarView.java  # Calendar UI
│         ├─ TodoListView.java  # To-do list UI
│         └─ TimerView.java     # Pomodoro timer UI
│
└─ images/             # Extra pictures if needed
```

---

##  Team & Credits

- **saltypal** – Project lead, Java & LangChain4j 
- **Akshita Dindukurthi** – Calendar & UI design  
- **Harshil Reddy** – To-do list & Pomodoro Timer  
- **Prithvi S** – support 

---

## License

This project is licensed under the **MIT License**.  
Started off as a project for OOP course, We aim to make this project a bigger sucess and contribute in productivity tech.
Feel free to use, modify, and share!

---

Thank you for checking out **SimpliNote4j**! 🎉  
Enjoy your private, local AI assistant and productivity suite!  
