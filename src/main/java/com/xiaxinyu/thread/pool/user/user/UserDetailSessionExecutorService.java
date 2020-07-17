package com.xiaxinyu.thread.pool.user.user;

import com.xiaxinyu.thread.pool.core.SessionExecutorService;
import com.xiaxinyu.thread.pool.user.UserDetailSessionUserDTO;

import javax.validation.constraints.NotNull;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * 用户信息会话执行器
 * <ul>
 *     <li>默认自动继承父类的上下文用户信息</li>
 *     <li>可指定用户注入到上下文中执行指定信息</li>
 * </ul>
 * @author LiuYang
 * @date 2020/7/7
 */
public interface UserDetailSessionExecutorService extends SessionExecutorService {
    /**
     * 获取指定用户的执行线程
     * <ul>
     *     <li>
     *         warn: 委托的执行器依旧是原对象{@link UserDetailSessionExecutorServiceImpl(ExecutorService)}
     *         <br/>
     *         请注意适用，避免出现死锁
     *     </li>
     *     <li>上下文信息设置异常，会报出错误</li>
     *     <li>实现参考：com.crc.crcloud.starter.asgard.common.AbstractAsgardConsumer#beforeInvoke(CustomUserDetails)</li>
     * </ul>
     * @param detailSessionUserDTO 上下文用户信息
     * @return 会话执行服务
     */
    @NotNull
    UserDetailSessionExecutorService as(@NotNull UserDetailSessionUserDTO detailSessionUserDTO);

    /**
     * 指定用户执行任务
     * <p>task中用户上下文信息被设置为detailSessionUserDTO</p>
     * @param task 任务
     * @param detailSessionUserDTO 用户信息
     * @param <T> 返回值信息
     * @return Future
     */
    <T> Future<T> submit(@NotNull Callable<T> task, @NotNull UserDetailSessionUserDTO detailSessionUserDTO);
}
