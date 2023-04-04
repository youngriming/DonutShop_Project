package com.shop.Service;

import com.shop.dto.NoticeDto;
import com.shop.dto.NoticeSearchDto;
import com.shop.entity.Notice;
import com.shop.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
@Transactional
public class NoticeService {
    private final NoticeRepository noticeRepository;

    public Long saveNotice(NoticeDto noticeDto){
        Notice notice= Notice.createNotice(noticeDto);
        noticeRepository.save(notice);
        return notice.getId();
    }

    @Transactional
    public NoticeDto getNoticeDtl(Long noticeId){
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(EntityNotFoundException::new); // 없으면 예외 던져줌.
        NoticeDto noticeDto = NoticeDto.of(notice);
        return noticeDto;
    }

    public Long updateNotice(NoticeDto noticeDto) throws Exception{
        Notice notice = noticeRepository.findById(noticeDto.getId())
                .orElseThrow(EntityNotFoundException::new);
        notice.updateNotice(noticeDto);
        return notice.getId();
    }

    public void deleteNotice(NoticeDto noticeDto) throws Exception{
        Notice notice = noticeRepository.findById(noticeDto.getId())
                .orElseThrow(EntityNotFoundException::new);
        noticeRepository.delete(notice);
    }

    @Transactional(readOnly = true)
    public Page<Notice> getNoticeMainPage(NoticeSearchDto noticeSearchDto, Pageable pageable){
        return noticeRepository.getNoticeMainPage(noticeSearchDto,pageable);
    }

    @Transactional(readOnly = true)
    public Page<Notice> getNoticeMngPage(NoticeSearchDto noticeSearchDto, Pageable pageable){
        return noticeRepository.getNoticeMngPage(noticeSearchDto,pageable);
    }
}
