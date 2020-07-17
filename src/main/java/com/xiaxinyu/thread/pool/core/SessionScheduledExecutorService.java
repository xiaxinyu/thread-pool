package com.xiaxinyu.thread.pool.core;

import java.util.concurrent.ScheduledExecutorService;

/**
 * 会话传递线程池
 * <p>带调度</p>
 * @author LiuYang
 * @date 2020/7/6
 */
public interface SessionScheduledExecutorService extends ScheduledExecutorService, SessionExecutorService {

}
