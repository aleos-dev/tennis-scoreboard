<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>

<div class="pagination-controls">

    <div class="pagination-element">
        <c:if test="${matchesDto.page > 1}">
            <a href="${baseURL}${queryParam}&page=1&size=${pageable.pageSize}&sortDirection=${pageable.sortDirection}&sortBy=${pageable.sortBy.get()}">First</a>
        </c:if>
    </div>
    <div class="pagination-element">
        <c:if test="${matchesDto.hasPrevious}">
            <a href="${baseURL}${queryParam}&page=${matchesDto.page - 1}&size=${pageable.pageSize}&sortDirection=${pageable.sortDirection}&sortBy=${pageable.sortBy.get()}">Previous</a>
        </c:if>
    </div>
    <div class="pagination-element">
        <span>Page ${matchesDto.page}</span>
    </div>

    <div class="pagination-element">
        <c:if test="${matchesDto.hasNext}">
            <a href="${baseURL}${queryParam}&page=${matchesDto.page + 1}&size=${pageable.pageSize}&sortDirection=${pageable.sortDirection}&sortBy=${pageable.sortBy.get()}">Next</a>
        </c:if>
    </div>

    <div class="pagination-element">
        <c:if test="${matchesDto.hasNext}">
            <a href="${baseURL}${queryParam}&page=${matchesDto.totalPages}&size=${pageable.pageSize}&sortDirection=${pageable.sortDirection}&sortBy=${pageable.sortBy.get()}">Last: ${matchesDto.totalPages}</a>
        </c:if>
    </div>
</div>
