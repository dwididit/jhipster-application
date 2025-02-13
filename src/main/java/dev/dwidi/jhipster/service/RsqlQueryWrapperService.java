package dev.dwidi.jhipster.service;

import dev.dwidi.jhipster.security.SecurityUtils;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RsqlQueryWrapperService {

    private final RsqlColumnService rsqlColumnService;

    public <T, D> Page<D> executeQuery(
        String templateName,
        String rsqlQuery,
        Pageable pageable,
        QueryExecutor<T> queryExecutor,
        DtoMapper<T, D> dtoMapper
    ) {
        String userId = SecurityUtils.getCurrentUserLogin().orElse("system");
        List<String> visibleColumns = rsqlColumnService.getVisibleColumns(userId, templateName);
        String enhancedQuery = rsqlColumnService.enhanceQueryWithVisibleColumns(userId, templateName, rsqlQuery);

        log.debug("Using RSQL query: {}", enhancedQuery);
        log.debug("Visible columns: {}", visibleColumns);

        Page<T> result = queryExecutor.execute(enhancedQuery, pageable);

        List<D> dtos = result
            .getContent()
            .stream()
            .map(dtoMapper::toDto)
            .map(dto -> rsqlColumnService.filterDtoFields(dto, visibleColumns))
            .collect(Collectors.toList());

        return new PageImpl<>(dtos, pageable, result.getTotalElements());
    }

    @FunctionalInterface
    public interface QueryExecutor<T> {
        Page<T> execute(String query, Pageable pageable);
    }

    @FunctionalInterface
    public interface DtoMapper<T, D> {
        D toDto(T entity);
    }
}
