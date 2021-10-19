package ua.com.foxminded.university.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import java.util.List;

@Controller
public abstract class GenericController<E> {
    private static final Integer AMOUNT_PER_PAGE = 10;

    public String showGenericEntities(Integer totalPages, List<E> pagedEntities, Model model) {
        model.addAttribute("totalPages", totalPages);
        return payload(pagedEntities, model);
    }

    public String showEntitiesWithList(String stringPageNumber, List<E> allEntities, Model model) {
        Integer pageNumber = parsePageNumber(stringPageNumber);
        Integer totalPages = getTotalPages(allEntities);
        pageNumber = validatePageNumber(pageNumber, totalPages);
        model.addAttribute("totalPages", totalPages);
        if (allEntities.size() > AMOUNT_PER_PAGE) {
            int startIndex = (pageNumber - 1) * AMOUNT_PER_PAGE;
            int endIndex = startIndex + AMOUNT_PER_PAGE;
            if (endIndex >= allEntities.size()) {
                endIndex = allEntities.size();
            }
            allEntities = allEntities.subList(startIndex, endIndex);
        }
        return payload(allEntities, model);
    }

    protected Integer validatePageNumber(Integer pageNumber, Integer totalPages) {
        if (pageNumber > totalPages || pageNumber <= 0) {
            pageNumber = 1;
        }
        return pageNumber;
    }

    protected Integer parsePageNumber(String stringPageNumber) {
        Integer pageNumber;
        try {
            pageNumber = Integer.parseInt(stringPageNumber);
        } catch (NumberFormatException ex) {
            pageNumber = 1;
        }
        return pageNumber;
    }

    protected Integer getTotalPages(List<E> entities) {
        return (entities.size() / AMOUNT_PER_PAGE) + 1;
    }

    protected abstract String payload(List<E> entities, Model model);
}
