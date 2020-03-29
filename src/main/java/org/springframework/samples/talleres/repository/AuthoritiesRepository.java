package org.springframework.samples.talleres.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.talleres.model.Authorities;



public interface AuthoritiesRepository extends  CrudRepository<Authorities, String>{
	
}
