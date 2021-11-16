package it.regioneveneto.myp3.gestgraduatorie.web.dto;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.domain.Page;

public class PagedResult<ENTITY> implements Serializable {

	private static final long serialVersionUID = 977773587664826038L;

	private int totalPages;
	private long totalElements;
	private int number;
	private int size;
	private int numberOfElements;
	private List<ENTITY> content;
	private boolean first;
	private boolean last;

	public PagedResult() {
		super();
	}
	
	public PagedResult(List<ENTITY> content, long totalElements, int size, int number) {
		super();
		this.content = content;
		this.totalElements = totalElements;
		this.size = size;
		this.number = number;
		this.totalPages = (int) (this.totalElements / size) == 0 ? 1 : (int) (this.totalElements / size);
		if(this.totalElements % size != 0 && this.totalElements > size) this.totalPages ++;
		this.numberOfElements = this.content.size();
		this.first = this.number == 0 ? true : false;
		this.last = this.totalPages == this.number + 1 ? true : false; 
	}

	public PagedResult(Page<ENTITY> page) {
		super();
		this.totalPages = page.getTotalPages();
		this.totalElements = page.getTotalElements();
		this.number = page.getNumber();
		this.size = page.getSize();
		this.numberOfElements = page.getNumberOfElements();
		this.content = page.getContent();
		this.first = page.isFirst();
		this.last = page.isLast();
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public long getTotalElements() {
		return totalElements;
	}

	public void setTotalElements(long totalElements) {
		this.totalElements = totalElements;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getNumberOfElements() {
		return numberOfElements;
	}

	public void setNumberOfElements(int numberOfElements) {
		this.numberOfElements = numberOfElements;
	}

	public List<ENTITY> getContent() {
		return content;
	}

	public void setContent(List<ENTITY> content) {
		this.content = content;
	}

	public boolean isFirst() {
		return first;
	}

	public void setFirst(boolean first) {
		this.first = first;
	}

	public boolean isLast() {
		return last;
	}

	public void setLast(boolean last) {
		this.last = last;
	}
	
}