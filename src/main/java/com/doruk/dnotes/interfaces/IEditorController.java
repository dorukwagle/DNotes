package com.doruk.dnotes.interfaces;

import com.doruk.dnotes.dto.BookPageDto;

public interface IEditorController extends IController {
    void loadEditorDocument(BookPageDto note);
    void close();
    void freeze(boolean freeze);
}
