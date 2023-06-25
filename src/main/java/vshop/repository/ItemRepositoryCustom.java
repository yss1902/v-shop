package vshop.repository;

import vshop.domain.Item;
import vshop.request.ItemSearch;

import java.util.List;

public interface ItemRepositoryCustom {
    List<Item> getList(ItemSearch itemSearch);
}
