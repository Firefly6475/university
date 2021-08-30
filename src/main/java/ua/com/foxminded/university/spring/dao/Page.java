package ua.com.foxminded.university.spring.dao;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Page {
    private final int pageNumber;
    private final int amountOnPage;
}
