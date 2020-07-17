package com.xiaxinyu.thread.pool.user.user;


import com.xiaxinyu.thread.pool.core.impl.AbstractDataSessionExecutorProcessor;
import com.xiaxinyu.thread.pool.entity.CustomUserDetails;
import com.xiaxinyu.thread.pool.user.UserDetailSessionUserDTO;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.util.StringUtils;

import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.HashMap;
import java.util.Optional;

/**
 *
 * @author LiuYang
 * @date 2020/7/8
 */
@Slf4j
public class UserDetailSessionExecutorProcessor extends AbstractDataSessionExecutorProcessor<CustomUserDetails> {
    private final Logger log = LoggerFactory.getLogger(UserDetailSessionExecutorProcessor.class);

    public UserDetailSessionExecutorProcessor(UserDetailSessionUserDTO data) {
        this(data, DEFAULT_ORDER);
    }

    public UserDetailSessionExecutorProcessor(UserDetailSessionUserDTO data, int order) {
        super(convert(data), order);
    }

    @Override
    public void preHandle() {
        log.info("current thread SecurityContext is assigned as CustomUserDetails({})", super.getData().getUsername());
        //将用户信息设置到SecurityContextHolder.getContext()中
        Authentication user = new UsernamePasswordAuthenticationToken("default", "N/A", Collections.emptyList());
        OAuth2Request request = new OAuth2Request(new HashMap<>(0), "", Collections.emptyList(), true,
                Collections.emptySet(), Collections.emptySet(), null, null, null);
        OAuth2Authentication authentication = new OAuth2Authentication(request, user);
        OAuth2AuthenticationDetails oAuth2AuthenticationDetails = new OAuth2AuthenticationDetails(new MockHttpServletRequest());
        oAuth2AuthenticationDetails.setDecodedDetails(super.getData());
        authentication.setDetails(oAuth2AuthenticationDetails);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Override
    public void afterCompletion() {
        //执行完成后清除
        SecurityContextHolder.clearContext();
    }

    /**
     * 转换成系统中的上下文用户信息
     * @param detailSessionUserDTO 用户会话信息
     * @return 用户信息
     */
    @NotNull
    public static CustomUserDetails convert(@NotNull UserDetailSessionUserDTO detailSessionUserDTO) {
        CustomUserDetails customUserDetails = new CustomUserDetails(detailSessionUserDTO.getLoginName(), "unknown");
        customUserDetails.setUserId(detailSessionUserDTO.getId());
        customUserDetails.setEmail(detailSessionUserDTO.getEmail());
        customUserDetails.setAdmin(detailSessionUserDTO.getAdmin());

        customUserDetails.setOrganizationId(0L);
        customUserDetails.setLanguage("zh_CN");
        customUserDetails.setTimeZone("CCT");
        Optional.ofNullable(detailSessionUserDTO.getLanguage()).filter(StringUtils::hasText).ifPresent(customUserDetails::setLanguage);
        Optional.ofNullable(detailSessionUserDTO.getTimeZone()).filter(StringUtils::hasText).ifPresent(customUserDetails::setTimeZone);
        return customUserDetails;
    }
}
