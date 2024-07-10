package com.ide.api.service;

import com.ide.api.dto.LikeCountDTO;
import com.ide.api.entities.LikeIllustration;
import com.ide.api.enums.Mention;
import com.ide.api.repository.LikeIllustrationRepository;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

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

    public List<LikeCountDTO> countByMention(Integer docId){
        List<Object[]> result = this.likeIllustrationRepository.countByMention(docId);
        return result.stream()
                .map(arr -> new LikeCountDTO((String) arr[0], (BigInteger) arr[1]))
                .collect(Collectors.toList());

    }
}
