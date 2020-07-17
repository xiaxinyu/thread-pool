package com.xiaxinyu.thread.pool.user.user;


import com.xiaxinyu.thread.pool.core.SessionExecutorProcessor;
import com.xiaxinyu.thread.pool.core.impl.SessionExecutorServiceImpl;
import com.xiaxinyu.thread.pool.user.UserDetailSessionUserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.concurrent.DelegatingSecurityContextExecutorService;

import javax.validation.constraints.NotNull;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.function.Function;

/**
 *
 * @author LiuYang
 * @date 2020/7/7
 */
//@Slf4j
public class UserDetailSessionExecutorServiceImpl extends SessionExecutorServiceImpl implements UserDetailSessionExecutorService {

    private final Logger log = LoggerFactory.getLogger(UserDetailSessionExecutorServiceImpl.class);

    /**
     * 原始执行类
     */
    private @NotNull ExecutorService delegate;

    public UserDetailSessionExecutorServiceImpl(@NotNull ExecutorService delegate) {
        this(delegate, DelegatingSecurityContextExecutorService::new);
    }

    private UserDetailSessionExecutorServiceImpl(@NotNull ExecutorService delegate, @NotNull Function<ExecutorService, ExecutorService> wrapper) {
        super(wrapper.apply(delegate));
        this.delegate = delegate;
    }

    @Override
    public @NotNull UserDetailSessionExecutorService as(@NotNull UserDetailSessionUserDTO detailSessionUserDTO) {
        /*
         * 设置用户上下文信息拦截器
         */
        SessionExecutorProcessor interceptor = new UserDetailSessionExecutorProcessor(detailSessionUserDTO);
        return new UserDetailSessionExecutorServiceImpl(this.delegate
                , delegate -> new SessionExecutorServiceImpl(delegate, interceptor));
    }

    @Override
    public <T> Future<T> submit(@NotNull Callable<T> task, @NotNull UserDetailSessionUserDTO detailSessionUserDTO) {
        return this.as(detailSessionUserDTO).submit(task);
    }
}
