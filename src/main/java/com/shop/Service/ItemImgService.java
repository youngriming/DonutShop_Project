package com.shop.Service;

import com.shop.entity.ItemImg;
import com.shop.repository.ItemImgRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemImgService {
    @Value("${itemImgLocation}")
    private String itemImgLocation;

    private final ItemImgRepository itemImgRepository;
    private final FileService fileService;
    public void saveItemImg(ItemImg itemImg, MultipartFile itemImgFile) throws Exception{ //상품 이미지 저장
        String oriImgName=itemImgFile.getOriginalFilename();
        String imgName="";
        String imgUrl="";

        //파일 업로드
        if(!StringUtils.isEmpty(oriImgName)){
            imgName = fileService.uploadFile(itemImgLocation, oriImgName,
                    itemImgFile.getBytes());
            imgUrl="/images/item/"+imgName;
        }

        //상품 이미지 정보 저장
        itemImg.updateItemImg(oriImgName, imgName, imgUrl);
        itemImgRepository.save(itemImg);
    }
    //상품 이미지 데이터를 수정
    public void updateItemImg(Long itemImgId, MultipartFile itemImgFile) throws Exception{
        if(!itemImgFile.isEmpty()){
            ItemImg savedItemImg = itemImgRepository.findById(itemImgId) //기존에 저장되어있던 상품 이미지 엔티티 객체 찾아옴.
                    .orElseThrow(EntityNotFoundException::new);

            //기존 이미지 파일 삭제
            if(!StringUtils.isEmpty(savedItemImg.getImgName())){
                fileService.deleteFile(itemImgLocation+"/"+savedItemImg.getImgName());
            }
            String oriImgName = itemImgFile.getOriginalFilename();
            String imgName = fileService.uploadFile(itemImgLocation,oriImgName,itemImgFile.getBytes());
            String imgUrl = "/images/item/"+imgName;
            savedItemImg.updateItemImg(oriImgName,imgName,imgUrl);

        }
    }

    public void deleteItemImg(Long itemImgId, MultipartFile itemImgFile) throws Exception{
        System.out.println(itemImgId);
            ItemImg savedItemImg = itemImgRepository.findById(itemImgId) //기존에 저장되어있던 상품 이미지 엔티티 객체 찾아옴.
                    .orElseThrow(EntityNotFoundException::new);

            //기존 이미지 파일 삭제
            if(!StringUtils.isEmpty(savedItemImg.getImgName())){
                System.out.println(itemImgLocation+"/"+savedItemImg.getImgName());
                fileService.deleteFile(itemImgLocation+"/"+savedItemImg.getImgName());


        }
    }

}
