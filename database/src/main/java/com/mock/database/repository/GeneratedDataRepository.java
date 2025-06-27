package com.mock.database.repository;

import com.mock.database.entity.GeneratedData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GeneratedDataRepository extends JpaRepository<GeneratedData, Long>
{
    // Nothing
}
