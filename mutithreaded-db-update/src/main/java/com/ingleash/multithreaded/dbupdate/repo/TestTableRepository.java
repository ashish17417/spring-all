package com.ingleash.multithreaded.dbupdate.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ingleash.multithreaded.dbupdate.domain.TestTable;

@Repository
public interface TestTableRepository extends CrudRepository<TestTable, Long> {

}
