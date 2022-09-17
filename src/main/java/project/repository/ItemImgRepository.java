package project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.entity.ItemImg;

public interface ItemImgRepository extends JpaRepository<ItemImg, Long> {
}
