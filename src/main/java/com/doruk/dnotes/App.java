package com.doruk.dnotes;

import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Supplier;

import com.doruk.dnotes.MarkdownEditor.MarkdownEditor;
import com.doruk.dnotes.controllers.BookController;
import com.doruk.dnotes.controllers.EditorController;
import com.doruk.dnotes.controllers.HomePageController;
import com.doruk.dnotes.controllers.PreferenceController;
import com.doruk.dnotes.enums.Themes;
import com.doruk.dnotes.enums.ViewPage;
import com.doruk.dnotes.interfaces.IBookView;
import com.doruk.dnotes.interfaces.IController;
import com.doruk.dnotes.interfaces.IHomeView;
import com.doruk.dnotes.interfaces.INavigationController;
import com.doruk.dnotes.interfaces.IPreferenceView;
import com.doruk.dnotes.interfaces.IView;
import com.doruk.dnotes.utils.ThemeManager;
import com.doruk.dnotes.views.BookPage;
import com.doruk.dnotes.views.HomePage;
import com.doruk.dnotes.views.PreferencePage;

import javafx.application.Application;
import javafx.stage.Stage;


public class App extends Application {

    @Override
    public void start(Stage stage) {
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
        
        var navigationController = NavigationController.getInstance(stage);
        navigationController.goToBooksPage();
        
        ThemeManager.getInstance().applyGlobalTheme(Themes.CUPERTINO_DARK);
    }

    public static void main(String[] args) {
        launch();
    }
}