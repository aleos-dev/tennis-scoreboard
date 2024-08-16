package com.aleos.model.in;

import com.aleos.util.PropertiesUtil;

import java.util.Optional;

public interface Pageable {

        int getPageNumber();

        int getPageSize();

        int getOffset();

        Optional<String> getSortBy();

        String getSortDirection();

        Pageable next();

        Pageable previousOrFirst();

        Pageable first();

        boolean hasPrevious();

        static Pageable unpaged() {
                Optional<String> pageOpt = PropertiesUtil.get("pageable.default.page");
                Optional<String> sizeOpt = PropertiesUtil.get("pageable.default.size");
                Optional<String> sortDirectionOpt = PropertiesUtil.get("pageable.default.sortDirection");
                return PageableInfo.of(
                        pageOpt.map(Integer::parseInt).orElse(1),
                        sizeOpt.map(Integer::parseInt).orElse(10),
                        null,
                        sortDirectionOpt.orElse("ASC"));
        }
}
