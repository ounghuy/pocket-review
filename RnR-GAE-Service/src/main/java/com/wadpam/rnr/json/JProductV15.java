package com.wadpam.rnr.json;

/**
 *
 * @author os
 */
public class JProductV15 extends JBaseObject {

    /** The Many-To-One id (unconstrained) */
    private String      id;

    /** The location of the product */
    private JLocation   location;

    /** The total sum or all ratings */
    private Long        ratingSum = 0L;

    /** The total number of ratings */
    private Long        ratingCount = 0L;

    /** The deep link to the individual ratings */
    private String      ratingsURL;

    /** The total number of Likes */
    private Long        likeCount = 0L;

    /** The deep link to the individual likes */
    private String      likesURL;

    /** The total number of Comments */
    private Long        commentCount = 0L;

    /** The deep link to the individual likes */
    private String      commentsURL;


    @Override
    protected String subString() {
        return String.format("id:%s, location:%s, ratings:%d, average:%d, likes:%d, comments:%d",
                id, location, ratingCount, getRatingAverage(), likeCount, commentCount);
    }

    // Setters and Getters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public JLocation getLocation() {
        return location;
    }

    public void setLocation(JLocation location) {
        this.location = location;
    }

    public int getRatingAverage() {
        return 0 < ratingCount ? (int) (ratingSum / ratingCount) : 0;
    }

    public Long getRatingCount() {
        return ratingCount;
    }

    public void setRatingCount(Long ratingCount) {
        this.ratingCount = ratingCount;
    }

    public Long getRatingSum() {
        return ratingSum;
    }

    public void setRatingSum(Long ratingSum) {
        this.ratingSum = ratingSum;
    }

    public String getRatingsURL() {
        return ratingsURL;
    }

    public void setRatingsURL(String ratingsURL) {
        this.ratingsURL = ratingsURL;
    }

    public Long getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Long likeCount) {
        this.likeCount = likeCount;
    }

    public String getLikesURL() {
        return likesURL;
    }

    public void setLikesURL(String likesURL) {
        this.likesURL = likesURL;
    }

    public Long getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Long commentCount) {
        this.commentCount = commentCount;
    }

    public String getCommentsURL() {
        return commentsURL;
    }

    public void setCommentsURL(String commentsURL) {
        this.commentsURL = commentsURL;
    }
}
