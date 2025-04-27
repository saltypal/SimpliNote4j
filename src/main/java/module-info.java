module com.simplinote.simplinote {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires com.almasb.fxgl.all;
    requires langchain4j.open.ai;
    requires langchain4j.core;
    requires langchain4j.ollama;
    requires org.controlsfx.controls;
    requires com.github.dockerjava.transport.zerodep;
    requires langchain4j;
    requires com.fasterxml.jackson.databind;
    requires mapdb;
    requires com.google.gson;
    requires com.google.common;
    requires java.desktop;

    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    opens com.simplinote.simplinote to javafx.fxml;
    exports com.simplinote.simplinote;
}