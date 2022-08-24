package com.example.restservice;

import org.springframework.data.repository.CrudRepository;

public interface CounterRepository extends CrudRepository<Counter, Long> {}
