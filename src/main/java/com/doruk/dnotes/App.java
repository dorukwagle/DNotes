package com.doruk.dnotes;

import com.doruk.dnotes.MarkdownEditor.MarkdownEditor;
import com.doruk.dnotes.controllers.*;
import com.doruk.dnotes.dto.BookDto;
import com.doruk.dnotes.enums.*;
import com.doruk.dnotes.exceptions.DataAccessException;
import com.doruk.dnotes.interfaces.*;
import com.doruk.dnotes.models.QuickBook;
import com.doruk.dnotes.models.SharedBook;
import com.doruk.dnotes.store.BookStore;
import com.doruk.dnotes.store.GlobalConstants;
import com.doruk.dnotes.utils.DatabaseInitializer;
import com.doruk.dnotes.utils.ThemeManager;
import com.doruk.dnotes.utils.UpdatesTracker;
import com.doruk.dnotes.views.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.awt.*;
import java.lang.Thread.UncaughtExceptionHandler;
import java.sql.SQLException;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;
import java.util.function.Supplier;


public class App extends Application {

    private static final UncaughtExceptionHandler exceptionHandler = (t, e) -> {
        DIFactory.createLogger().error(t, (Exception) e);
        System.out.println("Exception caught by the global exception handler ");
        Platform.runLater(() -> {
            var confirm = DIFactory.createConfirmationModal("Do you want to exit?", e.getMessage());
            confirm.setOnOk(() -> System.exit(1));
            confirm.setOnCancel(() -> {
            });
            confirm.showAndWait();
        });
    };

    @Override
    public void start(Stage stage) {
        // set default javafx exception handler
        Thread.currentThread().setUncaughtExceptionHandler(exceptionHandler);

        // set icon for taskbarthrow new RuntimeException(e);
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/icon.png")));

        Map<ViewPage, Supplier<IView>> viewMap = Map.of(
                ViewPage.HOME, HomePage::new,
                ViewPage.BOOK, BookPage::new,
                ViewPage.PREFERENCE, PreferencePage::new,
                ViewPage.QUICK_NOTE, BookPage::new,
                ViewPage.SHARED_NOTE, BookPage::new,
                ViewPage.TRASH, TrashView::new,
                ViewPage.MANAGEMENT, ManagementView::new,
                ViewPage.EDITOR, () -> null
        );

        Map<ViewPage, BiFunction<IView, INavigationController, IController>> controllerMap = Map.of(
                ViewPage.HOME, (view, nav) -> new HomePageController((IHomeView) view, nav),
                ViewPage.BOOK, (view, nav) -> new BookController((IBookView) view, nav),
                ViewPage.QUICK_NOTE, (view, nav) -> new BookController((IBookView) view, nav, new QuickBook()),
                ViewPage.SHARED_NOTE, (view, nav) -> new BookController((IBookView) view, nav, new SharedBook()),
                ViewPage.EDITOR, (_, nav) -> new EditorController(new MarkdownEditor(), nav),
                ViewPage.PREFERENCE, (view, nav) -> new PreferenceController((IPreferenceView) view, nav),
                ViewPage.TRASH, (view, nav) -> new TrashController(nav, view),
                ViewPage.MANAGEMENT, (view, nav) -> new ManagementController(nav, view)
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
        stage.setOnCloseRequest(_ ->
                DIFactory.createEventManager().publishEvent(IEventManager.InternalEvent.SHUTDOWN));

        // finally start the home page
        // make sure to catch even the startup exceptions
        try {
            var prefs = DIFactory.createGlobalPreference();
            var preferredPageState = prefs.loadLong(Preference.AppStartup, AppStartup.StartFresh.getId());
            var preferredPage = AppStartup.fromId((int) preferredPageState);

            var navigationController = NavigationController.getInstance(stage);

            // register global shortcuts
            new GlobalShortcutListener(navigationController);

            // open the context menu on the next pulse
            Platform.runLater(() -> new ContextMenuController(navigationController).showContextMenuAtStartup());
            Platform.runLater(this::checkAndManageUpdate);
            if (preferredPage == AppStartup.StartFresh) {
                navigationController.goToHomePage();
                return;
            }

            var lastVisitedPage = prefs.loadLong(Preference.LastVisitedPage, ViewPage.HOME.getId());
            var page = ViewPage.fromId((int) lastVisitedPage);
            switch (page) {
//                case HOME -> navigationController.goToHomePage();
                case BOOK -> {
                    var bookId = prefs.loadString(Preference.LastOpenedBookId, "");
                    BookStore.setSelectedBook(new BookDto(bookId, "", "", ""));
                    navigationController.goToBooksPage();
                }
                case PREFERENCE -> navigationController.goToPreferencePage();
                case TRASH -> navigationController.goToTrashPage();
                case MANAGEMENT -> navigationController.goToManagementPage();
                default -> navigationController.goToHomePage();
            }
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
        prefs.saveLong(Preference.LastUpdateChecked, new Date().getTime());
    }

    private void checkAndManageUpdate() {
        CompletableFuture.runAsync(() -> {
            if (!UpdatesTracker.isUpdateAvailable())
                return;
            Platform.runLater(() -> {
                var model = DIFactory.createConfirmationModal("Update Available", "A new update is available. Do you want to Download it ?");
                model.setOnOk(this::openInBrowser);
                model.setOnCancel(null);
                model.showAndWait();
            });
        });
    }

    private void openInBrowser() {
        try {
            String os = System.getProperty("os.name").toLowerCase();
            Runtime rt = Runtime.getRuntime();

            if (os.contains("win")) {
                rt.exec(new String[]{"rundll32", "url.dll,FileProtocolHandler", GlobalConstants.UPDATE_DOWNLOAD_URL});
            } else if (os.contains("mac")) {
                rt.exec(new String[]{"open", GlobalConstants.UPDATE_DOWNLOAD_URL});
            } else if (os.contains("nix") || os.contains("nux")) {
                // Linux / BSD
                rt.exec(new String[]{"xdg-open", GlobalConstants.UPDATE_DOWNLOAD_URL});
            } else {
                throw new UnsupportedOperationException("Cannot open browser on this OS");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // catch all uncaught exceptions
        Thread.setDefaultUncaughtExceptionHandler(exceptionHandler);

        // check if another instance is running
        if (!SingleAppInstance.lockInstance()) {
            System.out.println("Another instance of dNotes is already running.");
            System.exit(0);
        }
        Runtime.getRuntime().addShutdownHook(new Thread(SingleAppInstance::releaseLock));
        System.setProperty("prism.title", GlobalConstants.APP_NAME);

        launch(args);
    }
}