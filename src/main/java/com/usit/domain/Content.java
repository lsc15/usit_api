package com.usit.domain; 
import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.usit.util.TimeUtil;

import lombok.Data;



/**
 * The persistent class for the content database table.
 *
 */
@Data
@Entity
@Table(name="content")
@NamedQuery(name="Content.findAll", query="SELECT c FROM Content c")
public class Content implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name="content_id")
    private Integer contentId;

    private String author;

    @Column(name="content_type_cd")
    private String contentTypeCd;

    @Lob
    private String detail;

    private String link;
    private String img;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column(name="mod_date")
    private LocalDateTime modDate;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column(name="reg_date")
    private LocalDateTime regDate;

    private String title;
    
    @Column(name="display_date")
    private String displayDate;
    
    @Column(name="use_yn")
    private String useYn;


    @PrePersist
    protected void onCreate() {
        regDate = TimeUtil.getZonedDateTimeNow("Asia/Seoul");
    }

    @PreUpdate
    protected void onUpdate() {
        modDate = TimeUtil.getZonedDateTimeNow("Asia/Seoul");
    }
}
