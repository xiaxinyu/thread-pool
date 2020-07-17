# 多线程会话传递

## 背景

在主线程存放在ThreadLocal中的信息，不能传递到子线程中，导致业务逻辑异常。

### 写在前面的话

整个代码都是比较抽象的，理解可能稍有难度，但是自定义度高，能基本能覆盖全部场景。

## 要求

1. 能传递线程中的一些会话信息
2. 在不同场景，可能有不同的会话信息需要传递，甚至有些数据会话不是从父线程中获取

## 设计

![session-thread](https://gitee.com/xiaobendanLY/service-pool-api/raw/master/img/session-thread.svg)



对提交的`Runnable`进行包装，包装成带有前置处理器与后置处理器的任务，在任务线程执行用户提交的任务处理前与处理后执行某些事情处理。

> 1. 装饰器模式，对`Runnable`装饰
> 2. 进一步细化装饰过程，进行任务提供前置处理和后置处理



## 实现

### 表结构

![image-20200708104223988](https://gitee.com/xiaobendanLY/service-pool-api/raw/master/img/image-20200708104223988.png)

![SessionExecutorServiceImpl](https://gitee.com/xiaobendanLY/service-pool-api/raw/master/img/SessionExecutorServiceImpl.png)



### `WrappingExecutorService`

对线程池进行任务包装的抽象类



### `SessionExecutorServiceImpl`

实现包装抽象类，实现包装方法，提供执行处理器接口配置

#### `wrapTask`

```java
/**
 * 会话传递执行线程实现类
 * @author LiuYang
 * @date 2020/7/6
 */
public class SessionExecutorServiceImpl extends WrappingExecutorService implements SessionExecutorService {
    @NotNull
    private final List<SessionExecutorProcessor> sessionExecutorProcessors = new ArrayList<>();
    
    @Override
    protected <T> Callable<T> wrapTask(Callable<T> callable) {
        return () -> {
            try {
                getSessionExecutorProcessors().forEach(SessionExecutorProcessor::preHandle);
                return callable.call();
            } finally {
                getSessionExecutorProcessors().forEach(SessionExecutorProcessor::afterCompletion);
            }
        };
    }
 }
```



### 覆盖范围

- SessionExecutorService
- SessionScheduledExecutorService

## 使用

> 目前提供了`线程执行服务`与`线程调度执行服务`两者使用一样，所以下面只演示`线程执行服务`

### 示例

```java
private static final ThreadLocal<String> threadLocal = new ThreadLocal<>();

public static void main(String[] args) {
	ThreadLocal<String> threadLocal = new ThreadLocal<>();
	threadLocal.set("会话传递成功");
	//基本使用
	SessionExecutorProcessor dataSessionExecutorProcessor = new AbstractDataSessionExecutorProcessor<String>(threadLocal.get()) {

		@Override
		public void preHandle() {
			threadLocal.set("数据方式传递: " + super.getData());
			System.out.println(threadLocal.get());
		}

		@Override
		public void afterCompletion() {
			threadLocal.remove();
		}
	};
	final String threadValue = threadLocal.get();
	SessionExecutorProcessor lambdaSessionExecutorProcessor = new LambdaSessionExecutorProcessor(() -> {
		threadLocal.set("lambda方式传递: " + threadValue);
		System.out.println(threadLocal.get());
	}, threadLocal::remove);

	SessionExecutorService sessionExecutorService = new SessionExecutorServiceImpl(ThreadUtil.newExecutor(3), dataSessionExecutorProcessor, lambdaSessionExecutorProcessor);
	sessionExecutorService.execute(() -> {
		System.out.println("当前线程会话值：" + threadLocal.get());
	});
}
```

**执行结果**

```verilog
数据方式传递: 会话传递成功
lambda方式传递: 会话传递成功
当前线程会话值：lambda方式传递: 会话传递成功
```





> 会话执行处理器`SessionExecutorProcessor`已经提供两种处理方式，基本上已经够用了
>
> - `AbstractDataSessionExecutorProcessor`-抽象数据拦截器
> - `LambdaSessionExecutorProcessor`-表达式拦截器



