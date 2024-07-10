package com.ide.api.dto;


import com.ide.api.enums.Mention;

import java.math.BigInteger;

public class LikeCountDTO {
    private String mention;
    private BigInteger count;

    public LikeCountDTO(String mention, BigInteger count) {
        this.mention = mention;
        this.count = count;
    }

    public String getMention() {
        return mention;
    }

    public void setMention(String mention) {
        this.mention = mention;
    }

    public BigInteger getCount() {
        return count;
    }

    public void setCount(BigInteger count) {
        this.count = count;
    }
}
