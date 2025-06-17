package com.doruk.dnotes.utils;

import com.doruk.dnotes.enums.ViewPage;
import com.doruk.dnotes.interfaces.IController;
import com.doruk.dnotes.interfaces.IView;

import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public class ControllerFactory {
    private static Map<ViewPage, Supplier<IView>> viewMap;
    private static Map<ViewPage, Function<IView, IController>> controllerMap;

    public static void init(Map<ViewPage, Supplier<IView>> views, Map<ViewPage, Function<IView, IController>> controller) {
        viewMap = views;
        controllerMap = controller;
    }

    public static IController create(ViewPage page) {
        var view = viewMap.get(page).get();
        return controllerMap.get(page).apply(view);
    }
}
