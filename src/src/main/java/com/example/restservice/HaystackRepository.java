package com.example.restservice;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface HaystackRepository extends CrudRepository<Haystack, Long> {
    @Transactional
    @Modifying
    @Query(
            value = "TRUNCATE haystack; DROP INDEX IF EXISTS haystack_value_idx",
            nativeQuery = true)
    void truncateTable();

    @Transactional
    @Modifying
    @Query(
            value = "CREATE INDEX ON haystack (value)",
            nativeQuery = true)
    void indexBTree();

    @Transactional
    @Modifying
    @Query(
            // TODO: write query to create hash index on value column
            // value = " ... ",
            nativeQuery = true)
    void indexHash();

    @Query(
            // TODO: return full row when the value column is a needle
            // value = " ... ",
            nativeQuery = true)
    Haystack seqScan();

    @Query(
            // TODO: return the performance metrics from running the seqScan query
            // value = "EXPLAIN ANALYZE ...",
            nativeQuery = true)
    List<String> seqScanPerf();

    @Query(
            // TODO: return values of id, uuid, and value from an inner join between haystack and haystackuuid tables when the value column is a needle
            // value = " ... ",
            nativeQuery = true)
    Haystack tableJoin();

    @Query(
            // TODO: return the performance metrics from running the tableJoin query
            // value = "EXPLAIN ANALYZE ...",
            nativeQuery = true)
    List<String> tableJoinPerf();
}

