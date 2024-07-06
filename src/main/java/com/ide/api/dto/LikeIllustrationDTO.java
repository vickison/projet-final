package com.ide.api.dto;

import com.ide.api.enums.Mention;

public class LikeIllustrationDTO {

    private Mention mention;

    public LikeIllustrationDTO() {
    }

    public LikeIllustrationDTO(Mention mention) {
        this.mention = mention;
    }

    public Mention getMention() {
        return mention;
    }

    public void setMention(Mention mention) {
        this.mention = mention;
    }
}
