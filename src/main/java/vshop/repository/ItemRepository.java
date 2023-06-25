package vshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vshop.domain.Item;

public interface ItemRepository extends JpaRepository<Item, Long>, ItemRepositoryCustom {
    //Item이란 엔티티와 Item의 프라이머리 키의 타입을 말한다.
}
