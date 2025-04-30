package com.simplinote.simplinote.features;

                    import javafx.fxml.FXMLLoader;
                    import javafx.scene.Parent;
                    import javafx.scene.Scene;
                    import javafx.stage.Stage;

                    public class AiChatFeature {
                        public void show() {
                            try {
                                Stage chatStage = new Stage();
                                chatStage.setTitle("SuperPie AI Chat");

                                // Use absolute path from classpath root
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/simplinote/simplinote/betterSuperPieMainChat.fxml"));
                                Parent root = loader.load();
                                Scene scene = new Scene(root);

                                chatStage.setScene(scene);
                                chatStage.show();
                            } catch (Exception e) {
                                System.err.println("Error loading AI chat: " + e.getMessage());
                                e.printStackTrace();
                            }
                        }
                    }