package org.marcelocc.investing.investboss.repositories;

import java.util.List;

import org.marcelocc.investing.investboss.models.csv.CustomCSV;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CsvRepository extends CrudRepository<CustomCSV, Long> {
    public List<CustomCSV> findByIdConsultaOrderByDateDesc(String idConsulta);
    public void deleteByIdConsulta(String idConsulta);
}
