package com.doruk.dnotes;

import com.doruk.dnotes.dataUtils.parser.BinaryMarkdownDecoder;
import com.doruk.dnotes.dataUtils.parser.BinaryMarkdownEncoder;
import com.doruk.dnotes.dto.BookDto;
import com.doruk.dnotes.dto.BookPageDto;
import com.doruk.dnotes.dto.CollectionDto;
import com.doruk.dnotes.interfaces.*;
import com.doruk.dnotes.models.BookModel;
import com.doruk.dnotes.models.BookPagesModel;
import com.doruk.dnotes.models.CollectionModel;
import com.doruk.dnotes.prefs.EditorPreference;
import com.doruk.dnotes.prefs.GlobalPreference;
import com.doruk.dnotes.utils.FileLogger;
import com.doruk.dnotes.utils.ShutdownManager;
import com.doruk.dnotes.views.components.ConfirmationModal;
import com.doruk.dnotes.views.components.OptionsModal;
import com.doruk.dnotes.views.components.PromptModal;

public class DIFactory {
    public static IPreference createGlobalPreference() {
        return GlobalPreference.getInstance();
    }

    public static IPreference createEditorPreference() {
        return EditorPreference.getInstance();
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

    public static IShutdownManager createShutdownManager() {
        return ShutdownManager.getInstance();
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
}
