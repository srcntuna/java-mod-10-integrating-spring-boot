package com.example.restservice;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

public interface HaystackUUIDRepository extends CrudRepository<HaystackUUID, Long> {

        @Transactional
        @Modifying
        @Query(value = "TRUNCATE haystackuuid; DROP INDEX IF EXISTS haystackuuid_uuid_idx", nativeQuery = true)
        void truncateTable();

        @Transactional
        @Modifying
        @Query(value = "CREATE INDEX ON haystackuuid (uuid)", nativeQuery = true)
        void indexBTree();

        // TODO: write query to create hash index on value column
        // value = " ... ",
        @Transactional
        @Modifying
        @Query(value = "CREATE INDEX ON haystackuuid USING HASH (uuid)", nativeQuery = true)
        void indexHash();

}
