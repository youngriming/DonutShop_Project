package com.shop.entity;

import com.shop.dto.NoticeDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="notice")
@Getter
@Setter
@ToString
public class Notice extends BaseEntity{
    @Id
    @Column(name="notice_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String author;
    @Lob
    @Column(nullable = false)
    private String noticeDetail;

    public static Notice createNotice(NoticeDto noticeDto){
        Notice notice = new Notice();
        notice.setTitle(noticeDto.getTitle());
        notice.setAuthor(noticeDto.getAuthor());
        notice.setNoticeDetail(noticeDto.getNoticeDetail());
        return notice;
    }

    public void updateNotice(NoticeDto noticeDto){
        this.title=noticeDto.getTitle();
        this.author=noticeDto.getAuthor();
        this.noticeDetail=noticeDto.getNoticeDetail();
    }

}
