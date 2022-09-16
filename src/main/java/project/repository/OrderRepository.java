package project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
