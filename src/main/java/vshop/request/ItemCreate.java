package vshop.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import vshop.exception.InvalidRequest;

import java.util.Arrays;
import java.util.List;

@Getter
@Setter
@ToString
public class ItemCreate {
    @NotBlank(message = "이름을 입력해주세요")
    private String name;
    @NotNull(message = "가격을 입력해주세요")
    private Long price;
    private String desc;

    @Builder
    public ItemCreate(String name, Long price, String desc) {
        this.name = name;
        this.price = price;
        this.desc = desc;
    }

    public void validate(){
        List<String> forbiddenWords = Arrays.asList("바보", "수박", "병신", "fuck", "Fuck");
        for (String forbiddenWord : forbiddenWords) {
            if (name.contains(forbiddenWord)) {
                throw new InvalidRequest("사용 불가능한 단어: " + forbiddenWord);
            }
        }
    }
}
