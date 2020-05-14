
package org.springframework.samples.talleres.talleres.springdatajpa;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.samples.talleres.model.Averia;
import org.springframework.samples.talleres.repository.AveriaRepository;

public interface SpringDataAveriaRepository extends AveriaRepository, Repository<Averia, Integer> {

	@Override
	@Query("SELECT averia FROM Averia averia WHERE averia.vehiculo.id =:vehiculoId")
	Collection<Averia> findAveriasByVehiculoId(int vehiculoId);

	@Override
	@Query("SELECT averia FROM Averia averia WHERE averia.id =:id")
	Averia findById(int id);

	@Override
	@Query("SELECT averia FROM Averia averia WHERE averia.cita.id =:citaId")
	Collection<Averia> findAveriasByCitaId(Integer citaId);

}
