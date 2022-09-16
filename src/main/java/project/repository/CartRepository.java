package project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.entity.Cart;

public interface CartRepository extends JpaRepository<Cart, Long> {
}
