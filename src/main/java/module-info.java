module com.doruk.dnotes {
    requires transitive javafx.base;
    requires transitive javafx.controls;
    requires transitive javafx.graphics;
    requires java.base;
    requires atlantafx.base;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.core;
    requires org.kordamp.ikonli.materialdesign2;

    requires org.fxmisc.richtext;
    requires reactfx;
    requires org.fxmisc.flowless;

    exports com.doruk.dnotes;
    exports com.doruk.dnotes.views;
    exports com.doruk.dnotes.views.components;
    exports com.doruk.dnotes.controllers;
    exports com.doruk.dnotes.MarkdownEditor;
    exports com.doruk.dnotes.dto;
    exports com.doruk.dnotes.interfaces;
}
