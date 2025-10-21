package com.doruk.dnotes;

import com.doruk.dnotes.MarkdownEditor.interfaces.IMarkdownEditor;
import com.doruk.dnotes.controllers.QuickNoteController;
import com.doruk.dnotes.controllers.SharedNoteController;
import com.doruk.dnotes.dataUtils.obfuscator.ObfuscatorInputStream;
import com.doruk.dnotes.dataUtils.obfuscator.ObfuscatorOutputStream;
import com.doruk.dnotes.dataUtils.parser.BinaryMarkdownDecoder;
import com.doruk.dnotes.dataUtils.parser.BinaryMarkdownEncoder;
import com.doruk.dnotes.dataUtils.readWrite.NoteReader;
import com.doruk.dnotes.dataUtils.readWrite.NoteWriter;
import com.doruk.dnotes.dto.BookDto;
import com.doruk.dnotes.dto.BookPageDto;
import com.doruk.dnotes.dto.CollectionDto;
import com.doruk.dnotes.interfaces.*;
import com.doruk.dnotes.models.*;
import com.doruk.dnotes.prefs.GlobalPreference;
import com.doruk.dnotes.utils.FileLogger;
import com.doruk.dnotes.utils.EventManager;
import com.doruk.dnotes.views.ContextView;
import com.doruk.dnotes.views.components.ConfirmationModal;
import com.doruk.dnotes.views.components.GenericModal;
import com.doruk.dnotes.views.components.OptionsModal;
import com.doruk.dnotes.views.components.PromptModal;
import javafx.scene.Parent;

import java.io.InputStream;
import java.io.OutputStream;

public class DIFactory {
    public static IPreference createGlobalPreference() {
        return GlobalPreference.getInstance();
    }

    public static IConfirmationModal createConfirmationModal(String title, String message) {
        return new ConfirmationModal(title, message);
    }

    public static IPromptModal createPromptModal(String title, String message, String field) {
        return new PromptModal(title, message, field);
    }
    
    public static ILogger createLogger() {
        return FileLogger.getInstance();
    }

    public static IEventManager createEventManager() {
        return EventManager.getInstance();
    }

    public static IOptionsModal createOptionsModal() {
        return new OptionsModal();
    }

    public static IModel<CollectionDto> createCollectionModel() {
        return new CollectionModel();
    }

    public static IModel<BookDto> createBookModel() {
        return new BookModel();
    }

    public static IModel<BookPageDto> createNoteModel() {
        return new BookPagesModel();
    }

    public static MarkdownEncoder createMarkdownEncoder(Enum<?>[] codecsName) {
        return new BinaryMarkdownEncoder(codecsName);
    }

    public static MarkdownDecoder createMarkdownDecoder(Enum<?>[] codecsName) {
        return new BinaryMarkdownDecoder(codecsName);
    }

    public static  InputStream createObfuscator(InputStream in, byte[] seed) {
        return new ObfuscatorInputStream(in, seed);
    }

    public static OutputStream createObfuscator(OutputStream out, byte[] seed) {
        return new ObfuscatorOutputStream(out, seed);
    }

    public static IReader createNoteReader(IMarkdownEditor editor) {
        return new NoteReader(editor);
    }

    public static IWriter createNoteWriter(IMarkdownEditor editor) {
        return new NoteWriter(editor);
    }

    public static GenericModal createGenericModal(Parent scene, boolean autoClose, int width, int height) {
        return new GenericModal(scene, autoClose, width, height);
    }

    public static ContextView createContextView() {
        return new ContextView();
    }

    public static SharedNoteController createSharedNoteController(INavigationController nav) {
        return new SharedNoteController(nav);
    }

    public static IQuickNoteController createQuickNoteController(INavigationController nav) {
        return new QuickNoteController(nav);
    }

    public static IManagementModel createManagementModel() {
        return new ManagementModel();
    }

    public static ITrashModel createTrashModel() {
        return new TrashModel();
    }
}
