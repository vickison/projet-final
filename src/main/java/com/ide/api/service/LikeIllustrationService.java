package com.ide.api.service;

import com.ide.api.entities.LikeIllustration;
import com.ide.api.repository.LikeIllustrationRepository;
import org.springframework.stereotype.Service;

@Service
public class LikeIllustrationService {
    private LikeIllustrationRepository likeIllustrationRepository;

    public LikeIllustrationService(LikeIllustrationRepository likeIllustrationRepository) {
        this.likeIllustrationRepository = likeIllustrationRepository;
    }

    public void createLikeIllustration(LikeIllustration illustration){
        this.likeIllustrationRepository.save(illustration);
    }

    public boolean existingLike(Integer docID, String utilIP){
        return this.likeIllustrationRepository.existsByDocumentIDAndUtilisateurIP(docID, utilIP);
    }

    public LikeIllustration findLikedIllus(Integer docID, String utilIP){
        return this.likeIllustrationRepository.findByDocumentIDAndUtilisateurIP(docID, utilIP);
    }
}
