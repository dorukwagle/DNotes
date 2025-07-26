package com.doruk.dnotes;

import com.doruk.dnotes.interfaces.IConfirmationModal;
import com.doruk.dnotes.interfaces.ILogger;
import com.doruk.dnotes.interfaces.IOptionsModal;
import com.doruk.dnotes.interfaces.IPreference;
import com.doruk.dnotes.interfaces.IPromptModal;
import com.doruk.dnotes.prefs.EditorPreference;
import com.doruk.dnotes.prefs.GlobalPreference;
import com.doruk.dnotes.utils.FileLogger;
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

    public static IOptionsModal createOptionsModal() {
        return new OptionsModal();
    }

    public static ILogger createLogger() {
        return new FileLogger();
    }
}
