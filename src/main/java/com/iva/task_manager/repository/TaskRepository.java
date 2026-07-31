package com.iva.task_manager.repository;

import com.iva.task_manager.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByUserUsername(String username);
} //kad kazem extend JPARepository dobijam besplatno bez da napisem ijednu liniju SQL-a
  // save task, find all, find by id, delete by id
  // <Task, Long> znaci ovaj repository radi sa Task entitetom, ciji je ID tipa Long
  // to je sve sto spring treba da zna da bi generisao svu tu logiku u pozadini
/*FindByUsername je magija te metode - "nadji sve taskove ciji user ima usernmae jednak vrednosti"*/
