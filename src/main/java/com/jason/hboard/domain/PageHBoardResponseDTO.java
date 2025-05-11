package com.jason.hboard.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class PageHBoardResponseDTO<T> {

  private int pageNo;
  private int pageSize;
  private int totalPosts;

  private int blockStartPage;
  private int blockEndPage;
  private int lastPage;

  private boolean showPrevBlockButton;
  private boolean showNextBlockButton;

  private List<T> responseDTOList;


  @Builder(builderMethodName = "withPageInfo") // 빌더의 이름 지정
  public PageHBoardResponseDTO(PageHBoardRequestDTO pageHBoardRequestDTO, List<T> responseDTOList, int totalPosts) {
    this.pageNo = pageHBoardRequestDTO.getPageNo();
    this.pageSize = pageHBoardRequestDTO.getPageSize();
    this.totalPosts = totalPosts;

    this.responseDTOList = responseDTOList;

    this.blockEndPage = (((this.pageNo - 1) / this.pageSize) + 1) * this.pageSize;
    this.blockStartPage = this.blockEndPage - (this.pageSize - 1);
    this.lastPage = (int)(Math.ceil(this.totalPosts/(double)pageSize));

    this.blockEndPage = Math.min(this.blockEndPage, this.lastPage);

    this.showPrevBlockButton = this.blockStartPage > 1;
    this.showNextBlockButton = this.blockEndPage < this.lastPage;
  }
}
