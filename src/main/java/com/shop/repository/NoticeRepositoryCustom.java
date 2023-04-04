package com.shop.repository;

import com.shop.dto.NoticeSearchDto;
import com.shop.entity.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NoticeRepositoryCustom {
    Page<Notice> getNoticeMainPage(NoticeSearchDto noticeSearchDto, Pageable pageable);

    Page<Notice> getNoticeMngPage(NoticeSearchDto noticeSearchDto, Pageable pageable);

}
