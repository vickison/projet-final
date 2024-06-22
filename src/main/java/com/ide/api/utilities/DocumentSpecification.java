package com.ide.api.utilities;

import com.ide.api.entities.Document;
import org.hibernate.query.criteria.internal.predicate.LikePredicate;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class DocumentSpecification implements Specification<Document> {
    private String keyword;
    public DocumentSpecification(String keyword){
        this.keyword = keyword;
    }
    @Override
    public Predicate toPredicate(Root<Document> root,
                                 CriteriaQuery<?> query,
                                 CriteriaBuilder builder){
        String likePattern = getLikePattern(keyword);
        Predicate titrePredicate = builder.like(builder.lower(root.get("titre")), likePattern);
        Predicate resumePredicate = builder.like(builder.lower(root.get("resume")), likePattern);
        //Predicate etiquettePredicate = builder.like(builder.lower(root.join("tableDocumentEtiquette").join("tableEtiquettes").get("tag")), likePattern);
        return builder.or(titrePredicate, resumePredicate);
    }

    private String getLikePattern(String searchTerm){
        if(searchTerm == null || searchTerm.isEmpty()){
            return "%";
        }else {
            return "%"+searchTerm.toLowerCase().replace(" ", "%")+"%";
        }
    }
}
