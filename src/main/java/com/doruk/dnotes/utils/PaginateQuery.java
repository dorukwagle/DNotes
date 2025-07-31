package com.doruk.dnotes.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.doruk.dnotes.dto.PaginationParams;
import com.doruk.dnotes.enums.SortBy;
import com.doruk.dnotes.enums.SortOrder;

public class PaginateQuery {
    private String model;
    private String select;
    private String where;
    private PaginationParams params;
    private String orderBy;
    private String searchBy;
    private String sortBy;
    private Connection connection;

    public PaginateQuery(String model, PaginationParams params) {
        this.model = model;        
        this.select = "";
        this.where = "";
        this.params = params;
        this.connection = DatabaseConnector.getConnection();
    }

    public PaginateQuery select(String select) {
        this.select = this.select.isEmpty() ? select.trim() : this.select + ", " + select.trim();
        return this;
    }

    public PaginateQuery where(String where) {
        this.where = this.where.isEmpty() ? where.trim() : this.where + " and " + where.trim();
        return this;
    }

    public PreparedStatement prepareStatement() throws SQLException {
        resolveParams();

        var statement = "SELECT " + 
            this.select + 
            " FROM " + 
            this.model + 
            (this.where.isEmpty() ? "" : " WHERE " + this.where) +
            " " +
            "ORDER BY " +
            this.orderBy;

        var stmt = this.connection.prepareStatement(statement);

        if (this.where.contains("?"))
            stmt.setString(1, "%" + this.params.getSearch().get() + "%");

        // log the query
        return stmt;
    }

    private void resolveParams() {
        var sortBy = params.getSortBy().orElse(SortBy.Date);
        var sortOrder = params.getSortOrder().orElse(SortOrder.Descending);
        var search = params.getSearch().orElse("");

        if (!search.isEmpty())
            this.where(this.searchBy + " LIKE ?");

        this.orderBy = (sortBy == SortBy.Date ? " updatedAt" : this.sortBy) + (sortOrder == SortOrder.Descending ? " DESC" : " ASC");
    }

    public PaginateQuery searchBy(String searchBy) {
        this.searchBy = searchBy;
        return this;
    }

    public PaginateQuery sortBy(String sortBy) {
        this.sortBy = sortBy;
        return this;
    }
}
