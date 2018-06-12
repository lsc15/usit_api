package com.usit.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="USIT_CODE")
public class UsitCode {

	private String masterCd;
	private String masterCdNm;
	@Id
	private String detailCd;
	private String detailCdNm;
	private String useYn;
	
}
