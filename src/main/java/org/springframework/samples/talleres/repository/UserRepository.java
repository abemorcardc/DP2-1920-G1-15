package org.springframework.samples.talleres.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.talleres.model.User;


public interface UserRepository extends  CrudRepository<User, String>{
	
}
