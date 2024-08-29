<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>

<div class="pagination-controls">

    <div class="pagination-element">
        <c:if test="${pageable.pageNumber > 1}">
            <a href="${requestScope.baseUrl}${queryParam}&page=1&size=${pageable.pageSize}&sortDirection=${pageable.sortDirection}&sortBy=${pageable.sortBy.get()}">First</a>
        </c:if>
    </div>

    <div class="pagination-element">
        <c:if test="${dto.hasPrevious}">
            <a href="${requestScope.baseUrl}${queryParam}&page=${pageable.pageNumber - 1}&size=${pageable.pageSize}&sortDirection=${pageable.sortDirection}&sortBy=${pageable.sortBy.get()}">Previous</a>
        </c:if>
    </div>

    <div class="pagination-element">
        <span>Page: ${dto.page}</span>
    </div>

    <div class="pagination-element">
        <c:if test="${dto.hasNext}">
            <a href="${requestScope.baseUrl}${queryParam}&page=${pageable.pageNumber + 1}&size=${pageable.pageSize}&sortDirection=${pageable.sortDirection}&sortBy=${pageable.sortBy.get()}">Next</a>
        </c:if>
    </div>

    <div class="pagination-element">
        <c:if test="${dto.hasNext}">
            <a href="${requestScope.baseUrl}${queryParam}&page=${requestScope.dto.totalPages}&size=${pageable.pageSize}&sortDirection=${pageable.sortDirection}&sortBy=${pageable.sortBy.get()}">Last: ${matchesDto.totalPages}</a>
        </c:if>
    </div>
</div>
