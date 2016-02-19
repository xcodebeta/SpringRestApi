package com.bluespurs.starterkit.controller.resource.assembler;

import com.bluespurs.starterkit.controller.resource.PagedCollection;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;

import java.util.Collection;
import java.util.stream.Collectors;

public interface Assembler<From, To> extends Converter<From, To> {
    To assemble(From from);

    @Override
    default To convert(From from) {
        return assemble(from);
    }

    default Collection<To> assemble(Collection<From> fromCollection) {
        return fromCollection.stream()
            .map(this::assemble)
            .collect(Collectors.toList());
    }

    default PagedCollection<To> assemble(Page<From> fromPagination) {
        PagedCollection<To> pageable = new PagedCollection<>();

        pageable.setContent(assemble(fromPagination.getContent()));
        pageable.setCurrentPage(fromPagination.getNumber() + 1); // Paging is 0-indexed in Spring.
        pageable.setTotalPages(fromPagination.getTotalPages());
        pageable.setTotalItems(fromPagination.getTotalElements());

        return pageable;
    }
}
