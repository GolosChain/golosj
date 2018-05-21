package eu.bittrade.libs.steemj.base.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Beneficiary {
    @JsonProperty("account")
    private AccountName name;
    @JsonProperty("weight")
    private short weight;

    public Beneficiary() {
    }

    public Beneficiary(AccountName name, short weight) {
        this.name = name;
        this.weight = weight;
    }

    public AccountName getName() {
        return name;
    }

    public void setName(AccountName name) {
        this.name = name;
    }

    public short getWeight() {
        return weight;
    }

    public void setWeight(short weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "Beneficiary{" +
                "name='" + name + '\'' +
                ", weight=" + weight +
                '}';
    }
}
