package com.usit.domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.usit.util.TimeUtil;

import lombok.Data;

/**
 * The persistent class for the comment database table.
 * 
 */
@Data
@Entity
@Table(name="comment")
@NamedQuery(name="Comment.findAll", query="SELECT c FROM Comment c")
public class Comment implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="comment_Id")
	private Integer commentId;

	@Column(name="child_cnt")
	private int childCnt;

	@Column(name="comment_type_cd")
	private String commentTypeCd;

	private String content;

	@Column(name="member_id")
	private Integer memberId;

	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	@Column(name="mod_date")
	private LocalDateTime modDate;

	@Column(name="mod_id")
	private Integer modId;
	
	@Column(name="reg_id")
	private Integer regId;
	
	@Column(name="parent_comment_id")
	private Integer parentCommentId;

	@Column(name="product_id")
	private Integer productId;

	private int rate;

	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	@Column(name="reg_date")
	private LocalDateTime regDate;

	

	
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "member_id", insertable = false, updatable = false)
	private Member member;

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "product_id", insertable = false, updatable = false)
	private Product product;
	
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "comment_type_cd", insertable = false, updatable = false)
	private UsitCode commentType;
	
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE, orphanRemoval = true)
	@JoinColumn(name="parent_comment_id")
	private List<Comment> replies;
	
	
	
	@PrePersist
	protected void onCreate() {
		regDate = TimeUtil.getZonedDateTimeNow("Asia/Seoul");
	}
	
	@PreUpdate
	protected void onUpdate() {
		modDate = TimeUtil.getZonedDateTimeNow("Asia/Seoul");
	}

}