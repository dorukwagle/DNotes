package com.doruk.dnotes.interfaces;

public interface IEditorController extends IController {
    void loadEditorDocument(String fileId);
    void close();
}
