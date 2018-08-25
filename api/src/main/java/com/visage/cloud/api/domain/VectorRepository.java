package com.visage.cloud.api.domain;

import com.visage.cloud.api.domain.entity.Vector;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.List;

//@Repository
//@RepositoryRestResource(collectionResourceRel = "vectors", path = "vectors")
public interface VectorRepository{//} extends CrudRepository<Vector, String> {

    List<Vector> findByC1();
}
