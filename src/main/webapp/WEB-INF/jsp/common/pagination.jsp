<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>

<div class="pagination-control">
    <c:if test="${matchesDto.hasPrevious}">
        <a href="?page=${requestScope.matchesDto.page - 1}">Previous</a>
    </c:if>
    <c:if test="${matchesDto.hasNext}">
        <a href="?page=${requestScope.matchesDto.page + 1}">Next</a>
    </c:if>
</div>
