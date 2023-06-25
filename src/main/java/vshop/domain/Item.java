package vshop.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Entity
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Long price;
    private String desc;

    @Builder
    public Item(String name, Long price, String desc) {
        this.name = name;
        this.price = price;
        this.desc = desc;
    }

    public ItemEditor.ItemEditorBuilder toEditor(){
        return ItemEditor.builder()
                .name(name)
                .price(price)
                .desc(desc);
    }

    public void edit(ItemEditor itemEditor) {
        name = itemEditor.getName();
        price = itemEditor.getPrice();
        desc = itemEditor.getDesc();
    }
}
