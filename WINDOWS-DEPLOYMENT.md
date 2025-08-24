# Windows Deployment Guide for SimpliNote4j

This guide explains how to create and deploy SimpliNote4j as a Windows application.

## Prerequisites

- Java 17 or later installed
- Maven 3.6+ installed
- Windows 10/11 (for creating Windows installers)

## Building the Application

### 1. Standard JAR Build
```bash
./mvnw clean package
```
This creates a fat JAR with all dependencies at `target/SimpliNote4j-1.0.0.jar`

### 2. Quick Windows Deployment

**For End Users:**
1. Copy `target/SimpliNote4j-1.0.0.jar` to a folder on Windows
2. Copy `run-windows.bat` to the same folder
3. Double-click `run-windows.bat` to launch the application

**System Requirements:**
- Java 17+ must be installed and in PATH
- JavaFX modules are included in the JAR

### 3. Creating Windows Installer (Advanced)

If you have a Windows development environment with jpackage available:

```bash
# First build the application
./mvnw clean package

# Create Windows installer (requires Windows environment)
./mvnw jpackage:jpackage
```

This will create:
- A Windows `.exe` installer in `target/installer/`
- The installer includes a bundled JRE (no Java installation required for end users)
- Start menu shortcuts and desktop shortcuts
- Professional Windows application appearance

### 4. Manual Windows Installer Creation

If the jpackage plugin doesn't work in your environment, you can use the system jpackage tool:

```bash
# Ensure you have JDK 17+ with jpackage
jpackage --version

# Create the installer
jpackage \
  --input target \
  --name SimpliNote4j \
  --main-jar SimpliNote4j-1.0.0.jar \
  --main-class com.simplinote.simplinote.Main \
  --type exe \
  --dest target/installer \
  --app-version 1.0.0 \
  --vendor "SimpliNote Team" \
  --description "AI-powered productivity application" \
  --win-dir-chooser \
  --win-menu \
  --win-shortcut \
  --icon icon.png
```

## Deployment Methods

### Method 1: Simple JAR Distribution
- **Pros**: Works on any system with Java 17+, small download
- **Cons**: Users need Java installed
- **Best for**: Technical users, development testing

### Method 2: Windows Installer (.exe)
- **Pros**: Professional deployment, includes JRE, easy for end users
- **Cons**: Larger file size (~200-300MB), Windows-only
- **Best for**: End user distribution

## File Structure for Distribution

```
SimpliNote4j-Windows/
├── SimpliNote4j-1.0.0.jar     # Main application
├── run-windows.bat             # Windows launcher script
├── README-Windows.txt          # User instructions
├── icon.png                    # Application icon
└── LICENSE                     # License file
```

## Troubleshooting

**"Java not found" error:**
- Install Java 17+ from https://adoptium.net/
- Ensure Java is in system PATH

**JavaFX errors:**
- The JAR includes JavaFX dependencies
- If issues persist, use: `java --module-path . --add-modules javafx.controls,javafx.fxml,javafx.web,javafx.media,javafx.swing -jar SimpliNote4j-1.0.0.jar`

**Module system errors:**
- Try running with: `java --add-opens java.base/java.lang=ALL-UNNAMED -jar SimpliNote4j-1.0.0.jar`

## Features Included

The Windows deployment includes all SimpliNote4j features:
- AI Chat powered by Ollama (requires Ollama installed separately)
- Calendar and scheduling
- To-do list management  
- Pomodoro timer
- Smart note-taking
- File import/export capabilities