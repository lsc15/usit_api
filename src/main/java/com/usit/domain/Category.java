package com.usit.domain;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@Entity
@Table(name="category")
public class Category {

	@Column(name="main_category_code")
	private String mainCategoryCode;
	
	@Column(name="major_category_code")
	private String majorCategoryCode;
	
	@Id
	@Column(name="sub_category_code")
	private String subCategoryCode;
	
	@Column(name="main_category_name")
	private String mainCategoryName;
	
	@Column(name="major_category_name")
	private String majorCategoryName;
	
	@Column(name="sub_category_name")
	private String subCategoryName;

	@Column(name="category_order")
	private int categoryOrder;
	
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	@Column(name="reg_date")
	private LocalDateTime regDate;
	
}
