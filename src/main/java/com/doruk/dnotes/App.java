package com.doruk.dnotes;

import java.lang.Thread.UncaughtExceptionHandler;
import java.sql.SQLException;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Supplier;

import com.doruk.dnotes.MarkdownEditor.MarkdownEditor;
import com.doruk.dnotes.controllers.*;
import com.doruk.dnotes.dto.BookDto;
import com.doruk.dnotes.enums.AppStartup;
import com.doruk.dnotes.enums.MarkdownEditorColor;
import com.doruk.dnotes.enums.Preference;
import com.doruk.dnotes.enums.Themes;
import com.doruk.dnotes.enums.ViewPage;
import com.doruk.dnotes.exceptions.DataAccessException;
import com.doruk.dnotes.interfaces.*;
import com.doruk.dnotes.store.BookStore;
import com.doruk.dnotes.utils.DatabaseInitializer;
import com.doruk.dnotes.utils.ThemeManager;
import com.doruk.dnotes.views.BookPage;
import com.doruk.dnotes.views.ContextView;
import com.doruk.dnotes.views.HomePage;
import com.doruk.dnotes.views.PreferencePage;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.stage.Stage;


public class App extends Application {

    private static final UncaughtExceptionHandler exceptionHandler = (t, e) -> {
        DIFactory.createLogger().error(t, (Exception) e);
        System.out.println("Exception caught by the global exception handler ");
        Platform.runLater(() -> {
            var confirm = DIFactory.createConfirmationModal("Do you want to exit?", e.getMessage());
            confirm.setOnOk(() -> System.exit(1));
            confirm.setOnCancel(() -> {});
            confirm.showAndWait();
        });
    };

    @Override
    public void start(Stage stage) {
        // set default javafx exception handler
        Thread.currentThread().setUncaughtExceptionHandler(exceptionHandler);

        Map<ViewPage, Supplier<IView>> viewMap = Map.of(
            ViewPage.HOME, HomePage::new,
            ViewPage.BOOK, BookPage::new,
            ViewPage.PREFERENCE, PreferencePage::new,
            ViewPage.EDITOR, () -> null
        );

        Map<ViewPage, BiFunction<IView, INavigationController, IController>> controllerMap = Map.of(
            ViewPage.HOME, (view, nav) -> new HomePageController((IHomeView)view, nav),
            ViewPage.BOOK, (view, nav) -> new BookController((IBookView)view, nav),
            ViewPage.EDITOR, (_, nav) -> new EditorController(new MarkdownEditor(), nav),
            ViewPage.PREFERENCE, (view, nav) -> new PreferenceController((IPreferenceView)view, nav)
        );
        
        ControllerFactory.init(viewMap, controllerMap);
     
        // save default settings in first run
        saveDefaultSettings();

        ThemeManager.getInstance().applyGlobalTheme();
       
        // initialize database if not already
        try {
            DatabaseInitializer.initialize();
        } catch (RuntimeException | SQLException e) {
            throw new DataAccessException("Failed to initialize the database. Application cannot run without it.", e);
        }

        // execute listeners for cleanup before shut down
        stage.setOnCloseRequest(_ -> DIFactory.createEventManager().publishEvent(IEventManager.InternalEvent.SHUTDOWN));

        // finally start the home page
        // make sure to catch even the startup exceptions
        try {
            var prefs = DIFactory.createGlobalPreference();
            var preferredPageState = prefs.loadLong(Preference.AppStartup, AppStartup.StartFresh.getId());
            var preferredPage = AppStartup.fromId((int)preferredPageState);

            var navigationController = NavigationController.getInstance(stage);

            if (preferredPage == AppStartup.StartFresh) {
                navigationController.goToHomePage();
                return;
            }

            var lastVisitedPage = prefs.loadLong(Preference.LastVisitedPage, ViewPage.HOME.getId());
            var page = ViewPage.fromId((int)lastVisitedPage);
            switch (page) {
//                case HOME -> navigationController.goToHomePage();
                case BOOK -> {
                    var bookId = prefs.loadString(Preference.LastOpenedBookId, "");
                    BookStore.setSelectedBook(new BookDto(bookId, "", "", ""));
                    navigationController.goToBooksPage();
                }
                case PREFERENCE -> navigationController.goToPreferencePage();
                default -> navigationController.goToHomePage();
            }

            // register global shortcuts
            new GlobalShortcutListener(navigationController.getScene());

            // finally open the context menu
            new ContextMenuController().showContextMenuAtStartup();
        } catch (Exception e) {
            exceptionHandler.uncaughtException(Thread.currentThread(), e);
        }
    }

    private static void saveDefaultSettings() {
        var prefs = DIFactory.createGlobalPreference();
        // only run if it's first run
        if (!prefs.loadBoolean(Preference.IsFirstRun, true)) 
            return;
        
        prefs.saveLong(Preference.Theme, Themes.CupertinoDark.getId());
        prefs.saveLong(Preference.EditorColor, MarkdownEditorColor.Muted.getId());
        prefs.saveBoolean(Preference.IsFirstRun, false);
        prefs.saveLong(Preference.LastVisitedPage, ViewPage.HOME.getId());
        prefs.saveLong(Preference.AppStartup, AppStartup.StartFresh.getId());
        prefs.saveBoolean(Preference.ShowContextMenuAtStartup, true);
    }

    public static void run(String[] args) {
        // catch all uncaught exceptions
        Thread.setDefaultUncaughtExceptionHandler(exceptionHandler);
        launch();
    }
}