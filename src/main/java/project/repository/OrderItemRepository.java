package project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.entity.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
