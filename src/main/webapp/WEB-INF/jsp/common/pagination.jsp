<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>

<div class="pagination-controls">
    <div class="pagination-element">
        <c:if test="${matchesDto.hasPrevious}">
            <a href="${baseURL}&page=${matchesDto.page - 1}">Previous</a>
        </c:if>
    </div>

    <div class="pagination-element">
        <c:if test="${matchesDto.page > 1}">
            <a href="${baseURL}&page=1">First</a>
        </c:if>
    </div>

    <div class="pagination-element">
        <span>Page ${matchesDto.page}</span>
    </div>

    <div class="pagination-element">
        <c:if test="${matchesDto.hasNext}">
            <a href="${baseURL}&page=${matchesDto.page + 1}">Next</a>
        </c:if>
    </div>

    <div class="pagination-element">
        <c:if test="${matchesDto.hasNext}">
            <a href="${baseURL}&page=${matchesDto.totalPages}">Last: ${matchesDto.totalPages}</a>
        </c:if>
    </div>
</div>
