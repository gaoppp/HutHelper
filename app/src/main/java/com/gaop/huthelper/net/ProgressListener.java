package com.gaop.huthelper.net;

/**
 * Created by 高沛 on 2016/9/1.
 *
 */
public interface ProgressListener {
    /**
     * @param progress     已经下载或上传字节数
     * @param total        总字节数
     * @param done         是否完成
     */
    void onProgress(long progress, long total, boolean done);
}