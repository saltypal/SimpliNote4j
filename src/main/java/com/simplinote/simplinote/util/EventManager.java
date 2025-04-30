package com.simplinote.simplinote.util;

import com.simplinote.simplinote.model.Events;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class EventManager {
    private static final String EVENT_FILE = "events.dat";

    public static void saveEvents(List<Events> events) {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(EVENT_FILE))) {
            oos.writeObject(new ArrayList<>(events));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public static List<Events> loadEvents() {
        if (new File(EVENT_FILE).exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(
                    new FileInputStream(EVENT_FILE))) {
                return (List<Events>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return new ArrayList<>();
    }
}