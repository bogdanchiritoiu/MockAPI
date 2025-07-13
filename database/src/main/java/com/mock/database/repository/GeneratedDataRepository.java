package com.mock.database.repository;

import com.mock.database.entity.GeneratedData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing {@link GeneratedData} entities.
 * This interface extends {@link JpaRepository} to provide standard CRUD operations
 * and additional methods for querying {@link GeneratedData} based on specific fields.
 * <p>
 * It is annotated with {@link Repository} to indicate that it is a Spring Data repository.
 */
@Repository
public interface GeneratedDataRepository extends JpaRepository<GeneratedData, Long>
{
    Optional<GeneratedData> findTopByEndpointOrderByInternalIdDesc(String endpoint);

    /**
     * Look up a single GeneratedData by its endpoint and internalId.
     */
    Optional<GeneratedData> findByEndpointAndInternalId(String endpoint, int internalId);

    List<GeneratedData> findByEndpoint(String endpoint);

}
