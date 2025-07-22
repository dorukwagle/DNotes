package com.doruk.dnotes.dto;

import java.util.Optional;

import com.doruk.dnotes.enums.SortBy;
import com.doruk.dnotes.enums.SortOrder;

public class PaginationParams {
    private Optional<String> search;
    private Optional<SortBy> sortBy;
    private Optional<SortOrder> sortOrder;

    public PaginationParams() {
        this.search = Optional.empty();
        this.sortBy = Optional.empty();
        this.sortOrder = Optional.empty();
    }

    public PaginationParams(String search, SortBy sortBy, SortOrder sortOrder) {
        this.search = Optional.ofNullable(search);
        this.sortBy = Optional.ofNullable(sortBy);
        this.sortOrder = Optional.ofNullable(sortOrder);
    }

    public Optional<String> getSearch() {
        return search;
    }

    public Optional<SortBy> getSortBy() {
        return sortBy;
    }

    public Optional<SortOrder> getSortOrder() {
        return sortOrder;
    }
}
