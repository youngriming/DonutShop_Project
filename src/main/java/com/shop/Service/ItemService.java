package com.shop.Service;

import com.shop.dto.ItemFormDto;
import com.shop.dto.ItemImgDto;
import com.shop.dto.ItemSearchDto;
import com.shop.dto.MainItemDto;
import com.shop.entity.Item;
import com.shop.entity.ItemImg;
import com.shop.repository.ItemImgRepository;
import com.shop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    private final ItemImgService itemImgService;
    private final ItemImgRepository itemImgRepository;

    public Long saveItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception{
        //상품 등록
        Item item=itemFormDto.createItem();
        itemRepository.save(item);

        //이미지 등록
        for(int i=0; i<itemImgFileList.size(); i++){
            ItemImg itemImg = new ItemImg();
            itemImg.setItem(item);
            if(i==0){
                itemImg.setRepImgYn("Y");
            }else{
                itemImg.setRepImgYn("N");
            }
            itemImgService.saveItemImg(itemImg, itemImgFileList.get(i));
        }
        return item.getId();

    }

    @Transactional(readOnly = true)
    public ItemFormDto getItemDtl(Long itemId){
        //itemId를 이용해서 DB에서 itemImg 엔티티 객체를 찾아와서 리스트에 넣어줌.
        List<ItemImg> itemImgList = itemImgRepository.findByItemIdOrderByIdAsc(itemId);
        List<ItemImgDto> itemImgDtoList = new ArrayList<>(); //Dto를 담을 리스트 생성
        for(ItemImg itemImg : itemImgList){ //itemImgList에서 itemImg 객체를 하나씩 뽑아서
            ItemImgDto itemImgDto = ItemImgDto.of(itemImg); //ModelMappler를 이용해 엔티티의 정보를 dto에 복사해준다.
            itemImgDtoList.add(itemImgDto); //itemImgDto를 Dto 리스트에 하나씩 담아준다
        }

        Item item = itemRepository.findById(itemId) //itemId를 이용해서 item 엔티티 객체를 찾아옴.
                .orElseThrow(EntityNotFoundException::new); // 없으면 예외 던져줌.
        ItemFormDto itemFormDto = ItemFormDto.of(item); // 찾아온 item엔티티를 modelMapper를 통해 itemFormDto에 값을 복사해줌.
        itemFormDto.setItemImgDtoList(itemImgDtoList); //생성된 itemFormDto에 위에서 만든 itemImgDtoList 정보를 넣어줌.
        return itemFormDto;
    }

    public Long updateItem(ItemFormDto itemFormDto,
                           List<MultipartFile> itemImgFileList) throws Exception{
        //상품 수정
        Item item = itemRepository.findById(itemFormDto.getId())
                .orElseThrow(EntityNotFoundException::new);
        item.updateItem(itemFormDto);

        //이미지 수정
        List<Long> itemImgIds = itemFormDto.getItemImgIds();
        for(int i=0; i<itemImgFileList.size(); i++){
            itemImgService.updateItemImg(itemImgIds.get(i), itemImgFileList.get(i));
        }
        return item.getId();
    }
    public void deleteItem(ItemFormDto itemFormDto,
                           List<MultipartFile> itemImgFileList) throws Exception{

        Item item = itemRepository.findById(itemFormDto.getId())
                .orElseThrow(EntityNotFoundException::new);

        //이미지 삭제
        List<Long> itemImgIds = itemFormDto.getItemImgIds();
        for(int i=0; i<itemImgFileList.size(); i++){
            itemImgService.deleteItemImg(itemImgIds.get(i), itemImgFileList.get(i));
        }
        List<ItemImg> ItemImgLists = itemImgRepository.findByItemIdOrderByIdAsc(item.getId());
        for(ItemImg itemImg : ItemImgLists){
            itemImgRepository.delete(itemImg);
        }

        //상품 삭제
        itemRepository.delete(item);

    }

    @Transactional(readOnly = true)
    public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable){
        return itemRepository.getAdminItemPage(itemSearchDto, pageable);
    }

    @Transactional(readOnly = true)
    public Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable){
        return itemRepository.getMainItemPage(itemSearchDto,pageable);
    }

    @Transactional(readOnly = true)
    public Page<MainItemDto> getNewItemPage(Pageable pageable){
        return itemRepository.getNewItemPage(pageable);
    }
}
