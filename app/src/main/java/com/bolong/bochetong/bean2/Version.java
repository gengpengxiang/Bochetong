package com.bolong.bochetong.bean2;


public class Version {

    /**
     * id : CE5690115FCD4DE7BA24B83D1314249F
     * versionNumber : 2.0.3
     * releaseTime : 1500604652000
     * downloadAddress : http://101.201.145.238/group1/M01/01/AF/ZcmR7llxaOyAMtifAmhXdhlk6MA399.apk
     * updateContent : 1.界面优化
     2.新增分享功能
     3.关于我们加入商务合作电话，车场详情界面修改
     4.修复了一些Bug
     */

    private String id;
    private String versionNumber;
    private long releaseTime;
    private String downloadAddress;
    private String updateContent;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(String versionNumber) {
        this.versionNumber = versionNumber;
    }

    public long getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(long releaseTime) {
        this.releaseTime = releaseTime;
    }

    public String getDownloadAddress() {
        return downloadAddress;
    }

    public void setDownloadAddress(String downloadAddress) {
        this.downloadAddress = downloadAddress;
    }

    public String getUpdateContent() {
        return updateContent;
    }

    public void setUpdateContent(String updateContent) {
        this.updateContent = updateContent;
    }
}
