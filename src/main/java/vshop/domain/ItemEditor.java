package vshop.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ItemEditor {
    private String name = null;
    private Long price = null;
    private String desc = null;

    @Builder
    public ItemEditor(String name, Long price, String desc) {
        this.name = name;
        this.price = price;
        this.desc = desc;
    }
}
