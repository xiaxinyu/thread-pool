package com.xiaxinyu.thread.pool.core;

import org.springframework.core.Ordered;

/**
 * 会话执行器处理器
 * <ul>
 *     <li>在任务线程执行用户任务前处理</li>
 *     <li>在任务线程执行用户任务后处理</li>
 * </ul>
 * @author LiuYang
 * @date 2020/7/6
 */
public interface SessionExecutorProcessor extends Ordered {
    /**
     * 默认排序值
     */
    int DEFAULT_ORDER = 100;

    /**
     * 执行前处理
     * <p>示例：执行前处理一些设置当前用户会话到ThreadLocal操作</p>
     */
    void preHandle();

    /**
     * 线程执行之后处理的一些事务
     *  <p>示例：执行后清除当前会话操作</p>
     */
    void afterCompletion();
}
