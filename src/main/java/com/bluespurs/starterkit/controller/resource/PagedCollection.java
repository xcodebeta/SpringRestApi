package com.bluespurs.starterkit.controller.resource;

import org.springframework.hateoas.ResourceSupport;

import java.util.Collection;
import java.util.Collections;

public class PagedCollection<To> extends ResourceSupport {
    private int totalPages;
    private int currentPage;
    private long totalItems;
    private Collection content = Collections.emptyList();

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public long getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(long totalItems) {
        this.totalItems = totalItems;
    }

    public Collection getContent() {
        return content;
    }

    public void setContent(Collection content) {
        this.content = content;
    }
}
