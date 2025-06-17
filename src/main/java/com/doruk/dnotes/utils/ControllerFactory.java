package com.doruk.dnotes.utils;

import com.doruk.dnotes.enums.ViewPage;
import com.doruk.dnotes.interfaces.IController;
import com.doruk.dnotes.interfaces.INavigationController;
import com.doruk.dnotes.interfaces.IView;

import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public class ControllerFactory {
    private static Map<ViewPage, Supplier<IView>> viewMap;
    private static Map<ViewPage, BiFunction<IView, INavigationController, IController>> controllerMap;

    public static void init(Map<ViewPage, Supplier<IView>> views, Map<ViewPage, BiFunction<IView, INavigationController, IController>> controller) {
        viewMap = views;
        controllerMap = controller;
    }

    public static IController create(ViewPage page, INavigationController nav) {
        var view = viewMap.get(page).get();
        return controllerMap.get(page).apply(view, nav);
    }
}
