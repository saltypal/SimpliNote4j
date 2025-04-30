package com.simplinote.simplinote.superpie;


import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.UserMessage;

import java.io.*;
import java.util.Map;
import static dev.langchain4j.data.message.ChatMessageDeserializer.messagesFromJson;
import static dev.langchain4j.data.message.ChatMessageSerializer.messagesToJson;

import org.apache.pdfbox.pdmodel.PDDocument;

import org.mapdb.DB;
import org.mapdb.DBMaker;
import static org.mapdb.Serializer.STRING;
import java.util.*;
import dev.langchain4j.data.message.*;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.request.ResponseFormat;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.ollama.OllamaChatModel;


public class Backend {
    public static Scanner scn = new Scanner(System.in);
    private ChatLanguageModel model;
    public String ModelName;
    private ResponseFormat ResponseFormat;
    public double temperature;
    private ChatMemory Memory;
    public Object CurrentChat;
//    private FileWriter PlainChatHistory;


    void newInit() throws IOException {
        this.ModelName = "gemma3:4b";
        this.temperature = 0.7;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        LocalDateTime now = LocalDateTime.now();
        String newChatName = "chat_" + dtf.format(now); // ex: chat_20250426_142501.txt

        ProcessBuilder processBuilder = new ProcessBuilder("ollama", "run", ModelName);

        this.CurrentChat = newChatName;

//        // Close old FileWriter if open (optional safety)
//        if (PlainChatHistory != null) {
//            PlainChatHistory.close();
//        }

        // Create a new FileWriter for the new chat
//        PlainChatHistory = new FileWriter(String.valueOf(CurrentChat));

        // (Optional) Reset memory or other session data
        memory_Init();
        VogueModel();
    }

    void oldInit(Object C) throws IOException {
        this.CurrentChat = C;
        this.ModelName = "gemma3:1b";
        this.temperature = 0.7;
        ProcessBuilder processBuilder = new ProcessBuilder("ollama", "run", ModelName);
//        PlainChatHistory = new FileWriter(CurrentChat.toString());
        memory_Init();
        VogueModel();
    }

    void memory_Init() {

        Memory = MessageWindowChatMemory.builder().id(CurrentChat).chatMemoryStore(new SuperPiePersistentMemoryStore()).maxMessages(10).build();

        Memory.add(SystemMessage.from("Your name is SuperPie. A chatbot designed using Ollama inferencing, via langchain4j on javafx. The LLM being used by you is " + ModelName + ". You are supposed to be the User's BestFriend and a VERY Optimistic person. You are supposed to Extremely helpful."));
    }


    void quit() {
//        SaveStatus();
        SuperPiePersistentMemoryStore.exit();
        ProcessBuilder processBuilder = new ProcessBuilder("ollama", "stop", ModelName);

    }


    ChatLanguageModel VogueModel() {
        this.model = OllamaChatModel.builder().modelName(ModelName).responseFormat(null).baseUrl("http://localhost:11434").temperature(temperature).build();
        return model;
    }

    ChatResponse YourMessage(UserMessage U) throws IOException {
        Memory.add(U);
//        PlainChatHistory.append("user: " + U.toString() + "\n");
        ChatRequest request = ChatRequest.builder().messages(Memory.messages()).build();
        return model.chat(request);
    }

    String BotReply(ChatResponse C) throws IOException {
        AiMessage aiMessage = C.aiMessage();
        Memory.add(aiMessage);
        String x = aiMessage.text();
//        PlainChatHistory.append("superpie: " + x + "\n");
        return x;
    }

    boolean image = false;

    UserMessage ReturnUserMessage(String S, boolean image, boolean pdf) {
        if (!image) {
            UserMessage X = UserMessage.from(
                    TextContent.from(S)
            );
            return X;
        } else {
            UserMessage X = UserMessage.from(
                    TextContent.from(S),
                    IGC
            );
            return X;
        }
    }


    public ImageContent IGC;

    ImageContent returnImageUserMessage(String Address, String ext) {
        try {
            Path path = Paths.get(Address);
            byte[] imageBytes = Files.readAllBytes(path);
            String base64Data = Base64.getEncoder().encodeToString(imageBytes);
            this.IGC = ImageContent.from(base64Data, "image/" + ext);
            return this.IGC;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

//    public PdfFileContent PFC;
//
//    PdfFileContent returnPDFUserMessage(String Address, String ext) {
//        try {
//
//            byte[] pdfBytes = Files.readAllBytes(Paths.get(Address); // This reads the file's binary data
//            PdfFileContent pdfContent = PdfFileContent.from(pdfBytes);
//            return this.PFC;
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }

    public String ModelTalking(String X, boolean img, boolean pdf) throws IOException {
        String S = BotReply(YourMessage(ReturnUserMessage(X, img, pdf)));
        return S;
    }


    class SuperPiePersistentMemoryStore implements ChatMemoryStore {


        private static final DB db;

        static {
            DBMaker.Maker maker = DBMaker.fileDB("User.db");
            maker.transactionEnable();
            db = maker.make();
        }

        private static final Map<String, String> map = db.hashMap("messages", STRING, STRING).createOrOpen();

        @Override
        public List<ChatMessage> getMessages(Object memoryId) {
            String json = map.get((String) memoryId);
            return messagesFromJson(json);
        }

        @Override
        public void updateMessages(Object memoryId, List<ChatMessage> messages) {
            String json = messagesToJson(messages);
            map.put((String) memoryId, json);
            db.commit();
        }

        @Override
        public void deleteMessages(Object memoryId) {
            map.remove((String) memoryId);
            db.commit();
        }

        public static void exit() {
            db.close();
        }

        public static Set<String> displayList() {
            // Return a copy of the key set to avoid potential concurrent modification issues
            return new HashSet<>(map.keySet());
        }

    }


}
