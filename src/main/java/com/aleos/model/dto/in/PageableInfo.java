package com.aleos.model.dto.in;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Value;

import java.util.Optional;

@Value(staticConstructor = "of")
public class PageableInfo implements Pageable {

    @Min(value = 1, message = "Page should be at least {value}.")
    @Max(value = 255, message = "Page should be no more than {value}.")
    int pageNumber;

    @Min(value = 0, message = "Size should be at least {value}.")
    @Max(value = 255, message = "Size should be no more than {value}.")
    int pageSize;

    @NotBlank(message = "Sort by field cannot be blank.")
//        @ValidSortBy(allowedValues = {"timestamp, name"})
    String sortBy;

    @Pattern(regexp = "(?i)asc|desc", message = "Sort direction must be either 'asc' or 'desc'.")
    String sortDirection;


    @Override
    public int getPageNumber() {
        return pageNumber;
    }

    @Override
    public int getPageSize() {
        return pageSize;
    }

    @Override
    public int getOffset() {
        return (pageNumber -1) * pageSize;
    }

    @Override
    public Optional<String> getSortBy() {
        return Optional.ofNullable(sortBy);
    }

    @Override
    public String getSortDirection() {
        return sortDirection;
    }

    @Override
    public Pageable next() {
        return PageableInfo.of(pageNumber + 1, pageSize, sortBy, sortDirection);
    }

    @Override
    public Pageable previousOrFirst() {
        return PageableInfo.of(Math.max(1, pageNumber - 1), pageSize, sortBy, sortDirection);
    }

    @Override
    public Pageable first() {
        return PageableInfo.of(1, pageSize, sortBy, sortDirection);
    }


    @Override
    public boolean hasPrevious() {
        return pageNumber > 1;
    }
}