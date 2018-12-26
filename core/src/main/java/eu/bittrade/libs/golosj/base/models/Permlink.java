package eu.bittrade.libs.golosj.base.models;

import java.security.InvalidParameterException;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import eu.bittrade.libs.golosj.exceptions.SteemInvalidTransactionException;
import eu.bittrade.libs.golosj.interfaces.ByteTransformable;
import eu.bittrade.libs.golosj.util.SteemJUtils;

/**
 * This class represents a "permlink". Steem defines "permlinks" as Strings,
 * which follow a specific pattern. To validate those patterns in golosj, this
 * class has been created and wraps the "permlink" String to offer the
 * validation methods.
 * 
 * @author <a href="http://Steemit.com/@dez1337">dez1337</a>
 */
public class Permlink implements ByteTransformable {
    private String link;

    /**
     * Create a new, empty "permlink".
     */
    public Permlink() {
        this.setLink("");
    }

    /**
     * Create a new "permlink".
     * 
     * @param link
     *            The permanent link to set.
     * @throws InvalidParameterException
     *             If the link is not valid (see {@link #setLink(String)}).
     * 
     */
    @JsonCreator
    public Permlink(String link) {
        this.setLink(link);
    }

    /**
     * Get the "permlink" of this instance.
     * 
     * @return The permanent link.
     */
    @JsonValue
    public String getLink() {
        return link;
    }

    /**
     * Set the "permlink" of this instance.
     * 
     * @param link
     *            The "permlink" in its String representation. The link can
     *            either be empty or needs to have a length between 0 and 256
     *            characters. If provided, only "a-z", "0-9" and "-" are allowed
     *            characters.
     * @throws InvalidParameterException
     *             If the link does not fulfill the requirements describes
     *             above.
     */
    public void setLink(String link) {
        if (link == null) {
            this.link = "";
        } else {
            if (!link.isEmpty()) {
                if (link.length() > 256) {
                    this.link = link.substring(256);
                } else if (!link.equals("18+") &&!link.matches("^[a-zA-Z0-9\\-]{0,256}")) {
                    throw new InvalidParameterException("current link is " + link + " "+
                            "The provided permlink contains invalid characters. Only 'a-z', '0-9' and '-' are allowed. "
                                    + "If copied from steemit.com, the permlink is only the part of the URL after the last '/'.");
                }
            }

            this.link = link;
        }
    }

    @Override
    public byte[] toByteArray() throws SteemInvalidTransactionException {
        return SteemJUtils.transformStringToVarIntByteArray(this.getLink());
    }

    @Override
    public boolean equals(Object otherPermlink) {
        if (this == otherPermlink)
            return true;
        if (otherPermlink == null || !(otherPermlink instanceof Permlink))
            return false;
        Permlink other = (Permlink) otherPermlink;
        return this.getLink().equals(other.getLink());
    }

    @Override
    public String toString() {
        return "Permlink{" +
                "link='" + link + '\'' +
                '}';
    }

    @Override
    public int hashCode() {
        int hashCode = 1;
        hashCode = 31 * hashCode + (this.getLink() == null ? 0 : this.getLink().hashCode());
        return hashCode;
    }

    /**
     * Returns {@code true} if, and only if, the permanent link has more than
     * {@code 0} characters.
     *
     * @return {@code true} if the permanent link has more than {@code 0},
     *         otherwise {@code false}
     */
    public boolean isEmpty() {
        return this.getLink().isEmpty();
    }
}
