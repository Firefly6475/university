package ua.com.foxminded.university.spring.dao;

public class Page {
    private int pageNumber;
    private int amountOnPage;

    public Page(int pageNumber, int amountOnPage) {
        this.pageNumber = pageNumber;
        this.amountOnPage = amountOnPage;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public int getAmountOnPage() {
        return amountOnPage;
    }
}
