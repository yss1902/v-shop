package vshop.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ItemEdit {
    @NotBlank(message = "이름을 입력해주세요")
    private String name;
    @NotNull(message = "가격을 입력해주세요")
    private Long price;
    private String desc;

    @Builder
    public ItemEdit(String name, Long price, String desc) {
        this.name = name;
        this.price = price;
        this.desc = desc;
    }
}
