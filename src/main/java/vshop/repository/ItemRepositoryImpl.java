package vshop.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import vshop.domain.Item;
import vshop.domain.QItem;
import vshop.request.ItemSearch;

import java.util.List;

@RequiredArgsConstructor
public class ItemRepositoryImpl implements ItemRepositoryCustom{
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Item> getList(ItemSearch itemSearch) {
        return jpaQueryFactory.selectFrom(QItem.item)
                .limit(itemSearch.getSize())
                .offset(itemSearch.getOffset())
                .orderBy(QItem.item.id.desc())
                .fetch();
    }
}
