<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<jsp:useBean id="queryParam" scope="request" type="java.lang.String"/>
<jsp:useBean id="pageable" scope="request" type="com.aleos.model.dto.in.PageableInfo"/>

<div class="pagination-controls">

    <div class="pagination-element">
        <c:if test="${dto.hasPrevious}">
            <a href="${requestScope.baseUrl}${queryParam.replaceAll("(before=[^&]*&?)", "")}&page=1&size=${pageable.pageSize}&sortDirection=${pageable.sortDirection}&sortBy=${pageable.sortBy.get()}">First</a>
        </c:if>
    </div>

    <div class="pagination-element">
        <c:if test="${dto.hasPrevious}">
            <c:set var="queryParam" value="${pageable.pageNumber == 2 ? queryParam.replaceAll('(before=[^&]*&?)', '') : queryParam}"/>
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
            <a href="${requestScope.baseUrl}${queryParam}&page=${requestScope.dto.totalPages}&size=${pageable.pageSize}&sortDirection=${pageable.sortDirection}&sortBy=${pageable.sortBy.get()}">Last: ${dto.totalPages}</a>
        </c:if>
    </div>
</div>
