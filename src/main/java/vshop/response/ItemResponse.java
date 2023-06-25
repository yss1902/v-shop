package vshop.response;

import lombok.Builder;
import lombok.Getter;
import vshop.domain.Item;

// 서비스 정책에 맞는 클래스
@Getter
public class ItemResponse {
    private final Long id;
    private final String name;
    private final Long price;
    private final String desc;

    //생성자 오버로딩
    public ItemResponse(Item item) {
        this.id = item.getId();
        this.name = item.getName();
        this.price = item.getPrice();
        this.desc = item.getDesc();
    }

    @Builder
    public ItemResponse(Long id, String name, Long price, String desc) {
        this.id = id;
        this.name = name.substring(0, Math.min(name.length(), 10));
        this.price = price;
        this.desc = desc;
    }
}
