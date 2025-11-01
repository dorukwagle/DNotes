module com.doruk.dnotes {
    requires transitive java.prefs;
    requires java.base;
    requires atlantafx.base;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.core;
    requires org.kordamp.ikonli.materialdesign2;

    requires java.sql;
    requires org.xerial.sqlitejdbc;

    requires org.fxmisc.flowless;
    requires org.fxmisc.undo;
    requires org.fxmisc.richtext;
    requires reactfx;
    requires java.xml.crypto;
    requires jbcrypt;
    requires java.desktop;

    exports com.doruk.dnotes;
    exports com.doruk.dnotes.views;
    exports com.doruk.dnotes.views.components;
    exports com.doruk.dnotes.controllers;
    exports com.doruk.dnotes.MarkdownEditor;
    exports com.doruk.dnotes.dto;
    exports com.doruk.dnotes.interfaces;
    exports com.doruk.dnotes.enums;
    exports com.doruk.dnotes.store;
    exports com.doruk.dnotes.prefs;
    exports com.doruk.dnotes.exceptions;
    exports com.doruk.dnotes.dataUtils;
    exports com.doruk.dnotes.dataUtils.crypto;

    exports com.doruk.dnotes.MarkdownEditor.interfaces;
    exports com.doruk.dnotes.MarkdownEditor.enums;
    exports com.doruk.dnotes.MarkdownEditor.docstyle;
    exports com.doruk.dnotes.MarkdownEditor.dto;
    exports com.doruk.dnotes.MarkdownEditor.utils;
    exports com.doruk.dnotes.MarkdownEditor.changeHandlers;
    exports com.doruk.dnotes.MarkdownEditor.keyActionHandlers;
    exports com.doruk.dnotes.MarkdownEditor.tools;
    exports com.doruk.dnotes.MarkdownEditor.renderers;
    
    exports com.doruk.dnotes.MarkdownEditor.codecs;
    exports com.doruk.dnotes.MarkdownEditor.codecs.codec;
    exports com.doruk.dnotes.MarkdownEditor.codecs.interfaces;
    exports com.doruk.dnotes.MarkdownEditor.codecs.dto;
    exports com.doruk.dnotes.MarkdownEditor.codecs.enums;

    exports com.doruk.dnotes.MarkdownEditor.customFxmisc.util;
}
