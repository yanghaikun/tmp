package net.rlair.flight.repository;

import net.rlair.flight.entity.Airplane;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Yang Haikun
 * @version 0.1.0.0
 */
@Repository
public interface AirplaneRepository extends JpaRepository<Airplane, Long> {
    @Transactional
    @Modifying
    @Query(value = "INSERT INTO `t_airplane`(`sn`, `model`) VALUES('B5829', '737-1'),('B5830', '737-2'),('B7088', '737-3'),('B1960', '738-1'),('B0808', '738-2'),('B7865', '73A-1'),('B7866', '73A-2'),('B7867', '73A-3'),('B1597', '73A-4'),('B1593', '73B-1'),('B1595', '73D-1'),('B5811', '73D-2'),('B5812', '73D-3'),('B6100', '73D-4');", nativeQuery=true)
    int init();
}
