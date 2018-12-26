package eu.bittrade.libs.golosj.base.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

/**
 * Created by yuri on 22.01.18.
 */

public class VoteLight {
    @JsonProperty("name")
    private String name;
    @JsonProperty("rshares")
    private long rshares;
    @JsonProperty("percent")
    private int percent;

    public VoteLight(String name, long rshares, int percent) {
        this.name = name;
        this.rshares = rshares;
        this.percent = percent;
    }

    @Override
    public String toString() {
        return "VoteLight{" +
                "name='" + name + '\'' +

                ", rshares=" + rshares +
                ", percent=" + percent +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VoteLight voteLight = (VoteLight) o;
        return rshares == voteLight.rshares &&
                percent == voteLight.percent &&
                Objects.equals(name, voteLight.name);
    }

    @Override
    public int hashCode() {

        return Objects.hash(name, rshares, percent);
    }

    public VoteLight() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getRshares() {
        return rshares;
    }

    public void setRshares(long rshares) {
        this.rshares = rshares;
    }

    public int getPercent() {
        return percent;
    }

    public void setPercent(short percent) {
        this.percent = percent;
    }
}
