package com.simplinote.simplinote;

import dev.langchain4j.agent.tool.ToolExecutionRequest;
//import dev.langchain4j.agent.tool.ToolExecutionResultMessage;
import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.agent.tool.ToolSpecifications;
import dev.langchain4j.model.chat.*;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.ollama.OllamaChatModel;

import java.util.List;
import java.util.Map;
import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.agent.tool.P;
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
import dev.langchain4j.agent.tool.Tool;
//import dev.langchain4j.agent.tool.ToolParameter.P;
import dev.langchain4j.agent.tool.ToolSpecification;


 class MathTools {

    @Tool("Adds two numbers and returns the result")
    public int sum(@P("First number") int a, @P("Second number") int b) {
        return a + b;
    }
}


public class ToolTry {

    public static void main(String[] args) {

        // ✅ Use local model from Ollama (make sure it's running)
        ChatLanguageModel model = OllamaChatModel.builder()
                .baseUrl("http://localhost:11434")
                .modelName("gemma3:4b") // or "gemma", "llama2", etc.
                .build();

        // ✅ Load tools
        MathTools mathTools = new MathTools();
        List<ToolSpecification> toolSpecs = ToolSpecifications.toolSpecificationsFrom(MathTools.class);

        // ✅ Initial user input
        UserMessage userMessage = UserMessage.from("Can you add 42 and 27 for me?");

        // ✅ First model call
        ChatRequest request = ChatRequest.builder()
                .messages(List.of(userMessage))
                .toolSpecifications(toolSpecs)
                .build();

        ChatResponse response = model.chat(request);
        AiMessage aiMessage = response.aiMessage();

        // ✅ Check if the model asked to run a tool
        if (aiMessage.hasToolExecutionRequests()) {
            for (ToolExecutionRequest toolRequest : aiMessage.toolExecutionRequests()) {
                System.out.println("🛠️ Tool requested: " + toolRequest.name());
                System.out.println("🧾 Arguments: " + toolRequest.arguments());



                // ✅ Manually run the tool
                int result = mathTools.sum(1, 2);
                System.out.println("✅ Tool result: " + result);

                // ✅ Send result back to the model
                ToolExecutionResultMessage resultMessage =
                        ToolExecutionResultMessage.from(toolRequest, String.valueOf(result));

                ChatRequest followUp = ChatRequest.builder()
                        .messages(List.of(userMessage, aiMessage, resultMessage))
                        .toolSpecifications(toolSpecs)
                        .build();

                ChatResponse finalResponse = model.chat(followUp);
                System.out.println("🧠 Final Answer: " + finalResponse.aiMessage().text());
            }
        } else {
            // Fallback if no tool is used
            System.out.println("🧠 Direct LLM Answer: " + aiMessage.text());
        }
    }
}
