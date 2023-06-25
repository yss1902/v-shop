package vshop.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vshop.domain.Item;
import vshop.domain.ItemEditor;
import vshop.exception.ItemNotFound;
import vshop.repository.ItemRepository;
import vshop.request.ItemCreate;
import vshop.request.ItemEdit;
import vshop.request.ItemSearch;
import vshop.response.ItemResponse;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    //아이템을 저장하는 메서드
    public void save(ItemCreate itemCreate){
        Item item = Item.builder()
                .name(itemCreate.getName())
                .price(itemCreate.getPrice())
                .desc(itemCreate.getDesc())
                .build();
        itemRepository.save(item);
    }
    public ItemResponse get(Long id){
        Item item = itemRepository.findById(id)
                .orElseThrow(ItemNotFound::new);

        return ItemResponse.builder()
                .id(item.getId())
                .name(item.getName())
                .price(item.getPrice())
                .desc(item.getDesc())
                .build();
    }

    public List<ItemResponse> getList(ItemSearch itemSearch) {
        return itemRepository.getList(itemSearch).stream()
                .map(ItemResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional //import org.springframework.transaction.annotation.Transactional;
    public void edit(Long id, ItemEdit itemEdit){
        Item item = itemRepository.findById(id)
                .orElseThrow(ItemNotFound::new);

        ItemEditor.ItemEditorBuilder editorBuilder = item.toEditor();
        ItemEditor itemEditor = editorBuilder.name(itemEdit.getName())
                .price(itemEdit.getPrice())
                .desc(itemEdit.getDesc())
                .build();
        item.edit(itemEditor);

    }

    public void delete(Long id) {
        Item item = itemRepository.findById(id)
                .orElseThrow(ItemNotFound::new);
        itemRepository.delete(item);
    }
}
