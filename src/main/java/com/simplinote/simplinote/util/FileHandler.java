package com.simplinote.simplinote.util;

                        import javafx.stage.FileChooser;
                        import javafx.stage.Window;
                        import java.io.*;
                        import java.nio.file.*;

                        public class FileHandler {
                            private FileChooser fileChooser;

                            public FileHandler() {
                                fileChooser = new FileChooser();
                                fileChooser.setTitle("Select File Location");
                                fileChooser.getExtensionFilters().add(
                                    new FileChooser.ExtensionFilter("Text Files", "*.txt")
                                );

                                // Set initial directory to user's documents folder
                                String userHome = System.getProperty("user.home");
                                fileChooser.setInitialDirectory(new File(userHome + "/Documents"));
                            }

                            public void saveToFile(String content, Window window) {
                                File file = fileChooser.showSaveDialog(window);
                                if (file != null) {
                                    try {
                                        Files.write(file.toPath(), content.getBytes());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                            public String loadFromFile(Window window) {
                                File file = fileChooser.showOpenDialog(window);
                                if (file != null) {
                                    try {
                                        return new String(Files.readAllBytes(file.toPath()));
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                                return null;
                            }
                        }