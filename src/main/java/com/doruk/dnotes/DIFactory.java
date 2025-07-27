package com.doruk.dnotes;

import com.doruk.dnotes.dto.BookDto;
import com.doruk.dnotes.dto.CollectionDto;
import com.doruk.dnotes.interfaces.IConfirmationModal;
import com.doruk.dnotes.interfaces.ILogger;
import com.doruk.dnotes.interfaces.IModel;
import com.doruk.dnotes.interfaces.IOptionsModal;
import com.doruk.dnotes.interfaces.IPreference;
import com.doruk.dnotes.interfaces.IPromptModal;
import com.doruk.dnotes.interfaces.IShutdownManager;
import com.doruk.dnotes.models.BookModel;
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
}
