package com.mogu.entity.wfuser;

import com.google.gson.annotations.Expose;

public class Pageinfo {

    @Expose
    private Integer defaultPageSize;
    @Expose
    private Integer nextPage;
    @Expose
    private Integer pageIndex;
    @Expose
    private Integer pageSize;
    @Expose
    private Integer prePage;
    @Expose
    private Integer totalPage;
    @Expose
    private Integer totalRec;

    /**
     * 
     * @return
     *     The defaultPageSize
     */
    public Integer getDefaultPageSize() {
        return defaultPageSize;
    }

    /**
     * 
     * @param defaultPageSize
     *     The defaultPageSize
     */
    public void setDefaultPageSize(Integer defaultPageSize) {
        this.defaultPageSize = defaultPageSize;
    }

    /**
     * 
     * @return
     *     The nextPage
     */
    public Integer getNextPage() {
        return nextPage;
    }

    /**
     * 
     * @param nextPage
     *     The nextPage
     */
    public void setNextPage(Integer nextPage) {
        this.nextPage = nextPage;
    }

    /**
     * 
     * @return
     *     The pageIndex
     */
    public Integer getPageIndex() {
        return pageIndex;
    }

    /**
     * 
     * @param pageIndex
     *     The pageIndex
     */
    public void setPageIndex(Integer pageIndex) {
        this.pageIndex = pageIndex;
    }

    /**
     * 
     * @return
     *     The pageSize
     */
    public Integer getPageSize() {
        return pageSize;
    }

    /**
     * 
     * @param pageSize
     *     The pageSize
     */
    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * 
     * @return
     *     The prePage
     */
    public Integer getPrePage() {
        return prePage;
    }

    /**
     * 
     * @param prePage
     *     The prePage
     */
    public void setPrePage(Integer prePage) {
        this.prePage = prePage;
    }

    /**
     * 
     * @return
     *     The totalPage
     */
    public Integer getTotalPage() {
        return totalPage;
    }

    /**
     * 
     * @param totalPage
     *     The totalPage
     */
    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }

    /**
     * 
     * @return
     *     The totalRec
     */
    public Integer getTotalRec() {
        return totalRec;
    }

    /**
     * 
     * @param totalRec
     *     The totalRec
     */
    public void setTotalRec(Integer totalRec) {
        this.totalRec = totalRec;
    }

}
