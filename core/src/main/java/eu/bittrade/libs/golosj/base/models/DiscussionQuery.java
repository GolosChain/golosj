package eu.bittrade.libs.golosj.base.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.annotation.Nullable;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;


/**
 * This class represents the Steem "discussion_query" object.
 *
 * @author <a href="http://steemit.com/@dez1337">dez1337</a>
 */
@JsonInclude(Include.NON_NULL)
public class DiscussionQuery {
    @JsonProperty("limit")
    @Nullable
    private Integer limit;
    @JsonProperty("select_tags")
    @Nullable
    private List<String> selectTags;
    @JsonProperty("filter_tags")
    @Nullable
    private List<String> filterTags;
    @JsonProperty("select_languages")
    @Nullable
    private List<String> selectLanguages;
    @JsonProperty("filter_languages")
    @Nullable
    private List<String> filterLanguages;
    @JsonProperty("vote_limit")
    @Nullable
    private Integer voteLimit;
    @JsonProperty("truncate_body")
    @Nullable
    private Integer truncateBody;
    @JsonProperty("select_authors")
    @Nullable
    private List<AccountName> selectAuthors;
    @JsonProperty("start_author")
    @Nullable
    private AccountName startAuthor;
    @JsonProperty("start_permlink")
    @Nullable
    private Permlink startPermlink;
    @JsonProperty("parent_author")
    @Nullable
    private AccountName parentAuthor;
    @JsonProperty("parent_permlink")
    @Nullable
    private Permlink parentPermlink;

    /**
     * Create a new DiscussionQuery instance to filter for discussions.
     */
    public DiscussionQuery() {
        this.setLimit(0);
        this.setTruncateBody(0);
    }

    public DiscussionQuery(@Nonnull
                                   Integer limit,
                           @Nullable
                                   List<String> selectTags,
                           @Nullable
                                   List<String> filterTags,
                           @Nullable
                                   List<String> selectLanguages,
                           @Nullable
                                   List<String> filterLanguages,
                           @Nullable
                                   Integer voteLimit,
                           @Nonnull
                                   Integer truncateBody,
                           @Nullable
                                   List<AccountName> selectAuthors,
                           @Nullable
                                   AccountName startAuthor,
                           @Nullable
                                   Permlink startPermlink,
                           @Nullable
                                   AccountName parentAuthor,
                           @Nullable
                                   Permlink parentPermlink) {
        this.limit = limit;
        this.selectTags = selectTags;
        this.filterTags = filterTags;
        this.selectLanguages = selectLanguages;
        this.filterLanguages = filterLanguages;
        this.voteLimit = voteLimit;
        this.truncateBody = truncateBody;
        this.selectAuthors = selectAuthors;
        this.startAuthor = startAuthor;
        this.startPermlink = startPermlink;
        this.parentAuthor = parentAuthor;
        this.parentPermlink = parentPermlink;
    }

    public void setSelectedAuthor(@Nonnull AccountName author) {
        setSelectAuthors(Collections.singletonList(author));
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public List<String> getSelectTags() {
        return selectTags;
    }

    public void setSelectTags(List<String> selectTags) {
        this.selectTags = selectTags;
    }

    public List<String> getFilterTags() {
        return filterTags;
    }

    public void setFilterTags(List<String> filterTags) {
        this.filterTags = filterTags;
    }

    public List<String> getSelectLanguages() {
        return selectLanguages;
    }

    public void setSelectLanguages(List<String> selectLanguages) {
        this.selectLanguages = selectLanguages;
    }

    public List<String> getFilterLanguages() {
        return filterLanguages;
    }

    public void setFilterLanguages(List<String> filterLanguages) {
        this.filterLanguages = filterLanguages;
    }

    public Integer getVoteLimit() {
        return voteLimit;
    }

    public void setVoteLimit(Integer voteLimit) {
        this.voteLimit = voteLimit;
    }

    public Integer getTruncateBody() {
        return truncateBody;
    }

    public void setTruncateBody(Integer truncateBody) {
        this.truncateBody = truncateBody;
    }

    public List<AccountName> getSelectAuthors() {
        return selectAuthors;
    }

    public void setSelectAuthors(List<AccountName> selectAuthors) {
        this.selectAuthors = selectAuthors;
    }

    public AccountName getStartAuthor() {
        return startAuthor;
    }

    public void setStartAuthor(AccountName startAuthor) {
        this.startAuthor = startAuthor;
    }

    public Permlink getStartPermlink() {
        return startPermlink;
    }

    public void setStartPermlink(Permlink startPermlink) {
        this.startPermlink = startPermlink;
    }

    public AccountName getParentAuthor() {
        return parentAuthor;
    }

    public void setParentAuthor(AccountName parentAuthor) {
        this.parentAuthor = parentAuthor;
    }

    public Permlink getParentPermlink() {
        return parentPermlink;
    }

    public void setParentPermlink(Permlink parentPermlink) {
        this.parentPermlink = parentPermlink;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DiscussionQuery that = (DiscussionQuery) o;

        if (limit != null ? !limit.equals(that.limit) : that.limit != null) return false;
        if (selectTags != null ? !selectTags.equals(that.selectTags) : that.selectTags != null) return false;
        if (filterTags != null ? !filterTags.equals(that.filterTags) : that.filterTags != null) return false;
        if (selectLanguages != null ? !selectLanguages.equals(that.selectLanguages) : that.selectLanguages != null)
            return false;
        if (filterLanguages != null ? !filterLanguages.equals(that.filterLanguages) : that.filterLanguages != null)
            return false;
        if (voteLimit != null ? !voteLimit.equals(that.voteLimit) : that.voteLimit != null) return false;
        if (truncateBody != null ? !truncateBody.equals(that.truncateBody) : that.truncateBody != null) return false;
        if (selectAuthors != null ? !selectAuthors.equals(that.selectAuthors) : that.selectAuthors != null)
            return false;
        if (startAuthor != null ? !startAuthor.equals(that.startAuthor) : that.startAuthor != null) return false;
        if (startPermlink != null ? !startPermlink.equals(that.startPermlink) : that.startPermlink != null)
            return false;
        if (parentAuthor != null ? !parentAuthor.equals(that.parentAuthor) : that.parentAuthor != null) return false;
        return parentPermlink != null ? parentPermlink.equals(that.parentPermlink) : that.parentPermlink == null;
    }

    @Override
    public int hashCode() {
        int result = limit != null ? limit.hashCode() : 0;
        result = 31 * result + (selectTags != null ? selectTags.hashCode() : 0);
        result = 31 * result + (filterTags != null ? filterTags.hashCode() : 0);
        result = 31 * result + (selectLanguages != null ? selectLanguages.hashCode() : 0);
        result = 31 * result + (filterLanguages != null ? filterLanguages.hashCode() : 0);
        result = 31 * result + (voteLimit != null ? voteLimit.hashCode() : 0);
        result = 31 * result + (truncateBody != null ? truncateBody.hashCode() : 0);
        result = 31 * result + (selectAuthors != null ? selectAuthors.hashCode() : 0);
        result = 31 * result + (startAuthor != null ? startAuthor.hashCode() : 0);
        result = 31 * result + (startPermlink != null ? startPermlink.hashCode() : 0);
        result = 31 * result + (parentAuthor != null ? parentAuthor.hashCode() : 0);
        result = 31 * result + (parentPermlink != null ? parentPermlink.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "DiscussionQuery{" +
                "limit=" + limit +
                ", selectTags=" + selectTags +
                ", filterTags=" + filterTags +
                ", selectLanguages=" + selectLanguages +
                ", filterLanguages=" + filterLanguages +
                ", voteLimit=" + voteLimit +
                ", truncateBody=" + truncateBody +
                ", selectAuthors=" + selectAuthors +
                ", startAuthor=" + startAuthor +
                ", startPermlink=" + startPermlink +
                ", parentAuthor=" + parentAuthor +
                ", parentPermlink=" + parentPermlink +
                '}';
    }

    public static Builder newBuilder() {
        return new Builder();
    }


    public static class Builder {
        private DiscussionQuery query;

        public Builder() {
            query = new DiscussionQuery();
        }

        public Builder setSelectedAuthor(@Nonnull AccountName author) {
            setSelectAuthors(Collections.singletonList(author));
            return this;
        }


        public Builder setLimit(Integer limit) {
            this.query.limit = limit;
            return this;
        }


        public Builder setSelectTags(List<String> selectTags) {
            this.query.selectTags = selectTags;
            return this;
        }


        public Builder setFilterTags(List<String> filterTags) {
            this.query.filterTags = filterTags;
            return this;

        }


        public Builder setSelectLanguages(List<String> selectLanguages) {
            this.query.selectLanguages = selectLanguages;
            return this;
        }


        public Builder setFilterLanguages(List<String> filterLanguages) {
            this.query.filterLanguages = filterLanguages;
            return this;
        }


        public Builder setVoteLimit(Integer voteLimit) {
            this.query.voteLimit = voteLimit;
            return this;
        }


        public Builder setTruncateBody(Integer truncateBody) {
            this.query.truncateBody = truncateBody;
            return this;
        }


        public Builder setSelectAuthors(List<AccountName> selectAuthors) {
            this.query.selectAuthors = selectAuthors;
            return this;
        }


        public Builder setStartAuthor(AccountName startAuthor) {
            this.query.startAuthor = startAuthor;
            return this;
        }


        public Builder setStartPermlink(Permlink startPermlink) {
            this.query.startPermlink = startPermlink;
            return this;
        }


        public Builder setParentAuthor(AccountName parentAuthor) {
            this.query.parentAuthor = parentAuthor;
            return this;
        }


        public Builder setParentPermlink(Permlink parentPermlink) {
            this.query.parentPermlink = parentPermlink;
            return this;
        }

        public DiscussionQuery build() {
            if (query == null) query = new DiscussionQuery();
            return query;
        }
    }
}
