package eu.bittrade.libs.golosj.base.models;

import com.fasterxml.jackson.annotation.JsonProperty;

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VoteLight)) return false;

        VoteLight voteLight = (VoteLight) o;


        if (rshares != voteLight.rshares) return false;
        if (percent != voteLight.percent) return false;
        return name != null ? name.equals(voteLight.name) : voteLight.name == null;
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
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;

        result = 31 * result + (int) (rshares ^ (rshares >>> 32));
        result = 31 * result + (int) percent;
        return result;
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
